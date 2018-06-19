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
   
	//static int flag = 0;

	/**
	 * 从流程详细信息中获得指定任务的tokenId和父tokenId(代表子流程)
	 * @param taskId ： 任务ID
	 * @param json ：由流程详细信息转化来的JSONObject
	 * @return Map中的key ： 1）tokenId 2) preTokenId
	 */
	public static final Map<Object, Object> getTokenIdAndPreTokenIdByTaskId(int taskId, JSONObject json) {
		
		int flag = 0;
		// 解析jsonObject
		JSONObject jsonObject = json.getJSONObject("data").getJSONObject("executionTree").getJSONObject("root");
		JSONArray jsonArray = jsonObject.getJSONArray("children");
		//ArrayList<HashMap<String, Object>> preTokenIdList = new ArrayList<>();
		// 第一次遍历children数组对象
		HashMap<Object, Object> hashMap = new HashMap<>();
		for (Iterator iterator = jsonArray.iterator(); iterator.hasNext();) {
			ArrayList<HashMap<String, Object>> preTokenIdList = new ArrayList<>();
			JSONObject object = (JSONObject) iterator.next();
			if (null != object.get("createdTaskIDs")) {
				// 此处比较createdTaskIDs数组中是否包含taskId
				JSONArray taskIdsArray = object.getJSONArray("createdTaskIDs");
				for (int i = 0; i < taskIdsArray.size(); i++) {
					if (String.valueOf(taskId).equals(taskIdsArray.get(i))) {
						hashMap.put("tokenId", object.get("tokenId"));
						// 第一次循环时，preTokenId为null
						hashMap.put("preTokenId", null);
						return hashMap;
					}
				}
			} else {
				// 每次循环下级时候把此级tokenId放入list中
				HashMap<String, Object> map = new HashMap<>();
				map.put("tokenId", object.get("tokenId"));
				map.put("flowObjectId", object.get("flowObjectId"));
				preTokenIdList.add(map);
				JSONArray jsonArray2 = object.getJSONArray("children");
				HashMap<Object, Object> utilMap = util(jsonArray2, taskId, preTokenIdList, flag);
				if (utilMap != null) {
					return utilMap;
				} /*else {
					System.out.println("未查到结果...");
				}*/
			}
		}
		return null;
	}

	private static HashMap<Object, Object> util(JSONArray jsonArray, int taskId,
			ArrayList<HashMap<String, Object>> preTokenIdList, int flag) {

		for (Iterator<?> iterator = jsonArray.iterator(); iterator.hasNext();) {
			HashMap<Object, Object> map = new HashMap<>();
			JSONObject object = (JSONObject) iterator.next();
			if (null != object.get("createdTaskIDs")) {
				JSONArray taskIdsArray = object.getJSONArray("createdTaskIDs");
				for (int i = 0; i < taskIdsArray.size(); i++) {
					if (String.valueOf(taskId).equals(taskIdsArray.get(i))) {
						map.put("tokenId", object.get("tokenId"));
						// 此处赋值上级preTokenId
						String flowObjectId = (String) object.get("flowObjectId");
						for (int j = flag; j >= 0; j--) {
							if (preTokenIdList.get(j) != null) {
								if (preTokenIdList.get(j).get("flowObjectId") != null
										&& !preTokenIdList.get(j).get("flowObjectId").equals(flowObjectId)) {
									map.put("preTokenId", preTokenIdList.get(j).get("tokenId"));
									return map;
								}
							} else {
								map.put("preTokenId", null);
							}
						}
						return map;
					}
				}
			}
			HashMap<String, Object> currentNodeMap = new HashMap<>();
			currentNodeMap.put("tokenId", object.get("tokenId"));
			currentNodeMap.put("flowObjectId", object.get("flowObjectId"));
			preTokenIdList.add(currentNodeMap);
			JSONArray jsonArray2 = object.getJSONArray("children");
			//每深入一级，flag加1
			flag++;
			if (null != jsonArray2) {
				HashMap<Object, Object> util = util(jsonArray2, taskId, preTokenIdList, flag);
				if (util != null) {
					return util;
				}
			}
		}
		return null;
	}

	/**
	 * 根据代表子流程的节点和子流程第一个任务的元素的元素id查询子流程的tokenId
	 * @param json： JSONObject对象
	 * @param preFlowObjectId：代表子流程的节点的元素id
	 * @param childFlowObjectId：代表子流程第一个任务节点的元素id
	 * @return 代表子流程的节点上的tokenId
	 */
	public static String getTokenIdIdentifySubProcess(JSONObject json, String preFlowObjectId, String childFlowObjectId) {

		JSONObject jsonObject = json.getJSONObject("data").getJSONObject("executionTree").getJSONObject("root");
		JSONArray jsonArray = jsonObject.getJSONArray("children");
		// 第一次遍历children数组对象
		for (Iterator iterator = jsonArray.iterator(); iterator.hasNext();) {
			JSONObject object = (JSONObject) iterator.next();
			if (preFlowObjectId.equals(object.get("flowObjectId"))) {
				JSONArray childArray = object.getJSONArray("children");
				if (null != childArray) {
					boolean resultFlag = queryCurrentTokenIdUtil(childArray, preFlowObjectId, childFlowObjectId);
					if (resultFlag) {
						String resultStr = object.get("tokenId").toString();
						return resultStr;
						// System.out.println("***" + object.get("tokenId"));
					}
				}
			}
		}
		return null;
	}

	/**
	 * 获取preFlowObjectId同级下的tokenId辅助方法
	 * 
	 * @param jsonArray
	 * @param preFlowObjectId
	 * @param childFlowObjectId
	 * @return
	 */
	private static boolean queryCurrentTokenIdUtil(JSONArray jsonArray, String preFlowObjectId,
			String childFlowObjectId) {

		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject object = (JSONObject) jsonArray.get(i);
			Object flowObjectId = object.get("flowObjectId");
			if (null == flowObjectId) {
				JSONArray childArray = object.getJSONArray("children");
				if (null == childArray) {
					return false;
				} else {
					boolean flag = queryCurrentTokenIdUtil(childArray, preFlowObjectId, childFlowObjectId);
					if (flag) {
						return true;
					}
				}
			} else {
				if (childFlowObjectId.equals(flowObjectId)) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean isExecutionTreeContainsFlowObjectId(String flowObjectId, JSONObject processData) {
        JSONObject data = processData.getJSONObject("data");
        String executionTreeStr = data.getString("executionTree");
        if (executionTreeStr == null) {
            return false;
        }
        return executionTreeStr.contains(flowObjectId);
    }

    public static boolean isProcessFinished(JSONObject processData) {
        JSONObject data = processData.getJSONObject("data");
        return "STATE_FINISHED".equalsIgnoreCase(data.getString("state"));
    }



}
