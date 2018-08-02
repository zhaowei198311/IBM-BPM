package com.desmart.desmartbpm.reflect;

import java.util.Iterator;
import java.util.List;

import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.desmart.common.constant.ServerResponse;
import com.desmart.common.exception.PlatformException;
import com.desmart.desmartbpm.dao.DhDataExchangeMapper;
import com.desmart.desmartbpm.dao.DhStepMapper;
import com.desmart.desmartbpm.entity.BpmFormField;
import com.desmart.desmartbpm.entity.DhStep;
import com.desmart.desmartbpm.enums.DhStepType;
import com.desmart.desmartbpm.service.BpmFormFieldService;
import com.desmart.desmartportal.entity.DhProcessInstance;
import com.desmart.desmartportal.service.DhProcessInstanceService;

/**
 * 从父流程的流程实例中获取当前表单字段名对应的所有数据 
 * 
 * @author lys
 *
 */
public class DhProcessGetAllDataByParentOnFirstMeta implements DhJavaClassTriggerTemplate {

	@Override
	public void execute(WebApplicationContext ac, String insUid, JSONObject jsonObject, DhStep dhStep) {
		// 获得bean
		DhProcessInstanceService dhProcessInstanceService = ac.getBean(DhProcessInstanceService.class);
		BpmFormFieldService bpmFormFieldService = ac.getBean(BpmFormFieldService.class);
		DhDataExchangeMapper dataExchangeMapper = ac.getBean(DhDataExchangeMapper.class);
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
			/*// 获得标识符和数据字段映射的数组
			JSONArray jsonArray = jsonObject.getJSONArray("data");
			for (int i = 0; i < jsonArray.size(); i++) {
*/
				/*JSONObject dataObject = jsonArray.getJSONObject(i);
				String identity = dataObject.getString("identity");

				// 根据设置的标识符和流程实例编号，获得需要的流程实例主键
				String targetInsUid = dataExchangeMapper.getInsUidByInsIdAndIdentity(insId, identity);
				if (targetInsUid == null) {
					throw new PlatformException("获取数据失败");
				}*/

				// 得到目标实例并获得数据
				//DhProcessInstance targetProcessInstance = dhProcessInstanceService.getByInsUid(targetInsUid);
				String parentInsUid = dhProcessInstance.getInsParent();
	        	// 得到目标实例并获得数据——父流程实例
	        	DhProcessInstance targetProcessInstance = dhProcessInstanceService.getByInsUid(parentInsUid);
				JSONObject insDataJson = JSON.parseObject(targetProcessInstance.getInsData());
				JSONObject formDataJson = insDataJson.getJSONObject("formData");

				Object key;
				for (Iterator<BpmFormField> var3 = formFieldList.iterator(); var3.hasNext();) {
					key = var3.next().getFldCodeName();
					if (formDataJson.get(key) != null) { // 目标的值为空的时候，不替换值到当前表单中去
						formDataJsonSource.put((String) key, formDataJson.get(key));
					}
				}

			/*}*/
			insDataJsonSource.put("formData", formDataJsonSource);
			dhProcessInstance.setInsData(insDataJsonSource.toJSONString());
			dhProcessInstanceService.updateByPrimaryKeySelective(dhProcessInstance);// 将数据写入到当前流程实例中
		} else {
			throw new PlatformException("环节表单步骤配置出现异常");
		}

	}

}
