//终止流程实例
function terminateProcessIns(){
	if(checkChecked()){
		var insUid = $("input[type='checkbox'][name='checkProcessIns']:checked").val();
		$.ajax({
			url : common.getPath()+'/dhProcessInsManage/terminateProcessIns',
			type : 'post',
			dataType : 'json',
			data : {
				"insUid" : insUid
			},
			beforeSend: function(){
				layer.load(1);
			},
			success : function(result){
				layer.alert(result.msg);
				if (result.status == 0) {
					getProcesssInstance();
				}
				layer.closeAll("loading");
			},error : function(){
				layer.alert("终止流程出现异常");
				layer.closeAll("loading");
			}
		})
	}else{
		return;
	}
}

//暂停流程实例
function pauseProcessIns(){
	if(checkChecked()){
		var insUid = $("input[type='checkbox'][name='checkProcessIns']:checked").val();
		$.ajax({
			url : common.getPath()+'/dhProcessInsManage/pauseProcessIns',
			type : 'post',
			dataType : 'json',
			data : {
				"insUid" : insUid
			},
			beforeSend: function(){
				layer.load(1);
			},
			success : function(result){
				layer.alert(result.msg);
				if (result.status == 0) {
					getProcesssInstance();
				}
				layer.closeAll("loading");
			},error : function(){
				layer.alert("暂停流程出现异常");
				layer.closeAll("loading");
			}
		})
	}else{
		return;
	}
	
}

//恢复流程实例
function resumeProcessIns(){
	if(checkChecked()){
		var insUid = $("input[type='checkbox'][name='checkProcessIns']:checked").val();
		$.ajax({
			url : common.getPath()+'/dhProcessInsManage/resumeProcessIns',
			type : 'post',
			dataType : 'json',
			data : {
				"insUid" : insUid
			},
			beforeSend: function(){
				layer.load(1);
			},
			success : function(result){
				layer.alert(result.msg);
				if (result.status == 0) {
					getProcesssInstance();
				}
				layer.closeAll("loading");
			},error : function(){
				layer.alert("恢复流程出现异常");
				layer.closeAll("loading");
			}
		})
	}else{
		return;
	}
	
}

function getProcessInsInfo(){
	if(checkChecked()){
		var insUid = $("input[type='checkbox'][name='checkProcessIns']:checked").val();
		$.ajax({
			url : common.getPath()+'/dhProcessInsManage/getProcessInsInfo',
			type : 'post',
			dataType : 'json',
			data : {
				"insUid" : insUid
			},
			beforeSend: function(){
				layer.load(1);
			},
			success : function(result){
				var jsonObject = JSON.parse(result.msg);
				if (result.status == 0) {
					$("#processIns-text-content").val(JSON.stringify(jsonObject.data));
					$("#processIns-text-div").show();
				}else{
					layer.alert(jsonObject.status);
				}
				layer.closeAll("loading");
			},error : function(){
				layer.alert("获取流程实例信息出现异常");
				layer.closeAll("loading");
			}
		})
	}else{
		return;
	}
	
}

function trunOffProcessIns(){
	if(checkChecked()){
		//var insUid = $("input[type='checkbox'][name='checkProcessIns']:checked").val();
		var taskUid = $("#activity_task").val();
		var activityId = $("#trunOffActivities").val();
		var userUid = $("#handleUser").val();
		var trunOffCause = $("#trunOffCause").val();
		if(taskUid == '' || taskUid == null || taskUid == undefined){
			layer.alert("请选择一个活动中的任务进行撤转");
			return;
		}
		if(userUid == '' || userUid == null || userUid == undefined){
			layer.alert("请选择任务处理人");
			return;
		}
		
		$.ajax({
			url : common.getPath()+'/dhProcessInsManage/trunOffProcessIns',
			type : 'post',
			dataType : 'json',
			data : {
				"taskUid" : taskUid,
				"activityId" : activityId,
				"userUid" : userUid,
				"trunOffCause" : trunOffCause
			},
			beforeSend: function(){
				layer.load(1);
			},
			success : function(result){
				if (result.status == 0) {
					
				}else{
					
				}
				layer.closeAll("loading");
			},error : function(){
				layer.alert("撤转流程实例出现异常");
				layer.closeAll("loading");
			}
		})
	}else{
		return;
	}
	
}

