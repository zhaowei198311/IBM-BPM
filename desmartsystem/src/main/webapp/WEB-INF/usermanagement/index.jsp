<%@ page language="java" contentType="text/html; charset=UTF-8"
    isErrorPage="true"
    pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<base href="<%=basePath%>">
		<meta charset="utf-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
  		<title>首页</title>
  		<link href="styles/css/layui.css" rel="stylesheet"/>
  		<link href="styles/css/my.css" rel="stylesheet" />
  		
	</head>
	<body>
		<ul class="layui-nav layui-nav-tree layui-nav-side">
			<li class="logo"><img src="images/logo.gif"/></li>
			<li class="layui-nav-item">
			     <a href="javascript:;"><i class="layui-icon">&#xe614;</i> 系统管理</a>
			    <dl class="layui-nav-child">
			      <dd><a href="sysUser/organization" target="iframe0">组织管理</a></dd>
			      <dd><a href="sysUser/user"  target="iframe0">用户管理</a></dd>
			      <dd><a href="sysRole/role" target="iframe0">业务角色管理</a></dd>
			      <dd><a href="sysResource/resource_list" target="iframe0">模块资源管理</a></dd>
			      <dd><a href="sysRole/system_role" target="iframe0">系统角色管理</a></dd>
			      <dd><a href="sysTeam/group" target="iframe0">角色组管理</a></dd>
			      <dd><a href="sysDictionary/dictionary" target="iframe0">数据字典</a></dd>
			      <dd><a href="http://localhost:8080/ssmquartztest/Quarz/QuarzList.jsp" target="iframe0">定时任务管理</a></dd>
			      <dd><a href="sysUser/globalConfig" target="iframe0">全局配置管理</a></dd>
			    </dl>
			</li>
			<li class="layui-nav-item">
			    <a href="javascript:;"><i class="layui-icon">&#xe609;</i> 流程管理</a>
			    <dl class="layui-nav-child">
			      <dd><a href="classify.html" target="iframe0">流程分类</a></dd>
			      <dd><a href="set.html"  target="iframe0">流程配置</a></dd>
			    </dl>
			</li>
		  	<li class="layui-nav-item"><a href="form_management.html" target="iframe0"><i class="layui-icon">&#xe62d;</i> 表单管理</a></li>
			<li class="layui-nav-item"><a href="interfaces/index" target="iframe0"><i class="layui-icon">&#xe614;</i> 接口管理</a></li>
		</ul>
		<div id="index_container">
			<div class="index_top">
				<span class="system_title">来伊份BPM后台管理系统</span>
				<span class="logout"><i class="layui-icon">&#xe60e;</i> 退出</span>
			</div>
			<div id="content_main">
				<iframe  name="iframe0" width="100%" height="100%" src="organization.html" frameborder="0" id="iframe_1"></iframe>
			</div>
			<div class="footer">
				Copyright &copy; 来伊份 2018
			</div>
		</div>
		
	</body>
	
</html>
	<script type="text/javascript" src="scripts/js/jquery-3.3.1.js" ></script>
	<script type="text/javascript" src="scripts/js/layui.all.js"></script>	
	<script>
        function changeFrameHeight(){
	        var ifm= document.getElementById("iframe_1"); 
	        ifm.height=document.documentElement.clientHeight-105;	
	    }
	    window.onresize=function(){  
	         changeFrameHeight();  	
	    } 
		window.onload=function(){
			var ifm= document.getElementById("iframe_1"); 
	        ifm.height=document.documentElement.clientHeight-105;
		}
	</script>