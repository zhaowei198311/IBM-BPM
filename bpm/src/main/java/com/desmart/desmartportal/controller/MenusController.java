/**
 * 
 */
package com.desmart.desmartportal.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.desmart.common.constant.ServerResponse;
import com.desmart.common.util.BpmProcessUtil;
import com.desmart.common.util.RequestSourceUtil;
import com.desmart.desmartbpm.service.DhProcessRetrieveService;
import com.desmart.desmartportal.common.Const;
import com.desmart.desmartportal.dao.DhTaskInstanceMapper;
import com.desmart.desmartportal.entity.DhTaskInstance;
import com.desmart.desmartportal.service.DhDraftsService;
import com.desmart.desmartportal.service.DhProcessInstanceService;
import com.desmart.desmartportal.service.DhTaskInstanceService;
import com.desmart.desmartsystem.entity.BpmGlobalConfig;
import com.desmart.desmartsystem.service.BpmGlobalConfigService;

/**
 * <p>
 * Title: 跳转WEB-INFO下jsp页面的 控制层 带参
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author zhaowei
 * @date 2018年4月27日
 */
@Controller
@RequestMapping("/menus")
public class MenusController {
	
	@Autowired
	private DhTaskInstanceService dhTaskInstanceService;
	@Autowired
	private DhDraftsService dhDraftsService;
	@Autowired
	private DhProcessInstanceService dhProcessInstanceService;
	@Autowired
	private BpmGlobalConfigService bpmGlobalConfigService;
	@Autowired
	private DhTaskInstanceMapper dhTaskInstanceMapper;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private DhProcessRetrieveService dhProcessRetrieveService;
	
	@RequestMapping("/index")
	public String index() {
		return "desmartportal/index";
	}


	/**
	 * 跳转到待办列表页面
	 * @return
	 */
	@RequestMapping("/backlog")
	public ModelAndView backlog() {
		ModelAndView mv = new ModelAndView();
		boolean isMobile = RequestSourceUtil.isMobileDevice(request.getHeader("user-Agent"));
		if(isMobile) {
			// 移动端普通的待办任务
			mv.setViewName("desmartportal/mobile_backlog");
		}else {
			// 普通的待办任务
			mv.setViewName("desmartportal/backlog");
		}
		return mv;
	}

	
	/**
	 * 跳转到发起流程的页面
	 * @param proUid 流程id
	 * @param proAppId 流程应用库id
	 * @return
	 */
	@RequestMapping("/startProcess")
	public ModelAndView startProcess(@RequestParam(value = "proUid",required = false) String proUid,
			@RequestParam(value = "proAppId",required = false) String proAppId, @RequestParam(value = "insUid",required = false) String insUid
			,String insBusinessKey) {
	    ModelAndView mv = new ModelAndView("desmartportal/process");
	    ServerResponse<Map<String, Object>> startResponse = dhProcessInstanceService.toStartProcess(proAppId, proUid, insUid,insBusinessKey);
	    if (startResponse.isSuccess()) {
	        mv.addAllObjects(startResponse.getData());
	    } else {
	        mv.setViewName("/desmartbpm/error");
	        mv.addObject("errorMessage", startResponse.getMsg());
	    }
		return mv;
	}
	
	/**
	 * 
	 * @Title: processInstance  
	 * @Description: 门店生命周期-流程实例页面  
	 * @param @param proUid
	 * @param @param proAppId
	 * @param @return  
	 * @return ModelAndView  
	 */
	@RequestMapping("/processInstanceByUser")
	public ModelAndView processInstance(@RequestParam(value = "proUid",required = false) String proUid,
										@RequestParam(value = "proAppId",required = false) String proAppId,
										@RequestParam(value = "proName",required = false) String proName) {
		
		ModelAndView mv = new ModelAndView("desmartportal/processInstance");
		ServerResponse serverResponse = dhProcessRetrieveService.assembleProcessRetrieveList(proAppId,proUid);
		if(serverResponse.isSuccess()) {
			mv.addObject("processRetrieveList",serverResponse.getData());
		}
		mv.addObject("proUid", proUid);
		mv.addObject("proAppId", proAppId);
		mv.addObject("proName", proName);
		return mv;
	}
	
	/**
	 * 展示流程实例对应的相关任务
	 * @Title: tasksOfProcessInstance  
	 * @Description:
	 * @param @param insUid
	 * @param @return  
	 * @return ModelAndView  
	 */
	@RequestMapping("/tasksOfProcessInstance")
	public ModelAndView processType(String insUid) {
		ModelAndView mv = new ModelAndView("desmartportal/tasksOfProcessInstance");
		mv.addObject("insUid", insUid);
//		mv.addObject("proUid", proUid);
//		mv.addObject("proAppId", proAppId);
//		mv.addObject("verUid", verUid);
//		mv.addObject("proName", proName);
//		mv.addObject("categoryName", categoryName);
		return mv;
	}
	
	@RequestMapping("queryProcess")
	public ModelAndView queryProcess() {
		ModelAndView mv = new ModelAndView("desmartportal/query_process");
		return mv;
	}
	
	@RequestMapping("finishProcess")
	public ModelAndView finishProcess() {
		ModelAndView mv = new ModelAndView("desmartportal/finished");
		return mv;
	}
	
