function getConductor(id, isSingle, actcCanChooseUser, actcAssignType,actcChooseableHandlerType) {
    if (actcCanChooseUser == 'FALSE') {
        return false;
    }
    var area=[];
    if(actcAssignType=='allUsers'||actcChooseableHandlerType=='allUsers'){
    	area=['615px', '492px'];
    }else{
    	area=['615px', '492px'];
    }

    var url = 'sysUser/assign_personnel?id=' + id + '&isSingle=' + isSingle + '&actcCanChooseUser=' + actcCanChooseUser + '&actcAssignType=' + actcAssignType +'&actcChooseableHandlerType='+actcChooseableHandlerType;
    layer.open({
        type: 2,
        title: '选择人员',
        shadeClose: true,
        shade: 0.8,
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
}

$(function () {
    selectDepart();
    saveDraftsData();
    toShowRouteBar(); 
    var dateStr = common.dateToSimpleString(new Date());
    // 为流程创建日期赋值
    $("#createDate").val(dateStr);
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
                e.preventDefault();
                var control = true; //用于控制复选框出现重复值
                var checkName = ""; //用于获得复选框的class值，分辨多个复选框
                
                // 发起流程             
                var finalData = {};
                // 表单数据
                var formData = common.getDesignFormData();
                finalData.formData = formData;
                // 流程数据
                var processData = {};
                //processData.proAppId = $("#proAppId").val();
                //processData.proUid = $("#proUid").val(); 
                //processData.proVerUid = $("#verUid").val();
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
                        layer.alert('保存成功')
                    }
                });
            });
    //end
}


var index = null; // 加载
function toShowRouteBar () {
	// 点击"提交"按钮弹出选人框
    $("#startProcess_btn").click(function (e) {
    e.preventDefault();
    var departNo = $("#departNo").val();
    var companyNumber = $("#companyNum").val();
    var insTitle = $("#insTitle").val();
    if(insTitle == null || insTitle== "" ){
    	layer.alert("请填写流程实例标题");
    	return;
    }
    if (departNo==null || departNo=="" || companyNum=="" || companyNum==null) {
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
    $.ajax({
    	url:"dhRoute/showRouteBar",
    	method:"post",
    	data:{
        	insUid:$("#insUid").val(),
            activityId:$("#activityId").val(),
        	departNo:departNo,
        	companyNum:companyNumber,
        	formData:common.getDesignFormData()
    	},
        success:function(result){
        	if(result.status==0){
        		$("#choose_user_tbody").empty();
            	var activityMetaList = result.data;
            	var chooseUserDiv = "";
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
                	$("#choose_user_tbody").append(chooseUserDiv);
                	}//end for
                }
        	}//end ajax
		});
		$(".display_container2").css("display","block");
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
                layer.alert('提交成功', {
                    icon: 1
                });
                window.history.back();
            }else {
                layer.alert(result.msg, {
                    icon: 2
                });
            }
        },
        error: function (result) {
            layer.close(index);
            layer.alert('提交失败', {
                icon: 2
            });
        }
    });
}

// 回退到上一页面
function back() {
	window.history.back();
}

