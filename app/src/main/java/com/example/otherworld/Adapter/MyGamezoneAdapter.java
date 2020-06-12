package com.example.otherworld.Adapter;

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
import com.example.otherworld.Model.Gamezone;
import com.example.otherworld.R;

import java.util.ArrayList;
import java.util.List;

import  android.content.Context;

public class MyGamezoneAdapter extends RecyclerView.Adapter<MyGamezoneAdapter.MyViewHolder> {

    Context context;
    List<Gamezone> gamezoneList;
    List<CardView> cardViewList;
    LocalBroadcastManager localBroadcastManager;


    public MyGamezoneAdapter( Context context, List<Gamezone> gamezoneList) {
        this.context = context;
        this.gamezoneList = gamezoneList;
        cardViewList = new ArrayList<>();
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.lauout_gamezone, viewGroup, false);
        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txt_gamezone_name.setText(gamezoneList.get(position).getName());
        if(!cardViewList.contains(holder.card_gamezone))
            cardViewList.add(holder.card_gamezone);

        holder.setiRecyclerItemSelectedListener(new IRecyclerItemSelectedListener() {
            @Override
            public void onItemSelectedListener(View view, int pos) {
                //Для не выбранных
                for(CardView cardView : cardViewList) {
                    cardView.setCardBackgroundColor(context.getResources().getColor(android.R.color.holo_blue_light));
                }

                //Для  выбранных
                holder.card_gamezone.setCardBackgroundColor(context.getResources().getColor(android.R.color.holo_purple));

                //посылаем локальный сигнал для кнопки next
                Intent intent = new Intent(Common.KEY_ENABLE_BUTTON_NEXT);
                intent.putExtra(Common.KEY_GAMEZONE_SELECTED,gamezoneList.get(pos));
                intent.putExtra(Common.KEY_STEP,2);
                localBroadcastManager.sendBroadcast(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return gamezoneList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txt_gamezone_name;
        CardView card_gamezone;

        IRecyclerItemSelectedListener iRecyclerItemSelectedListener;

        public void setiRecyclerItemSelectedListener(IRecyclerItemSelectedListener iRecyclerItemSelectedListener) {
            this.iRecyclerItemSelectedListener = iRecyclerItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
            card_gamezone = (CardView)itemView.findViewById(R.id.card_gamezone);
            txt_gamezone_name = (TextView)itemView.findViewById(R.id.txt_gamezone_name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            iRecyclerItemSelectedListener.onItemSelectedListener(view,getAdapterPosition());
        }
    }
}
