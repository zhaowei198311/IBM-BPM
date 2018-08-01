package com.desmart.desmartbpm.entity.engine;

/**
 * 描述引擎中的应用库
 */
public class LswProject {

    private String projectId; // 应用库id 无 "2066." 前缀
    private String name;  // 应用库名称


    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}