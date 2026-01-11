# GPT-generated placeholder for read_csv tests\n
import csv
from io import StringIO

import pandas as pd
import pytest

pytestmark = pytest.mark.gpt


def test_basic_csv_parsing_Gpt():
    data = "col1,col2\n1,2\n3,4\n"
    df = pd.read_csv(StringIO(data))

    assert list(df.columns) == ["col1", "col2"]
    assert df.shape == (2, 2)
    assert df.iloc[0, 0] == 1
    assert df.iloc[1, 1] == 4


def test_read_csv_no_header_with_names_Gpt():
    data = "1,2\n3,4\n"
    df = pd.read_csv(StringIO(data), header=None, names=["a", "b"])

    assert list(df.columns) == ["a", "b"]
    assert df.shape == (2, 2)
    assert df.iloc[0].tolist() == [1, 2]
    assert df.iloc[1].tolist() == [3, 4]


def test_index_col_single_column_Gpt():
    data = "id,value\n10,a\n20,b\n"
    df = pd.read_csv(StringIO(data), index_col="id")

    assert list(df.index) == [10, 20]
    assert list(df.columns) == ["value"]
    assert df.loc[10, "value"] == "a"
    assert df.loc[20, "value"] == "b"


def test_usecols_subset_Gpt():
    data = "a,b,c\n1,2,3\n4,5,6\n"
    df = pd.read_csv(StringIO(data), usecols=["a", "c"])

    assert list(df.columns) == ["a", "c"]
    assert df.shape == (2, 2)
    assert df.iloc[0].tolist() == [1, 3]


def test_dtype_and_converters_Gpt():
    data = "a,b\n1,10\n2,20\n"

    df = pd.read_csv(
        StringIO(data),
        dtype={"a": "int64"},
        converters={"b": lambda x: f"val-{x}"},
    )

    assert pd.api.types.is_integer_dtype(df["a"])
    assert df["b"].tolist() == ["val-10", "val-20"]


def test_na_values_and_keep_default_na_Gpt():
    data = "col\nNA\nmissing\nvalue\n"

    df_default = pd.read_csv(StringIO(data), na_values=["missing"])
    df_no_default = pd.read_csv(
        StringIO(data),
        na_values=["missing"],
        keep_default_na=False,
    )

    assert df_default["col"].isna().tolist() == [True, True, False]
    assert df_no_default["col"].isna().tolist() == [False, True, False]
    assert df_no_default["col"].iloc[0] == "NA"


def test_comment_lines_skipped_Gpt():
    data = "a,b\n1,2\n# comment line\n3,4\n"
    df = pd.read_csv(StringIO(data), comment="#")

    assert df.shape == (2, 2)
    assert df.iloc[0].tolist() == [1, 2]
    assert df.iloc[1].tolist() == [3, 4]


def test_delim_whitespace_true_parses_space_separated_Gpt():
    data = "a b c\n1 2 3\n4 5 6\n"
    df = pd.read_csv(StringIO(data), delim_whitespace=True)

    assert list(df.columns) == ["a", "b", "c"]
    assert df.iloc[0].tolist() == [1, 2, 3]
    assert df.iloc[1].tolist() == [4, 5, 6]


def test_sep_and_delimiter_conflict_raises_Gpt():
    data = "a,b\n1,2\n"
    with pytest.raises(ValueError) as excinfo:
        pd.read_csv(StringIO(data), sep=",", delimiter=";")

    assert "Specified a sep and a delimiter" in str(excinfo.value)


def test_delim_whitespace_and_sep_conflict_raises_Gpt():
    data = "a b\n1 2\n"
    with pytest.raises(ValueError) as excinfo:
        pd.read_csv(StringIO(data), sep=" ", delim_whitespace=True)

    assert "delim_whitespace=True" in str(excinfo.value)


def test_newline_as_separator_not_allowed_Gpt():
    data = "a\n1\n2\n"
    with pytest.raises(ValueError) as excinfo:
        pd.read_csv(StringIO(data), sep="\n")  # Intentional invalid usage

    msg = str(excinfo.value)
    assert "Specified \\n as separator or delimiter" in msg
    assert "line terminator as separator" in msg


def test_on_bad_lines_invalid_value_Gpt():
    data = "a,b\n1,2\n"
    with pytest.raises(ValueError) as excinfo:
        pd.read_csv(StringIO(data), on_bad_lines="invalid")

    assert "invalid for on_bad_lines" in str(excinfo.value)


