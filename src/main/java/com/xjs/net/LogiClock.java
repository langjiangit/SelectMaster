package com.xjs.net;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by xiejisheng on 18/4/20.
 */
public class LogiClock implements Runnable{

    public final static AtomicLong logiClock = new AtomicLong(System.currentTimeMillis());
    public void run() {
        while (true) {
            try {
                // 处理cpu打满的问题
                TimeUnit.MILLISECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            logiClock.incrementAndGet();
        }
    }

    public static long getLogiClock() {
        return logiClock.get();
    }

    public static void setLogiClock(long newer) {
        logiClock.set(newer);
    }
}
