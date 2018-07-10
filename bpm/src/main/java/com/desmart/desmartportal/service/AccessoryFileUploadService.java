package com.desmart.desmartportal.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartportal.entity.DhInstanceDocument;

public interface AccessoryFileUploadService {

	/**
	 * 循环新增附件数据
	 * @param dhInstanceDocuments
	 * @return
	 */
	public Integer insertDhInstanceDocuments(List<DhInstanceDocument> dhInstanceDocuments);
	/**
	 * 判断文件在当前流程是否已经上传
	 * @param appUid
	 * @param myFileName
	 * @param appDocUid
	 * @return
	 */
	public List<DhInstanceDocument> checkFileActivityIdByName(String appUid, String myFileName,String appDocUid);
	/**
	 * 根据条件查询附件列表
	 * @param dhInstanceDocument
	 * @return
	 */
	public List<DhInstanceDocument> loadFileListByCondition(DhInstanceDocument dhInstanceDocument);
	/**
	 * 批量修改
	 * @param dhInstanceDocuments
	 * @return
	 */
	public Integer updateFileByKeys(List<DhInstanceDocument> dhInstanceDocuments);
	/**
	 * 删除
	 * @param appDocUid
	 * @return
	 */
	public Integer deleteFileByAppDocUid(String appDocUid);
	/**
	 * 上传文件
	 * @param multipartFiles
	 * @param uploadModels
	 * @param appUid
	 * @param actcCanUploadAttach
	 * @param taskUid
	 * @return
	 */
	public ServerResponse saveFile(MultipartFile[] multipartFiles,String uploadModels,
			String appUid,String activityId,String taskUid);
	/**
	 * 文件删除
	 * @param dhInstanceDocument
	 * @param actcCanDeleteAttach
	 * @param taskUid
	 * @return
	 */
	public ServerResponse deleteAccessoryFile(DhInstanceDocument dhInstanceDocument,String activityId,String taskUid);
	/**
	 * 更新附件
	 * @param multipartFile
	 * @param dhInstanceDocument
	 * @param actcCanUploadAttach
	 * @param taskUid
	 * @return
	 */
	public ServerResponse updateAccessoryFile(MultipartFile multipartFile
			,DhInstanceDocument dhInstanceDocument,String activityId,String taskUid);
	/**
	 * 根据主键查询
	 * @param appDocUid
	 * @return
	 */
	public DhInstanceDocument selectByPrimaryKey(String appDocUid);
	/**
	 * 加载wPaint插件中的图片
	 * @param dhInstanceDocument
	 * @param demoImagePath
	 * @return
	 */
	public ServerResponse loadImageData(DhInstanceDocument dhInstanceDocument, String demoImagePath);
	/**
	 * 保存编辑后的图片数据
	 * @param map
	 * @return
	 */
	public ServerResponse uploadEditData(Map map);
	/**
	 * 根据绝地路径删除文件
	 * @param absoulteImgPath
	 * @return
	 */
	public ServerResponse deleteTemporaryFile(String absoulteImgPath);
	/**
	 * 上传数据表格文件
	 * @param multipartFile
	 * @param dhInstanceDocument
	 * @param activityId 
	 * @param taskUid 
	 * @return
	 */
	public ServerResponse uploadXlsOrXlsxFile(MultipartFile multipartFile, DhInstanceDocument dhInstanceDocument, String taskUid, String activityId);
}
