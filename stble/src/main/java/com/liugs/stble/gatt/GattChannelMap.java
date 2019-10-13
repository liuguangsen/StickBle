package com.liugs.stble.gatt;

import java.util.LinkedHashMap;

public class GattChannelMap<K, V>  extends LinkedHashMap<K, V> {

    @Override
    public V put(K key, V value) {
        return super.put(key, value);
    }

    @Override
    public boolean remove(Object key, Object value) {
        if (value instanceof GattOperationWrapper){
            GattOperationWrapper wrapper = (GattOperationWrapper) value;
            wrapper.closeGattChannel();
        }
        return super.remove(key, value);
    }
}
