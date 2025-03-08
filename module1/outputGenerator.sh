#!/bin/bash

# (inputs) files needed : tcas_incorrect: the executed version of the program,
#                         cas-tsets.txt: all test cases of the program
# run this before: gcc -fprofile-arcs -ftest-coverage -o tcas_Incorrect tcas_Incorrect.c

# (outputs) output.csv : each line is the set of instructions executed by the testcase[numberofline]

# Check if the .gcno file exists; if not, compile the program
if [ ! -f tcas_Incorrect.gcno ]; then
  gcc -fprofile-arcs -ftest-coverage -o tcas_Incorrect tcas_Incorrect.c
fi

Incorrect="tcas_Incorrect"   # your incorrect version of the tcas program here! *****input 1 ******

Lines=$(wc -l < cas-tests.txt)  # Get the number of lines in cas-tests.txt    *****input 2 ******

for ((i=1; i<=Lines; i++)); do
  ./tcas_Incorrect $(awk "NR==$i" cas-tests.txt) # Generate the .gcda file
  gcov "$Incorrect"   # Generate the .gcov file

  # Parse gcov file: Extract executed line numbers and remove spaces
  awk -F':' '$1 ~ /^?[1-9][0-9]*$/ || $1 ~ /^\*/ {print $2}' tcas_Incorrect.c.gcov | tr -d ' ' | paste -sd, - >> output.csv

  # Cleanup temporary files
  rm -f tcas_Incorrect.gcda tcas_Incorrect.c.gcov
done
