var triggerToEdit = "";
var preFormData;
var stepUidToEdit;
var editIndex;
var saveFrom; // 触发保存的元素 saveBtn activityLi gatewayLi stepLi

var dates = "";// 查询权限信息时，存储当前步骤和表单id
var pageConfig = {
	pageNum : 1,
	pageSize : 5,
	total : 0,
	triTitle : "",
	triType : ""
};

layui.use('form', function() {
	var form = layui.form;
	form.on('select(assignType)', function(data) {
		if (data.value == "role" || data.value == "roleAndDepartment"
				|| data.value == "roleAndCompany") {
			$("#handleRole_div").show();
			$("#handleTeam_div").hide();
			$("#handleUser_div").hide();
			$("#handleField_div").hide();
		} else if (data.value == "team" || data.value == "teamAndDepartment"
				|| data.value == "teamAndCompany") {
			$("#handleUser_div").hide();
			$("#handleTeam_div").show();
			$("#handleRole_div").hide();
			$("#handleField_div").hide();
		} else if (data.value == "leaderOfPreActivityUser"
				|| data.value == "processCreator" || data.value == "none") {
			$("#handleRole_div").hide();
			$("#handleTeam_div").hide();
			$("#handleUser_div").hide();
			$("#handleField_div").hide();
		} else if (data.value == "users") {
			$("#handleRole_div").hide();
			$("#handleTeam_div").hide();
			$("#handleUser_div").show();
			$("#handleField_div").hide();
		} else if (data.value == "byField") {
			$("#handleRole_div").hide();
			$("#handleTeam_div").hide();
			$("#handleUser_div").hide();
			$("#handleField_div").show();
		}
	});

	form.on('select(chooseableHandlerType)', function(data) {
		if (data.value == "role" || data.value == "roleAndDepartment"
				|| data.value == "roleAndCompany") {
			$("#chooseableHandleRole_div").show();
			$("#chooseableHandleTeam_div").hide();
			$("#chooseableHandleUser_div").hide();
			$("#chooseableHandleField_div").hide();
			$("#chooseableHandleTrigger_div").hide();
		} else if (data.value == "team" || data.value == "teamAndDepartment"
				|| data.value == "teamAndCompany") {
			$("#chooseableHandleUser_div").hide();
			$("#chooseableHandleTeam_div").show();
			$("#chooseableHandleRole_div").hide();
			$("#chooseableHandleField_div").hide();
			$("#chooseableHandleTrigger_div").hide();
		} else if (data.value == "leaderOfPreActivityUser"
				|| data.value == "processCreator" || data.value == "none") {
			$("#chooseableHandleRole_div").hide();
			$("#chooseableHandleTeam_div").hide();
			$("#chooseableHandleUser_div").hide();
			$("#chooseableHandleField_div").hide();
			$("#chooseableHandleTrigger_div").hide();
		} else if (data.value == "users") {
			$("#chooseableHandleRole_div").hide();
			$("#chooseableHandleTeam_div").hide();
			$("#chooseableHandleUser_div").show();
			$("#chooseableHandleField_div").hide();
			$("#chooseableHandleTrigger_div").hide();
		} else if (data.value == "byField") {
			$("#chooseableHandleRole_div").hide();
			$("#chooseableHandleTeam_div").hide();
			$("#chooseableHandleUser_div").hide();
			$("#chooseableHandleField_div").show();
			$("#chooseableHandleTrigger_div").hide();
		} else if (data.value == "allUser") {
			$("#chooseableHandleRole_div").hide();
			$("#chooseableHandleTeam_div").hide();
			$("#chooseableHandleUser_div").hide();
			$("#chooseableHandleField_div").hide();
			$("#chooseableHandleTrigger_div").hide();
		} else if (data.value = 'byTrigger') {
			$("#chooseableHandleRole_div").hide();
			$("#chooseableHandleTeam_div").hide();
			$("#chooseableHandleUser_div").hide();
			$("#chooseableHandleField_div").hide();
			$("#chooseableHandleTrigger_div").show();
		}
	});

	form.on('select(rejectType)', function(data) {
		if (data.value == "toActivities") {
			$("#rejectActivities_div").show();
		} else {
			$("#rejectActivities_div").hide();
		}
	});

	form.on('radio(canReject)', function(data) {
		if (data.value == "TRUE") {
			$("#rejectType_div").show();
			if ($('select[name="actcRejectType"]').val() == 'toActivities') {
				$("#rejectActivities_div").show();
			} else {
				$("#rejectActivities_div").hide();
			}
		} else {
			$("#rejectType_div").hide();
			$("#rejectActivities_div").hide();
		}
	});
	// 切换新增步骤类型
	form.on('radio(stepTypeFilter)', function(data) {
		if (data.value == "form") {
			$("#form_innerArea").show();
			$("#addStepByAll").show();
			$("#trigger_innerArea").hide();
		} else {
			$("#form_innerArea").hide();
			$("#addStepByAll").hide();
			$("#trigger_innerArea").show();
		}
	});
	/*// 切换新增步骤类型
	form.on('radio(stepTypeFilter)', function(data) {
		if (data.value == "form") {
			$("#form_innerArea").show();
			$("#trigger_innerArea").hide();
		} else {
			$("#form_innerArea").hide();
			$("#trigger_innerArea").show();
		}
	});*/
	// 切换授权类型
	form.on('radio(perType)', function(data) {
		if (data.value == "titleField") {
			$("#field_permissions_table").css("display", "none");
			$("#title_permissions_table").css("display", "block");
		} else {
			$("#title_permissions_table").css("display", "none");
			$("#field_permissions_table").css("display", "block");
		}
	});
	// 切换是否显示自定义业务字段
	form.on('radio(stepBusinessKey)', function(data) {
		if (data.value == "default") {
			$("#stepBusinessKey_input").hide();
		} else {
			$("#stepBusinessKey_input").show();
		}
	});

	form.on('radio(ETS_stepBusinessKey)', function(data) {
		if (data.value == "false") {
			$("#ETS_stepBusinessKey").hide();
		} else {
			$("#ETS_stepBusinessKey").show();
		}
	});
	// 是否为可选处理人
	form.on('radio(actcCanChooseUser)', function(data) {
		if (data.value == "TRUE") {
			$('#actcChooseableHandler').show();
		} else {
			$('#actcChooseableHandler').hide();
		}
	});
});

