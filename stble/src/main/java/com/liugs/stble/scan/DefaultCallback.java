package com.liugs.stble.scan;

import android.bluetooth.le.ScanResult;

/**
 * 默认的数据上报
 */
public interface DefaultCallback<Result> extends BaseCallback<Result> {
    void onScanResult(ScanResult result);

    void onFinished();
}
