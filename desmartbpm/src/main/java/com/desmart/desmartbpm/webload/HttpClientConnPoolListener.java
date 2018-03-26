package com.desmart.desmartbpm.webload;


import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartbpm.entity.BpmGlobalConfig;
import com.desmart.desmartbpm.service.BpmGlobalConfigService;
import com.desmart.desmartbpm.util.http.HttpClientConnPoolUtils;

import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class HttpClientConnPoolListener implements ServletContextListener {
    private static final Logger LOG = LoggerFactory.getLogger(HttpClientConnPoolListener.class);

    public HttpClientConnPoolListener() {
    }

    public void contextDestroyed(ServletContextEvent ctxevt) {
        ServletContext sctx = ctxevt.getServletContext();
        PoolingHttpClientConnectionManager poolmgr = (PoolingHttpClientConnectionManager)sctx.getAttribute(Const.HTTP_CLIENT_CONNECTION_POOL);
        if (poolmgr != null) {
            LOG.info("BPM连接池正在关闭......");
            poolmgr.shutdown();
        }

    }

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContextEvent.getServletContext());
        BpmGlobalConfigService bpmGlobalConfigService = applicationContext.getBean(BpmGlobalConfigService.class);
        BpmGlobalConfig bpmcfg = bpmGlobalConfigService.getFirstActConfig();
        PoolingHttpClientConnectionManager poolmgr = new PoolingHttpClientConnectionManager();
        HttpClientConnPoolUtils.setHttpPoolCfg(poolmgr, bpmcfg);
        ServletContext servletContext = servletContextEvent.getServletContext();
        servletContext.setAttribute(Const.HTTP_CLIENT_CONNECTION_POOL, poolmgr);
    }
}

