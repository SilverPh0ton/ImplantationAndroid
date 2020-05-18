package com.cegeptr.projetagectr.logic.Entity;

public class History {
    String title , state;
    double price;

    public History(String title, String state, double price) {
        this.title = title;
        this.state = state;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
