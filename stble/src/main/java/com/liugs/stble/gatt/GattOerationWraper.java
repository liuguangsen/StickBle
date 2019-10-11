package com.liugs.stble.gatt;

import android.os.Handler;

public class GattOerationWraper {
    // gatt操作
    private GattOperation operation;
    // gatt连路管理
    private Handler handler = new Handler();
    // gat写入
    private GattWriteTask gattWriteTask;

    public GattOerationWraper() {
    }
}
