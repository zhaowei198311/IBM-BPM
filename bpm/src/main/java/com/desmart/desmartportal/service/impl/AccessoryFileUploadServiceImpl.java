package com.desmart.desmartportal.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartportal.common.EntityIdPrefix;
import com.desmart.desmartportal.dao.AccessoryFileUploadMapper;
import com.desmart.desmartportal.entity.DhInstanceDocument;
import com.desmart.desmartportal.service.AccessoryFileUploadService;
import com.desmart.desmartportal.util.DateUtil;
import com.desmart.desmartportal.util.SFTPUtil;
import com.desmart.desmartportal.util.UUIDTool;
import com.desmart.desmartsystem.service.BpmGlobalConfigService;
import com.jcraft.jsch.SftpException;

@Service
public class AccessoryFileUploadServiceImpl implements AccessoryFileUploadService{
	private static final Logger LOG = LoggerFactory.getLogger(AccessoryFileUploadServiceImpl.class);
	@Autowired
	private BpmGlobalConfigService bpmGlobalConfigService;
	@Resource
	private AccessoryFileUploadMapper accessoryFileuploadMapper;
	@Override
	public Integer insertDhInstanceDocuments(List<DhInstanceDocument> dhInstanceDocuments) {
		// TODO Auto-generated method stub
		return accessoryFileuploadMapper.insertDhInstanceDocuments(dhInstanceDocuments);
	}

	@Override
	public List<DhInstanceDocument> checkFileActivityIdByName(String appUid, String myFileName) {
		// TODO Auto-generated method stub
		return accessoryFileuploadMapper.checkFileActivityIdByName(appUid, myFileName);
	}

	@Override
	public List<DhInstanceDocument> loadFileListByCondition(DhInstanceDocument dhInstanceDocument) {
		// TODO Auto-generated method stub
		return accessoryFileuploadMapper.loadFileListByCondition(dhInstanceDocument);
	}

	@Override
	public Integer updateFileByKeys(List<DhInstanceDocument> dhInstanceDocuments) {
		// TODO Auto-generated method stub
		return accessoryFileuploadMapper.updateFileByKeys(dhInstanceDocuments);
	}

	@Override
	public Integer deleteFileByAppDocUid(String appDocUid) {
		// TODO Auto-generated method stub
		return accessoryFileuploadMapper.deleteFileByAppDocUid(appDocUid);
	}

	@Override
	@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
	public ServerResponse saveFile(MultipartFile[] multipartFiles, String uploadModels, String appUid, String taskId) {
		// TODO Auto-generated method stub
		List<DhInstanceDocument> fileUploadList = new ArrayList<DhInstanceDocument>();
		List<DhInstanceDocument> fileUpdateList = new ArrayList<DhInstanceDocument>();
		JSONObject jso=JSON.parseObject(uploadModels);//json字符串转换成jsonobject对象
		JSONArray jsarr=jso.getJSONArray("uploadModels");//jsonobject对象取得uploadModels对应的jsonarray数组
		//String js=JSONObject.toJSONString(jsarr, SerializerFeature.WriteClassName);//将array数组转换成字符串  
		//获取所有的文本框输入的值
		/*List<DhInstanceDocument> dhInstanceDocuments = JSONObject.parseArray(js,DhInstanceDocument.class);*/
		String creator = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
		
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
                	//System.out.println(checkFileName);
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
                    List<DhInstanceDocument> list = checkFileActivityIdByName(
                    		appUid,myFileName);
                    if(list!=null&&list.size()>0) {
                    	return ServerResponse.createBySuccess("上传失败，文件已存在！", 0);
                    } 
						try {
							InputStream inputStream = file.getInputStream();
						DhInstanceDocument dhInstanceDocument = new DhInstanceDocument();
						dhInstanceDocument.setAppDocFileName(myFileName);
						dhInstanceDocument.setAppDocStatus("del");//查询是否已有逻辑删除记录--唯一
						List<DhInstanceDocument> exitsList = loadFileListByCondition(dhInstanceDocument);
			            if(exitsList!=null&&exitsList.size()>0) {
			            	dhInstanceDocument = exitsList.get(0);
			            }else {
			            	String uuId = UUIDTool.getUUID();
			            	dhInstanceDocument.setAppDocUid(EntityIdPrefix.DH_INSTANCE_DOCUMENT+uuId);
			            }
			        	dhInstanceDocument.setAppDocFileUrl(directory+newFileName);//文件ftp存储路径
			        	dhInstanceDocument.setAppDocTitle(jObject.get("appDocTitle").toString());
			        	dhInstanceDocument.setAppDocComment(jObject.get("appDocComment").toString());
			        	dhInstanceDocument.setDocVersion(0);//文件版本
			        	dhInstanceDocument.setAppUid(appUid);
			        	dhInstanceDocument.setTaskId(taskId);
			        	dhInstanceDocument.setUserUid(creator);
			        	dhInstanceDocument.setAppDocType(file.getContentType());
			        	dhInstanceDocument.setAppDocCreateDate(Timestamp.valueOf(DateUtil.datetoString(new Date())));
						dhInstanceDocument.setAppDocIndex(i+1);
						dhInstanceDocument.setAppDocTags(jObject.get("appDocTags").toString());
						dhInstanceDocument.setAppDocStatus("normal");//是否被删除
						if(exitsList!=null&&exitsList.size()>0) {//有已存在删除记录的，则加入到修改中准备修改
							dhInstanceDocument.setDocVersion(exitsList.get(exitsList.size()-1).getDocVersion()+1);
							fileUpdateList.add(dhInstanceDocument);
						}else {
							fileUploadList.add(dhInstanceDocument);
						}
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
		int count = 0;
		if(fileUploadList.size()>0) {
			count = insertDhInstanceDocuments(fileUploadList);//新增
		}
		if(fileUpdateList.size()>0) {
			count += updateFileByKeys(fileUpdateList);//修改
		}
		if(count<=0) {
			return ServerResponse.createBySuccess(0);
		}else {
			return ServerResponse.createBySuccess(1);
		} 	
	}

	@Override
	@Transactional(rollbackFor= {Exception.class,RuntimeException.class})
	public ServerResponse deleteAccessoryFile(DhInstanceDocument dhInstanceDocument) {
		// TODO Auto-generated method stub
		String directory = dhInstanceDocument.getAppDocFileUrl().substring(0, dhInstanceDocument.getAppDocFileUrl().lastIndexOf("/")+1);
	    String filename = dhInstanceDocument.getAppDocFileUrl().substring(dhInstanceDocument.getAppDocFileUrl().lastIndexOf("/")+1
	    		,  dhInstanceDocument.getAppDocFileUrl().length());
	    SFTPUtil sftp = new SFTPUtil();
	    
	    if(sftp.removeFile(bpmGlobalConfigService.getFirstActConfig(), directory, filename))
	    {
	    	//int count = eleteFileByAppDocUid(dhInstanceDocument.getAppDocUid());
	    	//逻辑删除--批量修改方法
	    	List<DhInstanceDocument> list = new ArrayList<DhInstanceDocument>();
	    	dhInstanceDocument.setAppDocFileUrl("null");
	    	dhInstanceDocument.setAppDocStatus("del");//del表示删除
	    	list.add(dhInstanceDocument);
	    	int count = updateFileByKeys(list);
	    	return ServerResponse.createBySuccessMessage("删除成功！");
	    }else {
	    	return ServerResponse.createByErrorMessage("删除失败！");
	    }
	}

}
