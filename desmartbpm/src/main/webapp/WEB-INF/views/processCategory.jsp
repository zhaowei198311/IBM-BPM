<!DOCTYPE html>
<%@ page language="java"  pageEncoding="UTF-8"%>
<html>
    <head>
        <title>流程分类</title>
        <%@include file="common/head.jsp" %>
        <%@include file="common/tag.jsp" %>
        <link href="<%=basePath%>/resources/tree/css/demo.css" rel="stylesheet">
        <link href="<%=basePath%>/resources/tree/css/zTreeStyle/zTreeStyle.css" rel="stylesheet">
        
        <style>
            .ztree li span.button.add {
                margin-right: 2px;
                background-position: -143px 0px;
                vertical-align: top;
            }
            ul.ztree{
                border:0;
            }
            .display_container3 .layui-laypage select{
                display:none;
            }
            .layui-form-label{width:100px;}
            .layui-input-block{margin-left:130px;}
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
                            <div class="layui-col-md3" style="text-align:right;">
                                    <button class="layui-btn" >查询</button>
                                    <button class="layui-btn create_btn">添加</button>
                                    <button class="layui-btn delete_btn">删除</button>
                            </div>
                        </div>
                    </div>
                    <div style="width:100%;overflow-x:auto;">               
                        <table class="layui-table backlog_table link_table" lay-even lay-skin="nob" style="width:2000px;">
                            <colgroup>
                                <col>
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
                                  <th>流程应用库id</th>
                                  <th>流程图id</th>
                                  <th>流程编号</th>
                                  <th>表单页面路径</th>
                                  <th>创建者</th>
                                  <th>创建时间</th>
                                  <th>更新者</th>
                                  <th>更新时间</th>
                                </tr> 
                            </thead>
                            <tbody>
                                <tr>
                                  <td><input type="checkbox" name="" title='全选' lay-skin="primary"> 1</td>
                                  <td>流程名称</td>
                                  <td>2066.f5d9e3c6-2fa7-49fe-82bb-f17694b36b10</td>
                                  <td>25.64765b13-286b-4b91-8c19-4234c6e05e6d</td>
                                  <td>编号01</td>
                                  <td></td>
                                  <td>zhangsan</td>
                                  <td>2018-04-09 10:00:00</td>
                                  <td>zhangsan</td>
                                  <td>2018-04-09 10:00:00</td>
                                </tr>
                                <tr>
                                  <td><input type="checkbox" name="" title='全选' lay-skin="primary"> 2</td>
                                  <td>流程名称</td>
                                  <td>2066.f5d9e3c6-2fa7-49fe-82bb-f17694b36b10</td>
                                  <td>25.64765b13-286b-4b91-8c19-4234c6e05e6d</td>
                                  <td>编号01</td>
                                  <td></td>
                                  <td>zhangsan</td>
                                  <td>2018-04-09 10:00:00</td>
                                  <td>zhangsan</td>
                                  <td>2018-04-09 10:00:00</td>
                                </tr>
                                <tr>
                                  <td><input type="checkbox" name="" title='全选' lay-skin="primary"> 3</td>
                                  <td>流程名称</td>
                                  <td>2066.f5d9e3c6-2fa7-49fe-82bb-f17694b36b10</td>
                                  <td>25.64765b13-286b-4b91-8c19-4234c6e05e6d</td>
                                  <td>编号01</td>
                                  <td></td>
                                  <td>zhangsan</td>
                                  <td>2018-04-09 10:00:00</td>
                                  <td>zhangsan</td>
                                  <td>2018-04-09 10:00:00</td>
                                </tr>
                                <tr>
                                  <td><input type="checkbox" name="" title='全选' lay-skin="primary"> 4</td>
                                  <td>流程名称</td>
                                  <td>2066.f5d9e3c6-2fa7-49fe-82bb-f17694b36b10</td>
                                  <td>25.64765b13-286b-4b91-8c19-4234c6e05e6d</td>
                                  <td>编号01</td>
                                  <td></td>
                                  <td>zhangsan</td>
                                  <td>2018-04-09 10:00:00</td>
                                  <td>zhangsan</td>
                                  <td>2018-04-09 10:00:00</td>
                                </tr>
                                <tr>
                                  <td><input type="checkbox" name="" title='全选' lay-skin="primary"> 5</td>
                                  <td>流程名称</td>
                                  <td>2066.f5d9e3c6-2fa7-49fe-82bb-f17694b36b10</td>
                                  <td>25.64765b13-286b-4b91-8c19-4234c6e05e6d</td>
                                  <td>编号01</td>
                                  <td></td>
                                  <td>zhangsan</td>
                                  <td>2018-04-09 10:00:00</td>
                                  <td>zhangsan</td>
                                  <td>2018-04-09 10:00:00</td>
                                </tr>
                            </tbody>
                        </table>                        
                    </div>
                    <div id="demo7"></div>
                </div>
            </div>
        </div>
        <div class="display_container4">
        <div class="display_content4">
            <div class="top">
                编辑流程元数据
            </div>
            <div class="middle2">
                <form class="layui-form" action="" style="margin-top:30px;">
                  <div class="layui-form-item">
                    <label class="layui-form-label">流程名称</label>
                    <div class="layui-input-block">
                      <input type="text" name="title" required  lay-verify="required" value="流程名称1" autocomplete="off" class="layui-input">
                    </div>
                  </div>
                  <div class="layui-form-item">
                    <label class="layui-form-label">流程应用库</label>
                    <div class="layui-input-block">
                      <input type="text" name="title" required  lay-verify="required" value="2066.c2c0dc07-39c9-41de-8581-729ec9ce28a3" autocomplete="off" class="layui-input" disabled="disabled">
                    </div>
                  </div>
                  <div class="layui-form-item">
                    <label class="layui-form-label">流程图id</label>
                    <div class="layui-input-block">
                      <input type="text" name="title" required  lay-verify="required" value="2066.c2c0dc07-39c9-41de-8581-729ec9ce28a3" autocomplete="off" class="layui-input" disabled="disabled">
                    </div>
                  </div>
                  <div class="layui-form-item">
                    <label class="layui-form-label">表单页面路径</label>
                    <div class="layui-input-block">
                      <input type="text" name="title" required  lay-verify="required" value="" autocomplete="off" class="layui-input" disabled="disabled">
                    </div>
                  </div>
                  <div class="layui-form-item">
                    <label class="layui-form-label">流程编号</label>
                    <div class="layui-input-block">
                      <input type="text" name="title" required  lay-verify="required" value="编号01" autocomplete="off" class="layui-input" disabled="disabled">
                    </div>
                  </div>
                </form>
            </div>
            <div class="foot">
                <button class="layui-btn layui-btn sure_btn">确定</button>
                <button class="layui-btn layui-btn layui-btn-primary cancel_btn">取消</button>
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
                
                <div id="demo">
                    
                </div>
            </div>
        </div>
    </body>
    
