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

public abstract class ParserScanResult extends ScanCallback {

    private boolean isHandling;
    private HandlerThread handlerThread;
    private Handler handler;
    private Handler mainHandler = new Handler();

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

    public void start(BleScanConfig config) {
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
        handler.removeMessages(MSG_PARSER_RESULT);
        handler = null;
    }

    public boolean isHandling() {
        return isHandling;
    }

    public abstract void onScanResult();

    public abstract void onScanError();

    public abstract void onScanFinished();

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
