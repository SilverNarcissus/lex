#basic definition of type (shouldn't be changed!)
LETTER = [a-zA-Z]
DIGIT = [0-9]
DELIMITER = [\t\n\s]
OPERATOR = [+ | - | \* | \/ | % | ( | ) | [ | ] | { | }]
EMPTY = ε
DOT = \.
###########################
## some specification
## ~ means connect

# RE start:
$ ID = {LETTER}~({LETTER}|{DIGIT})*
$ INTEGER = {DIGIT}~{DIGIT}*
$ REAL = {DIGIT}~{DIGIT}*~{DOT}~{DIGIT}~{DIGIT}*
$ OPERATOR = {OPERATOR}
# RE end