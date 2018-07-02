package com.desmart.desmartbpm.util;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.desmart.desmartbpm.entity.DatRule;


/**
 * 校验网关规则的工具类
 */
public class DoolsUtil {

	private static final Logger LOG = LoggerFactory.getLogger(DoolsUtil.class);
	
	/**
	 * 根据网关规则获得结果
	 * @param data  参与校验的参数
	 * @param datRule  规则
	 * @return  map key="state" value="true/false"
	 * @throws Exception
	 */
	public static Map<String, Object> execute(Map<String, Object> data, DatRule datRule) throws Exception {
        StatefulKnowledgeSession kSession = null;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            System.setProperty("drools.dateformat", "yyyy-MM-dd HH:mm:ss");
            Map<String, Object> param = new HashMap<>();
            param.put("ruleProcess", datRule.getRuleProcess());
            param.put("effective", formatter.format(datRule.getStartTime()) + " 00:00:00");
            param.put("expires", formatter.format(datRule.getEndTime()) + " 23:59:59");
            param.put("ruleName", datRule.getRuleName());
            String ruleContent = (new FreeMarkUtil()).getHtml("DroolsTemplate.ftl", param);
            LOG.debug("执行的规则:" + ruleContent);
            KnowledgeBuilder kb = KnowledgeBuilderFactory.newKnowledgeBuilder();
            kb.add(ResourceFactory.newByteArrayResource(ruleContent.getBytes("utf-8")), ResourceType.DRL);
            KnowledgeBuilderErrors errors = kb.getErrors();
            Iterator var10 = errors.iterator();

            while(var10.hasNext()) {
                KnowledgeBuilderError error = (KnowledgeBuilderError)var10.next();
                LOG.error("Drools规则编译异常: {}" + error.getMessage());
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

            LOG.debug("规则执行结果:" + data);
        } catch (Exception var14) {
            data.clear();
            data.put("state", false);
            LOG.error("Drools规则执行异常: {}", var14);
        } finally {
            if (kSession != null) {
                kSession.dispose();
            }
        }

        return data;
    }
	
}

