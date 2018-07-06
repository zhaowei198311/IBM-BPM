<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>通知模板管理</title>
<%@include file="common/head.jsp" %>
<%@include file="common/tag.jsp" %>
<link rel="stylesheet" href="<%=basePath%>/resources/desmartbpm/css/my.css" media="all">
<link href="<%=basePath%>/resources/desmartbpm/css/layui.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=basePath%>/resources/desmartbpm/js/layui.all.js"></script>
<script type="text/javascript" src="<%=basePath%>/resources/desmartbpm/js/my/notifyTemplate.js"></script>
<style type="text/css">
.display_container5_custom{
    display: none;
    position: fixed;
    top: 0px;
    left: 0px;
    z-index: 8;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.3);
}
.display_content5_custom{
    overflow-y: auto;
    color: #717171;
    padding: 20px;
    width: 570px;
    height: 468px;
    background: #fff;
    position: absolute;
    margin: 60px 0 0 -306px;
    left: 50%;
    box-shadow: 0 0 10px #ccc;
}

.colorli {
	background-color: #9DA5EC;
	color: white;
}
.layui-form-label-custom{
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
<div class="layui-container" style="margin-top:20px;width:100%;">  
		  	<div class="layui-row">
			    <div class="layui-col-xs12">
					<div class="search_area">
						<div class="layui-row layui-form">
							<div class="layui-col-xs12">
								<div class="layui-col-xs2">
								<input type="text" placeholder="模板名称"  class="layui-input" id="template-name-search">
								</div>
								<div class="layui-col-xs2">
								<select id="template-type-search">
									<option value="">模板类型:全部</option>
									<option value="MAIL_NOTIFY_TEMPLATE">邮件模板</option>
									<option value="MESSAGE_NOTIFY_TEMPLATE">短信模板</option>
								</select>
							</div>
								<button style="margin-left:1%;" class="layui-btn layui-btn-sm" onclick="search();">查询</button>
								<button class="layui-btn layui-btn-sm" onclick="addNotifyTemplate();">新增模板</button>
						        <button class="layui-btn layui-btn-sm" onclick="updateNotifyTemplate();">修改模板</button>
						        <button class="layui-btn layui-btn-sm" onclick="deleteNotifyTemplate();">删除模板</button>
							</div>
						</div>
					</div>
					<div style="width:100%;overflow-x:auto;">		
						<div style="width:1500px;">	
						<table class="layui-table backlog_table" >
							<colgroup>
								<col width="5%;">
								<col>
							    <col>
							    <col>
							    <col>
							    <col>
							    <col>
							    <col>
							</colgroup>
							<thead>
							    <tr>
							      <th></th>
							      <th>模板名称</th>
							      <th>模板类型</th>
							      <th>模板内容</th>
							      <th>创建人</th>
							      <th>创建时间</th>
							      <th>修改人</th>
							      <th>修改时间</th>
							    </tr> 
							</thead>
							<tbody id="template_table_tbody" >
							    
							</tbody>
						</table>	
						</div>				
					</div>
					<div id="lay_page"></div>
			    </div>
		  	</div>
		</div>
	<!-- 通知模板  -->
	<div id="notifyt-template-div" class="display_container5_custom">
		<div class="display_content5_custom" >
			<div class="top"><label id="operation-title"></label></div>
			<div class="middle1" style="height: 356px;">
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
							<input class="layui-radio" type="radio" name="templateType" value="MAIL_NOTIFY_TEMPLATE" checked title="邮件模板"/> 
							<input class="layui-radio" type="radio" name="templateType" value="MESSAGE_NOTIFY_TEMPLATE"  title="短信模板"/>	
						</div>
					</div>
					
					<div class="layui-form-item">
						<label class="layui-form-label">模板内容</label>
						<div class="layui-input-block">
							<textarea id="templateContent" name="templateContent" lay-verify="required"
							   		 required class="layui-textarea" style="width: 100%;height:175px;">
							
							</textarea>
						</div>
					</div>
					</form>
				</div>
			</div>
			<div class="foot">
				<label class="layui-form-label-custom" style="color: red;">注意：(name)即通知的人员姓名
				,(title)即邮件标题,(proName)即流程名,(proNo)即流程编号,(approvalUrl)即办理任务的请求路径,
				[fieldName]中fieldName可以为表单字段的字段名
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
</body>
<script type="text/javascript">
//为翻页提供支持
var pageConfig = {
	pageNum : 1,
	pageSize : 8,
	templateName: "",
	templateType : "",
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
			layout : [ 'count', 'prev', 'page', 'next', 'limit', 'skip' ],
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
function pageNotifyTemplate(){
	$.ajax({
		url : common.getPath()+'/dhNotifyTemplate/pageNotifyTemplateList',
		type : 'post',
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
				drawTable(result.data);
			}
			layer.closeAll("loading");
		},error : function(){
			layer.closeAll("loading");
		}
	})
}

function drawTable(pageInfo, data) {
	pageConfig.pageNum = pageInfo.pageNum;
	pageConfig.pageSize = pageInfo.pageSize;
	pageConfig.total = pageInfo.total;
	doPage();
	// 渲染数据
	$("#template_table_tbody").html('');
	if (pageInfo.total == 0) {
		return;
	}

	var list = pageInfo.list;
	var startSort = pageInfo.startRow;//开始序号
	var trs = "";
	var type = "";
	var status = "";
	var delegateFlag= "";
	for (var i = 0; i < list.length; i++) {
		var item = list[i];
		var sortNum = startSort + i;
		var notifyTemplateType = "";
		if(item.templateType == "MAIL_NOTIFY_TEMPLATE"){
			notifyTemplateType = "邮件模板";
		}else if(item.templateType == "MESSAGE_NOTIFY_TEMPLATE"){
			notifyTemplateType = "短信模板";
		}
		trs += '<tr>'
				+'<td>'
				+ '<input type="checkbox" onclick="invertSelection(this)" name="checkNotifyTemplate" value="'+item.templateUid+'" lay-skin="primary">'
				+ sortNum 
				+ '</td>' 
				+ '<td>'
				+ item.templateName
				+ '</td>' 
				+ '<td data-templatetype="'+item.templateType+'">'
				+ notifyTemplateType
				+ '</td>'
				+ '<td><pre style="display: none;">'
				+ item.templateContent
				+ '</pre><a style="cursor:pointer" onclick="showNotifyTemplate(this);">点击查看</a></td>' 
				+ '<td>';
			trs += item.userName
				+ '</td>' 
				+ '<td>'
				+ common.dateToString(new Date(item.createTime))
				+ '</td>'
				+ '<td>';
				if(item.updateUserName!=null && item.updateUserName!=""){
					trs += item.updateUserName;
				}
				trs += '</td>'
				+ '<td>';
				if(item.updateTime!=null && item.updateTime!=""){
					trs += common.dateToString(new Date(item.updateTime));
				}
				trs += '</td>'					
				+ '</tr>';
	}
	$("#template_table_tbody").append(trs);

}
//模糊查询
function search(){
	pageConfig.templateName = $("#template-name-search").val();
	pageConfig.templateType = $("#template-type-search").val();

	pageNotifyTemplate();
}

function invertSelection(a){ 
	var checkeNodes= $("input[type='checkbox'][name='checkNotifyTemplate']"); 
	if($(a).prop("checked")==true){
	checkeNodes.prop("checked",false);
	$(a).prop("checked",true);
	}

};

</script>
</html>