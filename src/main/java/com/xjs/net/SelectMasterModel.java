package com.xjs.net;

import com.google.common.base.Objects;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by xiejisheng on 18/4/20.
 * 选主消息体
 */
public class SelectMasterModel {

    private String sender;
    private String master;
    private long timeClock;

    private SelectMasterModel(String sender, String master, long timeClock) {
        this.sender = sender;
        this.master = master;
        this.timeClock = timeClock;
    }

    public static SelectMasterModel create(String sender, String master, long timeClock) {
        return new SelectMasterModel(sender, master, timeClock);
    }

    public String getSender() {
        return sender;
    }

    public String getMaster() {
        return master;
    }

    public long getTimeClock() {
        return timeClock;
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = result*31 + sender.hashCode();
        result = result*31 + master.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SelectMasterModel)) {
            return false;
        }
        SelectMasterModel other = (SelectMasterModel) obj;
        return this.master.equals(other.master)
                && this.sender.equals(other.sender);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
    }
}
