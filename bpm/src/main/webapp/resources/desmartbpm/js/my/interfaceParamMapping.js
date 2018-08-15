//选择表单进行参数映射
form.on('select(table_sel)', function (data) {
    $("#col_md5").find(".layui-form-item").find(".layui-row").find("select").empty();
    $("#col_md6").find(".layui-form-item").find(".layui-row").find("select").empty();
    $(".list_mod").find(".layui-form-item").find(".layui-row").find(".listParam").empty();
    $(".list_mod").find(".layui-form-item").find(".layui-row").find(".listfiled").empty();
    var data = data.value; // 表单id
    // 截取字符串
    var formId = data.substring(0, data.indexOf('|')); // 表单ID
    var intUid = data.substring(data.indexOf('i')); // 参数ID
    var startNum = 1;
    $("#formUid").val(formId);
    $.ajax({
        url: common.getPath() + '/formField/queryFieldByFromUid',
        type: 'post',
        dataType: 'json',
        data: {
            formUid: formId
        },
        success: function (result) {
            for (var i = 0; result.status == 0 && i < result.data.length; i++) {
                var fldIndex = result.data[i].fldIndex; // 字段索引下标
                var fldCodeName = result.data[i].fldCodeName; // 字段名
                var indexs = startNum + i
                // 获取 接口参数的数据
                var trs = '<option value="' + result.data[i].fldCodeName + '">' +
                    result.data[i].fldCodeName +"("+result.data[i].fldName +")"
                    '</option>';
                var dataTableList = $("#col_md5").find(".layui-form-item").find(".layui-row");
                for (var j = 0; j < dataTableList.length; j++) {
                    var inputArr = $(dataTableList[j]).find("select");
                    //$(inputArr).empty();
                    $(inputArr).append(trs);
                }
                var dataTableList = $("#col_md6").find(".layui-form-item").find(".layui-row");
                for (var j = 0; j < dataTableList.length; j++) {
                    var inputArr = $(dataTableList[j]).find("select");
                    //$(inputArr).empty();
                    $(inputArr).append(trs);
                }
            }
            form.render();
        },
        error: function (result) {
            layer.alert('查询表单字段出错')
        }
    })
    // 查询 表单字段集合对象名称
    $.ajax({
        url: common.getPath() + '/formField/queryFormTabByFormUid',
        type: 'post',
        dataType: 'json',
        data: {
            formUid: formId
        },
        success: function (result) {
            if (result.status == 0) {
                for (var i = 0; i < result.data.length; i++) {
                    var trs = '<option value="' + result.data[i].fldCodeName + '">' +
                        result.data[i].fldCodeName +
                        '</option>';
                    var dataTableList = $(".list_mod").find(".layui-form-item").find(".layui-row");
                    for (var j = 0; j < dataTableList.length; j++) {
                        var inputArr = $(dataTableList[j]).find(".listParam");
                        //$(inputArr).empty();
                        $(inputArr).append(trs);
                    }
                }
            }
        },
        error: function (result) {
            layer.alert('查询表单字段集合对象出错')
        }
    })
    form.on('select(listParam_sel)', function (data) {
        $(".list_mod").find(".layui-form-item").find(".layui-row").find(".listfiled").empty();
        // 查询 表单字段集合对象 下 的 所有 字段信息
        var tableName = $("select[name=listParam_sel]").val();
        $.ajax({
            url: common.getPath() + '/formField/queryFormTabFieldByFormUidAndTabName',
            type: 'post',
            dataType: 'json',
            data: {
                formUid: $("#formUid").val(),
                tableName: tableName
            },
            success: function (result) {
                console.info(result)
                for (var i = 0; i < result.data.length; i++) {
                    var trs = '<option value="' + result.data[i].fldCodeName + '">' +
                        result.data[i].fldCodeName +
                        '</option>';
                    var dataTableList = $(".list_mod").find(".layui-form-item").find(".layui-row");
                    for (var j = 0; j < dataTableList.length; j++) {
                        var inputArr = $(dataTableList[j]).find(".listfiled");
                        //$(inputArr).empty();
                        $(inputArr).append(trs);
                    }
                }
                form.render();
            },
            error: function (result) {
                layer.alert('查询表单字段集合属性出错')
            }
        })
    })

    function queryOption(o) {
        alert("1")
        var options = document.getElementsByTagName("option");
        for (var i = 0; i < options.length; i++) {
            op = o.options[o.selectedIndex]
            if (!options[i].selected) continue;
            if (op != options[i] && options[i].value == o.value) {
                alert("选择值重复");
                o.value = "";
                return;
            }
        }
    }

    $("#paramMapping_sureBtn").click(function () {
        var triUid = "";
        $('input[name="tri_check"]:checked').each(function () {
            triUid = $(this).val();
        });
        var $activeLi = $("#my_collapse li.link_active");
        var actcUid = $activeLi.data('uid');
        var data = $("select[name=table_sel]").val();
        var formId = data.substring(0, data.indexOf('|')); // 表单ID
        var intUid = data.substring(data.indexOf('i')); // 参数ID
        var arr = new Array();
        // 判断是 输入还是输出 保存
        if ($("#paramterType").val() == 'outputParameter') {
            // 输出
            var dataList = $("#col_md6").find(".layui-form-item").find(".layui-row");
            for (var i = 0; i < dataList.length; i++) {
                var paraUid = $(dataList[i]).find(".paraUid").val();
                var paraName = $(dataList[i]).find(".paraName").val();
                var options = $(dataList[i]).find("option:selected").val();
                if ($(dataList[i]).find("option:selected").val() == null) {
                    layer.alert('表单参数不能为空')
                    return;
                } else {
                    var selects = $("#col_md6").find("select").not($(dataList[i]).find("select"));
                    for (var j = 0; j < selects.length; j++) {
                        if ($(dataList[i]).find("option:selected").val() == selects[j].value) {
                            layer.alert('参数不能相同')
                            return;
                        }
                    }
                    var info = {
                        triUid: triUid,
                        intUid: $("#triContent").val(),
                        dynUid: formId,
                        activityId: $("#activityId").val(),
                        paraName: paraName,
                        parameterType: $("#paramterType").val(),
                        paraUid: paraUid,
                        fldCodeName: $(dataList[i]).find("option:selected").val()
                    };
                    arr.push(info);
                }
            }
            var outputList = $(".list_mod").find(".layui-form-item").find(".layui-row");
            for (var i = 0; i < outputList.length; i++) {
                var paraUid = $(outputList[i]).find(".paraUid").val();
                var paraName = $(outputList[i]).find(".paraName").val();
                var options = $(outputList[i]).find("option:selected").val();
                if ($(outputList[i]).find("option:selected").val() == null) {
                    layer.alert('集合参数不能为空')
                    return;
                } else {
                    var selects = $(".list_mod").find("select").not($(outputList[i]).find("select"));
                    for (var j = 0; j < selects.length; j++) {
                        if ($(outputList[i]).find("option:selected").val() == selects[j].value) {
                            layer.alert('集合参数不能相同')
                            return;
                        }
                    }
                    var info = {
                        triUid: triUid,
                        intUid: $("#triContent").val(),
                        dynUid: formId,
                        activityId: $("#activityId").val(),
                        paraName: paraName,
                        parameterType: $("#paramterType").val(),
                        paraUid: paraUid,
                        fldCodeName: $(outputList[i]).find("option:selected").val()
                    };
                    arr.push(info);
                }
            }
            console.info(arr)
            $.ajax({
                url: common.getPath() + '/dhTriggerInterface/insertBatch',
                type: 'post',
                dataType: 'json',
                contentType: "application/json",
                data: JSON.stringify(arr),
                success: function (result) {
                    layer.alert('参数映射成功')
                    $("#triggerInterface_container").css("display", "none");
                },
                error: function (result) {
                    layer.alert('参数映射出错')
                }
            })
        } else {
            // 输入
            var dataList = $("#col_md5").find(".layui-form-item").find(".layui-row");
            for (var i = 0; i < dataList.length; i++) {
                var paraUid = $(dataList[i]).find(".paraUid").val();
                var paraName = $(dataList[i]).find(".paraName").val();
                var options = $(dataList[i]).find("option:selected").val();
                if ($(dataList[i]).find("option:selected").val() == null) {
                    layer.alert('表单参数不能为空')
                    return;
                } else {
                    var info = {
                        triUid: triUid,
                        intUid: $("#triContent").val(),
                        dynUid: formId,
                        activityId: $("#activityId").val(),
                        paraName: paraName,
                        parameterType: $("#paramterType").val(),
                        paraUid: paraUid,
                        fldCodeName: $(dataList[i]).find("option:selected").val()
                    };
                    arr.push(info);
                }
            }
            var inputList = $(".list_mod").find(".layui-form-item").find(".layui-row");
            for (var i = 0; i < inputList.length; i++) {
                var paraUid = $(inputList[i]).find(".paraUid").val();
                var paraName = $(inputList[i]).find(".paraName").val();
                var options = $(inputList[i]).find("option:selected").val();
                if ($(inputList[i]).find("option:selected").val() == null) {
                    layer.alert('集合参数不能为空')
                    return;
                } else {
                    var info = {
                        triUid: triUid,
                        intUid: $("#triContent").val(),
                        dynUid: formId,
                        activityId: $("#activityId").val(),
                        paraName: paraName,
                        parameterType: $("#paramterType").val(),
                        paraUid: paraUid,
                        fldCodeName: $(inputList[i]).find("option:selected").val()
                    };
                    arr.push(info);
                }
            }
            console.info(arr)
            $.ajax({
                url: common.getPath() + '/dhTriggerInterface/insertBatch',
                type: 'post',
                dataType: 'json',
                contentType: "application/json",
                data: JSON.stringify(arr),
                success: function (result) {
                    layer.alert('参数映射成功')
                    //$("#triggerInterface_container").css("display","none");
                    //loadActivityConf(actcUid);
                },
                error: function (result) {
                    layer.alert('参数映射出错')
                }
            })
        }
    });
});

