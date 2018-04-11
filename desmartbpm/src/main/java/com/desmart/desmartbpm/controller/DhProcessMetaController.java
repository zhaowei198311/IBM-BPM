package com.desmart.desmartbpm.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.desmart.desmartbpm.common.ServerResponse;
import com.desmart.desmartbpm.dao.DhProcessCategoryDao;
import com.desmart.desmartbpm.entity.DhProcessCategory;
import com.desmart.desmartbpm.entity.DhProcessMeta;
import com.desmart.desmartbpm.service.DhProcessCategoryService;
import com.desmart.desmartbpm.service.DhProcessMetaService;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping(value = "/processMeta")
public class DhProcessMetaController {
    @Autowired
    private DhProcessMetaService dhProcessMetaService;
    @Autowired
    private DhProcessCategoryService dhProcessCategoryService;
    
    @RequestMapping(value = "/index")
    public ModelAndView index() {
        return new ModelAndView("processIndex");
    }
    
    
    /**
     * 根据分类id列出下面的所有流程元数据(包括子分类下的流程元数据)
     */
    @RequestMapping(value = "/listByCategoryUid")
    @ResponseBody
    public ServerResponse listProcessMetaByCategoryUid(String categoryUid, String proName, @RequestParam(value="pageNum", defaultValue="1") Integer pageNum, 
            @RequestParam(value="pageSize", defaultValue="10")Integer pageSize) {
        // todo
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
     * @param categoryUid  分类id
     * @param metaInfo  要绑定的流程元数据
     * @return
     */
    @RequestMapping(value = "/create")
    @ResponseBody
    public ServerResponse createProcessMeta(DhProcessMeta dhProcessMeta) {
        return dhProcessMetaService.createDhProcessMeta(dhProcessMeta);
    }
    
    
}
