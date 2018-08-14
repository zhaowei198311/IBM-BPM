package com.desmart.desmartbpm.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import com.desmart.desmartbpm.enums.DhTriggerType;
import com.desmart.desmartsystem.dao.DhInterfaceMapper;
import com.desmart.desmartsystem.entity.DhInterface;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.desmart.common.constant.ServerResponse;
import com.desmart.common.exception.PlatformException;
import com.desmart.common.util.FormDataUtil;
import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartbpm.common.EntityIdPrefix;
import com.desmart.desmartbpm.dao.BpmFormFieldMapper;
import com.desmart.desmartbpm.dao.BpmFormManageMapper;
import com.desmart.desmartbpm.dao.BpmFormRelePublicFormMapper;
import com.desmart.desmartbpm.dao.DhTriggerInterfaceMapper;
import com.desmart.desmartbpm.dao.DhTriggerMapper;
import com.desmart.desmartbpm.entity.BpmFormField;
import com.desmart.desmartbpm.entity.DhStep;
import com.desmart.desmartbpm.entity.DhTrigger;
import com.desmart.desmartbpm.entity.DhTriggerInterface;
import com.desmart.desmartbpm.service.DhTriggerService;
import com.desmart.desmartportal.dao.DhProcessInstanceMapper;
import com.desmart.desmartportal.entity.DhProcessInstance;
import com.desmart.desmartsystem.entity.DhInterfaceParameter;
import com.desmart.desmartsystem.service.DhInterfaceExecuteService;
import com.desmart.desmartsystem.service.DhInterfaceParameterService;
import com.desmart.desmartsystem.util.Json;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 触发器服务
 */
@Service
public class DhTriggerServiceImpl implements DhTriggerService {
    private static final Logger logger = LoggerFactory.getLogger(DhTriggerServiceImpl.class);

    @Autowired
    private DhTriggerMapper dhTriggerMapper;
    @Autowired
	private DhTriggerInterfaceMapper dhTriggerInterfaceMapper;
    @Autowired
    private BpmFormManageMapper bpmFormManageMapper;
    @Autowired
    private BpmFormFieldMapper bpmFormFieldMapper;
    @Autowired
    private BpmFormRelePublicFormMapper bpmFormRelePublicFormMapper;
    @Autowired
    private DhProcessInstanceMapper dhProcessInstanceMapper;
    @Autowired
    private DhInterfaceParameterService dhInterfaceParameterService;
    @Autowired
    private DhInterfaceExecuteService dhInterfaceExecuteService;
    @Autowired
    private DhInterfaceMapper dhInterfaceMapper;

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
	public ServerResponse deleteTrigger(String triUid) {
		if (StringUtils.isBlank(triUid)) {
		    return ServerResponse.createByErrorMessage("缺少必要的参数");
        }
	    dhTriggerMapper.delete(triUid);
		return ServerResponse.createBySuccess();
	}



	@Override
	public ServerResponse saveTrigger(DhTrigger dhTrigger) {
		// 触发器id
		dhTrigger.setTriUid(EntityIdPrefix.DH_TRIGGER + UUID.randomUUID().toString());
        String triType = dhTrigger.getTriType();
        DhTriggerType dhTriggerType = DhTriggerType.codeOf(triType);
        if (dhTriggerType == null) {
            return ServerResponse.createByErrorMessage("触发器类型不符合要求");
        }
        if (dhTriggerType == DhTriggerType.INTERFACE) {
            String intUid = dhTrigger.getTriWebbot();
            if (StringUtils.isBlank(intUid)) {
                return ServerResponse.createByErrorMessage("接口类型的触发器缺少接口信息");
            }
            DhInterface dhInterface = dhInterfaceMapper.selectByintUid(intUid);
            if (dhInterface == null) {
                return ServerResponse.createByErrorMessage("触发器对应的接口不存在");
            }
        }
        // 获得当前登录用户的身份
    	String creator = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
    	dhTrigger.setCreator(creator);
    	dhTrigger.setUpdator(creator);
    	// 保存数据
		dhTriggerMapper.save(dhTrigger);
        return ServerResponse.createBySuccess();
    }

