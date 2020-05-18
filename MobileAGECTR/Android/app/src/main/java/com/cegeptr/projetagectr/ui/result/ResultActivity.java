package com.cegeptr.projetagectr.ui.result;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.cegeptr.projetagectr.R;
import com.cegeptr.projetagectr.logic.Const;
import com.cegeptr.projetagectr.logic.DataSingleton;
import com.cegeptr.projetagectr.logic.Entity.Book;
import com.cegeptr.projetagectr.logic.Entity.Concession;
import com.cegeptr.projetagectr.logic.NetworkStateReceiver;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class ResultActivity extends AppCompatActivity implements NetworkStateReceiver.NetworkStateReceiverListener{

    private ResultViewModel resultViewModel;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private DataSingleton data = DataSingleton.getInstance();
    public ProgressBar progressBar;
    private View detailCustomLayout;
    private Dialog detailDialog;
    private DecimalFormat twoDForm;
    private Book bookDisplayed;
    private TextView tvTitle, tvEdition, tvAmount;
    private SwipeRefreshLayout swipeRefreshLayout;
    private NetworkStateReceiver networkStateReceiver;

    /**
     * Initialise le layout de l'activité
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        resultViewModel = ViewModelProviders.of(this).get(ResultViewModel.class);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(this);
        this.registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));

        twoDForm = new DecimalFormat("0.00");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        Toolbar myToolbar = findViewById(R.id.activity_result_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = this.findViewById(R.id.activity_result_progressBar);
        if (!data.isDetailsResultsHasReturn()) {
            progressBar.setVisibility(View.VISIBLE);
        }

        tvTitle = findViewById(R.id.activity_result_title);
        tvEdition = findViewById(R.id.activity_result_edition);
        tvAmount = findViewById(R.id.activity_result_tv_amount);
        swipeRefreshLayout = findViewById(R.id.activity_result_swipeRefreshLayout);

        bookDisplayed = data.getDetailsResultsGroupResult().getBook();
        tvTitle.setText(bookDisplayed.getTitle());
        tvEdition.setText(bookDisplayed.getEdition());

        recyclerView = findViewById(R.id.activity_result_recyclerView);

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            //portrait mode
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            //landscape mode
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                data.detailSearch();
            }
        });

        /*========================Reopen Dialog========================*/
        if (resultViewModel.isDetailDialogDisplayed()) {
            inflateDetailDialog(resultViewModel.getConcessionDisplayed());
        }
    }

    /**
     * Intercept le touche de retour (bas du telephone)
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * intercept la fleche de retour (haut du telephone)
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Rafraichie la liste du RecyclerView
     */
    public void refreshList() {
        adapter = new ResultAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    /*================================Dialog================================*/
    /**
     * Initialise un layout de dialogue
     * @param concession
     */
    public void inflateDetailDialog(Concession concession) {
        detailCustomLayout = this
                .getLayoutInflater()
                .inflate(R.layout.dialog_book_details, null);

        detailDialog = new AlertDialog
                .Builder(this)
                .setView(detailCustomLayout)
                .create();

        detailDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        detailDialog.show();

        TextView tv_title = detailCustomLayout.findViewById(R.id.dialog_book_detail_tv_title);
        TextView tv_edition = detailCustomLayout.findViewById(R.id.dialog_book_detail_tv_edition);
        TextView tv_author = detailCustomLayout.findViewById(R.id.dialog_book_detail_tv_author);
        TextView tv_publisher = detailCustomLayout.findViewById(R.id.dialog_book_detail_tv_publisher);
        TextView tv_price = detailCustomLayout.findViewById(R.id.dialog_book_detail_tv_price);
        TextView tv_annotated = detailCustomLayout.findViewById(R.id.dialog_book_detail_tv_annotated);
        ImageView dialogImageView = detailCustomLayout.findViewById(R.id.dialog_book_detail_imageView);

        tv_title.setText(concession.getBook().getTitle());
        tv_edition.setText(concession.getBook().getEdition());
        tv_author.setText(concession.getBook().getAuthor());
        tv_publisher.setText(concession.getBook().getPublisher());
        tv_price.setText(String.valueOf(twoDForm.format(concession.getSellingPrice())) + "$");
        tv_annotated.setText((concession.getAnnotated()) ? R.string.yes_display : R.string.no_display);

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

        if (data.getConnectedUser() == null || concession.getIdCustomer() == data.getConnectedUser().getIdUser()) {
            detailCustomLayout
                    .findViewById(R.id.dialog_book_detail_reserver)
                    .setVisibility(View.GONE);
        }

        detailCustomLayout
                .findViewById(R.id.dialog_book_detail_reserver)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (data.getConnectedUser() != null) {
                            data.reserveConcession(concession.getIdConcession());
                            progressBar.setVisibility(View.VISIBLE);
                            detailDialog.dismiss();
                        }
                    }
                });

        detailCustomLayout
                .findViewById(R.id.dialog_book_detail_bt_cancel)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        detailDialog.dismiss();
                    }
                });


        detailDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                resultViewModel.setDetailDialogDisplayed(false);
            }
        });
    }


    /*================================État d'internet================================*/
    /**
     * Desactive l'écoute de l'état de réseau
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        networkStateReceiver.removeListener(this);
        this.unregisterReceiver(networkStateReceiver);
    }

    /**
     * Intercept l'évenement du retour de réseau
     */
    @Override
    public void networkAvailable() {
        data.detailSearch();
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
    protected void onResume() {
        super.onResume();
        refreshList();
        this.registerReceiver(broadcastReceiverFetch, new IntentFilter(Const.broadcastdetailSearch));
        this.registerReceiver(broadcastReceiverReserve, new IntentFilter(Const.broadcastreserveConcession));
    }

    /**
     * Arrête l'enregistrement des broadcast receiver
     */
    @Override
    protected void onPause() {
        super.onPause();

        this.unregisterReceiver(broadcastReceiverFetch);
        this.unregisterReceiver(broadcastReceiverReserve);
    }

    private BroadcastReceiver broadcastReceiverFetch = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshList();
            int amount = data.getDetailsResults().size();
            tvAmount.setText(String.valueOf(amount));
            progressBar.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
            data.updateBookAmount(bookDisplayed.getIdBook(), amount);

            if (amount == 0) {
                finish();
            }
        }
    };

    private BroadcastReceiver broadcastReceiverReserve = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getBooleanExtra(Const.REQUEST_SUCCES, false)) {
                Toast.makeText(ResultActivity.this, R.string.toast_msg_reserve_successful, Toast.LENGTH_SHORT).show();
            } else if (intent.getStringExtra(Const.REQUEST_MSG).equals(Const.MAX_SERVER_RESPONSE)) {
                Toast.makeText(ResultActivity.this, R.string.toast_msg_max_reservation, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ResultActivity.this, R.string.toast_msg_reserve_failed, Toast.LENGTH_SHORT).show();
            }
            data.detailSearch();
        }
    };
}
