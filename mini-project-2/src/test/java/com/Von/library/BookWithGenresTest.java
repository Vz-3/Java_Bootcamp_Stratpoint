package com.Von.library;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookWithGenresTest {
    private BookWithGenres book;

    @BeforeEach
    void setUp() {
        book = new BookWithGenres();

        book.setTitle("Everything, Everything");
        book.setAuthor("Nicola Yoon");
        book.setISBN("9780553496673");
        book.setGenre("Fiction;Young Adult;Contemporary;Romance");
        book.setPublisher("Delacorte Books");
    }

    @Test
    void test_getGenres() {
        String[] expectedGenres = {"Fiction", "Young Adult", "Contemporary", "Romance"};
        assertArrayEquals(expectedGenres, book.getGenres());
    }
}