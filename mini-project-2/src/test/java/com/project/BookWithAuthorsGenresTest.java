package com.project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookWithAuthorsGenresTest {
    private BookWithAuthorsGenres book;

    @BeforeEach
    void setUp() {
        book = new BookWithAuthorsGenres();
        book.setTitle("Sunrise Nights");
        book.setAuthor("Jeff Zentner;Brittany Cavallaro");
        book.setISBN("0063324539");
        book.setGenre("Young Adult;Romance;Fiction;Contemporary");
        book.setPublisher("Quilt Tree Books");
    }

    @Test
    void test_getGenres() {
        String[] expectedGenres = {"Young Adult","Romance","Fiction","Contemporary"};
        assertArrayEquals(expectedGenres, book.getGenres());
    }

    @Test
    void test_getAuthors() {
        String[] expectedAuthors = {"Jeff Zentner", "Brittany Cavallaro"};
        assertArrayEquals(expectedAuthors, book.getAuthors());
    }
}