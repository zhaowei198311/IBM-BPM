<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<head>
	<base href="<%=basePath%>">
	<meta charset="utf-8">
	<title>来伊份BPM系统</title>
	<meta name="renderer" content="webkit">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
	<link rel="stylesheet" href="resources/desmartportal/css/layui.css" media="all">
	<link rel="stylesheet" href="resources/desmartportal/css/admin.css" media="all">
	<link rel="stylesheet" href="resources/desmartportal/css/my.css" />
	<!-- <link rel="stylesheet" href="resources/desmartportal/css/menus/jquery-menus.css" /> -->
	<link rel="stylesheet" href="resources/desmartportal/css/jquery.mCustomScrollbar.css"/>
	<link rel="stylesheet" href="resources/desmartportal/css/my_menu.css"/>
	<script type="text/javascript" src="resources/desmartportal/js/jquery.min.js"></script>
	<script type="text/javascript" src="resources/desmartportal/js/jquery.mCustomScrollbar.concat.min.js"></script>
	<script type="text/javascript" src="resources/desmartportal/js/layui.all.js"></script>
	<script type="text/javascript" src="resources/desmartportal/js/common.js"></script>
	<style>
		.username {
			color: #8095a8;
			font-weight: 600;
			height: 40px;
			text-align: center;
		}

		.logout_img {
			width: 20px;
			margin-left: 10px;
			cursor: pointer;
		}
		
		.category_icon{
			float: right;
		}
	</style>
</head>

