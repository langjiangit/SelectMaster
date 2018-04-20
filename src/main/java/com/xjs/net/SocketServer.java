package com.xjs.net;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by xiejisheng on 18/4/20.
 */
public class SocketServer implements Runnable {

    public void run() {
        try {
            ServerSocket server = new ServerSocket(8121);
            if (!Thread.interrupted()) {
                while (true) {
                    new Thread(new ServerHandler(server.accept())).run();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
