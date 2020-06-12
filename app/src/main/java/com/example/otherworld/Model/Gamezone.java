package com.example.otherworld.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Gamezone implements Parcelable {
    private String name,gamezoneId;

    public Gamezone() {
    }

    protected Gamezone(Parcel in) {
        name = in.readString();
        gamezoneId = in.readString();
    }

    public static final Creator<Gamezone> CREATOR = new Creator<Gamezone>() {
        @Override
        public Gamezone createFromParcel(Parcel in) {
            return new Gamezone(in);
        }

        @Override
        public Gamezone[] newArray(int size) {
            return new Gamezone[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(gamezoneId);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGamezoneId() {
        return gamezoneId;
    }

    public void setGamezoneId(String gamezoneId) {
        this.gamezoneId = gamezoneId;
    }
}
