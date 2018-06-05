package com.desmart.common.util;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class TaskIdUtil {
	
	public static final Map<String, Object> queryTokenId(int taskId, JSONObject jsonObject){
    	Map<String, Object> map = new HashMap<>();
    	String datas = "";
    	String [] dataList = null;
    	// 解析jsonObject
    	JSONObject data = jsonObject.getJSONObject("data");
    	JSONObject executionTree = data.getJSONObject("executionTree");
    	JSONObject root = executionTree.getJSONObject("root");
    	// 父children
    	JSONArray children = root.getJSONArray("children");
    	for (int i = 0; i < children.size(); i++) {
			JSONObject childrenData= (JSONObject) children.get(i);
			// 
			String checkData = childrenData.toString().trim();
			if (checkData.contains("\"" + taskId + "\"")) {
				// taskId的标记位
				int taskIdSign = checkData.indexOf("\"" + taskId + "\"");
				// 以taskId的下标为末尾截取checkData
				String fristSubstring = checkData.substring(0, taskIdSign);
				// 找到tokenId最后一次出现的位置
				int childTokenkId = fristSubstring.lastIndexOf("\"tokenId\"");
				datas = fristSubstring.substring(childTokenkId, fristSubstring.length());
				dataList = datas.split("\"");
				// tokenId
				map.put("tokenId", dataList[3]);
				// parentTokenId
				String secondSubstring = fristSubstring.substring(0, childTokenkId);
				// 找到tokenId最后一次出现的位置
				int parentTokenkId = secondSubstring.lastIndexOf("\"tokenId\"");
				datas = secondSubstring.substring(parentTokenkId, secondSubstring.length());
				dataList = datas.split("\"");
				map.put("parentTokenId", dataList[3]);
				break;
			}
		}
    	return map;
    }
}