// 页面加载完成
$(function() {
	initCollapse();
	loadActivityConf(firstHumanMeteConf);

	$("#chooseTrigger_container").on("click", ":checkbox", function() {
		if ($(this).prop("checked")) {
			$("#chooseTrigger_container :checkbox").prop("checked", false);
			$(this).prop("checked", true);
		}
	});

	$("#form_tbody").on("click", ":checkbox", function() {
		if ($(this).prop("checked")) {
			$("#form_tbody :checkbox").prop("checked", false);
			$(this).prop("checked", true);
		}
	});

	// 校验规则
	$('#config_form')
			.validate(
					{
						rules : {
							actcAssignType : {
								required : true
							},
							handleUser : {
								required : function(element) {
									return $('select[name="actcAssignType"]')
											.val() == 'users';
								}
							},
							handleRole : {
								required : function(element) {
									return $('select[name="actcAssignType"]')
											.val().startsWith('role');
								}
							},
							handleTeam : {
								required : function(element) {
									return $('select[name="actcAssignType"]')
											.val().startsWith('team');
								}
							},
							handleField : {
								required : function(element) {
									return $('select[name="actcAssignType"]')
											.val() == 'byField';
								}
							},
							actcChooseableHandlerType : {
								required : true
							},
							chooseableHandleUser : {
								required : function(element) {
									var flag = $(
											'input[name="actcCanChooseUser"]:checked')
											.val();
									if (flag == "FALSE") {
										return false
									} else {
										return $(
												'select[name="actcChooseableHandlerType"]')
												.val() == 'users';
									}
								}
							},
							chooseableHandleRole : {
								required : function(element) {
									var flag = $(
											'input[name="actcCanChooseUser"]:checked')
											.val();
									if (flag == "FALSE") {
										return false
									} else {
										return $(
												'select[name="actcChooseableHandlerType"]')
												.val().startsWith('role');
									}
								}
							},
							chooseableHandleTeam : {
								required : function(element) {
									var flag = $(
											'input[name="actcCanChooseUser"]:checked')
											.val();
									if (flag == "FALSE") {
										return false
									} else {
										return $(
												'select[name="actcChooseableHandlerType"]')
												.val().startsWith('team');
									}
								}
							},
							chooseableHandleField : {
								required : function(element) {
									var flag = $(
											'input[name="actcCanChooseUser"]:checked')
											.val();
									if (flag == "FALSE") {
										return false
									} else {
										return $(
												'select[name="actcChooseableHandlerType"]')
												.val() == 'byField';
									}
								}
							},
							chooseableHandleTrigger : {
								required : function(element) {
									var flag = $(
											'input[name="actcCanChooseUser"]:checked')
											.val();
									if (flag == "FALSE") {
										return false
									} else {
										return $(
												'select[name="actcChooseableHandlerType"]')
												.val().startsWith('byTrigger');
									}
								}
							},
							actcCanChooseUser : {
								required : true
							},
							actcCanReject : {
								required : true
							},
							actcRejectType : {
								required : function(element) {
									return $('input[name="actcCanReject"]')
											.val() == "TRUE";
								}
							},
							rejectActivities : {
								required : function(element) {
									return ($('input[name="actcCanReject"]:checked')
											.val() == "TRUE" && $(
											'select[name="actcRejectType"]')
											.val() == "toActivities");
								}
							},
							actcCanAutocommit : {
								required : true
							},
							actcCanApprove : {
								required : true
							},
							actcCanUploadAttach : {
								required : true
							},
							actcCanEditAttach : {
								required : true
							},
							actcCanDeleteAttach : {
								required : true
							},
							actcCanDelegate : {
								required : true
							},
							actcCanRevoke : {
								required : true
							},
							actcCanAdd : {
								required : true
							},
							actcCanTransfer : {
								required : true
							},
							actcCanMailNotify : {
								required : true
							},
							actcCanMessageNotify : {
								required : true
							},
							actcAssignVariable : {
								required : true
							},
							signCountVarname : {
								required : true
							},
							actcSort : {
								required : true,
								digits : true
							},
							actcMailNotifyTemplate : {
								maxlength : 100
							}
						}
					});

	$('#sla_form').validate(
			{
				rules : {

					actcResponsibility : {
						maxlength : 666
					},
					actcTime : {
						number : true
					},
					actcTimeunit : {
						required : function(element) {
							return $('input[name="actcMailNotifyTemplate"]')
									.val().trim().length > 0;
						}
					},
					actcOuttimeTrigger : {
						maxlength : 60
					},
					actcOuttimeTemplate : {
						maxlength : 100,
						required : function(element) {
							if ($('input[name="outtimeUser"]').val()
									|| $('input[name="outtimeRole"]').val()
									|| $('input[name="outtimeTeam"]').val()) {
								return true;
							} else {
								return false;
							}
						}
					}
				},
				messages : {
					actcOuttimeTemplate : {
						required : "选择了超时通知人，未指定模版"
					}
				}
			});

	$("#back_btn").click(function() {
		window.history.back();
	});

	$("#choose_outtimeTri_btn").click(function() {
		triggerToEdit = 'actcOuttimeTrigger';
		getTriggerInfo();
		$("#chooseTrigger_container").show();
	});

	layui.use([ 'laypage', 'layer', 'form', 'jquery' ], function() {
		var laypage = layui.laypage, layer = layui.layer, form = layui.form;
		var $ = layui.jquery;
		// “确认”选择触发器
		$("#chooseTrigger_sureBtn").click(
				function() {
					$("#table_sel").empty();
					$("#col_md5").empty();
					var cks = $("[name='tri_check']:checked");
					if (!cks.length) {
						$("#" + triggerToEdit).val('');
						$("#" + triggerToEdit + "Title").val('');
						$("#chooseTrigger_container").hide();
						return;
					}
					if (cks.length > 1) {
						layer.alert("请选择一个触发器，不能选择多个");
						return;
					}
					var ck = cks.eq(0);
					var triUid = ck.val();
					var triTitle = ck.parent().next().html();
					var triType = ck.parent().next().next().html();
					var triContent = ck.parent().next().next().next().attr("title");
					var sortNum = 1;
					// 打开接口参数和form表单映射表单
					var stepType = "form";
					if (triType == "interface") {
						// 判断有没有表单
						$("#triggerInterface_container").show();
						$.ajax({
							url : common.getPath() + '/step/selectByStep',
							type : 'post',
							dataType : 'json',
							data : {
								proAppId : proAppId,
								proUid : proUid,
								proVerUid : proVerUid,
								stepType : stepType
							},
							success : function(result) {
								var chose = '<option>请选择表单</option>';
								$("#table_sel").append(chose)
								for (var i = 0; i < result.data.length; i++) {
									var trs = '<option value="'
											+ result.data[i].stepObjectUid
											+ '|'
											+ triContent
											+ '">' + result.data[i].formName
											+ '</option>';
									$("#table_sel").append(trs)
								}

								form.render();
							},
							error : function(result) {
								layer.alert('查询环节出错')
							}
						});
						// 接口参数
						$.ajax({
							url : common.getPath() + '/interfaceParamers/index',
							type : 'post',
							dataType : 'json',
							data : {
								intUid : triContent
							},
							success : function (result2){
								var list = result2.data.list;
								for (var i = 0; i < list.length; i++) {
									var paraIndex = list[i].paraIndex; // 接口索引下标
									var paraName = list[i].paraName; // 接口名
									var index = sortNum + i
									var trs = '<div class="layui-form-item">'
										+ '<div class="layui-row">'
										+ '<div class="layui-col-md6">'
										+ '<div class="layui-inline">'
										+ '<label class="layui-form-label" style="width: 100px">接口参数'+index+'</label>'
										+ '<div class="layui-input-inline">'
										+ '<input id="interfaceParam'+index+'" disabled="disabled" readonly="readonly" type="text" name="title" lay-verify="title" autocomplete="off" class="layui-input" value="'+paraName+'">'
										+ '</div>'
										+ '</div>'
										+ '</div>'
										+ '<div class="layui-col-md6">'
										+ '<label class="layui-form-label" style="width: 100px">表单参数'+index+'</label>'
										+ '<div class="layui-input-inline">'
										+ '<select id="tableParam'+index+'">'
										+ '</select>'
										+ '</div>'
										+ '</div>'
										+ '</div>'
										+ '</div>';
								$("#col_md5").append(trs)
								}
								form.render();
							},
							error : function(result2){
								layer.alert("查询接口参数出错")
							}
						})
					}
					$("#" + triggerToEdit).val(triUid);
					$("#" + triggerToEdit + "Title").val(triTitle);
					$("#chooseTrigger_container").hide();
				});

		form.on('select(table_sel)', function(data) {
			$("#col_md5").find(".layui-form-item").find(".layui-row").find("select").empty();
			var data = data.value; // 表单id
			// 截取字符串
			var formId = data.substring(0,data.indexOf('|')); // 表单ID
			var intUid = data.substring(data.indexOf('i')); // 参数ID
			var startNum = 1;			
			$.ajax({
				url : common.getPath() + '/formField/queryFieldByFromUid',
				type : 'post',
				dataType : 'json',
				data : {
					formUid : formId
				},
				success : function (result){	
					for (var i = 0; i < result.data.length; i++) {
						var fldIndex = result.data[i].fldIndex; // 字段索引下标
						var fldCodeName = result.data[i].fldCodeName; // 字段名
						var indexs = startNum + i
						// 获取 接口参数的数据
						 var trs = '<option id="">'
						 + result.data[i].fldCodeName
						 + '</option>';		
						var dataTableList = $("#col_md5").find(".layui-form-item").find(".layui-row");
						for (var j = 0; j < dataTableList.length; j++) {
							var inputArr = $(dataTableList[j]).find("select");
							//$(inputArr).empty();
							$(inputArr).append(trs);				
						}
					}
					form.render();
				},
				error : function (result){
					layer.alert('查询表单字段出错')
				}
			})
		})
		
		$("#paramMapping_sureBtn").click(function() {
			var triUid ="";
			$('input[name="tri_check"]:checked').each(function(){ 
				triUid =$(this).val(); 
			  }); 
			var $activeLi = $("#my_collapse li.link_active");
			var actcUid = $activeLi.data('uid');
			var data = $("select[name=table_sel]").val();
			var formId = data.substring(0,data.indexOf('|')); // 表单ID
			var intUid = data.substring(data.indexOf('i')); // 参数ID
			var arr = new Array();
			var dataList = $("#col_md5").find(".layui-form-item").find(".layui-row");
			for (var i = 0; i < dataList.length; i++) {
				var inputArr = $(dataList[i]).find("input");
				var info = {triUid : triUid,
						intUid : intUid,
						dynUid : formId,
						activityId : $("#activityId").val(),
						paraName : $(inputArr[0]).val(),
						fldCodeName : $(dataList[i]).find("option:selected").val()};
				arr.push(info);
			}
			$.ajax({
				url : common.getPath() + '/dhTriggerInterface/insertBatch',
				type : 'post',
				dataType : 'json',
				contentType: "application/json",
				data :JSON.stringify(arr),
				success : function (result){
					layer.alert('参数映射成功')
					$("#triggerInterface_container").css("display","none");
					loadActivityConf(actcUid);
				},
				error : function (result){
					layer.alert('参数映射出错')
				}
			})
		});
		
	});

	// “取消”选择触发器
	$("#chooseTrigger_cancelBtn").click(function() {
		$("#chooseTrigger_container").hide();
	});

	// 选择处理人（人员）
	$("#choose_handle_user").click(function() {
		common.chooseUser('handleUser', 'false');
	});
	// 选择处理人（角色）
	$("#choose_handle_role").click(function() {
		common.chooseRole('handleRole', 'false');
	});
	// 选择处理人（角色组）
	$("#choose_handle_team").click(function() {
		common.chooseTeam('handleTeam', 'false');
	});

	// 选择可选处理人（人员）
	$("#choose_able_handle_user").click(function() {
		common.chooseUser('chooseableHandleUser', 'false');
	});
	// 选择可选处理人（角色）
	$("#choose_able_handle_role").click(function() {
		common.chooseRole('chooseableHandleRole', 'false');
	});
	// 选择可选处理人（角色组）
	$("#choose_able_handle_team").click(function() {
		common.chooseTeam('chooseableHandleTeam', 'false');
	});
	// 选择可选处理人（触发器）
	$("#choose_HandleTri_btn").click(function() {
		triggerToEdit = 'chooseableHandleTrigger';
		getTriggerInfo();
		$("#chooseTrigger_container").show();
	});

	// 选择超时通知人（人员）
	$("#choose_outtime_user").click(function() {
		common.chooseUser('outtimeUser', 'false');
	});
	// 选择超时通知人（角色）
	$("#choose_outtime_role").click(function() {
		common.chooseRole('outtimeRole', 'false');
	});
	// 选择超时通知人（角色组）
	$("#choose_outtime_team").click(function() {
		common.chooseTeam('outtimeTeam', 'false');
	});
	// 新增流程中点击选择触发器
	$("#choose_stepTri_btn").click(function() {
		triggerToEdit = 'trigger_of_step';
		getTriggerInfo();
		$("#chooseTrigger_container").show();
	});
	$("#ETS_choose_stepTri_btn").click(function() {
		triggerToEdit = 'ETS_trigger_of_step';
		getTriggerInfo();
		$("#chooseTrigger_container").show();
	});

	// “新增步骤”按钮
	$("#add_step_btn").click(function() {
		$('#addStep_form')[0].reset();
		$('input[name="stepType"]').each(function() {
			if ($(this).val() == 'form') {
				$(this).prop("checked", true);
			} else {
				$(this).prop("checked", false);
			}
		});
		$('input[name="stepSort"]').val("");
		$('input[name="stepBusinessKeyType"]').each(function() {
			if ($(this).val() == 'default') {
				$(this).prop("checked", true);
			} else {
				$(this).prop("checked", false);
			}
		});
		$("#stepBusinessKey_input").hide();
		layui.form.render();
		$("#form_innerArea").show();
		$("#trigger_innerArea").hide();
		$("#addStep_container").show();

		formTable();

	})

	// “选择环节”
	$("#chooseActivity_i").click(function() {
		// $("#left_activity_ul").empty();
		$("#right_activity_ul").empty();
		// $("#left_activity_ul").append(activityStr);
		var choosedValue = $("#rejectActivities").val();
		if (!choosedValue) {
			$("#choose_activity_container").show();
			return;
		}
		var chooseIds = choosedValue.split(";");
		$("#left_activity_ul li").each(function() {
			var activityBpdId = $(this).data('activitybpdid');
			if ($.inArray(activityBpdId, chooseIds) != -1) {
				$(this).appendTo($("#right_activity_ul"));
			}
		});
		$("#choose_activity_container").show();
	})
	$("#search_form_btn").click(function() {
		formTable();
	});

	$("#chooseActivities_sureBtn").click(function() {
		var val = '';
		var val_view = '';
		$("#right_activity_ul li").each(function() {
			val += $(this).data('activitybpdid') + ";";
			val_view += $(this).html() + ";";
		});
		$("#rejectActivities").val(val);
		$("#rejectActivities_view").val(val_view);
		console.log(val);
		console.log(val_view);
		$("#choose_activity_container").hide();
	});

	$("#choose_activity_container").on('click', 'li', function() {
		if ($(this).hasClass('colorli')) {
			$(this).removeClass('colorli');
		} else {
			$(this).addClass('colorli');
		}
	});

	// 点击配置步骤
	$("#step_li").click(function() {
		if (getFormData() != preFormData) { // 配置变化了
			layer.confirm('环节配置有变动，是否保存？', {
				btn : [ '保存', '不保存' ]
			}, function() {
				saveFrom = 'stepLi';
				save('');
			}, function() {
			});
		}
	});

});

