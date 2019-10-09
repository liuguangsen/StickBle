package com.liugs.stble.gatt;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 封装Gatt操作api
 * connect,discoverService,setMtu,read,write
 */
public class GattOperation extends BluetoothGattCallback {

    private static final Object LOCK = new Object();
    private BluetoothGatt bluetoothGatt;

    private Context context;

    private BluetoothDevice bluetoothDevice;

    private String mac;

    private OnGattOperationCallback gattOperationCallback;

    private BleGattConfig gattConfig;

    private int currentState;
    private boolean hasFirstConnect;

    public GattOperation(Context context, String mac) {
        this.context = context;
        this.mac = mac;
        this.bluetoothDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(mac);
    }

    public GattOperation(Context context, BluetoothDevice bluetoothDevice) {
        this.context = context;
        this.bluetoothDevice = bluetoothDevice;
        this.mac = bluetoothDevice.getAddress();
    }

    public void setGattOperationCallback(OnGattOperationCallback gattOperationCallback) {
        this.gattOperationCallback = gattOperationCallback;
    }

    public void connect(boolean asynchronous) {
        bluetoothGatt = bluetoothDevice.connectGatt(context, asynchronous, this);
        synchronized (LOCK) {
            if (bluetoothGatt == null) {
                currentState = STATE_FIRST_CONNECT_START_FAILED;
            } else {
                currentState = STATE_FIRST_CONNECT_START_SUCCESS;
            }
        }
        gattOperationCallback.onOperationState(TYPE_CONNECT, currentState);
    }

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {

        synchronized (LOCK) {
            // connect操作成功
            if (status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothProfile.STATE_CONNECTED) {
                // 第一次建立gatt成功
                if (!hasFirstConnect) {
                    hasFirstConnect = true;
                    currentState = STATE_FIRST_CONNECT_SUCCESS;
                } else {
                    currentState = STATE_CONNECT_SUCCESS;
                }
            } else {
                // 统统认为connect操作失败，源码就是BluetoothProfile.STATE_DISCONNECTED
                if (!hasFirstConnect) {
                    currentState = STATE_FIRST_CONNECT_FAILED;
                } else {
                    currentState = STATE_CONNECT_FAILED;
                }
            }
        }
        gattOperationCallback.onOperationState(TYPE_CONNECT, currentState);
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        super.onServicesDiscovered(gatt, status);
    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        super.onCharacteristicWrite(gatt, characteristic, status);
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        super.onCharacteristicChanged(gatt, characteristic);
    }

    @Override
    public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
        super.onMtuChanged(gatt, mtu, status);
    }

    @IntDef({TYPE_CONNECT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {
    }

    /**
     * 定义几个常量
     */
    public static final int TYPE_CONNECT = 1;

    @IntDef({STATE_FIRST_CONNECT_START_SUCCESS, STATE_FIRST_CONNECT_START_FAILED, STATE_UNKNOWN,
            STATE_FIRST_CONNECT_SUCCESS, STATE_FIRST_CONNECT_FAILED, STATE_CONNECT_SUCCESS, STATE_CONNECT_FAILED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {
    }

    /**
     * 定义几个常量
     */
    public static final int STATE_FIRST_CONNECT_START_SUCCESS = 1;

    public static final int STATE_FIRST_CONNECT_START_FAILED = 2;

    public static final int STATE_FIRST_CONNECT_SUCCESS = 3;

    public static final int STATE_FIRST_CONNECT_FAILED = 4;

    public static final int STATE_CONNECT_SUCCESS = 5;

    public static final int STATE_CONNECT_FAILED = 6;

    public static final int STATE_UNKNOWN = 0;
}
