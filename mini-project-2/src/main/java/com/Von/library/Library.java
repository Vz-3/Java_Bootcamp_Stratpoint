package com.Von.library;

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

    /**
     * Implements a singleton design pattern.
     * @return reference to the only instance of <code>Library</code> class.
     */
    public static synchronized Library getInstance() {
        if (singleInstance == null)
            singleInstance = new Library();

        return singleInstance;
    }

    /**
     * Main Program Loop, asks the user
     * for performing the <code>add</code>, <code>remove</code>, and <code>search</code>
     * features for the <code>Book</code> objects.
     */
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

    /**
     * Returns <code>true</code> if a book matches the {@code targetISBN} parameter.
     * @param targetISBN
     * @return <code>true</code> if a book matches the {@code targetISBN} parameter.
     */
    private boolean removeBook(String targetISBN) {
        for (Book bookToRemove: bookShelf) {
            if (bookToRemove.getISBN().equals(targetISBN))
                return true;
        }
        return false;
    }

    /**
     * Asks user to enter a valid ISBN and
     * calls {@link #removeBook(String)} to
     * remove the book matching the ISBN input.
     */
    private void removeBookBy() {
        try {
            String validISBN = validateISBN();

            boolean success = removeBook(validISBN);
            if (success)
                System.out.println("Successfully removed book with ISBN:"+validISBN);
            else
                System.out.println("Failed to remove book with ISBN {"+validISBN+"}");
        } catch (Exception e) {
            System.err.println("Something went wrong! Error: " + e.getMessage());
        }
        continueWithProgram();
    }

    /**
     * Method for calling a linear search for a target query
     * and displaying the results by the book's title or all it's
     * attributes.
     * @param attributeIndex specific book attribute to search for.
     */
    private void processSearch(int attributeIndex) {
        String target;
        target = validateString("search query");

        ArrayList<Book> results = searchBook(attributeIndex, target);

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

            // Loops through all given attributes of a book or just the title.
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
            System.out.println("Failed to find results with search query {"+target+"}");
        }
    }

    /**
     * Returns a <code>list</code> of matching <code>Book</code> objects
     * based on a given query
     * @param attributeIndex specific book attribute to search for.
     * @param query specified regex to check if the attribute contains it.
     * @return a <code>list</code> of matches found, otherwise an empty <code>list</code>.
     */
    private ArrayList<Book> searchBook(int attributeIndex, String query) {
        ArrayList<Book> results = new ArrayList<>();

        switch (attributeIndex) {
            case 1: {
                    for (Book targetBook:bookShelf) {
                        if (targetBook.getTitle().toLowerCase().contains(query))
                            results.add(targetBook);
                    }
                }
                break;
            case 2: {
                    for (Book targetBook:bookShelf) {
                        if (targetBook.getAuthor().toLowerCase().contains(query))
                            results.add(targetBook);
                    }
                }
                break;
            case 3: {
                    for (Book targetBook:bookShelf) {
                        if (targetBook.getISBN().toLowerCase().contains(query))
                            results.add(targetBook);
                    }
                }
                break;
            case 4: {
                for (Book targetBook : bookShelf) {
                    if (targetBook.getGenre().toLowerCase().contains(query))
                        results.add(targetBook);
                }
            }
                break;
            case 5: {
                    for (Book targetBook:bookShelf) {
                        if (targetBook.getPublisher().toLowerCase().contains(query))
                            results.add(targetBook);
                    }
                }
                break;
            default:
                System.out.println("How did you get here?");
                break;
        }

        return results;
    }

    /**
     * Asks what attribute would be searched for.
     * The result will be passed to {@link #processSearch(int)},
     * otherwise, returns to options.
     */
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
                Search By: \
    
                [1] - Title\
    
                [2] - Author\
                
                [3] - ISBN\
                
                [4] - Genre\

                [5] - Publisher\
                
                [quit] - Return to library options.\
                """);
                }
                System.out.println("Enter search preference:");
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
                processSearch(attrIndex);
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

    /**
     * Method for validating the ISBN input with the use of
     * commons-validator from apache. Note that the return value
     * is only a valid ISBN, but it doesn't assert that it
     * is existing.
     * @return valid ISBN
     */
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

    /**
     * Method for validating an integer input for instantiating Books with
     * multiple values for an attribute.
     * @param attributeName specifies for what book attribute is the integer for.
     * @return a positive <code>int</code> excluding 0.
     */
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

        scn.nextLine(); // used to clear / flush the line, as to prevent a redundant the next System.out.printLn()
        return input;
    }

    /**
     * A function for centralizing and streamlining the
     * creation of a new <code>Book</code>. Once created,
     * it calls {@link #addBook(Book)} and returns if the
     * operation is a success.
     * @param bookType select from list of possible Book classes.
     * @return <code>true</code> if {@link #addBook(Book)} is
     * a success.
     */
    private boolean buildBook(int bookType) {
        Book myBook = null;

        String myTitle = validateString("title");
        String myISBN = validateISBN();
        String myPublisher = validateString("publisher");
        switch (bookType) {
            case 1: {
                    String myAuthor = validateString("author");
                    String myGenre = validateString("genre");
                    myBook = new Book(myTitle, myAuthor, myISBN, myGenre, myPublisher);
                }
                break;
            case 2: {
                    String myGenre2 = validateString("genre");
                    int authorCount = validateInt("number of authors");
                    myBook = new BookWithAuthors(myTitle, authorCount, myISBN, myGenre2, myPublisher);
                }
                break;
            case 3: {
                    String myAuthor2 = validateString("author");
                    int genreCount = validateInt("number of genres");
                    myBook = new BookWithGenres(myTitle, myAuthor2, myISBN, genreCount, myPublisher);
                }
                break;
            case 4: {
                    int authorCount2 = validateInt("number of authors");
                    int genreCount2 = validateInt("number of genres");
                    myBook = new BookWithAuthorsGenres(myTitle, authorCount2, myISBN, genreCount2, myPublisher);
                 }
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

    /**
     * Returns <code>true</code> if the
     * book is appended to the <code>bookShelf</code>.
     * @param bookToAdd new <code>Book</code> type.
     * @return <code>true</code> if added to the list.
     */
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
     * Asks the type of book to be instantiated and added to the
     * collections. Proceeds on calling {@link #buildBook(int)}
     * to create the book type, otherwise, returns to options.
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

                // If the condition is listed, exit out of the loop and store bookType.
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

    /**
     * A function that prompts to continue running the program,
     * accepts <code>y</code> or <code>n</code> to return back to
     * the {@link #start()} or exit the program.
     */
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
        else
            System.out.println("Closing program...");
    }
}