// 新增步骤table
function formTable() {
	$
			.ajax({
				url : common.getPath() + "/formManage/queryFormListBySelective",
				type : "post",
				data : {
					proUid : proUid,
					proVersion : proVerUid,
					dynTitle : $('#dynTitle').val(),
					dynDescription : $('#dynDescription').val()
				},
				dataType : "json",
				success : function(result) {
					$('#form_tbody').empty();
					var trs = '';
					$(result.data)
							.each(
									function(index) {
										trs += '<tr>';
										trs += '<td><input type="checkbox" name="dynUid_check" value="'
												+ this.dynUid
												+ '" lay-skin="primary">'
												+ (index + 1) + '</td>';
										trs += '<td>' + this.dynTitle + '</td>'
										trs += '<td>' + this.dynDescription
												+ '</td>'
										trs += '</tr>';
									});
					$("#form_tbody").append(trs);
				}
			});
}

// 初始化折叠菜单
function initCollapse() {
	$.ajax({
		url : common.getPath() + "/activityMeta/getActivitiyMetasForConfig",
		type : "post",
		dataType : "json",
		data : {
			"proAppId" : proAppId,
			"proUid" : proUid,
			"proVerUid" : proVerUid
		},
		success : function(result) {
			if (result.status == 0) {
				printCollapse(result.data);
			} else {
				layer.alert(result.msg);
			}
		},
		error : function() {
			layer.alert('操作失败');
		}
	});
}
// 画出折叠栏
function printCollapse(list) {
	var str = '';
	for (var i = 0; i < list.length; i++) {
		var process = list[i];
		var name = process.name;
		var children = process.children;
		str += '<div class="layui-colla-item">'
				+ '<h2 class="layui-colla-title">' + name + '</h2>';
		if (process.id == 'main') {
			// 如果是主流程的话，展开
			str += '<div class="layui-colla-content layui-show" id="content'
					+ i + '">';
		} else {
			str += '<div class="layui-colla-content " id="content' + i + '">';
		}
		str += '<ul class="link_list">';
		for (var j = 0; j < children.length; j++) {
			var meta = children[j];
			if (meta.activityId == firstHumanMeta) {
				// 如果这个环节是此流程的第一个环节
				str += '<li data-parentActivityId="' + meta.parentActivityId
						+ '" data-uid="' + meta.actcUid
						+ '" data-activitybpdid="' + meta.activityBpdId
						+ '" class="link_active" onclick="clickLi(this);">'
						+ meta.activityName + '</li>';
				$("#left_activity_ul").empty();
				$("#left_activity_ul").append(activityStr);
				var leftLi = $("#left_activity_ul").find("li");
				$(leftLi).each(function() {
					var checkParentId = $(this).data('parentactivityid');
					if (checkParentId != meta.parentActivityId) {
						$(this).remove();
					}
				});
			} else {
				str += '<li data-parentActivityId="' + meta.parentActivityId
						+ '" data-uid="' + meta.actcUid
						+ '" data-activitybpdid="' + meta.activityBpdId
						+ '" onclick="clickLi(this);">' + meta.activityName
						+ '</li>';
			}
		}
		str += '</ul>' + '</div>' + '</div>';
	}
	$("#my_collapse").append(str);
	layui.use('element', function() {
		var element = layui.element;
		element.init();
	});

}
// 点击li
function clickLi(li) {
	var $li = $(li);
	if ($li.hasClass('link_active')) {
		return;
	} else {
		var actcUid = $(li).data('uid');
		var parentActivityId = $(li).data('parentactivityid');
		$("#left_activity_ul").empty();
		$("#left_activity_ul").append(activityStr);
		var leftLi = $("#left_activity_ul").find("li");
		$(leftLi).each(function() {
			var checkParentId = $(this).data('parentactivityid');
			if (checkParentId != parentActivityId) {
				$(this).remove();
			}
		});
		if (getFormData() != preFormData) {// 配置变化了
			layer.confirm('是否先保存数据再切换环节？', {
				btn : [ '确定', '取消' ]
			}, function() {
				saveFrom = 'activityLi';
				save(actcUid);
			}, function() {
				$("#my_collapse li").each(function() {
					$(this).removeClass('link_active');
				});
				$li.addClass('link_active');
				loadActivityConf(actcUid);
			});

		} else {// 配置没有变化
			$("#my_collapse li").each(function() {
				$(this).removeClass('link_active');
			});
			$li.addClass('link_active');
			loadActivityConf(actcUid);
		}

	}
}

