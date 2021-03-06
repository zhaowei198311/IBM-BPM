package com.desmart.desmartbpm.controller;

import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.desmart.desmartbpm.service.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.desmart.common.annotation.log.Log;
import com.desmart.common.constant.ServerResponse;
import com.desmart.common.exception.PlatformException;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.entity.DhProcessDefinition;
import com.desmart.desmartbpm.util.http.HttpClientUtils;
import com.desmart.desmartbpm.vo.DhProcessDefinitionVo;
import com.desmart.desmartsystem.entity.BpmGlobalConfig;
import com.desmart.desmartsystem.service.BpmGlobalConfigService;

/**
 * 流程定义控制器
 */
@Controller
@RequestMapping(value = "/processDefinition")
public class DhProcessDefinitionController {
    private static final Logger logger = LoggerFactory.getLogger(DhProcessDefinitionController.class);
    
    @Autowired
    private DhProcessDefinitionService dhProcessDefinitionService;
    @Autowired
    private BpmGlobalConfigService bpmGlobalConfigService;
    @Autowired
    private DhStepService dhStepService;

    /**
     * 跳转到流程定义首页
     * @return
     */
    @RequestMapping(value = "/index")
    @ResponseBody
    public ModelAndView toIndex() {
        ModelAndView mv = new ModelAndView("desmartbpm/processDefinition");
        return mv;
    }
    
    /**
     * 根据流程元数据，和流程定义的状态列出对应的流程定义
     * @param metaUid  流程元数据id
     * @param proStatus 流程定义状态 all; enabled; synchronized
     * @return
     */
    @RequestMapping(value = "/listDefinitionByProcessMeta")
    @ResponseBody
    public ServerResponse listDefinitionByProcessMeta(String metaUid, String proStatus,
                                                      @RequestParam(value="pageNum", defaultValue="1") Integer pageNum,
                                                      @RequestParam(value="pageSize", defaultValue="10")Integer pageSize) {
        if (StringUtils.isBlank(proStatus)) {
            return ServerResponse.createByErrorMessage("参数异常：流程定义状态");
        }else if ("unsynchronized".equals(proStatus)) {
            return dhProcessDefinitionService.listUnsynchronizedProcessDefinitionByMetaUidAndStatus(metaUid, pageNum, pageSize);
        } else {
            return dhProcessDefinitionService.listSynchronizedProcessDefinitionByMetaUidAndStatus(metaUid, proStatus, pageNum, pageSize);
        }
    }

    /**
     * 创建一个流程定义，并生产相关的环节数据
     * @param proAppId  应用库id
     * @param proUid  流程图id
     * @param proVerUid 版本快照id
     * @return
     */
    @Log(description = "创建一个流程定义")
    @RequestMapping(value = "/create")
    @ResponseBody
    public ServerResponse synchronizeDhProcessDefinition(String proAppId, String proUid, String proVerUid) {

        try {
            ServerResponse serverResponse = dhProcessDefinitionService.createDhProcessDefinition(proAppId, proUid, proVerUid);
            return serverResponse;
        } catch (PlatformException pe) {
            logger.error("同步环节失败", pe);
            return ServerResponse.createByErrorMessage(pe.getMessage());
        } catch (Exception e) {
            logger.error("同步环节失败", e);
            return ServerResponse.createByErrorMessage("同步环节失败");
        }
    }

    
    @RequestMapping(value = "/getSynchronizedDefinition")
    @ResponseBody
    public ServerResponse<DhProcessDefinitionVo> getSynchronizedDhProcessDefinitionWithSnapshotInfo(String proAppId, String proUid, String proVerUid) {
        try {
            return dhProcessDefinitionService.getSynchronizedDhProcessDefinitionWithSnapshotInfo(proAppId, proUid, proVerUid);
        } catch (Exception e) {
            logger.error("获取流程定义信息失败", e);
            return ServerResponse.createByErrorMessage("获取流程定义信息失败");
        }
    }
    
    /**
     * 查看指定的流程定义是否存在
     * @return
     */
    @RequestMapping(value = "/tryEditDefinition")
    @ResponseBody
    public ServerResponse tryEditDefinition(String proAppId, String proUid, String proVerUid) {
        return dhProcessDefinitionService.isDhProcessDefinitionExist(proAppId, proUid, proVerUid);
    }

