package com.desmart.desmartbpm.controller;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.desmart.common.constant.ServerResponse;
import com.desmart.common.util.DateTimeUtil;
import com.desmart.desmartbpm.entity.DhTransferData;
import com.desmart.desmartbpm.service.*;
import com.desmart.desmartportal.dao.DhTaskInstanceMapper;
import com.desmart.desmartportal.entity.DhProcessInstance;
import com.desmart.desmartportal.entity.DhTaskInstance;
import com.desmart.desmartportal.service.DhProcessInstanceService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.desmart.common.util.BpmTaskUtil;
import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartbpm.common.HttpReturnStatus;
import com.desmart.desmartbpm.util.http.BpmTaskUtils;
import com.desmart.desmartportal.entity.CommonBusinessObject;
import com.desmart.desmartportal.entity.BpmRouteConditionResult;
import com.desmart.desmartsystem.entity.BpmGlobalConfig;
import com.desmart.desmartsystem.service.BpmGlobalConfigService;

@Controller
public class TestController extends BaseWebController {
    @Autowired
    private TestService testService;
    @Autowired
    private BpmGlobalConfigService bpmGlobalConfigService; 
    @Autowired
    private BpmProcessSnapshotService bpmProcessSnapshotService;
    @Autowired
    private DhProcessMetaService dhProcessService;
    @Autowired
    private SynchronizeTaskService synchronizeTaskService;
    @Autowired
    private DhTaskInstanceMapper dhTaskInstanceMapper;
    @Autowired
    private DhTransferService dhTransferService;
    @Autowired
    private DhTriggerStepService dhTriggerStepService;
    @Autowired
    private DhProcessInstanceService dhProcessInstanceService;
    @Autowired
    private DhTaskExceptionResolverService dhTaskExceptionResolverService;


    
    @RequestMapping(value = "/test")
    @ResponseBody
    public String test3(HttpServletRequest request, Integer page, Integer rows) {
        BpmTaskUtil bpmTaskUtil = new BpmTaskUtil(bpmGlobalConfigService.getFirstActConfig());
        CommonBusinessObject pubBo = new CommonBusinessObject();
        List<String> list1 = new ArrayList<>();
        list1.add("deadmin");
        list1.add("2222222");
        pubBo.setNextOwners_0(list1);
        Map<String, HttpReturnStatus> resultMap = bpmTaskUtil.commitTask(1517, pubBo, null);
        System.out.println(resultMap.get("commitTaskResult").getMsg());
        return "done";
    }
    
    
    @RequestMapping(value = "/error.do")
    @ResponseBody
    public String error(String username, String password) {
        return "error";
    }

    @RequestMapping(value = "/toLogin.do")
    public ModelAndView toLogin() {
        return new ModelAndView("desmartbpm/login");
    }

    @RequestMapping(value = "/user/login.do")
    @ResponseBody
    public String login(String username) {
        Subject user = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("caocao", "caocao");
        user.login(token);
        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute(Const.CURRENT_USER, username);
        session.setTimeout(17200000L);

        return "hello " + username;
    }

    @RequestMapping(value = "/document/open.do")
    @ResponseBody
    public String openDocument(String docId) {
        return "opened " + docId;
    }

    @RequestMapping(value = "/document/update.do")
    @ResponseBody
    public String updateDocument(String docId) {
        return "update " + docId;
    }

    @RequestMapping(value = "/task/submit.do")
    @ResponseBody
    public String submitTask(String taskId) {
        return "task submited " + taskId;
    }

    @RequestMapping(value = "/url1.do")
    @ResponseBody
    public String url1() {
        Subject user = SecurityUtils.getSubject();
        String _curruser = (String) user.getSession().getAttribute(Const.CURRENT_USER_NAME);
        System.out.println(user.getSession().getTimeout());
        testService.method1();
        return "hello," + _curruser + "url1 success";
    }

    @RequestMapping(value = "/url2.do")
    @ResponseBody
    public String url2() {
        Subject user = SecurityUtils.getSubject();
        String _curruser = (String) user.getSession().getAttribute(Const.CURRENT_USER_NAME);
        System.out.println(user.getSession().getTimeout());
        System.out.println(user.getSession().getLastAccessTime());
        return "hello," + _curruser + "url2 success";
    }

