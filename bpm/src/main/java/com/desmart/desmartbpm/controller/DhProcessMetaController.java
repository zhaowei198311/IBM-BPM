package com.desmart.desmartbpm.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.DhProcessCategory;
import com.desmart.desmartbpm.entity.DhProcessMeta;
import com.desmart.desmartbpm.entity.ZTreeNode;
import com.desmart.desmartbpm.service.DhProcessCategoryService;
import com.desmart.desmartbpm.service.DhProcessMetaService;

@Controller
@RequestMapping(value = "/processMeta")
public class DhProcessMetaController {
    private static final Logger LOG = LoggerFactory.getLogger(DhProcessMetaController.class);
    
    @Autowired
    private DhProcessMetaService dhProcessMetaService;
    @Autowired
    private DhProcessCategoryService dhProcessCategoryService;
    
    @RequestMapping(value = "/index")
    public ModelAndView index() {
        return new ModelAndView("desmartbpm/processIndex");
    }
    
    
    /**
     * 根据分类id列出下面的所有流程元数据(包括子分类下的流程元数据)
     */
    @RequestMapping(value = "/listByCategoryUid")
    @ResponseBody
    public ServerResponse listProcessMetaByCategoryUid(String categoryUid, String proName, @RequestParam(value="pageNum", defaultValue="1") Integer pageNum, 
            @RequestParam(value="pageSize", defaultValue="10")Integer pageSize) {

        ServerResponse<List<DhProcessCategory>> serverResponse = dhProcessCategoryService.listChildrenCategoryAndThisCategory(categoryUid);
        if (!serverResponse.isSuccess()) {
            return serverResponse;
        }
        return dhProcessMetaService.listDhProcessMetaByCategoryList(serverResponse.getData(), proName, pageNum, pageSize);
    }
    
    /**
     * 获得公开的流程元数据（没有绑定过的）
     * @param pageNum 
     * @param pageSize 
     * @param request 
     * @param processAppName  应用库名
     * @param processAppAcronym  应用库名缩略
     * @param display 流程名
     * @return
     */
    @RequestMapping(value = "/getExposedProcess")
    @ResponseBody
    public ServerResponse getExposedProcess(@RequestParam(value="pageNum", defaultValue="1")Integer pageNum, 
            @RequestParam(value="pageSize", defaultValue="10")Integer pageSize, HttpServletRequest request,
            String processAppName, String processAppAcronym, String display) {
        
        return dhProcessMetaService.getExposedProcess(pageNum, pageSize, processAppName, processAppAcronym, display);
    }
    
    /**
     * 创建流程元数据，与分类绑定
     * @return
     */
    @RequestMapping(value = "/create")
    @ResponseBody
    public ServerResponse createProcessMeta(DhProcessMeta dhProcessMeta) {
        return dhProcessMetaService.createDhProcessMeta(dhProcessMeta);
    }
    
    /**
     * 重命名元数据
     * @param metaUid
     * @param newName
     * @return
     */
    @RequestMapping(value = "/rename")
    @ResponseBody
    public ServerResponse renameDhProcessMeta(String metaUid, String newName) {
        return dhProcessMetaService.renameDhProcessMeta(metaUid, newName);
    }
    
    /**
     * 删除流程元数据
     * @param metaUid
     * @return
     */
    @RequestMapping(value = "/remove")
    @ResponseBody
    public ServerResponse removeProcessMeta(String metaUid) {
        try {
            return dhProcessMetaService.removeProcessMeta(metaUid);
        } catch (Exception e) {
            LOG.error("删除元数据失败", e);
            return ServerResponse.createByErrorMessage(e.getMessage());
        }
    }
    
    /**
     * 获取用于生成树的信息（包含分类和元数据）
     */
    @RequestMapping(value = "/getTreeData")
    @ResponseBody
    public String getTreeData() {
        List<DhProcessCategory> categoryList = dhProcessCategoryService.listAll();
        DhProcessCategory dhProcessCategory = new DhProcessCategory();
        dhProcessCategory.setCategoryUid("rootCategory");
        dhProcessCategory.setCategoryName("流程分类");
        dhProcessCategory.setCategoryParent("0");
        categoryList.add(dhProcessCategory);
        List<DhProcessMeta> metaList = dhProcessMetaService.listAll();
        List<ZTreeNode> nodesToShow = new ArrayList<ZTreeNode>();
        for (DhProcessCategory category : categoryList) {
            ZTreeNode node = new ZTreeNode();
            node.setId(category.getCategoryUid());
            node.setName(category.getCategoryName());
            node.setPid(category.getCategoryParent());
            node.setItemType("category");
            node.setIcon("../resources/desmartbpm/images/1.png");
            nodesToShow.add(node);
        }
        
        for (DhProcessMeta meta : metaList) {
            ZTreeNode node = new ZTreeNode();
            node.setId(meta.getProMetaUid());
            node.setName(meta.getProName());
            node.setPid(meta.getCategoryUid());
            node.setItemType("processMeta");
            node.setIcon("../resources/desmartbpm/images/2.png");
            nodesToShow.add(node);
        }
        return JSON.toJSONString(nodesToShow);
    }

    /**
     * 模糊查询满足流程名的所有元数据
     * @param proName
     * @return
     */
    @RequestMapping(value = "/searchByProName")
    @ResponseBody
    public ServerResponse<List<DhProcessMeta>> searchByProName(String proName) {
        try {
            return ServerResponse.createBySuccess(dhProcessMetaService.searchByProName(proName));
        } catch (Exception e) {
            LOG.error("查询流程元数据失败", e);
            return ServerResponse.createByErrorMessage("查询流程元数据失败");
        }
    }
}
