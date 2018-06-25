package com.desmart.common.util;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.desmart.desmartbpm.common.HttpReturnStatus;
import com.desmart.desmartbpm.util.http.BpmClientUtils;
import com.desmart.desmartportal.entity.CommonBusinessObject;
import com.desmart.desmartsystem.entity.BpmGlobalConfig;

/**
 * 与流程引擎交互流程操作
 *
 */
public class BpmProcessUtil {
    private static final Logger log = LoggerFactory.getLogger(BpmProcessUtil.class);
    
    private BpmGlobalConfig bpmGlobalConfig;
    
    public BpmProcessUtil(BpmGlobalConfig bpmGlobalConfig) {
        this.bpmGlobalConfig = bpmGlobalConfig;
    }
    
    /**
     * 发起流程
     * @param proAppId
     * @param proUid
     * @param proVerUid
     * @param pubBo
     * @return
     */
    public HttpReturnStatus startProcess(String proAppId, String proUid, String proVerUid, CommonBusinessObject pubBo) {
        RestUtil restUtil = new RestUtil(bpmGlobalConfig);
        HttpReturnStatus httpReturnStatus = null;
        try {
            String host = this.bpmGlobalConfig.getBpmServerHost();
            host = host.endsWith("/") ? host : host + "/";
            String url = host + "rest/bpm/wle/v1/process";
            
            Map<String, Object> paramsMap = new HashMap<>();
            paramsMap.put("pubBo", pubBo);
            JSONObject paramMapJson = new JSONObject(paramsMap);
            
            Map<String, Object> postMap = new HashMap<>();
            postMap.put("action", "start");
            postMap.put("bpdId", proUid);
            postMap.put("processAppId", proAppId);
            postMap.put("snapshotId", proVerUid);
            postMap.put("params", paramMapJson.toString());
            
            httpReturnStatus = restUtil.sendPost(url, postMap);
            
        } catch (Exception e) {
            log.error("发起流程失败", e);
            httpReturnStatus = new HttpReturnStatus();
            httpReturnStatus.setCode(-1);
            httpReturnStatus.setMsg(e.toString());
        } finally {
            restUtil.close();
        }
        return httpReturnStatus;
    }

	/**
	 * 根据实例id获得引擎中pubBo对象
	 * @param insId
	 * @return
	 */
	public HttpReturnStatus getProcessData(Integer insId) {
        HttpReturnStatus result = null;
        RestUtil restUtil = new RestUtil(bpmGlobalConfig);
        
        try {
            String host = this.bpmGlobalConfig.getBpmServerHost();
            String url = host + "rest/bpm/wle/v1/process/" + insId;
            Map<String, Object> params = new HashMap<>();
            
            result = restUtil.doGet(url, params);
        } catch (Exception e) {
            log.error("获得流程实例信息失败, 流程实例ID：" + insId, e);
            result = new HttpReturnStatus();
            result.setCode(-1);
            result.setMsg(e.toString());
        } finally {
            restUtil.close();
        }
        return result;
	}
	
