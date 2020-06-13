package com.example.otherworld.Common;

import com.example.otherworld.Model.Club;
import com.example.otherworld.Model.Gamezone;
import com.example.otherworld.Model.User;

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
    public static String IS_LOGIN = "IsLogin";
    public static Club currentClub;
    public static int step = 0;
    public static String city = "";
    public static Gamezone currentGamezone;
    public static User currentUser;
    public static int currentTimeSlot = -1;
    public static Calendar currentDate = Calendar.getInstance();
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
}