package com.desmart.desmartbpm.service;

import java.util.List;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartportal.entity.DhProcessInstance;
import com.github.pagehelper.PageInfo;

/**
 * 流程实例管理业务逻辑层接口
 * @author loser_wu
 * @since 2018年5月22日
 */
public interface DhProcessInsManageService {
	/**
	 * 根据条件查询流程管理界面展示
	 * @param dhProcessInstance
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	ServerResponse<PageInfo<List<DhProcessInstance>>> getProcesssInstanceListByCondition(Integer pageNum,Integer pageSize
				,DhProcessInstance dhProcessInstance);
	/**
	 * 终止流程实例
	 * @param dhProcessInstance
	 * @return
	 */
	ServerResponse terminateProcessIns(DhProcessInstance dhProcessInstance);
	/**
	 * 暂停流程实例
	 * @param dhProcessInstance
	 * @return
	 */
	ServerResponse pauseProcessIns(DhProcessInstance dhProcessInstance);
	/**
	 * 恢复流程实例
	 * @param dhProcessInstance
	 * @return
	 */
	ServerResponse resumeProcessIns(DhProcessInstance dhProcessInstance);
	/**
	 * 查询流程实例信息
	 * @param dhProcessInstance
	 * @return
	 */
	ServerResponse getProcessInsInfo(DhProcessInstance dhProcessInstance);
	/**
	 * 撤转流程实例
	 * @param dhProcessInstance
	 * @return
	 */
	ServerResponse trunOffProcessIns(DhProcessInstance dhProcessInstance);
}
