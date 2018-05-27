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

$(function () {
    clientSideInclude(document.getElementById('formId').value);
    saveDraftsData();
    saveData();
})
function clientSideInclude(){}

/**
 * 保存表单数据的方法
 */
var index2 = null;
var saveDraftsData = function () {
    $("#saveInfoBtn")
        .click(
            function (e) {
                e.preventDefault();
                var inputArr = $("table input");
                var selectArr = $("table select");
                var control = true; //用于控制复选框出现重复值
                var checkName = ""; //用于获得复选框的class值，分辨多个复选框
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
                document.getElementById('dataInfo').value = json;
                // 保存草稿数据                   
                var proUids = $("#proUid").val();
                var proAppIds = $("#proAppId").val();
                var verUids = $("#verUid").val();
                var proName = $("#proName").val();
                var userId = $("#userId").val();
                var dataInfo = $("#dataInfo").val();
                $.ajax({
                    url: "drafts/saveDrafts",
                    method: "post",
                    async: false,
                    data: {
                        dfsTitle: proName,
                        dfsData: dataInfo,
                        dfsCreator: userId,
                        proUid: proUids,
                        proVerUid: verUids,
                        proAppId: proAppIds
                    },
                    beforeSend: function () {
                        index2 = layer.load(1);
                    },
                    success: function (result) {
                        layer.close(index2);
                        layer.alert('保存成功')
                    }
                });
            });
    //end
}

/**
 * 发起流程
 */
var index = null; // 加载
var saveData = function () {
    $("#startProcess")
        .click(
            function (e) {
                e.preventDefault();
                var inputArr = $("table input");
                var selectArr = $("table select");
                var creatorInfo = $("#creatorInfo").val()
                if (!creatorInfo) {
                	layer.alert("缺少发起人信息");
                	return;
                }
                var departNo = creatorInfo.split(",")[0];
                var companyNumber = creatorInfo.split(",")[1];
                var control = true; //用于控制复选框出现重复值
                var checkName = ""; //用于获得复选框的class值，分辨多个复选框
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
                //获取审批人
                var user = $(".getUser").val().substring(0, 8);

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
                processData.departNo = departNo,
                processData.companyNumber = companyNumber,
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
                console.log(finalData);
                var startjson = "{";
                var formData = "formData"; // 表单数据外层
                var routeData = "routeData"; // 选人数据外层
                var approvaData = "approvaData"; // 审批数据外层
                var processData = "processData"; // 任务数据外层
                var endjson = "}";
                
                $.ajax({
                    url: 'processInstance/startProcess',
                    type: 'POST',
                    dataType: 'json',
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
                        }
                        if (result.status == 1) {
                            layer.alert('提交失败', {
                                icon: 2
                            });
                        }
                    },
                    error: function (result) {
                        layer.close(index);
                        layer.alert('提交失败', {
                            icon: 2
                        });
                    }
                });
            });
    //end
}

// 回退到上一页面
function back() {
    var proUid = $("#proUid").val();
    var proAppId = $("#proAppId").val();
    var verUid = $("#verUid").val();
    window.location.href = 'menus/processType?proUid=' + proUid
        + '&proAppId=' + proAppId + '&verUid=' + verUid;
}

// 查看流程图
function viewProcess() {

}