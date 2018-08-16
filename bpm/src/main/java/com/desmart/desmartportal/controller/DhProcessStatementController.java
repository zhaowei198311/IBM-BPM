package com.desmart.desmartportal.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartportal.entity.DhTaskInstance;
import com.desmart.desmartportal.service.DhProcessStatementService;
import com.desmart.desmartportal.util.ExcelReport;
import com.desmart.desmartsystem.util.MyDateUtils;

/**
 * <p>
 * Title: 流程报表
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author xsf
 * @date 2018年8月7日
 */
@Controller
@RequestMapping("/projectStatement")
public class DhProcessStatementController {

	@Autowired
	private DhProcessStatementService dhProcessStatementService;

	/**
	 * 流程过程业务报表页面跳转
	 */
	@RequestMapping(value = "/projectStatement")
	public String projectStatement() {
		return "desmartportal/statement/projectStatement";
	}

	/**
	 * 流程表单业务报表页面跳转
	 */
	@RequestMapping(value = "/formBusinessReport")
	public String formBusinessReport() {
		return "desmartportal/statement/formBusinessReport";
	}
	
	/**
	 * 门店生命周期业务报表页面跳转
	 */
	@RequestMapping(value = "/storeBusinessReport")
	public String storeBusinessReport() {
		return "desmartportal/statement/storeBusinessReport";
	}
	
	
	/**
	 * 
	 * 根据条件查询流程带分页
	 * 
	 * @param Map<String,
	 *            String> parameter
	 * @param pageNum
	 *            第几页
	 * @param pageSize
	 *            每页最大显示10
	 * @return
	 */
	@RequestMapping(value = "/queryTaskInstance")
	@ResponseBody
	public ServerResponse queryTaskInstance(@RequestParam Map<String, String> parameter, Integer pageNum,
			Integer pageSize) {
		System.out.println(pageNum);
		System.out.println(pageSize);

		return dhProcessStatementService.selectAllTask(parameter, pageNum, pageSize);
	}

	@RequestMapping(value = "/queryExportTaskInstance")
	public void queryExportTaskInstance(@RequestParam Map<String, String> parameter,HttpServletResponse response) throws Exception {
		List<DhTaskInstance> dhTaskInstanceList  = dhProcessStatementService.selectAllTask(parameter);
		Map<String,Object> bean = new HashMap<String,Object>();
		bean.put("createDate",MyDateUtils.getCurrentDate());
		bean.put("dhTaskInstanceList", dhTaskInstanceList);
		ExcelReport   excelReport=new ExcelReport();
		InputStream excInputStream = excelReport.makeReportFromTemplet("/Users/xsf/eclipse-workspace/bpm/src/main/webapp/resources/excel-template/流程过程业务数据.xls", bean);
		OutputStream os = response.getOutputStream();
        response.addHeader("Content-Disposition",
				"attachment;filename=" + new String(("流程过程业务数据" + ".xls").getBytes("utf-8"), "ISO-8859-1"));
		IOUtils.copy(excInputStream, os);
	}

}
