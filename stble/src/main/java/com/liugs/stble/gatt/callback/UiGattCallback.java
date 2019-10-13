package com.liugs.stble.gatt.callback;

public interface UiGattCallback extends BaseUiCallback {
    void onCreateChannelResult(boolean isSuccess);

    void onMessage(String msg);

    void onError(String error);
}
