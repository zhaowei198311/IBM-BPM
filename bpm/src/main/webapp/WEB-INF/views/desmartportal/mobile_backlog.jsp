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
	<title>代办列表</title>
	<link href="resources/desmartportal/css/layui.css" rel="stylesheet"/>
  	<link rel="stylesheet" href="resources/desmartportal/css/modules/laydate/default/laydate.css" />
  	<link href="resources/desmartportal/css/my.css" rel="stylesheet" />
  	<link href="resources/desmartportal/css/iconfont.css" rel="stylesheet" />
  	<link href="resources/desmartportal/css/mobile_backlog.css" rel="stylesheet" />
  	<script type="text/javascript" src="resources/desmartportal/js/layui.all.js"></script>
  	<script type="text/javascript" src="resources/desmartportal/js/jquery-3.3.1.js"></script>
	<script type="text/javascript" src="resources/desmartportal/lCalendar/js/lCalendar.js"></script>
	<link href="resources/desmartportal/lCalendar/css/lCalendar.css" rel="stylesheet" />
	<script type="text/javascript" src="resources/desmartportal/js/my/mobile_backlog.js"></script>
	<style type="text/css">
		.layui-progress-bar {
			display: block;
			min-width: 8px;
			height: 12px;
			background: #2067c5;
			background-image: -webkit-linear-gradient(top, rgba(255, 255, 255, 0.3),
				rgba(255, 255, 255, 0) 30%, rgba(0, 0, 0, 0) 65%, rgba(0, 0, 0, 0.2)),
				-webkit-linear-gradient(left, #2067c5, #24c1fc);
			background-image: -moz-linear-gradient(top, rgba(255, 255, 255, 0.3),
				rgba(255, 255, 255, 0) 30%, rgba(0, 0, 0, 0) 65%, rgba(0, 0, 0, 0.2)),
				-moz-linear-gradient(left, #2067c5, #24c1fc);
			background-image: -o-linear-gradient(top, rgba(255, 255, 255, 0.3),
				rgba(255, 255, 255, 0) 30%, rgba(0, 0, 0, 0) 65%, rgba(0, 0, 0, 0.2)),
				-o-linear-gradient(left, #2067c5, #24c1fc);
			background-image: linear-gradient(to bottom, rgba(255, 255, 255, 0.3),
				rgba(255, 255, 255, 0) 30%, rgba(0, 0, 0, 0) 65%, rgba(0, 0, 0, 0.2)),
				linear-gradient(to right, #2067c5, #24c1fc);
			border-radius: 4px;
			-webkit-box-shadow: inset 0 1px rgba(0, 0, 0, 0.2), inset 0 0 0 1px
				rgba(0, 0, 0, 0.2);
			box-shadow: inset 0 1px rgba(0, 0, 0, 0.2), inset 0 0 0 1px
				rgba(0, 0, 0, 0.2);
		}
	</style>
</head>
<body style="background-color: #f2f2f2;">
	<div class="mobile_container">
		<div class="mobile_top">
			<div class="top_left">
            	<!-- <i class="layui-icon">&#xe65c;</i> -->
            </div>
            <div class="top_right">
            	<i class="icon iconfont icon-shaixuan" id="operate_menu" title="筛选" onclick="fiterDivShow()"></i>
            </div>
            <div class="top_content">
            	<span>代办列表</span>
            </div>
		</div>
		<div class="mobile_middle">
			<ul class="tab_ul" id="backlog_list">
				<p class="table_list">
					<i class="layui-icon"></i>共<span id="daiban_icon">303</span>条任务
				</p>
				<li>
					<table>
						<tr>
							<th>流程标题：</th>
							<td>测试字段权限</td>
						</tr>
						<tr>
							<th>环节名称：</th>
							<td>task2</td>
						</tr>
						<tr>
							<th>上一环节处理人：</th>
							<td>马亚伟</td>
						</tr>
						<tr>
							<th>流程创建人：</th>
							<td>马亚伟</td>
						</tr>
						<tr>
							<th>任务接收时间：</th>
							<td>2018-07-10 18:35:17</td>
						</tr>
						<tr>
							<th colspan="2">
								<div class="layui-progress layui-progress-big"
									lay-filter="progressBar" lay-showpercent="yes"
									style="position: relative;">
									<div class="layui-progress-bar" lay-percent="0%"
										style="background-color: red; width: 75%;"></div>
									<span class="progress_time" style="right: 1%;">审批剩余时间6小时</span>
								</div>
							</th>
						</tr>
					</table>
					<div class="is_agent">
						<span>代</span>
					</div>
					<div class="task_status">
						<span>待处理</span>
					</div>
				</li>
				<li>
					<table>
						<tr>
							<th>流程标题：</th>
							<td>测试字段权限</td>
						</tr>
						<tr>
							<th>环节名称：</th>
							<td>task2</td>
						</tr>
						<tr>
							<th>上一环节处理人：</th>
							<td>马亚伟</td>
						</tr>
						<tr>
							<th>流程创建人：</th>
							<td>马亚伟</td>
						</tr>
						<tr>
							<th>任务接收时间：</th>
							<td>2018-07-10 18:35:17</td>
						</tr>
						<tr>
							<th colspan="2">
								<div class="layui-progress layui-progress-big"
									lay-filter="progressBar" lay-showpercent="yes"
									style="position: relative;">
									<div class="layui-progress-bar" lay-percent="0%"
										style="background-color: red; width: 75%;"></div>
									<span class="progress_time" style="right: 1%;">审批剩余时间6小时</span>
								</div>
							</th>
						</tr>
					</table>
					<div class="is_agent">
						<span>代</span>
					</div>
					<div class="task_status">
						<span>等待加签结束</span>
					</div>
				</li>
				<li>
					<table>
						<tr>
							<th>流程标题：</th>
							<td>测试字段权限</td>
						</tr>
						<tr>
							<th>环节名称：</th>
							<td>task2</td>
						</tr>
						<tr>
							<th>上一环节处理人：</th>
							<td>马亚伟</td>
						</tr>
						<tr>
							<th>流程创建人：</th>
							<td>马亚伟</td>
						</tr>
						<tr>
							<th>任务接收时间：</th>
							<td>2018-07-10 18:35:17</td>
						</tr>
						<tr>
							<th colspan="2">
								<div class="layui-progress layui-progress-big"
									lay-filter="progressBar" lay-showpercent="yes"
									style="position: relative;">
									<div class="layui-progress-bar" lay-percent="0%"
										style="background-color: red; width: 75%;"></div>
									<span class="progress_time" style="right: 1%;">审批剩余时间6小时</span>
								</div>
							</th>
						</tr>
					</table>
					<div class="task_status">
						<span>待处理</span>
					</div>
				</li>
				<li>
					<table>
						<tr>
							<th>流程标题：</th>
							<td>测试字段权限</td>
						</tr>
						<tr>
							<th>环节名称：</th>
							<td>task2</td>
						</tr>
						<tr>
							<th>上一环节处理人：</th>
							<td>马亚伟</td>
						</tr>
						<tr>
							<th>流程创建人：</th>
							<td>马亚伟</td>
						</tr>
						<tr>
							<th>任务接收时间：</th>
							<td>2018-07-10 18:35:17</td>
						</tr>
						<tr>
							<th colspan="2">
								<div class="layui-progress layui-progress-big"
									lay-filter="progressBar" lay-showpercent="yes"
									style="position: relative;">
									<div class="layui-progress-bar" lay-percent="0%"
										style="background-color: red; width: 75%;"></div>
									<span class="progress_time" style="right: 1%;">审批剩余时间6小时</span>
								</div>
							</th>
						</tr>
					</table>
					<div class="is_agent">
						<span>代</span>
					</div>
					<div class="task_status">
						<span>待处理</span>
					</div>
				</li>
				<li>
					<table>
						<tr>
							<th>流程标题：</th>
							<td>测试字段权限</td>
						</tr>
						<tr>
							<th>环节名称：</th>
							<td>task2</td>
						</tr>
						<tr>
							<th>上一环节处理人：</th>
							<td>马亚伟</td>
						</tr>
						<tr>
							<th>流程创建人：</th>
							<td>马亚伟</td>
						</tr>
						<tr>
							<th>任务接收时间：</th>
							<td>2018-07-10 18:35:17</td>
						</tr>
						<tr>
							<th colspan="2">
								<div class="layui-progress layui-progress-big"
									lay-filter="progressBar" lay-showpercent="yes"
									style="position: relative;">
									<div class="layui-progress-bar" lay-percent="0%"
										style="background-color: red; width: 75%;"></div>
									<span class="progress_time" style="right: 1%;">审批剩余时间6小时</span>
								</div>
							</th>
						</tr>
					</table>
					<div class="is_agent">
						<span>代</span>
					</div>
					<div class="task_status">
						<span>等待加签结束</span>
					</div>
				</li>
				<li>
					<table>
						<tr>
							<th>流程标题：</th>
							<td>测试字段权限</td>
						</tr>
						<tr>
							<th>环节名称：</th>
							<td>task2</td>
						</tr>
						<tr>
							<th>上一环节处理人：</th>
							<td>马亚伟</td>
						</tr>
						<tr>
							<th>流程创建人：</th>
							<td>马亚伟</td>
						</tr>
						<tr>
							<th>任务接收时间：</th>
							<td>2018-07-10 18:35:17</td>
						</tr>
						<tr>
							<th colspan="2">
								<div class="layui-progress layui-progress-big"
									lay-filter="progressBar" lay-showpercent="yes"
									style="position: relative;">
									<div class="layui-progress-bar" lay-percent="0%"
										style="background-color: red; width: 75%;"></div>
									<span class="progress_time" style="right: 1%;">审批剩余时间6小时</span>
								</div>
							</th>
						</tr>
					</table>
					<div class="task_status">
						<span>待处理</span>
					</div>
				</li>
				<li>
					<table>
						<tr>
							<th>流程标题：</th>
							<td>测试字段权限</td>
						</tr>
						<tr>
							<th>环节名称：</th>
							<td>task2</td>
						</tr>
						<tr>
							<th>上一环节处理人：</th>
							<td>马亚伟</td>
						</tr>
						<tr>
							<th>流程创建人：</th>
							<td>马亚伟</td>
						</tr>
						<tr>
							<th>任务接收时间：</th>
							<td>2018-07-10 18:35:17</td>
						</tr>
						<tr>
							<th colspan="2">
								<div class="layui-progress layui-progress-big"
									lay-filter="progressBar" lay-showpercent="yes"
									style="position: relative;">
									<div class="layui-progress-bar" lay-percent="0%"
										style="background-color: red; width: 75%;"></div>
									<span class="progress_time" style="right: 1%;">审批剩余时间6小时</span>
								</div>
							</th>
						</tr>
					</table>
					<div class="is_agent">
						<span>代</span>
					</div>
					<div class="task_status">
						<span>待处理</span>
					</div>
				</li>
				<li>
					<table>
						<tr>
							<th>流程标题：</th>
							<td>测试字段权限</td>
						</tr>
						<tr>
							<th>环节名称：</th>
							<td>task2</td>
						</tr>
						<tr>
							<th>上一环节处理人：</th>
							<td>马亚伟</td>
						</tr>
						<tr>
							<th>流程创建人：</th>
							<td>马亚伟</td>
						</tr>
						<tr>
							<th>任务接收时间：</th>
							<td>2018-07-10 18:35:17</td>
						</tr>
						<tr>
							<th colspan="2">
								<div class="layui-progress layui-progress-big"
									lay-filter="progressBar" lay-showpercent="yes"
									style="position: relative;">
									<div class="layui-progress-bar" lay-percent="0%"
										style="background-color: red; width: 75%;"></div>
									<span class="progress_time" style="right: 1%;">审批剩余时间6小时</span>
								</div>
							</th>
						</tr>
					</table>
					<div class="is_agent">
						<span>代</span>
					</div>
					<div class="task_status">
						<span>等待加签结束</span>
					</div>
				</li>
				<li>
					<table>
						<tr>
							<th>流程标题：</th>
							<td>测试字段权限</td>
						</tr>
						<tr>
							<th>环节名称：</th>
							<td>task2</td>
						</tr>
						<tr>
							<th>上一环节处理人：</th>
							<td>马亚伟</td>
						</tr>
						<tr>
							<th>流程创建人：</th>
							<td>马亚伟</td>
						</tr>
						<tr>
							<th>任务接收时间：</th>
							<td>2018-07-10 18:35:17</td>
						</tr>
						<tr>
							<th colspan="2">
								<div class="layui-progress layui-progress-big"
									lay-filter="progressBar" lay-showpercent="yes"
									style="position: relative;">
									<div class="layui-progress-bar" lay-percent="0%"
										style="background-color: red; width: 75%;"></div>
									<span class="progress_time" style="right: 1%;">审批剩余时间6小时</span>
								</div>
							</th>
						</tr>
					</table>
					<div class="task_status">
						<span>待处理</span>
					</div>
				</li>
			</ul>
		</div>
		<div id="filter_div">
			<form>
			<table class="layui-form">
				<tr>
					<th>流程发起人：</th>
					<td>
						<input type="text" class="layui-input" id="process_staff" placeholder="请输入流程发起人"/>
					</td>
				</tr>
				<tr>
					<th>上一环节处理人：</th>
					<td>
						<input type="text" class="layui-input" id="last_activity_staff" placeholder="请输入上一环节处理人"/>
					</td>
				</tr>
				<tr>
					<th>流程实例标题：</th>
					<td>
						<input type="text" class="layui-input" id="process_instance_title" placeholder="请输入流程标题"/>
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
				<tr>
					<td colspan="2" id="btn_td" style="text-align: center;">
						<input type="reset" class="layui-btn filter_btn" value="重置" style="margin-right:30px"/>
						<input type="button" class="layui-btn filter_btn" value="确定" style="margin-left:30px" onclick="queryTask()"/>
					</td>
				</tr>
			</table>
			</form>
		</div>
	</div>
</body>
</html>