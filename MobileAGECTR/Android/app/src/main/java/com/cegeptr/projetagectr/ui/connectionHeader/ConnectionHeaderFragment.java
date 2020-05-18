package com.cegeptr.projetagectr.ui.connectionHeader;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.cegeptr.projetagectr.R;
import com.cegeptr.projetagectr.logic.Const;
import com.cegeptr.projetagectr.logic.DataSingleton;
import com.cegeptr.projetagectr.logic.Entity.User;
import com.cegeptr.projetagectr.logic.MaskWatcher;

import java.util.regex.Pattern;

public class ConnectionHeaderFragment extends Fragment {

    private ConnectionHeaderViewModel connectionHeaderViewModel;
    private TextView tvConnexion, tvCreateAccount;
    private DataSingleton dataSingleton;
    private View loginCustomLayout, createAccountCustomLayout, resetPasswordCustomLayout;
    private Dialog loginDialog, createAccountDialog, resetPasswordAccountDialog;
    private SharedPreferences sharedpreferences;

    public static ConnectionHeaderFragment newInstance() {
        return new ConnectionHeaderFragment();
    }

    private View root;

    /**
     * Initialise le layout du fragment
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        connectionHeaderViewModel = ViewModelProviders.of(this).get(ConnectionHeaderViewModel.class);
        root = inflater.inflate(R.layout.fragment_connection_header, container, false);

        sharedpreferences = getActivity().getSharedPreferences(Const.MY_PREFERENCES, Context.MODE_PRIVATE);

        tvConnexion = root.findViewById(R.id.tvConnexion);
        tvCreateAccount = root.findViewById(R.id.tvCreateAccount);

        dataSingleton = DataSingleton.getInstance();

        tvConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inflateLoginDialog();
                loginDialog.show();
                connectionHeaderViewModel.setLoginDialogDisplayed(true);
            }
        });


        tvCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inflateCreateAccountDialog();
                createAccountDialog.show();
                connectionHeaderViewModel.setCreateAccountDialogDisplayed(true);
            }
        });

        /*========================Reopen Dialog========================*/
        if(connectionHeaderViewModel.isLoginDialogDisplayed()){
            inflateLoginDialog();
            loginDialog.show();
        }
        else if(connectionHeaderViewModel.isCreateAccountDialogDisplayed()){
            inflateCreateAccountDialog();
            createAccountDialog.show();
        }
        else if(connectionHeaderViewModel.isResetPasswordDialogDisplayed()){
            inflateRessetPasswordDialog();
            resetPasswordAccountDialog.show();
        }

