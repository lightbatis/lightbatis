/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 by Bart Kiers (original author) and Alexandre Vitorelli (contributor -> ported to CSharp)
 * Copyright (c) 2017-2020 by Ivan Kochurkin (Positive Technologies):
    added ECMAScript 6 support, cleared and transformed to the universal grammar.
 * Copyright (c) 2018 by Juan Alvarez (contributor -> ported to Go)
 * Copyright (c) 2019 by Student Main (contributor -> ES2020)
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
parser grammar JSVParser;

options {
    tokenVocab=JSVLexer;
    superClass=JavaScriptParserBase;
}

program
    : sourceElements? EOF
    ;

sourceElement
    : statement
    ;

statement
    : block
    | variableStatement
//    | importStatement
//    | exportStatement
    | emptyStatement
//    | classDeclaration
//    | expressionStatement
//    | ifStatement
      | ifThenStatement
//    | iterationStatement
//    | continueStatement
//    | breakStatement
    | returnStatement
    | matrixStatement
//    | yieldStatement
//    | withStatement
//    | labelledStatement
//    | switchStatement
//    | throwStatement
//    | tryStatement
//    | debuggerStatement
//    | functionDeclaration
    ;

block
    : '{' statementList? '}'
    ;

statementList
    : statement+
    ;
//
//importStatement
//    : Import importFromBlock
//    ;
//
//importFromBlock
//    : importDefault? (importNamespace | moduleItems) importFrom eos
//    | StringLiteral eos
//    ;
//
//moduleItems
//    : '{' (aliasName ',')* (aliasName ','?)? '}'
//    ;
//
//importDefault
//    : aliasName ','
//    ;
//
//importNamespace
//    : ('*' | identifierName) (As identifierName)?
//    ;
//
//importFrom
//    : From StringLiteral
//    ;
//
//aliasName
//    : identifierName (As identifierName)?
//    ;
//
//exportStatement
//    : Export (exportFromBlock | declaration) eos    # ExportDeclaration
//    | Export Default singleExpression eos           # ExportDefaultDeclaration
//    ;
//
//exportFromBlock
//    : importNamespace importFrom eos
//    | moduleItems importFrom? eos
//    ;

//declaration
//    : variableStatement
//    | classDeclaration
//    | functionDeclaration
//    ;

variableStatement
    : variableDeclarationList
    | variableDeclarationList eos
    ;


variableDeclarationList
    : varModifier variableDeclaration (',' variableDeclaration)*
    ;

variableDeclaration
    : assignable ('=' singleExpression)? // ECMAScript 6: Array & Object Matching
    ;

emptyStatement
    : SemiColon
    ;

expressionStatement
    : {this.notOpenBraceAndNotFunction()}? expressionSequence eos
    ;
// if ... then ... endif
ifThenStatement
    : If expressionSequence Then statement Endif eos
    ;

// 参数库
// (a, b)
// 8,9
// [+,19], 10
// (-,19], 12
matrixStatement
    :  {this.lineAhead()}?'(' Identifier (',' Identifier)+ ')'  matrixRow+ eos
    ;
 //矩阵的一行
 matrixRow
     : {this.lineAhead()}?matrixElement(',' matrixElement)+ eos
     ;

 matrixElement
     : matrixElementNumber
     | matrixElementGreaterThan
     | matrixElementAtLeast
     | matrixElementLessThan
     | matrixElementAtMost
     | matrixElementClose
     | matrixElementOpen
     | matrixElementOpenClose
     | matrixElementCloseOpen
     ;

matrixElementNumber
    : DecimalLiteral
    ;

// matrixElementLiteral
//     : DecimalLiteral
//     | '+'
//     | '-'
//     ;

// * 全封闭的区间 : [T0, T1]
// * 左开右闭区间 : (T0, T1]
// * 左闭右开区间 : [T0, T1)
// * 全开放的区间 : (T0, T1)

