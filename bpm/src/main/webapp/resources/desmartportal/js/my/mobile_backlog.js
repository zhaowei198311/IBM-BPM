var index = "";
//异步请求的分页参数
var backlogPageConfig = {
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
	
	getFinisedTaskInstanceInfo();
	
	getEndTaskInstanceInfo();
	
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
	
	//回到顶部
	$(".layui-fixbar-top").click(function(){
		jQuery('html,body').animate({
		    scrollTop: 0
		}, 300);
	});
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
	var liId = $(".layui-tab-title").find(".layui-this").prop("id");
	switch(liId){
		case "backlog_li":{
			//当滚动条到底时,这里是触发内容
			backlogPageConfig.createProcessUserName = $("#process_staff").val().trim();
			backlogPageConfig.taskPreviousUsrUsername = $("#last_activity_staff").val().trim();
			backlogPageConfig.insTitle = $("#process_instance_title").val().trim();
			var startTime = $("#start_time").val().trim();
			var endTime = $("#end_time").val().trim();
			if(startTime>endTime){
				layer.msg("开始时间必须早于结束时间", {icon: 2});
				return;
			}
			backlogPageConfig.startTime = startTime;
			backlogPageConfig.endTime = endTime;
			backlogPageConfig.proName = $("#proName").val().trim();
			backlogPageConfig.pageNum = 1;
			backlogPageConfig.pageSize = 8;
			getTaskInstanceInfo();
			break;
		}
		case "finised_li":{
			finishedPageConfig.createProcessUserName = $("#process_staff").val().trim();
			finishedPageConfig.taskPreviousUsrUsername = $("#last_activity_staff").val().trim();
			finishedPageConfig.insTitle = $("#process_instance_title").val().trim();
			var startTime = $("#start_time").val().trim();
			var endTime = $("#end_time").val().trim();
			if(startTime>endTime){
				layer.msg("开始时间必须早于结束时间", {icon: 2});
				return;
			}
			finishedPageConfig.startTime = startTime;
			finishedPageConfig.endTime = endTime;
			finishedPageConfig.proName = $("#proName").val().trim();
			finishedPageConfig.pageNum = 1;
			finishedPageConfig.pageSize = 8;
			getFinisedTaskInstanceInfo();
			break;
		}
		case "end_li":{
			endPageConfig.createProcessUserName = $("#process_staff").val().trim();
			endPageConfig.taskPreviousUsrUsername = $("#last_activity_staff").val().trim();
			endPageConfig.insTitle = $("#process_instance_title").val().trim();
			var startTime = $("#start_time").val().trim();
			var endTime = $("#end_time").val().trim();
			if(startTime>endTime){
				layer.msg("开始时间必须早于结束时间", {icon: 2});
				return;
			}
			endPageConfig.startTime = startTime;
			endPageConfig.endTime = endTime;
			endPageConfig.proName = $("#proName").val().trim();
			endPageConfig.pageNum = 1;
			endPageConfig.pageSize = 8;
			getEndTaskInstanceInfo();
			break;
		}
	}
	$("#process_staff").val("");
	$("#last_activity_staff").val("");
	$("#process_instance_title").val("");
	$("#start_time").val("");
	$("#end_time").val("");
	$("#proName").val("");
	layer.close(index);
}

//异步加载代办列表
function userAsync() {
	$(window).scroll(function(){
		var liId = $(".layui-tab-title").find(".layui-this").prop("id");
		//监听事件内容
		if(getScrollHeight() == getWindowHeight() + getDocumentTop()){
			switch(liId){
				case "backlog_li":{
					//当滚动条到底时,这里是触发内容
					if(backlogPageConfig.pageNum==1){
						backlogPageConfig.pageNum = 3;
					}else{
						backlogPageConfig.pageNum++;
					}
					backlogPageConfig.pageSize=4;
					getTaskInstanceInfo();
					break;
				}
				case "finised_li":{
					if(finishedPageConfig.pageNum==1){
						finishedPageConfig.pageNum = 3;
					}else{
						finishedPageConfig.pageNum++;
					}
					finishedPageConfig.pageSize=4;
					getFinisedTaskInstanceInfo();
					break;
				}
				case "end_li":{
					if(endPageConfig.pageNum==1){
						endPageConfig.pageNum = 3;
					}else{
						endPageConfig.pageNum++;
					}
					endPageConfig.pageSize=4;
					getEndTaskInstanceInfo();
					break;
				}
			}
		}
	});
}