<body>
	<div id="LAY_app">
		<div class="layui-layout layui-layout-admin">
			<div class="layui-header">
				<div class="layui-nav index_top">
					<span class="layui-breadcrumb" style="margin-left: 220px;float: left;">
						<a href="javascript:;">我的任务</a>
						<a href="javascript:;">
							<cite>待办任务</cite>
						</a>
					</span>
					<span class="logout">
						<span style="padding-right:20px;cursor:default;">${userName}</span>
						<span onclick="logout();"><i class="layui-icon">&#xe60e;</i> 注销</span>
					</span>
				</div>
			</div>
			<!-- 侧边菜单 -->
			<div class="layui-side layui-side-menu">
				<div class="layui-side-scroll">
					<div class="layui-side layui-bg-black">
						<div class="layui-side-scroll">
							<!-- 左侧导航区域（可配合layui已有的垂直导航） -->
							<ul class="layui-nav layui-nav-tree" lay-filter="test">
								<li class="logo">
									<img src="resources/desmartportal/images/logo.png" class="big_logo" />
									<img src="resources/desmartportal/images/logo.png" class="mini_logo" />
									<span class="system_title">来伊份BPM系统</span>
								</li>
								<li class="layui-nav-item layui-nav-itemed">
									<a href="javascript:;" id="my_task">
										<i class="layui-icon">&#xe60e;</i>
										<span> 我的任务</span>
									</a>
									<dl class="layui-nav-child">
										<dd id="approvalTask" class="layui-this">
											<a href="menus/backlog" target="iframe0" class="typeColor" onclick="getUserTask();">
												待办任务
												<span class="layui-badge" id="daiban_icon">0</span>
											</a>
										</dd>
										<dd id="finishedTask">
											<a href="menus/finishProcess" target="iframe0" class="typeColor">已办任务</a>
										</dd>
									</dl>
								</li>
								<li class="layui-nav-item">
									<a href="menus/notRedProcess" target="iframe0" class="typeColor" id="notRedProcess">
										<i class="layui-icon">&#xe645;</i>
										<span> 抄送</span>
									</a>
								</li>
								<li class="layui-nav-item">
									<a href="drafts/index" target="iframe0" class="typeColor" id="drafts">
										<i class="layui-icon">&#xe640;</i>
										<span> 草稿箱</span>
									</a>
								</li>
								<li class="layui-nav-item">
									<a href="agent/index" target="iframe0" class="typeColor" id="agent">
										<i class="layui-icon">&#xe6b2;</i>
										<span> 代理设置</span>
									</a>
								</li>
								<li class="layui-nav-item">
									<a id="shopLife" href="javascript:void(0)" target="iframe0" class="typeColor" id="shopLife">
										<i class="layui-icon">&#xe68e;</i>
										<span> 门店生命周期</span>
									</a>
								</li>
								<li class="layui-nav-item">
									<a href="javascript:void(0)" >
										<i class="layui-icon">&#xe629;</i>
										<span> 报表管理</span>
									</a>
									<dl class="layui-nav-child">
										<dd id="projectStatement">
											<a href="projectStatement/projectStatement" target="iframe0" class="typeColor">
												流程过程业务报表
											</a>
										</dd>
										<dd id="formBusinessReport">
											<a href="projectStatement/formBusinessReport" target="iframe0" class="typeColor">流程表单业务报表</a>
										</dd>
										<dd id="storeBusinessReport">
											<a href="projectStatement/storeBusinessReport" target="iframe0"  title="门店全生命周期业务报表" class="typeColor">
												门店全生命周期业务报表
											</a>
										</dd>
									</dl>
								</li>
								<!-- <i class="layui-icon kit-side-fold" style="color:#ea6000;margin-left:20px;" title="收缩菜单">&#xe647;</i> -->
							</ul>
						</div>
					</div>
				</div>
			</div>
			<div class="layui-body" id="LAY_app_body">
				<div class="layadmin-tabsbody-item layui-show">
					<iframe src="menus/backlog" id="frameContent" name="iframe0" frameborder="0" class="layadmin-iframe"></iframe>
					<input type="hidden" id="userId" value="${userId}"/>
					<div class="menu_container">
						<div class="menu menu0">
							<div class="menu_title">
								<span class="menu_title_span">
									门店生命周期
								</span>
							</div>
							<br>
							<div class="ul_li">
								<ul>
								
								</ul>
							</div>				
						</div>
						<div class="menu menu1">
							<div class="menu_title">
								<span class="menu_title_span">
									一级菜单1
								</span>
							</div>
							<br>
							<div class="ul_li">
								<ul>
									
								</ul>
							</div>
							<!--<div class="arrow_down">
								<img src="img/down_arrow.png" class="arrow_down_img"/>
							</div>-->
						</div>	
						<div class="menu menu2">
							<div class="menu_title">
								<span class="menu_title_span">
									二级菜单1
								</span>
							</div>
							<br>
							<div class="ul_li">
								<ul>
									
								</ul>
							</div>
				 		</div>	
				 		<div class="menu menu3">
							<div class="menu_title">
								<span class="menu_title_span">
									三级级菜单1
								</span>
							</div>
							<br>
							<div class="ul_li">
								<ul>
									
								</ul>
							</div>
				 		</div>
					</div>
				</div>
			</div>
			<div class="layui-footer">
				<div class="footer">Copyright &copy; 来伊份 2018</div>
			</div>
			<!-- 辅助元素，一般用于移动设备下遮罩 -->
			<div class="layadmin-body-shade" layadmin-event="shade"></div>
		</div>
	</div>
	<script type="text/javascript" src="resources/desmartportal/js/index.js"></script>
	<script>
		$(function () {
			layui.use('element', function () {
				var element = layui.element;
				
				element.render();
			});
			
			getUserTask();
			// 定时 去查询我的 代办任务
			window.setInterval(getUserTask, 30000);
		})

		// 获取用户有多少代办
		function getUserTask() {
			var uId = document.getElementById('userId').value;
			$.ajax({
				url:common.getPath() + '/user/todoTask',
				type: 'POST',
				dataType: 'text',
				data: {
					userId: uId
				},
				success: function (result) {
					// 渲染到待办
					if (result == 0) {
						$("#daiban_icon").css("display", "none");
						var daibanObj = document.getElementById("frameContent").contentDocument.getElementById("daiban_icon");
						$(daibanObj).text(result);
					} else if (result > 0) {
						$("#daiban_icon").text(result);
						var daibanObj = document.getElementById("frameContent").contentDocument.getElementById("daiban_icon");
						$(daibanObj).text(result);
					} else {
						layer.msg("登陆超时！请重新登陆！", function () {
							window.location.reload();
						});
					}
				}
			})
		}
		
		//注销
		function logout(){
			window.location.href = common.getPath()+"/user/logout";
		}
		
		function openProView(insId){
			$.ajax({
		        url: common.getPath() +'/processInstance/viewProcess',
		        type: 'post',
		        dataType: 'text',
		        data: {
		            insId: insId
		        },
		        success: function (result) {
		            var index = layer.open({
		                type: 2,
		                title: '流程图',
		                shadeClose: true,
		                offset: ['50px', '20%'],
		                shade: 0.3,
		                maxmin:true,
		                area: ['890px', '570px'],
		                content: result
		            });
		        }
		    });
		}
	</script>
</body>
</html>