//全封闭的区间 : [T0, T1]
matrixElementClose
    : '[' DecimalLiteral(',' DecimalLiteral)+  ']'
    ;

 //全开放的区间 : (T0, T1)
 matrixElementOpen
     : '(' DecimalLiteral(',' DecimalLiteral)+  ')'
     ;

 //左开右闭区间 : (T0, T1]
 matrixElementOpenClose
     : '(' DecimalLiteral(',' DecimalLiteral)+  ']'
     ;

 //左闭右开区间 : [T0, T1)
 matrixElementCloseOpen
     : '[' DecimalLiteral(',' DecimalLiteral)+  ')'
     ;

//(a..+∞) {x | x > a}         greaterThan
matrixElementGreaterThan
    : '(' DecimalLiteral ',' '+' ')'
    ;

//[a..+∞) {x | x >= a}        atLeast
matrixElementAtLeast
    : '[' DecimalLiteral ',' '+' ')'
    | '[' DecimalLiteral ',' '+' ']'
    ;

//(-∞..b) {x | x < b}         lessThan
matrixElementLessThan
    : '(' '-' ',' DecimalLiteral ')'
    ;

//(-∞..b] {x | x <= b}        atMost
matrixElementAtMost
    : '(' '-' ',' DecimalLiteral ']'
    ;

//ifStatement
//    : If '(' expressionSequence ')' statement (Else statement)?
//    ;


//iterationStatement
//    : Do statement While '(' expressionSequence ')' eos                                                                       # DoStatement
//    | While '(' expressionSequence ')' statement                                                                              # WhileStatement
//    | For '(' (expressionSequence | variableDeclarationList)? ';' expressionSequence? ';' expressionSequence? ')' statement   # ForStatement
//    | For '(' (singleExpression | variableDeclarationList) In expressionSequence ')' statement                                # ForInStatement
//    // strange, 'of' is an identifier. and this.p("of") not work in sometime.
//    | For Await? '(' (singleExpression | variableDeclarationList) identifier{this.p("of")}? expressionSequence ')' statement  # ForOfStatement
//    ;

varModifier  // let, const - ECMAScript 6
    : Var
    | let
    | Const
    |
    ;

//continueStatement
//    : Continue ({this.notLineTerminator()}? identifier)? eos
//    ;

//breakStatement
//    : Break ({this.notLineTerminator()}? identifier)? eos
//    ;

returnStatement
    : Return ({this.notLineTerminator()}? expressionSequence)?
    ;
//
//yieldStatement
//    : Yield ({this.notLineTerminator()}? expressionSequence)? eos
//    ;
//
//withStatement
//    : With '(' expressionSequence ')' statement
//    ;
//
//switchStatement
//    : Switch '(' expressionSequence ')' caseBlock
//    ;
//
//caseBlock
//    : '{' caseClauses? (defaultClause caseClauses?)? '}'
//    ;
//
//caseClauses
//    : caseClause+
//    ;
//
//caseClause
//    : Case expressionSequence ':' statementList?
//    ;
//
//defaultClause
//    : Default ':' statementList?
//    ;
//
//labelledStatement
//    : identifier ':' statement
//    ;
//
//throwStatement
//    : Throw {this.notLineTerminator()}? expressionSequence eos
//    ;
//
//tryStatement
//    : Try block (catchProduction finallyProduction? | finallyProduction)
//    ;
//
//catchProduction
//    : Catch ('(' assignable? ')')? block
//    ;
//
//finallyProduction
//    : Finally block
//    ;
//
//debuggerStatement
//    : Debugger eos
//    ;
//
//functionDeclaration
//    : Async? Function '*'? identifier '(' formalParameterList? ')' '{' functionBody '}'
//    ;
//
//classDeclaration
//    : Class identifier classTail
//    ;
//
//classTail
//    : (Extends singleExpression)? '{' classElement* '}'
//    ;
//
//classElement
//    : (Static | {this.n("static")}? identifier | Async)* (methodDefinition | assignable '=' objectLiteral ';')
//    | emptyStatement
//    | '#'? propertyName '=' singleExpression
//    ;
//
//methodDefinition
//    : '*'? '#'? propertyName '(' formalParameterList? ')' '{' functionBody '}'
//    | '*'? '#'? getter '(' ')' '{' functionBody '}'
//    | '*'? '#'? setter '(' formalParameterList? ')' '{' functionBody '}'
//    ;

