/**
 * 
 */
package com.desmart.desmartportal.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartbpm.dao.DhProcessMetaMapper;
import com.desmart.desmartbpm.entity.DhProcessCategory;
import com.desmart.desmartbpm.entity.DhProcessMeta;
import com.desmart.common.exception.PlatformException;
import com.desmart.desmartportal.common.EntityIdPrefix;
import com.desmart.desmartportal.dao.DhAgentMapper;
import com.desmart.desmartportal.entity.DhAgent;
import com.desmart.desmartportal.entity.DhAgentProInfo;
import com.desmart.desmartportal.service.DhAgentService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**  
* <p>Title: 代理设置实现类</p>  
* <p>Description: </p>  
* @author loser_wu
* @date 2018年5月18日  
*/
@Service
@Transactional
public class DhAgentServiceImpl implements DhAgentService {
	@Autowired
	private DhAgentMapper dhAgentMapper;
	
	@Autowired
	private DhProcessMetaMapper dhProcessMetaMapper;
	
	/**
	 * 根据代理Id删除代理数据
	 */
	@Override
	public ServerResponse deleteAgentByAgentId(String agentId) {
		int delAgent = dhAgentMapper.deleteByAgentId(agentId);
		if(delAgent!=1) {
			throw new PlatformException("删除代理信息失败");
		}
		int agentProSize = dhAgentMapper.queryDhAgentProInfoByAgentId(agentId).size();
		int delAgentPro = dhAgentMapper.deleteAgentProById(agentId);
		if(delAgentPro!=agentProSize) {
			throw new PlatformException("删除代理流程信息失败");
		}
		return ServerResponse.createBySuccess();
	}

	/**
	 * 查询所有代理数据
	 */
	@Override
	public ServerResponse<PageInfo<List<DhAgent>>> selectAgentList(Integer pageNum, Integer pageSize, String person) {
		//获取当前用户
		String currUser = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
		PageHelper.startPage(pageNum, pageSize);
		//根据当前用户和代理人查询代理信息集合
		List<DhAgent> resultList = dhAgentMapper.selectAgentList(currUser,person);
		for(DhAgent dhAgent:resultList) {
			//查询代理信息是否代理全部流程
			if("TRUE".equals(dhAgent.getAgentIsAll())) {
				DhProcessMeta dhProcessMeta = new DhProcessMeta();
				dhProcessMeta.setProMetaUid("allProMeta");
				dhProcessMeta.setProName("所有流程");
				List<DhProcessMeta> dhProcessMetaList = new ArrayList<>();
				dhProcessMetaList.add(dhProcessMeta);
				dhAgent.setDhProcessMetaList(dhProcessMetaList);
			} else {
				//获得所有的代理流程集合
				dhAgent.setDhProcessMetaList(queryDhProcessMetaByAgentId(dhAgent.getAgentId()));
			}
		}
		PageInfo<List<DhAgent>> pageInfo = new PageInfo(resultList);
		return ServerResponse.createBySuccess(pageInfo);
	}
	
	/**
	 * 通过代理信息Id获得代理流程信息
	 */
	private List<DhProcessMeta> queryDhProcessMetaByAgentId(String agentId){
		List<DhProcessMeta> dhProcessMetaList = new ArrayList<>();
		//根据流程库id，流程id查询对应的流程元集合
		for(DhAgentProInfo dhAgentProInfo:dhAgentMapper.queryDhAgentProInfoByAgentId(agentId)) {
			DhProcessMeta dhProcessMeta = dhProcessMetaMapper
					.queryByProAppIdAndProUid(dhAgentProInfo.getProAppId(), dhAgentProInfo.getProUid());
			dhProcessMetaList.add(dhProcessMeta);
		}
		return dhProcessMetaList;
	}

