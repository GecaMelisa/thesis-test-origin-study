from pathlib import Path
import pytest

@pytest.fixture
def datapath():
    base = Path(__file__).resolve().parent  # .../tests
    pandas_base = base.parent / "pandas-src" / "pandas" / "tests"

    def _datapath(*parts: str) -> str:
        for root in (base, pandas_base):
            candidate = root.joinpath(*parts)
            if candidate.exists():
                return str(candidate)
        raise FileNotFoundError(f"test data not found for {parts!r}")

    return _datapath
