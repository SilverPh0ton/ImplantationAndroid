package com.cegeptr.projetagectr.ui.search;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cegeptr.projetagectr.R;
import com.cegeptr.projetagectr.logic.Const;
import com.cegeptr.projetagectr.logic.DataSingleton;

public class SearchFragment extends Fragment {

    private SearchViewModel searchViewModel;
    private DataSingleton data = DataSingleton.getInstance();

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        searchViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        root = inflater.inflate(R.layout.fragment_research, container, false);

        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager horizontalLayoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

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
    public void refreshList() {
        adapter= new SearchAdapter(parentActivity, SearchFragment.this, true);
        rv_pop.setAdapter(adapter);
    }

    public void refreshListRecent() {
        adapterRecent= new SearchAdapter(parentActivity, SearchFragment.this, false);
        rv_recent.setAdapter(adapterRecent);
    }

    /*================================Broadcast================================*/
    /**
     * Refresh liste des groupe result et enregistre les broadcast receiver
     */
    @Override
    public void onResume() {
        super.onResume();
        refreshList();
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
            refreshList();
        }
    };

    private BroadcastReceiver broadcastReceiverRecent = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshListRecent();
        }
    };

}