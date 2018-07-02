// 为翻页提供支持
var pageConfig = {
    pageNum: 1,
    pageSize: 10,
    total: 0,
    metaUid: "",
	proName: ""
};

var setting = {
    view: {
        selectedMulti: true
    },
    data: {
        simpleData: {
            enable: true,
            idKey: "id",
            pIdKey: "pid",
            rootPId: "rootCategory"
        }
    },
    callback: {
        onClick: zTreeOnClick// 点击回调
    }
};


$(function() {
    // 加载树
    $.ajax({
        url: common.getPath() + "/processMeta/getTreeData",
        type: "post",
        data: {},
        dataType: "json",
        success: function(result) {
            $.fn.zTree.init($("#treeDemo"), setting, result);
            if (getCookie("processDefinition_selectedMetaUid")) {
                var treeObject = $.fn.zTree.getZTreeObj("treeDemo");
                var node = treeObject.getNodeByParam("id", getCookie("processDefinition_selectedMetaUid"));
                treeObject.selectNode(node, true);
            }
        }
    });

	// 选中之前选择的节点
	if (getCookie("processDefinition_selectedMetaUid")) {
		pageConfig.metaUid = getCookie("processDefinition_selectedMetaUid");
		getInfo();  // 获取数据
	} else {
		doPage(); // 刷新分页栏
	}
	
});

function zTreeOnClick(event, treeId, treeNode) {
	if (treeNode.itemType == "processMeta") {
		pageConfig.metaUid = treeNode.id;
		getInfo();
	} else {
		$('#definitionList_tbody').html('');
		pageConfig.pageNum = 1;
		pageConfig.total = 0;
		doPage();
	}
}

function getInfo() {
	// 查询
    common.doPostAjax({
		'url': common.getPath() + "/processDefinition/listDefinitionByProcessMeta",
        'data': {
            "metaUid" : pageConfig.metaUid,
            "pageNum" : pageConfig.pageNum,
            "pageSize" : pageConfig.pageSize
        },
		'fn': function(data) {
            setCookie("processDefinition_selectedMetaUid", pageConfig.metaUid, 7200);
            drawTable(data);
        }
	});


}

// 绘制表格
function drawTable(pageInfo) {
	pageConfig.pageNum = pageInfo.pageNum;
	pageConfig.pageSize = pageInfo.pageSize;
	pageConfig.total = pageInfo.total;
	doPage();

	$('#definitionList_tbody').html('');
	if (pageInfo.total == 0) {
		return;
	}

	var list = pageInfo.list;
	var startSort = pageInfo.startRow;
	var trs = "";
	for (var i = 0; i < list.length; i++) {
		var vo = list[i];
		var sortNum = startSort + i;

		trs += '<tr>'
				+ '<td><input type="checkbox" name="definition_ck" lay-skin="primary" '
				+ 'data-prouid="'
				+ vo.proUid
				+ '" data-proappid="'
				+ vo.proAppId
				+ '" data-proveruid="'
				+ vo.proVerUid
				+ '" data-isactive="'+ vo.isActive +'">'
				+ sortNum
				+ '</td>'
				+ '<td>'
				+ vo.proName
				+ '</td>'
				// + '<td>'
				// + vo.proVerUid
				// + '</td>'
				+ '<td attr="verName">'
				+ vo.verName
				+ '</td>'
				+ '<td attr="isActive">'
				+ vo.isActive
				+ '</td>'
				+ '<td attr="createTime">'
				+ vo.verCreateTime
				+ '</td>'
				+ '<td attr="status">'
				+ vo.proStatus
				+ '</td>'
				+ '<td  attr="updator">'
				+ vo.updator
				+ '</td>'
				+ '<td  attr="updateTime">'
				+ vo.updateTime
				+ '</td>'
				+ '</tr>';
	}
	$("#definitionList_tbody").append(trs);

}

