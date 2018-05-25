package com.desmart.desmartportal.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.desmart.desmartportal.util.SFTPUtil;
import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartportal.common.EntityIdPrefix;
import com.desmart.desmartportal.common.ServerResponse;
import com.desmart.desmartportal.entity.DhInstanceDocument;
import com.desmart.desmartportal.service.AccessoryFileUploadService;
import com.desmart.desmartportal.util.DateUtil;
import com.desmart.desmartportal.util.UUIDTool;
import com.desmart.desmartsystem.entity.BpmGlobalConfig;
import com.desmart.desmartsystem.service.BpmGlobalConfigService;
import com.jcraft.jsch.SftpException;

@Controller
@RequestMapping("/accessoryFileUpload")
public class AccessoryFileUploadController {
	private static final Logger LOG = LoggerFactory.getLogger(AccessoryFileUploadController.class);
	
	@Autowired
	private AccessoryFileUploadService accessoryFileUploadServiceImpl;
	@Autowired
	private BpmGlobalConfigService bpmGlobalConfigService;
	

	@RequestMapping("/fileUploadLay.do")
	public ModelAndView lay() {  
		System.out.println("INNNNNNN");
		return new ModelAndView("desmartportal/fileUploadLay");
	}
	
	@RequestMapping(value="saveFile.do")
	@ResponseBody
	public ServerResponse saveFile(@RequestParam("files")MultipartFile[] multipartFiles
			,@RequestParam("uploadModels") String uploadModels,
			@RequestParam("appUid")String appUid,@RequestParam("taskId")String taskId) {
			return accessoryFileUploadServiceImpl.saveFile(multipartFiles, uploadModels, appUid, taskId);
	}
	
	@RequestMapping(value="loadFileList.do")
	@ResponseBody
	public ServerResponse<List<DhInstanceDocument>> loadFileList(
			DhInstanceDocument dhInstanceDocument) {//加载已上传附件列表
		dhInstanceDocument.setAppDocStatus("normal");//normal表示没有被删除
		List<DhInstanceDocument> list = accessoryFileUploadServiceImpl.loadFileListByCondition(dhInstanceDocument);
		return ServerResponse.createBySuccess(list);
	}
	
	@RequestMapping("singleFileDown.do")
	public void singleFileDown(DhInstanceDocument dhInstanceDocument,HttpServletResponse response){
		//System.out.println(dhInstanceDocument.getAppDocFileName());
		response.setContentType("text/html; charset=UTF-8"); //设置编码字符  
		response.setContentType("application/x-msdownload"); //设置内容类型为下载类型  
		OutputStream out = null;
		try {
		
		response.setHeader("Content-disposition", "attachment;filename="
		+new String(dhInstanceDocument.getAppDocFileName().getBytes(), "ISO-8859-1"));//设置下载的文件名称  
	    out = response.getOutputStream();   //创建页面返回方式为输出流，会自动弹出下载框

	    String directory = dhInstanceDocument.getAppDocFileUrl().substring(0, dhInstanceDocument.getAppDocFileUrl().lastIndexOf("/")+1);
	    String filename = dhInstanceDocument.getAppDocFileUrl().substring(dhInstanceDocument.getAppDocFileUrl().lastIndexOf("/")+1
	    		,  dhInstanceDocument.getAppDocFileUrl().length());
	    SFTPUtil sftp = new SFTPUtil();
	    
	    sftp.getOututStream(bpmGlobalConfigService.getFirstActConfig(), directory, filename,out);	    
	    
	    out.flush();
		} catch (IOException e) {
			// TODO: handle exception
			LOG.error(e.getMessage());
		}finally {
			if(out!=null) {
				try {
					out.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					LOG.error(e.getMessage());
				}  
			}
		}
	}
	
	@RequestMapping("deleteAccessoryFile.do")
	@ResponseBody
	public ServerResponse deleteAccessoryFile(DhInstanceDocument dhInstanceDocument) {
		return accessoryFileUploadServiceImpl.deleteAccessoryFile(dhInstanceDocument);
	}
	/*
	@RequestMapping("bachFileDown.do")
	public void batchFileDown(List<DhInstanceDocument> dhInstanceDocuments) {
		System.out.println(dhInstanceDocuments.size());
	}*/
	@RequestMapping("loadGlobalConfig.do")
	@ResponseBody
	public ServerResponse loadGlobalConfig() {
		BpmGlobalConfig bpmGlobalConfig = bpmGlobalConfigService.getFirstActConfig();
		return ServerResponse.createBySuccess(bpmGlobalConfig);
	}
	
	@RequestMapping(value="updateAccessoryFile.do")
	@ResponseBody
	public ServerResponse updateAccessoryFile(@RequestParam("file")MultipartFile multipartFile
			,@RequestParam("appUid")String appUid,@RequestParam("taskId")String taskId) {
			return null;
	}
}
