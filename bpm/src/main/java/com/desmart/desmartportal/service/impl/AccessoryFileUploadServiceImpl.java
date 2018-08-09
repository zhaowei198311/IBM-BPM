package com.desmart.desmartportal.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.desmart.common.constant.ServerResponse;
import com.desmart.common.excelForm.GoodsStateModifyForm;
import com.desmart.common.util.ExcelUtil;
import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartbpm.dao.BpmActivityMetaMapper;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartportal.common.EntityIdPrefix;
import com.desmart.desmartportal.dao.AccessoryFileUploadMapper;
import com.desmart.desmartportal.dao.DhTaskInstanceMapper;
import com.desmart.desmartportal.entity.DhInstanceDocument;
import com.desmart.desmartportal.entity.DhTaskInstance;
import com.desmart.desmartportal.service.AccessoryFileUploadService;
import com.desmart.desmartportal.util.DateUtil;
import com.desmart.desmartportal.util.SFTPUtil;
import com.desmart.desmartportal.util.UUIDTool;
import com.desmart.desmartsystem.entity.BpmGlobalConfig;
import com.desmart.desmartsystem.service.BpmGlobalConfigService;
import com.jcraft.jsch.SftpException;

@Service
public class AccessoryFileUploadServiceImpl implements AccessoryFileUploadService{
	private static final Logger LOG = LoggerFactory.getLogger(AccessoryFileUploadServiceImpl.class);
	@Autowired
	private BpmGlobalConfigService bpmGlobalConfigService;
	@Resource
	private AccessoryFileUploadMapper accessoryFileUploadMapper;
	@Autowired
	private BpmActivityMetaMapper bpmActivityMetaMapper;
	@Autowired
	private DhTaskInstanceMapper dhTaskInstanceMapper;
	
	@Override
	public Integer insertDhInstanceDocuments(List<DhInstanceDocument> dhInstanceDocuments) {
		return accessoryFileUploadMapper.insertDhInstanceDocuments(dhInstanceDocuments);
	}

	@Override
	public List<DhInstanceDocument> checkFileActivityIdByName(String appUid, String myFileName,String appDocUid) {
		return accessoryFileUploadMapper.checkFileActivityIdByName(appUid, myFileName,	appDocUid);
	}

	@Override
	public List<DhInstanceDocument> loadFileListByCondition(DhInstanceDocument dhInstanceDocument) {
		return accessoryFileUploadMapper.loadFileListByCondition(dhInstanceDocument);
	}

	@Override
	public Integer updateFileByKeys(List<DhInstanceDocument> dhInstanceDocuments) {
		return accessoryFileUploadMapper.updateFileByKeys(dhInstanceDocuments);
	}

	@Override
	public Integer deleteFileByAppDocUid(String appDocUid) {
		return accessoryFileUploadMapper.deleteFileByAppDocUid(appDocUid);
	}

