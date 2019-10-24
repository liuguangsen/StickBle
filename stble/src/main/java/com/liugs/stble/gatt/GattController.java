package com.liugs.stble.gatt;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.liugs.stble.gatt.callback.ControllerCallback;
import com.liugs.stble.gatt.callback.UiGattCallback;

/**
 * 增加一个gatt多次销毁重建的优化，目的，还是确保链路建立成功
 * 这个待看完源码，确定优化点
 */
public class GattController implements ControllerCallback, BaseOperationWrapper {
    private GattOperationWrapper wrapper;

    public GattController(Context context, String mac) {
        wrapper = new GattOperationWrapper(context, mac);
        wrapper.setControllerCallback(this);
    }

    public GattController(Context context, BluetoothDevice device) {
        wrapper = new GattOperationWrapper(context, device);
        wrapper.setControllerCallback(this);
    }

    @Override
    public void onState(int state) {

    }

    @Override
    public String getMac() {
        return wrapper.getMac();
    }

    @Override
    public void setConfig(GattConfig config) {
        wrapper.setConfig(config);
    }

    @Override
    public void createGattChannel() {
        wrapper.createGattChannel();
    }

    @Override
    public void closeGattChannel() {
        wrapper.closeGattChannel();
    }

    @Override
    public void setUiGattCallback(UiGattCallback callback) {
        wrapper.setUiGattCallback(callback);
    }

    @Override
    public void createWriteTask() {
        wrapper.createWriteTask();
    }

    @Override
    public void write(byte[] src) {
        wrapper.write(src);
    }

    @Override
    public void writeBackground(byte[] src) {
        wrapper.writeBackground(src);
    }

    @Override
    public void stopWrite() {
        wrapper.stopWrite();
    }

    public GattOperationWrapper getWrapper() {
        return wrapper;
    }
}
