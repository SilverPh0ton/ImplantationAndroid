package com.cegeptr.projetagectr.ui.info;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.cegeptr.projetagectr.R;
import com.cegeptr.projetagectr.logic.Const;
import com.cegeptr.projetagectr.logic.DataSingleton;
import com.cegeptr.projetagectr.logic.Entity.User;
import com.cegeptr.projetagectr.logic.MaskWatcher;

import java.util.regex.Pattern;

public class InfoFragment extends Fragment {

    private InfoViewModel infoViewModel;
    private DataSingleton data = DataSingleton.getInstance();
    private TextView tvNom, tvPrenom, tvCourriel, tvTel;
    private View root, passwordCustomLayout, infoCustomLayout, avatarCustomLayout;
    private Dialog passwordDialog, infoDialog, avatarDialog;
    private ImageView imageView;

    /**
     * Initialise le layout du fragment
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        infoViewModel = ViewModelProviders.of(this).get(InfoViewModel.class);
        root = inflater.inflate(R.layout.fragment_info, container, false);

        loadInfo();

        Button bt_edit_info = root.findViewById(R.id.fragment_info_bt_edit_info);
        Button bt_edit_password = root.findViewById(R.id.fragment_info_bt_edit_password);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inflateAvatarDialog();
                avatarDialog.show();
                infoViewModel.setAvatarDialogDisplayed(true);
            }
        });

        bt_edit_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inflateInfoDialog();
                infoDialog.show();
                infoViewModel.setInfoDialogDisplayed(true);
            }
        });

        bt_edit_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inflatePasswordDialog();
                passwordDialog.show();
                infoViewModel.setPasswordDialogDisplayed(true);
            }
        });

        /*========================Reopen Dialog========================*/
        if(infoViewModel.isAvatarDialogDisplayed()){
            inflateAvatarDialog();
            avatarDialog.show();
        }
        else if(infoViewModel.isInfoDialogDisplayed()){
            inflateInfoDialog();
            infoDialog.show();
        }
        else if(infoViewModel.isPasswordDialogDisplayed()){
            inflatePasswordDialog();
            passwordDialog.show();
        }

