$(function(){
	// 选择用户
	$("#choose_handle_user").click(function() {
		common.chooseUser('userId', 'true');
	});
	$("#choose_target_user").click(function(){
		common.chooseUser('targetUser', 'true');
	});
	// 加载数据
	getClosedTaskInstanceByPage();
	//全选
	 $("#checked-All-ins").click(function(){ 
		var checkeNodes= $("input[type='checkbox'][name='checkTaskIns']");
	    checkeNodes.prop("checked",$(this).prop("checked")); 
	});
});
layui.use('laydate', function(){
	var laydate = layui.laydate;
	  	laydate.render({
	    elem: '#taskInitDate-search',
	    type: 'datetime',
	    range: true
	});
});

//设置用户标签
function setLabelUserView(a){
	$("#labelUserView").empty();
	var userView = $(a).val();
	if(userView != null ){
		userView = userView.substring(0,userView.length-1);
	}
	$("#labelUserView").text(userView+"的待办任务");
	
}

//为翻页提供支持
var pageConfig = {
	pageNum : 1,
	pageSize : 8,
	usrUid : "",
	isAgent : "",
	startTime : null,
	endTime : null,
	proName : "",
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
					getClosedTaskInstanceByPage();
				}
			}
		});
	});
}
function getClosedTaskInstanceByPage(){
	
	$.ajax({
		url : common.getPath()+'/dhTaskInstanceTurn/getClosedTaskInstanceByPage',
		type : 'post',
		dataType : 'json',
		data : {
			pageNum : pageConfig.pageNum,
			pageSize : pageConfig.pageSize,
			usrUid : pageConfig.usrUid,
			isAgent : pageConfig.isAgent,
			startTime : pageConfig.startTime,
			endTime : pageConfig.endTime,
			proName : pageConfig.proName
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
function drawTable(pageInfo,data){
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
	var startSort = pageInfo.startRow;//开始序号
	var trs = "";
	var type = "";
	for (var i = 0; i < list.length; i++) {
		var meta = list[i];
		var sortNum = startSort + i;
		var agentOdate = new Date(meta.taskInitDate);
		var InitDate = common.dateToString(agentOdate);
		
		var finishDate = "";
		if(meta.taskFinishDate!=null && meta.taskFinishDate!=""){
			var agentOdate1 = new Date(meta.taskFinishDate);
			finishDate = common.dateToString(agentOdate1);
		}
		var insTitle = "";
        if(meta.dhProcessInstance.insTitle!=null){
        	insTitle = interceptStr(meta.dhProcessInstance.insTitle,15);
        }
        var taskTitle = "";
        if(meta.taskTitle != null){
        	taskTitle = interceptStr(meta.taskTitle,15);
        }
		trs += '<tr>' 
			+ '<td>' + sortNum 
			+'<input name = "checkTaskIns" type = "checkbox" onclick="invertSelection(this)" value = "'+meta.taskUid+'"/>'
			+'</td>'
			+ '<td>' 
            + meta.dhProcessInstance.proName 
			+ '</td>'
			+ '<td title = \''+meta.dhProcessInstance.insTitle+'\'>'
			+insTitle
			+'</td>'
			+ '<td title = \''+meta.taskTitle+'\'>'
			+ taskTitle
			+ '</td>' 
			+ '<td>' 
			+ meta.taskHandler
            + '</td>' 
			+ '<td>';
		if(meta.taskAgentUserName!=null && meta.taskAgentUserName!=""){
           	trs += meta.taskAgentUserName;
        }
		trs += '</td><td>';
		if(meta.taskPreviousUsrUsername!=null && meta.taskPreviousUsrUsername!=""){
			trs += meta.taskPreviousUsrUsername;
		}
		trs += '</td><td>';
		if(meta.sysUser.userName!=null && meta.sysUser.userName!=""){
			trs += meta.sysUser.userName;
		}
		trs += '</td>'
			+ '<td>'
			+ InitDate
			+'</td>' 
			+'<td>'
			+finishDate
			+'</td>'
			+ '</tr>';
	}
	$("#proMet_table_tbody").append(trs);
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
					getClosedTaskInstanceByPage();
				}
			}
		});
	});
}
//模糊查询
function search(){
	pageConfig.startTime = $("#taskInitDate-search").val().split(" - ")[0];
	pageConfig.endTime = $("#taskInitDate-search").val().split(" - ")[1];
	pageConfig.proName = $("#task-proName-search").val();
	pageConfig.usrUid = $("#userId").val().split(";")[0];//第一次加载，没有分号，选人后有分号
	getClosedTaskInstanceByPage();
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
		$("#task-trun-title").text("批量抄送")
	}else if(operation=="byAll"){
		$("#task-trun-title").text("抄送全部")
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
			
			//批量抄送任务
			var checkedNodes= $("input[type='checkbox'][name='checkTaskIns']:checked");
			var checkedTaskUidList = new Array();
			for (var i = 0; i < checkedNodes.length; i++) {
				checkedTaskUidList.push($(checkedNodes[i]).val());
			}
			if(checkedTaskUidList.length>0){
				var targetUserUid = $("#targetUser").val();
				var sourceUserUid = $("#userId").val().split(";")[0];//第一次加载，没有分号，选人后有分号
				if(targetUserUid!=null && targetUserUid.length >0){
					targetUserUid = targetUserUid.substring(0,targetUserUid.length-1);
				$.ajax({
					url: common.getPath() + "/dhTaskInstanceTurn/batchTransferTaskInstanceByUser",
					type: "post",
					dataType: "json",
					data: {
						"dhTaskUidList":checkedTaskUidList,
						"sourceUserUid":sourceUserUid,
						"targetUserUid":targetUserUid
					},
					traditional: true,
					beforeSend:function(){
						layer.load(1);
					},
					success: function (result) {
						if (result.status == 0) {
							getClosedTaskInstanceByPage();
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
				layer.alert("请选择要抄送的任务");
			}
		},
		trunTaskInsByAll : function trunTaskInsByAll(){
			//抄送用户所有任务
			var sourceUserUid = $("#userId").val().split(";")[0];//第一次加载，没有分号，选人后有分号
			var targetUserUid = $("#targetUser").val();
			pageConfig.startTime = $("#taskInitDate-search").val().split(" - ")[0];
			pageConfig.endTime = $("#taskInitDate-search").val().split(" - ")[1];
			pageConfig.proName = $("#task-proName-search").val();
			pageConfig.usrUid = $("#userId").val().split(";")[0];//第一次加载，没有分号，选人后有分号
			
			if(targetUserUid!=null && targetUserUid.length >0){
				targetUserUid = targetUserUid.substring(0,targetUserUid.length-1);
				layer.confirm("抄送全部数据量过大,颇为耗时,是否继续？", function () {
				$.ajax({
					url:common.getPath() + "/dhTaskInstanceTurn/allTransferTaskInstanceByUser",
					type:"post",
					dataType:"json",
					data:{
						"sourceUserUid":sourceUserUid,
						"targetUserUid":targetUserUid,
						"isAgent" : pageConfig.isAgent,
						"startTime" : pageConfig.startTime,
						"endTime" : pageConfig.endTime,
						"proName" : pageConfig.proName,
						"usrUid" : pageConfig.usrUid
					},
					beforeSend:function(){
						layer.load(1);
					},
					success:function(result){
						if (result.status == 0) {
							getClosedTaskInstanceByPage();
						}
						layer.alert(result.msg);
						layer.closeAll("loading");
					},
					error:function(){
						layer.alert('操作失败');
						layer.closeAll("loading");
					}
				});
				});
			}else{
				layer.alert("请选择任务的接收人")
			}
		}
}

