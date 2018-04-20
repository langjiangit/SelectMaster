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
            socket.setKeepAlive(true);
            InputStream is = socket.getInputStream();
            BufferedReader bufferedReader = 
                    new BufferedReader(new InputStreamReader(is));
            process(bufferedReader.readLine(), socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void process(String msgLine, Socket socket) {
        List<String> tuple = Splitter.on(",").splitToList(msgLine);
        SelectMasterModel selectMasterModel
                = SelectMasterModel.create(tuple.get(0), tuple.get(1), Long.parseLong(tuple.get(2)));
        System.out.println(selectMasterModel);
        if (Math.abs(selectMasterModel.getTimeClock() - LogiClock.getLogiClock()) <= distanceTimeExpend ) {
            if (selectMasterModel.getTimeClock() >= LogiClock.getLogiClock()) {
                LogiClock.setLogiClock(selectMasterModel.getTimeClock());
                try {
                    OutputStream os = socket.getOutputStream();
                    String sender = Joiner.on(":").join(InnetUtils.getCurrAddress().getIp(), InnetUtils.getCurrAddress().getPort());
                    String master = selectMasterModel.getMaster();
                    String msg
                            = Joiner.on(",").join(sender, master, LogiClock.getLogiClock());
                    os.write(Joiner.on("").join(msg, "\n").getBytes());
                    os.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    OutputStream os = socket.getOutputStream();
                    String sender = Joiner.on(":").join(InnetUtils.getCurrAddress().getIp(), InnetUtils.getCurrAddress().getPort());
                    String msg
                            = Joiner.on(",").join(sender, sender, LogiClock.getLogiClock());
                    os.write(Joiner.on("").join(msg, "\n").getBytes());
                    os.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