	/**
	 * 获得对应异常的详细信息用于保存
	 * @param e
	 * @return
	 */
	private static String getStackMsg(Throwable e) {
		StringBuffer sb = new StringBuffer();
		sb.append(e.toString() + "\n");
		StackTraceElement[] stackArray = e.getStackTrace();
		for (int i = 0; i < stackArray.length; i++) {
			StackTraceElement element = stackArray[i];
			sb.append("\t"+element.toString() + "\n");
		}
		return sb.toString();
	}

	@Override
	public ServerResponse invokeTrigger(WebApplicationContext wac, String insUid, DhStep dhStep){
		String triUid = dhStep.getStepObjectUid();
		DhTrigger dhTrigger = dhTriggerMapper.getByPrimaryKey(triUid);
		Map<String,String> resultMap = new HashMap<>();
		if ("javaclass".equals(dhTrigger.getTriType())) {
			try {
				Class<?> clz = Class.forName(dhTrigger.getTriWebbot());
				Object obj = clz.newInstance();
				JSONObject jb = JSON.parseObject(dhTrigger.getTriParam());
				Method md = obj.getClass().getMethod("execute",
						new Class[] { WebApplicationContext.class, String.class, JSONObject.class, DhStep.class });
				md.invoke(obj, new Object[] { wac, insUid, jb, dhStep });
				resultMap.put("status", "0");
			} catch (InvocationTargetException invokeException) {
				logger.error("调用反射类异常，实例主键：" + insUid, invokeException);
				resultMap.put("status", "1");
				resultMap.put("msg", getStackMsg(invokeException.getTargetException()));
				return ServerResponse.createBySuccess(resultMap);
			} catch (Exception e) {
				logger.error("调用反射类异常，实例主键：" + insUid, e);
				resultMap.put("status", "1");
				resultMap.put("msg", getStackMsg(e));
				return ServerResponse.createBySuccess(resultMap);
			}
		}else if("interface".equals(dhTrigger.getTriType())) {
			return transferInterface(insUid, dhStep, dhTrigger);
		}else{
			resultMap.put("status", "1");
			resultMap.put("msg", "触发器类型为："+dhTrigger.getTriType());
			return ServerResponse.createBySuccess(resultMap);
		}
		return ServerResponse.createBySuccess(resultMap);
	}

