package com.liugs.stble.gatt;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

public class GattChannelFactory {
    public static GattOperationWrapper createWrapper(Context context, String mac) {
        return new GattOperationWrapper(context, mac);
    }

    public static GattOperationWrapper createWrapper(Context context, BluetoothDevice device) {
        return new GattOperationWrapper(context, device);
    }
}
