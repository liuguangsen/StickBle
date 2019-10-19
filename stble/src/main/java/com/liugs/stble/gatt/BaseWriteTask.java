package com.liugs.stble.gatt;

import android.util.Log;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static android.content.ContentValues.TAG;

public abstract class BaseWriteTask extends Thread {

    private ReentrantLock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    private volatile boolean isStoped;
    private volatile boolean isPaused;

    @Override
    public void run() {
        while (!isStoped) {
            try {
                lock.lock();
                while (isPaused) {
                    condition.await();
                }
                if (!isPaused && !isStoped) {
                    doBackgroundTask();
                    condition.await();
                }
            } catch (InterruptedException e) {
                Log.i(TAG, "写线程终止");
            } finally {
                lock.unlock();
            }
        }
    }

    /**
     * 写数据
     *
     * @param src 数据
     */
    public abstract void addWriteData(byte[] src);

    /**
     * 执行任务
     */
    public abstract void doBackgroundTask();

    public abstract void setState(@GattOperation.State int state);


    public void pauseState() {
        if (this.isPaused) {
            return;
        }
        this.isPaused = true;
        notifyNext();
    }

    public void resumeState() {
        if (!this.isPaused) {
            return;
        }
        this.isPaused = false;
        notifyNext();
    }

    /**
     * 继续任务
     */
    public void notifyNext() {
        lock.lock();
        try {
            condition.signal();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 停止任务
     */
    public void stopWriteTask() {
        this.isStoped = true;
        notifyNext();
    }
}
