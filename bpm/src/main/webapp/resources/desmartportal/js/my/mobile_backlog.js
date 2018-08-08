var index = "";
//异步请求的分页参数
var pageConfig = {
	pageNum:1,
	pageSize:8,
	createProcessUserName : "",
	taskPreviousUsrUsername: "",
	insTitle : "",
	isAgent:"",
	startTime : null,
	endTime: null,
	proName:"",
	total : 0
};

var calendar1 = null;
var calendar2 = null;
$(function(){
	// 加载数据
	getTaskInstanceInfo();
	
	calendar1 = new lCalendar();
	calendar1.init({
		'trigger': '#start_time',
		'type': 'date'
	});
	calendar2 = new lCalendar();
	calendar2.init({
		'trigger': '#end_time',
		'type': 'date'
	});
	
	//异步加载代办列表
	userAsync();
});

//打开筛选条件div的方法
function fiterDivShow(){
	$("body").css({"position":"fixed"});
	index = layer.open({
		type: 1
		,content: $("#filter_div")
		,offset: '40px'
		,title: false
    	,shadeClose: true
    	,closeBtn:0
    	,shade: 0.3
    	,anim:2
    	,zIndex:100
    	,resize:false
    	,area: ['width:100%', '350px']
		,end:function(){
			$("#filter_div").css("display","none");
			$("body").css({"position":""});
		}
	});
	
	
	var maxDate = "";
	$("#start_time").focus(function(){
		console.log($("#end_time").val());
		if($("#end_time").val()!=null && $("#end_time").val()!=""){
			maxDate = $("#end_time").val();
			calendar1.setDate({
				'maxDate':maxDate
			});
		}
	});

	
	var minDate = "";
	$("#end_time").focus(function(){
		console.log($("#start_time").val());
		if($("#start_time").val()!=null && $("#start_time").val()!=""){
			minDate = $("#start_time").val();
			calendar2.setDate({
				'minDate':minDate
			});
		}
	});
}

//确认搜索条件的方法
function queryTask(){
	pageConfig.createProcessUserName = $("#process_staff").val().trim();
	pageConfig.taskPreviousUsrUsername = $("#last_activity_staff").val().trim();
	pageConfig.insTitle = $("#process_instance_title").val().trim();
	var startTime = $("#start_time").val().trim();
	var endTime = $("#end_time").val().trim();
	if(startTime>endTime){
		layer.msg("开始时间必须早于结束时间", {icon: 2});
		return;
	}
	pageConfig.startTime = startTime;
	pageConfig.endTime = endTime;
	pageConfig.proName = $("#proName").val().trim();
	pageConfig.pageNum = 1;
	pageConfig.pageSize = 8;
	getTaskInstanceInfo();
	layer.close(index);
}

//异步加载代办列表
function userAsync() {
	$(window).scroll(function(){
		//监听事件内容
		if(getScrollHeight() == getWindowHeight() + getDocumentTop()){
			//当滚动条到底时,这里是触发内容
		    if(pageConfig.pageNum==1){
				pageConfig.pageNum = 3;
			}else{
				pageConfig.pageNum++;
			}
			pageConfig.pageSize=4;
			getTaskInstanceInfo();
		}
	});
}

//查询用户的代办
function getTaskInstanceInfo(){
	$.ajax({
		url :common.getPath() + '/taskInstance/loadBackLog',
		type : 'post',
		beforeSend:function(){
			layer.load(1);
		},
		dataType : 'json',
		data : {
			pageNum : pageConfig.pageNum,
			pageSize : pageConfig.pageSize,
			createProcessUserName : pageConfig.createProcessUserName,
			taskPreviousUsrUsername: pageConfig.taskPreviousUsrUsername,
			insTitle : pageConfig.insTitle,
			isAgent:pageConfig.isAgent,
			startTime : pageConfig.startTime,
			endTime: pageConfig.endTime,
			proName:pageConfig.proName
		},
		success : function(result){
			if (result.status == 0) {
				drawTable(result.data);
			}
			layer.closeAll("loading");
		},
		error:function(){
			layer.closeAll("loading");
		}
	})
}

