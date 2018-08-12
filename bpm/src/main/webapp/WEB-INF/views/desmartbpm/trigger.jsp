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
		.choose_interface{position:absolute;right:10px;top:8px;cursor:pointer;z-index:1;}
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
					<button class="layui-btn create_btn" id="addTriBtn">添加</button>
					<button class="layui-btn create_btn" id="importBtn">导入</button>
				</div>
			</div>
		</div>
		<div>
			<table class="layui-table backlog_table " lay-even lay-skin="nob">
				<colgroup>
					<col width="3%">
					<col width="13%">
					<col width="10%">
					<col width="7%">
					<col width="25%">
					<col width="15%">
					<col width="7%">
					<col width="20%">
					<col width="7%">
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
		
		<div class="display_container" id="tri_container">
			<div class="display_content3" style="width: 800px;height:466px;">
				<div class="top" id="triTopDiv">新增触发器</div>
				<form id="form1" class="layui-form" action="" >
					<div class="layui-row">
						<div class="layui-col-md5">
							<div class="layui-form-item">
								<label class="layui-form-label">触发器类型*:</label>
								<div class="layui-input-inline" >
									<select id="triType" name="triType" lay-filter="triType">
										<option value="">请选择触发器类型</option>
										<option value="javaclass" selected>javaclass</option>
										<option value="interface">interface</option>
										<option value="chooseUser">chooseUser</option>
										<option value="validate">validate</option>
										<option value="sql">sql</option>
										<option value="script">script</option>
									</select>
								</div>
							</div>
						</div>
						<div class="layui-col-md7">
							<div class="layui-form-item">
								<label class="layui-form-label">触发器名称*:</label>
								<div class="layui-input-inline" style="width: 70%;">
									<input type="text" id="triTitle" name="triTitle" lay-verify="triTitle" autocomplete="off" class="layui-input">
								</div>
							</div>
						</div>
					</div>
					<div class="layui-row triWebbot" id="webbotRow">
						<div class="layui-col-md12">
							<!-- 填写java反射类名-->
							<div class="layui-form-item ">
								<label class="layui-form-label">触发器执行命令:</label>
								<div class="layui-input-inline" style="width: 70%;">
									<input id="triWebbot" name="triWebbot" type="text" lay-verify="triWebbot" autocomplete="off" class="layui-input" placeholder="若触发器为javaclass类型，请输入java反射类"
									/>
								</div>
							</div>
						</div>
					</div>
					<div class="layui-row triInterface" id="interfaceRow" style="display: none;">
						<div class="layui-col-md12">
							<!-- 选择接口-->
							<div class="layui-form-item " >
								<label class="layui-form-label">请选择接口*:</label>
								<div class="layui-input-inline" style="width: 70%;" >
									<input type="hidden" name="addInterface" id="addInterface" value="">
									<input type="text" name="addInterface_view" id="addInterface_view" class="layui-input" disabled="disabled">
									<i class="layui-icon choose_interface" id="chooseInterface" title="选择接口">&#xe615;</i>
								</div>
							</div>
						</div>
					</div>
					<div class="layui-row">
						<div class="layui-col-md12">
							<label class="layui-form-label">触发器参数:</label>
							<div class="layui-input-block">
								<textarea id="triParam" name="triParam" lay-verify="triParam" autocomplete="off" class="layui-textarea"></textarea>
							</div>
						</div>
					</div>
					<div class="layui-row" style="margin-top: 20px;">
						<div class="layui-col-md12">
							<label class="layui-form-label">触发器描述:</label>
							<div class="layui-input-block">
								<textarea placeholder="" class="layui-textarea" id="triDescription" name="triDescription"></textarea>
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
		
		

		<div class="display_container3" >
			<div class="display_content3" style="width:800px;height:580px;">
				<div class="search_area">
					<input type="hidden" id="interfaceId" /> <input type="hidden"
						id="interfaceType" />
					<div class="layui-row layui-form">
						<div class="layui-col-md1" style="float: left; width: 250px;">
							<input type="text" id="interfaceName" placeholder="请输入接口名称"
								autocomplete="off" class="layui-input" />
						</div>
						<div class="layui-col-md1" style="float: left; margin-left: 10px;">
							<button class="layui-btn create_btn"
								onclick="searchInterfaceList()">查询</button>
						</div>
					</div>
				</div>
				<div id="interfaceData">
					<table class="layui-table" lay-even lay-skin="nob">
						<colgroup>
							<col />
							<col />
							<col />
							<col />
							<col />
						</colgroup>
						<thead>
							<tr>
								<th>序号</th>
								<th>接口名称</th>
								<th>接口标签</th>
								<th>接口类型</th>
								<th>接口访问地址</th>
							</tr>
						</thead>
						<tbody id="tabletrDetail">

						</tbody>
					</table>
					<div id="lay_page2"></div>
					<div class="foot">
						<button class="layui-btn layui-btn btn_addInterface" id="btn_addInterface">确定</button>
						<button class="layui-btn layui-btn layui-btn-primary" id="close">取消</button>
					</div>
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
