// 为翻页提供支持
var pageConfig = {
	pageNum : 1,
	pageSize : 10,
	total : 0
}

var index;
$.ajaxSetup({beforeSend : function(){index=layer.load(1);}
,complete: function () {layer.close(index)}
});

layui.use([ 'laypage', 'layer' ], function() {
	var laypage = layui.laypage, layer = layui.layer;
	// 完整功能
	laypage.render({
		elem : 'lay_page',
		count : 50,
		limit : 10,
		layout : [ 'count', 'prev', 'page', 'next', 'limit', 'skip' ],
		jump : function(obj) {
			// console.log(obj)
		}
	});
});

// 分页
function doPage() {
	layui.use([ 'laypage', 'layer' ], function() {
		var laypage = layui.laypage, layer = layui.layer;
		// 完整功能
		laypage.render({
			elem : 'lay_page',
			curr : pageConfig.pageNum,
			count : pageConfig.total,
			limit : pageConfig.pageSize,
			layout : [ 'count', 'prev', 'page', 'next', 'limit', 'skip' ],
			jump : function(obj, first) {
				// obj包含了当前分页的所有参数
				pageConfig.pageNum = obj.curr;
				pageConfig.pageSize = obj.limit;
				if (!first) {
					getInterfaceInfo();
				}
			}
		});
	});
}

$(document).ready(function() {
	// 加载数据
	getInterfaceInfo();

    transferInterface.init();

	$(".cancel_btn").click(function() {
		$(".display_container").css("display", "none");
		$("#form1").validate().resetForm();
	})
	$(".cancel2_btn").click(function() {
		$(".display_container3").css("display", "none");
	})

	$(".cancel3_btn").click(function() {
		$(".display_container4").css("display", "none");
	})

	$(".cancel4_btn").click(function() {
		$(".display_container5").css("display", "none");
	})

	$(".sure4_btn").click(function() {
		var $form = $('#updaArrayForm');
		if (!$form.valid()) {
			return false;
		}
		var url = $form.serialize();
		var intStatus = $('#intStatus2').val();
		if (intStatus == 'disabled') {
			url += '&intStatus=' + intStatus;
		}
		
		$.ajax({
			url : 'interfaces/update',
			type : 'POST',
			dataType : 'json',
			data : url,
			success : function(result) {
				if (result.success == true) {
					layer.alert(result.msg);
					$('.serch_interface').click();
					closePopup('exposed_table3_container', 'id');
				} else {
					layer.alert(result.msg);
				}
			}
		});
	})

})

$("#addInterfaces").click(function() {
	$('#titleTop').text('新增接口');
	interfaceInputShowAndHide("", "");
	$('#intStatus').val('enabled');
	popupDivAndReset('display_container', 'class');
})

$("#cancel_btn").click(function() {
	$(".display_container").css("display", "none")
})

$("#sure_btn").click(function() {
	
	if (!$("#form1").valid()) {
		return false;
	};
	

	var url = $('#form1').serialize();
	var intStatus = $('#intStatus').val();
	if (intStatus == 'disabled') {
		url += '&intStatus=' + intStatus;
	}
	
	if($("#intType").val()==''){
		layer.alert('请选择接口类型!');
		return false;
	};
	
	$.ajax({
		url : 'interfaces/add',
		type : 'POST',
		dataType : 'json',
		data : url,
		success : function(result) {
			if (result.success) {
				layer.alert(result.msg);
				getInterfaceInfo();
				closePopup('display_container', 'class');
			} else {
				layer.alert(result.msg);
			}
		}
	});
})

function getInterfaceInfo() {
	$.ajax({
		url : 'interfaces/queryDhInterfaceByTitle',
		type : 'post',
		dataType : 'json',
		data :$('#interfaceSearch').serialize()+"&pageNum="+pageConfig.pageNum+"&pageSize="+pageConfig.pageSize,
		success : function(result) {
			if (result.status == 0) {
				drawTable(result.data);
			}
		}
	});
}

//
function flasher() {
	document.flash.inputes1.style.color = "blue"
}

