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
	 * @param appDocUid
	 * @return
	 */
	public List<DhInstanceDocument> checkFileActivityIdByName(@Param("appUid")String appUid, @Param("myFileName")String myFileName
			,@Param("appDocUid")String appDocUid);
	/**
	 * 根据条件查询文件列表
	 * @param dhInstanceDocument
	 * @return
	 */
	public List<DhInstanceDocument> loadFileListByCondition(DhInstanceDocument dhInstanceDocument);
	/**
	 * 批量修改
	 * @param dhInstanceDocuments
	 * @return
	 */
	public Integer updateFileByKeys(@Param("dhInstanceDocuments")List<DhInstanceDocument> dhInstanceDocuments);
	/**
	 * 删除
	 * @param appDocUid
	 * @return
	 */
	public Integer deleteFileByAppDocUid(String appDocUid);
	/**
	 * 根据文件标识修改文件
	 * @param dhInstanceDocument
	 * @return
	 */
	public Integer updateFileByFileCard(DhInstanceDocument dhInstanceDocument);
	/**
	 * 根据主键查询
	 * @param appDocUid
	 * @return
	 */
	public DhInstanceDocument selectByPrimaryKey(String appDocUid);
	/**
	 * 更新文件时根据主键修改
	 * @param dhInstanceDocument
	 * @return
	 */
	public Integer updateFileByPrimaryKey(DhInstanceDocument dhInstanceDocument);
}
