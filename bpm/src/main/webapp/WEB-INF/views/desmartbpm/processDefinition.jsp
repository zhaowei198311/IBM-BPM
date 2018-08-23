<!DOCTYPE html>
<%@ page language="java"  pageEncoding="UTF-8"%>
<html>
    <head>
        <title>流程定义</title>
        <%@include file="common/head.jsp" %>
        <%@include file="common/tag.jsp" %>
        <link rel="stylesheet" href="<%=basePath%>/resources/desmartbpm/css/my.css" media="all">
        <link href="<%=basePath%>/resources/desmartbpm/tree/css/demo.css" rel="stylesheet">
        <link href="<%=basePath%>/resources/desmartbpm/tree/css/zTreeStyle/zTreeStyle.css" rel="stylesheet">
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
                                <input type="text" placeholder="流程名称"  class="layui-input" id="proName_input">
                            </div>
                            <div class="layui-col-md1">
                                <select id="proStatusSel" name="proStauts"  lay-filter="proStatus">
                                    <option value="enabled">已启用</option>
                                    <option value="synchronized">已同步</option>
                                    <option value="unsynchronized">未同步</option>
                                </select>
                            </div>
                            <div class="layui-col-md9" style="text-align:left;padding-left:20px;line-height:38px;">
                                    <button class="layui-btn layui-btn-sm" id="searchByProName_btn">查询</button>
                                    <button class="layui-btn layui-btn-primary layui-btn-sm " id="synchr_btn">环节同步</button>
                                    <button class="layui-btn layui-btn-primary layui-btn-sm" id="updateAppBtn">升级应用库</button>
                                    <button class="layui-btn layui-btn-primary layui-btn-sm" id="snapshotFlowChart_btn">快照流程图</button>
                                    <button class="layui-btn layui-btn-primary layui-btn-sm " id="toEditDefinition_btn">流程配置</button>
                                    <button class="layui-btn layui-btn-primary layui-btn-sm " id="toEditActivityConf_btn">环节配置</button>
                                    <button class="layui-btn layui-btn-primary layui-btn-sm" id="enable_btn">启用</button>
                                    <button class="layui-btn layui-btn-primary layui-btn-sm" id="disable_btn">停用</button>
                                    <%-- 此按钮弃用 <button class="layui-btn layui-btn-primary layui-btn-sm" id="querySimilarProcess">拷贝</button> --%>
                                    <button class="layui-btn layui-btn-primary layui-btn-sm" id="exportBtn">导出</button>
                                    <button class="layui-btn layui-btn-primary layui-btn-sm" id="importBtn">导入</button>

                            </div>
                        </div>
                    </div>
                    <div style="width:100%;overflow-x:auto;">               
                        <table class="layui-table backlog_table" lay-even lay-skin="nob" style="width:100%;">
                            <thead>
                                <tr>
                                  <th>序号</th>
                                  <th>流程名称</th>
                                  <!-- <th>快照号</th> -->
                                  <th>快照名称</th>
                                  <th>激活状态</th>
                                  <th>快照创建时间</th>
                                  <th>流程定义状态</th>
                                  <th>修改人</th>
                                  <th>修改时间</th>
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
                        <thead>
                            <tr>
                              <th><input type="checkbox" name="" title='全选' lay-skin="primary"> 序号</th>
                              <th>流程名称</th>
                              <th>流程应用库id</th>
                              <th>流程图id</th>
                            </tr> 
                        </thead>
                        <tbody></tbody>
                    </table>
                </div>
                <div id="demo8"></div>
                <div class="foot">
                    <button class="layui-btn layui-btn sure_btn">确定</button>
                    <button class="layui-btn layui-btn layui-btn-primary cancel_btn">取消</button>
                </div>
            </div>
        </div>
        
        <div class="display_container8">
            <div class="display_content8">
                <div class="top">
                    请选一个流程从中复制配置
                </div>
                <div class="middle1" style="height: 75%;width: 98%">
                    <table class="layui-table backlog_table" lay-even lay-skin="nob">
                        <thead>
                            <tr>
                              <th><input type="checkbox" name="" lay-skin="primary"> 序号</th>
                              <th>流程名称</th>
                              <th>快照号</th>
                              <th>快照名称</th>
                              <th>流程定义状态</th>
                            </tr> 
                        </thead>
                        <tbody id="similar_process"></tbody>
                    </table>
                </div>
                <div class="foot">
                    <button class="layui-btn layui-btn sure_btn" onclick="copyProcess()">拷贝</button>
                    <button class="layui-btn layui-btn layui-btn-primary cancel_btn">取消</button>
                </div>
            </div>
        </div>
    </body>
    <div style="display: none;"> 
        <p>升级到指定版本</p>
        <form action="<%=basePath%>/processAppUpdate/updateToNewVersion" >
            proAppId<input type="text" name="proAppId" value="2066.740849e3-3831-4a48-a92e-cf45175c12c2" style="width: 500px; margin: 10px;"/><br/>
            oldProVerUid<input type="text" name="oldProVerUid" value="2064.be0b31a2-f117-4dbb-bb3d-150357b6469c" style="width: 500px; margin: 10px;"/><br/>
            newProVerUid<input type="text" name="newProVerUid" value="2064.4d98c6eb-250f-4154-a053-5b072853a586" style="width: 500px; margin: 10px;"/><input type="submit" value="submit"><br/>

        </form>
        <p>拉取指定版本图</p>
        <form action="<%=basePath%>/processAppUpdate/pullDefintionByAppIdAndSnapshotId" >
            proAppId<input type="text" name="proAppId" value="2066.740849e3-3831-4a48-a92e-cf45175c12c2"/>
            proVerUid<input type="text" name="proVerUid"/>
            <input type="submit" value="submit">
        </form>
    </div>
<script type="text/javascript" src="<%=basePath%>/resources/desmartbpm/js/layui.all.js"></script>
<script type="text/javascript" src="<%=basePath%>/resources/desmartbpm/tree/js/jquery.ztree.core.js"></script>
<script type="text/javascript" src="<%=basePath%>/resources/desmartbpm/tree/js/jquery.ztree.excheck.js"></script>
<script type="text/javascript" src="<%=basePath%>/resources/desmartbpm/tree/js/jquery.ztree.exedit.js"></script>
<script type="text/javascript" src="<%=basePath%>/resources/desmartbpm/js/my/processDefinition.js"></script>
<script>

	
     $(document).ready(function(){
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
             $(".display_container8").css("display","none");
         })
         $(".display_container8").css("display","none");
     });

</script>
</html>
    