	@Override
	@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
	public ServerResponse saveFile(MultipartFile[] multipartFiles, String uploadModels, String appUid
			,String activityId,String taskUid) {
		DhTaskInstance dhTaskInstance = dhTaskInstanceMapper.selectByPrimaryKey(taskUid);
		if(dhTaskInstance==null||!DhTaskInstance.STATUS_CLOSED.equals(dhTaskInstance.getTaskStatus())) {
		
		BpmActivityMeta bpmActivityMeta = bpmActivityMetaMapper.queryByPrimaryKey(activityId);
		if(Const.Boolean.TRUE.equals(bpmActivityMeta.getDhActivityConf().getActcCanUploadAttach())) {
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
                    String directory = DhInstanceDocument.DOC_PROCESS_DIRECTORY;
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String[] dateStrings = simpleDateFormat.format(new Date()).split("-");
                    directory+=dateStrings[0]+"/"+dateStrings[1]+"/"+dateStrings[2]+"/";//获取文件上传目录
                    // 年/月/日/当前时间戳+文件名
                    String newFileName = DateUtil.datetoString(new Date())+myFileName;
                    //判断文件在当前流程是否已经上传
                    List<DhInstanceDocument> list = checkFileActivityIdByName(
                    		appUid,myFileName,null);
                    if(list!=null&&list.size()>0) {
                    	return ServerResponse.createByErrorMessage("上传失败，文件名已存在！请选择对应文件进行更新");
                    } 
						try {
							InputStream inputStream = file.getInputStream();
						DhInstanceDocument dhInstanceDocument = new DhInstanceDocument();
						dhInstanceDocument.setAppDocFileName(myFileName);
						/*dhInstanceDocument.setAppDocStatus("del");//查询是否已有逻辑删除记录--唯一
						List<DhInstanceDocument> exitsList = loadFileListByCondition(dhInstanceDocument);
			            if(exitsList!=null&&exitsList.size()>0) {
			            	dhInstanceDocument = exitsList.get(0);
			            }else {
			            	String uuId = UUIDTool.getUUID();
			            	dhInstanceDocument.setAppDocUid(EntityIdPrefix.DH_INSTANCE_DOCUMENT+uuId);
			            }*/
						String uuId = UUIDTool.getUUID();
		            	dhInstanceDocument.setAppDocUid(EntityIdPrefix.DH_INSTANCE_DOCUMENT+uuId);
			        	dhInstanceDocument.setAppDocFileUrl(directory+newFileName);//文件ftp存储路径
			        	dhInstanceDocument.setAppDocTitle(jObject.get("appDocTitle").toString());
			        	dhInstanceDocument.setAppDocComment(jObject.get("appDocComment").toString());
			        	dhInstanceDocument.setDocVersion(0);//文件版本
			        	dhInstanceDocument.setAppUid(appUid);
			        	dhInstanceDocument.setTaskId(activityId);
			        	dhInstanceDocument.setUserUid(creator);
			        	dhInstanceDocument.setAppDocType(file.getContentType());
			        	dhInstanceDocument.setAppDocCreateDate(Timestamp.valueOf(DateUtil.datetoString(new Date())));
						dhInstanceDocument.setAppDocIndex(i+1);
						dhInstanceDocument.setAppDocTags(jObject.get("appDocTags").toString());
						dhInstanceDocument.setAppDocStatus(Const.FileStatus.NORMAL);//是否被删除
						dhInstanceDocument.setAppDocIsHistory(Const.Boolean.FALSE);
						dhInstanceDocument.setAppDocIdCard(EntityIdPrefix.DH_INSTANCE_FILE_CARD+uuId);//不同版本中的文件唯一标识
						/*if(exitsList!=null&&exitsList.size()>0) {//有已存在删除记录的，则加入到修改中准备修改
							dhInstanceDocument.setDocVersion(exitsList.get(exitsList.size()-1).getDocVersion()+1);
							fileUpdateList.add(dhInstanceDocument);
						}else {*/
							fileUploadList.add(dhInstanceDocument);
						/*}*/
						try {
							SFTPUtil sftp = new SFTPUtil();
				        	sftp.upload(bpmGlobalConfigService.getFirstActConfig(), directory, newFileName, inputStream);
						} catch (SftpException e) {
							// TODO Auto-generated catch block
							LOG.error("保存附件失败", e);
							return ServerResponse.createByErrorMessage("保存附件失败");
						}
						
						} catch (IOException e) {
							// TODO Auto-generated catch block
							LOG.error("保存附件失败", e);
							return ServerResponse.createByErrorMessage("保存附件失败");
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
			return ServerResponse.createByErrorMessage("上传文件出现异常");
		}else {
			return ServerResponse.createBySuccess();
		} 
		}else {
			return ServerResponse.createByErrorMessage("上传权限验证失败 ");
		}	
		}else {
			return ServerResponse.createByErrorMessage("任务已完成，无法操作附件");
		}
		
	}

