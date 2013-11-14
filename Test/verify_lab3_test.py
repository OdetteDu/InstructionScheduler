#!/usr/bin/python
# use under FALL_2013/LAB3/TestData/

import os, sys

ILOC_INST=['load','loadI','store','add','sub','mult','lshift', 'rshift', 'output', 'nop']

sim = './sim '

if len(sys.argv) > 1:
   sim = sys.argv[1]

os.system('find -name "*.i" > testing_block_location')
f = open('testing_block_location', 'r')
while True:
    line = f.readline()
    if line == "":
        break
    print 'checking test %s' % line.strip()
    f_test = open(line.strip(), 'r')
    expected_out = []
    input = sim
    while True:
        line_test = f_test.readline()
        if line_test == "":
            break
        if not '//' in line_test:
            op = ''
            if len(line_test.strip()) > 0:
                op = line_test.strip().split()[0]
            if not op == '' and not op in ILOC_INST:
                print 'test %s using operator not in spec' % line.strip()
                print 'line: %s' % line_test
                sys.exit(1)
            if op == 'output':
                mem = int(line_test.strip().split()[1])
                if mem not in range(1024, 32767):
                    print 'visible memory out of bound in test %s' % line
                    print 'line: %s' % line_test
        if 'Usage' in line_test:
            input = input + line_test.split('/')[1].split('<')[0].strip()
        if "Expected oupput" in line_test:
            for num in line_test.rsplit(':').split(' '):
                expected_out.append(num)
    f_test.close()
    if input == sim:
        input = input + ' -s 3 '
    cmd = input + ' < ' + line.strip() + ' > tmp'
    os.system(cmd)
    f_result = open('tmp', 'r')
    results = []
    while True:
        line_result = f_result.readline()
        if line_result == "":
            break
        results.append(line_result.strip())
    f_result.close()
    if len(expected_out) > 0:
        if not expected_out == results:
            print '====test %s does not generate output as described'
            sys.exit(1)

os.system('rm tmp testing_block_location')