// ajax获取配置文件信息
function loadActivityConf(actcUid) {
	$.ajax({
		url : common.getPath() + "/activityConf/getData",
		type : "post",
		dataType : "json",
		data : {
			"actcUid" : actcUid
		},
		success : function(result) {
			if (result.status == 0) {
				console.log(result.data);
				initConf(result.data);
				document.getElementById("activityId").value = result.data.conf.activityId
				step_table(result.data.stepList);

			} else {
				layer.alert(result.msg);
			}
		},
		error : function() {
			layer.alert('操作失败,请稍后再试');
		}
	});
}

// step table数据填充
function step_table(data) {
	$('#step_table').empty();
	var trs = '';
	$(data)
			.each(
					function(index, val) {
						trs += '<tr>';
						trs += '<td>' + this.stepSort + '</td>';
						if (this.stepType == 'trigger') {
							trs += '<td>触发器</td>'
						} else {
							trs += '<td>表单</td>'
						}
						trs += '<td>' + this.stepBusinessKey + '</td>'
						if (this.formName == null) {
							trs += '<td></td>'
						} else {
							trs += '<td>' + this.formName + '</td>'
						}
						if (this.triTitle == null) {
							trs += '<td></td>'
						} else {
							trs += '<td>' + this.triTitle + '</td>'
						}
						var value = encodeURI(JSON.stringify(val));
						if (this.stepType == 'trigger') {
							trs += '<td>'
									+ '<i class="layui-icon" title="上移" onclick="resortStep(\''
									+ val.stepUid
									+ '\', \'reduce\');">&#xe619;</i>'
									+ '<i class="layui-icon" title="下移" onclick="resortStep(\''
									+ val.stepUid
									+ '\', \'increase\');">&#xe61a;</i>'
									+ '<i class="layui-icon delete_btn" title="编辑" onclick=stepEdit("'
									+ value
									+ '") >&#xe642;</i><i class="layui-icon delete_btn" title="删除" onclick="deleteStep(\''
									+ this.stepUid + '\');">&#xe640;</i>'
									+ '<i class="layui-icon" title="修改参数映射" onclick=triggerEdit(\''
									+ this.stepObjectUid
									+ '\');>&#xe654;</i>'
						} else {
							trs += '<td>'
									+ '<i class="layui-icon" title="上移" onclick="resortStep(\''
									+ val.stepUid
									+ '\', \'reduce\');">&#xe619;</i>'
									+ '<i class="layui-icon" title="下移" onclick="resortStep(\''
									+ val.stepUid
									+ '\', \'increase\');">&#xe61a;</i>'
									+ '<i class="layui-icon delete_btn" title="编辑" onclick=stepFormEdit("'
									+ value
									+ '") >&#xe642;</i><i class="layui-icon delete_btn" title="删除" onclick="deleteStep(\''
									+ this.stepUid
									+ '\');">&#xe640;</i>'
									+ '<i class="layui-icon" title="编辑字段权限" onclick=formFieldEdit("'
									+ value + '"); >&#xe654;</i>'

						}
						trs += '</td>';
						trs += '</tr>';
					});
	$("#step_table").append(trs);
}

function formFieldEdit(data) {
	$(".display_container4").css("display", "block");
	// 查找该步骤绑定表单的所有字段及权限信息
	dates = jQuery.parseJSON(decodeURI(data));
	$.ajax({
		url : common.getPath() + "/formField/queryFieldByFormUidAndStepId",
		type : "post",
		dataType : "json",
		data : {
			stepUid : dates.stepUid,
			formUid : dates.stepObjectUid,
			fieldType : ""
		},
		success : function(result) {
			drawPerTable(result, "field_permissions_table", "");
		}
	});
	$.ajax({
		url : common.getPath() + "/formField/queryFieldByFormUidAndStepId",
		type : "post",
		dataType : "json",
		data : {
			stepUid : dates.stepUid,
			formUid : dates.stepObjectUid,
			fieldType : "title"
		},
		success : function(result) {
			drawPerTable(result, "title_permissions_table", "title");
		}
	});
	// $(".form-horizontal").serialize();
}
// 修改之前 查询触发器数据信息
function triggerEdit(triggerUid){
	layui.use([ 'laypage', 'layer', 'form', 'jquery' ], function() {
		var laypage = layui.laypage, layer = layui.layer, form = layui.form;
		var $ = layui.jquery;
	$(".display_container8").css("display", "block");
	var actcUid = getCurrentActcUid();
	// 查询做映射的数据
	$.ajax({
		url : common.getPath() + '/dhTriggerInterface/selectTriggerAndForm',
		type : 'post',
		dataType : 'json',
		data : {
			triUid : triggerUid,
			activityId : $("#activityId").val()
		},
		success : function(result){
			console.info(result);
			$("#update_param").empty();
			if(result.status == 0){
				var list = result.data;
				var sortNum = 1;
				var startNum = 1;
				var formId  = "";
				for (var i = 0; i < list.length; i++) {
					formId =  list[i].dynUid;
					var paraName = list[i].paraName; // 接口参数
					var fldCodeName = list[i].fldCodeName; // 表单字段
					var index = sortNum + i 
					var trs = '<div class="layui-form-item">'
						+ '<div class="layui-row">'
						+ '<div class="layui-col-md6">'
						+ '<div class="layui-inline">'
						+ '<label class="layui-form-label" style="width: 100px">接口参数'+index+'</label>'
						+ '<div class="layui-input-inline">'
						+ '<input id="interfaceParam'+index+'" disabled="disabled" readonly="readonly" type="text" name="title" lay-verify="title" autocomplete="off" class="layui-input" value="'+paraName+'">'
						+ '</div>'
						+ '</div>'
						+ '</div>'
						+ '<div class="layui-col-md6">'
						+ '<label class="layui-form-label" style="width: 100px">表单参数'+index+'</label>'
						+ '<div class="layui-input-inline">'
						+ '<select id="tableParam'+index+'">'
						+ '<option value="'+fldCodeName+'">'
						+ fldCodeName
						+ '</option>'
						+ '</select>'
						+ '</div>'
						+ '</div>'
						+ '</div>'
						+ '</div>';
					var trs2 = '<option value="'+list[0].bpmForm.dynTitle+'">'
							 + list[0].bpmForm.dynTitle
							 + '</option>'
				$("#update_param").append(trs)
				$("#tb_Trigger").append(trs2)
				}
				$.ajax({
					url : common.getPath() + '/formField/queryFieldByFromUid',
					type : 'post',
					dataType : 'json',
					data : {
						formUid : formId
					},
					success : function (result){	
						for (var i = 0; i < result.data.length; i++) {
							var fldIndex = result.data[i].fldIndex; // 字段索引下标
							var fldCodeName = result.data[i].fldCodeName; // 字段名
							var indexs = startNum + i
							// 获取 接口参数的数据
							 var trs = '<option value="'+result.data[i].fldCodeName+'">'
							 + result.data[i].fldCodeName
							 + '</option>';		
							var dataTableList = $("#update_param").find(".layui-form-item").find(".layui-row");
							for (var j = 0; j < dataTableList.length; j++) {
								var inputArr = $(dataTableList[j]).find("select");
								$(inputArr).append(trs);				
							}
							} 
						/*删除重复项*/ 
						var itemList = $("#update_param").find(".layui-form-item");
						for (var j = 0; j < itemList.length+1; j++) {
								$("#tableParam"+j+" option").each(function() {
									text = $(this).text();
									if($("#tableParam"+j+" option:contains("+text+")").length > 1)
										$("#tableParam"+j+" option:contains("+text+"):gt(0)").remove();
								});
						}
						form.render();
					}
				});
				form.render();
			}
		},
		error : function(result){
			layer.alert("查询映射数据失败")
		}
		})
		
		$("#triggerSave").click(function(){
			var arr = new Array();
			var dataList = $("#update_param").find(".layui-form-item").find(".layui-row");
			for (var i = 0; i < dataList.length; i++) {
				var inputArr = $(dataList[i]).find("input");
				var info = {triUid : triggerUid,
						activityId : $("#activityId").val(),
						paraName : $(inputArr[0]).val(),
						fldCodeName : $(dataList[i]).find("option:selected").val()};
				arr.push(info);
			}
			$.ajax({
				url : common.getPath() + '/dhTriggerInterface/updateBatch',
				type : 'post',
				dataType : 'json',
				data : JSON.stringify(arr),
				contentType: "application/json;charset=utf-8",
				success : function(result){
					layer.alert("修改成功")
				},
				error : function(result){
					layer.alert("修改失败")
				}			
			})
		})
	})
}


