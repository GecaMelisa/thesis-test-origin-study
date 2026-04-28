"""
Claude-generated unit tests for requests.structures

Scope: CaseInsensitiveDict (all core methods), LookupDict
"""

import pytest

from requests.structures import CaseInsensitiveDict, LookupDict


# ---------------------------------------------------------------------------
# CaseInsensitiveDict
# ---------------------------------------------------------------------------


class TestCaseInsensitiveDictInit:
    def test_empty_init(self):
        d = CaseInsensitiveDict()
        assert len(d) == 0
        assert list(d) == []

    def test_init_from_dict(self):
        d = CaseInsensitiveDict({"Content-Type": "application/json"})
        assert d["content-type"] == "application/json"
        assert d["CONTENT-TYPE"] == "application/json"

    def test_init_from_list_of_tuples(self):
        d = CaseInsensitiveDict([("Accept", "text/html"), ("X-Token", "abc")])
        assert d["accept"] == "text/html"
        assert d["x-token"] == "abc"

    def test_init_with_kwargs(self):
        d = CaseInsensitiveDict(Accept="text/html")
        assert d["accept"] == "text/html"

    def test_init_none_is_empty(self):
        d = CaseInsensitiveDict(None)
        assert len(d) == 0


class TestCaseInsensitiveDictAccess:
    def test_getitem_case_insensitive(self):
        d = CaseInsensitiveDict({"Accept": "application/json"})
        assert d["accept"] == "application/json"
        assert d["ACCEPT"] == "application/json"
        assert d["Accept"] == "application/json"
        assert d["aCcEpT"] == "application/json"

    def test_getitem_missing_raises_key_error(self):
        d = CaseInsensitiveDict()
        with pytest.raises(KeyError):
            _ = d["missing"]

    def test_setitem_preserves_original_case(self):
        d = CaseInsensitiveDict()
        d["Content-Type"] = "text/plain"
        assert list(d.keys()) == ["Content-Type"]

    def test_setitem_overwrite_updates_value_and_case(self):
        d = CaseInsensitiveDict()
        d["accept"] = "text/html"
        d["ACCEPT"] = "application/json"
        assert d["accept"] == "application/json"
        assert list(d.keys()) == ["ACCEPT"]

    def test_contains_case_insensitive(self):
        d = CaseInsensitiveDict({"Authorization": "Bearer token"})
        assert "authorization" in d
        assert "AUTHORIZATION" in d
        assert "Authorization" in d
        assert "missing" not in d

    def test_delitem_case_insensitive(self):
        d = CaseInsensitiveDict({"Accept": "text/html"})
        del d["ACCEPT"]
        assert "accept" not in d
        assert len(d) == 0

    def test_delitem_missing_raises_key_error(self):
        d = CaseInsensitiveDict()
        with pytest.raises(KeyError):
            del d["missing"]


class TestCaseInsensitiveDictIteration:
    def test_iter_returns_original_case_keys(self):
        d = CaseInsensitiveDict({"Content-Type": "text/html", "Accept": "*/*"})
        keys = list(d)
        assert "Content-Type" in keys
        assert "Accept" in keys

    def test_len(self):
        d = CaseInsensitiveDict({"a": "1", "b": "2", "c": "3"})
        assert len(d) == 3

    def test_len_after_overwrite(self):
        d = CaseInsensitiveDict({"Accept": "text/html"})
        d["ACCEPT"] = "application/json"
        assert len(d) == 1

    def test_lower_items(self):
        d = CaseInsensitiveDict({"Content-Type": "text/html", "Accept": "*/*"})
        lower = dict(d.lower_items())
        assert lower["content-type"] == "text/html"
        assert lower["accept"] == "*/*"

    def test_items_yields_original_case_keys(self):
        d = CaseInsensitiveDict({"X-Custom-Header": "value"})
        items = dict(d.items())
        assert "X-Custom-Header" in items


