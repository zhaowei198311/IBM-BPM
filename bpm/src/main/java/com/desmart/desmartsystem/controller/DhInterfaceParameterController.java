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
	public ServerResponse listAll(@RequestParam(value="intUid") String intUid,@RequestParam(value="pageNum", defaultValue="1") Integer pageNum,@RequestParam(value="pageSize", defaultValue="10")Integer pageSize) {
		return dhInterfaceParameterService.listDhInterfaceParameter(intUid, pageNum, pageSize);
	}
	
	@RequestMapping(value = "/add")
	@ResponseBody
	public ServerResponse saveInterfaceParamers(@RequestBody List<DhInterfaceParameter> dhInterfaceParameterList) {
		
		try {
			dhInterfaceParameterService.saveDhInterfaceParametere(dhInterfaceParameterList);
			return ServerResponse.createBySuccessMessage("添加成功");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return ServerResponse.createByErrorMessage("添加失败");
		}
	}
	
	@RequestMapping(value = "/saveOrUpdate")
	@ResponseBody
	public ServerResponse saveOrUpdate(@RequestBody List<DhInterfaceParameter> dhInterfaceParameterList) {
		try {
			dhInterfaceParameterService.saveOrUpdate(dhInterfaceParameterList);
			return ServerResponse.createBySuccessMessage("修改成功");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return ServerResponse.createByErrorMessage("添加失败");
		}
	}
	
	@RequestMapping(value = "/update")
	@ResponseBody
	public ServerResponse updateInterfaceParamers(DhInterfaceParameter dhInterfaceParameter) {
		try {
			dhInterfaceParameterService.updateDhInterfaceParametere(dhInterfaceParameter);
			return ServerResponse.createBySuccessMessage("修改成功");
		} catch (Exception e) {
			return ServerResponse.createByErrorMessage("修改失败");
		}
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
