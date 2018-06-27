package com.desmart.desmartportal.service;

import com.desmart.common.constant.ServerResponse;

/**
 * 已办页面表单打印的业务逻辑层
 * @author loser_wu
 * @since 2018年6月26日
 */
public interface FinishFormPrintService {
	/**
	 * 保存要打印的表单内容
	 * @param formUid
	 * @param webpage
	 * @return
	 */
	ServerResponse saveFormPrintContent(String formUid, String webpage);

	/**
	 * 根据表单id查询打印的内容
	 * @param formUid
	 * @return
	 */
	ServerResponse queryPrintContentByFormUid(String formUid);

	/**
	 * 根据表单id删除打印内容
	 * @param formUid
	 * @return
	 */
	int deletePrintContentByFormUid(String formUid);
}
