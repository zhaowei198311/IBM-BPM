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
				<button class="layui-btn" >保存</button>				
			</div>
			<div class="top_div">
				流程发起目录导航
				<button class="layui-btn layui-btn-sm layui-btn-primary new_btn" style="float:right;">创建分组</button>
			</div>			
			<div class="menu_div" >
			  	<div class="layui-row">
				    <div class="layui-col-xs3">
				      	1、分组标题<span class="red">*</span>
				    </div>
				    <div class="layui-col-xs8">
				     	<input type="text" value="1" class="txt"/>
				    </div>
				    <div class="layui-col-xs1" style="text-align:right;">
				      	<button class="layui-btn layui-btn-sm layui-btn-danger">删除</button>
				    </div>
			 	</div>
			 	<div class="layui-row">
				    <div class="layui-col-xs3">
				      	流程名称
				    </div>
				    <div class="layui-col-xs8">
				     	图标路径
				    </div>
				    <div class="layui-col-xs1" style="text-align:right;">
				      	<i class="layui-icon add_menu"  title="添加流程">&#xe654;</i> 
				    </div>
			 	</div>
			 	<div class="layui-row last">
				    <div class="layui-col-xs3">
				      	新员工
				    </div>
				    <div class="layui-col-xs8">
				     	<input type="text"  placeholder="请使用64*64像素图片，格式例如：../console/images/logo.png"  class="txt"/>
				    </div>
				    <div class="layui-col-xs1" style="text-align:right;">
				      	<i class="layui-icon delete_menu"  title="删除流程">&#x1006;</i> 
				    </div>
			 	</div>
			</div>
			<div class="menu_div" >
			  	<div class="layui-row">
				    <div class="layui-col-xs3">
				      	2、分组标题<span class="red">*</span>
				    </div>
				    <div class="layui-col-xs8">
				     	<input type="text" value="2" class="txt"/>
				    </div>
				    <div class="layui-col-xs1" style="text-align:right;">
				      	<button class="layui-btn layui-btn-sm layui-btn-danger">删除</button>
				    </div>
			 	</div>
			 	<div class="layui-row">
				    <div class="layui-col-xs3">
				      	流程名称
				    </div>
				    <div class="layui-col-xs8">
				     	图标路径
				    </div>
				    <div class="layui-col-xs1" style="text-align:right;">
				      	<i class="layui-icon add_menu" title="添加流程">&#xe654;</i> 
				    </div>
			 	</div>
			 	<div class="layui-row last">
				    <div class="layui-col-xs3">
				      	新员工
				    </div>
				    <div class="layui-col-xs8">
				     	<input type="text" placeholder="请使用64*64像素图片，格式例如：../console/images/logo.png" class="txt"/>
				    </div>
				    <div class="layui-col-xs1" style="text-align:right;">
				      	<i class="layui-icon delete_menu" title="删除流程">&#x1006;</i> 
				    </div>
			 	</div>
			</div>
		</div>
	</body>
	<div class="display_container">
		<div class="display_content">
			<div class="top">
				选择流程
			</div>
			<div class="middle">
				<input type="text" class="search_user" placeholder="快速搜索APP名称"/>
				<i class="layui-icon choice_user" style="position:absolute;right:20px;top:15px;cursor:pointer;">&#xe615;</i>
				<p class="advanced_p">报销</p>
				<form class="layui-form" action=""> 
					<div class="layui-form-item">
					    <div class="layui-input-block">
					        <input type="checkbox" name="" title="日常报销/ceshi" lay-skin="primary">
							<input type="checkbox" name="" title="日常报销/demo1" lay-skin="primary"> 
							<input type="checkbox" name="" title="日常报销/报销" lay-skin="primary">
							<input type="checkbox" name="" title="日常报销/新员工" lay-skin="primary"> 
					    </div>
					</div>				  
				</form>
				<p class="advanced_p">工作台</p>
				<form class="layui-form" action=""> 
					<div class="layui-form-item">
					    <div class="layui-input-block">
					        <input type="checkbox" name="" title="委托审批流程组/委托审批" lay-skin="primary"> 
					    </div>
					</div>				  
				</form>
				<p class="advanced_p">内容管理</p>
				<form class="layui-form" action=""> 
					<div class="layui-form-item">
					    <div class="layui-input-block">
					        <input type="checkbox" name="" title="CMS内容发布流程/内容发布流程" lay-skin="primary"> 
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
	<script type="text/javascript" src="resources/desmartportal/js/jquery-3.3.1.js" ></script>
	<script type="text/javascript" src="resources/desmartportal/js/layui.all.js"></script>	
	<script type="text/javascript" src="resources/desmartportal/js/common.js"></script>
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
	
	<script>
		$(function(){
			$(".add_menu").click(function(){
				$(".display_container").css("display","block");
			})
			$(".sure_btn").click(function(){
				$(".display_container").css("display","none");
			})
			$(".cancel_btn").click(function(){
				$(".display_container").css("display","none");
			})
			$(".new_btn").click(function(){
				var le=$(".menu_div").length + 1;
				$(".container").append($('<div class="menu_div" >'+
			  	'<div class="layui-row">'+
				    '<div class="layui-col-md3">'+le+
				      	'、分组标题<span class="red">*</span>'+
				    '</div>'+
				   '<div class="layui-col-md8">'+
				     	'<input type="text" class="txt"/>'+
				    '</div>'+
				    '<div class="layui-col-md1" style="text-align:right;">'+
				      	'<button class="layui-btn layui-btn-sm layui-btn-danger">删除</button>'+
				    '</div>'+
			 	'</div>'+
			 	'<div class="layui-row">'+
				    '<div class="layui-col-md3">'+
				      '流程名称'+
				    '</div>'+
				    '<div class="layui-col-md8">'+
				       '图标路径'+
				    '</div>'+
				    '<div class="layui-col-md1" style="text-align:right;">'+
				       '<i class="layui-icon add_menu"  title="添加流程">&#xe654;</i> '+
				    '</div>'+
			 	'</div>'));
			 	$(".delete_menu").click(function(){
					$(this).parent().parent().parent().remove();
				});
				$(".add_menu").click(function(){
				$(".display_container").css("display","block");
				})
				$(".sure_btn").click(function(){
					$(".display_container").css("display","none");
				})
				$(".cancel_btn").click(function(){
					$(".display_container").css("display","none");
				})
			 	});
				
			})
	</script>