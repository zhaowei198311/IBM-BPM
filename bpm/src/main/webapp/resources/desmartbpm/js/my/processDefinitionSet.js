var triggerToEdit;
var firstChooseRole = true;
var firstChooseTeam = true;
var pageConfig = {
    pageNum: 1,
    pageSize: 5,
    total: 0,
    triTitle: "",
    triType: ""
}
layui.use('form', function(){
    var form = layui.form;
    //是否为可选处理人
    form.on('radio(isAllUserStart)', function(data){
   	 if (data.value == "TRUE") {
   		 	$(data.elem).parent().parent().parent().find(".layui-form-item").hide();
   		 	$(data.elem).parent().parent().show();
   		 $(data.elem).parent().parent().parent()
   		 	.find(".layui-form-item").find("input[type='text']").val("");
   		 $(data.elem).parent().parent().parent()
		 	.find(".layui-form-item").find("input[type='hidden']").val("");
        } else {
        	$(data.elem).parent().parent().parent().find(".layui-form-item").show();
        }
   });
});

// 处理可启动的关键字模块
var startBusinessKey = {
    elements: {
        showContinerI: $('#showContinerI'),
        container: $('#chooseStartBusinessKeyContainer'),
        unstartUl: $('#unstartUl'),
        startUl: $('#startUl'),
        toRightBtn: $('#toRightBtn'),
        toLeftBtn: $('#toLeftBtn'),
        sureBtn: $('#startBusinessKeySureBtn'),
        valueInput: $('#proStartBusinessKey')  // 存放提交值的控件
    },
    init: function(stepBusinessKeyStr) {
        startBusinessKey.elements.showContinerI.click(function(){
            startBusinessKey.elements.container.show();
        });
        startBusinessKey.elements.container.on('click', 'li', function(event){
            var $li = $(event.target);
            $li.toggleClass('colorli');
        });
        startBusinessKey.elements.container.on('mouseenter', 'li', function(e){
            var $li = $(e.target);
            $li.addClass('tempColorli');
        });
        startBusinessKey.elements.container.on('mouseleave', 'li', function(e){
            var $li = $(e.target);
            $li.removeClass('tempColorli');
        });
        startBusinessKey.elements.sureBtn.click(function(){
            var $lis = startBusinessKey.elements.startUl.find('li');
            var str = '';
            for (var i = 0; i < $lis.length; i++) {
                str += $lis.eq(i).data('businesskey') + ';';
            }
            startBusinessKey.elements.valueInput.val(str);
            startBusinessKey.elements.container.hide();
            startBusinessKey.elements.container.find('li').each(function(){
                $(this).removeClass('colorli');
                $(this).removeClass('tempColorli');
            });
        });
        startBusinessKey.elements.toRightBtn.click(function(){
            startBusinessKey.elements.unstartUl.find('.colorli').each(function(){
                $(this).appendTo(startBusinessKey.elements.startUl);
                $(this).removeClass('colorli');
            });
        });
        startBusinessKey.elements.toLeftBtn.click(function(){
            startBusinessKey.elements.startUl.find('.colorli').each(function(){
                $(this).appendTo(startBusinessKey.elements.unstartUl);
                $(this).removeClass('colorli');
            });
        });
        // 如果这个流程定义下存在步骤
        var allBusinessKeyArr = [];
        var startBusinessKeyArr = [];
        if (stepBusinessKeyStr) {
            stepBusinessKeyStr = stepBusinessKeyStr.substring(0, stepBusinessKeyStr.length - 1);
            allBusinessKeyArr = stepBusinessKeyStr.split(';');
        }
        var startBusinessKeyStr = startBusinessKey.elements.valueInput.val();
        if (startBusinessKeyStr) {
            startBusinessKeyStr = startBusinessKeyStr.substring(0, startBusinessKeyStr.length - 1);
            startBusinessKeyArr = startBusinessKeyStr.split(';');
        }
        console.log(allBusinessKeyArr);
        console.log(startBusinessKeyArr);
        if (allBusinessKeyArr.length) {
            var leftStr = '';
            var rightStr = '';
            for (var i = 0; i < allBusinessKeyArr.length; i++) {
                var businessKey = allBusinessKeyArr[i];
                var businessKeyShow = businessKey.length > 10 ? businessKey.substring(0, 10) + '...' : businessKey;
                if (startBusinessKeyArr.includes(businessKey)) {
                    rightStr += '<li data-businesskey="'+businessKey+'" title="'+businessKey+'">' + businessKeyShow + '</li>';
                } else {
                    leftStr += '<li data-businesskey="'+businessKey+'" title="'+businessKey+'">' + businessKeyShow + '</li>';
                }
            }
            startBusinessKey.elements.unstartUl.append(leftStr);
            startBusinessKey.elements.startUl.append(rightStr);
        }



    }
};


