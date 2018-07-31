<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>系统日志</title>
<%@include file="common/head.jsp"%>
<%@include file="common/tag.jsp"%>
<link href="<%=basePath%>/resources/desmartbpm/tree/css/demo.css"
	rel="stylesheet" media="all">
<link href="<%=basePath%>/resources/desmartbpm/css/my.css"
	rel="stylesheet" media="all" />
	<style>
		.display_content_accessory_file {
			display: none;
			color: #717171;
			padding: 20px;
			width: 70%;
			height: 60%;
			background: #fff;
			position: fixed;
			left: 12.5%;
			top: 16%;
			box-shadow: 0 0 10px #ccc;
		}
	</style>
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
						<option value="chooseUser">chooseUser</option>
						<option value="validate">validate</option>
					</select>
				</div>
				<div class="layui-col-md8" style="text-align: left; padding-left:15px;">
					<button class="layui-btn search_btn" id="searchMeat_btn">查询</button>
					<button class="layui-btn create_btn" id="show_expose_btn">添加</button>
					<button class="layui-btn create_btn" id="importBtn">导入</button>
				</div>
			</div>
		</div>
		<div>
			<table class="layui-table backlog_table " lay-even lay-skin="nob">
				<colgroup>
					<col width="4%">
					<col width="8%">
					<col width="10%">
					<col width="10%">
					<col width="8%">
					<col width="15%">
					<col width="15%">
					<col width="10%">
					<col width="10%">
					<col width="10%">
				</colgroup>
				<thead>
					<tr>
						<th>序号</th>
						<th>日志类型</th>
						<th>操作人工号</th>
						<th>操作人姓名</th>
						<th>主机ip</th>
						<th>请求参数</th>
						<th>响应参数</th>
						<th>接口描述</th>
						<th>请求路径</th>
						<th>访问时间</th>
						<!-- <th>附加备注</th> -->
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
								<label class="layui-form-label" style="width: 100px">触发器类型*:</label>
								<div class="layui-input-inline">
									<select id="triType" name="triType" lay-filter="triType">
										<option value="">请选择触发器类型</option>
										<option value="script">script</option>
										<option value="sql">sql</option>
										<option value="interface">interface</option>
										<option value="javaclass">javaclass</option>
										<option value="chooseUser">chooseUser</option>
										<option value="validate">validate</option>
									</select>
								</div>
							</div>
							<div class="layui-col-md6">
								<div class="layui-inline">
									<label class="layui-form-label" style="width: 100px">触发器名称*:</label>
									<div class="layui-input-inline">
										<input type="text" id="triTitle" name="triTitle"
											lay-verify="triTitle" autocomplete="off" class="layui-input">
									</div>
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
								<label class="layui-form-label" style="width: 100px">触发器执行命令:</label>
								<div class="layui-input-inline" id="div_triWebbot">
									<input id="triWebbot" name="triWebbot" type="text"
										lay-verify="triWebbot" autocomplete="off"
										class="layui-input paraDescription" style="width: 568px" placeholder="若触发器为javaclass类型，请输入java反射类"/>
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
		
		
		<div class="display_container2">
		<input id="triUid" style="display: none;">
			<div class="display_content3">
				<div class="top" style="color: red;">修改触发器</div>
				<form id="form2" class="layui-form" action=""
					style="margin-top: 30px;">
					<div class="layui-form-item">
						<div class="layui-row">
							<div class="layui-col-md6">
								<label class="layui-form-label" style="width: 100px">触发器类型*:</label>
								<div class="layui-input-inline">
									<select id="triType2" name="triType2" lay-filter="triType2">
										<option value="script">script</option>
										<option value="sql">sql</option>
										<option value="interface">interface</option>
										<option value="javaclass">javaclass</option>
										<option value="chooseUser">chooseUser</option>
										<option value="validate">validate</option>
									</select>
								</div>
							</div>
							<div class="layui-col-md6">
								<div class="layui-inline">
									<label class="layui-form-label" style="width: 100px">触发器名称*:</label>
									<div class="layui-input-inline">
										<input type="text" id="triTitle2" name="triTitle2"
											lay-verify="triTitle2" autocomplete="off" class="layui-input">
									</div>
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
										<input width="80%" type="text" id="triDescription2" name="triDescription2"
											lay-verify="triDescription2" autocomplete="off" class="layui-input">
									</div>
								</div>
							</div>
							<div class="layui-col-md6">
								<label class="layui-form-label" style="width: 100px">触发器参数:</label>
								<div class="layui-input-inline">
									<input type="text" id="triParam2" name="triParam2"
										lay-verify="triParam2" autocomplete="off" class="layui-input">
								</div>
							</div>
						</div>
					</div>
					<div class="layui-form-item">
						<div class="layui-form-item triWebbot">
							<div class="layui-inline">
								<label class="layui-form-label" style="width: 100px">触发器执行命令:</label>
								<div class="layui-input-inline" id="div_triWebbot">
									<input id="triWebbot2" name="triWebbot2" type="text"
										lay-verify="triWebbot2" autocomplete="off"
										class="layui-input trigerWebbot" style="width: 568px"/>
								</div>
							</div>
						</div>
					</div>
					<div class="layui-form-item ">
						<div class="layui-form-item triInterface" style="display:none;">
							<div class="layui-inline">
								<label class="layui-form-label" style="width: 100px">请选择接口*:</label>
								<div class="layui-input-inline">
									<select id="triWebbotType2" class="trigerWebbot" name="triWebbotType2"
										lay-filter="triWebbotType2">
									</select>
								</div>
							</div>
						</div>
					</div>
				</form>
				<div class="foot">
					<button id="update_btn" class="layui-btn layui-btn update_btn">确定</button>
					<button id="cancel_btn" class="layui-btn layui-btn cancel_btn">取消</button>
				</div>
			</div>
		</div>
		<!-- 批量导入触发器 -->
		<div class="display_content_accessory_file" id="upload_file_modal">
			<div class="top">文件上传</div>
			<div class="upload_overflow_middle">
				<div class="layui-upload-drag" style="width: 94.5%;">
					<i class="layui-icon"></i>
					<p>点击上传，或将文件拖拽到此处</p>
				</div>
				<div class="layui-upload">
					<div class="layui-upload-list">
						<table class="layui-table">
							<colgroup>
								<col width="5%">
								<col width="20%">
								<col width="10%">
								<col width="20%">
							</colgroup>
							<thead>
							<tr>
								<th>文件名</th>
								<th>大小</th>
								<!-- <th>文件标题</th>
                                        <th>文件标签</th>
                                        <th>文件说明</th> -->
								<th>状态</th>
								<th>操作</th>
							</tr>
							</thead>
							<tbody class="fileList"></tbody>
						</table>
					</div>
				</div>
			</div>
			<div class="foot_accessory_file">
				<button type="button" class="layui-btn listAction">开始上传</button>
				<button class="layui-btn layui-btn layui-btn-primary cancel_btn" onclick="cancelClick(this)">关闭</button>
			</div>
		</div>
	</div>
<script src="<%=basePath%>/resources/desmartbpm/js/layui.all.js"></script>
<script src="<%=basePath%>/resources/desmartbpm/lay/modules/laypage.js"></script>
<script src="<%=basePath%>/resources/desmartbpm/js/my/trigger.js"></script>
</body>
</html>