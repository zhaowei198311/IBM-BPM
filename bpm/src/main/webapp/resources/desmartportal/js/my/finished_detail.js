//跟踪流程图
function processView(insId) {
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
				shade : 0,
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