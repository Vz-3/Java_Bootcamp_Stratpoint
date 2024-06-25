package com.project;

public class BookWithAuthors extends Book{

    public BookWithAuthors(String newTitle, int listOfAuthors, String idISBN, String newGenre, String newPublisher) {
        super(newTitle, listOfAuthors, idISBN, newGenre, newPublisher);
    }

    @Override
    public void displayAttributes() {
        for (int i=0;i<attributes.length;i++) {
            if (i != 1)
                System.out.println(attributeMap.get(i)+":"+attributes[i]);
            else {
                System.out.println(attributeMap.get(i)+"s:");
                String[] authorNames = attributes[i].split(separator);

                for (String author: authorNames)
                    System.out.println("\t"+author.trim());
            }
        }
    }

    public String[] getAuthors() {
        return super.getAuthor().split(separator);
    }

}
