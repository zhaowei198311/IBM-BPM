// 为翻页提供支持
var pageConfig = {
    pageNum: 1,
    pageSize: 10,
    total: 0
}
var triggerEditMode = '';
var triUidToEdit = '';
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


var triggerElemnts = {
    triContainer: $('#tri_container'),
    triTopDiv: $('#triTopDiv'),
    triForm: $('#form1'),
    triTypeSel: $('#triType'),
    triTitleInput: $('#triTitle'),
    triWebbotInput: $('#triWebbot'),
    triParamTextArea: $('#triParam'),
    triDescriptionTextArea: $('#triDescription'),
    interfaceHidden: $('#addInterface'),
    interfaceView: $('#addInterface_view'),
    interfaceRow: $('#interfaceRow'),
    webbotRow: $('#webbotRow'),
    sureBtn: $('#sure_btn'),
    cancelBtn: $('#cancel_btn')
};


// 切换到创建模式
function switchToCreateMode() {
    triggerEditMode = 'create';
    triggerElemnts.triTopDiv.html('新增触发器');
    triggerElemnts.triTitleInput.val('');
    triggerElemnts.triTypeSel.val('javaclass');
    triggerElemnts.triTypeSel.prop('disabled', false);
    triggerElemnts.webbotRow.show();
    triggerElemnts.interfaceRow.hide();
    triggerElemnts.interfaceHidden.val('');
    triggerElemnts.interfaceView.val('');
    triggerElemnts.triWebbotInput.val('');
    triggerElemnts.triParamTextArea.val('');
    triggerElemnts.triParamTextArea.prop('disabled', false);
    triggerElemnts.triDescriptionTextArea.val('');
    triggerElemnts.interfaceHidden.val('');
    layui.form.render();
    triggerElemnts.triContainer.show();
}

function initTriContainerToEditMode(trigger) {
    triggerEditMode = 'update';
    triggerElemnts.triTopDiv.html('更新触发器');
    triggerElemnts.triTitleInput.val(trigger.triTitle);
    triggerElemnts.triDescriptionTextArea.val(trigger.triDescription);
    triUidToEdit = trigger.triUid;
    triggerElemnts.triTypeSel.val(trigger.triType);
    triggerElemnts.triTypeSel.prop('disabled', true);
    triggerElemnts.triWebbotInput.val(trigger.triWebbot);
    triggerElemnts.triParamTextArea.val(trigger.triParam);
    if (trigger.triType == 'interface') {
        // 如果是接口类型
        triggerElemnts.interfaceView.val(trigger.interfaceTitle);
        triggerElemnts.interfaceHidden.val(trigger.triWebbot);
        triggerElemnts.triParamTextArea.prop('disabled', true);
        triggerElemnts.interfaceRow.show();
        triggerElemnts.webbotRow.hide();
    } else {
        triggerElemnts.triParamTextArea.prop('disabled', false);
        triggerElemnts.interfaceHidden.val('');
        triggerElemnts.interfaceView.val('');
        triggerElemnts.interfaceRow.hide();
        triggerElemnts.webbotRow.show();
    }
    layui.form.render();
    triggerElemnts.triContainer.show();
}

// 切换到编辑模式
function switchToEditMode(triUid) {
    $.ajax({
        url: common.getPath() + '/trigger/serachByPrimarkey',
        type: 'post',
        dataType: 'json',
        data: {
            triUid: triUid
        },
        success: function (result) {
            if (result.status == 0) {
                initTriContainerToEditMode(result.data);
            } else {
                layer.alert('获取触发器数据失败');
            }
        }
    });
}



// 创建触发器
function createTrigger() {
    // 新增触发器
    var triTitle = $("#triTitle").val().trim();
    var triWebbot = $("#triWebbot").val().trim();
    var triType = $("#triType").val().trim();
    var triDescription = $("#triDescription").val().trim();
    var triParam = $("#triParam").val().trim();
    if (!triTitle) {
        layer.alert('请填写触发器名称');
        return;
    }
    if (!triType) {
        layer.alert('请选择触发器类型');
        return;
    }
    // 当初触发器类型是javaClass的时候验证
    if (triType == 'javaclass') {
        if (!triParam) {
            triParam = '{}';
        } else {
            try {
                JSON.parse(triParam);
            } catch (e) {
                layer.alert('javaclass的触发器参数需要符合JSON格式，请重新填写');
                return;
            }
        }
        if (!triWebbot) {
            layer.alert('请填写java类的全限定名');
            return;
        }
        if (!/^(\w+\.)+\w+$/.exec(triWebbot)) {
            layer.alert("类名不符合规范，请重新填写");
            return;
        }
    }
    var webbot = "";
    if ($("#triType").val() == "interface") {
        webbot = $("#addInterface").val();
    } else {
        webbot = $("#triWebbot").val();
    }
    $.ajax({
        url: common.getPath() + '/trigger/save',
        type: 'POST',
        dataType: 'json',
        data: {
            triTitle: triTitle,
            triDescription: triDescription,
            triType: triType,
            triWebbot: webbot,
            triParam: triParam
        },
        beforeSend: function() {
            layer.load(1);
        },
        success: function (result) {
            layer.closeAll('loading');
            if (result.status == 0) {
                var tempIndex = layer.alert('创建成功', function () {
                    getTriggerInfo();
                    layer.close(tempIndex);
                    triggerElemnts.triContainer.hide();
                });
            } else {
                layer.alert(result.msg);
            }
        },
        error: function (result) {
            layer.closeAll('loading');
            layer.alert('操作失败，请稍后再试');
        }
    });
}

