package org.example;

import java.util.ArrayList;
import java.util.HashMap;

public class Calculator {
    protected String appName = "My Calculator App";
    protected String closeKeyword = "quit";
    protected String expression = "";
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
                    System.out.println("= "+evaluateExpression());
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
            int ctr = 0;
            while (operations.contains('^') || ctr > ctrLimit) {
                int operationIndex = operations.indexOf('^');

                // Calculate answer and update values and operations.
                double answer = Math.pow(values.get(operationIndex), values.get(operationIndex+1));
                values.set(operationIndex, answer);
                values.remove(operationIndex+1);

                operations.remove(operationIndex);

                ctr++;
            }
        }
        else if (operations.contains('*') || operations.contains('/')) {
            for (int i=0, k=operations.size(); i<k;i++) {
                boolean flag = false;
                double answer = 0;
                if (operations.get(i)=='*') {
                    flag = true;
                    answer = values.get(i) * values.get(i+1);
                }
                else if (operations.get(i)=='/'){
                    flag = true;
                    answer = values.get(i) / values.get(i+1);
                }

                if (flag) {
                    values.set(i, answer);
                    values.remove(i+1);

                    operations.remove(i);
                    k--;
                }
            }
        }
        else {
            int ctr1 = 0;
            while (!operations.isEmpty() || ctr1 > ctrLimit) {
                double answer = operations.get(0)=='+' ? values.get(0) + values.get(1) : values.get(0) - values.get(1);
                values.set(0, answer);
                values.remove(1);

                operations.remove(0);
                ctr1++;
            }
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

    void reset() {
        expression = "";
        closeKeyword = "quit";
        values.clear();
        operations.clear();
    }

    public String getAppName() {
        return appName;
    }

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
}

