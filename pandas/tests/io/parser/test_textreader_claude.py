import csv
import io
from io import StringIO

import numpy as np
import pandas as pd
import pytest

pytestmark = pytest.mark.claude


# Claude-generated tests for pandas.read_csv / TextReader


def test_basic_csv_parsing_Claude():
    data = "a,b,c\n1,2,3\n4,5,6\n"
    df = pd.read_csv(StringIO(data))

    assert list(df.columns) == ["a", "b", "c"]
    assert df.shape == (2, 3)
    assert df.iloc[0, 0] == 1
    assert df.iloc[1, 2] == 6


def test_no_header_with_names_Claude():
    data = "1,2\n3,4\n"
    df = pd.read_csv(StringIO(data), header=None, names=["x", "y"])

    assert list(df.columns) == ["x", "y"]
    assert df.shape == (2, 2)
    assert df.iloc[0].tolist() == [1, 2]
    assert df.iloc[1].tolist() == [3, 4]


def test_header_none_default_integer_columns_Claude():
    data = "1,2,3\n4,5,6"
    df = pd.read_csv(StringIO(data), header=None)

    assert list(df.columns) == [0, 1, 2]
    assert df.iloc[0, 2] == 3


def test_index_col_single_Claude():
    data = "id,val\n10,a\n20,b\n"
    df = pd.read_csv(StringIO(data), index_col="id")

    assert list(df.index) == [10, 20]
    assert list(df.columns) == ["val"]
    assert df.loc[10, "val"] == "a"


def test_usecols_subset_Claude():
    data = "a,b,c\n1,2,3\n4,5,6\n"
    df = pd.read_csv(StringIO(data), usecols=["a", "c"])

    assert list(df.columns) == ["a", "c"]
    assert df.shape == (2, 2)
    assert df.iloc[0].tolist() == [1, 3]


def test_dtype_override_Claude():
    data = "a,b\n1,2\n3,4"
    df = pd.read_csv(StringIO(data), dtype={"a": "float64", "b": "string"})

    assert df.dtypes["a"] == np.dtype("float64")
    assert df.dtypes["b"] == pd.StringDtype()
    assert df.iloc[1, 0] == 3.0
    assert df.iloc[0, 1] == "2"


def test_dtype_inference_int_and_float_Claude():
    data = "a,b\n1,2.5\n3,4.0"
    df = pd.read_csv(StringIO(data))

    assert df.dtypes["a"] == np.dtype("int64")
    assert df.dtypes["b"] == np.dtype("float64")
    assert df.iloc[0, 1] == 2.5


def test_converters_Claude():
    data = "a,b\n1,10\n2,20\n"
    df = pd.read_csv(
        StringIO(data),
        converters={"b": lambda x: f"val-{x}"},
    )

    assert df["b"].tolist() == ["val-10", "val-20"]


def test_na_values_custom_Claude():
    data = "col\nNA\nmissing\nvalue\n"
    df = pd.read_csv(StringIO(data), na_values=["missing"])

    assert df["col"].isna().tolist() == [True, True, False]


def test_keep_default_na_false_Claude():
    data = "col\nNA\nmissing\nvalue\n"
    df = pd.read_csv(
        StringIO(data),
        na_values=["missing"],
        keep_default_na=False,
    )

    assert df["col"].isna().tolist() == [False, True, False]
    assert df["col"].iloc[0] == "NA"


def test_na_filter_disabled_Claude():
    data = "val\nNA\nnull"
    df = pd.read_csv(StringIO(data), na_filter=False)

    assert df.shape == (2, 1)
    assert df.iloc[0, 0] == "NA"
    assert df.iloc[1, 0] == "null"


def test_comment_lines_skipped_Claude():
    data = "a,b\n1,2\n# this is a comment\n3,4\n"
    df = pd.read_csv(StringIO(data), comment="#")

    assert df.shape == (2, 2)
    assert df.iloc[0].tolist() == [1, 2]
    assert df.iloc[1].tolist() == [3, 4]


def test_skipinitialspace_Claude():
    data = "a, b\n1, 2\n3, 4"
    df = pd.read_csv(StringIO(data), skipinitialspace=True)

    assert df.iloc[0, 1] == 2
    assert df.iloc[1, 0] == 3


def test_no_skipinitialspace_preserves_space_Claude():
    data = "a, b\n1, 2"
    df = pd.read_csv(StringIO(data), skipinitialspace=False, dtype=str)

    assert " b" in df.columns
    assert df.loc[0, " b"] == " 2"


