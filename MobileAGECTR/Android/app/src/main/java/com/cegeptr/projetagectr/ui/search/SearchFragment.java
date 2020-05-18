package com.cegeptr.projetagectr.ui.search;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.cegeptr.projetagectr.R;
import com.cegeptr.projetagectr.logic.Const;
import com.cegeptr.projetagectr.logic.DataSingleton;

public class SearchFragment extends Fragment {

    private SearchViewModel searchViewModel;
    private DataSingleton data = DataSingleton.getInstance();
    private NavController navController;
    private TextView tvSearch;

    /**
     * Initialise le layout du fragment
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        searchViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        View root = inflater.inflate(R.layout.fragment_research, container, false);

        tvSearch = root.findViewById(R.id.fragment_search_et_query);
        tvSearch.setText(searchViewModel.getQuery());
        tvSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){}
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchViewModel.setQuery(charSequence.toString());}
            @Override public void afterTextChanged(Editable editable){}
        });

        Button bt_search = root.findViewById(R.id.fragment_research_bt_search);
        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tvSearch.getText().toString().length() >= Const.MIN_QUERY_SIZE){
                    data.setGroupResultsQuery(tvSearch.getText().toString());
                    data.search();
                    navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                    navController.navigate(R.id.nav_group_result);
                }
                else{
                    Toast.makeText(view.getContext(), R.string.toast_msg_query_to_short , Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;
    }
}