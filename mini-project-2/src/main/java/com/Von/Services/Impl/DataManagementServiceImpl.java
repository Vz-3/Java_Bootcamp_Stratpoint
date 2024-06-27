package com.Von.Services.Impl;

import com.Von.Models.Book;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import com.Von.Models.BookWithAuthors;
import com.Von.Models.BookWithAuthorsGenres;
import com.Von.Models.BookWithGenres;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class DataManagementServiceImpl implements com.Von.Services.DataManagementService {
    private static final String fileName = "src/main/resources/data.books.json";

    public static ArrayList<Book> importData() {
        ArrayList<Book> existingRecords = new ArrayList<>();

        try {
            String jsonContent = FileUtils.readFileToString(new File(fileName), StandardCharsets.UTF_8);
            JSONArray jsonArray = new JSONArray(jsonContent);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String title = jsonObject.getString("title");
                String author = jsonObject.getString("author");
                String isbn = jsonObject.getString("isbn");
                String genre = jsonObject.getString("genre");
                String publisher = jsonObject.getString("publisher");

                Book book = getBook(author, genre);

                book.setTitle(title);
                book.setAuthor(author);
                book.setISBN(isbn);
                book.setGenre(genre);
                book.setPublisher(publisher);

                existingRecords.add(book);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return existingRecords;
    }

    private static Book getBook(String author, String genre) {
        Book book;
        // Properly rebuilds the specific type of book before adding to existing records.
        if (author.contains(";") && genre.contains(";")) {
            book = new BookWithAuthorsGenres();
        }
        else if (author.contains(";")) {
            book = new BookWithAuthors();
        }
        else if (genre.contains(";")) {
            book = new BookWithGenres();
        }
        else
            book = new Book();
        return book;
    }

    public static void storeData(ArrayList<Book> books) {
        try (FileWriter writer = new FileWriter(fileName)) {
            StringBuilder json = new StringBuilder("[\n");
            for (Book book : books) {
                json.append("    {\n")
                        .append("        \"title\": \"").append(book.getTitle()).append("\",\n")
                        .append("        \"author\": \"").append(book.getAuthor()).append("\",\n")
                        .append("        \"isbn\": \"").append(book.getISBN()).append("\",\n")
                        .append("        \"genre\": \"").append(book.getGenre()).append("\",\n")
                        .append("        \"publisher\": \"").append(book.getPublisher()).append("\"\n")
                        .append("    },\n");
            }
            json.deleteCharAt(json.length() - 2); // Remove the trailing comma and newline
            json.append("]");

            writer.write(json.toString());
            System.out.println("Data written to " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
