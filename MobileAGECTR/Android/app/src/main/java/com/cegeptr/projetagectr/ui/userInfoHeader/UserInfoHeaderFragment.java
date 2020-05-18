package com.cegeptr.projetagectr.ui.userInfoHeader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.cegeptr.projetagectr.R;
import com.cegeptr.projetagectr.logic.Const;
import com.cegeptr.projetagectr.logic.DataSingleton;
import com.cegeptr.projetagectr.logic.Entity.User;

public class UserInfoHeaderFragment extends Fragment {

    private UserInfoHeaderViewModel mViewModel;

    private ImageView ivProfilIcon;
    private TextView tvUserName;
    private TextView tvUserEmail;

    private DataSingleton dataSingleton;

    public static UserInfoHeaderFragment newInstance() {
        return new UserInfoHeaderFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        mViewModel = ViewModelProviders.of(this).get(UserInfoHeaderViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_user_info_header, container, false);

        ivProfilIcon = root.findViewById(R.id.ivProfilIcon);
        tvUserName = root.findViewById(R.id.tvUserName);
        tvUserEmail = root.findViewById(R.id.tvUserEmail);

        dataSingleton = DataSingleton.getInstance();
        User connectedUser = dataSingleton.getConnectedUser();

        if(dataSingleton.getConnectedUser()!= null)
        {
            String userFullName = connectedUser.getFirstName() + " " + connectedUser.getLastName();
            //WHEN ADDED TO DB SET PROFILICON
            tvUserName.setText(userFullName);
            tvUserEmail.setText(connectedUser.getEmail());
            updateAvatar();
        }
        else
        {
            tvUserName.setText("USER NOT FOUN");
            tvUserEmail.setText("");
        }

        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra(Const.DISPLAY_FRAGMENT, Const.DISPLAY_FRAGMENT_INFO);
                intent.setAction(Const.broadcastChangeFragment);
                view.getContext().sendBroadcast(intent);
            }
        });

        return root;
    }

    private void updateAvatar(){
        User connectedUser = dataSingleton.getConnectedUser();
        switch (connectedUser.getAvatar()) {
            case 1:
            default:
                ivProfilIcon.setImageDrawable(getResources().getDrawable(R.drawable.avatar1));
                break;
            case 2:
                ivProfilIcon.setImageDrawable(getResources().getDrawable(R.drawable.avatar2));
                break;
            case 3:
                ivProfilIcon.setImageDrawable(getResources().getDrawable(R.drawable.avatar3));
                break;
            case 4:
                ivProfilIcon.setImageDrawable(getResources().getDrawable(R.drawable.avatar4));
                break;
            case 5:
                ivProfilIcon.setImageDrawable(getResources().getDrawable(R.drawable.avatar5));
                break;
            case 6:
                ivProfilIcon.setImageDrawable(getResources().getDrawable(R.drawable.avatar6));
                break;
            case 7:
                ivProfilIcon.setImageDrawable(getResources().getDrawable(R.drawable.avatar7));
                break;
            case 8:
                ivProfilIcon.setImageDrawable(getResources().getDrawable(R.drawable.avatar8));
                break;
            case 9:
                ivProfilIcon.setImageDrawable(getResources().getDrawable(R.drawable.avatar9));
                break;
            case 10:
                ivProfilIcon.setImageDrawable(getResources().getDrawable(R.drawable.avatar10));
                break;
            case 11:
                ivProfilIcon.setImageDrawable(getResources().getDrawable(R.drawable.avatar11));
                break;
            case 12:
                ivProfilIcon.setImageDrawable(getResources().getDrawable(R.drawable.avatar12));
                break;
            case 13:
                ivProfilIcon.setImageDrawable(getResources().getDrawable(R.drawable.avatar0));
                break;
        }
    }

    /*================================Broadcast================================*/
    /**
     * Enregistre les Broadcast Receiver
     */
    @Override
    public void onResume() {
        super.onResume();
        this.getContext().registerReceiver(broadcastReceiver, new IntentFilter(Const.broadcastupdateAvatar));
    }

    /**
     * Arrête l'enregistrement des broadcast receiver
     */
    @Override
    public void onPause() {
        super.onPause();
        this.getContext().unregisterReceiver(broadcastReceiver);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateAvatar();
        }
    };
}