</html>
        <script type="text/javascript" src="<%=basePath%>/resources/tree/js/jquery.ztree.core.js"></script>
        <script type="text/javascript" src="<%=basePath%>/resources/tree/js/jquery.ztree.excheck.js"></script>
        <script type="text/javascript" src="<%=basePath%>/resources/tree/js/jquery.ztree.exedit.js"></script>
    <script>
        //tree
        var setting = {
            view: {
                addHoverDom: addHoverDom,
                removeHoverDom: removeHoverDom,
                selectedMulti: false
            },
            edit: {
                enable: true,
                editNameSelectAll: true,
                showRemoveBtn: showRemoveBtn,               
                showRenameBtn: showRenameBtn,           
            },
            data: {
            	key: {
            		name: "categoryName"
            	},
                simpleData: {
                    enable: true,
                    idKey: "categoryUid",
                    pIdKey: "categoryParent",
                    rootPId: "rootCategory"
                }
            },
            callback: {
            	onClick: zTreeOnClick,// 点击回调
                beforeDrag: beforeDrag,
                beforeEditName: beforeEditName,
                beforeRemove: beforeRemove,
                beforeRename: beforeRename,
                onRemove: onRemove,
                onRename: onRename
            }
        };

        var zNodes = ${zNodes};
        
        
        function zTreeOnClick(event, treeId, treeNode) {
        	//console.log("treeId:" + treeId);
        	//console.log("treeNode:" + treeNode);
        	console.log(treeNode.categoryUid);
        	console.log(treeNode.categoryName);
        }
        
        var log, className = "dark";
        function beforeDrag(treeId, treeNodes) {
            return false;
        }
        function beforeEditName(treeId, treeNode) {
            className = (className === "dark" ? "":"dark");
            showLog("[ "+getTime()+" beforeEditName ]&nbsp;&nbsp;&nbsp;&nbsp; " + treeNode.name);
            var zTree = $.fn.zTree.getZTreeObj("treeDemo");
            zTree.selectNode(treeNode);
            setTimeout(function() {
                if (confirm("进入节点 -- " + treeNode.name + " 的编辑状态吗？")) {
                    setTimeout(function() {
                        zTree.editName(treeNode);
                    }, 0);
                }
            }, 0);
            return false;
        }
        function beforeRemove(treeId, treeNode) {
        	console.log(treeId);
        	console.log(treeNode);
            className = (className === "dark" ? "":"dark");
            showLog("[ "+getTime()+" beforeRemove ]&nbsp;&nbsp;&nbsp;&nbsp; " + treeNode.name);
            var zTree = $.fn.zTree.getZTreeObj("treeDemo");
            zTree.selectNode(treeNode);
            return confirm("确认删除 节点 -- " + treeNode.name + " 吗？");
        }
        function onRemove(e, treeId, treeNode) {
            showLog("[ "+getTime()+" onRemove ]&nbsp;&nbsp;&nbsp;&nbsp; " + treeNode.name);
        }
        function beforeRename(treeId, treeNode, newName, isCancel) {
            className = (className === "dark" ? "":"dark");
            showLog((isCancel ? "<span style='color:red'>":"") + "[ "+getTime()+" beforeRename ]&nbsp;&nbsp;&nbsp;&nbsp; " + treeNode.name + (isCancel ? "</span>":""));
            if (newName.length == 0) {
                setTimeout(function() {
                    var zTree = $.fn.zTree.getZTreeObj("treeDemo");
                    zTree.cancelEditName();
                    alert("节点名称不能为空.");
                }, 0);
                return false;
            }
            return true;
        }
        function onRename(e, treeId, treeNode, isCancel) {
            showLog((isCancel ? "<span style='color:red'>":"") + "[ "+getTime()+" onRename ]&nbsp;&nbsp;&nbsp;&nbsp; " + treeNode.name + (isCancel ? "</span>":""));
        }
        function showRemoveBtn(treeId, treeNode) {
            return !treeNode.isFirstNode;
        }
        function showRenameBtn(treeId, treeNode) {
            return !treeNode.isLastNode;
        }
        function showLog(str) {
            if (!log) log = $("#log");
            log.append("<li class='"+className+"'>"+str+"</li>");
            if(log.children("li").length > 8) {
                log.get(0).removeChild(log.children("li")[0]);
            }
        }
        function getTime() {
            var now= new Date(),
            h=now.getHours(),
            m=now.getMinutes(),
            s=now.getSeconds(),
            ms=now.getMilliseconds();
            return (h+":"+m+":"+s+ " " +ms);
        }

        var newCount = 1;
        function addHoverDom(treeId, treeNode) {
            var sObj = $("#" + treeNode.tId + "_span");
            if (treeNode.editNameFlag || $("#addBtn_"+treeNode.tId).length>0) return;
            var addStr = "<span class='button add' id='addBtn_" + treeNode.tId
                + "' title='add node' onfocus='this.blur();'></span>";
            sObj.after(addStr);
            var btn = $("#addBtn_"+treeNode.tId);
            if (btn) btn.bind("click", function(){
                var zTree = $.fn.zTree.getZTreeObj("treeDemo");
                zTree.addNodes(treeNode, {id:(100 + newCount), pId:treeNode.id, name:"new node" + (newCount++)});
                return false;
            });
        };
        function removeHoverDom(treeId, treeNode) {
            $("#addBtn_"+treeNode.tId).unbind().remove();
        };
        function selectAll() {
            var zTree = $.fn.zTree.getZTreeObj("treeDemo");
            zTree.setting.edit.editNameSelectAll =  $("#selectAll").attr("checked");
        }
        //分页
        layui.use(['laypage', 'layer'], function(){
            var laypage = layui.laypage,layer = layui.layer;  
              //完整功能
            laypage.render({
                elem: 'demo7'
                ,count: 50
                ,limit: 10
                ,layout: ['count', 'prev', 'page', 'next', 'limit', 'skip']
                ,jump: function(obj){
                  console.log(obj)
                }
            }); 
            laypage.render({
                elem: 'demo8'
                ,count: 50
                ,limit: 5
                ,layout: ['count', 'prev', 'page', 'next', 'limit', 'skip']
                ,jump: function(obj){
                  console.log(obj)
                }
            }); 
        });
        
        $(document).ready(function(){
            $.fn.zTree.init($("#treeDemo"), setting, zNodes);
            $("#selectAll").bind("click", selectAll);
            
            $(".create_btn").click(function(){
                $(".display_container3").css("display","block");
            })
            $(".link_table tr td").dblclick(function(){
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