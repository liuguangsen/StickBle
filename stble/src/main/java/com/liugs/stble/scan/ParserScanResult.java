package com.liugs.stble.scan;

import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;

import java.util.List;

/**
 * Created by liuguangsen on 2019/4/21.
 */

public abstract class ParserScanResult extends ScanCallback {

    private boolean isHandling;

    @Override
    public void onScanResult(int callbackType, ScanResult result) {
        super.onScanResult(callbackType, result);
    }

    @Override
    public void onBatchScanResults(List<ScanResult> results) {
        super.onBatchScanResults(results);
    }

    @Override
    public void onScanFailed(int errorCode) {
        super.onScanFailed(errorCode);
    }

    public void start(){
        isHandling = true;
    }

    public void stop(){
        isHandling = false;
    }

    public boolean isHandling() {
        return isHandling;
    }

    public abstract void onScanResult();

    public abstract void onScanError();
}
