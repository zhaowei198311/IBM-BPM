package com.desmart.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.desmart.desmartportal.entity.DhDrafts;
import com.desmart.desmartportal.entity.DhProcessInstance;

/**
 * 对流程实例中的insData进行操作的工具类
 */
public class InsDataUtil {

    public static DhProcessInstance mergeDraftDataForStartProcess(DhDrafts drafts, DhProcessInstance draftInstance) {
        JSONObject insDataPro = JSON.parseObject(draftInstance.getInsData());
        JSONObject insDataDraft = JSON.parseObject(drafts.getDfsData());
        JSONObject formDataPro = insDataPro.getJSONObject("formData");
        JSONObject formDataDraft = insDataDraft.getJSONObject("formData");
        JSONObject mergedFormData = FormDataUtil.formDataCombine(formDataDraft, formDataPro);
        insDataPro.put("formData", mergedFormData);

        insDataDraft.getJSONObject("processData");
        insDataPro.getJSONObject("processData");
        return null;
    }




}