<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<head>
<base href="<%=basePath%>">
<meta charset="utf-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1">
<link href="resources/desmartportal/css/layui.css" rel="stylesheet" media="all" />
<link href="resources/desmartportal/css/my.css" rel="stylesheet" media="all" />
<link href="resources/desmartportal/css/modules/laydate/default/laydate.css"
	rel="stylesheet" media="all"/>
<link href="resources/desmartportal/css/modules/layer/default/layer.css"
	rel="stylesheet" media="all"/>
<link href="resources/desmartportal/css/modules/code.css"
	rel="stylesheet" media="all"/>
<link href="resources/desmartportal/tree/css/zTreeStyle/zTreeStyle.css" rel="stylesheet">
<style type="text/css">
.process_name_td {
	overflow: hidden;
	text-overflow: ellipsis;
	white-space: nowrap;
}

.display_container {
	display: none;
	position: absolute;
	top: 0;
	left: 0;
	z-index: 10;
	background: rgba(0,0,0,0.3);
	width: 100%;
	height: 100%;
}

.display_content {
	color: #717171;
	padding: 20px;
	width: 36%;
	height: 350px;
	background: #fff;
	position: absolute;
	margin: 100px 0 0 -300px;
	left: 55%;
	box-shadow: 0 0 10px #ccc;
}

.display_container1 {
	display: none;
	position: absolute;
	top: 0;
	left: 0;
	z-index: 10;
	background: rgba(0,0,0,0.3);
	width: 100%;
	height: 100%;
}

.display_content1 {
	color: #717171;
	padding: 20px;
	width: 36%;
	height: 350px;
	background: #fff;
	position: absolute;
	margin: 100px 0 0 -300px;
	left: 55%;
	box-shadow: 0 0 10px #ccc;
}

.middle .layui-input{
	width:80%;
}

.middle {
	height: 250px;
	width: 96%;
	border: 1px solid #ccc;
	position: relative;
	padding: 0 10px;
}

.agent_pro_top{
	height:40px;
	width:94%;
	margin:auto;
	font-size: 20px;
	margin-top:10px;
	margin-bottom:10px;
}

.agent_pro_middle{
	width:94%;
	height:380px;
	margin:auto;
}

#middle_left_tree{
	float: left;
	width:48%;
	border: 1px solid #ccc;
	height:100%;
	overflow-y: auto;
}

#middle_right_list .layui-table{
	text-align:center;
	margin-top:0px;
}

#middle_right_list{
	overflow-y: auto;
}

#middle_right_list th,#middle_right_list td{
	text-align:center;
	padding:0px;
}

#middle_right_list{
	float: right;
	width:48%;
	border: 1px solid #ccc;
	height:100%;
	background-color:#f2f2f2;
}

#update_middle_left_tree{
	float: left;
	width:48%;
	border: 1px solid #ccc;
	height:100%;
	overflow-y: auto;
}

#update_middle_right_list .layui-table{
	text-align:center;
	margin-top:0px;
}

#update_middle_right_list th,#update_middle_right_list td{
	text-align:center;
	padding:0px;
}

#update_middle_right_list{
	float: right;
	width:48%;
	border: 1px solid #ccc;
	height:100%;
	background-color:#f2f2f2;
	overflow-y: auto;
}

