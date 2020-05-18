package com.cegeptr.projetagectr.ui.groupResult;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.cegeptr.projetagectr.R;
import com.cegeptr.projetagectr.logic.Const;
import com.cegeptr.projetagectr.logic.DataSingleton;
import com.cegeptr.projetagectr.logic.NetworkStateReceiver;

public class GroupResultFragment extends Fragment implements NetworkStateReceiver.NetworkStateReceiverListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private DataSingleton data = DataSingleton.getInstance();
    private ProgressBar progressBar;
    private Activity parentActivity = this.getActivity();
    private TextView tv_no_group_result;
    private Button bt_backToSearch;
    private SwipeRefreshLayout swipeRefreshLayout;
    private NetworkStateReceiver networkStateReceiver;

    /**
     * Initialise le layout du fragment
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_group_result, container, false);

        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(this);
        this.getActivity().registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));

        tv_no_group_result = root.findViewById(R.id.fragment_group_result_tv_no_group_result);
        bt_backToSearch = root.findViewById(R.id.fragment_group_result_back_to_search);
        progressBar = root.findViewById(R.id.fragment_group_result_progressBar);
        swipeRefreshLayout = root.findViewById(R.id.fragment_group_result_swipeRefreshLayout);

        if(!data.isGroupResultsHasReturn()){
            progressBar.setVisibility(View.VISIBLE);
        }

        bt_backToSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra(Const.DISPLAY_FRAGMENT, Const.DISPLAY_FRAGMENT_SEARCH);
                intent.setAction(Const.broadcastChangeFragment);
                root.getContext().sendBroadcast(intent);
            }
        });

        recyclerView = root.findViewById(R.id.fragment_group_result_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            //portrait mode
            recyclerView.setLayoutManager(new GridLayoutManager(root.getContext(),1));
        } else {
            //landscape mode
            recyclerView.setLayoutManager(new GridLayoutManager(root.getContext(),2));
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                data.search();
            }
        });

        return root;
    }

    /**
     * Rafraichie la liste du RecyclerView
     */
    private void refreshList(){
        adapter= new GroupResultAdpater(parentActivity,GroupResultFragment.this);
        if(data.getGroupResults().size()==0){
            tv_no_group_result.setVisibility(View.VISIBLE);
            bt_backToSearch.setVisibility(View.VISIBLE);
        }else {
            tv_no_group_result.setVisibility(View.GONE);
            bt_backToSearch.setVisibility(View.GONE);
        }
        recyclerView.setAdapter(adapter);
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
        data.search();
        progressBar.setVisibility(View.VISIBLE);
    }

    /**
     * Intercept l'évenement de perte de réseau
     */
    @Override
    public void networkUnavailable() { }

    /*================================Broadcast================================*/
    /**
     * Refresh liste des groupe result et enregistre les broadcast receiver
     */
    @Override
    public void onResume() {
        super.onResume();
        refreshList();
        this.getContext().registerReceiver(broadcastReceiver,new IntentFilter(Const.broadcastsearch));
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
     * Broadcast receiver de la recherche du group fragment
     */
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            swipeRefreshLayout.setRefreshing(false);
            progressBar.setVisibility(View.GONE);
            refreshList();
        }
    };
}
