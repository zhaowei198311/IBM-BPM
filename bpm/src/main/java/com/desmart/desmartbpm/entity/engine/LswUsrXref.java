package com.desmart.desmartbpm.entity.engine;

/**
 * 描述引擎中的一个任务处理人
 */
public class LswUsrXref {
    private Integer userId;  // 主键
    private String userName; // 对应引擎主键/即工号
    private String fullName; // 姓名
    private String provider; // ldap信息

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    @Override
    public String toString() {
        return "LswUsrXre{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", fullName='" + fullName + '\'' +
                ", provider='" + provider + '\'' +
                '}';
    }
}