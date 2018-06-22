package com.desmart.desmartbpm.service.impl;

import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSONObject;
import com.desmart.common.constant.ServerResponse;
import com.desmart.common.exception.PlatformException;
import com.desmart.common.util.FormDataUtil;
import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartbpm.common.EntityIdPrefix;
import com.desmart.desmartbpm.dao.BpmFormFieldMapper;
import com.desmart.desmartbpm.dao.BpmFormManageMapper;
import com.desmart.desmartbpm.dao.DhTriggerInterfaceMapper;
import com.desmart.desmartbpm.dao.DhTriggerMapper;
import com.desmart.desmartbpm.entity.BpmFormField;
import com.desmart.desmartbpm.entity.DhTrigger;
import com.desmart.desmartbpm.entity.DhTriggerInterface;
import com.desmart.desmartbpm.service.BpmFormFieldService;
import com.desmart.desmartbpm.service.DhTriggerService;
import com.desmart.desmartportal.dao.DhProcessInstanceMapper;
import com.desmart.desmartportal.entity.DhInstanceDocument;
import com.desmart.desmartportal.entity.DhProcessInstance;
import com.desmart.desmartsystem.service.DhInterfaceExecuteService;
import com.desmart.desmartsystem.util.Json;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rabbitmq.tools.json.JSONUtil;

/**
 * 触发器服务
 */
@Service
public class DhTriggerServiceImpl implements DhTriggerService {
    private static final Logger LOG = LoggerFactory.getLogger(DhTriggerServiceImpl.class);

    @Autowired
    private DhTriggerMapper dhTriggerMapper;
    
    @Autowired
	private DhTriggerInterfaceMapper dhTriggerInterfaceMapper;
    
    @Autowired
    private BpmFormManageMapper bpmFormManageMapper;
    
    @Autowired
    private BpmFormFieldMapper bpmFormFieldMapper;
    
    @Autowired
    private DhProcessInstanceMapper dhProcessInstanceMapper;

    @Autowired
    private DhInterfaceExecuteService dhInterfaceExecuteService;
    
    @Override
    public ServerResponse searchTrigger(DhTrigger dhTrigger, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        PageHelper.orderBy("update_time desc");
        List<DhTrigger> list = dhTriggerMapper.searchBySelective(dhTrigger);
        PageInfo pageInfo = new PageInfo(list);
        return ServerResponse.createBySuccess(pageInfo);
    }


	/* (non-Javadoc)
	 * @see com.desmart.desmartbpm.service.DhTriggerService#deleteTrigger(java.lang.String)
	 */
	@Override
	public int deleteTrigger(String triUid) {
		return dhTriggerMapper.delete(triUid);
	}


	/* (non-Javadoc)
	 * @see com.desmart.desmartbpm.service.DhTriggerService#saveTrigger(com.desmart.desmartbpm.entity.DhTrigger)
	 */
	@Override
	public int saveTrigger(DhTrigger dhTrigger) {
		// 触发器id
		dhTrigger.setTriUid(EntityIdPrefix.DH_TRIGGER + UUID.randomUUID().toString());
    	// 获得当前登录用户的身份
    	String creator = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
    	dhTrigger.setCreator(creator);
    	dhTrigger.setUpdator(creator);
    	// 保存数据
		return dhTriggerMapper.save(dhTrigger);
	}

	@Override
	public void invokeTrigger(WebApplicationContext wac, String insUid, String triUid) throws Exception{
		DhTrigger dhTrigger = dhTriggerMapper.getByPrimaryKey(triUid);
		if ("javaclass".equals(dhTrigger.getTriType())) {
			Class<?> clz = Class.forName(dhTrigger.getTriWebbot());
			Object obj = clz.newInstance();
			JSONObject jb = JSONObject.parseObject(dhTrigger.getTriParam());
			Method md = obj.getClass().getDeclaredMethod("execute",
					new Class[] { WebApplicationContext.class, String.class, org.json.JSONObject.class });
			md.invoke(obj, new Object[] { wac, insUid, jb });
		}else if("interface".equals(dhTrigger.getTriType())) {
			transferInterface(insUid, triUid, dhTrigger);
		}
	}