// 更新触发器
function updateTrigger() {
    var triTitle = $("#triTitle").val().trim();
    var triWebbot = $("#triWebbot").val().trim();
    var triType = $("#triType").val().trim();
    var triDescription = $("#triDescription").val().trim();
    var triParam = $("#triParam").val().trim();
    if (!triTitle) {
        layer.alert('请填写触发器名称');
        return;
    }
    if (!triType) {
        layer.alert('请选择触发器类型');
        return;
    }
    // 当初触发器类型是javaClass的时候验证
    if (triType == 'javaclass') {
        if (!triParam) {
            triParam = '{}';
        } else {
            try {
                JSON.parse(triParam);
            } catch (e) {
                layer.alert('javaclass的触发器参数需要符合JSON格式，请重新填写');
                return;
            }
        }
        if (!triWebbot) {
            layer.alert('请填写java类的全限定名');
            return;
        }
        if (!/^(\w+\.)+\w+$/.exec(triWebbot)) {
            layer.alert("类名不符合规范，请重新填写");
            return;
        }
    }
    var webbot = "";
    if (triType == 'interface') {
        webbot = triggerElemnts.interfaceHidden.val();
    } else {
        webbot = triggerElemnts.triWebbotInput.val();
    }
    $.ajax({
        url: common.getPath() + '/trigger/update',
        type: 'post',
        dataType: 'json',
        data: {
            triUid: triUidToEdit,
            triTitle: triTitle,
            triDescription: triDescription,
            triType: triType,
            triWebbot: webbot,
            triParam: triParam
        },
        beforeSend: function(){
            layer.load(1);
        },
        success: function (result) {
            layer.closeAll('loading');
            if (result.status == 0) {
                var tempIndex = layer.alert('修改成功', function () {
                    getTriggerInfo();
                    triggerElemnts.triContainer.hide();
                    layer.close(tempIndex);
                });
            } else {
                layer.alert(result.msg);
            }
        },
        error: function () {
            layer.closeAll('loading');
            layer.alert('修改失败，请稍后再试');
        }
    });

}


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

function getInterfaceInfo(){
	$.ajax({
		url :  common.getPath() + '/interfaces/queryDhInterfaceList',
		type : 'post',
		dataType : 'json',
		data : {
			"pageNum": pageConfig.pageNum,
            "pageSize": pageConfig.pageSize,
			"intStatus" : 'enabled'
		},
		beforeSend : function(){
			index = layer.load(1);
		},
		success : function(result){
			layer.close(index)
			if (result.status == 0) {
				console.info(result.data)
				drawTable2(result.data);
			}
		},
		error : function(){
			layer.close(index)
		}
	})
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
            + '<i class="layui-icon"  title="修改触发器"  onclick=switchToEditMode("'
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


function drawTable2(pageInfo){
	 	pageConfig.pageNum = pageInfo.pageNum;
	    pageConfig.pageSize = pageInfo.pageSize;
	    pageConfig.total = pageInfo.total;
	    doPage2();
	    // 渲染数据
	    $("#tabletrDetail").html('');
	    if (pageInfo.total == 0) {
	        return;
	    }
	    
	    var list = pageInfo.list;
	    var startSort = pageInfo.startRow;// 开始序号
	    var trs = "";
	    var intUrl = "";
	    for (var i = 0; i < list.length; i++) {
	        var meta = list[i];
	        var sortNum = startSort + i;
	        if (meta.intUrl != null && meta.intUrl.length > 8) {
	        	intUrl = beautySub(meta.intUrl, 7);
	        } else if (meta.intUrl != null) {
	        	intUrl = meta.intUrl
	        }
	        trs += '<tr><td>'
	        	+  '<input type="checkbox" name="interface_check" value="'+meta.intUid+'" onclick="onSelOne(this)"/>'
	            + sortNum
	            + '</td>'
	            + '<td>'
	            + meta.intTitle
	            + '</td>'
	            + '<td>'
	            + meta.intLabel
	            + '</td>'
	            + '<td>'
	            + meta.intType
	            + '</td>'
	            + '<td title="' + meta.intUrl + '">'
	            + intUrl
	            + '</td>'
	            + '</tr>';
	    }
	    $("#tabletrDetail").append(trs);
}

function doPage2(){
    layui.use(['laypage', 'layer', 'form', 'jquery'], function () {
        var laypage = layui.laypage, layer = layui.layer, form = layui.form;
        var $ = layui.jquery;
        // 完整功能
        laypage.render({
            elem: 'lay_page2',
            curr: pageConfig.pageNum,
            count: pageConfig.total,
            limit: pageConfig.pageSize,
            layout: ['count', 'prev', 'page', 'next', 'limit', 'skip'],
            jump: function (obj, first) {
                // obj包含了当前分页的所有参数
                pageConfig.pageNum = obj.curr;
                pageConfig.pageSize = obj.limit;
                if (!first) {
                	getInterfaceInfo();
                }
            }
        });
    })
}

//复选框只能选择一个
function onSelOne(obj) {
	$('input[name="interface_check"]').not($(obj))
			.prop("checked", false);
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
            if (value == 'interface') {
                triggerElemnts.interfaceRow.show();
                triggerElemnts.webbotRow.hide();
                triggerElemnts.triParamTextArea.prop('disabled', true);
                triggerElemnts.triParamTextArea.val('');
            } else {
                triggerElemnts.interfaceHidden.val('');
                triggerElemnts.interfaceView.val('');
                triggerElemnts.interfaceRow.hide();
                triggerElemnts.webbotRow.show();
                triggerElemnts.triParamTextArea.prop('disabled', false);
            }
            layui.form.render();
        });
    });
}


