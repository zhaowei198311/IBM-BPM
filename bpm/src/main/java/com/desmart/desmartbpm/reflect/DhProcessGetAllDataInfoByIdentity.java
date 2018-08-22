package com.desmart.desmartbpm.reflect;

import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.desmart.common.exception.PlatformException;
import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartbpm.dao.DhDataExchangeMapper;
import com.desmart.desmartbpm.entity.DhStep;
import com.desmart.desmartportal.entity.DhProcessInstance;
import com.desmart.desmartportal.service.DhProcessInstanceService;

/**
 * 通过标识符数组从目标流程的流程实例中获取数据 
 * 参数格式 
 * { "data":[ { "identity": "第一个标识","exchange": [ {"isOverrides":"TRUE" //是否覆盖当前数据TRUE/FALSE } ] } ]}
 * 
 * @author lys
 *
 */
public class DhProcessGetAllDataInfoByIdentity extends DhOneTimeJavaClassTrigger {
	
	@Override
	public void doOneTime(WebApplicationContext ac, String insUid, JSONObject jsonObject, DhStep dhStep) {
		// 获得bean
				DhProcessInstanceService dhProcessInstanceService = ac.getBean(DhProcessInstanceService.class);
				DhDataExchangeMapper dataExchangeMapper = ac.getBean(DhDataExchangeMapper.class);

				DhProcessInstance dhProcessInstance = dhProcessInstanceService.getByInsUid(insUid);
				JSONObject insDataJsonSource = JSON.parseObject(dhProcessInstance.getInsData());
				JSONObject formDataJsonSource = insDataJsonSource.getJSONObject("formData");
				int insId = dhProcessInstance.getInsId();
				// 获得标识符和数据字段映射的数组
				JSONArray jsonArray = jsonObject.getJSONArray("data");
				for (int i = 0; i < jsonArray.size(); i++) {

					JSONObject dataObject = jsonArray.getJSONObject(i);
					String identity = dataObject.getString("identity");
					JSONArray exchangeArr = dataObject.getJSONArray("exchange");

					// 根据设置的标识符和流程实例编号，获得需要的流程实例主键
					String targetInsUid = dataExchangeMapper.getInsUidByInsIdAndIdentity(insId, identity);
					if (targetInsUid == null) {
						throw new PlatformException("获取数据失败，通过标识符找目标实例失败：identity:"+identity+",insId:"+insId);
					}
					for (int j = 0; j < exchangeArr.size(); j++) {
						String isOverrides = exchangeArr.getJSONObject(j).getString("isOverrides");

						// 得到目标实例并获得数据
						DhProcessInstance targetProcessInstance = dhProcessInstanceService.getByInsUid(targetInsUid);
						JSONObject insDataJson = JSON.parseObject(targetProcessInstance.getInsData());
						JSONObject formDataJson = insDataJson.getJSONObject("formData");

						if (Const.Boolean.TRUE.equals(isOverrides)) {
							Object key;
							for (Iterator var3 = formDataJsonSource.keySet().iterator(); var3.hasNext();) {
								key = var3.next();
								if (formDataJson.get(key) != null) { // 目标的值为空的时候，不替换值到当前表单中去
									formDataJsonSource.put((String) key, formDataJson.get(key));
								}
							}
						} else {
							Object key;
							for (Iterator var3 = formDataJsonSource.keySet().iterator(); var3.hasNext();) {
								key = var3.next();
								if (formDataJson.get(key) != null) { // 目标的值为空的时候，不替换值到当前表单中去
									if (formDataJsonSource.get(key) == null // 当前的表单数据有值时则不更新值
											|| StringUtils.isBlank(formDataJsonSource.get(key).toString())) {
										formDataJsonSource.put((String) key, formDataJson.get(key));
									}
								}
							}
						}
					}
				}
				insDataJsonSource.put("formData", formDataJsonSource);
				dhProcessInstance.setInsData(insDataJsonSource.toJSONString());
				dhProcessInstanceService.updateByPrimaryKeySelective(dhProcessInstance);// 将数据写入到当前流程实例中
			
	}

}