	@RequestMapping("notRedProcess")
	public ModelAndView notRedProcess() {
		ModelAndView mv = new ModelAndView("desmartportal/not_read");
		return mv;
	}
	
	
	@RequestMapping("draftsInfo")
	public ModelAndView draftsInfo(@RequestParam(value = "insUid",required = false) String insUid,
			@RequestParam(value = "dfsId",required = false) String dfsId) {
		ModelAndView mv = new ModelAndView("desmartportal/drafts_info");
		mv.addAllObjects(dhDraftsService.selectDraftsAndFromInfo(dfsId, insUid));
		return mv;
	}
	
	/**
	 * 去待办明细页面
	 * @param taskUid
	 * @return
	 */
	@RequestMapping("approval")
	public ModelAndView toDealTask(@RequestParam(value="taskUid") String taskUid) {
		ModelAndView mv = new ModelAndView();
		// 判断该任务是否为加签任务
		DhTaskInstance checkDhTaskInstance = dhTaskInstanceMapper.selectByPrimaryKey(taskUid);
		String taskType = checkDhTaskInstance.getTaskType();
		if (DhTaskInstance.TYPE_NORMAL_ADD.equals(taskType) || DhTaskInstance.TYPE_SIMPLE_LOOPADD.equals(taskType)
				|| DhTaskInstance.TYPE_MULTI_INSTANCE_LOOPADD.equals(taskType)) {
			if(RequestSourceUtil.isMobileDevice(request.getHeader("user-Agent"))) {
				// 如果是会签的任务
				mv.setViewName("desmartportal/mobile_addSign");
			}else {
				// 如果是会签的任务
				mv.setViewName("desmartportal/addSign");
			}
			
			ServerResponse<Map<String, Object>> serverResponse = dhTaskInstanceService.toAddSign(taskUid);
			if (serverResponse.isSuccess()) {
	            mv.addAllObjects(serverResponse.getData());
	        } else {
	            mv.setViewName("/desmartbpm/error");
	            mv.addObject("errorMessage", serverResponse.getMsg());
	        }
			return mv;
		}
		
		String currUserUid = (String)SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
		// 等待加签  代理给人家的任务  跳转 已办详情页面
		if(DhTaskInstance.STATUS_WAIT_ALL_ADD_FINISH.equals(checkDhTaskInstance.getTaskStatus())
			|| (checkDhTaskInstance.getTaskDelegateUser()!=null 
				&& !"".equals(checkDhTaskInstance.getTaskDelegateUser())
				&& !currUserUid.equals(checkDhTaskInstance.getTaskDelegateUser()))
			) {
			if(RequestSourceUtil.isMobileDevice(request.getHeader("user-Agent"))) {
				mv.setViewName("desmartportal/mobile_finished_detail");
			}else {
				mv.setViewName("desmartportal/finished_detail");
			}
			ServerResponse<Map<String, Object>> serverResponse = dhTaskInstanceService.toFinshedTaskDetail(taskUid);
			if (serverResponse.isSuccess()) {
	            mv.addAllObjects(serverResponse.getData());
	        } else {
	            mv.setViewName("/desmartbpm/error");
	            mv.addObject("errorMessage", serverResponse.getMsg());
	        }
			return mv;
		}
		
		boolean isMobile = RequestSourceUtil.isMobileDevice(request.getHeader("user-Agent"));
		if(isMobile) {
			// 移动端普通的待办任务
			mv.setViewName("desmartportal/mobile_approval");
		}else {
			// 普通的待办任务
			mv.setViewName("desmartportal/approval");
		}
		ServerResponse<Map<String, Object>> serverResponse = dhTaskInstanceService.toDealTask(taskUid);
		if (serverResponse.isSuccess()) {
            mv.addAllObjects(serverResponse.getData());
        } else {
            mv.setViewName("/desmartbpm/error");
            mv.addObject("errorMessage", serverResponse.getMsg());
        }
		return mv;
	}
	
	@RequestMapping("viewProcessImage")
	public ModelAndView viewProcessImage(@RequestParam(value="insId") String insId) {
		ModelAndView mv = new ModelAndView("desmartportal/viewProcessImage");
		mv.addObject("insId",insId);
		return mv;
	}
	
	@RequestMapping("test")
	public void test() {
        BpmGlobalConfig bpmGlobalConfig = bpmGlobalConfigService.getFirstActConfig();
        BpmProcessUtil bpmProcessUtil = new BpmProcessUtil(bpmGlobalConfig);
        // bpmProcessUtil.rejectProcess(661, "bpdid:9ba2e5f1271c68f2:-1a57422b:163382b7a4a:-7ff3", "00011178");
	}
	
	/**
	 * 已办明细页面
	 */
	@RequestMapping("/finshed_detail")
	public ModelAndView toFinshedDetail(@RequestParam(value="taskUid") String taskUid) {
		ModelAndView mv = new ModelAndView("desmartportal/finished_detail");
		if(RequestSourceUtil.isMobileDevice(request.getHeader("user-Agent"))) {
			mv.setViewName("desmartportal/mobile_finished_detail");
		}
		ServerResponse<Map<String, Object>> serverResponse = dhTaskInstanceService.toFinshedTaskDetail(taskUid);
		if (serverResponse.isSuccess()) {
            mv.addAllObjects(serverResponse.getData());
        } else {
            mv.setViewName("/desmartbpm/error");
            mv.addObject("errorMessage", serverResponse.getMsg());
        }
		return mv;
	}
}
