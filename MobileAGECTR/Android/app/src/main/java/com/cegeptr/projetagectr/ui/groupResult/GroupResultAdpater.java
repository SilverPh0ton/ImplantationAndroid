package com.cegeptr.projetagectr.ui.groupResult;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.cegeptr.projetagectr.R;
import com.cegeptr.projetagectr.logic.Const;
import com.cegeptr.projetagectr.logic.DataSingleton;
import com.cegeptr.projetagectr.logic.Entity.GroupResult;
import com.cegeptr.projetagectr.ui.result.ResultActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GroupResultAdpater extends RecyclerView.Adapter<GroupResultAdpater.GroupResultViewHolder>{

    private DataSingleton data;
    private ArrayList<GroupResult> list;
    private Fragment parentFragment;

    /**
     * Adapte pour le recyclerView
     * @param context
     * @param parentFragment
     */
    public GroupResultAdpater(Context context, Fragment parentFragment){
        this.parentFragment = parentFragment;
        data = DataSingleton.getInstance();
        list = data.getGroupResults();
    }

    /**
     * Crée le layout
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public GroupResultAdpater.GroupResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_item_group_result, parent, false);
        return new GroupResultViewHolder(view);
    }

    /**
     * Bind les info avec un élément du recyclerView
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(GroupResultAdpater.GroupResultViewHolder holder, int position) {
        holder.tvTitle.setText(list.get(position).getBook().getTitle());
        holder.tvEdition.setText(list.get(position).getBook().getEdition());
        holder.tvCount.setText(String.valueOf(list.get(position).getAmount()));
        holder.groupResult = list.get(position);
        Picasso
                .get()
                .load(Const.BOOK_IMG_ADDRESS + list.get(position).getBook().getUrlPhoto())
                .placeholder(R.drawable.book_default)
                .error(R.drawable.book_default)
                .into(holder.imageView);
    }

    /**
     * Obtient le nombre d'item dans le recycleView
     * @return
     */
    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * Set le click du recyler du groupResult
     */
    public class GroupResultViewHolder extends RecyclerView.ViewHolder{

        TextView tvTitle, tvEdition, tvCount;
        ImageView imageView;
        GroupResult groupResult;

        public GroupResultViewHolder(View view){
            super(view);

            tvTitle= view.findViewById(R.id.recyclerItem_group_result_title);
            tvEdition= view.findViewById(R.id.recyclerItem_group_result_edition);
            tvCount= view.findViewById(R.id.recyclerItem_group_result_amount);
            imageView= view.findViewById(R.id.recyclerItem_group_result_imageView);

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