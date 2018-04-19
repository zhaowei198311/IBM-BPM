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
$(function() {
	getPermissionStart();
	getAllRoleList();
	getAllTeamList();
    $('#form1').validate({
        rules : {
            proTime : {
                number : true
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

    // “保存”按钮
    $("#save_btn").click(function () {
        if (!$("#form1").valid() || !$("#form4").valid()) {
            return;
        }

        var dataToSend = getData();
        console.log(dataToSend);
        if (!dataToSend.isOk) {
            layer.alert(dataToSend.msg);
            return;
        }
        dataToSend.proStatus = "SETTING";
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
         dataToSend.proStatus = "SETTED";
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
        var url = common.getSystemPath() + "/sysUser/select_personnel?id=permissionStartUser&isSingle=false";
        window.open(url);
    });
    
    // 选择发起角色
    $("#chooseRole_btn").click(function(){
    	if (firstChooseRole) {
    		var choosedRoles = $("#permissionStartRole").val();
        	var chooseIds = choosedRoles.split(";");
        	$(".role_wait_li").each(function() {
        		var $li = $(this);
    			var roleUid = $li.data("roleuid");
    			var roleName = $li.data("rolename");
    			if($.inArray(roleUid, chooseIds) != -1){
    				var str = '<li class="role_choose_li" data-roleuid="'+roleUid+'" data-rolename="'+roleName+'">'+roleName+'</li>';
    				$("#selectedRole_ul").append(str);
    				$li.remove();
    			}
        	});
    		firstChooseRole = false;
    	}
    	$("#chooseRole_container").show();
    });
    
    // 选择发起角色组
    $("#chooseTeam_btn").click(function(){
    	if (firstChooseTeam) {
    		var choosedTeams = $("#permissionStartTeam").val();
        	var chooseIds = choosedTeams.split(";");
        	$(".team_wait_li").each(function() {
        		var $li = $(this);
    			var teamUid = $li.data("teamuid");
    			var teamName = $li.data("teamname");
    			if($.inArray(teamUid, chooseIds) != -1){
    				var str = '<li class="team_choose_li" data-teamuid="'+teamUid+'" data-teamname="'+teamName+'">'+teamName+'</li>';
    				$("#choosedTeam_ul").append(str);
    				$li.remove();
    			}
        	});
    		firstChooseTeam = false;
    	}
    	$("#chooseTeam_container").show();
    });
    $("#chooseRole_container").on("click", "li", function(e){
    	var $li = $(e.target);   // 相当于元素绑定事件的 $(this)
    	if ($li.hasClass("colorli")) {
    		$li.removeClass("colorli");
    	} else {
    		$li.addClass("colorli");
    	}
    });
    $("#chooseTeam_container").on("click", "li", function(e){
    	var $li = $(e.target);   // 相当于元素绑定事件的 $(this)
    	if ($li.hasClass("colorli")) {
    		$li.removeClass("colorli");
    	} else {
    		$li.addClass("colorli");
    	}
    });
    $("#addRole_btn").click(function() {
    	var roleIds = [];
    	$(".role_choose_li").each(function() {
    		roleIds.push($(this).data("roleuid"));
    	});
    	$(".role_wait_li").each(function() {
    		var $li = $(this);
    		if ($li.hasClass("colorli")) {
    			var roleUid = $li.data("roleuid");
    			var roleName = $li.data("rolename");
    			if($.inArray(roleUid, roleIds) == -1){
					var str = '<li class="role_choose_li" data-roleuid="'+roleUid+'" data-rolename="'+roleName+'">'+roleName+'</li>';
					$("#selectedRole_ul").append(str);
				}
    			$li.remove();
    		}
    	});
    });
    $("#removeRole_btn").click(function() {
    	var roleIds = [];
    	$(".role_wait_li").each(function() {
    		roleIds.push($(this).data("roleuid"));
    	});
    	$(".role_choose_li").each(function() {
    		var $li = $(this);
    		if ($li.hasClass("colorli")) {
    			var roleUid = $li.data("roleuid");
    			var roleName = $li.data("rolename");
    			if($.inArray(roleUid, roleIds) == -1){
					var str = '<li class="role_wait_li" data-roleuid="'+roleUid+'" data-rolename="'+roleName+'">'+roleName+'</li>';
					$("#waitRole_ul").append(str);
				}
    			$li.remove();
    		}
    	});
    });
    $("#addTeam_btn").click(function() {
    	var teamIds = [];
    	$(".team_choose_li").each(function() {
    		teamIds.push($(this).data("teamuid"));
    	});
    	$(".team_wait_li").each(function() {
    		var $li = $(this);
    		if ($li.hasClass("colorli")) {
    			var teamUid = $li.data("teamuid");
    			var teamName = $li.data("teamname");
    			if($.inArray(teamUid, teamIds) == -1){
					var str = '<li class="team_choose_li" data-teamuid="'+teamUid+'" data-teamname="'+teamName+'">'+teamName+'</li>';
					$("#choosedTeam_ul").append(str);
				}
    			$li.remove();
    		}
    	});
    });
    $("#removeTeam_btn").click(function() {
        var teamIds = [];
        $(".team_wait_li").each(function() {
            teamIds.push($(this).data("teamuid"));
        });
        $(".team_choose_li").each(function() {
            var $li = $(this);
            if ($li.hasClass("colorli")) {
                var teamUid = $li.data("teamuid");
                var teamName = $li.data("teamname");
                if($.inArray(teamUid, teamIds) == -1){
                    var str = '<li class="team_wait_li" data-teamuid="'+teamUid+'" data-teamname="'+teamName+'">'+teamName+'</li>';
                    $("#waitTeam_ul").append(str);
                }
                $li.remove();
            }
        });
    });
    $("#chooseRole_sureBtn").click(function() {
    	var permissionStartRole = "";
    	var permissionStartRole_view = "";
    	$(".role_choose_li").each(function() {
    		var $li = $(this);
    		permissionStartRole += $li.data("roleuid") + ";";
    		permissionStartRole_view += $li.data("rolename") + ";";
    	});
    	$("#permissionStartRole").val(permissionStartRole);
    	$("#permissionStartRole_view").val(permissionStartRole_view);
    	$("#chooseRole_container").hide();
    });
    $("#chooseRole_cancelBtn").click(function() {
    	$("#chooseRole_container").hide();
    });
    $("#chooseTeam_sureBtn").click(function() {
    	var permissionStartTeam = "";
    	var permissionStartTeam_view = "";
    	$(".team_choose_li").each(function() {
    		var $li = $(this);
    		permissionStartTeam += $li.data("teamuid") + ";";
    		permissionStartTeam_view += $li.data("teamname") + ";";
    	});
    	$("#permissionStartTeam").val(permissionStartTeam);
    	$("#permissionStartTeam_view").val(permissionStartTeam_view);
    	$("#chooseTeam_container").hide();
    });
    $("#chooseTeam_cancelBtn").click(function() {
    	$("#chooseTeam_container").hide();
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
        if (trigger.triParam.length > 20) {
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
function getAllRoleList() {
	$.ajax({
		url: common.getSystemPath() + "/sysRole/roleList",
		dataType: "json",
		type: "post",
		data: {},
		success: function(list) {
			var str = "";
			for (var i = 0; i < list.length; i++) {
				var role = list[i];
				var roleUid = role.roleUid;
				var roleName = role.roleName;
				str += '<li class="role_wait_li" data-roleuid="'+roleUid+'" data-rolename="'+roleName+'">'+roleName+'</li>';
			}
			$("#waitRole_ul").append(str);
		}
		
	});
}
function getAllTeamList() {
	$.ajax({
		url: common.getSystemPath() + "/sysTeam/sysTeamList",
		dataType: "json",
		type: "post",
		data: {},
		success: function(list) {
			var str = "";
			for (var i = 0; i < list.length; i++) {
				var team = list[i];
				var teamUid = team.teamUid;
				var teamName = team.teamName;
				str += '<li class="team_wait_li" data-teamuid="'+teamUid+'" data-teamname="'+teamName+'">'+teamName+'</li>';
			}
			$("#waitTeam_ul").append(str);
		}
		
	});
}