package com.desmart.common.util;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONObject;

public class JSONUtils {
	
    public JSONUtils() {
    }

    /**
     * 将新的JSONObject的属性更新到老的JSONObject,SignCount不更新
     * @param newObj
     * @param oldObj 原先的对象
     * @return
     */
    public static final JSONObject combine(JSONObject newObj, JSONObject oldObj) {
        Object key;
        Object combinedObjVal;
        for(Iterator var3 = newObj.keySet().iterator(); var3.hasNext(); oldObj.put(key.toString(), combinedObjVal)) {
            key = var3.next();
            JSONObject newObjVal = newObj.optJSONObject(key.toString());
            JSONObject oldObjVal = oldObj.optJSONObject(key.toString());
            combinedObjVal = null;
            if (newObjVal != null && oldObjVal != null) {
                combinedObjVal = combine(newObjVal, oldObjVal);
            } else {
                // 如果是signCount就直接设置为新对象中的值
                combinedObjVal = newObj.get(key.toString());
            }
        }

        return oldObj;
    }

    public static final void append(JSONArray oldArr, JSONArray newArr) {
        for(int i = 0; i < newArr.length(); ++i) {
            oldArr.put(newArr.get(i));
        }

    }
}

