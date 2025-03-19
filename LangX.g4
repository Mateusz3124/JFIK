grammar LangX;

prog: ( stat? NEWLINE )* 
    ;

stat:	 ID '=' expr0   #assign
	| PRINT ID   		#print
	| READ ID   		#read
    ;

expr0:  expr1			#single0
      | expr1 ADD expr1	#add 
    ;

expr1:  expr2			#single1
      | expr2 MULT expr2	#mult 
    ;

expr2:   INT			#int
       | REAL			#real
       | TOINT expr2		#toint
       | TOREAL expr2		#toreal
       | '(' expr0 ')'		#par
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

MULT: '*'
    ;

NEWLINE:	'\r'? '\n'
    ;

WS:   (' '|'\t')+ { skip(); }
    ;