//查询用户的代办
function getTaskInstanceInfo(){
	$.ajax({
		url :common.getPath() + '/taskInstance/loadBackLogMove',
		type : 'post',
		beforeSend:function(){
			layer.load();
		},
		dataType : 'json',
		data : {
			pageNum : backlogPageConfig.pageNum,
			pageSize : backlogPageConfig.pageSize,
			createProcessUserName : backlogPageConfig.createProcessUserName,
			taskPreviousUsrUsername: backlogPageConfig.taskPreviousUsrUsername,
			insTitle : backlogPageConfig.insTitle,
			isAgent:backlogPageConfig.isAgent,
			startTime : backlogPageConfig.startTime,
			endTime: backlogPageConfig.endTime,
			proName:backlogPageConfig.proName
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
	if(backlogPageConfig.pageNum==1){
		$("#backlog_list").html('');
		var total = data.total;
		$("#daiban_icon").text(total);
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
			+'<div class="backlog_top">'
			+'<div class="backlog_process">'
			+'<span class="backlog_process_name">'+meta.dhProcessInstance.proName+'</span>'//流程名称
			+'<span class="backlog_process_no"> ';
		if(meta.dhProcessInstance.proNo!=null && meta.dhProcessInstance.proNo!=""){
			liHtml += meta.dhProcessInstance.proNo
		}
		liHtml += '</span>'//流程编号
			+'<span class="backlog_task_status">'+status+'</span>'//任务状态
			+'</div>'
			+'</div>'
			+'<div class="backlog_content">'
			+'<table style="width:75%;">'
			+'<thead>'
			+'<tr>'
			+'<td class="process_title_div">'
			+'<span class="process_title">'+meta.dhProcessInstance.insTitle+'</span>'//流程标题
			+'</td>'
			+'</tr>'
			+'</thead>'
			+'<tbody>'
			+'<tr>'
			+'<td>前序处理人：';
		if(meta.taskPreviousUsrUsername!=null && meta.taskPreviousUsrUsername!=""){
			liHtml += meta.taskPreviousUsrUsername;
		}
		liHtml += '</td>'//上环节处理人
			+'</tr>'
			+'<tr>'
			+'<td>创建人：';
		if(meta.sysUser.userName!=null && meta.sysUser.userName!=""){
			liHtml += meta.sysUser.userName;
		}
		liHtml += '</td>'//创建人
			+'</tr>'
			+'<tr>'
			+'<td>'+InitDate+'</td>'//接收时间
			+'</tr>'
			+'</tbody>'
			+'</table>';
		
		if(meta.progerssParamMap!=null){
			// 剩余时间
	        var hour = meta.progerssParamMap.hour;
	        // 剩余时间百分比
	        var percent = meta.progerssParamMap.percent;
	        if(hour!=null && percent!=null){
	        	liHtml += '<div class="progress_bar_div" style="width:85px;">'
	    			+'<div class="progress_time_div">'
	    			+'<span class="progress_time">';
	        	if (hour == -1) {
	        		liHtml += "审批已超时";
	            } else {
	            	liHtml += "剩余：" + hour + "小时";             
	            }
	        	liHtml += '</span>'//进度条剩余时间
	    			+'</div>'
	    			+'<div class="layui-progress" lay-filter="progressBar" style="position: relative;">'
	    			+'<div class="layui-progress-bar" lay-percent="'+(100-percent)+'" style="background-color: #FF9052; "></div>'
	    			+'</div>'
	    			+'</div>';
	        }
		}
		
		if(meta.taskDelegateUser != null && meta.taskDelegateUser !=""){
			liHtml += '<div class="task_agent_status">'
				+'<span class="task_agent_text">代</span>'//是否代理
				+'</div>';
		}
		liHtml += '</div>'
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

//打开 代办的 详细页面
function openApproval(taskUid){
	layer.load();
	window.location.href = common.getPath() +'/menus/approval?taskUid='+taskUid;
	layer.closeAll("loading");
}

/************************** 已办 *************************/
var finishedPageConfig = {
	pageNum : 1,
	pageSize : 8,
	createProcessUserName : "",
	taskPreviousUsrUsername : "",
	insTitle : "",
	proName : "",
	isAgent : "",
	startTime : null,
	endTime : null,
	total : 0
}

function getFinisedTaskInstanceInfo(){
	$.ajax({
		url : common.getPath() +'/taskInstance/loadPageTaskByClosedMove',
		type : 'post',
		dataType : 'json',
		beforeSend:function(){
			layer.load();
		},
		data : {
			pageNum : finishedPageConfig.pageNum,
			pageSize : finishedPageConfig.pageSize,
			createProcessUserName : finishedPageConfig.createProcessUserName,
			taskPreviousUsrUsername: finishedPageConfig.taskPreviousUsrUsername,
			insTitle : finishedPageConfig.insTitle,
			proName : finishedPageConfig.proName,
			isAgent:finishedPageConfig.isAgent,
			startTime : finishedPageConfig.startTime,
			endTime: finishedPageConfig.endTime
		},
		success : function(result){
			if(result.status == 0){
				drawFinisedTable(result.data);
			}
			layer.closeAll("loading");
		},
		error:function(){
			layer.closeAll("loading");
		}
	})
}

function drawFinisedTable(data){
	// 渲染数据
	if(finishedPageConfig.pageNum==1){
		$("#finised_list").html('');
		var total = data.total;
		$("#yiban_icon").text(total);
	}
	var list = data.list;
	var liHtml = "";
	var type = "";
	for (var i = 0; i < list.length; i++) {
		var meta = list[i];
		var agentOdate = new Date(meta.taskInitDate);
		var InitDate = datetimeFormat_1(agentOdate);
		var finishDate = "";
		if(meta.taskFinishDate!=null && meta.taskFinishDate!=""){
			var agentOdate1 = new Date(meta.taskFinishDate);
			finishDate = datetimeFormat_1(agentOdate1);
		}
		var insTitle = meta.dhProcessInstance.insTitle;
		var insId = meta.dhProcessInstance.insId;
		liHtml += '<li onclick=openFinishedDetail("'+meta.taskUid+'")>'
			+'<div class="backlog_top">'
			+'<div class="backlog_process">'
			+'<span class="backlog_process_name">'+ meta.dhProcessInstance.proName +'</span>'
			+' <span class="backlog_process_no"> ';
		if(meta.dhProcessInstance.proNo!=null && meta.dhProcessInstance.proNo!=""){
			liHtml += meta.dhProcessInstance.proNo
		}
		liHtml += '</span>'
			+'</div>'
			+'</div>'
			+'<div class="backlog_content">'
			+'<table>'
			+'<thead>'
			+'<tr>'
			+'<td class="process_title_div">'
			+'<span class="process_title">'+insTitle+'</span>'
			+'</td>'
			+'</tr>'
			+'</thead>'
			+'<tbody>'
			+'<tr>'
			+'<td>前序处理人：';
		if(meta.taskPreviousUsrUsername!=null && meta.taskPreviousUsrUsername!=""){
			liHtml += meta.taskPreviousUsrUsername;
		}
		liHtml += '</td>'
			+'</tr>'
			+'<tr>'
			+'<td>创建人：';
		if(meta.sysUser.userName!=null && meta.sysUser.userName!=""){
			liHtml += meta.sysUser.userName;
		}
		liHtml += '</td>'
			+'</tr>'
			+'<tr>'
			+'<td>接收时间：'+InitDate+'</td>'
			+'</tr>'
			+'<tr>'
			+'<td>处理时间：'+finishDate+'</td>'
			+'</tr>'
			+'</tbody>'
			+'</table>';
		if(meta.taskDelegateUser != null && meta.taskDelegateUser !=""){
			liHtml += '<div class="task_agent_status">'
				+'<span class="task_agent_text">代</span>'
				+'</div>';
		}
		liHtml += '</div>'
			+'</li>';
	}
	$("#finised_list").append(liHtml);
}

/************************** 办结 *************************/
var endPageConfig = {
	pageNum : 1,
	pageSize : 8,
	createProcessUserName : "",
	taskPreviousUsrUsername : "",
	insTitle : "",
	proName : "",
	isAgent : "",
	startTime : null,
	endTime : null,
	total : 0
}

function getEndTaskInstanceInfo(){
	$.ajax({
		url : common.getPath() +'/taskInstance/loadPageTaskByEndMove',
		type : 'post',
		dataType : 'json',
		beforeSend:function(){
			layer.load();
		},
		data : {
			pageNum : endPageConfig.pageNum,
			pageSize : endPageConfig.pageSize,
			createProcessUserName : endPageConfig.createProcessUserName,
			taskPreviousUsrUsername: endPageConfig.taskPreviousUsrUsername,
			insTitle : endPageConfig.insTitle,
			proName : endPageConfig.proName,
			isAgent:endPageConfig.isAgent,
			startTime : endPageConfig.startTime,
			endTime: endPageConfig.endTime
		},
		success : function(result){
			if(result.status == 0){
				drawEndTable(result.data);
			}
			layer.closeAll("loading");
		},
		error:function(){
			layer.closeAll("loading");
		}
	})
}

function drawEndTable(data){
	// 渲染数据
	if(endPageConfig.pageNum==1){
		$("#end_list").html('');
		var total = data.total;
		$("#banjie_icon").text(total);
	}
	var list = data.list;
	var liHtml = "";
	var type = "";
	for (var i = 0; i < list.length; i++) {
		var meta = list[i];
		var agentOdate = new Date(meta.taskInitDate);
		var InitDate = datetimeFormat_1(agentOdate);
		var finishDate = "";
		if(meta.taskFinishDate!=null && meta.taskFinishDate!=""){
			var agentOdate1 = new Date(meta.taskFinishDate);
			finishDate = datetimeFormat_1(agentOdate1);
		}
		var insTitle = meta.dhProcessInstance.insTitle;
		var insId = meta.dhProcessInstance.insId;
		liHtml += '<li onclick=openFinishedDetail("'+meta.taskUid+'")>'
			+'<div class="backlog_top">'
			+'<div class="backlog_process">'
			+'<span class="backlog_process_name">'+ meta.dhProcessInstance.proName +'</span>'
			+' <span class="backlog_process_no"> ';
		if(meta.dhProcessInstance.proNo!=null && meta.dhProcessInstance.proNo!=""){
			liHtml += meta.dhProcessInstance.proNo
		}
		liHtml += '</span>'
			+'</div>'
			+'</div>'
			+'<div class="backlog_content">'
			+'<table>'
			+'<thead>'
			+'<tr>'
			+'<td class="process_title_div">'
			+'<span class="process_title">'+insTitle+'</span>'
			+'</td>'
			+'</tr>'
			+'</thead>'
			+'<tbody>'
			+'<tr>'
			+'<td>前序处理人：';
		if(meta.taskPreviousUsrUsername!=null && meta.taskPreviousUsrUsername!=""){
			liHtml += meta.taskPreviousUsrUsername;
		}
		liHtml += '</td>'
			+'</tr>'
			+'<tr>'
			+'<td>创建人：';
		if(meta.sysUser.userName!=null && meta.sysUser.userName!=""){
			liHtml += meta.sysUser.userName;
		}
		liHtml += '</td>'
			+'</tr>'
			+'<tr>'
			+'<td>接收时间：'+InitDate+'</td>'
			+'</tr>'
			+'<tr>'
			+'<td>处理时间：'+finishDate+'</td>'
			+'</tr>'
			+'</tbody>'
			+'</table>';
		if(meta.taskDelegateUser != null && meta.taskDelegateUser !=""){
			liHtml += '<div class="task_agent_status">'
				+'<span class="task_agent_text">代</span>'
				+'</div>';
		}
		liHtml += '</div>'
			+'</li>';
	}
	$("#end_list").append(liHtml);
}

// 文档高度
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