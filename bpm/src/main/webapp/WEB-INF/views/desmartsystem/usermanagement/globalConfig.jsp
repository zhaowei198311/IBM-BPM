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
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
  		<title>待办任务</title>
  		<%@ include file="common/common.jsp" %>
  		<style>
 			#usersul li, #user_add li{list-style-type:none;padding-left:12px;padding-top:2px;padding-bottom:2px;border-bottom:1px solid #CCC;}
 			#usersul , #user_add{list-style-type:none;padding-left:0px;width:100%;}
 			.colorli{background-color:#9DA5EC;color: white;}
 			ul{ width:200px;}
		</style>
	</head>
	<body>
		<div class="container">
		<div class="content" style="height: 300px">
			<div class="top">全局管理配置</div>
			<form id="form1" class="layui-form" action=""
				style="margin-top: 30px;">
				<div class="layui-form-item">
					<div class="layui-inline" style="width:500px;">
						<label class="layui-form-label">管理员用户名:</label>
						<div class="layui-input-inline">
							<input type="text" id="bpmAdminName" name="bpmAdminName"
								lay-verify="bpmAdminName" autocomplete="off" class="layui-input"/>
						</div>
					</div>
					<div class="layui-inline"  style="width:500px;">
						<label class="layui-form-label">管理员密码:</label>
						<div class="layui-input-inline">
							<input type="text" id="bpmAdminPsw" name="bpmAdminPsw"
								lay-verify="bpmAdminPsw" autocomplete="off"
								class="layui-input"/>
						</div>
					</div>
				</div>
				<div class="layui-form-item">
					<div class="layui-inline"  style="width:500px;">
						<label class="layui-form-label">客户端超时时间:</label>
						<div class="layui-input-inline">
							<input type="text" id="bpmClientTimeout" name="bpmClientTimeout"
								lay-verify="bpmClientTimeout" autocomplete="off"
								class="layui-input"/>
						</div>
					</div>
					<div class="layui-inline"  style="width:500px;">
						<label class="layui-form-label">流程引擎ip端口:</label>
						<div class="layui-input-inline">
							<input type="text" id="bpmServerHost" name="bpmServerHost" lay-verify="bpmServerHost"
								autocomplete="off" class="layui-input"/>
						</div>
					</div>
				</div>
				<div class="layui-form-item">
					<div class="layui-inline"  style="width:500px;">
						<label class="layui-form-label">平台ip端口:</label>
						<div class="layui-input-inline">
							<input type="text" id="bpmformsHost" name="bpmformsHost"
								lay-verify="bpmformsHost" autocomplete="off"
								class="layui-input"/>
						</div>
					</div>
					<div class="layui-inline"  style="width:500px;">
						<label class="layui-form-label">平台上下文:</label>
						<div class="layui-input-inline">
							<input type="text" id="bpmformsWebContext" name="bpmformsWebContext"
								lay-verify="bpmformsWebContext" autocomplete="off" class="layui-input"/>
						</div>
					</div>
				</div>
				<div class="layui-form-item">
					<div class="layui-inline"  style="width:500px;">
						<label class="layui-form-label">配置名:</label>
						<div class="layui-input-inline">
							<input type="text" id="configName" name="configName"
								lay-verify="configName" autocomplete="off" class="layui-input"/>
						</div>
					</div>
					<div class="layui-inline"  style="width:500px;">
						<label class="layui-form-label">附件上传根目录:</label>
						<div class="layui-input-inline">
							<input type="text" id="sftpPath" name="sftpPath"
								lay-verify="sftpPath" autocomplete="off" class="layui-input"/>
						</div>
					</div>
				</div>
				<div class="layui-form-item">
					<div class="layui-inline" style="width:500px;">
						<label class="layui-form-label">附件上传用户名:</label>
						<div class="layui-input-inline">
							<input type="text" id="sftpUserName" name="sftpUserName"
								lay-verify="sftpUserName" autocomplete="off" class="layui-input"/>
						</div>
					</div>
					<div class="layui-inline" style="width:500px;">
						<label class="layui-form-label">附件上传根密码:</label>
						<div class="layui-input-inline">
							<input type="text" id="sftpPassword" name="sftpPassword"
								lay-verify="sftpPassword" autocomplete="off" class="layui-input"/>
						</div>
					</div>
				</div>
				<div class="layui-form-item">
					<div class="layui-inline" style="width:500px;">
						<label class="layui-form-label">附件上传ip地址:</label>
						<div class="layui-input-inline">
							<input type="text" id="sftpIp" name="sftpIp"
								lay-verify="sftpIp" autocomplete="off" class="layui-input"/>
						</div>
					</div>
					<div class="layui-inline" style="width:500px;">
						<label class="layui-form-label">附件上传端口号:</label>
						<div class="layui-input-inline">
							<input type="text" id="sftpPort" name="sftpPort"
								lay-verify="sftpPort" autocomplete="off" class="layui-input"/>
						</div>
					</div>
				</div>
				<div class="layui-form-item">
					<div class="layui-inline" style="width:500px;">
						<label class="layui-form-label">附件上传最大大小:</label>
						<div class="layui-input-inline">
							<input type="tel" id="maxFileSize" name="maxFileSize"
								lay-verify="maxFileSize" autocomplete="off" class="layui-input"/>
						</div>
					</div>
					<div class="layui-inline" style="width:500px;">
						<label class="layui-form-label">附件上传最大数量:</label>
						<div class="layui-input-inline">
							<input type="tel" id="maxFileCount" name="maxFileCount"
								lay-verify="maxFileCount" autocomplete="off" class="layui-input"/>
						</div>
					</div>
				</div>
				<div class="layui-form-item">
				<div class="layui-inline" style="width:500px;">
						<label class="layui-form-label">附件上传文件后缀:</label>
						<div class="layui-input-inline" style="width:350px;">
							<input type="text" id="fileFormat" name="fileFormat"
								lay-verify="fileFormat" autocomplete="off" class="layui-input"/>
						</div>
					</div>
				</div>
				 <input type="hidden" id="configId" />
			</form>
			<div class="foot">
				<button id="sure_btn" class="layui-btn layui-btn sure_btn" onclick="formSubmit();">确定</button>
			</div>
		</div>
	</div>
	</body>
	
	
</html>
<script type="text/javascript" src="resources/desmartsystem/js/jquery-3.3.1.js" charset="utf-8"></script>
<script type="text/javascript" src="resources/desmartsystem/js/layui.all.js" charset="utf-8"></script>
	<script>
	// 为翻页提供支持
	var pageConfig = {
		pageNum : 1,
		pageSize : 10,
		total : 0
	}

	layui.use([ 'laypage', 'layer' ], function() {
		var laypage = layui.laypage, layer = layui.layer;
		//完整功能
		laypage.render({
			elem : 'lay_page',
			count : 50,
			limit : 10,
			layout : [ 'count', 'prev', 'page', 'next', 'limit', 'skip' ],
			jump : function(obj) {
				console.log(obj)
			}
		});
	});
	
	
	$(document).ready(function() {
		// 加载数据
		  getGlobalConfigInfo();
		
		$("input[type='tel']").number();
	});
	
	function getGlobalConfigInfo() {
		$.ajax({
			url : '<%=request.getContextPath()%>/globalConfig/allGlobalConfig',
			type : 'POST',
			dataType : 'json',
			data : {
				pageNum : pageConfig.pageNum,
				pageSize : pageConfig.pageSize,
			},
			success : function(result) {
				drawTable(result.data)
			}
		})
	}
	
	function drawTable(pageInfo, data){
		var list = pageInfo.list;
		var meta = list[0];
		var configId = meta.configId;  // 涓婚敭
		var configName = meta.configName;  // 閰嶇疆鍚?
		var bpmServerHost = meta.bpmServerHost;  // bpm寮曟搸host
		var bpmAdminName = meta.bpmAdminName;   // 绠＄悊鍛樿处鍙?
		var bpmAdminPsw = meta.bpmAdminPsw;    // 绠＄悊鍛樺瘑鐮?
		var configStatus = meta.configStatus;   // 閰嶇疆鐘舵?? on 鍚敤  off 鍋滅敤
		var gmtTimeZone = meta.gmtTimeZone;   // 鏃跺尯
		var bpmformsHost = meta.bpmformsHost;  // 骞冲彴host
		var bpmClientTimeout = meta.bpmClientTimeout;  // 瓒呮椂鏃堕棿
		var bpmformsWebContext = meta.bpmformsWebContext; // 骞冲彴搴旂敤涓婁笅鏂?
		var httpMaxConnection = meta.httpMaxConnection; // 鏈?澶ttp杩炴帴
		var preRouteMaxConnection = meta.preRouteMaxConnection;
		var schedulerHost = meta.schedulerHost;  // 瀹氭椂浠诲姟绠＄悊host
		var bpmAdminEmail = meta.bpmAdminEmail;  // 绠＄悊鍛橀偖浠跺湴鍧?
		var bpmApiHost = meta.bpmApiHost;   // 骞冲彴api鍦板潃
		var sftpPath = meta.sftpPath;   // 骞冲彴api鍦板潃
		var sftpUserName = meta.sftpUserName;   // 骞冲彴api鍦板潃
		var sftpPassword = meta.sftpPassword;   // 骞冲彴api鍦板潃
		var sftpIp = meta.sftpIp;   // 骞冲彴api鍦板潃
		var sftpPort = meta.sftpPort;
		var maxFileSize = meta.maxFileSize;
		var maxFileCount = meta.maxFileCount;
		var fileFormat = meta.fileFormat;
		$('#configId').val(configId);
		$('#bpmAdminName').val(bpmAdminName);
		$('#bpmAdminPsw').val(bpmAdminPsw);
		$('#bpmClientTimeout').val(bpmClientTimeout);
		$('#bpmServerHost').val(bpmServerHost);
		$('#bpmformsHost').val(bpmformsHost);
		$('#bpmformsWebContext').val(bpmformsWebContext);
		$('#configName').val(configName);
		$('#sftpPath').val(sftpPath);
		$('#sftpUserName').val(sftpUserName);
		$('#sftpPassword').val(sftpPassword);
		$('#sftpIp').val(sftpIp);
		$('#sftpPort').val(sftpPort);
		$('#maxFileSize').val(maxFileSize);
		$('#maxFileCount').val(maxFileCount);
		$('#fileFormat').val(fileFormat);
	}
	
	function formSubmit(){
		var bpmAdminName = $('#bpmAdminName').val();
		var bpmAdminPsw = $('#bpmAdminPsw').val();
		var bpmClientTimeout = $('#bpmClientTimeout').val();
		var bpmServerHost = $('#bpmServerHost').val();
		var bpmformsHost = $('#bpmformsHost').val();
		var bpmformsWebContext = $('#bpmformsWebContext').val();
		var configName = $('#configName').val();
		var sftpPath = $('#sftpPath').val();
		var sftpUserName = $('#sftpUserName').val();
		var sftpPassword = $('#sftpPassword').val();
		var sftpIp = $('#sftpIp').val();
		var sftpPort = $('#sftpPort').val();
		var maxFileSize = $('#maxFileSize').val();
		var maxFileCount = $('#maxFileCount').val();
		var fileFormat = $('#fileFormat').val();
		var configId = $('#configId').val();
		$.ajax({
			url : 'globalConfig/saveGlobalConfig',
			type : 'POST',
			dataType : 'text',
			data : {
				bpmAdminName : bpmAdminName,
				bpmAdminPsw : bpmAdminPsw,
				bpmClientTimeout : bpmClientTimeout,
				bpmServerHost : bpmServerHost,
				bpmformsHost : bpmformsHost,
				bpmformsWebContext : bpmformsWebContext,
				configName : configName,
				configId : configId,
				sftpPath : sftpPath,
				sftpUserName : sftpUserName,
				sftpPassword : sftpPassword,
				sftpIp : sftpIp,
				sftpPort:sftpPort,
				maxFileSize:maxFileSize,
				maxFileCount:maxFileCount,
				fileFormat:fileFormat
			},
			success : function(result) {
				var index = layer.open({
					title:'信息'
					,shade : 0.3
					,content: '保存成功！',
					end: function () {
						//window.location.href = "globalConfig/globalConfig";
						getGlobalConfigInfo();
				      }
				}); 
				layer.style(index, {
			    	zoom:1.1
			    });
			}
		})
	}
	
	jQuery.fn.number = function() {
		this.bind("keypress", function(e) {
			var code = (e.keyCode ? e.keyCode : e.which); // 兼容火狐 IE
			// 火狐下不能使用退格键
			if (e.keyCode == 0x8) {
				return;
			}
			return code >= 48 && code <= 57
		});
		this.bind("paste", function() {
			return false;
		});
	}; 
	</script>	
