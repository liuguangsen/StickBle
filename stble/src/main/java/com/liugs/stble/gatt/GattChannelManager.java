package com.liugs.stble.gatt;

import com.liugs.stble.BleManager;
import com.liugs.stble.gatt.callback.BaseUiCallback;
import com.liugs.stble.gatt.callback.UiGattCallback;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 管理多个gatt通道的操作以及操作结果回调
 * 1.connect 2.write 3.notify
 */
public class GattChannelManager {
    private GattChannelManager() {
    }

    private static class Holder {
        private static final GattChannelManager INSTANCE = new GattChannelManager();
    }

    public static GattChannelManager getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * mac地址对应的gatt通道管理者列表
     */
    private GattChannelMap<String, GattController> deviceList = new GattChannelMap<>();
    /**
     * mac地址对应的gatt操作callback
     */
    private LinkedHashMap<String, BaseUiCallback> uiCallback = new LinkedHashMap<>();

    /**
     * 链路链接
     *
     * @param config
     * @param callback
     */
    public void startGatt(GattConfig config, UiGattCallback callback) {
        String mac = config.getMac();
        GattController wrapper = deviceList.get(mac);
        if (wrapper != null) {
            return;
        }
        wrapper = GattChannelFactory.createWrapper(BleManager.getInstance().getAppContext(), mac);
        wrapper.setUiGattCallback(callback);
        wrapper.setConfig(config);
        deviceList.put(mac, wrapper);
        uiCallback.put(mac, callback);
        wrapper.createGattChannel();
    }

    /**
     * 关闭链路链接
     *
     * @param mac
     */
    public void closeGatt(String mac) {
        GattController wrapper = deviceList.get(mac);
        if (wrapper != null) {
            wrapper.closeGattChannel();
        }
        deviceList.remove(mac);
        uiCallback.remove(mac);
    }

    public void clearGatt() {
        Iterator<Map.Entry<String, GattController>> iterator = deviceList.entrySet().iterator();
        while (iterator.hasNext()) {
            iterator.remove();
        }
        Iterator<Map.Entry<String, BaseUiCallback>> UiIterator = uiCallback.entrySet().iterator();
        while (iterator.hasNext()) {
            UiIterator.remove();
        }
    }
}
