function getConductor(id, isSingle, actcCanChooseUser, actcAssignType) {
    console.log(actcCanChooseUser);
    if (actcCanChooseUser == 'FALSE') {
        layer.alert('没有配置可选处理人!');
        return false;
    }

    var url = 'sysUser/assign_personnel?id=' + id + '&isSingle=' + isSingle + '&actcCanChooseUser=' + actcCanChooseUser + '&actcAssignType=' + actcAssignType;
    layer.open({
        type: 2,
        title: '选择人员',
        shadeClose: true,
        shade: 0.8,
        area: ['680px', '520px'],
        content: [url, 'yes'],
        success: function (layero, lockIndex) {
            var body = layer.getChildFrame('body', lockIndex);
            //绑定解锁按钮的点击事件
            body.find('button#close').on('click', function () {
                layer.close(lockIndex);
                //location.reload();//刷新
            });
        }
    });
}

layui.use('layedit', function () {
    var layedit = layui.layedit;
    editIndex = layedit.build('demo', {
        height: 100,
        tool: ['strong' //加粗
            , 'italic' //斜体
            , 'underline' //下划线
            , 'del' //删除线

            , '|' //分割线

            , 'left' //左对齐
            , 'center' //居中对齐
            , 'right' //右对齐
        ]
    }); //建立编辑器

});

$(function () {
	// 驳回
	var actcCanReject = $("#actcCanReject").val();
	// 传阅
	var actcCanTransfer = $("#actcCanTransfer").val();
	// 加签
	var actcCanAdd = $("#actcCanAdd").val();
	if (actcCanReject == "FALSE") {
		$("#reject").css("display","none");
	}
	if (actcCanTransfer == "FALSE") {
		$("#transfer").css("display","none");
	}
	if (actcCanAdd == "FALSE") {
		$("#add").css("display","none");
	}
    getAllDataInfo();
    $(".add_row")
        .click(
            function () {
                var le = $(".create_table tbody tr").length + 1;
                $(".create_table")
                    .append(
                        $('<tr>' +
                            '<td>' +
                            le +
                            '</td>' +
                            '<td><input type="text" class="txt"/></td>' +
                            '<td><input type="text" class="txt"/></td>' +
                            '<td><input type="text" class="txt"/></td>' +
                            '<td><input type="text" class="txt"/></td>' +
                            '<td><i class="layui-icon delete_row">&#xe640;</i></td>' +
                            '</tr>'));
                $(".delete_row").click(function () {
                    $(this).parent().parent().remove();
                });
            });
    $(".delete_row").click(function () {
        $(this).parent().parent().remove();
    });
    $(".upload").click(function () {
        $(".upload_file").click();
    });
    // 加签
    $("#add").click(function() {
    	$("#handleUser_view").val("");
    	$(".display_container7").css("display","block");
    })
    // 选择处理人（人员）
    $("#choose_handle_user").click(function() {
    	common.chooseUser('handleUser', 'false');
    });
    $(".cancel_btn").click(function() {
    	$(".display_container7").css("display","none");
    })

    // 查询审批进度剩余进度百分比
    var proUid = $("#proUid").val();
    var proVerUid = $("#proVerUid").val();
    var proAppId = $("#proAppId").val();
    var taskUid = $("#taskUid").val();
    $.ajax({
        async: false,
        url: common.getPath() + "/taskInstance/queryProgressBar",
        type: "post",
        dataType: "json",
        data: {
            proUid: proUid,
            proVerUid: proVerUid,
            proAppId: proAppId,
            taskUid: taskUid
        },
        success: function (data) {
            var result = data.data;
            // 剩余时间
            var hour = result.hour;
            // 剩余时间百分比
            var percent = result.percent;
            if (data.status == 0) {
                if (result.hour == -1) {
                    $(".layui-progress").append('<span class="progress_time">审批已超时</span>');
                    $(".progress_time").css('right', '4%');
                } else {
                    $(".layui-progress").append('<span class="progress_time">审批剩余时间' + hour + '小时</span>');
                    if (percent == 0) {
                        $(".progress_time").css('right', '87%');
                    } else {
                        $(".progress_time").css('right', 90 - percent + '%');
                    }
                }
                // 加载进度条
                layui.use('element', function () {
                    var $ = layui.jquery,
                        element = layui.element; //Tab的切换功能，切换事件监听等，需要依赖element模块
                    // 延迟加载
                    setTimeout(function () {
                        if (percent > 50) {
                            $('.layui-progress-bar').css('background-color', 'yellow');
                        }
                        if (percent > 80) {
                            $('.layui-progress-bar').css('background-color', 'red');
                        }
                        element.progress('progressBar', percent + '%');
                    }, 500);
                });
            } else {
                $(".layui-progress").append('<span class="progress_time">加载失败!</span>');
            }
        }
    });
});

