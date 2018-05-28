package com.desmart.desmartbpm.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.desmartbpm.dao.DatRuleConditionMapper;
import com.desmart.desmartbpm.entity.DatRule;
import com.desmart.desmartbpm.entity.DatRuleCondition;
import com.desmart.desmartbpm.util.FreeMarkUtil;
import com.desmart.desmartportal.util.DateUtil;

@Service
public class DroolsEngineService {
	@Autowired
	private DatRuleConditionMapper datRuleConditionMapper;
	
	public DroolsEngineService() {
	}

	public Map<String, Object> jsonObjToExecuteData(JSONObject jsonObject, DatRule datRule) {
		List<DatRuleCondition> list = datRuleConditionMapper.getDatruleConditionByRuleId(datRule.getRuleId());
		if (list != null && list.size() > 0) {
			Map<String, Object> data = new HashMap<String, Object>();
			for (DatRuleCondition datRuleCondition : list) {
				data.put(datRuleCondition.getLeftValue(), jsonObject.get(datRuleCondition.getLeftValue()));
			}
			return data;
		} else {
			return null;
		}
	}

	/**
	 * @param jsonObject(要验证的规则条件键值对，{leftValue:""}
	 * @param datRule
	 * @return  规则执行结果:{state=true, returnType=text}
	 * @throws Exception
	 */
	public Map<String, Object> execute(JSONObject jsonObject, DatRule datRule) throws Exception {
		StatefulKnowledgeSession kSession = null;
		Map<String, Object> data = jsonObjToExecuteData(jsonObject, datRule);
		if (data != null) {
			try {
				// SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				System.setProperty("drools.dateformat", "yyyy-MM-dd HH:mm:ss");
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("ruleProcess", datRule.getRuleProcess());
				param.put("effective", datRule.getStartTime());
				/*param.put("expires", datRule.getEndTime());*/
				param.put("expires", DateUtil.getDateAddDay(datRule.getStartTime(), 20));
				param.put("ruleName", "ruleName");
				String ruleContent = (new FreeMarkUtil()).getHtml("DroolsTemplate.ftl", param);
				System.out.println("执行的规则:" + ruleContent);
				KnowledgeBuilder kb = KnowledgeBuilderFactory.newKnowledgeBuilder();
				kb.add(ResourceFactory.newByteArrayResource(ruleContent.getBytes("utf-8")), ResourceType.DRL);
				KnowledgeBuilderErrors errors = kb.getErrors();
				for (KnowledgeBuilderError error : errors) {
					new RuntimeException("Drools规则编译异常" + error.getMessage());
				}

				KnowledgeBase kBase = KnowledgeBaseFactory.newKnowledgeBase();
				kBase.addKnowledgePackages(kb.getKnowledgePackages());
				kSession = kBase.newStatefulKnowledgeSession();
				kSession.insert(data);
				kSession.fireAllRules();
				if (data.size() == 1) {
					Boolean state = (Boolean) data.get("state");
					if (state != null && state) {
						data.put("returnType", datRule.getReturnType());
					} else {
						data.put("state", false);
					}
				} else {
					data.clear();
					data.put("state", false);
				}

				System.out.println("规则执行结果:" + data);
			} catch (Exception var14) {
				data.clear();
				data.put("state", false);
				new RuntimeException("Drools规则执行异常", var14);
			} finally {
				if (kSession != null) {
					kSession.dispose();
				}

			}

			return data;
		} else {
			Map<String, Object> data1 = new HashMap<String, Object>();
			data1.put("state", false);
			return data1;
		}
	}

	
}
