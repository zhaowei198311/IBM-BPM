layui.use('form',function(){
	var form = layui.form;
	form.on('radio(templateTypeFilter)', function(data) {
		if (data.value == "MAIL_NOTIFY_TEMPLATE") {
			$('#templateSubjectDiv').show();
		} else {
			$("#templateSubject").val("");
			$('#templateSubjectDiv').hide();
		}
	});
});
//为翻页提供支持
var pageConfig = {
	pageNum : 1,
	pageSize : 8,
	templateName: "",
	templateType : "",
	total : 0
}

$(document).ready(function() {
	// 加载数据
	pageNotifyTemplate();

})

//分页
function doPage() {
	layui.use([ 'laypage', 'layer' ], function() {
		var laypage = layui.laypage, layer = layui.layer;
		//完整功能
		laypage.render({
			elem : 'lay_page',
			curr : pageConfig.pageNum,
			count : pageConfig.total,
			limit : pageConfig.pageSize,
			layout : [ 'count', 'prev', 'page', 'next', 'limit', 'skip' ],
			jump : function(obj, first) {
				// obj包含了当前分页的所有参数  
				pageConfig.pageNum = obj.curr;
				pageConfig.pageSize = obj.limit;
				if (!first) {
					pageNotifyTemplate();
				}
			}
		});
	});
}
function pageNotifyTemplate(){
	$.ajax({
		url : common.getPath()+'/dhNotifyTemplate/pageNotifyTemplateList',
		type : 'post',
		dataType : 'json',
		data : {
			pageNum : pageConfig.pageNum,
			pageSize : pageConfig.pageSize,
			templateName: pageConfig.templateName,
			templateType : pageConfig.templateType
		},
		beforeSend: function(){
			layer.load(1);
		},
		success : function(result){
			if (result.status == 0) {
				drawTable(result.data);
			}
			layer.closeAll("loading");
		},error : function(){
			layer.closeAll("loading");
		}
	})
}

function drawTable(pageInfo, data) {
	pageConfig.pageNum = pageInfo.pageNum;
	pageConfig.pageSize = pageInfo.pageSize;
	pageConfig.total = pageInfo.total;
	doPage();
	// 渲染数据
	$("#template_table_tbody").html('');
	if (pageInfo.total == 0) {
		return;
	}

	var list = pageInfo.list;
	var startSort = pageInfo.startRow;//开始序号
	var trs = "";
	var type = "";
	var status = "";
	var delegateFlag= "";
	for (var i = 0; i < list.length; i++) {
		var item = list[i];
		var sortNum = startSort + i;
		var notifyTemplateType = "";
		if(item.templateType == "MAIL_NOTIFY_TEMPLATE"){
			notifyTemplateType = "邮件模板";
		}else if(item.templateType == "MESSAGE_NOTIFY_TEMPLATE"){
			notifyTemplateType = "短信模板";
		}
		trs += '<tr>'
				+'<td>'
				+ sortNum 
				+ '</td>' 
				+ '<td>'
				+ item.templateName
				+ '</td>' 
				+ '<td data-templatetype="'+item.templateType+'">'
				+ notifyTemplateType
				+ '</td>'
				+ '<td>';
				if(item.templateSubject!=null && item.templateSubject!=""){
					trs += item.templateSubject;
					}
				trs += '</td>'
				+ '<td><pre style="display: none;">'
				+ item.templateContent
				+ '</pre><a style="cursor:pointer" onclick="showNotifyTemplate(this);">点击查看</a></td>' 
				+ '<td>';
			trs += item.userName
				+ '</td>' 
				+ '<td>'
				+ common.dateToString(new Date(item.createTime))
				+ '</td>'
				+ '<td>';
				if(item.updateUserName!=null && item.updateUserName!=""){
					trs += item.updateUserName;
				}
				trs += '</td>'
				+ '<td>';
				if(item.updateTime!=null && item.updateTime!=""){
					trs += common.dateToString(new Date(item.updateTime));
				}
				trs += '</td><td>'	
					+ '<i class="layui-icon" data-templateuid="'+item.templateUid+'"  title="修改模板"  onclick=updateNotifyTemplate(this) >&#xe642;</i>'
		            + '<i class="layui-icon"  title="删除模板"  onclick=deleteNotifyTemplate("'
		            + item.templateUid + '") >&#xe640;</i>'
				+'</td>'
				+ '</tr>';
	}
	$("#template_table_tbody").append(trs);

}
//模糊查询
function search(){
	pageConfig.templateName = $("#template-name-search").val();
	pageConfig.templateType = $("#template-type-search").val();

	pageNotifyTemplate();
}

function invertSelection(a){ 
	var checkeNodes= $("input[type='checkbox'][name='checkNotifyTemplate']"); 
	if($(a).prop("checked")==true){
	checkeNodes.prop("checked",false);
	$(a).prop("checked",true);
	}

};

function addNotifyTemplate(){
	$("#operation-title").text("新增通知模板");
	$("#submitOperation").text("新增");
	$("#submitOperation").css("display","inline-block");
	$(".cancel_btn").text("取消");
	$("#templateUid").val("");
	$("input[name='templateName']").val("");
	$("input[name='templateType'][value='MAIL_NOTIFY_TEMPLATE']").prop("checked",true);
	$("#templateSubject").val("");
	$("#templateContent").val("");
	layui.form.render("radio");
	layui.event.call($("#templateSubjectDiv"),'form','radio(templateTypeFilter)',{"value":"MAIL_NOTIFY_TEMPLATE"});
	$("#notifyt-template-div").show();
}

