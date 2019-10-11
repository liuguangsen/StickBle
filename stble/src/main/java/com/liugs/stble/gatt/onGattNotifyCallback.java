package com.liugs.stble.gatt;

import android.bluetooth.BluetoothGattCharacteristic;

public interface onGattNotifyCallback {
    void onNotifyResult(BluetoothGattCharacteristic characteristic);
}
