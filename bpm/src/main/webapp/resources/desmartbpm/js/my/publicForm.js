var oldFormName = "";//修改表单信息时表单的旧名称
var oldFormDescription = "";//修改表单信息时的旧描述
var oldFormCode = "";//修改表单信息时的旧表单编码
var updateFormId = "";//修改表单时表单的Id
var copuFormId = "";//复制表单时表单的Id

//分页控件的参数
var pageConfig = {
	pageNum : 1,
	pageSize : 10,
	total : 0
}

$(function() {
	$("add_form_code").onlyNumAlpha();
	$("update_form_code").onlyNumAlpha();
	$("copy_form_code").onlyNumAlpha();
	
	queryFormByName();
	searchForm();
	
	$(".cancel_btn").click(function(){
		$(".display_container").css("display","none");
		$(".display_container1").css("display","none");
		$(".display_container2").css("display","none");
	});

    transferPublicForm.init();
});

//查询所有的公共表单数据
function queryFormByName() {
	$.ajax({
		url : common.getPath() + "/publicForm/listFormByFormName",
		method : "post",
		data : {
			formName : $("#search_form_name").val().trim(),
			pageNum : pageConfig.pageNum,
			pageSize : pageConfig.pageSize
		},
		success : function(result) {
			drawTable(result.data);
		}
	});
}
//表单信息模糊查询
function searchForm() {
	$("#search_btn").click(function() {
		queryFormByName();
	});
}

//渲染数据表格
function drawTable(pageInfo) {
	pageConfig.pageNum = pageInfo.pageNum;
	pageConfig.pageSize = pageInfo.pageSize;
	pageConfig.total = pageInfo.total;
	doPage();
	// 渲染数据
	$("#form_table_tbody").html('');
	if (pageInfo.total == 0) {
		return;
	}

	var list = pageInfo.list;
	var startSort = pageInfo.startRow;//开始序号
	var trs = "";
	for (var i = 0; i < list.length; i++) {
		var formInfo = list[i];
		var sortNum = startSort + i;
		var createTime = common.dateToString(new Date(formInfo.createTime));
		trs += '<tr data-formuid="'
				+ formInfo.publicFormUid
				+ '">'
				+ '<td><input type="checkbox" name="formInfo_check" onclick="onClickSel(this);" value="'
				+ formInfo.publicFormUid + '" lay-skin="primary"> ' + sortNum
				+ '</td>' 
				+ '<td>' + formInfo.publicFormName + '</td>'
				+ '<td>' + formInfo.publicFormCode + '</td>';
		if (formInfo.publicFormDescription != null
				&& formInfo.publicFormDescription != "") {
			trs += '<td>' + formInfo.publicFormDescription + '</td>';
		} else {
			trs += '<td></td>';
		}
		trs += '<td>'
				+ createTime
				+ '</td>'
				+ '<td>'
				+ formInfo.creatorName
				+ '</td>'
				+ '<td><i class="layui-icon" onclick="updateFormModal(this);" title="修改表单属性">&#xe642;</i>'
                + ' <i class="layui-icon" onclick="transferPublicForm.exportPublicForm(\'' + formInfo.publicFormUid + '\')" title="导出表单">&#xe601;</i>'
				+ ' <i class="layui-icon" onclick="updateFormContent(this);" title="修改表单内容">&#xe60a;</i></td>'
				+ '</tr>';
	}
	$("#form_table_tbody").append(trs);
}

//复选框全选，取消全选
function onClickHander(obj){
	if(obj.checked){
		$("input[name='formInfo_check']").prop("checked",true);
	}else{
		$("input[name='formInfo_check']").prop("checked",false);
	}
}

//复选框分选
function onClickSel(obj){
	if(obj.checked){
		var allSel = false;
		$("input[name='formInfo_check']").each(function(){
			if(!$(this).is(":checked")){
				allSel = true;
			}
		});
		
		//如果有checkbox没有被选中
		if(allSel){
			$("input[name='allSel']").prop("checked",false);
		}else{
			$("input[name='allSel']").prop("checked",true);
		}
	}else{
		$("input[name='allSel']").prop("checked",false);
	}
}

