package com.xjs.net;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * Created by xiejisheng on 18/4/20.
 */
public class FileUtils {

    public static final String MASTER = "master";
    private static final String PROPERTIES_FILE = "select_master.prop";
    private static final Map<String, AddressModel> machines = Maps.newHashMap();
    private static final String CURR = "curr";
    private static final String OTHER_ONE = "other1";
    private static final String OTHER_TWO = "other2";
    private static final String SELECTED_MASTER = "selectedMaster";

    static {
//        System.out.println(System.getProperty("java.class.path"));
        URL resourceAsStream = FileUtils.class.getClassLoader().getResource(PROPERTIES_FILE);
        File file = null;
        try {
            file = new File(resourceAsStream.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        try {
            List<String> props = Files.readLines(file, Charset.forName("utf8"));
            for (String each : props) {
                List<String> prop = Splitter.on(":").splitToList(each);
                String key = prop.get(0);
                AddressModel addressModel
                        = AddressModel.create(prop.get(1), Integer.parseInt(prop.get(2)));
                machines.put(key, addressModel);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static AddressModel getCurrAddress() {
        return machines.get(CURR);
    }

    public static List<AddressModel> getOtherAddress() {
        return ImmutableList.of(machines.get(OTHER_ONE), machines.get(OTHER_TWO));
    }

    public static AddressModel getMaster() {
        AddressModel addressModel = machines.get(SELECTED_MASTER);
        return addressModel;
    }

    public static void write(String master) {
        URL resourceAsStream = FileUtils.class.getClassLoader().getResource(PROPERTIES_FILE);
        File file = null;
        try {
            file = new File(resourceAsStream.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        try {
            Files.append(Joiner.on("").join(MASTER, ":", master), file, Charset.forName("utf8"));
        } catch (Throwable t) {
            System.out.println(t);
        }
    }
}
