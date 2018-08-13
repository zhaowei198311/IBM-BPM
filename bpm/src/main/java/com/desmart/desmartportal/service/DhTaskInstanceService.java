/**
 * 
 */
package com.desmart.desmartportal.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.entity.DataForSubmitTask;
import com.desmart.desmartportal.entity.BpmRoutingData;
import com.desmart.desmartportal.entity.CommonBusinessObject;
import com.desmart.desmartportal.entity.DhProcessInstance;
import com.desmart.desmartportal.entity.DhTaskInstance;
import com.desmart.desmartsystem.entity.BpmGlobalConfig;
import com.github.pagehelper.PageInfo;

/**  
* <p>Title: 任务实例Service</p>  
* <p>Description: </p>  
* @author zhaowei  
* @date 2018年5月9日  
*/
public interface DhTaskInstanceService {
	
	ServerResponse<PageInfo<List<DhTaskInstance>>> selectAllTask(DhTaskInstance taskInstance,Integer pageNum, Integer pageSize);
	
	ServerResponse<PageInfo<List<DhTaskInstance>>> selectByPrimaryKey(String taskUid,Integer pageNum, Integer pageSize);
	
	int updateByPrimaryKey(String taskUid);
	
	int deleteByPrimaryKey(String taskUid);
	
	void insertTask(DhTaskInstance taskInstance);
	
	int selectByusrUid(String usrUid);
	
	int selectByusrUidFinsh(String usrUid);
	
	ServerResponse<PageInfo<List<DhProcessInstance>>> selectTaskByUser(DhTaskInstance taskInstance,Integer pageNum, Integer pageSize);
	
	int getMaxTaskIdInDb();
	
	/**
	 * 批量插入任务
	 * @param list
	 * @return
	 */
	int insertBatch(List<DhTaskInstance> list);

	/**
	 * 发起流程成功后，提交第一个任务
	 * @param dhProcessInstance  主流程
	 * @param taskId  任务id
	 * @param firstHumanActivity   任务节点
	 * @param processContainFirstTask
	 * @param routingData  预测的下个环节信息
	 * @param pubBo  引擎中对象
	 * @param dataJson  发起流程时提交的信息
	 * @return
	 */
	ServerResponse commitFirstTask(int taskId, BpmActivityMeta firstHumanActivity, DhProcessInstance processContainFirstTask,
								   BpmRoutingData routingData,
								   CommonBusinessObject pubBo, JSONObject dataJson);

	/**
	 * 完成任务
	 */
	ServerResponse perform(String data);

	/**
	 * 完成任务，根据是否存在下一个步骤区别处理：<br/>
	 * 没有下个步骤：调用RESTFul API完成任务并创建/关闭流程 <br/>
	 * 存在下个步骤：推送到MQ消息队列处理 <br/>
	 * @param dataForSubmitTask  提交任务需要的信息
	 * @return
	 */
	ServerResponse finishTaskOrSendToMq(DataForSubmitTask dataForSubmitTask);
	
	/**
	 * 查看平台任务表中是否存在指定taskId的任务
	 * @param taskId
	 * @return
	 */
	boolean isTaskExists(int taskId);
	
	/**
	 * 获得待办任务信息, 跳转到处理待办页面的数据
	 * @param taskUid
	 * @return
	 */
	ServerResponse<Map<String, Object>> toDealTask(String taskUid);
	
	/**
	 * 根据条件查询任务
	 * @param dhTaskInstance
	 * @return
	 */
	List<DhTaskInstance> selectByCondition(DhTaskInstance dhTaskInstance);
	
	/**
	 * 根据任务实例查询任务数据和流程数据
	 * @param taskInstance
	 * @return
	 */
	ServerResponse<PageInfo<List<DhTaskInstance>>> selectTaskAndProcessInfo(DhTaskInstance taskInstance,Integer pageNum, Integer pageSize);
	
	/**
	 * 
	 * @Title: queryProgressBar  
	 * @Description: 查询审批进度  
	 * @param @param bpmActivityMeta
	 * @param @return  
	 * @return ServerResponse<?>  
	 */
	ServerResponse<?> queryProgressBar(String activityId, String taskUid);

	/**
	 * 加签确定
	 * @param dhTaskInstance  userUid属性存放 处理人， taskType属性定义加签类型， taskUid为当前任务主键
	 * @param creator
	 * @return
	 */
	ServerResponse<?> addSure(DhTaskInstance dhTaskInstance, String creator);

	/**
	 * 根据已办任务id查询已办的任务明细
	 * @param taskUid
	 */
	ServerResponse<Map<String, Object>> toFinshedTaskDetail(String taskUid);
	
	/**
	 * 
	 * @Title: toAddSign  
	 * @Description: 根据taskUid查询加签代办任务
	 * @param @param taskUid
	 * @param @return  
	 * @return ServerResponse<?>  
	 */
	ServerResponse<Map<String, Object>> toAddSign(String taskUid);
	
	/**
	 * 会签任务审批完成
	 * @param @param taskUid
	 * @param @param approvalContent
	 * @param @return  
	 * @return ServerResponse<?>  
	 */
	ServerResponse<?> finishAdd(String taskUid, String activityId, String approvalContent);
	/**
	 * 根据条件查询待办
	 * @param startTime
	 * @param endTime
	 * @param dhTaskInstance
	 * @return
	 */
	ServerResponse<PageInfo<List<DhTaskInstance>>> selectBackLogTaskInfoByCondition(
			Date startTime,
			Date endTime,
			DhTaskInstance dhTaskInstance, Integer pageNum, Integer pageSize,String isAgent);

