grammar LangX;

prog: ( stat? NEWLINE )* 
    ;

stat:   ID ':' type 	#assignType 
    | ID '=' expr0      #assign
	| PRINT ID   		#print
	| READ ID   		#read
    ;

expr0:  expr1			#single0
      | expr1 ADD expr1	#add 
    ;

expr1: expr2            #single1
      | expr2 SUB expr2 #sub
    ;

expr2:  expr3			#single2
      | expr3 MULT expr3	#mult 
    ;

expr3: expr4           #single3
      | expr4 DIV expr4     #div 
    ;

expr4:   INT			#int
       | REAL			#real
       | TOINT expr4		#toint
       | TOREAL expr4		#toreal
       | '(' expr0 ')'		#par
    ;

type:   'int'
       | 'real'
    ;

PRINT:	'print' 
    ;

READ:	'read' 
    ;

TOINT: '(int)'
    ;

TOREAL: '(real)'
    ;

ID:   ('a'..'z'|'A'..'Z')+
    ;

REAL: '0'..'9'+'.''0'..'9'+
    ;

INT: '0'..'9'+
    ;

ADD: '+'
    ;

SUB: '-'
    ;

MULT: '*'
    ;

DIV: '/'
    ;

NEWLINE:	'\r'? '\n'
    ;

WS:   (' '|'\t')+ { skip(); }
    ;
