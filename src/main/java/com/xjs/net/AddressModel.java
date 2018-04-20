package com.xjs.net;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by xiejisheng on 18/4/20.
 */
public class AddressModel {
    private final String ip;
    private final int port;

    private AddressModel(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public static AddressModel create(String ip, int port) {
        return new AddressModel(ip, port);
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
    }
}