        return root;
    }

    /**
     * Création du compte utilisateur
     * @return
     */
    private boolean tryCreateUser() {
        ProgressBar progressBar = createAccountDialog.findViewById(R.id.dialog_create_account_progressBar);
        CheckBox cb_condition = createAccountCustomLayout.findViewById(R.id.dialog_create_account_cb_condition);
        EditText matricule = createAccountCustomLayout.findViewById(R.id.dialog_create_account_et_matricule);
        EditText firstName = createAccountCustomLayout.findViewById(R.id.dialog_create_account_et_prenom);
        EditText lastName = createAccountCustomLayout.findViewById(R.id.dialog_create_account_et_nom);
        EditText phone = createAccountCustomLayout.findViewById(R.id.dialog_create_account_et_tel);
        EditText email = createAccountCustomLayout.findViewById(R.id.dialog_create_account_et_courriel);
        EditText password = createAccountCustomLayout.findViewById(R.id.dialog_create_account_et_mdp);
        EditText confirmPassword = createAccountCustomLayout.findViewById(R.id.dialog_create_account_et_mdp_conf);
        String validPhone = "";

        if (!cb_condition.isChecked()) {
            Toast.makeText(createAccountCustomLayout.getContext(), R.string.toast_msg_need_to_accept_condition, Toast.LENGTH_LONG).show();
            return false;
        } else if (firstName.getText().toString().trim().equals("")) {
            Toast.makeText(createAccountCustomLayout.getContext(), R.string.toast_msg_need_to_enter_firstName, Toast.LENGTH_LONG).show();
            return false;
        } else if (lastName.getText().toString().trim().equals("")) {
            Toast.makeText(createAccountCustomLayout.getContext(), R.string.toast_msg_need_to_enter_lastName, Toast.LENGTH_LONG).show();
            return false;
        } else if (password.getText().toString().trim().equals("")) {
            Toast.makeText(createAccountCustomLayout.getContext(), R.string.toast_msg_need_to_enter_password, Toast.LENGTH_LONG).show();
            return false;
        } else if (email.getText().toString().trim().equals("")) {
            Toast.makeText(createAccountCustomLayout.getContext(), R.string.toast_msg_need_to_enter_email, Toast.LENGTH_LONG).show();
            return false;
        }else if(email.getText().toString().trim().contains(Const.DENY_EMAIL)){
            Toast.makeText(this.getContext(),R.string.toast_msg_deny_cegep_email,Toast.LENGTH_LONG).show();
            return false;
        } else if(!checkPassword(password.getText().toString().trim())){
            Toast.makeText(createAccountCustomLayout.getContext(), R.string.toast_msg_need_to_enter_valid_password, Toast.LENGTH_LONG).show();
            return false;
        }else if(!password.getText().toString().trim().equals(confirmPassword.getText().toString().trim())){
            Toast.makeText(createAccountCustomLayout.getContext(), R.string.toast_msg_need_to_enter_matching_new_password, Toast.LENGTH_LONG).show();
            return false;
        }else if(phone.getText().toString().length()<14&&phone.getText().toString().length()>0){
            Toast.makeText(this.getContext(), R.string.toast_msg_deny_phone_number, Toast.LENGTH_LONG).show();
            return false;
        }else if(phone.getText().toString().length()==0){
            validPhone = "";
        }else {
            validPhone =phone.getText().toString().substring(1, 4)+phone.getText().toString().substring(6, 9)+phone.getText().toString().substring(10, 14);
        }
        User createdUser = new User(
                0,
                1,
                matricule.getText().toString(),
                firstName.getText().toString(),
                lastName.getText().toString(),
                validPhone,
                email.getText().toString(),
                password.getText().toString()
        );
        dataSingleton.addUser(createdUser);
        progressBar.setVisibility(View.VISIBLE);
        return true;
    }

    /**
     * Vérifie si le compte est valide
     * @param arg
     * @return Retourne si le mot de passe est valide
     */
    public boolean checkPassword(String arg) {
        return Pattern.compile("^(?=.{8,})(?=.*[a-z])(?=.*[A-Z])(?=.*[!?*()@#$%^&+=]).*$").matcher(arg).find();
    }


    /*================================Dialog================================*/
    /**
     * Initialise un layout de dialogue
     */
    private void inflateLoginDialog(){
        loginCustomLayout = getActivity()
                .getLayoutInflater()
                .inflate(R.layout.dialog_login, null);

        loginDialog = new AlertDialog
                .Builder(getContext())
                .setView(loginCustomLayout)
                .create();

        loginDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        final ProgressBar progressBar = loginCustomLayout.findViewById(R.id.dialog_login_progressBar);
        final EditText etEmail = loginCustomLayout.findViewById(R.id.dialog_login_etEmail);
        final EditText etPassword = loginCustomLayout.findViewById(R.id.dialog_login_tbMotdepasse);
        final CheckBox cb_keep_loged = loginCustomLayout.findViewById(R.id.dialog_login_cb_keep_loged);
        final TextView tv_reset_password = loginCustomLayout.findViewById(R.id.dialog_login_tv_reset_password);

        cb_keep_loged.setChecked(connectionHeaderViewModel.isKeepConnected());
        etPassword.setText(connectionHeaderViewModel.getPassword());
        etEmail.setText(connectionHeaderViewModel.getEmail());

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){}
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {connectionHeaderViewModel.setPassword(charSequence.toString());}
            @Override public void afterTextChanged(Editable editable){}
        });
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){}
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {connectionHeaderViewModel.setEmail(charSequence.toString());}
            @Override public void afterTextChanged(Editable editable){}
        });
        cb_keep_loged.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                connectionHeaderViewModel.setKeepConnected(b);
            }
        });

        loginCustomLayout
                .findViewById(R.id.dialog_login_bt_Connexion)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String email = etEmail.getText().toString();
                        String password = etPassword.getText().toString();

                        dataSingleton.checkLogin(email, password);
                        progressBar.setVisibility(View.VISIBLE);
                        etEmail.requestFocus();
                    }
                });

        loginCustomLayout
                .findViewById(R.id.dialog_login_bt_cancel)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        loginDialog.dismiss();
                    }
                });

        loginDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                connectionHeaderViewModel.setLoginDialogDisplayed(false);
                connectionHeaderViewModel.resetLoginCache();
            }
        });

        tv_reset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginDialog.dismiss();
                inflateRessetPasswordDialog();
                resetPasswordAccountDialog.show();
                connectionHeaderViewModel.setResetPasswordDialogDisplayed(true);
            }
        });
    }

    /**
     * Crée le layout du reset password
     */
    private void inflateRessetPasswordDialog(){
        resetPasswordCustomLayout = getActivity()
                .getLayoutInflater()
                .inflate(R.layout.dialog_reset_password, null);

        resetPasswordAccountDialog = new AlertDialog
                .Builder(getContext())
                .setView(resetPasswordCustomLayout)
                .create();

        resetPasswordAccountDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        final ProgressBar progressBar = resetPasswordCustomLayout.findViewById(R.id.dialog_reset_password_progressBar);
        final EditText etEmail = resetPasswordCustomLayout.findViewById(R.id.dialog_reset_password_etEmail);

        etEmail.setText(connectionHeaderViewModel.getEmailForReset());

        etEmail.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){}
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {connectionHeaderViewModel.setEmailForReset(charSequence.toString());}
            @Override public void afterTextChanged(Editable editable){}
        });

        resetPasswordCustomLayout
                .findViewById(R.id.dialog_reset_password_bt_reset)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dataSingleton.resetPassword(etEmail.getText().toString());
                        progressBar.setVisibility(View.VISIBLE);
                        //resetPasswordAccountDialog.dismiss();
                    }
                });

        resetPasswordCustomLayout
                .findViewById(R.id.dialog_reset_password_bt_cancel)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        resetPasswordAccountDialog.dismiss();
                    }
                });

        resetPasswordAccountDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                connectionHeaderViewModel.setResetPasswordDialogDisplayed(false);
                connectionHeaderViewModel.resetResetPasswordCache();

            }
        });
    }

    /**
     * Crée le layout de la création du compte
     */
    private void inflateCreateAccountDialog(){
        createAccountCustomLayout = getActivity()
                .getLayoutInflater()
                .inflate(R.layout.dialog_create_account, null);

        createAccountDialog = new AlertDialog
                .Builder(getContext())
                .setView(createAccountCustomLayout)
                .create();

        createAccountDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        final CheckBox cb_condition = createAccountCustomLayout.findViewById(R.id.dialog_create_account_cb_condition);
        final EditText matricule = createAccountCustomLayout.findViewById(R.id.dialog_create_account_et_matricule);
        final EditText firstName = createAccountCustomLayout.findViewById(R.id.dialog_create_account_et_prenom);
        final EditText lastName = createAccountCustomLayout.findViewById(R.id.dialog_create_account_et_nom);
        final EditText phone = createAccountCustomLayout.findViewById(R.id.dialog_create_account_et_tel);
        final EditText email = createAccountCustomLayout.findViewById(R.id.dialog_create_account_et_courriel);
        final EditText password = createAccountCustomLayout.findViewById(R.id.dialog_create_account_et_mdp);
        final EditText confirmPassword = createAccountCustomLayout.findViewById(R.id.dialog_create_account_et_mdp_conf);

        cb_condition.setChecked(connectionHeaderViewModel.isConditionChecked());
        matricule.setText(connectionHeaderViewModel.getMatricule());
        firstName.setText(connectionHeaderViewModel.getFirstName());
        lastName.setText(connectionHeaderViewModel.getLastName());
        phone.setText(connectionHeaderViewModel.getPhoneNumber());
        email.setText(connectionHeaderViewModel.getNewEmail());
        password.setText(connectionHeaderViewModel.getNewPassword());
        confirmPassword.setText(connectionHeaderViewModel.getNewPasswordConf());

        phone.addTextChangedListener(new MaskWatcher("(###)-### ####"));

        matricule.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){}
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {connectionHeaderViewModel.setMatricule(charSequence.toString());}
            @Override public void afterTextChanged(Editable editable){}
        });
        firstName.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){}
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {connectionHeaderViewModel.setFirstName(charSequence.toString());}
            @Override public void afterTextChanged(Editable editable){}
        });
        lastName.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){}
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {connectionHeaderViewModel.setLastName(charSequence.toString());}
            @Override public void afterTextChanged(Editable editable){}
        });
        phone.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){}
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {connectionHeaderViewModel.setPhoneNumber(charSequence.toString());}
            @Override public void afterTextChanged(Editable editable){}
        });
        email.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){}
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {connectionHeaderViewModel.setNewEmail(charSequence.toString());}
            @Override public void afterTextChanged(Editable editable){}
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){}
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {connectionHeaderViewModel.setNewPassword(charSequence.toString());}
            @Override public void afterTextChanged(Editable editable){}
        });
        confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){}
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {connectionHeaderViewModel.setNewPasswordConf(charSequence.toString());}
            @Override public void afterTextChanged(Editable editable){}
        });
        cb_condition.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                connectionHeaderViewModel.setConditionChecked(b);
            }
        });

        createAccountCustomLayout
                .findViewById(R.id.dialog_create_account_layout_condition)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Dialog dialog = new AlertDialog.Builder(view.getContext())
                                .setTitle(R.string.alert_title_terms_and_conditions)
                                .setMessage(R.string.alert_text_terms_and_conditions)
                                .setPositiveButton(R.string.alert_button_accept, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        cb_condition.setChecked(true);
                                        dialog.dismiss();
                                    }
                                })
                                .setNegativeButton(R.string.alert_button_refuse, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        cb_condition.setChecked(false);
                                        dialog.dismiss();
                                    }
                                }).create();
                        dialog.show();
                    }
                });

        createAccountCustomLayout
                .findViewById(R.id.dialog_create_account_bt_save)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tryCreateUser();
                    }
                });

        createAccountCustomLayout
                .findViewById(R.id.dialog_create_account_bt_cancel)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        createAccountDialog.dismiss();
                    }
                });
        createAccountDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                connectionHeaderViewModel.setCreateAccountDialogDisplayed(false);
                connectionHeaderViewModel.resetCreatAcountCache();
            }
        });
    }

    /*================================Broadcast================================*/
    /**
     * Enregistre les Broadcast Receiver
     */
    @Override
    public void onResume() {
        super.onResume();
        root.getContext().registerReceiver(broadcastCheckLoginReceiver, new IntentFilter(Const.broadcastcheckLogin));
        root.getContext().registerReceiver(broadcastAddUserReceiver, new IntentFilter(Const.broadcastaddUser));
        root.getContext().registerReceiver(broadcastResetPasswordReceiver, new IntentFilter(Const.broadcastResetPassword));
    }

    /**
     * Arrête l'enregistrement des broadcast receiver
     */
    @Override
    public void onPause() {
        super.onPause();
        root.getContext().unregisterReceiver(broadcastCheckLoginReceiver);
        root.getContext().unregisterReceiver(broadcastAddUserReceiver);
        root.getContext().unregisterReceiver(broadcastResetPasswordReceiver);
    }

    /**
     * Broadcast receiver pour la connexion
     */
    private BroadcastReceiver broadcastCheckLoginReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            EditText etEmail = loginCustomLayout.findViewById(R.id.dialog_login_etEmail);
            EditText etPassword = loginCustomLayout.findViewById(R.id.dialog_login_tbMotdepasse);
            ProgressBar progressBar = loginCustomLayout.findViewById(R.id.dialog_login_progressBar);
            TextView tvError = loginCustomLayout.findViewById(R.id.dialog_login_error);
            CheckBox cb_keep_loged = loginCustomLayout.findViewById(R.id.dialog_login_cb_keep_loged);

            progressBar.setVisibility(View.GONE);

            if (dataSingleton.getConnectedUser() != null) {
                tvError.setVisibility(View.GONE);
                loginDialog.dismiss();

                Intent intentToSend = new Intent();
                intentToSend.setAction(Const.broadcastRefreshDrawer);
                ConnectionHeaderFragment.this.getContext().sendBroadcast(intentToSend);

                SharedPreferences.Editor editor = sharedpreferences.edit();
                if(cb_keep_loged.isChecked()){
                    editor.putString(Const.PREFERENCE_HASHED_PASSWORD, etPassword.getText().toString());
                    editor.putString(Const.PREFERENCES_USERNAME, etEmail.getText().toString());
                    editor.apply();
                }else{
                    editor.clear();
                    editor.apply();
                }
            } else {
                tvError.setVisibility(View.VISIBLE);
                etEmail.setText(null);
                etPassword.setText(null);
            }
        }
    };

    /**
     * Broadcast receiver pour l'ajout d'utilisateur
     */
    private BroadcastReceiver broadcastAddUserReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ProgressBar progressBar = createAccountDialog.findViewById(R.id.dialog_create_account_progressBar);
            progressBar.setVisibility(View.GONE);

            if (dataSingleton.getConnectedUser() != null) {
                createAccountDialog.dismiss();

                Intent intentToSend = new Intent();
                intentToSend.setAction(Const.broadcastRefreshDrawer);
                ConnectionHeaderFragment.this.getContext().sendBroadcast(intentToSend);

            }
        }
    };

    /**
     * Broadcast receiver pour le nouveau mot de passe
     */
    private BroadcastReceiver broadcastResetPasswordReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ProgressBar progressBar = resetPasswordAccountDialog.findViewById(R.id.dialog_reset_password_progressBar);

            progressBar.setVisibility(View.GONE);

            if(intent.getBooleanExtra(Const.REQUEST_SUCCES,false)){
                Toast.makeText(context, R.string.toast_msg_password_reset_successful, Toast.LENGTH_LONG).show();
                resetPasswordAccountDialog.dismiss();
            }
            else{
                Toast.makeText(context, R.string.toast_msg_password_reset_failed, Toast.LENGTH_LONG).show();
            }

        }
    };

}
