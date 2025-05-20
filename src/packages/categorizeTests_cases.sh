#!/bin/bash

# --- Define absolute paths to files in src/packages ---
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CORRECT_C="$SCRIPT_DIR/tcas_Correct.c"
INCORRECT_C="$SCRIPT_DIR/tcas_Incorrect.c"
TEST_CASES="$SCRIPT_DIR/cas-tests.txt"

PASSING_FILE="$SCRIPT_DIR/Passing_test_cases.txt"
FAILING_FILE="$SCRIPT_DIR/Failing_test_cases.txt"

# --- Compile programs ---
gcc -o "$SCRIPT_DIR/tcas_Correct" "$CORRECT_C"
gcc -o "$SCRIPT_DIR/tcas_Incorrect" "$INCORRECT_C"

# --- Initialize output files ---
> "$PASSING_FILE"
> "$FAILING_FILE"

# --- Run test cases ---
while IFS= read -r test_case; do
  "$SCRIPT_DIR/tcas_Correct" $test_case > "$SCRIPT_DIR/correct_output.txt"
  "$SCRIPT_DIR/tcas_Incorrect" $test_case > "$SCRIPT_DIR/incorrect_output.txt"

  if diff "$SCRIPT_DIR/correct_output.txt" "$SCRIPT_DIR/incorrect_output.txt" > /dev/null; then
    echo "$test_case" >> "$PASSING_FILE"
  else
    echo "$test_case" >> "$FAILING_FILE"
  fi
done < "$TEST_CASES"

# --- Cleanup ---
rm -f "$SCRIPT_DIR/correct_output.txt" "$SCRIPT_DIR/incorrect_output.txt"