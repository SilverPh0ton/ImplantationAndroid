package com.cegeptr.projetagectr.ui.editBook;

import android.app.Application;
import android.content.Intent;

import androidx.lifecycle.ViewModel;

import com.cegeptr.projetagectr.logic.Const;
import com.cegeptr.projetagectr.logic.DataSingleton;
import com.cegeptr.projetagectr.logic.Entity.Concession;

public class EditBookViewModel extends ViewModel {

    private DataSingleton data = DataSingleton.getInstance();

    private Concession loadedConcession;
    private Double price;
    private Double originalPrice;

    public EditBookViewModel(Application application, Intent intent) {
        loadedConcession = data.getMyConcessionById(intent.getIntExtra(Const.CONCESSION_TO_DISPLAY, 0));

        price = loadedConcession.getCustomerPrice();
        originalPrice = price;
    }

    public Concession getLoadedConcession() {
        return loadedConcession;
    }

    public void setLoadedConcession(Concession loadedConcession) {
        this.loadedConcession = loadedConcession;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Double originalPrice) {
        this.originalPrice = originalPrice;
    }
}