	@Override
	@Transactional(rollbackFor= {Exception.class,RuntimeException.class})
	public ServerResponse deleteAccessoryFile(DhInstanceDocument dhInstanceDocument
			,String activityId,String taskUid) {
		
		DhTaskInstance dhTaskInstance = dhTaskInstanceMapper.selectByPrimaryKey(taskUid);
		if(dhTaskInstance==null||!DhTaskInstance.STATUS_CLOSED.equals(dhTaskInstance.getTaskStatus())) {
		
		BpmActivityMeta bpmActivityMeta = bpmActivityMetaMapper.queryByPrimaryKey(activityId);
		if(Const.Boolean.TRUE.equals(bpmActivityMeta.getDhActivityConf().getActcCanDeleteAttach())) {
	    //逻辑删除--批量修改方法
	    List<DhInstanceDocument> list = new ArrayList<DhInstanceDocument>();
	    DhInstanceDocument selectCondition = new DhInstanceDocument();
	    selectCondition.setAppDocIdCard(dhInstanceDocument.getAppDocIdCard());
	    selectCondition.setAppDocIsHistory(Const.Boolean.TRUE);
	    selectCondition.setAppDocTags(DhInstanceDocument.DOC_TAGS_PROCESS);//表示是流程附件
	    list = accessoryFileUploadMapper.loadFileListByCondition(selectCondition);//得到当前标识的历史版本文件，将其也一并修改
	    //dhInstanceDocument.setAppDocFileUrl("null");
	    list.add(dhInstanceDocument);
	    String creator = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
	    for (DhInstanceDocument dhInstanceDocument2 : list) {
	    	dhInstanceDocument2.setAppDocStatus(Const.FileStatus.DEL);//del表示删除
	    	dhInstanceDocument2.setAppDocUpdateDate(DateUtil.format(new Date()));
	    	dhInstanceDocument2.setUpdateUserUid(creator);
		}
	    int count = updateFileByKeys(list);
	    	return ServerResponse.createBySuccessMessage("删除成功！");
		}else {
			return ServerResponse.createByErrorMessage("删除权限验证失败！");
		}
		}else {
			return ServerResponse.createByErrorMessage("任务已完成，无法操作附件");
		}
	}

	@Override
	@Transactional(rollbackFor= {Exception.class,RuntimeException.class})
	public ServerResponse updateAccessoryFile(MultipartFile multipartFile, DhInstanceDocument dhInstanceDocument
			,String activityId,String taskUid) {
		DhTaskInstance dhTaskInstance = dhTaskInstanceMapper.selectByPrimaryKey(taskUid);
		if(dhTaskInstance==null||!DhTaskInstance.STATUS_CLOSED.equals(dhTaskInstance.getTaskStatus())) {
		
		BpmActivityMeta bpmActivityMeta = bpmActivityMetaMapper.queryByPrimaryKey(activityId);
		if(Const.Boolean.TRUE.equals(bpmActivityMeta.getDhActivityConf().getActcCanUploadAttach())) {
		//取得当前上传文件的文件名称  
        String myFileName = multipartFile.getOriginalFilename();
        /** 检查更新文件在最新版本是否重名 **/
        List<DhInstanceDocument> checkList = accessoryFileUploadMapper
        		.checkFileActivityIdByName(dhInstanceDocument.getAppUid(), myFileName, dhInstanceDocument.getAppDocUid());
		if(checkList!=null && checkList.size()>0) {
			return ServerResponse.createByErrorMessage("文件名与其它文件名冲突，请选择相应的文件更新或重命名后上传！");
		}else {
			String creator = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String currTime = simpleDateFormat.format(new Date());
			Date currentDate = DateUtil.format(new Date());
			
			dhInstanceDocument.setUpdateUserUid(creator);
			dhInstanceDocument.setAppDocUpdateDate(currentDate);
		dhInstanceDocument.setAppDocIsHistory(Const.Boolean.TRUE);
		Integer count = accessoryFileUploadMapper.updateFileByPrimaryKey(dhInstanceDocument);//对当前文件修改-修改人-修改时间-历史
		DhInstanceDocument oldDhInstanceDocument = accessoryFileUploadMapper.selectByPrimaryKey(dhInstanceDocument.getAppDocUid());
		if(count > 0 ) {
			//当前新增文件
			InputStream inputStream;
			try {
				inputStream = multipartFile.getInputStream();
			
			DhInstanceDocument newDhInstanceDocument = new DhInstanceDocument();
			String directory = DhInstanceDocument.DOC_PROCESS_DIRECTORY;
            String[] dateStrings = currTime.split("-");
            directory+=dateStrings[0]+"/"+dateStrings[1]+"/"+dateStrings[2]+"/";//获取文件上传目录
            // 年/月/日/当前时间戳+文件名
            String newFileName = DateUtil.datetoString(currentDate)+myFileName;
            
            newDhInstanceDocument.setAppDocIsHistory(Const.Boolean.FALSE);
            newDhInstanceDocument.setAppDocIdCard(dhInstanceDocument.getAppDocIdCard());
            newDhInstanceDocument.setDocVersion(oldDhInstanceDocument.getDocVersion()+1);
            newDhInstanceDocument.setAppUid(dhInstanceDocument.getAppUid());
            newDhInstanceDocument.setTaskId(activityId);
            newDhInstanceDocument.setAppDocCreateDate(Timestamp.valueOf(DateUtil.datetoString(currentDate)));
            newDhInstanceDocument.setUserUid(creator);
            
			String uuId = UUIDTool.getUUID();
			newDhInstanceDocument.setAppDocFileName(myFileName);
			newDhInstanceDocument.setAppDocUid(EntityIdPrefix.DH_INSTANCE_DOCUMENT+uuId);
			newDhInstanceDocument.setAppDocFileUrl(directory+newFileName);//文件ftp存储路径
			newDhInstanceDocument.setAppDocType(multipartFile.getContentType());newDhInstanceDocument.setAppDocIndex(1);
			newDhInstanceDocument.setAppDocTags(DhInstanceDocument.DOC_TAGS_PROCESS);//表示是流程附件
			newDhInstanceDocument.setAppDocStatus(Const.FileStatus.NORMAL);//是否被删除
			List<DhInstanceDocument> insert = new ArrayList<DhInstanceDocument>();
			insert.add(newDhInstanceDocument);
			accessoryFileUploadMapper.insertDhInstanceDocuments(insert);
			try {
				SFTPUtil sftp = new SFTPUtil();
	        	sftp.upload(bpmGlobalConfigService.getFirstActConfig(), directory, newFileName, inputStream);
			} catch (SftpException e) {
				// TODO Auto-generated catch block
				LOG.error("保存附件失败", e);
				return ServerResponse.createByErrorMessage("更新文件失败！");
			}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				LOG.error("保存附件失败", e1);
				return ServerResponse.createByErrorMessage("更新文件失败！");
			}
			return ServerResponse.createBySuccessMessage("更新文件成功！");
		}else {
			return ServerResponse.createByErrorMessage("更新文件失败！");
		}
		}
		}else {
			return ServerResponse.createByErrorMessage("更新文件权限验证失败！");
		}
		}else {
			return ServerResponse.createByErrorMessage("任务已完成，无法操作附件");
		}
	}

