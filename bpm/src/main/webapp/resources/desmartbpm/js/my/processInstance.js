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