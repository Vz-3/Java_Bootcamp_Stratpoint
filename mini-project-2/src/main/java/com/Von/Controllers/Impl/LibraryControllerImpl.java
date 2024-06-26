package com.Von.Controllers.Impl;

import java.util.ArrayList;
import java.util.Scanner;

import com.Von.Models.Book;
import com.Von.Controllers.LibraryController;

import static com.Von.Services.Impl.BookServiceImpl.validateISBN;
import static com.Von.Services.Impl.BookServiceImpl.validateString;
import com.Von.Services.Impl.LibraryServiceImpl;
import com.Von.Services.LibraryService;

public class LibraryControllerImpl implements LibraryController {
    private final Scanner scn = new Scanner(System.in);
    private static LibraryControllerImpl singleInstance = null;
    private final ArrayList<Book> bookShelf = new ArrayList<>();
    private final LibraryService libService = new LibraryServiceImpl();

    private LibraryControllerImpl() {
    }

    /**
     * Implements a singleton design pattern.
     * @return reference to the only instance of <code>Library</code> class.
     */
    public static synchronized LibraryControllerImpl getInstance() {
        if (singleInstance == null)
            singleInstance = new LibraryControllerImpl();

        return singleInstance;
    }

    /**
     * Main Program Loop, asks the user
     * for performing the <code>add</code>, <code>remove</code>, and <code>search</code>
     * features for the <code>Book</code> objects.
     */
    public void start() {
        Boolean usingLibrary = true;

        Integer libraryOption = -1;

        do {
            try {
                System.out.println("""
                ====== Library Management System ====== \
    
                [1] - Register and add a book.\
    
                [2] - Remove an existing book.\
                
                [3] - Search for a book.\

                [exit] - Exit application.\
                
                ======================================= \
                
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
     * Remove a book by asking user to enter a valid ISBN and
     * call <code>LibraryService.removeBook(Book)</code> to
     * remove the book if there's a matching ISBN input.
     */
    public void removeBookBy() {
        try {
            String validISBN = validateISBN();

            Boolean success = libService.removeBook(validISBN, bookShelf);
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
    private void processSearch(Integer attributeIndex) {
        String target;
        target = validateString("search query");

        ArrayList<Book> results = libService.searchBook(attributeIndex, target, bookShelf);

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
     * Search for a book by specifying the attribute
     * and call <code>LibraryService.searchBook(Integer, String)</code>.
     * {@link #processSearch(Integer)} will prompt the user to
     * retrieve all details of a book or just it's <code>title</code>.
     */
    public void searchBookBy() {
        Integer ctr=0;
        Integer maxLimit = 10000;
        Integer attrIndex = -1;

        Boolean isValidInput = false;
        Boolean proceed = false;

        do {
            try {
                if (ctr == 0) {
                    System.out.println("""
                ====== Search By ===== \
    
                [1] - Title\
    
                [2] - Author\
                
                [3] - ISBN\
                
                [4] - Genre\

                [5] - Publisher\
                
                [quit] - Return to library options.\
                
                ====================== \
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
                        isValidInput = true;
                    }
                }
                ctr++;
            } catch (Exception e) {
                System.out.println("Invalid input, kindly try again.");
            }
        } while (ctr < maxLimit && !isValidInput);

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
     * Add a book to the library's list of books by
     * specifying the type of book and setting the book's attributes
     * through <code>LibraryService.buildBook(Integer)</code> &
     * <code>LibraryService.addBook(Book)</code>.
     */
    public void addBookBy() {
        Integer ctr=0;
        Integer maxLimit = 10000;
        Integer bookType = -1;

        Boolean isValidInput = false;
        Boolean proceed = false;

        do {
            try {
                if (ctr == 0) {
                    System.out.println("""
                ====== Book Configurations ====== \
    
                [1] - Book with individual values for each attributes.\
    
                [2] - Book with multiple authors.\
                
                [3] - Book with multiple genres.\
                
                [4] - Book with multiple authors and genres.\
                
                [quit] - Return to library options.\
                
                ================================= \
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
                        isValidInput = true;
                    }
                }
                ctr++;
            } catch (Exception e) {
                System.out.println("Invalid input, kindly try again.");
            }
        } while (ctr < maxLimit && !isValidInput);

        if (proceed) {
            Book newBook = libService.buildBook(bookType);
            Boolean success = libService.addBook(newBook, bookShelf);
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
