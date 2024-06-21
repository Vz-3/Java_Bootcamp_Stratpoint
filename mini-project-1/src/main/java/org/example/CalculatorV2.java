package org.example;

import java.util.ArrayList;
import java.util.Stack;

import static java.lang.Double.NaN;

public class CalculatorV2 extends Calculator {

    private final ArrayList<String> tokens = new ArrayList<>();
    private ArrayList<String> reversePolishNotation = new ArrayList<>();

    public CalculatorV2() {
    }

    public CalculatorV2(String newAppName) {
        super(newAppName);
    }

    public CalculatorV2(String newAppName, String newCloseKeyword) {
        super(newAppName, newCloseKeyword);
    }

    @Override
    public void calculate() {
        if (!expression.isEmpty()) {
            if (checkExpression())
                if (tokenizeExpression())
                    if (shuntingYard())
                       setAnswer(String.valueOf(evaluateExpression()));
            reset();
        }
    }

    @Override
    void reset() {
        super.reset();
        tokens.clear();
        reversePolishNotation.clear();
    }

    private static int getPrecedence(char ch) {
        if (ch == '+' || ch == '-')
            return 1;
        else if (ch == '*' || ch == '/')
            return 2;
        else if (ch == '^')
            return 3;
        else
            return -1;
    }

    private boolean tokenizeExpression() {
        // Tokenize the expression into atoms and assess the logic.
        boolean dotIsUsed = false; // resets whenever the tokenizer encounters an operator.
        atomType currentType;
        StringBuilder valueToBeAppended = new StringBuilder();
        String errorType = "Parser";

        char[] expr = expression.toCharArray();
        atomType previousType = checkAtomType(expr[0]);

        if (previousType == atomType.OPERATOR) {
            reportError(errorType, "missing expression before '"+expr[0]+"'.");
            return false;
        }
        else if (previousType == atomType.RIGHT_PARENTHESIS) {
            reportError(errorType, " mismatched parenthesis.");
        }
        else if (previousType == atomType.LEFT_PARENTHESIS) {
            tokens.add(String.valueOf(expr[0]));
        }
        else {
            valueToBeAppended.append(expr[0]);
            if (previousType == atomType.DOT)
                dotIsUsed = true;
        }

        for (int i = 1; i<expression.length();i++) {
            currentType = checkAtomType(expr[i]);

            // This is only accessible if the previous type is a number, which it should always be.
            if (previousType == currentType) {
                if (currentType == atomType.OPERATOR || currentType == atomType.DOT) {
                    reportError(errorType, "redundant symbol.");
                    return false;
                }
                else if (currentType == atomType.LEFT_PARENTHESIS || currentType == atomType.RIGHT_PARENTHESIS)
                    tokens.add(String.valueOf(expr[i]));
                else
                    valueToBeAppended.append(expr[i]); // Only number at this point.
            }
            else {
                if (currentType == atomType.OPERATOR) {
                    /*
                    Given that the expression can't start with an operator or dot, the change of type into operator
                    indicates that a number existed before it, ergo append the value.
                    */
                    if (previousType == atomType.LEFT_PARENTHESIS) {
                        reportError(errorType, "missing expression before "+expr[i]);
                        return false;
                    }
                    if (previousType == atomType.NUMBER)
                        tokens.add(String.valueOf(valueToBeAppended));

                    // dotIsUsed is set to false again
                    dotIsUsed = false;
                    tokens.add(String.valueOf(expr[i]));
                }
                else if (currentType == atomType.LEFT_PARENTHESIS) {
                    if (previousType == atomType.NUMBER || previousType == atomType.DOT) {
                        reportError(errorType, "missing operator before "+expr[i]);
                        return false;
                    }
                    else if (previousType == atomType.RIGHT_PARENTHESIS) {
                        reportError(errorType, "missing operator before "+expr[i]);
                        return false;
                    }
                    tokens.add(String.valueOf(expr[i]));
                }
                else if (currentType == atomType.RIGHT_PARENTHESIS) {
                    if (previousType == atomType.OPERATOR || previousType == atomType.LEFT_PARENTHESIS || previousType == atomType.DOT) {
                        reportError(errorType, "missing expression before "+expr[i]);
                        return false;
                    }
                    tokens.add(String.valueOf(valueToBeAppended));
                    tokens.add(String.valueOf(expr[i]));
                }
                else if (currentType == atomType.DOT) {
                    if (dotIsUsed && previousType == atomType.NUMBER) {
                        reportError(errorType, "invalid use of '.' symbol.");
                        return false;
                    }
                    else if (previousType == atomType.RIGHT_PARENTHESIS) {
                        reportError(errorType, "missing operator before "+expr[i]);
                        return false;
                    }
                    valueToBeAppended.append('.');
                    dotIsUsed = true;
                }
                else {
                    // Reset so that a new value will be appended.
                    if (previousType == atomType.OPERATOR || previousType == atomType.LEFT_PARENTHESIS) {
                        valueToBeAppended.setLength(0);
                        valueToBeAppended.append(expr[i]);
                    }
                    else if (previousType == atomType.RIGHT_PARENTHESIS) {
                        reportError(errorType, "missing operator before "+expr[i]);
                        return false;
                    }
                    else
                        valueToBeAppended.append(expr[i]);
                }
                previousType = currentType;
            }
        }
        if (previousType == atomType.NUMBER) {
            tokens.add(String.valueOf(valueToBeAppended));
        }
        // Debugging
        // System.out.println("Tokens: "+tokens);
        return true;
    }

