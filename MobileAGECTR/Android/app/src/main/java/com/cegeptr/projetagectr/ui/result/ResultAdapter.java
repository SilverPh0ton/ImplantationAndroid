package com.cegeptr.projetagectr.ui.result;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.cegeptr.projetagectr.R;
import com.cegeptr.projetagectr.logic.Const;
import com.cegeptr.projetagectr.logic.DataSingleton;
import com.cegeptr.projetagectr.logic.Entity.Concession;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.BookResultViewHolder> {

    private ResultViewModel resultViewModel;
    private ArrayList<Concession> concessions;
    private Activity parentActivity;
    private DataSingleton data;
    private DecimalFormat twoDForm;

    public ResultAdapter(Activity parentActivity) {
        this.parentActivity = parentActivity;
        resultViewModel = ViewModelProviders.of((FragmentActivity) this.parentActivity).get(ResultViewModel.class);
        data = DataSingleton.getInstance();
        concessions = data.getDetailsResults();
        twoDForm = new DecimalFormat("0.00");
    }

    @NonNull
    @Override
    public ResultAdapter.BookResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_item_result, parent, false);
        return new BookResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultAdapter.BookResultViewHolder holder, int position) {
        holder.tvPrice.setText(String.valueOf(twoDForm.format(concessions.get(position).getSellingPrice()) + "$"));
        holder.concession = concessions.get(position);

        String img_url =((concessions.get(position).getUrlPhoto() == null) ?
                Const.BOOK_IMG_ADDRESS+concessions.get(position).getBook().getUrlPhoto():
                Const.CONCESSION_IMG_ADDRESS+concessions.get(position).getUrlPhoto()+".png"
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

    public class BookResultViewHolder extends RecyclerView.ViewHolder {

        TextView tvPrice;
        Concession concession;
        ImageView imageView;

        private BookResultViewHolder(View view) {
            super(view);

            tvPrice = view.findViewById(R.id.recyclerItem_result_tv_price);
            imageView= view.findViewById(R.id.recyclerItem_result_imageView);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((ResultActivity)parentActivity).inflateDetailDialog(concession);
                    resultViewModel.setConcessionDisplayed(concession);
                    resultViewModel.setDetailDialogDisplayed(true);
                }
            });
        }
    }
}
