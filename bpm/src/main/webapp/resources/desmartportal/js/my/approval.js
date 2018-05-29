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

function getAllDataInfo() {
    // 拼装数据
    var activityId = ""
    var userUid = ""
    var insData = $("#insData").text();
    $('.getUser').each(function () {
        activityId = $(this).attr('id');
        userUid = $(this).val();
    });
    var json = "{";
    var formData = "formData"; // 表单数据外层
    var routeData = "routeData"; // 选人数据外层
    var approvaData = "approvaData"; // 审批数据外层
    var endjson = "}";
    // 
    var jsonStr = "" + json + "\"" + formData + "\":" + insData + ",\"" + approvaData + "\":{\"a\":{\"A\":\"C\"}},\"" + routeData + "\":{\"activityId\":\"" + activityId + "\"}" + endjson + "";
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
    var taskId = $("#taskId").val();
    var activityId = ""
    var userUid = ""
    var insData = $("#insData").text();
    $('.getUser').each(function () {
        activityId = $(this).attr('id');
        userUid = $(this).val();
    });
    var json = "{";
    var formData = "formData"; // 表单数据外层
    var routeData = "routeData"; // 选人数据外层
    var approvaData = "approvaData"; // 审批数据外层
    var taskData = "taskData"; // 任务数据外层
    var endjson = "}";
    // 数据信息
    var jsonStr = "" + json + "\"" + formData + "\":" + insData + ",\"" + approvaData + "\":{\"a\":{\"A\":\"C\"}},\"" + routeData + "\":{\"activityId\":\"" + activityId + "\",\"userUid\":\"" + userUid + "\"},\"" + taskData + "\":{\"taskId\":\"" + taskId + "\"}" + endjson + "";
    $.ajax({
        url: 'taskInstance/finshedTask',
        type: 'POST',
        dataType: 'text',
        data: {
            data: jsonStr
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
}

function reject() {
    var activityId = $("#activityId").val();
    var insId = $("#insId").val();
    alert(activityId+"~~~~"+insId)
}

function back() {
    window.location.href = 'menus/backlog';
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