	@Override
	public DhInstanceDocument selectByPrimaryKey(String appDocUid) {
		return accessoryFileUploadMapper.selectByPrimaryKey(appDocUid);
	}

	@Override
	public ServerResponse loadImageData(DhInstanceDocument dhInstanceDocument, String demoImagePath) {
		dhInstanceDocument = accessoryFileUploadMapper
				.selectByPrimaryKey(dhInstanceDocument.getAppDocUid());
	try {
	SFTPUtil sftp = new SFTPUtil();
	String directory = dhInstanceDocument.getAppDocFileUrl().substring(0,
			dhInstanceDocument.getAppDocFileUrl().lastIndexOf("/") + 1);
	String filename = dhInstanceDocument.getAppDocFileUrl().substring(
			dhInstanceDocument.getAppDocFileUrl().lastIndexOf("/") + 1,
			dhInstanceDocument.getAppDocFileUrl().length());
	File demoImagePakge = new File(demoImagePath);
	if(!demoImagePakge.exists()) { 
		demoImagePakge.mkdirs();
	}
	String newDemoFileName = ((DateUtil.datetoString(new Date())
			+dhInstanceDocument.getAppDocFileName()).replaceAll(" ", "")).replaceAll(":", "-");
	// 根据临时的demoImage包路径，创建demoImage文件
	 File demoImage= new File(demoImagePath+newDemoFileName);
	if (!demoImage.exists()) {
			demoImage.createNewFile();
	}
	FileOutputStream outputStream = new FileOutputStream(demoImage);
	Long count = sftp.getOututStream(bpmGlobalConfigService.getFirstActConfig()
			, directory, filename, outputStream);
	outputStream.flush();
	outputStream.close();
	
	Map<String, Object> data = new HashMap<>();
	data.put("demoFileName", newDemoFileName);
	data.put("absoulteImgPath", demoImagePath+newDemoFileName);
	data.put("dhInstanceDocument", dhInstanceDocument);
	
	return ServerResponse.createBySuccess(data);		
	} catch (Exception e) {
		e.printStackTrace();
	}
	return ServerResponse.createByErrorMessage("加载图片失败");
	}