// 请求数据成功
function drawTable(pageInfo, data) {
	pageConfig.pageNum = pageInfo.pageNum;
	pageConfig.pageSize = pageInfo.pageSize;
	pageConfig.total = pageInfo.total;
	doPage();
	// 渲染数据
	$("#proMet_table_tbody").html('');
	if (pageInfo.total == 0) {
		return;
	}

	var list = pageInfo.list;
	var startSort = pageInfo.startRow;// 开始序号
	var trs = "";
	for (var i = 0; i < list.length; i++) {
		var meta = list[i];
		var sortNum = startSort + i;
		var status = "";
		if (meta.intStatus == "enabled") {
			status = "启用";
		} else {
			status = "停用"
		}
		trs += '<tr>' + '<td>'
				+ sortNum
				+ '</td>'
				+ '<td>'
				+ meta.intTitle
				+ '</td>'
				+ '<td>'
				+ meta.intDescription
				+ '</td>'
				+ '<td>'
				+ meta.intType
				+ '</td>'
				+ '<td id="requestUrl" class="txt-ell" title="'+meta.intUrl+'">'
				+ meta.intUrl
				+ '</td>'
				+ '<td>'
				+ isEmpty(meta.intCallMethod)
				+ '</td>'
				+ '<td>'
				+ status
				+ '</td>'
				+ '<td>'
				+ '<i class="layui-icon" title="复制接口" onclick=copyInterface("'+ meta.intUid + '"); ><img src="resources/desmartsystem/images/face/copy.png" style="height:16px; width:16px;margin-right:4px;margin-bottom:4px;" /></i>'
				//+ '<i class="layui-icon"  title="接口测试" style="font-size:16px;" onclick=textInterface("'+ meta.intUid + '","' + meta.intTitle+ '","input")  >&#xe64c;</i>'
				+ '<i class="layui-icon" title="接口测试" onclick=textInterface("'+ meta.intUid + '","' + encodeURI(meta.intTitle)+ '","input"); ><img src="resources/desmartsystem/images/interface.png" style="height:16px; width:16px;margin-bottom:4px;" /></i>'
				+ '<i class="layui-icon"  title="修改接口"  onclick=updatate("'+ meta.intUid + '") >&#xe642;</i>'
            	+ '<i class="layui-icon"  title="导出接口"  onclick=transferInterface.exportInterface("'+ meta.intUid + '") >&#xe601;</i>'
				+ '<i class="layui-icon"  title="删除接口"  onclick=del("'+ meta.intUid + '") >&#xe640;</i>'
				+ '<i class="layui-icon" style="font-size:17px;"  title="绑定参数"  onclick=info("'+ meta.intUid + '")>&#xe716;</i>' + '</td>' + '</tr>'
	}
	$("#proMet_table_tbody").append(trs);

}

//复制接口
function copyInterface(intUid){
		$('#titleTop').text('复制接口');
		// 复制接口页面
		popupDivAndReset('display_container','class');
		$.ajax({
			url : 'interfaces/queryDhInterfaceById',
			type : 'POST',
			dataType : 'json',
			data : {
				intUid : intUid
			},
			success : function(result) {
				$("#intDescription").val("");
				$('#intStatus').val('enabled');
				$("#intType").val(result.intType);
				$("#intTitle").val('');
				$("#intUrl").val(result.intUrl);
				$("#intCallMethod").val('');
				$("#intLoginUser").val(result.intLoginUser);
				$("#intLoginPwd").val(result.intLoginPwd);
				$("#intRequestXmlAdd").val('');
				$("#intResponseXmlAdd").val('');
				interfaceInputShowAndHide(result.intType,'');
			}
		})
}

// 按钮事件
function info(intUid) {
	// “接口参数详情”按钮
	$("#exposed_table_container").css("display", "block");
	// 给新增参数页面赋接口uid
	$("#intUid").val(intUid);
	getParamersInfo(intUid);
}

function add() {
	$('#arryParameterDiv').hide();
	var fomr2 = $("#form2");
	fomr2.validate().resetForm();
	fomr2[0].reset();
	byParameterTypeHideAndShowElement('String', '');
	$("#exposed_table2_container").css("display", "block");
}

