package com.xjs;

import com.xjs.net.SocketServer;

import java.util.concurrent.TimeUnit;

/**
 * Created by xiejisheng on 18/4/18.
 */
public class Selectmaster {

    public static void main(String[] args) {
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
        new Thread(new SocketServer()).run();
    }
}