	/**
	 * 调用接口传递参数并接收返回值
	 */
	private void transferInterface(String insUid, String triUid, DhTrigger dhTrigger) throws Exception {
		String insData = dhProcessInstanceMapper.selectByPrimaryKey(insUid).getInsData();
		JSONObject formDataObj = JSONObject.parseObject(insData).getJSONObject("formData");
		List<DhTriggerInterface> dhTriggerInterfaceList = dhTriggerInterfaceMapper.queryTriIntByTriUidAndType(triUid,
				"inputParameter");
		String intUid = dhTrigger.getTriWebbot();
		String inputParameter = "\"inputParameter\":{";
		for (int i = 0; i < dhTriggerInterfaceList.size(); i++) {
			DhTriggerInterface triInt = dhTriggerInterfaceList.get(i);
			String fieldCodeName = triInt.getFldCodeName();
			String paramName = triInt.getParaName();
			BpmFormField formField = bpmFormFieldMapper.queryFieldByFldUidAndCodeName(triInt.getDynUid(),
					fieldCodeName);
			if (null == formField) {
				List<String> publicFormUidList = bpmFormManageMapper.queryFormReleByFormUid(triInt.getDynUid());
				for (String publicFormUid : publicFormUidList) {
					formField = bpmFormFieldMapper.queryFieldByFldUidAndCodeName(publicFormUid, fieldCodeName);
					if (null != formField) {
						break;
					}
				}
			}
			if ("object".equals(formField.getFldType())) {
				/*List<BpmFormField> tableFieldList = bpmFormFieldMapper.queryFieldByTableFldUid(formField.getFldUid());
				for (int j = 0; j < tableFieldList.size(); j++) {
					BpmFormField tableField = tableFieldList.get(i);

				}*/
			} else {
				String fieldValue = FormDataUtil.getStringValue(fieldCodeName,formDataObj);
				if (null == fieldValue) {
					fieldValue = "";
				}
				inputParameter += "\"" + paramName + "\":\"" + fieldValue + "\"";
				if (i != dhTriggerInterfaceList.size() - 1) {
					inputParameter += ",";
				}
			}
		}
		inputParameter += "}";
		String paramJson = "{\"intUid\":\"" + intUid + "\"," + inputParameter + "}";
		JSONObject paramObj = JSONObject.parseObject(paramJson);
		Json json = dhInterfaceExecuteService.interfaceSchedule(paramObj);
		handleInterfaceData(json, insUid, triUid);
	}
	
	/**
	 * 处理接口json返回值更新对应的formData
	 */
	private void handleInterfaceData(Json json,String insUid, String triUid){
		if(!json.isSuccess()) {
			throw new PlatformException(json.getMsg());
		}
		String jsonStr = json.getMsg();
		JSONObject jsonObj = JSONObject.parseObject(jsonStr);
		List<DhTriggerInterface> dhTriggerInterfaceList = dhTriggerInterfaceMapper.queryTriIntByTriUidAndType(triUid,"outputParameter");
		String formDataStr = "{";
		for(int i=0;i<dhTriggerInterfaceList.size();i++) {
			DhTriggerInterface triInt = dhTriggerInterfaceList.get(i);
			String paramName = triInt.getParaName();
			String returnValue = jsonObj.getString(paramName);
			String fieldCodeName = triInt.getFldCodeName();
			BpmFormField formField = bpmFormFieldMapper.queryFieldByFldUidAndCodeName(triInt.getDynUid(),
					fieldCodeName);
			if (null == formField) {
				List<String> publicFormUidList = bpmFormManageMapper.queryFormReleByFormUid(triInt.getDynUid());
				for (String publicFormUid : publicFormUidList) {
					formField = bpmFormFieldMapper.queryFieldByFldUidAndCodeName(publicFormUid, fieldCodeName);
					if (null != formField) {
						break;
					}
				}
			}
			if ("object".equals(formField.getFldType())) {
				/*List<BpmFormField> tableFieldList = bpmFormFieldMapper.queryFieldByTableFldUid(formField.getFldUid());
				for (int j = 0; j < tableFieldList.size(); j++) {
					BpmFormField tableField = tableFieldList.get(i);

				}*/
			} else {
				if(null == returnValue) {
					returnValue = "";
				}
				formDataStr += "\""+fieldCodeName+"\":{\"value\":\""+returnValue+"\"}";
				if (i != dhTriggerInterfaceList.size() - 1) {
					formDataStr += ",";
				}
			}
		}//end for
		formDataStr += "}";
		DhProcessInstance dhProcessInstance = dhProcessInstanceMapper.selectByPrimaryKey(insUid);
		String insData = dhProcessInstance.getInsData();
		JSONObject oldObj = JSONObject.parseObject(insData).getJSONObject("formData");
		JSONObject formDataObj = FormDataUtil.formDataCombine(JSONObject.parseObject(formDataStr), oldObj);
		JSONObject insDataObj = JSONObject.parseObject(insData);
		insDataObj.put("formData", formDataObj);
		String newInsData = insDataObj.toJSONString();
		dhProcessInstance.setInsData(newInsData);
		dhProcessInstanceMapper.updateByPrimaryKeySelective(dhProcessInstance);
	}

	@Override
	public ServerResponse<List<String>> invokeChooseUserTrigger(WebApplicationContext wac, String insUid, String triUid){
		DhTrigger dhTrigger = dhTriggerMapper.getByPrimaryKey(triUid);
		if ("javaclass".equals(dhTrigger.getTriType())) {
			try {
				Class<?> clz = Class.forName(dhTrigger.getTriWebbot());
				Object obj = clz.newInstance();
				JSONObject jb = null;
					try {
						if (dhTrigger.getTriParam()!=null) {
							jb = JSONObject.parseObject(dhTrigger.getTriParam());
						}
						Method md = obj.getClass().getDeclaredMethod("execute", 
								new Class []{WebApplicationContext.class, String.class,
										JSONObject.class});
						return ServerResponse.createBySuccess((List<String>)md.invoke(obj, new Object[]{wac, insUid, jb}));
					} catch (Exception e) {
						// TODO: handle exception
						return ServerResponse.createByErrorMessage("触发器执行异常，"+e.getMessage());
					}
				
			} catch (Exception e) {
				e.printStackTrace();
				return ServerResponse.createByErrorMessage("触发器调用异常，"+e.getMessage());
			}
		}
		return ServerResponse.createByErrorMessage("触发器调用异常，该触发器目前只开放javaClass调用");
	}
	
}