// 加签确定
function addSure(){
	var taskId = $("#taskId").val();
	var insUid = $("#insUid").val();
	var usrUid = $("#handleUser_view").val();
	var activityBpdId = $("#activityId").val();
	if (usrUid == null || usrUid == "") {
		layer.alert("请选择人员!");
		return;
	}
	$.ajax({
		async: false,
		url: common.getPath() + "/taskInstance/addSure",
		type: 'post',
		dataType: 'json',
		data: {
			insUid: insUid,
			taskId: taskId,
			activityBpdId: activityBpdId,
			usrUid: usrUid
		},
		success: function(data){
			if (data.status == 0) {
				layer.alert("操作成功!");
				$(".display_container7").css("display","none");
			}else {
				layer.alert("操作失败!")
			}
		}
	})
}

function getAllDataInfo() {
    // 拼装数据
	var inputArr = $("table input");
    var activityId = ""
    var userUid = ""
    var insData = $("#insData").text();
    $('.getUser').each(function () {
        activityId = $(this).attr('id');
        userUid = $(this).val();
    });
    var json = "{";
    for (var i = 0; i < inputArr.length; i++) {
        var type = $(inputArr[i]).attr("type");
        var textJson = "";
        var checkJson = "";
        switch (type) {
            case "text": {
                if ($(inputArr[i]).prop("class") == "layui-input layui-unselect") {
                    var name = $(inputArr[i]).parent()
                        .parent().prev().prop("name");
                    var value = $("[name='" + name + "']")
                        .val();
                    textJson = "\"" + name
                        + "\":{\"value\":\"" + value
                        + "\"}";
                    break;
                }
            }
                ;
            case "tel":
                ;
            case "date":
                ;
            case "textarae": {
                var name = $(inputArr[i]).attr("name");
                var value = $("[name='" + name + "']")
                    .val();
                textJson = "\"" + name + "\":{\"value\":\""
                    + value + "\"}";
                break;
            }
            case "radio": {
                var name = $(inputArr[i]).attr("name");
                var radio = $("[name='" + name + "']")
                    .parent().parent().find(
                        "input:radio:checked");
                textJson = "\"" + name + "\":{\"value\":\""
                    + radio.attr("id") + "\"}";
                break;
            }
            case "checkbox": {
                var name = $(inputArr[i]).attr("name");
                var checkbox = $("[name='" + name + "']")
                    .parent().parent().find(
                        "input:checkbox:checked");
                //判断每次的复选框是否为同一个class
                if (control) {
                    checkName = checkbox.attr("name");
                } else {
                    if (checkName != checkbox.attr("name")) {
                        checkName = checkbox.attr("name");
                        control = true;
                    }
                }
                if (control) {
                    control = false;
                    checkJson += "\"" + checkName
                        + "\":{\"value\":[";
                    for (var j = 0; j < checkbox.length; j++) {
                        if (j == checkbox.length - 1) {
                            checkJson += "\""
                                + $(checkbox[j]).attr(
                                    "id") + "\"";
                        } else {
                            checkJson += "\""
                                + $(checkbox[j]).attr(
                                    "id") + "\",";
                        }
                    }
                    checkJson += "]},";
                }

                json += checkJson;
                break;
            }
        }//end switch
        textJson += ",";
        if (json.indexOf(textJson) == -1) {
            json += textJson;
        }
    }
    //获得最后一位字符是否为","
    var charStr = json.substring(json.length - 1,
        json.length);

    if (charStr == ",") {
        json = json.substring(0, json.length - 1);
    }
    json += "}";
    // 发起流程             
    var finalData = {};
    // 表单数据
    var formData = JSON.parse(json);
    finalData.formData = formData;
    // 流程数据
    var processData = {};
    //processData.proAppId = $("#proAppId").val();
    //processData.proUid = $("#proUid").val(); 
    //processData.proVerUid = $("#verUid").val();
    processData.insUid = $("#insUid").val();
    // processData.departNo = departNo,
    // processData.companyNumber = companyNumber,
    finalData.processData = processData;

    var activityId = ""
    var userUid = ""
    var insData = $("#insData").text();
    // 路由数据
    var routeData = [];
    $('.getUser').each(function () {
        var item = {};
        item.activityId = $(this).attr('id');
        item.userUid = $(this).val();
        item.assignVarName = $(this).data("assignvarname");
        item.signCountVarName =  $(this).data("signcountvarname");
        item.loopType = $(this).data("looptype");
        routeData.push(item);
    });
    finalData.routeData = routeData;
}