//渲染代办表格
function drawTable(data) {
	// 渲染数据
	if(pageConfig.pageNum==1){
		$("#backlog_list").html('');
		var total = data.total;
		var totalHtml = '<p class="table_list">'
			+'<i class="layui-icon">&#xe61d;</i>共<span id="daiban_icon">'
			+total+'</span>条任务'
			+'</p>';
		$("#backlog_list").append(totalHtml);
	}
	var list = data.list;
	var liHtml = "";
	var type = "";
	var status = "";
	for (var i = 0; i < list.length; i++) {
		var meta = list[i];
		if(meta.taskStatus==12){
			status = "待处理";
		}
		if(meta.taskStatus==-2){
			status = "等待加签结束";
		}
		var agentOdate = new Date(meta.taskInitDate);
		var InitDate = datetimeFormat_1(agentOdate);
		liHtml += '<li onclick=openApproval("'+meta.taskUid+'")>'
			+'<table>'
			+'<tr>'
			+'<th>流程名称：</th>'
			+'<td>' 
            +meta.dhProcessInstance.proName 
            +'</td>'
			+'</tr>'
			+'<tr>'
			+'<th>流程标题：</th>'
			+'<td>'+meta.dhProcessInstance.insTitle+'</td>'
			+'</tr>'
			+'<tr>'
			+'<th>环节名称：</th>'
			+'<td>'+meta.taskTitle+'</td>'
			+'</tr>'
			+'<tr>'
			+'<th>上一环节处理人：</th>'
			+'<td>';
		if(meta.taskPreviousUsrUsername!=null && meta.taskPreviousUsrUsername!=""){
			liHtml += meta.taskPreviousUsrUsername;
		}
		liHtml+='</td>'
			+'</tr>'
			+'<tr>'
			+'<th>流程创建人：</th>'
			+'<td>';
		if(meta.sysUser.userName!=null && meta.sysUser.userName!=""){
			liHtml += meta.sysUser.userName;
		}
		liHtml+='</td>'
			+'</tr>'
			+'<tr>'
			+'<th>任务接收时间：</th>'
			+'<td>'+InitDate+'</td>'
			+'</tr>'
			+'<tr>'
			+'<th colspan="2">';
		
		if(meta.taskType=="normal"){
			liHtml+=taskProgerss(meta.taskActivityId,meta.taskUid);
		}else{
			liHtml+=taskProgerss("",meta.taskUid);
		}
			
		liHtml+='</th>'
			+'</tr>'
			+'</table>';
		if(meta.taskDelegateUser != null && meta.taskDelegateUser !=""){
			liHtml+='<div class="is_agent">'
				+'<span>代</span>'
				+'</div>';
		}
		liHtml+='<div class="task_status">'
			+'<span>'+status+'</span>'
			+'</div>'
			+'</li>';
	}
	$("#backlog_list").append(liHtml);
	
	// 加载进度条
    layui.use('element', function () {
        var $ = layui.jquery,
            element = layui.element; //Tab的切换功能，切换事件监听等，需要依赖element模块
        
        element.render();
    });
}

//查询任务的完成进度
function taskProgerss(activityId,taskUid){
	var proHtml = "";
	// 查询审批进度剩余进度百分比
    $.ajax({
        async: false,
        url: common.getPath() + "/taskInstance/queryProgressBar",
        type: "post",
        dataType: "json",
        data: {
        	activityId: activityId,
            taskUid: taskUid
        },
        success: function (data) {
            var result = data.data;
            // 剩余时间
            var hour = result.hour;
            // 剩余时间百分比
            var percent = result.percent;
            if (data.status == 0) {
            	proHtml = '<div class="layui-progress layui-progress-big" lay-filter="progressBar" style="position: relative;">'
            		+'<div class="layui-progress-bar" lay-percent="'+(100-percent)+'%"></div>';
                if (hour == -1) {
                	proHtml += "<span class='progress_time' style='right: 1%;'>审批已超时</span>";
                } else {
                	proHtml += "<span class='progress_time' style='right: 1%;'>审批剩余时间" + hour + "小时</span>";             
                }
                proHtml+='</div>';
            } else {
            	proHtml = '<div class="layui-progress layui-progress-big" lay-filter="progressBar" '
            		+'加载失败'
            		+'lay-showPercent="yes" style="position: relative;">';
            }
        }
    });
    return proHtml;
}

//打开 代办的 详细页面
function openApproval(taskUid){
	layer.load(1);
	window.location.href = common.getPath() +'/menus/approval?taskUid='+taskUid;
	layer.closeAll("loading");
}

//文档高度
function getDocumentTop() {
	var scrollTop = 0, bodyScrollTop = 0, documentScrollTop = 0;
	if (document.body) {
		bodyScrollTop = document.body.scrollTop;
	}
	if (document.documentElement) {
		documentScrollTop = document.documentElement.scrollTop;
	}
	scrollTop = (bodyScrollTop - documentScrollTop > 0) ? bodyScrollTop
			: documentScrollTop;
	return scrollTop;
}

//可视窗口高度
function getWindowHeight() {
	var windowHeight = 0;
	if (document.compatMode == "CSS1Compat") {
		windowHeight = document.documentElement.clientHeight;
	} else {
		windowHeight = document.body.clientHeight;
	}
	return windowHeight;
}

//滚动条滚动高度
function getScrollHeight() {
	var scrollHeight = 0, bodyScrollHeight = 0, documentScrollHeight = 0;
	if (document.body) {
		bodyScrollHeight = document.body.scrollHeight;
	}
	if (document.documentElement) {
		documentScrollHeight = document.documentElement.scrollHeight;
	}
	scrollHeight = (bodyScrollHeight - documentScrollHeight > 0) ? bodyScrollHeight
			: documentScrollHeight;
	return scrollHeight;
}