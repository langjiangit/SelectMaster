package com.xjs.net;

import com.google.common.base.Objects;
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
    public boolean equals(Object obj) {
        if (!(obj instanceof AddressModel)) {
            return false;
        }

        AddressModel other = (AddressModel) obj;
        return ip.equals(other.ip) && port == other.port;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = result*31 + ip.hashCode();
        result = result*31 + port;
        return result;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
    }
}
