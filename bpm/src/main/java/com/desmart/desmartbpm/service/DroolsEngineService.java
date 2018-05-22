package com.desmart.desmartbpm.service;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
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
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import com.desmart.desmartbpm.dao.DatRuleConditionMapper;
import com.desmart.desmartbpm.dao.DatRuleMapper;
import com.desmart.desmartbpm.entity.DatRule;
import com.desmart.desmartbpm.entity.DatRuleCondition;
import com.desmart.desmartbpm.util.FreeMarkUtil;
@Service
public class DroolsEngineService {
	   	@Autowired
	    private DatRuleConditionMapper datRuleConditionMapper;
	
	    public DroolsEngineService() {
	    }
	    
	    public Map<String, Object> jsonObjToExecuteData(JSONObject jsonObject, DatRule datRule){
	    	List<DatRuleCondition> list = datRuleConditionMapper.getDatruleConditionByRuleId(datRule.getRuleId());
	    	Map<String, Object> data = new HashMap<String,Object>();
	        for (DatRuleCondition datRuleCondition : list) {data.put(datRuleCondition.getLeftValue(), 
			    		  		jsonObject.get(datRuleCondition.getLeftValue()));
			}
	    	return data;
	    }

	    public Map<String, Object> execute(JSONObject jsonObject, DatRule datRule, boolean result) throws Exception {
	        StatefulKnowledgeSession kSession = null;
	        Map<String, Object> data =  jsonObjToExecuteData(jsonObject, datRule);
	        try {
	            //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	            System.setProperty("drools.dateformat", "yyyy-MM-dd HH:mm:ss");
	            Map<String, Object> param = new HashMap<String, Object>();
	            param.put("ruleProcess", datRule.getRuleProcess());
	            param.put("effective",datRule.getStartTime());
	            param.put("expires", datRule.getEndTime());
	            param.put("ruleName", datRule.getRuleName());
	            String ruleContent = (new FreeMarkUtil()).getHtml("DroolsTemplate.ftl", param);
	            System.out.println("执行的规则:" + ruleContent);
	            KnowledgeBuilder kb = KnowledgeBuilderFactory.newKnowledgeBuilder();
	            kb.add(ResourceFactory.newByteArrayResource(ruleContent.getBytes("utf-8")), ResourceType.DRL);
	            KnowledgeBuilderErrors errors = kb.getErrors();
	            Iterator var10 = errors.iterator();

	            while(var10.hasNext()) {
	                KnowledgeBuilderError error = (KnowledgeBuilderError)var10.next();
	                new RuntimeException("Drools规则编译异常" + error.getMessage());
	            }

	            KnowledgeBase kBase = KnowledgeBaseFactory.newKnowledgeBase();
	            kBase.addKnowledgePackages(kb.getKnowledgePackages());
	            kSession = kBase.newStatefulKnowledgeSession();
	            kSession.insert(data);
	            kSession.fireAllRules();
	            if (data.size() == 1) {
	                Boolean state = (Boolean)data.get("state");
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
	    } 
	    
	    /**
	     * 测试
	     * @param datRuleMapper
	     * @param jsonObject
	     * @param datRule
	     * @return
	     */
	    public Map<String, Object> jsonObjToExecuteDataTest(DatRuleMapper datRuleMapper,JSONObject jsonObject, DatRule datRule){
	    	List<DatRuleCondition> list = datRuleConditionMapper.getDatruleConditionByRuleId(datRule.getRuleId());
	    	Map<String, Object> data = new HashMap<String,Object>();
	        for (DatRuleCondition datRuleCondition : list) {
	        	System.out.println("key===="+datRuleCondition.getLeftValue());
			    System.out.println(data.put(datRuleCondition.getLeftValue(), 
			    		  		jsonObject.get(datRuleCondition.getLeftValue())) );
			}
	    	return data;
	    }
	    @Test
		public void test() throws Exception {
	    	// 获取业务逻辑类productService查询商品信息        
	    	ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");  
	    	DatRuleMapper datRuleMapper = (DatRuleMapper)applicationContext.getBean("datRuleMapper");
	    	List<DatRule> datRules = datRuleMapper.getPreRulesLikeRuleName("act_meta:88e7a64d-3056-421b-b804-ffb9fd454930");
	        for (DatRule datRule : datRules) {
				JSONObject params = new JSONObject();
				params.put("sa", 20);
				params.put("ss", "null");
				Map<String, Object> data = new DroolsEngineService().executeTest(
						datRuleMapper,params, datRule, true);
				System.out.println("执行规则检查：返回结果--"+data.get("state")
				+"--returnType--"+data.get("returnType"));
			}
	    }
	    
	    public Map<String, Object> executeTest(DatRuleMapper datRuleMapper,JSONObject jsonObject, DatRule datRule, boolean result) throws Exception {
	        StatefulKnowledgeSession kSession = null;
	        Map<String, Object> data =  jsonObjToExecuteDataTest(datRuleMapper,jsonObject, datRule);
	        try {
	            //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	            System.setProperty("drools.dateformat", "yyyy-MM-dd HH:mm:ss");
	            Map<String, Object> param = new HashMap<String, Object>();
	            param.put("ruleProcess", datRule.getRuleProcess());
	            param.put("effective",datRule.getStartTime());
	            param.put("expires", datRule.getEndTime());
	            param.put("ruleName", datRule.getRuleName());
	            String ruleContent = (new FreeMarkUtil()).getHtml("DroolsTemplate.ftl", param);
	            System.out.println("执行的规则:" + ruleContent);
	            KnowledgeBuilder kb = KnowledgeBuilderFactory.newKnowledgeBuilder();
	            kb.add(ResourceFactory.newByteArrayResource(ruleContent.getBytes("utf-8")), ResourceType.DRL);
	            KnowledgeBuilderErrors errors = kb.getErrors();
	            Iterator var10 = errors.iterator();

	            while(var10.hasNext()) {
	                KnowledgeBuilderError error = (KnowledgeBuilderError)var10.next();
	                new RuntimeException("Drools规则编译异常" + error.getMessage());
	            }

	            KnowledgeBase kBase = KnowledgeBaseFactory.newKnowledgeBase();
	            kBase.addKnowledgePackages(kb.getKnowledgePackages());
	            kSession = kBase.newStatefulKnowledgeSession();
	            kSession.insert(data);
	            kSession.fireAllRules();
	            if (data.size() == 1) {
	                Boolean state = (Boolean)data.get("state");
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
	    } 
	    

	    public static void main(String[] args) throws Exception {
	        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        System.setProperty("drools.dateformat", "yyyy-MM-dd HH:mm:ss");
	        String rule = "import java.util.Map; \r\nimport java.util.HashMap;\r\n rule \"testrule\"\r\ndate-effective '2017-08-10 00:00:00'\r\ndate-expires '2018-08-10 23:59:59'\r\nwhen\r\n$m : Map()\r\nMap ( (this['bizpanelId'] == 'bizpanelId:a55e7f9f-471e-4740-b328-b5a003e6d1ff'    &&  this['districtName'] == '苏州'    &&  this['empNum'] == 'empid:6079c376-19df-4528-9f93-246d1698a20a'  )  )\r\nthen\r\n$m.clear();\r\n$m.put('state',true);\r\nend\r\n";
	        System.out.println(rule);
	        Map<String, Object> tmp = new HashMap();
	        StatefulKnowledgeSession kSession = null;
	        tmp.put("bizpanelId", "bizpanelId:a55e7f9f-471e-4740-b328-b5a003e6d1ff");
	        tmp.put("districtName", "苏州");
	        tmp.put("empNum", "empid:6079c376-19df-4528-9f93-246d1698a20a");
	        boolean result = false;
	        KnowledgeBuilder kb = KnowledgeBuilderFactory.newKnowledgeBuilder();
	        kb.add(ResourceFactory.newByteArrayResource(rule.getBytes("utf-8")), ResourceType.DRL);
	        KnowledgeBuilderErrors errors = kb.getErrors();
	        Iterator var9 = errors.iterator();

	        while(var9.hasNext()) {
	            KnowledgeBuilderError error = (KnowledgeBuilderError)var9.next();
	            new RuntimeException("Drools规则编译异常" + error.getMessage());
	        }

	        KnowledgeBase kBase = KnowledgeBaseFactory.newKnowledgeBase();
	        kBase.addKnowledgePackages(kb.getKnowledgePackages());
	        kSession = kBase.newStatefulKnowledgeSession();
	        kSession.insert(tmp);
	        kSession.fireAllRules();
	        if (tmp.size() == 1) {
	            Boolean state = (Boolean)tmp.get("state");
	            if (state != null && state) {
	                tmp.put("returnType", "");
	            } else {
	                tmp.put("state", false);
	            }
	        } else {
	            tmp.clear();
	            tmp.put("state", false);
	        }

	        System.out.println("规则执行结果:" + tmp);
	    }
}

