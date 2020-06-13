package com.example.otherworld;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import com.example.otherworld.Adapter.MyViewPagerAdapter;
import com.example.otherworld.Common.Common;
import com.example.otherworld.Common.NonSwipeViewPager;
import com.example.otherworld.Model.Gamezone;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.shuhart.stepview.StepView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;

public class BookingActivity extends AppCompatActivity {

    LocalBroadcastManager localBroadcastManager;
    AlertDialog dialog;
    CollectionReference gamezoneRef;

    @BindView(R.id.step_view)
    StepView stepView;

    @BindView(R.id.view_pager)
    NonSwipeViewPager viewPager;

    @BindView(R.id.btn_previous_step)
    Button btn_previous_step;

    @BindView(R.id.btn_next_step)
    Button btn_next_step;

    @OnClick(R.id.btn_previous_step)
    void previousStep(){
        if (Common.step == 3 || Common.step > 0)
        {
            Common.step--;
            viewPager.setCurrentItem(Common.step);
            if(Common.step < 3)
            {
                btn_next_step.setEnabled(true);
                setColorButton();
            }
        }

    }
    //Event
    @OnClick(R.id.btn_next_step)
    void nextClick(){
        if(Common.step < 3 || Common.step == 0)
        {
            Common.step++;
            if(Common.step == 1)
            {
                if(Common.currentClub != null)
                    loadGamezoneByClub(Common.currentClub.getClubId());
            }
            else if (Common.step == 2)
            {
                if(Common.currentGamezone != null)
                    loadTimaSlotOfGamezone(Common.currentGamezone.getGamezoneId());
            }else if (Common.step == 3)
            {
                if(Common.currentTimeSlot != -1)
                    confirmBooking();
            }

            viewPager.setCurrentItem(Common.step);
        }
    }

    private void confirmBooking() {
        //Посылаем сигнал к 4 фрагменту
        Intent intent = new Intent(Common.KEY_CONFIRM_BOOKING);
        localBroadcastManager.sendBroadcast(intent);
    }

    private void loadTimaSlotOfGamezone(String gamezoneId) {
        //Посылаю сигнал к 3 фрагменту

        Intent intent = new Intent(Common.KEY_DISPLAY_TIME_SLOT);
        localBroadcastManager.sendBroadcast(intent);
    }

    private void loadGamezoneByClub(String clubId) {
        dialog.show();
        if(!TextUtils.isEmpty(Common.city)){
            //  /Address/Новосибирск/Branch/CrdAvxUR7B9lYvVisXvc/Gamezone
            gamezoneRef = FirebaseFirestore.getInstance().collection("Address").document("VR клуб Дурой мир")
                    .collection("Branch").document(clubId).collection("Gamezone");

            gamezoneRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    ArrayList<Gamezone> gamezones = new ArrayList<>();
                    for(QueryDocumentSnapshot gamezoneSnapShot:task.getResult())
                    {
                        Gamezone gamezone = gamezoneSnapShot.toObject(Gamezone.class);
                        gamezone.setGamezoneId(gamezoneSnapShot.getId());
                        gamezones.add(gamezone);
                    }
                    Intent intent = new Intent(Common.KEY_GAMEZONE_LOAD_DONE);
                    intent.putParcelableArrayListExtra(Common.KEY_GAMEZONE_LOAD_DONE,gamezones);
                    localBroadcastManager.sendBroadcast(intent);
                    dialog.dismiss();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                }
            });

        }



    }


    //ДЛЯ Broadcast
    private BroadcastReceiver buttonNextReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int step = intent.getIntExtra(Common.KEY_STEP,0);
            if(step == 1)
                Common.currentClub = intent.getParcelableExtra(Common.KEY_CLUB_STORE);
            else if (step == 2)
                Common.currentGamezone = intent.getParcelableExtra(Common.KEY_GAMEZONE_SELECTED);
            else if (step == 3)
                Common.currentTimeSlot = intent.getIntExtra(Common.KEY_TIME_SLOT,-1);


            btn_next_step.setEnabled(true);
            setColorButton();
        }
    };

    @Override
    protected void onDestroy() {
        localBroadcastManager.unregisterReceiver(buttonNextReceiver);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        ButterKnife.bind(BookingActivity.this);

        dialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(buttonNextReceiver,new IntentFilter(Common.KEY_ENABLE_BUTTON_NEXT));

        setupStepView();
        setColorButton();

        //view
        viewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(4);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {

                stepView.go(i,true);

                if (i == 0)
                    btn_previous_step.setEnabled(false);
                else
                    btn_previous_step.setEnabled(true);

                btn_next_step.setEnabled(false);
                setColorButton();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void setColorButton() {
        if (btn_next_step.isEnabled()){
            btn_next_step.setBackgroundResource(android.R.color.holo_purple);
        }
        else{
            btn_next_step.setBackgroundResource(R.color.btn_color);
        }

        if (btn_previous_step.isEnabled()){
            btn_previous_step.setBackgroundResource(android.R.color.holo_purple);
        }
        else{
            btn_previous_step.setBackgroundResource(R.color.btn_color);
        }

    }

    private void setupStepView() {
        List<String> stepList = new ArrayList<>();
        stepList.add("Услуга");
        stepList.add("Игровая зона");
        stepList.add("Время");
        stepList.add("Подтвердить");
        stepView.setSteps(stepList);
    }
}