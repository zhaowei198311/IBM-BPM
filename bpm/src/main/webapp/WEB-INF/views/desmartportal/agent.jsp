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
	background: rgba(255, 255, 255, 0.8);
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
}

#middle_right_list .layui-table{
	text-align:center;
	margin-top:0px;
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
	<div class="layui-container" style="margin-top: 20px; width: 100%;">
		<div class="search_area">
			<div class="layui-row layui-form">
				<div class="layui-col-md3">
					<input id="agentPerson" type="text" placeholder="代理委托人" class="layui-input" style="width:250px;">
				</div>
				<div class="layui-col-md3" style="text-align: left; width: 200px">
					<button class="layui-btn select_btn" onclick="search()">查询</button>
					<button class="layui-btn select_btn" onclick="add()">新增</button>
				</div>
			</div>
		</div>
		<div>
			<table class="layui-table backlog_table" lay-even lay-skin="nob"
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
						<th><input type="checkbox"> 序号</th>
						<th class="process_name_td">代理的流程名称</th>
						<th>代理开始时间</th>
						<th>代理结束时间</th>
						<th>代理委托人</th>
						<th>代理状态</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody id="agent_table_tbody"></tbody>
			</table>
		</div>
		<div id="lay_page"></div>
	</div>
	
	<div class="display_container">
		<div class="display_content">
			<div class="top">
				新增代理
			</div>
			<div class="middle" id="addMiddle">
				<form class="layui-form form-horizontal" method="post" action="agent/saveAgent" style="margin-top:30px;">
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
							<input type="text" id="addAgentProcess" name="addAgentProcess" value="" autocomplete="off" class="layui-input" style="float:left"> 
							<i class="layui-icon" id="choose_agent_process" style="position:relative;left:2%;font-size:30px;">&#xe672;</i>
						</div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">代理委托人</label>
						<div class="layui-input-block">
							<input type="text" id="addAgentPerson_view" name="addAgentPerson" class="layui-input" style="float:left" readonly>
							<i class="layui-icon" id="choose_agent_user" style="position:relative;left:2%;font-size:30px;">&#xe612;</i>
							<input type="hidden" id="addAgentPerson"/>
						</div>
					</div>
				</form>
			</div>
			<div class="foot">
				<button class="layui-btn layui-btn sure_btn" type="button" onclick="formSubmit();" >确定</button>
				<button class="layui-btn layui-btn layui-btn-primary cancel_btn" onclick="cancel()">取消</button>
			</div>
		</div>
	</div>
	
	<div id="choose_agent_process_box" style="display:none;">
		<div class="agent_pro_top layui-row">
			<div class="layui-col-md5">
				<input id="search_processName" type="text" placeholder="流程名称" class="layui-input"/>
			</div>
			<button class="layui-btn select_btn layui-col-md2 layui-col-md-offset1" id="search_pro_btn">查询</button>
		</div>
		<div class="agent_pro_middle">
			<div id="middle_left_tree">
				<ul id="category_tree" class="ztree" style="width:auto;height:100%;"></ul>
			</div>
			<div id="middle_right_list">
				<table class="layui-table">
					<colgroup>
						<col width="10%">
						<col width="90%">
					</colgroup>
					<thead>
						<tr>
							<th><input type="checkbox"/></th>
							<th>流程名称</th>
						</tr>
					</thead>
					<tbody id="agent_pro_list">
					</tbody>
				</table>
			</div>
		</div>
		<div class="agent_pro_foot">
			<button class="layui-btn layui-btn sure_btn" type="button">确定</button>
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

	layui.use([ 'laypage', 'layer' ], function() {
		var laypage = layui.laypage, layer = layui.layer;
		//完整功能
		laypage.render({
			elem : 'lay_page',
			count : 50,
			limit : 10,
			layout : [ 'count', 'prev', 'page', 'next', 'limit', 'skip' ],
			jump : function(obj) {
				console.log(obj)
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
	
	$(document).ready(function() {
		// 加载数据
		getAgentInfo();
		
		$("#choose_agent_user").click(function() {
	        common.chooseUser('addAgentPerson', 'true');
	    });
		
		// 加载树
        $.ajax({
            url: common.getPath() + "/processCategory/getTreeData",
            type: "post",
            dataType: "json",
            success: function(result) {
                $.fn.zTree.init($("#category_tree"), setting, result);
            }
        });
		
		$("#choose_agent_process").click(function(){
			layer.open({
				type: 1,
	    	    title: '选择流程',
	    	    shadeClose: true,
	    	    shade: 0.8,
	    	    area: ['600px', '540px'],
	    	    content: $("#choose_agent_process_box"),
	    	    success: function (layero, lockIndex) {
	    	        var body = layer.getChildFrame('body', lockIndex);
	    	        body.find('button#close').on('click', function () {
	    	            layer.close(lockIndex);
	    	        });
	    	    }
			});
		});
	});
	
	function zTreeOnClick(event, treeId, treeNode) {
    	var categoryUid = treeNode.id;
    	var categoryName = treeNode.name;
    	var categoryUid = treeNode.id;
    	//$("#proName_input").val('');
    	//getMetaInfo();
    }
	
	 function getAgentInfo() {
		$.ajax({
			url : 'agent/queryAgentByList',
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
			for(var j=0;j<meta.dhProcessMetaList.length;j++){
				var processMetaName = (meta.dhProcessMetaList)[j].proName;
				tdTitle += processMetaName;
				if(j!=meta.dhProcessMetaList.length-1){
					tdTitle += ",";
				}
			}
			
			var agentStatus = "启用";
			if(meta.agentStatus == "DISABLED"){
				agentStatus = "停用";
			}
			trs += '<tr>'
				+ '<td><input type="checkbox" value="'+meta.agentId+'"/> ' + sortNum + '</td>'
				+ '<td class="process_name_td" title="'+tdTitle+'">' + tdTitle + '</td>'
				+ '<td>' + agentSdate + '</td>'
				+ '<td>' + agentEdate + '</td>'
				+ '<td>' + meta.agentClienteleName +'</td>'
				+ '<td>' + agentStatus +'</td>'
				+ '<td><i class="layui-icon" title="修改代理设置" onclick=del("'+ meta.agentId + '") >&#xe640;</i></td>'
				+ '</tr>';
		}
		$("#agent_table_tbody").append(trs);
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
	
	function search(){
		pageConfig.person = $("#agentPerson").val();
		getAgentInfo();
	}
	function add(){
		$(".display_container .layui-input").val("");
		$(".display_container").css("display", "block");	
	}
	function cancel(){
		$(".display_container").css("display", "none");	
	}
	function del(id){
		layer.confirm('是否删除该代理？', {
			btn : [ '确定', '取消' ], //按钮
			shade : false
		//不显示遮罩
		}, function(index) {
			// 提交表单的代码，然后用 layer.close 关闭就可以了，取消可以省略 ajax请求
			$.ajax({
				url : 'agent/deleteAgentById',
				type : 'POST',
				dataType : 'text',
				data : {
					agentId : id
				},
				success : function(result) {
					// 删除成功后 ajxa跳转 查询controller
					window.location.href = "agent/index";
				}
			})
			layer.close(index);
		});
	}
	
	layui.use([ 'laydate'], function(){  
        var $ = layui.$;  
        var laydate = layui.laydate;   
        var nowTime = new Date().valueOf();
        var max = null;

        var start = laydate.render({
            elem: '#addAgentSdate',
            type: 'datetime',
            min: nowTime,
            done: function(value, date){
                endMax = end.config.max;
                end.config.min = date;
                end.config.min.month = date.month -1;
            }
        });
        
        var end = laydate.render({
            elem: '#addAgentEdate',
            type: 'datetime',
            min: nowTime,
            btns: ['clear', 'confirm'],
            done: function(value, date){
                if($.trim(value) == ''){
                    var curDate = new Date();
                    date = {'date': curDate.getDate(), 'month': curDate.getMonth()+1, 'year': curDate.getFullYear()};
                }
                start.config.max = date;
                start.config.max.month = date.month -1;
            }
     	});
	 });
	
     function formSubmit(){
		var agentOdate = new Date($('#agentOdate').val());
		var agentOperator = $('#agentOperator').val();
		var agentClientele = $('#agentClientele').val();
		var agentStatus = $('#agentStatus').val();
		$.ajax({
			url : 'agent/saveAgent',
			type : 'POST',
			dataType : 'text',
			data : {
				agentOdate : agentOdate,
				agentOperator : agentOperator,
				agentClientele : agentClientele,
				agentStatus : agentStatus
			},
			success : function(result) {
				// 删除成功后 ajxa跳转 查询controller
				window.location.href = "agent/index";
			}
		})
		
	}
</script>

