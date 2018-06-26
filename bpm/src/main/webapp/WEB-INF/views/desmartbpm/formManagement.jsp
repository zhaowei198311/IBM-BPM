<!DOCTYPE html>
<%@ page language="java"  pageEncoding="UTF-8"%>
<html>
	<head>
  		<title>流程表单管理</title>
  		<%@include file="common/head.jsp" %>
        <%@include file="common/tag.jsp" %>
        <link rel="stylesheet" href="<%=basePath%>/resources/desmartbpm/css/my.css" media="all">
        <link href="<%=basePath%>/resources/desmartbpm/tree/css/demo.css" rel="stylesheet">
        <link href="<%=basePath%>/resources/desmartbpm/tree/css/zTreeStyle/zTreeStyle.css" rel="stylesheet">
  		<style>
			ul.ztree {
				border: 0;
			}
			
			.display_container_copy {
				display: none;
				position: absolute;
				top: 0;
				left: 0;
				z-index: 10;
				background: rgba(0,0,0,0.3);
				width: 100%;
				height: 100%;
			}
			.display_content3{
				width:800px;
			}
			.display_content_copy {
				overflow-y: auto;
				color: #717171;
				padding: 20px;
				width: 400px;
				height: 300px;
				background: #fff;
				position: absolute;
				margin: 100px 0 0 -200px;
				left: 50%;
				box-shadow: 0 0 10px #ccc;
			}
			
			.display_container_sel_copy{
				display: none;
				position: absolute;
				top: 0;
				left: 0;
				z-index: 10;
				background: rgba(0,0,0,0.3);
				width: 100%;
				height: 100%;
			}
			.display_content_sel_copy{
				overflow-y: auto;
				color: #717171;
				padding: 20px;
				width: 400px;
				height: 300px;
				background: #fff;
				position: absolute;
				margin: 100px 0 0 -200px;
				left: 50%;
				box-shadow: 0 0 10px #ccc;
			}
			.display_sel_copy{
				display: none;
				position: absolute;
				top: 0;
				left: 0;
				z-index: 10;
				background: rgba(0,0,0,0.3);
				width: 100%;
				height: 100%;
			}
			
			.display_sel_content_copy {
				overflow-y: auto;
				color: #717171;
				padding: 20px;
				width: 280px;
				height: 400px;
				background: #fff;
				position: absolute;
				margin: 100px 0 0 -200px;
				left: 50%;
				box-shadow: 0 0 10px #ccc;
			}
			
			.middle_copy{
				height: 300px;
				width: 96%;
				border: 1px solid #ccc;
				position: relative;
				padding: 0 10px;
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
							<div class="layui-col-md9 layui-col-md-offset1" style="text-align:left;">
						        <button class="layui-btn" id="searchForm_btn">查询</button>
						        <button class="layui-btn create_btn">新增</button>
						        <button class="layui-btn delete_btn" style="background: #FF5151" onclick="deleteForm();">删除</button>
						        <button class="layui-btn copy_sel_btn">复制到其它流程</button>
						        <button class="layui-btn copy_btn">从其它流程复制</button>
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
							      <th><input type="checkbox" name="allSel" onclick="onClickHander(this);" title='全选' lay-skin="primary"> 序号</th>
							      <th>表单名称</th>
							      <th>表单描述</th>
							      <th>所属流程名</th>
							      <th>所属快照名</th>
							      <th>操作</th>
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
					<form class="layui-form" id="create-form" action="" style="margin-top:30px;">
					  <div class="layui-form-item">
					    <label class="layui-form-label">表单名称</label>
					    <div class="layui-input-block">
					      <input type="text" id="form-name" name="formName" class="layui-input">
					    </div>
					  </div>
					  <div class="layui-form-item">
					    <label class="layui-form-label">表单描述</label>
					    <div class="layui-input-block">
					      <input type="text" id="form-description" name="formDescription" class="layui-input">
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
		<div class="display_container2">
			<div class="display_content">
				<div class="top">
					修改表单信息
				</div>
				<div class="middle">
					<form class="layui-form" action="" style="margin-top:30px;">
					  <div class="layui-form-item">
					    <label class="layui-form-label">表单名称</label>
					    <div class="layui-input-block">
					      <input type="text" id="update-form-name" name="formName" class="layui-input">
					    </div>
					  </div>
					  <div class="layui-form-item">
					    <label class="layui-form-label">表单描述</label>
					    <div class="layui-input-block">
					      <input type="text" id="update-form-description" name="formDescription" class="layui-input">
					    </div>
					  </div>				  
					</form>
				</div>
				<div class="foot">
					<button class="layui-btn layui-btn sure_btn" onclick="updateForm();">确定</button>
					<button class="layui-btn layui-btn layui-btn-primary cancel_btn">取消</button>
				</div>
			</div>
		</div>
		<div class="display_container3">
			<div class="display_content3">
				<div class="top">
					<div class="layui-col-md12">请选择一个要复制的表单</div>
					<div class="">
						<div class="layui-col-md3" style="margin:5px 0 ;">
							<input id="copy_formTitle" type="text" class="layui-input" placeholder="表单名称" style="font-size:15px;">
						</div>
						<div class="layui-col-md2 layui-col-md-offset1" style="text-align:center;margin:5px 0;">
							<button class="layui-btn layui-btn-sm" id="copy_searchForm_btn">查询</button>
						</div>
					</div>
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
						      <th>序号</th>
						      <th>表单名称</th>
						      <th>表单描述</th>
						      <th>所属流程名</th>
						      <th>所属快照名</th>
						    </tr> 
						</thead>
						<tbody id="copy_formInfo">
						    
						</tbody>
					</table>			
				</div>
				<div id="lay_page_copy"></div>
				<div class="foot">
					<button class="layui-btn layui-btn sure_btn" id="copy_form_btn">确定</button>
					<button class="layui-btn layui-btn layui-btn-primary cancel_btn" 
						onclick="$('.display_container3').css('display','none');">取消</button>
				</div>
			</div>
		</div>
		<!-- 复制表单填写信息模态框 -->
		<div class="display_container_copy">
			<div class="display_content_copy">
				<div class="top">
					请填写复制之后表单信息
				</div>
				<div class="middle">
					<form class="layui-form" action="" style="margin-top:30px;">
					  <div class="layui-form-item">
					    <label class="layui-form-label">表单名称</label>
					    <div class="layui-input-block">
					      <input type="text" id="copy-form-name" name="formName" class="layui-input">
					    </div>
					  </div>
					  <div class="layui-form-item">
					    <label class="layui-form-label">表单描述</label>
					    <div class="layui-input-block">
					      <input type="text" id="copy-form-description" name="formDescription" class="layui-input">
					    </div>
					  </div>				  
					</form>
				</div>
				<div class="foot">
					<button class="layui-btn layui-btn sure_btn" onclick="copyForm();">确定</button>
					<button class="layui-btn layui-btn layui-btn-primary cancel_btn"
						onclick="$('.display_container_copy').css('display','none');">取消</button>
				</div>
			</div>
		</div>
		
		<!-- 复制表单到指定位置的模态框 -->
		<div class="display_sel_copy">
			<div class="display_sel_content_copy">
				<div class="top">
					请选择一个流程快照
				</div>
				<div class="middle_copy">
					<ul id="category_copy_tree" class="ztree" style="width:auto;height:93%;"></ul>
				</div>
				<div class="foot">
					<button class="layui-btn layui-btn sure_btn" onclick="sureCopyPro()">确定</button>
					<button class="layui-btn layui-btn layui-btn-primary cancel_btn" onclick="$('.display_sel_copy').css('display','none');">取消</button>
				</div>
			</div>
		</div>
		
		<!-- 复制表单到指定位置的填写信息模态框 -->
		<div class="display_container_sel_copy">
			<div class="display_content_sel_copy">
				<div class="top">
					请填写复制之后表单信息
				</div>
				<div class="middle">
					<form class="layui-form" action="" style="margin-top:30px;">
					  <div class="layui-form-item">
					    <label class="layui-form-label">表单名称</label>
					    <div class="layui-input-block">
					      <input type="text" id="copy-sel-form-name" name="formName" class="layui-input">
					    </div>
					  </div>
					  <div class="layui-form-item">
					    <label class="layui-form-label">表单描述</label>
					    <div class="layui-input-block">
					      <input type="text" id="copy-sel-form-description" name="formDescription" class="layui-input">
					    </div>
					  </div>				  
					</form>
				</div>
				<div class="foot">
					<button class="layui-btn layui-btn sure_btn" onclick="copySelForm();">确定</button>
					<button class="layui-btn layui-btn layui-btn-primary cancel_btn"
						onclick="$('.display_container_sel_copy').css('display','none');">取消</button>
				</div>
			</div>
		</div>
	</body>
</html>
<script type="text/javascript" src="<%=basePath%>/resources/desmartbpm/js/layui.all.js"></script>
<script type="text/javascript" src="<%=basePath%>/resources/desmartbpm/tree/js/jquery.ztree.core.js"></script>
<script type="text/javascript" src="<%=basePath%>/resources/desmartbpm/tree/js/jquery.ztree.excheck.js"></script>
<script type="text/javascript" src="<%=basePath%>/resources/desmartbpm/tree/js/jquery.ztree.exedit.js"></script>
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
	    var pageConfigCopy = {
	    	pageNum: 1,
		    pageSize: 5,
		    total: 0
	    }
	
	    var createFromFlag = false;//是否可以创建表单的控制变量
	    var copyFromFlag = false;//是否可以复制表单的控制变量
	    var oldFormName = "";//修改表单信息时表单的旧名称
	    var oldFormDescription = "";//修改表单信息时的旧描述
	    var oldProUid = "";//修改表单信息时的流程id
	    var oldProVersion = "";//修改表单信息时的流程版本id
	    var updateFormId = "";//修改表单时表单的Id
	    var copyFormId = "";//复制表单时表单的Id
	    var copyFormSelId = "";//复制表单到其他快照时表单的Id
	    var copyFormSelProId = "";//复制表单到其他快照时流程的Id
	    var copyFormSelVerId = "";//复制表单到其他快照时快照的Id
	    
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
	    
		var setting1 = {
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
	                onClick: zTreeCopyOnClick// 点击回调 ，自己定义
	            }
	    	};

	    function zTreeOnClick(event, treeId, treeNode) {
	    	switch(treeNode.itemType){
	    		case "category":{
	    			createFromFlag = false;
	    			$("input[name='allSel']").prop("checked",false);
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
	    			$("input[name='allSel']").prop("checked",false);
	    			pageConfig.proCategoryUid = "";
	    			pageConfig.proUid = treeNode.id;
	    			pageConfig.proVerUid = "";
	    			getFormInfoByProDefinition();
	    			break;
	    		}
	    		case "processDefinition":{//根据流程定义的ID以及版本ID获得表单数据
	    			createFromFlag = true;
	    			$("input[name='allSel']").prop("checked",false);
	    			pageConfig.proCategoryUid = "";
	    			pageConfig.proUid = treeNode.pid;
	    			pageConfig.proVerUid = treeNode.id;
	    			getFormInfoByProDefinition();
	    			break;
	    		}
	    	}
	    };
	    
	    function zTreeCopyOnClick(event, treeId, treeNode) {
	    	switch(treeNode.itemType){
	    		case "category":{
	    			copyFromFlag = false;
	    			break;
	    		}
	    		case "processMeta":{
	    			copyFromFlag = false;
	    			break;
	    		}
	    		case "processDefinition":{
	    			copyFromFlag = true;
	    			copyFormSelProId = treeNode.pid;
	    			copyFormSelVerId = treeNode.id;
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
                    		$("input[name='allSel']").prop("checked",false);
                    		getFormInfoByProDefinition();
                    	}
                    }
                }); 
            });
        }
		
     	// 分页
        function doPageCopy() {
            layui.use(['laypage', 'layer'], function(){
                var laypage = layui.laypage,layer = layui.layer;  
                //完整功能
                laypage.render({
                    elem: 'lay_page_copy',
                    curr: pageConfigCopy.pageNum,
                    count: pageConfigCopy.total,
                    limit: pageConfigCopy.pageSize,
                    layout: ['count', 'prev', 'page', 'next', 'skip'],
                    jump: function(obj, first){
                    	// obj包含了当前分页的所有参数  
                    	pageConfigCopy.pageNum = obj.curr;
                    	pageConfigCopy.pageSize = obj.limit;
                    	if (!first) {
                    		getFormInfoByProDefinitionCopy();
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
		
      	//复制快照--模糊查询
        $("#copy_searchForm_btn").click(function() {
        	pageConfigCopy.pageNum = 1;
        	pageConfigCopy.total = 0;
        	getFormInfoByProDefinitionCopy();
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
						 $.fn.zTree.init($("#category_copy_tree"), setting1, result);
					 }
				 });
				var treeObj = $.fn.zTree.getZTreeObj("category_tree");
				var parentNode = treeObj.getNodeByParam("id", proUid);
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
						 $.fn.zTree.init($("#category_copy_tree"), setting1, result);
					 }
				 });
				pageConfig.proCategoryUid = "";
				pageConfig.proUid = "";
				pageConfig.proVerUid = "";
				getFormInfoByProDefinition();
			}
			
			$(".create_btn").click(function() {
				if(createFromFlag){
					$("#create-form")[0].reset();
					$(".display_container").css("display", "block");
				}else{
					layer.alert("请选择一个流程定义版本");
				}
			})
			
			$(".copy_btn").click(function() {
				if(createFromFlag){
					$("#copy_formTitle").val("");
					//复制快照--表格数据
					getFormInfoByProDefinitionCopy();
					$(".display_container3").css("display", "block");
				}else{
					layer.alert("请选择一个存放复制快照的流程定义版本");
				}
			});
			
			$("#copy_form_btn").click(function() {
				var checkSel = $("input[name='copyFormInfo_check']:checked");
				if(checkSel.length==1){
					copyFormId = checkSel.val();
					var copyFormName = checkSel.parent().next().text().trim()+"_copy"+_getRandomString(2);
					$("#copy-form-name").val(copyFormName);
					var oldFormDes = checkSel.parent().next().next().text().trim();
					if(oldFormDes!=null && oldFormDes!=""){
						$("#copy-form-description").val(oldFormDes+"_copy");
					}
					$(".display_container_copy").css("display", "block");
				}else{
					layer.alert("请选择一个要复制的表单");
				}
			});
			
			$(".copy_sel_btn").click(function() {
				var checkInput = $("input[name='formInfo_check']:checked");
				if(checkInput.length<1){
					layer.alert("请选择一个要复制的表单");
				}else if(checkInput.length==1){
					copyFromFlag = false;
					copyFormSelId = checkInput.val();
					var copyFormName = checkInput.parent().next().text().trim()+"_copy"+_getRandomString(2);
					$("#copy-sel-form-name").val(copyFormName);
					var oldFormDes = checkInput.parent().next().next().text().trim();
					if(oldFormDes!=null && oldFormDes!=""){
						$("#copy-sel-form-description").val(oldFormDes+"_copy");
					}
					$(".display_sel_copy").css("display","block");
				}else{
					layer.alert("一次只能复制一个表单");
				}
			});
			
			$(".cancel_btn").click(function() {
				$(".display_container").css("display", "none");
				$(".display_container2").css("display", "none");
			})
		});
		
		function sureCopyPro(){
			if(copyFromFlag){
				$(".display_sel_copy").css("display","none");
				$(".display_container_sel_copy").css("display","block");
			}else{
				layer.alert("请选择一个流程快照");
			}
		}
		
		function copySelForm(){
			var proUid = copyFormSelProId;
			var proVersion = copyFormSelVerId;
			var formName = $("#copy-sel-form-name").val().trim();
			var formDescription = $("#copy-sel-form-description").val().trim();
			
			$.ajax({
				url:common.getPath()+"/formManage/queryProFormByName",
				method:"post",
				data:{
					dynTitle:formName,
					proUid:proUid,
					proVersion:proVersion
				},
				success:function(result){
					if(result.status==0){ 
						$.ajax({
							url:common.getPath()+"/formManage/copyForm",
							method:"post",
							data:{
								dynUid:copyFormSelId,
								dynTitle:formName,
								dynDescription:formDescription,
								proUid:proUid,
								proVersion:proVersion
							},
							success:function(result2){
								if(result2.status==0){
									getFormInfoByProDefinition();
									$(".display_container_sel_copy").css("display", "none");
									layer.alert("复制成功");
								}else{
									layer.alert("复制失败");
								}
							}
						});
					}else{
						layer.alert("同一个流程定义表单名不能重复");
					} 
				}
			});	
		}
		
		//复制快照--根据流程定义(proUid,proVerUid)
		function getFormInfoByProDefinitionCopy(){
			$.ajax({
				url:common.getPath() + "/formManage/listFormByProDefinition",
				type:"post",
				dataType:"json",
				data:{
					"pageNum":pageConfigCopy.pageNum,
					"pageSize":pageConfigCopy.pageSize,
					"formTitle":$("#copy_formTitle").val().trim()
				},
				success:function(result){
					if(result.status==0){
						drawTableCopy(result.data);
					}
				}
			});
		}
		
		//复制快照--渲染表格
		function drawTableCopy(pageInfo) {
        	pageConfigCopy.pageNum = pageInfo.pageNum;
        	pageConfigCopy.pageSize = pageInfo.pageSize;
        	pageConfigCopy.total = pageInfo.total;
        	doPageCopy();
        	// 渲染数据
        	$("#copy_formInfo").html('');
        	if (pageInfo.total == 0) {
        		return;
        	}
        	
        	var list = pageInfo.list;
        	var startSort = pageInfo.startRow;//开始序号
        	var trs = "";
        	for(var i=0; i<list.length; i++) {
        		var formInfo = list[i];
        		var sortNum = startSort + i;
                var updateTime = "";
        		trs += '<tr data-formuid="'+formInfo.dynUid
        					+'" data-prouid="'+formInfo.proUid
        					+'" data-proversion="'+formInfo.proVersion+'">'
        					+ '<td><input type="checkbox" name="copyFormInfo_check" onclick="onSelOne(this);" value="' + formInfo.dynUid + '" lay-skin="primary"> '+ sortNum +'</td>'
        		            + '<td>'+formInfo.dynTitle+'</td>'
        		if(formInfo.dynDescription!=null && formInfo.dynDescription!=""){
        			trs += '<td>'+formInfo.dynDescription+'</td>';
        		}else{
        			trs += '<td></td>';
        		}
        		trs += '<td>'+formInfo.proMetaName+'</td>'
        		            + '<td>'+formInfo.proVerName+'</td>'
        		            + '</tr>';
        	}
        	$("#copy_formInfo").append(trs);
        }
		
		//根据流程定义(proUid,proVerUid)
		function getFormInfoByProDefinition(){
			layer.load(1);
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
						layer.closeAll("loading");
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
        		var formInfo = list[i];
        		var sortNum = startSort + i;
        		var createTime = "";
                var updateTime = "";
        		trs += '<tr data-formuid="'+formInfo.dynUid
        					+'" data-prouid="'+formInfo.proUid
        					+'" data-proversion="'+formInfo.proversion+'">'
        					+ '<td><input type="checkbox" name="formInfo_check" onclick="onClickSel(this);" value="' + formInfo.dynUid + '" lay-skin="primary"> '+ sortNum +'</td>'
        		            + '<td>'+formInfo.dynTitle+'</td>';
        		if(formInfo.dynDescription!=null && formInfo.dynDescription!=""){
        		   trs += '<td>'+formInfo.dynDescription+'</td>';
        		}else{
        		   trs += '<td></td>';
        		}
        		trs += '<td>'+formInfo.proMetaName+'</td>'
	            			+'<td>'+formInfo.proVerName+'</td>'
        		            + '<td><i class="layui-icon" onclick="updateFormModal(this);" title="修改表单属性">&#xe642;</i>'+
        		            ' <i class="layui-icon" onclick="updateFormContent(this);" title="修改表单内容">&#xe60a;</i></td>'
        		            + '</tr>';
        	}
        	$("#formInfo-table-tbody").append(trs);
        }
		
      	//新建表单--跳转到表单设计器页面
		function createForm(){
			var formName = $("#form-name").val().trim();
			if(formName==null || formName==""){
				layer.alert("请填写表单名");
			}else{
				$.ajax({
					url:common.getPath()+"/formManage/queryProFormByName",
					method:"post",
					data:{
						dynTitle:formName,
						proUid:pageConfig.proUid,
						proVersion:pageConfig.proVerUid
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
							layer.alert("同一个流程定义表单名不能重复");
						} 
					}
				});
			}
		}
		
		//修改表单信息
		function updateForm(){
			var formName = $("#update-form-name").val().trim();
			var formDescription = $("#update-form-description").val().trim();
			if(formName==null || formName==""){
				layer.alert("请填写表单名");
			}else if(oldFormName==formName && oldFormDescription==formDescription){
				$(".display_container2").css("display", "none");
			}else if(oldFormName==formName && oldFormDescription!=formDescription){
				$.ajax({
					url:common.getPath()+"/formManage/updateFormInfo",
					method:"post",
					data:{
						dynUid:updateFormId,
						dynTitle:formName,
						dynDescription:formDescription
					},
					success:function(result2){
						if(result2.status==0){
							layer.alert("表单属性修改成功");
							getFormInfoByProDefinition();
							$(".display_container2").css("display", "none");
						}else{
							layer.alert("表单属性修改失败");
						}
					}
				});
			}else{
				$.ajax({
					url:common.getPath()+"/formManage/queryProFormByName",
					method:"post",
					data:{
						dynTitle:formName,
						proUid:oldProUid,
						proVersion:oldProVersion
					},
					success:function(result){
						if(result.status==0){
							$.ajax({
								url:common.getPath()+"/formManage/updateFormInfo",
								method:"post",
								data:{
									dynUid:updateFormId,
									dynTitle:formName,
									dynDescription:formDescription
								},
								success:function(result2){
									if(result2.status==0){
										layer.alert("表单属性修改成功");
										getFormInfoByProDefinition();
										$(".display_container2").css("display", "none");
									}else{
										layer.alert("表单属性修改失败");
									}
								}
							});
						}else{
							layer.alert("同一个流程定义表单名不能重复");
						} 
					}
				});
			}
		}
		
		//修改表单内容
		function updateFormContent(obj){
			var trObj = $(obj).parent().parent();
			var formId = trObj.data("formuid");
			var dynTitle = $(trObj.find("td")[1]).text().trim();
			var dynDescription = $(trObj.find("td")[2]).text().trim();
			var href = "/formManage/designForm?formUid="+formId
					+"&formName="+dynTitle
					+"&formDescription="+dynDescription
					+"&proUid="+pageConfig.proUid	//当用户未点击流程定义时，表单内容中包含了流程定义Id及版本Id
					+"&proVersion="+pageConfig.proVerUid;
			window.location.href = common.getPath()+href;
		}
		
		//删除表单
		function deleteForm(){
			var checkedArr = $("input[name='formInfo_check']:checked");
			if(checkedArr.length==0){
				layer.alert("请选中要删除的表单数据");
			}else{
				var checkedFormUid = new Array();
				checkedArr.each(function(){
					checkedFormUid.push($(this).val());
				});
				
				layer.confirm("确认删除表单数据？", function () {
					//先判断表单数据是否可删除
					$.ajax({
						url:common.getPath()+"/formManage/isBindStep",
						method:"post",
						data:{"formUids":checkedFormUid},
						traditional: true,//传递数组给后台
						success:function(result){
							if(result.status==0){//未绑定
								$.ajax({
									url:common.getPath()+"/formManage/deleteForm",
									method:"post",
									data:{"formUids":checkedFormUid},
									traditional: true,//传递数组给后台
									success:function(result){
										if(result.status==0){
											getFormInfoByProDefinition();
											//如果有checkbox没有被选中
											if($("input[name='formInfo_check']:checked").length==0 ||
													$("input[name='formInfo_check']:checked").length==$("input[name='formInfo_check']").length){
												$("input[name='allSel']").prop("checked",false);
											}
											layer.alert('删除表单数据成功');
										}else{
											layer.alert("删除表单数据失败");
										}
									}
								});// end delete
							}else{
								layer.alert("该表单已被步骤绑定");
							}
						}
					});//end ajax
				});//end layer.confirm
			}
		}
		
		//复制表单数据
		function copyForm(){
			var proUid = pageConfig.proUid;
			var proVersion = pageConfig.proVerUid;
			var formName = $("#copy-form-name").val().trim();
			var formDescription = $("#copy-form-description").val().trim();
			
			$.ajax({
				url:common.getPath()+"/formManage/queryProFormByName",
				method:"post",
				data:{
					dynTitle:formName,
					proUid:proUid,
					proVersion:proVersion
				},
				success:function(result){
					if(result.status==0){
						$.ajax({
							url:common.getPath()+"/formManage/copyForm",
							method:"post",
							data:{
								dynUid:copyFormId,
								dynTitle:formName,
								dynDescription:formDescription,
								proUid:proUid,
								proVersion:proVersion
							},
							success:function(result2){
								if(result2.status==0){
									getFormInfoByProDefinition();
									$(".display_container_copy").css("display", "none");
									$(".display_container3").css("display", "none");
								}else{
									layer.alert("复制失败");
								}
							}
						});
					}else{
						layer.alert("同一个流程定义表单名不能重复");
					}
				}
			});
		} 
		
		//点击修改按钮
		function updateFormModal(obj){
			var trObj = $(obj).parent().parent();
			updateFormId = trObj.data("formuid");
			var dynTitle = $(trObj.find("td")[1]).text().trim();
			var dynDescription = $(trObj.find("td")[2]).text().trim();
			oldFormDescription = dynDescription;
			oldFormName = dynTitle;
			oldProUid = trObj.data("prouid");
			oldProVersion = trObj.data("proversion");
			$("#update-form-name").val(dynTitle);
			$("#update-form-description").val(dynDescription);
			$(".display_container2").css("display", "block");
		}
		
		//复选框全选，取消全选
		function onClickHander(obj){
			if(obj.checked){
				$("input[name='formInfo_check']").prop("checked",true);
			}else{
				$("input[name='formInfo_check']").prop("checked",false);
			}
		}
		
		//复选框分选
		function onClickSel(obj){
			if(obj.checked){
				var allSel = false;
				$("input[name='formInfo_check']").each(function(){
					if(!$(this).is(":checked")){
						allSel = true;
					}
				});
				
				//如果有checkbox没有被选中
				if(allSel){
					$("input[name='allSel']").prop("checked",false);
				}else{
					$("input[name='allSel']").prop("checked",true);
				}
			}else{
				$("input[name='allSel']").prop("checked",false);
			}
		}
		
		//复制快照复选框只能选择一个
		function onSelOne(obj){
			$('input[name="copyFormInfo_check"]').not($(obj)).prop("checked", false);
		}
		
		//生成随机码的方法
		function _getRandomString(len) {  
		    len = len || 32;  
		    var $chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'; // 默认去掉了容易混淆的字符oOLl,9gq,Vv,Uu,I1  
		    var maxPos = $chars.length;  
		    var pwd = '';  
		    for (i = 0; i < len; i++) {  
		        pwd += $chars.charAt(Math.floor(Math.random() * maxPos));  
		    }  
		    return pwd;  
		} 
</script>
