package com.desmart.desmartbpm.reflect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.desmart.desmartbpm.dao.BpmActivityMetaMapper;
import com.desmart.desmartportal.entity.DhProcessInstance;
import com.desmart.desmartportal.service.DhProcessInstanceService;
import org.springframework.web.context.WebApplicationContext;


public class DemoJavaClass2 implements DhJavaClassTriggerTemplate {

	@Override
	public void execute(WebApplicationContext ac, String insUid, JSONObject jsonObject) {
		BpmActivityMetaMapper bpmActivityMetaMapper = ac.getBean(BpmActivityMetaMapper.class);
        DhProcessInstanceService dhProcessInstanceService = ac.getBean(DhProcessInstanceService.class);

        DhProcessInstance dhProcessInstance = dhProcessInstanceService.getByInsUid(insUid);
        String insData = dhProcessInstance.getInsData();
        JSONObject insDataJson = JSON.parseObject(insData);
        JSONObject formData = insDataJson.getJSONObject("formData");

        // 改变formData

        JSONObject obj = new JSONObject();
        obj.put("value", "这个是新插入的值2");
        formData.put("新的值2", jsonObject.getString("value"));

        dhProcessInstance.setInsData(insDataJson.toJSONString());
        dhProcessInstanceService.updateByPrimaryKeySelective(dhProcessInstance);

    }

}
