import io
import pytest
import numpy as np
import pandas as pd

pytestmark = pytest.mark.codex


# Codex-generated alternative tests for pandas.read_csv

def test_comma_delimiter_basic_Codex():
    data = io.StringIO("a,b\n1,2\n3,4")
    df = pd.read_csv(data)
    assert df.shape == (2, 2)
    assert list(df.columns) == ["a", "b"]
    assert df.iloc[1, 1] == 4


def test_semicolon_delimiter_engine_c_Codex():
    data = io.StringIO("x;y\n5;7\n9;11")
    df = pd.read_csv(data, sep=";", engine="c")
    assert df.shape == (2, 2)
    assert df.dtypes.tolist() == [np.dtype("int64"), np.dtype("int64")]
    assert df.iloc[0, 1] == 7


def test_tab_delimiter_engine_python_Codex():
    data = io.StringIO("col1\tcol2\nfoo\tbar\nbaz\tqux")
    df = pd.read_csv(data, sep="\t", engine="python")
    assert df.shape == (2, 2)
    assert df.iloc[1, 0] == "baz"
    assert df.iloc[0, 1] == "bar"


def test_quoted_fields_preserved_Codex():
    data = io.StringIO(
        'name,comment\n"Alice","hello, world"\n"Bob","""quoted"" text"'
    )
    df = pd.read_csv(data)
    assert df.shape == (2, 2)
    assert df.iloc[0, 1] == "hello, world"
    assert df.iloc[1, 1] == '"quoted" text'


def test_quoted_with_delimiter_inside_Codex():
    csv = 'value\n"1,234"\n"5,678"'
    df = pd.read_csv(io.StringIO(csv))
    assert df.shape == (2, 1)
    assert df.iloc[0, 0] == "1,234"
    assert df.iloc[1, 0] == "5,678"


def test_double_quote_escape_Codex():
    csv = 'text\n"a""b"\n"c""""d"'
    df = pd.read_csv(io.StringIO(csv))
    assert df.shape == (2, 1)
    assert df.iloc[0, 0] == "a\"b"
    assert df.iloc[1, 0] == 'c""d'


def test_custom_na_values_Codex():
    csv = "a,b\nNA,2\n3,missing"
    df = pd.read_csv(io.StringIO(csv), na_values=["missing"])
    assert df.isna().sum().tolist() == [1, 1]
    assert np.isnan(df.iloc[0, 0])
    assert np.isnan(df.iloc[1, 1])


def test_na_filter_disabled_Codex():
    csv = "val\nNA\nnull"
    df = pd.read_csv(io.StringIO(csv), na_filter=False)
    assert df.shape == (2, 1)
    assert df.iloc[0, 0] == "NA"
    assert df.iloc[1, 0] == "null"


def test_encoding_utf16_le_Codex():
    text = "col\nç".encode("utf-16-le")
    buffer = io.BytesIO(text)
    df = pd.read_csv(buffer, encoding="utf-16-le")
    assert df.shape == (1, 1)
    assert df.iloc[0, 0] == "ç"


def test_encoding_errors_replace_Codex():
    raw = b"x\n\xff\xfe"
    df = pd.read_csv(io.BytesIO(raw), encoding_errors="replace")
    assert df.shape == (1, 1)
    assert df.iloc[0, 0].startswith("�")


def test_file_handle_not_closed_Codex():
    buf = io.StringIO("a,b\n1,2")
    df = pd.read_csv(buf)
    assert not buf.closed
    assert df.iloc[0, 0] == 1


def test_stringio_position_respected_Codex():
    buf = io.StringIO("header\nfirst\nsecond")
    _ = buf.readline()
    df = pd.read_csv(buf, header=None)
    assert df.shape == (2, 1)
    assert df.iloc[0, 0] == "first"
    assert df.iloc[1, 0] == "second"


def test_header_none_creates_default_columns_Codex():
    csv = "1,2\n3,4"
    df = pd.read_csv(io.StringIO(csv), header=None)
    assert list(df.columns) == [0, 1]
    assert df.iloc[1, 1] == 4


def test_header_override_names_Codex():
    csv = "a,b\n1,2"
    df = pd.read_csv(io.StringIO(csv), names=["x", "y"], header=0)
    assert list(df.columns) == ["x", "y"]
    assert df.iloc[0, 0] == 1


def test_skiprows_and_header_Codex():
    csv = "skip,me\na,b\n5,6\n7,8"
    df = pd.read_csv(io.StringIO(csv), skiprows=1)
    assert df.shape == (2, 2)
    assert list(df.columns) == ["a", "b"]
    assert df.iloc[1, 1] == 8


def test_dtype_inference_mixed_numeric_Codex():
    csv = "a,b\n1,2.5\n3,4.0"
    df = pd.read_csv(io.StringIO(csv))
    assert df.dtypes.tolist() == [np.dtype("int64"), np.dtype("float64")]
    assert df.iloc[0, 1] == 2.5


def test_dtype_specified_override_Codex():
    csv = "a,b\n1,2\n3,4"
    df = pd.read_csv(io.StringIO(csv), dtype={"a": "float64", "b": "string"})
    assert df.dtypes.tolist() == [np.dtype("float64"), pd.StringDtype()]
    assert df.iloc[1, 0] == 3.0
    assert df.iloc[0, 1] == "2"


def test_parser_error_on_unexpected_fields_Codex():
    csv = "a,b\n1,2,3"
    df = pd.read_csv(io.StringIO(csv))
    assert list(df.columns) == ["a", "b"]
    assert df.index.tolist() == [1]
    assert df.iloc[0].tolist() == [2, 3]


def test_multiline_quoted_field_Codex():
    csv = 'id,text\n1,"hello\nworld"\n2,"bye"'
    df = pd.read_csv(io.StringIO(csv))
    assert df.shape == (2, 2)
    assert df.iloc[0, 1] == "hello\nworld"
    assert df.iloc[1, 1] == "bye"


def test_skipinitialspace_trims_space_Codex():
    csv = "a, b\n1, 2\n3, 4"
    df = pd.read_csv(io.StringIO(csv), skipinitialspace=True)
    assert df.iloc[0, 1] == 2
    assert df.iloc[1, 0] == 3


def test_no_skipinitialspace_preserves_space_Codex():
    csv = "a, b\n1, 2"
    df = pd.read_csv(io.StringIO(csv), skipinitialspace=False, dtype=str)
    assert df.loc[0, " b"] == " 2"


def test_thousands_and_decimal_numeric_Codex():
    csv = "num\n1.234,5\n6.789,0"
    df = pd.read_csv(io.StringIO(csv), sep=";", thousands=".", decimal=",")
    assert df.shape == (2, 1)
    assert df.iloc[0, 0] == 1234.5
    assert df.iloc[1, 0] == 6789.0


def test_boolean_parsing_Codex():
    csv = "flag\nTrue\nFalse\ntrue"
    df = pd.read_csv(io.StringIO(csv), dtype={"flag": "boolean"})
    assert df.dtypes.tolist() == [pd.BooleanDtype()]
    assert bool(df.iloc[0, 0]) is True
    assert bool(df.iloc[1, 0]) is False
    assert bool(df.iloc[2, 0]) is True


def test_engine_switch_consistency_Codex():
    csv = "a|b\n1|2\n3|4"
    df_c = pd.read_csv(io.StringIO(csv), sep="|", engine="c")
    df_py = pd.read_csv(io.StringIO(csv), sep="|", engine="python")
    assert df_c.equals(df_py)
    assert df_c.iloc[1, 1] == 4
