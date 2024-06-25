package com.project;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import org.apache.commons.validator.routines.ISBNValidator;

public class Library {
    private final Scanner scn = new Scanner(System.in);
    private static Library singleInstance = null;
    private final ArrayList<Book> bookShelf = new ArrayList<>();

    private Library() {
    }

    // Implements a singleton design pattern.
    public static synchronized Library getInstance() {
        if (singleInstance == null)
            singleInstance = new Library();

        return singleInstance;
    }

    public void start() {
        boolean usingLibrary = true;

        int libraryOption = -1;

        do {
            try {
                System.out.println("""
                Library Management System\
    
                [1] - Register and add a book.\
    
                [2] - Remove an existing book.\
                
                [3] - Search for a book.\

                [exit] - Exit application.\

                What would you like to do today?\
                """);

                String userInput = scn.nextLine();
                if (userInput.equalsIgnoreCase("exit"))
                    usingLibrary = false;
                else {
                    if (userInput.matches("[123]")) {
                        libraryOption = Integer.parseInt(userInput);
                        usingLibrary = false;
                    }
                }
            } catch (Exception e) {
                System.out.println("Invalid input, kindly try again.");
            }
        } while(usingLibrary);

        switch (libraryOption) {
            case 1:
                addBookBy();
                break;
            case 2:
                removeBookBy();
                break;
            case 3:
                searchBookBy();
                break;
            default:
                System.out.println("Closing library!");
                break;
        }
    }

    private boolean removeBook(String targetISBN) {
        for (Book bookToRemove: bookShelf) {
            if (bookToRemove.getISBN().equals(targetISBN))
                return true;
        }
        return false;
    }

    private void removeBookBy() {
        try {
            String validISBN = validateISBN();

            boolean success = removeBook(validISBN);
            if (success)
                System.out.println("Successfully removed book with ISBN:"+validISBN);
            else
                System.out.println("Failed to remove book with ISBN:"+validISBN);
        } catch (Exception e) {
            System.err.println("Something went wrong! Error: " + e.getMessage());
        }
        continueWithProgram();
    }

    private void searchBook(int attributeIndex) {
        String target;
        ArrayList<Book> results = new ArrayList<>();
        target = validateString("search query");

        switch (attributeIndex) {
            case 1:
                for (Book targetBook:bookShelf) {
                    if (targetBook.getTitle().toLowerCase().contains(target.toLowerCase()))
                        results.add(targetBook);
                }
                break;
            case 2:
                for (Book targetBook:bookShelf) {
                    if (targetBook.getAuthor().toLowerCase().contains(target.toLowerCase()))
                        results.add(targetBook);
                }
                break;
            case 3:
                for (Book targetBook:bookShelf) {
                    if (targetBook.getISBN().toLowerCase().contains(target.toLowerCase()))
                        results.add(targetBook);
                }
                break;
            case 4:
                for (Book targetBook:bookShelf) {
                    if (targetBook.getGenre().toLowerCase().contains(target.toLowerCase()))
                        results.add(targetBook);
                }
                break;
            case 5:
                for (Book targetBook:bookShelf) {
                    if (targetBook.getPublisher().toLowerCase().contains(target.toLowerCase()))
                        results.add(targetBook);
                }
                break;
        }

        if (!results.isEmpty()) {
            String prompt;
            do {
                try {
                    System.out.println("Display full details? (y/n):");
                    prompt = scn.nextLine().toLowerCase();
                } catch (Exception e) {
                    System.err.println("Error reading input: " + e.getMessage());
                    prompt = ""; // to reset the input.
                }
            } while (!prompt.matches("[yn]"));

            if (prompt.equals("y")) {
                for (Book book: results) {
                    book.displayAttributes();
                }
            }
            else {
                for (Book book: results) {
                    System.out.println(book.getTitle());
                }
            }
        } else {
            System.out.println("Failed to find results with search query:"+target);
        }
    }

    private void searchBookBy() {
        int ctr=0;
        int maxLimit = 10000;
        int attrIndex = -1;

        boolean isValidInput = false;
        boolean proceed = false;

        do {
            try {
                if (ctr == 0) {
                    System.out.println("""
                Book Configurations: \
    
                [1] - Title\
    
                [2] - Author\
                
                [3] - ISBN\
                
                [4] - Genre\

                [5] - Publisher\
                
                [quit] - Return to library options.\
                """);
                }
                System.out.println("Enter book attribute:");
                String userInput = scn.nextLine();

                if (userInput.equalsIgnoreCase("quit"))
                    isValidInput = true;
                else {
                    if (userInput.matches("[12345]")) {
                        attrIndex = Integer.parseInt(userInput);
                        proceed = true;
                    }
                }
                ctr++;
            } catch (Exception e) {
                System.out.println("Invalid input, kindly try again.");
            }
        } while (ctr > maxLimit || isValidInput);

        if (proceed) {
            try {
                searchBook(attrIndex);
            } catch (Exception e) {
                System.err.println("Something went wrong! Error: " + e.getMessage());
            }
            continueWithProgram();
        }
        else
            start();
    }

