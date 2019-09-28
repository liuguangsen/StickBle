package com.liugs.stble.scan;

import android.bluetooth.le.ScanResult;

/**
 * 默认数据解析,以及数据上报
 *
 * @param <R> 解析实体类
 */
public class ScanLocal<R> {

    private BleScanConfig config;

    private BaseCallback<R> callback;

    public BleScanConfig getConfig() {
        return config;
    }

    public void setConfig(BleScanConfig config) {
        this.config = config;
    }

    public BaseCallback<R> getCallback() {
        return callback;
    }

    public void setCallback(BaseCallback<R> callback) {
        this.callback = callback;
    }

    protected R parser(ScanResult result){
        return null;
    }
}
