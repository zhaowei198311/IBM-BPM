package com.desmart.desmartbpm.reflect;

import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.desmart.desmartbpm.entity.DhStep;
import com.desmart.desmartportal.dao.DhProcessInstanceMapper;
import com.desmart.desmartportal.entity.DhProcessInstance;
import com.desmart.desmartportal.service.DhProcessInstanceService;
/**
 * 往主流程的流程实例中写入数据
 * 参数格式
 * {
    "data":[
        {
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
public class DhProcessSetDataInfoByMainTrigger implements DhJavaClassTriggerTemplate {

	@Override
	public void execute(WebApplicationContext ac, String insUid, JSONObject jsonObject,DhStep dhStep) {
		 // 获得bean
	    DhProcessInstanceService dhProcessInstanceService = ac.getBean(DhProcessInstanceService.class);
	    DhProcessInstanceMapper dhProcessInstanceMapper = ac.getBean(DhProcessInstanceMapper.class);
	    
	    DhProcessInstance dhProcessInstance = dhProcessInstanceService.getByInsUid(insUid);
        JSONObject insDataJsonSource = JSON.parseObject(dhProcessInstance.getInsData());
        JSONObject formDataJsonSource = insDataJsonSource.getJSONObject("formData");
        int insId = dhProcessInstance.getInsId();
        
        //获得标识符和数据字段映射的数组
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject dataObject = jsonArray.getJSONObject(i);


	        // 得到目标实例并获得数据——主流程实例
	        DhProcessInstance targetProcessInstance = dhProcessInstanceMapper.getMainProcessByInsId(insId);
	        JSONObject insDataJson = JSON.parseObject(targetProcessInstance.getInsData());
	        JSONObject formDataJson = insDataJson.getJSONObject("formData");
	        
	        //获得映射关系jsonobject对象数组
	        JSONArray mappingRelationArr = dataObject.getJSONArray("exchange");
	        for (int j = 0; j < mappingRelationArr.size(); j++) {
				JSONObject mappingRelationObj = mappingRelationArr.getJSONObject(j);
			
					String formKey = mappingRelationObj.getString("from");
					String toKey = mappingRelationObj.getString("to");//往主流程实例读数据写入数据
					//Object form = formDataJsonSource.getOrDefault(formKey, "");//返回指定键被映射到的值，如果此映射不包含键的映射，则返回defaultValue。
					Object form = formDataJsonSource.get(formKey);
					if(form == null) {
						form = "";
					}
					formDataJson.put(toKey, form);
			
			}
	        insDataJson.put("formData", formDataJson);
	        targetProcessInstance.setInsData(insDataJson.toJSONString());
	        dhProcessInstanceService.updateByPrimaryKeySelective(targetProcessInstance);//将数据写入到主流程实例中
		}
	}

}
