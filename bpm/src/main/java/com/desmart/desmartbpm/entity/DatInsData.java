package com.desmart.desmartbpm.entity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * 流程实例的insData数据
 */
public class DatInsData {

    private DatProcessData processData;
    private JSONArray formNoList;
    private JSONObject formData;

    public DatProcessData getProcessData() {
        return processData;
    }

    public void setProcessData(DatProcessData processData) {
        this.processData = processData;
    }

    public JSONArray getFormNoList() {
        return formNoList;
    }

    public void setFormNoList(JSONArray formNoList) {
        this.formNoList = formNoList;
    }

    public JSONObject getFormData() {
        return formData;
    }

    public void setFormData(JSONObject formData) {
        this.formData = formData;
    }
}