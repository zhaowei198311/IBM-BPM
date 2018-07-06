package com.desmart.desmartbpm.reflect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.desmart.common.exception.PlatformException;
import com.desmart.desmartbpm.dao.BpmActivityMetaMapper;
import com.desmart.desmartportal.entity.DhProcessInstance;
import com.desmart.desmartportal.service.DhProcessInstanceService;
import org.springframework.web.context.WebApplicationContext;


public class SetNextBusinessKey implements DhJavaClassTriggerTemplate {

	@Override
	public void execute(WebApplicationContext ac, String insUid, JSONObject param) {
		BpmActivityMetaMapper bpmActivityMetaMapper = ac.getBean(BpmActivityMetaMapper.class);
        DhProcessInstanceService dhProcessInstanceService = ac.getBean(DhProcessInstanceService.class);

        DhProcessInstance dhProcessInstance = dhProcessInstanceService.getByInsUid(insUid);
        String insData = dhProcessInstance.getInsData();
        JSONObject insDataJson = JSON.parseObject(insData);
        JSONObject processData = insDataJson.getJSONObject("processData");
        // 获得参数中的businessKey
        String businessKey = param.getString("nextBusinessKey");
        if (businessKey != null) {
            processData.put("nextBusinessKey", businessKey);
        } else {
            processData.put("nextBusinessKey", "default");
        }

        dhProcessInstance.setInsData(insDataJson.toJSONString());
        int count = dhProcessInstanceService.updateByPrimaryKeySelective(dhProcessInstance);
        if (count == 0) {
            throw new PlatformException("SetNextBusinessKey失败");
        }

    }

}
