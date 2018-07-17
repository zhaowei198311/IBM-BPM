package com.desmart.desmartbpm.service;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.DatRule;
import com.desmart.desmartbpm.entity.DatRuleCondition;
import com.desmart.desmartbpm.entity.DhProcessDefinition;
import com.desmart.desmartbpm.entity.DhTransferData;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 流程迁移服务，迁移到新环境
 */
public interface DhTransferService {

	/**
	 * 导出一个流程定义相关的数据
	 * @param proAppId
	 * @param proUid
	 * @param proVerUid
	 * @return
	 */
	ServerResponse<DhTransferData> exportProcessDefinition(String proAppId, String proUid, String proVerUid);

	/**
	 * 生成导出文件的文件名
	 * @param dhProcessDefinition
	 * @return
	 */
	String getExportFileName(DhProcessDefinition dhProcessDefinition);

	/**
	 * 将文件转化为供解析的对象
	 * @param file 上传的文件
	 * @return
	 * @throws IOException
	 */
	ServerResponse<DhTransferData> trunFileIntoDhTransferData(MultipartFile file);

	/**
	 * 导入一个流程定义, 如果重复则覆盖
	 * @param transferData  迁移使用的数据
	 * @return
	 */
	ServerResponse startImportProcessDefinition(DhTransferData transferData);

	/**
	 * 校验用来导入流程的数据 有效性
	 * @param transferData 迁移的数据
	 * @return 满足条件返回success
	 */
	ServerResponse validateTransferDataForImportProcessDefinition(DhTransferData transferData);


	/**
	 * 删除指定流程定义，并删除关联的表数据
	 * @param proAppId
	 * @param proUid
	 * @param proVerUid
	 * @return
	 */
	ServerResponse removeProcessDefinition(String proAppId, String proUid, String proVerUid);
}
