# InfixToPostFix-Calculator
Converts infix expression into postfix while handling errors for invalid expressions.
 
## Program name: calc.java
  The program's purpose is to convert an Infix expression into a postfix expression and then to calculate the result.
  Infix expression is taken as an input and result and the postfix expression is given back as output. This program
  also helps the user to find the value of a variable 'x' if enters it.
 
## Solution for problem and algorithms used:
  The best solution to the problem has been to use stacks; once while converting infix to postfix and secondly while
  evaluating the result. In the converter method (infixToPostFix() ) Stack is used to store operator while in
  calculatePostFix() method it is used to store operands.

## Data Structures used in this solution:
  Stack has been used from the Java library.
 
## Description of how to use the program and expected input/output:
  The user is required to input the infix expression. If the expression doesn't contain 'x' variable then result and
  converted expression will be printed immidiately, otherwise the user will be prompted to enter the value of x
  variable. User can enter 'q' instead of value of the variable x to quit from the program. User can enter the
  expression with or without spaces between operators and operands. User must not add more than one space between
  operands.
