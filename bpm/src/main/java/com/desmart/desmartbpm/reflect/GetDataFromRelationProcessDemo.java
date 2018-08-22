package com.desmart.desmartbpm.reflect;

import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.desmart.common.exception.PlatformException;
import com.desmart.common.util.FormDataUtil;
import com.desmart.desmartbpm.dao.DhDataExchangeMapper;
import com.desmart.desmartbpm.entity.DhStep;
import com.desmart.desmartportal.entity.DhProcessInstance;
import com.desmart.desmartportal.service.DhProcessInstanceService;

/**
 * 设置主流程范围内交互数据用的标识
 */
public class GetDataFromRelationProcessDemo implements DhJavaClassTriggerTemplate {

	@Override
	public void execute(WebApplicationContext ac, String insUid, JSONObject param,DhStep dhStep) {
	    // 需要通过哪个标识来找关联流程
	    String identity = "第一个标识";

        // 获得bean
	    DhProcessInstanceService dhProcessInstanceService = ac.getBean(DhProcessInstanceService.class);
        DhDataExchangeMapper dataExchangeMapper = ac.getBean(DhDataExchangeMapper.class);

        DhProcessInstance dhProcessInstance = dhProcessInstanceService.getByInsUid(insUid);
        JSONObject insDataJsonSource = JSON.parseObject(dhProcessInstance.getInsData());
        JSONObject formDataJsonSource = insDataJsonSource.getJSONObject("formData");

        int insId = dhProcessInstance.getInsId();

        // 根据设置的标识符和流程实例编号，获得需要的流程实例主键
        String targetInsUid = dataExchangeMapper.getInsUidByInsIdAndIdentity(insId, identity);
        if (targetInsUid == null) {
        	throw new PlatformException("获取数据失败，通过标识符找目标实例失败：identity:"+identity+",insId:"+insId);
        }
        // 得到目标实例并获得数据
        DhProcessInstance targetProcessInstance = dhProcessInstanceService.getByInsUid(targetInsUid);
        JSONObject insDataJson = JSON.parseObject(targetProcessInstance.getInsData());
        JSONObject formDataJson = insDataJson.getJSONObject("formData");
        System.out.println("||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        System.out.println(FormDataUtil.getStringValue("sapCode", formDataJson));

        JSONObject obj = new JSONObject();
        obj.put("value", FormDataUtil.getStringValue("sapCode", formDataJson) + "-add");
        formDataJsonSource.put("sapCode", obj);


        dhProcessInstance.setInsData(insDataJsonSource.toJSONString());
        dhProcessInstanceService.updateByPrimaryKeySelective(dhProcessInstance);

    }

}