	/**
	 * 根据条件查询已办
	 * @param startTime
	 * @param endTime
	 * @param dhTaskInstance
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	ServerResponse<PageInfo<List<DhTaskInstance>>> loadPageTaskByClosedByCondition(Date startTime, Date endTime, DhTaskInstance dhTaskInstance,
			Integer pageNum, Integer pageSize,String isAgent);
	
	/**
	 * 根据条件查询移动端已办
	 * @param startTime
	 * @param endTime
	 * @param dhTaskInstance
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	ServerResponse<PageInfo<List<DhTaskInstance>>> loadPageTaskByClosedByConditionMove(Date startTime, Date endTime, DhTaskInstance dhTaskInstance,
			Integer pageNum, Integer pageSize,String isAgent);
	
	/**
	 * 根据条件查询移动端办结
	 * @param startTime
	 * @param endTime
	 * @param dhTaskInstance
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	ServerResponse<PageInfo<List<DhTaskInstance>>> loadPageTaskByEndByConditionMove(Date startTime, Date endTime, DhTaskInstance dhTaskInstance,
			Integer pageNum, Integer pageSize,String isAgent);

	/**
	 * 根据用户查询已办数量
	 * @param userId
	 * @return
	 */
	Integer alreadyClosedTaskByusrUid(String userId);
	
	/**
	 * 
	 * @Title: queryTransfer  
	 * @Description: 查询抄送已读/未读任务  
	 * @param @param dhTaskInstance
	 * @param @return  
	 * @return ServerResponse<PageInfo<List<DhTaskInstance>>>  
	 */
	ServerResponse<PageInfo<List<DhTaskInstance>>> queryTransfer(Date startTime, Date endTime,
																DhTaskInstance dhTaskInstance,Integer pageNum, 
																Integer pageSize);
	/**
	 * 
	 * @Title: queryTransferNumber  
	 * @Description: 查询抄送已读/未读任务数量  
	 * @param @param dhTaskInstance
	 * @param @return  
	 * @return Integer  
	 */
	Integer queryTransferNumber(DhTaskInstance dhTaskInstance);
	
	/**
	 * 
	 * @Title: updateTaskStatusOfTransfer  
	 * @Description: 点击抄送未读详情页面，更改任务状态  
	 * @param @param taskUid
	 * @param @return  
	 * @return ServerResponse<?>  
	 */
	ServerResponse<?> updateTaskStatusOfTransfer(String taskUid);
	
	/**
	 * 
	 * @Title: transferSure  
	 * @Description: 抄送确认  
	 * @param @param taskUid
	 * @param @return  
	 * @return ServerResponse<?>  
	 */
	ServerResponse<?> transferSure(String taskUid, String usrUid, String activityId);

	/**
	 * 发起流程界面的分页展示
	 * @param dhTaskInstance
	 * @param pageNum
	 * @param pageSize
	 * @param insTitle
	 * @param insInitUser
	 * @param insStatusId
	 * @param proAppId
	 * @param proUid
	 * @return
	 */
	ServerResponse<PageInfo<List<DhTaskInstance>>> loadPageTaskByClosedByStartProcess(
													Integer pageNum, Integer pageSize, String insUid, String usrUid);

	/**
	 * 判断当前任务能否编辑insTitle
	 * @param taskInstance
	 * @return
	 */
	boolean canEditInsTitle(DhTaskInstance taskInstance, DhProcessInstance processInstance);

	/**
	 * 驳回任务
	 * @param data
	 * @return
	 */
	ServerResponse rejectTask(String data);

	/**
	 * 取回任务
	 * @param taskUid  任务主键
	 * @return
	 */
	ServerResponse revokeTask(String taskUid);
	/**
	 * 作废与当前任务相关的关联任务（同一个节点上）
	 * @param taskInstance
	 * @return
	 */
	Integer abandonRelationTaskOnTaskNode(DhTaskInstance currTaskInstance);

	/**
	 * 跳转到被驳回的环节
	 * @param data
	 * @return
	 */
	ServerResponse skipFromReject(String data);
	/**
	 * 在重试拉取任务列表中插入一条数据
	 * @param taskId  任务编号
	 * @return
	 */
	int saveTaskToRetryTable(int taskId);

	/**
	 * 完成任务时，更新任务实例状态
	 * @param dhTaskInstance  完成的任务实例
	 * @param taskData  用户提交上来的数据
	 * @return
	 */
	int updateDhTaskInstanceWhenFinishTask(DhTaskInstance dhTaskInstance, String taskData);

	/**
	 * 自动提交时，更新任务实例状态, 将任务还给初始处理人
	 * @param dhTaskInstance  完成的任务实例
	 * @param taskData  用户提交上来的数据
	 * @return
	 */
	int updateDhTaskInstanceWhenAutoCommit(DhTaskInstance dhTaskInstance, String originalUser);

	/**
	 * 取回过期的代理任务
	 * @return
	 */
	ServerResponse revokeAgentOutTask();

	/**
	 * 查询移动端代办列表
	 * @param startTime
	 * @param endTime
	 * @param dhTaskInstance
	 * @param pageNum
	 * @param pageSize
	 * @param isAgent
	 * @return
	 */
	ServerResponse<PageInfo<List<DhTaskInstance>>> selectBackLogTaskInfoByConditionMove(Date startTime, Date endTime,
			DhTaskInstance dhTaskInstance, Integer pageNum, Integer pageSize, String isAgent);

	/**
	 * 完成一个系统任务
	 * @param systemTaskInstance
	 * @param bpmGlobalConfig
	 * @return
	 */
	ServerResponse submitSystemTask(DhTaskInstance systemTaskInstance, BpmGlobalConfig bpmGlobalConfig);
}