// 渲染权限信息表格
function drawPerTable(result, perTableId, title) {
	$("#" + perTableId + " tbody").empty();
	var trs = '';
	$(result.data)
			.each(
					function(index, val) {
						trs += '<tr>';
						trs += '<td>' + (index + 1);
						trs += '<input type="hidden" name="fldUid" value="'
								+ this.fldUid + '">';
						trs += '<input type="hidden" name="stepUid" value="'
								+ dates.stepUid + '">';
						trs += '</td>';
						trs += '<td>' + this.fldName + '</td>';
						if (this.opActionList.length == 1) {
							var opAction = (this.opActionList)[0];
							if (opAction == 'EDIT') {
								trs += '<td><input type="radio"  name="opAction_'
										+ title
										+ this.fldUid
										+ '" checked="checked" value="EDIT"/></td>';
								trs += '<td><input type="radio"  name="opAction_'
										+ title
										+ this.fldUid
										+ '"  value="VIEW"/></td>';
								trs += '<td><input type="radio"  name="opAction_'
										+ title
										+ this.fldUid
										+ '" value="HIDDEN"/></td>';
							} else if (opAction == 'VIEW') {
								trs += '<td><input type="radio"  name="opAction_'
										+ title
										+ this.fldUid
										+ '"  value="EDIT"/></td>';
								trs += '<td><input type="radio"  name="opAction_'
										+ title
										+ this.fldUid
										+ '"  checked="checked"  value="VIEW"/></td>';
								trs += '<td><input type="radio"  name="opAction_'
										+ title
										+ this.fldUid
										+ '" value="HIDDEN"/></td>';
							} else if (opAction == 'HIDDEN') {
								trs += '<td><input type="radio"  name="opAction_'
										+ title
										+ this.fldUid
										+ '"  value="EDIT"/></td>';
								trs += '<td><input type="radio"  name="opAction_'
										+ title
										+ this.fldUid
										+ '"  value="VIEW"/></td>';
								trs += '<td><input type="radio"  name="opAction_'
										+ title
										+ this.fldUid
										+ '" checked="checked" value="HIDDEN"/></td>';
							}
							trs += '<td style="border-left:1px solid #CCC"><input type="checkbox" name="checkboxSel'
									+ title
									+ '" value="PRINT" onclick="onClickSel'
									+ title + '(this)"/></td>'
						} else if (this.opActionList.length == 2) {
							var opAction0 = (this.opActionList)[0];
							var opAction1 = (this.opActionList)[1];
							if (opAction0 == "PRINT") {
								if (opAction1 == 'EDIT') {
									trs += '<td><input type="radio"  name="opAction_'
											+ title
											+ this.fldUid
											+ '" checked="checked" value="EDIT"/></td>';
									trs += '<td><input type="radio"  name="opAction_'
											+ title
											+ this.fldUid
											+ '"  value="VIEW"/></td>';
									trs += '<td><input type="radio"  name="opAction_'
											+ title
											+ this.fldUid
											+ '" value="HIDDEN"/></td>';
								} else if (opAction1 == 'VIEW') {
									trs += '<td><input type="radio"  name="opAction_'
											+ title
											+ this.fldUid
											+ '"  value="EDIT"/></td>';
									trs += '<td><input type="radio"  name="opAction_'
											+ title
											+ this.fldUid
											+ '"  checked="checked"  value="VIEW"/></td>';
									trs += '<td><input type="radio"  name="opAction_'
											+ title
											+ this.fldUid
											+ '" value="HIDDEN"/></td>';
								} else if (opAction1 == 'HIDDEN') {
									trs += '<td><input type="radio"  name="opAction_'
											+ title
											+ this.fldUid
											+ '"  value="EDIT"/></td>';
									trs += '<td><input type="radio"  name="opAction_'
											+ title
											+ this.fldUid
											+ '"  value="VIEW"/></td>';
									trs += '<td><input type="radio"  name="opAction_'
											+ title
											+ this.fldUid
											+ '" checked="checked" value="HIDDEN"/></td>';
								}
								trs += '<td style="border-left:1px solid #CCC"><input type="checkbox" name="checkboxSel'
										+ title
										+ '" value="PRINT" onclick="onClickSel'
										+ title + '(this)" checked/></td>'
							} else if (opAction1 == "PRINT") {
								if (opAction0 == 'EDIT') {
									trs += '<td><input type="radio"  name="opAction_'
											+ title
											+ this.fldUid
											+ '" checked="checked" value="EDIT"/></td>';
									trs += '<td><input type="radio"  name="opAction_'
											+ title
											+ this.fldUid
											+ '"  value="VIEW"/></td>';
									trs += '<td><input type="radio"  name="opAction_'
											+ title
											+ this.fldUid
											+ '" value="HIDDEN"/></td>';
								} else if (opAction0 == 'VIEW') {
									trs += '<td><input type="radio"  name="opAction_'
											+ title
											+ this.fldUid
											+ '"  value="EDIT"/></td>';
									trs += '<td><input type="radio"  name="opAction_'
											+ title
											+ this.fldUid
											+ '"  checked="checked"  value="VIEW"/></td>';
									trs += '<td><input type="radio"  name="opAction_'
											+ title
											+ this.fldUid
											+ '" value="HIDDEN"/></td>';
								} else if (opAction0 == 'HIDDEN') {
									trs += '<td><input type="radio"  name="opAction_'
											+ title
											+ this.fldUid
											+ '"  value="EDIT"/></td>';
									trs += '<td><input type="radio"  name="opAction_'
											+ title
											+ this.fldUid
											+ '"  value="VIEW"/></td>';
									trs += '<td><input type="radio"  name="opAction_'
											+ title
											+ this.fldUid
											+ '" checked="checked" value="HIDDEN"/></td>';
								}
								trs += '<td style="border-left:1px solid #CCC"><input type="checkbox" name="checkboxSel'
										+ title
										+ '" value="PRINT" onclick="onClickSel'
										+ title + '(this)" checked/></td>'
							}
						}
						trs += '</tr>';
					});
	$("#" + perTableId + " tbody").append(trs);
	if (title != null && title != "") {
		radiocheckAlltitle(result.data.length);
	} else {
		radiocheckAll(result.data.length);
	}
	$("#" + perTableId + " tbody input[type='radio']").bind("click",
			function() {
				if (title != null && title != "") {
					radiocheckAlltitle(result.data.length);
				} else {
					radiocheckAll(result.data.length);
				}
			});

	$("#" + perTableId + " tbody input[type='checkbox']").each(function() {
		if (title != null && title != "") {
			onClickSeltitle(this);
		} else {
			onClickSel(this);
		}
	});
}

// 复选框全选，取消全选
function onClickHander(obj) {
	if (obj.checked) {
		$("input[name='checkboxSel']").prop("checked", true);
	} else {
		$("input[name='checkboxSel']").prop("checked", false);
	}
}

// 复选框分选
function onClickSel(obj) {
	if (obj.checked) {
		var allSel = false;
		$("input[name='checkboxSel']").each(function() {
			if (!$(this).is(":checked")) {
				allSel = true;
			}
		});

		// 如果有checkbox没有被选中
		if (allSel) {
			$("input[name='checkboxAll']").prop("checked", false);
		} else {
			$("input[name='checkboxAll']").prop("checked", true);
		}
	} else {
		$("input[name='checkboxAll']").prop("checked", false);
	}
}

// 复选框全选，取消全选
function onClickHandertitle(obj) {
	if (obj.checked) {
		$("input[name='checkboxSeltitle']").prop("checked", true);
	} else {
		$("input[name='checkboxSeltitle']").prop("checked", false);
	}
}

// 复选框分选
function onClickSeltitle(obj) {
	if (obj.checked) {
		var allSel = false;
		$("input[name='checkboxSeltitle']").each(function() {
			if (!$(this).is(":checked")) {
				allSel = true;
			}
		});

		// 如果有checkbox没有被选中
		if (allSel) {
			$("input[name='checkboxAlltitle']").prop("checked", false);
		} else {
			$("input[name='checkboxAlltitle']").prop("checked", true);
		}
	} else {
		$("input[name='checkboxAlltitle']").prop("checked", false);
	}
}

function stepEdit(data) {
	var dates = jQuery.parseJSON(decodeURI(data));
	console.log(dates.activityBpdId);
	if (dates.stepType == 'trigger') {
		$("#ETS_stepBusinessKey").hide();
		$("#ETS_stepSort").val(dates.stepSort);

		if (dates.stepBusinessKey == 'default') {
			$("#ETS_radio_default").prop("checked", true);
			$("#ETS_radio_custom").prop("checked", false);
		} else {
			$("#ETS_radio_default").prop("checked", false);
			$("#ETS_radio_custom").prop("checked", true);
			$("#ETS_stepBusinessKey").val(dates.stepBusinessKey);
			$("#ETS_stepBusinessKey").show();
		}
		$("#ETS_trigger_of_step").val(dates.stepObjectUid);
		$("#ETS_trigger_of_stepTitle").val(dates.triTitle);
		layui.form.render();
		stepUidToEdit = dates.stepUid;
		$("#ETS_container").show();
	}
}

// 修改环节关联表单信息
var updateFormUid = "";// 要修改步骤的关联表单
$("#update_search_form_btn").click(function() {
	(updateFormUid);
});

