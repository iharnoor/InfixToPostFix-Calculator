package Assignments;
/**
 * @author Harnoor Singh
 * Program name: calc.java
 * Due Date: Oct 31, 2017
 * <p>
 * Purpose of the program:
 * The program's purpose is to convert an Infix expression into a postfix expression and then to calculate the result.
 * Infix expression is taken as an input and result and the postfix expression is given back as output. This program
 * also helps the user to find the value of a variable 'x' if enters it.
 * <p>
 * Solution for problem and algorithms used:
 * The best solution to the problem has been to use stacks; once while converting infix to postfix and secondly while
 * evaluating the result. In the converter method (infixToPostFix() ) Stack is used to store operator while in
 * calculatePostFix() method it is used to store operands.
 * <p>
 * Data Structures used in this solution:
 * Stack has been used from the Java library.
 * <p>
 * Description of how to use the program and expected input/output:
 * The user is required to input the infix expression. If the expression doesn't contain 'x' variable then result and
 * converted expression will be printed immidiately, otherwise the user will be prompted to enter the value of x
 * variable. User can enter 'q' instead of value of the variable x to quit from the program. User can enter the
 * expression with or without spaces between operators and operands. User must not add more than one space between
 * operands.
 * <p>
 * Purpose of this class:
 * The driver class is main method class where methods from the calc class are called. The calc class
 * defines all the methods required for the functioning of the Calculator such as infixToPostFix and and calculatPostFix.
 */

import java.util.*;
import java.util.ArrayList;

/**
 * driver class with main method
 */
class driver {
    /**
     * Main Method
     * This method is used to call all the methods in the calc class.
     *
     * @param args
     */
    public static void main(String[] args) {
        calc cc = new calc();
        cc.startCalculator();
    }
}

/**
 * This is the class that defines the methods for the functioning of the calculator.
 */

class calc {
    /**
     * Method that calls the methods in this class in order to use this calculator.
     */
    void startCalculator() {
        System.out.println("Test expression (1234 + (3234 * 4432) - ((2243 * 5324) - (6243 % 5)) - 3)= " +
                (1234 + (3234 * 4432) - ((2243 * 5324) - (6243 % 5)) - 3));//use this expression to test
        String infix = userInputInfix();
        checkError(infix);
        infix = infix.replaceAll("\\s", "");// removing all spaces in the infix expression
        ArrayList<String> postFix = infixToPostFix(infix);
        String postFixStr = listToString(postFix);
        System.out.println("Converted expression: " + postFixStr);

        if (postFix.contains("x")) {
            while (true) {
                String input = userInputX();
                if (input.equals("q"))
                    System.exit(0);
                System.out.println("Answer to expression: " + calculatePostFix(postFixStr, input));
            }
        }
        System.out.println("Answer to expression: " + calculatePostFix(postFixStr, ""));
    }


    /**
     * Precondition : The String may or maynot contain spaces. Multiple operands are not allowed right now.
     * and do decimal numbers allowed
     *
     * @param expression Accepts a String infix expression
     * @return converts it postfix and returns it.
     */
    private ArrayList<String> infixToPostFix(String expression) {
        Stack<String> operator = new Stack<>();
        ArrayList<String> outputList = new ArrayList<>();
        for (int i = 0; i < expression.length(); i++) {
            String currentChar = expression.charAt(i) + "";
            if (isOperand(currentChar)) {
                numberLoop:
                while (i + 1 < expression.length()) {
                    String nextChar = expression.substring(i + 1, i + 2);
                    if (isOperand(nextChar)) {
                        currentChar += nextChar;
                        i++;
                    } else break numberLoop;
                }
                outputList.add(currentChar);
            } else if (isOperator(currentChar)) {
                if (operator.isEmpty())
                    operator.push(currentChar);
                else if (precedenceLevel(currentChar) > precedenceLevel(operator.peek())) {
                    operator.push(currentChar);
                } else if (precedenceLevel(currentChar) <= precedenceLevel(operator.peek())) {
                    while (!operator.isEmpty() && precedenceLevel(currentChar) <= precedenceLevel(operator.peek())) {
                        outputList.add(operator.pop());
                    }
                    operator.push(currentChar);
                }
            } else if (isLeftParentheses(currentChar)) {
                operator.push(currentChar);
                continue;
            } else if (isRightParentheses(currentChar)) {
                while (!operator.isEmpty() && !isLeftParentheses(operator.peek())) {
                    outputList.add(operator.pop());
                }
                operator.pop();
            }
            if (i == expression.length() - 1) {
                while (!operator.isEmpty()) {
                    outputList.add(operator.pop());
                }
            }
        }
        return outputList;
    }

    /**
     * Calculates the result of postfix expression.
     * Precondition: No decimal numbers are allowed.
     *
     * @param postFix   input String
     * @param userInput user Input if the user enters 'x' variable
     * @return integer result
     */
    private int calculatePostFix(String postFix, String userInput) {
        Stack<Integer> operand = new Stack<>();
        String[] postfixArr = postFix.split("\\s");
        for (String token : postfixArr) {
            if (token.equals("x"))
                operand.push(Integer.parseInt(userInput));
            else if (isNumber(token))
                operand.push(Integer.parseInt(token));
            else {
                calculateBinaryExp(operand, token);
            }
        }
        return operand.pop();
    }

