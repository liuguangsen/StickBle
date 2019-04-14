package com.liugs.stble;

import android.content.Context;

/**
 * 初始化蓝牙的公共配置
 */

public class BleManager {

    private BleManager() {
    }

    private static class Holder {
        static BleManager sBleManager = new BleManager();
    }

    public static BleManager getInstance() {
        return Holder.sBleManager;
    }

    public void init(Context context){

    }
}
