#input files: **tcas_Incorrect.c**   **cas-tests.txt**

#output files: **output.csv** (comtain the parsing results of gcov files for all test cases included)

#process: **scriptreader.java** will run the script gcovparser.sh 
**gcovparser.sh** generate the **.gcno** and **.gcda** and **.gcov** files for each test case and parse the .gcov file to extract the set of instructions executed by each test case and write it in result.csv
