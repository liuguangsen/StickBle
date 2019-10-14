package com.liugs.stble.gatt;

import java.util.LinkedHashMap;

public class GattChannelMap<K, V>  extends LinkedHashMap<K, V> {

    @Override
    public V put(K key, V value) {
        return super.put(key, value);
    }

    @Override
    protected boolean removeEldestEntry(Entry<K, V> eldest) {
        V value = eldest.getValue();
        if (value instanceof GattOperationWrapper){
            GattOperationWrapper wrapper = (GattOperationWrapper) value;
            wrapper.closeGattChannel();
        }
        return super.removeEldestEntry(eldest);
    }
}
