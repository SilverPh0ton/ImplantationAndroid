package com.cegeptr.projetagectr.logic.Entity;

public class Book {

    private int idBook;
    private String title,author,publisher,edition,barcode, urlPhoto;

    public Book(int idBook, String title, String author, String publisher, String edition, String barcode, String urlPhoto) {
        this.idBook = idBook;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.edition = edition;
        this.barcode = barcode;
        this.urlPhoto = urlPhoto;
    }

    public int getIdBook() {
        return idBook;
    }

    public void setIdBook(int idBook) {
        this.idBook = idBook;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

}

