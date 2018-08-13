$(function(){
	// 选择用户
	$("#choose_handle_user").click(function() {
		common.chooseUser('userId', 'true');
	});
	$("#choose_target_user").click(function(){
		common.chooseUser('targetUser', 'true');
	});
	// 加载数据
	getTaskInstanceByPage();
	//全选
	 $("#checked-All-ins").click(function(){ 
		var checkeNodes= $("input[type='checkbox'][name='checkTaskIns']");
	    checkeNodes.prop("checked",$(this).prop("checked")); 
	});
});
layui.use('form', function(){
	var form = layui.form;
	form.render("select");
});

//设置用户标签
function setLabelUserView(a){
	$("#labelUserView").empty();
	var userView = $(a).val();
	if(userView != null ){
		userView = userView.substring(0,userView.length-1);
	}
	$("#labelUserView").text(userView+"的待办任务");
	getTaskInstanceByPage();
}

//为翻页提供支持
var pageConfig = {
	pageNum : 1,
	pageSize : 8,
	usrUid : "",
	isAgent : "",
	total : 0
}

// 分页
function doPage() {
	layui.use([ 'laypage', 'layer' ], function() {
		var laypage = layui.laypage, layer = layui.layer;
		//完整功能
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
					getTaskInstanceByPage();
				}
			}
		});
	});
}
function getTaskInstanceByPage(){
	pageConfig.usrUid = $("#userId").val().split(";")[0];//第一次加载，没有分号，选人后有分号
	$.ajax({
		url : common.getPath()+'/dhTaskInstanceTurn/getTaskInstanceByPage',
		type : 'post',
		dataType : 'json',
		data : {
			pageNum : pageConfig.pageNum,
			pageSize : pageConfig.pageSize,
			usrUid : pageConfig.usrUid,
			isAgent : pageConfig.isAgent
		},
		beforeSend: function(){
			layer.load(1);
		},
		success : function(result){
			if (result.status == 0) {
				drawTable(result.data);
			}
			layer.closeAll("loading");
		},error : function(){
			layer.closeAll("loading");
		}
	})
}
//字符转换,截取指定字符长度
function interceptStr(str, len) {
    var reg = /[\u4e00-\u9fa5]/g,    //专业匹配中文
        slice = str.substring(0, len),
        chineseCharNum = (~~(slice.match(reg) && slice.match(reg).length)),
        realen = slice.length * 2 - chineseCharNum;
    return str.substr(0, realen) + (realen < str.length ? "..." : "");
}
function drawTable(pageInfo) {
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
    var startSort = pageInfo.startRow; //开始序号
    var trs = "";
    var type = "";
    var status = "";
    for (var i = 0; i < list.length; i++) {
        var meta = list[i];
        var sortNum = startSort + i;
        if (meta.taskStatus == 12) {
            status = "待处理";
        }
        if (meta.taskStatus == -2) {
            status = "等待加签结束";
        }
        var agentOdate = new Date(meta.taskInitDate);
        var InitDate = common.dateToString(agentOdate);
        var insTitle = "";
        if(meta.dhProcessInstance.insTitle!=null){
        	insTitle = interceptStr(meta.dhProcessInstance.insTitle,15);
        }
        var taskTitle = "";
        if(meta.taskTitle != null){
        	taskTitle = interceptStr(meta.taskTitle,15);
        }
        trs += '<tr>'
        	+'<td>'
        	+'<input name = "checkTaskIns" type = "checkbox" onclick="invertSelection(this)" value = "'+meta.taskUid+'"/>'
            +sortNum 
            +'</td>'
            +'<td>' 
            +meta.dhProcessInstance.proName
            +'</td>'
            +'<td title = \''+meta.dhProcessInstance.insTitle+'\'>'
            + insTitle
            +'</td>'
            +'<td title = \''+meta.taskTitle+'\'>'
            +taskTitle 
            +'</td>' 
            +'<td>' 
            +meta.taskHandler 
            +'</td>'
            +'<td>';
        if(meta.taskAgentUserName!=null && meta.taskAgentUserName!=""){
        	trs += meta.taskAgentUserName;
        }
        trs += '</td>' 
            +'<td>' 
            +status 
            +'</td>'
            +'<td>';
        if (meta.taskPreviousUsrUsername != null && meta.taskPreviousUsrUsername != "") {
            trs += meta.taskPreviousUsrUsername;
        }
        trs += '</td>' 
            +'<td>';
        if (meta.sysUser.userName != null && meta.sysUser.userName != "") {
            trs += meta.sysUser.userName;
        }
        trs += '</td>' 
            +'<td>' 
            +InitDate 
            +'</td>' 
            +'</tr>';
    }
    $("#proMet_table_tbody").append(trs);

}
//模糊查询
function search(){
	
}