function updateNotifyTemplate(a){
	$("#operation-title").text("修改通知模板");
	$("#submitOperation").text("保存");
	$("#submitOperation").css("display","inline-block");
	$(".cancel_btn").text("取消");
	/*var checkedElement = $("input[name='checkNotifyTemplate']:checked");
	if(checkedElement.length!=1){
		layer.alert("请选择一个模板记录");
		return;
	}*/
	
	var tr= $(a).parent().parent();
	$("input[name='templateName']").val(tr.find("td").eq(1).text());
	$("#templateContent").val(tr.find("td").eq(4).find("pre").text());
	var templateType = tr.find("td").eq(2).data("templatetype");
	$("input[name='templateType'][value='"+templateType+"']").prop("checked",true);
	$("#templateUid").val($(a).data("templateuid"));
	$("#templateSubject").val(tr.find("td").eq(3).text());
	layui.form.render("radio");
	layui.event.call($("#templateSubjectDiv"),'form','radio(templateTypeFilter)',{"value":templateType});
	$("#notifyt-template-div").show();
}

function deleteNotifyTemplate(templateUid){
	layer.confirm('确认要删除吗？',
        {btn : [ '确定', '取消' ]},function(index){
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
				layer.close(index);
			},error : function(){
				layer.alert("删除模板出现异常");
				layer.closeAll("loading");
				layer.close(index);
			}
			});
	});
}

function showNotifyTemplate(a){
	$("#operation-title").text("查看通知模板");
	$("#submitOperation").css("display","none");
	$(".cancel_btn").text("关闭");
	var tr= $(a).parent().parent();
	$("input[name='templateName']").val(tr.find("td").eq(1).text());
	$("#templateContent").val(tr.find("td").eq(4).find("pre").text());
	var templateType = tr.find("td").eq(2).data("templatetype");
	$("#templateSubject").val(tr.find("td").eq(3).text());
	$("input[name='templateType'][value='"+templateType+"']").prop("checked",true);
	layui.form.render("radio");
	layui.event.call($("#templateSubjectDiv"),'form','radio(templateTypeFilter)',{"value":templateType});
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
		templateSubject : {
			required : function(element) {
				return $("input[name='templateType']:checked").val()=='MAIL_NOTIFY_TEMPLATE';
			}
		},
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

var transferNotifyTemplate = {
    URL: {
        exportNotifyTemplate: common.getPath() + '/transfer/exportNotifyTemplate',
        tryImportNotifyTemplate: common.getPath() + '/transfer/tryImportNotifyTemplate',
        sureImportNotifyTemplate: common.getPath() + '/transfer/sureImportNotifyTemplate',
        cancelImportNotifyTemplate: common.getPath() + '/transfer/cancelImportTransferData'
    },
    init: function () {
        // 导入按钮
        layui.use('upload', function () {
            layui.upload.render({
                elem: $("#importBtn"),
                url: transferNotifyTemplate.URL.tryImportNotifyTemplate,
                data: {},
                exts: "json",
                field: "file",
                before: function (obj) {
                    layer.load(1);
                },
                done: function (result) {
                    layer.closeAll('loading');
                    if (result.status == 0) {
                        var data = result.data;
                        if (data.exists == 'FALSE') {
                            // 新的通知模版
                            var confirmIndex = layer.confirm('<p>请确认导入通知模版</p><p><b>通知模版标题：</b>' + data.templateName + '</p>', {
                                btn: ['导入', '取消']
                            }, function () {
                                transferNotifyTemplate.importNotifyTemplate();
                                layer.close(confirmIndex); // 关闭confirm层
                            }, function () {
                                $.post(transferNotifyTemplate.URL.cancelImportNotifyTemplate);
                            });
                        } else {
                            // 发现已有此通知模版
                            var confirmIndex = layer.confirm('<p>通知模版已存在，<b style="color:red;">是否覆盖配置</b></p><p><b>通知模版标题：</b>' + data.templateName + '</p>', {
                                btn: ['覆盖', '取消']
                            }, function () {
                                transferNotifyTemplate.importNotifyTemplate();
                                layer.close(confirmIndex); // 关闭confirm层
                            }, function () {
                                $.post(transferNotifyTemplate.URL.cancelImportNotifyTemplate);
                            });
                        }
                    } else {
                        layer.alert(result.msg);
                    }
                },
                error: function (result) {
                    layer.closeAll('loading');
                    layer.alert(result.msg);
                }
            });
        });
        //导出按钮
        $('#exportBtn').click(transferNotifyTemplate.exportNotifyTemplate);

    },
    exportNotifyTemplate: function (templateUid) {
        var $cks = $('input[name="checkNotifyTemplate"]:checked');
        if ($cks.length == 0 || $cks.length > 1) {
            layer.alert('请选择一个通知模版');
            return;
        }
        var templateUid = $cks.eq(0).val();
        common.downLoadFile(transferNotifyTemplate.URL.exportNotifyTemplate, {'templateUid': templateUid});
    },
    importNotifyTemplate: function () {
        $.ajax({
            url : transferNotifyTemplate.URL.sureImportNotifyTemplate,
            type : 'post',
            dataType : 'json',
            data : {},
            before: function () {
                layer.load(1);
            },
            success : function (result) {
                layer.closeAll('loading');
                if(result.status == 0 ){
                    pageNotifyTemplate();
                    layer.alert('导入成功');
                }else{
                    layer.alert(result.msg);
                }
            },
            error : function () {
                layer.closeAll('loading');
                layer.alert('导入失败，请稍后再试');
            }
        });
    }

};