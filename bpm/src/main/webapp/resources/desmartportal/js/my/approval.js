// bool值记录是否能直接跳跃提交 回到驳回处
var canSkipFromReject = false;
// bool值记录提交时是否需要填写审批意见
var needApprovalOpinion = "";
$(function () {
	// bool值记录提交时是否需要填写审批意见
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
});


function getConductor(id, isSingle, actcCanChooseUser, actcAssignType,actcChooseableHandlerType) {
    if (actcCanChooseUser == 'FALSE') {
        return false;
    }
    var area = [];
    if(actcAssignType=='allUser'||actcChooseableHandlerType=='allUser'){
    	area=['640px', '480px'];
    }else{
    	area=['515px', '480px'];
    }
    console.log(actcAssignType);
    console.log(actcChooseableHandlerType);
    var url = common.getPath()+'/sysUser/assign_personnel?id=' + id + '&isSingle=' + isSingle + '&actcCanChooseUser=' + actcCanChooseUser
        + '&actcAssignType=' + actcAssignType +'&actcChooseableHandlerType='+actcChooseableHandlerType + '&taskUid=' + $('#taskUid').val();
    var index = layer.open({
        type: 2,
        title: '选择人员',
        shadeClose: true,
        shade: 0.3,
        area: area,
        content: [url, 'yes'],
        success: function (layero, lockIndex) {
            var body = layer.getChildFrame('body', lockIndex);
            //绑定解锁按钮的点击事件
            body.find('button#close').on('click', function () {
                layer.close(lockIndex);
            });
        }
    });
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

	$("#middle10").on("click", ":checkbox", function(){
		if($(this).prop("checked")){
			$("#middle10 :checkbox").prop("checked", false);
            $(this).prop("checked", true);
         }
	});

	// "驳回" 确认
	$("#reject_btn").click(function (){
		$('input[name="check"]:checked').each(function(){ 
			var aprOpiComment = $("#myApprovalOpinion").val();
		    var taskId = $("#taskId").val();
		    var taskUid = $("#taskUid").val();
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
		        url: common.getPath()+'/taskInstance/rejectTask',
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
	})

    $(".add_row")
        .click(
            function () {
                var le = $(".create_table tbody tr").length + 1;
                $(".create_table")
                    .append(
                        $('<tr>' +
                            '<td>' +
                            le +
                            '</td>' +
                            '<td><input type="text" class="txt"/></td>' +
                            '<td><input type="text" class="txt"/></td>' +
                            '<td><input type="text" class="txt"/></td>' +
                            '<td><input type="text" class="txt"/></td>' +
                            '<td><i class="layui-icon delete_row">&#xe640;</i></td>' +
                            '</tr>'));
                $(".delete_row").click(function () {
                    $(this).parent().parent().remove();
                });
            });
    $(".delete_row").click(function () {
        $(this).parent().parent().remove();
    });
    $(".upload").click(function () {
        $(".upload_file").click();
    });
    
    // 会签
    $("#add").click(function() {
    	$("#handleUser_view").val("");
    	$(".display_container7").css("display","block");
    });
    // 会签选择处理人（人员）
    $("#choose_handle_user").click(function() {
    	common.chooseUser('handleUser', 'false');
    });
    
    // 抄送
    $("#transfer").click(function(){
    	$("#handleUser1_view").val("");
    	$(".display_container6").css("display","block");
    }); 
    // 抄送选择处理人（人员）
    $("#choose_handle_user1").click(function() {
    	common.chooseUser('handleUser1', 'false');
    });
    
    $(".cancel_btn").click(function() {
    	$(".display_container7").css("display","none");
    	$(".display_container6").css("display","none");
    });
    
    // 驳回
    $(".cancel5_btn").click(function() {
    	$(".display_container8").css("display","none");
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
                } else {
                    $(".layui-progress").append('<span class="progress_time">审批剩余时间' + hour + '小时</span>');
                }
                // 加载进度条
                layui.use('element', function () {
                    var $ = layui.jquery,
                        element = layui.element; //Tab的切换功能，切换事件监听等，需要依赖element模块
                    // 延迟加载
                    setTimeout(function () {
                        element.progress('progressBar', (100-percent) + '%');
                    }, 500);
                });
            } else {
                $(".layui-progress").append('<span class="progress_time">加载失败!</span>');
            }
        }
    });
});

// 加签确定
function addSure(){
	var taskUid = $("#taskUid").val();
	var taskId = $("#taskId").val();
	var insUid = ""+$("#insUid").val();
	var usrUid = $("#handleUser").val();
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
	var usrUid = $("#handleUser1").val();
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
	window.parent.openProView(insId);
}