        return root;
    }

    /**
     * Verifie le changement d'information
     * @return
     */
    private boolean tryChangeInfo() {
        final EditText et_firstName = infoCustomLayout.findViewById(R.id.dialog_change_info_et_prenom);
        final EditText et_lastName = infoCustomLayout.findViewById(R.id.dialog_change_info_et_nom);
        final EditText et_phone = infoCustomLayout.findViewById(R.id.dialog_change_info_et_tel);
        final EditText et_matricule = infoCustomLayout.findViewById(R.id.dialog_change_info__et_matricule);
        final EditText et_mail = infoCustomLayout.findViewById(R.id.dialog_change_info_et_courriel);
        final ProgressBar progressBar = infoDialog.findViewById(R.id.dialog_change_info_progressBar);

        if (et_lastName.getText().toString().trim().equals("")) {
            Toast.makeText(this.getContext(), R.string.toast_msg_need_to_enter_lastName, Toast.LENGTH_LONG).show();
            return false;
        } else if (et_firstName.getText().toString().trim().equals("")) {
            Toast.makeText(this.getContext(), R.string.toast_msg_need_to_enter_firstName, Toast.LENGTH_LONG).show();
            return false;
        } else if (et_mail.getText().toString().trim().equals("")) {
            Toast.makeText(this.getContext(), R.string.toast_msg_need_to_enter_email, Toast.LENGTH_LONG).show();
            return false;
        } else if (et_mail.getText().toString().trim().contains(Const.DENY_EMAIL)) {
            Toast.makeText(this.getContext(), R.string.toast_msg_deny_cegep_email, Toast.LENGTH_LONG).show();
            return false;
        } else if (et_phone.getText().toString().length() != 14) {
            Toast.makeText(this.getContext(), R.string.toast_msg_deny_phone_number, Toast.LENGTH_LONG).show();
            return false;
        }
        User user = new User(
                data.getConnectedUser().getIdUser(),
                data.getConnectedUser().getAvatar(),
                et_matricule.getText().toString(),
                et_firstName.getText().toString(),
                et_lastName.getText().toString(),
                et_phone.getText().toString().substring(1, 4) + et_phone.getText().toString().substring(6, 9) + et_phone.getText().toString().substring(10, 14),
                et_mail.getText().toString(),
                null
        );
        progressBar.setVisibility(View.VISIBLE);
        data.updateUser(user);
        return true;
    }

    /**
     * Verifie le changement de mot de passe
     * @return
     */
    private boolean tryChangePassword() {
        final EditText et_passwordOld = passwordCustomLayout.findViewById(R.id.dialog_change_password_et_actuelmdp);
        final EditText et_passwordNew = passwordCustomLayout.findViewById(R.id.dialog_change_password_et_mdp);
        final EditText et_passwordConfirm = passwordCustomLayout.findViewById(R.id.dialog_change_password_et_mdp_conf);
        final ProgressBar progressBar = passwordDialog.findViewById(R.id.dialog_change_password_progressBar);

        if (et_passwordOld.getText().toString().trim().equals("")) {
            Toast.makeText(this.getContext(), R.string.toast_msg_need_to_enter_password, Toast.LENGTH_LONG).show();
            return false;
        } else if (et_passwordNew.getText().toString().trim().equals("")) {
            Toast.makeText(this.getContext(), R.string.toast_msg_need_to_enter_password, Toast.LENGTH_LONG).show();
            return false;
        } else if (et_passwordConfirm.getText().toString().trim().equals("")) {
            Toast.makeText(this.getContext(), R.string.toast_msg_need_to_enter_password, Toast.LENGTH_LONG).show();
            return false;
        } else if (!et_passwordNew.getText().toString().trim().equals(et_passwordConfirm.getText().toString().trim())) {
            Toast.makeText(this.getContext(), R.string.toast_msg_need_to_enter_matching_new_password, Toast.LENGTH_LONG).show();
            return false;
        } else if (!checkPassword(et_passwordNew.getText().toString().trim())) {
            Toast.makeText(this.getContext(), R.string.toast_msg_need_to_enter_valid_password, Toast.LENGTH_LONG).show();
            return false;
        }

        progressBar.setVisibility(View.VISIBLE);
        data.updatePassword(
                data.getConnectedUser().getIdUser(),
                et_passwordOld.getText().toString(),
                et_passwordNew.getText().toString()
        );
        return true;
    }

    /**
     * verifie si le mots de passe est valide
     * @param arg
     * @return
     */
    public boolean checkPassword(String arg) {
        return Pattern.compile("^(?=.{8,})(?=.*[a-z])(?=.*[A-Z])(?=.*[!?*()@#$%^&+=]).*$").matcher(arg).find();
    }

    /**
     * change l'avatar
     * @param num
     */
    private void pickAvatar(int num) {
        data.getConnectedUser().setAvatar(num);
        infoViewModel.setAvatar(num);
        User user = new User(
                data.getConnectedUser().getIdUser(),
                data.getConnectedUser().getAvatar(),
                data.getConnectedUser().getMatricule(),
                data.getConnectedUser().getFirstName(),
                data.getConnectedUser().getLastName(),
                data.getConnectedUser().getPhoneNumber(),
                data.getConnectedUser().getEmail(),
                null
        );
        data.updateAvatar(user);
        displayAvatar(num);
        avatarDialog.dismiss();
    }

    /**
     * Charger les info de l'utilisateur
     */
    private void loadInfo(){
        tvNom = root.findViewById(R.id.fragment_info_tv_nom);
        tvPrenom = root.findViewById(R.id.fragment_info_tv_prenom);
        tvCourriel = root.findViewById(R.id.fragment_info_tv_mail);
        tvTel = root.findViewById(R.id.fragment_info_tv_tel);
        imageView = root.findViewById(R.id.fragment_info_iv_avatar);

        String phone = data.getConnectedUser().getPhoneNumber();

        if (data.getConnectedUser() != null) {
            String phoneChache = data.getConnectedUser().getPhoneNumber();

            if (phoneChache!=null && phoneChache.length() >= 10) {
                tvTel.setText("(" + phoneChache.substring(0, 3) + ")-" + phoneChache.substring(3, 6) + " " + phoneChache.substring(6));
            }
            else{
                tvTel.setText("");
            }
            tvNom.setText(data.getConnectedUser().getLastName());
            tvPrenom.setText(data.getConnectedUser().getFirstName());
            tvCourriel.setText(data.getConnectedUser().getEmail());
            displayAvatar(data.getConnectedUser().getAvatar());
        }
    }

    /**
     * affiche le bon avatar
     * @param num
     */
    private void displayAvatar(int num) {
        switch (num) {
            case 1:
            default:
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.avatar1));
                break;
            case 2:
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.avatar2));
                break;
            case 3:
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.avatar3));
                break;
            case 4:
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.avatar4));
                break;
            case 5:
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.avatar5));
                break;
            case 6:
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.avatar6));
                break;
            case 7:
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.avatar7));
                break;
            case 8:
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.avatar8));
                break;
            case 9:
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.avatar9));
                break;
            case 10:
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.avatar10));
                break;
            case 11:
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.avatar11));
                break;
            case 12:
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.avatar12));
                break;
            case 13:
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.avatar0));
                break;
        }
    }

    /*================================Dialog================================*/
    /**
     * Initialise un layout de dialogue
     */
    private void inflateAvatarDialog(){
        avatarCustomLayout = getActivity()
                .getLayoutInflater()
                .inflate(R.layout.dialog_pick_avatar, null);

        avatarDialog = new AlertDialog
                .Builder(getContext())
                .setView(avatarCustomLayout)
                .create();

        avatarDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        avatarCustomLayout
                .findViewById(R.id.iv_avatar1)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pickAvatar(1);
                    }
                });
        avatarCustomLayout
                .findViewById(R.id.iv_avatar2)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pickAvatar(2);
                    }
                });
        avatarCustomLayout
                .findViewById(R.id.iv_avatar3)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pickAvatar(3);
                    }
                });
        avatarCustomLayout
                .findViewById(R.id.iv_avatar4)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pickAvatar(4);
                    }
                });
        avatarCustomLayout
                .findViewById(R.id.iv_avatar5)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pickAvatar(5);
                    }
                });
        avatarCustomLayout
                .findViewById(R.id.iv_avatar6)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pickAvatar(6);
                    }
                });
        avatarCustomLayout
                .findViewById(R.id.iv_avatar7)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pickAvatar(7);
                    }
                });
        avatarCustomLayout
                .findViewById(R.id.iv_avatar8)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pickAvatar(8);
                    }
                });
        avatarCustomLayout
                .findViewById(R.id.iv_avatar9)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pickAvatar(9);
                    }
                });
        avatarCustomLayout
                .findViewById(R.id.iv_avatar10)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pickAvatar(10);
                    }
                });
        avatarCustomLayout
                .findViewById(R.id.iv_avatar11)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pickAvatar(11);
                    }
                });
        avatarCustomLayout
                .findViewById(R.id.iv_avatar12)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pickAvatar(12);
                    }
                });
        avatarCustomLayout
                .findViewById(R.id.dialog_avatar_title)
                .setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        pickAvatar(13);
                        return false;
                    }
                });

        avatarDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                infoViewModel.setAvatarDialogDisplayed(false);
            }
        });
    }

    /**
     * Initialise un layout de dialogue
     */
    private void inflateInfoDialog(){
        infoCustomLayout = getActivity()
                .getLayoutInflater()
                .inflate(R.layout.dialog_change_info, null);

        infoDialog = new AlertDialog
                .Builder(getContext())
                .setView(infoCustomLayout)
                .create();

        infoDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        final EditText et_firstName = infoCustomLayout.findViewById(R.id.dialog_change_info_et_prenom);
        final EditText et_lastName = infoCustomLayout.findViewById(R.id.dialog_change_info_et_nom);
        final EditText et_phone = infoCustomLayout.findViewById(R.id.dialog_change_info_et_tel);
        final EditText et_matricule = infoCustomLayout.findViewById(R.id.dialog_change_info__et_matricule);
        final EditText et_mail = infoCustomLayout.findViewById(R.id.dialog_change_info_et_courriel);

        et_firstName.setText(infoViewModel.getFirstName());
        et_lastName.setText(infoViewModel.getLastName());
        et_phone.setText(infoViewModel.getPhoneNumber());
        et_matricule.setText(infoViewModel.getMatricule());
        et_mail.setText(infoViewModel.getMail());
        et_phone.addTextChangedListener(new MaskWatcher("(###)-### ####"));

        et_firstName.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){}
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {infoViewModel.setFirstName(charSequence.toString());}
            @Override public void afterTextChanged(Editable editable){}
        });
        et_lastName.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){}
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {infoViewModel.setLastName(charSequence.toString());}
            @Override public void afterTextChanged(Editable editable){}
        });
        et_phone.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){}
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {infoViewModel.setPhoneNumber(charSequence.toString());}
            @Override public void afterTextChanged(Editable editable){}
        });
        et_matricule.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){}
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {infoViewModel.setMatricule(charSequence.toString());}
            @Override public void afterTextChanged(Editable editable){}
        });
        et_mail.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){}
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {infoViewModel.setMail(charSequence.toString());}
            @Override public void afterTextChanged(Editable editable){}
        });

        infoCustomLayout
                .findViewById(R.id.dialog_change_info_bt_save)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tryChangeInfo();
                    }
                });

        infoCustomLayout
                .findViewById(R.id.dialog_change_info_bt_cancel)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        infoDialog.dismiss();
                    }
                });

        infoDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                infoViewModel.setInfoDialogDisplayed(false);
                infoViewModel.resetInfoCache();
            }
        });
    }

    /**
     * Initialise un layout de dialogue
     */
    private void inflatePasswordDialog(){
        passwordCustomLayout = getActivity()
                .getLayoutInflater()
                .inflate(R.layout.dialog_change_password, null);

        passwordDialog = new AlertDialog
                .Builder(getContext())
                .setView(passwordCustomLayout)
                .create();

        passwordDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        final EditText et_passwordOld = passwordCustomLayout.findViewById(R.id.dialog_change_password_et_actuelmdp);
        final EditText et_passwordNew = passwordCustomLayout.findViewById(R.id.dialog_change_password_et_mdp);
        final EditText et_passwordConfirm = passwordCustomLayout.findViewById(R.id.dialog_change_password_et_mdp_conf);

        et_passwordOld.setText(infoViewModel.getOldPassword());
        et_passwordNew.setText(infoViewModel.getNewPassword());
        et_passwordConfirm.setText(infoViewModel.getNewPasswordConf());

        et_passwordOld.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){}
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {infoViewModel.setOldPassword(charSequence.toString());}
            @Override public void afterTextChanged(Editable editable){}
        });
        et_passwordNew.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {infoViewModel.setNewPassword(charSequence.toString());}
            @Override public void afterTextChanged(Editable editable) {}
        });
        et_passwordConfirm.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {infoViewModel.setNewPasswordConf(charSequence.toString());}
            @Override public void afterTextChanged(Editable editable) {}
        });

        passwordCustomLayout
                .findViewById(R.id.dialog_change_password_bt_save)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tryChangePassword();
                    }
                });

        passwordCustomLayout
                .findViewById(R.id.dialog_change_password_bt_cancel)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        passwordDialog.dismiss();
                    }
                });

        passwordDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                infoViewModel.setPasswordDialogDisplayed(false);
                infoViewModel.restPasswordCache();
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
        this.getContext().registerReceiver(broadcastReceiverChangeInfo, new IntentFilter(Const.broadcastupdateUser));
        this.getContext().registerReceiver(broadcastReceiverChangePassword, new IntentFilter(Const.broadcastupdatePassword));
    }

    /**
     * Arrête l'enregistrement des broadcast receiver
     */
    @Override
    public void onPause() {
        super.onPause();
        this.getContext().unregisterReceiver(broadcastReceiverChangeInfo);
        this.getContext().unregisterReceiver(broadcastReceiverChangePassword);
    }

    private BroadcastReceiver broadcastReceiverChangeInfo = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ProgressBar progressBar = infoDialog.findViewById(R.id.dialog_change_info_progressBar);
            progressBar.setVisibility(View.GONE);
            if (intent.getBooleanExtra(Const.REQUEST_SUCCES, false)) {
                Toast.makeText(InfoFragment.this.getContext(), R.string.toast_msg_change_info_successful, Toast.LENGTH_SHORT).show();
                loadInfo();
                infoDialog.dismiss();
            } else {
                Toast.makeText(InfoFragment.this.getContext(), R.string.toast_msg_change_info_failed, Toast.LENGTH_SHORT).show();
            }
        }
    };

    private BroadcastReceiver broadcastReceiverChangePassword = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ProgressBar progressBar = passwordDialog.findViewById(R.id.dialog_change_password_progressBar);
            progressBar.setVisibility(View.GONE);
            if (intent.getBooleanExtra(Const.REQUEST_SUCCES, false)) {
                Toast.makeText(InfoFragment.this.getContext(), R.string.toast_msg_change_password_successful, Toast.LENGTH_SHORT).show();
                passwordDialog.dismiss();
            } else {
                Toast.makeText(InfoFragment.this.getContext(), R.string.toast_msg_need_to_enter_matching_old_password, Toast.LENGTH_SHORT).show();
            }
        }
    };
}