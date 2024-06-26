package com.Von;

import com.Von.Models.BookWithAuthors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookWithAuthorsTest {
    private BookWithAuthors book;

    @BeforeEach
    void setUp() {
        book = new BookWithAuthors();

        book.setTitle("Secrets of the Enchanted Library");
        book.setAuthor("Jane A. Smith;Michael R. Johnson;Emily K. Lee");
        book.setISBN("978-1-234567-89-0");
        book.setGenre("Fantasy");
        book.setPublisher("Mystical Press");
    }

    @Test
    void test_getAuthors() {
        String[] expectedAuthors = {"Jane A. Smith", "Michael R. Johnson", "Emily K. Lee"};
        assertArrayEquals(expectedAuthors, book.getAuthors());
    }

}