    /**
     * Method for validating the string inputs when adding
     * a book, looping until a string is not blank, length is at least 4 characters,
     * and does not contain the separator ';' character as it should be reserved for
     * multiple elements.
     * @return <code>String</code> that is validated.
     */
    private String validateString(String attributeName) {
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

    private String validateISBN() {
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

    private int validateInt(String attributeName) {
        int input;
        do {
            try {
                System.out.println("Enter "+attributeName+":");
                input = scn.nextInt();
            } catch (InputMismatchException e) {
                System.err.println("Error reading input: " + e.getMessage());
                input = 0; // to reset the input.
            }
        } while(input <= 0);

        scn.nextLine(); // To clear / flush the line, as to prevent a redundant the next sout.printLn
        return input;
    }

    private boolean buildBook(int bookType) {
        Book myBook = null;

        String myTitle = validateString("title");
        String myISBN = validateISBN();
        String myPublisher = validateString("publisher");
        switch (bookType) {
            case 1:
                String myAuthor = validateString("author");
                String myGenre = validateString("genre");
                myBook = new Book(myTitle, myAuthor, myISBN, myGenre, myPublisher);
                break;
            case 2:
                int authorCount = validateInt("number of authors");
                String myGenre2 = validateString("genre");
                myBook = new BookWithAuthors(myTitle, authorCount, myISBN, myGenre2, myPublisher);
                break;
            case 3:
                int genreCount = validateInt("number of genres");
                String myAuthor2 = validateString("author");
                myBook = new BookWithGenres(myTitle, myAuthor2, myISBN, genreCount, myPublisher);
                break;
            case 4:
                int genreCount2 = validateInt("number of genres");
                int authorCount2 = validateInt("number of authors");
                myBook = new BookMany(myTitle, authorCount2, myISBN, genreCount2, myPublisher);
                break;
            default:
                System.out.println("How did you get here?");
                break;
        }

        if (myBook != null) {
            return addBook(myBook);
        }

        return false;
    }

    private boolean addBook(Book bookToAdd) {
        try {
            this.bookShelf.add(bookToAdd);
            return true;
        } catch (Exception e) {
            System.err.println("Failed to add book: " + e.getMessage());
            return false;
        }
    }

    /**
     * A pre-requisite before adding and instantiating a book.
     * Loops until the input is valid or exceeds <code>maxLimit</code>.
     * Once the input is valid, if it isn't quit, the method will call
     * {@link #buildBook(int)} to create the book type.
     */
    private void addBookBy() {
        int ctr=0;
        int maxLimit = 10000;
        int bookType = -1;

        boolean isValidInput = false;
        boolean proceed = false;

        do {
            try {
                if (ctr == 0) {
                    System.out.println("""
                Book Configurations: \
    
                [1] - Book with individual values for each attributes.\
    
                [2] - Book with multiple authors.\
                
                [3] - Book with multiple genres.\
                
                [4] - Book with multiple authors and genres.\
                
                [quit] - Return to library options.\
                """);
                }
                System.out.println("Enter book type:");
                String userInput = scn.nextLine();

                if (userInput.equalsIgnoreCase("quit"))
                    isValidInput = true;
                else {
                    if (userInput.matches("[1234]")) {
                        bookType = Integer.parseInt(userInput);
                        proceed = true;
                    }
                }
                ctr++;
            } catch (Exception e) {
                System.out.println("Invalid input, kindly try again.");
            }
        } while (ctr > maxLimit || isValidInput);

        if (proceed) {
            boolean success = buildBook(bookType);
            String resultMessage = success ? "Successfully added an entry!" : "Failed to add book.";
            System.out.println(resultMessage);

            continueWithProgram();
        }
        else
            start();
    }

    private void continueWithProgram() {
        String input;
        do {
            try {
                System.out.println("Continue using the program? (y/n):");
                input = scn.nextLine().toLowerCase();
            } catch (Exception e) {
                System.out.println("Invalid input, kindly try again.");
                input = "";
            }
        } while (!input.matches("[yn]"));

        if (input.equals("y"))
            start();
    }
}