//渲染分页插件
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
					$("input[name='allSel']").prop("checked", false);
					queryFormByName();
				}
			}
		});
	});
}

function showCreateFormModal() {
	$("#add_form_name").val("");
	$("#add_form_code").val("");
	$("#add_form_description").val("");
	$(".display_container").css("display", "block");
}

function saveForm(){
	var addFormName = $("#add_form_name").val().trim();
	var	addFormDescription = $("#add_form_description").val().trim();
	var addFormCode = $("#add_form_code").val().trim();
	if(addFormName!=null && addFormName!="" && addFormCode!="" && addFormCode!=null){
		layer.load(1);
		$.ajax({
			url:common.getPath()+"/publicForm/queryFormByFormNameAndCode",
			method:"post",
			data:{
				formName:addFormName,
				formCode:addFormCode
			},
			success:function(result){
				if(result.status==0){
					var href = "/publicForm/designForm?formName="+addFormName
							+"&formDescription="+addFormDescription
							+"&formCode="+addFormCode;
					window.location.href = common.getPath()+href;
					$(".display_container").css("display", "none");
				}else{
					layer.alert(result.msg);
				}
				layer.closeAll("loading");
			}
		});
	}else{
		layer.alert("输入的表单名和表单编码不能为空");
	}
}

//修改公共表单的内容
function updateFormContent(obj){
	var trObj = $(obj).parent().parent();
	var formId = trObj.data("formuid");
	var formName = $(trObj.find("td")[1]).text().trim();
	var formCode = $(trObj.find("td")[2]).text().trim();
	var formDescription = $(trObj.find("td")[3]).text().trim();
	var href = "/publicForm/designForm?formUid="+formId
			+"&formName="+formName
			+"&formCode="+formCode
			+"&formDescription="+formDescription;
	window.location.href = common.getPath()+href;
}

//修改表单属性的模态框
function updateFormModal(obj){
	var trObj = $(obj).parent().parent();
	updateFormId = trObj.data("formuid");
	var formName = $(trObj.find("td")[1]).text().trim();
	var formCode = $(trObj.find("td")[2]).text().trim();
	var formDescription = $(trObj.find("td")[3]).text().trim();
	oldFormDescription = formDescription;
	oldFormName = formName;
	oldFormCode = formCode;
	$("#update_form_name").val(oldFormName);
	$("#update_form_code").val(oldFormCode);
	$("#update_form_description").val(oldFormDescription);
	$(".display_container1").css("display", "block");
}

