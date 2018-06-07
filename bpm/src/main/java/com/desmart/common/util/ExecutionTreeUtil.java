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
	public static final Map<Object, Object> queryTokenId(int taskId, JSONObject jsonObject){
    	// 解析jsonObject
    	JSONObject data = jsonObject.getJSONObject("data").getJSONObject("executionTree").getJSONObject("root");
    	JSONArray childrenList = data.getJSONArray("children");
    	HashMap<Object, Object> map = util(childrenList, taskId);
    	System.out.println("map:" + map);
    	return map;
    }
	
	public static HashMap<Object, Object> util(JSONArray jsonArray, int taskId) {
		
		HashMap<Object, Object> hashMap = new HashMap<>();
		JSONObject result = new JSONObject();
		for (int i = 0; i < jsonArray.size(); i++) {
			// for (Iterator var = jsonArray.iterator(); var.hasNext();) {
			result = (JSONObject) jsonArray.get(i);
			preTokenIdList.add(result.get("tokenId"));
			if (null != result.get("createdTaskIDs")) {
				// 获取createdTaskIDs，此处createdTaskIDs是数组
				JSONArray jsonArray2 = result.getJSONArray("createdTaskIDs");
				// 迭代createdTaskIDs，找到能匹配taskId参数的
				for (Iterator iterator = jsonArray2.iterator(); iterator.hasNext();) {
					String object = (String) iterator.next();
					if (String.valueOf(taskId).equals(object)) {
						hashMap.put("createdTaskIDs", result.get("createdTaskIDs"));
						hashMap.put("tokenId", result.get("tokenId"));
						hashMap.put("preTokenId", null);
						if (preTokenIdList.size() > 0) {
							Collections.reverse(preTokenIdList);
							for (int j = flag; j > 0; j--) {
								if (preTokenIdList.get(flag) != null) {
									hashMap.put("preTokenId", preTokenIdList.get(j));
									break;
								}
							}
						}
						return hashMap;
					}
				}
			}
			flag++;
		}
		JSONArray jsonArray2 = result.getJSONArray("children");
		if (null != jsonArray2) {
			hashMap = util(jsonArray2, taskId);
		}
		if (hashMap != null) {
			return hashMap;
		}
		return null;
	}
}
