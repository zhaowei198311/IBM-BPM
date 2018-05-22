package com.desmart.desmartportal.service;

import java.util.List;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.desmart.desmartportal.common.ServerResponse;
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
	 * @return
	 */
	public List<DhInstanceDocument> checkFileActivityIdByName(String appUid, String myFileName);
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
	 * @param taskId
	 * @return
	 */
	public ServerResponse saveFile(MultipartFile[] multipartFiles,String uploadModels,
			String appUid,String taskId);
}
