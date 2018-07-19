package com.desmart.desmartbpm.reflect;

import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.desmart.common.exception.PlatformException;
import com.desmart.desmartbpm.dao.DhDataExchangeMapper;
import com.desmart.desmartbpm.entity.DhStep;
import com.desmart.desmartportal.entity.DhProcessInstance;
import com.desmart.desmartportal.service.DhProcessInstanceService;
/**
 * 通过标识符数组往子流程的流程实例中设置数据
 * 参数格式
 * {
    "data":[
        {
            "identity": "第一个标识",
            "exchange": [
                {
                    "from": "sap_num",
                    "to": "sap_num_copy"
                }
            ]
        }
    ]
        
}
 * @author lys
 *
 */
public class DhProcessSetDataInfoBySonTrigger implements DhJavaClassTriggerTemplate {

	@Override
	public void execute(WebApplicationContext ac, String insUid, JSONObject jsonObject,DhStep dhStep) {
		 // 获得bean
	    DhProcessInstanceService dhProcessInstanceService = ac.getBean(DhProcessInstanceService.class);
	    DhDataExchangeMapper dataExchangeMapper = ac.getBean(DhDataExchangeMapper.class);
	    
	    /**
	     * 当前流程实例
	     */
	    DhProcessInstance dhProcessInstance = dhProcessInstanceService.getByInsUid(insUid);
        JSONObject insDataJsonSource = JSON.parseObject(dhProcessInstance.getInsData());
        JSONObject formDataJsonSource = insDataJsonSource.getJSONObject("formData");
        int insId = dhProcessInstance.getInsId();
        
        //获得标识符和数据字段映射的数组
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject dataObject = jsonArray.getJSONObject(i);
			String identity = dataObject.getString("identity");
			 // 根据设置的标识符和流程实例编号，获得需要的流程实例主键
	        String targetInsUid = dataExchangeMapper.getInsUidByInsIdAndIdentity(insId, identity);
	        if (targetInsUid == null) {
	            throw new PlatformException("获取数据失败");
	        }
	        // 目标流程实例
	        DhProcessInstance targetProcessInstance = dhProcessInstanceService.getByInsUid(targetInsUid);
	        JSONObject insDataJson = JSON.parseObject(targetProcessInstance.getInsData());
	        JSONObject formDataJson = insDataJson.getJSONObject("formData");
	        
	        //获得映射关系jsonobject对象数组
	        JSONArray mappingRelationArr = dataObject.getJSONArray("exchange");
	        for (int j = 0; j < mappingRelationArr.size(); j++) {
				JSONObject mappingRelationObj = mappingRelationArr.getJSONObject(j);
		
					String formKey = mappingRelationObj.getString("from");
					String toKey = mappingRelationObj.getString("to");//从当前流程实例读数据写入到目标流程
					//Object form = formDataJsonSource.getOrDefault(formKey, "");//返回指定键被映射到的值，如果此映射不包含键的映射，则返回defaultValue。
					Object form = formDataJsonSource.get(formKey);
					if(form == null) {
						form = "";
					}
					formDataJson.put(toKey, form);
			}
	        insDataJson.put("formData", formDataJson);
	        targetProcessInstance.setInsData(insDataJson.toJSONString());
	        dhProcessInstanceService.updateByPrimaryKeySelective(targetProcessInstance);//将数据回写到目标子流程实例数据中
	        
		}
	}

}
