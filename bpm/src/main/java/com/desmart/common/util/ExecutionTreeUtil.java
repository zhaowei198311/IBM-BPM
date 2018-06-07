package com.desmart.common.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 用来分析执行树的工具类
 *
 */
public class ExecutionTreeUtil {
	
	static ArrayList<Object> preTokenIdList = new ArrayList<>();
	static int flag = 0;
	
	
    /**
     * 从流程详细信息中获得指定任务的tokenId和父tokenId
     * @param taskId 任务ID
     * @param jsonObject 由流程详细信息转化来的JSONObject
     * @return
     */
	public static final Map<Object, Object> queryTokenId(int taskId, JSONObject json){
    	// 解析jsonObject
    	JSONObject jsonObject = json.getJSONObject("data").getJSONObject("executionTree").getJSONObject("root");
		JSONArray jsonArray = jsonObject.getJSONArray("children");
		//第一次遍历children数组对象
		HashMap<Object,Object> hashMap = new HashMap<>();
		for (Iterator iterator = jsonArray.iterator(); iterator.hasNext();) {
			JSONObject object = (JSONObject) iterator.next();
			if(null != object.get("createdTaskIDs")) {
				//此处比较createdTaskIDs数组中是否包含taskId
				JSONArray taskIdsArray = object.getJSONArray("createdTaskIDs");
				for (int i = 0; i < taskIdsArray.size(); i++) {
					if (String.valueOf(taskId).equals(taskIdsArray.get(i))) {
						hashMap.put("tokenId", object.get("tokenId"));
						//第一次循环时，preTokenId为null
				        hashMap.put("preTokenId", null);
				        return hashMap;
					}
				}
			}else{
				//每次循环下级时候把此级tokenId放入list中
				preTokenIdList.add(object.get("tokenId"));
				JSONArray jsonArray2 = object.getJSONArray("children");
				HashMap utilMap = util(jsonArray2, taskId);
				if (utilMap != null) {
					return utilMap;
				} else {
					System.out.println("未查到结果...");
				}
			}
		}
		return null;
    }
	
	public static HashMap<Object, Object> util(JSONArray jsonArray, int taskId) {
		
		for (Iterator iterator = jsonArray.iterator(); iterator.hasNext();) {
			HashMap<Object,Object> map = new HashMap<>();
			JSONObject object = (JSONObject) iterator.next();
			if(null != object.get("createdTaskIDs")) {
				JSONArray taskIdsArray = object.getJSONArray("createdTaskIDs");
				for (int i = 0; i < taskIdsArray.size(); i++) {
					if (String.valueOf(taskId).equals(taskIdsArray.get(i))) {
						map.put("tokenId", object.get("tokenId"));
						//此处赋值上级preTokenId
						for (int j = flag; j >= 0; j--) {
							if(preTokenIdList.get(j) != null) {
								map.put("preTokenId", preTokenIdList.get(j));
								return map;
							}else {
								map.put("preTokenId", null);
							}
						}
						return map;
					}
				}	
			}else{
				JSONArray jsonArray2 = object.getJSONArray("children");
				preTokenIdList.add(object.get("tokenId"));
				flag++;
				HashMap util = util(jsonArray2, taskId);
				if(util != null) {
					return util;
				}
			}
		}
		return null;
	}
	
}
