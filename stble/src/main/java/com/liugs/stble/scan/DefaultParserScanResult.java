package com.liugs.stble.scan;

import android.bluetooth.le.ScanResult;
import android.os.Handler;
import android.util.Log;

import java.util.List;

/**
 * Created by liuguangsen on 2019/4/21.
 * 默认的应用层数据解析工作者
 */
public class DefaultParserScanResult<Result> extends BaseParser<Result> {
    private Handler mainHandler = new Handler();
    private DefaultCallback callback;
    private ScanLocal<Result> scanLocal;

    public DefaultParserScanResult() {
    }

    @Override
    public void onScanResult(int callbackType, ScanResult result) {
        super.onScanResult(callbackType, result);
        Log.i("MainActivity","onScanResult");
        if (result != null && callback != null) {
            Result parser = scanLocal.parser(result);
            if (parser != null) {
                callback.onScanResult(result);
            } else {
                callback.onScanResult(result);
            }
        }
    }

    @Override
    public void onBatchScanResults(List<ScanResult> results) {
        super.onBatchScanResults(results);
    }

    @Override
    public void onScanFailed(int errorCode) {
        super.onScanFailed(errorCode);
    }

    public void start(ScanLocal<Result> local) {
        super.start(local);
        Log.i("MainActivity","start");
        scanLocal = local;
        callback = (DefaultCallback) local.getCallback();
        BleScanConfig config = local.getConfig();
        mainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 到时间先把系统的扫描停止，再处理下到时间的回调，最后销毁当前的数据解析
                BleScanManager.getInstance().stopSystemBleScan();
                onScanFinished();
                stop();

            }
        }, config.getTime());
    }

    public void stop() {
        super.stop();
        release();
        Log.i("MainActivity","stop");
    }

    private void release() {
        callback = null;
        mainHandler.removeCallbacksAndMessages(null);
    }

    private void onScanFinished() {
        if (!isHasCanceled() && callback != null) {
            callback.onFinished();
        }
    }
}
