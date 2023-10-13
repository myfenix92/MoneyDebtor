package com.hfad.moneydebtor;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Comparator;
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
        holder.dateTake.setText(String.valueOf(usersDetailDataset.getDateString("date_take")));
        holder.summa.setText(String.valueOf(usersDetailDataset.getSumma()));
        holder.dateGive.setText(String.valueOf(usersDetailDataset.getDateString("date_give")));

        if (usersDetailDataset.getColor()) {
            holder.summa.setBackgroundResource(R.drawable.summa_bg_take);
        } else {
            holder.summa.setBackgroundResource(R.drawable.summa_bg_give);
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

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public Boolean sortHelper(boolean sort) {
        if (sort) {
            sort = false;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                dataList.sort(new Comparator<UsersDetailDataset>() {
                    @Override
                    public int compare(UsersDetailDataset o1, UsersDetailDataset o2) {
                        if (Long.compare(o1.getDate_give(), o2.getDate_give()) == 1) {
                            return -1;
                        }
                        return 0;
                    }
                });
            }
        } else {
            sort = true;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                dataList.sort(new Comparator<UsersDetailDataset>() {
                    @Override
                    public int compare(UsersDetailDataset o1, UsersDetailDataset o2) {
                        if (Long.compare(o1.getDate_give(), o2.getDate_give()) == -1) {
                            return -1;
                        }
                        return 0;
                    }
                });
            }
        }
       notifyDataSetChanged();
        return  sort;
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
