package com.desmart.test.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartportal.common.EntityIdPrefix;
import com.desmart.desmartportal.dao.DhInstanceDocumentMapper;
import com.desmart.desmartportal.entity.DhInstanceDocument;
import com.desmart.desmartportal.util.DateUtil;
import com.desmart.desmartportal.util.SFTPUtil;
import com.desmart.desmartportal.util.UUIDTool;
import com.desmart.desmartsystem.service.BpmGlobalConfigService;

@Controller
@RequestMapping("/wPaintTest")
public class WPaintTestController {
	@Autowired
	private BpmGlobalConfigService bpmGlobalConfigService;
	@Autowired
	private DhInstanceDocumentMapper dhInstanceDocumentMapper;
	
	@RequestMapping("/toWPaintTest")
	public ModelAndView toWPaintTest() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/desmartportal/wPaintTest");
		return modelAndView;
	}
	
	@RequestMapping("/loadImageData")
	@ResponseBody
	public ServerResponse loadImageData(DhInstanceDocument dhInstanceDocument,
				HttpServletRequest request) {
		dhInstanceDocument = dhInstanceDocumentMapper
					.selectByPrimaryKey(dhInstanceDocument.getAppDocUid());
		try {
		SFTPUtil sftp = new SFTPUtil();
		String directory = dhInstanceDocument.getAppDocFileUrl().substring(0,
				dhInstanceDocument.getAppDocFileUrl().lastIndexOf("/") + 1);
		String filename = dhInstanceDocument.getAppDocFileUrl().substring(
				dhInstanceDocument.getAppDocFileUrl().lastIndexOf("/") + 1,
				dhInstanceDocument.getAppDocFileUrl().length());
		String demoImagePath = request.getSession().getServletContext()
				.getRealPath("/resources/desmartportal/upload/demo/");
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
	
	@RequestMapping("/uploadEditData")
	@ResponseBody
	public ServerResponse uploadEditData(@RequestBody Map map) {
		String appDocUid = map.get("appDocUid").toString();
		DhInstanceDocument dhInstanceDocument = dhInstanceDocumentMapper.selectByPrimaryKey(appDocUid);
		
		String creator = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String currTime = simpleDateFormat.format(new Date());
		Date currentDate = DateUtil.format(new Date());
		
		String directory = "/AccessoryFile/";
        String[] dateStrings = currTime.split("-");
        directory+=dateStrings[0]+"/"+dateStrings[1]+"/"+dateStrings[2]+"/";//获取文件上传目录
       
        //对当前文件修改-修改人-修改时间-历史
        dhInstanceDocument.setUpdateUserUid(creator);
		dhInstanceDocument.setAppDocUpdateDate(currentDate);
		dhInstanceDocument.setAppDocIsHistory(Const.Boolean.TRUE);
		Integer count = dhInstanceDocumentMapper.updateFileByPrimaryKey(dhInstanceDocument);
        
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
		dhInstanceDocumentMapper.insertDhInstanceDocuments(insert);
		
		String imageData = map.get("image").toString();
		byte[] decoder = Base64.decodeBase64(imageData.replace("data:image/png;base64,","").getBytes());
		try {
			InputStream newInput = new ByteArrayInputStream(decoder);  
			SFTPUtil sftp = new SFTPUtil();
			sftp.upload(bpmGlobalConfigService.getFirstActConfig()
					, directory, newFileName, newInput);
			
			//删除加载出来的临时文件
			String absoulteImgPath = map.get("absoulteImgPath").toString();
			File file = new File("liuguilin");
	        // 判断文件是否存在
	        if (file.exists()) {
	            // 再去判断文件还是文件夹
	            if (file.isFile()) {
	                file.delete();
	            }
	        } 
			return ServerResponse.createBySuccessMessage("保存修改成功！");
		} catch (Exception e) {
			return ServerResponse.createByErrorMessage("保存修改失败！");
		}
	}
	
	/**
	 * 获得输入流中的内容
	 */
	private String readInputStream(InputStream inputStream) throws IOException {  
        byte[] buffer = new byte[1024];  
        int len = 0;  
        ByteArrayOutputStream bos = new ByteArrayOutputStream();  
        while((len = inputStream.read(buffer)) != -1) {  
            bos.write(buffer, 0, len);  
        }  
        bos.close();  
        return new String(bos.toByteArray(),"UTF-8");
    } 
	
	
	
}
