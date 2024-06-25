package com.project;

public class BookWithGenres extends Book{

    public BookWithGenres() {
    }

    public BookWithGenres(String newTitle, String newAuthor, String idISBN, int listOfGenres, String newPublisher) {
        super(newTitle, newAuthor, idISBN, listOfGenres, newPublisher);
    }

    @Override
    public void displayAttributes() {
        for (int i=0;i<attributes.length;i++) {
            if (i != 3)
                System.out.println(attributeMap.get(i)+":"+attributes[i]);
            else {
                System.out.println(attributeMap.get(i)+"s:");
                String[] genreNames = attributes[i].split(separator);

                for (String genre: genreNames)
                    System.out.println("\t"+genre.trim());
            }
        }
    }

    public String[] getGenres() {
        return super.getGenre().split(separator);
    }

}
