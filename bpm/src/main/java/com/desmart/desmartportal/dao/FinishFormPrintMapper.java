package com.desmart.desmartportal.dao;

import org.apache.ibatis.annotations.Param;

/**
 * 已办页面表单打印的数据持久层
 * @author loser_wu
 * @since 2018年6月26日
 */
public interface FinishFormPrintMapper {
	/**
	 * 保存要打印的表单内容
	 * @param formUid
	 * @param webpage
	 * @return
	 */
	int saveFormPrintContent(@Param("formUid")String formUid, @Param("webpage")String webpage);

	/**
	 * 根据表单id查询打印的内容
	 * @param formUid
	 * @return
	 */
	String queryPrintContentByFormUid(String formUid);

	/**
	 * 根据表单id删除打印内容
	 * @param formUid
	 * @return
	 */
	int deletePrintContentByFormUid(String formUid);
}
