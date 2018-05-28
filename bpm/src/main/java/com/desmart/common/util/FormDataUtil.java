/**
 * 
 */
package com.desmart.common.util;

import java.util.Iterator;

import org.apache.log4j.Logger;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**  
* <p>Title: 表单数据工具类</p>  
* <p>Description: </p>  
* @author zhaowei  
* @date 2018年5月28日  
*/
public class FormDataUtil {
	
	private Logger log = Logger.getLogger(FormDataUtil.class);
	
	/**
	 * 将新的Formdata 覆盖 旧的 Formdata
	 * @param oldObj 旧的json对象
	 * @param newObj 新的json对象
	 */
	public static JSONObject formDataCombine(JSONObject newObj, JSONObject oldObj) {
		try {
	        Object key;
	        Object combinedObjVal; // 接受新的json对象
	        for(Iterator var3 = oldObj.keySet().iterator(); var3.hasNext(); newObj.put(key.toString(), combinedObjVal)) {
	            key = var3.next();
	            //optJSONObject(key.toString());
	            JSONObject newObjVal = oldObj.getJSONObject("");
	            JSONObject oldObjVal = newObj.getJSONObject("");
	            combinedObjVal = null;
	            if (newObjVal != null && oldObjVal != null) {
	                combinedObjVal = formDataCombine(oldObj, newObj);
	            } else {
	                combinedObjVal = oldObj.get(key.toString());
	            }
	        }
	        System.err.println(newObj);
	        return newObj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		FormDataUtil a = new FormDataUtil();
    	JSONObject json1 = new JSONObject();
    	JSONArray array1 = new JSONArray();
    	array1.add("asdasd");
    	array1.add("222");
    	json1.put("name", "asdasdas");
    	json1.put("sex", "asdasdas");
    	json1.put("hobby", array1);
    	json1.put("1", "a");
    	json1.put("2", "b");
    	
    	JSONArray array2 = new JSONArray();
    	array2.add("啊啊啊");
    	array2.add("1111");
    	
    	JSONObject json2 = new JSONObject();
    	json2.put("name", "新的");
    	json2.put("sex", "男的");
    	json2.put("seccccx", "a");
    	json2.put("hobby", array2);
    	a.formDataCombine(json1, json2);
	}
}
