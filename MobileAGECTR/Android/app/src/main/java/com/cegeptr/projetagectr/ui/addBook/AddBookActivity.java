package com.cegeptr.projetagectr.ui.addBook;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.cegeptr.projetagectr.R;
import com.cegeptr.projetagectr.logic.Const;
import com.cegeptr.projetagectr.logic.DataSingleton;
import com.cegeptr.projetagectr.logic.Entity.Book;
import com.cegeptr.projetagectr.logic.Entity.Concession;
import com.google.android.gms.vision.barcode.Barcode;
import com.ivan200.photobarcodelib.PhotoBarcodeScanner;
import com.ivan200.photobarcodelib.PhotoBarcodeScannerBuilder;

public class AddBookActivity extends AppCompatActivity {

    private AddBookViewModel addBookViewModel;
    private Uri uriImage = null;
    private DataSingleton data = DataSingleton.getInstance();
    private ImageView imageView;
    private EditText et_barcode, et_author, et_edition, et_price, et_publisher, et_title;
    private CheckBox cb_annotated;
    private Button bt_confirm;
    private View scanerCustomLayout;
    private Dialog scanerDialog;

    /**
     * Initialise le layout de l'activité
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        addBookViewModel = ViewModelProviders.of(this).get(AddBookViewModel.class);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        Toolbar myToolbar = findViewById(R.id.activity_add_book_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView add_photo = findViewById(R.id.activity_add_book_add_photo);
        bt_confirm = findViewById(R.id.activity_add_book_btConfirm);
        Button bt_scan = findViewById(R.id.activity_add_book_bt_scan);

        imageView = findViewById(R.id.activity_add_book_imageView);
        et_title = findViewById(R.id.activity_add_book_tv_title);
        et_edition = findViewById(R.id.activity_add_book_tv_edition);
        et_author = findViewById(R.id.activity_add_book_tv_author);
        et_barcode = findViewById(R.id.activity_add_book_et_code);
        et_publisher = findViewById(R.id.activity_add_book_tv_publisher);
        et_price = findViewById(R.id.activity_add_book_tv_price);
        cb_annotated = findViewById(R.id.activity_add_book_cb_annotated);

        et_barcode.setText(getIntent().getStringExtra(Const.BARCODE_TO_SEARCH));
        et_title.setText(addBookViewModel.getTitle());
        et_edition.setText(addBookViewModel.getEdition());
        et_author.setText(addBookViewModel.getAuthor());
        et_publisher.setText(addBookViewModel.getPublisher());
        et_price.setText(String.valueOf(addBookViewModel.getPrice()));
        cb_annotated.setChecked(addBookViewModel.isAnnotated());
        imageView.setImageURI(addBookViewModel.getUriImage());

        et_title.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){}
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {addBookViewModel.setTitle(charSequence.toString());}
            @Override public void afterTextChanged(Editable editable){}
        });
        et_edition.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){}
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {addBookViewModel.setEdition(charSequence.toString());}
            @Override public void afterTextChanged(Editable editable){}
        });
        et_author.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){}
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {addBookViewModel.setAuthor(charSequence.toString());}
            @Override public void afterTextChanged(Editable editable){}
        });
        et_publisher.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){}
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {addBookViewModel.setPublisher(charSequence.toString());}
            @Override public void afterTextChanged(Editable editable){}
        });
        et_price.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){}
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {addBookViewModel.setPrice((isNumeric(charSequence.toString()))?Double.parseDouble(charSequence.toString()):0);}
            @Override public void afterTextChanged(Editable editable){}
        });
        cb_annotated.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                addBookViewModel.setAnnotated(isChecked);
            }
        });

        bt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String price =et_price.getText().toString().replace(",",".") ;
                if (price.trim().equals("")) {
                    Toast.makeText(AddBookActivity.this, R.string.toast_msg_invalid_barcode3, Toast.LENGTH_LONG).show();
                    return;
                } else if (price.trim().equals("")) {
                    Toast.makeText(AddBookActivity.this, R.string.toast_msg_need_to_enter_title, Toast.LENGTH_LONG).show();
                    return;
                } else if (price.trim().equals("") || et_price.getText().toString().trim().equals(".")) {
                    Toast.makeText(AddBookActivity.this, R.string.toast_msg_need_to_enter_price, Toast.LENGTH_LONG).show();
                    return;
                } else if (Double.parseDouble(price) >= 999.99) {
                    Toast.makeText(AddBookActivity.this, R.string.toast_msg_price_to_high, Toast.LENGTH_LONG).show();
                    return;
                }
                else if (Double.parseDouble(price) <= 0.50) {
                    Toast.makeText(AddBookActivity.this, R.string.toast_msg_price_to_low, Toast.LENGTH_LONG).show();
                    return;
                } else {

                    Book newbook = new Book(
                            (data.getAutoFillResult() != null)? data.getAutoFillResult().getIdBook() : 0,
                            et_title.getText().toString(),
                            et_author.getText().toString(),
                            et_publisher.getText().toString(),
                            et_edition.getText().toString(),
                            et_barcode.getText().toString(),
                            null
                    );
                    Concession newConcession = new Concession(
                            Const.STATE_PENDING,
                            null,
                            "",
                            "",
                            "",
                            Double.parseDouble(price),
                            0,
                            0,
                            data.getConnectedUser().getIdUser(),
                            newbook,
                            cb_annotated.isChecked()
                    );
                    ProgressBar progressBar = findViewById(R.id.activity_add_book_progressBar);
                    bt_confirm.setEnabled(false);
                    progressBar.setVisibility(View.VISIBLE);
                    data.addConcession(newConcession);
                }

            }
        });

        bt_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inflateScanerDialog();
                scanerDialog.show();
                addBookViewModel.setScanerDialogDisplayed(true);
            }
        });

        //Code pour l'appareil photo.
        add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {

                        String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

                        requestPermissions(permission, Const.IMAGE_PERMISSION_CODE);
                    } else {
                        SelectImage();
                    }
                }
            }
        });

        /*========================Reopen Dialog========================*/
        if(addBookViewModel.isScanerDialogDisplayed()){
            inflateScanerDialog();
            scanerDialog.show();
        }
    }

    /**
     * Demande les permissions à l'utilisateur
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case Const.IMAGE_PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SelectImage();
                } else {
                    Toast.makeText(this, R.string.toast_msg_permissions_refused, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /**
     * Au résultat de l'activité
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == Const.IMAGE_CAPTURE_CODE) {
                imageView.setImageURI(uriImage);
                addBookViewModel.setUriImage(uriImage);

            } else if (requestCode == Const.GALLERY_REQUEST_CODE) {

                uriImage = data.getData();
                imageView.setImageURI(uriImage);
                addBookViewModel.setUriImage(uriImage);
            }
        }
    }

    /**
     * Quand on appui sur une touche
     * @param keyCode
     * @param event
     * @return La touche appuyer
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
     * @return Return si l'item existe
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
     * Permet de sélectionner une image dans la gallerie
     */
    private void SelectImage() {

        final CharSequence[] items = {
                getResources().getString(R.string.alert_button_photo),
                getResources().getString(R.string.alert_button_gallery),
                getResources().getString(R.string.alert_button_cancel)
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.alert_title_pick_img));

        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals(getResources().getString(R.string.alert_button_photo))) {

                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, "Nouvelle image");
                    values.put(MediaStore.Images.Media.DESCRIPTION, "Description de la nouvelle image");
                    uriImage = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriImage);
                    startActivityForResult(cameraIntent, Const.IMAGE_CAPTURE_CODE);

                } else if (items[i].equals(getResources().getString(R.string.alert_button_gallery))) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, Const.GALLERY_REQUEST_CODE);

                } else if (items[i].equals(getResources().getString(R.string.alert_button_cancel))) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }

    /**
     * Confirme le fait de quitter
     */
    public void confirmQuiting() {
        Dialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.alert_title_leave)
                .setMessage(R.string.alert_text_leave)
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

    /*================================Dialog================================*/
    /**
     * Ouvre le scanner de code barre
     */
    private void inflateScanerDialog(){
        scanerCustomLayout = AddBookActivity.this
                .getLayoutInflater()
                .inflate(R.layout.dialog_barcode_prompt, null);

        scanerDialog = new AlertDialog
                .Builder(this)
                .setView(scanerCustomLayout)
                .create();

        scanerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        final EditText et_barCode = scanerCustomLayout.findViewById(R.id.dialog_barcode_et_code);
        final EditText barCode = findViewById(R.id.activity_add_book_et_code);

        et_barCode.setText(addBookViewModel.getScanBarcode());

        et_barCode.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){}
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {addBookViewModel.setScanBarcode(charSequence.toString());}
            @Override public void afterTextChanged(Editable editable){}
        });

        scanerCustomLayout
                .findViewById(R.id.dialog_barcode_bt_scan)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final PhotoBarcodeScanner photoBarcodeScanner = new PhotoBarcodeScannerBuilder()
                                .withActivity(AddBookActivity.this)
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
                        if (et_barCode.getText().length() <= 13 && et_barCode.getText().length() >= 10) {
                            data.tryAutoFill(et_barCode.getText().toString());
                            barCode.setText(et_barCode.getText().toString());
                            scanerDialog.dismiss();
                        } else {
                            Toast.makeText(AddBookActivity.this, R.string.toast_msg_invalid_barcode1, Toast.LENGTH_LONG).show();
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
                addBookViewModel.setScanerDialogDisplayed(false);
                addBookViewModel.resetCache();
            }
        });

    }

    /*================================Broadcast================================*/
    /**
     * Enregistre les Broadcast Receiver
     */
    @Override
    protected void onResume() {
        super.onResume();
        this.registerReceiver(broadcastReceiverAutoFill, new IntentFilter(Const.broadcastTryAutoFill));
        this.registerReceiver(broadcastReceiverSave, new IntentFilter(Const.broadcastaddConcession));
        this.registerReceiver(broadcastReceiverUploadImg, new IntentFilter(Const.broadcastupload_photo));
    }

    /**
     * Arrête l'enregistrement des broadcast receiver
     */
    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(broadcastReceiverAutoFill);
        this.unregisterReceiver(broadcastReceiverSave);
        this.unregisterReceiver(broadcastReceiverUploadImg);
    }

    /**
     * Broadcast Receiver de l'autofill
     */
    private BroadcastReceiver broadcastReceiverAutoFill = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (data.getAutoFillResult() != null) {
                et_author.setText(data.getAutoFillResult().getAuthor());
                et_title.setText(data.getAutoFillResult().getTitle());
                et_edition.setText(data.getAutoFillResult().getEdition());
                et_publisher.setText(data.getAutoFillResult().getPublisher());

                et_publisher.setEnabled(false);
                et_title.setEnabled(false);
                et_author.setEnabled(false);
                et_edition.setEnabled(false);
                Toast.makeText(AddBookActivity.this, R.string.toast_msg_valid_barcode, Toast.LENGTH_LONG).show();
            } else {
                et_publisher.setEnabled(true);
                et_title.setEnabled(true);
                et_author.setEnabled(true);
                et_edition.setEnabled(true);
                Toast.makeText(AddBookActivity.this, R.string.toast_msg_invalid_barcode2, Toast.LENGTH_LONG).show();
            }
        }
    };

    /**
     * Broadcast Receiver de l'enregistrement
     */
    private BroadcastReceiver broadcastReceiverSave = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getIntExtra(Const.ID_OF_ADDED_CONCESSION, 0) == 0) {
                ProgressBar progressBar = findViewById(R.id.activity_add_book_progressBar);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), R.string.toast_msg_add_failed, Toast.LENGTH_SHORT).show();
            } else {
                if (uriImage == null) {
                    Toast.makeText(getApplicationContext(), R.string.toast_msg_add_successful, Toast.LENGTH_SHORT).show();
                    ProgressBar progressBar = findViewById(R.id.activity_add_book_progressBar);
                    progressBar.setVisibility(View.GONE);
                    bt_confirm.setEnabled(true);
                    data.fetchMyConcessions();
                    finish();
                } else {
                    data.uploadPhoto(uriImage, intent.getIntExtra(Const.ID_OF_ADDED_CONCESSION, 0));
                }
            }
        }
    };

    /**
     * Broadcast Receiver de l'upload d'image
     */
    private BroadcastReceiver broadcastReceiverUploadImg = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ProgressBar progressBar = findViewById(R.id.activity_add_book_progressBar);
            progressBar.setVisibility(View.GONE);

            if (intent.getBooleanExtra(Const.REQUEST_SUCCES, false)) {
                Toast.makeText(getApplicationContext(), R.string.toast_msg_add_successful, Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), R.string.toast_msg_add_failed, Toast.LENGTH_SHORT).show();
            }
        }
    };

}
