package com.cegeptr.projetagectr.ui.search;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.cegeptr.projetagectr.R;
import com.cegeptr.projetagectr.logic.Const;
import com.cegeptr.projetagectr.logic.DataSingleton;
import com.cegeptr.projetagectr.logic.Entity.GroupResult;
import com.cegeptr.projetagectr.ui.result.ResultActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder>{
    private DataSingleton data;
    private ArrayList<GroupResult> list;
    private Fragment parentFragment;

    public SearchAdapter(Context context, Fragment parentFragment, boolean popular) {
        this.parentFragment = parentFragment;
        data = DataSingleton.getInstance();
        if (popular)
            list = data.getLstBookPop();
        else
            list = data.getLstBookRecent();
    }

    @NonNull
    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_item_group_result_min, parent, false);
        return new SearchAdapter.SearchViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.SearchViewHolder holder, int position) {
        holder.tvTitle.setText(list.get(position).getBook().getTitle());
        holder.tvCount.setText(String.valueOf(list.get(position).getAmount()));
        holder.groupResult = list.get(position);
        Picasso
                .get()
                .load(Const.BOOK_IMG_ADDRESS + list.get(position).getBook().getUrlPhoto())
                .placeholder(R.drawable.book_default)
                .error(R.drawable.book_default)
                .into(holder.imageView);
    }


    public class SearchViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvEdition, tvCount;
        ImageView imageView;
        GroupResult groupResult;

        private SearchViewHolder(View view) {
            super(view);

            tvTitle= view.findViewById(R.id.recyclerItem_result_min_title);
            tvCount= view.findViewById(R.id.recyclerItem_result_min_amount);
            imageView= view.findViewById(R.id.recyclerItem_result_min_image);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    data.setDetailsResultsGroupResult(groupResult);
                    data.detailSearch();
                    Intent intent = new Intent(view.getContext(), ResultActivity.class);
                    view.getContext().startActivity(intent);
                }
            });
        }
    }
}
