package com.desmart.desmartportal.service;

import com.alibaba.fastjson.JSONArray;
import com.desmart.desmartbpm.entity.DhStep;

/**
 * 管理表单号
 */
public interface DhFormNoService {

    /**
     * 根据表单步骤和现有的fomNo，生成formNo的jsonArray对象
     * @param formStep
     * @return
     */
    JSONArray updateFormNoListJsonObject(DhStep formStep, JSONArray oldFormNoJSONArray);
}