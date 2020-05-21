package com.cegeptr.projetagectr.ui.my_book;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.cegeptr.projetagectr.ui.addBook.AddBookActivity;
import com.cegeptr.projetagectr.ui.editBook.EditBookActivity;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ivan200.photobarcodelib.PhotoBarcodeScanner;
import com.ivan200.photobarcodelib.PhotoBarcodeScannerBuilder;

public class My_bookFragment extends Fragment implements NetworkStateReceiver.NetworkStateReceiverListener{

    private My_bookViewModel myBookViewModel;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ProgressBar progressBar;
    private DataSingleton data = DataSingleton.getInstance();
    private TextView tv_no_book_result;
    private View scanerCustomLayout, actionCustomLayout;
    private Dialog scanerDialog, actionDialog;
    private SwipeRefreshLayout swipeRefreshLayout;
    private NetworkStateReceiver networkStateReceiver;
    private Button renew_all;

    /**
     * Initialise le layout du fragment
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myBookViewModel = ViewModelProviders.of(this).get(My_bookViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_my_book, container, false);

        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(this);
        this.getActivity().registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));

        recyclerView = root.findViewById(R.id.fragment_my_book_recyclerView);
        progressBar = root.findViewById(R.id.fragment_my_book_progressBar);
        tv_no_book_result = root.findViewById(R.id.fragment_my_book_tv_no_book_result);
        swipeRefreshLayout = root.findViewById(R.id.fragment_my_book_swipeRefreshLayout);

        data.fetchMyConcessions();
        progressBar.setVisibility(View.VISIBLE);

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            //portrait mode
            recyclerView.setLayoutManager(new GridLayoutManager(root.getContext(),2));
        } else {
            //landscape mode
            recyclerView.setLayoutManager(new GridLayoutManager(root.getContext(),3));
        }

        FloatingActionButton fab = root.findViewById(R.id.fragment_my_book_bt_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inflateScanerDialog();
                scanerDialog.show();
                myBookViewModel.setScanerDialogDisplayed(true);
            }
        });

        renew_all = root.findViewById(R.id.fragment_my_book_bt_renew_all);
        renew_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.renewConcessionAll(data.getConnectedUser().getIdUser());
                Toast.makeText(v.getContext(), R.string.toast_msg_renew_successful_all, Toast.LENGTH_LONG).show();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                data.fetchMyConcessions();
            }
        });

        /*========================Reopen Dialog========================*/
        if(myBookViewModel.isScanerDialogDisplayed()){
            inflateScanerDialog();
            scanerDialog.show();
        }
        else if(myBookViewModel.isActionDialogDisplayed()){
            inflateActionDialog(myBookViewModel.getConcessionConcerned());
        }

