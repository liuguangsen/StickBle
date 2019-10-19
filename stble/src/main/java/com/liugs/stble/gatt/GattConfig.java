package com.liugs.stble.gatt;

import android.bluetooth.BluetoothGattCharacteristic;
import android.support.annotation.NonNull;

import java.util.UUID;

public class GattConfig extends BaseConfig {
    private int mtu;
    private boolean setMtu;
    private long connectDelayTime;
    private long setMtuDelayTime;
    private boolean discoverService;
    private long discoverDelayTime;
    private UUID writeCharacterUUID;
    private UUID notifyCharacterUUID;

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

    public void setDiscoverService(boolean discoverService, long discoverDelayTime,UUID writeCharacterUUID,UUID notifyCharacterUUID) {
        this.discoverService = discoverService;
        this.discoverDelayTime = discoverDelayTime;
        this.writeCharacterUUID = writeCharacterUUID;
        this.notifyCharacterUUID = notifyCharacterUUID;
    }

    public static class Builder{
        private GattConfig config;
        private int mtu;
        private boolean setMtu;
        private long connectDelayTime;
        private long setMtuDelayTime;
        private boolean discoverService;
        private long discoverDelayTime;
        private UUID writeCharacterUUID;
        private UUID notifyCharacterUUID;

        @NonNull
        private String mac;

        public Builder(@NonNull String mac) {
            config = new GattConfig(mac);
        }

        public String getMac() {
            return mac;
        }

        public Builder setMac(String mac) {
            this.mac = mac;
            return this;
        }

        public int getMtu() {
            return mtu;
        }

        public boolean isSetMtu() {
            return setMtu;
        }

        public Builder setMtu(boolean setMtu, int mtu,long setMtuDelayTime) {
            this.setMtu = setMtu;
            this.mtu = mtu;
            this.setMtuDelayTime = setMtuDelayTime;
            config.setMtu(setMtu,mtu,setMtuDelayTime);
            return this;
        }

        public long getSetMtuDelayTime() {
            return setMtuDelayTime;
        }

        public long getConnectDelayTime() {
            return connectDelayTime;
        }

        public Builder setConnectDelayTime(long connectDelayTime) {
            this.connectDelayTime = connectDelayTime;
            config.setConnectDelayTime(connectDelayTime);
            return this;
        }

        public boolean isDiscoverService() {
            return discoverService;
        }

        public long getDiscoverDelayTime() {
            return discoverDelayTime;
        }

        public Builder setDiscoverService(boolean discoverService, long discoverDelayTime,UUID writeCharacterUUID,UUID notifyCharacterUUID) {
            this.discoverService = discoverService;
            this.discoverDelayTime = discoverDelayTime;
            this.writeCharacterUUID = writeCharacterUUID;
            this.notifyCharacterUUID = notifyCharacterUUID;
            config.setDiscoverService(discoverService,discoverDelayTime,writeCharacterUUID,notifyCharacterUUID);
            return this;
        }

        public GattConfig build(){
            return config;
        }
    }
}
