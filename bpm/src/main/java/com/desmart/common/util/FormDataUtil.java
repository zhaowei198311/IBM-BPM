/**
 * 
 */
package com.desmart.common.util;

import java.util.Iterator;

import com.desmart.desmartportal.entity.DhProcessInstance;
import org.apache.log4j.Logger;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
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
	        for(Iterator var3 = newObj.keySet().iterator(); var3.hasNext();) {
	            key = var3.next();
	            if(newObj.get(key) != null) {    //新的值为空的时候，不替换new的值到old中去
	            	oldObj.put((String) key, newObj.get(key));
	            }
	        }
	        return oldObj;
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

    /**
     * 从流程实例中得到 String类型的formData
     * @param dhProcessInstance
     * @return
     */
	public static String getFormDataStringFromProcessInstance(DhProcessInstance dhProcessInstance) {
		JSONObject insDataJson = JSON.parseObject(dhProcessInstance.getInsData());
        JSONObject formDataJson = insDataJson.getJSONObject("formData");
        return formDataJson.toJSONString();
    }

    public static JSONObject getFormDataJsonFromProcessInstance(DhProcessInstance dhProcessInstance) {
		JSONObject insDataJson = JSON.parseObject(dhProcessInstance.getInsData());
		return insDataJson.getJSONObject("formData");
	}



	public static void main(String[] args) {
		FormDataUtil a = new FormDataUtil();

		String oldData = "{\"vendorType\":{\"value\":\"\"},\"fileName\":{\"value\":\"fdsffds\"},\"cooperationMode\":{\"value\":\"\"},\"financialCoding\":{\"value\":\"\"},\"landlord\":{\"value\":\"\"},\"useForPrint\":{\"value\":\"fddfdfdf\"},\"companyName\":{\"value\":\"\"},\"processNumber\":{\"value\":\"\"},\"contractLease\":{\"value\":\"\"},\"rent\":{\"value\":\"\"},\"bankOfDeposit\":{\"value\":\"\"},\"collaborator\":{\"value\":\"dffddf\"},\"ownershipCompany\":{\"value\":\"\"},\"reconciliationAccount\":{\"value\":\"\"},\"companyNumber\":{\"value\":\"\"},\"rentDate\":{\"value\":\"\"},\"price\":{\"value\":\"fdfddf\"},\"contact\":{\"value\":\"\"},\"contactNumber\":{\"value\":\"\"},\"paymentCycle\":{\"value\":\"\"},\"scopeOperation\":{\"value\":\"\"},\"rentFreeDate\":{\"value\":\"\"},\"email\":{\"value\":\"\"},\"termOfPayment\":{\"value\":\"\"},\"nameOfBank\":{\"value\":\"\"},\"orderCurrency\":{\"value\":\"\"},\"dateOfDelivery\":{\"value\":\"\"},\"dissolution\":{\"value\":\"undefined\"},\"contractCoding\":{\"value\":\"\"},\"vendorName\":{\"value\":\"\"},\"bankAddress\":{\"value\":\"\"},\"printNumber\":{\"value\":\"dfdf\"},\"sealName\":{\"value\":\"\"},\"landlordCode\":{\"value\":\"\"},\"builtUpArea\":{\"value\":\"\"},\"bankCountryCode\":{\"value\":\"\"},\"storeAddress\":{\"value\":\"\"},\"accountOpeningAccount\":{\"value\":\"\"},\"leaseDate\":{\"value\":\"\"},\"companyAddress\":{\"value\":\"\"},\"paymentMethod\":{\"value\":\"\"},\"postCode\":{\"value\":\"\"},\"standardContract\":{\"value\":\"4XX20\"},\"agencyFee\":{\"value\":\"\"},\"printTime\":{\"value\":\"\"}}";
		String newData = "{\"\":{\"value\":\"undefined\"},\"fileName\":{\"value\":\"fdsffds\"},\"financialCoding\":{\"value\":\"\"},\"useForPrint\":{\"value\":\"fddfdfdf\"},\"companyName\":{\"value\":\"\"},\"contractLease\":{\"value\":\"\"},\"rent\":{\"value\":\"\"},\"collaborator\":{\"value\":\"dffddf\"},\"reconciliationAccount\":{\"value\":\"\"},\"price\":{\"value\":\"fdfddf\"},\"contact\":{\"value\":\"\"},\"scopeOperation\":{\"value\":\"\"},\"rentFreeDate\":{\"value\":\"\"},\"termOfPayment\":{\"value\":\"\"},\"nameOfBank\":{\"value\":\"\"},\"orderCurrency\":{\"value\":\"\"},\"dissolution\":{\"value\":\"undefined\"},\"contractCoding\":{\"value\":\"\"},\"vendorName\":{\"value\":\"\"},\"bankAddress\":{\"value\":\"\"},\"sealName\":{\"value\":\"\"},\"landlordCode\":{\"value\":\"\"},\"builtUpArea\":{\"value\":\"\"},\"bankCountryCode\":{\"value\":\"\"},\"storeAddress\":{\"value\":\"\"},\"paymentMethod\":{\"value\":\"\"},\"postCode\":{\"value\":\"\"},\"standardContract\":{\"value\":\"4XX20\"},\"vendorType\":{\"value\":\"\"},\"cooperationMode\":{\"value\":\"\"},\"landlord\":{\"value\":\"\"},\"processNumber\":{\"value\":\"\"},\"bankOfDeposit\":{\"value\":\"\"},\"ownershipCompany\":{\"value\":\"\"},\"companyNumber\":{\"value\":\"\"},\"rentDate\":{\"value\":\"\"},\"contactNumber\":{\"value\":\"\"},\"paymentCycle\":{\"value\":\"\"},\"email\":{\"value\":\"\"},\"createDate\":{\"value\":\"2018-05-30\"},\"dateOfDelivery\":{\"value\":\"\"},\"userName\":{\"value\":\"马亚伟\"},\"userId\":{\"value\":\"00011178\"},\"printNumber\":{\"value\":\"dfdf\"},\"accountOpeningAccount\":{\"value\":\"\"},\"leaseDate\":{\"value\":\"\"},\"companyAddress\":{\"value\":\"\"},\"agencyFee\":{\"value\":\"\"},\"printTime\":{\"value\":\"\"}}";
		
		JSONObject formData = JSON.parseObject(newData);
		
		JSONObject formData2 = JSON.parseObject(oldData);
        
        a.formDataCombine(formData, formData2);

	}
	
	
}
