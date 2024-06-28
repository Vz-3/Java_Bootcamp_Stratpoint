package com.Von.Service.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class NumericInputValidator<T extends Number> {
    private static final Logger logger = LoggerFactory.getLogger(NumericInputValidator.class);
    private final Scanner scn = new Scanner(System.in);
    private final T defaultMaxValue;

    public NumericInputValidator(T defaultMaxValue) {
        this.defaultMaxValue = defaultMaxValue;
    }

    public T validateInput(String prompt) {
        T value;
        boolean firstTime = true;

        System.out.printf("%s: ", prompt);
        do {
            try {
                value = parseInput(scn.nextLine());
                if (value.doubleValue() <= 0 || value.doubleValue() > defaultMaxValue.doubleValue()) {
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
                logger.error("Utils.NumericInputValidator error: ",e);
                System.err.print("Invalid input. Please enter a valid positive value:");
            }
        } while (true);
    }

    private T parseInput(String input) {
        // Implement parsing logic based on T (Integer, Double, etc.)
        // For simplicity, assuming Double for now
        return (T) Double.valueOf(input);
    }
}