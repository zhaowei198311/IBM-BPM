var triggerToEdit = "";//查询相关触发器时存储在页面中的元素id
var preFormData; //用于装载环节配置的初始数据，判断用户切换环节时数据是否更改
var stepUidToEdit;//记录编辑步骤时的步骤id
var editIndex;//记录环节审批要求的编辑框的index
var saveFrom; // 触发保存的元素 saveBtn activityLi gatewayLi stepLi

var dates = "";// 查询权限信息时，存储当前步骤和表单id
//触发器的分页参数
var pageConfig = {
	pageNum : 1,
	pageSize : 5,
	total : 0,
	triTitle : "",
	triType : ""
};

// 对于系统任务相关的配置
var systemTask = {
    initListener : function (form) { // 元素的值变化时的操作 form 是layui.form
        form.on('radio(isSystemTask)', function(data) {
            if (data.value == "TRUE") {
                $('div[attr=delay-detail-type]').show();
                if ($('select[name=actcDelayType]').val() == 'time') {
                    $('div[attr=delay-detail-time]').show();
                    $('div[attr=delay-detail-field]').hide();
				} else if ($('select[name=actcDelayType]').val() == 'field') {
                    $('div[attr=delay-detail-field]').show();
                    $('div[attr=delay-detail-time]').hide();
				}
            } else {
                $('div[attr^=delay-detail-]').hide();
            }
        });
        form.on('select(delayType)', function (data) {
            if (data.value == "none") {
                $('div[attr=delay-detail-time]').hide();
                $('div[attr=delay-detail-field]').hide();
			} else if(data.value == "time") {
                $('div[attr=delay-detail-time]').show();
                $('div[attr=delay-detail-field]').hide();
			} else {
                $('div[attr=delay-detail-field]').show();
                $('div[attr=delay-detail-time]').hide();
			}
        });
    },
    initValue: function (conf) {
        $('div[attr^=delay-detail-]').hide();
        // 初始化input-text
        $('input[name="actcDelayTime"]').val(conf.actcDelayTime);
        $('input[name="actcDelayField"]').val(conf.actcDelayField);
        // 初始化select
        $('select[name="actcDelayType"]').val(conf.actcDelayType);
        $('select[name="actcDelayTimeunit"]').val(conf.actcDelayTimeunit);
        // 初始化radio
        $('input[name="actcIsSystemTask"]').each(function() {
            $(this).prop("checked", $(this).val() == conf.actcIsSystemTask ? true : false);
        });
        // 控制显示隐藏
        if (conf.actcIsSystemTask == 'TRUE') {
            $('div[attr=delay-detail-type]').show();
            if (conf.actcDelayType == 'time') {
                $('div[attr=delay-detail-time]').show();
            } else if (conf.actcDelayType == 'field') {
                $('div[attr=delay-detail-field]').show();
            }
        }
    }
}

//layui各个使用的模块初始化
var laypage = null;
var layer = null;
var form = null;
var element = null;
layui.use(['laypage', 'layer', 'form', 'element'], function () {
        laypage = layui.laypage,
        layer = layui.layer,
        form = layui.form;
        element = layui.element;
});
//默认处理人的类型
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
//可选处理人的类型
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
    } else if (data.value == 'byTrigger') {
        $("#chooseableHandleRole_div").hide();
        $("#chooseableHandleTeam_div").hide();
        $("#chooseableHandleUser_div").hide();
        $("#chooseableHandleField_div").hide();
        $("#chooseableHandleTrigger_div").show();
    }
});
//超时通知人员的类型
form.on('select(outtimeNotifyType)',function(data){
    if(data.value == 'users'){
        $("#outtimeUser_div").show();
    }else{
        $("#outtimeUser_div").hide();
    }
});
//内部通知人的类型
form.on('select(interiorNotifyType)',function(data){
    if(data.value == 'role'||data.value == 'roleAndCompany'){
        $("#interiorNotifyRole_div").show();
        $("#interiorNotifyUser_div").hide();
    }else if(data.value == 'users'){
        $("#interiorNotifyRole_div").hide();
        $("#interiorNotifyUser_div").show();
    }else{
        $("#interiorNotifyRole_div").hide();
        $("#interiorNotifyUser_div").hide();
    }
})
//驳回的类型
form.on('select(rejectType)', function(data) {
    if (data.value == "toActivities") {
        $("#rejectActivities_div").show();
    } else {
        $("#rejectActivities_div").hide();
    }
});
//是否可驳回
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
//修改步骤时的关键字
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
//对于系统任务相关的配置
systemTask.initListener(form);

$(function() {
    //初始化邮箱地址输入框
    $('#exteriorNotifyMail').manifest();
    
    $(".cancel_btn").click(function() {
        $(".display_container4").css("display", "none");
        $(".display_container10").css("display", "none");
        $(".display_container5").css("display", "none");
        $(".display_container8").css("display", "none");
    })

    //流程图
    $("#snapshotFlowChart_btn").click(function() {
        window.parent.openProView(proUid,proVerUid,proAppId);
    });

    //返回按钮
    $("#back_btn").click(function () {
		window.history.back();
	});
    
    $("#sure_btn").click(function() {
        $(".display_container3").css("display", "none");
        $(".display_container5").css("display", "none");
        $(".display_container6").css("display", "none");
        $(".display_container10").css("display", "none");
    })
    $("#cancel_btn").click(function() {
        $(".display_container3").css("display", "none");
        $(".display_container4").css("display", "none");
        $(".display_container8").css("display", "none");
        $(".display_container5").css("display", "none");
        $(".display_container6").css("display", "none");
        $(".display_container7").css("display", "none");
        $(".display_container10").css("display", "none");
    })
})

