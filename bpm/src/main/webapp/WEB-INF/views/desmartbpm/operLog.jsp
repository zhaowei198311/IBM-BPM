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

	</style>
</head>
<body>
	<div class="layui-container" style="margin-top: 20px; width: 100%;">
		<div class="search_area">
			<div class="layui-row layui-form">
				<div class="layui-col-md2">
					<input id="userId" type="text" placeholder="操作人工号"
						class="layui-input">
				</div>
				<div class="layui-col-md2">
					<select id="logType" name="logType">
						<option value="">请选日志类型</option>
						<option value="systemLog">系统日志</option>
						<option value="errorLog">错误日志</option>
					</select>
				</div>
				<div class="layui-col-md3">
						<input type="text" placeholder="日志创建时间" class="layui-input" id=createTime>
				</div>
				<div class="layui-col-md2" style="text-align: left; padding-left:15px;">
					<button class="layui-btn search_btn" onclick="search()">查询</button>
				</div>
			</div>
		</div>
		<div>
		<div style="width:100%;">
			 <table class="layui-table backlog_table " lay-even lay-skin="nob" style="width: 100%; table-layout: fixed;">
				<colgroup>
					<col width=3%;>
					<!-- <col width=7%;> -->
					<col width=7%;>
					<col width=12%;>
					<col width=10%;>
					<col width=15%;> 
					<col width=13%;>
					<col width=13%;>
					<col width=13%;>
					<col width=14%;>
				</colgroup>
				<thead>
					<tr>
						<th>序号</th>
						<th>日志类型</th>
						<!-- <th>操作人工号</th> -->
						<th>操作人</th>
						<th>主机ip</th>
						<th>请求参数</th>
						<th>响应参数</th>
						<th>接口描述</th>
						<th>请求路径</th>
						<th>访问时间</th>
						<!-- <th>附加备注</th> -->
					</tr>
				</thead>
				<tbody id="log_table_tbody"></tbody>
			</table>
		</div>
		</div>
		<div id="lay_page"></div>
	
	</div>
<script src="<%=basePath%>/resources/desmartbpm/js/layui.all.js"></script>
<script src="<%=basePath%>/resources/desmartbpm/lay/modules/laypage.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	// 加载数据
	pageOperLog();

})
//为翻页提供支持
var pageConfig = {
	pageNum : 1,
	pageSize : 10,
	logType: "",
	startTime: "",
	endTime: "",
	userId: "",
	total : 0
}

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
					pageOperLog();
				}
			}
		});
	});
}

function pageOperLog(){
	$.ajax({
		url : common.getPath()+'/operLog/pageOperLogList',
		type : 'post',
		dataType : 'json',
		data : {
			"pageNum" : pageConfig.pageNum,
			"pageSize" : pageConfig.pageSize,
			"userId": pageConfig.userId,
			"logType" : pageConfig.logType,
			"startTime" : pageConfig.startTime,
			"endTime" : pageConfig.endTime
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
function formatDate(date) {
	  var d = new Date(date),
	    month = '' + (d.getMonth() + 1),
	    day = '' + d.getDate(),
	    year = d.getFullYear();
	 
	  if (month.length < 2) month = '0' + month;
	  if (day.length < 2) day = '0' + day;
	 
	  return [year, month, day].join('-');
}
//模糊查询
function search(){
	pageConfig.userId = $("#userId").val();
	pageConfig.logType = $("#logType").val();
	var createTime = $("#createTime").val();
	if(createTime != null){
		var createTimeArr = createTime.split(" - ");
		if(createTimeArr.length>0){
			pageConfig.startTime = createTimeArr[0];
			pageConfig.endTime = createTimeArr[1];
		}
		
	}
	pageOperLog();
}
//字符转换,截取指定字符长度
function interceptStr(str, len) {
    var reg = /[\u4e00-\u9fa5]/g,    //专业匹配中文
        slice = str.substring(0, len),
        chineseCharNum = (~~(slice.match(reg) && slice.match(reg).length)),
        realen = slice.length * 2 - chineseCharNum;
    return str.substr(0, realen) + (realen < str.length ? "..." : "");
}
function drawTable(pageInfo){
	pageConfig.pageNum = pageInfo.pageNum;
	pageConfig.pageSize = pageInfo.pageSize;
	pageConfig.total = pageInfo.total;
	doPage();//渲染分页
	// 渲染数据
	$("#log_table_tbody").html('');
	if (pageInfo.total == 0) {
		return;
	}
	var list = pageInfo.list;
	var startSort = pageInfo.startRow;//开始序号
	var trs = "";
	for (var i = 0; i < list.length; i++) {
		var item = list[i];
		var sortNum = startSort + i;
		var logType = "";
		if(item.logType == "systemLog"){
			logType = "系统日志";
		}else if(item.logType == "errorLog"){
			logType = "错误日志";
		}
		var requestParam = "";
		if(item.requestParam!=null){
			requestParam = interceptStr(item.requestParam,10);
		}
		var responseParam ="";
		if(item.responseParam!=null && item.responseParam!=''){
			responseParam = interceptStr(item.responseParam,10);
		}
		var methodDescription ="";
		if(item.methodDescription!=null && item.methodDescription!=''){
			methodDescription = interceptStr(item.methodDescription,10);
		}
		var path ="";
		if(item.path!=null && item.path!=''){
			path = interceptStr(item.path,10);
		}
		trs += '<tr>'
				+'<td>'
				+ sortNum 
				+ '</td>' 
				+ '<td>'
				+ logType
				+ '</td>'
				/* + '<td>'
				+ item.userId
				+ '</td>' */
				+ '<td>'
				+ item.userName + '('+ item.userId + ')'
				+ '</td>' 
				+ '<td>'
				+ item.host
				+ '</td>' 
				+ '<td title=\'' + item.requestParam + '\'>'
				+ requestParam
			    +'</td>'
				+ '<td title=\'' + item.responseParam + '\'>'
				+ responseParam
				+ '</td>'
				+ '<td title=\'' + item.methodDescription + '\'>'
				+ methodDescription
			    +'</td>'
				+ '<td title=\'' + item.path + '\'>'
				+ path
				+ '</td>'
				+ '<td>'
				+ common.dateToString(new Date(item.createTime))
				+ '</td>'
				+ '</tr>';
	}
	$("#log_table_tbody").append(trs);
}

layui.use('laydate', function(){
	var laydate = layui.laydate;
  	laydate.render({
    elem: '#createTime',
    type: 'datetime',
    range: true
});
});



</script>
</body>
</html>