def test_delim_whitespace_Claude():
    data = "a b c\n1 2 3\n4 5 6\n"
    df = pd.read_csv(StringIO(data), delim_whitespace=True)

    assert list(df.columns) == ["a", "b", "c"]
    assert df.iloc[0].tolist() == [1, 2, 3]
    assert df.iloc[1].tolist() == [4, 5, 6]


def test_tab_delimiter_Claude():
    data = "col1\tcol2\nfoo\tbar\nbaz\tqux"
    df = pd.read_csv(StringIO(data), sep="\t")

    assert df.shape == (2, 2)
    assert df.iloc[1, 0] == "baz"
    assert df.iloc[0, 1] == "bar"


def test_semicolon_delimiter_Claude():
    data = "x;y\n5;7\n9;11"
    df = pd.read_csv(StringIO(data), sep=";")

    assert df.shape == (2, 2)
    assert df.iloc[0, 1] == 7


def test_thousands_and_decimal_Claude():
    data = "value\n1.234,5\n2.345,6\n"
    df = pd.read_csv(StringIO(data), sep=";", thousands=".", decimal=",")

    assert pytest.approx(df["value"].iloc[0], rel=1e-9) == 1234.5
    assert pytest.approx(df["value"].iloc[1], rel=1e-9) == 2345.6


def test_quoted_fields_with_delimiter_inside_Claude():
    data = 'value\n"1,234"\n"5,678"'
    df = pd.read_csv(StringIO(data))

    assert df.shape == (2, 1)
    assert df.iloc[0, 0] == "1,234"
    assert df.iloc[1, 0] == "5,678"


def test_double_quote_escape_Claude():
    data = 'text\n"a""b"\n"c""""d"'
    df = pd.read_csv(StringIO(data))

    assert df.iloc[0, 0] == 'a"b'
    assert df.iloc[1, 0] == 'c""d'


def test_multiline_quoted_field_Claude():
    data = 'id,text\n1,"hello\nworld"\n2,"bye"'
    df = pd.read_csv(StringIO(data))

    assert df.shape == (2, 2)
    assert df.iloc[0, 1] == "hello\nworld"
    assert df.iloc[1, 1] == "bye"


def test_skiprows_Claude():
    data = "skip,me\na,b\n5,6\n7,8"
    df = pd.read_csv(StringIO(data), skiprows=1)

    assert df.shape == (2, 2)
    assert list(df.columns) == ["a", "b"]
    assert df.iloc[1, 1] == 8


def test_skipfooter_python_engine_Claude():
    data = "a\n1\n2\n3\n"
    df = pd.read_csv(StringIO(data), skipfooter=1, engine="python")

    assert df.shape == (2, 1)
    assert df["a"].tolist() == [1, 2]


def test_skipfooter_c_engine_raises_Claude():
    data = "a\n1\n2\n3\n"
    with pytest.raises(ValueError, match="does not support skipfooter"):
        pd.read_csv(StringIO(data), skipfooter=1, engine="c")


def test_on_bad_lines_skip_Claude():
    data = "a,b\n1,2\n3,4,5\n6,7\n"
    df = pd.read_csv(StringIO(data), engine="python", on_bad_lines="skip")

    assert df.shape == (2, 2)
    assert df.iloc[0].tolist() == [1, 2]
    assert df.iloc[1].tolist() == [6, 7]


def test_on_bad_lines_error_raises_Claude():
    data = "a,b\n1,2\n3,4,5\n"
    with pytest.raises(pd.errors.ParserError):
        pd.read_csv(StringIO(data), engine="python", on_bad_lines="error")


def test_on_bad_lines_invalid_value_raises_Claude():
    data = "a,b\n1,2\n"
    with pytest.raises(ValueError, match="invalid for on_bad_lines"):
        pd.read_csv(StringIO(data), on_bad_lines="invalid")


def test_sep_and_delimiter_conflict_raises_Claude():
    data = "a,b\n1,2\n"
    with pytest.raises(ValueError, match="Specified a sep and a delimiter"):
        pd.read_csv(StringIO(data), sep=",", delimiter=";")


def test_delim_whitespace_and_sep_conflict_raises_Claude():
    data = "a b\n1 2\n"
    with pytest.raises(ValueError, match="delim_whitespace=True"):
        pd.read_csv(StringIO(data), sep=" ", delim_whitespace=True)


def test_newline_separator_raises_Claude():
    data = "a\n1\n2\n"
    with pytest.raises(ValueError, match="Specified \\\\n as separator"):
        pd.read_csv(StringIO(data), sep="\n")


