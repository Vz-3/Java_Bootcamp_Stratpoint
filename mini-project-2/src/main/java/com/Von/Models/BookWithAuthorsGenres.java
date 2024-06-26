package com.Von.Models;

public class BookWithAuthorsGenres extends Book {

    public BookWithAuthorsGenres() {
        super();
    }

    public BookWithAuthorsGenres(String newTitle, Integer listOfAuthors, String idISBN, Integer listOfGenres, String newPublisher) {
        super(newTitle, listOfAuthors, idISBN, listOfGenres, newPublisher);
    }

    @Override
    public void displayAttributes() {
        for (int i=0;i<attributes.length;i++) {
            if (i != 1 && i != 3)
                System.out.println(attributeMap.get(i)+":"+attributes[i]);
            else {
                System.out.println(attributeMap.get(i)+"s:");
                String[] elemNames = attributes[i].split(separator);

                for (String elem: elemNames)
                    System.out.println("\t"+elem.trim());
            }
        }
    }

    public String[] getGenres() {
        return super.getGenre().split(separator);
    }

    public String[] getAuthors() {
        return super.getAuthor().split(separator);
    }
}
