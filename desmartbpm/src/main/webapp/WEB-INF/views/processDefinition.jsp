<!DOCTYPE html>
<%@ page language="java"  pageEncoding="UTF-8"%>
<html>
    <head>
        <title>流程定义</title>
        <%@include file="common/head.jsp" %>
        <%@include file="common/tag.jsp" %>
        <link href="<%=basePath%>/resources/tree/css/demo.css" rel="stylesheet">
        <link href="<%=basePath%>/resources/tree/css/zTreeStyle/zTreeStyle.css" rel="stylesheet">
        <style>
            ul.ztree{
                border:0;
            }
            .display_container3 .layui-laypage select{
                display:none;
            }
        </style>
    </head>
    <body>
        <div class="layui-container" style="margin-top:20px;width:100%;">  
            <div class="layui-row">
                <div class="layui-col-md2" style="text-align: left;">
                    <ul id="treeDemo" class="ztree" style="width:auto;height:500px;"></ul>
                </div>
                <div class="layui-col-md10">
                    <div class="search_area">
                        <div class="layui-row layui-form">
                            <div class="layui-col-md2">
                                <input type="text" placeholder="流程名称"  class="layui-input">
                            </div>
                            <div class="layui-col-md10" style="text-align:right;">
                                    <button class="layui-btn layui-btn-sm" >查询</button>
                                    <button class="layui-btn layui-btn-sm create_btn">添加</button>
                                    <button class="layui-btn layui-btn-sm delete_btn">删除</button>
                                    <button class="layui-btn layui-btn-primary layui-btn-sm " id="synchr_btn">环节同步</button>
                                    <button class="layui-btn layui-btn-primary layui-btn-sm">同步快照流程图</button>
                                    <button class="layui-btn layui-btn-primary layui-btn-sm">快照流程图</button>
                                    <button class="layui-btn layui-btn-primary layui-btn-sm " id="toEditDefinition_btn">流程配置</button>
                                    <a href="link_set.html"><button class="layui-btn layui-btn-primary layui-btn-sm ">环节配置</button></a>
                                    <a href="draft.html"><button class="layui-btn layui-btn-primary layui-btn-sm">起草权限配置</button></a>
                                    <button class="layui-btn layui-btn-primary layui-btn-sm">拷贝</button>
                                    <button class="layui-btn layui-btn-primary layui-btn-sm">导出</button>
                                    <button class="layui-btn layui-btn-primary layui-btn-sm">导入</button>
                            </div>
                        </div>
                    </div>
                    <div style="width:100%;overflow-x:auto;">               
                        <table class="layui-table backlog_table" lay-even lay-skin="nob" style="width:2000px;">
                            <colgroup>
                                <col>
                                <col>
                                <col>
                                <col>
                                <col>
                                <col> 
                                <col>
                                <col>
                            </colgroup>
                            <thead>
                                <tr>
                                  <th><input type="checkbox" name="" title='全选' lay-skin="primary"> 序号</th>
                                  <th>流程名称</th>
                                  <th>快照号</th>
                                  <th>快照名称</th>
                                  <th>激活状态</th>
                                  <th>快照创建时间</th>
                                  <th>流程定义状态</th>
                                  <th>修改时间</th>
                                  <th>修改人</th>
                                </tr> 
                            </thead>
                            <tbody id="definitionList_tbody"></tbody>
                        </table>                
                    </div>
                    <div id="lay_page"></div>
                </div>
            </div>
        </div>
        <div class="display_container3">
            <div class="display_content3">
                <div class="top">
                    新增流程元数据
                </div>
                <div class="middle1">
                    <table class="layui-table backlog_table" lay-even lay-skin="nob">
                        <colgroup>
                            <col>
                            <col>
                            <col>
                            <col>
                        </colgroup>
                        <thead>
                            <tr>
                              <th><input type="checkbox" name="" title='全选' lay-skin="primary"> 序号</th>
                              <th>流程名称</th>
                              <th>流程应用库id</th>
                              <th>流程图id</th>
                            </tr> 
                        </thead>
                        <tbody>
                            <tr>
                                <td><input type="checkbox" name="" title='全选' lay-skin="primary"> 1</td>
                                <td>流程名称</td>
                                <td>2066.f5d9e3c6-2fa7...</td>
                                <td>25.64765b13-286b-4...</td>
                            </tr>
                            <tr>
                                <td><input type="checkbox" name="" title='全选' lay-skin="primary"> 2</td>
                                <td>流程名称</td>
                                <td>2066.f5d9e3c6-2fa7...</td>
                                <td>25.64765b13-286b-4...</td>
                            </tr>
                            <tr>
                                <td><input type="checkbox" name="" title='全选' lay-skin="primary"> 3</td>
                                <td>流程名称</td>
                                <td>2066.f5d9e3c6-2fa7...</td>
                                <td>25.64765b13-286b-4...</td>
                            </tr>
                            <tr>
                                <td><input type="checkbox" name="" title='全选' lay-skin="primary"> 4</td>
                                <td>流程名称</td>
                                <td>2066.f5d9e3c6-2fa7...</td>
                                <td>25.64765b13-286b-4...</td>
                            </tr>
                            <tr>
                                <td><input type="checkbox" name="" title='全选' lay-skin="primary"> 5</td>
                                <td>流程名称</td>
                                <td>2066.f5d9e3c6-2fa7...</td>
                                <td>25.64765b13-286b-4...</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                <div id="demo8"></div>
                <div class="foot">
                    <button class="layui-btn layui-btn sure_btn">确定</button>
                    <button class="layui-btn layui-btn layui-btn-primary cancel_btn">取消</button>
                </div>
            </div>
        </div>
    </body>
    
<script type="text/javascript" src="<%=basePath%>/resources/js/layui.all.js"></script>
<script type="text/javascript" src="<%=basePath%>/resources/tree/js/jquery.ztree.core.js"></script>
<script type="text/javascript" src="<%=basePath%>/resources/tree/js/jquery.ztree.excheck.js"></script>
<script type="text/javascript" src="<%=basePath%>/resources/tree/js/jquery.ztree.exedit.js"></script>
<script type="text/javascript" src="<%=basePath%>/resources/js/my/processDefinition.js"></script>
<script>
	// 为翻页提供支持
	var pageConfig = {
	    pageNum: 1,
	    pageSize: 10,
	    total: 0,
        metaUid: ""
	};

    var setting = {
            view: {
                selectedMulti: false
            },
            data: {
                simpleData: {
                    enable: true,
                    idKey: "id",
                    pIdKey: "pid",
                    rootPId: "rootCategory"
                }
            },
            callback: {
                onClick: zTreeOnClick// 点击回调
            }
    };
	
     $(document).ready(function(){
         // 加载树
         $.ajax({
        	 url: common.getPath() + "/processMeta/getTreeData",
        	 type: "post",
        	 data: {},
        	 dataType: "json",
        	 success: function(result) {
        		 $.fn.zTree.init($("#treeDemo"), setting, result);
        	 }
         });
         
         $(".create_btn").click(function(){
             $(".display_container3").css("display","block");
         })
         $(".edit_user").click(function(){
             $(".display_container4").css("display","block");
         })
         $(".sure_btn").click(function(){
             $(".display_container3").css("display","none");
             $(".display_container4").css("display","none");
         })
         $(".cancel_btn").click(function(){
             $(".display_container3").css("display","none");
             $(".display_container4").css("display","none");
         })
     });
</script>
</html>
    
