package com.liugs.stble.scan;

import android.bluetooth.le.ScanResult;
import android.os.Handler;
import android.util.Log;

import com.liugs.stble.exception.ParamException;

import java.util.List;

/**
 * Created by liuguangsen on 2019/4/21.
 * 默认的应用层数据解析工作者
 */
public class DefaultParserScanResult<Result> extends BaseParser<Result> {
    private Handler mainHandler = new Handler();
    private BleScanCallback<Result> callback;

    public DefaultParserScanResult() {
    }

    @Override
    public void onScanResult(int callbackType, ScanResult result) {
        super.onScanResult(callbackType, result);
        Log.i("MainActivity", "onScanResult");
        if (result != null && callback != null && isHandling()) {
            Result parser = scanLocal.parser(result);
            if (parser != null) {
                callback.onScanResult(parser);
            } else {
                Class<Result> clz = scanLocal.getClz();
                if (clz != null) {
                    callback.onScanResult(clz.cast(result));
                } else {
                    throw new ParamException("ScanLocal 泛型参数 错误");
                }
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

    public void start() {
        super.start();
        Log.i("MainActivity", "start");
        callback = (BleScanCallback<Result>) scanLocal.getCallback();
        BleScanConfig config = scanLocal.getConfig();
        mainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 到时间先把系统的扫描停止，再处理下到时间的回调，最后销毁当前的数据解析
                BleScanManager.getInstance().stopSystemBleScan();
                onScanFinished();
                stop();

            }
        }, config == null ? BleScanConfig.DEFAULT_SCAN_TOME : config.getTime());
    }

    public void stop() {
        super.stop();
        release();
        Log.i("MainActivity", "stop");
    }

    private void release() {
        callback = null;
        mainHandler.removeCallbacksAndMessages(null);
    }

    private void onScanFinished() {
        if (!isHandling() && callback != null) {
            callback.onFinished();
        }
    }
}
