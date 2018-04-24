<!DOCTYPE html>
<%@ page language="java"  pageEncoding="UTF-8"%>
<html>
	<head>
  		<title>表单管理</title>
  		<%@include file="common/head.jsp" %>
        <%@include file="common/tag.jsp" %>
        <link href="<%=basePath%>/resources/tree/css/demo.css" rel="stylesheet">
        <link href="<%=basePath%>/resources/tree/css/zTreeStyle/zTreeStyle.css" rel="stylesheet">
  		<style>
			ul.ztree{
				border:0;
			}
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
						    	<input id="formTitle" type="text" placeholder="表单名称"  class="layui-input">
							</div>
							<div class="layui-col-md3" style="text-align:right;">
						        <button class="layui-btn layui-btn-sm" id="searchForm_btn">查询</button>
						        <button class="layui-btn layui-btn-sm create_btn">新增</button>
						        <button class="layui-btn layui-btn-sm delete_btn">删除</button>
						        <button class="layui-btn layui-btn-sm copy_btn">复制快照</button>
							</div>
						</div>
					</div>
					<div style="width:100%;overflow-x:auto;">				
						<table class="layui-table backlog_table" lay-even lay-skin="nob">
							<colgroup>
								<col>
							    <col>
							    <col>
							    <col>
							    <col>
							</colgroup>
							<thead>
							    <tr>
							      <th><input type="checkbox" name="" title='全选' lay-skin="primary"> 序号</th>
							      <th>表单名称</th>
							      <th>表单描述</th>
							      <th>创建时间</th>
							      <th>创建人</th>
							    </tr> 
							</thead>
							<tbody id="formInfo-table-tbody">
							    
							</tbody>
						</table>				
					</div>
					<div id="lay_page"></div>
					<div style="display:none;">
						<input type="hidden" id="proUid" value="${ proUid }"/>
						<input type="hidden" id="proVersion" value="${ proVersion }"/>
					</div>
			    </div>
		  	</div>
		</div>
		<div class="display_container">
			<div class="display_content">
				<div class="top">
					新增表单
				</div>
				<div class="middle">
					<form class="layui-form" action="" style="margin-top:30px;">
					  <div class="layui-form-item">
					    <label class="layui-form-label">表单名称</label>
					    <div class="layui-input-block">
					      <input type="text" id="form-name" name="formName" required  lay-verify="required" value="" autocomplete="off" class="layui-input">
					    </div>
					  </div>
					  <div class="layui-form-item">
					    <label class="layui-form-label">表单描述</label>
					    <div class="layui-input-block">
					      <input type="text" id="form-description" name="formDescription" required  lay-verify="required" value="" autocomplete="off" class="layui-input">
					    </div>
					  </div>				  
					</form>
				</div>
				<div class="foot">
					<button class="layui-btn layui-btn sure_btn" onclick="createForm();">确定</button>
					<button class="layui-btn layui-btn layui-btn-primary cancel_btn">取消</button>
				</div>
			</div>
		</div>
		<div class="display_container3">
			<div class="display_content3">
				<div class="top">
					复制快照
				</div>
				<div class="middle1">
					<table class="layui-table backlog_table" lay-even lay-skin="nob">
						<colgroup>
							<col>
						    <col>
						    <col>
						    <col>
						    <col>
						</colgroup>
						<thead>
						    <tr>
						      <th><input type="checkbox" name="" title='全选' lay-skin="primary"> 序号</th>
						      <th>表单名称</th>
						      <th>表单描述</th>
						      <th>创建时间</th>
						      <th>创建人</th>
						    </tr> 
						</thead>
						<tbody>
						    <tr>
						      <td><input type="checkbox" name="" title='全选' lay-skin="primary"> 1</td>
						      <td>表单名称</td>
						      <td>描述内容...</td>
						      <td>2018-04-10 10：00：00</td>
						      <td>zhangsan</td>
						    </tr>
						    <tr>
						      <td><input type="checkbox" name="" title='全选' lay-skin="primary"> 2</td>
						      <td>表单名称</td>
						      <td>描述内容...</td>
						      <td>2018-04-10 10：00：00</td>
						      <td>zhangsan</td>
						    </tr>
						    <tr>
						      <td><input type="checkbox" name="" title='全选' lay-skin="primary"> 3</td>
						      <td>表单名称</td>
						      <td>描述内容...</td>
						      <td>2018-04-10 10：00：00</td>
						      <td>zhangsan</td>
						    </tr>
						    <tr>
						      <td><input type="checkbox" name="" title='全选' lay-skin="primary"> 4</td>
						      <td>表单名称</td>
						      <td>描述内容...</td>
						      <td>2018-04-10 10：00：00</td>
						      <td>zhangsan</td>
						    </tr>
						    <tr>
						      <td><input type="checkbox" name="" title='全选' lay-skin="primary"> 5</td>
						      <td>表单名称</td>
						      <td>描述内容...</td>
						      <td>2018-04-10 10：00：00</td>
						      <td>zhangsan</td>
						    </tr>
						</tbody>
					</table>			
				</div>
				<div id="lay_page"></div>
				<div class="foot">
					<button class="layui-btn layui-btn sure_btn">确定</button>
					<button class="layui-btn layui-btn layui-btn-primary cancel_btn">取消</button>
				</div>
			</div>
		</div>
	</body>
	
