package com.xjs.net;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author xjs
 * 名称服务
 */
public class NameServiceing implements Runnable {

    private final List<AddressModel> machines = Lists.newCopyOnWriteArrayList();
    ExecutorService es = Executors.newFixedThreadPool(1);

    public void run() {
        register();
    }

    private void register() {
        AddressModel broadCast = FileUtils.getBroadCast();
        if (null == broadCast) {
            throw new IllegalStateException("请设置服务注册地址，broadCast:ip:port");
        }
        doMonitor(broadCast);
    }

    private void doMonitor(AddressModel broadCast) {
        try {
            ServerSocket server = new ServerSocket(broadCast.getPort());
            while (true) {
                final Socket accept = server.accept();
                es.submit(new Runnable() {
                    public void run() {
                        process(accept);
                    }
                });
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }

    }

    private void process(Socket accept) {
        try {
            BufferedReader bufferedReader
                    = new BufferedReader(new InputStreamReader(accept.getInputStream()));
            String line = bufferedReader.readLine();
            List<String> ipPort = Splitter.on(":").splitToList(line);
            machines.add(AddressModel.create(ipPort.get(0), Integer.parseInt(ipPort.get(1))));
            FileUtils.put();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
