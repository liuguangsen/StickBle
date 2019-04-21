package com.liugs.stble.scan;

import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;

import com.liugs.stble.BleManager;

import java.lang.ref.WeakReference;

/**
 * Created by liuguangsen on 2019/4/21.
 */

public class BleScanManager {

    private final Object mLock = new Object();
    private BluetoothLeScanner bluetoothLeScanner;
    private WeakReference<IScanResultCallback> callback;

    private BleScanManager() {
    }

    private static class ScanHolder{
        static BleScanManager instance = new BleScanManager();
    }

    public static BleScanManager getInstance(){
        return ScanHolder.instance;
    }

    public void startScan(IScanResultCallback callback){
        if (callback == null){
            return;
        }
        this.callback = new WeakReference<>(callback);
        if (bluetoothLeScanner == null) {
            synchronized (mLock) {
                if (bluetoothLeScanner == null)
                    bluetoothLeScanner = BleManager.getInstance().getBluetoothAdapter().getBluetoothLeScanner();
            }
        }
        bluetoothLeScanner.startScan(sysScanCallback);
    }

    public void stopScan(){
        synchronized (mLock) {
            if (sysScanCallback.isHandling()) {
                sysScanCallback.stop();
            }
            callback.clear();
        }
        bluetoothLeScanner.stopScan(sysScanCallback);
    }

    private final ParserScanResult sysScanCallback = new ParserScanResult() {
        @Override
        public void onScanResult() {
            IScanResultCallback iScanResultCallback = callback.get();
            if (iScanResultCallback != null){
                iScanResultCallback.onScanResult();
            }
        }

        @Override
        public void onScanError() {
            IScanResultCallback iScanResultCallback = callback.get();
            if (iScanResultCallback != null){
                iScanResultCallback.onScanError();
            }
        }
    };
}