.agent_pro_foot{
	text-align: right;
	height: 50px;
	line-height: 50px;
	width:94%;
	margin:auto;
}
</style>
<title>代理设置页面</title>
</head>
<body>
	<div class="layui-fluid">
		<div class="body_content">
            <div class="search_area">
				<div class="layui-row layui-form">
					<div class="layui-col-md3">
						<label class="layui-form-label">代理人</label>
						<div class="layui-input-block">
							<input id="agentPerson" type="text" placeholder="代理人" class="layui-input" style="width:250px;">
						</div>
					</div>
					<div class="layui-col-md3" style="text-align: center;"> 
						<button class="layui-btn layui-btn-normal layui-btn-sm" onclick="search()">查询</button>
						<button class="layui-btn layui-btn-primary layui-btn-sm" onclick="add()">新增</button>
					</div>
				</div>
			</div>
			<div style="margin-top: 5px;">
				<table class="layui-table backlog_table layui-form" lay-even lay-skin="nob"
					lay-filter="demo" style="table-layout: fixed;">
					<colgroup>
						<col width="9%">
						<col width="25%">
						<col width="18%">
						<col width="18%">
						<col width="10%">
						<col width="10%">
						<col width="10%">
					</colgroup>
					<thead>
						<tr>
							<th><input type="checkbox" name="allSel" onclick="onClickHander(this);" lay-ignore> 序号</th>
							<th class="process_name_td">代理的流程名称</th>
							<th>代理开始时间</th>
							<th>代理结束时间</th>
							<th>代理人</th>
							<th>代理状态</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody id="agent_table_tbody"></tbody>
				</table>
			</div>
			<div id="lay_page"></div>
		</div>
	</div>
	
	<!-- 新增代理信息 -->
	<div class="display_container">
		<div class="display_content">
			<div class="top">
				新增代理
			</div>
			<div class="middle" id="addMiddle">
				<form class="layui-form form-horizontal" style="margin-top:30px;">
				    <div class="layui-form-item">
					    <label class="layui-form-label">开始时间</label>
						<div class="layui-input-block">
							<input type="text" id="addAgentSdate" name="addAgentSdate" value="" autocomplete="off" class="layui-input">
						</div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">结束时间</label>
						<div class="layui-input-block">
							<input type="text" id="addAgentEdate" name="addAgentEdate" value="" autocomplete="off" class="layui-input">
						</div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">代理的流程</label>
						<div class="layui-input-block">
							<input type="text" id="addAgentProcess" name="addAgentProcess" class="layui-input" style="float:left" readonly> 
							<i class="layui-icon" id="choose_agent_process" style="position:relative;left:2%;font-size:30px;">&#xe672;</i>
						</div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">代理人</label>
						<div class="layui-input-block">
							<input type="text" id="addAgentPerson_view" name="addAgentPerson" class="layui-input" style="float:left" readonly>
							<i class="layui-icon" id="choose_agent_user" style="position:relative;left:2%;font-size:30px;">&#xe612;</i>
							<input type="hidden" id="addAgentPerson"/>
						</div>
					</div>
				</form>
			</div>
			<div class="foot">
				<button class="layui-btn layui-btn sure_btn" type="button" onclick="addAgentInfo();" >确定</button>
				<button class="layui-btn layui-btn layui-btn-primary cancel_btn" onclick="cancel()">取消</button>
			</div>
		</div>
	</div>
	
	<div id="choose_agent_process_box" style="display:none;">
		<div class="agent_pro_top layui-row">
			<div class="layui-col-md5">
				<input id="search_processName" type="text" placeholder="流程名称" class="layui-input"/>
			</div>
			<button class="layui-btn select_btn layui-col-md2 layui-col-md-offset1" id="search_pro_btn" onclick="searchProBtn();">查询</button>
		</div>
		<div class="agent_pro_middle">
			<div id="middle_left_tree">
				<ul id="category_tree" class="ztree" style="width:auto;height:97%;"></ul>
			</div>
			<div id="middle_right_list">
				<table class="layui-table">
					<colgroup>
						<col width="10%">
						<col width="90%">
					</colgroup>
					<thead>
						<tr>
							<th><input type="checkbox" name="allProSel" onclick="onClickProAll(this);"/></th>
							<th>流程名称</th>
						</tr>
					</thead>
					<tbody id="agent_pro_list">
					</tbody>
				</table>
			</div>
		</div>
		<div class="agent_pro_foot">
			<button class="layui-btn layui-btn sure_btn" type="button" onclick="addProMeta();">确定</button>
			<button class="layui-btn layui-btn layui-btn-primary cancel_btn">取消</button>
		</div>
	</div>
	
	<!-- 修改代理信息 -->
	<div class="display_container1">
		<div class="display_content1">
			<div class="top">
				修改代理
			</div>
			<div class="middle" id="upateMiddle">
				<form class="layui-form form-horizontal" style="margin-top:30px;">
				    <div class="layui-form-item">
					    <label class="layui-form-label">开始时间</label>
						<div class="layui-input-block">
							<input type="text" id="updateAgentSdate" name="updateAgentSdate" value="" autocomplete="off" class="layui-input">
						</div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">结束时间</label>
						<div class="layui-input-block">
							<input type="text" id="updateAgentEdate" name="updateAgentEdate" value="" autocomplete="off" class="layui-input">
						</div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">代理的流程</label>
						<div class="layui-input-block">
							<input type="text" id="updateAgentProcess" name="updateAgentProcess" class="layui-input" style="float:left" readonly> 
							<i class="layui-icon" id="update_choose_agent_process" style="position:relative;left:2%;font-size:30px;">&#xe672;</i>
						</div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">代理人</label>
						<div class="layui-input-block">
							<input type="text" id="updateAgentPerson_view" name="updateAgentPerson" class="layui-input" style="float:left" readonly>
							<i class="layui-icon" id="update_choose_agent_user" style="position:relative;left:2%;font-size:30px;">&#xe612;</i>
							<input type="hidden" id="updateAgentPerson"/>
						</div>
					</div>
				</form>
			</div>
			<div class="foot">
				<button class="layui-btn layui-btn sure_btn" type="button" onclick="updateAgentInfo();" >确定</button>
				<button class="layui-btn layui-btn layui-btn-primary cancel_btn" onclick="cancel()">取消</button>
			</div>
		</div>
	</div>
	
	<div id="update_choose_agent_process_box" style="display:none;">
		<div class="agent_pro_top layui-row">
			<div class="layui-col-md5">
				<input id="update_search_processName" type="text" placeholder="流程名称" class="layui-input"/>
			</div>
			<button class="layui-btn select_btn layui-col-md2 layui-col-md-offset1" id="update_search_pro_btn" onclick="updateSearchProBtn();">查询</button>
		</div>
		<div class="agent_pro_middle">
			<div id="update_middle_left_tree">
				<ul id="update_category_tree" class="ztree" style="width:auto;height:97%;"></ul>
			</div>
			<div id="update_middle_right_list">
				<table class="layui-table">
					<colgroup>
						<col width="10%">
						<col width="90%">
					</colgroup>
					<thead>
						<tr>
							<th><input type="checkbox" name="updateAllProSel" onclick="updateOnClickProAll(this);"/></th>
							<th>流程名称</th>
						</tr>
					</thead>
					<tbody id="update_agent_pro_list">
					</tbody>
				</table>
			</div>
		</div>
		<div class="agent_pro_foot">
			<button class="layui-btn layui-btn sure_btn" type="button" onclick="updateProMeta();">确定</button>
			<button class="layui-btn layui-btn layui-btn-primary cancel_btn">取消</button>
		</div>
	</div>
