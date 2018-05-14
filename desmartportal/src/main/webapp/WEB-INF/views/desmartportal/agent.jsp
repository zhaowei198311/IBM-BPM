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
<link href="resources/css/layui.css" rel="stylesheet" media="all" />
<link href="resources/css/my.css" rel="stylesheet" media="all" />
<link href="resources/css/modules/laydate/default/laydate.css"
	rel="stylesheet" media="all"/>
<link href="resources/css/modules/layer/default/layer.css"
	rel="stylesheet" media="all"/>
<link href="resources/css/modules/code.css"
	rel="stylesheet" media="all"/>
<title>代理设置页面</title>
</head>
<body>
	<div class="layui-container" style="margin-top: 20px; width: 100%;">
		<div class="search_area">
			<div class="layui-row layui-form">
				<div class="layui-col-md3">
					<input id="agentPerson" type="text" placeholder="请输入代理设置人/代理委托人" class="layui-input" style="width:250px;">
				</div>
				<div class="layui-col-md3" style="text-align: left; width: 200px">
					<button class="layui-btn select_btn" onclick="search()">查询</button>
					<button class="layui-btn select_btn" onclick="add()">新增</button>
				</div>
			</div>
		</div>
		<div>
			<table class="layui-table backlog_table" lay-even lay-skin="nob"
				lay-filter="demo">
				<colgroup>
					<col>
					<col>
					<col>
					<col>
					<col>
					<col>
				</colgroup>
				<thead>
					<tr>
						<th>序号</th>
						<th>代理设置时间</th>
						<th>代理设置人</th>
						<th>代理委托人</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody id="agent_table_tbody" />
			</table>
		</div>
		<div id="lay_page"></div>
	</div>
	
	<div class="display_container">
		<div class="display_content" style="width: 350px;height: 400px;">
			<div class="top">
				新增代理
			</div>
			<label style="color: red;">带*为必填参数</label>
			<div class="middle" style="height:250px;">
				<form class="layui-form form-horizontal" method="post" action="agent/saveAgent" style="margin-top:30px;">
				  <div class="layui-form-item">
					<div class="layui-inline">
						<label class="layui-form-label">代理时间*:</label>
						<div class="layui-input-inline">
							<input type="text" id="agentOdate" name="agentOdate"
								lay-verify="agentOdate" autocomplete="off" class="layui-input" >
						</div>
					</div>
					<div class="layui-inline">
						<label class="layui-form-label">代理设置人*:</label>
						<div class="layui-input-inline">
							<input type="text" id="agentOperator" name="agentOperator"
								lay-verify="agentOperator" autocomplete="off"
								class="layui-input">
						</div>
					</div>
				</div>
				<div class="layui-form-item">
					<div class="layui-inline">
						<label class="layui-form-label">代理委托人*:</label>
						<div class="layui-input-inline">
							<input type="text" id="agentClientele" name="agentClientele"
								lay-verify="agentClientele" autocomplete="off"
								class="layui-input">
						</div>
					</div>
					<div class="layui-inline">
						<label class="layui-form-label">代理状态*:</label>
						<div class="layui-input-inline">
							<select id="agentStatus" name="agentStatus">
								<option value="">请选择</option>
								<option value="ENABLED">启用</option>
								<option value="DISABLED">停用</option>
							</select>
						</div>
					</div>
				  </div>
				   <input type="hidden" id="submit_add" />		  
				</form>
			</div>
			<div class="foot">
				<button class="layui-btn layui-btn sure_btn" type="button" onclick="formSubmit();" >确定</button>
				<button class="layui-btn layui-btn layui-btn-primary cancel_btn" onclick="cancel()">取消</button>
			</div>
		</div>
	</div>
</body>
</html>
<script type="text/javascript" src="resources/js/jquery-3.3.1.js" charset="utf-8"></script>
<script type="text/javascript" src="resources/js/layui.all.js" charset="utf-8"></script>
<script>
	
	// 为翻页提供支持
	var pageConfig = {
		pageNum : 1,
		pageSize : 10,
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
	
	
	$(document).ready(function() {
		// 加载数据
		  getAgentInfo();
	});

	 function getAgentInfo() {
		$.ajax({
			url : 'agent/queryAgentByList',
			type : 'POST',
			dataType : 'json',
			data : {
				pageNum : pageConfig.pageNum,
				pageSize : pageConfig.pageSize,
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
			var agentOdate = new Date(meta.agentOdate);
			var showDate = agentOdate.getFullYear()+"-"+(agentOdate.getMonth()+1)+"-"+agentOdate.getDate();
			var sortNum = startSort + i;
			trs += '<tr><td>'
					+ sortNum
					+ '</td>'
					+ '<td>'
					+ showDate
					+ '</td>'
					+ '<td>'
					+ meta.agentOperator
					+ '</td>'
					+ '<td>'
					+ meta.agentClientele
					+ '</td>'
					+ '<td>'
					+ '<i class="layui-icon"  title="删除草稿"  onclick=del("'
					+ meta.agentId + '") >&#xe640;</i>'
					+ '</td>'
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
		var agentPerson = $("#agentPerson").val();
		$.ajax({
			url : 'agent/queryAgentByPerson',
			type : 'POST',
			dataType : 'json',
			data : {
				person : agentPerson
			},
			success : function(result){
				drawTable(result.data)
			}
		})
	}
	function add(){
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
        var max = null;  
      
        var start = laydate.render({  
            elem: '#agentOdate',  
            type: 'datetime',  
            format:'yyyy/MM/dd',   
            btns: ['clear', 'confirm']
        });  
     }) 
     
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

