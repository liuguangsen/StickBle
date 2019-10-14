package com.liugs.stble.gatt;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.liugs.stble.gatt.callback.UiGattCallback;

import java.lang.ref.WeakReference;

/**
 * gatt链路,辅助gatt链路的核心类
 * 1.connect,discoverService(可选),setMtu(可选)
 */
public class GattOperationWrapper extends BaseOperationWraper implements OnGattOperationCallback {
    // gatt操作
    private GattOperation operation;
    // gatt连路管理
    private GattHandler handler;
    // gat写入
    private GattWriteTask gattWriteTask;

    private GattConfig config;

    private UiGattCallback uiGattCallback;

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

    public void setConfig(GattConfig config) {
        this.config = config;
    }

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

    public void closeGattChannel() {
        operation.disConnect();
        operation.close();
        operation.clearData();
    }

    private void setMtu() {
        operation.setMtu(config.getMtu());
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
                break;
            default:
                break;
        }
    }

    private void handleSetMtu(int state) {
        switch (state) {
            case GattOperation.STATE_SET_MTU_FAILED:
                break;
            case GattOperation.STATE_SET_MTU_SUCCESS:
                callCreateChannel();
                break;
            case GattOperation.STATE_SET_MUTING:
                break;
            default:
                break;
        }
    }

    private void handleDiscoverService(int state) {
        switch (state) {
            case GattOperation.STATE_DISCOVER_SERVICE_FAILED:
                break;
            case GattOperation.STATE_DISCOVER_SERVICE_SUCCESS:
                if (config.isSetMtu()) {
                    handler.sendEmptyMessageDelayed(MSG_SET_MTU, config.getSetMtuDelayTime());
                } else {
                    callCreateChannel();
                }
                break;
            case GattOperation.STATE_DISCOVER_SERVICING:
                break;
            default:
                break;
        }
    }

    @Override
    protected void handleConnectType(int state) {
        switch (state) {
            case GattOperation.STATE_FIRST_CONNECT_START_SUCCESS:
                // 第一次建立连接正常，等待建立成功
                break;
            case GattOperation.STATE_FIRST_CONNECT_START_FAILED:
                // 第一次建立连接异常
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
