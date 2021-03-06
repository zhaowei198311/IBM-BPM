package com.desmart.desmartbpm.controller;

import com.alibaba.fastjson.JSON;
import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.DhProcessDefinition;
import com.desmart.desmartbpm.entity.DhProcessMeta;
import com.desmart.desmartbpm.entity.DhTransferData;
import com.desmart.desmartbpm.entity.engine.LswSnapshot;
import com.desmart.desmartbpm.service.DhProcessDefinitionService;
import com.desmart.desmartbpm.service.DhProcessMetaService;
import com.desmart.desmartbpm.service.DhTransferService;
import com.desmart.desmartbpm.service.DhTriggerService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 管理导入导出的控制器
 */
@Controller
@RequestMapping(value = "/transfer")
public class DhTransferController {
    private static final Logger LOG = LoggerFactory.getLogger(DhTransferController.class);

    @Autowired
    private DhTransferService dhTransferService;
    @Autowired
    private DhProcessDefinitionService dhProcessDefinitionService;
    @Autowired
    private DhProcessMetaService dhProcessMetaService;
    @Autowired
    private DhTriggerService dhTriggerService;


    /**
     * 导出指定的流程定义
     * @param response
     * @param proAppId
     * @param proUid
     * @param proVerUid
     */
    @RequestMapping(value = "/exportProcessDefinition")
    public void exportProcessDefinition(HttpServletResponse response, String proAppId, String proUid, String proVerUid) {
        ServerResponse<DhTransferData> dhTransferDataServerResponse = dhTransferService.exportProcessDefinition(proAppId, proUid, proVerUid);
        if (dhTransferDataServerResponse.isSuccess()) {
            DhTransferData transferData = dhTransferDataServerResponse.getData();
            String fileName = dhTransferService.getExportFileName(transferData.getProcessDefinitionList().get(0));
            this.writeObjectAsJsonFile(response, transferData, "[流程定义]" + fileName);
        } else {
            this.writeErrorMessage(dhTransferDataServerResponse.getMsg(), response);
        }

    }

