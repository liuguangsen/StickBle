package com.liugs.stble.gatt;

import android.support.annotation.NonNull;

public class GattConfig extends BaseConfig {
    private int mtu;
    private boolean setMtu;
    private long connectDelayTime;
    private long setMtuDelayTime;
    private boolean discoverService;
    private long discoverDelayTime;

    @NonNull
    private String mac;

    public GattConfig(@NonNull String mac) {
        this.mac = mac;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public int getMtu() {
        return mtu;
    }

    public boolean isSetMtu() {
        return setMtu;
    }

    public void setMtu(boolean setMtu, int mtu,long setMtuDelayTime) {
        this.setMtu = setMtu;
        this.mtu = mtu;
        this.setMtuDelayTime = setMtuDelayTime;
    }

    public long getSetMtuDelayTime() {
        return setMtuDelayTime;
    }

    public long getConnectDelayTime() {
        return connectDelayTime;
    }

    public void setConnectDelayTime(long connectDelayTime) {
        this.connectDelayTime = connectDelayTime;
    }

    public boolean isDiscoverService() {
        return discoverService;
    }

    public long getDiscoverDelayTime() {
        return discoverDelayTime;
    }

    public void setDiscoverService(boolean discoverService, long discoverDelayTime) {
        this.discoverService = discoverService;
        this.discoverDelayTime = discoverDelayTime;
    }
}
