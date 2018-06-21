package com.desmart.common.util;

import java.util.*;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.desmart.desmartsystem.util.Json;
import org.apache.commons.lang3.StringUtils;

/**
 * 用来分析流程信息的工具类
 *
 */
public class ProcessDataUtil {

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
				JSONArray jsonArray2 = object.getJSONArray("children");
				//children数组有内容就继续往下遍历
				if(jsonArray2.size() > 0) {
					// 每次循环下级时候把此级tokenId放入list中
					HashMap<String, Object> map = new HashMap<>();
					map.put("tokenId", object.get("tokenId"));
					map.put("flowObjectId", object.get("flowObjectId"));
					preTokenIdList.add(map);
					HashMap<Object, Object> utilMap = util(jsonArray2, taskId, preTokenIdList, flag);
					
					return utilMap;
				}
				/*else {
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
						map.put("preTokenId", null);
						// 此处赋值上级preTokenId
						String flowObjectId = (String) object.get("flowObjectId");
						for (int j = flag; j >= 0; j--) {
							if (preTokenIdList.get(j) != null) {
								if (preTokenIdList.get(j).get("flowObjectId") != null
										&& !preTokenIdList.get(j).get("flowObjectId").equals(flowObjectId)) {
									map.put("preTokenId", preTokenIdList.get(j).get("tokenId"));
									return map;
								}
							}
						}
						return map;
					}
				}
			}
			
			//判断是否符合递归的条件
			JSONArray jsonArray2 = object.getJSONArray("children");
			if(jsonArray2 != null) {
				HashMap<String, Object> currentNodeMap = new HashMap<>();
				currentNodeMap.put("tokenId", object.get("tokenId"));
				currentNodeMap.put("flowObjectId", object.get("flowObjectId"));
				preTokenIdList.add(currentNodeMap);
				//每深入一级，flag加1
				flag = flag + 1;
				if (null != jsonArray2) {
					HashMap<Object, Object> util = util(jsonArray2, taskId, preTokenIdList, flag);
					if (util != null) {
						return util;
					}
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

    /**
     * 获得指定任务的tokenId，如果是多实例或简单循环任务，获得此节点上的顶级tokenId
     * @param taskId  任务id
     * @param json  流程的详细数据
     * @return
     */
    public static String getTokenIdOfTask(int taskId, JSONObject json) {
        Map<Object, Object> map = getTokenIdAndLastTokenIdByTaskId(taskId, json);
        if (map == null) return null;
        return (String) map.get("tokenId");
    }
    