        return root;
    }

    /**
     * Rafraichie la liste du RecyclerView
     */
    private void refreshList(){
        adapter = new My_bookAdapter(this);
        if(data.getMyConcessions().size()==0){
            tv_no_book_result.setVisibility(View.VISIBLE);
        }else {
            tv_no_book_result.setVisibility(View.GONE);
            renew_all.setVisibility(View.GONE);

            for (Concession item:data.getMyConcessions()) {
                if (item.getState().equals(Const.STATE_ACCEPT) || item.getState().equals(Const.STATE_TO_RENEW)) {
                    renew_all.setVisibility(View.VISIBLE);
                    break;
                }
            }

        }
        recyclerView.setAdapter(adapter);
    }

    /*================================Dialog================================*/

    /**
     * Initialise un layout de dialogue
     * @param concession
     */
    public void inflateActionDialog(Concession concession){
        actionCustomLayout = this
                .getLayoutInflater()
                .inflate(R.layout.dialog_concession_actions, null);

        actionDialog = new AlertDialog
                .Builder(this.getContext())
                .setView(actionCustomLayout)
                .create();

        actionDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        actionDialog.show();

        Button bt_edit_view = actionCustomLayout.findViewById(R.id.dialog_concess_action_edit_view);
        Button bt_renew = actionCustomLayout.findViewById(R.id.dialog_concess_action_renew);
        Button bt_give = actionCustomLayout.findViewById(R.id.dialog_concess_action_give);
        Button bt_remove = actionCustomLayout.findViewById(R.id.dialog_concess_action_remove);
        Button bt_delete = actionCustomLayout.findViewById(R.id.dialog_concess_action_delete);
        Button bt_cancel = actionCustomLayout.findViewById(R.id.dialog_concess_action_cancel);

        if (concession.getState().equals(Const.STATE_ACCEPT) || concession.getState().equals(Const.STATE_TO_RENEW)) {
            bt_delete.setVisibility(View.GONE);
        } else if (concession.getState().equals(Const.STATE_PENDING)) {
            bt_renew.setVisibility(View.GONE);
            bt_give.setVisibility(View.GONE);
            bt_remove.setVisibility(View.GONE);
        }

        bt_edit_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), EditBookActivity.class);
                intent.putExtra(Const.CONCESSION_TO_DISPLAY, concession.getIdConcession());
                view.getContext().startActivity(intent);
                actionDialog.dismiss();
            }
        });
        bt_renew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data.renewConcession(concession.getIdConcession());
                Toast.makeText(view.getContext(),R.string.toast_msg_renew_successful,Toast.LENGTH_LONG).show();
                actionDialog.dismiss();
            }
        });
        bt_give.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new AlertDialog.Builder(view.getContext())
                        .setTitle(R.string.alert_title_confirm_give)
                        .setMessage(R.string.alert_text_confirm_give)
                        .setPositiveButton(R.string.alert_button_yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                data.giveConcession(concession.getIdConcession());
                                actionDialog.dismiss();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton(R.string.alert_button_no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();
            }
        });
        bt_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new AlertDialog.Builder(view.getContext())
                        .setTitle(R.string.alert_title_confirm_remove)
                        .setMessage(R.string.alert_text_confirm_remove)
                        .setPositiveButton(R.string.alert_button_yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                data.removeConcession(concession.getIdConcession());
                                actionDialog.dismiss();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton(R.string.alert_button_no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();
            }
        });
        bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new AlertDialog.Builder(view.getContext())
                        .setTitle(R.string.alert_title_confirm_delete)
                        .setMessage(R.string.alert_text_confirm_delete)
                        .setPositiveButton(R.string.alert_button_yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                data.deleteConcession(concession.getIdConcession(),concession.getState());
                                actionDialog.dismiss();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton(R.string.alert_button_no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();
            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionDialog.dismiss();
            }
        });

        actionDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                myBookViewModel.setActionDialogDisplayed(false);
            }
        });
    }

    /**
     * Initialise un layout de dialogue
     */
    private void inflateScanerDialog(){
        scanerCustomLayout = getActivity()
                .getLayoutInflater()
                .inflate(R.layout.dialog_barcode_prompt, null);

        scanerDialog = new AlertDialog
                .Builder(getContext())
                .setView(scanerCustomLayout)
                .create();

        scanerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        final EditText et_barCode = scanerCustomLayout.findViewById(R.id.dialog_barcode_et_code);

        et_barCode.setText(myBookViewModel.getBarcode());

        et_barCode.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){}
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {myBookViewModel.setBarcode(charSequence.toString());}
            @Override public void afterTextChanged(Editable editable){}
        });

        scanerCustomLayout
                .findViewById(R.id.dialog_barcode_bt_scan)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final PhotoBarcodeScanner photoBarcodeScanner = new PhotoBarcodeScannerBuilder()
                                .withActivity(getActivity())
                                .withFocusOnTap(true)
                                .withResultListener((Barcode barcode) -> {
                                    et_barCode.setText(barcode.rawValue);
                                })
                                .build();
                        photoBarcodeScanner.start();
                    }
                });

        scanerCustomLayout
                .findViewById(R.id.dialog_barcode_bt_validate)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final EditText evBarCode = scanerCustomLayout.findViewById(R.id.dialog_barcode_et_code);
                        if(evBarCode.getText().length()<=13&&evBarCode.getText().length()>=10){
                            data.tryAutoFill(evBarCode.getText().toString());
                            Intent intent = new Intent(view.getContext(), AddBookActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra(Const.BARCODE_TO_SEARCH,evBarCode.getText().toString());
                            startActivity(intent);
                            scanerDialog.dismiss();}
                        else {
                            Toast.makeText(view.getContext(),R.string.toast_msg_invalid_barcode1,Toast.LENGTH_LONG).show();
                        }
                    }
                });

        scanerCustomLayout
                .findViewById(R.id.dialog_barcode_bt_cancel)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        scanerDialog.dismiss();
                    }
                });

        scanerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                myBookViewModel.setScanerDialogDisplayed(false);
                myBookViewModel.resetCache();
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
        data.fetchMyConcessions();
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
        this.getContext().registerReceiver(broadcastReceiverFetch, new IntentFilter(Const.broadcastfetchMyConcessions));
        this.getContext().registerReceiver(broadcastReceiverChangeConcessionState, new IntentFilter(Const.broadcastChangeConcessionState));
        refreshList();
    }

    /**
     * Arrête l'enregistrement des broadcast receiver
     */
    @Override
    public void onPause() {
        super.onPause();
        this.getContext().unregisterReceiver(broadcastReceiverFetch);
        this.getContext().unregisterReceiver(broadcastReceiverChangeConcessionState);
    }

    private BroadcastReceiver broadcastReceiverFetch = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            progressBar.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
            refreshList();
        }
    };

    private BroadcastReceiver broadcastReceiverChangeConcessionState = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            data.fetchMyConcessions();
        }
    };
}