function updateFormTable(formUid) {
	$
			.ajax({
				url : common.getPath() + "/formManage/queryFormListBySelective",
				type : "post",
				data : {
					proUid : proUid,
					proVersion : proVerUid,
					dynTitle : $('#updateDynTitle').val(),
					dynDescription : $('#updateDynDescription').val()
				},
				dataType : "json",
				success : function(result) {
					$('#update_step_form_tbody').empty();
					var trs = '';
					$(result.data)
							.each(
									function(index) {
										trs += '<tr>';
										if (formUid == this.dynUid) {
											trs += '<td><input type="checkbox" name="dynUid_check" value="'
													+ this.dynUid
													+ '" lay-skin="primary" checked>'
													+ (index + 1) + '</td>';
										} else {
											trs += '<td><input type="checkbox" name="dynUid_check" value="'
													+ this.dynUid
													+ '" lay-skin="primary">'
													+ (index + 1) + '</td>';
										}
										trs += '<td>' + this.dynTitle + '</td>'
										trs += '<td>' + this.dynDescription
												+ '</td>'
										trs += '</tr>';
									});
					$("#update_step_form_tbody").append(trs);
				}
			});
}

// 修改环节关联表单信息
function stepFormEdit(data) {
	var dates = jQuery.parseJSON(decodeURI(data));
	$("#update_step_form_container").find("input[type='text']").val("");
	$("#update_step_form_container").css("display", "block");
	$("#updateStepSort").val(dates.stepSort);
	if (dates.stepBusinessKey == "default") {
		$("#update_step_form_container").find("input[value='default']").prop(
				"checked", true);
		$("#update_step_form_container").find("input[value='custom']").prop(
				"checked", false);
	} else {
		$("#update_step_form_container").find("input[value='custom']").prop(
				"checked", true);
		$("#update_step_form_container").find("input[value='default']").prop(
				"checked", false);
		$("#update_stepBusinessKey_input").val(dates.stepBusinessKey);
	}
	$('#eidtstepUid').val(dates.stepUid);
	layui.form.render();
	updateFormTable(dates.stepObjectUid);
}

// 载入配置信息
function initConf(map) {
	var conf = map.conf;
	$("#handleUser_div").hide();
	$("#handleRole_div").hide();
	$("#handleTeam_div").hide();
	$("#handleField_div").hide();
	$("#chooseableHandleUser_div").hide();
	$("#chooseableHandleRole_div").hide();
	$("#chooseableHandleTeam_div").hide();
	$("#chooseableHandleField_div").hide();
	$("#chooseableHandleTrigger_div").hide();

	$('input[name="actcUid"]').val(conf.actcUid);
	$('input[name="actcSort"]').val(conf.actcSort);
	$('input[name="actcTime"]').val(conf.actcTime);
	$('input[name="actcMailNotifyTemplate"]').val(conf.actcMailNotifyTemplate);
	$('input[name="actcOuttimeTrigger"]').val(conf.actcOuttimeTrigger);
	$('input[name="actcOuttimeTriggerTitle"]')
			.val(conf.actcOuttimeTriggerTitle);
	$('input[name="actcOuttimeTemplate"]').val(conf.actcOuttimeTemplate);

	$('textarea[name="actcResponsibility"]').val(conf.actcResponsibility);

	$('select[name="actcTimeunit"]').val(conf.actcTimeunit);
	$('select[name="actcAssignType"]').val(conf.actcAssignType);
	showHandleDiv(conf.actcAssignType);

	$('select[name="actcAssignVariable"]').val(conf.actcAssignVariable);
	$('select[name="signCountVarname"]').val(conf.signCountVarname);
	$('select[name="actcRejectType"]').val(conf.actcRejectType);
	if (conf.actcRejectType == "toActivities") {
		$("#rejectType_div").show();
	} else {
		$("#rejectType_div").hide();
	}

	$('input[name="actcCanEditAttach"]').each(function() {
		if ($(this).val() == conf.actcCanEditAttach) {
			$(this).prop("checked", true);
		} else {
			$(this).prop("checked", false);
		}
	});
	$('input[name="actcCanUploadAttach"]').each(function() {
		if ($(this).val() == conf.actcCanUploadAttach) {
			$(this).prop("checked", true);
		} else {
			$(this).prop("checked", false);
		}
	});
	$('input[name="actcCanDeleteAttach"]').each(function() {
		if ($(this).val() == conf.actcCanDeleteAttach) {
			$(this).prop("checked", true);
		} else {
			$(this).prop("checked", false);
		}
	});
	$('input[name="actcCanDelegate"]').each(function() {
		if ($(this).val() == conf.actcCanDelegate) {
			$(this).prop("checked", true);
		} else {
			$(this).prop("checked", false);
		}
	});
	$('input[name="actcCanMessageNotify"]').each(function() {
		if ($(this).val() == conf.actcCanMessageNotify) {
			$(this).prop("checked", true);
		} else {
			$(this).prop("checked", false);
		}
	});
	$('input[name="actcCanMailNotify"]').each(function() {
		if ($(this).val() == conf.actcCanMailNotify) {
			$(this).prop("checked", true);
		} else {
			$(this).prop("checked", false);
		}
	});
	$('input[name="actcCanMessageNotify"]').each(
			function() {
				if ($(this).val() == conf.actcCanMessageNotify) {
					$(this).prop("checked", true);
					$('input[name = "actcMessageNotifyTemplate"]').val(
							conf.actcMessageNotifyTemplate);
				} else {
					$(this).prop("checked", false);
				}
			});
	$('input[name="actcCanReject"]').each(function() {
		if ($(this).val() == conf.actcCanReject) {
			$(this).prop("checked", true);
		} else {
			$(this).prop("checked", false);
		}
	});

	$('input[name="actcCanRevoke"]').each(function() {
		if ($(this).val() == conf.actcCanRevoke) {
			$(this).prop("checked", true);
		} else {
			$(this).prop("checked", false);
		}
	});
	$('input[name="actcCanAutocommit"]').each(function() {
		if ($(this).val() == conf.actcCanAutocommit) {
			$(this).prop("checked", true);
		} else {
			$(this).prop("checked", false);
		}
	});
	$('input[name="actcCanAdd"]').each(function() {
		if ($(this).val() == conf.actcCanAdd) {
			$(this).prop("checked", true);
		} else {
			$(this).prop("checked", false);
		}
	});
	$('input[name="actcCanApprove"]').each(function() {
		if ($(this).val() == conf.actcCanApprove) {
			$(this).prop("checked", true);
		} else {
			$(this).prop("checked", false);
		}
	});
	$('input[name="actcCanChooseUser"]').each(function() {
		if ($(this).val() == conf.actcCanChooseUser) {
			$(this).prop("checked", true);
			if ($(this).val() == "FALSE") {
				$('#actcChooseableHandler').hide();
			} else {
				$('#actcChooseableHandler').show();
			}
		} else if ($(this).val() != null) {
			$(this).prop("checked", false);
		} else {
			if ($(this).val() == "false") {
				$(this).prop("checked", true);
			}
		}
	});
	$('input[name="actcCanTransfer"]').each(function() {
		if ($(this).val() == conf.actcCanTransfer) {
			$(this).prop("checked", true);
		} else {
			$(this).prop("checked", false);
		}
	});
	$('input[name="handleUser"]').val(conf.handleUser);

	$('input[name="handleUser_view"]').val(conf.handleUserView);
	$('input[name="handleRole"]').val(conf.handleRole);
	$('input[name="handleRole_view"]').val(conf.handleRoleView);
	$('input[name="handleTeam"]').val(conf.handleTeam);
	$('input[name="handleTeam_view"]').val(conf.handleTeamView);
	$('input[name="handleField"]').val(conf.handleField);

	// 绑定可选处理人信息
	$('select[name="actcChooseableHandlerType"]').val(
			conf.actcChooseableHandlerType);
	showChosseAbleHandleDiv(conf.actcChooseableHandlerType);

	if (conf.actcChooseableHandlerType == "allUser") {
		$("#chooseableHandleRole_div").hide();
		$("#chooseableHandleTeam_div").hide();
		$("#chooseableHandleUser_div").hide();
		$("#chooseableHandleField_div").hide();
		$("#chooseableHandleTrigger_div").hide();
	}

	$('input[name="chooseableHandleUser"]').val(conf.chooseableHandleUser);
	$('input[name="chooseableHandleUser_view"]').val(
			conf.chooseableHandleUserView);
	$('input[name="chooseableHandleRole"]').val(conf.chooseableHandleRole);
	$('input[name="chooseableHandleRole_view"]').val(
			conf.chooseableHandleRoleView);
	$('input[name="chooseableHandleTeam"]').val(conf.chooseableHandleTeam);
	$('input[name="chooseableHandleTeam_view"]').val(
			conf.chooseableHandleTeamView);
	$('input[name="chooseableHandleField"]').val(conf.chooseableHandleField);
	$('input[name="chooseableHandleTriggerTitle"]').val(
			conf.chooseableHandleTriggerTitle);
	$('input[name="chooseableHandleTrigger"]')
			.val(conf.chooseableHandleTrigger);

	$('input[name="outtimeUser"]').val(conf.outtimeUser);
	$('input[name="outtimeUser_view"]').val(conf.outtimeUserView);
	$('input[name="outtimeRole"]').val(conf.outtimeRole);
	$('input[name="outtimeRole_view"]').val(conf.outtimeRoleView);
	$('input[name="outtimeTeam"]').val(conf.outtimeTeam);
	$('input[name="outtimeTeam_view"]').val(conf.outtimeTeamView);
	$('input[name="rejectActivities"]').val(conf.rejectActivities);
	$('input[name="rejectActivities_view"]').val(conf.rejectActivitiesView);
	if (conf.actcCanReject=='TRUE') {
		$("#rejectType_div").show();
		if(conf.actcRejectType=="toActivities"){
			$("#rejectActivities_div").show();
		}else{
			$("#rejectActivities_div").hide();
		}
	}else{
		$("#rejectType_div").hide();
		$("#rejectActivities_div").hide();
	}

	layui.form.render();
	layui.use('layedit', function() {
		var layedit = layui.layedit;
		editIndex = layedit.build('editDemo', {
			tool : [ 'strong', 'italic', 'underline', 'del', '|', 'left',
					'center', 'right' ]
		}); // 建立编辑器
	});
	// 记录当前的数据，用于判断数据是否变动
	preFormData = getFormData();
}

