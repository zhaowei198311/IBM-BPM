package com.desmart.desmartbpm.util.http;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;

import com.desmart.desmartsystem.entity.BpmGlobalConfig;

public class BpmRestApiUtils {
	
    protected BpmClientUtils clientUtils;
    protected BpmGlobalConfig bpmGlobalConfig;

    
    public BpmRestApiUtils(BpmGlobalConfig bpmGlobalConfig, ServletContext servletContext) {
        this.bpmGlobalConfig = bpmGlobalConfig;
        this.clientUtils = new BpmClientUtils(bpmGlobalConfig, servletContext);
    }
    
    /**
     * 创建一个BpmRestApiUtils
     * @param bpmGlobalConfig  全局配置
     * @param isAdmin 是否是管理员
     */
    public BpmRestApiUtils(BpmGlobalConfig bpmGlobalConfig, boolean isAdmin) {
        this.bpmGlobalConfig = bpmGlobalConfig;
        this.clientUtils = new BpmClientUtils(bpmGlobalConfig, isAdmin);
    }

    /**
     * 创建一个BpmRestApiUtils
     * @param bpmGlobalConfig
     * @param isAdmin
     * @param servletContext
     */
    public BpmRestApiUtils(BpmGlobalConfig bpmGlobalConfig, boolean isAdmin, ServletContext servletContext) {
        this.bpmGlobalConfig = bpmGlobalConfig;
        this.clientUtils = new BpmClientUtils(bpmGlobalConfig, isAdmin, servletContext);
    }

    /**
     * 获取Shiro的Session
     * @return
     */
    protected final Session getSession() {
        Session session = SecurityUtils.getSubject().getSession();
        return session;
    }
    
    /**
     * 关闭持有的httpClient
     */
    public final void closeClient() {
        this.clientUtils.closeClient();
    }

    public String downloadFile(HttpServletRequest request, String downloadUrl, String saveDiskPath) {
        return this.downloadFile(request, downloadUrl, saveDiskPath, "");
    }

    public String downloadFile(HttpServletRequest request, String downloadUrl, String saveDiskPath, String saveFileName) {
        String downDefName = this.clientUtils.downloadFile(request, downloadUrl, saveDiskPath, saveFileName);
        String tmpSavePath = saveDiskPath.replace("\\", "/");
        tmpSavePath = tmpSavePath.endsWith("/") ? tmpSavePath : tmpSavePath + "/";
        if (StringUtils.isBlank(saveFileName)) {
            tmpSavePath = tmpSavePath + downDefName;
        } else {
            tmpSavePath = tmpSavePath + saveFileName;
        }

        return tmpSavePath;
    }
}
