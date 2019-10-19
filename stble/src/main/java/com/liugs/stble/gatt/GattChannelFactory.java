package com.liugs.stble.gatt;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

public class GattChannelFactory {
    public static GattController createWrapper(Context context, String mac) {
        return new GattController(context, mac);
    }

    public static GattController createWrapper(Context context, BluetoothDevice device) {
        return new GattController(context, device);
    }
}
