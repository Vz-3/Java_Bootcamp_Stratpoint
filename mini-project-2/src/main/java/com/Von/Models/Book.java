package com.Von.Models;

import java.util.HashMap;

import static com.Von.Services.Utils.UtilsService.assignMultipleElements;

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

    /**
     * Maps the index of attributes to the corresponding name
     * of attribute for the use of {@link #displayAttributes()}.
     */
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
     * @param newTitle a single title.
     * @param newAuthor single or multiple authors separated by <code>;</code>.
     * @param idISBN a single validated ISBN.
     * @param newGenre single or multiple genres separated by <code>;</code>.
     * @param newPublisher a single publisher.
     */
    void assignAttributes(String newTitle, String newAuthor, String idISBN, String newGenre, String newPublisher) {
        this.attributes[0] = newTitle;
        this.attributes[1] = newAuthor;
        this.attributes[2] = idISBN;
        this.attributes[3] = newGenre;
        this.attributes[4] = newPublisher;
    }

    // Constructors
    public Book() {
        initializeHashMap();
    }

    public Book(String newTitle, String newAuthor, String idISBN, String newGenre, String newPublisher) {
        assignAttributes(newTitle, newAuthor, idISBN, newGenre, newPublisher);
        initializeHashMap();
    }

    protected Book(String newTitle, String newAuthor, String idISBN, Integer listOfGenres, String newPublisher) {
        String multipleGenre = assignMultipleElements(listOfGenres, "genre", separator);
        assignAttributes(newTitle, newAuthor, idISBN, multipleGenre, newPublisher);
        initializeHashMap();
    }

    protected Book(String newTitle, Integer listOfAuthors, String idISBN, String newGenre, String newPublisher) {
        String multipleAuthors = assignMultipleElements(listOfAuthors, "author", separator);
        assignAttributes(newTitle, multipleAuthors, idISBN, newGenre, newPublisher);
        initializeHashMap();
    }

    protected Book(String newTitle, Integer listOfAuthors, String idISBN, Integer listOfGenres, String newPublisher) {
        String multipleAuthors = assignMultipleElements(listOfAuthors, "author", separator);
        String multipleGenre = assignMultipleElements(listOfGenres, "genre", separator);

        assignAttributes(newTitle, multipleAuthors, idISBN, multipleGenre, newPublisher);
        initializeHashMap();
    }

    public void displayAttributes() {
        for (int i=0; i<attributes.length;i++) {
            System.out.println(attributeMap.get(i)+":"+attributes[i]);
        }
    }

    // Getters & setters

    /**
     * Returns the entire list of book attributes.
     * @return <code>Array</code> of length 5 containing all
     * book attributes.
     */
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
