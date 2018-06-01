package com.desmart.common.util;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.desmart.desmartbpm.common.HttpReturnStatus;
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
            
            return httpReturnStatus;
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
	 *  驳回通用服务
	 * @param instansId  当前流程实例标识id
	 * @param flowobjectId 需要驳回到哪一个节点的标识id
	 * @param user 将任务分配给某个用户标识id
	 */
	public void rejectProcess(int insId,String flowobjectId, String user) {
		log.info("驳回服务  开始...");
		RestUtil restUtil = new RestUtil(bpmGlobalConfig);
		String msg = ""; // 全局消息
		int tkiid = 0; // 任务标识
		String tokenId = "" ; 
		try {
            String host = this.bpmGlobalConfig.getBpmServerHost();
            host = host.endsWith("/") ? host : host + "/";
            String processUrl = host + "rest/bpm/wle/v1/process/"+insId;
			// 这一步操作 是调用API 获取当前流程实例信息数据
			Map<String, Object> getMap = new HashMap<>();
			getMap.put("parts", "data|header|executionTree");
			HttpReturnStatus result = restUtil.doGet(processUrl, getMap);
			msg = result.getMsg();
			// 判断当前状态如果服务器返回200 才让执行回退任务
			if(result.getCode()==200) {
				// JSON 转换当前信息 获取 flowobjectid 和 分配给 某个用户
				JSONObject jsonobject = JSONObject.parseObject(msg);		
				// 获取data内容·
				JSONObject data = (JSONObject) jsonobject.get("data");
				JSONObject executionTree = (JSONObject) data.get("executionTree");
				JSONObject root = (JSONObject) executionTree.get("root");
				// 拿取tokenid 当前token
				JSONArray jsonArray = root.getJSONArray("children");
				// 拿取当前 tkiid 任务标识  操作 是 分配任务
				JSONArray jsonArray2 = data.getJSONArray("tasks");
				for (int i = 0; i < jsonArray.size(); i++) {
					JSONObject object = jsonArray.getJSONObject(i);
					// 获取到当前tokenId 节点id 回退起始位置
					tokenId = object.getString("tokenId");
				}
				for (int j = jsonArray2.size()-1; j < jsonArray2.size(); j++) {
					JSONObject tasks = jsonArray2.getJSONObject(j);
					tkiid = tasks.getInteger("tkiid");
					log.info("任务标识:" + tasks.getInteger("tkiid"));
				}
				// 第二次请求 带入参数 注意 第二次请求 是以post 请求方式 去执行一个动作 moveToken  移动token
				Map<String, Object> postMap = new HashMap<>();
				postMap.put("action", "moveToken");
				postMap.put("resume", "true");
				postMap.put("parts", "data|header|executionTree");
				postMap.put("target", flowobjectId);
				postMap.put("tokenId", tokenId);
				HttpReturnStatus result2 = restUtil.sendPost(processUrl, postMap);
				// 将当前用户分配给user 
				int taskid = tkiid;
				Map<String, Object> putMap = new HashMap<>();
				putMap.put("action", "assign");
				putMap.put("toUser", user);
				putMap.put("parts", "all");
				String taskUrl = host + "rest/bpm/wle/v1/task/"+taskid;
				HttpReturnStatus result3 = restUtil.sendPut(taskUrl, putMap);
				if(result3.getCode()==200) {
					log.info("驳回成功");
				}
			}
			log.info("驳回服务 结束...");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("驳回服务 异常...");
		}
	}
	
}