	/**
	 * 从流程详细信息中获得指定任务的tokenId和父tokenId
	 * (与getTokenIdAndPreTokenIdByTaskId方法不同，此方法返回的preTokenId为检索到的createdTaskIDs与上级createdTaskIDs相同的父级的tokenId)
	 * @param taskId ： 任务ID
	 * @param json ：由流程详细信息转化来的JSONObject
	 * @return Map中的key ： 1）tokenId 2) preTokenId
	 */
	public static final Map<Object, Object> getTokenIdAndLastTokenIdByTaskId(int taskId, JSONObject json) {
		
		//标志位，每深入一级，flag+1（children数组递归的次数）
				int flag = 0;
				
				// 解析jsonObject得到目标数据
				JSONObject jsonObject = json.getJSONObject("data").getJSONObject("executionTree").getJSONObject("root");
				JSONArray jsonArray = jsonObject.getJSONArray("children");
				
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
								//hashMap.put("preTokenId", null);
								return hashMap;
							}
						}
					} else {
						JSONArray jsonArray2 = object.getJSONArray("children");
						//children数组有内容就继续往下遍历
						if(jsonArray2.size() > 0) {
							// 每次循环下级时候把此级tokenId放入list中
							HashMap<String, Object> map = new HashMap<>();
							map.put("tokenId", object.get("tokenId"));
							map.put("flowObjectId", object.get("flowObjectId"));
							/*HashMap<Object, Object> hashMap2 = new HashMap<>();
							hashMap2.put(flag, hashMap2);*/
							preTokenIdList.add(map);
							HashMap<Object, Object> utilMap = getTokenIdAndLastTokenIdUtil(jsonArray2, taskId, preTokenIdList, flag);
							if (utilMap != null) {
								
								if(utilMap.get("preTokenId") != null) {
									utilMap.put("tokenId", utilMap.get("preTokenId"));
								}
								utilMap.remove("preTokenId");
								return utilMap;
								
							}
						}
						 /*else {
							System.out.println("未查到结果...");
						}*/
					}
					
				}
				return null;
	}

	/**
	 * getTokenIdAndLastTokenIdByTaskId工具方法（递归用）
	 * @param jsonArray   children对象数组
	 * @param taskId    接收参数taskId
	 * @param preTokenIdList  从父节点开始，每个节点公用一个list，存此级下的所有数据
	 * @param flag   标志位，每深入一级，flag+1
	 * @return
	 */
	private static HashMap<Object, Object> getTokenIdAndLastTokenIdUtil(JSONArray jsonArray, int taskId,
			ArrayList<HashMap<String, Object>> preTokenIdList, int flag) {


		for (Iterator<?> iterator = jsonArray.iterator(); iterator.hasNext();) {
			HashMap<Object, Object> map = new HashMap<>();
			JSONObject object = (JSONObject) iterator.next();
			if (null != object.get("createdTaskIDs")) {
				JSONArray taskIdsArray = object.getJSONArray("createdTaskIDs");
				for (int i = 0; i < taskIdsArray.size(); i++) {
					if (String.valueOf(taskId).equals(taskIdsArray.get(i))) {
						map.put("tokenId", object.get("tokenId"));
						map.put("preTokenId", null);
						// 此处赋值上级preTokenId
						String flowObjectId = (String) object.get("flowObjectId");
						for (int j = flag; j >= 0; j--) {
							if (preTokenIdList.get(j) != null) {
								if (preTokenIdList.get(j).get("flowObjectId") != null
										&& preTokenIdList.get(j).get("flowObjectId").equals(flowObjectId)) {
									//map.put("lastTokenId", object.get("tokenId"));
									map.put("preTokenId", preTokenIdList.get(j).get("tokenId"));
									return map;
								}
							} 
						}
						return map;
					}
				}
			}
			
			//判断是否符合递归的条件
			JSONArray jsonArray2 = object.getJSONArray("children");
			if(jsonArray2 != null) {
				// 每次循环下级时候把此级tokenId放入list中
				HashMap<String, Object> currentNodeMap = new HashMap<>();
				currentNodeMap.put("tokenId", object.get("tokenId"));
				currentNodeMap.put("flowObjectId", object.get("flowObjectId"));
				preTokenIdList.add(currentNodeMap);
				//每深入一级，flag加1
				flag = flag + 1;
				HashMap<Object, Object> util = getTokenIdAndLastTokenIdUtil(jsonArray2, taskId, preTokenIdList, flag);
				if (util != null) {
					return util;
				}
			}
		}
		return null;
	}

    /**
     * 根据元素节点，找出该节点上未完成的任务, 如果有多个任务，只返回第一个
     * @param flowObjectId
     * @param data
     * @return
     */
	public static List<Integer> getActiveTaskIdByFlowObjectId(String flowObjectId, JSONObject data) {
        JSONObject dataJson = data.getJSONObject("data");
        JSONArray tasks = dataJson.getJSONArray("tasks");
        List<Integer> taskIdList = new ArrayList<>();
        for (int i = 0; i < tasks.size(); i++) {
            JSONObject taskJson = tasks.getJSONObject(i);
            if (!"Received".equals(taskJson.getString("status"))) {
                continue;
            }
            String taskFlowObjectId = taskJson.getString("flowObjectID");
            if (StringUtils.equals(flowObjectId, taskFlowObjectId)) {
                taskIdList.add(new Integer(taskJson.getString("tkiid")));
            }
        }
        return taskIdList;
    }

}