//修改表单的属性
function updateForm(){
	var formName = $("#update_form_name").val().trim();
	var formCode = $("#update_form_code").val().trim();
	var formDescription = $("#update_form_description").val().trim();
	if(formName==null || formName=="" || formCode=="" || formCode==null){
		layer.alert("请填写表单名和表单编码");
	}else if(oldFormName==formName && oldFormDescription==formDescription && oldFormCode==formCode){
		$(".display_container1").css("display", "none");
	}else if(oldFormName==formName && oldFormCode==formCode){
		$.ajax({
			url:common.getPath()+"/publicForm/updateFormInfo",
			method:"post",
			data:{
				publicFormUid:updateFormId,
				publicFormName:formName,
				publicFormCode:formCode,
				publicFormDescription:formDescription
			},
			success:function(result2){
				if(result2.status==0){
					layer.alert("表单属性修改成功");
					queryFormByName();
					$(".display_container1").css("display", "none");
				}else{
					layer.alert("表单属性修改失败");
				}
			}
		});
	}else if(oldFormName==formName){
		$.ajax({
			url:common.getPath()+"/publicForm/queryFormByFormNameAndCode",
			method:"post",
			data:{
				formCode:formCode
			},
			success:function(result){
				if(result.status==0){
					$.ajax({
						url:common.getPath()+"/publicForm/updateFormInfo",
						method:"post",
						data:{
							publicFormUid:updateFormId,
							publicFormName:formName,
							publicFormCode:formCode,
							publicFormDescription:formDescription
						},
						success:function(result2){
							if(result2.status==0){
								layer.alert("表单属性修改成功");
								queryFormByName();
								$(".display_container1").css("display", "none");
							}else{
								layer.alert("表单属性修改失败");
							}
						}
					});
				}else{
					layer.alert(result.msg);
				}
			}
		});
	}else if(oldFormCode==formCode){
		$.ajax({
			url:common.getPath()+"/publicForm/queryFormByFormNameAndCode",
			method:"post",
			data:{
				formName:formName
			},
			success:function(result){
				if(result.status==0){
					$.ajax({
						url:common.getPath()+"/publicForm/updateFormInfo",
						method:"post",
						data:{
							publicFormUid:updateFormId,
							publicFormName:formName,
							publicFormCode:formCode,
							publicFormDescription:formDescription
						},
						success:function(result2){
							if(result2.status==0){
								layer.alert("表单属性修改成功");
								queryFormByName();
								$(".display_container1").css("display", "none");
							}else{
								layer.alert("表单属性修改失败");
							}
						}
					});
				}else{
					layer.alert(result.msg);
				}
			}
		});
	}else{
		$.ajax({
			url:common.getPath()+"/publicForm/queryFormByFormNameAndCode",
			method:"post",
			data:{
				formName:formName,
				formCode:formCode
			},
			success:function(result){
				if(result.status==0){
					$.ajax({
						url:common.getPath()+"/publicForm/updateFormInfo",
						method:"post",
						data:{
							publicFormUid:updateFormId,
							publicFormName:formName,
							publicFormCode:formCode,
							publicFormDescription:formDescription
						},
						success:function(result2){
							if(result2.status==0){
								layer.alert("表单属性修改成功");
								queryFormByName();
								$(".display_container1").css("display", "none");
							}else{
								layer.alert("表单属性修改失败");
							}
						}
					});
				}else{
					layer.alert(result.msg);
				}
			}
		});
	}
}

//删除表单
function deleteForm(){
	var checkedFormArr = $("input[name='formInfo_check']:checked");
	var checkedFormUids = new Array();
	if(checkedFormArr.length>0){
		for(var i=0;i<checkedFormArr.length;i++){
			var checkedForm = checkedFormArr[i];
			checkedFormUids.push($(checkedForm).val());
		}
		$.ajax({
			url:common.getPath()+"/publicForm/isBindMainForm",
			method:"post",
			data:{
				"formUids":checkedFormUids
			},
			traditional: true,
			success:function(result){
				if(result.status==0){//未绑定
					layer.load(1);
					$.ajax({
						url:common.getPath()+"/publicForm/deleteForm",
						method:"post",
						traditional:true,
						data:{
							"formUids":checkedFormUids
						},
						success:function(result){
							if(result.status==0){
								queryFormByName();
								layer.alert("删除成功");
							}else{
								layer.alert("删除失败");
							}
							layer.closeAll("loading");
						}
					});
				}else{
					layer.alert("该子表单已被主表单绑定");
				}
			}
		});
	}else{
		layer.alert("请选中要删除的表单");
	}
}

//显示复制表单的模态框
function showCopyFormModal(){
	var checkedForm = $("input[name='formInfo_check']:checked");
	if(checkedForm.length==1){
		copyFormId = checkedForm.val();
		var formName = checkedForm.parent().parent().find("td:eq(1)").text()+"_copy"+_getRandomString(2);
		var formCode = checkedForm.parent().parent().find("td:eq(2)").text()+"_copy"+_getRandomString(2);
		var formDescription = checkedForm.parent().parent().find("td:eq(3)").text();
		$("#copy_form_name").val(formName);
		$("#copy_form_code").val(formCode);
		if(formDescription!=null && formDescription!=""){
			$("#copy_form_description").val(formDescription+"_copy");
		}else{
			$("#copy_form_description").val("");
		}
		$(".display_container2").css("display","block");
	}else if(checkedForm.length<1){
		layer.alert("请选择一个要复制的表单");
	}else{
		layer.alert("一次只能复制一个表单");
	}
}

