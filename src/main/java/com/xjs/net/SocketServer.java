package com.xjs.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by xiejisheng on 18/4/20.
 */
public class SocketServer {

    public static void receive() {
        try {
            if (InnetUtils.isSelectedMaster()) {
                System.exit(0);
            }

            AddressModel currAddress = InnetUtils.getCurrAddress();
            ServerSocket server = new ServerSocket(currAddress.getPort());
            if (!Thread.interrupted()) {
                while (true) {
                    try {
                        Socket accept = server.accept();
//                    System.out.println("xxxxxx");
//                    SocketClient.sendMsg(accept, "HTTP/1.1 200 OK\n\n\n <html>nihao</html>");
                        new ServerHandler(accept).run();
                    } catch (Throwable t) {
                        System.out.println(t);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
