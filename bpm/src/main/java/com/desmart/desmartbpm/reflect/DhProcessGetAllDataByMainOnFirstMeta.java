package com.desmart.desmartbpm.reflect;

import java.util.Iterator;
import java.util.List;

import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.desmart.common.constant.ServerResponse;
import com.desmart.common.exception.PlatformException;
import com.desmart.desmartbpm.dao.DhStepMapper;
import com.desmart.desmartbpm.entity.BpmFormField;
import com.desmart.desmartbpm.entity.DhStep;
import com.desmart.desmartbpm.enums.DhStepType;
import com.desmart.desmartbpm.service.BpmFormFieldService;
import com.desmart.desmartportal.dao.DhProcessInstanceMapper;
import com.desmart.desmartportal.entity.DhProcessInstance;
import com.desmart.desmartportal.service.DhProcessInstanceService;
/**
 * 从主流程的流程实例中获取当前表单字段名对应的所有数据
 * @author lys
 *
 */
public class DhProcessGetAllDataByMainOnFirstMeta extends DhOneTimeJavaClassTrigger {

	@Override
	public void doOneTime(WebApplicationContext ac, String insUid, JSONObject jsonObject, DhStep dhStep) {
		// 获得bean
				DhProcessInstanceService dhProcessInstanceService = ac.getBean(DhProcessInstanceService.class);
				DhProcessInstanceMapper dhProcessInstanceMapper = ac.getBean(DhProcessInstanceMapper.class);
				BpmFormFieldService bpmFormFieldService = ac.getBean(BpmFormFieldService.class);
				DhStepMapper dhStepMapper = ac.getBean(DhStepMapper.class);

				// 找到该环节配置中同步骤关键字的表单关键字
				DhStep selective1 = new DhStep(dhStep.getProAppId(), dhStep.getProUid(), dhStep.getProVerUid());
				selective1.setActivityBpdId(dhStep.getActivityBpdId());
				selective1.setStepBusinessKey(dhStep.getStepBusinessKey());
				selective1.setStepType(DhStepType.FORM.getCode());
				List<DhStep> checkList = dhStepMapper.listBySelective(selective1);
				if (checkList != null && checkList.size() == 1) {
					DhProcessInstance dhProcessInstance = dhProcessInstanceService.getByInsUid(insUid);
					JSONObject insDataJsonSource = JSON.parseObject(dhProcessInstance.getInsData());
					JSONObject formDataJsonSource = new JSONObject();// 保存当前环节表单数据
					ServerResponse<List<BpmFormField>> serverResponse = bpmFormFieldService
							.queryFieldByFormUid(checkList.get(0).getStepObjectUid());
					List<BpmFormField> formFieldList = serverResponse.getData();// 获得当前环节的所有表单字段集合
					int insId = dhProcessInstance.getInsId();
					
					// 得到目标实例并获得数据——主流程实例
					DhProcessInstance targetProcessInstance = dhProcessInstanceMapper.getMainProcessByInsId(insId);
					JSONObject insDataJson = JSON.parseObject(targetProcessInstance.getInsData());
					JSONObject formDataJson = insDataJson.getJSONObject("formData");

					Object key;
					for (Iterator<BpmFormField> var3 = formFieldList.iterator(); var3.hasNext();) {
						key = var3.next().getFldCodeName();
						if (formDataJson.get(key) != null) { // 目标的值为空的时候，不替换值到当前表单中去
							formDataJsonSource.put((String) key, formDataJson.get(key));
						}
					}

					insDataJsonSource.put("formData", formDataJsonSource);
					dhProcessInstance.setInsData(insDataJsonSource.toJSONString());
					dhProcessInstanceService.updateByPrimaryKeySelective(dhProcessInstance);// 将数据写入到当前流程实例中
				} else {
					throw new PlatformException("环节表单步骤配置出现异常");
				}
			}

}