$(function() {
	var isAllUserStart = $("#isAllUserStart").val();
	$("input[name='isAllUserStart']").each(function(){
		if($(this).val()==isAllUserStart){
			$(this).prop("checked",true);
			if($(this).val()=="TRUE"){
				$(this).parent().parent().parent().find(".layui-form-item").hide();
				$(this).parent().parent().show();
				 $(this).parent().parent().parent()
		   		 	.find(".layui-form-item").find("input[type='text']").val("");
		   		 $(this).parent().parent().parent()
				 	.find(".layui-form-item").find("input[type='hidden']").val("");
			}
			layui.form.render();
		}
	})
	getPermissionStart();
    startBusinessKey.init(stepBusinessKeyStr);

    $('#form1').validate({
        rules : {
            proTime : {
                digits : true
            },
            proDynaforms: {
            	maxlength: 100
            },
            proDerivationScreenTpl: {
            	maxlength: 128
            }
        }
    });
    $("#form4").validate({
        rules : {
            proHeight : {
                digits : true
            },
            proWidth : {
                digits : true
            },
            proTitleX: {
                digits : true
            },
            proTitleY: {
                digits : true
            }
        }
    });
    // “返回”按钮
    $("#back_btn").click(function () {
        window.history.back();
    });

    $("#chooseTrigger_container").on("click", ":checkbox", function(){
        if ($(this).prop("checked")) {
            $("#chooseTrigger_container :checkbox").prop("checked", false);
            $(this).prop("checked", true);
        }
    });
    
    // “保存”按钮
    $("#save_btn").click(function () {
        if (!$("#form1").valid() || !$("#form4").valid()) {
        	layer.alert("表单填写有异常参数，请检查后再提交");
            return;
        }

        var dataToSend = getData();
        console.log(dataToSend);
        if (!dataToSend.isOk) {
            layer.alert(dataToSend.msg);
            return;
        }
        
        $.ajax({
            url: common.getPath() + "/processDefinition/update",
            type: "post",
            dataType: "json",
            data: dataToSend,
            success: function (result) {
                if (result.status == 0) {
                    layer.alert("保存成功");
                } else {
                    layer.alert(result.msg);
                }
            }
        });

    });

    // “配置完成”按钮
    $("#finish_btn").click(function () {
    	 if (!$("#form1").valid() || !$("#form4").valid()) {
             return;
         }

         var dataToSend = getData();
         console.log(dataToSend);
         if (!dataToSend.isOk) {
             layer.alert(dataToSend.msg);
             return;
         }
         
         $.ajax({
             url: common.getPath() + "/processDefinition/update",
             type: "post",
             dataType: "json",
             data: dataToSend,
             success: function (result) {
                 if (result.status == 0) {
                     layer.alert("保存成功");
                 } else {
                     layer.alert(result.msg);
                 }
             }
         });
    });

    // 点击选择触发器
    $(".choose_tri").click(function () {
        triggerToEdit = $(this).data('identify');
        getInfo();
        $("#chooseTrigger_container").show();
    });

    // “确认”选择触发器
    $("#chooseTrigger_sureBtn").click(function () {
        var cks = $("[name='tri_check']:checked");
        if (!cks.length) {
            $("#" + triggerToEdit).val('');
            $("#" + triggerToEdit + "Title").val('');
            $("#chooseTrigger_container").hide();
            return;
        }
        if (cks.length > 1) {
            layer.alert("请选择一个触发器，不能选择多个");
            return;
        }
        var ck = cks.eq(0);
        var triUid = ck.val();
        var triTitle = ck.parent().next().html();

        $("#" + triggerToEdit).val(triUid);
        $("#" + triggerToEdit + "Title").val(triTitle);
        $("#chooseTrigger_container").hide();
    });

    // “取消”选择触发器
    $("#chooseTrigger_cancelBtn").click(function () {
        $("#chooseTrigger_container").hide();
    });

    // “查询”按钮
    $("#searchTrigger_btn").click(function () {
        pageConfig.triTitle = $("#triTitle_input").val().trim();
        pageConfig.triType = $("#triType_sel").val();
        pageConfig.pageNum = 1;
        getInfo();
    })

    // 选择发起人员
    $("#chooseUser_btn").click(function () {
    	common.chooseUser('permissionStartUser', 'false');
    });
    
    // 选择发起角色
    $("#chooseRole_btn").click(function(){
    	common.chooseRole('permissionStartRole', 'false');
    });
    
    // 选择发起角色组
    $("#chooseTeam_btn").click(function(){
    	common.chooseTeam('permissionStartTeam', 'false');
    });

});

