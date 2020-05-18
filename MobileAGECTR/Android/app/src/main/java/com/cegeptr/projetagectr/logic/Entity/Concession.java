package com.cegeptr.projetagectr.logic.Entity;

public class Concession {

    private String state, urlPhoto, reservedExpireDate, expireDate, refuseReason;
    private double customerPrice,sellingPrice;
    private int idConcession, idCustomer;
    private Book book;
    private Boolean isAnnotated;

    public Concession(String state, String urlPhoto, String reservedExpireDate, String expireDate, String refuseReason, double customerPrice, double sellingPrice, int idConcession, int idCustomer, Book book, Boolean isAnnotated) {
        this.state = state;
        this.urlPhoto = urlPhoto;
        this.reservedExpireDate = reservedExpireDate;
        this.expireDate = expireDate;
        this.refuseReason = refuseReason;
        this.customerPrice = customerPrice;
        this.sellingPrice = sellingPrice;
        this.idConcession = idConcession;
        this.idCustomer = idCustomer;
        this.book = book;
        this.isAnnotated = isAnnotated;
    }

    public String getRefuseReason() {
        return refuseReason;
    }

    public void setRefuseReason(String refuseReason) {
        this.refuseReason = refuseReason;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public double getCustomerPrice() {
        return customerPrice;
    }

    public void setCustomerPrice(double customerPrice) {
        this.customerPrice = customerPrice;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public int getIdConcession() {
        return idConcession;
    }

    public void setIdConcession(int idConcession) {
        this.idConcession = idConcession;
    }

    public int getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(int idCustomer) {
        this.idCustomer = idCustomer;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String getReservedExpireDate() {
        return reservedExpireDate;
    }

    public void setReservedExpireDate(String reservedExpireDate) {
        this.reservedExpireDate = reservedExpireDate;
    }

    public Boolean getAnnotated() {
        return isAnnotated;
    }

    public void setAnnotated(Boolean annotated) {
        isAnnotated = annotated;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }
}
