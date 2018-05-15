package com.desmart.desmartbpm.util.http;


import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpResponseUtils {
    private static final Logger LOG = LoggerFactory.getLogger(HttpResponseUtils.class);

    public HttpResponseUtils() {
    }

    public static void output(HttpServletResponse response, Object object) {
        output(response, object, "text/html;charset=utf-8");
    }

    public static void output(HttpServletResponse response, Object object, String contentType) {
        try {
            response.setContentType(contentType);
            PrintWriter out = response.getWriter();
            if (Exception.class.isInstance(object)) {
                ((Exception)Exception.class.cast(object)).printStackTrace(out);
            }

            out.println(object.toString());
            out.flush();
            out.close();
        } catch (Exception var4) {
            throw new RuntimeException(var4);
        }
    }

    public static String createRedirectErrorScript(String ctxPath, String errorMsg) {
        String encMsg = "";

        try {
            encMsg = URLEncoder.encode(errorMsg, "utf-8");
        } catch (UnsupportedEncodingException var6) {
            encMsg = errorMsg;
            LOG.error("字符串进行URL编码失败！字符串信息：" + errorMsg, var6);
        }

        String tmpPath = ctxPath.endsWith("/") ? ctxPath : ctxPath + "/";
        String redirectUrl = tmpPath + "console/error/500.xsp?errorMsg=" + encMsg;
        String script = "<script type='text/javascript'>window.location.replace(\"" + redirectUrl + "\");</script>";
        return script;
    }

    public static String getFileName(HttpResponse response) {
        Header contentHeader = response.getFirstHeader("Content-Disposition");
        String filename = null;
        if (contentHeader != null) {
            HeaderElement[] values = contentHeader.getElements();
            if (values.length == 1) {
                NameValuePair param = values[0].getParameterByName("filename");
                if (param != null) {
                    try {
                        filename = param.getValue();
                    } catch (Exception var6) {
                        LOG.error("获取Response流中的文件名发生异常！", var6);
                    }
                }
            }
        }

        return filename;
    }
}