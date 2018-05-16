package com.desmart.desmartportal.controller;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.desmart.desmartbpm.util.SFTPUtil;
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
					        	sftp.upload(bpmGlobalConfigService.getFirstActConfig(), directory, myFileName, inputStream);
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
	
}
