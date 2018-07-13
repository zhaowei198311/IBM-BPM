// bool值记录提交时是否需要填写审批意见
var needApprovalOpinion = "";
// bool值记录是否能直接跳跃提交 回到驳回处
var canSkipFromReject = false;

var pageConfig = {
	pageNum:1,
	pageSize:14
}

var form = null;
$(function () {
	needApprovalOpinion = $('#needApprovalOpinion').val() == 'true';
	if ($('#skipFromReject_newTaskOwnerName').val() && $('#skipFromReject_targetNodeName').val()) {
	    canSkipFromReject = true;
	}
	layui.use(['form'], function () {
        form = layui.form;
    });
    common.giveFormSetValue($("#formData").text());
    var fieldPermissionInfo = $("#fieldPermissionInfo").text();
    common.giveFormFieldPermission(fieldPermissionInfo);
    form.render();
    
    userAsync();
    
    departAsync();
    
    checkUserData();
    //点击通过
    $("#submit_btn").click(function(){
    	checkUserData();
    });
    
    //加载已上传的附件
    loadFileList();
});

//用于异步加载数据的参数
var asyncActcChooseableHandlerType = "";
var asyncActivityId = "";
var elementId = "";
var eleObj = "";

//用于关闭和开启选择页面的变量
var openChooseFlag = true;
var index = "";

//用户异步加载
function userAsync() {
	var start = $('#choose_user_table table').offset().top; ;
	var move = 0;
	$("#choose_user_table").scroll(function(){
		var end = $('#choose_user_table table').offset().top; 
		if(end<start){
			move = start-end;
			if(move >= 240){
				if(pageConfig.pageNum==1){
					pageConfig.pageNum = pageConfig.pageNum+2;
				}else{
					pageConfig.pageNum ++;
				}
				pageConfig.pageSize = 7;
				if(elementId!="" && elementId!=null){
					if(elementId=="submit_table"){
						queryUserByActc(asyncActcChooseableHandlerType,asyncActivityId,elementId,true,eleObj);
					}else{
						asyncActcChooseableHandlerType = "allUser";
						queryUserByActc(asyncActcChooseableHandlerType,asyncActivityId,elementId,true,eleObj);
					}
				}else{
					asyncActcChooseableHandlerType = "allUser";
					queryUserByActc(asyncActcChooseableHandlerType,asyncActivityId,elementId,false,eleObj);
				}
				start = end;
			}
		}else{
			start = end;
		}
	});
}

//通过选择下个环节可选处理人的方法
function getConductor(activityId, actcCanChooseUser, actcAssignType,actcChooseableHandlerType) {
	if (actcCanChooseUser == 'FALSE') {
        return false;
    }
	asyncActcChooseableHandlerType = actcChooseableHandlerType;
	asyncActivityId = activityId;
	elementId = "submit_table";
	queryUserByActc(actcChooseableHandlerType,activityId,elementId,true,eleObj);
	$(".choose_head").css("display","none");
	$("#choose_user_search_div").css("display","block");
	$(".choose_body").css("display","none");
	$("#choose_user_table").css("display","block");
	openChooseUserDiv();
}

//全局选人的方法
function getUser(obj,isMulti,id){
	eleObj = obj;
	elementId = id;
	jQuery('html,body').animate({
	    scrollTop: $(obj).parent().offset().top-50
	}, 300);
	activityId = $("#activityId").val();
	queryUserByActc("allUser",activityId,elementId,isMulti,eleObj);
	$(".choose_head").css("display","none");
	$("#choose_user_search_div").css("display","block");
	$(".choose_body").css("display","none");
	$("#choose_user_table").css("display","block");
	openChooseUserDiv();
}

