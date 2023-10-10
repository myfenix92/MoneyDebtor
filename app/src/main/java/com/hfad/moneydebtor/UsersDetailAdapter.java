package com.hfad.moneydebtor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UsersDetailAdapter extends RecyclerView.Adapter<UsersDetailAdapter.ViewHolder> {
    interface Listener {
        void onEditClick(UsersDetailDataset data, int position);
    }

    private Context context;
    private List<UsersDetailDataset> dataList;
    private Listener listener;

    public UsersDetailAdapter(Context context, List<UsersDetailDataset> dataList, Listener listener) {
        this.context = context;
        this.dataList = dataList;
        this.listener = listener;
    }

    @Override
    public UsersDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_detail_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(UsersDetailAdapter.ViewHolder holder, int position) {
        UsersDetailDataset usersDetailDataset = dataList.get(position);
        CardView cardView = holder.user_detail_cardView;
        holder.dateTake.setText(String.valueOf(usersDetailDataset.getDate_take()));
        holder.summa.setText(String.valueOf(usersDetailDataset.getSumma()));
        holder.dateGive.setText(String.valueOf(usersDetailDataset.getDate_give()));
        if (usersDetailDataset.getColor()) {
                holder.summa.setBackgroundColor(ContextCompat.getColor(context, R.color.take));
            } else {
                holder.summa.setBackgroundColor(ContextCompat.getColor(context, R.color.give));
            }
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onEditClick(usersDetailDataset, holder.getAdapterPosition());
                }
            }
        });

    }

    public void updateData(List<UsersDetailDataset> data) {
        dataList = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView user_detail_cardView;
        TextView dateTake;
        TextView summa;
        TextView dateGive;
        public ViewHolder(CardView itemView) {
            super(itemView);
            user_detail_cardView = itemView.findViewById(R.id.card_view_detail);
            dateTake = itemView.findViewById(R.id.date_take);
            summa = itemView.findViewById(R.id.summa);
            dateGive = itemView.findViewById(R.id.date_give);
        }

    }
}
