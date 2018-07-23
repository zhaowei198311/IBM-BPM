//跟踪流程图
function processView(insId) {
	window.parent.openProView(insId);
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

//去打印预览的方法
function prePrintIndex(){
	var formUid = $("#formId").val().trim();
	var formHtml = $("#formSet").html();
	var jsonStr = common.getDesignFormData();
	$("#preViewForm").html(formHtml);
	$("#preViewForm").find(".arrow").remove();
	giveFormSetValue(jsonStr);
	prePrint();
	var webpage = $("#preViewForm").html();
	layer.load(1);
	$.ajax({
		url:common.getPath()+"/finishFormPrint/toPDF",
		method:"post",
		async:false,
		data:{
			webpage:webpage
		},
		success:function(result){
			if(result.status == 0){
				window.open(result.data);
			}else{
				layer.alert("生成预览页失败");
			}
			layer.closeAll("loading");
		},
		error:function(){
			layer.closeAll("loading");
		}
	});
}

//给隐藏的打印div赋值
function giveFormSetValue(jsonStr){
	var json = JSON.parse(jsonStr)
	for(var name in json){
		var paramObj = json[name];
		var type = paramObj.type;
		if(type=="table"){
			var valueArr = paramObj.value;
			var trObj = $("#preViewForm").find("[name='"+name+"']").find("tbody tr").html();
			$("#preViewForm").find("[name='"+name+"']").find("tbody").html("");
			for(var i=0;i<valueArr.length;i++){
				$("#preViewForm").find("[name='"+name+"']").find("tbody").append("<tr>"+trObj+"</tr>");
				var valueObj = valueArr[i];
				var tdArr = $("#preViewForm").find("[name='"+name+"']").find("tbody tr:eq("+i+")").find("td");
				tdArr.each(function(index){
					if(index!=tdArr.length-1){
						var key = $(this).data("label");
						var value = valueObj[key];
						$(this).find("input").val(value);
					}
				});
			}
		}
		var tagName = $("#preViewForm").find("[name='"+name+"']").prop("tagName");
		switch(tagName){
			case "INPUT":{
				var tagType = $("#preViewForm").find("[name='"+name+"']").attr("type");
				switch(tagType){
					case "text":{
						if($("#preViewForm").find("[name='"+name+"']").attr("title")=="choose_user"){
							var valueStr = paramObj["value"];
							var descriptionStr = paramObj["description"];
							$("#preViewForm").find("[name='"+name+"']").val(descriptionStr);
							$("#preViewForm").find("[name='"+name+"']").parent().find("input[type='hidden']").val(valueStr);
							break;
						}else if($("#preViewForm").find("[name='"+name+"']").attr("title")=="choose_value"){
							var value = paramObj["value"];
							var description = paramObj["description"];
							var id = paramObj["id"];
							$("#preViewForm").find("[name='"+name+"']").val(description);
							$("#preViewForm").find("[name='"+name+"']").parent().find("input[class='value_id']").val(value);
							$("#preViewForm").find("[name='"+name+"']").parent().find("input[class='value_code']").val(id);
							break;
						}else if($("#preViewForm").find("[name='"+name+"']").attr("title")=="choose_depart"){
							var value = paramObj["value"];
							var description = paramObj["description"];
							$("#preViewForm").find("[name='"+name+"']").val(description);
							$("#preViewForm").find("[name='"+name+"']").parent().find("input[type='hidden']").val(value);
							break;
						}
					};
					case "tel":;
					case "date":{
						var value = paramObj["value"];
						$("#preViewForm").find("[name='"+name+"']").val(value);
						break;
					};
					case "radio":{
						$("#preViewForm").find("[name='"+name+"'][value='"+paramObj["value"]+"']").prop("checked","true");
						break;
					}
					case "checkbox":{
						var valueArr = paramObj["value"];
						for(var value in valueArr){
							$("#preViewForm").find("[name='"+name+"'][value='"+valueArr[value]+"']").prop("checked","true");
						}
						break;
					}
				}
				break;
			};
			case "SELECT":{
				var value = paramObj["value"];
				$("#preViewForm").find("[name='"+name+"']").val(value);
				break;
			};
			case "TEXTAREA":{
				var value = paramObj["value"];
				$("#preViewForm").find("[name='"+name+"']").val(value);
				break;
			}
		}
	}//end for
}

//给隐藏的打印div删掉不需打印的元素
function prePrint(){
	var view = $("#preViewForm");
	var tableArr = view.find("table");
	tableArr.each(function(){
		var print = $(this).attr("print");
		if(print=="no"){
			$(this).remove();
		}
	});
	
	var pArr = view.find("p");
	pArr.each(function(){
		var print = $(this).attr("print");
		if(print=="no"){
			$(this).remove();
		}
	});
	
	var subTdArr = view.find(".form-sub td");
	subTdArr.each(function(){
		var tdObj = $(this);
		if(tdObj.attr("class")=="td_sub"){
			var fieldValue = "";
			var fieldCodeName = "";
			if(tdObj.find("input[type='text']").length!=0){
				fieldValue = tdObj.find("input[type='text']").val();
				fieldCodeName = tdObj.find("input[type='text']").attr("name");
			}
			if(tdObj.find("input[type='tel']").length!=0){
				fieldValue = tdObj.find("input[type='tel']").val();
				fieldCodeName = tdObj.find("input[type='tel']").attr("name");
			} 
			if(tdObj.find("input[type='date']").length!=0){
				fieldValue = tdObj.find("input[type='date']").val();
				fieldCodeName = tdObj.find("input[type='date']").attr("name");
			}
			if(tdObj.find("input[type='radio']").length!=0){
				if(tdObj.find("input[type='radio']:checked").length>0){
					fieldValue = tdObj.find("input[type='radio']:checked").attr("title");
				}
				fieldCodeName = tdObj.find("input[type='radio']").attr("name");
			}
			if(tdObj.find("input[type='checkbox']").length!=0){
				if(tdObj.find("input[type='checkbox']:checked").length>0){
					var checkArr = tdObj.find("input[type='checkbox']:checked");
					checkArr.each(function(){
						fieldValue += $(this).attr("title")+";";
					});
				}
				fieldCodeName = tdObj.find("input[type='checkbox']").attr("name");
			}
			if(tdObj.find("select").length!=0){
				fieldValue = tdObj.find("select").next().find("input").val();
				fieldCodeName = tdObj.find("select").attr("name");
			}
			if(tdObj.find("textarea").length!=0){
				fieldValue = tdObj.find("textarea").val();
				fieldCodeName = tdObj.find("textarea").attr("name");
			}
			var name = fieldCodeName;
			if(view.find("[name='"+name+"']").attr("print")=="no"){
				var tagType = view.find("[name='"+name+"']").attr("type");
				view.find("[name='"+name+"']").parent().prev().remove();
				tdObj.remove();
				if(tagType=="radio" || tagType=="checkbox"){
					view.find("[name='"+name+"']").parent().prev().remove();
					tdObj.remove();
				}
			}else{
				if(fieldValue!="" && fieldValue!=null){
					tdObj.html(fieldValue);
				}else{
					view.find("[name='"+name+"']").attr("disabled","true");
					view.find("[name='"+name+"']").removeAttr("placeholder");
					var tagName = view.find("[name='"+name+"']").prop("tagName");
					var tagType = view.find("[name='"+name+"']").attr("type");
					var className = view.find("[name='"+name+"']").attr("class");
					if(tagType=="checkbox"){
						view.find("[name='"+name+"']").attr("disabled","true");
					}
					if(tagType=="radio"){
						view.find("[name='"+name+"']").attr("disabled","true");
						if(view.find("[name='"+name+"']:checked").length>0){
							var title = view.find("[name='"+name+"']:checked").attr("title");
							view.find("[name='"+name+"']").parent().html("<span style='margin-left:10px;'>"+title+"</span>");
						}else{
							view.find("[name='"+name+"']").parent().html("<input class='layui-input' disabled/>");
						}
					}
					if(tagName=="SELECT"){
						view.find("[name='"+name+"']").attr("disabled","true");
						view.find("[name='"+name+"']").next().find("input").attr("disabled","true");
						view.find("[name='"+name+"']").next().find("input").removeAttr("placeholder");
						view.find("[name='"+name+"']").next().find(".layui-edge").css("display","none");
					}
					if(view.find("[name='"+name+"']").attr("title")=="choose_user" 
						|| view.find("[name='"+name+"']").attr("title")=="choose_value"
						|| view.find("[name='"+name+"']").attr("title")=="choose_depart"){
						view.find("[name='"+name+"']").parent().find("i").css("display","none");
						view.find("[name='"+name+"']").css("width","100%");
					}
					if(className=="layui-input date"){
						view.find("[name='"+name+"']").attr("disabled","true");
						if(view.find("[name='"+name+"']").val()=="" || view.find("[name='"+name+"']").val()==null){
							view.find("[name='"+name+"']").prop("type","text");
						}
					}
				} //end if value is null
			}//end if is print
		}
	});
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