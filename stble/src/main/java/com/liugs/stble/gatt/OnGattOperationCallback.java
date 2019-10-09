package com.liugs.stble.gatt;

public interface OnGattOperationCallback {
    void onOperationState(@GattOperation.Type int type, @GattOperation.State int state);
}
