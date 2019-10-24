package com.liugs.stble.gatt;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.liugs.stble.gatt.callback.ControllerCallback;
import com.liugs.stble.gatt.callback.UiGattCallback;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * gatt链路,辅助gatt链路的核心类
 * 1.connect,discoverService(可选),setMtu(可选)
 * 缺陷：只能控制client主动操作， server的操作待优化，目前的优化点上，给建立链路设置合适的总超时时间进行close操作
 * 这个操作待查看源码，初步想法上再来一层GattOperationController
 */
public class GattOperationWrapper  implements BaseOperationWrapper , OnGattOperationCallback {
    // gatt操作
    private GattOperation operation;
    // gatt连路管理
    private GattHandler handler;
    // gat写入
    private GattWriteTask gattWriteTask;

    private BluetoothGattCharacteristic characteristic;

    private volatile boolean isStopWrited;

    private GattConfig config;

    private UiGattCallback uiGattCallback;

    private ControllerCallback controllerCallback;

    public GattOperationWrapper(Context context, BluetoothDevice device) {
        operation = new GattOperation(context, device);
        operation.setGattOperationCallback(this);
        handler = new GattHandler(Looper.getMainLooper(), this);
    }

    public GattOperationWrapper(Context context, String mac) {
        operation = new GattOperation(context, mac);
        operation.setGattOperationCallback(this);
        handler = new GattHandler(Looper.getMainLooper(), this);
    }

    public void setUiGattCallback(UiGattCallback uiGattCallback) {
        this.uiGattCallback = uiGattCallback;
    }

    public void setControllerCallback(ControllerCallback controllerCallback) {
        this.controllerCallback = controllerCallback;
    }

    public void setConfig(GattConfig config) {
        this.config = config;
    }

    @Override
    public String getMac() {
        return config.getMac();
    }

