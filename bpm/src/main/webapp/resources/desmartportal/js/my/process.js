function getConductor(id, isSingle, actcCanChooseUser, actcAssignType,actcChooseableHandlerType) {
    if (actcCanChooseUser == 'FALSE') {
        return false;
    }
    var area=[];
    if(actcAssignType=='allUser'||actcChooseableHandlerType=='allUser'){
    	area=['640px', '480px'];
    }else{
    	area=['370px', '430px'];
    }
    console.log(actcAssignType);
    console.log(actcChooseableHandlerType);
    var url = 'sysUser/assign_personnel?id=' + id + '&isSingle=' + isSingle + '&actcCanChooseUser=' + actcCanChooseUser
        + '&actcAssignType=' + actcAssignType +'&actcChooseableHandlerType='+actcChooseableHandlerType + '&taskUid=';
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
                //location.reload();//刷新
            });
        }
    });
    layer.style(index, {
    	zoom:1.1
    });
}

$(function () {
    selectDepart();
    saveDraftsData();
    toShowRouteBar(); 
    var dateStr = common.dateToSimpleString(new Date());
    // 为流程创建日期赋值
    $("#createDate").text("填写时间："+dateStr);
})

/**
 * 选择部门触发的事件
 */
function selectDepart(){
	layui.use('form', function(){
		var form = layui.form;
		
		form.on('select(creatorInfo)', function(data){
			var info = data.value;
			if(info!=null && info!=""){
				var departNo = info.split(",")[0];
				var companyCode = info.split(",")[1];
				$("#departNo").val(departNo);
				$("#companyNum").val(companyCode);
			}
		}); 
	});
}

/**
 * 保存草稿表单数据的方法
 */
var index2 = null;
var saveDraftsData = function () {
    $("#saveInfoBtn")
        .click(
            function (e) {
            	
            	if($.trim($("#insTitle_input").val())==null || $.trim($("#insTitle_input").val())==""){
            		layer.alert("流程标题不能为空");
            		return;
            	}            	
                e.preventDefault();
                var control = true; //用于控制复选框出现重复值
                var checkName = ""; //用于获得复选框的class值，分辨多个复选框
                
                // 发起流程             
                var finalData = {};
                // 表单数据
                var jsonStr = common.getDesignFormData();
                var formData = JSON.parse(jsonStr);
                finalData.formData = formData;
                // 流程数据
                var processData = {};
                processData.insUid = $("#insUid").val();
                processData.departNo = $("#departNo").val();
                processData.companyNumber = $("#companyNum").val();
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
                // 保存草稿数据                   
                var insUid = $("#insUid").val();
                var userId = $("#userId").val();
                var insTitle = $("#insTitle_input").val();
                $.ajax({
                    url: "drafts/saveDrafts",
                    method: "post",
                    async: false,
                    data: {
                    	dfsTitle: insTitle,
                        dfsData: JSON.stringify(finalData),
                        dfsCreator: userId,
                        insUid: insUid
                    },
                    beforeSend: function () {
                        index2 = layer.load(1);
                    },
                    success: function (result) {
                        layer.close(index2);
                        layer.alert('保存成功', function(index){
                        	window.history.back();
                        	  layer.close(index);
                        	});  
                    },
                    error : function (result){
                        layer.close(index2);
                        layer.alert('保存失败')
                    }
                });
            });
    //end
}


