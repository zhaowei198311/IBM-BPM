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
	        String obj = newObj.toJSONString();
	        return newObj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 从formData中取得指定key的值，没有值则返回null
	 * @param key
	 * @param formData
	 * @return
	 */
	public static String getStringValue(String key, JSONObject formData) {
	    JSONObject obj = (JSONObject)formData.get(key);
	    if (obj != null) {
	        return obj.getString("value");
	    } else {
	        return null;
	    }
	    
	}
	
	public static void main(String[] args) {
		FormDataUtil a = new FormDataUtil();


		
		JSONObject formData = new JSONObject();
        JSONObject oo = new JSONObject();
        oo.put("value", "cool");
        formData.put("name", oo);
        JSONObject oo1 = new JSONObject();
        oo1.put("value", "220.22");
        formData.put("salary", oo1);
        JSONObject oo5 = new JSONObject();
        oo5.put("value", "220.22");
        formData.put("zhaowei", oo5);
        System.out.println("1"+formData.toJSONString());
        
		JSONObject formData2 = new JSONObject();
        JSONObject oo2 = new JSONObject();
        oo2.put("value", "sadsad");
        formData2.put("name", oo2);
        JSONObject oo3 = new JSONObject();
        oo3.put("value", "cccc");
        formData2.put("salary", oo3);
        System.out.println("2"+formData2.toJSONString());
        
        a.formDataCombine(formData, formData2);

	}
}
