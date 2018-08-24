$(document).ready(function () {
	$(".list").css("height","304px");
	$("#jstree1").css("height","365px");
    $('#jstree1').jstree({
        'core': {
            'data': {
                "url": function(){
                	return "sysDepartment/selectDepartmentTreeNodeByParent?lazy";
                },
                "data": function (node) {
                	if(node.id !="#"){
                		return {
                            "id": node.id
                        };
                	}
		            return {"id" : "10000000"};
                }
            }
        }
    });
    $('#jstree1').bind("activate_node.jstree", function (obj, e) {
        $('#users #usersul').text("");
        $('#users #usersul').append("<li class='search-li'>正在加载...</li>");
        var currentNode = e.node;
        $.ajax({
            type: "post",
            cache: false,
            url: "sysUser/userList",
            data: {
                "departUid": currentNode.id
            },
            dataType: "json",
            error: function (xhr, ajaxOptions, thrownError) {

            },
            success: function (data) {
            	$('#users #usersul').text("");
                for (var i = 0; i < data.length; i++) {
                    var user = data[i];
                    var flag = true;
                    $('.tr_send_receiver td').each(function () {
                        if (("_" + user.userUid) == $(this).attr("id")) {
                           flag = false;
                        }
                    });
                    if(flag){
                    	$('#users #usersul').append("<li id='" + user.userUid + "' name='" 
                    			+ user.userName + "' title='"+user.station
                    			+"' onClick='selectClick(this);' ondblclick='dblSelClick(this)'>" 
                    			+ user.userName+"("+user.userUid+")" + "</li>");
                    }
                }//end for
            }
        });
    });
    
    //将原组件的值获得并放入已选列表
    var userTable = $("#receiver_table");
    userTable.empty();
    if(window.parent.document.getElementById(elementId).value!=null && window.parent.document.getElementById(elementId).value!=""){
    	var id = window.parent.document.getElementById(elementId).value.split(';');
        var name = window.parent.document.getElementById(elementId + "_view").value.split(';');
        var dataTitle = $(window.parent.document.getElementById(elementId)).attr("data-title")
        .split(';');
        for (var i = 0; i < name.length; i++) {
            if (name[i] != '') {
                var newName = name[i].replace(/\(.*?\)/g, '');
                var trHtml = "<tr class='tr_send_receiver'>" 
                    		+ "<td class='list_ul index' id='_" + id[i] 
                    		+ "' name='" + newName + "' title='"+dataTitle[i]
                    		+"' onclick='selectClick(this)' ondblclick='tdDblSelClick(this)' sendtype='send_receiver'>" 
                    		+ newName+"("+id[i]+")" + "</td>" + "</tr>";
                userTable.append(trHtml);
            }
        }
    }
    
    //保存
    $("#save").click(function(){
    	var tdArr = $("#receiver_table .tr_send_receiver td");
        if (isSingle == 'true') {
            if (tdArr.length > 1) {
                layer.alert('只能保存一个人!');
                return false;
            };
        }
        var useruid = "";
        var username = "";
        var dataTitle = "";
        tdArr.each(function () {
            useruid +=  $(this).attr('id').replace("_","")+ ";";
            username += $(this).text() + ";";
            dataTitle += $(this).attr("title")+";";
        });
        window.parent.document.getElementById(elementId).value = useruid;
        window.parent.document.getElementById(elementId + "_view").value = username;
        $(window.parent.document.getElementById(elementId)).attr("data-title",dataTitle);
        var title = username.replace(/;/g, "\n");
        if (isSingle == 'false') {
            window.parent.document.getElementById(elementId + "_view").title = title;
        }
        $('#close').click();
        $("#receiver_table .tr_send_receiver").remove();
        var inputObj = $(window.parent.document.getElementById(elementId + "_view"));
        if (inputObj.attr("onchange") != null && inputObj.attr("onchange") != "") {
            inputObj.trigger("change");
        }
    });
});

//人员检索
function search() {
	var searchVal = $(".search_txt").val();
	if(searchVal==null || searchVal==""){
		layer.msg("请输入查询条件",{icon:2,time:1000});
		return;
	}
	$(".search_txt").attr("disabled", "disabled");
    $('#users #usersul').text("");
    $('#users #usersul').append("<li class='search-li'>正在检索</li>");
    $.ajax({
        type: "post",
        cache: false,
        url: "sysUser/querySysUserByConditionPC",
        data: {
            "condition": searchVal
        },
        dataType: "json",
        error: function (xhr, ajaxOptions, thrownError) {
            //alert(thrownError);
        },
        success: function (result) {
            $('#users #usersul').text("");
            $(".search_txt").removeAttr("disabled");
            $("#user_desc").html("");
            if(result.status==0){
            	var data = result.data;
            	for (var i = 0; i < data.length; i++) {
                	var user = data[i];
                    var flag = true;
                    $('.tr_send_receiver td').each(function () {
                        if (("_" + user.userUid) == $(this).attr("id")) {
                           flag = false;
                        }
                    });
                    if(flag){
                    	$('#users #usersul').append("<li id='" + user.userUid + "' name='" 
                    			+ user.userName + "' title='"+user.station
                    			+"' onClick='selectClick(this);'  ondblclick='dblSelClick(this)'>" 
                    			+ user.userName+"("+user.userUid+")" + "</li>");
                    }
                }
            }
        }//end success
    });
}

