package com.example.otherworld.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.otherworld.Adapter.MyGamezoneAdapter;
import com.example.otherworld.Common.Common;
import com.example.otherworld.Common.SpaceItemDecoration;
import com.example.otherworld.Model.Gamezone;
import com.example.otherworld.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BookingStep2Fragment extends Fragment {

    Unbinder unbinder;
    LocalBroadcastManager localBroadcastManager;

    @BindView(R.id.recycler_gamezone)
    RecyclerView recycler_gamezone;

    private BroadcastReceiver gamezoneDoneReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<Gamezone> gamezoneArrayList = intent.getParcelableArrayListExtra(Common.KEY_GAMEZONE_LOAD_DONE);
            MyGamezoneAdapter adapter = new MyGamezoneAdapter (getContext(),gamezoneArrayList);
            recycler_gamezone.setAdapter(adapter);
        }
    };

    static BookingStep2Fragment instance;

    public static BookingStep2Fragment getInstance(){
        if (instance == null)
            instance = new BookingStep2Fragment();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(gamezoneDoneReceiver,new IntentFilter(Common.KEY_GAMEZONE_LOAD_DONE));
    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(gamezoneDoneReceiver);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);

         View itemView = inflater.inflate(R.layout.fragment_booking_step_two,container,false);
         unbinder = ButterKnife.bind(this,itemView);

         initView();

        return itemView;
    }

    private void initView() {

        recycler_gamezone.setHasFixedSize(true);
        recycler_gamezone.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recycler_gamezone.addItemDecoration(new SpaceItemDecoration(4));
    }
}
