package com.liugs.stble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.Context;
import android.support.annotation.NonNull;

import com.liugs.stble.exception.BluetoothNotSupportException;

/**
 * 初始化蓝牙的公共配置
 */

public class BleManager {

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothLeScanner bluetoothLeScanner;
    private Context appContext;

    private BleManager() {
    }

    private static class Holder {
        static BleManager sBleManager = new BleManager();
    }

    public static BleManager getInstance() {
        return Holder.sBleManager;
    }

    public void init(@NonNull Context context){
        this.appContext = context;
        BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        if (bluetoothManager == null){
            try {
                throw new BluetoothNotSupportException("No found BLUETOOTH_SERVICE");
            } catch (BluetoothNotSupportException e) {
                e.printStackTrace();
            }
            return;
        }
        bluetoothAdapter = bluetoothManager.getAdapter();
        if (bluetoothAdapter == null){
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        if (bluetoothAdapter == null){
            try {
                throw new BluetoothNotSupportException("Bluetooth is not supported in this hardware platform(your phone)");
            } catch (BluetoothNotSupportException e) {
                e.printStackTrace();
            }
        }
    }

    public BluetoothAdapter getBluetoothAdapter() {
        return bluetoothAdapter;
    }

    public Context getAppContext() {
        return appContext;
    }
}
