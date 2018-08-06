package com.desmart.desmartbpm.reflect;

import java.util.List;

import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.desmart.common.exception.PlatformException;
import com.desmart.desmartbpm.dao.DhStepMapper;
import com.desmart.desmartbpm.entity.DhStep;
import com.desmart.desmartbpm.enums.DhStepType;
import com.desmart.desmartportal.dao.DhProcessInstanceMapper;
import com.desmart.desmartportal.entity.DhProcessInstance;
import com.desmart.desmartportal.service.DhProcessInstanceService;
/**
 * 从主流程中获取表单流水号并设置当前流程中的formNoList
 * @author lys
 *
 */
public class DhProcessGetFormNoListByMain extends DhOneTimeJavaClassTrigger {

	@Override
	public void doOneTime(WebApplicationContext ac, String insUid, JSONObject jsonObject, DhStep dhStep) {
		// 获得bean
		DhProcessInstanceService dhProcessInstanceService = ac.getBean(DhProcessInstanceService.class);
		DhProcessInstanceMapper dhProcessInstanceMapper = ac.getBean(DhProcessInstanceMapper.class);
		DhStepMapper dhStepMapper = ac.getBean(DhStepMapper.class);

		// 找到该环节配置中同步骤关键字的表单关键字
		DhStep selective1 = new DhStep(dhStep.getProAppId(), dhStep.getProUid(), dhStep.getProVerUid());
		selective1.setActivityBpdId(dhStep.getActivityBpdId());
		selective1.setStepBusinessKey(dhStep.getStepBusinessKey());
		selective1.setStepType(DhStepType.FORM.getCode());
		List<DhStep> checkList = dhStepMapper.listBySelective(selective1);
		String currFormUid = checkList.get(0).getStepObjectUid();//获得当前表单的formuid
		if (checkList != null && checkList.size() == 1) {
			DhProcessInstance dhProcessInstance = dhProcessInstanceService.getByInsUid(insUid);
			JSONObject insDataJsonSource = JSON.parseObject(dhProcessInstance.getInsData());
			JSONArray currFormNoList = new JSONArray();//保存当前流程实例的formNoList
			
			int insId = dhProcessInstance.getInsId();
			// 得到目标实例并获得数据——主流程实例
			DhProcessInstance targetProcessInstance = dhProcessInstanceMapper.getMainProcessByInsId(insId);
			JSONObject insDataJson = JSON.parseObject(targetProcessInstance.getInsData());
			JSONArray formNoListArr = insDataJson.getJSONArray("formNoList");
			if(formNoListArr!=null&&formNoListArr.size()>0) {
				JSONObject formNoItem = formNoListArr.getJSONObject(0);
				formNoItem.put("formUid", currFormUid);
				currFormNoList.add(formNoItem);
				
				insDataJsonSource.put("formNoList", currFormNoList);
				dhProcessInstance.setInsData(insDataJsonSource.toJSONString());
				dhProcessInstanceService.updateByPrimaryKeySelective(dhProcessInstance);// 将数据写入到当前流程实例中
			}
			
		} else {
			throw new PlatformException("环节表单步骤配置出现异常");
		}
	}
}
