function getConductor(id, isSingle, actcCanChooseUser, actcAssignType,actcChooseableHandlerType) {
    if (actcCanChooseUser == 'FALSE') {
        return false;
    }
    var url = 'sysUser/assign_personnel?id=' + id + '&isSingle=' + isSingle + '&actcCanChooseUser=' + actcCanChooseUser + '&actcAssignType=' + actcAssignType +'&actcChooseableHandlerType='+actcChooseableHandlerType;
    layer.open({
        type: 2,
        title: '选择人员',
        shadeClose: true,
        shade: 0.3,
        area: ['615px', '492px'],
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
	
	$("#reject_btn").click(function (){
		$('input[name="check"]:checked').each(function(){ 
			var str = $(this).val();
		    var insId = str.substring(0,str.indexOf("+"));
		    var activityBpdId = str.substring(str.indexOf("+")+1,str.lastIndexOf("+"));
		    var userId = str.substring(str.lastIndexOf("+") + 1);
		    $.ajax({
		        url: 'processInstance/rejectProcess',
		        type: 'POST',
		        dataType: 'json',
		        data: {
		        	insId : insId,
		        	activityBpdId : activityBpdId,
		        	userId : userId
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
    // 加签
    $("#add").click(function() {
    	$("#handleUser_view").val("");
    	$(".display_container7").css("display","block");
    })
    // 选择处理人（人员）
    $("#choose_handle_user").click(function() {
    	common.chooseUser('handleUser', 'false');
    });
    $(".cancel_btn").click(function() {
    	$(".display_container7").css("display","none");
    })
    
    // 驳回
    $(".cancel5_btn").click(function() {
    	$(".display_container8").css("display","none");
    })

    // 查询审批进度剩余进度百分比
    var proUid = $("#proUid").val();
    var proVerUid = $("#proVerUid").val();
    var proAppId = $("#proAppId").val();
    var taskUid = $("#taskUid").val();
    $.ajax({
        async: false,
        url: common.getPath() + "/taskInstance/queryProgressBar",
        type: "post",
        dataType: "json",
        data: {
            proUid: proUid,
            proVerUid: proVerUid,
            proAppId: proAppId,
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
                    $(".progress_time").css('right', '4%');
                } else {
                    $(".layui-progress").append('<span class="progress_time">审批剩余时间' + hour + '小时</span>');
                    var num = 89 - percent;
                    if (num > 0) {
                    	$(".progress_time").css('right', num + '%');
					} else {
						$(".progress_time").css('right', '15%');
					}                
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

// 加签确定
function addSure(){
	var taskUid = $("#taskUid").val();
	var taskId = $("#taskId").val();
	var insUid = $("#insUid").val();
	var usrUid = $("#handleUser_view").val();
	var activityBpdId = $("#activityId").val();
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
			activityBpdId: activityBpdId,
			usrUid: usrUid,
			taskType: taskType
		},
		success: function(data){
			if (data.status == 0) {
				layer.alert("操作成功!");
				$(".display_container7").css("display","none");
			}else {
				layer.alert(data.msg)
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

function agree() {
	var inputArr = $("table input");
    var taskId = $("#taskId").val();
    var taskUid = $("#taskUid").val();
    var activityId = ""
    var userUid = ""
    //var insData = $("#insData").text();
    $('.getUser').each(function () {
        activityId = $(this).attr('id');
        userUid = $(this).val();
    });

    var insUid = $("#insUid").val();//流程实例id--ins_uid
    //var apr_activityId = $("#activityId").val();//环节id，activity_id
    var aprOpiComment = $("#myApprovalOpinion").val();//审批意见
    if(aprOpiComment==null || aprOpiComment == "" || aprOpiComment == undefined){
    	layer.alert("请填写审批意见");
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
    finalData.processData = processData;

    var activityId = ""
    var userUid = ""
    var insData = $("#insData").text();
    // 路由数据
    var routeData = [];
    $('.getUser').each(function () {
        var item = {};
        item.activityId = $(this).attr('id');
        item.userUid = $(this).val();
        item.assignVarName = $(this).data("assignvarname");
        item.signCountVarName =  $(this).data("signcountvarname");
        item.loopType = $(this).data("looptype");
        routeData.push(item);
    });
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

function queryRejectByActivitiy() {
    var activityId = $("#activityId").val();
    var insUid = $("#insUid").val();
    
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
$(function () {
   
    layui.use(['form'], function () {
        form = layui.form;
    });
    console.log($("#formData").text());
    
    common.giveFormSetValue($("#formData").text());
    form.render();
    var fieldPermissionInfo = $("#fieldPermissionInfo").text();
    common.giveFormFieldPermission(fieldPermissionInfo);
    form.render();
});

//提交流程-->选人
function checkUserData() {
    var departNo = $("#departNo").val();
    var companyNumber = $("#companyNum").val();
    var formData =common.getDesignFormData();
    var aprOpiComment = $("#myApprovalOpinion").val();//审批意见
    if(aprOpiComment==null || aprOpiComment == "" || aprOpiComment == undefined){
    	layer.alert("请填写审批意见");
    	return;
    }   
    if (departNo==null || departNo=="" || companyNum=="" || companyNum==null) {
    	layer.alert("缺少流程发起人信息");
    	return;
    }
    $.ajax({
    	url:"dhRoute/showRouteBar",
    	method:"post",
    	data:{
        	"insUid":$("#insUid").val(),
            "activityId":$("#activityId").val(),
        	"departNo":departNo,
        	"companyNum":companyNumber,
        	"formData":formData
    	},
        success:function(result){
        	if(result.status==0){
        		$("#choose_user_tbody").empty();
            	var activityMetaList = result.data;
            	var chooseUserDiv = "";
            	layer.load(1);
            	if(activityMetaList.length==0){
            		chooseUserDiv += '<tr>'
                		+'<th class="approval_th"><label for="link_name1">下一环节</label></th>'
						+'<td>流程结束</td>'
						+'<th class="approval_th">处理人</th>'
						+'<td></td>'
					+'</tr>';
            	}else{
            		for(var i=0;i<activityMetaList.length;i++){
                    	var activityMeta = activityMetaList[i];
                    	chooseUserDiv += '<tr>'
                    		+'<th class="approval_th"><label for="link_name1">下一环节</label></th>'
    						+'<td>'+activityMeta.activityName+'</td>'
    						+'<th class="approval_th">处理人</th>'
    						+'<td><input type="text" id="'+activityMeta.activityId+'_view" value="'+activityMeta.userName+'" name="addAgentPerson" class="layui-input" style="border-width:0px;padding:0px;" readonly></td>'
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
            	layer.closeAll("loading");
            }
    	}//end ajax
	});
	$(".display_container2").css("display","block"); //end
	
}

