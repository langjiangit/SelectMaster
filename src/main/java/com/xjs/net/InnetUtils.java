package com.xjs.net;

/**
 * Created by xiejisheng on 18/4/20.
 */
public class InnetUtils {

    public static AddressModel getCurrAddress() {
        return FileUtils.getCurrAddress();
    }

    public static boolean isSelectedMaster() {
        AddressModel master = FileUtils.getMaster();
        return null != master;
    }

    public static AddressModel getMaster() {
        AddressModel master = FileUtils.getMaster();
        return master;
    }
}