    @Override
    public void createGattChannel() {
        if (!GattOerationUtil.checkBluetoothAddress(config.getMac())) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (uiGattCallback != null) {
                        uiGattCallback.onError(config.getMac() + " is not a valid Bluetooth address");
                    }
                }
            });
            return;
        }
        operation.connect(false);
    }

    @Override
    public void closeGattChannel() {
        handler.removeCallbacksAndMessages(null);
        operation.disConnect();
        operation.close();
        operation.clearData();
    }

    @Override
    public void createWriteTask() {
        gattWriteTask = new GattWriteTask(this);
        gattWriteTask.start();
    }

    @Override
    public void write(byte[] src){
        characteristic.setValue(src);
        operation.write(characteristic);
    }

    @Override
    public void writeBackground(byte[] src) {
        if (isStopWrited){
            return;
        }
        if (gattWriteTask == null){
            createWriteTask();
        }
        gattWriteTask.addWriteData(src);
    }

    @Override
    public void stopWrite() {
        gattWriteTask.stopWriteTask();
    }

    @Override
    public void onOperationState(int type, int state) {
        switch (type) {
            case GattOperation.TYPE_CONNECT:
                handleConnectType(state);
                break;
            case GattOperation.TYPE_DISCOVER_SERVICE:
                handleDiscoverService(state);
                break;
            case GattOperation.TYPE_SET_MTU:
                handleSetMtu(state);
                break;
            case GattOperation.TYPE_RE_CONNECT:
                handleReConnectType(state);
                break;
            default:
                break;
        }
    }

    private void setMtu() {
        operation.setMtu(config.getMtu());
    }

    private void handleReConnectType(int state) {
        switch (state) {
            case GattOperation.STATE_RE_CONNECT_FAILED:
                handler.sendEmptyMessageDelayed(MSG_RE_CONNECT, config.getConnectDelayTime());
                break;
            case GattOperation.STATE_RE_CONNECTING:
                // 重连操作成功，等待结果
                break;
            case GattOperation.STATE_CONNECT_SUCCESS:
                // 重连成功
                break;
            case GattOperation.STATE_CONNECT_FAILED:
                handler.sendEmptyMessageDelayed(MSG_RE_CONNECT, config.getConnectDelayTime());
                break;
            default:
                break;
        }
    }

    private void handleSetMtu(int state) {
        switch (state) {
            case GattOperation.STATE_SET_MTU_FAILED:
                handler.sendEmptyMessageDelayed(MSG_SET_MTU, config.getSetMtuDelayTime());
                break;
            case GattOperation.STATE_SET_MTU_SUCCESS:
                callCreateChannel();
                break;
            case GattOperation.STATE_SET_MUTING:
                // 设置mtu操作成功，等待结果
                break;
            default:
                break;
        }
    }

    private void handleDiscoverService(int state) {
        switch (state) {
            case GattOperation.STATE_DISCOVER_SERVICE_FAILED:
                handler.sendEmptyMessageDelayed(MSG_DISCOVER_SERVICE, config.getDiscoverDelayTime());
                break;
            case GattOperation.STATE_DISCOVER_SERVICE_SUCCESS:
                BluetoothGattService service = operation.getGatt().getService(config.getServiceUUID());
                if (service != null){
                    characteristic = service.getCharacteristic(config.getWriteCharacterUUID());
                }
                if (characteristic == null && config.isRetry()){
                    handler.sendEmptyMessageDelayed(MSG_DISCOVER_SERVICE, config.getDiscoverDelayTime());
                    return;
                }
                if (config.isSetMtu()) {
                    handler.sendEmptyMessageDelayed(MSG_SET_MTU, config.getSetMtuDelayTime());
                } else {
                    callCreateChannel();
                }
                break;
            case GattOperation.STATE_DISCOVER_SERVICING:
                // discoverService成功，等待结果
                break;
            default:
                break;
        }
    }

    public void handleConnectType(int state) {
        controllerCallback.onState(state);
        switch (state) {
            case GattOperation.STATE_FIRST_CONNECT_START_SUCCESS:
                // 第一次建立连接正常，等待建立成功
                break;
            case GattOperation.STATE_FIRST_CONNECT_START_FAILED:
                // 第一次建立连接异常
                handler.sendEmptyMessageDelayed(MSG_FIRST_CONNECT, config.getConnectDelayTime());
                break;
            case GattOperation.STATE_FIRST_CONNECT_SUCCESS:
                // 建立链路成功了哦
                if (config.isDiscoverService()) {
                    handler.sendEmptyMessageDelayed(MSG_DISCOVER_SERVICE, config.getDiscoverDelayTime());
                } else if (config.isSetMtu()) {
                    handler.sendEmptyMessageDelayed(MSG_SET_MTU, config.getSetMtuDelayTime());
                } else {
                    callCreateChannel();
                }
                break;
            case GattOperation.STATE_FIRST_CONNECT_FAILED:
                // 建立链路失败了哦
                handler.sendEmptyMessageDelayed(MSG_RE_CONNECT, config.getConnectDelayTime());
                break;
        }
    }

    private void callCreateChannel() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (uiGattCallback != null) {
                    uiGattCallback.onCreateChannelResult(true);
                }
            }
        });
    }

    private static final int MSG_FIRST_CONNECT = 1001;
    private static final int MSG_RE_CONNECT = 1002;
    private static final int MSG_DISCOVER_SERVICE = 1003;
    private static final int MSG_SET_MTU = 1004;
    private static final int MSG_CLOSE = 1005;

    private static class GattHandler extends Handler {
        private WeakReference<GattOperationWrapper> reference;

        public GattHandler(Looper looper, GattOperationWrapper operation) {
            super(looper);
            this.reference = new WeakReference<>(operation);
        }

        @Override
        public void handleMessage(Message msg) {
            GattOperationWrapper gattOperation = reference.get();
            if (gattOperation == null) {
                return;
            }
            switch (msg.what) {
                case MSG_FIRST_CONNECT:
                    gattOperation.operation.connect(false);
                    break;
                case MSG_RE_CONNECT:
                    gattOperation.operation.reconnect();
                    break;
                case MSG_DISCOVER_SERVICE:
                    gattOperation.operation.discoverService();
                    break;
                case MSG_SET_MTU:
                    gattOperation.setMtu();
                    break;
                case MSG_CLOSE:
                    gattOperation.operation.disConnect();
                    gattOperation.operation.close();
                    break;
                default:
                    break;
            }
        }
    }
}
