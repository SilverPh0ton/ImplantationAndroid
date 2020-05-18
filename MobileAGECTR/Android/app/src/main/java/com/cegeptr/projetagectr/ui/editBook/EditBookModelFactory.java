package com.cegeptr.projetagectr.ui.editBook;

import android.app.Application;
import android.content.Intent;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class EditBookModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;
    private Intent intent;


    public EditBookModelFactory(Application application, Intent intent) {
        mApplication = application;
        this.intent = intent;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new EditBookViewModel(mApplication, intent);
    }
}