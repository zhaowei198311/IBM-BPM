$(function() {
	if (getCookie("processDefinition_selectedMetaUid")) {
		pageConfig.metaUid = getCookie("processDefinition_selectedMetaUid");
		getInfo();
	} else {
		doPage();
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
	$.ajax({
		url : common.getPath()
				+ "/processDefinition/listDefinitionByProcessMeta",
		dataType : "json",
		data : {
			"metaUid" : pageConfig.metaUid,
			"pageNum" : pageConfig.pageNum,
			"pageSize" : pageConfig.pageSize
		},
		type : "post",
		success : function(result) {
			if (result.status == 0) {
				setCookie("processDefinition_selectedMetaUid", pageConfig.metaUid, 7200);
				drawTable(result.data);
			}
		}
	});
}

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
				+ '" >'
				+ sortNum
				+ '</td>'
				+ '<td>'
				+ vo.proName
				+ '</td>'
				+ '<td>'
				+ vo.proVerUid
				+ '</td>'
				+ '<td>'
				+ vo.verName
				+ '</td>'
				+ '<td>'
				+ vo.isActive
				+ '</td>'
				+ '<td>'
				+ vo.verCreateTime
				+ '</td>'
				+ '<td>'
				+ vo.proStatus
				+ '</td>'
				+ '<td>'
				+ vo.updator
				+ '</td>'
				+ '<td>'
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
	// 同步
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
			type : "post",
			dataType : "json",
			data : {
				"proUid" : proUid,
				"proVerUid" : proVerUid,
				"proAppId" : proAppId
			},
			success : function(result) {
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
					    			$tr.find("td").eq(6).html(vo.proStatus); // 状态
					    			$tr.find("td").eq(7).html(vo.updator); // 修改人
					    			$tr.find("td").eq(8).html(vo.updateTime); // 修改时间
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
				layer.alert("同步失败，请稍后再试");
			}
		});
	});

	// “流程配置”按钮
	$("#toEditDefinition_btn")
			.click(
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

						$
								.ajax({
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
											window.location.href = common
													.getPath()
													+ "/processDefinition/editDefinition?proUid="
													+ proUid
													+ "&proAppId="
													+ proAppId
													+ "&proVerUid="
													+ proVerUid;
										} else {
											layer.alert(result.msg);
										}
									},
									error : function() {
										layer.alert("流程配置异常，请稍后再试");
									}
								});
					});
});

$(function() {
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
					shade : 0.8,
					area : [ '790px', '580px' ],
					content : result
				});
			}
		})
	})

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
			url: common.getPath() + "/processDefinition/selectSimilarProcessForCopy",
			type: "post",
			dataType: "json",
			data: {
				"proUid" : proUid,
				"proVerUid" : proVerUid,
				"proAppId" : proAppId
			},
			success: function(data) {
				var list = data.data;
				if (list.length > 0) {
					$(".display_container8").css("display","block");
//					$.ajax({
//						async: false,
//						url: common.getPath() + "/processDefinition/selectSimilarProcessForCopy",
//						type: "post",
//						dataType: "json",
//						data: {
//							"proUid" : proUid,
//							"proVerUid" : proVerUid,
//							"proAppId" : proAppId
//						},
//						success: function(data) {
							similarList(list);
//						}
//					})
				}else {
					layer.alert("请先进行环节同步！");
					return;
				}
			}
		})		
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
		$("#similar_process").append(trs);
	})
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
		async: false,
		url: common.getPath() + "/processDefinition/copySimilarProcess",
		type: "post",
		dataType: "json",
		data: {
			proUid : proUid,
			proVerUid : proVerUid,
			proAppId : proAppId,
			proUidNew : proUidNew,
			proVerUidNew : proVerUidNew,
			proAppIdNew : proAppIdNew
		},
		success: function(data) {
			if (data.status == 0) {
				layer.alert("拷贝成功！");
				$(".display_container8").css("display","none");
			}else {
				layer.alert("拷贝失败！");
				console.log(data.msg);
			}
		}
	})
}