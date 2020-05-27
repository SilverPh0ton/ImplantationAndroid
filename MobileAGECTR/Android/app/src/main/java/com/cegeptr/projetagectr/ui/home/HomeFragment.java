package com.cegeptr.projetagectr.ui.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cegeptr.projetagectr.R;
import com.cegeptr.projetagectr.logic.Const;
import com.cegeptr.projetagectr.logic.DataSingleton;

public class HomeFragment extends Fragment {

    private HomeViewModel searchViewModel;
    private DataSingleton data = DataSingleton.getInstance();

    private TextView tv_no_pop;
    private TextView tv_no_recent;
    private RecyclerView rv_pop, rv_recent;
    private RecyclerView.Adapter adapter, adapterRecent;
    private Activity parentActivity = this.getActivity();
    View root;

    /**
     * Initialise le layout du fragment
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        searchViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);

        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager horizontalLayoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        tv_no_pop = root.findViewById(R.id.fragment_home_no_pop);
        tv_no_recent = root.findViewById(R.id.fragment_home_no_recent);
        rv_pop = root.findViewById(R.id.recyclerview_popular);
        rv_recent = root.findViewById(R.id.recyclerview_most_recent);

        rv_pop.setLayoutManager(horizontalLayoutManager);
        rv_recent.setLayoutManager(horizontalLayoutManager2);

        data.refreshListPop();
        data.refreshListRecent();

        return root;
    }

    /**
     * Rafraichie la liste du RecyclerView
     */
    public void refreshListPop() {
        adapter= new HomeAdapter(parentActivity, HomeFragment.this, true);
        rv_pop.setAdapter(adapter);
        if(data.getLstBookPop().size()==0){
            tv_no_pop.setVisibility(View.VISIBLE);
        }else{
            tv_no_pop.setVisibility(View.GONE);
        }
    }

    public void refreshListRecent() {
        adapterRecent= new HomeAdapter(parentActivity, HomeFragment.this, false);
        rv_recent.setAdapter(adapterRecent);
        if(data.getLstBookRecent().size()==0){
            tv_no_recent.setVisibility(View.VISIBLE);
        }else{
            tv_no_recent.setVisibility(View.GONE);
        }
    }

    /*================================Broadcast================================*/
    /**
     * Refresh liste des groupe result et enregistre les broadcast receiver
     */
    @Override
    public void onResume() {
        super.onResume();
        refreshListPop();
        refreshListRecent();
        this.getContext().registerReceiver(broadcastReceiver,new IntentFilter(Const.broadcastBooksPopular));
        this.getContext().registerReceiver(broadcastReceiverRecent,new IntentFilter(Const.broadcastBooksRecent));
    }

    /**
     * Met en pause les broadcast receiver
     */
    @Override
    public void onPause() {
        super.onPause();
        this.getContext().unregisterReceiver(broadcastReceiver);
        this.getContext().unregisterReceiver(broadcastReceiverRecent);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshListPop();
        }
    };

    private BroadcastReceiver broadcastReceiverRecent = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshListRecent();
        }
    };

}