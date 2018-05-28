package bpm;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.desmart.desmartbpm.dao.DatRuleConditionMapper;
import com.desmart.desmartbpm.dao.DatRuleMapper;
import com.desmart.desmartbpm.entity.DatRule;
import com.desmart.desmartbpm.entity.DatRuleCondition;
import com.desmart.desmartbpm.service.DroolsEngineService;
import com.desmart.desmartbpm.util.FreeMarkUtil;

public class DroolsEngineServiceTest {
	private ApplicationContext applicationContext;
	/**
	 * @param datRuleMapper
	 * @param jsonObject
	 * @param datRule
	 * @return
	 */
	public Map<String, Object> jsonObjToExecuteDataTest(DatRuleConditionMapper datRuleConditionMapper, JSONObject jsonObject,
			DatRule datRule) {
		List<DatRuleCondition> list = datRuleConditionMapper.getDatruleConditionByRuleId(datRule.getRuleId());
		if(list!=null&&list.size()>0) {
		Map<String, Object> data = new HashMap<String, Object>();
		for (DatRuleCondition datRuleCondition : list) {
			System.out.println("key====" + datRuleCondition.getLeftValue());
			System.out.println(
					data.put(datRuleCondition.getLeftValue(), jsonObject.get(datRuleCondition.getLeftValue())));
		}
		return data;
		}else {
			return null;
		}
	}

	@Test
	public void test() throws Exception {
		applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		DatRuleMapper datRuleMapper = (DatRuleMapper) applicationContext.getBean("datRuleMapper");
		DatRuleConditionMapper datRuleConditionMapper = (DatRuleConditionMapper) applicationContext.getBean("datRuleConditionMapper");
		Set<String> ruleIds = new HashSet<String>();
		ruleIds.add("datrule:0221617b-e3e5-477c-8cc1-fab30cbd5632");
		ruleIds.add("datrule:2708112d-3e29-497a-a992-8777f4b89c58");
		List<DatRule> datRules = datRuleMapper.batchSelectDatRule(ruleIds);
		for (DatRule datRule : datRules) {
			JSONObject params = new JSONObject();
			params.put("ww", 2);
			params.put("name", "fdfdfd");
			Map<String, Object> data = new DroolsEngineServiceTest().executeTest(datRuleConditionMapper, params, datRule);
			System.out.println("执行规则检查：返回结果--" + data.get("state") + "--returnType--" + data.get("returnType"));
		}
	}

	public Map<String, Object> executeTest(DatRuleConditionMapper datRuleConditionMapper, JSONObject jsonObject, DatRule datRule)
			throws Exception {
		StatefulKnowledgeSession kSession = null;
		Map<String, Object> data = jsonObjToExecuteDataTest(datRuleConditionMapper, jsonObject, datRule);
		if (data != null) {
		try {
			// SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			System.setProperty("drools.dateformat", "yyyy-MM-dd HH:mm:ss");
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("ruleProcess", datRule.getRuleProcess());
			param.put("effective", datRule.getStartTime());
			param.put("expires", datRule.getEndTime());
			param.put("ruleName", datRule.getRuleName());
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
