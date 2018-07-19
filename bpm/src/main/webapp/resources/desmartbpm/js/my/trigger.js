// 为翻页提供支持
var pageConfig = {
    pageNum: 1,
    pageSize: 10,
    total: 0
}

// 加载事件
$(document).ready(function () {
    // 加载数据
    getTriggerInfo();
    transferTrigger.init();
    // 表单验证
    $('#form1').validate({
        rules: {
            triTitle: {
                required: true
            },
            triType: {
                required: true
            },
        }
    })
})

function getTriggerInfo() {
    $.ajax({
        url: common.getPath() + "/trigger/search",
        type: "post",
        dataType: "json",
        data: {
            "pageNum": pageConfig.pageNum,
            "pageSize": pageConfig.pageSize
        },
        success: function (result) {
            if (result.status == 0) {
                drawTable(result.data);
            }
        }
    });
}

// 请求数据成功
function drawTable(pageInfo) {
    pageConfig.pageNum = pageInfo.pageNum;
    pageConfig.pageSize = pageInfo.pageSize;
    pageConfig.total = pageInfo.total;
    doPage();
    // 渲染数据
    $("#trigger_table_tbody").html('');
    if (pageInfo.total == 0) {
        return;
    }

    var list = pageInfo.list;
    var startSort = pageInfo.startRow;// 开始序号
    var trs = "";
    var triPamaer = "";
    var triwebot = "";
    var triDescription = "";
    for (var i = 0; i < list.length; i++) {
        var meta = list[i];
        var sortNum = startSort + i;
        var createTime = "";
        var updateTime = "";
        if (meta.createTime) {
            createTime = common.dateToString(new Date(meta.createTime));
        }
        if (meta.updateTime) {
            updateTime = common.dateToString(new Date(meta.updateTime));
        }
        // 判断参数为空
        if (meta.triParam == null || meta.triParam == '') {
            triPamaer = "没有参数"
        } else {
            triPamaer = beautySub(meta.triParam, 15);
        }

        if (meta.triDescription == null || meta.triDescription == '') {
            triDescription = "没有描述";
        } else {
            triDescription = beautySub(meta.triDescription, 5);
        }

        //	alert(meta.triWebbot.length)
        // 触发执行命令太长 需要修改
        if (meta.triWebbot != null && meta.triWebbot.length > 15) {
            triwebot = beautySub(meta.triWebbot, 15);
        } else if (meta.triWebbot != null) {
            triwebot = meta.triWebbot
        } else {
            triwebot = "没有执行命令"
        }
        trs += '<tr><td>'
            + sortNum
            + '</td>'
            + '<td>'
            + meta.triTitle
            + '</td>'
            + '<td>'
            + triDescription
            + '</td>'
            + '<td>'
            + meta.triType
            + '</td>'
            + '<td title="' + meta.triWebbot + '">'
            + triwebot
            + '</td>'
			+ '<td title=\''+meta.triParam+'\'>' 
            + triPamaer
            + '</td>'
            + '<td>'
            + meta.sysUser.userName
            + '</td>'
            + '<td>'
            + createTime
            + '</td>'
            + '<td>'
            + '<i class="layui-icon"  title="修改触发器"  onclick=updatate("'
            + meta.triUid + '") >&#xe642;</i>'
            + '<i class="layui-icon"  title="导出触发器"  onclick=transferTrigger.exportTrigger("'
            + meta.triUid + '") >&#xe601;</i>'
            + '<i class="layui-icon"  title="删除触发器"  onclick=del("'
            + meta.triUid + '") >&#xe640;</i>'
            + '</td>'
            + '</tr>';
    }
    $("#trigger_table_tbody").append(trs);

}

// 分页
function doPage() {
    layui.use(['laypage', 'layer', 'form', 'jquery'], function () {
        var laypage = layui.laypage, layer = layui.layer, form = layui.form;
        var $ = layui.jquery;
        // 完整功能
        laypage.render({
            elem: 'lay_page',
            curr: pageConfig.pageNum,
            count: pageConfig.total,
            limit: pageConfig.pageSize,
            layout: ['count', 'prev', 'page', 'next', 'limit', 'skip'],
            jump: function (obj, first) {
                // obj包含了当前分页的所有参数
                pageConfig.pageNum = obj.curr;
                pageConfig.pageSize = obj.limit;
                if (!first) {
                    getTriggerInfo();
                }
            }
        });

        form.on('select(triType)', function (data) {
            var value = data.value;
            byParameterTypeHideAndShowElement(value, "");
            if (data.value == 'interface') {
                // 请求ajax 获取接口数据
                $.ajax({
                    url: common.getPath() + '/interfaces/queryDhInterfaceList',
                    type: 'post',
                    dataType: 'json',
                    data: {
                        intStatus: 'enabled'
                    },
                    success: function (result) {
                        var data = result.data.list;
                        for (var i = 0; i < data.length; i++) {
                            var trs = '<option value="' + data[i].intUid + '">'
                                + data[i].intTitle
                                + '</option>';
                            $("#triWebbotType").append(trs)
                        }

                        form.render();
                    },
                    error: function () {
                        layer.msg('查询接口异常', {
                            icon: 5
                        });
                    }
                })
                // 隐藏 参数

                $("#triParam").attr('disabled', true)
                $("#triParam").addClass("layui-disabled")
                form.render();
            } else {
                $("#triParam").attr('disabled', false)
                $("#triParam").removeClass("layui-disabled")
            }
        });
    });
}