function processView(insId) {
    $.ajax({
        url: 'processInstance/viewProcess',
        type: 'post',
        dataType: 'text',
        data: {
            insId: insId
        },
        success: function (result) {
            layer.open({
                type: 2,
                title: '流程图',
                shadeClose: true,
                shade: 0.8,
                area: ['790px', '580px'],
                content: result
            });
        }
    })
}

function agree() {
	var inputArr = $("table input");
    var taskId = $("#taskId").val();
    var taskUid = $("#taskUid").val();
    var activityId = ""
    var userUid = ""
    //var insData = $("#insData").text();
    $('.getUser').each(function () {
        activityId = $(this).attr('id');
        userUid = $(this).val();
    });

    var insUid = $("#insUid").val();//流程实例id--ins_uid
    var apr_taskUid = $("#activityId").val();//环节id，activity_id
    var aprOpiComment = $("#myApprovalOpinion").text();//审批意见
	var aprStatus = "ok";
	/*var approvaInfo ={
			"aprOpiComment":aprOpiComment,
			"aprStatus":aprStatus
			} */

    /*var json = "{";

    var formData = "formData"; // 表单数据外层
    var routeData = "routeData"; // 选人数据外层
    var approvalData = "approvalData"; // 审批数据外层
    var taskData = "taskData"; // 任务数据外层
    var endjson = "}";*/
    // 数据信息
   
    // 发起流程             
    var finalData = {};
    // 表单数据
    var jsonStr = common.getDesignFormData();
    var formData = JSON.parse(jsonStr);
    
    finalData.formData = formData;
    // 流程数据
    var processData = {};
    //processData.proAppId = $("#proAppId").val();
    //processData.proUid = $("#proUid").val(); 
    //processData.proVerUid = $("#verUid").val();
    processData.insUid = $("#insUid").val();
    // processData.departNo = departNo,
    // processData.companyNumber = companyNumber,
    finalData.processData = processData;

    var activityId = ""
    var userUid = ""
    var insData = $("#insData").text();
    // 路由数据
    var routeData = [];
    $('.getUser').each(function () {
        var item = {};
        item.activityId = $(this).attr('id');
        item.userUid = $(this).val();
        item.assignVarName = $(this).data("assignvarname");
        item.signCountVarName =  $(this).data("signcountvarname");
        item.loopType = $(this).data("looptype");
        routeData.push(item);
    });
    finalData.routeData = routeData;
    finalData.taskData = {"taskId":taskId,"taskUid":taskUid};
    finalData.approvalData = {"aprOpiComment":aprOpiComment,"aprStatus":aprStatus};
    $.ajax({
        url: 'taskInstance/finshedTask',
        type: 'POST',
        dataType: 'text',
        data: {
            data: JSON.stringify(finalData)
        },
        beforeSend: function () {
            index = layer.load(1);
        },
        success: function (result) {
            layer.close(index);
            if (result.status == 0) {
                layer.alert('提交成功', {
                    icon: 1
                });
            }else{
                layer.alert(result.msg, {
                    icon: 2
                });
            }
            $(".display_container").css("display","none"); 
        },
        error: function (result) {
            layer.close(index);
            layer.alert('提交失败', {
                icon: 2
            });
        }
    });
}

function reject() {
    var activityId = $("#activityId").val();
    var insId = $("#insId").val();
    alert(activityId+"~~~~"+insId)
}

function back() {
	window.location.href = 'javascript:history.go(-1)';
}

//数据信息
var view = $(".container-fluid");
var form = null;
$(function () {
    var insData = $("#insData").text();
    layui.use(['form'], function () {
        form = layui.form;
    });
    console.log(insData);
    var insDataFromDb = JSON.parse(insData);
    var formData = insDataFromDb.formData;
    var str = JSON.stringify(formData);
    getdata(str);
});

function getdata(jsonStr) {
    var json = JSON.parse(jsonStr);
    for (var name in json) {
        var paramObj = json[name];
        //给各个组件赋值
        setValue(paramObj, name);
        //判断组件是否可见
        isDisplay(paramObj, name);
        //判断组件对象是否可编辑
        isEdit(paramObj, name);
    }
}

/**
 * 根据组件对象的类型给各个组件赋值
 * @param paramObj 组件对象
 * @param id 各个组件的id(单选框为class)
 */
