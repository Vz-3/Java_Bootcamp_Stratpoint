package com.Von.Services;

import com.Von.Models.Book;

import java.util.ArrayList;

public interface LibraryService {
    boolean removeBook(String targetISBN, ArrayList<Book> bookShelf);
    ArrayList<Book> searchBook(Integer attributeIndex, String query, ArrayList<Book> bookShelf);
    Book buildBook(Integer bookType);
    boolean addBook(Book bookToAdd, ArrayList<Book> bookShelf);
}