	@Override
	public ServerResponse uploadEditData(Map map) {
		String appDocUid = map.get("appDocUid").toString();
		DhInstanceDocument dhInstanceDocument = accessoryFileUploadMapper.selectByPrimaryKey(appDocUid);
		
		String creator = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String currTime = simpleDateFormat.format(new Date());
		Date currentDate = DateUtil.format(new Date());
		
		String directory = DhInstanceDocument.DOC_PROCESS_DIRECTORY;
        String[] dateStrings = currTime.split("-");
        directory+=dateStrings[0]+"/"+dateStrings[1]+"/"+dateStrings[2]+"/";//获取文件上传目录
       
        //对当前文件修改-修改人-修改时间-历史
        dhInstanceDocument.setUpdateUserUid(creator);
		dhInstanceDocument.setAppDocUpdateDate(currentDate);
		dhInstanceDocument.setAppDocIsHistory(Const.Boolean.TRUE);
		Integer count = accessoryFileUploadMapper.updateFileByPrimaryKey(dhInstanceDocument);
        
		// 年/月/日/当前时间戳+文件名------上传新文件
		String newFileName = DateUtil.datetoString(new Date())+dhInstanceDocument.getAppDocFileName();
		DhInstanceDocument newDhInstanceDocument = new DhInstanceDocument();
		
        newDhInstanceDocument.setAppDocIsHistory(Const.Boolean.FALSE);
        newDhInstanceDocument.setAppDocIdCard(dhInstanceDocument.getAppDocIdCard());
        newDhInstanceDocument.setDocVersion(dhInstanceDocument.getDocVersion()+1);
        newDhInstanceDocument.setAppUid(dhInstanceDocument.getAppUid());
        newDhInstanceDocument.setTaskId(dhInstanceDocument.getTaskId());
        newDhInstanceDocument.setAppDocCreateDate(Timestamp.valueOf(DateUtil.datetoString(currentDate)));
        newDhInstanceDocument.setUserUid(creator);
        
		String uuId = UUIDTool.getUUID();
		newDhInstanceDocument.setAppDocFileName(dhInstanceDocument.getAppDocFileName());
		newDhInstanceDocument.setAppDocUid(EntityIdPrefix.DH_INSTANCE_DOCUMENT+uuId);
		newDhInstanceDocument.setAppDocFileUrl(directory+newFileName);//文件ftp存储路径
		newDhInstanceDocument.setAppDocType(dhInstanceDocument.getAppDocType());
		newDhInstanceDocument.setAppDocIndex(1);
		newDhInstanceDocument.setAppDocStatus(Const.FileStatus.NORMAL);//是否被删除
		List<DhInstanceDocument> insert = new ArrayList<DhInstanceDocument>();
		insert.add(newDhInstanceDocument);
		accessoryFileUploadMapper.insertDhInstanceDocuments(insert);
		
		String imageData = map.get("image").toString();
		byte[] decoder = Base64.decodeBase64(imageData.replace("data:image/png;base64,","").getBytes());
		try {
			InputStream newInput = new ByteArrayInputStream(decoder);  
			SFTPUtil sftp = new SFTPUtil();
			sftp.upload(bpmGlobalConfigService.getFirstActConfig()
					, directory, newFileName, newInput);
			String absoulteImgPath = map.get("absoulteImgPath").toString();
			deleteTemporaryFile(absoulteImgPath);
			return ServerResponse.createBySuccessMessage("保存修改成功！");
		} catch (Exception e) {
			return ServerResponse.createByErrorMessage("保存修改失败！");
		}
	}

