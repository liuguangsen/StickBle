package com.liugs.stble.scan;

import android.bluetooth.le.BluetoothLeScanner;
import android.util.Log;

import com.liugs.stble.BleManager;
import com.liugs.stble.exception.BluetoothNotSupportException;

/**
 * Created by liuguangsen on 2019/4/21.
 * 系统蓝牙服务功能控制器主要操作系统蓝牙api
 * 开启扫描和关闭扫描以及配置扫描的选项
 */

public class BleScanManager {

    private static final Object SCAN_LOCK = new Object();
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
        BaseParser<Result> resultBaseParser;
        if (bluetoothLeScanner == null) {
            synchronized (SCAN_LOCK) {
                if (bluetoothLeScanner == null)
                    bluetoothLeScanner = BleManager.getInstance().getBluetoothAdapter().getBluetoothLeScanner();
            }
        }
        if (bluetoothLeScanner == null){
            try {
                throw new BluetoothNotSupportException("手机不支持ble扫描");
            } catch (BluetoothNotSupportException e) {
                return;
            }
        }
        resultBaseParser = ParserResultFactory.buildParser(scanLocal);
        sysScanCallback = resultBaseParser;
        BleScanConfig config = scanLocal.getConfig();
        if (config != null && config.getScanSettings() != null) {
            bluetoothLeScanner.startScan(config.getBleScanFilters(), config.getScanSettings(), resultBaseParser);
        } else {
            Log.i("MainActivity", "startScan");
            bluetoothLeScanner.startScan(resultBaseParser);
        }
        resultBaseParser.start();
    }

    public void stopScan() {
        stopSystemBleScan();
        synchronized (SCAN_LOCK) {
            if (sysScanCallback != null && sysScanCallback.isHandling()) {
                sysScanCallback.stop();
            }
        }

    }

    public void stopSystemBleScan() {
        if (sysScanCallback != null && sysScanCallback.isHandling()) {
            Log.i("MainActivity", "stopSystemBleScan");
            bluetoothLeScanner.stopScan(sysScanCallback);
        }
    }
}