    /**
     * 跳转到配置流程定义
     * @param proAppId
     * @param proUid
     * @param proVerUid
     * @return
     */
    @RequestMapping(value = "/editDefinition")
    public ModelAndView editDefinition(String proAppId, String proUid, String proVerUid) {
        ModelAndView mv = new ModelAndView("desmartbpm/processDefinitionSet");
        ServerResponse serverResponse = dhProcessDefinitionService.isDhProcessDefinitionExist(proAppId, proUid, proVerUid);
        if (serverResponse.isSuccess()) {
            mv.addObject("definition", serverResponse.getData());
            Set<String> businessKeys = dhStepService.listStepBusinessKeyOfMainProcess(proAppId, proUid, proVerUid);
            mv.addObject("businessKeys", businessKeys);
        }
        return mv;
    }

    @Log(description = "更新流程定义")
    @RequestMapping(value = "/update")
    @ResponseBody
    public ServerResponse updateDhProcessDefinition(DhProcessDefinition definition) {

        try {
            return dhProcessDefinitionService.updateDhProcessDefinition(definition);
        } catch(Exception e) {
            logger.error("更新流程定义失败", e);
            return ServerResponse.createByErrorMessage(e.getMessage());
        }
    }
    
    @Log(description="查看流程图模块")
    @RequestMapping(value = "/snapshotFlowChart")
    @ResponseBody
    public String viewFlowChart (String proAppId, String proUid, String proVerUid, HttpServletRequest request) {
    	logger.info("请求查看流程图"+proAppId);
    	BpmGlobalConfig gcfg = bpmGlobalConfigService.getFirstActConfig();
    	String url = gcfg.getBpmServerHost()+"rest/bpm/wle/v1/visual/processModel/"+proUid+"?snapshotId="+proVerUid+"&image=true";
    	HttpClientUtils httpUtils = new HttpClientUtils();
    	// 验证
    	String msg = httpUtils.checkLoginIbm(url);
    	return url;
    }
    
    /**
     * 
     * @Title: whetherLinkSynchronization  
     * @Description: 拷贝前验证是否进行环节同步  
     * @param @param bpmActivityMeta
     * @param @return  
     * @return ServerResponse  
     * @throws
     */
    @RequestMapping(value = "/whetherLinkSynchronization")
    @ResponseBody
    public ServerResponse whetherLinkSynchronization(BpmActivityMeta bpmActivityMeta){
    	return dhProcessDefinitionService.checkWhetherLinkSynchronization(bpmActivityMeta);
    }
    /**
     * 
     * @Title: selectSimilarProcessForCopy  
     * @Description: 查询拷贝所需的同类流程信息  
     * @param @return  
     * @return ServerResponse
     * @throws
     */
    @RequestMapping(value = "/selectSimilarProcessForCopy")
    @ResponseBody
    public ServerResponse selectSimilarProcessForCopy(DhProcessDefinition dhProcessDefinition){
    	return dhProcessDefinitionService.listProcessDefinitionById(dhProcessDefinition);
    }
    
    /**
     * 
     * @Title: copySimilarProcess  
     * @Description: 拷贝选定的同类流程信息  
     * @param @param dhProcessDefinition
     * @param @return  
     * @return ServerResponse
     * @throws
     */
    @RequestMapping(value = "/copySimilarProcess")
    @ResponseBody
    public ServerResponse copySimilarProcess(@RequestParam Map<String, Object> mapId){
    	return dhProcessDefinitionService.copySimilarProcess(mapId);
    }
    
    /**
     * 启用一个版本
     * @param proAppId
     * @param proUid
     * @param proVerUid
     * @return
     */
    @Log(description = "启用一个流程定义的版本")
    @RequestMapping(value = "/enableDefinition")
    @ResponseBody
    public ServerResponse enableDefinition(String proAppId, String proUid, String proVerUid) {
        try {
            return dhProcessDefinitionService.enableProcessDefinition(proAppId, proUid, proVerUid);
        } catch(Exception e) {
            logger.error("启用流程失败", e);
            return ServerResponse.createByErrorMessage("启用流程失败");
        }
    }

    /**
     * 停用一个版本
     * @param proAppId
     * @param proUid
     * @param proVerUid
     * @return
     */
    @Log(description = "停用一个流程定义版本")
    @RequestMapping(value = "/disableDefinition")
    @ResponseBody
    public ServerResponse disableDefinition(String proAppId, String proUid, String proVerUid) {
        try {
            return dhProcessDefinitionService.disableProcessDefinition(proAppId, proUid, proVerUid);
        } catch(Exception e) {
            logger.error("启用流程失败", e);
            return ServerResponse.createByErrorMessage("启用流程失败");
        }
    }


    /**
     * 刷新公开的流程
     * @return
     */
    @Log(description = "重新获取公开的流程数据")
    @RequestMapping(value = "/reloadExposedItems")
    @ResponseBody
    public ServerResponse reloadExposedItems() {
        return dhProcessDefinitionService.reloadExposedItems();
    }





}
