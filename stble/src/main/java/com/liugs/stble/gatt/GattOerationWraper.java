package com.liugs.stble.gatt;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * gatt链路
 */
public class GattOerationWraper extends BaseOperationWraper implements OnGattOperationCallback {
    // gatt操作
    private GattOperation operation;
    // gatt连路管理
    private GattHandler handler;
    // gat写入
    private GattWriteTask gattWriteTask;

    private GattConfig config;

    public GattOerationWraper(Context context, BluetoothDevice device) {
        operation = new GattOperation(context, device);
        operation.setGattOperationCallback(this);
        handler = new GattHandler(this);
    }

    public GattOerationWraper(Context context, String mac) {
        operation = new GattOperation(context, mac);
        operation.setGattOperationCallback(this);
    }

    public void CreateGattChannel() {
        operation.connect(true);
    }

    public void closeGattChannel() {
        operation.disConnect();
        operation.close();
    }

    public void discoverService() {
        operation.discoverService();
    }

    public void setMtu() {
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
            case GattOperation.STATE_FIRST_CONNECT_FAILED:
                // 建立链路失败了哦
                break;
        }
    }

    private static class GattHandler extends Handler {
        private WeakReference<GattOerationWraper> reference;

        public GattHandler(GattOerationWraper operation) {
            this.reference = new WeakReference<>(operation);
        }

        @Override
        public void handleMessage(Message msg) {
            GattOerationWraper gattOperation = reference.get();
            if (gattOperation == null) {
                return;
            }

        }
    }


}