/* 向服务器请求数据 */
function getTriggerInfo() {
	$.ajax({
		url : common.getPath() + "/trigger/search",
		type : "post",
		dataType : "json",
		data : {
			"pageNum" : pageConfig.pageNum,
			"pageSize" : pageConfig.pageSize,
			"triTitle" : pageConfig.triTitle,
			"triType" : pageConfig.triType
		},
		success : function(result) {
			if (result.status == 0) {
				drawTable(result.data);
			}
		}
	});
}
function drawTable(pageInfo) {
	pageConfig.pageNum = pageInfo.pageNum;
	pageConfig.pageSize = pageInfo.pageSize;
	pageConfig.total = pageInfo.total;
	doTriggerPage();
	$("#chooseTrigger_tbody").html("");
	if (pageInfo.total == 0) {
		return;
	}

	var list = pageInfo.list;
	var startSort = pageInfo.startRow;// 开始序号
	var trs = "";
	for (var i = 0; i < list.length; i++) {
		var trigger = list[i];
		var sortNum = startSort + i;
		var tempWebbot;
		var tempParam;
		if (trigger.triWebbot.length > 20) {
			tempWebbot = trigger.triWebbot.substring(0, 20) + "...";
		} else {
			tempWebbot = trigger.triWebbot;
		}
		if (trigger.triParam != null && trigger.triParam.length > 20) {
			tempParam = trigger.triParam.substring(0, 20) + "...";
		} else {
			tempParam = trigger.triParam;
		}
		trs += '<tr><td><input type="checkbox" name="tri_check" value="'
				+ trigger.triUid + '" lay-skin="primary">' + sortNum + '</td>'
				+ '<td>' + trigger.triTitle + '</td>' + '<td>'
				+ trigger.triType + '</td>' + '<td title="' + trigger.triWebbot
				+ '">' + tempWebbot + '</td>' + '<td title="'
				+ trigger.triParam + '">' + tempParam + '</td>' + '</tr>';
	}
	$("#chooseTrigger_tbody").append(trs);
}
// 分页插件刷新
function doTriggerPage() {
	layui.use([ 'laypage', 'layer' ], function() {
		var laypage = layui.laypage, layer = layui.layer;
		// 完整功能
		laypage.render({
			elem : 'lay_page',
			curr : pageConfig.pageNum,
			count : pageConfig.total,
			limit : pageConfig.pageSize,
			layout : [ 'count', 'prev', 'page', 'next', 'skip' ],
			jump : function(obj, first) {
				// 点击新页码进入此方法，obj包含了点击的页数的信息
				pageConfig.pageNum = obj.curr;
				pageConfig.pageSize = obj.limit;
				if (!first) {
					getTriggerInfo();
				}
			}
		});
	});
}
// “查询”按钮
$("#searchTrigger_btn").click(function() {
	pageConfig.triTitle = $("#triTitle_input").val().trim();
	pageConfig.triType = $("#triType_sel").val();
	pageConfig.pageNum = 1;
	getTriggerInfo();
})

function showHandleDiv(assignType) {
	if (assignType == "roleAndDepartment" || assignType == "roleAndCompany"
			|| assignType == "role") {
		$("#handleRole_div").show();
		$("#handleTeam_div").hide();
		$("#handleUser_div").hide();
		$("#handleField_div").hide();
	} else if (assignType == "teamAndCompany" || assignType == "teamAndCompany"
			|| assignType == "team") {
		$("#handleRole_div").hide();
		$("#handleTeam_div").show();
		$("#handleUser_div").hide();
		$("#handleField_div").hide();
	} else if (assignType == "leaderOfPreActivityUser"
			|| assignType == "processCreator" || assignType == 'none') {
		$("#handleRole_div").hide();
		$("#handleTeam_div").hide();
		$("#handleUser_div").hide();
		$("#handleField_div").hide();
	} else if (assignType == "users") {
		$("#handleRole_div").hide();
		$("#handleTeam_div").hide();
		$("#handleUser_div").show();
		$("#handleField_div").hide();
	} else if (assignType == "byField") {
		$("#handleRole_div").hide();
		$("#handleTeam_div").hide();
		$("#handleUser_div").hide();
		$("#handleField_div").show();
	}
}
// 绑定可选处理人下拉列表信息
function showChosseAbleHandleDiv(assignType) {
	if (assignType == "roleAndDepartment" || assignType == "roleAndCompany"
			|| assignType == "role") {
		$("#chooseableHandleRole_div").show();
		$("#chooseableHandleTeam_div").hide();
		$("#chooseableHandleUser_div").hide();
		$("#chooseableHandleField_div").hide();
		$("#chooseableHandleTrigger_div").hide();
	} else if (assignType == "teamAndDepartment"
			|| assignType == "teamAndCompany" || assignType == "team") {
		$("#chooseableHandleRole_div").hide();
		$("#chooseableHandleTeam_div").show();
		$("#chooseableHandleUser_div").hide();
		$("#chooseableHandleField_div").hide();
		$("#chooseableHandleTrigger_div").hide();
	} else if (assignType == "leaderOfPreActivityUser"
			|| assignType == "processCreator" || assignType == 'none') {
		$("#chooseableHandleRole_div").hide();
		$("#chooseableHandleTeam_div").hide();
		$("#chooseableHandleUser_div").hide();
		$("#chooseableHandleField_div").hide();
		$("#chooseableHandleTrigger_div").hide();
	} else if (assignType == "users") {
		$("#chooseableHandleRole_div").hide();
		$("#chooseableHandleTeam_div").hide();
		$("#chooseableHandleUser_div").show();
		$("#chooseableHandleField_div").hide();
		$("#chooseableHandleTrigger_div").hide();
	} else if (assignType == "byField") {
		$("#chooseableHandleRole_div").hide();
		$("#chooseableHandleTeam_div").hide();
		$("#chooseableHandleUser_div").hide();
		$("#chooseableHandleField_div").show();
		$("#chooseableHandleTrigger_div").hide();
	} else if (assignType == "allUser") {
		$("#chooseableHandleRole_div").hide();
		$("#chooseableHandleTeam_div").hide();
		$("#chooseableHandleUser_div").hide();
		$("#chooseableHandleField_div").hide();
		$("#chooseableHandleTrigger_div").hide();
	} else if (assignType == "byTrigger") {
		$("#chooseableHandleRole_div").hide();
		$("#chooseableHandleTeam_div").hide();
		$("#chooseableHandleUser_div").hide();
		$("#chooseableHandleField_div").hide();
		$("#chooseableHandleTrigger_div").show();
	}
}
function moveActivityToRight() {
	$("#left_activity_ul li.colorli").each(function() {
		$(this).removeClass("colorli");
		$(this).appendTo($("#right_activity_ul"));
	});
}
function moveActivityToLeft() {
	$("#right_activity_ul li.colorli").each(function() {
		$(this).removeClass("colorli");
		$(this).appendTo($("#left_activity_ul"));
	});
}
// “保存”按钮
function save(actcUid) {
	if ($("#humanActivity_li").hasClass("layui-this")) {
		// 提交环节配置变更
		layui.layedit.sync(editIndex);
		if (!$('#config_form').valid() || !$('#sla_form').valid()) {
			layer.alert("验证失败，请检查后提交");
			return;
		}
		var info = getFormData();
		$.ajax({
			url : common.getPath() + "/activityConf/update",
			type : "post",
			dataType : "json",
			data : info,
			success : function(result) {
				if (result.status == 0) {
					layer.alert('操作成功');
					if (actcUid) { // 需要切换环节
						$("#my_collapse li").each(function() {
							if ($(this).data('uid') == actcUid) {
								$(this).addClass('link_active');
							} else {
								$(this).removeClass('link_active');
							}
						});
						loadActivityConf(actcUid);
					} else {
						loadActivityConf($('input[name="actcUid"]').val());
					}
				} else {
					// 保存失败的处理
					layer.alert(result.msg);
					if (saveFrom == 'stepLi') {
						$("#actc_li").click();
					}
				}
			},
			error : function() {
				layer.alert('操作失败');
			}
		});
	} else if ($("#gateway_li").hasClass("layui-this")) {
		// 提交网关规则配置
		submitAddDatRule();
	}

}
function getFormData() {
	return $('#config_form').serialize() + "&" + $('#sla_form').serialize();
}

