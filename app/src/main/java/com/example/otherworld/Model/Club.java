package com.example.otherworld.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Club implements Parcelable {
    private String name,address,phone,openHours,clubId;

    public Club() {
    }

    protected Club(Parcel in) {
        name = in.readString();
        address = in.readString();
        phone = in.readString();
        openHours = in.readString();
        clubId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(phone);
        dest.writeString(openHours);
        dest.writeString(clubId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Club> CREATOR = new Creator<Club>() {
        @Override
        public Club createFromParcel(Parcel in) {
            return new Club(in);
        }

        @Override
        public Club[] newArray(int size) {
            return new Club[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOpenHours() {
        return openHours;
    }

    public void setOpenHours(String openHours) {
        this.openHours = openHours;
    }

    public String getClubId() {
        return clubId;
    }

    public void setClubId(String clubId) {
        this.clubId = clubId;
    }
}
