package com.liugs.stble.scan;

import android.bluetooth.le.ScanResult;

/**
 * 默认数据解析,以及数据上报
 *
 * @param <Result> 解析实体类
 */
public class ScanLocal<Result> {

    private BleScanConfig config;

    private Class<Result> clz;

    private BaseCallback<Result> callback;

    public BleScanConfig getConfig() {
        return config;
    }

    public ScanLocal(Class<Result> clz) {
        this.clz = clz;
    }

    public void setConfig(BleScanConfig config) {
        this.config = config;
    }



    public BaseCallback<Result> getCallback() {
        return callback;
    }

    public Class<Result> getClz() {
        return clz;
    }

    public void setCallback(BaseCallback<Result> callback) {
        this.callback = callback;
    }

    protected Result parser(ScanResult result){
        return null;
    }
}
