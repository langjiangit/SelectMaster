package com.xjs.net;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.primitives.Longs;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @author xjs
 * 选举投票
 */
public class QuorumVote {

    private static final List<String> successVoteMaster = Lists.newCopyOnWriteArrayList();
    private static final Map<String, SelectMasterModel> successVoteMsgs = Maps.newConcurrentMap();

    public static boolean isSuccessVote() {
        if (successVoteMaster.isEmpty())
            return false;
        if (successVoteMaster.size() != FileUtils.getMachinesNum())
            return false;
        String prevMaster = successVoteMaster.get(0);
        for (int i = 1; i < successVoteMaster.size(); ++i) {
            String forwardMaster = successVoteMaster.get(i);
            if (!prevMaster.equals(forwardMaster)) {
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
                successVoteMaster.add(newVote);
            }
        } else {
            successVoteMsgs.put(masterModel.getMaster(), masterModel);
            successVoteMaster.add(newVote);
        }
    }

    public static SelectMasterModel getMaster() {
        if (!successVoteMsgs.isEmpty() && isSuccessVote()) {
            return successVoteMsgs.values().iterator().next();
        }
        return null;
    }
}
