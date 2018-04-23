package com.xjs.net;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.io.BufferedReader;
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

    private final List<AddressModel> registerMachines = Lists.newCopyOnWriteArrayList();
    ExecutorService es = Executors.newFixedThreadPool(1);

    public void run() {
        register();
    }

    private void register() {
        AddressModel broadCast = FileUtils.getBroadCast();
        if (null == broadCast) {
            throw new IllegalStateException("请设置服务注册地址，broadCast:{ip}:{port}");
        }
        doRegister(broadCast);
    }

    private void doRegister(AddressModel broadCast) {
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
            String hostAddress = accept.getInetAddress().getHostAddress();
            int port = accept.getPort();
            registerMachines.add(AddressModel.create(hostAddress, port));
            accept.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public List<AddressModel> getRegisterMachines() {
        return ImmutableList.copyOf(registerMachines);
    }
}
