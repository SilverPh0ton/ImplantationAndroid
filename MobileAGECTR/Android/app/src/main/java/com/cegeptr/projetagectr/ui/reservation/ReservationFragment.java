package com.cegeptr.projetagectr.ui.reservation;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.cegeptr.projetagectr.R;
import com.cegeptr.projetagectr.logic.Const;
import com.cegeptr.projetagectr.logic.DataSingleton;
import com.cegeptr.projetagectr.logic.Entity.Concession;
import com.cegeptr.projetagectr.logic.NetworkStateReceiver;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class ReservationFragment extends Fragment implements NetworkStateReceiver.NetworkStateReceiverListener {

    private ReservationViewModel reservationViewModel;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    public ProgressBar progressBar;
    private TextView tv_no_reservation_result;
    private DataSingleton data = DataSingleton.getInstance();
    private View detailCustomLayout;
    private Dialog detailDialog;
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
        reservationViewModel = ViewModelProviders.of(this).get(ReservationViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_reservation, container, false);

        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(this);
        this.getActivity().registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));

        recyclerView = root.findViewById(R.id.fragment_reservation_recyclerView);
        progressBar = root.findViewById(R.id.fragment_reservation_progressBar);
        tv_no_reservation_result = root.findViewById(R.id.fragment_reservation_tv_no_reservation_result);
        swipeRefreshLayout = root.findViewById(R.id.fragment_reservation_swipeRefreshLayout);

        data.fetchMyReservations();
        progressBar.setVisibility(View.VISIBLE);

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            //portrait mode
            recyclerView.setLayoutManager(new GridLayoutManager(root.getContext(),2));
        } else {
            //landscape mode
            recyclerView.setLayoutManager(new GridLayoutManager(root.getContext(),3));
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                data.fetchMyReservations();
            }
        });

        /*========================Reopen Dialog========================*/
        if(reservationViewModel.isDetailDialogDisplayed()){
            inflateDetailDialog(reservationViewModel.getConcessionDisplayed());
        }

        return root;
    }

    /**
     * Rafraichie la liste du RecyclerView
     */
    public void refreshList(){
        adapter = new ReservationAdapter(this);
        if(data.getMyReservation().size()==0){
            tv_no_reservation_result.setVisibility(View.VISIBLE);
        }else {
            tv_no_reservation_result.setVisibility(View.GONE);
        }
        recyclerView.setAdapter(adapter);
    }

    /*================================Dialog================================*/
    /**
     * Initialise un layout de dialogue
     * @param concession
     */
    public void inflateDetailDialog(Concession concession){
        detailCustomLayout = this
                .getActivity()
                .getLayoutInflater()
                .inflate(R.layout.dialog_book_details_reservation, null);

        detailDialog = new AlertDialog
                .Builder(this.getContext())
                .setView(detailCustomLayout)
                .create();

        detailDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        detailDialog.show();

        DecimalFormat twoDForm = new DecimalFormat("0.00");

        TextView tv_title = detailCustomLayout.findViewById(R.id.dialog_book_detail_reservation_tv_title);
        TextView tv_edition = detailCustomLayout.findViewById(R.id.dialog_book_detail_reservation_tv_edition);
        TextView tv_author = detailCustomLayout.findViewById(R.id.dialog_book_detail_reservation_tv_author);
        TextView tv_publisher = detailCustomLayout.findViewById(R.id.dialog_book_detail_reservation_tv_publisher);
        TextView tv_price = detailCustomLayout.findViewById(R.id.dialog_book_detail_reservation_tv_price);
        ImageView dialogImageView = detailCustomLayout.findViewById(R.id.dialog_book_detail_reservation_imageView);
        TextView tv_annotated = detailCustomLayout.findViewById(R.id.dialog_book_detail_reservation_tv_annotated);
        tv_annotated.setText((concession.getAnnotated())?R.string.yes_display:R.string.no_display);

        tv_title.setText(concession.getBook().getTitle());
        tv_edition.setText(concession.getBook().getEdition());
        tv_author.setText(concession.getBook().getAuthor());
        tv_publisher.setText(concession.getBook().getPublisher());
        tv_price.setText(twoDForm.format(concession.getSellingPrice()) + "$");

        String img_url = ((concession.getUrlPhoto() == null) ?
                Const.BOOK_IMG_ADDRESS + concession.getBook().getUrlPhoto() :
                Const.CONCESSION_IMG_ADDRESS + concession.getUrlPhoto()
        );
        Picasso
                .get()
                .load(img_url)
                .placeholder(R.drawable.book_default)
                .error(R.drawable.book_default)
                .into(dialogImageView);

        detailCustomLayout
                .findViewById(R.id.dialog_book_detail_reservation_unreserver)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        data.unreserveConcession(concession.getIdConcession());
                        progressBar.setVisibility(View.VISIBLE);
                        detailDialog.dismiss();
                    }
                });

        detailCustomLayout
                .findViewById(R.id.dialog_book_detail_reservation_bt_cancel)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        detailDialog.dismiss();
                    }
                });

        detailDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                reservationViewModel.setDetailDialogDisplayed(false);
            }
        });
    }

    /*================================État d'internet================================*/
    /**
     * Desactive l'écoute de l'état de réseau
     */
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
        data.fetchMyReservations();
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
        this.getContext().registerReceiver(broadcastReceiverFetch, new IntentFilter(Const.broadcastfetchMyReservations));
        this.getContext().registerReceiver(broadcastReceiverUnreserve, new IntentFilter(Const.broadcastunreserveConcession));
        refreshList();
    }
    /**
     * Arrête l'enregistrement des broadcast receiver
     */
    @Override
    public void onPause() {
        super.onPause();
        this.getContext().unregisterReceiver(broadcastReceiverFetch);
    }

    private BroadcastReceiver broadcastReceiverUnreserve = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getBooleanExtra(Const.REQUEST_SUCCES,false)){
                Toast.makeText(context, R.string.toast_msg_unreserve_successful , Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(context, R.string.toast_msg_unreserve_failed , Toast.LENGTH_SHORT).show();
            }
            data.fetchMyReservations();
        }
    };

    private BroadcastReceiver broadcastReceiverFetch = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            progressBar.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
            refreshList();
        }
    };
}