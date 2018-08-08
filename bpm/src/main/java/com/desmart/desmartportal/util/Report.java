package com.desmart.desmartportal.util;

import java.io.InputStream;
import java.util.Collection;
import java.util.Map;

public interface Report {
	@SuppressWarnings("rawtypes")
	public void makeReport(Collection collection, String filePath);
	
	public void makeReport(String[] dataStr, String filePath);
	
	@SuppressWarnings("rawtypes")
	public void makeReport(Collection collection, String[] collumHead, String filePath);
	/**
	 * 按模板生成报表，使用jxls设置报表模板,用于通过浏览器下载报表
	 * @param templetFileName 模板文件绝对路径+模板文件名
	 * @param beans 模板参数对象集合
	 * @return InputStream  
	 */
	@SuppressWarnings("rawtypes")
	public InputStream makeReportFromTemplet(String templetFileName, Map beans);
	/**
	 * 按模板生成报表，使用jxls设置报表模板,直接生成本地文件
	 * @param templetFileName
	 * @param beans
	 * @param targetFileName
	 */
	@SuppressWarnings("rawtypes")
	public void makeReportFromTemplet(String templetFileName, Map beans, String targetFileName);
	
}
