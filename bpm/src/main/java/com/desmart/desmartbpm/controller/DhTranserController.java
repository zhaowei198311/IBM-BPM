package com.desmart.desmartbpm.controller;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.DhProcessDefinition;
import com.desmart.desmartbpm.entity.DhProcessMeta;
import com.desmart.desmartbpm.entity.DhTransferData;
import com.desmart.desmartbpm.service.DhProcessDefinitionService;
import com.desmart.desmartbpm.service.DhProcessMetaService;
import com.desmart.desmartbpm.service.DhTransferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

/**
 * 管理导入导出的控制器
 */
@Controller
@RequestMapping(value = "/transfer")
public class DhTranserController {
    private static final Logger LOG = LoggerFactory.getLogger(DhTranserController.class);

    @Autowired
    private DhTransferService dhTransferService;
    @Autowired
    private DhProcessDefinitionService dhProcessDefinitionService;
    @Autowired
    private DhProcessMetaService dhProcessMetaService;

    /**
     * 导入一个流程定义前，校验状态
     * @param file
     * @return
     *  status = 0 && data = add  可以导入
     *  status = 0 && data = override  确认是否覆盖
     *  status != 0 文件错误
     */
    @RequestMapping(value = "/tryImportDefinition")
    @ResponseBody
    public ServerResponse tryImportDefinition(@RequestParam("file") MultipartFile file, HttpSession session) {
        try {
            ServerResponse<DhTransferData> turnIntoResponse = dhTransferService.trunFileIntoDhTransferData(file);
            if (!turnIntoResponse.isSuccess()) {
                return turnIntoResponse;
            }
            DhTransferData transferData = turnIntoResponse.getData();
            ServerResponse validateResponse = dhTransferService.validateTransferDataForImportProcessDefinition(transferData);
            if (!validateResponse.isSuccess()) {
                return validateResponse;
            }
            // 文件基本校验通过
            // 将文件数据保存在session中
            session.setAttribute(DhTransferData.ATTRIBUTE_IN_SESSION, transferData);
            DhProcessDefinition processDefinition = transferData.getProcessDefinitionList().get(0);
            String proAppId = processDefinition.getProAppId(),
                    proUid = processDefinition.getProUid(),
                    proVerUid = processDefinition.getProVerUid();

            DhProcessMeta processMeta = dhProcessMetaService.getByProAppIdAndProUid(proAppId, proUid);
            if (processMeta == null) {
                return ServerResponse.createByErrorMessage("请先添加流程元数据");
            }
            DhProcessDefinition definitionInDb = dhProcessDefinitionService.getDhProcessDefinition(proAppId, proUid, proVerUid);
            if (definitionInDb == null) {
                // 属于新加的流程
                return ServerResponse.createBySuccess("new");
            } else {
                // 确认是否需要覆盖
                return ServerResponse.createBySuccess("override");
            }
        } catch (Exception e) {
            LOG.error("尝试导入流程失败", e);
            session.removeAttribute(DhTransferData.ATTRIBUTE_IN_SESSION);
            return ServerResponse.createByErrorMessage("导入流程失败");
        }
    }

    /**
     * 导入一个流程定义
     * @param file 导入的文件
     * @return
     */
    @RequestMapping(value = "/sureImportProcessDefinition")
    @ResponseBody
    public ServerResponse sureImportProcessDefinition(HttpSession session) {
        try {
            DhTransferData transferData = (DhTransferData) session.getAttribute(DhTransferData.ATTRIBUTE_IN_SESSION);
            if (transferData == null) {
                return ServerResponse.createByErrorMessage("导入流程失败, 请重试");
            }
            // 开始导入
            return dhTransferService.startImportProcessDefinition(transferData);
        } catch (Exception e) {
            LOG.error("导入流程失败", e);
            session.removeAttribute(DhTransferData.ATTRIBUTE_IN_SESSION);
            return ServerResponse.createByErrorMessage("导入流程失败");
        }
    }

    @RequestMapping(value = "/cancelImportProcessDefinition")
    @ResponseBody
    public ServerResponse cancelImportProcessDefinition(HttpSession session) {
        session.removeAttribute(DhTransferData.ATTRIBUTE_IN_SESSION);
        return ServerResponse.createBySuccess();
    }
}