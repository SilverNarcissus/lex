#basic definition of type (shouldn't be changed!)
LETTER = [a-zA-Z]
DIGIT = [0-9]
DELIMITER = [\t | \n | \s | ;]
OPERATOR = [+ | - | \* | \/ | % | ( | ) | [ | ] | { | } | < | > | \. | =]
EMPTY = ε
DOT = \.
###########################
## some specification
## ~ means connect

# RE start:
$ ID = {LETTER}~({LETTER}|{DIGIT})*
$ INTEGER = {DIGIT}~{DIGIT}*
$ REAL = {DIGIT}~{DIGIT}*~{DOT}~{DIGIT}~{DIGIT}*
$ OPERATOR = {OPERATOR}|{DOT}
# RE end

# keywords:
% abstract, assert, boolean, break, byte, case, catch, char, class, const, continue, default, do, double, else, enum, extends, false, final, finally,float, for, goto, if, implements, import, instanceof, int, interface, long, native, new, null, package, private, protected, public, return, short, static, strictfp, super, switch, synchronized, this, throw, throws, transient, try, true, void, volatile, while