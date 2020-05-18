package com.cegeptr.projetagectr.ui.addBook;

import android.net.Uri;

import androidx.lifecycle.ViewModel;

public class AddBookViewModel extends ViewModel {
    private String title;
    private String edition;
    private String author;
    private String publisher;
    private Double price;
    private boolean annotated;
    private Uri uriImage;

    private String scanBarcode;
    private boolean scanerDialogDisplayed;

    public AddBookViewModel() {
        title = "";
        edition = "";
        author = "";
        publisher = "";
        price = 0.0;
        annotated = false;
        uriImage = Uri.EMPTY;

        resetCache();
        scanerDialogDisplayed=false;
    }

    public void resetCache(){
        scanBarcode = "";
    }

    public boolean isScanerDialogDisplayed() {
        return scanerDialogDisplayed;
    }

    public void setScanerDialogDisplayed(boolean scanerDialogDisplayed) {
        this.scanerDialogDisplayed = scanerDialogDisplayed;
    }

    public String getScanBarcode() {
        return scanBarcode;
    }

    public void setScanBarcode(String scanBarcode) {
        this.scanBarcode = scanBarcode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public boolean isAnnotated() {
        return annotated;
    }

    public void setAnnotated(boolean annotated) {
        this.annotated = annotated;
    }

    public Uri getUriImage() {
        return uriImage;
    }

    public void setUriImage(Uri uriImage) {
        this.uriImage = uriImage;
    }
}