function searchInterfaceList(){
	var intTitle = $("#interfaceName").val();
	$.ajax({
		url :  common.getPath() + '/interfaces/queryDhInterfaceByTitle',
		type : 'post',
		dataType : 'json',
		data : {
			"pageNum": pageConfig.pageNum,
            "pageSize": pageConfig.pageSize,
			"intStatus" : 'enabled',
			"intTitle" : intTitle	
		},
		beforeSend : function(){
			index = layer.load(1);
		},
		success : function(result){
			layer.close(index)
			if (result.status == 0) {
				drawTable2(result.data);
			}
		},
		error : function(){
			layer.close(index)
		}
	})
}

function del(triUid) {
    layer.confirm('是否删除该触发器？', { title: '提示' }, function (index) {
        $.ajax({
            url: common.getPath() + '/trigger/delete',
            type: 'POST',
            dataType: 'json',
            data: {
                triUid: triUid
            },
            success: function (result) {
                if (result.status == 0) {
                    var tempIndex = layer.alert('删除成功', function () {
                        getTriggerInfo();
                        layer.close(tempIndex);
                    });
                } else {
                    layer.alert(result.msg);
                }
            },
            error: function () {
                layer.alert('删除失败，请稍后再试');
            }
        })
        layer.close(index);
    });
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

$("#addTriBtn").click(function () {
    switchToCreateMode();
});

triggerElemnts.cancelBtn.click(function () {
   triggerElemnts.triContainer.hide();
});

$("#close").click(function(){
	$(".display_container3").css("display", "none");
})
//interface_check
$("#btn_addInterface").click(function(){
	// 选择添加哪个接口
	$('input[name="interface_check"]:checked').each(function() {// 遍历每一个名字为interest的复选框，其中选中的执行函数
		var intTitle = $(this).parent().next().text();
		var intvalue = $(this).attr('value');
		$(".display_container3").css("display", "none");
		$("#addInterface_view").val(intTitle);
		$("#addInterface").val(intvalue);
	});
})

var indx = null;
$("#chooseInterface").click(function(){
	// 查询 所有接口
	$(".display_container3").css("display", "block");
	$.ajax({
		url :  common.getPath() + '/interfaces/queryDhInterfaceList',
		type : 'post',
		dataType : 'json',
		data : {
			"pageNum": pageConfig.pageNum,
            "pageSize": pageConfig.pageSize,
			"intStatus" : 'enabled'
		},
		beforeSend : function(){
			index = layer.load(1);
		},
		success : function(result){
			layer.close(index)
			if (result.status == 0) {
				console.info(result.data)
				drawTable2(result.data);
			}
		},
		error : function(){
			layer.close(index)
		}
	})
	
});

triggerElemnts.sureBtn.click(function () {
    if (triggerEditMode == 'create') {
        createTrigger();
    } else if (triggerEditMode == 'update') {
        updateTrigger();
    }
});



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
        cancelImportTrigger: common.getPath() + '/transfer/cancelImportTransferData'
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
                    getTriggerInfo();
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

