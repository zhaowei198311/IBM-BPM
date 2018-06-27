package com.desmart.desmartportal.controller;

import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.desmart.common.constant.ServerResponse;
import com.desmart.common.util.HtmlToPdf;
import com.desmart.desmartportal.service.FinishFormPrintService;

/**
 * 代办页面表单打印控制器
 * @author loser_wu
 * @since 2018年6月26日
 */
@Controller
@RequestMapping(value = "/finishFormPrint")
public class FinishFormPrintController {
	@Autowired
	private FinishFormPrintService finishFormPrintService;
	
	@RequestMapping("/toPDF")
	@ResponseBody
	public ServerResponse goPDF(HttpServletRequest request,String webpage) {
		//这里ip和端口号暂时先写在这
		String formUid = UUID.randomUUID().toString();
		finishFormPrintService.saveFormPrintContent(formUid, webpage);
		String path = "127.0.0.1:8088/bpm/finishFormPrint/toPrint?formUid="+formUid;
		String pdfPath = request.getSession().getServletContext().getRealPath("/resources/form");
		String pdfName = formUid + ".pdf";
		if (HtmlToPdf.convert(path, pdfPath + "\\" + pdfName)) {
			String destPath = "http://127.0.0.1:8088/" + request.getContextPath() + "/resources/form/" + pdfName;
			try {
	            URL url=new URL(destPath);
	            URLConnection conn=url.openConnection();
	            String str=conn.getHeaderField(0);
	            System.out.println(str);
	            if (str.indexOf("200")> 0){
	                return ServerResponse.createBySuccess(destPath);
	            }else{
	                return ServerResponse.createByError();
	            }
	        } catch (Exception ex) {
	        	ex.printStackTrace();
	            return ServerResponse.createByError();
	        }
		}else {
			return ServerResponse.createByError();
		}
	}
	
	@RequestMapping(value = "/toPrint")
	public ModelAndView toPrint(String formUid) {
		ModelAndView mv = new ModelAndView("desmartportal/print");
		ServerResponse response = finishFormPrintService.queryPrintContentByFormUid(formUid);
		if(!response.isSuccess()) {
			mv.setViewName("/desmartbpm/error");
	        mv.addObject("errorMessage", response.getMsg());
		}else {
			String webpage = (String) response.getData();
			mv.addObject("webpage", webpage);
		}
		finishFormPrintService.deletePrintContentByFormUid(formUid);
		return mv;
	}
}
