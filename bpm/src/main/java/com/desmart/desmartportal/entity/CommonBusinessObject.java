package com.desmart.desmartportal.entity;

import java.io.Serializable;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonBusinessObject implements Cloneable, Serializable {
    private static final Logger LOG = LoggerFactory.getLogger(CommonBusinessObject.class);
    public static final String CREATOR_VARNAME = "creatorId";
    public static final String[] NEXT_OWNER_VARNAME = new String[]{"nextOwners_0", "nextOwners_1", "nextOwners_2", "nextOwners_3", "nextOwners_4", "nextOwners_5", "nextOwners_6", "nextOwners_7", "nextOwners_8", "nextOwners_9", "nextOwners_10", "nextOwners_11", "nextOwners_12", "nextOwners_13", "nextOwners_14", "nextOwners_15", "nextOwners_16", "nextOwners_17", "nextOwners_18", "nextOwners_19", "nextOwners_20"};
    public static final String[] OWNER_SIGN_COUNT = new String[]{"signCount_0", "signCount_1", "signCount_2", "signCount_3", "signCount_4", "signCount_5", "signCount_6", "signCount_7", "signCount_8", "signCount_9", "signCount_10", "signCount_11", "signCount_12", "signCount_13", "signCount_14", "signCount_15", "signCount_16", "signCount_17", "signCount_18", "signCount_19", "signCount_20"};
    private String stepId;
    private String creatorId;
    private String creatorName;
    private String docTitle;
    private String prevTaskId;
    private String documentId;
    private String appId;
    private String params;
    private List<String> nextOwners_0;
    private Double ttlAmount;
    private String instanceId;
    private String isOverSign;
    private String isAgent;
    private String instanceName;
    private String prevTaskName;
    private String prevTaskStartTime;
    private String prevTaskDoneTime;
    private String prevTaskDueTime;
    private List<String> nextOwners_1;
    private List<String> nextOwners_2;
    private List<String> nextOwners_3;
    private List<String> nextOwners_4;
    private List<String> nextOwners_5;
    private List<String> nextOwners_6;
    private List<String> nextOwners_7;
    private List<String> nextOwners_8;
    private List<String> nextOwners_9;
    private List<String> nextOwners_10;
    private List<String> nextOwners_11;
    private List<String> nextOwners_12;
    private List<String> nextOwners_13;
    private List<String> nextOwners_14;
    private List<String> nextOwners_15;
    private List<String> nextOwners_16;
    private List<String> nextOwners_17;
    private List<String> nextOwners_18;
    private List<String> nextOwners_19;
    private List<String> nextOwners_20;
    private String bpdId;
    private String procAppId;
    private String snapshotId;
    private String taskOwner;
    private String smartformsHost;
    private String sysMsg;
    private int signCount_0;
    private int signCount_1;
    private int signCount_2;
    private int signCount_3;
    private int signCount_4;
    private int signCount_5;
    private int signCount_6;
    private int signCount_7;
    private int signCount_8;
    private int signCount_9;
    private int signCount_10;
    private int signCount_11;
    private int signCount_12;
    private int signCount_13;
    private int signCount_14;
    private int signCount_15;
    private int signCount_16;
    private int signCount_17;
    private int signCount_18;
    private int signCount_19;
    private int signCount_20;
    private String tag;
    private String tag2;
    private String tag3;

    public CommonBusinessObject() {
    }

    /**
     * 创建pubBo并设置流程实例编号
     * @param insId
     */
    public CommonBusinessObject(int insId) {
        this.instanceId = String.valueOf(insId);
    }

    public CommonBusinessObject clone() {
        CommonBusinessObject bo = null;

        try {
            bo = (CommonBusinessObject)super.clone();
        } catch (CloneNotSupportedException var3) {
            bo = null;
            LOG.error("克隆BO对象失败！", var3);
        }

        return bo;
    }

    public String getStepId() {
        return this.stepId;
    }

    public void setStepId(String stepId) {
        this.stepId = stepId;
    }

    public String getCreatorId() {
        return this.creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return this.creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getDocTitle() {
        return this.docTitle;
    }

    public void setDocTitle(String docTitle) {
        this.docTitle = docTitle;
    }

    public String getPrevTaskId() {
        return this.prevTaskId;
    }

    public void setPrevTaskId(String prevTaskId) {
        this.prevTaskId = prevTaskId;
    }

    public String getDocumentId() {
        return this.documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getAppId() {
        return this.appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getParams() {
        return this.params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public Double getTtlAmount() {
        return this.ttlAmount;
    }

    public void setTtlAmount(Double ttlAmount) {
        this.ttlAmount = ttlAmount;
    }

    public String getInstanceId() {
        return this.instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getIsOverSign() {
        return this.isOverSign;
    }

    public void setIsOverSign(String isOverSign) {
        this.isOverSign = isOverSign;
    }

    public String getIsAgent() {
        return this.isAgent;
    }

    public void setIsAgent(String isAgent) {
        this.isAgent = isAgent;
    }

    public String getInstanceName() {
        return this.instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getPrevTaskName() {
        return this.prevTaskName;
    }

    public void setPrevTaskName(String prevTaskName) {
        this.prevTaskName = prevTaskName;
    }

    public String getPrevTaskStartTime() {
        return this.prevTaskStartTime;
    }

    public void setPrevTaskStartTime(String prevTaskStartTime) {
        this.prevTaskStartTime = prevTaskStartTime;
    }

    public String getPrevTaskDoneTime() {
        return this.prevTaskDoneTime;
    }

    public void setPrevTaskDoneTime(String prevTaskDoneTime) {
        this.prevTaskDoneTime = prevTaskDoneTime;
    }

    public String getPrevTaskDueTime() {
        return this.prevTaskDueTime;
    }

    public void setPrevTaskDueTime(String prevTaskDueTime) {
        this.prevTaskDueTime = prevTaskDueTime;
    }

    public List<String> getNextOwners_0() {
        return this.nextOwners_0;
    }

    public void setNextOwners_0(List<String> nextOwners_0) {
        this.nextOwners_0 = nextOwners_0;
    }

    public List<String> getNextOwners_1() {
        return this.nextOwners_1;
    }

    public void setNextOwners_1(List<String> nextOwners_1) {
        this.nextOwners_1 = nextOwners_1;
    }

    public List<String> getNextOwners_2() {
        return this.nextOwners_2;
    }

    public void setNextOwners_2(List<String> nextOwners_2) {
        this.nextOwners_2 = nextOwners_2;
    }

    public List<String> getNextOwners_3() {
        return this.nextOwners_3;
    }

    public void setNextOwners_3(List<String> nextOwners_3) {
        this.nextOwners_3 = nextOwners_3;
    }

    public List<String> getNextOwners_4() {
        return this.nextOwners_4;
    }

    public void setNextOwners_4(List<String> nextOwners_4) {
        this.nextOwners_4 = nextOwners_4;
    }

    public List<String> getNextOwners_5() {
        return this.nextOwners_5;
    }

    public void setNextOwners_5(List<String> nextOwners_5) {
        this.nextOwners_5 = nextOwners_5;
    }

    public List<String> getNextOwners_6() {
        return this.nextOwners_6;
    }

    public void setNextOwners_6(List<String> nextOwners_6) {
        this.nextOwners_6 = nextOwners_6;
    }

    public List<String> getNextOwners_7() {
        return this.nextOwners_7;
    }

    public void setNextOwners_7(List<String> nextOwners_7) {
        this.nextOwners_7 = nextOwners_7;
    }

    public List<String> getNextOwners_8() {
        return this.nextOwners_8;
    }

    public void setNextOwners_8(List<String> nextOwners_8) {
        this.nextOwners_8 = nextOwners_8;
    }

    public List<String> getNextOwners_9() {
        return this.nextOwners_9;
    }

    public void setNextOwners_9(List<String> nextOwners_9) {
        this.nextOwners_9 = nextOwners_9;
    }

    public List<String> getNextOwners_10() {
        return this.nextOwners_10;
    }

    public void setNextOwners_10(List<String> nextOwners_10) {
        this.nextOwners_10 = nextOwners_10;
    }

    public List<String> getNextOwners_11() {
        return this.nextOwners_11;
    }

    public void setNextOwners_11(List<String> nextOwners_11) {
        this.nextOwners_11 = nextOwners_11;
    }

    public List<String> getNextOwners_12() {
        return this.nextOwners_12;
    }

    public void setNextOwners_12(List<String> nextOwners_12) {
        this.nextOwners_12 = nextOwners_12;
    }

    public List<String> getNextOwners_13() {
        return this.nextOwners_13;
    }

    public void setNextOwners_13(List<String> nextOwners_13) {
        this.nextOwners_13 = nextOwners_13;
    }

    public List<String> getNextOwners_14() {
        return this.nextOwners_14;
    }

    public void setNextOwners_14(List<String> nextOwners_14) {
        this.nextOwners_14 = nextOwners_14;
    }

    public List<String> getNextOwners_15() {
        return this.nextOwners_15;
    }

    public void setNextOwners_15(List<String> nextOwners_15) {
        this.nextOwners_15 = nextOwners_15;
    }

    public List<String> getNextOwners_16() {
        return this.nextOwners_16;
    }

    public void setNextOwners_16(List<String> nextOwners_16) {
        this.nextOwners_16 = nextOwners_16;
    }

    public List<String> getNextOwners_17() {
        return this.nextOwners_17;
    }

    public void setNextOwners_17(List<String> nextOwners_17) {
        this.nextOwners_17 = nextOwners_17;
    }

    public List<String> getNextOwners_18() {
        return this.nextOwners_18;
    }

    public void setNextOwners_18(List<String> nextOwners_18) {
        this.nextOwners_18 = nextOwners_18;
    }

    public List<String> getNextOwners_19() {
        return this.nextOwners_19;
    }

    public void setNextOwners_19(List<String> nextOwners_19) {
        this.nextOwners_19 = nextOwners_19;
    }

    public List<String> getNextOwners_20() {
        return this.nextOwners_20;
    }

    public void setNextOwners_20(List<String> nextOwners_20) {
        this.nextOwners_20 = nextOwners_20;
    }

    public String getBpdId() {
        return this.bpdId;
    }

    public void setBpdId(String bpdId) {
        this.bpdId = bpdId;
    }

    public String getProcAppId() {
        return this.procAppId;
    }

    public void setProcAppId(String procAppId) {
        this.procAppId = procAppId;
    }

    public String getSnapshotId() {
        return this.snapshotId;
    }

    public void setSnapshotId(String snapshotId) {
        this.snapshotId = snapshotId;
    }

    public String getTaskOwner() {
        return this.taskOwner;
    }

    public void setTaskOwner(String taskOwner) {
        this.taskOwner = taskOwner;
    }

    public String getSmartformsHost() {
        return this.smartformsHost;
    }

    public void setSmartformsHost(String smartformsHost) {
        this.smartformsHost = smartformsHost;
    }

    public String getSysMsg() {
        return this.sysMsg;
    }

    public void setSysMsg(String sysMsg) {
        this.sysMsg = sysMsg;
    }

    public int getSignCount_0() {
        return this.signCount_0;
    }

    public void setSignCount_0(int signCount_0) {
        this.signCount_0 = signCount_0;
    }

    public int getSignCount_1() {
        return this.signCount_1;
    }

    public void setSignCount_1(int signCount_1) {
        this.signCount_1 = signCount_1;
    }

    public int getSignCount_2() {
        return this.signCount_2;
    }

    public void setSignCount_2(int signCount_2) {
        this.signCount_2 = signCount_2;
    }

    public int getSignCount_3() {
        return this.signCount_3;
    }

    public void setSignCount_3(int signCount_3) {
        this.signCount_3 = signCount_3;
    }

    public int getSignCount_4() {
        return this.signCount_4;
    }

    public void setSignCount_4(int signCount_4) {
        this.signCount_4 = signCount_4;
    }

    public int getSignCount_5() {
        return this.signCount_5;
    }

    public void setSignCount_5(int signCount_5) {
        this.signCount_5 = signCount_5;
    }

    public int getSignCount_6() {
        return this.signCount_6;
    }

    public void setSignCount_6(int signCount_6) {
        this.signCount_6 = signCount_6;
    }

    public int getSignCount_7() {
        return this.signCount_7;
    }

    public void setSignCount_7(int signCount_7) {
        this.signCount_7 = signCount_7;
    }

    public int getSignCount_8() {
        return this.signCount_8;
    }

    public void setSignCount_8(int signCount_8) {
        this.signCount_8 = signCount_8;
    }

    public int getSignCount_9() {
        return this.signCount_9;
    }

    public void setSignCount_9(int signCount_9) {
        this.signCount_9 = signCount_9;
    }

    public int getSignCount_10() {
        return this.signCount_10;
    }

    public void setSignCount_10(int signCount_10) {
        this.signCount_10 = signCount_10;
    }

    public int getSignCount_11() {
        return this.signCount_11;
    }

    public void setSignCount_11(int signCount_11) {
        this.signCount_11 = signCount_11;
    }

    public int getSignCount_12() {
        return this.signCount_12;
    }

    public void setSignCount_12(int signCount_12) {
        this.signCount_12 = signCount_12;
    }

    public int getSignCount_13() {
        return this.signCount_13;
    }

    public void setSignCount_13(int signCount_13) {
        this.signCount_13 = signCount_13;
    }

    public int getSignCount_14() {
        return this.signCount_14;
    }

    public void setSignCount_14(int signCount_14) {
        this.signCount_14 = signCount_14;
    }

    public int getSignCount_15() {
        return this.signCount_15;
    }

    public void setSignCount_15(int signCount_15) {
        this.signCount_15 = signCount_15;
    }

    public int getSignCount_16() {
        return this.signCount_16;
    }

    public void setSignCount_16(int signCount_16) {
        this.signCount_16 = signCount_16;
    }

    public int getSignCount_17() {
        return this.signCount_17;
    }

    public void setSignCount_17(int signCount_17) {
        this.signCount_17 = signCount_17;
    }

    public int getSignCount_18() {
        return this.signCount_18;
    }

    public void setSignCount_18(int signCount_18) {
        this.signCount_18 = signCount_18;
    }

    public int getSignCount_19() {
        return this.signCount_19;
    }

    public void setSignCount_19(int signCount_19) {
        this.signCount_19 = signCount_19;
    }

    public int getSignCount_20() {
        return this.signCount_20;
    }

    public void setSignCount_20(int signCount_20) {
        this.signCount_20 = signCount_20;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTag2() {
        return this.tag2;
    }

    public void setTag2(String tag2) {
        this.tag2 = tag2;
    }

    public String getTag3() {
        return this.tag3;
    }

    public void setTag3(String tag3) {
        this.tag3 = tag3;
    }
}