package com.cegeptr.projetagectr.ui;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.cegeptr.projetagectr.R;
import com.cegeptr.projetagectr.logic.Const;
import com.cegeptr.projetagectr.logic.DataSingleton;
import com.cegeptr.projetagectr.logic.NetworkStateReceiver;
import com.cegeptr.projetagectr.ui.connectionHeader.ConnectionHeaderFragment;
import com.cegeptr.projetagectr.ui.userInfoHeader.UserInfoHeaderFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NetworkStateReceiver.NetworkStateReceiverListener{

    private AppBarConfiguration mAppBarConfiguration;
    private NavController navController;

    private DataSingleton dataSingleton = DataSingleton.getInstance(this);
    private Button btDeconnexion;
    private FragmentTransaction fragmentTransaction;

    private NavigationView navigationView;

    private DrawerLayout drawer;

    private Fragment connectionHeaderFragment;
    private Fragment userInfoHeaderFragment;

    private SharedPreferences sharedPreferences;
    private NetworkStateReceiver networkStateReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(this);
        this.registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_search,
                R.id.nav_about,
                R.id.nav_info,
                R.id.nav_reservation,
                R.id.nav_my_book,
                R.id.nav_group_result,
                R.id.nav_my_history)
                .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        userInfoHeaderFragment = new UserInfoHeaderFragment();
        connectionHeaderFragment = new ConnectionHeaderFragment();

        btDeconnexion = findViewById(R.id.btDeconnexion);
        btDeconnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new AlertDialog.Builder(view.getContext())
                        .setTitle(R.string.alert_title_disconect)
                        .setMessage(R.string.alert_text_disconect)
                        .setPositiveButton(R.string.alert_button_yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dataSingleton.disconnectUser();
                                refreshDrawer();

                                navController.navigate(R.id.nav_search);

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
        
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                refreshDrawer();
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        autoConncet();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem item = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) item.getActionView();
        final EditText searchEditText = (EditText) searchView.findViewById(R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.colorTextDark));
        searchEditText.setHintTextColor(getResources().getColor(R.color.colorTextDark));
        searchView.setQueryHint(Html.fromHtml("<font color = #919191>" + getResources().getString(R.string.query_hint) + "</font>"));
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                searchEditText.setTextColor(
                        (hasFocus)?
                                getResources().getColor(R.color.colorTextLight):
                                getResources().getColor(R.color.colorPrimary)
                );
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query.length() >= Const.MIN_QUERY_SIZE){
                    searchView.clearFocus();
                    navController.navigate(R.id.nav_group_result);
                    dataSingleton.setGroupResultsQuery(query);
                    dataSingleton.search();
                }
                else{
                    Toast.makeText(MainActivity.this, R.string.toast_msg_query_to_short , Toast.LENGTH_SHORT).show();
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {return false;}
        });

        return true;
    }

    private void refreshDrawer(){
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Menu menuNav = navigationView.getMenu();
        MenuItem profilItem = menuNav.findItem(R.id.nav_profil);
        MenuItem infoItem = menuNav.findItem(R.id.nav_info);
        MenuItem reservItem = menuNav.findItem(R.id.nav_reservation);
        MenuItem bookItem = menuNav.findItem(R.id.nav_my_book);
        MenuItem historyItem = menuNav.findItem(R.id.nav_my_history);
        if(dataSingleton.getConnectedUser() != null)
        {
            fragmentTransaction.replace(R.id.nav_header, userInfoHeaderFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            btDeconnexion.setVisibility(View.VISIBLE);
            btDeconnexion.setEnabled(true);

            profilItem.setEnabled(true);
            infoItem.setEnabled(true);
            reservItem.setEnabled(true);
            bookItem.setEnabled(true);
            historyItem.setEnabled(true);

        }
        else
        {
            fragmentTransaction.replace(R.id.nav_header, connectionHeaderFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            profilItem.setEnabled(false);
            infoItem.setEnabled(false);
            reservItem.setEnabled(false);
            bookItem.setEnabled(false);
            historyItem.setEnabled(false);

            btDeconnexion.setVisibility(View.INVISIBLE);
            btDeconnexion.setEnabled(false);
        }
    }

    private void autoConncet(){
        if(dataSingleton.getConnectedUser() == null){
            sharedPreferences = getSharedPreferences(Const.MY_PREFERENCES, Context.MODE_PRIVATE);
            String hasedPaswword = sharedPreferences.getString(Const.PREFERENCE_HASHED_PASSWORD, null);
            String username = sharedPreferences.getString(Const.PREFERENCES_USERNAME, null);
            if(hasedPaswword != null && username != null){
                dataSingleton.autoLogin(username,hasedPaswword);
            }
        }
    }

    /*================================État d'internet================================*/
    public void onDestroy() {
        super.onDestroy();
        networkStateReceiver.removeListener(this);
        this.unregisterReceiver(networkStateReceiver);
    }

    @Override
    public void networkAvailable() {
        autoConncet();
    }

    @Override
    public void networkUnavailable() { }

    /*================================Broadcast================================*/
    /**
     * Enregistre les Broadcast Receiver
     */
    @Override
    public void onResume() {
        super.onResume();
        this.registerReceiver(broadcastReceiverRefreshDrawer, new IntentFilter(Const.broadcastRefreshDrawer));
        this.registerReceiver(broadcastReceiverChangeFragment, new IntentFilter(Const.broadcastChangeFragment));

    }

    /**
     * Arrête l'enregistrement des broadcast receiver
     */
    @Override
    public void onPause() {
        super.onPause();
        this.unregisterReceiver(broadcastReceiverRefreshDrawer);
        this.unregisterReceiver(broadcastReceiverChangeFragment);
    }

    private BroadcastReceiver broadcastReceiverRefreshDrawer = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshDrawer();
        }
    };

    private BroadcastReceiver broadcastReceiverChangeFragment = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getStringExtra(Const.DISPLAY_FRAGMENT)){
                case Const.DISPLAY_FRAGMENT_SEARCH:{
                    navController.navigate(R.id.nav_search);
                    break;
                }
                case Const.DISPLAY_FRAGMENT_ABOUT:{
                    navController.navigate(R.id.nav_about);
                    break;
                }
                case Const.DISPLAY_FRAGMENT_INFO:{
                    navController.navigate(R.id.nav_info);
                    break;
                }
                case Const.DISPLAY_FRAGMENT_RESERVATION:{
                    navController.navigate(R.id.nav_reservation);
                    break;
                }
                case Const.DISPLAY_FRAGMENT_MY_BOOK:{
                    navController.navigate(R.id.nav_my_book);
                    break;
                }
                case Const.DISPLAY_FRAGMENT_MY_HYSTORY:{
                    navController.navigate(R.id.nav_my_history);
                    break;
                }
            }
            drawer.closeDrawers();
        }
    };
}