</html>
	<script src="<%=basePath%>/resources/js/layui.all.js"></script>
	<script type="text/javascript" src="<%=basePath%>/resources/tree/js/jquery.ztree.core.js"></script>
    <script type="text/javascript" src="<%=basePath%>/resources/tree/js/jquery.ztree.excheck.js"></script>
    <script type="text/javascript" src="<%=basePath%>/resources/tree/js/jquery.ztree.exedit.js"></script>
	<script>
		// 为翻页提供支持
	    var pageConfig = {
	    	pageNum: 1,
	    	pageSize: 10,
	    	total: 0,
	    	proCategoryUid:"rootCategory",
	    	proUid:"",
	    	proVerUid:""
	    }
	    // 为弹出框的分页控件服务
	    /* var pageConfig2 = {
	        pageNum: 1,
	        pageSize: 5,
	        total: 0,
	        processAppName: "",
	        processAppAcronym: "",
	        display: ""
	    } */
	
	    var createFromFlag = false;//是否可以创建表单的控制变量
	    
		//tree
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
                onClick: zTreeOnClick// 点击回调 ，自己定义
            }
    	};

	    function zTreeOnClick(event, treeId, treeNode) {
	    	switch(treeNode.itemType){
	    		case "category":{
	    			createFromFlag = false;
	    			if(treeNode.id=="rootCategory"){
	    				pageConfig.proCategoryUid = "";
		    			pageConfig.proUid = "";
		    			pageConfig.proVerUid = "";
		    			getFormInfoByProDefinition();
	    			}else{
	    				pageConfig.proCategoryUid = treeNode.id;
		    			pageConfig.proUid = "";
		    			pageConfig.proVerUid = "";
		    			getFormInfoByProDefinition();
	    			}
	    			break;
	    		}
	    		case "processMeta":{//根据流程元获得流程定义-->得到表单数据
	    			createFromFlag = false;
	    			pageConfig.proCategoryUid = "";
	    			pageConfig.proUid = treeNode.id;
	    			pageConfig.proVerUid = "";
	    			getFormInfoByProDefinition();
	    			break;
	    		}
	    		case "processDefinition":{//根据流程定义的ID以及版本ID获得表单数据
	    			createFromFlag = true;
	    			pageConfig.proCategoryUid = "";
	    			pageConfig.proUid = treeNode.pid;
	    			pageConfig.proVerUid = treeNode.id;
	    			getFormInfoByProDefinition();
	    			break;
	    		}
	    	}
	    };

		// 分页
        function doPage() {
            layui.use(['laypage', 'layer'], function(){
                var laypage = layui.laypage,layer = layui.layer;  
                  //完整功能
                laypage.render({
                    elem: 'lay_page',
                    curr: pageConfig.pageNum,
                    count: pageConfig.total,
                    limit: pageConfig.pageSize,
                    layout: ['count', 'prev', 'page', 'next', 'limit', 'skip'],
                    jump: function(obj, first){
                    	// obj包含了当前分页的所有参数  
                    	pageConfig.pageNum = obj.curr;
                    	pageConfig.pageSize = obj.limit;
                    	if (!first) {
                    		getFormInfoByProDefinition();
                    	}
                    }
                }); 
            });
        }

		//模糊查询
        $("#searchForm_btn").click(function() {
        	pageConfig.pageNum = 1;
        	pageConfig.total = 0;
        	getFormInfoByProDefinition();
        });
		
		$(document).ready(function() {
			var proUid = $("#proUid").val();
			var proVersion = $("#proVersion").val();
			if(proUid!=null && proUid!="" && proVersion!="" && proVersion!=null){
				//获得流程树的数据
				$.ajax({
					 url: common.getPath() + "/formManage/getTreeData",
					 type: "post",
					 async:false,
					 dataType: "json",
					 success: function(result) {
						 $.fn.zTree.init($("#category_tree"), setting, result);
					 }
				 });
				var treeObj = $.fn.zTree.getZTreeObj("category_tree");
				var parentNode = treeObj.getNodeByParam("id", proUid)
				var node = treeObj.getNodeByParam("id", proVersion, parentNode);
				treeObj.selectNode(node,true);
				pageConfig.proCategoryUid = "";
				pageConfig.proUid = proUid;
				pageConfig.proVerUid = proVersion;
				getFormInfoByProDefinition();
				createFromFlag = true;
			}else{
				//获得流程树的数据
				$.ajax({
					 url: common.getPath() + "/formManage/getTreeData",
					 type: "post",
					 async:true,
					 dataType: "json",
					 success: function(result) {
						 $.fn.zTree.init($("#category_tree"), setting, result);
					 }
				 });
				pageConfig.proCategoryUid = "";
				pageConfig.proUid = "";
				pageConfig.proVerUid = "";
				getFormInfoByProDefinition();
			}
			
			$(".create_btn").click(function() {
				if(createFromFlag){
					$(".display_container").css("display", "block");
				}else{
					layer.alert("请选择一个流程定义版本");
				}
			})
			/* $(".copy_btn").click(function() {
				$(".display_container3").css("display", "block");
			})
			$(".sure_btn").click(function() {
				$(".display_container3").css("display", "none");
			}) */
			$(".cancel_btn").click(function() {
				$(".display_container").css("display", "none");
				$(".display_container3").css("display", "none");
			})
		});
		
		//新建表单--跳转到表单设计器页面
		function createForm(){
			var formName = $("#form-name").val().trim();
			if(formName==null || formName==""){
				layer.alert("请填写表单名");
			}else{
				$.ajax({
					url:common.getPath()+"/formManage/queryFormByName",
					method:"post",
					data:{
						dynTitle:formName
					},
					success:function(result){
						if(result.status==0){
							var href = "/formManage/designForm?formName="+formName
									+"&formDescription="+$("#form-description").val().trim()
									+"&proUid="+pageConfig.proUid
									+"&proVersion="+pageConfig.proVerUid;
							window.location.href = common.getPath()+href;
							$(".display_container").css("display", "none");
						}else{
							layer.alert("表单名已存在，不能重复");
						}
					}
				});
			}
			return;
		}
		
		//根据流程定义(proUid,proVerUid)
		function getFormInfoByProDefinition(){
			$.ajax({
				url:common.getPath() + "/formManage/listFormByProDefinition",
				type:"post",
				dataType:"json",
				data:{
					"pageNum":pageConfig.pageNum,
					"pageSize":pageConfig.pageSize,
					"proUid":pageConfig.proUid,
					"proVerUid":pageConfig.proVerUid,
					"proCategoryUid":pageConfig.proCategoryUid,
					"formTitle":$("#formTitle").val().trim()
				},
				success:function(result){
					if(result.status==0){
						drawTable(result.data);
					}
				}
			});
		}
		
		// 渲染表格
        function drawTable(pageInfo) {
        	pageConfig.pageNum = pageInfo.pageNum;
        	pageConfig.pageSize = pageInfo.pageSize;
        	pageConfig.total = pageInfo.total;
        	doPage();
        	// 渲染数据
        	$("#formInfo-table-tbody").html('');
        	if (pageInfo.total == 0) {
        		return;
        	}
        	
        	var list = pageInfo.list;
        	var startSort = pageInfo.startRow;//开始序号
        	var trs = "";
        	for(var i=0; i<list.length; i++) {
        		console.log($("#formInfo-table-tbody").find('tr').length)
        		var formInfo = list[i];
        		var sortNum = startSort + i;
        		var createTime = "";
                var updateTime = "";
                if (formInfo.createTime) {
                	createTime = common.dateToString(new Date(formInfo.createTime));
                }
        		trs += '<tr data-formuid="'+formInfo.dynUid+'" ondblclick="showEditDiv(this);">'
        					+ '<td><input type="checkbox" name="formInfo_check" value="' + formInfo.dynUid + '" lay-skin="primary">'+ sortNum +'</td>'
        		            + '<td>'+formInfo.dynTitle+'</td>'
        		            + '<td>'+formInfo.dynDescription+'</td>'
        		            + '<td>'+createTime+'</td>'
        		            + '<td>'+formInfo.creatorFullName+'</td>'
        		            + '</tr>';
        	}
        	$("#formInfo-table-tbody").append(trs);
        	
        }
		
		function showEditDiv(tr){
			var formuid = $(tr).data('formuid');
			alert(formuid);
		}
	</script>
