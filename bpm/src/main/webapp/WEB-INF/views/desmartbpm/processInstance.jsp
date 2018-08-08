<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>流程实例管理</title>
<%@include file="common/head.jsp" %>
<%@include file="common/tag.jsp" %>
<link rel="stylesheet" href="<%=basePath%>/resources/desmartbpm/css/my.css" media="all">
<link href="<%=basePath%>/resources/desmartbpm/css/layui.css" rel="stylesheet"/>
<link rel="stylesheet" href="<%=basePath%>/resources/desmartbpm/css/modules/laydate/default/laydate.css" />
<script type="text/javascript" src="<%=basePath%>/resources/desmartbpm/js/jquery-3.3.1.js" ></script>
<script type="text/javascript" src="<%=basePath%>/resources/desmartbpm/js/layui.all.js"></script>
<script type="text/javascript" src="<%=basePath%>/resources/desmartbpm/js/common.js"></script>
<script type="text/javascript" src="<%=basePath%>/resources/desmartbpm/js/my/processInstance.js"></script>
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
    height: 400px;
    background: #fff;
    position: absolute;
    margin: 80px 0 0 -306px;
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
    padding: 9px 1%;
    width: 50%;
    font-weight: 400;
    line-height: 20px;
    text-align: right;
}
.layui-input-block-custom {
    margin-left: 52%;
    min-height: 36px;
}
</style>