//根据条件查询用户列表
function queryUserByActc(actcChooseableHandlerType,activityId,elementId,isMulti,obj){
	var userUidArrStr = "";
	if(elementId!="" && elementId!=null){
		var userObjArr = $("#"+elementId+" .delete_choose_user");
		userObjArr.each(function(){
			var userUid = $(this).attr("value");
			userUidArrStr += userUid+";"
		});
	}else{
		var userUid = $(obj).parent().find("input[type='hidden']").val();
		userUidArrStr += userUid+";"
	}
	
	if(actcChooseableHandlerType!='allUser'){
		var insUid=$("#insUid").val();
		var formData=$("#formData").text();
		var companyNum=$("#companyNum").val();
		var departNo=$("#departNo").val();
		var taskUid=$('#taskUid').val();
		$.ajax({
			type:'post',
			url:'dhRoute/choosableHandlerMove',
			beforeSend:function(){
				layer.load(1);
			},
			data:{
			    'insUid': insUid,
                'companyNum': companyNum,
                'departNo': departNo,
                'activityId': activityId,
                'formData': formData,
                'taskUid': taskUid,
                'userUidArrStr':userUidArrStr,
                'condition':$("#search_user_input").val()
			},
			dataType:'json',
			success: function (result){
				$("#choose_user_tbody").empty();
				if(result.status==0){
					drawChooseUserTable(result.data,isMulti,elementId,obj);
				}else{
					$("#choose_user_tbody").append("<th colspan='2'>未找到符合条件的数据</th>");
				}
				layer.closeAll("loading");
			},
			error:function(){
				layer.closeAll("loading");
			}
		});	
	}else{
		$.ajax({
			url:"sysUser/allSysUserMove",
			beforeSend:function(){
				layer.load(1);
			},
			method:'post',
			data:{
				pageNo:pageConfig.pageNum,
				pageSize:pageConfig.pageSize,
				userUidArrStr:userUidArrStr,
                condition:$("#search_user_input").val()
			},
			success:function(result){
				if(pageConfig.pageNum==1){
					$("#choose_user_tbody").empty();
				}
				if(result.status==0){
					drawChooseUserTable(result.data.list,isMulti,elementId,obj);
				}else{
					$("#choose_user_tbody").append("<th colspan='2'>未找到符合条件的数据</th>");
				}
				layer.closeAll("loading");
			},
			error:function(){
				layer.closeAll("loading");
			}
		});
	}
}

//模糊查询选人的方法
function searchChooseUser(){
	pageConfig.pageNum = 1;
	pageConfig.pageSize = 14;
	if(elementId!="" && elementId!=null){
		if(elementId=="submit_table"){
			queryUserByActc(asyncActcChooseableHandlerType,asyncActivityId,elementId,true,eleObj);
		}else{
			asyncActcChooseableHandlerType = "allUser";
			queryUserByActc(asyncActcChooseableHandlerType,asyncActivityId,elementId,true,eleObj);
		}
	}else{
		asyncActcChooseableHandlerType = "allUser";
		queryUserByActc(asyncActcChooseableHandlerType,asyncActivityId,elementId,false,eleObj);
	}
}

//渲染选人的表格
function drawChooseUserTable(data,isMulti,elementId,obj){
	eleObj = obj;
	if(data.length==0){
		$("#choose_user_tbody").append("<th colspan='3'>未找到符合条件的数据</th>");
	}else{
		var trHtml = "";
		for(var i=0;i<data.length;i++){
			var item = data[i];
			trHtml += "<tr onclick='clickUserFun(this,"+isMulti+",\""+elementId+"\");'>"
				+"<td>"+item.userUid+"</td>"
				+"<td>"+item.userName+"</td>"
				+"</tr>";
		}
		$("#choose_user_tbody").append(trHtml);
	}
}

//选择具体数据字典分类的数据内容
function chooseDicData(obj) {
	eleObj = obj;
	jQuery('html,body').animate({
	    scrollTop: $(obj).parent().offset().top-50
	}, 300);
	var dicUid = $(obj).parent().find("input[type='text']").attr("database_type");
	var dicDataUid = $(obj).parent().find("input[type='hidden']").val();
	$.ajax({
		url: "sysDictionary/listOnDicDataBydicUidMove",
		method: "post",
		async:false,
		data: {
			dicDataUid:dicDataUid,
			dicUid: dicUid,
			condition:$("#search_value_input").val()
		},
		success: function(result){
			$("#choose_value_tbody").empty();
			if(result.status==0){
				drawDicDataTable(result.data);
				$(".choose_head").css("display","none");
				$("#choose_value_search_div").css("display","block");
				$(".choose_body").css("display","none");
				$("#choose_value_table").css("display","block");
				if(openChooseFlag){
					openChooseFlag = false;
					openChooseUserDiv();
				}
			}else{
				$("#choose_value_tbody").append("<th colspan='2'>未找到符合条件的数据</th>");
			}
		}
	});
}

//模糊查询数据字典表格数据的方法
function searchDicData(){
	chooseDicData(eleObj);
}

//渲染选择数据字典的表格
function drawDicDataTable(data){
	if(data.length==0){
		$("#choose_value_tbody").append("<th colspan='3'>未找到符合条件的数据</th>");
	}else{
		var trHtml = "";
		for(var i=0;i<data.length;i++){
			var item = data[i];
			trHtml += "<tr onclick='clickValueFun(this);' value='"+item.dicDataUid+"'>"
				+"<td>"+item.dicDataName+"</td>";
			if(item.dicDataDescription!=null && item.dicDataDescription!=""){
				trHtml += "<td>"+item.dicDataDescription+"</td>";
			}else{
				trHtml += "<td></td>";
			}
			trHtml += "</tr>";
		}
		$("#choose_value_tbody").append(trHtml);
	}
}

