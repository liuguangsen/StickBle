package com.liugs.stble.scan;

import android.bluetooth.le.BluetoothLeScanner;
import android.util.Log;

import com.liugs.stble.BleManager;

/**
 * Created by liuguangsen on 2019/4/21.
 * 系统蓝牙服务功能控制器主要操作系统蓝牙api
 * 开启扫描和关闭扫描以及配置扫描的选项
 */

public class BleScanManager {

    private final Object mLock = new Object();
    private BluetoothLeScanner bluetoothLeScanner;

    // TODO 解析系统的callback 针对不同的设置进行定制
    private BaseParser sysScanCallback;

    private BleScanManager() {
    }

    private static class ScanHolder {
        static BleScanManager instance = new BleScanManager();
    }

    public static BleScanManager getInstance() {
        return ScanHolder.instance;
    }

    public <Result> void startScan(ScanLocal<Result> scanLocal) {
        BaseParser<Result> resultBaseParser = null;
        if (bluetoothLeScanner == null) {
            synchronized (mLock) {
                if (bluetoothLeScanner == null)
                    bluetoothLeScanner = BleManager.getInstance().getBluetoothAdapter().getBluetoothLeScanner();
                resultBaseParser = ParserResultFactory.buildParser(scanLocal);
                sysScanCallback = resultBaseParser;
            }
        }
        // TODO 配置扫描参数
        if (resultBaseParser != null) {
            Log.i("MainActivity","startScan");
            bluetoothLeScanner.startScan(resultBaseParser);
            // TODO 配置扫描时间的参数
            resultBaseParser.start(scanLocal);
        }
    }

    public void stopScan() {
        synchronized (mLock) {
            if (sysScanCallback.isHandling()) {
                sysScanCallback.stop();
            }
        }
        stopSystemBleScan();
    }

    public void stopSystemBleScan() {
        Log.i("MainActivity","stopSystemBleScan");
        bluetoothLeScanner.stopScan(sysScanCallback);
    }

    // TODO 取消扫描 目前只影响是否finish的上报
    public void cancelScan() {
        synchronized (mLock) {
            if (sysScanCallback.isHandling()) {
                sysScanCallback.cancel();
            }
        }
        stopScan();
    }
}
