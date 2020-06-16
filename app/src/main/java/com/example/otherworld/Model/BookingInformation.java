package com.example.otherworld.Model;

import com.google.firebase.Timestamp;

public class BookingInformation {
    private String customerName,customerPhone,time,gamezoneId,clubId,clubName,clubAddress;
    private Long slot;


    public BookingInformation() {
    }

    public BookingInformation(String customerName, String customerPhone, String time, String gamezoneId, String clubId, String clubName, String clubAddress, Long slot) {
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.time = time;
        this.gamezoneId = gamezoneId;
        this.clubId = clubId;
        this.clubName = clubName;
        this.clubAddress = clubAddress;
        this.slot = slot;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getGamezoneId() {
        return gamezoneId;
    }

    public void setGamezoneId(String gamezoneId) {
        this.gamezoneId = gamezoneId;
    }

    public String getClubId() {
        return clubId;
    }

    public void setClubId(String clubId) {
        this.clubId = clubId;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getClubAddress() {
        return clubAddress;
    }

    public void setClubAddress(String clubAddress) {
        this.clubAddress = clubAddress;
    }

    public Long getSlot() {
        return slot;
    }

    public void setSlot(Long slot) {
        this.slot = slot;
    }


}
