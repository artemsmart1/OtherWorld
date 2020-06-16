package com.example.otherworld.Common;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.otherworld.Model.Club;
import com.example.otherworld.Model.Gamezone;
import com.example.otherworld.Model.MyToken;
import com.example.otherworld.Model.User;
import com.example.otherworld.R;
import com.example.otherworld.Service.MyFCMService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Common {
    public static final String KEY_ENABLE_BUTTON_NEXT = "ENABLE_BUTTON_NEXT";
    public static final String KEY_CLUB_STORE = "CLUB_SAVE";
    public static final String KEY_GAMEZONE_LOAD_DONE = "GAMEZONE_LOAD_DONE";
    public static final String KEY_DISPLAY_TIME_SLOT = "DISPLAY_TIME_SLOT";
    public static final String KEY_STEP = "STEP";
    public static final String KEY_GAMEZONE_SELECTED = "GAMEZONE_SELECTED";
    public static final int TIME_SLOT_TOTAL = 13;
    public static final Object DISABLE_TAG = "DISABLE";
    public static final String KEY_TIME_SLOT = "TIME_SLOT";
    public static final String KEY_CONFIRM_BOOKING = "CONFIRM_BOOKING";
    public static final String TITLE_KEY = "title";
    public static final String CONTENT_KEY = "content";
    public static String IS_LOGIN = "IsLogin";
    public static Club currentClub;
    public static int step = 0;
    public static String city = "";
    public static Gamezone currentGamezone;
    public static User currentUser;
    public static int currentTimeSlot = -1;
    public static Calendar bookingDate = Calendar.getInstance();
    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd:MM:yyyy");

    public static String convertTimeSlotToString(int slot) {
        switch (slot) {
            case 0:
                return "9:00-10:00";
            case 1:
                return "10:00-11:00";
            case 2:
                return "11:00-12:00";
            case 3:
                return "12:00-13:00";
            case 4:
                return "13:00-14:00";
            case 5:
                return "14:00-15:00";
            case 6:
                return "15:00-16:00";
            case 7:
                return "16:00-17:00";
            case 8:
                return "17:00-18:00";
            case 9:
                return "18:00-19:00";
            case 10:
                return "19:00-20:00";
            case 11:
                return "20:00-21:00";
            case 12:
                return "21:00-22:00";
            default:
                return "Закрыто";

        }


    }

    public static void showNotification(Context context, int not_id, String title, String content, Intent intent) {
        PendingIntent pendingIntent = null;
        if(intent != null)
            pendingIntent = PendingIntent.getActivity(
                    context,
                    not_id,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );
        String NOTIFICATION_CHANNEL_ID = "gamezone_client_app";
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                NotificationChannel notificationChannel =
                        new NotificationChannel(NOTIFICATION_CHANNEL_ID,"My Notification",NotificationManager.IMPORTANCE_DEFAULT);

                notificationChannel.setDescription("Chanel description");
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.BLUE);
                notificationChannel.setVibrationPattern(new long[]{0,1000,500,1000});
                notificationChannel.enableVibration(true);
                notificationManager.createNotificationChannel(notificationChannel);

        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,NOTIFICATION_CHANNEL_ID);
                builder.setContentTitle(title)
                        .setContentText(content)
                        .setAutoCancel(false)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.mipmap.ic_launcher));

        if(pendingIntent != null)
            builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();

        notificationManager.notify(not_id,notification);
    }

    public static enum TOKEN_TYPE{
        CLIENT,MANAGER
    }

    public static String formatShoppingItemName(String name) {
        return name;
    }

    public static void updateToken(final String s) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null)
        {
            MyToken myToken = new MyToken();
            myToken.setToken(s);
            myToken.setTokenType(TOKEN_TYPE.CLIENT);
            myToken.setUserPhone(user.getPhoneNumber());

            FirebaseFirestore.getInstance()
                    .collection("Tokens")
                    .document(user.getPhoneNumber())
                    .set(myToken)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
        }
    }
}