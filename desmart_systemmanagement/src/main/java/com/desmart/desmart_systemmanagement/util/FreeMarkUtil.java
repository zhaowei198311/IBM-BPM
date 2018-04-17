package com.desmart.desmart_systemmanagement.util;

import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.Map;
import org.apache.commons.io.FilenameUtils;

public class FreeMarkUtil {
    private static final Logger LOG = LoggerFactory.getLogger(FreeMarkUtil.class);
    private Configuration cfg;

    public FreeMarkUtil() {
        this("/com/desmart/desmartbpm/template");
    }

    public FreeMarkUtil(String tplPath) {
        this.cfg = new Configuration(Configuration.VERSION_2_3_23);
        this.cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        this.cfg.setClassForTemplateLoading(this.getClass(), tplPath);
        this.cfg.setDefaultEncoding("UTF-8");
    }

    public Configuration getCfg() {
        return this.cfg;
    }

    /**
     * 根据.ftl模版文件和入参生成对应的字符串
     * @param templateName  模版名
     * @param data 数据
     * @return
     */
    public String getHtml(String templateName, Map<String, Object> data) {
        StringWriter writer = new StringWriter();

        try {
            Template template = this.cfg.getTemplate(templateName, "utf-8");
            template.process(data, writer);
        } catch (Exception var5) {
            LOG.error("渲染模版内容时发生异常！templateName=" + templateName, var5, new String[0]);
        }

        String html = writer.toString();
        return html;
    }


}
