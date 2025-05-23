grammar LangX;

prog: ( (stat|function|struct|class|generator)? NEWLINE )* ;

class: 'class' ID classBlock
;

classBlock: '{' (structVal? NEWLINE)* (function? NEWLINE)* '}'
;

struct: 'struct' ID structBlock
;

structBlock: '{' (structVal? NEWLINE)* '}'
;

structVal: ID ':' type
;

function: FUNCTION fparam fblock ENDFUNCTION
;

generator: GENERATOR fparam gblock
;


fparam: ID ':' funcType
;

fblock: '{' ( stat? NEWLINE )* (return NEWLINE)?
; 

gblock: '{' ( (stat|return)? NEWLINE )* '}'
; 

return: 'return' expr0
;

stat:   ID ':' type                     #assignType
      | ID'.'ID '=' expr0               #assignStructKey
      | ID ':' 'struct' ID              #assignTypedStruct
      | ID ':' 'class' ID               #assignTypedStruct
      | ID ':' type '=' expr0           #assignTyped
      | 'global' ID ':' type            #assignTypeGlobal
      | 'global' ID ':' type '=' expr0  #assignTypedGlobal
      | ID '=' expr0                    #assign
      | PRINT expr0                     #print
      | READ ID                         #read
      | ID '()'    			    #callSingle
      | ID.ID '()'                      #callClass                    
      | ID.NEXT '()'                    #callGenerator                  
      | IF equal '{' blockif '}'	    #if
      | 'for' expr0 '{' blockfor '}'    #for
    ;

blockif: ( stat? NEWLINE )* 
; 

blockfor: ( stat? NEWLINE )* 
; 

IF:	'if'
;

equal: '(' expr0 '==' expr0 ')'
;

expr0:  expr1                          #single0
      | expr0 ADD expr1                #add
    ;

expr1: expr2                           #single1
      | expr1 SUB expr2                #sub
    ;

expr2:  expr3                          #single2
      | expr2 MULT expr3               #mult
    ;

expr3: expr4                           #single3
      | expr3 DIV expr4                #div
    ;

//manage ID
expr4:   INT                           #int
       | INT64                         #int64
       | REAL                          #real
       | FLOAT32                       #float32
       | FLOAT64                       #float64
       | valueOfID                     #valueof
       | TOINT expr4                   #toint
       | TOINT64 expr4                 #toint64
       | TOREAL expr4                  #toreal
       | TOFLOAT32 expr4               #tofloat32
       | TOFLOAT64 expr4               #tofloat64
       | '(' expr0 ')'                 #par
       | ID '()'    		         #call
       | ID.ID '()'                    #callClassReturn       
       | ID.NEXT '()'                  #callGeneratorReturn       
    ;

valueOfID: ID                         #idStat
      | ID'.'ID                       #structValue 
;

type:   'int'
      | 'int64'
      | 'real'
      | 'float32'
      | 'float64'
      | 'any'
    ;

funcType:   'int'
      | 'int64'
      | 'real'
      | 'float32'
      | 'float64'
      | 'void'
    ;

FUNCTION: 'fun'
;

GENERATOR: 'gen'
;

ENDFUNCTION:	'}'
;

NEXT: 'next' ;

PRINT: 'print' ;
READ: 'read' ;

TOINT:     '(int)' ;
TOINT64:   '(int64)' ;
TOREAL:    '(real)' ;
TOFLOAT32: '(float32)' ;
TOFLOAT64: '(float64)' ;

ID:   ('a'..'z'|'A'..'Z')+ ;

REAL: '0'..'9'+ '.' '0'..'9'+ ;
INT: '0'..'9'+ ;
INT64: ('0'..'9')+ 'l' ;
FLOAT32: ('0'..'9')+ '.' ('0'..'9')+ 'f' ;
FLOAT64: ('0'..'9')+ '.' ('0'..'9')+ 'd' ;

ADD: '+' ;
SUB: '-' ;
MULT: '*' ;
DIV: '/' ;

NEWLINE: '\r'? '\n' ;
WS: (' ' | '\t')+ -> skip ;
