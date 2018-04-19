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
  		<link href="resources/css/layui.css" rel="stylesheet"/>
  		<link href="resources/css/my.css" rel="stylesheet" />
  		
	</head>
	<body>
		<ul class="layui-nav layui-nav-tree layui-nav-side">
			<li class="logo"><img src="resources/images/logo.gif"/></li>
			<li class="layui-nav-item">
			     <a href="javascript:;"><i class="layui-icon">&#xe614;</i> 我的待办</a>
			     <dl class="layui-nav-child">
			      <dd><a href="" target="iframe0">1</a></dd>
			      <dd><a href=""  target="iframe0">2</a></dd>
			    </dl>
			</li>
			<li class="layui-nav-item">
			    <a href="javascript:;"><i class="layui-icon">&#xe609;</i> 我的已办</a>
			    <dl class="layui-nav-child">
			      <dd><a href="" target="iframe0">1</a></dd>
			    </dl>
			</li>
		  	<li class="layui-nav-item"><a href="" target="iframe0"><i class="layui-icon">&#xe62d;</i> 我的待阅</a></li>
			<li class="layui-nav-item"><a href="" target="iframe0"><i class="layui-icon">&#xe64c;</i> 我的已阅</a></li>
			<li class="layui-nav-item"><a href="" target="iframe0"><i class="layui-icon">&#xe614;</i> 草稿箱</a></li>
			<li class="layui-nav-item"><a href="" target="iframe0"><i class="layui-icon">&#xe614;</i> 我发起的流程</a></li>
			<li class="layui-nav-item"><a href="" target="iframe0"><i class="layui-icon">&#xe614;</i> 流程中心列表</a></li>
			<li class="layui-nav-item"><a href="" target="iframe0"><i class="layui-icon">&#xe614;</i> 代理设置</a></li>
			<li class="layui-nav-item"><a href="" target="iframe0"><i class="layui-icon">&#xe614;</i> 报表</a></li>
		</ul>
		<div id="index_container">
			<div class="index_top">
				<span class="system_title">来伊份BPM个人工作台</span>
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
	<script type="text/javascript" src="resources/js/jquery-3.3.1.js" ></script>
	<script type="text/javascript" src="resources/js/layui.all.js"></script>	
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