formalParameterList
    : formalParameterArg (',' formalParameterArg)* (',' lastFormalParameterArg)?
    | lastFormalParameterArg
    ;

formalParameterArg
    : assignable ('=' singleExpression)?      // ECMAScript 6: Initialization
    ;

lastFormalParameterArg                        // ECMAScript 6: Rest Parameter
    : Ellipsis singleExpression
    ;

//functionBody
//    : sourceElements?
//    ;

sourceElements
    : sourceElement+
    ;

arrayLiteral
    : ('[' elementList ']')
    ;

elementList
    : ','* arrayElement? (','+ arrayElement)* ','* // Yes, everything is optional
    ;

arrayElement
    : Ellipsis? singleExpression
    ;

propertyAssignment
    : propertyName ':' singleExpression                                             # PropertyExpressionAssignment
    | '[' singleExpression ']' ':' singleExpression                                 # ComputedPropertyExpressionAssignment
    //| Async? '*'? propertyName '(' formalParameterList?  ')'  '{' functionBody '}'  # FunctionProperty
    //| getter '(' ')' '{' functionBody '}'                                           # PropertyGetter
   // | setter '(' formalParameterArg ')' '{' functionBody '}'                        # PropertySetter
    | Ellipsis? singleExpression                                                    # PropertyShorthand
    ;

propertyName
    : identifierName
    | StringLiteral
    | numericLiteral
    | '[' singleExpression ']'
    ;

arguments
    : '('(argument (',' argument)* ','?)?')'
    ;

argument
    : Ellipsis? (singleExpression | identifier)
    ;

expressionSequence
    : singleExpression (',' singleExpression)*
    ;

singleExpression
    :
    //anoymousFunction                                                      # FunctionExpression
//    | Class identifier? classTail                                           # ClassExpression
     singleExpression '[' expressionSequence ']'                           # MemberIndexExpression
    | singleExpression '?'? '.' '#'? identifierName                         # MemberDotExpression
    | singleExpression arguments                                            # ArgumentsExpression
    | New singleExpression arguments?                                       # NewExpression
    | New '.' identifier                                                    # MetaExpression // new.target
    | singleExpression {this.notLineTerminator()}? '++'                     # PostIncrementExpression
    | singleExpression {this.notLineTerminator()}? '--'                     # PostDecreaseExpression
    | Delete singleExpression                                               # DeleteExpression
    | Void singleExpression                                                 # VoidExpression
    | Typeof singleExpression                                               # TypeofExpression
    | '++' singleExpression                                                 # PreIncrementExpression
    | '--' singleExpression                                                 # PreDecreaseExpression
    | '+' singleExpression                                                  # UnaryPlusExpression
    | '-' singleExpression                                                  # UnaryMinusExpression
    | '~' singleExpression                                                  # BitNotExpression
    | '!' singleExpression                                                  # NotExpression
    | Await singleExpression                                                # AwaitExpression
    | <assoc=right> singleExpression '**' singleExpression                  # PowerExpression
    | singleExpression ('*' | '/' | '%') singleExpression                   # MultiplicativeExpression
    | singleExpression ('+' | '-') singleExpression                         # AdditiveExpression
    | singleExpression '??' singleExpression                                # CoalesceExpression
    | singleExpression ('<<' | '>>' | '>>>') singleExpression               # BitShiftExpression
    | singleExpression ('<' | '>' | '<=' | '>=') singleExpression           # RelationalExpression
    | singleExpression Instanceof singleExpression                          # InstanceofExpression
    | singleExpression In singleExpression                                  # InExpression
    | singleExpression ('==' | '!=' | '===' | '!==') singleExpression       # EqualityExpression
    | singleExpression '&' singleExpression                                 # BitAndExpression
    | singleExpression '^' singleExpression                                 # BitXOrExpression
    | singleExpression '|' singleExpression                                 # BitOrExpression
    | singleExpression '&&' singleExpression                                # LogicalAndExpression
    | singleExpression '||' singleExpression                                # LogicalOrExpression
    | singleExpression '?' singleExpression ':' singleExpression            # TernaryExpression
    | <assoc=right> singleExpression '=' singleExpression                   # AssignmentExpression
    | <assoc=right> singleExpression assignmentOperator singleExpression    # AssignmentOperatorExpression
    | Import '(' singleExpression ')'                                       # ImportExpression
    | singleExpression TemplateStringLiteral                                # TemplateStringExpression  // ECMAScript 6
    //| yieldStatement                                                        # YieldExpression // ECMAScript 6
    | This                                                                  # ThisExpression
    | identifier                                                            # IdentifierExpression
    | Super                                                                 # SuperExpression
    | literal                                                               # LiteralExpression
    | arrayLiteral                                                          # ArrayLiteralExpression
    | objectLiteral                                                         # ObjectLiteralExpression
    | '(' expressionSequence ')'                                            # ParenthesizedExpression
    ;