//参数映射字段一些事件监听
element.on('tab(addParamter)', function (data) {
    var stepType = "form";
    var index = data.index; // 得到当前Tab的所在下标
    var sortNum = 1;
    $("#table_sel").empty();
    $("#col_md5").empty();
    $("#col_md6").empty();
    $(".list_mod").empty();
    if (index == 1) {
        // 输出参数
        $("#paramterType").val("outputParameter");
        $.ajax({
            url: common.getPath() + '/step/selectByStep',
            type: 'post',
            dataType: 'json',
            data: {
                proAppId: proAppId,
                proUid: proUid,
                proVerUid: proVerUid,
                stepType: stepType
            },
            success: function (result) {
                var chose = '<option value="">请选择表单</option>';
                $("#table_sel").append(chose)
                for (var i = 0; i < result.data.length; i++) {
                    var trs = '<option value="' +
                        result.data[i].stepObjectUid +
                        '|' +
                        $("#triContent").val() +
                        '">' + result.data[i].formName +
                        '</option>';
                    $("#table_sel").append(trs)
                }
                $("#table_sel option").each(function () {
                    text = $(this).text();
                    if ($("#table_sel option:contains(" + text + ")").length > 1)
                        $("#table_sel option:contains(" + text + "):gt(0)").remove();
                });
                form.render();
            },
            error: function (result) {
                layer.alert('查询环节出错')
            }
        });
        $("#table_sel option").each(function () {
            text = $(this).text();
            if ($("#table_sel option:contains(" + text + ")").length > 1)
                $("#table_sel option:contains(" + text + "):gt(0)").remove();
        });
        //	document.getElmentById("paramterType").value="outputParameter";
        var dataTableList = $("#col_md6").find(".layui-form-item").find(".layui-row");
        for (var i = 0; i < dataTableList.length; i++) {
            var jNum = sortNum + i
            $(".interfacelabel" + jNum).text("输出接口参数" + jNum);
            $(".tablelabel" + jNum).text("输出表单参数" + jNum);
        }
        $.ajax({
            url: common.getPath() + '/interfaceParamers/byQueryParameter',
            type: 'post',
            dataType: 'json',
            data: {
                intUid: $("#triContent").val(),
                paraInOut: 'output'
            },
            success: function (result2) {
                var list = result2;
                console.info(list)
                for (var i = 0; i < list.length; i++) {
                    var paraIndex = list[i].paraIndex; // 接口索引下标
                    var paraName = list[i].paraName; // 接口名
                    var paraUid = list[i].paraUid // 接口参数id
                    var paraType = list[i].paraType // 接口参数类型
                    var paraParent = list[i].paraParent // 父参数
                    var paraDescription = list[i].paraDescription // 接口参数描述
                    var index = sortNum + i
                    // 判断有没有list 集合数据 进行映射
                    if (list[i].paraType != "Array" && list[i].paraParent == null) {
                        var trs = '<div class="layui-form-item">' +
                            '<div class="layui-row">' +
                            '<div class="layui-col-md6">' +
                            '<div class="layui-inline">' +
                            '<label class="layui-form-label interfacelabel' + index + '" style="width: 100px">' + paraDescription + '</label>' +
                            '<div class="layui-input-inline">' +
                            '<input class="paraUid" value="' + paraUid + '" style="display: none;"/>' +
                            '<input id="interfaceParam' + index + '" disabled="disabled" readonly="readonly" type="text" name="title" lay-verify="title" autocomplete="off" class="layui-input paraName" value="' + paraName + '">' +
                            '</div>' +
                            '</div>' +
                            '</div>' +
                            '<div class="layui-col-md6">' +
                            '<label class="layui-form-label tablelabel' + index + '" style="width: 100px">表单参数' + index + '</label>' +
                            '<div class="layui-input-inline">' +
                            '<select id="tableParam' + index + '" lay-search onchange="queryOption(this)">' +
                            '</select>' +
                            '</div>' +
                            '</div>' +
                            '</div>' +
                            '</div>';
                        $("#col_md6").append(trs)
                    } else if (list[i].paraType == "Array") {
                        $(".col_list").css("display", "block");
                        var listTrs = '<div class="layui-form-item">' +
                            '<div class="layui-row">' +
                            '<div class="layui-col-md6">' +
                            '<div class="layui-inline">' +
                            '<label class="layui-form-label interfacelabel' + index + '" style="width: 100px">' + paraDescription + '</label>' +
                            '<div class="layui-input-inline">' +
                            '<input class="paraUid" value="' + paraUid + '" style="display: none;"/>' +
                            '<input id="interfaceParam' + index + '" disabled="disabled" readonly="readonly" type="text" name="title" lay-verify="title" autocomplete="off" class="layui-input paraName" value="' + paraName + '">' +
                            '</div>' +
                            '</div>' +
                            '</div>' +
                            '<div class="layui-col-md6">' +
                            '<label class="layui-form-label tablelabel' + index + '" style="width: 100px">集合对象' + index + '</label>' +
                            '<div class="layui-input-inline">' +
                            '<select id="tableParam' + index + '" name="listParam_sel" lay-filter="listParam_sel" class="listParam" lay-search onchange="queryOption(this)">' +
                            '</select>' +
                            '</div>' +
                            '</div>' +
                            '</div>' +
                            '</div>';
                        $(".list_mod").append(listTrs)
                    } else if (list[i].paraParent != null) {
                        $(".col_list").css("display", "block");
                        var listTrs = '<div class="layui-form-item">' +
                            '<div class="layui-row">' +
                            '<div class="layui-col-md6">' +
                            '<div class="layui-inline">' +
                            '<label class="layui-form-label interfacelabel' + index + '" style="width: 100px">' + paraDescription + '</label>' +
                            '<div class="layui-input-inline">' +
                            '<input class="paraUid" value="' + paraUid + '" style="display: none;"/>' +
                            '<input id="interfaceParam' + index + '" disabled="disabled" readonly="readonly" type="text" name="title" lay-verify="title" autocomplete="off" class="layui-input paraName" value="' + paraName + '">' +
                            '</div>' +
                            '</div>' +
                            '</div>' +
                            '<div class="layui-col-md6">' +
                            '<label class="layui-form-label tablelabel' + index + '" style="width: 100px">集合参数' + index + '</label>' +
                            '<div class="layui-input-inline">' +
                            '<select id="tableParam' + index + '" name="listfiled_sel"  lay-filter="listfiled_sel" class="listfiled" lay-search onchange="queryOption(this)">' +
                            '</select>' +
                            '</div>' +
                            '</div>' +
                            '</div>' +
                            '</div>';
                        $(".list_mod").append(listTrs)
                    }
                }
                form.render();
            },
            error: function (result2) {
                layer.alert("查询接口参数出错")
            }
        })
        form.render();
    } else {
        // 输入
        $("#paramterType").val("inputParameter");
        $.ajax({
            url: common.getPath() + '/step/selectByStep',
            type: 'post',
            dataType: 'json',
            data: {
                proAppId: proAppId,
                proUid: proUid,
                proVerUid: proVerUid,
                stepType: stepType
            },
            success: function (result) {
                var chose = '<option value="">请选择表单</option>';
                $("#table_sel").append(chose)
                for (var i = 0; i < result.data.length; i++) {
                    var trs = '<option value="' +
                        result.data[i].stepObjectUid +
                        '|' +
                        $("#triContent").val() +
                        '">' + result.data[i].formName +
                        '</option>';
                    $("#table_sel").append(trs)
                }
                $("#table_sel option").each(function () {
                    text = $(this).text();
                    if ($("#table_sel option:contains(" + text + ")").length > 1)
                        $("#table_sel option:contains(" + text + "):gt(0)").remove();
                });
                form.render();
            },
            error: function (result) {
                layer.alert('查询环节出错')
            }
        });
        //	document.getElmentById("paramterType").value="outputParameter";
        var dataTableList = $("#col_md5").find(".layui-form-item").find(".layui-row");
        for (var i = 0; i < dataTableList.length; i++) {
            var jNum = sortNum + i
            $(".interfacelabel" + jNum).text("输出接口参数" + jNum);
            $(".tablelabel" + jNum).text("输出表单参数" + jNum);
        }
        $.ajax({
            url: common.getPath() + '/interfaceParamers/byQueryParameter',
            type: 'post',
            dataType: 'json',
            data: {
                intUid: $("#triContent").val(),
                paraInOut: 'input'
            },
            success: function (result2) {
                var list = result2;
                console.info(list)
                for (var i = 0; i < list.length; i++) {
                    var paraIndex = list[i].paraIndex; // 接口索引下标
                    var paraName = list[i].paraName; // 接口名
                    var paraUid = list[i].paraUid // 接口参数id
                    var paraType = list[i].paraType // 接口参数类型
                    var paraParent = list[i].paraParent // 父参数
                    var paraDescription = list[i].paraDescription // 接口参数描述
                    var index = sortNum + i
                    // 判断有没有list 集合数据 进行映射
                    if (list[i].paraType != "Array" && list[i].paraParent == null) {
                        var trs = '<div class="layui-form-item">' +
                            '<div class="layui-row">' +
                            '<div class="layui-col-md6">' +
                            '<div class="layui-inline">' +
                            '<label class="layui-form-label interfacelabel' + index + '" style="width: 100px">' + paraDescription + '</label>' +
                            '<div class="layui-input-inline">' +
                            '<input class="paraUid" value="' + paraUid + '" style="display: none;"/>' +
                            '<input id="interfaceParam' + index + '" disabled="disabled" readonly="readonly" type="text" name="title" lay-verify="title" autocomplete="off" class="layui-input paraName" value="' + paraName + '">' +
                            '</div>' +
                            '</div>' +
                            '</div>' +
                            '<div class="layui-col-md6">' +
                            '<label class="layui-form-label tablelabel' + index + '" style="width: 100px">表单参数' + index + '</label>' +
                            '<div class="layui-input-inline">' +
                            '<select id="tableParam' + index + '" lay-search onchange="queryOption(this)">' +
                            '</select>' +
                            '</div>' +
                            '</div>' +
                            '</div>' +
                            '</div>';
                        $("#col_md5").append(trs)
                    } else if (list[i].paraType == "Array") {
                        $(".col_list").css("display", "block");
                        var listTrs = '<div class="layui-form-item">' +
                            '<div class="layui-row">' +
                            '<div class="layui-col-md6">' +
                            '<div class="layui-inline">' +
                            '<label class="layui-form-label interfacelabel' + index + '" style="width: 100px">' + paraDescription + '</label>' +
                            '<div class="layui-input-inline">' +
                            '<input class="paraUid" value="' + paraUid + '" style="display: none;"/>' +
                            '<input id="interfaceParam' + index + '" disabled="disabled" readonly="readonly" type="text" name="title" lay-verify="title" autocomplete="off" class="layui-input paraName" value="' + paraName + '">' +
                            '</div>' +
                            '</div>' +
                            '</div>' +
                            '<div class="layui-col-md6">' +
                            '<label class="layui-form-label tablelabel' + index + '" style="width: 100px">集合对象' + index + '</label>' +
                            '<div class="layui-input-inline">' +
                            '<select id="tableParam' + index + '" name="listParam_sel" lay-filter="listParam_sel" class="listParam" lay-search onchange="queryOption(this)">' +
                            '</select>' +
                            '</div>' +
                            '</div>' +
                            '</div>' +
                            '</div>';
                        $(".list_mod").append(listTrs)
                    } else if (list[i].paraParent != null) {
                        $(".col_list").css("display", "block");
                        var listTrs = '<div class="layui-form-item">' +
                            '<div class="layui-row">' +
                            '<div class="layui-col-md6">' +
                            '<div class="layui-inline">' +
                            '<label class="layui-form-label interfacelabel' + index + '" style="width: 100px">' + paraDescription + '</label>' +
                            '<div class="layui-input-inline">' +
                            '<input class="paraUid" value="' + paraUid + '" style="display: none;"/>' +
                            '<input id="interfaceParam' + index + '" disabled="disabled" readonly="readonly" type="text" name="title" lay-verify="title" autocomplete="off" class="layui-input paraName" value="' + paraName + '">' +
                            '</div>' +
                            '</div>' +
                            '</div>' +
                            '<div class="layui-col-md6">' +
                            '<label class="layui-form-label tablelabel' + index + '" style="width: 100px">集合参数' + index + '</label>' +
                            '<div class="layui-input-inline">' +
                            '<select id="tableParam' + index + '" name="listfiled_sel"  lay-filter="listfiled_sel" class="listfiled" lay-search onchange="queryOption(this)">' +
                            '</select>' +
                            '</div>' +
                            '</div>' +
                            '</div>' +
                            '</div>';
                        $(".list_mod").append(listTrs)
                    }
                }
                form.render();
            },
            error: function (result2) {
                layer.alert("查询接口参数出错")
            }
        })
        form.render();
    }
});

