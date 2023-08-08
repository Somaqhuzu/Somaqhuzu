#!/usr/bin/python3
import os
import sys
from prettytable import PrettyTable

table = PrettyTable(["cutoff","Time","Speeup"])

file = open("output.csv","r")
k=64
serial=int(file.readline())
table.add_row(["serial",serial,1.0])
time = " "
while time!='q':
    try:
        tt=int(file.readline().strip("\n"))
        time = round(serial/int(tt),2)
    except:
        break
    cutoff = str(k)  
    table.add_row([cutoff,tt,time])
    k = k * 2
print(table)