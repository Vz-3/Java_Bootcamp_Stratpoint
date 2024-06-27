package com.Von.Services.Impl;

import com.Von.Services.BookService;
import org.apache.commons.validator.routines.ISBNValidator;

import java.util.InputMismatchException;
import java.util.Scanner;

public class BookServiceImpl implements BookService {
    private static final Scanner scn = new Scanner(System.in);

    private BookServiceImpl() {
    }

    /**
     * Method for validating the string inputs when adding
     * a book, looping until a string is not blank, length is at least 4 characters,
     * and does not contain the separator ';' character as it should be reserved for
     * multiple elements.
     * @return <code>String</code> that is validated.
     */
    public static String validateString(String attributeName) {
        String input;
        do {
            try {
                System.out.println("Enter "+attributeName+":");
                input = scn.nextLine();
            } catch (Exception e) {
                System.err.println("Error reading input: " + e.getMessage());
                input = ""; // to reset the input.
            }
        } while(input.trim().length() < 4 || input.isBlank() || input.contains(";"));

        return input;
    }

    public static String validateString(String attributeName, Integer minLength) {
        String input;
        do {
            try {
                System.out.println("Enter "+attributeName+":");
                input = scn.nextLine();
            } catch (Exception e) {
                System.err.println("Error reading input: " + e.getMessage());
                input = ""; // to reset the input.
            }
        } while(input.trim().length() < minLength || input.isBlank() || input.contains(";"));

        return input;
    }

    /**
     * Method for validating the ISBN input with the use of
     * commons-validator from apache. Note that the return value
     * is only a valid ISBN, but it doesn't assert that it
     * is existing.
     * @return valid ISBN
     */
    public static String validateISBN() {
        ISBNValidator validator = new ISBNValidator();
        String isbn;
        do {
            try {
                System.out.println("Enter ISBN:");
                isbn = scn.nextLine().replaceAll("[\\s-]", ""); // Remove hyphens and spaces
            } catch (Exception e) {
                System.err.println("Error reading input: " + e.getMessage());
                isbn = ""; // Reset the input
            }
        } while (isbn.isBlank() || isbn.length() < 10 || !validator.isValid(isbn));

        // Normalize to ISBN-13 if needed
        if (isbn.length() == 10) {
            isbn = "978" + isbn; // Add prefix for ISBN-13
        }

        return isbn;
    }

    /**
     * Method for validating an integer input for instantiating Books with
     * multiple values for an attribute.
     * @param attributeName specifies for what book attribute is the integer for.
     * @return a positive <code>int</code> excluding 0.
     */
    public static Integer validateInt(String attributeName) {
        Integer input;
        do {
            try {
                System.out.println("Enter "+attributeName+":");
                input = scn.nextInt();
            } catch (InputMismatchException e) {
                System.err.println("Error reading input: " + e.getMessage());
                input = 0; // to reset the input.
            }
        } while(input <= 0);

        scn.nextLine(); // used to clear / flush the line, as to prevent a redundant the next System.out.printLn()
        return input;
    }

    /**
     * Method for creating a formatted string for handling multiple values for a book
     * attribute. Separated with the use of ';' char.
     * @param elementsCount number of times to insert a value to the string.
     * @param elementName name of book attribute
     * @param separator ;
     * @return <code>String</code> separated by ';'.
     */
    public static String assignMultipleElements(Integer elementsCount, String elementName, String separator) {
        String elem = "";

        for (int i=0;i<elementsCount;i++) {
            elem = elem.concat(validateString(elementName+"["+i+"]")).trim().concat(separator);
        }

        return elem;
    }
}
