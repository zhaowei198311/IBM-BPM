package com.desmart.desmartbpm.service;

import java.util.List;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.DhProcessRetrieve;

public interface DhProcessRetrieveService {
	/**
	 * 新增
	 * @param dhProcessRetrieve
	 * @return
	 */
	public Integer insert(DhProcessRetrieve dhProcessRetrieve);
	/**
	 * 删除
	 * @param dhProcessRetrieve
	 * @return
	 */
	public Integer delete(DhProcessRetrieve dhProcessRetrieve);
	/**
	 * 修改
	 * @param dhProcessRetrieve
	 * @return
	 */
	public Integer update(DhProcessRetrieve dhProcessRetrieve);
	/**
	 * 根据流程元数据的proAppId、proUid查询检索的字段
	 * @param metaUid
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public ServerResponse queryProcessRetrieve(String metaUid, Integer pageNum, Integer pageSize);
	/**
	 * 添加检索字段
	 * @param dhProcessRetrieve
	 * @param metaUid
	 * @return
	 */
	public ServerResponse addProcessRetrieve(DhProcessRetrieve dhProcessRetrieve
				,String metaUid);
	/**
	 * 修改检索字段
	 * @param dhProcessRetrieve
	 * @return
	 */
	public ServerResponse updateProcessRetrieve(DhProcessRetrieve dhProcessRetrieve);
	/**
	 * 删除检索字段
	 * @param dhProcessRetrieve
	 * @return
	 */
	public ServerResponse deleteProcessRetrieve(DhProcessRetrieve dhProcessRetrieve);
	/**
	 * 装配动态搜索所需的数据
	 * @param proAppId
	 * @param proUid
	 * @return
	 */
	public ServerResponse assembleProcessRetrieveList(String proAppId, String proUid);
	/**
	 * 检查检索字段在同一个流程元数据是否已经存在
	 * @param proAppId
	 * @param proUid
	 * @param fieldName
	 * @return
	 */
	public List<DhProcessRetrieve> checkeFieldName(String proAppId, String proUid,String fieldName);
}
