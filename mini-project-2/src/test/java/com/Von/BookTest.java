package com.Von;

import com.Von.Models.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {
    private Book book;

    @BeforeEach
    void setUp() {
        book = new Book("The Count of Monte Cristo", "Alexandre Dumas", "9780451529701", "Action", "Phil Blank");
    }

    @Test
    void test_getTitle() {
        assertEquals("The Count of Monte Cristo", book.getTitle());
    }

    @Test
    void test_getAuthor() {
        assertEquals("Alexandre Dumas", book.getAuthor());
    }

    @Test
    void test_getISBN() {
        assertEquals("9780451529701", book.getISBN());
    }

    @Test
    void test_getGenre() {
        assertEquals("Action", book.getGenre());
    }

    @Test
    void test_getPublisher() {
        assertEquals("Phil Blank", book.getPublisher());
    }
}