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
  		<link href="resources/desmartportal/css/layui.css" rel="stylesheet"/>
  		<link rel="stylesheet" href="resources/desmartportal/css/modules/laydate/default/laydate.css" />
  		<link href="resources/desmartportal/css/my.css" rel="stylesheet" />
  		
	</head>
	<body>
		<div class="container">
			<div class="search_area">
				<div class="layui-row layui-form">
					<div class="layui-col-xs3">
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
					</div>
					<div class="layui-col-xs2">
				    	<input type="text" placeholder="来自"  class="layui-input">
					</div>
					<div class="layui-col-xs2">
						<input type="text" placeholder="标题"  class="layui-input">
					</div>
					<div class="layui-col-xs2">
						<input type="text"  placeholder="开始时间"  class="layui-input" id="test1">
					</div>
					<div class="layui-col-xs2">
					    <input type="text"  placeholder="结束时间"  class="layui-input" id="test2">				    
					</div>
					<div class="layui-col-xs1" style="text-align:right;">
					        <button class="layui-btn" >查询</button>
					</div>
				</div>
			</div>
			<div>
				<p class="table_list"><i class="layui-icon">&#xe61d;</i>共3条任务</p>
				<table class="layui-table backlog_table">
					<colgroup>
					    <col width="60">
					    <col width="100">
					    <col>
					    <col width="150">
					    <col width="150">
					    <col width="150">
					</colgroup>
					<thead>
					    <tr>
					      <th>序号</th>
					      <th>来自</th>
					      <th>标题</th>
					      <th>类型</th>
					      <th>接收时间</th>
					      <th>阅读时间</th>
					    </tr> 
					</thead>
					<tbody>
					    <tr>
					      <td>1</td>
					      <td>管理员</td>
					      <td><i class="layui-icon backlog_img">&#xe63c;</i> 人生就像是一场修行</td>
					      <td>日常报销</td>
					      <td>2018-03-12</td>
					      <td>2018-03-12</td>
					    </tr>
					    <tr>
					      <td>2</td>
					      <td>管理员</td>
					      <td><i class="layui-icon backlog_img">&#xe63c;</i> 人生就像是一场修行</td>
					      <td>日常报销</td>
					      <td>2018-03-12</td>
					      <td>2018-03-12</td>
					    </tr>
					    <tr>
					      <td>3</td>
					      <td>管理员</td>
					      <td><i class="layui-icon backlog_img">&#xe63c;</i> 人生就像是一场修行</td>
					      <td>日常报销</td>
					      <td>2018-03-12</td>
					      <td>2018-03-12</td>
					    </tr>
					</tbody>
				</table>
			</div>
		</div>
		<script type="text/javascript" src="resources/desmartportal/js/jquery-3.3.1.js" ></script>
		<script type="text/javascript" src="resources/desmartportal/js/layui.all.js"></script>	
		<!--IE8只能支持jQuery1.9-->
	    <!--[if lte IE 8]>
	    <script src="http://cdn.bootcss.com/jquery/1.9.0/jquery.min.js"></script>
	    <![endif]-->
		<!-- 让IE8/9支持媒体查询，从而兼容栅格 -->
		<!--[if lt IE 9]>
		  <script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
		  <script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
		<![endif]-->
	</body>
	
</html>
	<script type="text/javascript" src="resources/desmartportal/js/common.js"></script>
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
	</script>