package mini_c;

import java_cup.runtime.*;
import java.util.*;
import static mini_c.sym.*;

%%

%class Lexer
%unicode
%cup
%cupdebug
%line
%column
%yylexthrow Exception

/* The symbols produced by the lexical analyzer not just integers, but objects
   of type java_cup.runtime.Symbol. To create such an object, one invokes the
   function symbol(), defined below, and supplies an integer constant, which
   identifies a terminal symbol; if necessary, one also supplies a semantic
   value, of an appropriate type -- this must match the type declared for this
   terminal symbol in Parser.cup. */

/* See https://www2.in.tum.de/repos/cup/develop/src/java_cup/runtime/ */

/* Technical note: CUP seems to assume that the two integer parameters
   passed to the Symbol constructor are character counts for the left
   and right positions. Instead, we choose to provide line and column
   information. Accordingly, we will replace CUP's error reporting
   routine with our own. */

%{

    private Symbol symbol(int id)
    {
	return new Symbol(id, yyline, yycolumn);
    }

    private Symbol symbol(int id, Object value)
    {
	return new Symbol(id, yyline, yycolumn, value);
    }

    static Stack<Integer> indent = new Stack<Integer>();
    { indent.push(0); }

%}

LineTerminator     = \r | \n | \r\n

WhiteSpace         = [ \t\r\n\f]+

Comment            = "\/\/" .* {LineTerminator}

Comment2           = "/*"( [^*] | (\*+[^*/]) )*\*+\/ 

Identifier         = ( [:letter:] | _ ) ( [:letter:] | [:digit:] | _ )*

OctalDigit         = [0-7]

HexaDigit          = [0-9] | [a-f] | [A-F]

Integer            = ( 0
                        | [1-9][:digit:]*
                        | 0 {OctalDigit}+
                        | 0x {HexaDigit}+
                     )

Char               = \' . \'


/* ' */
%%

/* A specification of which regular expressions to recognize and what
   symbols to produce. */

<YYINITIAL> {

    "="
    { return symbol(EQUAL); }

    ";"
    { return symbol(SCOLON); }

    ","
    { return symbol(COMMA); }

    "("
    { return symbol(LP); }

    ")"
    { return symbol(RP); }

    "{"
    { return symbol(LC); }

    "}"
    { return symbol(RC); }

    "+"
    { return symbol(PLUS); }

    "-"
    { return symbol(MINUS); }

    "->"
    { return symbol(ARROW); }

    "*"
    { return symbol(TIMES); }

    "/"
    { return symbol(DIV); }

    "<"
    { return symbol(LT); }

    "<="
    { return symbol(LE); }

    ">"
    { return symbol(GT); }

    ">="
    { return symbol(GE); }

    "=="
    { return symbol(EQ); }

    "!="
    { return symbol(NE); }

    "&&"
    { return symbol(AND); }

    "||"
    { return symbol(OR); }

    "!"
    { return symbol(NOT); }

    "struct"
    { return symbol(STRUCT); }

    "int"
    { return symbol(TYPE); }

    "void"
    { return symbol(TYPE); }

    "if"
    { return symbol(IF); }

    "else"
    { return symbol(ELSE); }

    "return"
    { return symbol(RETURN); }

    "while"
    { return symbol(WHILE); }

    "sizeof"
    { return symbol(SIZEOF); }

    {Identifier}
    { return symbol(IDENT, yytext().intern()); }

    {Integer}
    { return symbol(CST, Integer.decode(yytext())); }

    {Char}
    { return symbol(CST, (int) yytext().charAt(1)); }

    {WhiteSpace}
    { /* ignore */ }

    {Comment}
    { /* ignore */ }

    {Comment2}
    { /* ignore2 */ }

    .
    { throw new Exception (String.format (
        "Line %d, column %d: illegal character: '%s'\n", yyline, yycolumn, yytext()
      ));
    }

}


