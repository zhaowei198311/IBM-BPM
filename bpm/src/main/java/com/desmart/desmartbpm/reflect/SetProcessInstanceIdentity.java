package com.desmart.desmartbpm.reflect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.desmart.common.exception.PlatformException;
import com.desmart.desmartbpm.dao.BpmActivityMetaMapper;
import com.desmart.desmartbpm.dao.DhDataExchangeMapper;
import com.desmart.desmartbpm.entity.DhDataExchange;
import com.desmart.desmartportal.entity.DhProcessInstance;
import com.desmart.desmartportal.service.DhProcessInstanceService;
import org.springframework.web.context.WebApplicationContext;

/**
 * 设置主流程范围内交互数据用的标识
 */
public class SetProcessInstanceIdentity implements DhJavaClassTriggerTemplate {

	@Override
	public void execute(WebApplicationContext ac, String insUid, JSONObject param) {
	    String identity = null;
        if (param == null || (identity = param.getString("identity")) == null) {
            throw new PlatformException("缺少必要的参数，调用失败");
        }

        if (identity.length() > 33) {
            throw new PlatformException("标识符过长，调用失败");
        }

        // 获得bean
	    DhProcessInstanceService dhProcessInstanceService = ac.getBean(DhProcessInstanceService.class);
        DhDataExchangeMapper dataExchangeMapper = ac.getBean(DhDataExchangeMapper.class);

        DhProcessInstance dhProcessInstance = dhProcessInstanceService.getByInsUid(insUid);
        int insId = dhProcessInstance.getInsId();

        // 查看是否已经有此记录
        String identityInDb = dataExchangeMapper.getInsUidByInsIdAndIdentity(insId, identity);
        int count = 0;
        if (identityInDb == null) {
            // 插入记录
            count = dataExchangeMapper.insert(new DhDataExchange(insId, identity, insUid));
        } else {
            // 更新记录
            count = dataExchangeMapper.updateByInsUidByInsIdAndIdentity(new DhDataExchange(insId, identity, insUid));
        }

        if (count == 0) {
            throw new PlatformException("设置identity失败");
        }

    }

}
