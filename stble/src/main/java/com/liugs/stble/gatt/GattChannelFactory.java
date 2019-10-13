package com.liugs.stble.gatt;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

public class GattChannelFactory {
    public static GattOperationWrapper createWraper(Context context, String mac){
        return new GattOperationWrapper(context,mac);
    }

    public static GattOperationWrapper createWraper(Context context, BluetoothDevice device){
        return new GattOperationWrapper(context,device);
    }
}
