package com.hfad.moneydebtor;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {
    interface Listener{
        void onViewClick(UsersDataset data, int position);
    }
    private Context context;
    private List<UsersDataset> dataList;
    private Listener listener;
    private int delayAnimate = 50;
    
    public UsersAdapter(Context context, List<UsersDataset> dataList, Listener listener) {
        this.context = context;
        this.dataList = dataList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UsersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.all_users_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersAdapter.ViewHolder holder, int position) {
        UsersDataset usersDataset = dataList.get(position);
        CardView cardView = holder.all_users_cardView;
        holder.itemView.setVisibility(View.INVISIBLE);
        setAnimation(cardView, position);
        holder.name_user.setText(usersDataset.getName_user());
        holder.summa.setText(String.valueOf(usersDataset.getAll_summa()));
        if (Double.parseDouble(holder.summa.getText().toString()) > 0) {
            cardView.setBackgroundResource(R.drawable.main_card_bg_positive);
        } else if (Double.parseDouble(holder.summa.getText().toString()) < 0) {
            cardView.setBackgroundResource(R.drawable.main_card_bg_negative);
        } else {
            cardView.setBackgroundResource(R.drawable.main_card_bg);
        }
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onViewClick(usersDataset, holder.getAdapterPosition());
                }
            }
        });
    }

    public void setAnimation(View view, int position) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation animation;
                animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
                view.startAnimation(animation);
                view.setVisibility(View.VISIBLE);
            }
        }, (long) delayAnimate * position);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name_user;
        TextView summa;
        CardView all_users_cardView;
        MenuItem menuItem;
        public ViewHolder(CardView itemView) {
            super(itemView);
            all_users_cardView = itemView.findViewById(R.id.card_view_all_users);
            name_user = itemView.findViewById(R.id.user_name);
            summa = itemView.findViewById(R.id.user_summa);
        }
    }
}