</body>
</html>
<script type="text/javascript" src="resources/desmartportal/js/jquery-3.3.1.js" charset="utf-8"></script>
<script type="text/javascript" src="resources/desmartportal/js/layui.all.js" charset="utf-8"></script>
<script type="text/javascript" src="resources/desmartportal/tree/js/jquery.ztree.core.js"></script>
<script type="text/javascript" src="resources/desmartportal/tree/js/jquery.ztree.excheck.js"></script>
<script type="text/javascript" src="resources/desmartportal/tree/js/jquery.ztree.exedit.js"></script>
<script type="text/javascript" src="resources/desmartportal/js/common.js" charset="utf-8"></script>
<script>
	
	// 为翻页提供支持
	var pageConfig = {
		pageNum : 1,
		pageSize : 10,
		person : "",
		total : 0
	}
	
	var categoryUid = "rootCategory";//新增代理时点击流程树获得流程分类Id
	var updateCategoryUid = "rootCategory";//修改代理时点击流程树获得流程分类Id
	var proMetaUidArr = new Array();//新增代理时选择代理流程的ID数组
	var proMetaNameArr = new Array();//新增代理时选择代理流程的name数组
	var updateProMetaUidArr = new Array();//修改代理时选择代理流程的ID数组
	var updateProMetaNameArr = new Array();//修改代理时选择代理流程的name数组
	var allProMetaNum = 0;//新增代理时选择流程的数量
	var updateAllProMetaNum = 0;//修改代理时选择流程的数量
	var updateAgentId = "";//修改代理信息的代理Id
	var updateIsUse = "ENABLED";//修改代理信息时是否启用
	
	layui.use([ 'laypage', 'layer' ], function() {
		var laypage = layui.laypage, layer = layui.layer;
		//完整功能
		laypage.render({
			elem : 'lay_page',
			count : 50,
			limit : 10,
			layout : [ 'count', 'prev', 'page', 'next', 'limit', 'skip' ],
			jump : function(obj) {
				
			}
		});
	});
	
	var setting = {
	    data: {
	        key: {
	            name: "name",
	            icon: "menuIcon"
	        },
		    simpleData: {
		        enable: true,
		        idKey: "id",
		        pIdKey: "pid",
		        rootPId: "rootCategory"
		    }
		},
	    callback: {
	        onClick: zTreeOnClick
	    }
	};
	
	var setting1 = {
		data: {
			key: {
		    	name: "name",
		    	icon: "menuIcon"
		    },
			simpleData: {
				enable: true,
				idKey: "id",
				pIdKey: "pid",
				rootPId: "rootCategory"
			}
		},
		callback: {
			onClick: zTreeUpdateOnClick
		}
	};
	
	$(document).ready(function() {
		// 加载数据
		getAgentInfo();
		
		getMetaInfo("rootCategory");
		
		$("#choose_agent_user").click(function() {
	        common.chooseUser('addAgentPerson', 'true');
	    });
		
		$("#update_choose_agent_user").click(function() {
	        common.chooseUser('updateAgentPerson', 'true');
	    });
		
		// 加载树
        $.ajax({
            url: common.getPath() + "/processCategory/getTreeData",
            type: "post",
            dataType: "json",
            success: function(result) {
                $.fn.zTree.init($("#category_tree"), setting, result);
                $.fn.zTree.init($("#update_category_tree"), setting1, result);
            }
        });
		
		//弹出选择流程信息的弹层
		$("#choose_agent_process").click(function(){
			var index = layer.open({
				type: 1,
	    	    title: '选择流程',
	    	    shadeClose: true,
	    	    shade: 0.3,
	    	    offset:"50px",
	    	    area: ['600px', '540px'],
	    	    content: $("#choose_agent_process_box"),
	    	    cancel: function(index, layero){ 
	    	    	$("#choose_agent_process_box").css("display","none");
	    	   		layer.close(index);
	    	    	return true; 
	    	    },
	    	    success: function (layero, lockIndex) {
	    	    	$("#choose_agent_process_box").find('.cancel_btn').on('click', function () {
	    	    		$("#choose_agent_process_box").css("display","none");
	    	    		layer.close(lockIndex);
	    	        });
	    	    	$("#choose_agent_process_box").find('.sure_btn').on('click', function () {
	    	    		$("#choose_agent_process_box").css("display","none");
	    	    		layer.close(lockIndex);
	    	        });
	    	    },
	    	    end:function(layero, lockIndex){
	    	    	$("#choose_agent_process_box").css("display","none");
	    	    	layer.close(lockIndex);
	    	    }
			});
			layer.style(index, {
		    	zoom:1.1
		    });
		});//end click
		
		//弹出选择流程信息的弹层
		$("#update_choose_agent_process").click(function(){
			var index = layer.open({
				type: 1,
	    	    title: '选择流程',
	    	    shadeClose: true,
	    	    shade: 0.3,
	    	    area: ['600px', '540px'],
	    	    content: $("#update_choose_agent_process_box"),
	    	    cancel: function(index, layero){ 
	    	    	$("#update_choose_agent_process_box").css("display","none");
	    	   		layer.close(index);
	    	    	return true; 
	    	    },
	    	    success: function (layero, lockIndex) {
	    	    	$("#update_choose_agent_process_box").find('.cancel_btn').on('click', function () {
	    	    		$("#update_choose_agent_process_box").css("display","none");
	    	    		layer.close(lockIndex);
	    	        });
	    	    	$("#update_choose_agent_process_box").find('.sure_btn').on('click', function () {
	    	    		$("#update_choose_agent_process_box").css("display","none");
	    	    		layer.close(lockIndex);
	    	        });
	    	    },
	    	    end:function(layero, lockIndex){
	    	    	$("#update_choose_agent_process_box").css("display","none");
	    	    	layer.close(lockIndex);
	    	    }
			});
			layer.style(index, {
		    	zoom:1.1
		    });
		});//end click
	});
	
	//流程分类树节点的双击事件
	function zTreeOnClick(event, treeId, treeNode) {
    	categoryUid = treeNode.id;
    	getMetaInfo(categoryUid);
    }
	
	function zTreeUpdateOnClick(event, treeId, treeNode){
		updateCategoryUid = treeNode.id;
    	getUpdateMetaInfo(updateCategoryUid);
	}
	
	//模糊查询流程元集合数据
	function searchProBtn(){
		getMetaInfo(categoryUid);
	}
	
	//模糊查询修改流程元集合数据
	function updateSearchProBtn(){
		getUpdateMetaInfo(updateCategoryUid);
	}
	
	//add根据分类Id获得流程元集合数据
	function getMetaInfo(categoryUid){
		$.ajax({
			url:common.getPath() +"/agent/listByCategoryUid",
			method:"post",
			beforeSend:function(){
				layer.load(1);
			},
			data:{
				categoryUid:categoryUid,
				proName:$("#search_processName").val()
			},
			success:function(result){
				if(result.status==0){
					if(categoryUid == "rootCategory"){
						allProMetaNum = result.data.length;
					}
					drawProTable(result.data);
					$("input[name='allProSel']").prop("checked",false);
					$("input[name='proNameCheck']").prop("checked",false);
					var proArr = $("input[name='proNameCheck']");
					for(var i=0;i<proArr.length;i++){
						var proMetaUid = $(proArr[i]).val();
						if($.inArray(proMetaUid,proMetaUidArr)!=-1){
							$(proArr[i]).prop("checked",true);
							onClickProSel(proArr[i]);
						}
					}
				}
				layer.closeAll('loading');
			},//end success
			error:function(){
				layer.closeAll('loading');
			}
		});
	}
	
	//update根据分类Id获得流程元集合数据
	function getUpdateMetaInfo(categoryUid){
		$.ajax({
			url:common.getPath() +"/agent/listByCategoryUid",
			method:"post",
			beforeSend:function(){
				layer.load(1);
			},
			data:{
				categoryUid:categoryUid,
				proName:$("#update_search_processName").val()
			},
			success:function(result){
				if(result.status==0){
					if(categoryUid == "rootCategory"){
						updateAllProMetaNum = result.data.length;
					}
					drawUpdateProTable(result.data);
					$("input[name='updateAllProSel']").prop("checked",false);
					$("input[name='updateProNameCheck']").prop("checked",false);
					var proArr = $("input[name='updateProNameCheck']");
					for(var i=0;i<proArr.length;i++){
						var updateProMetaUid = $(proArr[i]).val();
						if(updateProMetaUidArr[0]=="allProMeta"){
							$(proArr[i]).prop("checked",true);
							updateOnClickProSel(proArr[i]);
							if(i==proArr.length-1){
								updateProMetaUidArr.splice($.inArray("allProMeta",updateProMetaUidArr),1);
								updateProMetaNameArr.splice($.inArray("所有流程",updateProMetaNameArr),1);
							}
						}else if($.inArray(updateProMetaUid,updateProMetaUidArr)!=-1){
							$(proArr[i]).prop("checked",true);
							updateOnClickProSel(proArr[i]);
						}
					} 
				}
				layer.closeAll('loading');
			},//end success
			error:function(){
				layer.closeAll('loading');
			}
		});
	}
	
	//选择要添加代理的流程信息
	function addProMeta(){
		var addProMetaStr = proMetaNameArr.join(";");
		if(allProMetaNum!=proMetaUidArr.length){
			$("#addAgentProcess").val(addProMetaStr);
		}else{
			$("#addAgentProcess").val("所有流程");
		}
	} 
	
	//添加代理信息
	function addAgentInfo(){
		var agentSdate = $("#addAgentSdate").val();
		var agentEdate = $("#addAgentEdate").val();
		var agentPerson = $("#addAgentPerson").val().replace(";","");
		//判断值不为空
		if(agentSdate!=null && agentSdate!="" && agentEdate!=null && agentEdate!=""
			&& agentPerson!=null && agentPerson!="" && proMetaUidArr.length!=0){
			//判断结束时间 晚于 开始时间
			var start = new Date(agentSdate.replace("-", "/").replace("-", "/"));  
			var end = new Date(agentEdate.replace("-", "/").replace("-", "/"));  
			if(start<end){
				var agentIsAll = "TRUE";
				if(allProMetaNum!=proMetaUidArr.length){
					agentIsAll = "FALSE";
				}
				//添加代理信息
				$.ajax({
					url:common.getPath() +"/agent/addAgentInfo",
					method:"post",
					beforeSend:function(){
						layer.load(1);
					},
					traditional: true,//传递数组给后台
					data:{
						agentProMetaUidArr:proMetaUidArr,
						agentSdate:agentSdate,
						agentEdate:agentEdate,
						agentPerson:agentPerson,
						agentIsAll:agentIsAll
					},
					success:function(result){
						if(result.status==0){
							getAgentInfo();
							$(".display_container").css("display", "none");
							layer.alert("添加代理设置成功");
						}else{
							layer.alert(result.msg);
						}
						layer.closeAll('loading');
					},
					error:function(){
						layer.closeAll('loading');
					}
				});
			}else{
				layer.alert("结束时间不能早于开始时间");
			}//end 判断时间
		}else{
			layer.alert("请填写所有参数");
		}//end if
	}
	
	//修改代理流程元数据的方法
	function updateProMeta(){
		var updateProMetaStr = updateProMetaNameArr.join(";");
		if(updateAllProMetaNum != updateProMetaUidArr.length){
			$("#updateAgentProcess").val(updateProMetaStr);
		}else{
			$("#updateAgentProcess").val("所有流程");
		}
	}
	
	//修改代理信息的方法
	function updateAgentInfo(){
		var updateAgentSdate = $("#updateAgentSdate").val();
		var updateAgentEdate = $("#updateAgentEdate").val();
		var updateAgentPerson = $("#updateAgentPerson").val().replace(";","");
		//判断值不为空
		if(updateAgentSdate!=null && updateAgentSdate!="" && updateAgentEdate!=null && updateAgentEdate!=""
			&& updateAgentPerson!=null && updateAgentPerson!="" && updateProMetaUidArr.length!=0){
			//判断结束时间 晚于 开始时间
			var start = new Date(updateAgentSdate.replace("-", "/").replace("-", "/"));  
			var end = new Date(updateAgentEdate.replace("-", "/").replace("-", "/"));  
			if(start<end){
				var updateAgentIsAll = "TRUE";
				if(updateAllProMetaNum != updateProMetaUidArr.length){
					updateAgentIsAll = "FALSE";
				}
				//修改代理信息
				$.ajax({
					url:common.getPath() +"/agent/updateAgentInfo",
					method:"post",
					beforeSend:function(){
						layer.load(1);
					},
					traditional: true,//传递数组给后台
					data:{
						agentId:updateAgentId,
						agentProMetaUidArr:updateProMetaUidArr,
						agentSdate:updateAgentSdate,
						agentEdate:updateAgentEdate,
						agentClientele:updateAgentPerson,
						agentStatus:updateIsUse,
						agentIsAll:updateAgentIsAll
					},
					success:function(result){
						if(result.status==0){
							getAgentInfo();
							$(".display_container1").css("display", "none");
							layer.alert("修改代理设置成功");
						}else{
							layer.alert(result.msg);
						}
						layer.closeAll('loading');
					},
					error:function(){
						layer.closeAll('loading');
					}
				});
			}else{
				layer.alert("结束时间不能早于开始时间");
			}//end 判断时间
		}else{
			layer.alert("请填写所有参数");
		}//end if
	}
	
	//查询代理信息
	 function getAgentInfo() {
		$.ajax({
			url : common.getPath() +'/agent/queryAgentByList',
			type : 'POST',
			dataType : 'json',
			data : {
				pageNum : pageConfig.pageNum,
				pageSize : pageConfig.pageSize,
				person : pageConfig.person
			},
			success : function(result) {
				drawTable(result.data)
			}
		})
	}
	
	//渲染代理流程信息表格
	function drawProTable(data){
		$("#agent_pro_list").html("");
		var trs = "";
		for(var i=0;i<data.length;i++){
			var meta = data[i];
			trs += '<tr>'+
					'<td><input type="checkbox" value="'+meta.proMetaUid+'" name="proNameCheck" onclick="onClickProSel(this);"/></td>'+
					'<td>'+meta.proName+'</td>'+
					'</tr>';
		}
		$("#agent_pro_list").append(trs);
	}
	
	//渲染修改代理流程信息表格
	function drawUpdateProTable(data){
		$("#update_agent_pro_list").html("");
		var trs = "";
		for(var i=0;i<data.length;i++){
			var meta = data[i];
			trs += '<tr>'+
					'<td><input type="checkbox" value="'+meta.proMetaUid+'" name="updateProNameCheck" onclick="updateOnClickProSel(this);"/></td>'+
					'<td>'+meta.proName+'</td>'+
					'</tr>';
		}
		$("#update_agent_pro_list").append(trs);
	}
	
	//选择代理流程的复选框全选，取消全选
	function onClickProAll(obj){
		if(obj.checked){
			$("input[name='proNameCheck']").prop("checked",true);
			$("input[name='proNameCheck']").each(function(){
				if($.inArray($(this).val(),proMetaUidArr)==-1){
					proMetaUidArr.push($(this).val());
					proMetaNameArr.push($(this).parent().next().text());
				}
			});
		}else{
			$("input[name='proNameCheck']").prop("checked",false);
			$("input[name='proNameCheck']").each(function(){
				proMetaUidArr.splice($.inArray($(this).val(),proMetaUidArr),1);
				proMetaNameArr.splice($.inArray($(this).parent().next().text(),proMetaNameArr),1);
			});
		}
	}
	
	//选择代理流程的复选框分选
	function onClickProSel(obj){
		if(obj.checked){
			if($.inArray($(obj).val(),proMetaUidArr)==-1){
				proMetaUidArr.push($(obj).val());
				proMetaNameArr.push($(obj).parent().next().text());
			}
			var allSel = false;
			$("input[name='proNameCheck']").each(function(){
				if(!$(this).is(":checked")){
					allSel = true;
				}
			});
			
			//如果有checkbox没有被选中
			if(allSel){
				$("input[name='allProSel']").prop("checked",false);
			}else{
				$("input[name='allProSel']").prop("checked",true);
			}
		}else{
			proMetaUidArr.splice($.inArray($(obj).val(),proMetaUidArr),1);
			proMetaNameArr.splice($.inArray($(obj).parent().next().text(),proMetaNameArr),1);
			$("input[name='allProSel']").prop("checked",false);
		}
	}
	
	//选择修改代理流程的复选框全选，取消全选
	function updateOnClickProAll(obj){
		if(obj.checked){
			$("input[name='updateProNameCheck']").prop("checked",true);
			$("input[name='updateProNameCheck']").each(function(){
				if($.inArray($(this).val(),updateProMetaUidArr)==-1){
					updateProMetaUidArr.push($(this).val());
					updateProMetaNameArr.push($(this).parent().next().text());
				}
			});
		}else{
			$("input[name='updateProNameCheck']").prop("checked",false);
			$("input[name='updateProNameCheck']").each(function(){
				updateProMetaUidArr.splice($.inArray($(this).val(),updateProMetaUidArr),1);
				updateProMetaNameArr.splice($.inArray($(this).parent().next().text(),updateProMetaNameArr),1);
			});
		}
	}
	
	//选择代理流程的复选框分选
	function updateOnClickProSel(obj){
		if(obj.checked){
			if($.inArray($(obj).val(),updateProMetaUidArr)==-1){
				updateProMetaUidArr.push($(obj).val());
				updateProMetaNameArr.push($(obj).parent().next().text());
			}
			var allSel = false;
			$("input[name='updateProNameCheck']").each(function(){
				if(!$(this).is(":checked")){
					allSel = true;
				}
			});
			
			//如果有checkbox没有被选中
			if(allSel){
				$("input[name='updateAllProSel']").prop("checked",false);
			}else{
				$("input[name='updateAllProSel']").prop("checked",true);
			}
		}else{
			updateProMetaUidArr.splice($.inArray($(obj).val(),updateProMetaUidArr),1);
			updateProMetaNameArr.splice($.inArray($(obj).parent().next().text(),updateProMetaNameArr),1);
			$("input[name='updateAllProSel']").prop("checked",false);
		}
	}
	
	//代理信息复选框全选，取消全选
	function onClickHander(obj){
		if(obj.checked){
			$("input[name='agent_check']").prop("checked",true);
		}else{
			$("input[name='agent_check']").prop("checked",false);
		}
	}
	
	//代理信息复选框分选
	function onClickSel(obj){
		if(obj.checked){
			var allSel = false;
			$("input[name='agent_check']").each(function(){
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
	
	//渲染代理信息表格
	function drawTable(pageInfo, data){
		pageConfig.pageNum = pageInfo.pageNum;
		pageConfig.pageSize = pageInfo.pageSize;
		pageConfig.total = pageInfo.total;
		doPage();
		// 渲染数据
		$("#agent_table_tbody").html('');
		if (pageInfo.total == 0) {
			return;
		}

		var list = pageInfo.list;
		var startSort = pageInfo.startRow;//开始序号
		var trs = "";
		for (var i = 0; i < list.length; i++) {
			var meta = list[i];
			var agentSdate = common.dateToString(new Date(meta.agentSdate));
			var agentEdate = common.dateToString(new Date(meta.agentEdate));
			var sortNum = startSort + i;
			var tdTitle = "";
			var updateAgentProcessIds = "";
			for(var j=0;j<meta.dhProcessMetaList.length;j++){
				var processMetaName = (meta.dhProcessMetaList)[j].proName;
				var processMetaUid = (meta.dhProcessMetaList)[j].proMetaUid;
				tdTitle += processMetaName;
				updateAgentProcessIds += processMetaUid;
				if(j!=meta.dhProcessMetaList.length-1){
					tdTitle += ";";
					updateAgentProcessIds += ";";
				}
			}
			
			var agentStatus = true;
			if(meta.agentStatus == "DISABLED"){
				agentStatus = false;
			}
			trs += '<tr data-agentid="'+meta.agentId+'">'
				+ '<td><input type="checkbox" value="'+meta.agentId+'" name="agent_check" onclick="onClickSel(this);" lay-ignore/> ' + sortNum + '</td>'
				+ '<td class="process_name_td" title="'+tdTitle+'">' + tdTitle + '</td>'
				+ '<td>' + agentSdate + '</td>'
				+ '<td>' + agentEdate + '</td>'
				+ '<td id="'+meta.agentClientele+'">' + meta.agentClienteleName +'</td>';
			if(agentStatus){
				trs += 	'<td><input name="agentStatus" checked type="checkbox" lay-skin="switch" lay-text="on|off" lay-filter="isUse"></td>';
			}else{
				trs += 	'<td><input name="agentStatus" type="checkbox" lay-skin="switch" lay-text="on|off" lay-filter="isUse"></td>';
			}
			trs	+= '<td><i class="layui-icon" title="修改代理设置" onclick=update(this)>&#xe642;</i>'
				+ '<i class="layui-icon" title="删除代理设置" onclick=del("'+ meta.agentId + '")>&#xe640;</i>'
				+ '<input type="hidden" id="updateAgentProcessIds" value="'+updateAgentProcessIds+'"/></td>'
				+ '</tr>';
		}
		$("#agent_table_tbody").append(trs);
		layui.use('form', function(){
			var form = layui.form; 
			form.render('checkbox');
			form.on('switch(isUse)', function(data){
				var agentId = $(this).parent().parent().data("agentid");
				var agentProArr = $(this).parent().parent().find("#updateAgentProcessIds").val().split(";");
				var agentStratDate = $(this).parent().parent().find("td:eq(2)").text();
				var agentEndDate = $(this).parent().parent().find("td:eq(3)").text();
				var agentStatus = "";
				if(this.checked){
					agentStatus = "ENABLED";
			    }else{
			    	agentStatus = "DISABLED";
			    }
				$.ajax({
					url:common.getPath() +"/agent/updateAgentStatus",
					method:"post",
					traditional: true,
					data:{
						agentId:agentId,
						agentProMetaUidArr:agentProArr,
						agentSdate:agentStratDate,
						agentEdate:agentEndDate,
						agentStatus:agentStatus
					},
					success:function(result){
						if(result.status != 0){
							layer.alert(result.msg);	
						}else{
							if(agentStatus=="ENABLED"){
								layer.alert("代理启用成功");
							}else{
								layer.alert("代理停用成功");
							}
						}
						getAgentInfo();
					}
				});
			});//end function
		});
	}
	
	// 分页
	function doPage() {
		layui.use([ 'laypage', 'layer' ], function() {
			var laypage = layui.laypage, layer = layui.layer;
			//完整功能
			laypage.render({
				elem : 'lay_page',
				curr : pageConfig.pageNum,
				count : pageConfig.total,
				limit : pageConfig.pageSize,
				layout : [ 'count', 'prev', 'page', 'next', 'limit', 'skip' ],
				jump : function(obj, first) {
					// obj包含了当前分页的所有参数  
					pageConfig.pageNum = obj.curr;
					pageConfig.pageSize = obj.limit;
					if (!first) {
						getAgentInfo();
					}
				}
			});
		});
	}
	
	//模糊查询代理信息
	function search(){
		pageConfig.person = $("#agentPerson").val();
		getAgentInfo();
	}
	
	//显示新增代理模态框
	function add(){
		$(".display_container .layui-input").val("");
		proMetaUidArr = new Array();
		proMetaNameArr = new Array();
		$("input[name='allProSel']").prop("checked",false);
		$("input[name='proNameCheck']").prop("checked",false);
		$(".display_container").css("display", "block");	
	}
	
	//显示修改代理模态框
	function update(obj){
		$(".display_container1 .layui-input").val("");
		$("input[name='updateAllProSel']").prop("checked",false);
		$("input[name='updateProNameCheck']").prop("checked",false);
		
		var updateAgentSdate = $(obj).parent().parent().find("td:eq(2)").text();
		var updateAgentEdate = $(obj).parent().parent().find("td:eq(3)").text();
		var updateAgentPersonName = $(obj).parent().parent().find("td:eq(4)").text();
		var updateAgentPersonId = $(obj).parent().parent().find("td:eq(4)").prop("id");
		var updateAgentProcess = $(obj).parent().parent().find("td:eq(1)").text();
		updateProMetaUidArr = $(obj).parent().find("#updateAgentProcessIds").val().split(";");
		updateProMetaNameArr = updateAgentProcess.split(";");
		getUpdateMetaInfo("rootCategory");
		updateAgentId = $(obj).parent().parent().data("agentid");
		var isUse = $(obj).parent().parent().find("input[name='agentStatus']").prop("checked");
		if(isUse){
			updateIsUse = "ENABLED";
		}else{
			updateIsUse = "DISABLED";
		}
		
		$("#updateAgentSdate").val(updateAgentSdate);
		$("#updateAgentEdate").val(updateAgentEdate);
		$("#updateAgentPerson_view").val(updateAgentPersonName);
		$("#updateAgentPerson").val(updateAgentPersonId);
		$("#updateAgentProcess").val(updateAgentProcess);
		$(".display_container1").css("display", "block");	
	}
	
	//隐藏新增代理模态框
	function cancel(){
		$(".display_container").css("display", "none");	
		$(".display_container1").css("display", "none");
	}
	
	function del(id){
		layer.confirm('是否删除该代理？', {
			btn : [ '确定', '取消' ], //按钮
			shade : false
		//不显示遮罩
		}, function(index) {
			// 提交表单的代码，然后用 layer.close 关闭就可以了，取消可以省略 ajax请求
			$.ajax({
				url : common.getPath() +'/agent/deleteAgentById',
				type : 'POST',
				data : {
					agentId : id
				},
				success : function(result) {
					if(result.status == 0){
						layer.alert("删除成功");
						getAgentInfo();
					}else{
						layer.alert("删除失败");
					}
				}
			})
			layer.close(index);
		});
	}
	
	//日期控件渲染
	layui.use([ 'laydate'], function(){  
        var $ = layui.$;  
        var laydate = layui.laydate;   
        var nowTime = new Date().valueOf();
        var max = null;

        var start = laydate.render({
            elem: '#addAgentSdate',
            type: 'datetime',
            min: nowTime
        });
        
        var end = laydate.render({
            elem: '#addAgentEdate',
            type: 'datetime',
            min: nowTime
     	});
        
        var updateStart = laydate.render({
            elem: '#updateAgentSdate',
            type: 'datetime',
            min: nowTime
        });
        
        var updateEnd = laydate.render({
            elem: '#updateAgentEdate',
            type: 'datetime',
            min: nowTime
     	});
	 });
</script>

