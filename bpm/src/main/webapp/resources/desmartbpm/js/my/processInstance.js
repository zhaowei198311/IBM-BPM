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
		var userArr = userUid.split(";");
		if(activityId == "" || activityId == null){
			if(userArr.length > 1 && userArr[1] != "" ){
				layer.alert("更换任务处理人只能选择一个任务处理人");
				return;
			}
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
					getProcesssInstance();
					layer.alert("撤转流程实例成功");
					$("#processIns-trun-off-div").hide();
				}else{
					layer.alert(result.msg);
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
//var activitysStr = "";
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
					activitysStr = "";
					for (var i = 0; i < result.data.activityMetaList.length; i++) {
						/*activitysStr += '<li data-activityid="'+result.data.activityMetaList[i].activityId+'">' 
								+result.data.activityMetaList[i].activityName+'</li>';*/
						activitysStr += '<tr><td style= "text-align: center;">'
								+'<input type="checkbox" name = "choose_activity_checkbox" '
								+' onclick = "checkedActivity(this)" value ="'
								+result.data.activityMetaList[i].activityId+'"/> </td>'
								+'<td style= "text-align: center;">'
								+result.data.activityMetaList[i].activityName+'</td></tr>';
					}
					$("#choose_activity_tbody").append(activitysStr);
					//$("#left_activity_ul").append(activitysStr);
					
					var tasksStr = '<option value=""> --请选择-- </option>';
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
	$("#chooseActivity_i").click(function(){
		$("#choose_activity_container").show();
	});
	//确定选中环节
	$("#chooseActivities_sureBtn").click(function(){
		$("#trunOffActivities").val("");
		$("#trunOffActivities_view").val("");
		var checkedNode= $("input[type='checkbox'][name='choose_activity_checkbox']:checked"); 
		$("#trunOffActivities").val(checkedNode.val());
		var checkedNode_view = checkedNode.parent().next().text();
		$("#trunOffActivities_view").val(checkedNode_view);
		$('#choose_activity_container').hide();
	});
});

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
//只选择一个目标环节
function checkedActivity(a){
	var checkeNodes= $("input[type='checkbox'][name='choose_activity_checkbox']"); 
	if($(a).prop("checked")==true){
	checkeNodes.prop("checked",false);
	$(a).prop("checked",true);
	}
}