var index = null; // 加载
function toShowRouteBar() {
    // 点击"提交"按钮弹出选人框
    $("#startProcess_btn").click(function (e) {
        e.preventDefault();
        var departNo = $("#departNo").val();
        var companyNumber = $("#companyNum").val();
        if (departNo == null || departNo == "" || companyNum == "" || companyNum == null) {
            layer.alert("缺少发起人信息");
            return;
        }
        var insTitle = $("#insTitle_input").val();
        if (!insTitle || insTitle.trim() == '') {
            layer.alert("缺少流程标题");
            return;
        }
        if (!insTitle.length > 60) {
            layer.alert("流程标题过长，请重新填写");
            return;
        }
        //必填项验证，勿删
        if (!common.validateFormMust("startProcess_btn")) {
            return;
        }
        //表单组件正则验证
        if (!common.validateRegx()) {
            return;
        }
        //表单提交验证方法，可在设计表单时重构
        if (!check_before_submit()) {
            return;
        }
        $.ajax({
            url: "dhRoute/showRouteBar",
            method: "post",
            data: {
                insUid: $("#insUid").val(),
                activityId: $("#activityId").val(),
                departNo: departNo,
                companyNum: companyNumber,
                formData: common.getDesignFormData()
            },
            beforeSend: function () {
                index = layer.load(1);
            },
            success: function (result) {
                if (result.status == 0) {
                    $("#choose_user_tbody").empty();
                    var activityMetaList = result.data;
                    var chooseUserDiv = "";
                    if (activityMetaList.length == 0) {
                        chooseUserDiv += '<tr>'
                            +'<th class="approval_th"><label for="link_name1">下一环节</label></th>'
                            +'<td>流程结束</td>'
                            +'<th class="approval_th">处理人</th>'
                            +'<td></td>'
                            +'</tr>';
                    } else {
                        for (var i = 0; i < activityMetaList.length; i++) {
                            var activityMeta = activityMetaList[i];
                            chooseUserDiv += '<tr>'
                                + '<th class="approval_th"><label for="link_name1">下一环节</label></th>'
                                + '<td>' + activityMeta.activityName + '</td>'
                                + '<th class="approval_th">处理人</th>'
                                + '<td>';
                            if (activityMeta.userName != null && activityMeta.userName != "") {
                                chooseUserDiv += '<input type="text" id="' + activityMeta.activityId + '_view" value="' + activityMeta.userName + '" name="addAgentPerson" class="layui-input" style="border-width:0px;padding:0px;" readonly>'
                            } else {
                                chooseUserDiv += '<input type="text" id="' + activityMeta.activityId + '_view" name="addAgentPerson" class="layui-input" style="border-width:0px;padding:0px;" readonly>'
                            }
                            chooseUserDiv += '</td>'
                                + '<th style="text-align:center;">'
                                + '<i class="layui-icon choose_user1" onclick=getConductor("' + activityMeta.activityId
                                + '","false","' + activityMeta.dhActivityConf.actcCanChooseUser + '","'
                                + activityMeta.dhActivityConf.actcAssignType + '","' + activityMeta.dhActivityConf.actcChooseableHandlerType + '"); >&#xe612;</i> '
                                + '<input type="hidden" class="getUser" id="' + activityMeta.activityId + '"  value="' + activityMeta.userUid + '" '
                                + 'data-assignvarname="' + activityMeta.dhActivityConf.actcAssignVariable + '" data-signcountvarname="' + activityMeta.dhActivityConf.signCountVarname + '"'
                                + 'data-looptype="' + activityMeta.loopType + '" />'
                                + '<input type="hidden"  id="choosable_' + activityMeta.activityId + '"  value="' + activityMeta.userUid + '"  />'
                                + '</th>'
                                + '</tr>';
                        }//end for
                    }
                    $("#choose_user_tbody").append(chooseUserDiv);
                    $(".display_container2").css("display", "block");
                    layer.close(index);
                }
            }//end ajax
        });
    }); //end
}

//发起流程     
function submitProcess(){
	var finalData = {};
    var json = common.getDesignFormData();
    console.log(json);
    // 表单数据
    var formData = JSON.parse(json);
    finalData.formData = formData;
    // 流程数据
    var processData = {};
    processData.insUid = $("#insUid").val();
    processData.departNo = $("#departNo").val();
    processData.companyNumber = $("#companyNum").val();
    processData.insTitle = $("#insTitle_input").val();
    finalData.processData = processData;
    // 路由数据
    var routeData = [];
    var lackNextOwner = false;
    $('.getUser').each(function () {
        var item = {};
        item.activityId = $(this).attr('id');
        item.userUid = $(this).val();
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
    console.log(finalData);
    $.ajax({
        url: common.getPath() + '/processInstance/startProcess',
        type: 'POST',
        dataType: 'json',
        data: {
            data: JSON.stringify(finalData)
        },
        beforeSend: function () {
            index = layer.load(1);
        },
        success: function (result) {
            layer.close(index);
            if (result.status == 0) {
                layer.alert('提交成功', function(){
                	window.history.back();
                });
                
            }else {
                layer.alert(result.msg);
            }
        },
        error: function (result) {
            layer.close(index);
            layer.alert('提交失败');
        }
    });
}

// 回退到上一页面
function back() {
	window.history.back();
}

//检查是否存在草稿数据，无草稿则保存一份草稿
var checkDraftsIndex;
var checkCount = 0;
function checkDraftsExtis(){
	if(checkCount==0){//第一次点击开始上传时检查
	var control = true; //用于控制复选框出现重复值
    var checkName = ""; //用于获得复选框的class值，分辨多个复选框
    
    // 发起流程             
    var finalData = {};
    // 表单数据
    var formData = common.getDesignFormData();
    finalData.formData = formData;
    // 流程数据
    var processData = {};
    processData.insUid = $("#insUid").val();
    processData.departNo = $("#departNo").val();
    processData.companyNumber = $("#companyNum").val();
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
    // 保存草稿数据                   
    var insUid = $("#insUid").val();
    var userId = $("#userId").val();
    var insTitle = $("#insTitle").val();
    $.ajax({
        url: "drafts/checkDraftsExtis",
        method: "post",
        async: false,
        data: {
        	dfsTitle: insTitle,
            dfsData: JSON.stringify(finalData),
            dfsCreator: userId,
            insUid: insUid
        },
        beforeSend: function () {
        	checkDraftsIndex = layer.load(1);
        },
        success:function(result){
        	checkCount = result.data;
        	if(result.data<=0){
        		layer.alert("保存草稿出现异常");
        	}
        	layer.close(checkDraftsIndex);
        },error:function(){
        	layer.close(checkDraftsIndex);
        	layer.alert("检查草稿出现异常");
        }
		
	});
	}
}

//提交前验证方法
function check_before_submit(){
	console.log("1");
	return true;
}

