package com.cegeptr.projetagectr.ui.home;

import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private String query;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}