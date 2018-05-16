package com.desmart.desmartportal.service;

import java.util.List;

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
}