def test_on_bad_lines_callable_requires_python_engine_Gpt():
    data = "a,b\n1,2\n3,4,5\n"
    with pytest.raises(ValueError) as excinfo:
        pd.read_csv(StringIO(data), engine="c", on_bad_lines=lambda row: row)

    msg = str(excinfo.value)
    assert "on_bad_line can only be a callable function" in msg
    assert "engine='python'" in msg


def test_on_bad_lines_skip_malformed_row_Gpt():
    data = "a,b\n1,2\n3,4,5\n6,7\n"

    df = pd.read_csv(StringIO(data), engine="python", on_bad_lines="skip")

    assert df.shape == (2, 2)
    assert df.iloc[0].tolist() == [1, 2]
    assert df.iloc[1].tolist() == [6, 7]


def test_on_bad_lines_error_raises_parsererror_Gpt():
    data = "a,b\n1,2\n3,4,5\n"

    with pytest.raises(pd.errors.ParserError):
        pd.read_csv(StringIO(data), engine="python", on_bad_lines="error")


def test_skipfooter_trims_lines_with_default_engine_Gpt():
    data = "a\n1\n2\n3\n"
    df = pd.read_csv(StringIO(data), skipfooter=1, engine="python")

    assert df.shape == (2, 1)
    assert df["a"].tolist() == [1, 2]


def test_skipfooter_with_c_engine_raises_Gpt():
    data = "a\n1\n2\n3\n"
    with pytest.raises(ValueError) as excinfo:
        pd.read_csv(StringIO(data), skipfooter=1, engine="c")

    assert "does not support skipfooter" in str(excinfo.value)


def test_unknown_engine_raises_Gpt():
    data = "a,b\n1,2\n"
    with pytest.raises(ValueError) as excinfo:
        pd.read_csv(StringIO(data), engine="unknown")

    assert "Unknown engine" in str(excinfo.value)


def test_iterator_true_returns_textfilereader_Gpt():
    data = "a,b\n1,2\n3,4\n"
    reader = pd.read_csv(StringIO(data), iterator=True)

    first_chunk = next(reader)
    with pytest.raises(StopIteration):
        next(reader)

    assert hasattr(reader, "get_chunk")
    assert first_chunk.shape == (2, 2)


def test_chunksize_iteration_over_rows_Gpt():
    data = "a,b\n1,2\n3,4\n5,6\n"
    reader = pd.read_csv(StringIO(data), chunksize=1)

    chunks = list(reader)
    assert len(chunks) == 3
    assert all(chunk.shape == (1, 2) for chunk in chunks)
    assert chunks[0].iloc[0].tolist() == [1, 2]
    assert chunks[2].iloc[0].tolist() == [5, 6]


def test_custom_csv_dialect_overrides_delimiter_Gpt():
    class SemiColonDialect(csv.Dialect):
        delimiter = ";"
        quotechar = '"'
        doublequote = True
        skipinitialspace = False
        lineterminator = "\n"
        quoting = csv.QUOTE_MINIMAL

    data = "a;b\n1;2\n3;4\n"
    df = pd.read_csv(StringIO(data), dialect=SemiColonDialect)

    assert list(df.columns) == ["a", "b"]
    assert df.shape == (2, 2)
    assert df.iloc[0].tolist() == [1, 2]


def test_engine_regex_separator_with_c_engine_raises_Gpt():
    data = "1ab2ab3\n"
    with pytest.raises(ValueError) as excinfo:
        pd.read_csv(StringIO(data), sep="ab", engine="c", header=None)

    msg = str(excinfo.value)
    assert "does not support regex separators" in msg
    assert "separators > 1 char" in msg


def test_sep_none_with_c_engine_raises_Gpt():
    data = "1 2\n3 4\n"
    with pytest.raises(ValueError) as excinfo:
        pd.read_csv(StringIO(data), sep=None, engine="c", header=None)

    msg = str(excinfo.value)
    assert "sep=None" in msg
    assert "delim_whitespace=False" in msg


def test_parse_dates_default_false_with_none_Gpt():
    data = "d,val\n2020-01-01,1\n2020-01-02,2\n"
    df = pd.read_csv(StringIO(data), parse_dates=None)

    assert not pd.api.types.is_datetime64_any_dtype(df["d"])
    assert df["d"].iloc[0] == "2020-01-01"


def test_thousands_and_decimal_parsing_Gpt():
    data = "value\n1.234,5\n2.345,6\n"
    df = pd.read_csv(StringIO(data), sep=";", thousands=".", decimal=",")
    values = df["value"].tolist()

    assert pytest.approx(values[0], rel=1e-9) == 1234.5
    assert pytest.approx(values[1], rel=1e-9) == 2345.6
