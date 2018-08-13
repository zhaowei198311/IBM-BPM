package com.desmart.desmartbpm.reflect;

import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSONObject;
import com.desmart.desmartbpm.entity.DhStep;

/**
 * 新开店营业通知流程完成后执行该触发器记录新开店信息
 * @author zbw
 *
 */
public class DhSaveNewStoreInfo implements DhJavaClassTriggerTemplate{

	@Override
	public void execute(WebApplicationContext ac, String insUid, JSONObject jsonObject, DhStep dhStep) {
		//获取表单内容，封装并保存
		//TODO
	}

}