// “确认”选择触发器
function sureChooseTrigger() {
    $("#table_sel").empty();
    $("#col_md5").empty();
    $(".list_mod").empty();
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
    $("#triContent").val(triContent);
    var sortNum = 1;
    // 打开接口参数和form表单映射表单
    var stepType = "form";
    if (triType == "interface") {
        // 判断有没有表单
        $("#triggerInterface_container").show();
        $.ajax({
            url: common.getPath() + '/step/selectByStep',
            type: 'post',
            dataType: 'json',
            data: {
                proAppId: proAppId,
                proUid: proUid,
                proVerUid: proVerUid,
                stepType: stepType
            },
            success: function (result) {
                var chose = '<option value="">请选择表单</option>';
                $("#table_sel").append(chose)
                for (var i = 0; i < result.data.length; i++) {
                    var trs = '<option value="' +
                        result.data[i].stepObjectUid +
                        '|' +
                        triContent +
                        '">' + result.data[i].formName +
                        '</option>';
                    $("#table_sel").append(trs)
                }
                $("#table_sel option").each(function () {
                    text = $(this).text();
                    if ($("#table_sel option:contains(" + text + ")").length > 1)
                        $("#table_sel option:contains(" + text + "):gt(0)").remove();
                });
                form.render();
            },
            error: function (result) {
                layer.alert('查询环节出错')
            }
        });
        // 接口参数 默认为 输入 
        $.ajax({
            url: common.getPath() + '/interfaceParamers/byQueryParameter',
            type: 'post',
            dataType: 'json',
            data: {
                intUid: triContent,
                paraInOut: 'input'
            },
            success: function (result2) {
                var list = result2;
                console.info(list)
                for (var i = 0; i < list.length; i++) {
                    var paraIndex = list[i].paraIndex; // 接口索引下标
                    var paraName = list[i].paraName; // 接口名
                    var paraUid = list[i].paraUid // 接口参数id
                    var paraType = list[i].paraType // 接口参数类型
                    var paraParent = list[i].paraParent // 父参数
                    var paraDescription = list[i].paraDescription // 参数描述
                    var index = sortNum + i
                    // 判断有没有list 集合数据 进行映射
                    if (list[i].paraType != "Array" && list[i].paraParent == null) {
                        var trs = '<div class="layui-form-item">' +
                            '<div class="layui-row">' +
                            '<div class="layui-col-md6">' +
                            '<div class="layui-inline">' +
                            '<label class="layui-form-label interfacelabel' + index + '" style="width: 100px">' + paraDescription + '</label>' +
                            '<div class="layui-input-inline">' +
                            '<input class="paraUid" value="' + paraUid + '" style="display: none;"/>' +
                            '<input id="interfaceParam' + index + '" disabled="disabled" readonly="readonly" type="text" name="title" lay-verify="title" autocomplete="off" class="layui-input paraName" value="' + paraName + '">' +
                            '</div>' +
                            '</div>' +
                            '</div>' +
                            '<div class="layui-col-md6">' +
                            '<label class="layui-form-label tablelabel' + index + '" style="width: 100px">表单参数' + index + '</label>' +
                            '<div class="layui-input-inline">' +
                            '<select id="tableParam' + index + '" lay-search onchange="queryOption(this)">' +
                            '</select>' +
                            '</div>' +
                            '</div>' +
                            '</div>' +
                            '</div>';
                        $("#col_md5").append(trs)
                        $(".col_list").css("display", "none");
                    } else if (list[i].paraType == "Array") {
                        $(".col_list").css("display", "block");
                        var listTrs = '<div class="layui-form-item">' +
                            '<div class="layui-row">' +
                            '<div class="layui-col-md6">' +
                            '<div class="layui-inline">' +
                            '<label class="layui-form-label interfacelabel' + index + '" style="width: 100px">' + paraDescription + '</label>' +
                            '<div class="layui-input-inline">' +
                            '<input class="paraUid" value="' + paraUid + '" style="display: none;"/>' +
                            '<input id="interfaceParam' + index + '" disabled="disabled" readonly="readonly" type="text" name="title" lay-verify="title" autocomplete="off" class="layui-input paraName" value="' + paraName + '">' +
                            '</div>' +
                            '</div>' +
                            '</div>' +
                            '<div class="layui-col-md6">' +
                            '<label class="layui-form-label tablelabel' + index + '" style="width: 100px">集合对象' + index + '</label>' +
                            '<div class="layui-input-inline">' +
                            '<select id="tableParam' + index + '" name="listParam_sel" lay-filter="listParam_sel" class="listParam" lay-search onchange="queryOption(this)">' +
                            '</select>' +
                            '</div>' +
                            '</div>' +
                            '</div>' +
                            '</div>';
                        $(".list_mod").append(listTrs)
                    } else if (list[i].paraParent != null) {
                        $(".col_list").css("display", "block");
                        var listTrs = '<div class="layui-form-item">' +
                            '<div class="layui-row">' +
                            '<div class="layui-col-md6">' +
                            '<div class="layui-inline">' +
                            '<label class="layui-form-label interfacelabel' + index + '" style="width: 100px">' + paraDescription + '</label>' +
                            '<div class="layui-input-inline">' +
                            '<input class="paraUid" value="' + paraUid + '" style="display: none;"/>' +
                            '<input id="interfaceParam' + index + '" disabled="disabled" readonly="readonly" type="text" name="title" lay-verify="title" autocomplete="off" class="layui-input paraName" value="' + paraName + '">' +
                            '</div>' +
                            '</div>' +
                            '</div>' +
                            '<div class="layui-col-md6">' +
                            '<label class="layui-form-label tablelabel' + index + '" style="width: 100px">集合参数' + index + '</label>' +
                            '<div class="layui-input-inline">' +
                            '<select id="tableParam' + index + '" name="listfiled_sel"  lay-filter="listfiled_sel" class="listfiled" lay-search onchange="queryOption(this)">' +
                            '</select>' +
                            '</div>' +
                            '</div>' +
                            '</div>' +
                            '</div>';
                        $(".list_mod").append(listTrs)
                    }
                }
                form.render();
            },
            error: function (result2) {
                layer.alert("查询接口参数出错")
            }
        })
    }
    $("#" + triggerToEdit).val(triUid);
    $("#" + triggerToEdit + "Title").val(triTitle);
    $("#chooseTrigger_container").hide();
}

