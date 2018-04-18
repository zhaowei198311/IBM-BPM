package com.desmart.desmartbpm.common;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
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
import org.junit.Test;

import com.desmart.desmartbpm.util.FreeMarkUtil;

public class RuleTest {
	
	@Test
	public void test1() throws Exception {
        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.setProperty("drools.dateformat", "yyyy-MM-dd HH:mm:ss");
        String rule = "import java.util.Map; \r\nimport java.util.HashMap;\r\n rule \"testrule\"\r\ndate-effective '2017-08-10 00:00:00'\r\ndate-expires '2018-08-10 23:59:59'\r\nwhen\r\n$m : Map()\r\nMap ( (this['bizpanelId'] == 'bizpanelId:a55e7f9f-471e-4740-b328-b5a003e6d1ff'    &&  this['districtName'] == '苏州'    &&  this['empNum'] > 2.3  )  )\r\nthen\r\n$m.clear();\r\n$m.put('state',true);\r\nend\r\n";
        System.out.println(rule);
        Map<String, Object> tmp = new HashMap();
        
        tmp.put("bizpanelId", "bizpanelId:a55e7f9f-471e-4740-b328-b5a003e6d1ff");
        tmp.put("districtName", "苏州");
        tmp.put("empNum", "300");
        boolean result = false;
        
        // 创建规则生成器
        KnowledgeBuilder knowlegeBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        // 添加规则
        knowlegeBuilder.add(ResourceFactory.newByteArrayResource(rule.getBytes("utf-8")), ResourceType.DRL);
        // 检查规则有没有异常
        KnowledgeBuilderErrors errors = knowlegeBuilder.getErrors();
        Iterator var9 = errors.iterator();

        while (var9.hasNext()) {
            KnowledgeBuilderError error = (KnowledgeBuilderError)var9.next();
            new RuntimeException("Drools规则编译异常" + error.getMessage());
        }

        // KnowledgeBase 是 Drools 提供的用来收集应用当中知识（knowledge）定义的知识库对象
        KnowledgeBase kBase = KnowledgeBaseFactory.newKnowledgeBase();
        kBase.addKnowledgePackages(knowlegeBuilder.getKnowledgePackages());
        
        StatefulKnowledgeSession kSession = null;
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
	
	@Test
	public void test2() {
        StatefulKnowledgeSession kSession = null;
        Map<String, Object> data = new HashMap<>();
        data.put("bizpanelId", "bizpanelId:a55e7f9f-471e-4740-b328-b5a003e6d1ff");
        data.put("districtName", "苏州");
        data.put("empNum", "300");
        
        
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            System.setProperty("drools.dateformat", "yyyy-MM-dd HH:mm:ss");
            Map<String, Object> param = new HashMap<>();
            param.put("ruleProcess", "Map ( (this['bizpanelId'] == 'bizpanelId:a55e7f9f-471e-4740-b328-b5a003e6d1ff'    &&  this['districtName'] == '苏州'    &&  this['empNum'] > 2.3  )  )");
            param.put("effective",   "2001-11-1 00:00:00");
            param.put("expires", "2019-12-12 23:59:59");
            param.put("ruleName", "ruleName");
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
                    data.put("returnType", "PARAMS");
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
		
		
	}
	
	
	
}
