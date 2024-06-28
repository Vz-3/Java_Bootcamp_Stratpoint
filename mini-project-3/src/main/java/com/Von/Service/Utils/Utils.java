package com.Von.Service.Utils;

import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utils {
    private static final Scanner scn = new Scanner(System.in);
    private static final Integer defaultMinLength = 4;
    private static final Integer defaultMaxLength = 20;
    private static final Integer defaultMaxPrice = 9_999_999;
    private static final Logger logger = LoggerFactory.getLogger(Utils.class);

    private Utils() {}

    private static String validator(String fieldName, Integer minLength, Integer maxLength) {
        String validString;
        Boolean firstTime = true;
        final Pattern invalidChars = Pattern.compile("[^a-zA-z0-9.,()&\\s]");

        System.out.printf("%s:", fieldName);
        do {
            try {

                validString = scn.nextLine();
                Matcher matcher = invalidChars.matcher(validString);

                if (validString.isBlank()
                        || (validString.trim().length() < minLength || validString.trim().length() > maxLength)
                        || matcher.find()) {
                    if (firstTime) {
                        System.err.printf("""
                                ===== Invalid input =====
                                Either:
                                \t Blank input
                                \t Less than %d characters
                                \t More than %d characters
                                \t Invalid character/symbol
                                Please try again:""", minLength, maxLength);
                        firstTime = false;
                    }
                    else
                        System.out.print("Title:");
                }
                else
                    return validString;
            } catch (NoSuchElementException e) {
                logger.error("Utils.validator error: ",e);
                validString = ""; // Reset the input.
            }
        } while (true);
    }

    public static String validateStringInput(String fieldName) {
        return validator(fieldName, defaultMinLength, defaultMaxLength);
    }

    public static String validateStringInput(String fieldName, Integer maxLength) {
        return validator(fieldName, defaultMinLength, maxLength);
    }

    public static String validateStringInput(String fieldName, Integer minLength, Integer maxLength) {
        return validator(fieldName, minLength, maxLength);
    }
}
