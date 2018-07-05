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
		'url': common.getPath() + "/dhProcessRetrieve/queryProcessRetrieve",
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
				+ '<td><input type="checkbox" data-dataset="'+vo.dataSet+'" data-dicname = "'
				+ vo.dicName+'" value="'+vo.retrieveUid+'"/>'
				+ sortNum
				+ '</td>'
				+ '<td>'
				+ vo.fieldLabel
				+ '</td>'
				+ '<td>'
				+ vo.fieldName
				+ '</td>'
				+ '<td>'
				+ vo.elementType
				+ '</td>'
				+ '<td>';
				if(vo.isScope=='TRUE'){
					trs+='是'
				}else{
					trs+='否'
				}
			trs += '</td>'
				+ '<td>';
				if(vo.dataSource != null){
					trs +=  vo.dataSource
				}
			trs +='</td>'
				+ '<td>'
				+ vo.userName
				+ '</td><td>'
				+ common.dateToString(new Date(vo.createTime))
				+ '</td><td>';
				if(vo.updateUserName!=null){
					trs += vo.updateUserName
				}
			trs +='</td><td>';
				if(vo.updateTime!=null){
					trs += common.dateToString(new Date(vo.updateTime))
				}
			trs +='</td></tr>';
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
    
	// checkbox排他选择
	$("#definitionList_tbody").on("click", ":checkbox", function(){
		if ($(this).prop("checked")) {
			$("#definitionList_tbody :checkbox").prop("checked", false);
			$(this).prop("checked", true);
		}
	});
});

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

layui.use('form', function() {
	var form = layui.form;
	form.on('select(elementTypeFilter)', function(data) {
		if(data.value == "input"){
			//$("input[name='isScope'][value='TRUE']").prop("checked",true);
			$(".is-scope").show();
		}else{
			$(".is-scope").hide();
		}
		if(data.value == "select"){
			$("input[name='isScope'][value='FALSE']").prop("checked",true);
			$(".source-by-dictionaries").show();
		}else{
			$("#dataSet_view").val("");
			$("#dataSet").val("");
			$(".source-by-dictionaries").hide();
		}
		if(data.value == "date"){
			//$("input[name='isScope'][value='TRUE']").prop("checked",true);
			$(".is-scope").show();
			$(".source-by-dictionaries").hide();
		}
		form.render("radio");
	});
});

//校验规则

$('#addProcessRetrieve_form').validate(
		{
			rules : {

				filedLabel : {
					required : function(element) {
						return $('input[name="filedLabel"]')
								.val().trim().length > 0;
					}
				},
				filedName : {
					required : function(element) {
						return $('input[name="filedName"]')
								.val().trim().length > 0;
					}
				},
				isScope : {
					maxlength : 100,
					required : function(element) {
						if ($('select[name="elementType"]').val()=="input") {
							return true;
						} else {
							return false;
						}
					}
				},
				dataSet : {
					required : function(element) {
					if($('select[name="elementType"]').val()=="select"){
						return true;
					}else{
						return false;
					}
					}
				}
			}
		});

//下拉列表的数据类型选择
function selectData(obj) {
	var elementId = $(obj).prev().prop("id");
	common.chooseDictionary(elementId);
}

/**
 * 提交新增、修改流程检索字段
 * @returns
 */
function submitOperationProcessRetrieve(){
	if (!$('#addProcessRetrieve_form').valid()) {
		layer.alert("验证失败，请检查后提交");
		return;
	}
	var metaUid = getCookie("processDefinition_selectedMetaUid");
	if(metaUid == null || metaUid == undefined || metaUid == ""){
		layer.alert("请选择一个流程元数据进行操作");
		return;
	}
	var formData = $('#addProcessRetrieve_form').serializeArray();
	//var data = formData +"&"+{"metaUid":metaUid};
	$.ajax({
		url: common.getPath() + "/dhProcessRetrieve/operationProcessRetrieve?metaUid="+metaUid,
		dataType:"json",
		data:formData,
		beforeSend:function(){
			layer.load(1);
		},
		success:function(result){
			layer.alert(result.msg);
			if(result.status==0){
				getInfo();
			}
			$('.display_container5').hide();
			layer.closeAll("loading");
		},
		error:function(){
			layer.alert("操作异常");
			layer.closeAll("loading");
		}
	});
}
/**删除检索字段**/
function deleteProcessRetrieve(){
	if($("#definitionList_tbody :checkbox:checked").length!=1){
		layer.alert("请选择一个流程检索字段进行删除");
		return;
	}
	var metaUid = getCookie("processDefinition_selectedMetaUid");
	if(metaUid == null || metaUid == undefined || metaUid == ""){
		layer.alert("请选择一个流程元数据进行操作");
		return;
	}
	var retrieveUid = $("#definitionList_tbody :checkbox:checked").val();
	$.ajax({
		url: common.getPath() + "/dhProcessRetrieve/deleteProcessRetrieve",
		dataType:"json",
		data:{"retrieveUid":retrieveUid},
		beforeSend:function(){
			layer.load(1);
		},
		success:function(result){
			layer.alert(result.msg);
			if(result.status==0){
				getInfo();
			}
			$('.display_container5').hide();
			layer.closeAll("loading");
		},
		error:function(){
			layer.alert("删除异常");
			layer.closeAll("loading");
		}
	});
}

/** 修改检索字段 **/
function updateProcessRetrieve(){
	if($("#definitionList_tbody :checkbox:checked").length!=1){
		layer.alert("请选择一个流程检索字段进行修改");
		return;
	}
	$("#retrieveTitle").text("修改检索字段");
	$("input[name='retrieveUid']").val($("#definitionList_tbody :checkbox:checked").val());
	var tr = $("#definitionList_tbody :checkbox:checked").parent().parent();
	$("input[name='fieldLabel']").val(tr.find("td").eq(1).text());
	$("input[name='fieldName']").val(tr.find("td").eq(2).text());
	$("#elementType").val(tr.find("td").eq(3).text());
	if(tr.find("td").eq(4).text()=="是"){
		$("input[name='isScope'][value='TRUE']").prop("checked",true);
	}else{
		$("input[name='isScope'][value='FALSE']").prop("checked",true);
	}
	if(tr.find("td").eq(5).text()=="数据字典拉取"){
		$("#dataSet").val($("#definitionList_tbody :checkbox:checked").data("dataset"));
		$("#dataSet_view").val($("#definitionList_tbody :checkbox:checked").data("dicname"));
	}
	layui.form.render();
	var data = {};
	data.value = $("#elementType").val();
	layui.event.call($("#elementType"),'form','select(elementTypeFilter)',data);
	$(".display_container5").show();
}

/**新增检索字段**/
function addProcessRetrieve(){
	$("input[name='retrieveUid']").val("");
	$("input[name='isScope'][value='TRUE']").prop("checked",true);
	$('#retrieveTitle').text('新增检索字段');
	layui.form.render("radio");
	$('.display_container5').show();
}
