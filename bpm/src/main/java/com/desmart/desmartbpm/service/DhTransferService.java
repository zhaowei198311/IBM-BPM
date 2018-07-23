package com.desmart.desmartbpm.service;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
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


	ServerResponse tryImportProcessDefinition(MultipartFile file, HttpSession session);

	/**
	 * 将JSON文件转化为DhTransferData对象
	 * @param file 上传的文件
	 * @return
	 * @throws IOException
	 */
	ServerResponse<DhTransferData> turnJsonFileIntoDhTransferData(MultipartFile file);

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

	/**
	 * 导出触发器， 如果触发器是接口类型，会带出接口的信息
	 * @param triUid 触发器主键
	 * @return
	 */
	ServerResponse<DhTransferData> exportTrigger(String triUid);

	/**
	 * 导入触发器前的准备
	 * @param file 用户上传的文件
	 * @param session httpSession
	 * @return  map中的值
	 * exists: "TRUE"   triTitle: "标题"
	 */
	ServerResponse<Map<String, String>> tryImportTrigger(MultipartFile file, HttpSession session);

	/**
	 * 导入触发器
	 * @param transferData session中保存的信息
	 * @return
	 */
	ServerResponse startImprtTrigger(DhTransferData transferData);

	/**
	 * 导出接口
	 * @param intUid
	 * @return
	 */
	ServerResponse<DhTransferData> exportInterface(String intUid);

	/**
	 * 导入接口前的准备
	 * @param file
	 * @param session
	 * @return
	 */
	ServerResponse tryImportInterface(MultipartFile file, HttpSession session);

	/**
	 * 开始导入接口
	 * @param transferData
	 * @return
	 */
	ServerResponse startImportInterface(DhTransferData transferData);

	/**
	 * 导入公共表单前的准备
	 * @param file
	 * @param session
	 * @return
	 */
    ServerResponse tryImportPublicForm(MultipartFile file, HttpSession session);

	/**
	 * 导出公共表单
	 * @param publicFormUid 公共表单主键
	 * @return
	 */
	ServerResponse<DhTransferData> exportPublicForm(String publicFormUid);

	/**
	 * 开始导入公共表单
	 * @param transferData
	 * @return
	 */
	ServerResponse startImportPublicForm(DhTransferData transferData);

	/**
	 * 导出通知模版
	 * @param templateUid 通知模版主键
	 * @return
	 */
    ServerResponse<DhTransferData> exportNotifyTemplate(String templateUid);

	/**
	 * 准备导入通知模版
	 * @param file
	 * @param session
	 * @return
	 */
	ServerResponse tryImportNotifyTemplate(MultipartFile file, HttpSession session);

	/**
	 * 导入通知模版
	 * @param transferData
	 * @return
	 */
	ServerResponse startImportNotifyTemplate(DhTransferData transferData);
}
