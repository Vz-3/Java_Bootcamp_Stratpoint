package com.Von.Services.Impl;

import com.Von.Models.Book;
import com.Von.Models.BookWithAuthors;
import com.Von.Models.BookWithAuthorsGenres;
import com.Von.Models.BookWithGenres;
import com.Von.Services.LibraryService;

import java.util.ArrayList;

import static com.Von.Services.Impl.BookServiceImpl.*;
import static com.Von.Services.Impl.BookServiceImpl.validateInt;

public class LibraryServiceImpl implements LibraryService {
    public LibraryServiceImpl() {
    }

    /**
     * Returns <code>true</code> if a book matches the {@code targetISBN} parameter.
     * @param targetISBN valid ISBN.
     * @return <code>true</code> if a book matches the {@code targetISBN} parameter.
     */
    public boolean removeBook(String targetISBN, ArrayList<Book> bookShelf) {
        for (Book bookToRemove: bookShelf) {
            if (bookToRemove.getISBN().equals(targetISBN))
                return true;
        }
        return false;
    }

    /**
     * Returns a <code>list</code> of matching <code>Book</code> objects
     * based on a given query
     * @param attributeIndex specific book attribute to search for.
     * @param query specified regex to check if the attribute contains it.
     * @return a <code>list</code> of matches found, otherwise an empty <code>list</code>.
     */
    public ArrayList<Book> searchBook(Integer attributeIndex, String query, ArrayList<Book> bookShelf) {
        ArrayList<Book> results = new ArrayList<>();

        if (bookShelf.isEmpty())
            return results;

        System.out.println(bookShelf);

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
                    if (targetBook.getISBN().contains(query))
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
     * A function for centralizing and streamlining the
     * creation of a new <code>Book</code>. Once created,
     * it returns the instantiated Book.
     * @param bookType select from list of possible Book classes.
     * @return <code>Book</code> object.
     */
    public Book buildBook(Integer bookType) {
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

        if (myBook != null)
            throw new NullPointerException("Failed to build a book!");
        return myBook;
    }

    /**
     * Returns <code>true</code> if the
     * book is appended to the <code>bookShelf</code>.
     * @param bookToAdd new <code>Book</code> type.
     * @return <code>true</code> if added to the list.
     */
    public boolean addBook(Book bookToAdd, ArrayList<Book> bookShelf) {
        try {
            return bookShelf.add(bookToAdd);
        } catch (Exception e) {
            System.err.println("Failed to add book: " + e.getMessage());
            return false;
        }
    }
}
