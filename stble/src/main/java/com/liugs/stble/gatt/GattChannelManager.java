package com.liugs.stble.gatt;

import com.liugs.stble.BleManager;
import com.liugs.stble.gatt.callback.BaseUiCallback;
import com.liugs.stble.gatt.callback.UiGattCallback;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class GattChannelManager {
    private GattChannelManager() {
    }

    private static class Holder {
        private static final GattChannelManager INSTANCE = new GattChannelManager();
    }

    public static GattChannelManager getInstance() {
        return Holder.INSTANCE;
    }

    private GattChannelMap<String, GattOperationWrapper> deviceList = new GattChannelMap<>();
    private LinkedHashMap<String, BaseUiCallback>uiCallback = new LinkedHashMap<>();

    public void startGatt(GattConfig config, UiGattCallback callback) {
        String mac = config.getMac();
        GattOperationWrapper wrapper = GattChannelFactory.createWraper(BleManager.getInstance().getAppContext(), mac);
        wrapper.setUiGattCallback(callback);
        wrapper.setConfig(config);
        deviceList.put(mac, wrapper);
        uiCallback.put(mac,callback);
        wrapper.createGattChannel();
    }

    public void closeGatt(String mac) {
        deviceList.remove(mac);
        uiCallback.remove(mac);
    }

    public void clearGatt() {
        Iterator<Map.Entry<String, GattOperationWrapper>> iterator = deviceList.entrySet().iterator();
        while (iterator.hasNext()) {
            iterator.remove();
        }
        Iterator<Map.Entry<String, BaseUiCallback>> UiIterator = uiCallback.entrySet().iterator();
        while (iterator.hasNext()) {
            UiIterator.remove();
        }
    }
}