//点击数据字典表格行
function clickValueFun(obj){
	var dicDataUid = $(obj).attr("value");
	var dicDataName = $(obj).find("td:eq(0)").text().trim();
	$(obj).css("display","none");
	$(eleObj).parent().find("input[type='text']").val(dicDataName);
	$(eleObj).parent().find("input[type='hidden']").val(dicDataUid);
	chooseDicData(eleObj);
	layer.close(index);
}

//选择部门的分页条件
var pageConfigDepart = {
	pageNum:1,
	pageSize:14
}

//动态选部门的方法
function desChooseDepart(obj) {
	eleObj = obj;
	jQuery('html,body').animate({
	    scrollTop: $(obj).parent().offset().top-50
	}, 300);
	var departUid = $(obj).parent().find("input[type='hidden']").val();
	$.ajax({
		url:"sysDepartment/allSysDepartmentMove",
		method:"post",
		data:{
			pageNo:pageConfigDepart.pageNum,
			pageSize:pageConfigDepart.pageSize,
			departUid:departUid,
			condition:$("#search_depart_input").val()
		},
		success:function(result){
			if(pageConfigDepart.pageNum==1){
				$("#choose_depart_tbody").empty();
			}
			if(result.status==0){
				drawChooseDepartTable(result.data.list);
				$(".choose_head").css("display","none");
				$("#choose_depart_search_div").css("display","block");
				$(".choose_body").css("display","none");
				$("#choose_depart_table").css("display","block");
				if(openChooseFlag){
					openChooseFlag = false;
					openChooseUserDiv();
				}
			}else{
				$("#choose_depart_tbody").append("<th colspan='3'>未找到符合条件的数据</th>");
			}
		}
	});
}

//渲染选择部门的表格
function drawChooseDepartTable(data){
	if(data.length==0){
		$("#choose_depart_tbody").append("<th colspan='3'>未找到符合条件的数据</th>");
	}else{
		var trHtml = "";
		for(var i=0;i<data.length;i++){
			var item = data[i];
			trHtml += "<tr onclick='clickDepartFun(this);' value='"+item.dicDataUid+"'>"
				+"<td>"+item.departUid+"</td>"
				+"<td>"+item.departName+"</td>"
				+"</tr>"; 
		}
		$("#choose_depart_tbody").append(trHtml);
	}
}

//选择部门的行点击事件
function clickDepartFun(obj){
	var departUid = $(obj).find("td:eq(0)").text().trim();
	var departName = $(obj).find("td:eq(1)").text().trim();
	$(obj).css("display","none");
	$(eleObj).parent().find("input[type='text']").val(departName);
	$(eleObj).parent().find("input[type='hidden']").val(departUid);
	desChooseDepart(eleObj);
	layer.close(index);
}

//模糊查询部门的方法
function searchChooseDepart(obj){
	pageConfigDepart.pageNum = 1;
	pageSize = 14;
	desChooseDepart(eleObj);
}

//部门异步加载
function departAsync() {
	var start = $('#choose_depart_table table').offset().top; ;
	var move = 0;
	$("#choose_depart_table").scroll(function(){
		var end = $('#choose_depart_table table').offset().top; 
		if(end<start){
			move = start-end;
			if(move >= 240){
				if(pageConfigDepart.pageNum==1){
					pageConfigDepart.pageNum = pageConfigDepart.pageNum+2;
				}else{
					pageConfigDepart.pageNum ++;
				}
				pageConfigDepart.pageSize = 7;
				desChooseDepart(eleObj);
				start = end;
			}
		}else{
			start = end;
		}
	});
}

//打开选择层
function openChooseUserDiv(){
	//页面层
	index = layer.open({
		type: 1
		,content: $("#choose_div")
		,offset: 'b'
		,title: false
    	,shadeClose: true
    	,closeBtn:0
    	,shade: 0.3
    	,anim:2
    	,resize:false
    	,area: ['width:100%', '300px']
		,success:function(){
			
		}
		,end:function(){
			pageConfig.pageNum = 1;
			pageConfig.pageSize = 14;
			openChooseFlag = true;
			$("#choose_div").css("display","none");
			$("#search_depart_input").val("");
			$("#search_value_input").val("");
			$("#search_user_input").val("");
		}
	});
}

