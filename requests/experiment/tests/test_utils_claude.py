"""
Claude-generated unit tests for requests.utils

Scope: parse_header_links, to_key_val_list, requote_uri,
       get_auth_from_url, is_valid_cidr
"""

import pytest

from requests.utils import (
    get_auth_from_url,
    is_valid_cidr,
    parse_header_links,
    requote_uri,
    to_key_val_list,
)


# ---------------------------------------------------------------------------
# parse_header_links
# ---------------------------------------------------------------------------


class TestParseHeaderLinks:
    def test_single_link_with_rel(self):
        result = parse_header_links('<http://example.com/page2>; rel="next"')
        assert len(result) == 1
        assert result[0]["url"] == "http://example.com/page2"
        assert result[0]["rel"] == "next"

    def test_single_link_no_params(self):
        result = parse_header_links("<http://example.com/>")
        assert len(result) == 1
        assert result[0]["url"] == "http://example.com/"
        assert "rel" not in result[0]

    def test_multiple_links(self):
        header = (
            '<http://example.com/page2>; rel="next", '
            '<http://example.com/page1>; rel="prev"'
        )
        result = parse_header_links(header)
        assert len(result) == 2
        assert result[0]["url"] == "http://example.com/page2"
        assert result[0]["rel"] == "next"
        assert result[1]["url"] == "http://example.com/page1"
        assert result[1]["rel"] == "prev"

    def test_link_with_type_param(self):
        header = '<http://example.com/img.jpeg>; rel=front; type="image/jpeg"'
        result = parse_header_links(header)
        assert len(result) == 1
        link = result[0]
        assert link["url"] == "http://example.com/img.jpeg"
        assert link["rel"] == "front"
        assert link["type"] == "image/jpeg"

    def test_empty_string(self):
        assert parse_header_links("") == []

    def test_whitespace_only(self):
        assert parse_header_links("   ") == []

    def test_link_trailing_semicolon(self):
        result = parse_header_links("<http://example.com/>;")
        assert len(result) == 1
        assert result[0]["url"] == "http://example.com/"

    def test_link_url_stripped_of_angle_brackets(self):
        result = parse_header_links("<http://a.com/path>")
        assert result[0]["url"] == "http://a.com/path"
        assert "<" not in result[0]["url"]
        assert ">" not in result[0]["url"]

    def test_multiple_links_compact_spacing(self):
        header = "<http://a.com/1>; rel=first,<http://a.com/2>; rel=last"
        result = parse_header_links(header)
        assert len(result) == 2
        assert result[0]["rel"] == "first"
        assert result[1]["rel"] == "last"

    def test_return_type_is_list_of_dicts(self):
        result = parse_header_links("<http://example.com/>; rel=self")
        assert isinstance(result, list)
        assert isinstance(result[0], dict)


# ---------------------------------------------------------------------------
# to_key_val_list
# ---------------------------------------------------------------------------


class TestToKeyValList:
    def test_none_returns_none(self):
        assert to_key_val_list(None) is None

    def test_list_of_tuples_unchanged(self):
        data = [("key", "val"), ("foo", "bar")]
        result = to_key_val_list(data)
        assert result == [("key", "val"), ("foo", "bar")]
        assert isinstance(result, list)

    def test_dict_becomes_list(self):
        result = to_key_val_list({"a": "1"})
        assert isinstance(result, list)
        assert ("a", "1") in result

    def test_tuple_of_tuples(self):
        result = to_key_val_list((("x", "1"), ("y", "2")))
        assert ("x", "1") in result
        assert ("y", "2") in result

    def test_string_raises_value_error(self):
        with pytest.raises(ValueError):
            to_key_val_list("invalid")

    def test_bytes_raises_value_error(self):
        with pytest.raises(ValueError):
            to_key_val_list(b"bytes")

    def test_bool_raises_value_error(self):
        with pytest.raises(ValueError):
            to_key_val_list(True)

    def test_int_raises_value_error(self):
        with pytest.raises(ValueError):
            to_key_val_list(42)

    def test_empty_list_returns_empty_list(self):
        result = to_key_val_list([])
        assert result == []
        assert isinstance(result, list)

    def test_ordered_dict_preserves_order(self):
        from collections import OrderedDict

        od = OrderedDict([("first", "1"), ("second", "2"), ("third", "3")])
        result = to_key_val_list(od)
        assert result == [("first", "1"), ("second", "2"), ("third", "3")]


# ---------------------------------------------------------------------------
# requote_uri
# ---------------------------------------------------------------------------


