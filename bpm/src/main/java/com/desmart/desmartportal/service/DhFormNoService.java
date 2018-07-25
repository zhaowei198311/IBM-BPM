package com.desmart.desmartportal.service;

import com.alibaba.fastjson.JSONArray;
import com.desmart.desmartbpm.entity.BpmForm;

/**
 * 管理表单号
 */
public interface DhFormNoService {

    /**
     * 根据表单步骤和现有的fomNoList对象，生成formNo的jsonArray对象
     * @param bpmForm
     * @return
     */
    JSONArray updateFormNoListJsonObject(BpmForm bpmForm, JSONArray oldFormNoJSONArray);

    String findFormNoByFormUid(String formUid, JSONArray formNoListJson);
}