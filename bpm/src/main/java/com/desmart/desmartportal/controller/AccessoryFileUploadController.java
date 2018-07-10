package com.desmart.desmartportal.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
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
	
	@RequestMapping(value = "saveFile.do")
	@ResponseBody
	public ServerResponse saveFile(@RequestParam("files") MultipartFile[] multipartFiles,
			@RequestParam("uploadModels") String uploadModels, @RequestParam("appUid") String appUid,
			@RequestParam("activityId")String activityId
			,@RequestParam("taskUid")String taskUid) {
		return accessoryFileUploadServiceImpl.saveFile(multipartFiles, uploadModels
				, appUid,activityId,taskUid);
	}

	@RequestMapping(value = "loadFileList.do")
	@ResponseBody
	public ServerResponse<List<DhInstanceDocument>> loadFileList(DhInstanceDocument dhInstanceDocument) {// 加载已上传附件列表
		dhInstanceDocument.setAppDocStatus(Const.FileStatus.NORMAL);// normal表示没有被删除
		dhInstanceDocument.setAppDocIsHistory(Const.Boolean.FALSE);// 表示不是历史文件
		dhInstanceDocument.setAppDocTags(DhInstanceDocument.DOC_TAGS_PROCESS);//表示是流程附件
		List<DhInstanceDocument> list = accessoryFileUploadServiceImpl.loadFileListByCondition(dhInstanceDocument);
		return ServerResponse.createBySuccess(list);
	}

	@RequestMapping("singleFileDown.do")
	public void singleFileDown(DhInstanceDocument dhInstanceDocument, HttpServletResponse response) {
		// System.out.println(dhInstanceDocument.getAppDocFileName());
		dhInstanceDocument = accessoryFileUploadServiceImpl.selectByPrimaryKey(dhInstanceDocument.getAppDocUid());
		OutputStream out = null;
		try {
			String fileName = dhInstanceDocument.getAppDocFileName();
			response.setHeader("Content-disposition", "attachment; filename=" + fileName);
			fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1"); 
			response.setHeader("Content-disposition", String.format("attachment; filename=\"%s\"", fileName));  
			response.setCharacterEncoding("UTF-8"); 
			response.setContentType("text/html; charset=UTF-8"); // 设置编码字符
			response.setContentType(dhInstanceDocument.getAppDocType()); // 设置返回内容的mime类型
			
			out = response.getOutputStream(); // 创建页面返回方式为输出流，会自动弹出下载框

			String directory = dhInstanceDocument.getAppDocFileUrl().substring(0,
					dhInstanceDocument.getAppDocFileUrl().lastIndexOf("/") + 1);
			String filename = dhInstanceDocument.getAppDocFileUrl().substring(
					dhInstanceDocument.getAppDocFileUrl().lastIndexOf("/") + 1,
					dhInstanceDocument.getAppDocFileUrl().length());
			SFTPUtil sftp = new SFTPUtil();

			Long fileLength = sftp.getOututStream(bpmGlobalConfigService.getFirstActConfig()
					, directory, filename, out);
			response.setHeader("Content-Length", String.valueOf(fileLength));
			out.flush();
		} catch (IOException e) {
			// TODO: handle exception
			LOG.error(e.getMessage());
		} finally {
			if (out != null) {
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
	public ServerResponse deleteAccessoryFile(DhInstanceDocument dhInstanceDocument,
			@RequestParam("activityId")String activityId
			,@RequestParam(value = "taskUid",required=false)String taskUid) {
		return accessoryFileUploadServiceImpl.deleteAccessoryFile(dhInstanceDocument,activityId
				,taskUid);
	}

	@RequestMapping("bachFileDown.do")
	public void batchFileDown(String[] appDocFileName, String[] appDocFileUrl, HttpServletResponse response,
			HttpServletRequest request) {
		if (appDocFileName != null && appDocFileUrl != null) {
			if (appDocFileName.length == appDocFileUrl.length) {
				List<DhInstanceDocument> dhInstanceDocuments = new ArrayList<DhInstanceDocument>();
				for (int i = 0; i < appDocFileUrl.length; i++) {
					DhInstanceDocument dhInstanceDocument = new DhInstanceDocument();
					dhInstanceDocument.setAppDocFileName(appDocFileName[i]);
					dhInstanceDocument.setAppDocFileUrl(appDocFileUrl[i]);
					dhInstanceDocuments.add(dhInstanceDocument);
				}
				// 打包下载开始
				//首先需要移除掉session当中的数据，因为如果是第二次下载数据的话，这些数据已经存在
	            //会导致进度条先是100%的状态，然后才从0%开始
	            request.getSession().removeAttribute("totalCount");
	            request.getSession().removeAttribute("curCount");
	            request.getSession().removeAttribute("percent");
				/*
				 * response.setContentType("text/html; charset=UTF-8"); //设置编码字符
				 * response.setContentType("application/zip"); //设置内容类型为zip
				 */
				OutputStream out = null;
				try {
					String fileName = "流程附件.zip";
					response.setHeader("Content-disposition", "attachment; filename=" + fileName);
					fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1"); 
					response.setHeader("Content-disposition", String.format("attachment; filename=\"%s\"", fileName));  
					response.setCharacterEncoding("UTF-8"); 
					//response.setContentType("text/html; charset=UTF-8"); // 设置编码字符
					//response.setContentType("application/x-msdownload"); // 设置内容类型为下载类型
					out = response.getOutputStream(); // 创建页面返回方式为输出流，会自动弹出下载框

					// 创建压缩文件需要的空的zip包
					String zipBasePath = request.getSession().getServletContext()
							.getRealPath("/resources/desmartportal/upload/zip/");
					String zipFilePath = zipBasePath + "temp.zip";
					File zip = new File(zipBasePath);
					if(!zip.exists()) { 
						zip.mkdirs();
					}
					// 根据临时的zip压缩包路径，创建zip文件
					 zip = new File(zipFilePath);
					if (!zip.exists()) {
						zip.createNewFile();
					}
					// 创建zip文件输出流
					FileOutputStream fos = new FileOutputStream(zip);
					ZipOutputStream zipOut = new ZipOutputStream(fos);
					
					Integer totalCount = dhInstanceDocuments.size();//获得文件总数
			        request.getSession().setAttribute("totalCount",totalCount);
			        Integer curCount = 0;
			        
					for (int i = 0; i < dhInstanceDocuments.size(); i++) {
						DhInstanceDocument dhInstanceDocument = dhInstanceDocuments.get(i);
						String directory = dhInstanceDocument.getAppDocFileUrl().substring(0,
								dhInstanceDocument.getAppDocFileUrl().lastIndexOf("/") + 1);
						String filename = dhInstanceDocument.getAppDocFileUrl().substring(
								dhInstanceDocument.getAppDocFileUrl().lastIndexOf("/") + 1,
								dhInstanceDocument.getAppDocFileUrl().length());
						SFTPUtil sftp = new SFTPUtil();
						BpmGlobalConfig gcfg = bpmGlobalConfigService.getFirstActConfig();
						// 将文件写入zip内，即将文件进行打包
						ZipEntry ze = new ZipEntry(dhInstanceDocument.getAppDocFileName()); // 获取文件名
						zipOut.putNextEntry(ze);
						zipOut.setEncoding("GBK");
						//计算百分比
						curCount += sftp.getBatchDown(gcfg, directory, filename, zipOut);
						double dPercent=(double)curCount/totalCount;   //将计算出来的数转换成double
						int percent=(int)(dPercent*100);               //再乘上100取整
						request.getSession().setAttribute("curCount", curCount);
						request.getSession().setAttribute("percent", percent);    //比如这里是50
					}
					fos.flush();  
					zipOut.flush();  
					zipOut.close();
					fos.close();
					// 将打包后的文件写到客户端，使用缓冲流输出
					InputStream fis = new BufferedInputStream(new FileInputStream(zipFilePath));
					byte[] buff = new byte[4096];
					int size = 0;
					while ((size = fis.read(buff)) != -1) {
						out.write(buff, 0, size);
					}
					response.setHeader("Content-Length", String.valueOf(fis.available()));
					fis.close();
				} catch (IOException e) {
					// TODO: handle exception
					LOG.error(e.getMessage());
				} finally {
					if (out != null) {
						try {
							out.flush();
							out.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							LOG.error(e.getMessage());
						}
					}
				}
			}
		}
	}

	@RequestMapping("loadGlobalConfig.do")
	@ResponseBody
	public ServerResponse loadGlobalConfig() {
		BpmGlobalConfig bpmGlobalConfig = bpmGlobalConfigService.getFirstActConfig();
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("maxFileSize", bpmGlobalConfig.getMaxFileSize());
		data.put("maxFileCount", bpmGlobalConfig.getMaxFileCount());
		data.put("fileFormat", bpmGlobalConfig.getFileFormat());
		return ServerResponse.createBySuccess(bpmGlobalConfig);
	}

	@RequestMapping(value = "updateAccessoryFile.do")
	@ResponseBody
	public ServerResponse updateAccessoryFile(@RequestParam("file") MultipartFile multipartFile,
			DhInstanceDocument dhInstanceDocument,
			@RequestParam("activityId")String activityId
			,@RequestParam("taskUid")String taskUid) {
		if (dhInstanceDocument.getAppDocIdCard() == null || "".equals(dhInstanceDocument.getAppDocIdCard())) {
			return ServerResponse.createByErrorMessage("更新文件异常！");
		} else {
			return accessoryFileUploadServiceImpl.updateAccessoryFile(multipartFile
					, dhInstanceDocument,activityId,taskUid);
		}
	}
	@RequestMapping("loadHistoryFile.do")
	@ResponseBody
	public ServerResponse loadHistoryFile(DhInstanceDocument dhInstanceDocument) {
		dhInstanceDocument.setAppDocIsHistory(Const.Boolean.TRUE);
		dhInstanceDocument.setAppDocTags(DhInstanceDocument.DOC_TAGS_PROCESS);//表示是流程附件
		List<DhInstanceDocument> list = accessoryFileUploadServiceImpl.loadFileListByCondition(dhInstanceDocument);
		return ServerResponse.createBySuccess(list);
	}
	@RequestMapping("flushProgress.do")
	@ResponseBody
	public ServerResponse flushProgress(HttpServletRequest request) {
		  HashMap<String,Object> map=null;
		  HttpSession session = request.getSession();
          map=new HashMap<String, Object>();
          Integer totalCount = 0;
          if(session.getAttribute("totalCount")!=null) {
        	  totalCount = Integer.valueOf(String.valueOf(session.getAttribute("totalCount")));
          }
          Integer curCount = 0;
          if(session.getAttribute("curCount")!=null) {
        	  curCount = Integer.valueOf(String.valueOf(session.getAttribute("curCount")));
          }
          Integer percent = 0;
          if(session.getAttribute("percent")!=null) {
        	  percent = Integer.valueOf(String.valueOf(session.getAttribute("percent")));
          }
          map.put("totalCount", totalCount);  //总条数
          map.put("curCount", curCount);      //已导条数
          map.put("percent", percent);          //百分比数字
          map.put("percentText", percent+"%");  //百分比文本
		return ServerResponse.createBySuccess(map);
	}
	
	@RequestMapping("/loadImageData")
	@ResponseBody
	public ServerResponse loadImageData(DhInstanceDocument dhInstanceDocument,
				HttpServletRequest request) {
		
		String demoImagePath = request.getSession().getServletContext()
				.getRealPath("/resources/desmartportal/upload/demo/");
		return accessoryFileUploadServiceImpl.loadImageData(dhInstanceDocument,demoImagePath);
	}
	
	@RequestMapping("/uploadEditData")
	@ResponseBody
	public ServerResponse uploadEditData(@RequestBody Map map) {
		return accessoryFileUploadServiceImpl.uploadEditData(map);
	}
	@RequestMapping("/deleteTemporaryFile")
	@ResponseBody
	public ServerResponse deleteTemporaryFile(String absoulteImgPath) {
		return accessoryFileUploadServiceImpl.deleteTemporaryFile(absoulteImgPath);
	}
	@RequestMapping(value = "/uploadXlsOrXlsxFile")
	@ResponseBody
	public ServerResponse uploadXlsOrXlsxFile(@RequestParam("file")MultipartFile multipartFile,DhInstanceDocument dhInstanceDocument
				,@RequestParam("taskUid")String taskUid,@RequestParam("activityId")String activityId) {	
		return accessoryFileUploadServiceImpl.uploadXlsOrXlsxFile(multipartFile,dhInstanceDocument,taskUid,activityId);
	}
	@RequestMapping(value = "/loadDataFormFileList")
	@ResponseBody
	public ServerResponse<List<DhInstanceDocument>> loadDataFormFileList(DhInstanceDocument dhInstanceDocument) {// 加载已上传的数据表格文件列表
		dhInstanceDocument.setAppDocStatus(Const.FileStatus.NORMAL);// normal表示没有被删除
		dhInstanceDocument.setAppDocIsHistory(Const.Boolean.FALSE);// 表示不是历史文件
		dhInstanceDocument.setAppDocTags(DhInstanceDocument.DOC_TAGS_DATAFORM);//表示是
		List<DhInstanceDocument> list = accessoryFileUploadServiceImpl.loadFileListByCondition(dhInstanceDocument);
		return ServerResponse.createBySuccess(list);
	}
}