function doPage() {
	layui.use([ 'laypage', 'layer' ], function() {
		var laypage = layui.laypage, layer = layui.layer;
		laypage.render({
			elem : 'lay_page',
			curr : pageConfig.pageNum,
			count : pageConfig.total,
			limit : pageConfig.pageSize,
			layout : [ 'count', 'prev', 'page', 'next', 'limit', 'skip' ],
			jump : function(obj, first) {
				pageConfig.pageNum = obj.curr;
				pageConfig.pageSize = obj.limit;
				if (!first) {
					getInfo();
				}
			}
		});
	});
}

$(function() {
	// “环节同步”
	$("#synchr_btn").click(function() {
		var cks = $("[name='definition_ck']:checked");
		if (!cks.length) {
			layer.alert("请选择要同步的流程定义");
			return;
		}
		if (cks.length > 1) {
			layer.alert("请选择一个要同步的流程定义，不能选择多个");
			return;
		}

		var ck = cks.eq(0);
		var proUid = ck.data('prouid');
		var proVerUid = ck.data('proveruid');
		var proAppId = ck.data('proappid');
		$.ajax({
			url : common.getPath() + "/processDefinition/create",
			beforeSend : function(){
				layer.load(1);
			},
			type : "post",
			dataType : "json",
			data : {
				"proUid" : proUid,
				"proVerUid" : proVerUid,
				"proAppId" : proAppId
			},
			success : function(result) {
				layer.closeAll('loading');
				if (result.status == 0) {
					layer.alert("同步成功");
					// 更新这条记录的信息
					$.ajax({
						url: common.getPath() + "/processDefinition/getSynchronizedDefinition",
					    type: "post",
					    dataType: "json",
					    data: {
							"proUid" : proUid,
							"proVerUid" : proVerUid,
							"proAppId" : proAppId
						},
					    success:function(result) {
					    	if (result.status == 0) {
					    		var vo = result.data;
					    		var ck = $("#definitionList_tbody :checkbox:checked");
					    		if (ck.data('proveruid') == vo.proVerUid) {
					    			var $tr = ck.parent().parent();
					    			$tr.find('td[attr=status]').html(vo.proStatus); // 状态
					    			$tr.find("td[attr=updator]").html(vo.updator); // 修改人
					    			$tr.find("td[attr=updateTime]").html(vo.updateTime); // 修改时间
					    		}
					    	} else {
					    		layer.alert(result.msg);
					    	}
					    }
					});
				} else {
					layer.alert(result.msg);
				}
			},
			error : function() {
				layer.closeAll('loading');
				layer.alert("同步失败，请稍后再试");
			}
		});
	});

	// 启用版本
	$("#enable_btn").click(function(){
		var cks = $("[name='definition_ck']:checked");
		if (!cks.length) {
			layer.alert("请选择一个流程定义");
			return;
		}
		if (cks.length > 1) {
			layer.alert("请选择一个流程定义，不能选择多个");
			return;
		}
		var ck = cks.eq(0);
		var proUid = ck.data('prouid');
		var proVerUid = ck.data('proveruid');
		var proAppId = ck.data('proappid');
		// 查看版本是否激活
		if (ck.data('isactive') != '激活') {
			layer.alert("选择的版本未激活，不能设为启用");
			return;
		}
		layer.confirm("是否确认启用此版本？", function(){
			$.ajax({
				url : common.getPath()
						+ "/processDefinition/enableDefinition",
				dataType : "json",
				data : {
					"proAppId": proAppId,
					"proUid": proUid,
					"proVerUid": proVerUid
				},
				type : "post",
				success : function(result) {
					if (result.status == 0) {
						$("#definitionList_tbody tr").each(function() {
							var status = $(this).find('td[attr=status]').html();
							if (status == '已启用') {
								$(this).find('td[attr=status]').html('已同步');
							}
						});
						ck.parent().parent().find('td[attr=status]').html("已启用");
						layer.alert("启用成功");
					}
				}
			});
		});
		
	});

	// “流程配置”按钮
    $("#toEditDefinition_btn")
        .click(function () {
            var cks = $("[name='definition_ck']:checked");
            if (!cks.length) {
                layer.alert("请选择一个流程定义");
                return;
            }
            if (cks.length > 1) {
                layer.alert("请选择一个流程定义，不能选择多个");
                return;
            }
            var ck = cks.eq(0);
            var proUid = ck.data('prouid');
            var proVerUid = ck.data('proveruid');
            var proAppId = ck.data('proappid');

            $.ajax({
                url: common.getPath() + "/processDefinition/tryEditDefinition",
                dataType: "json",
                type: "post",
                data: {
                    "proUid": proUid,
                    "proVerUid": proVerUid,
                    "proAppId": proAppId
                },
                success: function (result) {
                    if (result.status == 0) {
                        window.location.href = common
                                .getPath() + "/processDefinition/editDefinition?proUid=" + proUid + "&proAppId=" + proAppId +
                            "&proVerUid=" + proVerUid;
                    } else {
                        layer.alert(result.msg);
                    }
                },
                error: function () {
                    layer.alert("流程配置异常，请稍后再试");
                }
            });
        });

	// 查询按钮
    $('#searchByProName_btn').click(function(){
        common.doPostAjax({
			'url': common.getPath() + "/processMeta/searchByProName",
			'data': {
				'proName': $('#proName_input').val()
			},
			'fn': selectNodeOnTree
		});
	});
});

