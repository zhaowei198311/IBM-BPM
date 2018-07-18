/**
 * 
 */
package com.desmart.desmartsystem.controller;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartsystem.entity.DhInterface;
import com.desmart.desmartsystem.entity.DhInterfaceParameter;
import com.desmart.desmartsystem.service.DhInterfaceParameterService;
import com.desmart.desmartsystem.service.DhInterfaceService;

/**
 * <p>
 * Title: DhInterfaceController
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author zhaowei
 * @date 2018年4月12日
 */
@Controller
@RequestMapping(value = "/interfaces")
public class DhInterfaceController { 

	@Autowired
	private DhInterfaceService dhInterfaceService;
	@Autowired
	private DhInterfaceParameterService dhInterfaceParameterService;

	@RequestMapping(value = "/index")
	public ModelAndView index() {
		ModelAndView modev = new ModelAndView("desmartsystem/interfaceManagement");
		return modev;
	}
	
	@RequestMapping(value = "/interfaceTest")
	public ModelAndView interfaceTest(String intUid,String intTitle,String paraInOut) {
       String encode = "iso8859-1";      
       try {      
           if (intTitle.equals(new String(intTitle.getBytes(encode), encode))) {      //判断是不是GB2312
        	   intTitle=new String(intTitle.getBytes("iso8859-1"),"utf-8");
            }      
        } catch (Exception e) {      
        	e.printStackTrace();
        }      
		ModelAndView modev = new ModelAndView("desmartsystem/usermanagement/interface/interfaceTest");
		modev.addObject("intUid", intUid);
		modev.addObject("intTitle", intTitle);
		
		
		//接口参数查询参数
		DhInterfaceParameter dhInterfaceParameter=new DhInterfaceParameter();
		dhInterfaceParameter.setIntUid(intUid);
		dhInterfaceParameter.setParaInOut(paraInOut);
		
		
		modev.addObject("dhInterfaceParameterList", dhInterfaceParameterService.byQueryParameter(dhInterfaceParameter));
		return modev;
	}
	
	@RequestMapping(value = "/queryDhInterfaceList")
	@ResponseBody
	public ServerResponse queryDhInterface(@RequestParam(value="pageNum", defaultValue="1") Integer pageNum,@RequestParam(value="pageSize", defaultValue="10")Integer pageSize,String intStatus) {
        // todo
        return  dhInterfaceService.listDhInterfaceByStatus(pageNum, pageSize, intStatus);
	}

	@RequestMapping(value = "/add")
	@ResponseBody
	public  ServerResponse addDhInterface(DhInterface dhInterface) {
		try {
			dhInterfaceService.saveDhInterface(dhInterface);
			return ServerResponse.createBySuccessMessage("添加成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ServerResponse.createByErrorMessage("添加失败!");
		}
	}

	@RequestMapping(value = "/del")
	@ResponseBody
	public ServerResponse deleteDhInterface(@RequestParam(value = "intUid") String interfaceId) {
		try {
			dhInterfaceService.delDhInterface(interfaceId);
			return ServerResponse.createBySuccessMessage("删除成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ServerResponse.createByErrorMessage("删除失败!");
		}
	}
	
	
	@RequestMapping(value = "/update")
	@ResponseBody
	public ServerResponse updateDhInterface(DhInterface dhInterface) {
		try {
			dhInterfaceService.updateDhInterface(dhInterface);
			return ServerResponse.createBySuccessMessage("修改成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ServerResponse.createByErrorMessage("修改失败!");
		}
	}
	
	@RequestMapping(value = "/queryDhInterfaceById")
	@ResponseBody
	public DhInterface selectDhInterfaceByid(@RequestParam(value = "intUid")String intUid) {
		return dhInterfaceService.selectDhInterfaceByid(intUid);
	}
	
	@RequestMapping(value = "/queryDhInterfaceByTitle")
	@ResponseBody
	public ServerResponse listDhInterfaceByTitle(
			@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,String intTitle,String intType,String intStatus) {
		Map<String, Object> paramsMap = new HashMap<>();
		paramsMap.put("intTitle", intTitle);
		paramsMap.put("intType", intType);
		paramsMap.put("intStatus", intStatus);
		return dhInterfaceService.listDhInterfaceByTitle(paramsMap, pageNum, pageSize);
	}
}
