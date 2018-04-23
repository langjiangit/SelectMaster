package com.xjs.net;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.primitives.Longs;

import java.io.*;
import java.net.Socket;
import java.util.List;

/**
 * Created by xiejisheng on 18/4/20.
 */
public class ServerHandler implements Runnable {

    /**
     * 假设机器CPU的频率是a == 4GHZ=4*10^9
     * 光速c == 3*10^8 光纤传输速度b == 2/3*c=2*10^8
     * 局域网内，理论上机器X在w端口发送的消息到达机器Y的z端口的时间无限趋近于0
     * a/b≈2*10,这个数字再放大，考虑机器X生成消息到机器Y解析消息
     * 机器X差不多可以进行200个时钟周期，当然具体的值可以经过反复测量
     */
    final long distanceTimeExpend = 200L;
    private final Socket socket;

    public ServerHandler(Socket accept) {
        socket = accept;
    }

    public void run() {
        try {
            InputStream is = socket.getInputStream();
            BufferedReader bufferedReader = 
                    new BufferedReader(new InputStreamReader(is));
            process(bufferedReader.readLine(), socket);
        } catch (Throwable t) {
            System.out.println(ServerHandler.class.getCanonicalName());
            t.printStackTrace();
        }
    }

    private void process(String msgLine, Socket socket) {
        List<String> tuple = Splitter.on(":").splitToList(msgLine);
        if (tuple.size() < 3) {
            SocketClient.sendMsg(socket, "HTTP/1.1 200 OK\n\n\n <html>nihao</html>");
        }
        SelectMasterModel receiveMsg
                = SelectMasterModel.create(tuple.get(0), tuple.get(1), Long.parseLong(tuple.get(2)));

        System.out.println(Joiner.on(" ").join(InnetUtils.getCurrAddress(),"reveive", receiveMsg));

//        if (Math.abs(receiveMsg.getTimeClock() - LogiClock.getLogiClock()) <= distanceTimeExpend
//                && receiveMsg.getTimeClock() >= LogiClock.getLogiClock()) {
        if (Longs.compare(receiveMsg.getTimeClock()+3000, LogiClock.getLogiClock()) > 0) {
            LogiClock.setLogiClock(receiveMsg.getTimeClock());
            String sender
                    = Joiner.on(",").join(InnetUtils.getCurrAddress().getIp(), InnetUtils.getCurrAddress().getPort());
            String master = receiveMsg.getMaster();
            String msg
                    = Joiner.on(":").join(sender, master, LogiClock.getLogiClock());
            System.out.println(Joiner.on(" ").join(InnetUtils.getCurrAddress(),"send", msg));
//            SocketClient.sendMsg(socket, msg);
            SocketClient.sendMsg(msg);
            QuorumVote.put(receiveMsg);
        } else {
            String sender = Joiner.on(",").join(InnetUtils.getCurrAddress().getIp(), InnetUtils.getCurrAddress().getPort());
            String msg
                    = Joiner.on(":").join(sender, sender, LogiClock.getLogiClock());
            System.out.println(Joiner.on(" ").join(InnetUtils.getCurrAddress(),"send", msg));
            SocketClient.sendMsg(msg);
            QuorumVote.put(SelectMasterModel.create(sender, sender, LogiClock.getLogiClock()));
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