$(function() {
	// 查看"快照流程图"
	$("#snapshotFlowChart_btn").click(function() {
		var cks = $("[name='definition_ck']:checked")
		if (!cks.length) {
			layer.alert("请选择一个流程定义");
			return;
		}
		if (cks.length > 1) {
			layer.alert("请选择一个流程定义，不能选择多个");
			return;
		}
		var ck = cks.eq(0);
		var proUid = ck.data('prouid');
		var proVerUid = ck.data('proveruid');
		var proAppId = ck.data('proappid');

		$.ajax({
			url : common.getPath() + "/processDefinition/snapshotFlowChart",
			dataType : "text",
			type : "POST",
			data : {
				"proUid" : proUid,
				"proVerUid" : proVerUid,
				"proAppId" : proAppId
			},
			success : function(result) {
				layer.open({
					type : 2,
					title : '流程快照',
					shadeClose : true,
					shade : 0.3,
					area : [ '790px', '580px' ],
					content : result
				});
			}
		})
	})

	// "流程配置"按钮
	$("#toEditActivityConf_btn").click(
			function() {
				var cks = $("[name='definition_ck']:checked");
				if (!cks.length) {
					layer.alert("请选择一个流程定义");
					return;
				}
				if (cks.length > 1) {
					layer.alert("请选择一个流程定义，不能选择多个");
					return;
				}
				var ck = cks.eq(0);
				var proUid = ck.data('prouid');
				var proVerUid = ck.data('proveruid');
				var proAppId = ck.data('proappid');
				$.ajax({
					url : common.getPath()
							+ "/processDefinition/tryEditDefinition",
					dataType : "json",
					type : "post",
					data : {
						"proUid" : proUid,
						"proVerUid" : proVerUid,
						"proAppId" : proAppId
					},
					success : function(result) {
						if (result.status == 0) {
							window.location.href = common.getPath()
									+ "/activityConf/edit?proUid=" + proUid
									+ "&proAppId=" + proAppId + "&proVerUid="
									+ proVerUid;
						} else {
							layer.alert(result.msg);
						}
					},
					error : function() {
						layer.alert("操作失败，请稍后再试");
					}
				});

			});
	
	// 查询同类流程
	$("#querySimilarProcess").click(function(){
		var checkList = $("[name='definition_ck']:checked");
		// 只能选择一个流程进行拷贝
		if (checkList.length != 1) {
			layer.alert("请只选择一条流程定义！");
			return;
		}
		var ck = checkList.eq(0);
		var proUid = ck.data('prouid');
		var proVerUid = ck.data('proveruid');
		var proAppId = ck.data('proappid');
		// 判断是否进行环节同步操作
		$.ajax({
			async: false,
			url: common.getPath() + "/processDefinition/whetherLinkSynchronization",
			type: "post",
			dataType: "json",
			data: {
				"bpdId" : proUid,
				"snapshotId" : proVerUid,
				"proAppId" : proAppId
			},
			success: function(data) {
				if (data.status == 0) {
					$(".display_container8").css("display","block");
					$.ajax({
						async: false,
						url: common.getPath() + "/processDefinition/selectSimilarProcessForCopy",
						type: "post",
						dataType: "json",
						data: {
							"proUid" : proUid,
							"proVerUid" : proVerUid,
							"proAppId" : proAppId
						},
						success: function(data) {
							similarList(data.data);
						}
					})
				}else {
					layer.alert("请先进行环节同步！");
					return;
				}
			}
		})		
	});
	
	// checkbox排他选择
	$("#definitionList_tbody").on("click", ":checkbox", function(){
		if ($(this).prop("checked")) {
			$("#definitionList_tbody :checkbox").prop("checked", false);
			$(this).prop("checked", true);
		}
	});
});

