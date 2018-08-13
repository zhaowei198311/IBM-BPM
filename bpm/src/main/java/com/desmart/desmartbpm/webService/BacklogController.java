package com.desmart.desmartbpm.webService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartportal.entity.DhTaskInstance;
import com.desmart.desmartportal.service.DhTaskInstanceService;
import com.github.pagehelper.PageInfo;

/**
 * 门户集成获取待办数量
 * @author zbw
 *
 */
@Controller
@RequestMapping(value="/backLog")
public class BacklogController {
	
	@Autowired
	private DhTaskInstanceService dhTaskInstanceService;
	
	/*@Autowired
	private BpmGlobalConfigService bpmGlobalConfigService;*/
	
	/**
	 * 根据用户id和传入的size获取改用户的待办任务数量
	 * @param userId  用户id
	 * @param size    需要查询的数量
	 * @return  待办任务list集合
	 */
	@ResponseBody
	@RequestMapping(value="/queryByUserId")
	public List<JSONObject> getBackLogByUserId(@RequestParam(value="userId")String userId, @RequestParam(value="size",defaultValue="10")int size){
		//封装查询实体类 //List<DhTaskInstance>
		DhTaskInstance dhTaskInstance = new DhTaskInstance();
		dhTaskInstance.setUsrUid(userId);
		//按条件检索
		ServerResponse<PageInfo<List<DhTaskInstance>>> result = dhTaskInstanceService.selectBackLogTaskInfoByCondition(null, null, dhTaskInstance, 1, size, null);
		List<JSONObject> resultList = getBackLogByUserIdUtil(result);
		
		//List<DhTaskInstance> list = data.getList();
		return resultList;
	}

	
	/**
	 * 根据检索出来的结果封装返回list内容
	 * @param result
	 * @return
	 */
	public List<JSONObject> getBackLogByUserIdUtil(ServerResponse<PageInfo<List<DhTaskInstance>>> result){
		
		//返回结果，jsonObject集合
		List<JSONObject> resultList = new ArrayList<JSONObject>();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//拼接封装参数中的url
		//BpmGlobalConfig firstActConfig = bpmGlobalConfigService.getFirstActConfig();
		//解析目标参数转为目的参数
		PageInfo<List<DhTaskInstance>> data = result.getData();
		List<List<DhTaskInstance>> list = data.getList();
		for (int i = 0; i < list.size(); i++) {
			//String name = list.get(i).getClass().getName();
			DhTaskInstance dhTaskInstance = (DhTaskInstance)list.get(i);
			JSONObject json = new JSONObject();
			json.put("appdesc", "BPM");
			json.put("description", "");
			json.put("id", dhTaskInstance.getTaskId());
			json.put("publish", dhTaskInstance.getTaskHandler());
			json.put("publishDate", format.format(dhTaskInstance.getTaskInitDate()));
			json.put("tableid", "");
			json.put("tablename", "");
			json.put("taskdesc", dhTaskInstance.getTaskTitle());
			json.put("title", "BPM" + dhTaskInstance.getTaskTitle());
			json.put("status", getInstanceStatus(String.valueOf(dhTaskInstance.getTaskStatus())));
			String url = "/menus/approval?taskUid=" + dhTaskInstance.getTaskUid();
			json.put("url", url);
			resultList.add(json);
		}
		return resultList;
	}
	
	
	/**
	 * 获取任务类型中文描述
	 * @param stutes
	 * @return
	 */
	public String getInstanceStatus(String stutes) {
	
		if("-2".equals(stutes)) {
			return "等待加签结束";
		}else if("-3".equals(stutes)){
			return "等待上一个人加签结束";
	    }else if("11".equals(stutes)){
		    return "新任务";
		}else if("12".equals(stutes)){
			return "接收到任务";
		}else if("13".equals(stutes)){
			return "回复任务";
		}else if("14".equals(stutes)){
			return "转发任务";
		}else if("21".equals(stutes)){
			return "传送任务";
		}else if("31".equals(stutes)){
			return "运行中";
		}else if("32".equals(stutes)){
			return "任务关闭";
		}else if("-1".equals(stutes)){
			return "任务废弃";
		}else if("41".equals(stutes)){
			return "特殊任务";
		}else if("91".equals(stutes)){
			return "删除任务";
		}else if("61".equals(stutes)){
			return "警报任务";
		}else if("62".equals(stutes)){
			return "帮助请求任务";
		}else if("63".equals(stutes)){
			return "说明任务";
		}else if("65".equals(stutes)){
			return "已应答帮助请求任务";
		}else if("66".equals(stutes)){
			return "忽略帮助请求任务";
		}else if("70".equals(stutes)){
			return "继承任务";
		}else if("71".equals(stutes)){
			return "标记任务";
		}else if("72".equals(stutes)){
			return "协作任务";
		}else if("P".equals(stutes)){
			return "标记任务暂停";
		}else {
			return "";
		}
		
	}
	//[{"appdesc":"办公用品部门计划","description":"","id":"1376","publish":"胡晨杰","publishDate":"2018-04-18 17:23:52","status":"关闭","tableid":"mrpid","tablename":"MRP","taskdesc":"主管人审核部门办公用品月度计划","title":"MRP--主管人审核部门办公用品月度计划","url":"":"http://eam.laiyifen.com:9082/maximo/login/autologinservlet?username=00011178&type=email&value=MRP&uniqueid=1376"}]"}]
}
