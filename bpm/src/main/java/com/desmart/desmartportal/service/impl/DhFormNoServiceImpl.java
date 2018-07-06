package com.desmart.desmartportal.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.desmart.desmartbpm.entity.DhStep;
import com.desmart.desmartportal.service.DhFormNoService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class DhFormNoServiceImpl implements DhFormNoService {
    private Logger log = Logger.getLogger(DhFormNoServiceImpl.class);

    @Override
    public JSONArray generateFormNoJsonArray(DhStep formStep, JSONArray oldFormNoJSONArray) {
        JSONArray result = null;
        String formUid = formStep.getStepObjectUid();

        boolean exist = false;
        if (oldFormNoJSONArray == null) {

        }
        for (int i = 0; i < oldFormNoJSONArray.size(); i++) {
            JSONObject jsonObject = oldFormNoJSONArray.getJSONObject(i);
            if (formUid.equals(jsonObject.getString("formUid"))) {
                exist = true;
                break;
            }
        }
        if (exist) {
            return result;
        }
        JSONObject newObject = new JSONObject();
        newObject.put("", "");
        oldFormNoJSONArray.add(newObject);

        return null;
    }


    /**
     * 生成一个表单号
     * @return
     */
    private String generateFormNo() {


        return null;
    }

    public static void main(String[] args){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        System.out.println(sdf.format(new Date()));
    }
}