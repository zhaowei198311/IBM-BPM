<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
    String path = request.getContextPath();
     String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
             + path + "/";
%>
<html>
        
<head>
    <base href="<%=basePath%>">
    <meta charset="utf-8" />
    <meta name="apple-mobile-web-app-capable" content="yes">  
	<meta name="viewport" content="width=device-width, initial-scale=1.0,minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">  
	<title>待办列表</title>
	<link href="resources/desmartportal/css/mymobile_layui.css" rel="stylesheet"/>
  	<link rel="stylesheet" href="resources/desmartportal/css/modules/laydate/default/laydate.css" />
  	<link href="resources/desmartportal/css/iconfont.css" rel="stylesheet" />
  	<link href="resources/desmartportal/css/mymobile.css" rel="stylesheet" />
  	<link href="resources/desmartportal/css/mobile_backlog.css?v=1.01" rel="stylesheet" />
  	<script type="text/javascript" src="resources/desmartportal/js/layui.all.js"></script>
  	<script type="text/javascript" src="resources/desmartportal/js/jquery-3.3.1.js"></script>
	<script type="text/javascript" src="resources/desmartportal/lCalendar/js/lCalendar.js"></script>
	<link href="resources/desmartportal/lCalendar/css/lCalendar.css" rel="stylesheet" />
	<script type="text/javascript" src="resources/desmartportal/js/common.js"></script>
	<script type="text/javascript" src="resources/desmartportal/js/my/mobile_backlog.js"></script>
</head>
<body style="background-color: #f2f2f2;width:100%;">
	<div class="mobile_container">
		<div class="mobile_top">
			<div class="top_left">
            	<!-- <i class="layui-icon">&#xe65c;</i> -->
            </div>
            <div class="top_right">
            	<!-- <i class="icon iconfont icon-shaixuan" id="operate_menu" title="筛选" onclick="fiterDivShow()"></i> -->
            </div>
            <div class="top_content">
            	<span>待办列表</span>
            </div>
		</div>
		<div class="mobile_middle">
			<div class="task_div layui-tab layui-tab-brief">
				<ul class="layui-tab-title">
					<li	class="layui-this" id="backlog_li">待办(<span id="daiban_icon">2018</span>)</li>
					<li id="finised_li">已办(<span id="yiban_icon">8</span>)</li>
					<li id="end_li">办结(<span id="banjie_icon">10</span>)</li>
				</ul>
				<div class="layui-tab-content">
					<div class="layui-tab-item layui-show" id="backlog_ul">
						<ul class="tab_ul" id="backlog_list">
							<!-- <li>
								<div class="backlog_top">
									<div class="backlog_process">
										<span class="backlog_process_name">门店新开店流程</span>
										<span class="backlog_process_no">100010185BG60</span>
										<span class="backlog_task_status">待处理</span>
									</div>
								</div>
								<div class="backlog_content">
									<table>
										<thead>
											<tr>
												<td class="process_title_div">
													<span class="process_title">门店新开店申请-马亚伟</span>
												</td>
											</tr>
										</thead>
										<tbody>
											<tr>
												<td>前序处理人：马亚伟</td>
											</tr>
											<tr>
												<td>创建人：马亚伟</td>
											</tr>
											<tr>
												<td>2018-08-10 13:33:33</td>
											</tr>
										</tbody>
									</table>
									<div class="progress_bar_div">
										<div class="progress_time_div">
											<span class="progress_time">剩余：6小时</span>
										</div>
										<div class="layui-progress" lay-filter="progressBar" lay-showpercent="yes" style="position: relative;">
											<div class="layui-progress-bar" lay-percent="0%"
												style="background-color: #FF9052; width: 75%;"></div>
										</div>
									</div>
									<div class="task_agent_status">
										<span class="task_agent_text">代</span>
									</div>
								</div>
							</li> -->
						</ul>
					</div>
					<div class="layui-tab-item handle_table" id="finished_ul">
						<ul class="tab_ul" id="finised_list">
							<!-- <li>
								<div class="backlog_top">
									<div class="backlog_process">
										<span class="backlog_process_name">门店新开店流程</span>
										<span class="backlog_process_no">100010185BG60</span>
									</div>
								</div>
								<div class="backlog_content">
									<table>
										<thead>
											<tr>
												<td class="process_title_div">
													<span class="process_title">门店新开店申请-马亚伟</span>
												</td>
											</tr>
										</thead>
										<tbody>
											<tr>
												<td>前序处理人：马亚伟</td>
											</tr>
											<tr>
												<td>创建人：马亚伟</td>
											</tr>
											<tr>
												<td>接收时间：2018-08-10 13:33:33</td>
											</tr>
											<tr>
												<td>处理时间：2018-08-10 13:33:33</td>
											</tr>
										</tbody>
									</table>
									<div class="task_agent_status">
										<span class="task_agent_text">代</span>
									</div>
								</div>
							</li> -->
						</ul>
					</div>
					<div class="layui-tab-item handle_table" id="end_ul">
						<ul class="tab_ul" id="end_list">
						</ul>
					</div>
				</div>
			</div>
			<ul class="layui-fixbar">
				<li class="layui-icon layui-fixbar-top" lay-type="top" style="display: list-item;margin-bottom: 5px;">
					<i class="icon iconfont icon-40" id="operate_menu" title="筛选" onclick="fiterDivShow()"></i>
				</li>
				<li class="layui-icon layui-fixbar-top" lay-type="top" style="display: list-item;">
					&#xe604;
				</li>
			</ul>
		</div>
		<div id="filter_div">
			<form>
			<table class="layui-form">
				<tr>
					<th>发起人：</th>
					<td>
						<input type="text" class="layui-input" id="process_staff" placeholder="请输入流程发起人"/>
					</td>
				</tr>
				<tr>
					<th>前序处理人：</th>
					<td>
						<input type="text" class="layui-input" id="last_activity_staff" placeholder="请输入上一环节处理人"/>
					</td>
				</tr>
				<tr>
					<th>流程主题：</th>
					<td>
						<input type="text" class="layui-input" id="process_instance_title" placeholder="请输入流程主题"/>
					</td>
				</tr>
				<tr>
					<th>流程名称：</th>
					<td>
						<input type="text" class="layui-input" id="proName" placeholder="请输入流程名称"/>
					</td>
				</tr>
				<tr>
					<th>开始时间：</th>
					<td>
						<input type="text" class="layui-input" id="start_time" placeholder="请输入开始时间" readonly="readonly" data-lcalendar="1900-01-1,2099-12-31"/>
					</td>
				</tr>
				<tr>
					<th>结束时间：</th>
					<td>
						<input type="text" class="layui-input" id="end_time" placeholder="请输入结束时间" readonly="readonly" data-lcalendar="1900-01-1,2099-12-31"/>
					</td>
				</tr>
			</table>
			<div class="filter_btn_div">
				<input type="reset" class="layui-btn filter_btn" value="重置"/>
				<input type="button" class="layui-btn filter_btn" value="搜索" onclick="queryTask()"/>
			</div>
			</form>
		</div>
	</div>
</body>
<script type="text/javascript">
	$(function(){
		var t1 = window.setInterval(function(){
			if($(".mobile_top").is(":hidden")){
				$(".mobile_middle").css("margin-top","-10px");
				window.clearInterval(t1);
			}
		},50); 
	})
</script>
</html>