package com.cegeptr.projetagectr.ui.history;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cegeptr.projetagectr.R;
import com.cegeptr.projetagectr.logic.Const;
import com.cegeptr.projetagectr.logic.DataSingleton;
import com.cegeptr.projetagectr.logic.Entity.History;
import com.cegeptr.projetagectr.logic.NetworkStateReceiver;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class HistoryFragment extends Fragment implements NetworkStateReceiver.NetworkStateReceiverListener{

    private TextView tv_no_history;
    private TableLayout historyTable;
    private DataSingleton dataSingleton;
    private ProgressBar progressBar;
    private NetworkStateReceiver networkStateReceiver;
    private DecimalFormat twoDForm;

    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }

    /**
     * Initialise le layout du fragment
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root =  inflater.inflate(R.layout.fragment_history, container, false);
        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(this);
        this.getActivity().registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));

        dataSingleton = DataSingleton.getInstance();

        tv_no_history = root.findViewById(R.id.fragment_history_tv_no_history);
        historyTable = root.findViewById(R.id.fragment_history_table);
        progressBar = root.findViewById(R.id.fragment_history_progressBar);
        progressBar.setVisibility(View.VISIBLE);
        dataSingleton.fetchMyHistory();
        twoDForm = new DecimalFormat("0.00");

        return root;
    }

    /**
     * Ajoute une ligne dans l'historique
     * @param history
     */
    private void addRow(History history) {
        TableRow row= new TableRow(getContext());
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        row.setLayoutParams(lp);

        TextView tvTitle = new TextView(getContext());
        tvTitle.setText(history.getTitle());
        tvTitle.setTextSize(14);
        tvTitle.setTextColor(getResources().getColor(R.color.colorTextDark));
        TableRow.LayoutParams params1 = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 3f);
        tvTitle.setLayoutParams(params1);

        TextView tvState = new TextView(getContext());
        switch(history.getState()) {
            case Const.STATE_GIVEN:
                tvState.setText(R.string.state_given);
                break;
            case Const.STATE_PAYED:
                tvState.setText(R.string.state_sold);
                break;
            case Const.STATE_REMOVED:
                tvState.setText(R.string.state_removed);
                break;
            case Const.STATE_DENIED:
                tvState.setText(R.string.state_denied);
                break;
        }
        tvState.setTextSize(14);
        tvState.setTextColor(getResources().getColor(R.color.colorTextDark));
        TableRow.LayoutParams params2 = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 2f);
        tvState.setLayoutParams(params2);


        TextView tvPrice = new TextView(getContext());
        Double price;

        tvPrice.setText(twoDForm.format(history.getPrice()) + " $");
        tvPrice.setTextSize(14);
        tvPrice.setTextColor(getResources().getColor(R.color.colorTextDark));
        TableRow.LayoutParams params3 = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
        tvPrice.setLayoutParams(params3);

        row.addView(tvTitle);
        row.addView(tvState);
        row.addView(tvPrice);
        historyTable.addView(row,0);
    }

    /**
     * Remplis la table
     */
    private void fillTable()
    {
        historyTable.removeAllViews();
        ArrayList<History> histories = dataSingleton.getMyHistory();
        if(histories.size()==0){
            tv_no_history.setVisibility(View.VISIBLE);
        }else {
            tv_no_history.setVisibility(View.GONE);
        for(History history:  histories)
        {
            addRow(history);
        }}
    }


    /*================================État d'internet================================*/
    /**
     * Desactive l'écoute de l'état de réseau
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        networkStateReceiver.removeListener(this);
        this.getActivity().unregisterReceiver(networkStateReceiver);
    }

    /**
     * Intercept l'évenement du retour de réseau
     */
    @Override
    public void networkAvailable() {
        dataSingleton.fetchMyHistory();
        progressBar.setVisibility(View.VISIBLE);
    }

    /**
     * Intercept l'évenement de perte de réseau
     */
    @Override
    public void networkUnavailable() { }

    /*================================Broadcast================================*/
    /**
     * Enregistre les Broadcast Receiver
     */
    @Override
    public void onResume() {
        super.onResume();
        this.getContext().registerReceiver(broadcastReceiver, new IntentFilter(Const.broadcastfetchMyHistory));
    }

    /**
     * Met en pause les broadcast receiver
     */
    @Override
    public void onPause() {
        super.onPause();
        this.getContext().unregisterReceiver(broadcastReceiver);
    }

    /**
     * Broadcast receiver de ce qu'il y a dans l'historique
     */
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            progressBar.setVisibility(View.GONE);
            fillTable();
        }
    };

}