function del(intUid) {
	layer.confirm('是否删除该接口？', {
		btn : [ '确定', '取消' ]
	// 不显示遮罩
	}, function(index) {
		// 提交表单的代码，然后用 layer.close 关闭就可以了，取消可以省略 ajax请求
		$.ajax({
			url : 'interfaces/del',
			type : 'POST',
			dataType : 'json',
			data : {
				intUid : intUid
			},
			success : function(result) {
				if (result.success == true) {
					layer.alert(result.msg);
					//$('.serch_interface').click();
					getInterfaceInfo();
				} else {
					layer.alert(result.msg);
				}
			}
		})
		layer.close(index);
	});
}

function updatate(intUid) {
	// 修改接口页面
	
	popupDivAndReset('display_container5','class');
	
	layui.use([ 'layer', 'form' ], function() {
		var form = layui.form, layer = layui.layer, $ = layui.jquery;
		form.on('switch(intStatusUpd)', function(data) {
			var ckd = this.checked ? 'enabled' : 'disabled';
			document.getElementById("intStatus2").value = ckd;
		});
		$.ajax({
			url : 'interfaces/queryDhInterfaceById',
			type : 'POST',
			dataType : 'json',
			data : {
				intUid : intUid
			},
			success : function(result) {
				//$("#exposed_table3_container").css("display", "block");
				$("#intUid2").val(result.intUid);
				$("#intDescription2").val(result.intDescription);
				if (result.intStatus == "enabled") {
					document.getElementById('intStatus2').checked = true;
				} else {
					document.getElementById('intStatus2').checked = false;
				}
				$("#intType2").val(result.intType);
				$("#intTitle2").val(result.intTitle);
				$("#intUrl2").val(result.intUrl);
				$("#intCallMethod2").val(result.intCallMethod);
				$("#intLoginUser2").val(result.intLoginUser);
				$("#intLoginPwd2").val(result.intLoginPwd);
				$("#intLoginPwd2").val(result.intLoginPwd);
				$("#intResponseXml2").val(result.intResponseXml);
				$("#intRequestXml2").val(result.intRequestXml);

				interfaceInputShowAndHide(result.intType, 1);
				form.render();
			}
		})
	})
}

function getParamersInfo(intUid) {
	$.ajax({
		url : 'interfaceParamers/byQueryParameter',
		type : 'post',
		dataType : 'json',
		data : {
			intUid : intUid
		},
		success : function(result) {
			drawTable2(result);
		}
	})
}

