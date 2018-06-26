<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>流程实例管理</title>
<%@include file="common/head.jsp" %>
<%@include file="common/tag.jsp" %>
<link href="<%=basePath%>/resources/desmartbpm/css/layui.css" rel="stylesheet"/>
<link rel="stylesheet" href="<%=basePath%>/resources/desmartbpm/css/modules/laydate/default/laydate.css" />
<script type="text/javascript" src="<%=basePath%>/resources/desmartbpm/js/jquery-3.3.1.js" ></script>
<script type="text/javascript" src="<%=basePath%>/resources/desmartbpm/js/layui.all.js"></script>
<script type="text/javascript" src="<%=basePath%>/resources/desmartbpm/js/common.js"></script>
<script type="text/javascript" src="<%=basePath%>/resources/desmartbpm/js/my/processInstance.js"></script>
</head>
<body>
<div class="layui-container" style="margin-top:20px;width:100%;">  
		  	<div class="layui-row">
			    <div class="layui-col-xs12">
					<div class="search_area">
						<div class="layui-row layui-form">
							<div class="layui-col-xs12" style="text-align:right;">
								<div class="layui-col-xs2">
								<input type="text" placeholder="流程实例标题"  class="layui-input" id="process-insTitle-search">
								</div>
								<div class="layui-col-xs2">
								<input type="text" placeholder="发起人姓名"  class="layui-input" id="process-initUserFullname-search">
								</div>
								<button class="layui-btn layui-btn-sm" onclick="search();">查询</button>
						        <button class="layui-btn layui-btn-sm" onclick="pauseProcessIns();">暂停流程实例</button>
						        <button class="layui-btn layui-btn-sm" onclick="resumeProcessIns();">恢复流程实例</button>
						        <button class="layui-btn layui-btn-sm" onclick="terminateProcessIns();">终止流程实例</button>
						        <button class="layui-btn layui-btn-sm">重试流程实例</button>
						        <button class="layui-btn layui-btn-sm">重新路由实例</button>
						        <button class="layui-btn layui-btn-sm">查找流程实例</button>
						        <button class="layui-btn layui-btn-sm">获取实例信息</button>
							</div>
						</div>
					</div>
					<div style="width:100%;overflow-x:auto;">				
						<table class="layui-table backlog_table">
							<colgroup>
								<col width="7%">
							    <col>
							    <col>
							    <col>
							    <col>
							    <col>
							    <col>
							</colgroup>
							<thead>
							    <tr>
							      <th><input type="checkbox" id="checked-All-ins" title='全选' lay-skin="primary"> 序号</th>
							      <th>流程名称</th>
							      <th>实例ID</th>
							      <th>实例状态</th>
							      <th>流程实例标题</th>
							      <th>流程发起人</th>
							      <th>当前活动任务</th>
							      <th>任务处理人</th>
							    </tr> 
							</thead>
							<tbody id="proMet_table_tbody" >
							    
							</tbody>
						</table>				
					</div>
					<div id="lay_page"></div>
			    </div>
		  	</div>
		</div>
</body>
<script type="text/javascript">
//为翻页提供支持
var pageConfig = {
	pageNum : 1,
	pageSize : 8,
	initUserFullname: "",
	insTitle : "",
	total : 0
}

$(document).ready(function() {
	// 加载数据
	getProcesssInstance();
	//全选
	$("#checked-All-ins").click(function(){ 
		var checkeNodes= $("input[type='checkbox'][name='checkProcessIns']");
	    checkeNodes.prop("checked",$(this).prop("checked")); 
	});
})

// 分页
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
					getProcesssInstance();
				}
			}
		});
	});
}
function getProcesssInstance(){
	$.ajax({
		url : common.getPath()+'/dhProcessInsManage/getProcesssInstance',
		type : 'post',
		dataType : 'json',
		data : {
			pageNum : pageConfig.pageNum,
			pageSize : pageConfig.pageSize,
			initUserFullname: pageConfig.initUserFullname,
			insTitle : pageConfig.insTitle
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
	$("#proMet_table_tbody").html('');
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
		
		if(item.insStatus=='1'){
			item.insStatus = '运转中';
		}else if(item.insStatus=='2'){
			item.insStatus = '完成';
		}else if(item.insStatus=='3'){
			item.insStatus = '失败';
		}else if(item.insStatus=='4'){
			item.insStatus = '终止';
		}else if(item.insStatus=='5'){
			item.insStatus = '未启动';
		}else if(item.insStatus=='6'){
			item.insStatus = '暂停';
		}
		trs += '<tr>'
				+'<td>'
				+ '<input type="checkbox" onclick="invertSelection(this)" name="checkProcessIns" value="'+item.insUid+'" lay-skin="primary">'
				+ sortNum 
				+ '</td>' 
				+ '<td>'
				+ item.proName
				+ '</td>' 
				+ '<td>'
				+ item.insId
				+ '</td>'
				+ '<td>'
				+ item.insStatus
				+ '</td>' 
				+ '<td>'
				+ item.insTitle
				+ '</td>' 
				+ '<td>'
				+ item.initUserFullname
				+ '</td>'
				+ '<td>';
				if(item.taskTitle!=null && item.taskTitle!=""){
					trs += item.taskTitle;
				}
				trs += '</td>'
				+ '<td>';
				if(item.handleUserName!=null && item.handleUserName!=""){
					trs += item.handleUserName;
				}
				trs += '</td>'					
				+ '</tr>';
	}
	$("#proMet_table_tbody").append(trs);

}
//模糊查询
function search(){
	pageConfig.initUserFullname = $("#process-initUserFullname-search").val();
	pageConfig.insTitle = $("#process-insTitle-search").val();

	getProcesssInstance();
}

//反选
function invertSelection(a){ 
	var checkeNodes= $("input[type='checkbox'][name='checkProcessIns']"); 
	var checkedNodes= $("input[type='checkbox'][name='checkProcessIns']:checked");
	if(checkedNodes.length==checkeNodes.length){
	   	  $("#checked-All-ins").prop("checked",$(a).prop("checked")); 
	   }else{ 
		  $("#checked-All-ins").prop("checked",false); 
	} 
};
</script>
</html>