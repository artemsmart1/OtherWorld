package com.example.otherworld.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.otherworld.Common.Common;
import com.example.otherworld.Interface.IRecyclerItemSelectedListener;
import com.example.otherworld.Model.Club;
import com.example.otherworld.R;

import java.util.ArrayList;
import java.util.List;

public class MyClubAdapter extends RecyclerView.Adapter<MyClubAdapter.MyViewHolder> {

    Context context;
    List<Club> clubList;
    List<CardView> cardViewList;
    LocalBroadcastManager localBroadcastManager;

    public MyClubAdapter(Context context, List<Club> clubList) {
        this.context = context;
        this.clubList = clubList;
        cardViewList = new ArrayList<>();
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.
                from(context).inflate(R.layout.layout_club,viewGroup,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txt_club_name.setText(clubList.get(position).getName());
        holder.txt_club_address.setText(clubList.get(position).getAddress());
        if(!cardViewList.contains(holder.card_club))
            cardViewList.add(holder.card_club);

        holder.setiRecyclerItemSelectedListener(new IRecyclerItemSelectedListener() {
            @Override
            public void onItemSelectedListener(View view, int pos) {
                //цвет для не выбранных белый
                for(CardView cardView:cardViewList)
                    cardView.setCardBackgroundColor(context.getResources().getColor(android.R.color.holo_blue_light));
                //для выбранных другой цвет
                holder.card_club.setCardBackgroundColor(context.getResources().getColor(android.R.color.holo_purple));
                //выключаю кнопку вперед
                Intent intent = new Intent(Common.KEY_ENABLE_BUTTON_NEXT);
                intent.putExtra(Common.KEY_CLUB_STORE,clubList.get(pos));
                intent.putExtra(Common.KEY_STEP,1);
                localBroadcastManager.sendBroadcast(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return clubList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_club_name,txt_club_address;
        CardView card_club;

        IRecyclerItemSelectedListener iRecyclerItemSelectedListener;

        public void setiRecyclerItemSelectedListener(IRecyclerItemSelectedListener iRecyclerItemSelectedListener) {
            this.iRecyclerItemSelectedListener = iRecyclerItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            card_club = (CardView)itemView.findViewById(R.id.card_club);
            txt_club_address = (TextView)itemView.findViewById(R.id.txt_club_address);
            txt_club_name = (TextView)itemView.findViewById(R.id.txt_club_name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            iRecyclerItemSelectedListener.onItemSelectedListener(view,getAdapterPosition());
        }
    }
}