//查看流程图
function showProcessInsMap(){
	if(checkChecked()){
		var insId = $("input[type='checkbox'][name='checkProcessIns']:checked").data("insid");
		$.ajax({
	        url: common.getPath()+'/processInstance/viewProcess',
	        type: 'post',
	        dataType: 'text',
	        data: {
	        	insId: insId
	        },
	        success: function (result) {
	            layer.open({
	                type: 2,
	                title: '流程图',
	                shadeClose: true,
	                shade: 0.3,
	                area: ['790px', '580px'],
	                content: result
	            });
	        }
	    })
	}else{
		return;
	}
}

function toTrunOffProcessIns(){
	if(checkChecked()){
		var insUid = $("input[type='checkbox'][name='checkProcessIns']:checked").val();
		$.ajax({
			url : common.getPath()+'/dhProcessInsManage/toTrunOffProcessIns',
			type : 'post',
			dataType : 'json',
			data : {
				"insUid" : insUid
			},
			beforeSend: function(){
				layer.load(1);
			},
			success : function(result){
				if (result.status == 0) {
					$("#activity_task").empty();
					$("#left_activity_ul").empty();
					$("#trunOffActivities").val("");
					$("#trunOffActivities_view").val("");
					$("#handleUser").val("");
					$("#handleUser_view").val("");
					$("#trunOffCause").val("");
					var activitysStr = "";
					var tasksStr = '<option value=""> --请选择-- </option>';
					for (var i = 0; i < result.data.activityMetaList.length; i++) {
						activitysStr += '<li data-activityId="'+result.data.activityMetaList[i].activityId+'">' 
								+result.data.activityMetaList[i].activityName+'</li>';
					}
					$("#left_activity_ul").append(activitysStr);
					for (var i = 0; i < result.data.taskList.length; i++) {
						if(result.data.taskList[i].taskStatus == "12"){
							tasksStr += '<option value="'+result.data.taskList[i].taskUid+'">'
							+result.data.taskList[i].taskTitle+'(任务待处理 —— '+result.data.taskList[i].usrUid+')</option>';
						}else if(result.data.taskList[i].taskStatus == "-2"){
							tasksStr += '<option value="'+result.data.taskList[i].taskUid+'">'
							+result.data.taskList[i].taskTitle+'(等待加签结束 —— '+result.data.taskList[i].usrUid+')</option>';
						}
					}
					$("#activity_task").append(tasksStr);
					layui.form.render();
					$("#processIns-trun-off-div").show();
				}else{
					layer.alert(result.msg);
				}
				layer.closeAll("loading");
			},error : function(){
				layer.alert("查询信息出现异常");
				layer.closeAll("loading");
			}
		})
	}else{
		return;
	}
}

$(function(){
	// 选择任务处理人
	$("#choose_handle_user").click(function() {
		common.chooseUser('handleUser', 'false');
	});
	// “选择环节”
	$("#chooseActivity_i").click(function() {
		$("#right_activity_ul").empty();
		var choosedValue = $("#rejectActivities").val();
		if (!choosedValue) {
			$("#choose_activity_container").show();
			return;
		}
		var chooseIds = choosedValue.split(";");
		$("#left_activity_ul li").each(function() {
			var activityBpdId = $(this).data('activitybpdid');
			if ($.inArray(activityBpdId, chooseIds) != -1) {
				$(this).appendTo($("#right_activity_ul"));
			}
		});
		$("#choose_activity_container").show();
	})
	$("#chooseActivities_sureBtn").click(function() {
		var val = '';
		var val_view = '';
		$("#right_activity_ul li").each(function() {
			val += $(this).data('activitybpdid');
			val_view += $(this).html();
		});
		$("#trunOffActivities").val(val);
		$("#trunOffActivities_view").val(val_view);
		$("#choose_activity_container").hide();
	});

	$("#choose_activity_container").on('click', 'li', function() {
		if ($(this).hasClass('colorli')) {
			$(this).removeClass('colorli');
		} else {
			$(this).addClass('colorli');
		}
	});
});

function moveActivityToRight() {
	$("#left_activity_ul li.colorli").each(function() {
		if($("#right_activity_ul li").length<1){//只允许选择一个撤回目标环节
			$(this).removeClass("colorli");
			$(this).appendTo($("#right_activity_ul"));
		}else{
			layer.alert("只允许选择一个撤回目标环节");
		}
	});
}
function moveActivityToLeft() {
	$("#right_activity_ul li.colorli").each(function() {
		$(this).removeClass("colorli");
		$(this).appendTo($("#left_activity_ul"));
	});
}

//判断只允许选中一个流程实例
function checkChecked(){
	var checkedNodes= $("input[type='checkbox'][name='checkProcessIns']:checked");
	if(checkedNodes.length==1){
		return true;
	}else{
		layer.alert("请选择一个流程实例进行操作！");
		return false;
	}
}