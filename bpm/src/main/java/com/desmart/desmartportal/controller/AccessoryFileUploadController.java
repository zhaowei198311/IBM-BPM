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
			@RequestParam("taskId") String taskId,
			@RequestParam("activityId")String activityId) {
		return accessoryFileUploadServiceImpl.saveFile(multipartFiles, uploadModels, appUid, taskId,activityId);
	}

	@RequestMapping(value = "loadFileList.do")
	@ResponseBody
	public ServerResponse<List<DhInstanceDocument>> loadFileList(DhInstanceDocument dhInstanceDocument) {// 加载已上传附件列表
		dhInstanceDocument.setAppDocStatus("normal");// normal表示没有被删除
		dhInstanceDocument.setAppDocIsHistory(Const.Boolean.FALSE);// 表示不是历史文件
		List<DhInstanceDocument> list = accessoryFileUploadServiceImpl.loadFileListByCondition(dhInstanceDocument);
		return ServerResponse.createBySuccess(list);
	}

	@RequestMapping("singleFileDown.do")
	public void singleFileDown(DhInstanceDocument dhInstanceDocument, HttpServletResponse response) {
		// System.out.println(dhInstanceDocument.getAppDocFileName());
		response.setContentType("text/html; charset=UTF-8"); // 设置编码字符
		response.setContentType("application/x-msdownload"); // 设置内容类型为下载类型
		OutputStream out = null;
		try {

			response.setHeader("Content-disposition", "attachment;filename="
					+ new String(dhInstanceDocument.getAppDocFileName().getBytes(), "ISO-8859-1"));// 设置下载的文件名称
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
			@RequestParam("activityId")String activityId) {
		return accessoryFileUploadServiceImpl.deleteAccessoryFile(dhInstanceDocument,activityId);
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
				response.setContentType("text/html; charset=UTF-8"); // 设置编码字符
				response.setContentType("application/x-msdownload"); // 设置内容类型为下载类型
				OutputStream out = null;
				try {
					response.setHeader("Content-disposition",
							"attachment;filename=" + new String("流程附件".getBytes(), "ISO-8859-1")+".zip");// 设置下载的文件名称
					out = response.getOutputStream(); // 创建页面返回方式为输出流，会自动弹出下载框

					// 创建压缩文件需要的空的zip包
					String zipBasePath = request.getSession().getServletContext()
							.getRealPath("/resources/desmartportal/upload/zip/");
					String zipFilePath = zipBasePath + "temp.zip";
					// 根据临时的zip压缩包路径，创建zip文件
					File zip = new File(zipFilePath);
					if (!zip.exists()) {
						zip.createNewFile();
					}
					// 创建zip文件输出流
					FileOutputStream fos = new FileOutputStream(zip);
					ZipOutputStream zipOut = new ZipOutputStream(fos);
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
						sftp.getBatchDown(gcfg, directory, filename, zipOut);

					}
					fos.flush();  
					zipOut.flush();  
					zipOut.close();
					fos.close();
					// 将打包后的文件写到客户端，使用缓冲流输出
					InputStream fis = new BufferedInputStream(new FileInputStream(zipFilePath));
					//计算百分比
					Integer totalCount = fis.available();//获得文件总大小
					Integer curCount = 0;
					byte[] buff = new byte[4096];
					int size = 0;
					while ((size = fis.read(buff)) != -1) {
						curCount+=size;
						out.write(buff, 0, size);
					}
					
					 double dPercent=(double)curCount/totalCount;   //将计算出来的数转换成double
			            int percent=(int)(dPercent*100);               //再乘上100取整
			            request.getSession().setAttribute("totalCount",totalCount);
			            request.getSession().setAttribute("curCount", curCount);
			            request.getSession().setAttribute("percent", percent);    //比如这里是50
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
			@RequestParam("activityId")String activityId) {
		if (dhInstanceDocument.getAppDocIdCard() == null || "".equals(dhInstanceDocument.getAppDocIdCard())) {
			return ServerResponse.createByErrorMessage("更新文件异常！");
		} else {
			return accessoryFileUploadServiceImpl.updateAccessoryFile(multipartFile, dhInstanceDocument,activityId);
		}
	}
	@RequestMapping("loadHistoryFile.do")
	@ResponseBody
	public ServerResponse loadHistoryFile(DhInstanceDocument dhInstanceDocument) {
		dhInstanceDocument.setAppDocIsHistory(Const.Boolean.TRUE);
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
}
