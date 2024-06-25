package com.project;

public class BookMany extends Book{

    public BookMany(String newTitle, int listOfAuthors, String idISBN, int listOfGenres, String newPublisher) {
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