assignable
    : identifier
    | arrayLiteral
    | objectLiteral
    ;

objectLiteral
    : '{' (propertyAssignment (',' propertyAssignment)*)? ','? '}'
    ;
//
//anoymousFunction
//    : functionDeclaration                                                       # FunctionDecl
//    | Async? Function '*'? '(' formalParameterList? ')' '{' functionBody '}'    # AnoymousFunctionDecl
//    | Async? arrowFunctionParameters '=>' arrowFunctionBody                     # ArrowFunction
//    ;
//
//arrowFunctionParameters
//    : identifier
//    | '(' formalParameterList? ')'
//    ;
//
//arrowFunctionBody
//    : singleExpression
//    | '{' functionBody '}'
//    ;

assignmentOperator
    : '*='
    | '/='
    | '%='
    | '+='
    | '-='
    | '<<='
    | '>>='
    | '>>>='
    | '&='
    | '^='
    | '|='
    | '**='
    ;

literal
    : NullLiteral
    | BooleanLiteral
    | StringLiteral
    | TemplateStringLiteral
    | RegularExpressionLiteral
    | numericLiteral
    | bigintLiteral
    ;

numericLiteral
    : DecimalLiteral
    | HexIntegerLiteral
    | OctalIntegerLiteral
    | OctalIntegerLiteral2
    | BinaryIntegerLiteral
    ;

bigintLiteral
    : BigDecimalIntegerLiteral
    | BigHexIntegerLiteral
    | BigOctalIntegerLiteral
    | BigBinaryIntegerLiteral
    ;

getter
    : {this.n("get")}? identifier propertyName
    ;

setter
    : {this.n("set")}? identifier propertyName
    ;

identifierName
    : identifier
    | reservedWord
    ;

identifier
    : Identifier
    | NonStrictLet
    | Async
    ;

reservedWord
    : keyword
    | NullLiteral
    | BooleanLiteral
    ;

keyword
    : Break
    | Do
    | Instanceof
    | Typeof
    | Case
    | Else
    | New
    | Var
    | Catch
    | Finally
    | Return
    | Void
    | Continue
    | For
    | Switch
    | While
    | Debugger
    | Function
    | This
    | With
    | Default
    | If
    | Throw
    | Delete
    | In
    | Try
    | Then
    | Endif

    | Class
    | Enum
    | Extends
    | Super
    | Const
    | Export
    | Import
    | Implements
    | let
    | Private
    | Public
    | Interface
    | Package
    | Protected
    | Static
    | Yield
    | Async
    | Await
    | From
    | As
    ;

let
    : NonStrictLet
    | StrictLet
    ;

eos
    : SemiColon
    | EOF
    | {this.lineTerminatorAhead()}?
    | {this.closeBrace()}?
    ;
