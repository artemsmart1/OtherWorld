package com.example.otherworld.Interface;

import com.example.otherworld.Model.BookingInformation;

public interface IBookinginfoLoadListener {
    void onBookinginfoLoadEmpty();
    void onBookinginfoSuccess(BookingInformation bookingInformation);
    void onBookinginfoFailed(String message);
}
