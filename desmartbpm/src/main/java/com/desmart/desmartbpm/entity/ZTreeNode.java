package com.desmart.desmartbpm.entity;

public class ZTreeNode {
    private String id;
    private String pid;
    private String name; // 节点显示名称
    private String icon;
    private String itemType;
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getPid() {
        return pid;
    }
    public void setPid(String pid) {
        this.pid = pid;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getIcon() {
        return icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }
    public String getItemType() {
        return itemType;
    }
    public void setItemType(String itemType) {
        this.itemType = itemType;
    }
    @Override
    public String toString() {
        return "ZTreeNode [id=" + id + ", pid=" + pid + ", name=" + name
                + ", icon=" + icon + ", itemType=" + itemType + "]";
    }
    
    
}