class TestRequoteUri:
    def test_already_quoted_percent_sign(self):
        uri = "http://example.com/fiz?buz=%25ppicture"
        assert requote_uri(uri) == "http://example.com/fiz?buz=%25ppicture"

    def test_unquoted_percent_gets_quoted(self):
        uri = "http://example.com/fiz?buz=%ppicture"
        assert requote_uri(uri) == "http://example.com/fiz?buz=%25ppicture"

    def test_plain_uri_unchanged(self):
        uri = "http://example.com/path"
        assert requote_uri(uri) == "http://example.com/path"

    def test_unreserved_chars_unquoted(self):
        # %7E is '~', an unreserved char — should be unquoted
        uri = "http://example.com/path%7Emore"
        result = requote_uri(uri)
        assert "~" in result

    def test_reserved_chars_stay_encoded(self):
        # %20 is space — not unreserved, should stay encoded
        uri = "http://example.com/path%20end"
        result = requote_uri(uri)
        assert "%20" in result

    def test_path_with_no_encoding(self):
        uri = "http://example.com/simple/path"
        assert requote_uri(uri) == "http://example.com/simple/path"

    def test_query_string_preserved(self):
        uri = "http://example.com/?key=value&other=123"
        result = requote_uri(uri)
        assert "key=value" in result
        assert "other=123" in result

    def test_fragment_preserved(self):
        uri = "http://example.com/path#section"
        result = requote_uri(uri)
        assert "#section" in result

    def test_illegal_percent_sequence_quoted(self):
        # %-- is illegal, should be re-encoded
        uri = "http://example.com/?a=%--"
        result = requote_uri(uri)
        assert result == "http://example.com/?a=%--"


# ---------------------------------------------------------------------------
# get_auth_from_url
# ---------------------------------------------------------------------------


class TestGetAuthFromUrl:
    def test_basic_credentials(self):
        url = "http://user:pass@example.com/"
        assert get_auth_from_url(url) == ("user", "pass")

    def test_no_credentials_returns_empty_strings(self):
        url = "http://example.com/path"
        user, password = get_auth_from_url(url)
        assert user == ""
        assert password == ""

    def test_percent_encoded_credentials(self):
        url = "http://user%40name:p%40ss@example.com/"
        user, password = get_auth_from_url(url)
        assert user == "user@name"
        assert password == "p@ss"

    def test_encoded_special_chars_in_password(self):
        url = "http://user:pass%23word@example.com/"
        _, password = get_auth_from_url(url)
        assert password == "pass#word"

    def test_space_encoded_in_password(self):
        url = "http://user:pass%20word@example.com/"
        _, password = get_auth_from_url(url)
        assert password == "pass word"

    def test_url_with_path_and_query(self):
        url = "http://user:pass@example.com/path?query=yes"
        assert get_auth_from_url(url) == ("user", "pass")

    def test_username_only(self):
        # urlparse returns empty string for password when only username present
        url = "http://user@example.com/"
        user, password = get_auth_from_url(url)
        assert isinstance(user, str)
        assert isinstance(password, str)

    def test_returns_tuple(self):
        result = get_auth_from_url("http://a:b@example.com/")
        assert isinstance(result, tuple)
        assert len(result) == 2

    def test_complex_special_chars(self):
        # user = "%!*'();:@&=+$,/?#[] "
        import requests.compat as compat

        user = "%!*'();:@&=+$,/?#[] "
        encoded_user = compat.quote(user, "")
        password = "%!*'();:@&=+$,/?#[] "
        encoded_password = compat.quote(password, "")
        url = f"http://{encoded_user}:{encoded_password}@example.com/"
        result_user, result_password = get_auth_from_url(url)
        assert result_user == user
        assert result_password == password


# ---------------------------------------------------------------------------
# is_valid_cidr
# ---------------------------------------------------------------------------


class TestIsValidCidr:
    def test_valid_cidr_24(self):
        assert is_valid_cidr("192.168.1.0/24") is True

    def test_valid_cidr_8(self):
        assert is_valid_cidr("10.0.0.0/8") is True

    def test_valid_cidr_32(self):
        assert is_valid_cidr("192.168.1.1/32") is True

    def test_valid_cidr_1(self):
        assert is_valid_cidr("128.0.0.0/1") is True

    def test_invalid_no_slash(self):
        assert is_valid_cidr("192.168.1.0") is False

    def test_invalid_mask_zero(self):
        assert is_valid_cidr("192.168.1.0/0") is False

    def test_invalid_mask_33(self):
        assert is_valid_cidr("192.168.1.0/33") is False

    def test_invalid_mask_non_numeric(self):
        assert is_valid_cidr("192.168.1.0/abc") is False

    def test_invalid_ip_out_of_range(self):
        assert is_valid_cidr("192.168.1.999/24") is False

    def test_invalid_negative_mask(self):
        assert is_valid_cidr("192.168.1.0/-1") is False

    def test_invalid_multiple_slashes(self):
        assert is_valid_cidr("192.168.1.0/24/8") is False

    def test_invalid_empty_string(self):
        assert is_valid_cidr("") is False

    def test_invalid_hostname_not_ip(self):
        assert is_valid_cidr("example.com/24") is False
