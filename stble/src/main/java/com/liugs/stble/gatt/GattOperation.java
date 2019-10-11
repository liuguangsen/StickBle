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
    private onGattWriteCallback gattWriteCallback;
    private onGattNotifyCallback gattNotifyCallback;

    private BleGattConfig gattConfig;

    private volatile int currentState;
    private volatile boolean isClosed;
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

    public void setGattWriteCallback(onGattWriteCallback gattWriteCallback) {
        this.gattWriteCallback = gattWriteCallback;
    }

    public void setGattNotifyCallback(onGattNotifyCallback gattNotifyCallback) {
        this.gattNotifyCallback = gattNotifyCallback;
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
        if (gattOperationCallback != null) {
            gattOperationCallback.onOperationState(TYPE_CONNECT, currentState);
        }
    }

    public void reconnect() {
        if (bluetoothGatt == null) {
            return;
        }
        boolean connect = bluetoothGatt.connect();
        synchronized (LOCK) {
            if (connect) {
                currentState = STATE_RE_CONNECTING;
            } else {
                currentState = STATE_RE_CONNECT_FAILED;
            }
        }
        if (gattOperationCallback != null) {
            gattOperationCallback.onOperationState(TYPE_RE_CONNECT, currentState);
        }
    }

    public void discoverService() {
        if (bluetoothGatt == null) {
            return;
        }
        boolean isSuccess = bluetoothGatt.discoverServices();
        synchronized (LOCK) {
            if (isSuccess) {
                currentState = STATE_DISCOVER_SERVICING;
            } else {
                currentState = STATE_DISCOVER_SERVICE_FAILED;
            }
        }
        if (gattOperationCallback != null) {
            gattOperationCallback.onOperationState(TYPE_DISCOVER_SERVICE, currentState);
        }
    }



    public void disConnecct() {
        if (bluetoothGatt == null) {
            return;
        }
        bluetoothGatt.disconnect();
    }

    public void close() {
        if (bluetoothGatt == null) {
            return;
        }
        bluetoothGatt.close();
        synchronized (LOCK) {
            isClosed = true;
        }
    }

    public boolean isClosed() {
        return isClosed;
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
                // 参照源码BluetoothProfile.STATE_DISCONNECTED，统统认为connect操作失败
                if (!hasFirstConnect) {
                    currentState = STATE_FIRST_CONNECT_FAILED;
                } else {
                    currentState = STATE_CONNECT_FAILED;
                }
            }
        }
        if (gattOperationCallback != null) {
            int currentType = hasFirstConnect ? TYPE_RE_CONNECT : TYPE_CONNECT;
            gattOperationCallback.onOperationState(currentType, currentState);
        }
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        synchronized (LOCK) {
            // connect操作成功
            if (status == BluetoothGatt.GATT_SUCCESS) {
                currentState = STATE_DISCOVER_SERVICE_FAILED;
            } else {
                currentState = STATE_DISCOVER_SERVICE_FAILED;
            }
        }
        if (gattOperationCallback != null) {
            gattOperationCallback.onOperationState(TYPE_DISCOVER_SERVICE, currentState);
        }
    }

    public void write(BluetoothGattCharacteristic characteristic) {
        if (bluetoothGatt == null || currentState != STATE_FIRST_CONNECT_SUCCESS || currentState != STATE_CONNECT_SUCCESS || isClosed) {
            if (gattWriteCallback != null) {
                gattWriteCallback.onWriteResult(false);
            }
        } else {
            boolean isSuccess = bluetoothGatt.writeCharacteristic(characteristic);
            if (!isSuccess) {
                if (gattWriteCallback != null) {
                    gattWriteCallback.onWriteResult(false);
                }
            }
        }
    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        if (gattWriteCallback != null) {
            gattWriteCallback.onWriteResult(status == BluetoothGatt.GATT_SUCCESS);
        }
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        if (gattNotifyCallback != null){
            gattNotifyCallback.onNotifyResult(characteristic);
        }
    }

    @Override
    public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
        // TODO可靠传输 先不搞
    }

    public void setMtu(int mtu) {
        if (bluetoothGatt == null) {
            return;
        }
        boolean isSuccess = bluetoothGatt.requestMtu(mtu);
        synchronized (LOCK) {
            if (isSuccess) {
                currentState = STATE_SET_MUTING;
            } else {
                currentState = STATE_SET_MTU_FAILED;
            }
        }
        if (gattOperationCallback != null) {
            gattOperationCallback.onOperationState(TYPE_SET_MTU, currentState);
        }
    }

    @Override
    public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
        synchronized (LOCK) {
            // connect操作成功
            if (status == BluetoothGatt.GATT_SUCCESS) {
                currentState = STATE_SET_MTU_SUCCESS;
            } else {
                currentState = STATE_SET_MTU_FAILED;
            }
        }
        if (gattOperationCallback != null) {
            gattOperationCallback.onOperationState(TYPE_SET_MTU, currentState);
        }
    }

    @IntDef({TYPE_CONNECT, TYPE_RE_CONNECT, TYPE_DISCOVER_SERVICE, TYPE_SET_MTU})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {
    }

    /**
     * 定义几个常量
     */
    public static final int TYPE_CONNECT = 1;

    public static final int TYPE_RE_CONNECT = 3;

    public static final int TYPE_DISCOVER_SERVICE = 4;

    public static final int TYPE_SET_MTU = 5;

    @IntDef({STATE_FIRST_CONNECT_START_SUCCESS, STATE_FIRST_CONNECT_START_FAILED, STATE_UNKNOWN,
            STATE_FIRST_CONNECT_SUCCESS, STATE_FIRST_CONNECT_FAILED, STATE_CONNECT_SUCCESS, STATE_CONNECT_FAILED,
            STATE_RE_CONNECTING, STATE_DISCOVER_SERVICING, STATE_DISCOVER_SERVICE_FAILED, STATE_DISCOVER_SERVICE_SUCCESS,
            STATE_SET_MUTING, STATE_SET_MTU_FAILED, STATE_SET_MTU_SUCCESS})
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

    public static final int STATE_RE_CONNECTING = 7;

    public static final int STATE_RE_CONNECT_FAILED = 8;

    public static final int STATE_DISCOVER_SERVICING = 9;

    public static final int STATE_DISCOVER_SERVICE_FAILED = 10;

    public static final int STATE_DISCOVER_SERVICE_SUCCESS = 11;

    public static final int STATE_SET_MUTING = 12;

    public static final int STATE_SET_MTU_FAILED = 13;

    public static final int STATE_SET_MTU_SUCCESS = 14;

    public static final int STATE_UNKNOWN = 0;
}
