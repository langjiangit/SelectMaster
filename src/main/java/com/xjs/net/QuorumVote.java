package com.xjs.net;

import com.google.common.collect.Maps;
import com.google.common.primitives.Longs;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @author xjs
 * 选举投票
 */
public class QuorumVote {

    private static final Map<String, SelectMasterModel> successVoteMsgs = Maps.newConcurrentMap();

    public static boolean isSuccessVote() {
        String preVoteMaster = StringUtils.EMPTY;
        for (String aMaster : successVoteMsgs.keySet()) {
            if (!preVoteMaster.equals(aMaster)) {
                return false;
            }
        }
        return true;
    }

    public static void put(SelectMasterModel masterModel) {
        String newVote = masterModel.getMaster();
        if (successVoteMsgs.containsKey(newVote)) {
            SelectMasterModel exitVote = successVoteMsgs.get(newVote);
            if (Longs.compare(masterModel.getTimeClock(), exitVote.getTimeClock()) > 0) {
                successVoteMsgs.put(masterModel.getMaster(), masterModel);
            }
        }
    }

    public static SelectMasterModel getMaster() {
        if (isSuccessVote()) {
            return successVoteMsgs.values().iterator().next();
        }
        return null;
    }
}
