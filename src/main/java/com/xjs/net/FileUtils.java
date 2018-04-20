package com.xjs.net;

import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * Created by xiejisheng on 18/4/20.
 */
public class FileUtils {

    private static final String PROPERTIES_FILE = "select_master.prop";
    private static final Map<String, AddressModel> machines = Maps.newHashMap();
    private static final String CURR = "curr";
    private static final String OTHER_ONE = "other1";
    private static final String OTHER_TWO = "other2";
    static {
        File file = new File(PROPERTIES_FILE);
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
}
