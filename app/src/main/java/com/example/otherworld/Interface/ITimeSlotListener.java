package com.example.otherworld.Interface;

import com.example.otherworld.Model.TimeSlot;


import java.util.List;

public interface ITimeSlotListener {
    void onTimeSlotLoadSuccess(List<TimeSlot> timeSlotList);
    void onTimeSlotLoadFailed(String message);
    void onTimeSlotLoadEmpty();
}