	/**
	 * 将流程实例中pubBo的数据更新
	 * Map中的key：
	 * 1） "getProcessResult"
	 * 2) "setProcessResult"
	 * 3) "errorResult"  当发生错误时
	 * @param insId 实例编号
	 * @param pubBo 引擎中参数
	 * @param getProcessResult  如果已经查询过流程实例信息，传入此参数，方法内不再去查询, 没有的话传入null
	 * @return
	 */
	public Map<String, HttpReturnStatus> setProcessData(Integer insId, CommonBusinessObject pubBo,
	        HttpReturnStatus getProcessResult) {
	    Map<String, HttpReturnStatus> resultMap = new HashMap<>();
	    RestUtil restUtil = new RestUtil(bpmGlobalConfig);
	    
	    try {
	        if (getProcessResult == null) {
	            getProcessResult = this.getProcessData(insId);
	        }
	        if (!HttpReturnStatusUtil.isErrorResult(getProcessResult)) {
	            resultMap.put("getProcessResult", getProcessResult);
	            JSONObject jsoResult = new JSONObject(getProcessResult.getMsg());
	            JSONObject dataJson = jsoResult.getJSONObject("data");
	            JSONObject variablesJson = dataJson.getJSONObject("variables");
	            JSONObject oldPubBo = variablesJson.getJSONObject("pubBo");
	            if (!oldPubBo.isNull("@metadata")) {
	                // 如果pubBo中有"@metadata"这个键，则移除
	                oldPubBo.remove("@metadata");
	            }
	            JSONObject newPubBo = new JSONObject(pubBo);
	            newPubBo = JSONUtils.combine(newPubBo, oldPubBo);
                
	            String key;
                JSONObject jsoNextOwners;
                Set<String> keyset = newPubBo.keySet();
                Iterator<String> iterator = keyset.iterator();
                while(iterator.hasNext()) {
                    key = iterator.next();
                    if (key.startsWith("nextOwners_")) {
                        jsoNextOwners = newPubBo.optJSONObject(key);
                        /* 
                         * 如果是oldPubBo中带过来的， 取JSONObject可以取到，
                         * 如果是newPubBo中设置的，是一个JSONArray数组，无法用取JSONObject方法获得
                         */
                        if (jsoNextOwners != null) {
                            JSONArray jayOwnerItems = jsoNextOwners.optJSONArray("items");
                            List<String> tmpOwners = new ArrayList<>();
                            if (jayOwnerItems != null) {
                                for(int i = 0; i < jayOwnerItems.length(); ++i) {
                                    tmpOwners.add(jayOwnerItems.optString(i, ""));
                                }
                            }
                            newPubBo.put(key, tmpOwners);
                        }
                    }
                }
                // 新的pubBo准备完成
                System.out.println(newPubBo);
                
                String host = this.bpmGlobalConfig.getBpmServerHost();
                String url = host + "rest/bpm/wle/v1/process/" + insId + "/variable/pubBo";
                String postContent = newPubBo.toString();
                HttpReturnStatus setProcessResult = restUtil.doPut(url, postContent);
                if (!HttpReturnStatusUtil.isErrorResult(setProcessResult)) {
                    resultMap.put("setProcessResult", setProcessResult);
                } else {
                    resultMap.put("errorResult", setProcessResult);
                }
	        } else {
                resultMap.put("errorResult", getProcessResult);
            }
	    } catch (Exception e) {
            log.error("设置流程实例中数据出错，实例ID：" + insId, e);
            HttpReturnStatus errorStatus = new HttpReturnStatus();
            errorStatus.setCode(-1);
            errorStatus.setMsg(e.toString());
            resultMap.put("errorResult", errorStatus);
        } finally {
            restUtil.close();
        }
        return resultMap;
	}
	
	/**
	 * 设置流程实例中的pubBo并移动令牌
	 * @param insId
	 * @param pubBo
	 * @param tokenId  token编号
	 * @return
	 * 返回值中包含的key
	 * "getProcessResult"
	 * "setProcessResult"
	 * "moveTokenResult"
	 * "errorResult" 当调用失败时有这个key
	 */
	public Map<String, HttpReturnStatus> setDataAndMoveToken(Integer insId, String flowObjectId, CommonBusinessObject pubBo, String tokenId) {
	    Map<String, HttpReturnStatus> resultMap = new HashMap<>();
	    RestUtil restUtil = new RestUtil(bpmGlobalConfig);
	    
	    try {
	        String host = this.bpmGlobalConfig.getBpmServerHost();
	        // 这一步操作 是调用API 获取当前流程实例信息数据
	        HttpReturnStatus getProcessResult = this.getProcessData(insId);
	        if (!HttpReturnStatusUtil.isErrorResult(getProcessResult)) {
	            resultMap.put("getProcessResult", getProcessResult);
	            // 先设置数据，再移动token
	            resultMap = this.setProcessData(insId, pubBo, getProcessResult);
	            if (HttpReturnStatusUtil.findErrorResult(resultMap).get("errorResult") == null) {
	                // 设置数据成功, 移动token
	                String moveTokenUrl = host + "rest/bpm/wle/v1/process/"+insId;
	                Map<String, Object> postMap = new HashMap<>();
	                postMap.put("action", "moveToken");
	                postMap.put("resume", "true");
	                postMap.put("parts", "data|header|executionTree");
	                postMap.put("target", flowObjectId);
	                postMap.put("tokenId", tokenId);
	                // 调用RESTfual API
	                HttpReturnStatus moveTokenResult = restUtil.sendPost(moveTokenUrl, postMap);
	                if (!HttpReturnStatusUtil.isErrorResult(moveTokenResult)) {
	                    resultMap.put("moveTokenResult", moveTokenResult);
	                } else {
	                    resultMap.put("errorResult", moveTokenResult);
	                }
	            } else {
	                // 设置数据失败
	                return resultMap;
	            }
	        } else {
	            resultMap.put("errorResult", getProcessResult);
	        }
	    } catch (Exception e) {
            log.error("移动令牌失败，实例ID: " + insId + ", flowObjectId: " + flowObjectId, e);
            HttpReturnStatus errorStatus = new HttpReturnStatus();
            errorStatus.setCode(-1);
            errorStatus.setMsg(e.toString());
            resultMap.put("errorResult", errorStatus);
        } finally {
            restUtil.close();
        }
        return resultMap;
	}

