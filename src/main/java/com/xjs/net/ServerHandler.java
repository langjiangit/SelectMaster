package com.xjs.net;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

import java.io.*;
import java.net.Socket;
import java.util.List;

/**
 * Created by xiejisheng on 18/4/20.
 */
public class ServerHandler implements Runnable {

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
        } catch (IOException e) {
            e.printStackTrace();
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

        if (Math.abs(receiveMsg.getTimeClock() - LogiClock.getLogiClock()) <= distanceTimeExpend
                && receiveMsg.getTimeClock() >= LogiClock.getLogiClock()) {
            LogiClock.setLogiClock(receiveMsg.getTimeClock());
            String sender
                    = Joiner.on(",").join(InnetUtils.getCurrAddress().getIp(), InnetUtils.getCurrAddress().getPort());
            String master = receiveMsg.getMaster();
            String msg
                    = Joiner.on(":").join(sender, master, LogiClock.getLogiClock());
            System.out.println(Joiner.on(" ").join(InnetUtils.getCurrAddress(),"send", msg));
            SocketClient.sendMsg(socket, msg);
            QuorumVote.put(receiveMsg);
        } else {
            String sender = Joiner.on(",").join(InnetUtils.getCurrAddress().getIp(), InnetUtils.getCurrAddress().getPort());
            String msg
                    = Joiner.on(":").join(sender, sender, LogiClock.getLogiClock());
            System.out.println(Joiner.on(" ").join(InnetUtils.getCurrAddress(),"send", msg));
            SocketClient.sendMsg(socket, msg);
            QuorumVote.put(SelectMasterModel.create(sender, sender, LogiClock.getLogiClock()));
        }
    }
}
