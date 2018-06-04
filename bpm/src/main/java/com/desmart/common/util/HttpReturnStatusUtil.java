package com.desmart.common.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import com.desmart.desmartbpm.common.HttpReturnStatus;

/**
 * 检查调用引擎RESTful API后的结果是否正确
 * @author yaoyunqing
 *
 */
public class HttpReturnStatusUtil {
    /**
     * 判断HttpReturnStatus是否反映出调用失败
     * 成功的调用必须满足条件：
     * 1） 状态码200
     * 2） 含有响应正文
     * 3） 响应正文中的状态"status" 不是 "error"
     * @param httpResult
     * @return
     */
    public static boolean isErrorResult(HttpReturnStatus httpResult) {
        boolean result = false;
        if (httpResult.getCode() == 200 && !StringUtils.isBlank(httpResult.getMsg())) {
            // 状态码是200 并且 响应体不为空时
            String msg = httpResult.getMsg();
            JSONObject jsoMsg = new JSONObject(msg);
            String status = jsoMsg.optString("status", "");
            if ("error".equals(status)) {
                result = true;
            }
        } else {
            result = true;
        }
        return result;
    }
    
    /**
     * 查看连续调用的返回结果中有没有异常的
     * 有的话在Map的"errorResult" key 设置这个HttpRetunStatus
     * @param resultMap
     * @return
     */
    public static Map<String, HttpReturnStatus> findErrorResult(Map<String, HttpReturnStatus> resultMap) {
        Map<String, HttpReturnStatus> errorHttpStatusMap = new HashMap();
        if (resultMap.containsKey("errorResult")) {
            // 如果"errorResult"这个key有对应的HttpReturnStatus则返回这个key对应的值
            errorHttpStatusMap.put("errorResult", (HttpReturnStatus)resultMap.get("errorResult"));
        } else {
            // 如果"errorResult"这个key没有对应的值，遍历所有key对应的HttpReturnStatus看有没有错误的
            Iterator var3 = resultMap.keySet().iterator();
            while(var3.hasNext()) {
                String key = (String)var3.next();
                HttpReturnStatus httpStatus = (HttpReturnStatus)resultMap.get(key);
                if (httpStatus.getCode() != 200 || StringUtils.isBlank(httpStatus.getMsg())) {
                    errorHttpStatusMap.put("errorResult", httpStatus);
                    break;
                }
                String msg = httpStatus.getMsg();
                JSONObject jsoMsg = new JSONObject(msg);
                String status = jsoMsg.optString("status", "");
                if ("error".equals(status)) {
                    errorHttpStatusMap.put("errorResult", httpStatus);
                    break;
                }
            }
        }

        return errorHttpStatusMap;
    }
}