    /**
     * 将一个对象以.json文件形式输出给浏览器
     * @param response  HttpServletResponse
     * @param item    需要输出的对象
     * @param fileName  文件名： "sample.json"
     * @param <T>
     */
    private <T> void writeObjectAsJsonFile(HttpServletResponse response, T item, String fileName) {
        if (StringUtils.isBlank(fileName)) {
            fileName = "data.json";
        } else if (!fileName.endsWith(".json")) {
            fileName += ".json";
        }
        String dataStr = JSON.toJSONString(item);
        OutputStream out = null;
        BufferedOutputStream bufOut = null;
        try {
            fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
            response.setHeader("Content-disposition", "attachment; filename=" + fileName);
            response.setHeader("Content-disposition", String.format("attachment; filename=\"%s\"", fileName));
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html; charset=UTF-8"); // 设置编码字符
            out = response.getOutputStream();
            bufOut = new BufferedOutputStream(out);
            bufOut.write(dataStr.getBytes("UTF-8"));
        } catch (Exception e) {
            LOG.error("导出失败", e);
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

    /**
     * 将错误信息写入 html的body中
     * @param errMsg  错误信息 eg: 此对象不存在
     * @param response HttpServletResponse
     */
    private void writeErrorMessage(String errMsg, HttpServletResponse response) {
        PrintWriter pw = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        try {
            pw = response.getWriter();
            pw.write("msg:" + errMsg);
        } catch (IOException e) {
            LOG.error("导出失败", e);
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }

    /**
     * 导入流程定义前校验
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
            return dhTransferService.tryImportProcessDefinition(file, session);
        } catch (Exception e) {
            LOG.error("尝试导入流程失败", e);
            return ServerResponse.createByErrorMessage("尝试导入流程失败");
        }
    }

    /**
     * 导入一个流程定义
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

    /**
     * 取消导入操作
     * @param session
     * @return
     */
    @RequestMapping(value = "/cancelImportTransferData")
    @ResponseBody
    public ServerResponse cancelImportProcessDefinition(HttpSession session) {
        session.removeAttribute(DhTransferData.ATTRIBUTE_IN_SESSION);
        return ServerResponse.createBySuccess();
    }

    /**
     * 导出指定触发器
     * @param triUid 触发器主键
     * @param response
     */
    @RequestMapping(value = "/exportTrigger")
    public void exportTrigger(String triUid, HttpServletResponse response) {
        ServerResponse<DhTransferData> getTriggerResponse = dhTransferService.exportTrigger(triUid);
        if (getTriggerResponse.isSuccess()) {
            DhTransferData transferData = getTriggerResponse.getData();
            this.writeObjectAsJsonFile(response, transferData,
                    "[触发器]" + transferData.getTriggerList().get(0).getTriTitle());
        } else {
            this.writeErrorMessage(getTriggerResponse.getMsg(), response);
        }
    }

    /**
     * 导入触发器前的准备
     * @param file
     * @param session
     * @return
     */
    @RequestMapping(value = "/tryImportTrigger")
    @ResponseBody
    public ServerResponse tryImportTrigger(@RequestParam("file") MultipartFile file, HttpSession session) {
        return dhTransferService.tryImportTrigger(file, session);
    }

    /**
     * 开始导入触发器
     * @param session
     * @return
     */
    @RequestMapping(value = "/sureImportTrigger")
    @ResponseBody
    public ServerResponse sureImportTrigger(HttpSession session) {
        try {
            DhTransferData transferData = (DhTransferData) session.getAttribute(DhTransferData.ATTRIBUTE_IN_SESSION);
            if (transferData == null) {
                return ServerResponse.createByErrorMessage("导入触发器失败, 请重试");
            }
            // 开始导入
            return dhTransferService.startImprtTrigger(transferData);
        } catch (Exception e) {
            LOG.error("导入触发器失败", e);
            session.removeAttribute(DhTransferData.ATTRIBUTE_IN_SESSION);
            return ServerResponse.createByErrorMessage("导入触发器失败");
        }
    }

    @RequestMapping(value = "/exportInterface")
    @ResponseBody
    public void exportInterface(String intUid, HttpServletResponse response) {
        ServerResponse<DhTransferData> exportInterfaceResponse = dhTransferService.exportInterface(intUid);
        if (exportInterfaceResponse.isSuccess()) {
            DhTransferData transferData = exportInterfaceResponse.getData();
            this.writeObjectAsJsonFile(response, transferData,
                    "[接口]" + transferData.getInterfaceList().get(0).getIntTitle());
        } else {
            this.writeErrorMessage(exportInterfaceResponse.getMsg(), response);
        }
    }


    @RequestMapping(value = "/tryImportInterface")
    @ResponseBody
    public ServerResponse tryImportInterface(@RequestParam("file") MultipartFile file, HttpSession session) {
        return dhTransferService.tryImportInterface(file, session);
    }


    @RequestMapping(value = "/sureImportInterface")
    @ResponseBody
    public ServerResponse sureImportInterface(HttpSession session) {
        try {
            DhTransferData transferData = (DhTransferData) session.getAttribute(DhTransferData.ATTRIBUTE_IN_SESSION);
            if (transferData == null) {
                return ServerResponse.createByErrorMessage("导入接口失败, 请重试");
            }
            // 开始导入
            return dhTransferService.startImportInterface(transferData);
        } catch (Exception e) {
            LOG.error("导入接口失败", e);
            session.removeAttribute(DhTransferData.ATTRIBUTE_IN_SESSION);
            return ServerResponse.createByErrorMessage("导入接口失败");
        }
    }

    /**
     * 导出子表单
     * @param publicFormUid  子表单主键
     * @param response
     */
    @RequestMapping(value = "/exportPublicForm")
    public void exportPublicForm(String publicFormUid, HttpServletResponse response) {
        ServerResponse<DhTransferData> exportInterfaceResponse = dhTransferService.exportPublicForm(publicFormUid);
        if (exportInterfaceResponse.isSuccess()) {
            DhTransferData transferData = exportInterfaceResponse.getData();
            this.writeObjectAsJsonFile(response, transferData,
                    "[公共表单]" + transferData.getPublicFormList().get(0).getPublicFormName());
        } else {
            this.writeErrorMessage(exportInterfaceResponse.getMsg(), response);
        }
    }

    /**
     * 导入子表单前的准备
     * @param file
     * @param session
     * @return
     */
    @RequestMapping(value = "/tryImportPublicForm")
    @ResponseBody
    public ServerResponse tryImportPublicForm(@RequestParam("file") MultipartFile file, HttpSession session) {
        return dhTransferService.tryImportPublicForm(file, session);
    }


    /**
     * 导入子表单
     * @param session
     * @return
     */
    @RequestMapping(value = "/sureImportPublicForm")
    @ResponseBody
    public ServerResponse sureImportPublicForm(HttpSession session) {
        try {
            DhTransferData transferData = (DhTransferData) session.getAttribute(DhTransferData.ATTRIBUTE_IN_SESSION);
            if (transferData == null) {
                return ServerResponse.createByErrorMessage("导入子表单失败, 请重试");
            }
            // 开始导入
            return dhTransferService.startImportPublicForm(transferData);
        } catch (Exception e) {
            LOG.error("导入子表单失败", e);
            session.removeAttribute(DhTransferData.ATTRIBUTE_IN_SESSION);
            return ServerResponse.createByErrorMessage("导入子表单失败");
        }
    }


    /**
     * 导出通知模版
     * @param templateUid 通知模版主键
     * @param response
     */
    @RequestMapping(value = "/exportNotifyTemplate")
    public void exportNotifyTemplate(String templateUid, HttpServletResponse response) {
        ServerResponse<DhTransferData> exportInterfaceResponse = dhTransferService.exportNotifyTemplate(templateUid);
        if (exportInterfaceResponse.isSuccess()) {
            DhTransferData transferData = exportInterfaceResponse.getData();
            this.writeObjectAsJsonFile(response, transferData,
                    "[通知模版]" + transferData.getNotifyTemplateList().get(0).getTemplateName());
        } else {
            this.writeErrorMessage(exportInterfaceResponse.getMsg(), response);
        }
    }

    /**
     * 导入通知模版的准备
     * @param file
     * @param session
     * @return
     */
    @RequestMapping(value = "/tryImportNotifyTemplate")
    @ResponseBody
    public ServerResponse tryImportNotifyTemplate(@RequestParam("file") MultipartFile file, HttpSession session) {
        return dhTransferService.tryImportNotifyTemplate(file, session);
    }

    /**
     * 导入通知模版
     * @param session
     * @return
     */
    @RequestMapping(value = "/sureImportNotifyTemplate")
    @ResponseBody
    public ServerResponse sureImportNotifyTemplate(HttpSession session) {
        try {
            DhTransferData transferData = (DhTransferData) session.getAttribute(DhTransferData.ATTRIBUTE_IN_SESSION);
            if (transferData == null) {
                return ServerResponse.createByErrorMessage("导入通知模版失败, 请重试");
            }
            // 开始导入
            return dhTransferService.startImportNotifyTemplate(transferData);
        } catch (Exception e) {
            LOG.error("导入通知模版失败", e);
            session.removeAttribute(DhTransferData.ATTRIBUTE_IN_SESSION);
            return ServerResponse.createByErrorMessage("导入通知模版失败");
        }
    }

}