	@Override
	public ServerResponse deleteTemporaryFile(String absoulteImgPath) {
		//删除加载出来的临时文件
		File file = new File(absoulteImgPath);
        // 判断文件是否存在
        if (file.exists()) {
            // 再去判断文件还是文件夹
            if (file.isFile()) {
                file.delete();
            }
        } 
		return ServerResponse.createBySuccess();
	}

	@Override
	public ServerResponse uploadXlsOrXlsxFile(MultipartFile multipartFile, DhInstanceDocument dhInstanceDocument
				,String taskUid,String activityId) {
		//判断是否已经上传了文件数据
		DhInstanceDocument selective = new DhInstanceDocument();
		//selective.setTaskId(activityId);
		selective.setAppUid(dhInstanceDocument.getAppUid());
		selective.setAppDocStatus(Const.FileStatus.NORMAL);
		selective.setAppDocTags(DhInstanceDocument.DOC_TAGS_DATAFORM);
		selective.setAppDocIsHistory(Const.Boolean.FALSE);
		List<DhInstanceDocument> checkList = this.loadFileListByCondition(selective);
		if (checkList != null && checkList.size() > 0) {
			return ServerResponse.createByErrorMessage("导入失败,只能导入一个数据文件");
		}
		/*CommonsMultipartFile cf = (CommonsMultipartFile) multipartFile;
		DiskFileItem fi = (DiskFileItem) cf.getFileItem();
		File file = fi.getStoreLocation();*/
		ExcelUtil excelUtil = ExcelUtil.getInstance();
		if (excelUtil.checkExcelTitleAndSort(multipartFile, GoodsStateModifyForm.class,4)) {
			ServerResponse serverResponse = excelUtil.checkExcelContent(multipartFile, GoodsStateModifyForm.class,4);
			if (serverResponse.isSuccess()) {
				String myFileName = multipartFile.getOriginalFilename();
				if (myFileName.trim() != "") {
					// 当前新增文件
					InputStream inputStream;
					try {
						inputStream = multipartFile.getInputStream();
						String creator = (String) SecurityUtils.getSubject().getSession()
								.getAttribute(Const.CURRENT_USER);
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
						String currTime = simpleDateFormat.format(new Date());
						Date currentDate = DateUtil.format(new Date());
						DhInstanceDocument newDhInstanceDocument = new DhInstanceDocument();
						String directory = DhInstanceDocument.DOC_DATAFORM_DIRECTORY;
						String[] dateStrings = currTime.split("-");
						directory += dateStrings[0] + "/" + dateStrings[1] + "/" + dateStrings[2] + "/";// 获取文件上传目录
						// 年/月/日/当前时间戳+文件名
						String newFileName = DateUtil.datetoString(currentDate) + myFileName;

						newDhInstanceDocument.setAppDocTags(DhInstanceDocument.DOC_TAGS_DATAFORM);// 数据表格文件
						newDhInstanceDocument.setAppDocIsHistory(Const.Boolean.FALSE);
						newDhInstanceDocument.setAppDocIdCard(dhInstanceDocument.getAppDocIdCard());
						newDhInstanceDocument.setAppUid(dhInstanceDocument.getAppUid());
						newDhInstanceDocument.setTaskId(activityId);
						newDhInstanceDocument
								.setAppDocCreateDate(Timestamp.valueOf(DateUtil.datetoString(currentDate)));
						newDhInstanceDocument.setUserUid(creator);

						String uuId = UUIDTool.getUUID();
						newDhInstanceDocument.setAppDocFileName(myFileName);
						newDhInstanceDocument.setAppDocUid(EntityIdPrefix.DH_INSTANCE_DOCUMENT + uuId);
						newDhInstanceDocument.setAppDocFileUrl(directory + newFileName);// 文件ftp存储路径
						newDhInstanceDocument.setAppDocType(multipartFile.getContentType());
						newDhInstanceDocument.setAppDocIndex(1);
						newDhInstanceDocument.setDocVersion(0);
						newDhInstanceDocument.setAppDocStatus(Const.FileStatus.NORMAL);// 是否被删除
						List<DhInstanceDocument> insert = new ArrayList<DhInstanceDocument>();
						insert.add(newDhInstanceDocument);
						accessoryFileUploadMapper.insertDhInstanceDocuments(insert);
						try {
							SFTPUtil sftp = new SFTPUtil();
							sftp.upload(bpmGlobalConfigService.getFirstActConfig(), directory, newFileName,
									inputStream);
							return ServerResponse.createBySuccessMessage("导入文件成功");
						} catch (SftpException e) {
							LOG.error("保存附件失败", e);
							return ServerResponse.createByErrorMessage("导入文件失败！");
						}
					} catch (IOException e1) {
						LOG.error("保存附件失败", e1);
						return ServerResponse.createByErrorMessage("导入文件失败！");
					}

				} else {
					return ServerResponse.createByErrorMessage("导入文件失败,文件不存在");
				}
			} else {
				return ServerResponse.createByErrorMessage("导入失败," + serverResponse.getMsg());
			}

		} else {
			return ServerResponse.createByErrorMessage("导入失败,首部的列名及排列顺序与模板文件的不一致");
		}
	}

