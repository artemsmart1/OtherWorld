package com.example.otherworld.Fragments;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.otherworld.Common.Common;
import com.example.otherworld.Model.BookingInformation;
import com.example.otherworld.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;

public class BookingStep4Fragment extends Fragment {

    SimpleDateFormat simpleDateFormat;
    LocalBroadcastManager localBroadcastManager;
    Unbinder unbinder;
    AlertDialog dialog;

    @BindView(R.id.txt_booking_gamezone)
    TextView txt_booking_gamezone;

    @BindView(R.id.txt_booking_time_text)
    TextView txt_booking_time_text;

    @BindView(R.id.txt_club_address)
    TextView txt_club_address;

    @BindView(R.id.txt_club_name)
    TextView txt_club_name;

    @BindView(R.id.txt_club_open_hours)
    TextView txt_club_open_hours;

    @BindView(R.id.txt_club_phone)
    TextView txt_club_phone;

    @OnClick(R.id.btn_confirm)
    void confirmBooking(){

        dialog.show();





        BookingInformation bookingInformation = new BookingInformation();

        bookingInformation.setGamezoneId(Common.currentGamezone.getGamezoneId());
        bookingInformation.setCustomerName(Common.currentUser.getName());
        bookingInformation.setCustomerPhone(Common.currentUser.getPhoneNumber());
        bookingInformation.setClubId(Common.currentClub.getClubId());
        bookingInformation.setClubAddress(Common.currentClub.getAddress());
        bookingInformation.setClubName(Common.currentClub.getName());
        bookingInformation.setTime(new StringBuilder(Common.convertTimeSlotToString(Common.currentTimeSlot))
                .append("в").append(simpleDateFormat.format(Common.bookingDate.getTime())).toString());
        bookingInformation.setSlot(Long.valueOf(Common.currentTimeSlot));

        DocumentReference bookingDate = FirebaseFirestore.getInstance()
                .collection("Address")
                .document(Common.city)
                .collection("Branch")
                .document(Common.currentClub.getClubId())
                .collection("Gamezone")
                .document(Common.currentGamezone.getGamezoneId()).collection(Common.simpleDateFormat.format(Common.bookingDate.getTime()))
                .document(String.valueOf(Common.currentTimeSlot));

        bookingDate.set(bookingInformation).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                addToUserBooking(bookingInformation);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addToUserBooking(BookingInformation bookingInformation) {


        CollectionReference userBooking = FirebaseFirestore.getInstance().collection("User").document(Common.currentUser.getPhoneNumber()).collection("Booking");

        userBooking.whereEqualTo("Готово",false).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.getResult().isEmpty())
                {
                    userBooking.document().set(bookingInformation).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            if(dialog.isShowing())
                                dialog.dismiss();


                            resetStaticData();
                            getActivity().finish();
                            Toast.makeText(getContext(), "Успешно", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if(dialog.isShowing())
                                dialog.dismiss();

                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                    {
                        if(dialog.isShowing())
                            dialog.dismiss();

                        resetStaticData();
                        getActivity().finish();
                        Toast.makeText(getContext(), "Успешно", Toast.LENGTH_SHORT).show();
                    }
            }
        });
    }





    private void resetStaticData() {
        Common.step = 0;
        Common.currentTimeSlot = -1;
        Common.currentClub = null;
        Common.currentGamezone = null;
        Common.bookingDate.add(Calendar.DATE,0);
    }

    BroadcastReceiver confirmBookingReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setData();
        }
    };

    private void setData() {
        txt_booking_gamezone.setText(Common.currentGamezone.getName());
        txt_booking_time_text.setText(new StringBuilder(Common.convertTimeSlotToString(Common.currentTimeSlot))
                .append("в").append(simpleDateFormat.format(Common.bookingDate.getTime())));

        txt_club_address.setText(Common.currentClub.getAddress());
        txt_club_name.setText(Common.currentClub.getName());
        txt_club_open_hours.setText(Common.currentClub.getOpenHours());
    }


    static BookingStep4Fragment instance;

    public static BookingStep4Fragment getInstance(){
        if (instance == null)
            instance = new BookingStep4Fragment();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        simpleDateFormat = new SimpleDateFormat("dd:MM:yyyy");

        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(confirmBookingReceiver,new IntentFilter(Common.KEY_CONFIRM_BOOKING));

        dialog = new SpotsDialog.Builder().setContext(getContext()).setCancelable(false).build();
    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(confirmBookingReceiver);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View itemView = inflater.inflate(R.layout.fragment_booking_step_four,container,false);
        unbinder = ButterKnife.bind(this,itemView);

        return itemView;
    }
}