    @RequestMapping(value = "/unauthorizedUrl.do")
    @ResponseBody
    public String url3() {
        Subject user = SecurityUtils.getSubject();
        String _curruser = (String) user.getSession().getAttribute(Const.CURRENT_USER_NAME);
        System.out.println(user.getSession().getTimeout());
        System.out.println(user.getSession().getLastAccessTime());
        return "hello," + _curruser + " this is  unauthorizedUrl ";
    }


    
    @RequestMapping(value = "/test.do")
    @ResponseBody
    public Object test(HttpServletRequest request) {
        //bpmProcessSnapshotService.processModel(request, "25.0e089867-84d7-489b-a114-2f8d92c226a9", "", "2066.49fe4fdc-8488-4663-92d1-d1befcb671c2");
        bpmProcessSnapshotService.processModel("25.0e089867-84d7-489b-a114-2f8d92c226a9", "", "2066.49fe4fdc-8488-4663-92d1-d1befcb671c2");
        return "Success";
    }
    
    // localhost:8088/desmartbpm/generateActivity.do?bpdid=25.96218c34-781b-4992-b335-3504146e69eb&snapshoutId=2064.9303b288-e202-465f-9742-f042b5521fa5&processAppid=2066.49fe4fdc-8488-4663-92d1-d1befcb671c2&newVersionId=v1.0
    @RequestMapping(value = "/generateActivity.do")
    @ResponseBody
    public String test(HttpServletRequest request, String bpdid, String snapshoutId, String processAppid, String newVersionId) {
    	bpmProcessSnapshotService.processModel(bpdid, snapshoutId, processAppid);
        return "success";
    }
    
    @RequestMapping(value = "/getManger")
    @ResponseBody
    public String getManager(HttpServletRequest request) {
    	Object obj = request.getServletContext().getAttribute(Const.HTTP_CLIENT_CONNECTION_POOL);
    	System.out.println(obj);
    	return "done";
    }
    
    @RequestMapping(value = "/testgetTaskData")
    @ResponseBody
    public String testGetData(String taskId, HttpServletRequest request) {
    	BpmGlobalConfig config = bpmGlobalConfigService.getFirstActConfig();
    	BpmTaskUtils taskUtils = new BpmTaskUtils(config, request.getServletContext());
    	HttpReturnStatus status = taskUtils.getTaskData(request, "1861");
    	System.out.println(status.getMsg());
        JSONObject jsonResult = new JSONObject(status.getMsg());
        JSONObject temp = jsonResult.getJSONObject("data").getJSONObject("resultMap").getJSONObject("pubBo");
        
        System.out.println("instanceName: " + temp.optString("instanceName", ""));
        System.out.println("taskOwner: " + temp.optString("taskOwner", ""));
    	return temp.toString();
    }
    
    
    @RequestMapping(value = "/testSetTaskData")
    @ResponseBody
    public Map testSetTaskData(String taskId, HttpServletRequest request) {
    	BpmGlobalConfig config = bpmGlobalConfigService.getFirstActConfig();
    	BpmTaskUtils taskUtils = new BpmTaskUtils(config, request.getServletContext());
    	
    	CommonBusinessObject pubBo = new CommonBusinessObject();
		pubBo.setCreatorId("changeMessage");
    	BpmRouteConditionResult routeBo = new BpmRouteConditionResult();
		routeBo.setResult_0("true");
		routeBo.setUsed(true);
    	
		Map<String, HttpReturnStatus> map = taskUtils.setTaskData(request, taskId, pubBo, routeBo);
    	
    	return map;
    }
    
    
    @RequestMapping(value = "/testSendMessage")
    @ResponseBody
    public String testSendMessage() {
        // mqSendUtil.sendMessage("GOGOGO");
    	return "success";
    }
    
    
    @RequestMapping(value = "/test/chooseRole")
    public ModelAndView chooseRole(String id, String isSingle) {
    	ModelAndView mv = new ModelAndView("desmartbpm/common/chooseRole");
    	mv.addObject("id", id);
    	mv.addObject("isSingle", isSingle);
    	return mv;
    }
    @RequestMapping(value = "/test/chooseTeam")
    public ModelAndView chooseTeam(String id, String isSingle) {
        ModelAndView mv = new ModelAndView("desmartbpm/common/chooseTeam");
        mv.addObject("id", id);
        mv.addObject("isSingle", isSingle);
        return mv;
    }
    
    @RequestMapping(value = "/testError")
    public ModelAndView toError() {
        ModelAndView mv = new ModelAndView("desmartbpm/error");
        mv.addObject("errorMessage", "流程实例不存在");
        return mv;
    }

