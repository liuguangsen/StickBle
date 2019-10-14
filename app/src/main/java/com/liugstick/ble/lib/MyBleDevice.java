package com.liugstick.ble.lib;

import android.os.Parcel;
import android.os.Parcelable;

public class MyBleDevice implements Parcelable {
    private String name;
    private String mac;

    public MyBleDevice() {
    }

    protected MyBleDevice(Parcel in) {
        name = in.readString();
        mac = in.readString();
    }

    public static final Creator<MyBleDevice> CREATOR = new Creator<MyBleDevice>() {
        @Override
        public MyBleDevice createFromParcel(Parcel in) {
            return new MyBleDevice(in);
        }

        @Override
        public MyBleDevice[] newArray(int size) {
            return new MyBleDevice[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(mac);
    }
}
