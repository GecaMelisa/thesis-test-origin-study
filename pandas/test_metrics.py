from pathlib import Path
import ast


TEST_FILE = Path("tests/io/parser/test_textreader_codex.py")


def count_asserts_and_tests(path: Path):
    source = path.read_text(encoding="utf-8")
    tree = ast.parse(source)

    num_tests = 0
    num_asserts = 0

    for node in ast.walk(tree):
        # test funkcije: def test_*
        if isinstance(node, ast.FunctionDef) and node.name.startswith("test_"):
            num_tests += 1
        # assert izrazi
        if isinstance(node, ast.Assert):
            num_asserts += 1

    # grube linije koda (bez praznih i komentara)
    loc = sum(
        1
        for line in source.splitlines()
        if line.strip() and not line.strip().startswith("#")
    )

    return num_tests, num_asserts, loc


def main():
    num_tests, num_asserts, loc = count_asserts_and_tests(TEST_FILE)

    print(f"File: {TEST_FILE}")
    print(f"Number of test functions: {num_tests}")
    print(f"Number of assert statements: {num_asserts}")
    print(f"Lines of code (non-empty, non-comment): {loc}")
    if loc:
        print(f"Assertion density (asserts / LOC): {num_asserts / loc:.3f}")
    if num_tests:
        print(f"Asserts per test function: {num_asserts / num_tests:.2f}")


if __name__ == "__main__":
    main()