// 请求数据成功
function drawTable2(data) {
//	pageConfig.pageNum = pageInfo.pageNum;
//	pageConfig.pageSize = pageInfo.pageSize;
//	pageConfig.total = pageInfo.total;
	// 渲染数据
	$("#exposed_table_tbody").html('');
	if (data.length == 0) {
		return;
	}
	var list = data;
	var startSort =0;// 开始序号
	var sortNumTow = 1;
	var displaySrotNum = "";
	for (var i = 0; i < list.length; i++) {
		var meta = list[i];
		var sortNum = startSort + i;
		var isMust = "";
		if (meta.isMust == "true") {
			isMust = "是"
		} else {
			isMust = "否"
		}
		var paraParenName = meta.paraParentName;
		var paraType = meta.paraType;
		if (paraParenName == null) {
			displaySrotNum = sortNumTow++;
		} else {
			displaySrotNum = "";
		}

		if (paraParenName == null) {
			var trs = '<tr>';
			trs += '<td>' + displaySrotNum + '</td>';
			trs += '<td>' + meta.paraName + '</td>';
			trs += '<td>' + meta.paraDescription + '</td>';
			trs += '<td>' + meta.paraType + '</td>';
			trs += '<td>' + isEmpty(meta.paraSize) + '</td>';
			trs += '<td>' + isMust + '</td>';
			trs += '<td>' + isEmpty(meta.dateFormat) + '</td>';
			trs += '<td>' + isEmpty(meta.paraParentName) + '</td>';
			if (isEmpty(meta.paraInOut) == 'input') {
				trs += '<td>输入</td>';
			} else {
				trs += '<td>输出</td>';
			}
			trs += '<td><i class="layui-icon" title="修改参数" onclick=getParameter("'
					+ meta.paraUid
					+ '","update"); >&#xe642;</i> <i class="layui-icon" title="删除参数" onclick=deleteParameter("'
					+ meta.paraUid
					+ '","'
					+ meta.intUid
					+ '"); >&#xe640;</i></td>';
			trs += '</tr>';
			$("#exposed_table_tbody").append(trs);
		}

		if (paraType == 'Array') {
			var paraUid = meta.paraUid;
			$.ajax({
						url : 'interfaceParamers/byQueryParameter',
						type : 'POST',
						dataType : 'json',
						async : false,
						data : {
							paraParent : paraUid
						},
						success : function(result) {
							
							for (var j = 0; j < result.length; j++) {
								
								var isMustChildren = result[j].isMust;
								if (result.isMust == "true") {
									isMustChildren = "是"
								} else {
									isMustChildren = "否"
								}
								
								var trs = '<tr>';
								trs += '<td></td>';
								trs += '<td>' + result[j].paraName + '</td>';
								trs += '<td>' + result[j].paraDescription
										+ '</td>';
								trs += '<td>' + result[j].paraType + '</td>';
								trs += '<td>' + isEmpty(result[j].paraSize)
										+ '</td>';
								trs += '<td>' + isMustChildren + '</td>';
								trs += '<td>' + isEmpty(result[j].dateFormat)
										+ '</td>';
								trs += '<td>'
										+ isEmpty(result[j].paraParentName)
										+ '</td>';
								if (isEmpty(result[j].paraInOut) == 'input') {
									trs += '<td>输入</td>';
								} else {
									trs += '<td>输出</td>';
								}
								trs += '<td><i class="layui-icon" title="修改参数" onclick=getParameter("'
										+ result[j].paraUid
										+ '","update"); >&#xe642;</i> <i class="layui-icon" title="删除参数" onclick=deleteParameter("'
										+ result[j].paraUid
										+ '","'
										+ result[j].intUid
										+ '"); >&#xe640;</i></td>';
								trs += '</tr>';
								$("#exposed_table_tbody").append(trs);
							}
						}
					});
		}
	}
}

// 退出
function back() {
	window.location.href = 'javascript:history.go(-1)';
}


var transferInterface = {
	URL: {
        exportInterface: 'transfer/exportInterface',
        tryImportInterface: 'transfer/tryImportInterface',
        sureImportInterface: 'transfer/sureImportInterface',
        cancelImportInterface: 'transfer/cancelImportTransferData'
	},
	init: function () {
        // 导入按钮
        layui.use('upload', function () {
            layui.upload.render({
                elem: $("#importBtn"),
                url: transferInterface.URL.tryImportInterface,
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
                            var confirmIndex = layer.confirm('<p>请确认导入接口</p><p><b>接口标题：</b>' + data.intTitle + '</p>', {
                                btn: ['导入', '取消']
                            }, function () {
                                transferInterface.importInterface();
                                layer.close(confirmIndex); // 关闭confirm层
                            }, function () {
                                $.post(transferInterface.URL.cancelImportInterface);
                            });
                        } else {
                            // 发现已有此触发器
                            var confirmIndex = layer.confirm('<p>接口已存在，<b style="color:red;">是否覆盖配置</b></p><p><b>接口标题：</b>' + data.intTitle + '</p>', {
                                btn: ['覆盖', '取消']
                            }, function () {
                                transferInterface.importInterface();
                                layer.close(confirmIndex); // 关闭confirm层
                            }, function () {
                                $.post(transferInterface.URL.cancelImportInterface);
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
	exportInterface: function (intUid) {
		downLoadFile(transferInterface.URL.exportInterface, {"intUid": intUid});
    },
	importInterface: function () {
        $.ajax({
            url : transferInterface.URL.sureImportInterface,
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

function downLoadFile(URL, PARAMS) {
    var temp_form = document.createElement("form");
    temp_form .action = URL;
    // temp_form .target = "_blank"; 如需新打开窗口 form 的target属性要设置为'_blank'
    temp_form .method = "post";
    temp_form .style.display = "none";
    for (var x in PARAMS) {
        var opt = document.createElement("textarea");
        opt.name = x;
        opt.value = PARAMS[x];
        temp_form .appendChild(opt);
    }
    document.body.appendChild(temp_form);
    temp_form.submit();
    temp_form.remove();
}