function byParameterTypeHideAndShowElement(paraType, selector) {
    var triWebbot = $('.triWebbot'); // 触发器执行命令
    var trijiekou = $('.triInterface'); // 选择接口
    var triParam = $('.triParam'); // 参数
    var triType = $('.triType');
    switch (paraType) {
        case 'sql':
            triWebbot.show();
            triParam.show();
            trijiekou.hide();
            clearTableData();
            break;
        case 'javaclass':
            triWebbot.show();
            triParam.show();
            trijiekou.hide();
            clearTableData();
            break;
        case 'interface':
            trijiekou.css("display", "block");
            triParam.show();
            triWebbot.hide();
            $("#triWebbotType").empty();
            clearTableData();
            break;
        case 'script':
            triWebbot.show();
            triParam.show();
            trijiekou.hide();
            clearTableData();
            break;
    }
}

function clearTableData() {
    $("#triWebbot").val("");
    $("#triParam").val("");
    $("#triTitle").val("");
    $("#triDescription").val("");
}


function del(triUid) {
    layer.confirm('是否删除该触发器？', { icon: 3, title: '提示' }, function (index) {
        $.ajax({
            url: common.getPath() + '/trigger/delete',
            type: 'POST',
            dataType: 'text',
            data: {
                triUid: triUid
            },
            success: function (result) {
                window.location.href = common.getPath() + "/trigger/index";
            }
        })
        layer.close(index);
    });
}

function updatate(triUid) {
    $("#triUid").val(triUid)
    $("#triWebbotType2").empty();
    var triWebbot = "";
    layui.use(['form', 'layer', 'jquery'], function () {
        layer = layui.layer, form = layui.form;
        var $ = layui.jquery;
        $(".display_container2").css("display", "block");
        $.ajax({
            url: common.getPath() + '/trigger/serachByPrimarkey',
            type: 'POST',
            dataType: 'json',
            data: {
                triUid: triUid
            },
            success: function (result) {
                console.info(result)
                if (result.status == 0) {
                    byParameterTypeHideAndShowElement(result.data.triType, "");
                    $("#triType2").attr('disabled', true)
                    $("#triType2").addClass("layui-disabled")
                    triWebbot = result.data.triWebbot
                    if (result.data.triType == "interface") {
                        $("#triParam2").attr('disabled', true)
                        $("#triParam2").addClass("layui-disabled")
                        // 请求ajax 获取接口数据
                        $.ajax({
                            url: common.getPath() + '/interfaces/queryDhInterfaceList',
                            type: 'post',
                            dataType: 'json',
                            data: {
                            },
                            success: function (result) {
                                var data = result.data.list;
                                for (var i = 0; i < data.length; i++) {
                                    var trs = '<option value="' + data[i].intUid + '">'
                                        + data[i].intTitle
                                        + '</option>';
                                    $("#triWebbotType2").append(trs)
                                }
                                // 添加完 接口后 默认选中 查出对应接口
                                $("#triWebbotType2 option[value='" + triWebbot + "']").attr("selected", true);
                                form.render();
                            },
                            error: function () {
                                layer.msg('查询接口异常', {
                                    icon: 5
                                });
                            }
                        })
                    } else {
                        $("#triParam2").attr('disabled', false)
                        $("#triParam2").removeClass("layui-disabled")
                    }
                    $("#triTitle2").val(result.data.triTitle);
                    $("#triDescription2").val(result.data.triDescription);
                    $("#triParam2").val(result.data.triParam);
                    $("#triWebbot2").val(result.data.triWebbot);
                    $("#triType2").val(result.data.triType);
                    form.render();
                }
            },
            error: function (result) {
                layer.alert('查询失败')
            }
        })
    })
}

$(".search_btn").click(function () {
    var name = document.getElementById("triggerName_input").value;
    var type = document.getElementById("triggerType_select").value;
    $.ajax({
        url: common.getPath() + '/trigger/search',
        type: 'POST',
        dataType: 'json',
        data: {
            triTitle: name,
            triType: type
        },
        success: function (result) {
            if (result.status == 0) {
                drawTable(result.data);
            }
        }
    })
})

$("#show_expose_btn").click(function () {
    $(".display_container").css("display", "block");
})

