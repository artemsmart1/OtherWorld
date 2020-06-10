package com.example.otherworld.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.otherworld.Model.Club;
import com.example.otherworld.R;

import java.util.List;

public class MyClubAdapter extends RecyclerView.Adapter<MyClubAdapter.MyViewHolder> {

    Context context;
    List<Club> clubList;

    public MyClubAdapter(Context context, List<Club> clubList) {
        this.context = context;
        this.clubList = clubList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_club,viewGroup,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txt_club_name.setText(clubList.get(position).getName());
        holder.txt_club_address.setText(clubList.get(position).getAddress());
    }

    @Override
    public int getItemCount() {
        return clubList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_club_name,txt_club_address;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_club_address = (TextView)itemView.findViewById(R.id.txt_club_address);
            txt_club_name = (TextView)itemView.findViewById(R.id.txt_club_name);
        }
    }
}