//复制表单
function copyForm(){
	layer.load(1);
	var copyFormName = $("#copy_form_name").val().trim();
	var copyFormCode = $("#copy_form_code").val().trim();
	var copyFormDescription = $("#copy_form_description").val().trim();
	if(copyFormName==null || copyFormName=="" || copyFormCode==null || copyFormCode==""){
		layer.alert("请填写表单名和表单编码");
	}else{
		$.ajax({
			url:common.getPath()+"/publicForm/queryFormByFormNameAndCode",
			method:"post",
			data:{
				formName:copyFormName,
				formCode:copyFormCode
			},
			success:function(result){
				if(result.status==0){
					$.ajax({
						url:common.getPath()+"/publicForm/copyForm",
						method:"post",
						data:{
							publicFormUid:copyFormId,
							publicFormName:copyFormName,
							publicFormCode:copyFormCode,
							publicFormDescription:copyFormDescription
						},
						success:function(result){
							if(result.status==0){
								queryFormByName();
								layer.alert("复制成功");
							}else{
								layer.alert("复制失败");
							}
							$(".display_container2").css("display","none");
						}
					});
				}else{
					layer.alert(result.msg);
				}
			}
		});//end ajax
	}
	layer.closeAll("loading");
}

//生成随机码的方法
function _getRandomString(len) {  
    len = len || 32;  
    var $chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'; // 默认去掉了容易混淆的字符oOLl,9gq,Vv,Uu,I1  
    var maxPos = $chars.length;  
    var pwd = '';  
    for (i = 0; i < len; i++) {  
        pwd += $chars.charAt(Math.floor(Math.random() * maxPos));  
    }  
    return pwd;  
} 

//只能输入英文和数字
$.fn.onlyNumAlpha = function () {
	var oldValue = "";
	$(this).keydown(function(event){
		oldValue = this.value;
	});
	$(this).keyup(function (event) {
		if(!/^[A-Za-z0-9_-]+$/.test(this.value)) {
			if($(this).val().trim()!="" && $(this).val().trim()!=null){
				this.value=oldValue;
			}
		}
	});
};

var transferPublicForm = {
    URL: {
        exportPublicForm: common.getPath() + '/transfer/exportPublicForm',
        tryImportPublicForm: common.getPath() + '/transfer/tryImportPublicForm',
        sureImportPublicForm: common.getPath() + '/transfer/sureImportPublicForm',
        cancelImportPublicForm: common.getPath() + '/transfer/cancelImportTransferData'
    },
    init: function () {
        // 导入按钮
        layui.use('upload', function () {
            layui.upload.render({
                elem: $("#importBtn"),
                url: transferPublicForm.URL.tryImportPublicForm,
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
                            // 新的子表单
                            var confirmIndex = layer.confirm('<p>请确认导入子表单</p><p><b>子表单标题：</b>' + data.formName + '</p>', {
                                btn: ['导入', '取消']
                            }, function () {
                                transferPublicForm.importPublicForm();
                                layer.close(confirmIndex); // 关闭confirm层
                            }, function () {
                                $.post(transferPublicForm.URL.cancelImportPublicForm);
                            });
                        } else {
                            // 发现已有此子表单
                            var confirmIndex = layer.confirm('<p>子表单已存在，<b style="color:red;">是否覆盖配置</b></p><p><b>子表单标题：</b>' + data.formName + '</p>', {
                                btn: ['覆盖', '取消']
                            }, function () {
                                transferPublicForm.importPublicForm();
                                layer.close(confirmIndex); // 关闭confirm层
                            }, function () {
                                $.post(transferPublicForm.URL.cancelImportPublicForm);
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

    },
    exportPublicForm: function (publicFormUid) {
        if (!publicFormUid) {
            layer.alert('出错了，请刷新页面再试');
        }
        common.downLoadFile(transferPublicForm.URL.exportPublicForm, {'publicFormUid': publicFormUid});
    },
    importPublicForm: function () {
        $.ajax({
            url : transferPublicForm.URL.sureImportPublicForm,
            type : 'post',
            dataType : 'json',
            data : {},
            before: function () {
                layer.load(1);
            },
            success : function (result) {
                layer.closeAll('loading');
                if(result.status == 0 ){
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