$(".cancel_btn").click(function () {
    $(".display_container").css("display", "none");
    $(".display_container2").css("display", "none");
    $("#form1").validate().resetForm();
})

$(".sure_btn").click(function () {
    // 新增触发器
    var triTitle = $("#triTitle").val();
    var triWebbot = $("#triWebbot").val();
    var triType = $("#triType").val();
    var triDescription = $("#triDescription").val();
    var triParam = $("#triParam").val();
    if (triTitle.replace(/(^s*)|(s*$)/g, "").length != 0 && triType != null) {
        var webbot = "";
        var options = $("#triWebbotType option:selected");
        if ($("#triType").val() == "interface") {
            webbot = options.val();
        } else {
            webbot = $("#triWebbot").val();
        }
        $.ajax({
            url: common.getPath() + '/trigger/save',
            type: 'POST',
            dataType: 'text',
            data: {
                triTitle: triTitle,
                triDescription: triDescription,
                triType: triType,
                triWebbot: webbot,
                triParam: triParam
            },
            success: function (result) {
                window.location.href = common.getPath() + '/trigger/index'
            },
            error: function (result) {
                layer.msg('添加失败', {
                    icon: 5
                });
            }
        })
    } else {
        layer.alert('参数不能为空')
    }
})

// 修改触发器
$(".update_btn").click(function () {
    if ($("#triType2").val() == "interface") {
        $.ajax({
            url: common.getPath() + '/trigger/update',
            type: 'POST',
            dataType: 'text',
            data: {
                triTitle: $("#triTitle2").val(),
                triDescription: $("#triDescription2").val(),
                triType: $("#triType2").val(),
                triWebbot: $("#triWebbotType2").val(),
                triParam: $("#triParam2").val(),
                triUid: $("#triUid").val()
            },
            success: function (result) {
                layer.alert('修改成功', function (index) {
                    window.location.href = common.getPath() + '/trigger/index'
                    layer.close(index);
                });
            },
            error: function (result) {
                layer.alert("修改失败");
            }
        })
    } else {
        $.ajax({
            url: common.getPath() + '/trigger/update',
            type: 'POST',
            dataType: 'text',
            data: {
                triTitle: $("#triTitle2").val(),
                triDescription: $("#triDescription2").val(),
                triType: $("#triType2").val(),
                triWebbot: $("#triWebbot2").val(),
                triParam: $("#triParam2").val(),
                triUid: $("#triUid").val()
            },
            success: function (result) {
                layer.alert('修改成功', function (index) {
                    window.location.href = common.getPath() + '/trigger/index'
                    layer.close(index);
                });
            },
            error: function (result) {
                layer.alert("修改失败");
            }
        })
    }
})

// 字符转换
function beautySub(str, len) {
    var reg = /[\u4e00-\u9fa5]/g,    //专业匹配中文
        slice = str.substring(0, len),
        chineseCharNum = (~~(slice.match(reg) && slice.match(reg).length)),
        realen = slice.length * 2 - chineseCharNum;
    return str.substr(0, realen) + (realen < str.length ? "..." : "");
}



var transferTrigger = {
    URL: {
    	exportTrigger: common.getPath() + '/transfer/exportTrigger',
        tryImportTrigger: common.getPath() + '/transfer/tryImportTrigger',
        sureImportTrigger: common.getPath() + '/transfer/sureImportTrigger',
        cancelImportTrigger: common.getPath() + '/transfer/cancelImporTransferData'
    },
    init: function () {
        // 导入按钮
        layui.use('upload', function () {
            layui.upload.render({
                elem: $("#importBtn"),
                url: transferTrigger.URL.tryImportTrigger,
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
                            // 新的触发器
                            var confirmIndex = layer.confirm('<p>请确认导入触发器</p><p><b>触发器标题：</b>' + data.triTitle + '</p>', {
                                btn: ['导入', '取消']
                            }, function () {
                                transferTrigger.importTrigger();
                                layer.close(confirmIndex); // 关闭confirm层
                            }, function () {
                                $.post(transferTrigger.URL.cancelImportTrigger);
                            });
                        } else {
                            // 发现已有此触发器
                            var confirmIndex = layer.confirm('<p>触发器已存在，<b style="color:red;">是否覆盖配置</b></p><p><b>触发器标题：</b>' + data.triTitle + '</p>', {
                                btn: ['覆盖', '取消']
                            }, function () {
                                transferTrigger.importTrigger();
                                layer.close(confirmIndex); // 关闭confirm层
                            }, function () {
                                $.post(transferTrigger.URL.cancelImportTrigger);
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
    exportTrigger: function (triUid) {
        if (!triUid) {
        	layer.alert('出错了，请刷新页面再试');
		}
		common.downLoadFile(transferTrigger.URL.exportTrigger, {'triUid': triUid});
    },
    importTrigger: function () {
        $.ajax({
            url : transferTrigger.URL.sureImportTrigger,
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

