package com.liugs.stble.gatt;

import android.util.Log;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 根据链路状态区分操作，以及提供start，stop方法
 */
public class GattWriteTask implements onGattWriteCallback, Runnable {
    private static final String TAG = "GattWriteTask";
    private ReentrantLock lock;
    private Condition condition;
    private LinkedBlockingDeque<byte[]> deque = new LinkedBlockingDeque<>();
    private GattOperationWrapper gattOperation;
    private byte[] currentValue;
    private volatile boolean isStop;

    public GattWriteTask(GattOperationWrapper gattOperation) {
        this.gattOperation = gattOperation;
    }

    public GattWriteTask() {
        lock = new ReentrantLock();
        condition = lock.newCondition();
    }

    @Override
    public void onWriteResult(boolean isSuccess) {
        if (!isSuccess) {
            deque.addFirst(currentValue);
        }
        goWhile();
    }

    private void goWhile() {
        lock.lock();
        try {
            condition.signal();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void run() {
        while (isStop) {
            try {
                lock.lock();
                currentValue = deque.poll();
                gattOperation.write(currentValue);
                condition.wait();
            } catch (InterruptedException e) {
                Log.i(TAG, "写线程终止");
            } finally {
                lock.unlock();
            }
        }
    }

    public void addWriteData(byte[] src) {
        deque.add(src);
    }

    public void stopWrite() {
        this.isStop = true;
        goWhile();
    }
}
