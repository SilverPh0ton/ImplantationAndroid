package com.cegeptr.projetagectr.ui.reservation;

import com.cegeptr.projetagectr.logic.Entity.Concession;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ReservationViewModel extends ViewModel {

    private boolean detailDialogDisplayed;
    private Concession concessionDisplayed;

    public boolean isDetailDialogDisplayed() {
        return detailDialogDisplayed;
    }

    public void setDetailDialogDisplayed(boolean detailDialogDisplayed) {
        this.detailDialogDisplayed = detailDialogDisplayed;
    }

    public Concession getConcessionDisplayed() {
        return concessionDisplayed;
    }

    public void setConcessionDisplayed(Concession concessionDisplayed) {
        this.concessionDisplayed = concessionDisplayed;
    }
}