//添加按钮
$("#btn_add_user").click(function () {
    $("#usersul li").each(function () {
        var alreadyHas = "false";
        var selectUser = $(this);
        if ($(this).hasClass('list_active')) {
            //判断是否已经添加
            $('.tr_send_receiver td').each(function () {
                if (("_" + $(selectUser).attr("id")) == $(this).attr("id")) {
                    alreadyHas = "true";
                }
            });

            if (alreadyHas != "true") {
                $("#receiver_table").append("<tr class='tr_send_receiver'>" 
                		+ "<td class='list_ul index' id='_" + $(selectUser).attr('id') 
                		+ "' name='" + $(selectUser).attr('name') + "' title='"+$(selectUser).attr('title')
                		+"' onclick='selectClick(this)' ondblclick='tdDblSelClick(this)' sendtype='send_receiver'>" 
                		+ $(selectUser).attr('name')+"("+$(selectUser).attr('id')+")" + "</td>" + "</tr>");
                $(selectUser).remove();
            }
        }
    });
    $("#usersul li").each(function () {
        if ($(this).hasClass('list_active')) {
            $(this).removeClass('list_active');
        }
    });
});

//删除按钮
$("#single_delete").click(function () {
    $("#receiver_table .tr_send_receiver td").each(function () {
        if ($(this).hasClass("list_active")) {
        	var userUid = $(this).attr('id').replace("_","");
        	var liHtml = '<li id="'+userUid+'" name="'
        		+$(this).attr('name')+'" title="'+$(this).attr('title')
        		+'" onclick="selectClick(this);" ondblclick="dblSelClick(this)">'+$(this).attr('name')+'('+userUid+')</li>';
        	$("#usersul").append(liHtml);
            $(this).parent().remove();
            $(this).remove();
        }
    });
});

//全部删除
$("#all_delete").click(function () {
    $("#receiver_table .tr_send_receiver td").each(function () {
        var userUid = $(this).attr('id').replace("_","");
    	var userName = $(this).attr('name');
    	var title = $(this).attr('title');
        var liHtml = '<li id="'+userUid+'" name="'
    		+userName+'" title="'+title
    		+'" onclick="selectClick(this);" ondblclick="dblSelClick(this)">'
    		+userName+'('+userUid+')</li>';
    	$("#usersul").append(liHtml);
    	$(this).parent().remove();
    	$(this).remove();
    });
});

//列表选中
function selectClick(_this) {
    if ($(_this).hasClass("list_active")) {
        $(_this).removeClass("list_active");
    } else {
        $(_this).addClass("list_active");
    }
}

//列表双击
function dblSelClick(obj){
	var alreadyHas = "false";
	$('.tr_send_receiver td').each(function () {
        if (("_" + $(obj).attr("id")) == $(this).attr("id")) {
            alreadyHas = "true";
        }
    });
    if (alreadyHas != "true") {
        $("#receiver_table").append("<tr class='tr_send_receiver'>" 
        		+ "<td class='list_ul index' id='_" + $(obj).attr('id') 
        		+ "' name='" + $(obj).attr('name') + "' title='"+$(obj).attr('title')
        		+"' onclick='selectClick(this)' ondblclick='tdDblSelClick(this)' sendtype='send_receiver'>" 
        		+ $(obj).attr('name')+"("+$(obj).attr('id')+")" + "</td>" + "</tr>");
        $(obj).remove();
    }
}

//表格双击
function tdDblSelClick(obj){
	var userUid = $(obj).attr('id').replace("_","");
	var liHtml = '<li id="'+userUid+'" name="'
		+$(obj).attr('name')+'" title="'+$(obj).attr('title')
		+'" onclick="selectClick(this);" ondblclick="dblSelClick(this)">'+$(obj).attr('name')+'('+userUid+')</li>';
	$("#usersul").append(liHtml);
	$(obj).parent().remove();
	$(obj).remove();
}
//输入框清除按钮
$(".search_txt").on('keyup', function (event) {
    if (event.keyCode == 13) {
        search($(this).val());
    } else {
        if ($(this).val().length == '0') {
            $(".close-nocircle").css("display", "none");
        } else {
            $(".close-nocircle").css("display", "block");
        }
    }
});