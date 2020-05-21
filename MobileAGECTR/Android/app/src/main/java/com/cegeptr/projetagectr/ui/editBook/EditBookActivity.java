package com.cegeptr.projetagectr.ui.editBook;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.cegeptr.projetagectr.R;
import com.cegeptr.projetagectr.logic.Const;
import com.cegeptr.projetagectr.logic.DataSingleton;
import com.cegeptr.projetagectr.logic.Entity.Concession;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class EditBookActivity extends AppCompatActivity {

    private EditBookViewModel editBookViewModel;
    private DataSingleton data = DataSingleton.getInstance();

    private TextView label_expiratioin, tv_DateExpiration, label_refuse, tv_refuse, tv_barcode, tv_author, tv_edition, et_price, tv_publisher, tv_title, tv_annotated;
    private ImageView imageView;

    /**
     * Initialise le layout de l'activité
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        editBookViewModel = ViewModelProviders.of(this, new EditBookModelFactory(this.getApplication(), getIntent())).get(EditBookViewModel.class);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        Toolbar myToolbar = findViewById(R.id.activity_edit_book_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button bt_confirm = findViewById(R.id.activity_edit_book_btConfirm);
        Button bt_archive = findViewById(R.id.activity_edit_book_btArchive);

        imageView = findViewById(R.id.activity_edit_book_imageView);
        label_expiratioin = findViewById(R.id.activity_edit_book_label_date_exp);
        tv_DateExpiration = findViewById(R.id.activity_edit_book_tv_date_exp);
        label_refuse = findViewById(R.id.activity_edit_book_label_refuse);
        tv_refuse = findViewById(R.id.activity_edit_book_tv_refused);
        tv_barcode = findViewById(R.id.activity_edit_book_tv_code);
        tv_author = findViewById(R.id.activity_edit_book_tv_author);
        tv_edition = findViewById(R.id.activity_edit_book_tv_edition);
        et_price = findViewById(R.id.activity_edit_book_et_price);
        tv_publisher = findViewById(R.id.activity_edit_book_tv_publisher);
        tv_title = findViewById(R.id.activity_edit_book_tv_title);
        tv_annotated = findViewById(R.id.activity_edit_book_tv_annotated);


        et_price.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){}
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {editBookViewModel.setPrice((isNumeric(charSequence.toString()))?Double.parseDouble(charSequence.toString()):0);}
            @Override public void afterTextChanged(Editable editable){}
        });

        String state = editBookViewModel.getLoadedConcession().getState();
        if(state.equals(Const.STATE_DENIED)){
            label_refuse.setVisibility(View.VISIBLE);
            tv_refuse.setVisibility(View.VISIBLE);
            tv_refuse.setText(editBookViewModel.getLoadedConcession().getRefuseReason());
            bt_confirm.setVisibility(View.GONE);
            bt_archive.setVisibility(View.VISIBLE);
        }
        else if(state.equals(Const.STATE_ACCEPT) || state.equals(Const.STATE_TO_RENEW)){
            label_expiratioin.setVisibility(View.VISIBLE);
            tv_DateExpiration.setVisibility(View.VISIBLE);
            tv_DateExpiration.setText(editBookViewModel.getLoadedConcession().getExpireDate());
            bt_confirm.setVisibility(View.VISIBLE);
            bt_archive.setVisibility(View.GONE);
        }
        DecimalFormat twoDForm = new DecimalFormat("0.00");

        tv_barcode.setText(editBookViewModel.getLoadedConcession().getBook().getBarcode());
        String author = editBookViewModel.getLoadedConcession().getBook().getAuthor();
        tv_author.setText(author== null || author.trim().equals("")?"--":author);
        String edition = editBookViewModel.getLoadedConcession().getBook().getEdition();
        tv_edition.setText(edition== null ||edition.trim().equals("")?"--":edition);
        et_price.setText(twoDForm.format(editBookViewModel.getPrice()));
        String publisher = editBookViewModel.getLoadedConcession().getBook().getPublisher();
        tv_publisher.setText(publisher== null || publisher.trim().equals("")?"--":publisher);
        tv_title.setText(editBookViewModel.getLoadedConcession().getBook().getTitle());
        tv_annotated.setText((editBookViewModel.getLoadedConcession().getAnnotated())? getString(R.string.yes_display) : getString(R.string.no_display));

        String bookState = editBookViewModel.getLoadedConcession().getState();
        if(bookState.equals(Const.STATE_ACCEPT) || bookState.equals(Const.STATE_PENDING) || bookState.equals(Const.STATE_UPDATE) ){
            et_price.setEnabled(true);
        }
        else{
            et_price.setEnabled(false);
        }

        String img_url =((editBookViewModel.getLoadedConcession().getUrlPhoto() == null) ?
                Const.BOOK_IMG_ADDRESS+editBookViewModel.getLoadedConcession().getBook().getUrlPhoto()+".png":
                Const.CONCESSION_IMG_ADDRESS+editBookViewModel.getLoadedConcession().getUrlPhoto()+".png"
        );
        Picasso
                .get()
                .load(img_url)
                .placeholder(R.drawable.book_default)
                .error(R.drawable.book_default)
                .into(imageView);


        bt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String price=et_price.getText().toString().replace(",",".");
                 if (price.trim().equals("") || price.trim().equals(".")) {
                    Toast.makeText(EditBookActivity.this, R.string.toast_msg_need_to_enter_price, Toast.LENGTH_LONG).show();
                    return;
                } else if (Double.parseDouble(price) >= 999.99) {
                    Toast.makeText(EditBookActivity.this, R.string.toast_msg_price_to_high, Toast.LENGTH_LONG).show();
                    return;
                } else if (Double.parseDouble(price) <= 0.50) {
                     Toast.makeText(EditBookActivity.this, R.string.toast_msg_price_to_low, Toast.LENGTH_LONG).show();
                     return;
                 }else {
                    if (editBookViewModel.getOriginalPrice() != Double.parseDouble(price)) {
                        Concession updatedConcession = new Concession(
                                editBookViewModel.getLoadedConcession().getState(),
                                editBookViewModel.getLoadedConcession().getUrlPhoto(),
                                editBookViewModel.getLoadedConcession().getReservedExpireDate(),
                                editBookViewModel.getLoadedConcession().getExpireDate(),
                                editBookViewModel.getLoadedConcession().getRefuseReason(),
                                Double.parseDouble(price.trim()),
                                editBookViewModel.getLoadedConcession().getSellingPrice(),
                                editBookViewModel.getLoadedConcession().getIdConcession(),
                                data.getConnectedUser().getIdUser(),
                                editBookViewModel.getLoadedConcession().getBook(),
                                editBookViewModel.getLoadedConcession().getAnnotated()
                        );
                        ProgressBar progressBar = findViewById(R.id.activity_edit_book_progressBar);
                        progressBar.setVisibility(View.VISIBLE);
                        data.updateConcession(updatedConcession);
                    }
                    else{
                        finish();
                    }
                }
            }
        });
        bt_archive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressBar progressBar = findViewById(R.id.activity_edit_book_progressBar);
                progressBar.setVisibility(View.VISIBLE);
                data.archiveConcession(editBookViewModel.getLoadedConcession().getIdConcession());
            }
        });
    }

    /**
     * Enregistre pour chaque touche appuyer
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            confirmQuiting();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * Changement dans le menu
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            confirmQuiting();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Confirme le fait de quitter
     */
    public void confirmQuiting() {

        if (!isNumeric(et_price.getText().toString())||editBookViewModel.getOriginalPrice() != Double.parseDouble(et_price.getText().toString())) {
            Dialog dialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.alert_title_leave)
                    .setMessage(R.string.alert_text_save_before_leave)
                    .setPositiveButton(R.string.alert_button_yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            finish();
                        }
                    })
                    .setNegativeButton(R.string.alert_button_no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    }).create();
            dialog.show();
        } else {
            finish();
        }

    }

    /**
     * Détermine si le string est un numérique
     * @param strNum
     * @return return si le string est un nombre
     */
    public boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    /*================================Broadcast================================*/
    /**
     * Enregistre les Broadcast Receiver
     */
    @Override
    protected void onResume() {
        super.onResume();
        this.registerReceiver(broadcastReceiverSave, new IntentFilter(Const.broadcastupdateConcession));
        this.registerReceiver(broadcastReceiverArchive, new IntentFilter(Const.broadcastArchiveConcession));
    }

    /**
     * Arrête l'enregistrement des broadcast receiver
     */
    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(broadcastReceiverSave);
        this.unregisterReceiver(broadcastReceiverArchive);
    }

    /**
     * Broadcast Receiver de la modification
     */
    private BroadcastReceiver broadcastReceiverSave = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(getApplicationContext(), R.string.toast_msg_edit_successful, Toast.LENGTH_SHORT).show();
            ProgressBar progressBar = findViewById(R.id.activity_edit_book_progressBar);
            progressBar.setVisibility(View.GONE);
            data.fetchMyConcessions();
            finish();
        }
    };

    /**
     * Broadcast Receiver de l'archivage
     */
    private BroadcastReceiver broadcastReceiverArchive = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(getApplicationContext(), R.string.toast_msg_archive_concession_successful, Toast.LENGTH_SHORT).show();
            ProgressBar progressBar = findViewById(R.id.activity_edit_book_progressBar);
            progressBar.setVisibility(View.GONE);
            data.fetchMyConcessions();
            finish();
        }
    };
}
