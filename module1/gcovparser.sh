#!/bin/bash

# (inputs) files needed : tcas_incorrect: the executed version of the program,
#                         cas-tsets.txt: all test cases of the program
#                         tcas_incorrect.gcno else add gcc -fprofile-arcs -ftest-coverage -o tcas_Incorrect tcas_Incorrect.c

# (outputs) output.csv : each line is the set of instructions executed by the testcase(numberofline) 

Incorrect="tcas_Incorrect"   # your incorrect version of the tcas program here!

Lines=$(wc -l < cas-tests.txt)  # Get the number of lines in cas-tests.txt

for ((i=1; i<=Lines; i++)); do
  ./tcas_Incorrect $(awk "NR==$i" cas-tests.txt) # generate the .gcda file associate to the specific test case extracted by the awk command

  gcov "$Incorrect" # generate the .gcov file conataining the covorage data of the specific test case



  awk -F':' '$1 ~ /^?[1-9][0-9]*$/ || $1 ~ /^?\*/ {print $2}' tcas_Incorrect.c.gcov > t.csv
  paste -sd, t.csv >> output.csv

  rm tcas_Incorrect.gcda tcas_Incorrect.c.gcov
  rm -f t.csv
done
