package com.xjs.net;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by xiejisheng on 18/4/20.
 * 逻辑时钟
 * 同步各个CPU的逻辑时钟，处理分布式机器各CPU漂移的问题
 * 收敛
 */
public class LogiClock {

    public final static AtomicLong logiClock = new AtomicLong(System.currentTimeMillis());
    public static void run() {
        while (true) {
            try {
                // 处理cpu打满的问题
                TimeUnit.MILLISECONDS.sleep(1);
            } catch (Throwable t) {
                System.out.println(LogiClock.class.getCanonicalName());
                t.printStackTrace();
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