	@Override
	public Map<String, String> getDelegateResult(String proAppid, String proUid, String userUid) {
		Map<String,String> agentInfoMap = new HashMap<String, String>();
		//查询用户当前的代理信息
		List<DhAgent> dhAgentList = dhAgentMapper.getDelegateByUserId(userUid);
		if(dhAgentList.size()>=1) {
			//判断是否全部代理
			if("TRUE".equals(dhAgentList.get(0).getAgentIsAll())) {
				//返回代理的代理信息id和代理人id
				agentInfoMap.put("delegateUser",dhAgentList.get(0).getAgentClientele());
				agentInfoMap.put("agentId", dhAgentList.get(0).getAgentId());
				return agentInfoMap;
			}else {
				//查询当前时间指定流程的代理信息
				DhAgent dhAgent = dhAgentMapper.getDelegateResult(proAppid,proUid,userUid);
				if(null == dhAgent) {
					return null;
				}else {
					agentInfoMap.put("delegateUser",dhAgent.getAgentClientele());
					agentInfoMap.put("agentId", dhAgent.getAgentId());
					return agentInfoMap;
				}
			}
		}else{
			return null;
		}
	}

	@Override
	public ServerResponse<List<DhProcessMeta>> listDhProcessMetaByCategoryList(List<DhProcessCategory> categoryList, String proName) {
		if (categoryList == null || categoryList.isEmpty()) {
            return ServerResponse.createByErrorMessage("列表参数错误");
        }
        // 获取所有子分类的元数据
		List<DhProcessMeta> metalist = dhProcessMetaMapper.listByCategoryListAndProName(categoryList, proName);
        return ServerResponse.createBySuccess(metalist);
	}

	@Override
	public ServerResponse addAgentInfo(Date agentSdate, Date agentEdate, 
			List<DhProcessMeta> metaList, String agentPerson,String agentIsAll) {
		String agentId = EntityIdPrefix.DH_AGENT_INFO + UUID.randomUUID().toString();
		String agentOperator = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
		DhAgent dhAgent = new DhAgent();
		dhAgent.setAgentId(agentId);
		dhAgent.setAgentOperator(agentOperator);
		dhAgent.setAgentClientele(agentPerson);
		dhAgent.setAgentStatus("ENABLED");
		dhAgent.setAgentSdate(new Timestamp(agentSdate.getTime()));
		dhAgent.setAgentEdate(new Timestamp(agentEdate.getTime()));
		dhAgent.setAgentIsAll(agentIsAll);
		//添加代理信息
		if(1!=dhAgentMapper.addAgentInfo(dhAgent)) {
			throw new PlatformException("添加代理信息失败");
		}else {
			//判断是否全部代理
			if(agentIsAll.equals("FALSE")) {
				int result = 0;
				//未全部代理，将代理的流程添加到中间表
				for(DhProcessMeta meta:metaList) {
					String agentProInfoId = EntityIdPrefix.DH_AGENT_PRO_INFO + UUID.randomUUID().toString();
					DhAgentProInfo agentProInfo = new DhAgentProInfo(agentProInfoId,
								agentId,meta.getProAppId(),meta.getProUid());
					result += dhAgentMapper.addAgentProInfo(agentProInfo);
				}
				if(result!=metaList.size()) {
					throw new PlatformException("添加代理流程信息失败");
				}
			}
		}
		return ServerResponse.createBySuccess();
	}

	@Override
	public ServerResponse<List<DhProcessMeta>> queryConformProMeta(Date agentSdate, Date agentEdate,
			String[] agentProMetaUidArr) {
		String agentOperator = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
		//查询某个时间段内当前用户的代理信息
		List<DhAgent> dhAgentList = dhAgentMapper.queryAgentInfoByUserAndDate(agentOperator,agentSdate,agentEdate);
		if(dhAgentList.size()==1) {
			//当代理信息只有一条并且为全部代理
			if("TRUE".equals(dhAgentList.get(0).getAgentIsAll())) {
				throw new PlatformException("在该时间段内有目标流程已被代理");
			}
		}
		
		List<DhProcessMeta> metaList = new ArrayList<>();
		for(String agentProMetaUid:agentProMetaUidArr) {
			DhProcessMeta meta = dhProcessMetaMapper.queryByProMetaUid(agentProMetaUid);
			//判断某个流程在时间段内是否被代理
			if(dhAgentMapper.queryAgentProInfoBySelective(agentSdate,agentEdate,meta,agentOperator).isEmpty()) {
				metaList.add(meta);
			}else {
				throw new PlatformException("在该时间段内有目标流程已被代理");
			}
		}
		return ServerResponse.createBySuccess(metaList);
	}

