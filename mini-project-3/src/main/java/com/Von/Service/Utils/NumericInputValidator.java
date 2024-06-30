package com.Von.Service.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;
import java.util.Scanner;

/**
 * A utility class for validating numeric input (of type <code>BigDecimal</code>).
 *
 * @param <T> The type of numeric value (e.g., <code>BigDecimal</code>).
 */
public class NumericInputValidator<T extends BigDecimal> {
    private static final Logger logger = LoggerFactory.getLogger(NumericInputValidator.class);
    private final Scanner scn = new Scanner(System.in);
    private final T defaultMaxValue;

    /**
     * Constructs a new <code>NumericInputValidator</code> with the specified default maximum value.
     *
     * @param defaultMaxValue The maximum allowed value for input validation.
     */
    public NumericInputValidator(T defaultMaxValue) {
        this.defaultMaxValue = defaultMaxValue;
    }

    /**
     * Validates user input for a numeric value.
     *
     * @param prompt The prompt to display when requesting input.
     * @return The valid numeric value.
     */
    public T validateInput(String prompt) {
        T value;
        boolean firstTime = true;

        System.out.printf("%s: ", prompt);
        do {
            try {
                value = parseInput(scn.nextLine());
                if (value.compareTo(BigDecimal.ZERO) <= 0 || value.compareTo(defaultMaxValue) > 0) {
                    if (firstTime) {
                        System.err.printf("Value must be greater than zero but cannot exceed the max limit of %s. %nPlease try again:", defaultMaxValue);
                        firstTime = false;
                    } else {
                        System.out.printf("%s:", prompt);
                    }
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                logger.error("Utils.NumericInputValidator error: ", e);
                System.err.print("Invalid input. Please enter a valid positive value:");
            }
        } while (true);
    }

    /**
     * Parses the input string into a numeric value.
     *
     * @param input The input string to parse.
     * @return The parsed numeric value.
     */
    @SuppressWarnings("unchecked")
    private T parseInput(String input) {
        // Parse input into BigDecimal
        return (T) new BigDecimal(input);
    }
}
