package com.desmart.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.desmart.desmartportal.entity.DhDrafts;
import com.desmart.desmartportal.entity.DhProcessInstance;
import org.apache.commons.lang3.StringUtils;

import java.io.PrintStream;

/**
 * 对流程实例中的insData进行操作的工具类
 */
public class InsDataUtil {

    /**
     * 将未发起的草稿记录中的formData数据与流程实例中的formData数据汇总
     * @param drafts  草稿对象
     * @param draftInstance  流程实例（未发起的）
     * @return  装配好insData的流程实例对象
     */
    public static DhProcessInstance mergeDraftDataForStartProcess(DhDrafts drafts, DhProcessInstance draftInstance) {
        JSONObject insDataPro = JSON.parseObject(draftInstance.getInsData());
        JSONObject insDataDraft = JSON.parseObject(drafts.getDfsData());
        JSONObject formDataPro = insDataPro.getJSONObject("formData");
        JSONObject formDataDraft = insDataDraft.getJSONObject("formData");
        JSONObject mergedFormData = FormDataUtil.formDataCombine(formDataDraft, formDataPro);
        insDataPro.put("formData", mergedFormData);

        JSONObject processDataDraft = insDataDraft.getJSONObject("processData");
        JSONObject processDataPro = insDataPro.getJSONObject("processData");
        if (StringUtils.isNotBlank(processDataDraft.getString("departNo"))) {
            processDataPro.put("departNo", processDataDraft.getString("departNo"));
        }
        if (StringUtils.isNotBlank(processDataDraft.getString("companyNumber"))) {
            processDataPro.put("companyNumber", processDataDraft.getString("companyNumber"));
        }
        draftInstance.setInsData(insDataPro.toJSONString());
        return draftInstance;
    }

    /**
     * 从流程实例中获得下个流程的流程关键字
     * 当找不到时返回 default
     * @param dhProcessInstance
     * @return
     */
    public static String getBusinessKeyOfNextProcess(DhProcessInstance dhProcessInstance) {
        String insData = dhProcessInstance.getInsData();
        JSONObject insDataJson = JSON.parseObject(insData);
        JSONObject processDataJson = insDataJson.getJSONObject("processData");
        if (processDataJson != null) {
            String nextBusinessKey = processDataJson.getString("nextBusinessKey");
            if (StringUtils.isBlank(nextBusinessKey)) {
                return dhProcessInstance.getInsBusinessKey();
            } else {
                return nextBusinessKey;
            }
        } else {
            return dhProcessInstance.getInsBusinessKey();
        }
    }


}