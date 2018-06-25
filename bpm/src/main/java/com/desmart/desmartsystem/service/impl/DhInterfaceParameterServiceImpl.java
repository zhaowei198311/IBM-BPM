/**
 * 
 */
package com.desmart.desmartsystem.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartportal.common.Const;
import com.desmart.desmartsystem.common.EntityIdPrefix;
import com.desmart.desmartsystem.dao.DhInterfaceParameterMapper;
import com.desmart.desmartsystem.entity.DhInterfaceParameter;
import com.desmart.desmartsystem.service.DhInterfaceParameterService;
import com.desmart.desmartsystem.util.IterfaceValidate;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**  
* <p>Title: DhInterfaceParameterImpl</p>  
* <p>Description: </p>  
* @author zhaowei  
* @date 2018年4月17日  
*/
@Service
public class DhInterfaceParameterServiceImpl implements DhInterfaceParameterService {
	
	private static final Logger LOG = LoggerFactory.getLogger(DhInterfaceParameterServiceImpl.class);
	
	@Autowired
	private DhInterfaceParameterMapper dhInterfaceParameterMapper;
	
	/* (non-Javadoc)
	 * @see com.desmart.desmartbpm.service.DhInterfaceParameterService#listDhInterfaceParameter(java.lang.String, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public ServerResponse<PageInfo<List<DhInterfaceParameter>>> listDhInterfaceParameter(String intUid, Integer pageNum,
			Integer pageSize) {
		pageSize=100;
		PageHelper.startPage(pageNum, pageSize);
		List<DhInterfaceParameter> interfaceParameterlist = dhInterfaceParameterMapper.listAll(intUid);
		PageInfo<List<DhInterfaceParameter>> pageInfo = new PageInfo(interfaceParameterlist);
		return ServerResponse.createBySuccess(pageInfo);
	}

	/* (non-Javadoc)
	 * @see com.desmart.desmartbpm.service.DhInterfaceParameterService#delDhInterfaceParameter(java.lang.String)
	 */
	@Override
	public void delDhInterfaceParameter(String paraUid) {
		if(paraUid!=null) {
			dhInterfaceParameterMapper.delete(paraUid);
		}
	}

	/* (non-Javadoc)
	 * @see com.desmart.desmartbpm.service.DhInterfaceParameterService#saveDhInterfaceParametere(com.desmart.desmartbpm.entity.DhInterfaceParameter)
	 */
	@Override
	@Transactional
	public ServerResponse saveDhInterfaceParametere(List<DhInterfaceParameter> dhInterfaceParameterList) {
		int size=dhInterfaceParameterList.size();
		
		for (DhInterfaceParameter dhInterfaceParameter : dhInterfaceParameterList) {
			
			String dateFarmat = dhInterfaceParameter.getDateFormat();
			if(StringUtils.isNotBlank(dateFarmat)) {
				boolean bol = IterfaceValidate.dateFormat(dateFarmat);
				if(!bol) {
					return ServerResponse.createByErrorMessage(dhInterfaceParameter.getParaName()+"日期格式不正确!");
				}
			}
		}
		
		
		if(size>1) {//添加数组类型参数
			String paraParent="";
			String paraInOut="";
			for (DhInterfaceParameter dhInterfaceParameter : dhInterfaceParameterList) {
				String paraUid=EntityIdPrefix.DH_INTERFACE_PARAMETER + UUID.randomUUID().toString();
				dhInterfaceParameter.setParaUid(paraUid);
				String paraType=dhInterfaceParameter.getParaType();
				if(paraType.equals(Const.PARAMETER_TYPE_ARRAY)) {
					paraParent=paraUid;
					paraInOut=dhInterfaceParameter.getParaInOut();
				}else{
					dhInterfaceParameter.setParaParent(paraParent);
					dhInterfaceParameter.setParaInOut(paraInOut);
				} 
				dhInterfaceParameter.setParaUid(paraUid);
				dhInterfaceParameterMapper.save(dhInterfaceParameter);
			}
		}else {//添加非array类型参数
			for (DhInterfaceParameter dhInterfaceParameter : dhInterfaceParameterList) {
				String paraUid=EntityIdPrefix.DH_INTERFACE_PARAMETER + UUID.randomUUID().toString();
				dhInterfaceParameter.setParaUid(paraUid);
				dhInterfaceParameterMapper.save(dhInterfaceParameter);
			}
		}
		return ServerResponse.createBySuccessMessage("创建成功");
	}

