// 页面加载完成
$(function () {
	initCollapse();
	if (firstHumanMeteConf) {
        loadActivityConf(firstHumanMeteConf);
	}

	// 校验规则
	$('#config_form')
		.validate({
			rules: {
				actcAssignType: {
					required: true
				},
				handleUser: {
					required: function (element) {
						return $('select[name="actcAssignType"]')
							.val() == 'users';
					}
				},
				handleRole: {
					required: function (element) {
						return $('select[name="actcAssignType"]')
							.val().startsWith('role');
					}
				},
				handleTeam: {
					required: function (element) {
						return $('select[name="actcAssignType"]')
							.val().startsWith('team');
					}
				},
				handleField: {
					required: function (element) {
						return $('select[name="actcAssignType"]')
							.val() == 'byField';
					}
				},
				actcChooseableHandlerType: {
					required: true
				},
				chooseableHandleUser: {
					required: function (element) {
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
				chooseableHandleRole: {
					required: function (element) {
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
				chooseableHandleTeam: {
					required: function (element) {
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
				chooseableHandleField: {
					required: function (element) {
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
				chooseableHandleTrigger: {
					required: function (element) {
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
				actcCanChooseUser: {
					required: true
				},
				actcCanReject: {
					required: true
				},
				actcRejectType: {
					required: function (element) {
						return $('input[name="actcCanReject"]')
							.val() == "TRUE";
					}
				},
				rejectActivities: {
					required: function (element) {
						return ($('input[name="actcCanReject"]:checked')
							.val() == "TRUE" && $(
								'select[name="actcRejectType"]')
							.val() == "toActivities");
					}
				},
				actcCanAutocommit: {
					required: true
				},
				actcCanApprove: {
					required: true
				},
				actcCanSkipFromReject: {
					required: true
				},
				actcCanUploadAttach: {
					required: true
				},
				actcCanEditAttach: {
					required: true
				},
				actcCanDeleteAttach: {
					required: true
				},
				actcCanDelegate: {
					required: true
				},
				actcCanRevoke: {
					required: true
				},
				actcCanAdd: {
					required: true
				},
				actcCanTransfer: {
					required: true
				},
				actcCanMailNotify: {
					required: true
				},
				actcCanMessageNotify: {
					required: true
				},
				actcAssignVariable: {
					required: true
				},
				signCountVarname: {
					required: true
				},
				actcSort: {
					required: true,
					digits: true
				},
				actcMailNotifyTemplate: {
					maxlength: 100
				}
			}
		});

	// 自定义验证规则
	jQuery.validator.addMethod('actcDelayTimeRule', function (value, element) {
		if ($('select[name=actcDelayType]').val() == 'time' && $('input[name=actcIsSystemTask]').val() == 'TRUE') {
			if (!/^[0-9]*[1-9][0-9]*$/.exec(value) || +value > 99999) {
				return false;
			}
			return true;
		} else {
			return true;
		}
	}, '请输入1-5位正整数');
	jQuery.validator.addMethod('actcDelayFieldRule', function (value, element) {
		if ($('select[name=actcDelayType]').val() == 'field' && $('input[name=actcIsSystemTask]').val() == 'TRUE') {
			if (!value || value.trim().length == 0) {
				return false;
			}
			return true;
		} else {
			return true;
		}
	}, '请输入表示时间的表单字段');

	$('#sla_form').validate({
		rules: {
			actcTime: {
				positiveInteger5: true
			},
			actcDelayTime: {
				actcDelayTimeRule: true
			},
			actcTimeunit: {
				required: function (element) {
					return $('input[name="actcMailNotifyTemplate"]')
						.val().trim().length > 0;
				}
			},
			actcDelayTimeunit: {
				required: function (element) {
					return $('select[name=actcDelayType]').val() == 'time' && $('input[name=actcIsSystemTask]').val() == 'TRUE';
				}
			},
			actcOuttimeTrigger: {
				maxlength: 60
			},
			actcOuttimeTemplate: {
				maxlength: 100,
				required: function (element) {
					if ($('select[name="actcOuttimeNotifyType"]').val() != "") {
						return true;
					} else {
						return false;
					}
				}
			},
			outtimeUser: {
				required: function (element) {
					if ($('select[name="actcOuttimeNotifyType"]').val() == "users") {
						return true;
					} else {
						return false;
					}
				}
			},
			actcDelayField: {
				actcDelayFieldRule: true,
				maxlength: 30
			},
			interiorNotifyUser: {
				required: function (element) {
					if ($('select[name="actcInteriorNotifyType"]').val() == "users") {
						return true;
					} else {
						return false;
					}
				}
			},
			interiorNotifyRole: {
				required: function (element) {
					if ($('select[name="actcInteriorNotifyType"]').val() == "role") {
						return true;
					} else {
						return false;
					}
				}
			},
			actcInteriorNotifyTemplate: {
				maxlength: 100,
				required: function (element) {
					if ($('select[name="actcInteriorNotifyType"]').val() != "") {
						return true;
					} else {
						return false;
					}
				}
			},
			actcExteriorNotifyTemplate: {
				maxlength: 100,
				required: function (element) {
					if ($('#exteriorNotifyMail').manifest('values') != null &&
						$('#exteriorNotifyMail').manifest('values').length > 0) {
						return true;
					} else {
						return false;
					}
				}
			}
		},

		messages: {
			actcOuttimeTemplate: {
				required: "选择了超时通知人，未指定模版"
			}
		}
	});

	//选择超时触发器
	$("#choose_outtimeTri_btn").click(function () {
		triggerToEdit = 'actcOuttimeTrigger';
		getTriggerInfo();
		$("#chooseTrigger_container").show();
	});

	// 选择处理人（人员）
	$("#choose_handle_user").click(function () {
		common.chooseUser('handleUser', 'false');
	});
	// 选择处理人（角色）
	$("#choose_handle_role").click(function () {
		common.chooseRole('handleRole', 'false');
	});
	// 选择处理人（角色组）
	$("#choose_handle_team").click(function () {
		common.chooseTeam('handleTeam', 'false');
	});

	// 选择可选处理人（人员）
	$("#choose_able_handle_user").click(function () {
		common.chooseUser('chooseableHandleUser', 'false');
	});
	// 选择可选处理人（角色）
	$("#choose_able_handle_role").click(function () {
		common.chooseRole('chooseableHandleRole', 'false');
	});
	// 选择可选处理人（角色组）
	$("#choose_able_handle_team").click(function () {
		common.chooseTeam('chooseableHandleTeam', 'false');
	});
	// 选择可选处理人（触发器）
	$("#choose_HandleTri_btn").click(function () {
		triggerToEdit = 'chooseableHandleTrigger';
		getTriggerInfo();
		$("#chooseTrigger_container").show();
	});
	// 选择邮件通知模板
	$("#chooseEmailTemplate_i").click(function () {
		common.chooseNotifyTemplate('actcMailNotifyTemplate', 'MAIL_NOTIFY_TEMPLATE');
	});
	// 选择超时通知人（人员）
	$("#choose_outtime_user").click(function () {
		common.chooseUser('outtimeUser', 'false');
	});
	// 选择超时通知人（角色）
	$("#choose_outtime_role").click(function () {
		common.chooseRole('outtimeRole', 'false');
	});
	// 选择超时通知人（角色组）
	$("#choose_outtime_team").click(function () {
		common.chooseTeam('outtimeTeam', 'false');
	});
	// 选择超时通知模板
	$("#chooseOuttimeTemplate_i").click(function () {
		common.chooseNotifyTemplate('actcOuttimeTemplate', '');
	});
	//选择内部通知模板
	$("#chooseInteriorNotifyTemplate_i").click(function () {
		common.chooseNotifyTemplate('actcInteriorNotifyTemplate', 'MAIL_NOTIFY_TEMPLATE');
	})
	//选择外部通知模板
	$("#chooseExteriorNotifyTemplate_i").click(function () {
		common.chooseNotifyTemplate('actcExteriorNotifyTemplate', 'MAIL_NOTIFY_TEMPLATE');
	})
	// 选择内部通知人（人员）
	$("#choose_interior_notify_user").click(function () {
		common.chooseUser('interiorNotifyUser', 'false');
	});
	// 选择内部通知（角色）
	$("#choose_interior_notify_role").click(function () {
		common.chooseRole('interiorNotifyRole', 'false');
	});

	// “选择环节”
	$("#chooseActivity_i").click(function () {
		// $("#left_activity_ul").empty();
		$("#right_activity_ul").empty();
		// $("#left_activity_ul").append(activityStr);
		var choosedValue = $("#rejectActivities").val();
		if (!choosedValue) {
			$("#choose_activity_container").show();
			return;
		}
		var chooseIds = choosedValue.split(";");
		$("#left_activity_ul li").each(function () {
			var activityBpdId = $(this).data('activitybpdid');
			if ($.inArray(activityBpdId, chooseIds) != -1) {
				$(this).appendTo($("#right_activity_ul"));
			}
		});
		$("#choose_activity_container").show();
	})
	$("#search_form_btn").click(function () {
		formTable();
	});

	$("#chooseActivities_sureBtn").click(function () {
		var val = '';
		var val_view = '';
		$("#right_activity_ul li").each(function () {
			val += $(this).data('activitybpdid') + ";";
			val_view += $(this).html() + ";";
		});
		$("#rejectActivities").val(val);
		$("#rejectActivities_view").val(val_view);
		console.log(val);
		console.log(val_view);
		$("#choose_activity_container").hide();
	});

	$("#choose_activity_container").on('click', 'li', function () {
		if ($(this).hasClass('colorli')) {
			$(this).removeClass('colorli');
		} else {
			$(this).addClass('colorli');
		}
	});

	// 点击配置步骤
	$("#step_li").click(function () {
		if (getFormData() != preFormData) { // 配置变化了
			layer.confirm('环节配置有变动，是否保存？', {
				btn: ['保存', '不保存']
			}, function () {
				saveFrom = 'stepLi';
				save('');
			}, function () {});
		}
	});

});

// 初始化折叠菜单
function initCollapse() {
	$.ajax({
		url: common.getPath() + "/activityMeta/getActivitiyMetasForConfig",
		type: "post",
		dataType: "json",
		data: {
			"proAppId": proAppId,
			"proUid": proUid,
			"proVerUid": proVerUid
		},
		success: function (result) {
			if (result.status == 0) {
				printCollapse(result.data);
			} else {
				layer.alert(result.msg);
			}
		},
		error: function () {
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
		str += '<div class="layui-colla-item">' +
			'<h2 class="layui-colla-title">' + name + '</h2>';
		if (process.id == 'main') {
			// 如果是主流程的话，展开
			str += '<div class="layui-colla-content layui-show" id="content' +
				i + '">';
		} else {
			str += '<div class="layui-colla-content " id="content' + i + '">';
		}
		str += '<ul class="link_list">';
		for (var j = 0; j < children.length; j++) {
			var meta = children[j];
			if (meta.activityId == firstHumanMeta) {
				// 如果这个环节是此流程的第一个环节
				str += '<li data-parentActivityId="' + meta.parentActivityId +
					'" data-uid="' + meta.actcUid +
					'" data-activitybpdid="' + meta.activityBpdId +
					'" class="link_active" onclick="clickLi(this);" title="' + meta.activityName + '">';
				if (meta.activityName.length > 10) {
					str += meta.activityName.substring(0, 10) + "...";
				} else {
					str += meta.activityName;
				}
				str += '</li>';
				$("#left_activity_ul").empty();
				$("#left_activity_ul").append(activityStr);
				var leftLi = $("#left_activity_ul").find("li");
				$(leftLi).each(function () {
					var checkParentId = $(this).data('parentactivityid');
					if (checkParentId != meta.parentActivityId) {
						$(this).remove();
					}
				});
			} else {
				str += '<li data-parentActivityId="' + meta.parentActivityId +
					'" data-uid="' + meta.actcUid +
					'" data-activitybpdid="' + meta.activityBpdId +
					'" onclick="clickLi(this);" title="' + meta.activityName + '">';
				if (meta.activityName.length > 10) {
					str += meta.activityName.substring(0, 10) + "...";
				} else {
					str += meta.activityName;
				}
				str += '</li>';
			}
		}
		str += '</ul>' + '</div>' + '</div>';
	}
	$("#my_collapse").append(str);
	layui.use('element', function () {
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
		$(leftLi).each(function () {
			var checkParentId = $(this).data('parentactivityid');
			if (checkParentId != parentActivityId) {
				$(this).remove();
			}
		});
		if (getFormData() != preFormData) { // 配置变化了
			layer.confirm('是否先保存数据再切换环节？', {
				btn: ['确定', '取消']
			}, function () {
				saveFrom = 'activityLi';
				save(actcUid);
			}, function () {
				$("#my_collapse li").each(function () {
					$(this).removeClass('link_active');
				});
				$li.addClass('link_active');
				loadActivityConf(actcUid);
			});

		} else { // 配置没有变化
			$("#my_collapse li").each(function () {
				$(this).removeClass('link_active');
			});
			$li.addClass('link_active');
			loadActivityConf(actcUid);
		}

	}
}

// ajax获取配置文件信息
var loadConfIndex = '';
function loadActivityConf(actcUid) {
	$.ajax({
		url: common.getPath() + "/activityConf/getData",
		type: "post",
		dataType: "json",
		data: {
			"actcUid": actcUid
		},
		beforeSend: function () {
            loadConfIndex = layer.load(1);
        },
		success: function (result) {
			layer.close(loadConfIndex);
			if (result.status == 0) {
				initConf(result.data);
				document.getElementById("activityId").value = result.data.conf.activityId
				step_table(result.data.stepList);

			} else {
				layer.alert(result.msg);
			}
		},
		error: function () {
			layer.alert('操作失败,请稍后再试');
		}
	});
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
	$("#outtimeUser_div").hide();
	systemTask.initValue(conf);

	// 初始化输入框
	$('input[name="actcUid"]').val(conf.actcUid);
	$('input[name="actcSort"]').val(conf.actcSort);
	$('input[name="actcTime"]').val(conf.actcTime);
	$('input[name="actcMailNotifyTemplate"]').val(conf.actcMailNotifyTemplate);
	$('input[name="actcOuttimeTrigger"]').val(conf.actcOuttimeTrigger);
	$('input[name="actcOuttimeTriggerTitle"]')
		.val(conf.actcOuttimeTriggerTitle);
	$('input[name="actcOuttimeTemplate"]').val(conf.actcOuttimeTemplate);
	$('input[name="actcOuttimeTemplate_view"]').val(conf.actcOuttimeTemplateView);
	$('select[name="actcOuttimeNotifyType"]').val(conf.actcOuttimeNotifyType);
	if (conf.actcOuttimeNotifyType == "users") {
		$("#outtimeUser_div").show();
	} else {
		$("#outtimeUser_div").hide();
	}

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

	$('input[name="actcCanEditAttach"]').each(function () {
		if ($(this).val() == conf.actcCanEditAttach) {
			$(this).prop("checked", true);
		} else {
			$(this).prop("checked", false);
		}
	});
	$('input[name="actcCanUploadAttach"]').each(function () {
		if ($(this).val() == conf.actcCanUploadAttach) {
			$(this).prop("checked", true);
		} else {
			$(this).prop("checked", false);
		}
	});
	$('input[name="actcCanDeleteAttach"]').each(function () {
		if ($(this).val() == conf.actcCanDeleteAttach) {
			$(this).prop("checked", true);
		} else {
			$(this).prop("checked", false);
		}
	});
	$('input[name="actcCanDelegate"]').each(function () {
		if ($(this).val() == conf.actcCanDelegate) {
			$(this).prop("checked", true);
		} else {
			$(this).prop("checked", false);
		}
	});
	$('input[name="actcCanMessageNotify"]').each(function () {
		if ($(this).val() == conf.actcCanMessageNotify) {
			$(this).prop("checked", true);
		} else {
			$(this).prop("checked", false);
		}
	});
	$('input[name="actcCanMailNotify"]').each(function () {
		if ($(this).val() == conf.actcCanMailNotify) {
			$(this).prop("checked", true);
		} else {
			$(this).prop("checked", false);
		}
	});
	$('input[name="actcCanMessageNotify"]').each(
		function () {
			if ($(this).val() == conf.actcCanMessageNotify) {
				$(this).prop("checked", true);
				$('input[name = "actcMessageNotifyTemplate"]').val(
					conf.actcMessageNotifyTemplate);
			} else {
				$(this).prop("checked", false);
			}
		});
	$('input[name="actcCanReject"]').each(function () {
		if ($(this).val() == conf.actcCanReject) {
			$(this).prop("checked", true);
		} else {
			$(this).prop("checked", false);
		}
	});

	$('input[name="actcCanRevoke"]').each(function () {
		if ($(this).val() == conf.actcCanRevoke) {
			$(this).prop("checked", true);
		} else {
			$(this).prop("checked", false);
		}
	});
	$('input[name="actcCanAutocommit"]').each(function () {
		if ($(this).val() == conf.actcCanAutocommit) {
			$(this).prop("checked", true);
		} else {
			$(this).prop("checked", false);
		}
	});
	$('input[name="actcCanAdd"]').each(function () {
		if ($(this).val() == conf.actcCanAdd) {
			$(this).prop("checked", true);
		} else {
			$(this).prop("checked", false);
		}
	});
	$('input[name="actcCanApprove"]').each(function () {
		if ($(this).val() == conf.actcCanApprove) {
			$(this).prop("checked", true);
		} else {
			$(this).prop("checked", false);
		}
	});
	$('input[name="actcCanSkipFromReject"]').each(function () {
		if ($(this).val() == conf.actcCanSkipFromReject) {
			$(this).prop("checked", true);
		} else {
			$(this).prop("checked", false);
		}
	});

	$('input[name="actcCanChooseUser"]').each(function () {
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
	$('input[name="actcCanTransfer"]').each(function () {
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
	if (conf.actcCanReject == 'TRUE') {
		$("#rejectType_div").show();
		if (conf.actcRejectType == "toActivities") {
			$("#rejectActivities_div").show();
		} else {
			$("#rejectActivities_div").hide();
		}
	} else {
		$("#rejectType_div").hide();
		$("#rejectActivities_div").hide();
	}

	//绑定通知模板信息
	$('input[name="actcMailNotifyTemplate"]').val(conf.actcMailNotifyTemplate);
	$('input[name="actcMailNotifyTemplate_view"]').val(conf.actcMailNotifyTemplateView);

	//绑定通知指定人信息
	$('select[name="actcInteriorNotifyType"]').val(conf.actcInteriorNotifyType);
	$('input[name="actcInteriorNotifyTemplate"]').val(conf.actcInteriorNotifyTemplate);
	$('input[name="actcInteriorNotifyTemplate_view"]').val(conf.actcInteriorNotifyTemplateView);
	$('input[name="actcExteriorNotifyTemplate"]').val(conf.actcExteriorNotifyTemplate);
	$('input[name="actcExteriorNotifyTemplate_view"]').val(conf.actcExteriorNotifyTemplateView);

	//$("#exteriorNotifyMail").val("");
	if (conf.actcInteriorNotifyType == '' || conf.actcInteriorNotifyType == null) {
		$("#interiorNotifyUser_div").hide();
		$("#interiorNotifyRole_div").hide();
	} else {
		if (conf.actcInteriorNotifyType == "users") {
			$("#interiorNotifyUser").val(conf.interiorNotifyUser);
			$("#interiorNotifyUser_view").val(conf.interiorNotifyUserView);
			$("#interiorNotifyUser_div").show();
			$("#interiorNotifyRole_div").hide();
		} else if (conf.actcInteriorNotifyType == "role"|| conf.actcInteriorNotifyType=="roleAndCompany") {
			$("#interiorNotifyRole").val(conf.interiorNotifyRole);
			$("#interiorNotifyRole_view").val(conf.interiorNotifyRoleView);
			$("#interiorNotifyUser_div").hide();
			$("#interiorNotifyRole_div").show();
		}
	}
	$('#exteriorNotifyMail').manifest('remove');
	if (conf.exteriorNotifyMailList != null) {
		for (var i = 0; i < conf.exteriorNotifyMailList.length; i++) {
			$('#exteriorNotifyMail').manifest('add', conf.exteriorNotifyMailList[i], null, true, false);
		}
	}


	layui.use('layedit', function () {
		var layedit = layui.layedit;
		editIndex = layedit.build('editDemo', {
			tool: ['strong', 'italic', 'underline', 'del', '|', 'left',
				'center', 'right'
			]
		}); // 建立编辑器
	});

	layui.form.render();
	// 记录当前的数据，用于判断数据是否变动
	preFormData = getFormData();
}

/* 向服务器请求数据 */
function getTriggerInfo() {
	$.ajax({
		url: common.getPath() + "/trigger/search",
		type: "post",
		dataType: "json",
		data: {
			"pageNum": pageConfig.pageNum,
			"pageSize": pageConfig.pageSize,
			"triTitle": pageConfig.triTitle,
			"triType": pageConfig.triType
		},
		success: function (result) {
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
	var startSort = pageInfo.startRow; // 开始序号
	var trs = "";
	for (var i = 0; i < list.length; i++) {
		var trigger = list[i];
		var sortNum = startSort + i;
		var tempWebbot;
		var tempParam;
		if (trigger.triWebbot != null && trigger.triWebbot.length > 20) {
			tempWebbot = trigger.triWebbot.substring(0, 20) + "...";
		} else {
			tempWebbot = trigger.triWebbot;
		}
		if (trigger.triParam != null && trigger.triParam.length > 20) {
			tempParam = trigger.triParam.substring(0, 20) + "...";
		} else {
			tempParam = trigger.triParam;
		}
		trs += '<tr><td><input type="checkbox" name="tri_check" value="' +
			trigger.triUid + '" lay-skin="primary">' + sortNum + '</td>' +
			'<td>' + trigger.triTitle + '</td>' + '<td>' +
			trigger.triType + '</td>' + '<td title="' + trigger.triWebbot +
			'">' + tempWebbot + '</td>' + '<td title="' +
			trigger.triParam + '">' + tempParam + '</td>' + '</tr>';
	}
	$("#chooseTrigger_tbody").append(trs);
}
// 分页插件刷新
function doTriggerPage() {
	layui.use(['laypage', 'layer'], function () {
		var laypage = layui.laypage,
			layer = layui.layer;
		// 完整功能
		laypage.render({
			elem: 'lay_page',
			curr: pageConfig.pageNum,
			count: pageConfig.total,
			limit: pageConfig.pageSize,
			layout: ['count', 'prev', 'page', 'next', 'skip'],
			jump: function (obj, first) {
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
$("#searchTrigger_btn").click(function () {
	pageConfig.triTitle = $("#triTitle_input").val().trim();
	pageConfig.triType = $("#triType_sel").val();
	pageConfig.pageNum = 1;
	getTriggerInfo();
})

function showHandleDiv(assignType) {
	if (assignType == "roleAndDepartment" || assignType == "roleAndCompany" ||
		assignType == "role") {
		$("#handleRole_div").show();
		$("#handleTeam_div").hide();
		$("#handleUser_div").hide();
		$("#handleField_div").hide();
	} else if (assignType == "teamAndCompany" || assignType == "teamAndDepartment" ||
		assignType == "team") {
		$("#handleRole_div").hide();
		$("#handleTeam_div").show();
		$("#handleUser_div").hide();
		$("#handleField_div").hide();
	} else if (assignType == "leaderOfPreActivityUser" ||
		assignType == "processCreator" || assignType == 'none') {
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
	if (assignType == "roleAndDepartment" || assignType == "roleAndCompany" ||
		assignType == "role") {
		$("#chooseableHandleRole_div").show();
		$("#chooseableHandleTeam_div").hide();
		$("#chooseableHandleUser_div").hide();
		$("#chooseableHandleField_div").hide();
		$("#chooseableHandleTrigger_div").hide();
	} else if (assignType == "teamAndDepartment" ||
		assignType == "teamAndCompany" || assignType == "team") {
		$("#chooseableHandleRole_div").hide();
		$("#chooseableHandleTeam_div").show();
		$("#chooseableHandleUser_div").hide();
		$("#chooseableHandleField_div").hide();
		$("#chooseableHandleTrigger_div").hide();
	} else if (assignType == "leaderOfPreActivityUser" ||
		assignType == "processCreator" || assignType == 'none') {
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
	$("#left_activity_ul li.colorli").each(function () {
		$(this).removeClass("colorli");
		$(this).appendTo($("#right_activity_ul"));
	});
}

function moveActivityToLeft() {
	$("#right_activity_ul li.colorli").each(function () {
		$(this).removeClass("colorli");
		$(this).appendTo($("#left_activity_ul"));
	});
}
// “保存”按钮
function save(actcUid) {
	if ($("#humanActivity_li").hasClass("layui-this")) {
		// 提交环节配置变更
		layui.layedit.sync(editIndex);
		//设置邮箱地址的值
		/*var emailValues = $('#exteriorNotifyMail').manifest('values');
		var str = "";
		for (var i = 0; i < emailValues.length; i++) {
			str += emailValues[i]+";";
		}
		//$("#exteriorNotifyMail").val(str);
		*/
		if (!$('#config_form').valid() || !$('#sla_form').valid()) {
			layer.alert("验证失败，请检查后提交");
			return;
		}

		var info = getFormData();

		$.ajax({
			url: common.getPath() + "/activityConf/update",
			type: "post",
			dataType: "json",
			data: info,
			success: function (result) {
				if (result.status == 0) {
					layer.alert('操作成功');
					if (actcUid) { // 需要切换环节
						$("#my_collapse li").each(function () {
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
			error: function () {
				$("#exteriorNotifyMail").val("");
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
	var actcUid = getCurrentActcUid();
	var stepBusinessKey;
	console.log($('input:radio[name=stepType]:checked')
		.val());
	var stepBusinessKeyType = $(
			'input[name="stepBusinessKeyType"]:checked')
		.val();
	if (stepBusinessKeyType != 'default') {
		stepBusinessKey = $('input[name="stepBusinessKey"]')
			.val();
		if (!stepBusinessKey ||
			stepBusinessKey.length > 100 ||
			stepBusinessKey.trim().length == 0) {
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
		'将会覆盖同关键字的表单步骤,是否继续？', {
			btn: ['是', '否']
		},
		function () {
			$.ajax({
				url: common.getPath() + "/step/createStepToAll",
				type: "post",
				dataType: "json",
				data: {
					"proAppId": proAppId,
					"proUid": proUid,
					"proVerUid": proVerUid,
					"stepBusinessKey": stepBusinessKey,
					"stepType": stepType,
					"stepObjectUid": stepObjectUid
				},
				success: function (result) {
					if (result.status == 0) {
						layer.alert("创建步骤成功");
						$('#addStep_container').hide();
						loadActivityConf(actcUid);
					} else {
						layer.alert(result.msg);
					}
				},
				error: function () {
					layer.alert('操作失败');
				}
			});

			layer.close(1);
		},
		function () {
			layer.close(1);
		});
}

// 获得当前正在配置的环节配置主键
function getCurrentActcUid() {
	var $activeLi = $("#my_collapse li.link_active");
	var actcUid = $activeLi.data('uid');
	return actcUid;
}