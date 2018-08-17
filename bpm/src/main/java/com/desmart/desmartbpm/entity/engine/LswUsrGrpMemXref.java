package com.desmart.desmartbpm.entity.engine;

/**
 * 临时组与用户的对应关系
 */
public class LswUsrGrpMemXref {

    private int groupId;  // 临时组ID
    private String userId;   // 引擎中的人员ID
    private String userUid;  // 平台中的人员工号

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }
}