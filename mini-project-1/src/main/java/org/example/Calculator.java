package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 *
 * Initial implementation of a Command-line Interface (CLI) calculator that takes in basic expressions.
 *
 * <p> Given that the approach to the project is similar to a parser, a symbol {@code negativeOperator} is
 * assigned by default to character 'n' to reference the use of a negative value to distinguish it
 * from the minus symbol.</p>
 *
 * <p>For example, this code allows the user to perform the four basic mathematical operations:
 * <blockquote><pre>{@code
 *     > 100 + 2 * 3 / 5 - 27.30
 *     = 73.9}
 * </pre></blockquote>
 *
 * <p> Additionally, it is able to perform exponential operation:</p>
 * <blockquote><pre>{@code
 *     > 3 ^ 2 ^ 4
 *     = 43046721}
 *</pre></blockquote>
 *
 * <p> Lastly, this serves as an example on using a negative value:
 * <blockquote><pre>{@code
 *      >n100 + n.25
 *      = -100.25}
 *</pre></blockquote></p>
 * <p>As it is treated similar to a parser, the code manually
 * handles the common errors that would render an expression to
 * be syntactically incorrect.</p>
 *
 * <p>To use the class, the method {@link #setExpression(String) setExpression}{@code (String foo)} has to be
 * called first to store the user's input. Afterwards, call {@link #calculate() calculate} to perform the entire
 * process, which is broken down into three sub-processes, namely, {@link #checkExpression() checkExpression},
 * {@link #tokenizeExpression() tokenizeExpression}, and {@link #evaluateExpression() evaluateExpression}. Lastly,
 * use the getter function {@link #getAnswer() getAnswer} to retrieve the result. If an error is encountered, it
 * will be reported and the result of {@code getAnswer()} will be empty.</p>
 *
 * <p>The other methods not covered are helpers or companion methods for {@link #calculate()} or its parts,
 * otherwise, it is for the class constructor. </p>
 */
public class Calculator {
    protected String appName = "My Calculator App";
    protected String closeKeyword = "quit"; //not good for main as it depends on this variable to exit out of a loop
    protected String expression = "";

    private String answer = "";

    /**
     * Stores the list of numerical values after {@link #tokenizeExpression()}.
     */
    private final ArrayList<Double> values = new ArrayList<>();
    /**
     * Stores the list of operations to be performed after  {@link #tokenizeExpression()}.
     */
    private final ArrayList<Character> operations = new ArrayList<>();

    /**
     * Stores the list of recognized characters that falls under numerical values.
     */
    protected HashMap<Character, Integer> numberMap = new HashMap<>();
    /**
     * Stores the list of recognized characters that falls under operation symbols.
     */
    protected HashMap<Character, Integer> symbolMap = new HashMap<>();

    /**
     * The symbol to be substituted for the unary operator {@code -}.
     */
    protected char negativeOperator = 'n'; //technically, the unary operator - is not an operator, but a function, higher precedence. not 1:1 ratio for v2 since it can't be used in -(x + y) scenario.

    /**
     * List of token types for {@code Calculator} and {@code CalculatorV2} classes.
     */
    enum atomType {
        NUMBER,
        DOT,
        OPERATOR,
        LEFT_PARENTHESIS,
        RIGHT_PARENTHESIS
    }

    // Constructors
    /**
     Default constructor. Constructs a new {@code Calculator} that
     initializes the list of recognized symbols for numerical values
     and operations.
     */
    public Calculator() {
        initializeHashMap();
    }

    /**
     * Constructs a new {@code Calculator} that takes in
     * a string to change the variable {@link #appName}.
     * @param newAppName variable when using {@link #getAppName()}.
     */
    public Calculator(String newAppName) {
        this.appName = newAppName;
        initializeHashMap();
    }

    /**
     * Constructs a new {@code Calculator} that takes in two strings,
     * the first for changing the variable {@link #appName} and the
     * second for changing {@link #closeKeyword}.
     * @param newAppName variable when using {@link #getAppName()}.
     * @param newCloseKeyword variable when using {@link #getCloseKeyword()}.
     */
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
                else
                    setAnswer(""); // clears answer.
        }
        reset();
    }

    /**
     * Checks if the expression contains the {@link #closeKeyword} or unrecognized character.
     * @return {@code true} if the variable {@link #expression} is valid for tokenizing.
     */
    boolean checkExpression() {
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

    /**
     * Helper class for {@link #tokenizeExpression()}.
     * @param atom a character that is an operation symbol or a part of a number.
     * @return the corresponding type of the character.
     */
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

    /**
     * Checks if the expression's syntax is correct, otherwise, reports the first error.
     * @return {@code true} if the {@link #expression} is syntactically correct.
     */
    private boolean tokenizeExpression() {
        // Tokenize the expression into atoms and assess the logic (EMDAS and semantics ).
        boolean dotIsUsed = false; // resets whenever the tokenizer encounters a symbol.
        boolean negativeIsUsed = false;
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
            else if (previousType == atomType.NUMBER && expr[0] == getNegativeOperator()) {
                negativeIsUsed = true;
                valueToBeAppended.replace(0,1,"-");
            }
        }

        for (int i = 1; i<expression.length();i++) {
            char token = expr[i];
            currentType = checkAtomType(token);

            if (previousType == currentType) {
                if (currentType == atomType.OPERATOR || currentType == atomType.DOT || token == getNegativeOperator()) {
                    reportError(errorType, "redundant symbol.");
                    return false;
                } else
                    valueToBeAppended.append(token);
            } else {
                if (token == getNegativeOperator()) {
                    if (negativeIsUsed ||  dotIsUsed || previousType == atomType.NUMBER) {
                        reportError(errorType, " Redundant / Invalid use of unary operator.");
                        return false;
                    }

                    valueToBeAppended.setLength(0);
                    negativeIsUsed = true;
                    valueToBeAppended.append('-');
                }
                else if (currentType == atomType.OPERATOR) {
                    /*
                    Given that the expression can't start with an operator or dot, the change of type into operator
                    indicates that a number/negative existed before it, ergo append the value.
                    */
                    if (previousType == atomType.DOT) {
                        reportError(errorType, "invalid use of '.'");
                        return false;
                    }
                    if (expr[i-1] == getNegativeOperator()) {
                        reportError(errorType, "invalid use of unary operator '-'");
                        return false;
                    }
                    values.add(Double.parseDouble(String.valueOf(valueToBeAppended)));
                    // dot & negative flag are set to false again
                    dotIsUsed = false;
                    negativeIsUsed = false;
                    operations.add(token);

                    valueToBeAppended.setLength(0);
                } else if (currentType == atomType.DOT) {
                    if (dotIsUsed && previousType == atomType.NUMBER) {
                        reportError(errorType, "Invalid use of '.' symbol.");
                        return false;
                    }
                    valueToBeAppended.append('.');
                    dotIsUsed = true;
                } else {
                    valueToBeAppended.append(token);
                }
                previousType = currentType;
            }
        }

        // if last atom is an operator, else append the value.
        if (previousType == atomType.OPERATOR || expr[expression.length()-1] == getNegativeOperator()) {
            reportError(errorType, "missing expression after '"+expr[expression.length()-1]+"'.");
            return false;
        }
        else {
            if (Objects.equals(String.valueOf(valueToBeAppended), ".")) {
                reportError(errorType, "missing expression after '"+expr[expression.length()-1]+"'.");
                return false;
            }
            values.add(Double.parseDouble(String.valueOf(valueToBeAppended)));
        }

        // debugging
//       System.out.println("Values: "+values);
//       System.out.println("Operations: "+operations);

        return true;
    }

    /**
     * Evaluates the {@link #values} and {@link #operations}, performing the mathematical operations
     * and setting the result of the computation.
     * @return a {@code double} data type of the answer.
     */
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

    /**
     * A crude implementation of common error handling for the calculator.
     * @param errorType category of error.
     * @param errorMessage specific error message for debugging.
     */
    protected void reportError(String errorType, String errorMessage) {
        System.out.println("Error type:"+errorType+" - "+errorMessage);
        System.out.println("Expression: "+getExpression());
    }

    // Getters & Setters

    /**
     * Getter for {@link #appName}.
     * @return {@code String}
     */
    public String getAppName() {
        return appName;
    }

    /**
     * Getter for the {@link #closeKeyword}.
     * @return {@code String}
     */
    public String getCloseKeyword() {
        return closeKeyword;
    }

    /**
     * Setter for the {@link #closeKeyword}.
     * @param newCloseKeyword  {@code String}
     */
    protected void setCloseKeyword(String newCloseKeyword) {
        this.closeKeyword = newCloseKeyword;
    }

    /**
     * Getter for the {@link #expression}.
     * @return {@code String}
     */
    public String getExpression() {
        return expression;
    }

    /**
     * Setter for the {@link #expression}
     * @param newExpression = {@code String}
     */
    public void setExpression(String newExpression) {
        this.expression = newExpression;
    }

    /**
     * Getter for the {@link #answer}
     * @return {@code String}
     */
    public String getAnswer() {return answer;}

    /**
     * Setter for the {@link #answer}
     * @param newAnswer {@code String}
     */
    protected void setAnswer(String newAnswer) {this.answer = newAnswer; }

    /**
     * Getter for the {@link #negativeOperator}
     * @return {@code char}
     */
    public char getNegativeOperator() { return negativeOperator;}

    /**
     * Setter for the {@link #negativeOperator}
     * @param newNegativeOperator {@code char}
     */
    public void setNegativeOperator(char newNegativeOperator) {this.negativeOperator = newNegativeOperator;}
    // Misc.

    /**
     * Initializes a hash map of recognized symbols for {@link #checkExpression()}.
     */
    void initializeHashMap() {
        numberMap.put(getNegativeOperator(), null); // will serve as the character for the replacement of unary operator '-', might add option to assign what symbol in the future.
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
    }

    /**
     * Resets the local variables: {@link #expression}, {@link #closeKeyword}, {@link #values}, & {@link #operations}.
     */
    void reset() {
        expression = "";
        closeKeyword = "quit";
        values.clear();
        operations.clear();
    }

}

