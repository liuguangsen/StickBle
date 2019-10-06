package com.liugs.stble.scan;

/**
 * 默认的数据上报
 */
public interface BleScanCallback<Result> extends BaseCallback<Result> {
    void onScanResult(Result result);

    void onFinished();
}