var setValue = function (paramObj, name) {
    var tagName = $("[name='" + name + "']").prop("tagName");
    switch (tagName) {
        case "INPUT":
            {
                var tagType = $("[name='" + name + "']").attr("type");
                switch (tagType) {
                    case "text":
                        ;
                    case "tel":
                        ;
                    case "date":
                        {
                            $("[name='" + name + "']").val(paramObj["value"]);
                            form.render();
                            break;
                        };
                    case "radio":
                        {
                            $("[name='" + name + "'][id='" + paramObj["value"] + "']").prop("checked", "true");
                            form.render();
                            break;
                        }
                    case "checkbox":
                        {
                            var valueArr = paramObj["value"];
                            for (var value in valueArr) {
                                $("[name='" + name + "'][id='" + valueArr[value] + "']").prop("checked", "true");
                            }
                            form.render();
                            break;
                        }
                }
                break;
            };
        case "SELECT":
            ;
        case "TEXTAREA":
            {
                $("[name='" + name + "']").val(paramObj["value"]);
                form.render();
                break;
            }
    }
}


/**
 * 判读组件对象是否可见
 * @param paramObj 组件对象
 * @param id 各个组件的id(单选框为class)
 */
var isDisplay = function (paramObj, name) {
    var display = paramObj["display"];
    if (display == "none") {
        var tagType = $("[name='" + name + "']").attr("type");
        $("[name='" + name + "']").parent().css("display", "none");
        $("[name='" + name + "']").parent().prev().css("display", "none");
        if (tagType == "radio" || tagType == "checkbox") {
            $("[name='" + name + "']").parent().css("display", "none");
            $("[name='" + name + "']").parent().prev().css("display", "none");
        }
    }
}

/**
 * 判读组件对象是否可编辑
 * @param paramObj 组件对象
 * @param id 各个组件的id(单选框为class)
 */
var isEdit = function (paramObj, name) {
    var edit = paramObj["edit"];
    if (edit == "no") {
        $("[name='" + name + "']").attr("readonly", "true");
        var tagName = $("[name='" + name + "']").prop("tagName");
        var tagType = $("[name='" + name + "']").attr("type");
        var className = $("[name='" + name + "']").attr("class");
        if (tagType == "radio" || tagType == "checkbox") {
            $("[name='" + name + "']").attr("disabled", "true");
        }
        if (tagName == "SELECT") {
            $("[name='" + name + "']").attr("disabled", "true");
        }
        if (className == "date") {
            $("[name='" + name + "']").attr("disabled", "true");
        }
    }
}
//提交流程-->选人
function checkUserData() {
    var departNo = $("#departNo").val();
    var companyNumber = $("#companyNum").val();
    var formData =common.getDesignFormData();
        
    if (departNo==null || departNo=="" || companyNum=="" || companyNum==null) {
    	layer.alert("缺少提交人信息");
    	return;
    }
    $.ajax({
    	url:"dhRoute/showRouteBar",
    	method:"post",
    	data:{
        	"insUid":$("#insUid").val(),
            "activityId":$("#activityId").val(),
        	"departNo":departNo,
        	"companyNum":companyNumber,
        	"formData":formData
    	},
        success:function(result){
        	if(result.status==0){
        		$(".choose_middle .layui-form").empty();
            	var activityMetaList = result.data;
            	var chooseUserDiv = "";
            	for(var i=0;i<activityMetaList.length;i++){
                	var activityMeta = activityMetaList[i];
                	chooseUserDiv += '<div class="choose_user_div">'
						+'<label class="form_label layui-col-md2">下一环节</label>'
						+'<div class="layui-col-md4">'
							+'<input type="text" name="title" readonly  value="'+activityMeta.activityName+'" class="layui-input" style="border-width:1px;"/>'
						+'</div>'
						+'<label class="form_label layui-col-md2">处理人</label>'
						+'<div class="layui-col-md4">'
							+'<input type="text" id="'+activityMeta.activityId+'_view" value="'+activityMeta.userName+'" name="addAgentPerson" class="layui-input" style="float:left;border-width:1px;" readonly>'
							+'<i class="layui-icon" onclick=getConductor("'+activityMeta.activityId
								+'","false","'+activityMeta.dhActivityConf.actcCanChooseUser+'","'
								+activityMeta.dhActivityConf.actcAssignType+'"); '
								+'style="position:relative;left:2%;font-size:30px;">&#xe612;</i>'
							+'<input type="hidden" class="getUser" id="'+activityMeta.activityId+'"  value="'+activityMeta.userUid+'" '
								+'data-assignvarname="'+activityMeta.dhActivityConf.actcAssignVariable+'" data-signcountvarname="'+activityMeta.dhActivityConf.signCountVarname +'"'
								+'data-looptype="'+activityMeta.loopType+'" />'
							+'<input type="hidden"  id="choosable_'+activityMeta.activityId+'"  value="'+activityMeta.userUid+'"  />'
						+'</div>'
						+'</div>';
                		$(".choose_middle .layui-form").append(chooseUserDiv);
                	}//end for
                }
        	}//end ajax
		});
		$(".display_container").css("display","block"); //end
}
