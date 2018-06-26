//跟踪流程图
function processView(insId) {
	console.log(common.getDesignFormData());
	$.ajax({
		url : 'processInstance/viewProcess',
		type : 'post',
		dataType : 'text',
		data : {
			insId : insId
		},
		success : function(result) {
			layer.open({
				type : 2,
				title : '流程图',
				shadeClose : true,
				shade : 0.3,
				area : [ '790px', '580px' ],
				content : result
			});
		}
	});
}

//数据信息
var view = $(".container-fluid");
var form = null;
$(function () {
    var insData = $("#insData").text();
    layui.use(['form'], function () {
        form = layui.form;
    });
    console.log("已办任务表单数据渲染:"+insData);
    var insDataFromDb = JSON.parse(insData);
    var formData = insDataFromDb.formData;
    var str = JSON.stringify(formData);
    common.giveFormSetValue(str);
    var fieldPermissionInfo = $("#fieldPermissionInfo").text();
    common.giveFormFieldPermission(fieldPermissionInfo);
    form.render();
});

function back() {
	window.location.href = 'javascript:history.go(-1)';
}

function revokeTask(taskUid) {
    $.ajax({
        url : "taskInstance/revokeTask",
        type : "post",
        dataType : "json",
        data : {
            "taskUid": taskUid
		},
        beforeSend : function(){
            layer.load(1);
        },
        success : function(result){
            layer.closeAll('loading');
            if(result.status == 0){
				layer.alert('取回成功，请去待办页面处理', function () {
                    window.history.back();
                });
            }else{
                layer.alert(result.msg);
            }
        },
        error : function(){
            layer.closeAll('loading');
            layer.alert('操作失败');
        }
    });
}