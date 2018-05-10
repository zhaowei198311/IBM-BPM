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
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
  		<title>待办任务</title>
  		<link href="css/layui.css" rel="stylesheet"/>
  		<link rel="stylesheet" href="css/modules/laydate/default/laydate.css" />
  		<link rel="stylesheet" href="tree/css/demo.css" type="text/css">
		<link rel="stylesheet" href="tree/css/zTreeStyle/zTreeStyle.css" type="text/css">
  		<link href="css/my.css" rel="stylesheet" />
  		<style>
			ul.ztree{
				border:0;
			}
  		</style>
  		
	</head>
	<body>
		<div class="container">
			<div class="layui-row">
				<!--<div class="layui-col-md2" style="text-align: left;">
					<div class="tree" id="demo" style="margin-left:0;">
						<ul id="treeDemo" class="ztree" style="width:auto;height:500px;"></ul>
					</div>
			    </div>-->
				<div class="layui-col-md12">
					<div class="search_area">
						<div class="layui-row layui-form">
							<!--<div class="layui-col-md3">
							    <div class="layui-form-pane">
							    	<div class="layui-form-item">
							          	<label class="layui-form-label" style="cursor:pointer;">刷新</label>
								        <div class="layui-input-block">
								            <select class="layui-input-block group_select" name="group" lay-verify="required">
											  	<option value="">分组</option>
											  	<option value="01">不分组</option>
											  	<option value="02">按类型分组</option>
											  	<option value="03">按任务创建分组</option>
											  	<option value="04">按任务创建人所在部门分组</option>
											  	<option value="05">按优先级分组</option>
											</select>
								        </div>
							       </div>					    	     
							    </div>
							</div>-->
							<div class="layui-col-md2">
						    	<input type="text" placeholder="来自"  class="layui-input">
							</div>
							<div class="layui-col-md2">
								<input type="text" placeholder="标题"  class="layui-input">
							</div>
							<div class="layui-col-md2">
								<input type="text"  placeholder="开始时间"  class="layui-input" id="test1">
							</div>
							<div class="layui-col-md2">
							    <input type="text"  placeholder="结束时间"  class="layui-input" id="test2">				    
							</div>
							<div class="layui-col-md1" style="text-align:right;">
							        <button class="layui-btn" >查询</button>
							</div>
						</div>
					</div>
					<div>
						<p class="table_list"><i class="layui-icon">&#xe61d;</i>共3条任务</p>
						<table class="layui-table backlog_table" lay-even lay-skin="nob">
							<colgroup>
							    <col width="60">
							    <col width="100">
							    <col>
							    <col width="100">
							    <col width="100">
							    <col width="100">
							    <col width="150">
							    <col width="80">
							    <col width="80">
							    <col width="80">
							    <col width="80">
							</colgroup>
							<thead>
							    <tr>
							      <th>序号</th>
							      <th>来自</th>
							      <th>标题</th>
							      <th>类型</th>
							      <th>创建时间</th>
							      <th>状态</th>
							      <th>当前审批人</th>
							      <th>跟踪</th>
							      <th>催办</th>
							      <th>撤销</th>
							      <th>归档</th>
							    </tr> 
							</thead>
							<tbody>
							    <tr>
							      <td>1</td>
							      <td>管理员</td>
							      <td><i class="layui-icon backlog_img">&#xe63c;</i> 人生就像是一场修行</td>
							      <td>日常报销</td>
							      <td>2018-03-12</td>
							      <td>执行中</td>
							      <td>张三</td>
							      <td><i class="layui-icon tail">&#xe641;</i></td>
							      <td><i class="layui-icon tail">&#xe637;</i></td>
							      <td><i class="layui-icon back">&#xe633;</i></td>
							    </tr>
							    <tr>
							      <td>2</td>
							      <td>管理员</td>
							      <td><i class="layui-icon backlog_img">&#xe63c;</i> 人生就像是一场修行</td>
							      <td>日常报销</td>
							     <td>2018-03-12</td>
							      <td>已完成</td>
							      <td>张三</td>
							      <td><i class="layui-icon tail">&#xe641;</i></td>
							      <td></td>
							      <td></td>
							      <td><i class="layui-icon tail">&#xe7a0;</i></td>
							    </tr>
							    <tr>
							      <td>3</td>
							      <td>管理员</td>
							      <td><i class="layui-icon backlog_img">&#xe63c;</i> 人生就像是一场修行</td>
							      <td>日常报销</td>
							      <td>2018-03-12</td>
							      <td>执行中</td>
							      <td>张三</td>
							      <td><i class="layui-icon tail">&#xe641;</i></td>
							      <td><i class="layui-icon tail">&#xe637;</i></td>
							      <td><i class="layui-icon back">&#xe633;</i></td>
							    </tr>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</body>
	
</html>
	<script type="text/javascript" src="js/jquery-3.3.1.js" ></script>
	<script type="text/javascript" src="js/layui.all.js"></script>	
	<script type="text/javascript" src="tree/js/jquery.ztree.core.js"></script>
	<script type="text/javascript" src="tree/js/jquery.ztree.excheck.js"></script>
	<script type="text/javascript" src="tree/js/jquery.ztree.exedit.js"></script>
	<script>
		layui.use('laydate', function(){
			var laydate = layui.laydate;
			  	laydate.render({
			    elem: '#test1'
			});
		});
		layui.use('laydate', function(){
			var laydate = layui.laydate;
			  	laydate.render({
			    elem: '#test2'
			});
		});
		var setting = {	};

		var zNodes =[
			{ name:"父节点1 - 展开", open:true,
				children: [
					{ name:"父节点11 - 折叠",
						children: [
							{ name:"叶子节点111"},
							{ name:"叶子节点112"},
							{ name:"叶子节点113"},
							{ name:"叶子节点114"}
						]},
					{ name:"父节点12 - 折叠",
						children: [
							{ name:"叶子节点121"},
							{ name:"叶子节点122"},
							{ name:"叶子节点123"},
							{ name:"叶子节点124"}
						]},
					{ name:"父节点13 - 没有子节点", isParent:true}
				]},
			{ name:"父节点2 - 折叠",
				children: [
					{ name:"父节点21 - 展开", open:true,
						children: [
							{ name:"叶子节点211"},
							{ name:"叶子节点212"},
							{ name:"叶子节点213"},
							{ name:"叶子节点214"}
						]},
					{ name:"父节点22 - 折叠",
						children: [
							{ name:"叶子节点221"},
							{ name:"叶子节点222"},
							{ name:"叶子节点223"},
							{ name:"叶子节点224"}
						]},
					{ name:"父节点23 - 折叠",
						children: [
							{ name:"叶子节点231"},
							{ name:"叶子节点232"},
							{ name:"叶子节点233"},
							{ name:"叶子节点234"}
						]}
				]},
			{ name:"父节点3 - 没有子节点", isParent:true}

		];
		$(function(){
			$.fn.zTree.init($("#treeDemo"), setting, zNodes);
		})
	</script>