	/**
	 * 调用接口传递参数并接收返回值
	    map中的参数：
	 *   "status": 0 调用成功 1 调用失败
	 *   "msg" 错误信息
	 */
	private ServerResponse<Map<String, String>> transferInterface(String insUid, DhStep dhStep, DhTrigger dhTrigger){
		String paramJson = "";
		String triUid = dhStep.getStepObjectUid();
		String stepUid = dhStep.getStepUid();
		Map<String,String> resultMap = new HashMap<>();
		try {
			//获得表单数据
			String insData = dhProcessInstanceMapper.selectByPrimaryKey(insUid).getInsData();
			JSONObject formDataObj = JSONObject.parseObject(insData).getJSONObject("formData");
			//获得接口输入触发器集合
			List<DhTriggerInterface> dhTriggerInterfaceList = dhTriggerInterfaceMapper.queryTriIntByTriUidAndType(triUid,
					stepUid,"inputParameter");
			//获得对应的接口id
			String intUid = dhTrigger.getTriWebbot();
			String inputParameter = "\"inputParameter\":{";
			for (int i = 0; i < dhTriggerInterfaceList.size(); i++) {
				DhTriggerInterface triInt = dhTriggerInterfaceList.get(i);
				//获得接口参数映射的表单name
				String fieldCodeName = triInt.getFldCodeName();
				//获得接口参数name
				String paramName = triInt.getParaName();
				BpmFormField formField = bpmFormFieldMapper.queryFieldByFldUidAndCodeName(triInt.getDynUid(),
						fieldCodeName);
				if (null == formField) {
					//查询该字段是否为子表单中的字段
					List<String> publicFormUidList = bpmFormRelePublicFormMapper.queryFormReleByFormUid(triInt.getDynUid());
					for (String publicFormUid : publicFormUidList) {
						formField = bpmFormFieldMapper.queryFieldByFldUidAndCodeName(publicFormUid, fieldCodeName);
						//找到对应的表单字段
						if (null != formField) {
							break;
						}
					}
				}
				//判断表单字段类型
				if ("object".equals(formField.getFldType())) {
					//获得对应输入list类型的参数下所有普通参数集合
					DhInterfaceParameter dhInterfaceParameter = new DhInterfaceParameter();
					dhInterfaceParameter.setIntUid(intUid);
					dhInterfaceParameter.setParaParent(triInt.getParaUid());
					dhInterfaceParameter.setParaType("input");
					List<DhInterfaceParameter> paramList = dhInterfaceParameterService.byQueryParameter(dhInterfaceParameter);
					//获得表格对应的数据
					String fieldValue = FormDataUtil.getStringValue(fieldCodeName,formDataObj);
					if(null==fieldValue || "".equals(fieldValue)) {
						continue;
					}
					JSONArray tableData = JSONArray.parseArray(fieldValue);
					inputParameter += "\""+paramName+"\":[";
					for(int k=0;k<tableData.size();k++) {
						//获得insData中对应的fieldValue
						inputParameter += "{";
						for(int j=0;j<paramList.size();j++) {
							DhInterfaceParameter interParam = paramList.get(j);
							//获得对应的参数name
							String childParamName = interParam.getParaName();
							//获得参数对应的触发器接口映射对象
							DhTriggerInterface childTriInter = dhTriggerInterfaceMapper.queryTriIntByInCondition(triUid,
										interParam.getParaUid(),"inputParameter");
							if(null==childTriInter) {
								continue;
							}
							//获得对应的表格字段
							String tableFieldCodeName = childTriInter.getFldCodeName();
							BpmFormField tableField = bpmFormFieldMapper.queryFieldByFldUidAndCodeName(triInt.getDynUid(),
									tableFieldCodeName);
							//查询字段是否为子表单中字段
							if (null == tableField) {
								List<String> publicFormUidList = bpmFormRelePublicFormMapper.queryFormReleByFormUid(triInt.getDynUid());
								for (String publicFormUid : publicFormUidList) {
									tableField = bpmFormFieldMapper.queryFieldByFldUidAndCodeName(publicFormUid, tableFieldCodeName);
									//找到对应的表格字段
									if (null != tableField) {
										break;
									}
								}
							}
							JSONObject tableJson = tableData.getJSONObject(k);
							String tableFieldCodeValue = tableJson.get(tableFieldCodeName).toString();
							inputParameter += "\""+childParamName+"\":\""+tableFieldCodeValue+"\"";
							if(j!=tableData.size()-1) {
								inputParameter += ",";
							}
						}
						inputParameter += "}";
						if(k!=paramList.size()-1) {
							inputParameter += ",";
						}
					}
					inputParameter += "]";
				//表格中的字段
				} else if (formField.getFldType().indexOf("object_")!=-1) {
					continue;
				} else {
					//获得insData中指定字段的value
					String fieldValue = FormDataUtil.getStringValue(fieldCodeName,formDataObj);
					if (null == fieldValue) {
						fieldValue = "";
					}
					//将参数name和value拼接成接口需要的格式
					inputParameter += "\"" + paramName + "\":\"" + fieldValue + "\"";
					if (i != dhTriggerInterfaceList.size() - 1) {
						inputParameter += ",";
					}
				}
			}
			inputParameter += "}";
			paramJson = "{\"intUid\":\"" + intUid + "\",\"inputParameter\"" + inputParameter + "}";
			JSONObject paramObj = JSONObject.parseObject(paramJson);
			//调用接口处理数据并接收回调数据
			Json json = dhInterfaceExecuteService.interfaceSchedule(paramObj);
			//处理回调数据
			handleInterfaceData(json, insUid, dhStep);
			resultMap.put("status", "0");
		}catch(Exception e) {
			logger.error("调用接口失败, 实例uid" + insUid, e);
			resultMap.put("status", "1");
			resultMap.put("msg", getStackMsg(e));
			resultMap.put("param", paramJson);
			return ServerResponse.createBySuccess(resultMap);
		}
		return ServerResponse.createBySuccess(resultMap);
	}
	