    /**
     * To calculate the result of binary expression. Called in calculatePostFix() method
     *
     * @param operand Stack that gets modified
     * @param token   Tokens in the postfix expression
     */
    private static void calculateBinaryExp(Stack<Integer> operand, String token) {
        int right, left;
        try {
            right = operand.pop();
            left = operand.pop();


            switch (token) {
                case "+":
                    operand.push(left + right);
                    break;
                case "*":
                    operand.push(left * right);
                    break;
                case "-":
                    operand.push(left - right);
                    break;
                case "/":
                    if (right != 0)
                        operand.push(left / right);
                    else {
                        System.out.println("Division by zero Error");
                        System.exit(-1);
                    }
                    break;
                case "%":
                    if (right != 0)
                        operand.push(left % right);
                    else {
                        System.out.println("Division by zero Error");
                        System.exit(-1);
                    }
                    break;
            }
        } catch (EmptyStackException e) {
            System.out.println("Empty Stack Exception. Please add operator between every two operands");
            System.exit(-1);
        }
    }

    /**
     * Checks for errors. No spaces are inputted.
     * If there is error the program will stop.
     *
     * @param input Infix expression is passed as input.
     */
    private void checkError(String input) {
        String[] tokenArray;
        if (input.contains(" ")) {
            tokenArray = input.split(" ");
            for (int i = 0; i < tokenArray.length; i++)
                if (i < tokenArray.length - 1 && isNumber(tokenArray[i]) && isNumber(tokenArray[i + 1])) {
                    System.out.println("Please put operator between two operands");
                    System.exit(-1);
                }
        }
        input = input.replaceAll("\\s", "");
        int countOpen = 0;
        int countClosed = 0;
        for (int i = 0; i < input.length(); i++) {
            String currentChar = input.substring(i, i + 1);
            String previousChar = " ";
            if (i > 0)
                previousChar = input.substring(i - 1, i);
            if (currentChar.equals("("))
                countOpen++;
            if (currentChar.equals(")"))
                countClosed++;
            if (currentChar.equals(".")) {
                System.out.println("Error in expression!! Cannot accept floating point numbers.");
                System.exit(-1);
            } else if (isOperator(currentChar) && isOperator(previousChar)) {
                System.out.printf("Error in expression!! The %s operator cannot be preceded by a %s operator.\n", currentChar, previousChar);
                System.exit(-1);
            } else if (isLeftParentheses(previousChar) && isOperator(currentChar)) {
                System.out.println("Error in expression!! No operator between operand and left parentheses.");
                System.exit(-1);
            } else if (isRightParentheses(currentChar) && isOperator(previousChar)) {
                System.out.println("Error in expression!! No operator between operand and right parentheses.");
                System.exit(-1);
            } else if (i == input.length() - 1) {
                if (countClosed > countOpen) {
                    System.out.println("Error in expression!! No matching right parentheses for a right parentheses.");
                    System.exit(-1);
                } else if (countClosed < countOpen) {
                    System.out.println("Error in expression!! No matching left parentheses for a right parentheses.");
                    System.exit(-1);
                }
            }

        }
    }

    /**
     * To take user input for the infix expression
     *
     * @return The string infix expression.
     */
    private String userInputInfix() {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter infix expression:");
        String exp = scan.nextLine();
        System.out.println();
        return exp;
    }

    /**
     * Takes user input for the variable x if entered in the expression.
     *
     * @return : the string inputted for the value of x
     */
    private String userInputX() {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter value of x: ");
        String input = scan.next().trim();
        System.out.println();
        return input;
    }

    /**
     * checks if the character is operator or not.
     *
     * @param operator character passed
     * @return boolean
     */
    private boolean isOperator(String operator) {
        char op = operator.charAt(0);
        return operator.length() == 1 && op == '*' || op == '-' || op == '/' || op == '+' || op == '%';
    }

    /**
     * Returns the precedence level of the operator.
     *
     * @param operator Operator
     * @return precedence level (int)
     */
    private int precedenceLevel(String operator) {
        char op = operator.charAt(0);
        return (op == '*' || op == '%' || op == '/') ? 2 : (op == '+' || op == '-') ? 1 : -1;
    }

    /**
     * Checks if the character is left parentheses or not
     *
     * @param s character
     * @return boolean
     */
    private boolean isLeftParentheses(String s) {
        return s.length() == 1 && (s.charAt(0) == '(');
    }

    /**
     * Checks if the character is right parentheses or not
     *
     * @param s Assumes that string is of one character.
     * @return boolean
     */
    private boolean isRightParentheses(String s) {
        return s.length() == 1 && (s.charAt(0) == ')');
    }

    /**
     * checks if the character is operand or not.
     *
     * @param op Assumes that string is of one character.
     * @return boolean
     */
    private boolean isOperand(String op) {
        char operand = op.charAt(0);
        return operand >= '0' && operand <= '9' || operand == 'x' && op.length() == 1;
    }

    /**
     * Converts an ArrayList to a String.
     *
     * @param list ArrayList
     * @return String
     */
    private String listToString(ArrayList<String> list) {
        String str = list.toString().replaceAll(",", "");
        return str.substring(1, str.length() - 1);
    }

    /**
     * To check if the string is numeric or not.
     *
     * @param s String
     * @return true if number else false
     */
    private boolean isNumber(String s) {
        return s != null && s.matches("[-+]?\\d+");
    }
}