    private boolean shuntingYard() {
        Stack<Character> operatorStack = new Stack<>();
        ArrayList<String> outputQueue = new ArrayList<>();
        String errorType = "Shunting Yard Algorithm";

        for (String token : tokens) {
            if (checkIfNumber(token))
                outputQueue.add(token);
            else if (checkAtomType(token.charAt(0)) == atomType.LEFT_PARENTHESIS)
                operatorStack.push(token.charAt(0));
            else if (checkAtomType(token.charAt(0)) == atomType.RIGHT_PARENTHESIS) {
                while (!operatorStack.isEmpty() && operatorStack.peek() != '(' ) {
                    outputQueue.add(String.valueOf(operatorStack.pop()));
                }
                // Asserts that ( exists
                operatorStack.pop(); // flush the left parenthesis into the void.

            } else {
                while ((!operatorStack.isEmpty() && (operatorStack.peek() != '(')) //condition 1
                        &&
                        (
                                (getPrecedence(operatorStack.peek()) > getPrecedence(token.charAt(0)))
                                        ||
                                        (getPrecedence(operatorStack.peek()) == getPrecedence(token.charAt(0))
                                                && hasLeftAssociativity(token.charAt(0)))
                        )
                ) {
                    outputQueue.add(String.valueOf(operatorStack.pop()));
                }
                operatorStack.push(token.charAt(0));
            }
        }

        while (!operatorStack.isEmpty()) {
            if (operatorStack.peek() == '(' || operatorStack.peek() == ')') {
                reportError(errorType, "mismatched parenthesis.");
                return false;
            }
            outputQueue.add(String.valueOf(operatorStack.pop()));
        }

        setRPN(outputQueue);

        //System.out.println("operator stack: "+operatorStack+" | outputQueue: "+outputQueue);
        return true;
    }

    private double evaluateExpression() {
        Stack<String> rpnStack = new Stack<>();
        int ctr = 0;
        int ctrLimit = 10000;

        while (!reversePolishNotation.isEmpty() || ctrLimit < ctr) {
            String element = reversePolishNotation.get(0);

            if (checkIfNumber(element)) {
                rpnStack.push(element);
            }
            else {
                Double opr2 = Double.valueOf(rpnStack.pop());
                Double opr1 = Double.valueOf(rpnStack.pop());
                Double answer = switch (element) {
                    case "+" -> opr1 + opr2;
                    case "-" -> opr1 - opr2;
                    case "*" -> opr1 * opr2;
                    case "/" -> opr1 / opr2;
                    default -> getExponentResult(opr1, opr2);
                };
                rpnStack.push(answer.toString());
            }
            reversePolishNotation.remove(0);
            ctr++;
        }
        return Double.parseDouble(rpnStack.pop());
    }

    private double getExponentResult(Double base, Double exp) {
        double res = 1;

        if (base == NaN || exp == NaN)
            return NaN;

        if (base == 0)
            return 0;

        if (exp == 0)
            return res;
        else
            return base * getExponentResult(base, exp - 1);

    }

    private boolean checkIfNumber(String element) {
        return element.matches("-?\\d+(\\.\\d+)?"); // technically catches negative number but the tokenizer doesn't.
    }

    private boolean hasLeftAssociativity(char ch) {
        return ch != '^';
    }

    @Override
    atomType checkAtomType(char atom) {
        if (atom == '(') {
            return atomType.LEFT_PARENTHESIS;
        }
        else if (atom == ')') {
            return atomType.RIGHT_PARENTHESIS;
        }
        else if (atom == '.') {
            return atomType.DOT;
        }
        else if (symbolMap.containsKey(atom)) {
            return atomType.OPERATOR;
        }
        else
            return atomType.NUMBER;
    }

    @Override
    void initializeHashMap() {
        super.initializeHashMap();
        symbolMap.put('(', null);
        symbolMap.put(')', null);
    }

    // Getters & setters
    protected ArrayList<String> getRPN() {
        return this.reversePolishNotation;
    }

    private void setRPN(ArrayList<String> rpn) {
        this.reversePolishNotation = rpn;
    }

}
