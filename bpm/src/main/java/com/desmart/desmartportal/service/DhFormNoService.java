package com.desmart.desmartportal.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.desmart.desmartbpm.entity.DhStep;

/**
 * 管理表单号
 */
public interface DhFormNoService {

    /**
     * 生成formNo的jsonArray对象
     * @param formStep
     * @return
     */
    JSONArray generateFormNoJsonArray(DhStep formStep, JSONArray oldFormNoJSONArray);
}