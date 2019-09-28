package com.liugs.stble.scan;

import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import java.util.List;

/**
 * Created by liuguangsen on 2019/4/21.
 */

public class ParserScanResult<Result> extends BaseParser<Result> {

    private boolean isHandling;
    private HandlerThread handlerThread;
    private Handler handler;
    private Handler mainHandler = new Handler();
    private ScanLocal scanLocal;

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

    public void start(ScanLocal<Result> local) {
        BleScanConfig config = local.getConfig();
        isHandling = true;
        handlerThread = new HandlerThread("ParserScanResult");
        handlerThread.start();
        handler = new ParserHandler(handlerThread.getLooper());
        mainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stop();
                onScanFinished();
            }
        },config.getTime());
    }

    public void stop() {
        isHandling = false;
        handlerThread.quitSafely();
        handlerThread = null;
        handler.removeCallbacksAndMessages(null);
        handler = null;
    }

    public boolean isHandling() {
        return isHandling;
    }

    public void onScanFinished(){

    }

    private static final int MSG_PARSER_RESULT = 1001;

    private static class ParserHandler extends android.os.Handler {
        public ParserHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_PARSER_RESULT:
                    break;
                default:
                    break;
            }
        }
    }
}
