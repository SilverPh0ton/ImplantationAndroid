package com.cegeptr.projetagectr.logic.Entity;

public class GroupResult {

    private Book book;
    private int amount;

    public GroupResult(Book book, int amount) {
        this.book = book;
        this.amount = amount;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
