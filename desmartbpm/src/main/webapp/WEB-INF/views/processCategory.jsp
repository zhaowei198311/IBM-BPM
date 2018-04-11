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
                    <ul id="category_tree" class="ztree" style="width:auto;height:500px;"></ul>
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
                                    <button onclick="test();">test</button>
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
                
                
            </div>
        </div>
        <div class="display_container" id="addCategory_container">
             <div class="display_content">
                 <div class="top">填写分类名</div>
                 <div class="middle1">
                     <input type="text"  class="layui-input" id="categoryName_input"/>
                 </div>
                 <div class="foot">
                     <button class="layui-btn layui-btn sure_btn" id="addCategorySure_btn">确定</button>
                     <button class="layui-btn layui-btn layui-btn-primary cancel_btn" id="addCategoryCancel_btn">取消</button>
                 </div>
              </div>
         </div>
    </body>
    
</html>
        <script type="text/javascript" src="<%=basePath%>/resources/tree/js/jquery.ztree.core.js"></script>
        <script type="text/javascript" src="<%=basePath%>/resources/tree/js/jquery.ztree.excheck.js"></script>
        <script type="text/javascript" src="<%=basePath%>/resources/tree/js/jquery.ztree.exedit.js"></script>
    <script>
        var oldCategoryName = "";// 记录重命名分类的原名字
        var parentNode = "";  // 记录往哪个节点下新建分类
        var parentNodeTId = ""; // 父节点的zTree唯一id
        var tempRemoveTreeNode; // 记录要被删除的节点
        var tempRemoveParentTreeNode; // 记录要被删除的节点的父节点
        
        // 为翻页提供支持
        var pageConfig = {
        	pageNum = 1;
        	pageSize = 10;
        	categoryUid = rootCategory;
        	
        }
        
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
                renameTitle: '重命名分类',
                removeTitile: '删除分类',
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
                beforeEditName: beforeEditName, // 改名前函数
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
        	//console.log(treeNode.categoryName);
        }
        
        var log, className = "dark";
        function beforeDrag(treeId, treeNodes) {
            return false;
        }
        // 编辑节点名前
        function beforeEditName(treeId, treeNode) {
        	// 全局变量中记录修改前的名字，用于还原
        	oldCategoryName = treeNode.categoryName;
            className = (className === "dark" ? "":"dark");
            showLog("[ "+getTime()+" beforeEditName ]&nbsp;&nbsp;&nbsp;&nbsp; " + treeNode.name);
            var zTree = $.fn.zTree.getZTreeObj("category_tree");
            zTree.selectNode(treeNode);
            setTimeout(function() {
                if (confirm("进入分类" + treeNode.categoryName + " 的编辑状态吗？")) {
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
            var zTree = $.fn.zTree.getZTreeObj("category_tree");
            zTree.selectNode(treeNode);
            tempRemoveTreeNode = treeNode;
            tempRemoveParentTreeNode = treeNode.getParentNode();
            return confirm("确认删除分类" + treeNode.categoryName + " 吗？");
        }
        function onRemove(e, treeId, treeNode) {
        	$.ajax({
        		url: common.getPath() + "/processCategory/removeCategory",
        		dataType: "json",
        		type: "post",
        		data: {
        			"categoryUid": tempRemoveTreeNode.categoryUid
        		},
        		success: function(result) {
        			if (result.status == 0) {
        				
        			} else {
        				var zTree = $.fn.zTree.getZTreeObj("category_tree");
        	            zTree.addNodes(tempRemoveParentTreeNode,  tempRemoveTreeNode);
        				alert(result.msg);
        			}
        		},
        		error: function() {
        			var zTree = $.fn.zTree.getZTreeObj("category_tree");
                    zTree.addNodes(tempRemoveParentTreeNode,  tempRemoveTreeNode);
        		}
        	});
        }
        
        // 用户确认修改分类名
        function beforeRename(treeId, treeNode, newName, isCancel) {
            className = (className === "dark" ? "":"dark");
            showLog((isCancel ? "<span style='color:red'>":"") + "[ "+getTime()+" beforeRename ]&nbsp;&nbsp;&nbsp;&nbsp; " + treeNode.name + (isCancel ? "</span>":""));
            if (newName.length == 0) {
                setTimeout(function() {
                    var zTree = $.fn.zTree.getZTreeObj("category_tree");
                    zTree.cancelEditName();
                    alert("节点名称不能为空.");
                }, 0);
                return false;
            }
            return true;
        }
        
        function onRename(e, treeId, treeNode, isCancel) {
            showLog((isCancel ? "<span style='color:red'>":"") + "[ "+getTime()+" onRename ]&nbsp;&nbsp;&nbsp;&nbsp; " + treeNode.name + (isCancel ? "</span>":""));
            $.ajax({
                url: common.getPath() + "/processCategory/renameCategory",
                type: "post",
                data: {
                    "categoryUid": treeNode.categoryUid,
                    "newName": treeNode.categoryName
                },
                dataType: "json",
                success : function(result) {
                    if (result.status == 0 ){// 修改成功
                    } else {// 修改失败，还原名字
                    	var zTree = $.fn.zTree.getZTreeObj(treeId);
                    	treeNode.categoryName = oldCategoryName; 
                    	zTree.updateNode(treeNode);
                    	alert(result.msg);
                    }
                }
            });
        }
        // 是否显示删除按钮
        function showRemoveBtn(treeId, treeNode) {
        	if (treeNode.categoryUid == 'rootCategory') {
                return false;
            }
            return true;
        }
        // 是否显示改名按钮
        function showRenameBtn(treeId, treeNode) {
        	if (treeNode.categoryUid == 'rootCategory') {
        		return false;
        	}
            return true;
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
                + "' title='新增分类' onfocus='this.blur();'></span>";
            sObj.after(addStr);
            var btn = $("#addBtn_"+treeNode.tId);
            if (btn) btn.bind("click", function(){
            	parentNode = treeNode.categoryUid;
            	parentNodeTId = treeNode.tId;
                // ajax新建一个分类，再加一个节点
                $("#categoryName_input").val('');
            	$("#addCategory_container").css("display","block");
            	$("#categoryName_input").focus();
                return false;
            });
        };
        function removeHoverDom(treeId, treeNode) {
            $("#addBtn_"+treeNode.tId).unbind().remove();
        };
        function selectAll() {
            var zTree = $.fn.zTree.getZTreeObj("category_tree");
            zTree.setting.edit.editNameSelectAll =  $("#selectAll").attr("checked");
        }
        //分页
        layui.use(['laypage', 'layer'], function(){
            var laypage = layui.laypage,layer = layui.layer;  
              //完整功能
            laypage.render({
                elem: 'demo7',
                count: 50,
                limit: 10,
                layout: ['count', 'prev', 'page', 'next', 'limit', 'skip'],
                jump: function(obj){
                    console.log(obj);
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
            $.fn.zTree.init($("#category_tree"), setting, zNodes);
            
            getInfo();
            
            
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
            
            // 确认新建分类
            $("#addCategorySure_btn").click(function(){
            	var newName = $("#categoryName_input").val().trim();
            	
            	console.log(newName);
            	if (!newName) {
            		alert("分类名不能为空");
            		return;
            	}
            	$.ajax({
            		url: common.getPath() + "/processCategory/addCategory",
            		type: "post",
            		dataType: "json",
            		data: {
            			"categoryParent": parentNode,
            			"categoryName": newName
            		},
            		success: function(result) {
            			if (result.status == 0) {
            				$("#addCategory_container").css("display","none");
            				// 在tree上加入新节点
            				var zTree = $.fn.zTree.getZTreeObj("category_tree");
            				var treeNode = zTree.getNodeByTId(parentNodeTId);
            				zTree.addNodes(treeNode, {"categoryUid": result.data.categoryUid, "categoryParent": result.data.categoryParent, "categoryName": result.data.categoryName });
            			} else {
            				alert(result.msg);
            			}
            		}
            	});
            });
            
            // 取消新建分类
            $("#addCategoryCancel_btn").click(function(){
            	$("#addCategory_container").css("display","none")
            });
            
            
        });
        function test() {
        	$("#addCategory_container").css("display","block");
        }
        
        function getInfo() {
        	$.ajax({
        		url: common.getPath() + "/processCategory/addCategory",
        		type: "post",
        		dataType: "json",
        		data: {
        			"pageNum": pageNum1,
        			"pageSize": pageSize1
        		}
        	});
        }
  </script>