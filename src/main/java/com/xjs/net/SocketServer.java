package com.xjs.net;

import com.google.common.base.Joiner;

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
                System.out.println(Joiner.on("").join("master节点已选出：", InnetUtils.getMaster()));
                System.exit(0);
            }

            AddressModel currAddress = InnetUtils.getCurrAddress();
            ServerSocket server = new ServerSocket(currAddress.getPort());
            if (!Thread.interrupted()) {
                while (true) {
                    try {
                        Socket accept = server.accept();
                        if (QuorumVote.isSuccessVote()) {
                            FileUtils.write(QuorumVote.getMaster().getMaster());
                        }
                        new ServerHandler(accept).run();
                    } catch (Throwable t) {
                        System.out.println(t);
                    }
                }
            }
        } catch (Throwable t) {
            System.out.println(t);
        }
    }
}
