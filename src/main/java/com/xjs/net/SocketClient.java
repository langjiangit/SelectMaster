package com.xjs.net;

import com.google.common.base.Joiner;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.Time;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by xiejisheng on 18/4/20.
 */
public class SocketClient {

    public static void sendMsg(){
        String msg = Joiner.on(":").join(InnetUtils.getCurrAddress(), InnetUtils.getCurrAddress(), LogiClock.getLogiClock());
        doSendMsg(msg);
    }

    private static void doSendMsg(String msg) {
        List<AddressModel> otherAddress = FileUtils.getOtherAddress();
        for (AddressModel addressModel : otherAddress) {
            Socket socket = null;
            try {
                socket = new Socket();
                InetSocketAddress inetSocketAddress = new InetSocketAddress(addressModel.getIp(), addressModel.getPort());
                while (!socket.isConnected()) {
                    socket = new Socket();
                    try {
                        TimeUnit.SECONDS.sleep(1);
                        socket.connect(inetSocketAddress);
                    }catch (Throwable t) {
                        System.out.println(SocketClient.class.getCanonicalName());
                        t.printStackTrace();
                    }
                    System.out.println(Joiner.on("").join(InnetUtils.getCurrAddress(), "尝试与", addressModel, "建立连接"));
                }

                OutputStream os = socket.getOutputStream();
                os.write(Joiner.on("").join(msg, "\n").getBytes());
                os.flush();
            } catch (Throwable t) {
                System.out.println(SocketClient.class.getCanonicalName());
                t.printStackTrace();
            }
        }
    }

    public static void sendMsg(String msg){
        doSendMsg(msg);
    }

    public static void sendMsg(Socket socket, String msg) {
        try {
            OutputStream os = socket.getOutputStream();
            os.write(Joiner.on("").join(msg, "\n").getBytes());
            os.flush();
            os.close();
        } catch (Throwable t) {
            System.out.println(SocketClient.class.getCanonicalName());
            t.printStackTrace();
        }
    }

    public static void register() {
        Socket socket = new Socket();

    }
}
