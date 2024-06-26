package com.project;

import java.util.HashMap;
import java.util.Scanner;

public class Book {
    /**
     * Attributes are:
     * <p>
     *      <ol>
     *          <li>Title - <code>String</code></li>
     *          <li>Author / Multiple Authors - <code>String</code></li>
     *          <li>ISBN - <code>String</code> (As to handle leading zeroes or hyphens)</li>
     *          <li>Genre / Category - <code>String</code></li>
     *          <li>Publisher - <code>String</code></li>
     *      </ol>
     * </p>
     */
    protected String[] attributes = new String[5];
    protected HashMap<Integer, String> attributeMap = new HashMap<>();
    protected String separator = ";";
    private final Scanner scn = new Scanner(System.in);

    private void initializeHashMap() {
        attributeMap.put(0, "Title");
        attributeMap.put(1, "Author");
        attributeMap.put(2, "ISBN");
        attributeMap.put(3, "Genre");
        attributeMap.put(4, "Publisher");
    }

    /**
     * Helper function for the constructor class.
     * Takes in the parameters and assigns it to the class variable <code>attributes</code> in order.
     * @param newTitle
     * @param newAuthor can be multiple authors separated by <code>;</code>.
     * @param idISBN
     * @param newGenre can be multiple genres separated by <code>;</code>.
     * @param newPublisher
     */
    void assignAttributes(String newTitle, String newAuthor, String idISBN, String newGenre, String newPublisher) {
        this.attributes[0] = newTitle;
        this.attributes[1] = newAuthor;
        this.attributes[2] = idISBN;
        this.attributes[3] = newGenre;
        this.attributes[4] = newPublisher;
    }
    private String getValidString() {
        String validString = "";
        do {
            try {
                System.out.print(">");
                validString = scn.nextLine();
            } catch (Exception e) {
                System.out.println("Invalid input, kindly try again.");
            }
        } while (validString.length() < 4 || validString.isBlank() || validString.contains(";"));
        return validString;
    }

    String assignMultipleElements(int elementsCount, String elementName) {
        String elem = "";

        for (int i=0;i<elementsCount;i++) {
            System.out.println("Enter "+elementName+"["+i+"]:");
            elem = elem.concat(getValidString()).trim().concat(separator);
        }

        return elem;
    }

    // Constructors
    public Book() {
    }

    public Book(String newTitle, String newAuthor, String idISBN, String newGenre, String newPublisher) {
        assignAttributes(newTitle, newAuthor, idISBN, newGenre, newPublisher);
        initializeHashMap();
    }

    protected Book(String newTitle, String newAuthor, String idISBN, int listOfGenres, String newPublisher) {
        String multipleGenre = assignMultipleElements(listOfGenres, "genre");
        assignAttributes(newTitle, newAuthor, idISBN, multipleGenre, newPublisher);
        initializeHashMap();
    }

    protected Book(String newTitle, int listOfAuthors, String idISBN, String newGenre, String newPublisher) {
        String multipleAuthors = assignMultipleElements(listOfAuthors, "author");
        assignAttributes(newTitle, multipleAuthors, idISBN, newGenre, newPublisher);
        initializeHashMap();
    }

    protected Book(String newTitle, int listOfAuthors, String idISBN, int listOfGenres, String newPublisher) {
        String multipleAuthors = assignMultipleElements(listOfAuthors, "author");
        String multipleGenre = assignMultipleElements(listOfGenres, "genre");

        assignAttributes(newTitle, multipleAuthors, idISBN, multipleGenre, newPublisher);
        initializeHashMap();
    }

    public void displayAttributes() {
        for (int i=0; i<attributes.length;i++) {
            System.out.println(attributeMap.get(i)+":"+attributes[i]);
        }
    }

    // Getters & setters
    public String[] getBookAttributes() {
        return this.attributes;
    }

    public String getTitle() {
        return this.attributes[0];
    }

    public String getAuthor() {
        return this.attributes[1];
    }

    public String getISBN() {
        return this.attributes[2];
    }

    public String getGenre() {
        return this.attributes[3];
    }

    public String getPublisher() {
        return this.attributes[4];
    }

    public void setTitle(String newTitle) {
        this.attributes[0] = newTitle;
    }

    public void setAuthor(String newAuthor) {
        this.attributes[1] = newAuthor;
    }

    public void setISBN(String newISBN) {
        this.attributes[2] = newISBN;
    }

    public void setGenre(String newGenre) {
        this.attributes[3] = newGenre;
    }

    public void setPublisher(String newPublisher) {
        this.attributes[4] = newPublisher;
    }

}