//添加步骤到所有环节
function addStepToAll() {
	
						var stepObjectUid;
						//var $activeLi = $("#my_collapse li.link_active");
						var actcUid = getCurrentActcUid();
						//var activityBpdId = $activeLi.data('activitybpdid');
						var stepBusinessKey;
						console.log($('input:radio[name=stepType]:checked')
								.val());
						var stepBusinessKeyType = $(
								'input[name="stepBusinessKeyType"]:checked')
								.val();
						if (stepBusinessKeyType != 'default') {
							stepBusinessKey = $('input[name="stepBusinessKey"]')
									.val();
							if (!stepBusinessKey
									|| stepBusinessKey.length > 100
									|| stepBusinessKey.trim().length == 0) {
								layer.alert('步骤关键字验证失败，过长或未填写');
								return;
							}
						} else {
							stepBusinessKey = 'default';
						}
						var stepType = $('input[name="stepType"]:checked')
								.val();
						if (stepType == 'form') {
							var formCheck = $('#form_tbody input[name="dynUid_check"]:checked');
							stepObjectUid = formCheck.val();

							if (!stepObjectUid) {
								layer.alert('请选择表单');
								return;
							}

							if (formCheck.length > 1) {
								layer.alert('请选择一个表单，不能选择多个');
								return false;
							}

						}
				layer.confirm(
						'将会覆盖同关键字的表单步骤,是否继续？',
					{
						btn : [ '是', '否' ]
					},
					function() {
						$.ajax({
							url : common.getPath() + "/step/createStepToAll",
							type : "post",
							dataType : "json",
							data : {
								"proAppId" : proAppId,
								"proUid" : proUid,
								"proVerUid" : proVerUid,
								"stepBusinessKey" : stepBusinessKey,
								"stepType" : stepType,
								"stepObjectUid" : stepObjectUid
							},
							success : function(result) {
								if (result.status == 0) {
									layer.alert("创建步骤成功");
									$('#addStep_container').hide();
									loadActivityConf(actcUid);
								} else {
									layer.alert(result.msg);
								}
							},
							error : function() {
								layer.alert('操作失败');
							}
						});
						
						layer.close(1);
					}, function() {
						layer.close(1);
					});
}

// 添加步骤
function addStep() {
	var stepObjectUid;
	var $activeLi = $("#my_collapse li.link_active");
	var actcUid = getCurrentActcUid();
	var activityBpdId = $activeLi.data('activitybpdid');
	var stepBusinessKey;
	// console.log($('input[name="stepType"]:checked').val());
	console.log($('input:radio[name=stepType]:checked').val());
	var stepBusinessKeyType = $('input[name="stepBusinessKeyType"]:checked')
			.val();
	if (stepBusinessKeyType != 'default') {
		stepBusinessKey = $('input[name="stepBusinessKey"]').val();
		if (!stepBusinessKey || stepBusinessKey.length > 100
				|| stepBusinessKey.trim().length == 0) {
			layer.alert('步骤关键字验证失败，过长或未填写');
			return;
		}
	} else {
		stepBusinessKey = 'default';
	}
	var stepType = $('input[name="stepType"]:checked').val();
	if (stepType == 'form') {
		var formCheck = $('#form_tbody input[name="dynUid_check"]:checked');
		stepObjectUid = formCheck.val();

		if (!stepObjectUid) {
			layer.alert('请选择表单');
			return;
		}

		if (formCheck.length > 1) {
			layer.alert('请选择一个表单，不能选择多个');
			return false;
		}

		$.ajax({
			url : common.getPath() + "/step/create",
			type : "post",
			dataType : "json",
			data : {
				"proAppId" : proAppId,
				"proUid" : proUid,
				"proVerUid" : proVerUid,
				"activityBpdId" : activityBpdId,
				"stepBusinessKey" : stepBusinessKey,
				"stepType" : stepType,
				"stepObjectUid" : stepObjectUid
			},
			beforeSend : function(){
				layer.load(1);
			},
			success : function(result) {
				if (result.status == 0) {
					layer.alert("创建步骤成功");
					$('#addStep_container').hide();
					loadActivityConf(actcUid);
				} else {
					layer.alert(result.msg);
				}
				layer.closeAll('loading');
			},
			error : function() {
				layer.alert('操作失败');
				layer.closeAll('loading');
			}
		});

	} else if (stepType == 'trigger') {
		stepObjectUid = $("#trigger_of_step").val();
		if (!stepObjectUid) {
			layer.alert('请选择触发器');
			return;
		}
		$.ajax({
			url : common.getPath() + "/step/create",
			type : "post",
			dataType : "json",
			data : {
				"proAppId" : proAppId,
				"proUid" : proUid,
				"proVerUid" : proVerUid,
				"activityBpdId" : activityBpdId,
				"stepBusinessKey" : stepBusinessKey,
				"stepType" : stepType,
				"stepObjectUid" : stepObjectUid
			},
			success : function(result) {
				if (result.status == 0) {
					$('#addStep_container').hide();
					loadActivityConf(actcUid);
				} else {
					layer.alert(result.msg);
				}
			},
			error : function() {
				layer.alert('操作失败');
			}
		});

	}

}

// 更新步骤关联表单信息
function updateStep() {
	var stepObjectUid;
	var actcUid = getCurrentActcUid();
	var formCheck = $('#update_step_form_tbody input[name="dynUid_check"]:checked');
	stepObjectUid = formCheck.val();

	if (!stepObjectUid) {
		layer.alert('请选择表单');
		return;
	}

	if (formCheck.length > 1) {
		layer.alert('请选择一个表单，不能选择多个');
		return false;
	}

	$.ajax({
		url : common.getPath() + "/step/updateStep",
		type : "post",
		dataType : "json",
		data : {
			"stepUid" : $('#eidtstepUid').val(),
			"stepObjectUid" : stepObjectUid
		},
		success : function(result) {
			if (result.status == 0) {
				$('#update_step_form_container').hide();
				loadActivityConf(actcUid);
			} else {
				layer.alert(result.msg);
			}
		},
		error : function() {
			layer.alert('操作失败');
		}
	});
}

// 更新触发器类型的步骤
function updateTriggerStep() {
	var stepUid = stepUidToEdit;
	var actcUid = getCurrentActcUid();
	var stepObjectUid = $("#ETS_trigger_of_step").val();
	if (!stepObjectUid) {
		layer.alert('请选择触发器');
		return;
	}
	$.ajax({
		url : common.getPath() + "/step/updateStep",
		type : "post",
		dataType : "json",
		data : {
			"stepUid" : stepUid,
			"stepObjectUid" : stepObjectUid
		},
		success : function(result) {
			if (result.status == 0) {
				$('#ETS_container').hide();
				loadActivityConf(actcUid);
			} else {
				layer.alert(result.msg);
			}
		},
		error : function() {
			layer.alert('操作失败');
		}
	});
}
// 删除步骤
function deleteStep(stepUid) {
	if (!stepUid) {
		return;
	}
	$.ajax({
		url : common.getPath() + "/step/delete",
		type : "post",
		dataType : "json",
		data : {
			"stepUid" : stepUid
		},
		success : function(result) {
			if (result.status == 0) {
				layer.alert("删除成功");
				loadActivityConf(getCurrentActcUid());
			} else {
				layer.alert(result.msg);
			}
		},
		error : function() {
			layer.alert('操作失败');
		}
	});
}

function submitAddDatRule() {
}

function resortStep(stepUid, resortType) {
	$.ajax({
		url : common.getPath() + "/step/resortStep",
		type : "post",
		dataType : "json",
		data : {
			"stepUid" : stepUid,
			"resortType" : resortType
		},
		success : function(result) {
			if (result.status == 0) {
				loadActivityConf(getCurrentActcUid());
			} else {
				layer.alert(result.msg);
			}
		},
		error : function() {
			layer.alert('操作失败');
		}
	});
}

// 获得当前正在配置的环节配置主键
function getCurrentActcUid() {
	var $activeLi = $("#my_collapse li.link_active");
	var actcUid = $activeLi.data('uid');
	return actcUid;
}
