package com.cegeptr.projetagectr.ui.reservation;

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
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder> {

    private ReservationViewModel reservationViewModel;
    private ArrayList<Concession> concessions;
    private Fragment parentFragment;
    private DataSingleton data;

    public ReservationAdapter(Fragment parentFragment) {
        this.parentFragment = parentFragment;
        reservationViewModel = ViewModelProviders.of(parentFragment).get(ReservationViewModel.class);
        data = DataSingleton.getInstance();

        concessions = data.getMyReservation();
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_item_reservation, parent, false);
        return new ReservationAdapter.ReservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        holder.tvTitre.setText(concessions.get(position).getBook().getTitle());

        Date reservedExpireDate = null;
        try {
            reservedExpireDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(concessions.get(position).getReservedExpireDate());
        } catch (ParseException | NullPointerException e) {
            e.printStackTrace();
        }

        Date now = new Date();
        long hoursLeft = (reservedExpireDate.getTime() - now.getTime()) / (60 * 60 * 1000);

        holder.tvTimeRemaining.setText("Temps restant : " + hoursLeft + " hrs");

        holder.concession = concessions.get(position);

        String img_url = ((concessions.get(position).getUrlPhoto() == null) ?
                Const.BOOK_IMG_ADDRESS + concessions.get(position).getBook().getUrlPhoto()+".png" :
                Const.CONCESSION_IMG_ADDRESS + concessions.get(position).getUrlPhoto()+".png"
        );
        Picasso
                .get()
                .load(img_url)
                .placeholder(R.drawable.book_default)
                .error(R.drawable.book_default)
                .into(holder.ivPhotoLivre);
    }

    @Override
    public int getItemCount() {
        return concessions.size();
    }

    public class ReservationViewHolder extends RecyclerView.ViewHolder {

        TextView tvTimeRemaining;
        ImageView ivPhotoLivre;
        TextView tvTitre;

        Concession concession;

        private ReservationViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTimeRemaining = itemView.findViewById(R.id.recyclerItem_reservation_tv_time_remaining);
            ivPhotoLivre = itemView.findViewById(R.id.recyclerItem_reservation_imageView);
            tvTitre = itemView.findViewById(R.id.recyclerItem_reservation_tv_title);
            ivPhotoLivre = itemView.findViewById(R.id.recyclerItem_reservation_imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((ReservationFragment)parentFragment).inflateDetailDialog(concession);
                    reservationViewModel.setConcessionDisplayed(concession);
                    reservationViewModel.setDetailDialogDisplayed(true);
                }
            });
        }
    }


}


