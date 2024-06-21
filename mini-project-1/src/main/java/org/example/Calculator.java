package org.example;

import java.util.ArrayList;
import java.util.HashMap;

public class Calculator {
    protected String appName = "My Calculator App";
    protected String closeKeyword = "quit";
    protected String expression = "";
    String answer = "";

    private final ArrayList<Double> values = new ArrayList<>();
    private final ArrayList<Character> operations = new ArrayList<>();

    protected HashMap<Character, Integer> numberMap = new HashMap<>();
    protected HashMap<Character, Integer> symbolMap = new HashMap<>();


    enum atomType {
        NUMBER,
        DOT,
        OPERATOR,
        LEFT_PARENTHESIS,
        RIGHT_PARENTHESIS
    }

    public Calculator() {
        initializeHashMap();
    }

    public Calculator(String newAppName) {
        this.appName = newAppName;
        initializeHashMap();
    }

    public Calculator(String newAppName, String newCloseKeyword) {
        this.appName = newAppName;
        initializeHashMap();
        setCloseKeyword(newCloseKeyword);
        System.out.println("New close keyword is "+getCloseKeyword());
    }

    public void calculate() {
        if (!expression.isEmpty()) {
            if (checkExpression())
                if (tokenizeExpression())
                    setAnswer(String.valueOf(evaluateExpression()));
            reset();
        }
    }

    boolean checkExpression() {
        // Identifies if there is an unrecognized character before proceeding to check the logic of the expression
        if (expression.toLowerCase().equals(getCloseKeyword())) {
            return false;
        }

        char[] expr = expression.toCharArray();
        for (int i = 0; i < expression.length(); i++) {
            if (!(numberMap.containsKey(expr[i]) || symbolMap.containsKey(expr[i]))) {
                reportError("Invalid expression", expr[i]+" is not recognized.");
                return false;
            }
        }
        return true;
    }

    atomType checkAtomType(char atom) {
        if (atom == '.') {
            return atomType.DOT;
        }
        else if (symbolMap.containsKey(atom)) {
            return atomType.OPERATOR;
        }
        else
            return atomType.NUMBER;
    }

    private boolean tokenizeExpression() {
        // Tokenize the expression into atoms and assess the logic (EMDAS and semantics ).
        boolean dotIsUsed = false; // resets whenever the tokenizer encounters a symbol.
        atomType currentType;
        StringBuilder valueToBeAppended = new StringBuilder();
        String errorType = "Parser";

        char[] expr = expression.toCharArray();
        atomType previousType = checkAtomType(expr[0]);

        if (previousType == atomType.OPERATOR) {
            reportError(errorType, "missing expression before '"+expr[0]+"'.");
            return false;
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
                    reportError(errorType, "Redundant symbol.");
                    return false;
                } else
                    valueToBeAppended.append(expr[i]);
            } else {
                if (currentType == atomType.OPERATOR) {
                    /*
                    Given that the expression can't start with an operator or dot, the change of type into operator
                    indicates that a number existed before it, ergo append the value.
                    */
                    values.add(Double.parseDouble(String.valueOf(valueToBeAppended)));
                    // dotIsUsed is set to false again
                    dotIsUsed = false;
                    operations.add(expr[i]);
                } else if (currentType == atomType.DOT) {
                    if (dotIsUsed && previousType == atomType.NUMBER) {
                        reportError(errorType, "Invalid use of '.' symbol.");
                        return false;
                    }
                    valueToBeAppended.append('.');
                    dotIsUsed = true;
                } else {
                    // Reset so that a new value will be appended.
                    if (previousType == atomType.OPERATOR) {
                        valueToBeAppended.setLength(0);
                        valueToBeAppended.append(expr[i]);
                    }
                    else
                        valueToBeAppended.append(expr[i]);
                }
                previousType = currentType;
            }
        }

        // if last atom is an operator, else append the value.
        if (previousType == atomType.OPERATOR) {
            reportError(errorType, "missing expression after '"+expr[expression.length()-1]+"'.");
            return false;
        }
        else {
            values.add(Double.parseDouble(String.valueOf(valueToBeAppended)));
        }

        // debugging
//        System.out.println("Values "+values);
//        System.out.println("Operators "+operations);

        return true;
    }

    private double evaluateExpression() {
        int ctrLimit = 10000;

        if (values.size() == 1) {
            return values.get(0);
        }
        else if (operations.contains('^')) {
            int operationIndex = operations.lastIndexOf('^'); // always find the first instance

            double answer = Math.pow(values.get(operationIndex), values.get(operationIndex+1)); // Right to left associativity!
            values.set(operationIndex, answer);
            values.remove(operationIndex+1);

            operations.remove(operationIndex);
        }
        // left associativity for the rest...
        else if (operations.contains('*') || operations.contains('/')) {
            double answer = 0;
            int multiplyIndex = operations.indexOf('*');
            int divisionIndex = operations.indexOf('/');
            int index = -1;

            if (multiplyIndex != -1 && divisionIndex != -1) {
                answer = multiplyIndex < divisionIndex ? values.get(multiplyIndex) * values.get(multiplyIndex+1) : values.get(divisionIndex) / values.get(divisionIndex+1);
                index = Math.min(multiplyIndex, divisionIndex);
            }
            else if (multiplyIndex == -1) {
                answer = values.get(divisionIndex) / values.get(divisionIndex + 1);
                index = divisionIndex;
            }
            else {
                answer = values.get(multiplyIndex) * values.get(multiplyIndex + 1);
                index = multiplyIndex;
            }

            if (index == -1) {
                throw new RuntimeException("invalid index!");
            }

            values.set(index, answer);
            values.remove(index+1);
            operations.remove(index);
        }
        else {
            double answer = operations.get(0)=='+' ? values.get(0) + values.get(1) : values.get(0) - values.get(1);
            values.set(0, answer);
            values.remove(1);

            operations.remove(0);
        }
        return evaluateExpression();
    }

    protected void reportError(String errorType, String errorMessage) {
        System.out.println("Error type:"+errorType+" - "+errorMessage);
        System.out.println("Expression: "+getExpression());
    }

    // Getters & Setters
    public String getCloseKeyword() {
        return closeKeyword;
    }

    protected void setCloseKeyword(String newCloseKeyword) {
        this.closeKeyword = newCloseKeyword;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String newExpression) {
        this.expression = newExpression;
    }

    public String getAnswer() {return answer;}

    protected void setAnswer(String newAnswer) {this.answer = newAnswer; }
    // Misc.
    void initializeHashMap() {
        numberMap.put('0', null);
        numberMap.put('1', null);
        numberMap.put('2', null);
        numberMap.put('3', null);
        numberMap.put('4', null);
        numberMap.put('5', null);
        numberMap.put('6', null);
        numberMap.put('7', null);
        numberMap.put('8', null);
        numberMap.put('9', null);

        symbolMap.put('.', null);
        symbolMap.put('+', null);
        symbolMap.put('-', null);
        symbolMap.put('*', null);
        symbolMap.put('/', null);
        symbolMap.put('^', null);
//        symbolMap.put('(', null);
//        symbolMap.put(')', null);
    }

    void reset() {
        expression = "";
        closeKeyword = "quit";
        values.clear();
        operations.clear();
    }

    public String getAppName() {
        return appName;
    }

}

