package com.desmart.desmartportal.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartportal.entity.DhInstanceDocument;
import com.desmart.desmartportal.service.AccessoryFileUploadService;
import com.desmart.desmartportal.util.SFTPUtil;
import com.desmart.desmartsystem.entity.BpmGlobalConfig;
import com.desmart.desmartsystem.service.BpmGlobalConfigService;

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
		dhInstanceDocument.setAppDocIsHistory(Const.Boolean.FALSE);//表示不是历史文件
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
		Map<String, Object> data = new HashMap<String,Object>();
		data.put("maxFileSize", bpmGlobalConfig.getMaxFileSize());
		data.put("maxFileCount", bpmGlobalConfig.getMaxFileCount());
		data.put("fileFormat", bpmGlobalConfig.getFileFormat());
		return ServerResponse.createBySuccess(bpmGlobalConfig);
	}
	
	@RequestMapping(value="updateAccessoryFile.do")
	@ResponseBody
	public ServerResponse updateAccessoryFile(@RequestParam("file")MultipartFile multipartFile
			,DhInstanceDocument dhInstanceDocument) {
			if(dhInstanceDocument.getAppDocIdCard()==null||
					"".equals(dhInstanceDocument.getAppDocIdCard())) {
				return ServerResponse.createByErrorMessage("更新文件异常！");
			}else {
				return accessoryFileUploadServiceImpl.updateAccessoryFile(multipartFile, dhInstanceDocument);
			}
	}
}
