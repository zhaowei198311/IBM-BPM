function addNotifyTemplate(){
	$("#operation-title").text("新增通知模板");
	$("#submitOperation").text("新增");
	$("#submitOperation").css("display","inline-block");
	$(".cancel_btn").text("取消");
	$("#templateUid").val("");
	$("input[name='templateType'][value='MAIL_NOTIFY_TEMPLATE']").prop("checked",true);
	$("#templateContent").val("");
	layui.form.render("radio");
	$("#notifyt-template-div").show();
}

function updateNotifyTemplate(){
	$("#operation-title").text("修改通知模板");
	$("#submitOperation").text("保存");
	$("#submitOperation").css("display","inline-block");
	$(".cancel_btn").text("取消");
	var checkedElement = $("input[name='checkNotifyTemplate']:checked");
	if(checkedElement.length!=1){
		layer.alert("请选择一个模板记录");
		return;
	}
	var tr= checkedElement.parent().parent();
	$("input[name='templateName']").val(tr.find("td").eq(1).text());
	$("#templateContent").val(tr.find("td").eq(3).find("pre").text());
	var templateType = tr.find("td").eq(2).data("templatetype");
	$("input[name='templateType'][value='"+templateType+"']").prop("checked",true);
	$("#templateUid").val(checkedElement.val());
	layui.form.render("radio");
	$("#notifyt-template-div").show();
}

function deleteNotifyTemplate(){
	var checkedElement = $("input[name='checkNotifyTemplate']:checked");
	if(checkedElement.length!=1){
		layer.alert("请选择一个模板记录");
		return;
	}
	var templateUid = checkedElement.val();
	$.ajax({
		url : common.getPath()+'/dhNotifyTemplate/deleteNotifyTemplate',
		type : 'post',
		dataType : 'json',
		data : {
			"templateUid" : templateUid
		},
		beforeSend: function(){
			layer.load(1);
		},
		success : function(result){
			layer.alert(result.msg);
			if (result.status == 0) {
				pageNotifyTemplate();
			}
			layer.closeAll("loading");
		},error : function(){
			layer.alert("删除模板出现异常");
			layer.closeAll("loading");
		}
	})
}

function showNotifyTemplate(a){
	$("#operation-title").text("查看通知模板");
	$("#submitOperation").css("display","none");
	$(".cancel_btn").text("关闭");
	var tr= $(a).parent().parent();
	$("input[name='templateName']").val(tr.find("td").eq(1).text());
	$("#templateContent").val(tr.find("td").eq(3).find("pre").text());
	var templateType = tr.find("td").eq(2).data("templatetype");
	$("input[name='templateType'][value='"+templateType+"']").prop("checked",true);
	layui.form.render("radio");
	$("#notifyt-template-div").show();
}

/** 提交操作 **/
function submitOperation(){
	
	if (!$('#operationTemplateForm').valid()) {
		layer.alert("验证失败，请检查后提交");
		return;
	}
	var fromData = $('#operationTemplateForm').serializeArray();
	$.ajax({
		url : common.getPath()+'/dhNotifyTemplate/submitOperationTemplate',
		type : 'post',
		dataType : 'json',
		data : fromData,
		beforeSend: function(){
			layer.load(1);
		},
		success : function(result){
			layer.alert(result.msg);
			if (result.status == 0) {
				pageNotifyTemplate();
			}
			$('#notifyt-template-div').hide();
			layer.closeAll("loading");
		},error : function(){
			layer.alert("提交操作出现异常");
			layer.closeAll("loading");
		}
	})
}

/** 校验规则 **/
$("#operationTemplateForm").validate({
	rules:{
		templateName : {
			required : function(element) {
				return !$("input[name='templateName']").val().trim().length > 0;
			}
		},
		templateContent : {
			required : function(element){
				return !$("#templateContent").val().trim().length > 0;
			}
		}
		
	}
});