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
import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartbpm.common.EntityIdPrefix;
import com.desmart.desmartbpm.dao.DhTriggerMapper;
import com.desmart.desmartbpm.entity.DhTrigger;
import com.desmart.desmartbpm.service.DhTriggerService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 触发器服务
 */
@Service
public class DhTriggerServiceImpl implements DhTriggerService {
    private static final Logger LOG = LoggerFactory.getLogger(DhTriggerServiceImpl.class);

    @Autowired
    private DhTriggerMapper dhTriggerMapper;

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
	public void invokeTrigger(WebApplicationContext wac, String insUid, String triUid){
		DhTrigger dhTrigger = dhTriggerMapper.getByPrimaryKey(triUid);
		if ("javaclass".equals(dhTrigger.getTriType())) {
			try {
				Class<?> clz = Class.forName(dhTrigger.getTriWebbot());
				Object obj = clz.newInstance();
				JSONObject jb = JSONObject.parseObject(dhTrigger.getTriParam());
				Method md = obj.getClass().getDeclaredMethod("execute", 
						new Class []{WebApplicationContext.class, String.class,
								org.json.JSONObject.class});
				md.invoke(obj, new Object[]{wac, insUid, jb});
			} catch (Exception e) {
				e.printStackTrace();
			}
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
	
}