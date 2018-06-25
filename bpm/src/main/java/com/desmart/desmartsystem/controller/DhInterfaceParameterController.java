/**
 * 
 */
package com.desmart.desmartsystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartsystem.entity.DhInterfaceParameter;
import com.desmart.desmartsystem.service.DhInterfaceParameterService;

/**  
* <p>Title: 接口参数控制层</p>  
* <p>Description: </p>  
* @author zhaowei  
* @date 2018年4月17日  
*/
@Controller
@RequestMapping(value = "/interfaceParamers")
public class DhInterfaceParameterController {
	
	
	@Autowired
	private DhInterfaceParameterService dhInterfaceParameterService;
	
	
	
	//拼装参数配置页面设置的值	
	@RequestMapping(value = "/interfaceParameter")
	public ModelAndView interfaceParameter(String intUid){
		ModelAndView model=new ModelAndView("desmartsystem/interfaceParameter");
		model.addObject("interfaceParameterList",dhInterfaceParameterService.querybyintUid(intUid));
		return model;
	}
	
	@RequestMapping(value = "/index")
	@ResponseBody
	public ServerResponse listAll(@RequestParam(value="intUid") String intUid,@RequestParam(value="pageNum", defaultValue="1") Integer pageNum,@RequestParam(value="pageSize", defaultValue="100")Integer pageSize) {
		return dhInterfaceParameterService.listDhInterfaceParameter(intUid, pageNum, pageSize);
	}
	
	@RequestMapping(value = "/add")
	@ResponseBody
	public ServerResponse saveInterfaceParamers(@RequestBody List<DhInterfaceParameter> dhInterfaceParameterList) {
		ServerResponse s=null;
		try {
			s=dhInterfaceParameterService.saveDhInterfaceParametere(dhInterfaceParameterList);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return ServerResponse.createByErrorMessage("创建失败");
		}
		return s;
	}
	
	@RequestMapping(value = "/saveOrUpdate")
	@ResponseBody
	public ServerResponse saveOrUpdate(@RequestBody List<DhInterfaceParameter> dhInterfaceParameterList) {
		
		ServerResponse sr=null;
		try {
			sr=dhInterfaceParameterService.saveOrUpdate(dhInterfaceParameterList);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return ServerResponse.createByErrorMessage("修改失败");
		}
		return sr;
	}
	
	@RequestMapping(value = "/update")
	@ResponseBody
	public ServerResponse updateInterfaceParamers(DhInterfaceParameter dhInterfaceParameter) {
		ServerResponse r=null;
		try {
			r=dhInterfaceParameterService.updateDhInterfaceParametere(dhInterfaceParameter);
		} catch (Exception e) {
			return ServerResponse.createByErrorMessage("修改失败");
		}
		return r;
	}
	
	@RequestMapping(value = "/delete")
	@ResponseBody
	public ServerResponse deleteInterfaceParamers(String paraUid) {
		try {
			dhInterfaceParameterService.delDhInterfaceParameter(paraUid);
			return ServerResponse.createBySuccessMessage("删除成功");
		} catch (Exception e) {
			return ServerResponse.createByErrorMessage("删除失败");
		}
	}
	
	@RequestMapping(value = "/queryByparaId")
	@ResponseBody
	public DhInterfaceParameter queryByparaUid(String paraUid) {
		return dhInterfaceParameterService.selectByparaUid(paraUid);
	}
	
	
	@RequestMapping(value = "/byQueryParameter")
	@ResponseBody
	public List<DhInterfaceParameter> byQueryParameter(DhInterfaceParameter dhInterfaceParameter) {
		return dhInterfaceParameterService.byQueryParameter(dhInterfaceParameter);
	}
	
	@RequestMapping(value = "/homePage")
	public ModelAndView homePage(String intUid) {	
		System.err.println(intUid);
		ModelAndView mv = new ModelAndView("desmartsystem/index");
		mv.addObject("intUid", intUid);
		return mv;
	}
}
