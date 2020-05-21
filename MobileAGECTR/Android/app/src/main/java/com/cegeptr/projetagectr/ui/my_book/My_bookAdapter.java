package com.cegeptr.projetagectr.ui.my_book;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.cegeptr.projetagectr.R;
import com.cegeptr.projetagectr.logic.Const;
import com.cegeptr.projetagectr.logic.DataSingleton;
import com.cegeptr.projetagectr.logic.Entity.Concession;
import com.cegeptr.projetagectr.ui.editBook.EditBookActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class My_bookAdapter extends RecyclerView.Adapter<My_bookAdapter.My_bookViewHolder> {

    private My_bookViewModel myBookViewModel;
    private DataSingleton data;
    private ArrayList<Concession> concessions;
    private Fragment parentFragment;


    public My_bookAdapter(Fragment parentActivity) {
        this.parentFragment = parentActivity;
        myBookViewModel = ViewModelProviders.of(this.parentFragment).get(My_bookViewModel.class);
        data = DataSingleton.getInstance();
        concessions = data.getMyConcessions();
    }

    @NonNull
    @Override
    public My_bookAdapter.My_bookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_item_my_book, parent, false);
        return new My_bookAdapter.My_bookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull My_bookViewHolder holder, int position) {
        holder.tvTitle.setText(concessions.get(position).getBook().getTitle());
        holder.concession = concessions.get(position);
        if (concessions.get(position).getState().equals(Const.STATE_ACCEPT)) {
            holder.tvState.setText(R.string.state_accept);
            holder.tvState.setBackgroundResource(R.drawable.border_top_accept);
        } else if (concessions.get(position).getState().equals(Const.STATE_DENIED)) {
            holder.tvState.setText(R.string.state_denied);
            holder.tvState.setBackgroundResource(R.drawable.border_top_denied);
        } else if (concessions.get(position).getState().equals(Const.STATE_PENDING)) {
            holder.tvState.setText(R.string.state_pending);
            holder.tvState.setBackgroundResource(R.drawable.border_top_pending);
        } else if (concessions.get(position).getState().equals(Const.STATE_TO_REMOVE)) {
            holder.tvState.setText(R.string.state_removed);
            holder.tvState.setBackgroundResource(R.drawable.border_top_removed);
        } else if (concessions.get(position).getState().equals(Const.STATE_TO_PAY)) {
            holder.tvState.setText(R.string.state_sold);
            holder.tvState.setBackgroundResource(R.drawable.border_top_sold);
        } else if (concessions.get(position).getState().equals(Const.STATE_TO_RENEW)) {
            holder.tvState.setText(R.string.state_to_renew);
            holder.tvState.setBackgroundResource(R.drawable.border_top_to_renew);
        } else if (concessions.get(position).getState().equals(Const.STATE_TO_GIVE)) {
            holder.tvState.setText(R.string.state_given);
            holder.tvState.setBackgroundResource(R.drawable.border_top_given);
        } else if (concessions.get(position).getState().equals(Const.STATE_UPDATE)) {
            holder.tvState.setText(R.string.state_update);
            holder.tvState.setBackgroundResource(R.drawable.border_top_update);
        }
        String img_url = ((concessions.get(position).getUrlPhoto() == null) ?
                Const.BOOK_IMG_ADDRESS + concessions.get(position).getBook().getUrlPhoto()+".png" :
                Const.CONCESSION_IMG_ADDRESS + concessions.get(position).getUrlPhoto()+".png"
        );
        Picasso
                .get()
                .load(img_url)
                .placeholder(R.drawable.book_default)
                .error(R.drawable.book_default)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return concessions.size();
    }


    public class My_bookViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvState;
        Concession concession;
        ImageView imageView;

        public My_bookViewHolder(View view) {
            super(view);

            tvTitle = view.findViewById(R.id.recyclerItem_my_book_tv_title);
            tvState = view.findViewById(R.id.recyclerItem_my_book_tv_state);
            imageView = view.findViewById(R.id.recyclerItem_my_book_imageView);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(concession.getState().equals(Const.STATE_ACCEPT) || concession.getState().equals(Const.STATE_TO_RENEW) || concession.getState().equals(Const.STATE_PENDING))
                    {
                        ((My_bookFragment) parentFragment).inflateActionDialog(concession);
                        myBookViewModel.setConcessionConcerned(concession);
                        myBookViewModel.setActionDialogDisplayed(true);
                    }
                    else{
                        Intent intent = new Intent(view.getContext(), EditBookActivity.class);
                        intent.putExtra(Const.CONCESSION_TO_DISPLAY, concession.getIdConcession());
                        view.getContext().startActivity(intent);
                    }
                }
            });
        }
    }
}