class TestCaseInsensitiveDictEquality:
    def test_equal_to_plain_dict_case_insensitive(self):
        d = CaseInsensitiveDict({"Accept": "application/json"})
        assert d == {"accept": "application/json"}
        assert d == {"ACCEPT": "application/json"}
        assert d == {"Accept": "application/json"}

    def test_not_equal_different_value(self):
        d = CaseInsensitiveDict({"Accept": "application/json"})
        assert d != {"Accept": "text/html"}

    def test_not_equal_empty(self):
        d = CaseInsensitiveDict({"Accept": "application/json"})
        assert d != {}

    def test_not_equal_non_mapping(self):
        d = CaseInsensitiveDict({"Accept": "application/json"})
        assert (d == "string") is NotImplemented or d != "string"

    def test_two_case_insensitive_dicts_equal(self):
        d1 = CaseInsensitiveDict({"Content-Type": "text/html"})
        d2 = CaseInsensitiveDict({"content-type": "text/html"})
        assert d1 == d2


class TestCaseInsensitiveDictCopyAndRepr:
    def test_copy_is_independent(self):
        d = CaseInsensitiveDict({"Accept": "text/html"})
        c = d.copy()
        c["Accept"] = "application/json"
        assert d["Accept"] == "text/html"
        assert c["Accept"] == "application/json"

    def test_copy_has_same_content(self):
        d = CaseInsensitiveDict({"Accept": "text/html", "X-Token": "abc"})
        c = d.copy()
        assert c == d

    def test_copy_is_not_same_object(self):
        d = CaseInsensitiveDict({"Accept": "text/html"})
        assert d.copy() is not d

    def test_repr_shows_dict_format(self):
        d = CaseInsensitiveDict({"Accept": "application/json"})
        r = repr(d)
        assert "Accept" in r
        assert "application/json" in r

    def test_repr_is_string(self):
        d = CaseInsensitiveDict()
        assert isinstance(repr(d), str)


class TestCaseInsensitiveDictUpdate:
    def test_update_from_dict(self):
        d = CaseInsensitiveDict({"Accept": "text/html"})
        d.update({"Content-Type": "application/json"})
        assert d["content-type"] == "application/json"
        assert d["accept"] == "text/html"

    def test_update_overwrites_case_insensitive(self):
        d = CaseInsensitiveDict({"Accept": "text/html"})
        d.update({"ACCEPT": "application/json"})
        assert d["accept"] == "application/json"
        assert len(d) == 1

    def test_get_with_default(self):
        d = CaseInsensitiveDict({"Accept": "text/html"})
        assert d.get("accept") == "text/html"
        assert d.get("missing") is None
        assert d.get("missing", "default") == "default"


# ---------------------------------------------------------------------------
# LookupDict
# ---------------------------------------------------------------------------


class TestLookupDict:
    def test_repr(self):
        ld = LookupDict("status_codes")
        assert repr(ld) == "<lookup 'status_codes'>"

    def test_repr_with_none_name(self):
        ld = LookupDict()
        assert repr(ld) == "<lookup 'None'>"

    def test_getitem_returns_none_for_missing(self):
        ld = LookupDict("test")
        assert ld["nonexistent"] is None

    def test_getitem_returns_set_attr(self):
        ld = LookupDict("test")
        ld.ok = 200
        assert ld["ok"] == 200

    def test_get_returns_none_for_missing(self):
        ld = LookupDict("test")
        assert ld.get("nonexistent") is None

    def test_get_returns_default_for_missing(self):
        ld = LookupDict("test")
        assert ld.get("nonexistent", 404) == 404

    def test_get_returns_value_for_existing(self):
        ld = LookupDict("test")
        ld.bad_gateway = 502
        assert ld.get("bad_gateway") == 502

    def test_multiple_attributes(self):
        ld = LookupDict("http_status")
        ld.ok = 200
        ld.not_found = 404
        ld.server_error = 500
        assert ld["ok"] == 200
        assert ld["not_found"] == 404
        assert ld["server_error"] == 500

    def test_attribute_vs_key_access(self):
        ld = LookupDict("test")
        ld.created = 201
        assert ld["created"] == ld.created == 201