// 修改之前 查询触发器数据信息
function triggerEdit(triggerUid) {
    layui.use(['laypage', 'layer', 'form', 'jquery', 'element'], function () {
        var laypage = layui.laypage,
            layer = layui.layer,
            form = layui.form;
        var element = layui.element;
        var $ = layui.jquery;
        $(".display_container8").css("display", "block");
        var actcUid = getCurrentActcUid();
        // 查询做映射的数据
        $.ajax({
            url: common.getPath() + '/dhTriggerInterface/selectTriggerAndForm',
            type: 'post',
            async: false,
            dataType: 'json',
            data: {
                triUid: triggerUid,
                activityId: $("#activityId").val(),
                parameterType: $("#paramterType").val()
            },
            success: function (result) {
                console.info(result);
                $("#update_param").empty();
                $(".update_mod").empty();
                $("#tb_Trigger").empty();
                if (result.status == 0) {
                    var list = result.data;
                    var sortNum = 1;
                    var startNum = 1;
                    var formId = "";
                    var formName = "";
                    console.info(list)
                    for (var i = 0; i < list.length; i++) {
                        formId = list[i].dynUid;
                        var paraIndex = list[i].dhInterfaceParameter.paraIndex; // 接口索引下标
                        var paraName = list[i].dhInterfaceParameter.paraName; // 接口名
                        var paraUid = list[i].dhInterfaceParameter.paraUid // 接口参数id
                        var paraType = list[i].dhInterfaceParameter.paraType // 接口参数类型
                        var paraParent = list[i].dhInterfaceParameter.paraParent // 父参数
                        var paraDescription = list[i].dhInterfaceParameter.paraDescription // 参数描述
                        formName = list[i].bpmForm.dynTitle // 表单名称
                        formId = list[i].bpmForm.dynUid // 表单ID
                        var index = sortNum + i
                        if (paraType != "Array" && paraParent == null) {
                            var trs = '<div class="layui-form-item">' +
                                '<div class="layui-row">' +
                                '<div class="layui-col-md6">' +
                                '<div class="layui-inline">' +
                                '<label class="layui-form-label interfacelabel' + index + '" style="width: 100px">' + paraDescription + '</label>' +
                                '<div class="layui-input-inline">' +
                                '<input class="paraUid" value="' + paraUid + '" style="display: none;"/>' +
                                '<input id="interfaceParam' + index + '" disabled="disabled" readonly="readonly" type="text" name="title" lay-verify="title" autocomplete="off" class="layui-input paraName" value="' + paraName + '">' +
                                '</div>' +
                                '</div>' +
                                '</div>' +
                                '<div class="layui-col-md6">' +
                                '<label class="layui-form-label tablelabel' + index + '" style="width: 100px">表单参数' + index + '</label>' +
                                '<div class="layui-input-inline">' +
                                '<select id="tableParam' + index + '" lay-search onchange="queryOption(this)">' +
                                '</select>' +
                                '</div>' +
                                '</div>' +
                                '</div>' +
                                '</div>';
                            $("#update_param").append(trs)
                            $(".update_list").css("display", "none");
                        } else if (paraType == "Array") {
                            $(".update_list").css("display", "block");
                            var listTrs = '<div class="layui-form-item">' +
                                '<div class="layui-row">' +
                                '<div class="layui-col-md6">' +
                                '<div class="layui-inline">' +
                                '<label class="layui-form-label interfacelabel' + index + '" style="width: 100px">' + paraDescription + '</label>' +
                                '<div class="layui-input-inline">' +
                                '<input class="paraUid" value="' + paraUid + '" style="display: none;"/>' +
                                '<input id="interfaceParam' + index + '" disabled="disabled" readonly="readonly" type="text" name="title" lay-verify="title" autocomplete="off" class="layui-input paraName" value="' + paraName + '">' +
                                '</div>' +
                                '</div>' +
                                '</div>' +
                                '<div class="layui-col-md6">' +
                                '<label class="layui-form-label tablelabel' + index + '" style="width: 100px">集合对象' + index + '</label>' +
                                '<div class="layui-input-inline">' +
                                '<select id="tableParam' + index + '" name="listParam_sel" lay-filter="listParam_sel" class="listParam" lay-search onchange="queryOption(this)">' +
                                '</select>' +
                                '</div>' +
                                '</div>' +
                                '</div>' +
                                '</div>';
                            $(".update_mod").append(listTrs)
                        } else if (paraParent != null) {
                            $(".update_list").css("display", "block");
                            var listTrs = '<div class="layui-form-item">' +
                                '<div class="layui-row">' +
                                '<div class="layui-col-md6">' +
                                '<div class="layui-inline">' +
                                '<label class="layui-form-label interfacelabel' + index + '" style="width: 100px">' + paraDescription + '</label>' +
                                '<div class="layui-input-inline">' +
                                '<input class="paraUid" value="' + paraUid + '" style="display: none;"/>' +
                                '<input id="interfaceParam' + index + '" disabled="disabled" readonly="readonly" type="text" name="title" lay-verify="title" autocomplete="off" class="layui-input paraName" value="' + paraName + '">' +
                                '</div>' +
                                '</div>' +
                                '</div>' +
                                '<div class="layui-col-md6">' +
                                '<label class="layui-form-label tablelabel' + index + '" style="width: 100px">集合参数' + index + '</label>' +
                                '<div class="layui-input-inline">' +
                                '<select id="tableParam' + index + '" name="listfiled_sel"  lay-filter="listfiled_sel" class="listfiled" lay-search onchange="queryOption(this)">' +
                                '</select>' +
                                '</div>' +
                                '</div>' +
                                '</div>' +
                                '</div>';
                            $(".update_mod").append(listTrs)
                        }
                        form.render();
                    }
                    var trs2 = '<option value="' + formName + '">' +
                        formName +
                        '</option>';
                    $("#tb_Trigger").append(trs2)
                    $.ajax({
                        url: common.getPath() + '/formField/queryFieldByFromUid',
                        type: 'post',
                        dataType: 'json',
                        data: {
                            formUid: formId
                        },
                        success: function (result) {
                            // 输入
                            for (var i = 0; result.status == 0 && i < result.data.length; i++) {
                                var fldIndex = result.data[i].fldIndex; // 字段索引下标
                                var fldCodeName = result.data[i].fldCodeName; // 字段名
                                var indexs = startNum + i
                                // 获取 接口参数的数据
                                var trs = '<option value="' + result.data[i].fldCodeName + '">' +
                                    result.data[i].fldCodeName +
                                    '</option>';
                                var dataTableList = $("#update_param").find(".layui-form-item").find(".layui-row");
                                for (var j = 0; j < dataTableList.length; j++) {
                                    var inputArr = $(dataTableList[j]).find("select");
                                    $(inputArr).append(trs);
                                }
                                var arrayList = $(".update_mod").find(".layui-form-item").find(".layui-row");
                                for (var j = 0; j < arrayList.length; j++) {
                                    var inputArr2 = $(arrayList[j]).find("select");
                                    $(inputArr2).append(trs);
                                }
                            }
                            /*删除重复项*/
                            var itemList = $("#update_param").find(".layui-form-item");
                            for (var j = 0; j < itemList.length + 1; j++) {
                                $("#tableParam" + j + " option").each(function () {
                                    text = $(this).text();
                                    if ($("#tableParam" + j + " option:contains(" + text + ")").length > 1)
                                        $("#tableParam" + j + " option:contains(" + text + "):gt(0)").remove();
                                });
                            }
                            $.ajax({
                                url: common.getPath() + '/dhTriggerInterface/selectTriggerAndForm',
                                type: 'post',
                                async: false,
                                dataType: 'json',
                                data: {
                                    triUid: triggerUid,
                                    activityId: $("#activityId").val(),
                                    parameterType: $("#paramterType").val()
                                },
                                success: function (result) {
                                    if (result.status == 0) {
                                        var list = result.data;
                                        var startNum = 1;
                                        var formId = "";
                                        for (var i = 0; i < list.length; i++) {
                                            var num1 = i + startNum
                                            $("#interfaceParam" + num1).val(list[i].paraName);
                                            $("#tableParam" + num1 + " option[value='" + list[i].fldCodeName + "']").attr("selected", true);
                                            form.render('select');
                                        }
                                        form.render();
                                    }
                                },
                                error: function (result) {
                                    layer.alert('查询失败')
                                }
                            })
                            form.render();
                        }
                    });
                    form.render();
                }
            },
            error: function (result) {
                layer.alert("查询映射数据失败")
            }
        })

        $("#triggerSave").click(function () {
            // 判断是修改 输出参数 还是 输入参数
            if ($("#paramterType").val() == "inputParameter") {
                // 输入参数
                var arr = new Array();
                var dataList = $("#update_param").find(".layui-form-item").find(".layui-row");
                var num = 1;
                for (var i = 0; i < dataList.length; i++) {
                    var index = num + i
                    var inputArr = $(dataList[i]).find("input");
                    var info = {
                        triUid: triggerUid,
                        activityId: $("#activityId").val(),
                        paraName: $("#interfaceParam" + index + "").val(),
                        fldCodeName: $(dataList[i]).find("option:selected").val(),
                        parameterType: $("#paramterType").val()
                    };
                    arr.push(info);
                }
                console.info(arr)
                $.ajax({
                    url: common.getPath() + '/dhTriggerInterface/updateBatch',
                    type: 'post',
                    dataType: 'json',
                    data: JSON.stringify(arr),
                    contentType: "application/json;charset=utf-8",
                    success: function (result) {
                        layer.alert("修改成功")
                    },
                    error: function (result) {
                        layer.alert("修改失败")
                    }
                })
            } else {
                var arr = new Array();
                var dataList = $("#update_param2").find(".layui-form-item").find(".layui-row");
                var num = 1;
                for (var i = 0; i < dataList.length; i++) {
                    var inputArr = $(dataList[i]).find("input");
                    var index = num + i
                    var info = {
                        triUid: triggerUid,
                        activityId: $("#activityId").val(),
                        paraName: $("#interfaceParam" + index + "").val(),
                        fldCodeName: $(dataList[i]).find("option:selected").val(),
                        parameterType: $("#paramterType").val()
                    };
                    arr.push(info);
                }
                $.ajax({
                    url: common.getPath() + '/dhTriggerInterface/updateBatch',
                    type: 'post',
                    dataType: 'json',
                    data: JSON.stringify(arr),
                    contentType: "application/json;charset=utf-8",
                    success: function (result) {
                        layer.alert("修改成功")
                    },
                    error: function (result) {
                        layer.alert("修改失败")
                    }
                })
            }
        })

        //修改参数映射一些事件监听
        element.on('tab(updatParamter)', function (data) {
            $("#update_param").empty();
            $("#update_param2").empty();
            $(".update_mod").empty();
            var index = data.index; // 得到当前Tab的所在下标
            var sortNum = 1;
            if (index == 1) {
                // 输出参数
                $("#paramterType").val("outputParameter");
                var dataTableList = $("#update_param2").find(".layui-form-item").find(".layui-row");
                for (var i = 0; i < dataTableList.length; i++) {
                    var jNum = sortNum + i
                    $(".interfacelabel" + jNum).text("输出接口参数" + jNum);
                    $(".tablelabel" + jNum).text("输出表单参数" + jNum);
                }
                // 查询 输出参数  修改
                $.ajax({
                    url: common.getPath() + '/dhTriggerInterface/selectTriggerAndForm',
                    type: 'post',
                    async: false,
                    dataType: 'json',
                    data: {
                        triUid: triggerUid,
                        activityId: $("#activityId").val(),
                        parameterType: $("#paramterType").val()
                    },
                    success: function (result) {
                        if (result.status == 0) {
                            var list = result.data;
                            var startNum = 1;
                            var formId = "";
                            console.info(list)
                            for (var i = 0; i < list.length; i++) {
                                formId = list[i].dynUid;
                                var index = i + startNum
                                var paraIndex = list[i].dhInterfaceParameter.paraIndex; // 接口索引下标
                                var paraName = list[i].dhInterfaceParameter.paraName; // 接口名
                                var paraUid = list[i].dhInterfaceParameter.paraUid // 接口参数id
                                var paraType = list[i].dhInterfaceParameter.paraType // 接口参数类型
                                var paraParent = list[i].dhInterfaceParameter.paraParent // 父参数			
                                if (paraType != "Array" && paraParent == null) {
                                    var trs = '<div class="layui-form-item">' +
                                        '<div class="layui-row">' +
                                        '<div class="layui-col-md6">' +
                                        '<div class="layui-inline">' +
                                        '<label class="layui-form-label interfacelabel' + index + '" style="width: 100px">输出接口参数' + index + '</label>' +
                                        '<div class="layui-input-inline">' +
                                        '<input class="paraUid" value="' + paraUid + '" style="display: none;"/>' +
                                        '<input id="interfaceParam' + index + '" disabled="disabled" readonly="readonly" type="text" name="title" lay-verify="title" autocomplete="off" class="layui-input paraName" value="' + paraName + '">' +
                                        '</div>' +
                                        '</div>' +
                                        '</div>' +
                                        '<div class="layui-col-md6">' +
                                        '<label class="layui-form-label tablelabel' + index + '" style="width: 100px">输出表单参数' + index + '</label>' +
                                        '<div class="layui-input-inline">' +
                                        '<select id="tableParam' + index + '" lay-search onchange="queryOption(this)">' +
                                        '</select>' +
                                        '</div>' +
                                        '</div>' +
                                        '</div>' +
                                        '</div>';
                                    $("#update_param2").append(trs)
                                    $(".update_list").css("display", "none");
                                } else if (paraType == "Array") {
                                    $(".update_list").css("display", "block");
                                    var listTrs = '<div class="layui-form-item">' +
                                        '<div class="layui-row">' +
                                        '<div class="layui-col-md6">' +
                                        '<div class="layui-inline">' +
                                        '<label class="layui-form-label interfacelabel' + index + '" style="width: 100px">接口集合对象' + index + '</label>' +
                                        '<div class="layui-input-inline">' +
                                        '<input class="paraUid" value="' + paraUid + '" style="display: none;"/>' +
                                        '<input id="interfaceParam' + index + '" disabled="disabled" readonly="readonly" type="text" name="title" lay-verify="title" autocomplete="off" class="layui-input paraName" value="' + paraName + '">' +
                                        '</div>' +
                                        '</div>' +
                                        '</div>' +
                                        '<div class="layui-col-md6">' +
                                        '<label class="layui-form-label tablelabel' + index + '" style="width: 100px">表单集合对象' + index + '</label>' +
                                        '<div class="layui-input-inline">' +
                                        '<select id="tableParam' + index + '" name="listParam_sel" lay-filter="listParam_sel" class="listParam" lay-search onchange="queryOption(this)">' +
                                        '</select>' +
                                        '</div>' +
                                        '</div>' +
                                        '</div>' +
                                        '</div>';
                                    $(".update_mod").append(listTrs)
                                } else if (paraParent != null) {
                                    $(".update_list").css("display", "block");
                                    var listTrs = '<div class="layui-form-item">' +
                                        '<div class="layui-row">' +
                                        '<div class="layui-col-md6">' +
                                        '<div class="layui-inline">' +
                                        '<label class="layui-form-label interfacelabel' + index + '" style="width: 100px">接口集合参数' + index + '</label>' +
                                        '<div class="layui-input-inline">' +
                                        '<input class="paraUid" value="' + paraUid + '" style="display: none;"/>' +
                                        '<input id="interfaceParam' + index + '" disabled="disabled" readonly="readonly" type="text" name="title" lay-verify="title" autocomplete="off" class="layui-input paraName" value="' + paraName + '">' +
                                        '</div>' +
                                        '</div>' +
                                        '</div>' +
                                        '<div class="layui-col-md6">' +
                                        '<label class="layui-form-label tablelabel' + index + '" style="width: 100px">表单集合参数' + index + '</label>' +
                                        '<div class="layui-input-inline">' +
                                        '<select id="tableParam' + index + '" name="listfiled_sel"  lay-filter="listfiled_sel" class="listfiled" lay-search onchange="queryOption(this)">' +
                                        '</select>' +
                                        '</div>' +
                                        '</div>' +
                                        '</div>' +
                                        '</div>';
                                    $(".update_mod").append(listTrs)
                                }

                                $.ajax({
                                    url: common.getPath() + '/formField/queryFieldByFromUid',
                                    type: 'post',
                                    dataType: 'json',
                                    data: {
                                        formUid: formId
                                    },
                                    success: function (result) {
                                        // 输入
                                        for (var i = 0; result.status == 0 && i < result.data.length; i++) {
                                            var fldIndex = result.data[i].fldIndex; // 字段索引下标
                                            var fldCodeName = result.data[i].fldCodeName; // 字段名
                                            var indexs = startNum + i
                                            // 获取 接口参数的数据
                                            var trs = '<option value="' + result.data[i].fldCodeName + '">' +
                                                result.data[i].fldCodeName +
                                                '</option>';
                                            var dataTableList = $("#update_param2").find(".layui-form-item").find(".layui-row");
                                            for (var j = 0; j < dataTableList.length; j++) {
                                                var inputArr = $(dataTableList[j]).find("select");
                                                $(inputArr).append(trs);
                                            }
                                            var arrayList = $(".update_mod").find(".layui-form-item").find(".layui-row");
                                            for (var j = 0; j < arrayList.length; j++) {
                                                var inputArr2 = $(arrayList[j]).find("select");
                                                $(inputArr2).append(trs);
                                            }
                                        }
                                        /*删除重复项*/
                                        var itemList = $("#update_param2").find(".layui-form-item");
                                        for (var j = 0; j < itemList.length + 1; j++) {
                                            $("#tableParam" + j + " option").each(function () {
                                                text = $(this).text();
                                                if ($("#tableParam" + j + " option:contains(" + text + ")").length > 1)
                                                    $("#tableParam" + j + " option:contains(" + text + "):gt(0)").remove();
                                            });
                                        }
                                        $.ajax({
                                            url: common.getPath() + '/dhTriggerInterface/selectTriggerAndForm',
                                            type: 'post',
                                            async: false,
                                            dataType: 'json',
                                            data: {
                                                triUid: triggerUid,
                                                activityId: $("#activityId").val(),
                                                parameterType: $("#paramterType").val()
                                            },
                                            success: function (result) {
                                                if (result.status == 0) {
                                                    var list = result.data;
                                                    var startNum = 1;
                                                    var formId = "";
                                                    for (var i = 0; i < list.length; i++) {
                                                        var num1 = i + startNum
                                                        $("#interfaceParam" + num1).val(list[i].paraName);
                                                        $("#tableParam" + num1 + " option[value='" + list[i].fldCodeName + "']").attr("selected", true);
                                                        form.render('select');
                                                    }
                                                    form.render();
                                                }
                                            },
                                            error: function (result) {
                                                layer.alert('查询失败')
                                            }
                                        })
                                        form.render();
                                    }
                                });
                                form.render();



                                $("#interfaceParam" + index).val(list[i].paraName);
                                $("#tableParam" + index + " option[value='" + list[i].fldCodeName + "']").attr("selected", true);
                                form.render('select');
                            }
                            form.render();
                        }
                    },
                    error: function (result) {
                        layer.alert('查询失败')
                    }
                })
                var itemList = $("#update_param2").find(".layui-form-item");
                for (var j = 0; j < itemList.length + 1; j++) {
                    $("#tableParam" + j + " option").each(function () {
                        text = $(this).text();
                        if ($("#tableParam" + j + " option:contains(" + text + ")").length > 1)
                            $("#tableParam" + j + " option:contains(" + text + "):gt(0)").remove();
                    });
                }
                form.render();
            } else {
                // 输入参数
                $("#paramterType").val("inputParameter");
                var dataTableList = $("#update_param").find(".layui-form-item").find(".layui-row");
                for (var j = 0; j < dataTableList.length; j++) {
                    var jNum2 = sortNum + j
                    $(".interfacelabel" + jNum2).text("输入接口参数" + jNum2)
                    $(".tablelabel" + jNum2).text("输入表单参数" + jNum2)
                }
                // 查询 输入参数  修改
                $.ajax({
                    url: common.getPath() + '/dhTriggerInterface/selectTriggerAndForm',
                    type: 'post',
                    async: false,
                    dataType: 'json',
                    data: {
                        triUid: triggerUid,
                        activityId: $("#activityId").val(),
                        parameterType: $("#paramterType").val()
                    },
                    success: function (result) {
                        console.info(result);
                        $("#update_param").empty();
                        $(".update_mod").empty();
                        $("#tb_Trigger").empty();
                        if (result.status == 0) {
                            var list = result.data;
                            var sortNum = 1;
                            var startNum = 1;
                            var formId = "";
                            var formName = "";
                            console.info(list)
                            for (var i = 0; i < list.length; i++) {
                                formId = list[i].dynUid;
                                var paraIndex = list[i].dhInterfaceParameter.paraIndex; // 接口索引下标
                                var paraName = list[i].dhInterfaceParameter.paraName; // 接口名
                                var paraUid = list[i].dhInterfaceParameter.paraUid // 接口参数id
                                var paraType = list[i].dhInterfaceParameter.paraType // 接口参数类型
                                var paraParent = list[i].dhInterfaceParameter.paraParent // 父参数
                                formName = list[i].bpmForm.dynTitle // 表单名称
                                formId = list[i].bpmForm.dynUid // 表单ID
                                var index = sortNum + i
                                if (paraType != "Array" && paraParent == null) {
                                    var trs = '<div class="layui-form-item">' +
                                        '<div class="layui-row">' +
                                        '<div class="layui-col-md6">' +
                                        '<div class="layui-inline">' +
                                        '<label class="layui-form-label interfacelabel' + index + '" style="width: 100px">输入接口参数' + index + '</label>' +
                                        '<div class="layui-input-inline">' +
                                        '<input class="paraUid" value="' + paraUid + '" style="display: none;"/>' +
                                        '<input id="interfaceParam' + index + '" disabled="disabled" readonly="readonly" type="text" name="title" lay-verify="title" autocomplete="off" class="layui-input paraName" value="' + paraName + '">' +
                                        '</div>' +
                                        '</div>' +
                                        '</div>' +
                                        '<div class="layui-col-md6">' +
                                        '<label class="layui-form-label tablelabel' + index + '" style="width: 100px">输入表单参数' + index + '</label>' +
                                        '<div class="layui-input-inline">' +
                                        '<select id="tableParam' + index + '" lay-search onchange="queryOption(this)">' +
                                        '</select>' +
                                        '</div>' +
                                        '</div>' +
                                        '</div>' +
                                        '</div>';
                                    $("#update_param").append(trs)
                                    $(".update_list").css("display", "none");
                                } else if (paraType == "Array") {
                                    $(".update_list").css("display", "block");
                                    var listTrs = '<div class="layui-form-item">' +
                                        '<div class="layui-row">' +
                                        '<div class="layui-col-md6">' +
                                        '<div class="layui-inline">' +
                                        '<label class="layui-form-label interfacelabel' + index + '" style="width: 100px">接口集合对象' + index + '</label>' +
                                        '<div class="layui-input-inline">' +
                                        '<input class="paraUid" value="' + paraUid + '" style="display: none;"/>' +
                                        '<input id="interfaceParam' + index + '" disabled="disabled" readonly="readonly" type="text" name="title" lay-verify="title" autocomplete="off" class="layui-input paraName" value="' + paraName + '">' +
                                        '</div>' +
                                        '</div>' +
                                        '</div>' +
                                        '<div class="layui-col-md6">' +
                                        '<label class="layui-form-label tablelabel' + index + '" style="width: 100px">表单集合对象' + index + '</label>' +
                                        '<div class="layui-input-inline">' +
                                        '<select id="tableParam' + index + '" name="listParam_sel" lay-filter="listParam_sel" class="listParam" lay-search onchange="queryOption(this)">' +
                                        '</select>' +
                                        '</div>' +
                                        '</div>' +
                                        '</div>' +
                                        '</div>';
                                    $(".update_mod").append(listTrs)
                                } else if (paraParent != null) {
                                    $(".update_list").css("display", "block");
                                    var listTrs = '<div class="layui-form-item">' +
                                        '<div class="layui-row">' +
                                        '<div class="layui-col-md6">' +
                                        '<div class="layui-inline">' +
                                        '<label class="layui-form-label interfacelabel' + index + '" style="width: 100px">接口集合参数' + index + '</label>' +
                                        '<div class="layui-input-inline">' +
                                        '<input class="paraUid" value="' + paraUid + '" style="display: none;"/>' +
                                        '<input id="interfaceParam' + index + '" disabled="disabled" readonly="readonly" type="text" name="title" lay-verify="title" autocomplete="off" class="layui-input paraName" value="' + paraName + '">' +
                                        '</div>' +
                                        '</div>' +
                                        '</div>' +
                                        '<div class="layui-col-md6">' +
                                        '<label class="layui-form-label tablelabel' + index + '" style="width: 100px">表单集合参数' + index + '</label>' +
                                        '<div class="layui-input-inline">' +
                                        '<select id="tableParam' + index + '" name="listfiled_sel"  lay-filter="listfiled_sel" class="listfiled" lay-search onchange="queryOption(this)">' +
                                        '</select>' +
                                        '</div>' +
                                        '</div>' +
                                        '</div>' +
                                        '</div>';
                                    $(".update_mod").append(listTrs)
                                }
                                form.render();
                            }
                            var trs2 = '<option value="' + formName + '">' +
                                formName +
                                '</option>';
                            $("#tb_Trigger").append(trs2)
                            $.ajax({
                                url: common.getPath() + '/formField/queryFieldByFromUid',
                                type: 'post',
                                dataType: 'json',
                                data: {
                                    formUid: formId
                                },
                                success: function (result) {
                                    // 输入
                                    for (var i = 0; result.status == 0 && i < result.data.length; i++) {
                                        var fldIndex = result.data[i].fldIndex; // 字段索引下标
                                        var fldCodeName = result.data[i].fldCodeName; // 字段名
                                        var indexs = startNum + i
                                        // 获取 接口参数的数据
                                        var trs = '<option value="' + result.data[i].fldCodeName + '">' +
                                            result.data[i].fldCodeName +
                                            '</option>';
                                        var dataTableList = $("#update_param").find(".layui-form-item").find(".layui-row");
                                        for (var j = 0; j < dataTableList.length; j++) {
                                            var inputArr = $(dataTableList[j]).find("select");
                                            $(inputArr).append(trs);
                                        }
                                        var arrayList = $(".update_mod").find(".layui-form-item").find(".layui-row");
                                        for (var j = 0; j < arrayList.length; j++) {
                                            var inputArr2 = $(arrayList[j]).find("select");
                                            $(inputArr2).append(trs);
                                        }
                                    }
                                    /*删除重复项*/
                                    var itemList = $("#update_param").find(".layui-form-item");
                                    for (var j = 0; j < itemList.length + 1; j++) {
                                        $("#tableParam" + j + " option").each(function () {
                                            text = $(this).text();
                                            if ($("#tableParam" + j + " option:contains(" + text + ")").length > 1)
                                                $("#tableParam" + j + " option:contains(" + text + "):gt(0)").remove();
                                        });
                                    }
                                    $.ajax({
                                        url: common.getPath() + '/dhTriggerInterface/selectTriggerAndForm',
                                        type: 'post',
                                        async: false,
                                        dataType: 'json',
                                        data: {
                                            triUid: triggerUid,
                                            activityId: $("#activityId").val(),
                                            parameterType: $("#paramterType").val()
                                        },
                                        success: function (result) {
                                            if (result.status == 0) {
                                                var list = result.data;
                                                var startNum = 1;
                                                var formId = "";
                                                for (var i = 0; i < list.length; i++) {
                                                    var num1 = i + startNum
                                                    $("#interfaceParam" + num1).val(list[i].paraName);
                                                    $("#tableParam" + num1 + " option[value='" + list[i].fldCodeName + "']").attr("selected", true);
                                                    form.render('select');
                                                }
                                                form.render();
                                            }
                                        },
                                        error: function (result) {
                                            layer.alert('查询失败')
                                        }
                                    })
                                    form.render();
                                }
                            });
                            form.render();
                        }
                    },
                    error: function (result) {
                        layer.alert("查询映射数据失败")
                    }
                })
                form.render();
            }
        });
    })
}