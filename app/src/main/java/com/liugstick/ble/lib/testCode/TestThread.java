package com.liugstick.ble.lib.testCode;

import android.util.Log;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class TestThread extends Thread {
    public static final String TAG = "liugstest";
    private ReentrantLock lock = new ReentrantLock();
    private Condition writeCondition = lock.newCondition();
    private volatile boolean isStop;
    private volatile boolean isPause;

    public TestThread() {
    }

    @Override
    public void run() {
        Log.i(TAG, "开始任务");
        while (!isStop) {

            try {
                lock.lock();
                while (isPause) {
                    writeCondition.await();
                }

                if (!isStop) {
                    Log.i(TAG, "执行任务");
                    writeCondition.await();
                }

            } catch (InterruptedException e) {
                Log.i(TAG, "写线程终止");
            } finally {
                lock.unlock();
            }
        }
        Log.i(TAG, "任务结束");
    }

    void pauseState() {
        if (isPause){
            return;
        }
        this.isPause = true;
        lock.lock();
        try {
            Log.i(TAG, "pause任务");
            writeCondition.signal();
        } finally {
            lock.unlock();
        }
    }

    void resumeState(){
        if (!this.isPause){
            return;
        }
        this.isPause = false;
        lock.lock();
        try {
            Log.i(TAG, "resume任务");
            writeCondition.signal();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 继续任务
     */
    void notifyNext() {
        lock.lock();
        try {
            Log.i(TAG, "下一个任务");
            writeCondition.signal();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 停止任务
     */
    public void stopWriteTask() {
        Log.i(TAG, "停止任务");
        this.isStop = true;
        notifyNext();
    }
}