//反选
function invertSelection(a){ 
	var checkeNodes= $("input[type='checkbox'][name='checkTaskIns']"); 
	var checkedNodes= $("input[type='checkbox'][name='checkTaskIns']:checked");
	
	if(checkeNodes.length == checkedNodes.length){
		$("#checked-All-ins").prop("checked",true);
	}else{
		$("#checked-All-ins").prop("checked",false);
	}

};

//显示移交操作框
function showTurnTaskIns(obj){
	var operation = $(obj).val();
	$("#task-trun-operation").val(operation);
	$("#task-trun-title").empty();
	if(operation=="byBatch"){
		$("#task-trun-title").text("批量移交")
	}else if(operation=="byAll"){
		$("#task-trun-title").text("全部移交")
	}else{
		return;
	}
	$("#task-trun-off-div").show();
}

//提交任务移交
function trunOffTaskIns(){
	var operation = $("#task-trun-operation").val();
	trunOffTaskInsOperation.operation = operation;
	trunOffTaskInsOperation.trunOffTaskIns();
}
//检查批量移交任务是否选中要移交的任务
function checkTurnBatchTaskIns(obj){
	var checkedNodes= $("input[type='checkbox'][name='checkTaskIns']:checked");
	if(checkedNodes.length<=0){
		layer.alert("请选择要移交的任务");
		return;
	}else{
		showTurnTaskIns(obj);
	}
}
//提交任务移交处理
var trunOffTaskInsOperation = {
		operation : "",
		trunOffTaskIns : function trunOffTaskIns(){
			if(trunOffTaskInsOperation.operation=="byBatch"){
				trunOffTaskInsOperation.trunTaskInsByBatch();
			}else if(trunOffTaskInsOperation.operation=="byAll"){
				trunOffTaskInsOperation.trunTaskInsByAll();
			}
		},
		trunTaskInsByBatch : function trunTaskInsByBatch(){
			
			//批量移交任务
			var checkedNodes= $("input[type='checkbox'][name='checkTaskIns']:checked");
			var checkedTaskUidList = new Array();
			for (var i = 0; i < checkedNodes.length; i++) {
				checkedTaskUidList.push($(checkedNodes[i]).val());
			}
			if(checkedTaskUidList.length>0){
				var targetUserUid = $("#targetUser").val();
				var trunOffCause = $("#trunOffCause").val();
				var sourceUserUid = $("#userId").val().split(";")[0];//第一次加载，没有分号，选人后有分号
				if(targetUserUid!=null && targetUserUid.length >0){
					targetUserUid = targetUserUid.substring(0,targetUserUid.length-1);
				$.ajax({
					url: common.getPath() + "/dhTaskInstanceTurn/batchTurnTaskInstanceByUser",
					type: "post",
					dataType: "json",
					data: {
						"dhTaskUidList":checkedTaskUidList,
						"sourceUserUid":sourceUserUid,
						"targetUserUid":targetUserUid,
						"turnTaskCause":trunOffCause
					},
					traditional: true,
					beforeSend:function(){
						layer.load(1);
					},
					success: function (result) {
						if (result.status == 0) {
							getTaskInstanceByPage();
						}
						layer.alert(result.msg);
						layer.closeAll("loading");
					},
					error: function () {
						layer.alert('操作失败');
						layer.closeAll("loading");
					}
				});
				}else{
					layer.alert("请选择任务的接收人")
				}
			}else{
				layer.alert("请选择要移交的任务");
			}
		},
		trunTaskInsByAll : function trunTaskInsByAll(){
			//移交用户所有任务
			var sourceUserUid = $("#userId").val().split(";")[0];//第一次加载，没有分号，选人后有分号
			var targetUserUid = $("#targetUser").val();
			var trunOffCause = $("#trunOffCause").val();
			if(targetUserUid!=null && targetUserUid.length >0){
				targetUserUid = targetUserUid.substring(0,targetUserUid.length-1);
				$.ajax({
					url:common.getPath() + "/dhTaskInstanceTurn/allTurnTaskInstanceByUser",
					type:"post",
					dataType:"json",
					data:{
						"sourceUserUid":sourceUserUid,
						"targetUserUid":targetUserUid,
						"turnTaskCause":trunOffCause
					},
					beforeSend:function(){
						layer.load(1);
					},
					success:function(result){
						if (result.status == 0) {
							getTaskInstanceByPage();
						}
						layer.alert(result.msg);
						layer.closeAll("loading");
					},
					error:function(){
						layer.alert('操作失败');
						layer.closeAll("loading");
					}
				});
			}else{
				layer.alert("请选择任务的接收人")
			}
		}
}

