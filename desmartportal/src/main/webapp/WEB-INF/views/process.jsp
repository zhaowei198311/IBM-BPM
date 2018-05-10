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
<meta charset="utf-8">
<title>发起流程</title>
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
<link href="resources/formDesign/css/bootstrap-combined.min.css"
	rel="stylesheet">
<link href="resources/formDesign/css/layoutit.css" rel="stylesheet">
<link href="resources/css/modules/laydate/default/laydate.css" rel="stylesheet">
<link rel="stylesheet" href="resources/css/layui.css" media="all">
<link rel="stylesheet" href="resources/css/admin.css" media="all">
<link rel="stylesheet" href="resources/css/my.css" />
</head>
<body>
		<div class="search_area top_btn">		
			<input id="formId" value="${formId}" style="display: none;">	
			<input id="proUid" value="${proUid}" style="display: none;">
			<input id="proAppId" value="${proAppId}" style="display: none;">
			<input id="verUid" value="${verUid}" style="display: none;">
			<span style="padding-left:10px;color:#777;font-size:18px;">门店生命周期流程</span>
			<span style="float:right;padding-right:20px;">
				<button class="layui-btn  layui-btn-sm">流程图</button><button class="layui-btn  layui-btn-sm">保存</button><button class="layui-btn layui-btn-sm" onclick="startProcess()">提交</button><button class="layui-btn layui-btn-sm">转办</button><button class="layui-btn layui-btn-sm">会签</button><button class="layui-btn layui-btn-sm">驳回</button>
				<a href="backlog.html" style="margin-left:10px;"><button class="layui-btn layui-btn-sm back_btn">退出</button></a>
			</span>
		</div>
		<div class="container">			
			<div class="content">
				<div id="formSet">
				
				</div>
			</div>
		</div>
	</body>	

</html>
<script type="text/javascript" src="resources/js/jquery-3.3.1.js"></script>
<script type="text/javascript" src="resources/js/layui.all.js"></script>
<script type="text/javascript" src="resources/js/common.js"></script>
<script type="text/javascript" src="resources/formDesign/js/my.js"></script>
<script>
			function startProcess(){
				var proUids = $("#proUid").val();
				var proAppIds =  $("#proAppId").val();
				var verUids = $("#verUid").val();
				 $.ajax({
					url : 'process/startProcess',
					type : 'POST',
					dataType : 'json',
					data : {
						proUid : proUids,
						proAppId : proAppIds,
						verUid : verUids
					},
					success : function(result){
						if (result.status == 0) {
							layer.alert('提交成功', {icon: 1});
						}
						if (result.status == 1) {
							layer.alert('提交失败', {icon: 2});
						}
					},
					error :  function(result){
						layer.alert('提交失败', {icon: 2});
					}
				}); 
			}
			
		
			function clientSideInclude(dynUid) {
				$.ajax({
					url:"formSet/getFormFileByFormUid",
					method:"post",
					async:false,
					data:{
						dynUid:dynUid//表单Id，唯一主键
					},
					success:function(result){
						if(result.status==0){
							console.log(result.data);
							$("#formSet").html(result.data);
						}
					}
				});
			}
			$(function(){
				clientSideInclude(document.getElementById('formId').value);
			})
</script>