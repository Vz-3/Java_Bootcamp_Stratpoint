package com.Von.Services.Utils;

import com.Von.Models.Book;
import com.Von.Models.BookWithAuthors;
import com.Von.Models.BookWithAuthorsGenres;
import com.Von.Models.BookWithGenres;
import org.apache.commons.io.FileUtils;
import org.apache.commons.validator.routines.ISBNValidator;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class UtilsService {
    private static final Scanner scn = new Scanner(System.in);
    private static final String fileName = "src/main/resources/data.books.json";

    private UtilsService() {
    }

    public static ArrayList<Book> importData() {
        ArrayList<Book> existingRecords = new ArrayList<>();

        try {
            String jsonContent = FileUtils.readFileToString(new File(fileName), StandardCharsets.UTF_8);
            JSONArray jsonArray = new JSONArray(jsonContent);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String title = jsonObject.getString("title");
                String author = jsonObject.getString("author");
                String isbn = jsonObject.getString("isbn");
                String genre = jsonObject.getString("genre");
                String publisher = jsonObject.getString("publisher");

                Book book = getBook(author, genre);

                book.setTitle(title);
                book.setAuthor(author);
                book.setISBN(isbn);
                book.setGenre(genre);
                book.setPublisher(publisher);

                existingRecords.add(book);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return existingRecords;
    }

    private static Book getBook(String author, String genre) {
        Book book;
        // Properly rebuilds the specific type of book before adding to existing records.
        if (author.contains(";") && genre.contains(";")) {
            book = new BookWithAuthorsGenres();
        }
        else if (author.contains(";")) {
            book = new BookWithAuthors();
        }
        else if (genre.contains(";")) {
            book = new BookWithGenres();
        }
        else
            book = new Book();
        return book;
    }

    public static void storeData(ArrayList<Book> books) {
        try (FileWriter writer = new FileWriter(fileName)) {
            StringBuilder json = new StringBuilder("[\n");
            for (Book book : books) {
                json.append("    {\n")
                        .append("        \"title\": \"").append(book.getTitle()).append("\",\n")
                        .append("        \"author\": \"").append(book.getAuthor()).append("\",\n")
                        .append("        \"isbn\": \"").append(book.getISBN()).append("\",\n")
                        .append("        \"genre\": \"").append(book.getGenre()).append("\",\n")
                        .append("        \"publisher\": \"").append(book.getPublisher()).append("\"\n")
                        .append("    },\n");
            }
            json.deleteCharAt(json.length() - 2); // Remove the trailing comma and newline
            json.append("]");

            writer.write(json.toString());
            System.out.println("Data written to " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public static boolean isUnique(String isbn, ArrayList<Book> books) {
        return books.stream().noneMatch(book -> book.getISBN().equals(isbn));
    }


    /**
     * Method for validating the ISBN input with the use of
     * commons-validator from apache. Note that the return value
     * is only a valid ISBN, but it doesn't assert that it
     * is existing.
     * @return valid ISBN
     */
    public static String validateISBN(ArrayList<Book> books) {
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
        } while (isbn.isBlank() || isbn.length() < 10 || !validator.isValid(isbn) || !isUnique(isbn,books));

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
