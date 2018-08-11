package com.desmart.desmartbpm.reflect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.desmart.common.constant.ServerResponse;
import com.desmart.common.exception.PlatformException;
import com.desmart.desmartbpm.entity.DatInsData;
import com.desmart.desmartbpm.entity.DhStep;
import com.desmart.desmartportal.dao.DhProcessInstanceMapper;
import com.desmart.desmartportal.entity.DatLaunchProcessResult;
import com.desmart.desmartportal.entity.DhProcessInstance;
import com.desmart.desmartportal.service.DhProcessInstanceService;
import org.springframework.web.context.WebApplicationContext;

/**
 * 发起从用印流程合同备案流程
 */
public class LaunchConstractRegisterProcess extends DhOneTimeJavaClassTrigger {

    @Override
    public void doOneTime(WebApplicationContext ac, String insUid, JSONObject jsonObject, DhStep dhStep) {
        // 获得Bean
        DhProcessInstanceService dhProcessInstanceService = ac.getBean(DhProcessInstanceService.class);
        DhProcessInstanceMapper dhProcessInstanceMapper = ac.getBean(DhProcessInstanceMapper.class);

        // 获得用印申请流程信息
        DhProcessInstance currProcessInstance = dhProcessInstanceMapper.selectByPrimaryKey(insUid);

        DhProcessInstance lauchTemplate = new DhProcessInstance();
        lauchTemplate.setProAppId("2066.16a6eec4-b3b5-4211-986c-31a94841e288");
        lauchTemplate.setProUid("25.f12891f9-5734-4c07-be13-57d6cfc7e423");
        lauchTemplate.setInsInitUser(currProcessInstance.getInsInitUser());
        lauchTemplate.setDepartNo(currProcessInstance.getDepartNo());
        lauchTemplate.setCompanyNumber(currProcessInstance.getCompanyNumber());
        lauchTemplate.setInsTitle(currProcessInstance.getInsTitle() + "-合同备案");
        lauchTemplate.setInsBusinessKey(currProcessInstance.getInsBusinessKey());

        ServerResponse<DatLaunchProcessResult> response = dhProcessInstanceService.launchProcess(lauchTemplate);
        if (!response.isSuccess()) {
            throw new PlatformException("触发器发起流程失败，" + response.getMsg());
        }
        DatLaunchProcessResult launchProcessResult = response.getData();
        // 将用印里的formData数据带入备案流程
        String insDataStrSource = currProcessInstance.getInsData();
        DatInsData datInsDataSource = JSONObject.parseObject(insDataStrSource,  new TypeReference<DatInsData>(){});
        DhProcessInstance processContainsFirstTask = launchProcessResult.getProcessContainsFirstTask();
        String insDataTarget = processContainsFirstTask.getInsData();
        DatInsData datInsDataTarget = JSONObject.parseObject(insDataTarget,  new TypeReference<DatInsData>(){});
        datInsDataTarget.setFormData(datInsDataSource.getFormData()); // formData设置为用印的formData
        String finalInsData = JSON.toJSONString(datInsDataTarget);

        // 更新新流程数据
        DhProcessInstance instanceSelective = new DhProcessInstance();
        instanceSelective.setInsUid(processContainsFirstTask.getInsUid());
        instanceSelective.setInsData(finalInsData);
        dhProcessInstanceMapper.updateByPrimaryKeySelective(instanceSelective);
    }


}