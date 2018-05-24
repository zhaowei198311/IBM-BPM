var oldFormName = "";//修改表单信息时表单的旧名称
var oldFormDescription = "";//修改表单信息时的旧描述
var updateFormId = "";//修改表单时表单的Id

//分页控件的参数
var pageConfig = {
	pageNum : 1,
	pageSize : 10,
	total : 0
}

$(function() {
	queryFormByName();
	searchForm();
	
	$(".cancel_btn").click(function(){
		$(".display_container").css("display","none");
		$(".display_container1").css("display","none");
	});
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
				+ formInfo.dynUid + '" lay-skin="primary"> ' + sortNum
				+ '</td>' + '<td>' + formInfo.publicFormName + '</td>';
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
	$("#add_form_name").val("");
	$(".display_container").css("display", "block");
}

function saveForm(){
	var addFormName = $("#add_form_name").val().trim();
	var	addFormDescription = $("#add_form_description").val().trim();
	if(addFormName!=null && addFormName!=""){
		$.ajax({
			url:common.getPath()+"/publicForm/queryFormByFormName",
			method:"post",
			data:{
				formName:addFormName
			},
			success:function(result){
				if(result.status==0){
					var href = "/publicForm/designForm?formName="+addFormName
							+"&formDescription="+addFormDescription
					window.location.href = common.getPath()+href;
					$(".display_container").css("display", "none");
				}else{
					layer.alert(result.msg);
				}
			}
		});
	}else{
		layer.alert("输入的表单名不能为空");
	}
}

//修改公共表单的内容
function updateFormContent(obj){
	var trObj = $(obj).parent().parent();
	var formId = trObj.data("formuid");
	var formName = $(trObj.find("td")[1]).text().trim();
	var formDescription = $(trObj.find("td")[2]).text().trim();
	var href = "/publicForm/designForm?formUid="+formId
			+"&formName="+formName
			+"&formDescription="+formDescription;
	window.location.href = common.getPath()+href;
}

//修改表单属性的模态框
function updateFormModal(obj){
	var trObj = $(obj).parent().parent();
	updateFormId = trObj.data("formuid");
	var formName = $(trObj.find("td")[1]).text().trim();
	var formDescription = $(trObj.find("td")[2]).text().trim();
	oldFormDescription = formDescription;
	oldFormName = formName;
	$("#update_form_name").val(oldFormName);
	$("#update_form_description").val(oldFormDescription);
	$(".display_container1").css("display", "block");
}

//修改表单的属性
function updateForm(){
	var formName = $("#update_form_name").val().trim();
	var formDescription = $("#update_form_description").val().trim();
	if(formName==null || formName==""){
		layer.alert("请填写表单名");
	}else if(oldFormName==formName && oldFormDescription==formDescription){
		$(".display_container1").css("display", "none");
	}else if(oldFormName==formName && oldFormDescription!=formDescription){
		$.ajax({
			url:common.getPath()+"/publicForm/updateFormInfo",
			method:"post",
			data:{
				publicFormUid:updateFormId,
				publicFormName:formName,
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
		$.ajax({
			url:common.getPath()+"/publicForm/queryFormByFormName",
			method:"post",
			data:{
				publicFormName:formName
			},
			success:function(result){
				if(result.status==0){
					$.ajax({
						url:common.getPath()+"/publicForm/updateFormInfo",
						method:"post",
						data:{
							publicFormUid:updateFormId,
							publicFormName:formName,
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
					layer.alert("表单名已存在，不能重复");
				}
			}
		});
	}
}