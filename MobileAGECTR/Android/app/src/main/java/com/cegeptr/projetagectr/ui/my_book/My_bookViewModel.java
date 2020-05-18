package com.cegeptr.projetagectr.ui.my_book;

import com.cegeptr.projetagectr.logic.Entity.Concession;

import androidx.lifecycle.ViewModel;

public class My_bookViewModel extends ViewModel {

    private boolean scanerDialogDisplayed;
    private boolean actionDialogDisplayed;
    private String barcode;
    private Concession concessionConcerned;

    public My_bookViewModel(){
        resetCache();
    }

    public void resetCache(){
        barcode = "";
    }

    public boolean isScanerDialogDisplayed() {
        return scanerDialogDisplayed;
    }

    public void setScanerDialogDisplayed(boolean scanerDialogDisplayed) {
        this.scanerDialogDisplayed = scanerDialogDisplayed;
    }

    public boolean isActionDialogDisplayed() {
        return actionDialogDisplayed;
    }

    public void setActionDialogDisplayed(boolean actionDialogDisplayed) {
        this.actionDialogDisplayed = actionDialogDisplayed;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }



    public Concession getConcessionConcerned() {
        return concessionConcerned;
    }

    public void setConcessionConcerned(Concession concessionConcerned) {
        this.concessionConcerned = concessionConcerned;
    }
}