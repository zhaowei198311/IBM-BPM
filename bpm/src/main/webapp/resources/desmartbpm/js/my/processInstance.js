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
					$(".display_container5").show();
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
		var insUid = $("input[type='checkbox'][name='checkProcessIns']:checked").val();
		$.ajax({
			url : common.getPath()+'/dhProcessInsManage/trunOffProcessIns',
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

$(function(){
	$(".cancel_btn").click(function() {
		$(".display_container5").css("display", "none");
	})
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