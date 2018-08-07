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
		<base href="<%=basePath%>" />
		<meta charset="utf-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1" />
  		<title>选择通知模板</title>
  		<%@ include file="common/common.jsp" %>
<style type="text/css">
#templateData {
	height: 300px;
	overflow-y: auto;
}

.display_container5_custom {
	display: none;
	position: fixed;
	top: 0px;
	left: 0px;
	z-index: 8;
	width: 100%;
	height: 100%;
	background: rgba(0, 0, 0, 0.3);
}

.display_content5_custom {
	overflow-y: auto;
	color: #717171;
	padding: 20px;
	width: 570px;
	height: 86%;
	background: #fff;
	position: absolute;
	margin: 1% 0 0 -306px;
	left: 50%;
	box-shadow: 0 0 10px #ccc;
}

.colorli {
	background-color: #9DA5EC;
	color: white;
}

.layui-form-label-custom {
	float: left;
	display: block;
	width: 69%;
	font-weight: 400;
	line-height: 20px;
	text-align: left;
}
</style>
</head>
	<body>
		<!-- 初始化页面 -->
		<div class="container">
			<div class="search_area">
				<input type="hidden" id="elementId" value="${ id }"/>
				<input type="hidden" id="templateType" value="${ templateType }"/>
				<div class="layui-row layui-form">
					<div class="layui-col-md1" style="float:left;width: 250px;">
						<input type="text" id="templateName" placeholder="请输入内容" autocomplete="off" class="layui-input"/>
					</div>
					<div class="layui-col-md1" style="float:left;margin-left:10px;">
						<button class="layui-btn create_btn" onclick="searchTemplateList()">查询</button>
					</div>
				</div>						
			</div>
			<div id="templateData">				
				<table class="layui-table" lay-even lay-skin="nob">
					<colgroup>
					    <col/>
					    <col/>
					    <col/>
					    <col/>
					    <col/>
					</colgroup>
					<thead>
					    <tr>
					      <th>序号</th>
					      <th>模板名称</th>
					      <th>模板类型</th>
					      <th>模板详情</th>
					      <th>创建人</th>
					    </tr> 
					</thead>
					<tbody id="tabletrDetail">

					</tbody>
				</table>
				<div id="lay_page"></div>
			</div>
			<div class="foot" style="position: absolute;bottom: 35px;right: 5px;">
				<button class="layui-btn layui-btn sure_btn" id="sure_btn">确定</button>
				<button class="layui-btn layui-btn layui-btn-primary " id="close">取消</button>
			</div>
	
		</div>
					<!-- 通知模板  -->
	<div id="notifyt-template-div" class="display_container5_custom">
		<div class="display_content5_custom" >
			<div class="top" style="height: 30px;"><label id="operation-title"></label></div>
			<div class="middle1" style="height: 71%;">
				<div class="layui-form" style="padding-top: 4%;padding-right: 4%;">
				<form id="operationTemplateForm" action="" class="layui-form">
					<input id="templateUid" name="templateUid" type="hidden">
					<div class="layui-form-item">
						<label class="layui-form-label">模板名称</label>
						<div class="layui-input-block">
							<input class="layui-input" name="templateName"
							 required lay-verify="required" placeholder="请输入" >
						</div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">模板类型</label>
						<div class="layui-input-block">
							<input class="layui-radio" lay-filter="templateTypeFilter" type="radio" name="templateType" value="MAIL_NOTIFY_TEMPLATE" checked title="邮件模板"/> 
							<input class="layui-radio" lay-filter="templateTypeFilter"  type="radio" name="templateType" value="MESSAGE_NOTIFY_TEMPLATE"  title="短信模板"/>	
						</div>
					</div>
					<div class="layui-form-item"  id="templateSubjectDiv">
						<label class="layui-form-label">邮件主题</label>
						<div class="layui-input-block">
							<textarea id="templateSubject" name="templateSubject" lay-verify="required"
							   		 required class="layui-textarea" style="width: 100%;height:100px;">
							
							</textarea>
						</div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">主体内容</label>
						<div class="layui-input-block">
							<textarea id="templateContent" name="templateContent" lay-verify="required"
							   		 required class="layui-textarea" style="width: 100%;height:100px;">
							
							</textarea>
						</div>
					</div>
					</form>
				</div>
			</div>
			<div class="foot">
				<label class="layui-form-label-custom" style="color: red;">注意：
				{proName}即流程名,{insTitle}即流程实例标题,{proNo}即流程编号,{hideUrl}即办理任务隐藏的请求路径,
				{showUrl}即办理任务显示的请求路径,{fieldName}中fieldName可以为表单字段的字段名
				</label>
				<div class="layui-input-block" style="position: relative;">
				<button id="submitOperation" type="button" onclick="submitOperation();"
					class="layui-btn layui-btn sure_btn">确定</button>
				<button class="layui-btn layui-btn layui-btn-primary cancel_btn"
						onclick="$('#notifyt-template-div').hide();">取消</button>
				</div>
				</div>
		</div>
	</div>
	<script type="text/javascript">
	layui.use('form',function(){
		var form = layui.form;
		form.on('radio(templateTypeFilter)', function(data) {
			if (data.value == "MAIL_NOTIFY_TEMPLATE") {
				$('#templateSubjectDiv').show();
			} else {
				$('#templateSubjectDiv').hide();
			}
		});
	});
	//为翻页提供支持
	var pageConfig = {
		pageNum : 1,
		pageSize : 8,
		templateName: "",
		templateType : $("#templateType").val(),
		total : 0
	}

	$(document).ready(function() {
		// 加载数据
		pageNotifyTemplate();

	})

		//分页
		function doPage() {
			layui.use([ 'laypage', 'layer' ], function() {
				var laypage = layui.laypage, layer = layui.layer;
				//完整功能
				laypage.render({
							elem : 'lay_page',
							curr : pageConfig.pageNum,
							count : pageConfig.total,
							limit : pageConfig.pageSize,
							layout : [ 'count', 'prev', 'page', 'next',
									'limit', 'skip' ],
							jump : function(obj, first) {
								// obj包含了当前分页的所有参数  
								pageConfig.pageNum = obj.curr;
								pageConfig.pageSize = obj.limit;
								if (!first) {
									pageNotifyTemplate();
								}
							}
						});
			});
		}
		
		$(function() {
			$("#sure_btn").click(function() {
						var inputObj = $("[name='template_check']:checked");
						var templateUid = inputObj.val();
						var elementId = $("#elementId").val();
						var templateName = inputObj.parent().next().text().trim();
						window.parent.document.getElementById(elementId).value = templateUid;
						window.parent.document.getElementById(elementId+"_view").value = templateName;
						$('#close').click();
					});
		});
		//复选框只能选择一个
		function onSelOne(obj) {
			$('input[name="template_check"]').not($(obj))
					.prop("checked", false);
		}

		function tableDetail(pageInfo) {
			pageConfig.pageNum = pageInfo.pageNum;
			pageConfig.pageSize = pageInfo.pageSize;
			pageConfig.total = pageInfo.total;
			doPage();
			// 渲染数据
			$("#tabletrDetail").empty();

			var list = pageInfo.list;
			for (var i = 0; i < list.length; i++) {
				var data = list[i];
				var notifyTemplateType = "";
				if (data.templateType == "MAIL_NOTIFY_TEMPLATE") {
					notifyTemplateType = "邮件模板";
				} else if (data.templateType == "MESSAGE_NOTIFY_TEMPLATE") {
					notifyTemplateType = "短信模板";
				}
				var str = '<tr><td>'
						+ '<input type="checkbox" name="template_check" value="'
						+ data.templateUid
						+ '" onclick="onSelOne(this)"/>'
						+ (i + 1)
						+ '</td>'
						+ '<td>'
						+ data.templateName
						+ '</td><td data-templatetype="'+data.templateType+'">'
						+ notifyTemplateType
						+ '</td>'
						+ '<td data-templatesubject ="'+ data.templateSubject +'"><pre style="display: none;">'
						+ data.templateContent
						+ '</pre>'
						+ '<a style="cursor:pointer" onclick="showNotifyTemplate(this);">点击查看</a></td>'
						+ '<td>' + data.userName + '</td>';
				$("#tabletrDetail").append(str);
			}
		}
		//查询所有的通知模板信息
		function pageNotifyTemplate() {

			$.ajax({
				url : '<%=request.getContextPath()%>/dhNotifyTemplate/pageNotifyTemplateList',
				method : "post",
				async : false,
				dataType : 'json',
				data : {
					pageNum : pageConfig.pageNum,
					pageSize : pageConfig.pageSize,
					templateName: pageConfig.templateName,
					templateType : pageConfig.templateType
				},
				beforeSend: function(){
					layer.load(1);
				},
				success : function(result){
					if (result.status == 0) {
						tableDetail(result.data);
					}
					layer.closeAll("loading");
				},error : function(){
					layer.closeAll("loading");
				}
			})
		}
		//模糊查询
		function searchTemplateList() {
			pageConfig.templateName = $("#templateName").val().trim();
			pageNotifyTemplate();
		}
		//查看通知模板
		function showNotifyTemplate(a) {
			$("#operation-title").text("查看通知模板");
			$("#submitOperation").css("display", "none");
			$(".cancel_btn").text("关闭");
			var tr = $(a).parent().parent();
			$("input[name='templateName']").val(tr.find("td").eq(1).text());
			$("#templateContent").val(tr.find("td").eq(3).find("pre").text());
			var templateType = tr.find("td").eq(2).data("templatetype");
			$("#templateSubject").val($(a).parent().data("templatesubject"));
			$("input[name='templateType'][value='" + templateType + "']").prop(
					"checked", true);
			layui.form.render("radio");
			layui.event.call($("#templateSubjectDiv"), 'form',
					'radio(templateTypeFilter)', {
						"value" : templateType
					});
			$("#notifyt-template-div").show();
		}
	</script>
	</body>
</html>
