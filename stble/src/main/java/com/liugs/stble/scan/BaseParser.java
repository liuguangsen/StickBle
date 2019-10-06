package com.liugs.stble.scan;

import android.bluetooth.le.ScanCallback;

/**
 * 应用层数据基础解析工作类
 * 注意生命周期
 * start,stop
 */
public class BaseParser<Result> extends ScanCallback {
    protected ScanLocal<Result> scanLocal;
    private boolean isHandling;

    protected void start(){
        isHandling = true;
    }

    protected void stop(){
        isHandling = false;
    }

    public void setScanLocal(ScanLocal<Result> scanLocal) {
        this.scanLocal = scanLocal;
    }

    boolean isHandling() {
        return isHandling;
    }
}
