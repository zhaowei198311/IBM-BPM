$(function () {
    //选择触发器弹框
    $("#chooseTrigger_container").on("click", ":checkbox", function () {
        if ($(this).prop("checked")) {
            $("#chooseTrigger_container :checkbox").prop("checked", false);
            $(this).prop("checked", true);
        }
    });
    //新增步骤弹出框（渲染表单的表格）
    $("#form_tbody").on("click", ":checkbox", function () {
        if ($(this).prop("checked")) {
            $("#form_tbody :checkbox").prop("checked", false);
            $(this).prop("checked", true);
        }
    });

    //普通字段权限只读
    $("#fieldviewAllclick").click(
        function () {
            if (this.checked) {
                $("#field_permissions_table").find(
                    "input[value='VIEW']").prop("checked", true);
            }
        });

    //普通字段权限隐藏
    $("#fieldhiddenAllclick").click(
        function () {
            if (this.checked) {
                $("#field_permissions_table").find(
                    "input[value='HIDDEN']").prop("checked", true);
            }
        });

    //标题字段权限只读
    $("#titleviewAllclick").click(
        function () {
            if (this.checked) {
                $("#title_permissions_table").find(
                    "input[value='VIEW']").prop("checked", true);
            }
        });

    //标题字段权限隐藏
    $("#titlehiddenAllclick").click(
        function () {
            if (this.checked) {
                $("#title_permissions_table").find(
                    "input[value='HIDDEN']").prop("checked", true);
            }
        });

    // “取消”选择触发器
    $("#chooseTrigger_cancelBtn").click(function () {
        $("#chooseTrigger_container").hide();
    });

    // 新增步骤中点击选择触发器
    $("#choose_stepTri_btn").click(function () {
        triggerToEdit = 'trigger_of_step';
        getTriggerInfo();
        $("#chooseTrigger_container").show();
    });
    $("#ETS_choose_stepTri_btn").click(function () {
        triggerToEdit = 'ETS_trigger_of_step';
        getTriggerInfo();
        $("#chooseTrigger_container").show();
    });

    // “新增步骤”按钮
    $("#add_step_btn").click(function () {
        $('#addStep_form')[0].reset();
        $('input[name="stepType"]').each(function () {
            if ($(this).val() == 'form') {
                $(this).prop("checked", true);
            } else {
                $(this).prop("checked", false);
            }
        });
        $('input[name="stepSort"]').val("");
        $('input[name="stepBusinessKeyType"]').each(function () {
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
});

//保存字段权限
function saveFieldPermission() {
    var $activeLi = $("#my_collapse li.link_active");
    var actcUid = $activeLi.data('uid');

    var jsonArr = new Array();
    //普通字段的权限信息
    var radioSelArr = $("#field_permissions_table tbody").find(
        "input[type='radio']:checked");
    radioSelArr.each(function () {
        var opObjUid = $(this).parent().parent().find(
            "input[name='fldUid']").val();
        var stepUid = $(this).parent().parent().find(
            "input[name='stepUid']").val();
        var jsonParam = {
            stepUid: stepUid, //步骤ID
            opObjUid: opObjUid, //表单字段ID
            opObjType: "FIELD",
            opAction: "" //EDIT，HIDDEN，VIEW
        };
        var opAction = $(this).val();
        jsonParam.opAction = opAction;
        jsonArr.push(jsonParam);
    });

    var fieldCheckArr = $("#field_permissions_table tbody")
        .find("input[type='checkbox']:checked");
    fieldCheckArr.each(function () {
        var opObjUid = $(this).parent().parent().find(
            "input[name='fldUid']").val();
        var stepUid = $(this).parent().parent().find(
            "input[name='stepUid']").val();
        var jsonParam = {
            stepUid: stepUid, //步骤ID
            opObjUid: opObjUid, //表单字段ID
            opObjType: "FIELD",
            opAction: "" //PRINT，SKIP
        };
        var opAction = $(this).val();
        jsonParam.opAction = opAction;
        jsonArr.push(jsonParam);
    });

    //标题块的权限信息
    var titleRadioSelArr = $("#title_permissions_table tbody")
        .find("input[type='radio']:checked");
    titleRadioSelArr.each(function () {
        var opObjUid = $(this).parent().parent().find(
            "input[name='fldUid']").val();
        var stepUid = $(this).parent().parent().find(
            "input[name='stepUid']").val();
        var jsonParam = {
            stepUid: stepUid, //步骤ID
            opObjUid: opObjUid, //表单字段ID
            opObjType: "FIELD",
            opAction: "" //EDIT，HIDDEN，VIEW
        };
        var opAction = $(this).val();
        jsonParam.opAction = opAction;
        jsonArr.push(jsonParam);
    });

    var titleCheckArr = $("#title_permissions_table tbody")
        .find("input[type='checkbox']:checked");
    titleCheckArr.each(function () {
        var opObjUid = $(this).parent().parent().find(
            "input[name='fldUid']").val();
        var stepUid = $(this).parent().parent().find(
            "input[name='stepUid']").val();
        var jsonParam = {
            stepUid: stepUid, //步骤ID
            opObjUid: opObjUid, //表单字段ID
            opObjType: "FIELD",
            opAction: "" //PRINT，SKIP
        };
        var opAction = $(this).val();
        jsonParam.opAction = opAction;
        jsonArr.push(jsonParam);
    });
    console.log(JSON.stringify(jsonArr));
    //给表单字段添加权限
    $.ajax({
        url: common.getPath() +
            "/formField/saveFormFieldPermission",
        method: "post",
        dataType: "json",
        contentType: "application/json",
        beforeSend: function () {
            layer.load(1);
        },
        data: JSON.stringify(jsonArr),
        success: function (result) {
            if (result.status == 0) {
                $('#editFieldPermissions').hide();
                layer.alert("修改成功");
                loadActivityConf(actcUid);
            } else {
                layer.alert(result.msg);
            }
            layer.closeAll("loading");
        },
        error: function () {
            layer.closeAll("loading");
        }
    });
}

//编辑 只读  隐藏
function radiocheckAll(dataLength) {
    var $table = $("#field_permissions_table");
    var HIDDEN = $table.find("input[value='HIDDEN']:checked").length;
    var VIEW = $table.find("input[value='VIEW']:checked").length;
    var EDIT = $table.find("input[value='EDIT']:checked").length;
    if (HIDDEN == dataLength) {
        $('#fieldhiddenAllclick').click();
    } else if (VIEW == dataLength) {
        $('#fieldviewAllclick').click();
    } else if (EDIT == dataLength) {
        $('#fieldradioedit').click();
    } else {
        $("#editFieldPermissions input[name=radioAll]").prop("checked",
            false);
    }
}

function editAllclick(obj) {
    if (obj.checked) {
        $("#field_permissions_table").find("input[value='EDIT']").prop(
            "checked", true);
    }
}

//编辑 只读  隐藏
function radiocheckAlltitle(dataLength) {
    var $table = $("#title_permissions_table");
    var HIDDEN = $table.find("input[value='HIDDEN']:checked").length;
    var VIEW = $table.find("input[value='VIEW']:checked").length;
    var EDIT = $table.find("input[value='EDIT']:checked").length;
    if (HIDDEN == dataLength) {
        $('#titlehiddenAllclick').click();
    } else if (VIEW == dataLength) {
        $('#titleviewAllclick').click();
    } else if (EDIT == dataLength) {
        $('#titleradioedit').click();
    } else {
        $("#editFieldPermissions input[name=titleradioAll]").prop(
            "checked", false);
    }
}

function titleeditAllclick(obj) {
    if (obj.checked) {
        $("#title_permissions_table").find("input[value='EDIT']").prop(
            "checked", true);
    }
}

// 渲染权限信息表格
function drawPerTable(result, perTableId, title) {
	$("#" + perTableId + " tbody").empty();
	var trs = '';
	$(result.data)
		.each(function (index, val) {
			if ($(this).fldType != "hidden") {
				trs += '<tr>';
				trs += '<td>' + (index + 1);
				trs += '<input type="hidden" name="fldUid" value="' +
					this.fldUid + '">';
				trs += '<input type="hidden" name="stepUid" value="' +
					dates.stepUid + '">';
				trs += '</td>';
				trs += '<td>' + this.fldName + '</td>';
				for (var i = 0; i < 3; i++) {
					var opActionText = (this.opActionList)[i];
					switch (i) {
						case 0:
							{
								if (opActionText == 'EDIT') {
									trs += '<td><input type="radio"  name="opAction_' +
										title +
										this.fldUid +
										'" checked="checked" value="EDIT"/></td>';
									trs += '<td><input type="radio"  name="opAction_' +
										title +
										this.fldUid +
										'"  value="VIEW"/></td>';
									trs += '<td><input type="radio"  name="opAction_' +
										title +
										this.fldUid +
										'" value="HIDDEN"/></td>';
								} else if (opActionText == 'VIEW') {
									trs += '<td><input type="radio"  name="opAction_' +
										title +
										this.fldUid +
										'"  value="EDIT"/></td>';
									trs += '<td><input type="radio"  name="opAction_' +
										title +
										this.fldUid +
										'"  checked="checked"  value="VIEW"/></td>';
									trs += '<td><input type="radio"  name="opAction_' +
										title +
										this.fldUid +
										'" value="HIDDEN"/></td>';
								} else if (opActionText == 'HIDDEN') {
									trs += '<td><input type="radio"  name="opAction_' +
										title +
										this.fldUid +
										'"  value="EDIT"/></td>';
									trs += '<td><input type="radio"  name="opAction_' +
										title +
										this.fldUid +
										'"  value="VIEW"/></td>';
									trs += '<td><input type="radio"  name="opAction_' +
										title +
										this.fldUid +
										'" checked="checked" value="HIDDEN"/></td>';
								}
								break;
							};
						case 1:
							{
								if (opActionText == 'PRINT') {
									trs += '<td style="border-left:1px solid #CCC"><input type="checkbox" name="checkboxSel' +
										title +
										'" value="PRINT" onclick="onClickSel' +
										title + '(this)" checked/></td>';
								} else {
									trs += '<td style="border-left:1px solid #CCC"><input type="checkbox" name="checkboxSel' +
										title +
										'" value="PRINT" onclick="onClickSel' +
										title + '(this)"/></td>';
								}
								break;
							};
						case 2:
							{
								if (opActionText == 'SKIP') {
									trs += '<td><input type="checkbox" name="skipSel' +
										title +
										'" value="SKIP" onclick="skipSel' +
										title + '(this)" checked/></td>';
								} else {
									trs += '<td><input type="checkbox" name="skipSel' +
										title +
										'" value="SKIP" onclick="onSkipSel' +
										title + '(this)"/></td>';
								}
								break;
							};
					}
				}
				trs += '</tr>';
			}
		});
	$("#" + perTableId + " tbody").append(trs);
	if (title != null && title != "") {
		radiocheckAlltitle(result.data.length);
	} else {
		radiocheckAll(result.data.length);
	}
	$("#" + perTableId + " tbody input[type='radio']").bind("click",
		function () {
			if (title != null && title != "") {
				radiocheckAlltitle(result.data.length);
			} else {
				radiocheckAll(result.data.length);
			}
		});

	$("#" + perTableId + " tbody input[type='checkbox']").each(function () {
		var name = $(this).attr("name");
		if (name.indexOf("checkbox") != -1) {
			if (title != null && title != "") {
				onClickSeltitle(this);
			} else {
				onClickSel(this);
			}
		} else {
			if (title != null && title != "") {
				onSkipSeltitle(this);
			} else {
				onSkipSel(this);
			}
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
		$("input[name='checkboxSel']").each(function () {
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
		$("input[name='checkboxSeltitle']").each(function () {
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

//跳过必填的复选框全选，取消全选
function onSkipValidateHander(obj) {
	if (obj.checked) {
		$("input[name='skipSel']").prop("checked", true);
	} else {
		$("input[name='skipSel']").prop("checked", false);
	}
}

//跳过必填的复选框分选
function onSkipSel(obj) {
	if (obj.checked) {
		var allSel = false;
		$("input[name='skipSel']").each(function () {
			if (!$(this).is(":checked")) {
				allSel = true;
			}
		});

		// 如果有checkbox没有被选中
		if (allSel) {
			$("input[name='skipValidateAll']").prop("checked", false);
		} else {
			$("input[name='skipValidateAll']").prop("checked", true);
		}
	} else {
		$("input[name='skipValidateAll']").prop("checked", false);
	}
}

//跳过必填的复选框全选，取消全选
function onSkipValidateHandertitle(obj) {
	if (obj.checked) {
		$("input[name='skipSeltitle']").prop("checked", true);
	} else {
		$("input[name='skipSeltitle']").prop("checked", false);
	}
}

//跳过必填的复选框分选
function onSkipSeltitle(obj) {
	if (obj.checked) {
		var allSel = false;
		$("input[name='skipSeltitle']").each(function () {
			if (!$(this).is(":checked")) {
				allSel = true;
			}
		});

		// 如果有checkbox没有被选中
		if (allSel) {
			$("input[name='skipValidateAlltitle']").prop("checked", false);
		} else {
			$("input[name='skipValidateAlltitle']").prop("checked", true);
		}
	} else {
		$("input[name='skipValidateAlltitle']").prop("checked", false);
	}
}

//步骤编辑
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

// 修改步骤关联表单信息
var updateFormUid = ""; // 要修改步骤的关联表单
$("#update_search_form_btn").click(function () {
	updateFormTable(updateFormUid);
});
//渲染修改步骤关联表单信息的表格
function updateFormTable(formUid) {
	$.ajax({
		url: common.getPath() + "/formManage/queryFormListBySelective",
		type: "post",
		data: {
			proUid: proUid,
			proVersion: proVerUid,
			dynTitle: $('#updateDynTitle').val(),
			dynDescription: $('#updateDynDescription').val()
		},
		dataType: "json",
		success: function (result) {
			$('#update_step_form_tbody').empty();
			var trs = '';
			$(result.data)
				.each(
					function (index) {
						trs += '<tr>';
						if (formUid == this.dynUid) {
							trs += '<td><input type="checkbox" name="dynUid_check" value="' +
								this.dynUid +
								'" lay-skin="primary" checked>' +
								(index + 1) + '</td>';
						} else {
							trs += '<td><input type="checkbox" name="dynUid_check" value="' +
								this.dynUid +
								'" lay-skin="primary">' +
								(index + 1) + '</td>';
						}
						trs += '<td>' + this.dynTitle + '</td>'
						if (this.dynDescription != null && this.dynDescription != "") {
							trs += '<td>' + this.dynDescription +
								'</td>'
						} else {
							trs += '<td></td>'
						}
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
	updateFormUid = dates.stepObjectUid;
	updateFormTable(dates.stepObjectUid);
}

// 添加步骤
function addStep() {
	var stepObjectUid;
	var $activeLi = $("#my_collapse li.link_active");
	var actcUid = getCurrentActcUid();
	var activityId = $("#activityId").val();
	var activityBpdId = $activeLi.data('activitybpdid');
	var stepBusinessKey;
	// console.log($('input[name="stepType"]:checked').val());
	console.log($('input:radio[name=stepType]:checked').val());
	var stepBusinessKeyType = $('input[name="stepBusinessKeyType"]:checked')
		.val();
	if (stepBusinessKeyType != 'default') {
		stepBusinessKey = $('input[name="stepBusinessKey"]').val();
		if (!stepBusinessKey || stepBusinessKey.length > 100 ||
			stepBusinessKey.trim().length == 0) {
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
			url: common.getPath() + "/step/create",
			type: "post",
			dataType: "json",
            async : false,
			data: {
				"proAppId": proAppId,
				"proUid": proUid,
				"proVerUid": proVerUid,
				"activityBpdId": activityBpdId,
				"stepBusinessKey": stepBusinessKey,
				"stepType": stepType,
				"stepObjectUid": stepObjectUid,
				"actcUid": ""
			},
			beforeSend: function () {
				layer.load(1);
			},
			success: function (result) {
				if (result.status == 0) {
					layer.alert("创建步骤成功");
					$('#addStep_container').hide();
					loadActivityConf(actcUid);
				} else {
					layer.alert(result.msg);
				}
				layer.closeAll('loading');
			},
			error: function () {
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
		// 判断参数是否映射

		$.ajax({
			url: common.getPath() + "/step/create",
			type: "post",
			dataType: "json",
			async: false,
			data: {
				"proAppId": proAppId,
				"proUid": proUid,
				"proVerUid": proVerUid,
				"activityBpdId": activityBpdId,
				"stepBusinessKey": stepBusinessKey,
				"stepType": stepType,
				"stepObjectUid": stepObjectUid,
				"actcUid": activityId
			},
			success: function (result) {
				if (result.status == 0) {
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
		url: common.getPath() + "/step/updateStep",
		type: "post",
		dataType: "json",
		async: false,
		data: {
			"stepUid": $('#eidtstepUid').val(),
			"stepObjectUid": stepObjectUid
		},
		success: function (result) {
			if (result.status == 0) {
				$('#update_step_form_container').hide();
				loadActivityConf(actcUid);
			} else {
				layer.alert(result.msg);
			}
		},
		error: function () {
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
		url: common.getPath() + "/step/updateStep",
		type: "post",
		dataType: "json",
		async: false,
		data: {
			"stepUid": stepUid,
			"stepObjectUid": stepObjectUid
		},
		success: function (result) {
			if (result.status == 0) {
				$('#ETS_container').hide();
				loadActivityConf(actcUid);
			} else {
				layer.alert(result.msg);
			}
		},
		error: function () {
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
		url: common.getPath() + "/step/delete",
		type: "post",
		dataType: "json",
		async: false,
		data: {
			"stepUid": stepUid
		},
		success: function (result) {
			if (result.status == 0) {
				layer.alert("删除成功");
				loadActivityConf(getCurrentActcUid());
			} else {
				layer.alert(result.msg);
			}
		},
		error: function () {
			layer.alert('操作失败');
		}
	});
}

function resortStep(stepUid, resortType) {
	$.ajax({
		url: common.getPath() + "/step/resortStep",
		type: "post",
		dataType: "json",
		async: false,
		data: {
			"stepUid": stepUid,
			"resortType": resortType
		},
		success: function (result) {
			if (result.status == 0) {
				loadActivityConf(getCurrentActcUid());
			} else {
				layer.alert(result.msg);
			}
		},
		error: function () {
			layer.alert('操作失败');
		}
	});
}

// 新增步骤table
function formTable() {
    $.ajax({
        url: common.getPath() + "/formManage/queryFormListBySelective",
        type: "post",
        data: {
            proUid: proUid,
            proVersion: proVerUid,
            dynTitle: $('#dynTitle').val(),
            dynDescription: $('#dynDescription').val()
        },
        dataType: "json",
        success: function (result) {
            $('#form_tbody').empty();
            var trs = '';
            $(result.data)
                .each(
                    function (index) {
                        trs += '<tr>';
                        trs += '<td><input type="checkbox" name="dynUid_check" value="' +
                            this.dynUid +
                            '" lay-skin="primary">' +
                            (index + 1) + '</td>';
                        trs += '<td>' + this.dynTitle + '</td>'
                        if (this.dynDescription != null && this.dynDescription != "") {
                            trs += '<td>' + this.dynDescription;
                        } else {
                            trs += '<td>';
                        }
                        trs += '</td>'
                        trs += '</tr>';
                    });
            $("#form_tbody").append(trs);
        }
    });
}

// step table数据填充
function step_table(data) {
    $('#step_table').empty();
    var trs = '';
    $(data)
        .each(
            function (index, val) {
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
                    trs += '<td>' +
                        '<i class="layui-icon" title="上移" onclick="resortStep(\'' +
                        val.stepUid +
                        '\', \'reduce\');">&#xe619;</i>' +
                        '<i class="layui-icon" title="下移" onclick="resortStep(\'' +
                        val.stepUid +
                        '\', \'increase\');">&#xe61a;</i>' +
                        '<i class="layui-icon delete_btn" title="编辑" onclick=stepEdit("' +
                        value +
                        '") >&#xe642;</i><i class="layui-icon delete_btn" title="删除" onclick="deleteStep(\'' +
                        this.stepUid + '\');">&#xe640;</i>' +
                        '<i class="layui-icon" title="修改参数映射" onclick=triggerEdit(\'' +
                        this.stepObjectUid +
                        '\');>&#xe654;</i>'
                } else {
                    trs += '<td>' +
                        '<i class="layui-icon" title="上移" onclick="resortStep(\'' +
                        val.stepUid +
                        '\', \'reduce\');">&#xe619;</i>' +
                        '<i class="layui-icon" title="下移" onclick="resortStep(\'' +
                        val.stepUid +
                        '\', \'increase\');">&#xe61a;</i>' +
                        '<i class="layui-icon delete_btn" title="编辑" onclick=stepFormEdit("' +
                        value +
                        '") >&#xe642;</i><i class="layui-icon delete_btn" title="删除" onclick="deleteStep(\'' +
                        this.stepUid +
                        '\');">&#xe640;</i>' +
                        '<i class="layui-icon" title="编辑字段权限" onclick=formFieldEdit("' +
                        value + '"); >&#xe654;</i>'

                }
                trs += '</td>';
                trs += '</tr>';
            });
    $("#step_table").append(trs);
}

//根据参数查询字段权限信息
function formFieldEdit(data) {
    $(".display_container4").css("display", "block");
    // 查找该步骤绑定表单的所有字段及权限信息
    dates = jQuery.parseJSON(decodeURI(data));
    $.ajax({
        url: common.getPath() + "/formField/queryFieldByFormUidAndStepId",
        type: "post",
        dataType: "json",
        beforeSend: function () {
            layer.load(1);
        },
        data: {
            stepUid: dates.stepUid,
            formUid: dates.stepObjectUid,
            fieldType: ""
        },
        success: function (result) {
        	if(result.status==0){
        		drawPerTable(result, "field_permissions_table", "");
        	}
        },
        error: function () {
            layer.closeAll("loading");
        }
    });
    $.ajax({
        url: common.getPath() + "/formField/queryFieldByFormUidAndStepId",
        type: "post",
        dataType: "json",
        beforeSend: function () {
            layer.load(1);
        },
        data: {
            stepUid: dates.stepUid,
            formUid: dates.stepObjectUid,
            fieldType: "title"
        },
        success: function (result) {
        	if(result.status==0){
        		drawPerTable(result, "title_permissions_table", "title");
        	}
            layer.closeAll("loading");
        },
        error: function () {
            layer.closeAll("loading");
        }
    });
    // $(".form-horizontal").serialize();
}

