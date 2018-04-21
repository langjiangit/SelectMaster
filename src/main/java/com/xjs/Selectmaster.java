package com.xjs;

import com.xjs.net.SocketClient;
import com.xjs.net.SocketServer;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by xiejisheng on 18/4/18.
 */
public class Selectmaster {


    public static void main(String[] args) throws InterruptedException {
        ExecutorService server = Executors.newFixedThreadPool(1);
        ExecutorService client = Executors.newFixedThreadPool(1);
//        long logiClock = System.currentTimeMillis();
//        while(true) {
//            try {
//                TimeUnit.MILLISECONDS.sleep(1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            ++logiClock;
//            System.out.println(logiClock);
//        }
        final CountDownLatch latch = new CountDownLatch(1);
        server.submit(new Runnable() {
            public void run() {
                SocketServer.receive();
                latch.countDown();
            }
        });
        new Thread(new Runnable() {
            public void run() {
                SocketClient.sendMsg();
            }
        }).run();

        latch.await();
    }
}