	/**
	 * 处理接口json返回值更新对应的formData
	 */
	private void handleInterfaceData(Json json,String insUid, DhStep dhStep) throws Exception {
		if(!json.isSuccess()) {
			throw new PlatformException(json.getMsg());
		}
		String triUid = dhStep.getStepObjectUid();
		String stepUid = dhStep.getStepUid();
		String jsonStr = json.getMsg();
		JSONObject jsonObj = JSONObject.parseObject(jsonStr);
		//获得输出类型的触发器接口映射对象集合
		List<DhTriggerInterface> dhTriggerInterfaceList = dhTriggerInterfaceMapper.queryTriIntByTriUidAndType(triUid,stepUid,"outputParameter");
		String formDataStr = "{";
		for(int i=0;i<dhTriggerInterfaceList.size();i++) {
			DhTriggerInterface triInt = dhTriggerInterfaceList.get(i);
			//获得接口的参数name
			String paramName = triInt.getParaName();
			//获得参数对应的value
			String returnValue = jsonObj.getString(paramName);
			//获得对应的字段name
			String fieldCodeName = triInt.getFldCodeName();
			BpmFormField formField = bpmFormFieldMapper.queryFieldByFldUidAndCodeName(triInt.getDynUid(),
					fieldCodeName);
			if (null == formField) {
				//查询字段是否为子表单中的字段
				List<String> publicFormUidList = bpmFormRelePublicFormMapper.queryFormReleByFormUid(triInt.getDynUid());
				for (String publicFormUid : publicFormUidList) {
					formField = bpmFormFieldMapper.queryFieldByFldUidAndCodeName(publicFormUid, fieldCodeName);
					if (null != formField) {
						break;
					}
				}
			}
			//判断表单字段类型
			if ("object".equals(formField.getFldType())) {
				if(null==returnValue || "".equals(returnValue)) {
					continue;
				}
				//返回的参数为list
				JSONArray tableData = JSONArray.parseArray(returnValue);
				formDataStr += "\""+fieldCodeName+"\":{\"value\":[";
				for(int k=0;k<tableData.size();k++) {
					JSONObject tableJson = tableData.getJSONObject(k);
					//获得表格中字段集合
					List<BpmFormField> tableFieldList = bpmFormFieldMapper
							.queryFormTabFieldByFormUidAndTabName(triInt.getDynUid(),formField.getFldCodeName());
					formDataStr += "{";
					for (int j = 0; j < tableFieldList.size(); j++) {
						BpmFormField tableField = tableFieldList.get(j);
						String tableFieldCodeName = tableField.getFldCodeName();
						//获得参数对应的触发器接口映射对象
						DhTriggerInterface childTriInter = dhTriggerInterfaceMapper.queryTriIntByOutCondition(triUid,
								tableFieldCodeName,"outputParameter");
						if(null==childTriInter) {
							continue;
						}
						String tableFieldValue = tableJson.getString(childTriInter.getParaName());
						formDataStr += "\""+tableFieldCodeName+"\":\""+tableFieldValue+"\"";
						if(j!=tableFieldList.size()-1) {
							formDataStr += ",";
						}
					}
					formDataStr += "}";
					if(k!=tableData.size()-1) {
						formDataStr += ",";
					}
				}
				formDataStr += "]}";
				//表格中的字段
			} else if (formField.getFldType().indexOf("object_")!=-1) {
				continue;
			} else {
				if(null == returnValue) {
					returnValue = "";
				}
				//拼接insData中的json
				formDataStr += "\""+fieldCodeName+"\":{\"value\":\""+returnValue+"\"}";
				if (i != dhTriggerInterfaceList.size() - 1) {
					formDataStr += ",";
				}
			}
		}//end for
		formDataStr += "}";
		//获得流程实例对象
		DhProcessInstance dhProcessInstance = dhProcessInstanceMapper.selectByPrimaryKey(insUid);
		String insData = dhProcessInstance.getInsData();
		JSONObject oldObj = JSONObject.parseObject(insData).getJSONObject("formData");
		JSONObject formDataObj = FormDataUtil.formDataCombine(JSONObject.parseObject(formDataStr), oldObj);
		JSONObject insDataObj = JSONObject.parseObject(insData);
		//将新的json数据放入insData中
		insDataObj.put("formData", formDataObj);
		String newInsData = insDataObj.toJSONString();
		dhProcessInstance.setInsData(newInsData);
		int updateRow = dhProcessInstanceMapper.updateByPrimaryKeySelective(dhProcessInstance);
		if(1!=updateRow) {
			throw new PlatformException("接口数据插入失败");
		}
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


	/**
	 * 根据主键查询除法器 返回list
	 */
	@Override
	public ServerResponse getTriggerByPrimarkey(String triUid) {
		if (StringUtils.isBlank(triUid)) {
			return ServerResponse.createByErrorMessage("缺少必要的参数");
		}
		DhTrigger dhtrigger = dhTriggerMapper.getByPrimaryKey(triUid);
		if (DhTriggerType.INTERFACE.getCode().equals(dhtrigger.getTriType())) {
		    // 当是接口类型时，查询接口的名称
            String interfaceUid = dhtrigger.getTriWebbot();
            if (StringUtils.isNotBlank(interfaceUid)) {
                DhInterface dhInterface = dhInterfaceMapper.selectByintUid(interfaceUid);
                if (dhInterface != null) {
                    dhtrigger.setInterfaceTitle(dhInterface.getIntTitle());
                }
            }
        }
		return ServerResponse.createBySuccess(dhtrigger);
	}


	/**
	 * 修改触发器
	 */
	@Override
	public ServerResponse updateTrigger(DhTrigger dhTrigger) {
        String triUid = dhTrigger.getTriUid();
        if (StringUtils.isBlank(triUid)) {
            return ServerResponse.createByErrorMessage("缺少必要的参数");
        }
        DhTrigger triggerInDb = dhTriggerMapper.getByPrimaryKey(triUid);
        if (triggerInDb == null) {
            return ServerResponse.createByErrorMessage("修改失败，触发器不存在");
        }
        if (!triggerInDb.getTriType().equals(dhTrigger.getTriType())) {
            return ServerResponse.createByErrorMessage("修改失败，触发器类型不一致");
        }
        dhTrigger.setUpdator(String.valueOf(SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER)));
        dhTriggerMapper.updateByPrimayKeySelective(dhTrigger);
		return ServerResponse.createBySuccess();
	}


	@Override
	public List<DhTrigger> listByTriggerUidList(List<String> triggerUidList) {
		if (triggerUidList == null || triggerUidList.isEmpty()) {
			return new ArrayList<>();
		}
		return dhTriggerMapper.listByTriggerUidList(triggerUidList);
	}

	@Override
	public DhTrigger getTriggerByTriUid(String triUid) {
		if (StringUtils.isBlank(triUid)){
		    return null;
		}
        return dhTriggerMapper.getByPrimaryKey(triUid);
    }

    public static void main(String[] args){
		try {
			int n = 0/0;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println(e.getStackTrace().toString());
			System.out.println(e.getCause());
		}
	}

}