	/* (non-Javadoc)
	 * @see com.desmart.desmartsystem.service.DhInterfaceParameterService#updateDhInterfaceParametere(com.desmart.desmartsystem.entity.DhInterfaceParameter)
	 */
	@Override
	public ServerResponse updateDhInterfaceParametere(DhInterfaceParameter dhInterfaceParameter) {
		if(dhInterfaceParameter!=null) {
			String dateFarmat = dhInterfaceParameter.getDateFormat();
			if(StringUtils.isNotBlank(dateFarmat)) {
				boolean bol = IterfaceValidate.dateFormat(dateFarmat);
				if(!bol) {
					return ServerResponse.createByErrorMessage(dhInterfaceParameter.getParaName()+"日期格式不正确!");
				}
			}
		}
		dhInterfaceParameterMapper.update(dhInterfaceParameter);
		return ServerResponse.createBySuccessMessage("修改成功");
	}

	/* (non-Javadoc)
	 * @see com.desmart.desmartsystem.service.DhInterfaceParameterService#selectByparaUid(java.lang.String)
	 */
	@Override
	public DhInterfaceParameter selectByparaUid(String paraUid) {
		return dhInterfaceParameterMapper.selectByparaUid(paraUid);
	}

	@Override
	public List<DhInterfaceParameter> querybyintUid(String intUid) {
		// TODO Auto-generated method stub
		return dhInterfaceParameterMapper.listAll(intUid);
	}


	@Override
	public List<DhInterfaceParameter> byQueryParameter(DhInterfaceParameter dhInterfaceParameter) {
		// TODO Auto-generated method stub
		return dhInterfaceParameterMapper.byQueryParameter(dhInterfaceParameter);
	}

	@Override
	
	@Transactional
	public ServerResponse saveOrUpdate(List<DhInterfaceParameter> dhInterfaceParameterList) throws Exception {
		// TODO Auto-generated method stub
		
		
		for (DhInterfaceParameter dhInterfaceParameter : dhInterfaceParameterList) {
			String dateFarmat = dhInterfaceParameter.getDateFormat();
			if(StringUtils.isNotBlank(dateFarmat)) {
				boolean bol = IterfaceValidate.dateFormat(dateFarmat);
				if(!bol) {
					return ServerResponse.createByErrorMessage(dhInterfaceParameter.getParaName()+"日期格式不正确!");
				}
			}
		}
		
		
		String paraParent="";
		for (DhInterfaceParameter dhInterfaceParameter : dhInterfaceParameterList) {
			String paraUid=dhInterfaceParameter.getParaUid();
			String paraType=dhInterfaceParameter.getParaType();
			if(paraType.equals(Const.PARAMETER_TYPE_ARRAY)) {
				paraParent=paraUid;
			}
		}
		
		
		DhInterfaceParameter ifp=new DhInterfaceParameter();
		List<DhInterfaceParameter> dhInterfaceParameters=new ArrayList<DhInterfaceParameter>();
		for (DhInterfaceParameter dhInterfaceParameter : dhInterfaceParameterList) {
			String paraUid = dhInterfaceParameter.getParaUid();
			if(StringUtils.isNotBlank(paraUid)) {//如果主键不为空进行修改操作
				dhInterfaceParameters.add(dhInterfaceParameter);
			}
		}
		ifp.setDhInterfaceParameters(dhInterfaceParameters);
		ifp.setParaParent(paraParent);
		//删除掉不在集合中的数据
		if(dhInterfaceParameters.size()>0) {
			dhInterfaceParameterMapper.deleteArrayParameter(ifp);
		}
		
		

		for (DhInterfaceParameter dhInterfaceParameter : dhInterfaceParameterList) {
			String paraUid=dhInterfaceParameter.getParaUid();
			if(StringUtils.isNotBlank(paraUid)) {//如果主键不为空进行修改操作
				dhInterfaceParameterMapper.update(dhInterfaceParameter);
			}else {//为空进行添加
				String paraUidNew=EntityIdPrefix.DH_INTERFACE_PARAMETER + UUID.randomUUID().toString();
				dhInterfaceParameter.setParaUid(paraUidNew);
				dhInterfaceParameter.setParaParent(paraParent);
				dhInterfaceParameterMapper.save(dhInterfaceParameter);
			}
		}
		
		return ServerResponse.createBySuccessMessage("修改成功");
	}
	
	
}
