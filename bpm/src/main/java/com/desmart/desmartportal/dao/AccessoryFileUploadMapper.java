package com.desmart.desmartportal.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.desmart.desmartportal.entity.DhInstanceDocument;
@Repository
public interface AccessoryFileUploadMapper {
	/**
	 * 循环新增附件数据
	 * @param dhInstanceDocuments
	 * @return
	 */
	public Integer insertDhInstanceDocuments(@Param("dhInstanceDocuments")List<DhInstanceDocument> dhInstanceDocuments);
	/**
	 * 判断文件在当前流程是否已经上传
	 * @param appUid
	 * @param myFileName
	 * @return
	 */
	public List<DhInstanceDocument> checkFileActivityIdByName(@Param("appUid")String appUid, @Param("myFileName")String myFileName);
}
