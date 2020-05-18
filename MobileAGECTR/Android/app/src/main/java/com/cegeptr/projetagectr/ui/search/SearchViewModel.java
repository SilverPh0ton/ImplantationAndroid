package com.cegeptr.projetagectr.ui.search;

import androidx.lifecycle.ViewModel;

public class SearchViewModel extends ViewModel {

    private String query;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}