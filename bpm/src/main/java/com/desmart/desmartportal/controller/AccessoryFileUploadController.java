package com.desmart.desmartportal.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.annotations.Param;
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
import com.desmart.desmartportal.common.EntityIdPrefix;
import com.desmart.desmartportal.common.ServerResponse;
import com.desmart.desmartportal.entity.DhInstanceDocument;
import com.desmart.desmartportal.service.AccessoryFileUploadService;
import com.desmart.desmartportal.util.DateUtil;
import com.desmart.desmartportal.util.UUIDTool;
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
	
	@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
	@RequestMapping(value="saveFile.do")
	@ResponseBody
	public ServerResponse saveFile(@RequestParam("files")MultipartFile[] multipartFiles
			,@RequestParam("uploadModels") String uploadModels,
			@RequestParam("appUid")String appUid,@RequestParam("taskId")Integer taskId) {
			List<DhInstanceDocument> fileUploadList = new ArrayList<DhInstanceDocument>();
			JSONObject jso=JSON.parseObject(uploadModels);//json字符串转换成jsonobject对象
			JSONArray jsarr=jso.getJSONArray("uploadModels");//jsonobject对象取得uploadModels对应的jsonarray数组
			//String js=JSONObject.toJSONString(jsarr, SerializerFeature.WriteClassName);//将array数组转换成字符串  
			//获取所有的文本框输入的值
			/*List<DhInstanceDocument> dhInstanceDocuments = JSONObject.parseArray(js,DhInstanceDocument.class);*/
			String currUserUid = "sa";
			//System.out.println(dhInstanceDocuments.size());
			for (int i = 0; i < multipartFiles.length; i++) {
			
			//取得上传文件  
                MultipartFile file = multipartFiles[i];
                if(file != null){
                	//取得当前上传文件的文件名称  
                    String myFileName = file.getOriginalFilename();  
                    //DhInstanceDocument dhInstanceDocumentSource = null;
                    //for (DhInstanceDocument dhInstanceDocument : dhInstanceDocuments) {
                    JSONObject jObject = null;
                    for (int j = 0; j < jsarr.size(); j++) {
                    	JSONObject jsarrSon = (JSONObject)jsarr.get(j);
                    	String checkFileName = (String)jsarrSon.get("appDocFileName");
                    	/*DhInstanceDocument dhInstanceDocument = 
                    			(DhInstanceDocument)JSONObject.parseObject(str, DhInstanceDocument.class);*/
						if(myFileName.equals(checkFileName)) {//获取当前要上传文件的文本框输入的值
                    		//dhInstanceDocumentSource = dhInstanceDocument;
							jObject = jsarrSon;
                    		break;
                    	}
                    	System.out.println(checkFileName);
					}
                    //}
                    if(jObject==null) {
                    	return ServerResponse.createBySuccess("上传失败，网络繁忙！", 0);
                    }
                    //如果名称不为“”,说明该文件存在，否则说明该文件不存在  
                    if(myFileName.trim() !=""){  
                        String directory ="/AccessoryFile/";
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        String[] dateStrings = simpleDateFormat.format(new Date()).split("-");
                        directory+=dateStrings[0]+"/"+dateStrings[1]+"/"+dateStrings[2]+"/";//获取文件上传目录
                        // 年/月/日/当前时间戳+文件名
                        String newFileName = DateUtil.datetoString(new Date())+myFileName;
                        //判断文件在当前流程是否已经上传
                        List<DhInstanceDocument> list = accessoryFileUploadServiceImpl.checkFileActivityIdByName(
                        		appUid,myFileName);
                        if(list!=null&&list.size()>0) {
                        	return ServerResponse.createBySuccess("上传失败，文件已存在！", 0);
                        } 
							try {
								InputStream inputStream = file.getInputStream();
							DhInstanceDocument dhInstanceDocument = new DhInstanceDocument();
				        	String uuId = UUIDTool.getUUID();
				        	dhInstanceDocument.setAppDocUid(EntityIdPrefix.DH_INSTANCE_DOCUMENT+uuId);
				        	dhInstanceDocument.setAppDocFileName(myFileName);
				        	dhInstanceDocument.setAppDocFileUrl(directory+newFileName);//文件ftp存储路径
				        	dhInstanceDocument.setAppDocTitle(jObject.get("appDocTitle").toString());
				        	dhInstanceDocument.setAppDocComment(jObject.get("appDocComment").toString());
				        	dhInstanceDocument.setDocVersion(0);//文件版本
				        	dhInstanceDocument.setAppUid(appUid);
				        	dhInstanceDocument.setTaskId(taskId);
				        	dhInstanceDocument.setUserUid(currUserUid);
				        	dhInstanceDocument.setAppDocType(file.getContentType());
				        	dhInstanceDocument.setAppDocCreateDate(DateUtil.format(new Date()));
							dhInstanceDocument.setAppDocIndex(i+1);
							dhInstanceDocument.setAppDocTags(jObject.get("appDocTags").toString());
							dhInstanceDocument.setAppDocStatus("0");//是否被删除
							fileUploadList.add(dhInstanceDocument);
							
							try {
								SFTPUtil sftp = new SFTPUtil();
					        	sftp.upload(bpmGlobalConfigService.getFirstActConfig(), directory, newFileName, inputStream);
							} catch (SftpException e) {
								// TODO Auto-generated catch block
								LOG.error("保存附件失败", e);
								return ServerResponse.createBySuccess(0);
							}
							
							} catch (IOException e) {
								// TODO Auto-generated catch block
								LOG.error("保存附件失败", e);
								return ServerResponse.createBySuccess(0);
							}
                    }
                }
                
        }
        int count = accessoryFileUploadServiceImpl.insertDhInstanceDocuments(fileUploadList);
        if(count<=0) {
        	return ServerResponse.createBySuccess(0);
        }else {
		 return ServerResponse.createBySuccess(1);
        } 	
	}
	
	@RequestMapping(value="loadFileList.do")
	@ResponseBody
	public ServerResponse<List<DhInstanceDocument>> loadFileList(
			DhInstanceDocument dhInstanceDocument) {//加载已上传附件列表
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
	public ServerResponse deleteAccessoryFile(DhInstanceDocument dhInstanceDocument) {
		String directory = dhInstanceDocument.getAppDocFileUrl().substring(0, dhInstanceDocument.getAppDocFileUrl().lastIndexOf("/")+1);
	    String filename = dhInstanceDocument.getAppDocFileUrl().substring(dhInstanceDocument.getAppDocFileUrl().lastIndexOf("/")+1
	    		,  dhInstanceDocument.getAppDocFileUrl().length());
	    SFTPUtil sftp = new SFTPUtil();
	    
	    if(sftp.removeFile(bpmGlobalConfigService.getFirstActConfig(), directory, filename))
	    {
	    	int count = accessoryFileUploadServiceImpl.deleteFileByAppDocUid(dhInstanceDocument.getAppDocUid());
	    	
	    	return ServerResponse.createBySuccessMessage("删除成功！");
	    }else {
	    	return ServerResponse.createByErrorMessage("删除失败！");
	    }
	}
	/*
	@RequestMapping("bachFileDown.do")
	public void batchFileDown(List<DhInstanceDocument> dhInstanceDocuments) {
		System.out.println(dhInstanceDocuments.size());
	}*/
}