// 确认提交
function doSubmit() {
    if (canSkipFromReject) {
        skipFromReject();
    } else {
        submitTask();
    }
}

// 提交任务的方法
function submitTask() {
    var taskId = $("#taskId").val();
    var taskUid = $("#taskUid").val();
    var insUid = ""+$("#insUid").val();//流程实例id--ins_uid
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
        layer.alert("流程主题过长或未填写");
        return
    }
    // 发起流程             
    var finalData = {};
    // 表单数据
    var jsonStr = common.getDesignFormData();
    var formData = JSON.parse(jsonStr);
    
    finalData.formData = formData;
    // 流程数据
    var processData = {};
    processData.insUid = ""+$("#insUid").val();
    processData.insTitle = $("#insTitle_input").val().trim();
    finalData.processData = processData;

    // 路由数据
    var routeData = [];
    var lackNextOwner = false;
    $('.getUser').each(function () {
        var item = {};
        item.activityId = $(this).attr('id');
        item.userUid = ""+$(this).val();
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
        url: common.getPath() +'/taskInstance/finshedTask',
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

// 提交至驳回节点的方法
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
    // 发起流程
    var finalData = {};
    // 表单数据
    var formData = JSON.parse(common.getDesignFormData());
    // 流程数据
    var processData = {};
    processData.insUid = ""+$("#insUid").val();
    processData.insTitle = $("#insTitle_input").val().trim();
    finalData.processData = processData;
    finalData.formData = formData;
    finalData.taskData = {"taskUid": taskUid};
    finalData.approvalData = {"aprOpiComment": aprOpiComment};

    $.ajax({
        url: common.getPath() +'/taskInstance/skipFromReject',
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
    var insUid = ""+$("#insUid").val();
    var aprOpiComment = $("#myApprovalOpinion").val();//审批意见
    if(aprOpiComment == null || aprOpiComment == "" || aprOpiComment == undefined){
        alertNeedApprovalOpinion();
    	return;
    }
    $.ajax({
        url: common.getPath() +'/processInstance/queryRejectByActivity',
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
            layer.close(index);
        		$("#middle10").empty();
            	var rejectMapList = result.data;
            	var rejectDiv = "";
            	for(var i=0;i<rejectMapList.length;i++){
                	rejectDiv += "<li><input type='checkbox' name='check' value='"+rejectMapList[i].insId+"+"+rejectMapList[i].activityBpdId+"+"+rejectMapList[i].userId+"'/><label>"+rejectMapList[i].activityName+"————审批人:"+rejectMapList[i].userName+"</label></li>";    
                	}//end for
            	$("#middle10").append(rejectDiv);
        },
        error: function (result) {
            layer.close(index);
            layer.alert('查询驳回环节失败', {
                icon: 2
            });
        }
    });
    $(".display_container8").css("display","block");
}

function back() {
	window.location.href = 'javascript:history.go(-1)';
}

//数据信息
var view = $(".container-fluid");
var form = null;


// 单击"提交"按钮
function checkUserData() {
    var departNo = $("#departNo").val();
    var companyNumber = $("#companyNum").val();
    // var formData =common.getDesignFormData();
    var aprOpiComment = $("#myApprovalOpinion").val();//审批意见
    if (departNo==null || departNo=="" || companyNum=="" || companyNum==null) {
    	layer.alert("缺少流程发起人信息");
    	return;
    }
    if (!checkInsTitle()) {
        layer.alert("流程主题过长或未填写");
        return
    }
    //必填项验证，勿删
    if(!common.validateFormMust("startProcess_btn")){
    	return;
    }
    //表单组件正则验证
    if(!common.validateRegx()){
    	return;
    }
    //表单提交验证方法，可在设计表单时重构
    if(!check_before_submit()){
    	return;
    }
    if(needApprovalOpinion && (aprOpiComment==null || aprOpiComment == "" || aprOpiComment == undefined)){
        alertNeedApprovalOpinion();
    	return;
    }
    if (canSkipFromReject) {
        showRouteBarForSkipFromReject();
    } else {
        showRouteBar();
    }
}

// 为提交任务显示选人栏
function showRouteBar() {
    $.ajax({
        url:common.getPath() +"/dhRoute/showRouteBar",
        type:"post",
        data:{
            "taskUid": $("#taskUid").val(),
            "insUid":""+$("#insUid").val(),
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
                $("#choose_user_tbody").empty();
                var activityMetaList = result.data;
                var chooseUserDiv = "";
                if(activityMetaList.length == 0){
                    chooseUserDiv += '<tr>'
                        +'<th class="approval_th"><label for="link_name1" colspan="2">下一环节</label></th>'
                        +'<td colspan="2">流程结束</td>'
                        +'</tr>';
                }else{
                    for(var i=0;i<activityMetaList.length;i++){
                        var activityMeta = activityMetaList[i];
                        chooseUserDiv += '<tr>'
                            +'<th class="approval_th"><label for="link_name1">下一环节</label></th>'
                            +'<td>'+activityMeta.activityName+'</td>'
                            +'<th class="approval_th">处理人</th>'
                            +'<td>';
                        if(activityMeta.userName!=null && activityMeta.userName!=""){
                            chooseUserDiv += '<input type="text" id="'+activityMeta.activityId+'_view" value="'+activityMeta.userName+'" name="addAgentPerson" class="layui-input" style="border-width:0px;padding:0px;" readonly>'
                        }else{
                            chooseUserDiv += '<input type="text" id="'+activityMeta.activityId+'_view" name="addAgentPerson" class="layui-input" style="border-width:0px;padding:0px;" readonly>'
                        }
                        chooseUserDiv += '</td>'
                            +'<th style="text-align:center;">'
                            +'<i class="layui-icon choose_user1" onclick=getConductor("'+activityMeta.activityId
                            +'","false","'+activityMeta.dhActivityConf.actcCanChooseUser+'","'
                            +activityMeta.dhActivityConf.actcAssignType+'","'+activityMeta.dhActivityConf.actcChooseableHandlerType+'"); >&#xe612;</i> '
                            +'<input type="hidden" class="getUser" id="'+activityMeta.activityId+'"  value="'+activityMeta.userUid+'" '
                            +'data-assignvarname="'+activityMeta.dhActivityConf.actcAssignVariable+'" data-signcountvarname="'+activityMeta.dhActivityConf.signCountVarname +'"'
                            +'data-looptype="'+activityMeta.loopType+'" />'
                            +'<input type="hidden"  id="choosable_'+activityMeta.activityId+'"  value="'+activityMeta.userUid+'"  />'
                            +'</th>'
                            +'</tr>';
                    }//end for
                }
                $("#choose_user_tbody").append(chooseUserDiv);
                $(".display_container2").css("display","block"); //end
                layer.closeAll("loading");
            }else {
                layer.closeAll("loading");
                layer.alert(result.msg);
            }
        }
    });
}

// 为直接跳转到驳回人显示环节栏
function showRouteBarForSkipFromReject() {
    var targetNodeName = $('#skipFromReject_targetNodeName').val();
    var newTaskOwnerName = $('#skipFromReject_newTaskOwnerName').val();
    $("#choose_user_tbody").empty();
    var str = '<tr>'
             +   '<th class="approval_th"><label for="link_name1">下一环节</label></th>'
             +   '<td>' + targetNodeName + '</td>'
             +   '<th class="approval_th">处理人</th>'
             +   '<td>' + newTaskOwnerName + '</td>'
             + '</tr>';

    $("#choose_user_tbody").append(str);
    $(".display_container2").show();
}

/**
 * 保存草稿表单数据的方法
 */
function saveDraftsInfo() {
    var finalData = {};
    // 表单数据
    var formDataJsonStr = common.getDesignFormData();
    var formData = JSON.parse(formDataJsonStr);
    finalData.formData = formData;
    var aprOpiComment = $("#myApprovalOpinion").val();
    aprOpiComment = aprOpiComment.replace('/\n|\r\n/g',"<br>");
    var taskUid = $("#taskUid").val();
    finalData.taskData = {
        "taskUid": taskUid
    };
    finalData.approvalData = {
        "aprOpiComment": aprOpiComment
    };
    // 保存草稿数据
    var insUid = ""+$("#insUid").val();
    var insTitle = $("#insTitle_input").val();
    $.ajax({
        url: common.getPath() + "/drafts/saveTaskDraft",
        method: "post",
        async: false,
        data: {
            dfsTitle: insTitle,
            dfsData: JSON.stringify(finalData),
            insUid: insUid,
            taskUid: taskUid
        },
        beforeSend: function () {
            layer.load(1);
        },
        success: function (result) {
            layer.closeAll('loading');
            if (result.status == 0) {
                layer.alert('保存成功')
            } else {
                layer.alert(result.msg)
            }
        },
        error: function (){
            layer.closeAll('loading');
            layer.alert('保存草稿异常')
        }
    });
    //end
}

// 检查流程主题有没有填写
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
    $('#approve_p').show();
    $('#approve_div').show();
    $("#myApprovalOpinion").focus();
    layer.alert("请填写审批意见");
}

//提交前验证方法
function check_before_submit(){
	console.log("1");
	return true;
}