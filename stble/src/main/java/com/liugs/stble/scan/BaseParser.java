package com.liugs.stble.scan;

import android.bluetooth.le.ScanCallback;

/**
 * 应用层数据基础解析工作类
 * 注意生命周期
 * start,stop
 */
public class BaseParser<Result> extends ScanCallback {
    private boolean isHandling;
    private boolean isHasCanceled;

    protected void start(ScanLocal<Result> scanLocal){
        isHandling = true;
    }

    protected void stop(){
        isHandling = false;
    }

    boolean isHandling() {
        return isHandling;
    }

    void cancel(){
        isHasCanceled = true;
    }

    boolean isHasCanceled() {
        return isHasCanceled;
    }
}