function similarList(data){
	$("#similar_process").empty();
	var trs = "";
	$(data).each(function(i){
		var sortNum = i + 1;
		trs += '<tr>'
			+ '<td><input type="checkbox" name="similar" lay-skin="primary" '
			+ 'data-prouid="' + this.proUid + '" data-proappid="' + this.proAppId 
			+ '" data-proveruid="' + this.proVerUid + '" >' + sortNum + '</td>'
			+ '<td>'+ this.proName + '</td>'
			+ '<td>'+ this.proVerUid + '</td>'
			+ '<td>'+ this.verName + '</td>'
//			+ '<td>'+ this.isActive + '</td>'
//			+ '<td>'+ this.verCreateTime + '</td>'
			+ '<td>'+ this.proStatus + '</td>'
//			+ '<td>'+ this.updator + '</td>'
//			+ '<td>'+ this.updateTime + '</td>'
			+ '</tr>';
	})
	$("#similar_process").append(trs);
}
// 拷贝同类流程
function copyProcess(){
	var checkList = $("[name='similar']:checked");
	if (checkList.length != 1) {
		layer.alert("请只选择一条流程定义！");
		return false;
	}
	var ck = checkList.eq(0);
	var proUid = ck.data('prouid');
	var proVerUid = ck.data('proveruid');
	var proAppId = ck.data('proappid');

	var checkList_1 = $("[name='definition_ck']:checked");
	var ck_1 = checkList_1.eq(0);
	var proUidNew = ck_1.data('prouid');
	var proVerUidNew = ck_1.data('proveruid');
	var proAppIdNew = ck_1.data('proappid');

	$.ajax({
		url: common.getPath() + "/processDefinition/copySimilarProcess",
		type: "post",
		dataType: "json",
		beforeSend:function(){
			layer.load(1);
		},
		data: {
			proUid : proUid,
			proVerUid : proVerUid,
			proAppId : proAppId,
			proUidNew : proUidNew,
			proVerUidNew : proVerUidNew,
			proAppIdNew : proAppIdNew
		},
		success: function(data) {
			layer.closeAll('loading');
			if (data.status == 0) {
				layer.alert("拷贝成功！");
				$(".display_container8").css("display","none");
			}else {
				layer.alert(data.msg);
			}
			layer.closeAll("loading");
		},
		error : function() {
            layer.closeAll("loading");
            layer.alert('拷贝失败');
		}
	})
}

// 选择符合的节点
function selectNodeOnTree(metaList) {
    var treeObject = $.fn.zTree.getZTreeObj("treeDemo");
    treeObject.refresh();
	var nodesNeedBeExpand = [];
    for (var i = 0; i < metaList.length; i++) {
    	nodesNeedBeExpand.push(metaList[i].categoryUid);
	}

	var allNodes = treeObject.transformToArray(treeObject.getNodes());
    for (var i = 0; i < allNodes.length; i++) {
        var currNode = allNodes[i];
        if (currNode.itemType == 'processMeta' || currNode.id == 'rootCategory') {
        	continue;
		}
        var index = $.inArray(currNode.id, nodesNeedBeExpand);
        if (index == -1) {
            // 不是需折叠的
            treeObject.expandNode(currNode, false);
        } else {
            treeObject.expandNode(currNode, true);
        }
    }
	// 选中命中的节点
    for (var i = 0; i < metaList.length; i++) {
        var meta = metaList[i];
        var node = treeObject.getNodeByParam("id", meta.proMetaUid);
        treeObject.selectNode(node, true, true);
    }


}