    @RequestMapping(value = "/testAjax")
    @ResponseBody
    public ServerResponse<List<String>> testAjax(String name, String age) {
        System.out.println("name: " + name + "   age: " +  age);
        List<String> metaList = new ArrayList<>();
        metaList.add("hello");
        metaList.add("world");
        metaList.add("java");
        return ServerResponse.createBySuccess(metaList);
    }


    @RequestMapping(value= "/synTask")
    @ResponseBody
    public String synTask(int taskId) {
        synchronizeTaskService.SynchronizeTask(taskId);
        return "done";
    }

    @RequestMapping(value = "/listTask")
    @ResponseBody
    public List<DhTaskInstance> listTask(int taskId) {

        DhTaskInstance taskSelective = new DhTaskInstance();
        taskSelective.setTaskId(taskId);
        return dhTaskInstanceMapper.selectAllTask(taskSelective);
    }


    @RequestMapping(value = "/testGetFile")
    @ResponseBody
    public String testGetFile(HttpServletRequest request, HttpServletResponse response, String proAppId, String proUid, String proVerUid) {
        System.out.println(proAppId + " | " + proUid + " | " + proVerUid);
        ServerResponse<DhTransferData> dhTransferDataServerResponse = dhTransferService.exportProcessDefinition(proAppId, proUid, proVerUid);
        if (dhTransferDataServerResponse.isSuccess()) {
            DhTransferData transferData = dhTransferDataServerResponse.getData();
            String dataStr = JSON.toJSONString(transferData);
            OutputStream out = null;
            BufferedOutputStream bufOut = null;
            String fileName = "export.json";
            try {
                response.setHeader("Content-disposition", "attachment; filename=" + fileName);
                fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
                response.setHeader("Content-disposition", String.format("attachment; filename=\"%s\"", fileName));
                response.setCharacterEncoding("UTF-8");
                out = response.getOutputStream();
                bufOut = new BufferedOutputStream(out);
                bufOut.write(dataStr.getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bufOut != null) {
                    try {
                        bufOut.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


        }
        return "done";
    }

    @RequestMapping(value = "importFile")
    @ResponseBody
    public ServerResponse importFile(@RequestParam("file") MultipartFile file) {

        if (file != null) {
            //getRealPath获得的是本地文件系统的路径即
            // String uploadPath = request.getServletContext().getRealPath("upload");
            //System.out.println(uploadPath);
            try {
               // dhTransferService.trunJsonFileIntoJavaObject(file, "");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ServerResponse.createByErrorMessage("上传失败");
    }

    @RequestMapping(value = "/testMultiUpload")
    @ResponseBody
    public ServerResponse testMultiUpload(@RequestParam("file") MultipartFile file, String field1) {
        String fileName = file.getOriginalFilename();
        System.out.println(fileName);
        if (fileName.startsWith("2")) {
            return ServerResponse.createByErrorCodeMessage(1, "上传失败");
        }
        System.out.println(field1);
        return ServerResponse.createBySuccess(fileName);
    }


    @RequestMapping(value = "/testUpdateApp")
    @ResponseBody
    public ServerResponse testMultiUpload(String proAppId, String proVerUid) {
        //return dhProcessAppUpdateService.pullAllProcessActivityMeta(proAppId, proVerUid);
        return null;
    }

    // http://localhost:8088/bpm/test/recoverTask?taskUid=task_instance:561b3321-bef1-470e-8abb-cd5ad5458313
    @RequestMapping(value = "/recoverTask")
    @ResponseBody
    public ServerResponse testRetryException(String taskUid) {
        return dhTaskExceptionResolverService.recoverTask(taskUid);
    }


    @RequestMapping(value = "/testCreateAProcess")
    @ResponseBody
    public ServerResponse testCreateAProcess() {
        DhProcessInstance lauchTemplate = new DhProcessInstance();
        lauchTemplate.setProAppId("2066.16a6eec4-b3b5-4211-986c-31a94841e288");
        lauchTemplate.setProUid("25.f12891f9-5734-4c07-be13-57d6cfc7e423");
        lauchTemplate.setInsInitUser("00011178");
        lauchTemplate.setDepartNo("10010962");
        lauchTemplate.setCompanyNumber("2120");
        lauchTemplate.setInsTitle("合同备案:" + DateTimeUtil.dateToStr(new Date()));
        lauchTemplate.setInsBusinessKey("合同用印申请表");
        return dhProcessInstanceService.launchProcess(lauchTemplate);
    }
}