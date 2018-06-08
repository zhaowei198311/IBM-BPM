<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>触发器</title>
<%@include file="common/head.jsp"%>
<%@include file="common/tag.jsp"%>
<link href="<%=basePath%>/resources/desmartbpm/tree/css/demo.css"
	rel="stylesheet" media="all">
<link href="<%=basePath%>/resources/desmartbpm/css/my.css"
	rel="stylesheet" media="all" />
</head>
<body>
	<div class="layui-container" style="margin-top: 20px; width: 100%;">
		<div class="search_area">
			<div class="layui-row layui-form">
				<div class="layui-col-md2">
					<input id="triggerName_input" type="text" placeholder="触发器名称"
						class="layui-input">
				</div>
				<div class="layui-col-md2">
					<select id="triggerType_select" name="triggerType">
						<option value="">请选触发器类型</option>
						<option value="script">script</option>
						<option value="sql">sql</option>
						<option value="interface">interface</option>
						<option value="javaclass">javaclass</option>
					</select>
				</div>
				<div class="layui-col-md2" style="text-align: right; width: 180px">
					<button class="layui-btn search_btn" id="searchMeat_btn">查询</button>
					<button class="layui-btn create_btn" id="show_expose_btn">添加</button>
				</div>
			</div>
		</div>
		<div>
			<table class="layui-table backlog_table " lay-even lay-skin="nob">
				<colgroup>
					<col width="8%">
					<col width="15%">
					<col width="15%">
					<col width="9%">
					<col width="25%">
					<col width="15%">
					<col width="8%">
					<col width="8%">
					<col width="8%">
				</colgroup>
				<thead>
					<tr>
						<th>序号</th>
						<th>触发器标题</th>
						<th>触发器描述</th>
						<th>触发器类型</th>
						<th>触发器执行命令</th>
						<th>触发器参数</th>
						<th>创建人</th>
						<th>创建时间</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody id="trigger_table_tbody"></tbody>
			</table>
		</div>
		<div id="lay_page"></div>

		<div class="display_container">
			<div class="display_content3">
				<div class="top" style="color: red;">新增触发器</div>
				<label class="layui-input-label" style="color: red;">带*为必填参数</label>
				<form id="form1" class="layui-form" action=""
					style="margin-top: 30px;">
					<div class="layui-form-item">
						<div class="layui-row">
							<div class="layui-col-md6">
								<div class="layui-inline">
									<label class="layui-form-label" style="width: 100px">触发器名称*:</label>
									<div class="layui-input-inline">
										<input type="text" id="triTitle" name="triTitle"
											lay-verify="triTitle" autocomplete="off" class="layui-input">
									</div>
								</div>
							</div>
							<div class="layui-col-md6">
								<label class="layui-form-label" style="width: 100px">触发器类型*:</label>
								<div class="layui-input-inline">
									<select id="triType" name="triType" lay-filter="triType">
										<option value="">请选择触发器类型</option>
										<option value="script">script</option>
										<option value="sql">sql</option>
										<option value="interface">interface</option>
										<option value="javaclass">javaclass</option>
									</select>
								</div>
							</div>
						</div>
					</div>
					<div class="layui-form-item">
						<div class="layui-row">
							<div class="layui-col-md6">
								<div class="layui-inline">
									<label class="layui-form-label" style="width: 100px">触发器描述:</label>
									<div class="layui-input-inline">
										<input width="80%" type="text" id="triDescription" name="triDescription"
											lay-verify="triTitle" autocomplete="off" class="layui-input">
									</div>
								</div>
							</div>
							<div class="layui-col-md6">
								<label class="layui-form-label" style="width: 100px">触发器参数:</label>
								<div class="layui-input-inline">
									<input type="text" id="triParam" name="triParam"
										lay-verify="triParam" autocomplete="off" class="layui-input">
								</div>
							</div>
						</div>
					</div>
					<div class="layui-form-item">
						<div class="layui-form-item triWebbot">
							<div class="layui-inline">
								<label class="layui-form-label" style="width: 100px">触发器执行命令*:</label>
								<div class="layui-input-inline" id="div_triWebbot">
									<input id="triWebbot" name="triWebbot" type="text"
										lay-verify="triWebbot" autocomplete="off"
										class="layui-input paraDescription" />
								</div>
							</div>
						</div>
					</div>
					<div class="layui-form-item ">
						<div class="layui-form-item triInterface" style="display:none;">
							<div class="layui-inline">
								<label class="layui-form-label" style="width: 100px">请选择接口*:</label>
								<div class="layui-input-inline">
									<select id="triWebbotType" name="triWebbotType"
										lay-filter="triWebbotType">
									</select>
								</div>
							</div>
						</div>
					</div>
				</form>
				<div class="foot">
					<button id="sure_btn" class="layui-btn layui-btn sure_btn">确定</button>
					<button id="cancel_btn" class="layui-btn layui-btn cancel_btn">取消</button>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
<script src="<%=basePath%>/resources/desmartbpm/js/layui.all.js"></script>
<script src="<%=basePath%>/resources/desmartbpm/lay/modules/laypage.js"></script>
<script src="<%=basePath%>/resources/desmartbpm/js/my/trigger.js"></script>