</head>
<body>
<div class="layui-container" style="margin-top:20px;width:100%;">  
		  	<div class="layui-row">
			    <div class="layui-col-xs12">
					<div class="search_area">
						<div class="layui-row layui-form">
							<div class="layui-col-xs12" style="text-align:right;">
								<div class="layui-col-md2">
								<label class="layui-form-label-custom">流程实例状态</label>
								<div class="layui-input-block-custom">
									<select id="insStatusId" class="layui-input-block group_select"
										name="group" lay-verify="required">
										<option value="">全部</option>
										<option value="1">运转中</option>
										<option value="2">完成</option>
										<option value="3">失败</option>
										<option value="4">终止</option>
										<option value="5">未启动</option>
										<option value="6">暂停</option>
									</select>
								</div>
								</div>
								<div class="layui-col-xs1">
								<input type="text" placeholder="发起人姓名"  class="layui-input" id="process-initUserFullname-search">
								</div>
								<div class="layui-col-xs2">
								<input type="text" placeholder="流程实例标题"  class="layui-input" id="process-insTitle-search">
								</div>
								<div class="layui-col-xs1" style="width: 4%;">
								<button class="layui-btn layui-btn-sm" onclick="search();">查询</button>
								</div>
								<button class="layui-btn layui-btn-sm" onclick="showProcessInsMap();">查看流程图</button>
						        <button class="layui-btn layui-btn-sm" onclick="pauseProcessIns();">暂停流程实例</button>
						        <button class="layui-btn layui-btn-sm" onclick="resumeProcessIns();">恢复流程实例</button>
						        <button class="layui-btn layui-btn-sm" onclick="terminateProcessIns();">终止流程实例</button>
						        <!-- <button class="layui-btn layui-btn-sm">重试流程实例</button> -->
						        <button class="layui-btn layui-btn-sm" onclick="toTrunOffProcessIns();">撤转流程实例</button>
						        <!-- <button class="layui-btn layui-btn-sm">查找流程实例</button> -->
						        <button class="layui-btn layui-btn-sm" onclick="getProcessInsInfo()">获取实例信息</button>
							</div>
						</div>
					</div>
					<div style="width:100%;overflow-x:auto;">
						<div style="width: 100%;">		
						<table class="layui-table backlog_table">
							<colgroup>
								<col style="min-width:5%">
							    <col style="min-width:10%">
							    <col style="min-width:5%">
							    <col style="min-width:5%">
							    <col style="min-width:10%">
							    <col style="min-width:5%">
							    <col style="min-width:30%">
							    <col style="min-width:30%">
							</colgroup>
							<thead>
							    <tr>
							      <th><!-- <input type="checkbox" id="checked-All-ins" title='全选' lay-skin="primary">  -->序号</th>
							      <th>流程名称</th>
							      <th>实例ID</th>
							      <th>状态</th>
							      <th>流程实例标题</th>
							      <th>发起人</th>
							      <th>当前活动任务</th>
							      <th>任务处理人</th>
							    </tr> 
							</thead>
							<tbody id="proMet_table_tbody" >
							    
							</tbody>
						</table>	
						</div>					
					</div>
					<div id="lay_page"></div>
			    </div>
		  	</div>
		</div>
		<!-- 编辑流程实例信息 -->
	<div id="processIns-text-div" class="display_container5">
		<div class="display_content5" style="height: 430px;">
			<div class="top">编辑流程实例信息</div>
			<div class="middle1" style="height: 320px;">
				<textarea id="processIns-text-content" style="height: 99.4%;width: 99.6%;">
					
				</textarea>
			</div>
			<div class="foot" style="padding-top: 1.4%;">
				<button class="layui-btn layui-btn layui-btn-primary"
						onclick="saveProcessInsData();">保存</button>
				<button class="layui-btn layui-btn layui-btn-primary"
						onclick="$('#processIns-text-div').hide();">关闭</button>
			</div>
		</div>
	</div>
	<!-- 流程撤转 -->
	<div id="processIns-trun-off-div" class="display_container5_custom">
		<div class="display_content5_custom" style="height: 430px;">
			<div class="top">流程撤转</div>
			<div class="middle1" style="height: 320px;">
				<div class="layui-form" style="padding-top: 4%;padding-right: 4%;">
					<div class="layui-form-item">
						<label class="layui-form-label">活动的任务</label>
						<div class="layui-input-block">
							<select id="activity_task" name="activity_task" lay-verify="required">
									
							</select>
						</div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">目标环节</label>
						<div class="layui-input-block">
							<input type="hidden" name="trunOffActivities"
							 id="trunOffActivities" /> 
							 <input type="text" placeholder="请选择要跳转至的环节"
							 name="trunOffActivities_view" id="trunOffActivities_view"
							 value="" autocomplete="off" class="layui-input" disabled="disabled">  
							 <i class="layui-icon choose_num" id="chooseActivity_i"
							 title="选择环节">&#xe615;</i> 
						</div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">任务处理人</label>
						<div class="layui-input-block" style="position: relative;">
							 <input type="text" name="handleUser_view" 
								 id="handleUser_view" autocomplete="off"
								 placeholder="请选择任务处理人"
								 class="layui-input" disabled="disabled"> <i
								 class="layui-icon choose_user" id="choose_handle_user"
								 title="选择处理人">&#xe612;</i>
						 </div>
						<input type="hidden" id="handleUser" name="handleUser" />
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">撤转原因</label>
						<div class="layui-input-block">
							<textarea id="trunOffCause" name="trunOffCause" style="width: 100%;height: 100px;">
							
							</textarea>
						</div>
					</div>
				</div>
			</div>
			<div class="foot">
				<label class="layui-form-label-custom" style="color: red;">(注意：如果不选择目标环节,则默认为当前选中的任务更改任务处理人)</label>
				<div class="layui-input-block" style="position: relative;">
				<button type="button" onclick="showProcessInsMap();"
					class="layui-btn layui-btn sure_btn">流程图</button>
				<button type="button" onclick="trunOffProcessIns();"
					class="layui-btn layui-btn sure_btn">确定</button>
				<button class="layui-btn layui-btn layui-btn-primary cancel_btn"
						onclick="$('#processIns-trun-off-div').hide();">取消</button>
				</div>
				</div>
		</div>
	</div>
	<!-- 选择目标环节 -->
	<div class="display_container5_custom" id="choose_activity_container">
		<div class=display_content5_custom style="height: 430px;">
			<div class="top">选择目标环节</div>
			<div class="middle1" style="height: 320px;">
			<table width="100%;" height="100%;" class="layui-table">
			<colgroup>
				<col> 
				<col> 
			</colgroup>
			<thead>
				<tr><th style="text-align: center;">序号</th>
					<th style="text-align: center;">环节名称</th></tr>
			</thead>
			<tbody id="choose_activity_tbody">
			
			</tbody>
			</table>
			</div>
			<div class="foot">
				<button class="layui-btn layui-btn sure_btn"
					id="chooseActivities_sureBtn">确定</button>
				<button class="layui-btn layui-btn layui-btn-primary cancel_btn"
					onclick="$('#choose_activity_container').hide();">取消</button>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
