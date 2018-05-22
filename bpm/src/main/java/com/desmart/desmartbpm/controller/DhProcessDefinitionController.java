package com.desmart.desmartbpm.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.DhProcessDefinition;
import com.desmart.desmartbpm.service.DhGatewayLineService;
import com.desmart.desmartbpm.service.DhProcessCategoryService;
import com.desmart.desmartbpm.service.DhProcessDefinitionService;
import com.desmart.desmartbpm.service.DhProcessMetaService;
import com.desmart.desmartbpm.util.http.HttpClientUtils;
import com.desmart.desmartbpm.vo.DhProcessDefinitionVo;

/**
 * 流程定义控制器
 */
@Controller
@RequestMapping(value = "/processDefinition")
public class DhProcessDefinitionController {
    private static final Logger LOG = LoggerFactory.getLogger(DhProcessDefinitionController.class);
    
    @Autowired
    private DhProcessCategoryService dhProcessCategoryService;
    @Autowired
    private DhProcessMetaService dhProcessMetaService;
    @Autowired
    private DhProcessDefinitionService dhProcessDefinitionService;
    @Autowired
    private DhGatewayLineService dhGatewayLineService;
    
    
    
    @RequestMapping(value = "/index")
    @ResponseBody
    public ModelAndView toIndex() {
        ModelAndView mv = new ModelAndView("desmartbpm/processDefinition");
        return mv;
    }
    
    /**
     * 根据流程元数据，列出数据库中的流程定义，和公开的流程中相关的版本
     * @param metaUid
     * @return
     */
    @RequestMapping(value = "/listDefinitionByProcessMeta")
    @ResponseBody
    public ServerResponse listDefinitionByProcessMeta(String metaUid,
                                                      @RequestParam(value="pageNum", defaultValue="1") Integer pageNum,
                                                      @RequestParam(value="pageSize", defaultValue="10")Integer pageSize) {

        return dhProcessDefinitionService.listProcessDefinitionsIncludeUnSynchronized(metaUid, pageNum, pageSize);
    }

    /**
     * 创建一个流程定义，并生产相关的环节数据
     * @param proAppId  应用库id
     * @param proUid  流程图id
     * @param proVerUid 版本快照id
     * @param request
     * @return
     */
    @RequestMapping(value = "/create")
    @ResponseBody
    public ServerResponse synchronizeDhProcessDefinition(String proAppId, String proUid, String proVerUid, HttpServletRequest request) {

        try {
            ServerResponse serverResponse = dhProcessDefinitionService.createDhProcessDefinition(proAppId, proUid, proVerUid, request);
            if (serverResponse.isSuccess()) {
                // 判断是否需要同步网关路线
                boolean needGenerateGatewayLine = dhGatewayLineService.needGenerateGatewayLine(proAppId, proUid, proVerUid);
                if (needGenerateGatewayLine) {
                    return dhGatewayLineService.generateGatewayLine(proAppId, proUid, proVerUid);
                } else {
                    return serverResponse;
                }
            } else {
                return serverResponse;
            }
        } catch (Exception e) {
            LOG.error("同步环节失败", e);
            return ServerResponse.createByErrorMessage("同步环节失败");
        }
    }

    
    @RequestMapping(value = "/getSynchronizedDefinition")
    @ResponseBody
    public ServerResponse<DhProcessDefinitionVo> getSynchronizedDhProcessDefinitionWithSnapshotInfo(String proAppId, String proUid, String proVerUid) {
        try {
            return dhProcessDefinitionService.getSynchronizedDhProcessDefinitionWithSnapshotInfo(proAppId, proUid, proVerUid);
        } catch (Exception e) {
            LOG.error("获取流程定义信息失败", e);
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
        }
        return mv;
    }

    @RequestMapping(value = "/update")
    @ResponseBody
    public ServerResponse updateDhProcessDefinition(DhProcessDefinition definition) {

        try {
            return dhProcessDefinitionService.updateDhProcessDefinition(definition);
        } catch(Exception e) {
            LOG.error("更新流程定义失败", e);
            return ServerResponse.createByErrorMessage(e.getMessage());
        }
    }
    
    @RequestMapping(value = "/snapshotFlowChart")
    @ResponseBody
    public String viewFlowChart (String proAppId, String proUid, String proVerUid, HttpServletRequest request) {
    	LOG.info("请求查看流程图"+proAppId);    	 
    	String url = "http://10.0.4.201:9080/rest/bpm/wle/v1/visual/processModel/"+proUid+"?snapshotId="+proVerUid+"&image=true";
    	HttpClientUtils httpUtils = new HttpClientUtils();
    	// 验证
    	String msg = httpUtils.checkLoginIbm(url);
    	return url;
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
    @RequestMapping(value = "/enableDefinition")
    @ResponseBody
    public ServerResponse enableDefinition(String proAppId, String proUid, String proVerUid) {
        try {
            return dhProcessDefinitionService.enableProcessDefinition(proAppId, proUid, proVerUid);
        } catch(Exception e) {
            LOG.error("启用流程失败", e);
            return ServerResponse.createByErrorMessage("启用流程失败");
        }
    }
}