	@Override
	public ServerResponse deleteDataFormFileList(DhInstanceDocument dhInstanceDocument, String taskUid) {

		DhTaskInstance dhTaskInstance = dhTaskInstanceMapper.selectByPrimaryKey(taskUid);
		if(dhTaskInstance==null||!DhTaskInstance.STATUS_CLOSED.equals(dhTaskInstance.getTaskStatus())) {
		
		BpmActivityMeta bpmActivityMeta = bpmActivityMetaMapper.queryByPrimaryKey(dhInstanceDocument.getTaskId());
		if(Const.Boolean.TRUE.equals(bpmActivityMeta.getDhActivityConf().getActcCanDeleteAttach())) {
	    //逻辑删除--批量修改方法
	    List<DhInstanceDocument> list = new ArrayList<DhInstanceDocument>();
	    list.add(dhInstanceDocument);
	    String creator = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
	    for (DhInstanceDocument dhInstanceDocument2 : list) {
	    	dhInstanceDocument2.setAppDocStatus(Const.FileStatus.DEL);//del表示删除
	    	dhInstanceDocument2.setAppDocUpdateDate(DateUtil.format(new Date()));
	    	dhInstanceDocument2.setUpdateUserUid(creator);
		}
	    int count = updateFileByKeys(list);
	    	return ServerResponse.createBySuccessMessage("删除成功！");
		}else {
			return ServerResponse.createByErrorMessage("删除权限验证失败！");
		}
		}else {
			return ServerResponse.createByErrorMessage("任务已完成，无法操作数据文件");
		}
	}

	@Override
	public ServerResponse uploadImgFile(MultipartFile multipartFile) {
		Map<String,String> resultMap = new HashMap<>();
		try {
			// 取得上传文件
			MultipartFile file = multipartFile;
			if (file != null) {
				// 取得当前上传文件的文件名称
				String myFileName = file.getOriginalFilename();
				// 如果名称不为“”,说明该文件存在，否则说明该文件不存在
				if (myFileName.trim() != "") {
					String directory = DhInstanceDocument.DOC_IMG_DIRECTORY;
					// 年/月/日/当前时间戳+文件名
					String newFileName = (DateUtil.datetoString(new Date()) + myFileName).replaceAll(" ", "");
					InputStream inputStream = file.getInputStream();
					SFTPUtil sftp = new SFTPUtil();
					BpmGlobalConfig globalConfig = bpmGlobalConfigService.getFirstActConfig();
					sftp.upload(globalConfig, directory, newFileName, inputStream);
					resultMap.put("fileName", newFileName);
					resultMap.put("href", globalConfig.getSftpIp()+"/bpmdata"+directory+newFileName);
				}
			}
		} catch (Exception e) {
			LOG.error("保存附件失败", e);
			return ServerResponse.createByErrorMessage("保存附件失败");
		} 
		return ServerResponse.createBySuccess(resultMap);
	}

	@Override
	public ServerResponse deleteImgFile(String fileName) {
		SFTPUtil sftp = new SFTPUtil();
		String directory = DhInstanceDocument.DOC_IMG_DIRECTORY;
		BpmGlobalConfig globalConfig = bpmGlobalConfigService.getFirstActConfig();
		sftp.removeFile(globalConfig, directory, fileName);
		return ServerResponse.createBySuccess();
	}
}