	@Override
	public ServerResponse<List<DhProcessMeta>> queryConformProMetaNotSelf(DhAgent dhAgent, String[] agentProMetaUidArr) {
		String agentOperator = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
		//查询当前用户在某个时间段内排除指定代理外的其他代理信息集合
		List<DhAgent> dhAgentList = dhAgentMapper.queryAgentInfoByUserAndDateNotSelf(
				dhAgent.getAgentId(),agentOperator,dhAgent.getAgentSdate(),dhAgent.getAgentEdate());
		if(dhAgentList.size()==1) {
			//判断查询的代理集合是否为一条全部代理信息
			if("TRUE".equals(dhAgentList.get(0).getAgentIsAll())) {
				throw new PlatformException("在该时间段内有目标流程已被代理");
			}
		}
		//再判断该用户这次要代理的是否为全部流程
		if("allProMeta".equals(agentProMetaUidArr[0])) {
			//当前用户在时间段内有其他的流程代理信息
			if(dhAgentList.size()>=1) {
				throw new PlatformException("在该时间段内有目标流程已被代理");
			}
		}
		
		List<DhProcessMeta> metaList = new ArrayList<>();
		for(String agentProMetaUid:agentProMetaUidArr) {
			DhProcessMeta meta = dhProcessMetaMapper.queryByProMetaUid(agentProMetaUid);
			//查询时间段内当前用户排除指定代理信息外是否存在指定流程的代理信息
			if(dhAgentMapper.queryAgentProInfoBySelectiveNotSelf(dhAgent.getAgentId(),
					dhAgent.getAgentSdate(),dhAgent.getAgentEdate(),meta,agentOperator).isEmpty()) {
				metaList.add(meta);
			}else {
				throw new PlatformException("在该时间段内有目标流程已被代理");
			}
		}
		return ServerResponse.createBySuccess(metaList);
	}

	@Override
	public ServerResponse updateAgentInfo(DhAgent dhAgent, List<DhProcessMeta> metaList) {
		//修改代理设置
		int updateRow = dhAgentMapper.updateAgentById(dhAgent);
		if(1!=updateRow) {
			throw new PlatformException("修改代理设置信息失败");
		}else {
			//删除旧代理信息代理的流程信息
			dhAgentMapper.deleteAgentProById(dhAgent.getAgentId());
			//判断新代理信息是否代理全部流程
			if(dhAgent.getAgentIsAll().equals("FALSE")) {
				int addRow = 0;
				for(DhProcessMeta meta:metaList) {
					String agentProInfoId = EntityIdPrefix.DH_AGENT_PRO_INFO + UUID.randomUUID().toString();
					//添加代理流程信息
					DhAgentProInfo agentProInfo = new DhAgentProInfo(agentProInfoId, 
							dhAgent.getAgentId(), meta.getProAppId(), meta.getProUid());
					addRow += dhAgentMapper.addAgentProInfo(agentProInfo);
				}
				if(addRow != metaList.size()) {
					throw new PlatformException("修改代理流程失败");
				}
			}
		}
		return ServerResponse.createBySuccess();
	}

	@Override
	public ServerResponse<List<DhProcessMeta>> listProMeta(String[] agentProMetaUidArr) {
		//根据流程元id集合批量查询流程元集合
		List<DhProcessMeta> metaList = new ArrayList<>();
		for(String agentProMetaUid:agentProMetaUidArr) {
			DhProcessMeta meta = dhProcessMetaMapper.queryByProMetaUid(agentProMetaUid);
			metaList.add(meta);
		}
		return ServerResponse.createBySuccess(metaList);
	}

	@Override
	public ServerResponse updateAgentStatus(DhAgent dhAgent) {
		//修改代理信息的状态
		int updateRow = dhAgentMapper.updateAgentStatus(dhAgent);
		if(1!=updateRow) {
			throw new PlatformException("修改代理信息失败");
		}
		return ServerResponse.createBySuccess();
	}
}