//点击行
function clickUserFun(obj,isMulti,elementId){
	var userUid = $(obj).find("td:eq(0)").text().trim();
	var userName = $(obj).find("td:eq(1)").text().trim();
	var liHtml = '<li><div class="choose_user_name_span">'+userName
		+'</div><span><i class="layui-icon delete_choose_user" value="'+userUid
		+'" onclick="deleteAssembleUser(this);">&#x1007;</i></span></li>';
	if(isMulti){
		$("#"+elementId+" .choose_user_name_ul ul").append(liHtml);
		if(asyncActcChooseableHandlerType=='allUser'){
			pageConfig.pageNum = $(obj).parent().find("tr").length+1;
			pageConfig.pageSize = 1;
			queryUserByActc(asyncActcChooseableHandlerType,asyncActivityId,elementId,true,eleObj);
		}
	}else{
		pageConfig.pageNum = 1;
		pageConfig.pageSize = 14;
		$(eleObj).parent().find("input[type='text']").val(userName);
		$(eleObj).parent().find("input[type='hidden']").val(userUid);
		asyncActcChooseableHandlerType = "allUser";
		queryUserByActc(asyncActcChooseableHandlerType,asyncActivityId,elementId,false,eleObj);
		layer.close(index);
	}
	$(obj).css("display","none");
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
	$("#reject_btn").click(function (){
		queryRejectByActivitiy();
	});
	
    // 查询审批进度剩余进度百分比
    var activityId = $("#activityId").val();
    var taskUid = $("#taskUid").val();
    $.ajax({
        async: false,
        url: common.getPath() + "/taskInstance/queryProgressBar",
        type: "post",
        dataType: "json",
        data: {
        	activityId: activityId,
            taskUid: taskUid
        },
        success: function (data) {
            var result = data.data;
            // 剩余时间
            var hour = result.hour;
            // 剩余时间百分比
            var percent = result.percent;
            if (data.status == 0) {
                if (hour == -1) {
                    $(".layui-progress").append('<span class="progress_time">审批已超时</span>');
                    $(".progress_time").css('right', '1%');
                } else {
                    $(".layui-progress").append('<span class="progress_time">审批剩余时间' + hour + '小时</span>');
                    $(".progress_time").css('right', '1%');                
                }
                // 加载进度条
                layui.use('element', function () {
                    var $ = layui.jquery,
                        element = layui.element; //Tab的切换功能，切换事件监听等，需要依赖element模块
                    // 延迟加载
                    setTimeout(function () {
                    	if (percent > 50) {
                            $('.layui-progress-bar').css('background-color', '#FFFF33');
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

//提交按钮的触发事件
function submitTaskByHandleType(){
	$(".handle_table").each(function(){
		var display = $(this).css("display");
		if(display=="block"){
			var id = $(this).prop("id");
			switch(id){
				case "submit_table":{
					if (canSkipFromReject) {
				        skipFromReject();
				    } else {
				        submitTask();
				    }
					break;
				}
				case "countersign_table":{
					addSure();
					break;
				}
				case "reject_table":{
					rejectSure();
					break;
				}
				case "transfer_table":{
					transferSure();
					break;
				}
			}
		}
	});
}

// 提交通过任务的方法
function submitTask() {
    var taskId = $("#taskId").val();
    var taskUid = $("#taskUid").val();
    var insUid = $("#insUid").val();//流程实例id--ins_uid
    var departNo = $("#departNo").val();
    var companyNumber = $("#companyNum").val();
    var aprOpiComment = $("#myApprovalOpinion").val();//审批意见
    if (departNo==null || departNo=="" || companyNum=="" || companyNum==null) {
    	layer.alert("缺少流程发起人信息");
    	return;
    }
    // 获取审批意见
    var aprOpiComment = $("#myApprovalOpinion").val();
    console.log($("#myApprovalOpinion").val());
    if(needApprovalOpinion && (aprOpiComment == null || aprOpiComment == "" || aprOpiComment == undefined)){
        alertNeedApprovalOpinion();
    	return;
    }else{
    	aprOpiComment = aprOpiComment.replace('/\n|\r\n/g',"<br>"); 
    }
    // 校验标题有没有填写
    if (!checkInsTitle()) {
    	$(".mobile_menu li").css({"color":"#A0A0A0"});
    	$(".mobile_menu #form_content").parent().css({"color":"#009688"});
    	var id = $(".mobile_menu #form_content").attr("title");
    	$(".middle_content").css("display","none");
    	$("#"+id+"_div").css("display","block");
        layer.alert("流程标题过长或未填写");
        return
    }
    //必填项验证，勿删
    if(!common.validateFormMust("startProcess_btn")){
    	return;
    }
    // 发起流程             
    var finalData = {};
    // 表单数据
    var jsonStr = common.getDesignFormData();
    var formData = JSON.parse(jsonStr);
    
    finalData.formData = formData;
    // 流程数据
    var processData = {};
    processData.insUid = $("#insUid").val();
    processData.insTitle = $("#insTitle_input").val().trim();
    finalData.processData = processData;
    
    var handleUserIds = "";
    $("#submit_table .delete_choose_user").each(function(){
    	handleUserIds += $(this).attr("value")+";";
    });
    
    // 路由数据
    var routeData = [];
    var lackNextOwner = false;
    $(".getUser").each(function () {
        var item = {};
        item.activityId = $(this).attr('id');
        item.userUid = handleUserIds;
        console.log(item.userUid);
        if (item.userUid == null || item.userUid == undefined || item.userUid.trim() == '') {
            lackNextOwner = true;
        }
        item.assignVarName = $(this).data("assignvarname");
        item.signCountVarName =  $(this).data("signcountvarname");
        item.loopType = $(this).data("looptype");
        routeData.push(item);
    });
    if(lackNextOwner){
    	layer.alert('提交失败,未配置下一环节处理人');
    	return;
    }
    finalData.routeData = routeData;
    finalData.taskData = {"taskId":taskId,"taskUid":taskUid};
    finalData.approvalData = {"aprOpiComment":aprOpiComment};
    $.ajax({
        url: 'taskInstance/finshedTask',
        type: 'POST',
        dataType: 'json',
        data: {
            data: JSON.stringify(finalData)
        },
        beforeSend: function () {
            layer.load(1);
        },
        success: function (result) {
        	layer.closeAll('loading'); 
            if (result.status == 0) {
                layer.alert('提交成功', function(){
                	window.history.back();
                });
            }else{
                layer.alert(result.msg);
            }
            $(".display_container").css("display","none"); 
        },
        error: function (result) {
        	layer.closeAll('loading'); 
            layer.alert('提交失败');
        }
    });
}

//提交至驳回节点的方法
function skipFromReject() {
    var taskUid = $("#taskUid").val();
    // 获取审批意见
    var aprOpiComment = $("#myApprovalOpinion").val();
    if(needApprovalOpinion && (aprOpiComment == null || aprOpiComment == "" || aprOpiComment == undefined)){
        alertNeedApprovalOpinion();
        return;
    }else{
        aprOpiComment = aprOpiComment.replace('/\n|\r\n/g',"<br>");
    }
	// 校验标题有没有填写
    if (!checkInsTitle()) {
    	$(".mobile_menu li").css({"color":"#A0A0A0"});
    	$(".mobile_menu #form_content").parent().css({"color":"#009688"});
    	var id = $(".mobile_menu #form_content").attr("title");
    	$(".middle_content").css("display","none");
    	$("#"+id+"_div").css("display","block");
        layer.alert("流程标题过长或未填写");
        return
    }
    //必填项验证，勿删
    if(!common.validateFormMust("startProcess_btn")){
    	return;
    }
    // 发起流程
    var finalData = {};
    // 表单数据
    var formData = JSON.parse(common.getDesignFormData());
    // 流程数据
    var processData = {};
    processData.insUid = $("#insUid").val();
    processData.insTitle = $("#insTitle_input").val().trim();
    finalData.processData = processData;
    finalData.formData = formData;
    finalData.taskData = {"taskUid": taskUid};
    finalData.approvalData = {"aprOpiComment": aprOpiComment};

    $.ajax({
        url: 'taskInstance/skipFromReject',
        type: 'post',
        dataType: 'json',
        data: {
            'data': JSON.stringify(finalData)
        },
        beforeSend: function () {
            layer.load(1);
        },
        success: function(result) {
            layer.closeAll('loading');
            if (result.status == 0) {
                layer.alert('提交成功', function(){
                    window.history.back();
                });
            }else{
                layer.alert(result.msg);
            }
            $(".display_container").hide();
        },
        error: function () {
            layer.closeAll('loading');
            layer.alert('提交失败');
        }
    });
}

// 点击“驳回”按钮触发的动作
function queryRejectByActivitiy() {
    var activityId = $("#activityId").val();
    var insUid = $("#insUid").val();
    var aprOpiComment = $("#myApprovalOpinion").val();//审批意见
    $.ajax({
        url: 'processInstance/queryRejectByActivity',
        type: 'POST',
        dataType: 'json',
        data: {
        	activityId : activityId,
        	insUid : insUid
        },
        beforeSend: function () {
            index = layer.load(1);
        },
        success: function (result) {
        	if(result.status==0){
        		$("#reject_table table").empty();
            	var rejectMapList = result.data;
            	var rejectDiv = "";
            	for(var i=0;i<rejectMapList.length;i++){
            		rejectDiv += '<tr>'
								+'<th>驳回至：</th>'
								+'<td>'
								+'<span style="float:left;width:10%;">'
								+'<input type="checkbox" name="check" lay-skin="primary" '
								+'value="'+rejectMapList[i].insId+'+'+rejectMapList[i].activityBpdId
									+'+'+rejectMapList[i].userId+'"/>'
								+'</span> '
								+'<span style="float:left;width:80%;">'
								+rejectMapList[i].activityName+'————审批人:'+rejectMapList[i].userName
								+'</span>'
								+'</td>'
								+'</tr>';
                }//end for
            	$("#reject_table table").append(rejectDiv);
            	form.render();
        	}
        	layer.close(index);
        },
        error: function (result) {
            layer.close(index);
            layer.alert('查询驳回环节失败', {
                icon: 2
            });
        }
    });
}

function rejectSure(){
	$('input[name="check"]:checked').each(function(){ 
		var aprOpiComment = $("#myApprovalOpinion").val();
	    var taskId = $("#taskId").val();
	    var taskUid = $("#taskUid").val();
	    // 获取审批意见
	    var aprOpiComment = $("#myApprovalOpinion").val();
	    if(needApprovalOpinion && (aprOpiComment == null || aprOpiComment == "" || aprOpiComment == undefined)){
	        alertNeedApprovalOpinion();
	        return;
	    }else{
	        aprOpiComment = aprOpiComment.replace('/\n|\r\n/g',"<br>");
	    }
		// 校验标题有没有填写
	    if (!checkInsTitle()) {
	    	$(".mobile_menu li").css({"color":"#A0A0A0"});
	    	$(".mobile_menu #form_content").parent().css({"color":"#009688"});
	    	var id = $(".mobile_menu #form_content").attr("title");
	    	$(".middle_content").css("display","none");
	    	$("#"+id+"_div").css("display","block");
	        layer.alert("流程标题过长或未填写");
	        return
	    }
	    //必填项验证，勿删
	    if(!common.validateFormMust("startProcess_btn")){
	    	return;
	    }
	    var finalData = {};
	    // 路由数据
	    var routeData = {};
	    var item = {};
	    var str = $(this).val();
	    var insId = str.substring(0,str.indexOf("+"));
	    var userUid = str.substring(str.lastIndexOf("+") + 1);
	    var activityBpdId = str.substring(str.indexOf("+")+1,str.lastIndexOf("+"));
	    var activityId = $("#activityId").val();
	    finalData.routeData = {"insId":insId,"userUid":userUid,"activityBpdId":activityBpdId};
	    finalData.taskData = {"taskId":taskId,"taskUid":taskUid,"activityId":activityId};
	    finalData.approvalData = {"aprOpiComment":aprOpiComment}; 
	    
	    $.ajax({
	        url: 'taskInstance/rejectTask',
	        type: 'POST',
	        dataType: 'json',
	        data: {
	        	data : JSON.stringify(finalData)
	        },
	        beforeSend: function () {
	            index = layer.load(1);
	        },
	        success: function (result) {
	            layer.close(index);
	        	if(result.status == 0){
	        		layer.msg('驳回成功', {
	        			  icon: 1,
	        			  time: 2000 //2秒关闭（如果不配置，默认是3秒）
	        			}, function(){
	        				window.history.back();
	        			});   
	        	}else{
	        		layer.alert('驳回失败', {
		                icon: 2
		            });
	        	}
	        },
	        error: function (result) {
	            layer.close(index);
	            layer.alert('驳回失败', {
	                icon: 2
	            });
	        }
	    });
	 }) 
}

// 加签确定
function addSure(){
	var taskUid = $("#taskUid").val();
	var taskId = $("#taskId").val();
	var insUid = $("#insUid").val();
	var handleUserIds = "";
	$("#countersign_table .delete_choose_user").each(function(){
		handleUserIds += $(this).attr("value")+";";
	});
	var usrUid = handleUserIds;
	var taskActivityId = $("#activityId").val();
	var taskType = $(".layui-form_1 option:selected").val();
	if (usrUid == null || usrUid == "") {
		layer.alert("请选择人员!");
		return;
	}
	$.ajax({
		async: false,
		url: common.getPath() + "/taskInstance/addSure",
		type: 'post',
		dataType: 'json',
		data: {
			taskUid: taskUid,
			insUid: insUid,
			taskId: taskId,
			taskActivityId: taskActivityId,
			usrUid: usrUid,
			taskType: taskType
		},
		success: function(data){
			if (data.status == 0) {
				layer.alert("操作成功!", function(){
                	window.history.back();
                });
			}else {
				layer.alert(data.msg);
			}
		}
	})
}

// 抄送确认
function transferSure(){
	var taskUid = $("#taskUid").val();
	var handleUserIds = "";
	$("#transfer_table .delete_choose_user").each(function(){
		handleUserIds += $(this).attr("value")+";";
	});
	var usrUid = handleUserIds;
	var activityId = $("#activityId").val();
	if (usrUid == null || usrUid == "") {
		layer.alert("请选择人员!");
		return;
	}
	$.ajax({
		async: false,
		url: common.getPath() + '/taskInstance/transferSure',
		type: 'post',
		dataType: 'json',
		data: {
			taskUid: taskUid,
			usrUid: usrUid,
			activityId: activityId
		},
		success: function(data){
			if (data.status == 0) {
				layer.alert("操作成功!");
				$('.display_container6').css("display","none");
			}else {
				layer.alert(data.msg);
			}
		}
	})
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
                shade: 0.3,
                area: ['790px', '580px'],
                content: result
            });
        }
    })
}

function back() {
	window.location.href = 'javascript:history.go(-1)';
}

//数据信息
var view = $(".container-fluid");
var form = null;


// 单击"提交"按钮
function checkUserData() {
    if (canSkipFromReject) {
        showRouteBarForSkipFromReject();
    } else {
        showRouteBar();
    }
}

// 为提交任务显示选人栏
function showRouteBar() {
    $.ajax({
        url:"dhRoute/showRouteBar",
        method:"post",
        data:{
            "taskUid": $("#taskUid").val(),
            "insUid":$("#insUid").val(),
            "activityId":$("#activityId").val(),
            "departNo": $("#departNo").val(),
            "companyNum": $("#companyNum").val(),
            "formData": common.getDesignFormData()
        },
        beforeSend: function(){
            layer.load(1);
        }
        ,
        success:function(result){
            if(result.status==0){
                $("#submit_table table").empty();
                var activityMetaList = result.data;
                var chooseUserDiv = "";
                if(activityMetaList.length==0){
                    chooseUserDiv += '<tr>'
                        +'<th class="approval_th">下一环节：</th>'
                        +'<td>流程结束</td>'
                        +'</tr>'
                        +'<tr>'
                        +'<th class="approval_th">处理人：</th>'
                        +'<td></td>'
                        +'</tr>';
                }else{
                    for(var i=0;i<activityMetaList.length;i++){
                        var activityMeta = activityMetaList[i];
                        chooseUserDiv += '<tr>'
                            +'<th class="approval_th">下一环节：</th>'
                            +'<td>'+activityMeta.activityName+'</td>'
                            +'</tr>'
                            +'<tr>'
                            +'<th class="approval_th">处理人：</th>'
                            +'<td>';
                        if(activityMeta.userName!=null && activityMeta.userName!=""){
                        	var userNameArr = activityMeta.userName.split(";");
                        	chooseUserDiv += "<div class='choose_user_name_ul'><ul>";
                        	for(var j=0;j<userNameArr.length;j++){
                        		var userName = userNameArr[j];
                        		var userUid = activityMeta.userUid.split(";")[j];
                        		if(userName != "" && userName != null){
                        			if(activityMeta.dhActivityConf.actcCanChooseUser=="FALSE"){
                        				chooseUserDiv += '<li style="margin-right:15px;"><div class="choose_user_name_span">'+userName
                        				+'</div><span><i class="layui-icon delete_choose_user" style="display:none" value="'+userUid
                        				+'" onclick="deleteAssembleUser(this);">&#x1007;</i></span></li>';
                        			}else{
                        				chooseUserDiv += '<li><div class="choose_user_name_span">'+userName
                        				+'</div><span><i class="layui-icon delete_choose_user" value="'+userUid
                        				+'" onclick="deleteAssembleUser(this);">&#x1007;</i></span></li>';
                        			}
                        		}
                        	}
                        	chooseUserDiv += "</ul></div>";
                        }else{
                        	chooseUserDiv += "<div class='choose_user_name_ul'><ul>"
                        				+ '</ul></div>';
                        }
                        chooseUserDiv += '<i class="layui-icon choose_user1" onclick=getConductor("'+activityMeta.activityId
                            +'","'+activityMeta.dhActivityConf.actcCanChooseUser+'","'
                            +activityMeta.dhActivityConf.actcAssignType+'","'+activityMeta.dhActivityConf.actcChooseableHandlerType+'"); >&#xe770;</i> '
                            +'<input type="hidden" class="getUser" id="'+activityMeta.activityId
                            +'" data-assignvarname="'+activityMeta.dhActivityConf.actcAssignVariable
                            +'" data-signcountvarname="'+activityMeta.dhActivityConf.signCountVarname +'"'
                            +'data-looptype="'+activityMeta.loopType+'" />'
                            +'</td></tr>';
                    }//end for
                }
                $("#submit_table table").append(chooseUserDiv);
                layer.closeAll("loading");
            }else {
                layer.closeAll("loading");
                layer.alert(result.msg);
            }
        }
    });
}

//删除已选处理人
function deleteAssembleUser(obj){
	$(obj).parent().parent().remove();
}

// 为直接跳转到驳回人显示环节栏
function showRouteBarForSkipFromReject() {
    var targetNodeName = $('#skipFromReject_targetNodeName').val();
    var newTaskOwnerName = $('#skipFromReject_newTaskOwnerName').val();
    $("#submit_table table").empty();
    var chooseUserDiv = '<tr>'
    				+'<th class="approval_th">下一环节：</th>'
                    +'<td>'+targetNodeName+'</td>'
                    +'</tr>'
                    +'<tr>'
                    +'<th class="approval_th">处理人：</th>'
                    +'<td>'
			            +'<div class="choose_user_name_ul"><ul>'
						    +'<li><div class="choose_user_name_span" style="margin-right:15px">'+newTaskOwnerName
							+'</div></li>'
						+'</ul></div>'
					+'</td>';
    $("#submit_table table").append(chooseUserDiv);
}

/**
 * 保存草稿表单数据的方法
 */
var index2 = null;
function saveDraftsInfo() {
    var control = true; //用于控制复选框出现重复值
    var checkName = ""; //用于获得复选框的class值，分辨多个复选框

    // 发起流程
    var finalData = {};
    // 表单数据
    var jsonStr = common.getDesignFormData();
    var formData = JSON.parse(jsonStr);

    finalData.formData = formData;

    var aprOpiComment = $("#myApprovalOpinion").val();
    aprOpiComment = aprOpiComment.replace('/\n|\r\n/g',"<br>");
    var taskUid = $("#taskUid").val();
    var taskId = $("#taskId").val();
    finalData.taskData = {"taskId":taskId,"taskUid":taskUid};
    finalData.approvalData = {"aprOpiComment":aprOpiComment};
    // 保存草稿数据
    var insUid = $("#insUid").val();

    var insTitle = $("#insTitle_input").val();
    $.ajax({
        url: common.getPath()+"/drafts/saveDrafts",
        method: "post",
        async: false,
        data: {
            dfsTitle: insTitle,
            dfsData: JSON.stringify(finalData),
            insUid: insUid,
            taskUid: taskUid
        },
        beforeSend: function () {
            index2 = layer.load(1);
        },
        success: function (result) {
            if(result>0){
            layer.close(index2);
            layer.alert('保存成功')
            }else{
                layer.close(index2);
                layer.alert('保存失败')
            }
        },
        error: function (){
            layer.close(index2);
            layer.alert('保存异常')
        }
    });
    //end
}

// 检查流程标题有没有填写
function checkInsTitle() {
    if ($('#canEditInsTitle').val() == 'true') {
        var insTitle = $("#insTitle_input").val();
        if (!insTitle || insTitle.trim() == '') {
            return false;
        }
        if (!insTitle.length > 60) {
            return false;
        }
        return true;
    } else {
        return true;
    }
}

// 提醒填写审批意见
function alertNeedApprovalOpinion() {
	$(".mobile_menu li").css({"color":"#A0A0A0"});
	$(".mobile_menu #approve").parent().css({"color":"#009688"});
	var id = $(".mobile_menu #approve").attr("title");
	$(".middle_content").css("display","none");
	$("#"+id+"_div").css("display","block");
    $("#myApprovalOpinion").focus();
    layer.alert("请填写审批意见");
}

//加载已上传的文件列表
function loadFileList(){
	var appUid = $("#insUid").val();
	var taskStatus = $("#taskStatus").val();
	$.post('accessoryFileUpload/loadFileList.do'
		,{"appUid":appUid}
		,function(result){
		$("#loadFile_div").empty();
		var info = '<h1 style="clear: both;"></h1>';
		for (var i = 0; i < result.data.length; i++) {
			info += '<li>'
				+'<table>'
					+'<tr>'
						+'<th>附件名称：</th>'
						+'<td>'+result.data[i].appDocFileName+'</td>'
					+'</tr>'
					+'<tr>'
						+'<th>上传人：</th>'
						+'<td>'+result.data[i].appUserName+'</td>'
					+'</tr>'
					+'<tr>'
						+'<th>上传时间：</th>'
						+'<td>'+datetimeFormat_1(result.data[i].appDocCreateDate)+'</td>'
					+'</tr>'
					+'<tr>'
						+'<th>操作：</th>'
						+'<td value="'+result.data[i].appDocUid
						+'" onclick="singleDown(this)" style="color:#009688;">下载附件<i class="layui-icon">&#xe601;</i></td>'
					+'</tr>'
				+'</table>'
				+'</li>';
		}
		info+='<h1 style="clear: both;"></h1>';
		$("#loadFile_div").append(info);
	});
}

//单个下载触发事件
function singleDown(a){
  var url = common.getPath()+"/accessoryFileUpload/singleFileDown.do";
  var appDocUid = $(a).attr("value");
  post(url,{"appDocUid" : appDocUid});
};

// 文件下载(单个下载)
function post(URL, PARAMS) { 
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

//点击行到行详情页面
function showDataTr(obj){
	var trHtml = $(obj).parent().html();
	var tableTitle = $(obj).parent().parent().parent().attr("title");
	$("#tr_con_content title_p").text(tableTitle);
	$("#tr_con_content #tr_table").html(trHtml);
	$("#tr_con_content #tr_table").find("td").each(function(){
		if($(this).css("display")=="none"){
			var value = $(this).find("input").val();
			$(this).find("input").val(value);
			$(this).css("display","block");
		}else{
			$(this).css("display","none");
		}
	});
	$(".mobile_container").css("display","none");
	$("#table_tr_container").css("display","block");
}