def test_unknown_engine_raises_Claude():
    data = "a,b\n1,2\n"
    with pytest.raises(ValueError, match="Unknown engine"):
        pd.read_csv(StringIO(data), engine="unknown")


def test_iterator_returns_textfilereader_Claude():
    data = "a,b\n1,2\n3,4\n"
    reader = pd.read_csv(StringIO(data), iterator=True)

    chunk = next(reader)
    assert chunk.shape == (2, 2)
    assert hasattr(reader, "get_chunk")


def test_chunksize_iteration_Claude():
    data = "a,b\n1,2\n3,4\n5,6\n"
    reader = pd.read_csv(StringIO(data), chunksize=1)

    chunks = list(reader)
    assert len(chunks) == 3
    assert all(c.shape == (1, 2) for c in chunks)
    assert chunks[0].iloc[0].tolist() == [1, 2]
    assert chunks[2].iloc[0].tolist() == [5, 6]


def test_custom_dialect_Claude():
    class PipeDialect(csv.Dialect):
        delimiter = "|"
        quotechar = '"'
        doublequote = True
        skipinitialspace = False
        lineterminator = "\n"
        quoting = csv.QUOTE_MINIMAL

    data = "a|b\n1|2\n3|4\n"
    df = pd.read_csv(StringIO(data), dialect=PipeDialect)

    assert list(df.columns) == ["a", "b"]
    assert df.shape == (2, 2)
    assert df.iloc[1, 1] == 4


def test_boolean_parsing_Claude():
    data = "flag\nTrue\nFalse\ntrue"
    df = pd.read_csv(StringIO(data), dtype={"flag": "boolean"})

    assert df.dtypes["flag"] == pd.BooleanDtype()
    assert bool(df.iloc[0, 0]) is True
    assert bool(df.iloc[1, 0]) is False
    assert bool(df.iloc[2, 0]) is True


def test_engine_c_and_python_consistency_Claude():
    data = "a|b\n1|2\n3|4"
    df_c = pd.read_csv(StringIO(data), sep="|", engine="c")
    df_py = pd.read_csv(StringIO(data), sep="|", engine="python")

    assert df_c.equals(df_py)


def test_file_handle_not_closed_Claude():
    buf = StringIO("a,b\n1,2")
    df = pd.read_csv(buf)

    assert not buf.closed
    assert df.iloc[0, 0] == 1


def test_stringio_position_respected_Claude():
    buf = StringIO("header\nfirst\nsecond")
    _ = buf.readline()
    df = pd.read_csv(buf, header=None)

    assert df.shape == (2, 1)
    assert df.iloc[0, 0] == "first"
    assert df.iloc[1, 0] == "second"


def test_encoding_utf16_le_Claude():
    text = "col\nç".encode("utf-16-le")
    buffer = io.BytesIO(text)
    df = pd.read_csv(buffer, encoding="utf-16-le")

    assert df.shape == (1, 1)
    assert df.iloc[0, 0] == "ç"


def test_encoding_errors_replace_Claude():
    raw = b"x\n\xff\xfe"
    df = pd.read_csv(io.BytesIO(raw), encoding_errors="replace")

    assert df.shape == (1, 1)
    assert "\ufffd" in df.iloc[0, 0]


def test_empty_csv_with_names_Claude():
    with pd.read_csv(
        StringIO(), chunksize=20, header=None, names=["a", "b", "c"]
    ) as reader:
        assert hasattr(reader, "get_chunk")


def test_escapechar_Claude():
    data = '\\"hello world"\n\\"hello world"'
    df = pd.read_csv(
        StringIO(data),
        header=None,
        escapechar="\\",
    )

    assert df.iloc[0, 0] == '"hello world"'
    assert df.iloc[1, 0] == '"hello world"'


def test_parse_dates_none_stays_string_Claude():
    data = "d,val\n2020-01-01,1\n2020-01-02,2\n"
    df = pd.read_csv(StringIO(data), parse_dates=None)

    assert not pd.api.types.is_datetime64_any_dtype(df["d"])
    assert df["d"].iloc[0] == "2020-01-01"


def test_regex_separator_c_engine_raises_Claude():
    data = "1ab2ab3\n"
    with pytest.raises(ValueError, match="does not support regex separators"):
        pd.read_csv(StringIO(data), sep="ab", engine="c", header=None)


def test_on_bad_lines_callable_requires_python_engine_Claude():
    data = "a,b\n1,2\n3,4,5\n"
    with pytest.raises(ValueError, match="engine='python'"):
        pd.read_csv(StringIO(data), engine="c", on_bad_lines=lambda row: row)