// 获取页面上的数据
function getData() {
    var data = {};
    data.isOk = true; // 数据是否都正常
    data.msg = "";    // 错误信息
    data.proUid = $('[name="proUid"]').val();
    data.proAppId = $('[name="proAppId"]').val();
    data.proVerUid = $('[name="proVerUid"]').val();
    data.proTime = $('[name="proTime"]').val();
    data.proDynaforms = $('[name="proDynaforms"]').val();
    data.proTimeUnit = $('[name="proTimeUnit"]').val();
    data.proDerivationScreenTpl = $('[name="proDerivationScreenTpl"]').val();
    data.proTriStart = $('[name="proTriStart"]').val();
    data.proTriPaused = $('[name="proTriPaused"]').val();
    data.proTriReassigned = $('[name="proTriReassigned"]').val();
    data.proTriDeleted = $('[name="proTriDeleted"]').val();
    data.proTriUnpaused = $('[name="proTriUnpaused"]').val();
    data.proTriCanceled = $('[name="proTriCanceled"]').val();
    data.proHeight = $('[name="proHeight"]').val();
    data.proWidth = $('[name="proWidth"]').val();
    data.proTitleX = $('[name="proTitleX"]').val();
    data.proTitleY = $('[name="proTitleY"]').val();
    data.permissionStartUser = $('[name="permissionStartUser"]').val();
    data.permissionStartRole = $('[name="permissionStartRole"]').val();
    data.permissionStartTeam = $('[name="permissionStartTeam"]').val();
    data.isAllUserStart = $('[name="isAllUserStart"]:checked').val();
    data.proStartBusinessKey = $('[name="proStartBusinessKey"]').val();
    // if(!/^[0-9]*[1-9][0-9]*$/.test(data.proHeight) || !/^[0-9]*[1-9][0-9]*$/.test(data.proWidth)
    //     || !/^[0-9]*[1-9][0-9]*$/.test(data.proTitleX) || !/^[0-9]*[1-9][0-9]*$/.test(data.proTitleY)){
    //     data.isOk = false;
    //     data.msg = "流程图信息，需要输入正整数";
    // }
    return data;
}

/* 向服务器请求数据   */
function getInfo() {
    $.ajax({
        url: common.getPath() + "/trigger/search",
        type: "post",
        dataType: "json",
        data: {
            "pageNum": pageConfig.pageNum,
            "pageSize": pageConfig.pageSize,
            "triTitle": pageConfig.triTitle,
            "triType": pageConfig.triType
        },
        success: function(result) {
            if (result.status == 0) {
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
    $("#chooseTrigger_tbody").html("");
    if (pageInfo.total == 0) {
        return;
    }
    var triParam = "";
    var list = pageInfo.list;
    var startSort = pageInfo.startRow;//开始序号
    var trs = "";
    for(var i=0; i<list.length; i++) {
        var trigger = list[i];
        var sortNum = startSort + i;
        var tempWebbot;
        var tempParam;
        if (trigger.triWebbot.length > 20) {
            tempWebbot = trigger.triWebbot.substring(0, 20) + "...";
        } else {
            tempWebbot = trigger.triWebbot;
        }
        if (trigger.triParam == null || trigger.triParam == ""){
        	tempParam = "没有参数"
        } else if (trigger.triParam.length > 20){
            tempParam = trigger.triParam.substring(0, 20) + "...";
        } else {
        	tempParam = trigger.triParam;
        }

        console.log(tempWebbot)
        trs += '<tr><td><input type="checkbox" name="tri_check" value="' + trigger.triUid + '" lay-skin="primary">'+ sortNum +'</td>'
            + '<td>'+trigger.triTitle+'</td>'
            + '<td>'+trigger.triType+'</td>'
            + '<td title="'+trigger.triWebbot+'">'+tempWebbot+'</td>'
            + '<td title="'+trigger.triParam+'">'+tempParam+'</td>'
            + '</tr>';
    }
    $("#chooseTrigger_tbody").append(trs);
}
// 分页插件刷新
function doPage() {
    layui.use(['laypage', 'layer'], function(){
        var laypage = layui.laypage,layer = layui.layer;
        //完整功能
        laypage.render({
            elem: 'lay_page',
            curr: pageConfig.pageNum,
            count: pageConfig.total,
            limit: pageConfig.pageSize,
            layout: ['count', 'prev', 'page', 'next', 'skip'],
            jump: function(obj, first){
                // 点击新页码进入此方法，obj包含了点击的页数的信息
                pageConfig.pageNum = obj.curr;
                pageConfig.pageSize = obj.limit;
                if (!first) {
                    getInfo();
                }
            }
        });
    });
}

// 获得该流程的发起权限
function getPermissionStart() {
	$.ajax({
		url: common.getPath() + "/permission/processStart",
		dataType: "json",
		type: "post",
		data: {
			"proAppId": $('[name="proAppId"]').val(),
			"proUid": $('[name="proUid"]').val(),
		    "proVerUid": $('[name="proVerUid"]').val()
		},
		success: function(result) {
			if (result.status == 0) {
				var map = result.data;
		        $("#permissionStartUser").val(map.permissionStartUser);
		        $("#permissionStartUser_view").val(map.permissionStartUserView);
		        $("#permissionStartRole").val(map.permissionStartRole);
		        $("#permissionStartRole_view").val(map.permissionStartRoleView);
		        $("#permissionStartTeam").val(map.permissionStartTeam);
		        $("#permissionStartTeam_view").val(map.permissionStartTeamView);
			} else {
				layer.alert(result.msg);
			}
		}
	});
}

