package com.desmart.desmartbpm.entity.engine;

public class GroupAndMember {
    private Integer groupId;
    private String members;
    public Integer getGroupId() {
        return groupId;
    }
    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }
    public String getMembers() {
        return members;
    }
    public void setMembers(String members) {
        this.members = members;
    }
    @Override
    public String toString() {
        return "GroupAndMember [groupId=" + groupId + ", members=" + members
                + "]";
    }
    
    
}