    /**
     * 移动token
     * @param insId  流程实例编号
     * @param flowObjectId   流程图上元素id
     * @param tokenId  tokenId
     * @return
     */
    public HttpReturnStatus moveToken(Integer insId, String flowObjectId, String tokenId) {
        HttpReturnStatus result = null;
        RestUtil restUtil = new RestUtil(bpmGlobalConfig);

        try {
            String host = this.bpmGlobalConfig.getBpmServerHost();
            // 设置数据成功, 移动token
            String moveTokenUrl = host + "rest/bpm/wle/v1/process/"+insId;
            Map<String, Object> postMap = new HashMap<>();
            postMap.put("action", "moveToken");
            postMap.put("resume", "true");
            postMap.put("parts", "data|header|executionTree");
            postMap.put("target", flowObjectId);
            postMap.put("tokenId", tokenId);
            // 调用RESTfual API
            result = restUtil.sendPost(moveTokenUrl, postMap);
        } catch (Exception e) {
            log.error("移动令牌失败，实例ID: " + insId + ", flowObjectId: " + flowObjectId, e);
            result = new HttpReturnStatus();
            result.setCode(-1);
            result.setMsg(e.toString());

        } finally {
            restUtil.close();
        }
        return result;
    }


	
	public HttpReturnStatus getProcessModel(String proAppId, String bpdId, String snapshotId) {
	    RestUtil restUtil = new RestUtil(bpmGlobalConfig);
	    HttpReturnStatus result = null;
	    try {
	        String host = bpmGlobalConfig.getBpmServerHost();
	        host = host.endsWith("/") ? host : host + "/";
	        String url = host + "rest/bpm/wle/v1/processModel/{0}?{1}";
	        String params = "processAppId=" + proAppId;
	        if (StringUtils.isNotBlank(snapshotId)) {
	            params = params + "&snapshotId=" + snapshotId;
	        }
	        url = MessageFormat.format(url, bpdId, params);
	        Map<String, Object> pmap = new HashMap<>();
	        result = restUtil.doGet(url, pmap);
	    } catch (Exception e) {
	        log.error("获得ProcessModel失败：proAppId: " + proAppId + ", bpdId: " + bpdId + ", snapshotId: " + snapshotId, e);
	        result = new HttpReturnStatus();
	        result.setCode(-1);
	        result.setMsg(e.toString());
	    } finally {
	        restUtil.close();
	    }	    
	    return result; 
	}
	
	
	public HttpReturnStatus getVisualModel(String proAppId, String bpdId, String snapshotId) {
	    RestUtil restUtil = new RestUtil(bpmGlobalConfig);
        HttpReturnStatus result = null;
        try {
            String host = bpmGlobalConfig.getBpmServerHost();
            host = host.endsWith("/") ? host : host + "/";
	        String url = host + "rest/bpm/wle/v1/processModel/{0}?{1}";
            String params = "projectId=" + proAppId;
            if (StringUtils.isNotBlank(snapshotId)) {
                params = params + "&snapshotId=" + snapshotId;
            }
            url = MessageFormat.format(url, bpdId, params);
            Map<String, Object> pmap = new HashMap<>();
            result = restUtil.doGet(url, pmap);
        } catch (Exception e) {
            log.error("获得VisualModel失败：proAppId: " + proAppId + ", bpdId: " + bpdId + ", snapshotId: " + snapshotId, e);
            result = new HttpReturnStatus();
            result.setCode(-1);
            result.setMsg(e.toString());
        } finally {
            restUtil.close();
        }       
        return result;
	}

	/**
	 * 终止流程实例
	 * @param request
	 * @param instanceId
	 * @return
	 */
	public HttpReturnStatus terminateInstance(String instanceId) {
		HttpReturnStatus result = null;
		RestUtil restUtil = new RestUtil(bpmGlobalConfig);

		try {
			String host = this.bpmGlobalConfig.getBpmServerHost();
			host = host.endsWith("/") ? host : host + "/";
			String url = host + "rest/bpm/wle/v1/process/{0}";
			Map<String, Object> pmap = new HashMap();
			url = MessageFormat.format(url, instanceId);
			url = url + "?action=terminate";
			result = restUtil.doPut(url, pmap);
			if (BpmClientUtils.isErrorResult(result)) {
				log.error("停止流程实例失败！\r\n" + result.getMsg());
				result.setCode(-1);
			}
		} catch (Exception var7) {
			log.error("停止流程实例出错，instanceId：" + instanceId, var7);
			result.setCode(-1);
			result.setMsg(var7.toString());
		}

		return result;
	}


}
