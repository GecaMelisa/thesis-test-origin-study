# Thesis Test Origin Study

This repository compares human-written tests with AI-generated tests across two sources:

- JUnit examples (EPL-2.0): https://github.com/junit-team/junit-examples
- pandas (BSD-3-Clause): https://github.com/pandas-dev/pandas

## Contents

- junit-examples/
  - Source copied from junit-examples with human/gpt/codex @Tag annotations.
- pandas/
  - Subset of pandas tests used for read_csv/text reader comparison:
    - tests/io/parser/test_textreader.py (human)
    - tests/io/parser/test_textreader_gpt.py (gpt)
    - tests/io/parser/test_textreader_codex.py (codex)
  - scripts and pytest config used for metrics.
- LICENSES/
  - Upstream license files for third-party sources.

## Attribution

This repository contains modified test files derived from the upstream projects above.
All upstream licenses are preserved under LICENSES/ and referenced in THIRD_PARTY_NOTICES.md.

## Research scope

This is a research repository for thesis work comparing human and AI-generated tests.
It is not an official fork of either upstream project.
