/**
 * 
 */
package com.desmart.desmartportal.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.desmartbpm.dao.BpmActivityMetaMapper;
import com.desmart.desmartbpm.dao.DhActivityConfMapper;
import com.desmart.desmartbpm.dao.DhObjectPermissionMapper;
import com.desmart.desmartbpm.dao.DhStepMapper;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartportal.service.ProcessFormService;

/**  
* <p>Title: ProcessFormServiceImpl</p>  
* <p>Description: </p>  
* @author zhaowei  
* @date 2018年5月10日  
*/
@Service
public class ProcessFormServiceImpl implements ProcessFormService{
	
	@Autowired
	private BpmActivityMetaMapper bpmActivityMetaDao;
	
	@Autowired
	private DhActivityConfMapper dhActivityConfDao;
	
	@Autowired
	private DhStepMapper dhStepDao;
	
	@Autowired
	private DhObjectPermissionMapper dhObjectPermissionDao;
	
	private Logger log = Logger.getLogger(ProcessFormServiceImpl.class);
	
	/**
	 * 查找表单
	 * @param proAppId 流程应用库id
	 * @param proUid 流程id	
	 * @param verUid 版本id
	 * 
	 * 1.首先获取 流程元数据表下 的 流程图上元素id
	 * 2.然后通过元素id 去 查找bpm流程下的 一些 有用配置信息
	 * 3.流程图上元素id 获取 步骤表里面的 步骤id  表单id
	 * 4. 通过步骤id  去参数表里面获取 表单字段
	 */
	@Override
	public void queryProcessForm(String proAppId, String proUid, String verUid) {
		log.info("获取表单 Start...");
		try {
			BpmActivityMeta bpmActivityMeta = new BpmActivityMeta();
			bpmActivityMeta.setBpdId(proUid);
			bpmActivityMeta.setProAppId(proAppId);
			bpmActivityMeta.setSnapshotId(verUid);
			List<BpmActivityMeta> bpmActivityMetaList = bpmActivityMetaDao.queryByBpmActivityMetaSelective(bpmActivityMeta);
			for (BpmActivityMeta bpmActivityMeta2 : bpmActivityMetaList) {
				// bpmActivityMeta2.getActivityBpdId();
				System.err.println(bpmActivityMeta2.getActivityBpdId());
				System.err.println(bpmActivityMeta2.getActivityTo());
				System.err.println(bpmActivityMeta2.getBpmTaskType());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("获取表单 End...");
	}

}