layui.use('form', function(){
	var form = layui.form;
	form.render("select");
});
//为翻页提供支持
var pageConfig = {
	pageNum : 1,
	pageSize : 8,
	initUserFullname: "",
	insTitle : "",
	insStatusId : "",
	total : 0
}

$(document).ready(function() {
	// 加载数据
	getProcesssInstance();
	//全选
	/* $("#checked-All-ins").click(function(){ 
		var checkeNodes= $("input[type='checkbox'][name='checkProcessIns']");
	    checkeNodes.prop("checked",$(this).prop("checked")); 
	}); */
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
			insTitle : pageConfig.insTitle,
			insStatusId : pageConfig.insStatusId
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
//字符转换,截取指定字符长度
function interceptStr(str, len) {
    var reg = /[\u4e00-\u9fa5]/g,    //专业匹配中文
        slice = str.substring(0, len),
        chineseCharNum = (~~(slice.match(reg) && slice.match(reg).length)),
        realen = slice.length * 2 - chineseCharNum;
    return str.substr(0, realen) + (realen < str.length ? "..." : "");
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
		
		if(item.insStatusId == 1 ){
			item.insStatus = '运转中';
		}else if(item.insStatusId == 2){
			item.insStatus = '完成';
		}else if(item.insStatusId == 3){
			item.insStatus = '失败';
		}else if(item.insStatusId == 4){
			item.insStatus = '终止';
		}else if(item.insStatusId == 5){
			item.insStatus = '未启动';
		}else if(item.insStatusId == 6){
			item.insStatus = '暂停';
		}
		var taskTitle = '';
		if(item.taskTitle!=null && item.taskTitle!=""){
			taskTitle = interceptStr(item.taskTitle,15);
		}
		var handleUserName ='';
		if(item.handleUserName!=null && item.handleUserName!=""){
			handleUserName = interceptStr(item.handleUserName,15);
		}
		var insTitle = '';
		if(item.insTitle!=null && item.insTitle != "" ){
			insTitle = interceptStr(item.insTitle,15);
		}
		trs += '<tr>'
				+'<td>'
				+ '<input type="checkbox" data-insid ='+item.insId+' onclick="invertSelection(this)" name="checkProcessIns" value="'+item.insUid+'" lay-skin="primary">'
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
				+ '<td title=\''+item.insTitle+'\'>'
				+ insTitle
				+ '</td>' 
				+ '<td>'
				+ item.initUserFullname
				+ '</td>'
				+ '<td title=\''+item.taskTitle+'\'>'
				+taskTitle
				+ '</td>'
				+ '<td title=\''+item.handleUserName+'\'>'
				+handleUserName
				+ '</td>'					
				+ '</tr>';
	}
	$("#proMet_table_tbody").append(trs);

}
//模糊查询
function search(){
	pageConfig.initUserFullname = $("#process-initUserFullname-search").val();
	pageConfig.insTitle = $("#process-insTitle-search").val();
	pageConfig.insStatusId = $("#insStatusId").val();
	getProcesssInstance();
}

//反选
function invertSelection(a){ 
	var checkeNodes= $("input[type='checkbox'][name='checkProcessIns']"); 
	//var checkedNodes= $("input[type='checkbox'][name='checkProcessIns']:checked");
	if($(a).prop("checked")==true){
	checkeNodes.prop("checked",false);
	$(a).prop("checked",true);
	}

};
</script>
</html>