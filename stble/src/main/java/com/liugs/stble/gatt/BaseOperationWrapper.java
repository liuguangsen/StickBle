package com.liugs.stble.gatt;

import com.liugs.stble.gatt.callback.UiGattCallback;

public interface BaseOperationWrapper {

    String getMac();

    void setConfig(GattConfig config);

    void createGattChannel();

    void closeGattChannel();

   void setUiGattCallback(UiGattCallback callback);
}
