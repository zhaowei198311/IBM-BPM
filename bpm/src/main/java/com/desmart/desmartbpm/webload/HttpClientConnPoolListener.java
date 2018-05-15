package com.desmart.desmartbpm.webload;


import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartbpm.util.http.HttpClientConnPoolUtils;
import com.desmart.desmartsystem.entity.BpmGlobalConfig;
import com.desmart.desmartsystem.service.BpmGlobalConfigService;

public class HttpClientConnPoolListener implements ServletContextListener {
    private static final Logger LOG = LoggerFactory.getLogger(HttpClientConnPoolListener.class);

    public HttpClientConnPoolListener() {
    }

    /**
     * 初始化
     */
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContextEvent.getServletContext());
        BpmGlobalConfigService bpmGlobalConfigService = applicationContext.getBean(BpmGlobalConfigService.class);
        BpmGlobalConfig bpmcfg = bpmGlobalConfigService.getFirstActConfig();
        PoolingHttpClientConnectionManager poolmgr = new PoolingHttpClientConnectionManager();
        HttpClientConnPoolUtils.setHttpPoolCfg(poolmgr, bpmcfg);
        ServletContext servletContext = servletContextEvent.getServletContext();
        System.out.println("=====   设置Manager      ==========");
        servletContext.setAttribute(Const.HTTP_CLIENT_CONNECTION_POOL, poolmgr);
    }
    
    
    /**
     * 关闭
     */
    public void contextDestroyed(ServletContextEvent ctxevt) {
        ServletContext sctx = ctxevt.getServletContext();
        PoolingHttpClientConnectionManager poolmgr = (PoolingHttpClientConnectionManager)sctx.getAttribute(Const.HTTP_CLIENT_CONNECTION_POOL);
        if (poolmgr != null) {
            LOG.info("BPM连接池正在关闭......");
            poolmgr.shutdown();
        }

    }


}

