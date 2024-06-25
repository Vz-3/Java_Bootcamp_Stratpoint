package com.project;

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
    void getTitle() {
        assertEquals("The Count of Monte Cristo", book.getTitle());
    }

    @Test
    void getAuthor() {
        assertEquals("Alexandre Dumas", book.getAuthor());
    }

    @Test
    void getISBN() {
        assertEquals("9780451529701", book.getISBN());
    }

    @Test
    void getGenre() {
        assertEquals("Action", book.getGenre());
    }

    @Test
    void getPublisher() {
        assertEquals("Phil Blank", book.getPublisher());
    }
}