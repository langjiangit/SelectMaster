package com.xjs.net;

import com.google.common.base.Joiner;

import java.io.IOException;
import java.net.InetAddress;
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
                System.out.println(Joiner.on("").join("master节点已选出：", InnetUtils.getMaster()));
                System.exit(0);
            }

            AddressModel currAddress = InnetUtils.getCurrAddress();
            ServerSocket server = new ServerSocket();
            InetAddress inetAddress = server.getInetAddress();
            System.out.println(Joiner.on(" ").join(inetAddress.getHostAddress(), server.getLocalPort()));
            new Thread(SocketClient.register()).start();
            if (!Thread.interrupted()) {
                while (true) {
                    try {
                        Socket accept = server.accept();
                        if (QuorumVote.isSuccessVote()) {
                            FileUtils.write(QuorumVote.getMaster().getMaster());
                            System.out.println(Joiner.on("").join("master节点已选出：", QuorumVote.getMaster().getMaster()));
                            System.exit(0);
                        }
                        new ServerHandler(accept).run();
                    } catch (Throwable t) {
                        System.out.println(SocketServer.class.getCanonicalName());
                        t.printStackTrace();
                    }
                }
            }
        } catch (Throwable t) {
            System.out.println(SocketServer.class.getCanonicalName());
            t.printStackTrace();
        }
    }
}
