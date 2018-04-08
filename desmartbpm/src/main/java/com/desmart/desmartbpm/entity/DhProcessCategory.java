package com.desmart.desmartbpm.entity;

/**
 * 流程分类
 * @author yaoyunqing
 *
 */
public class DhProcessCategory {
    
    private String categoryUid; // 流程分类id

    private String categoryParent; // 父流程分类id

    private String categoryName; // 流程分类名称

    private String categoryIcon; // 流程分类图标

    public DhProcessCategory() {
        
    }
    
    public DhProcessCategory(String categoryUid, String categoryParent, String categoryName, String categoryIcon) {
        this.categoryUid = categoryUid;
        this.categoryParent = categoryParent;
        this.categoryName = categoryName;
        this.categoryIcon = categoryIcon;
    }

    

    public String getCategoryUid() {
        return categoryUid;
    }

    public void setCategoryUid(String categoryUid) {
        this.categoryUid = categoryUid == null ? null : categoryUid.trim();
    }

    public String getCategoryParent() {
        return categoryParent;
    }

    public void setCategoryParent(String categoryParent) {
        this.categoryParent = categoryParent == null ? null : categoryParent.trim();
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName == null ? null : categoryName.trim();
    }

    public String getCategoryIcon() {
        return categoryIcon;
    }

    public void setCategoryIcon(String categoryIcon) {
        this.categoryIcon = categoryIcon == null ? null : categoryIcon.trim();
    }

    @Override
    public String toString() {
        return "DhProcessCategory [categoryUid=" + categoryUid
                + ", categoryParent=" + categoryParent + ", categoryName="
                + categoryName + ", categoryIcon=" + categoryIcon + "]";
    }
    
    
}