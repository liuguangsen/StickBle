package com.liugs.stble.gatt;

import java.util.concurrent.LinkedBlockingDeque;

/**
 * 根据链路状态区分操作，以及提供start，stop方法
 */
public class GattWriteTask extends BaseWriteTask implements onGattWriteCallback {
    private static final String TAG = "GattWriteTask";
    private LinkedBlockingDeque<byte[]> deque = new LinkedBlockingDeque<>();
    private GattOperationWrapper gattOperation;
    private byte[] currentValue;


    public GattWriteTask(GattOperationWrapper gattOperation) {
        super();
        this.gattOperation = gattOperation;
    }

    @Override
    public void addWriteData(byte[] src) {
        deque.add(src);
    }

    @Override
    public void doBackgroundTask() {
        currentValue = deque.poll();
        gattOperation.write(currentValue);
    }

    @Override
    public void onWriteResult(boolean isSuccess) {
        if (!isSuccess) {
            deque.addFirst(currentValue);
        }
        notifyNext();
    }

    @Override
    public void pauseState() {
        super.pauseState();
    }

    @Override
    public void resumeState() {
        super.resumeState();
    }

    @Override
    public void setState(int state) {
        if (state == GattOperation.STATE_CONNECT_SUCCESS) {
            resumeState();
        } else if (state == GattOperation.STATE_CONNECT_FAILED) {
            pauseState();
        }
    }

    @Override
    public void stopWriteTask() {
        super.stopWriteTask();
        deque.clear();
        currentValue = null;
    }
}
