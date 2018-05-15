<!DOCTYPE html>
<%@ page language="java"  pageEncoding="UTF-8"%>
<html>
<head>
    <title>请选择角色</title>
    <%@include file="head.jsp" %>
    <%@include file="tag.jsp" %>
    <style>
        .layui-form-label{width:140px;}
        .layui-input-block{margin-left:170px;}
        .choose_tri{cursor:pointer;}
        .display_container3 {
            height: 600px;
        }
        .layui-form-item .layui-input-inline{width:70%;}
        .colorli {
            background-color: #9DA5EC;
            color: white;
        }
        .show_user_div,.select_ul{border:1px solid #ccc;}
        .show_user_div ul li,.select_ul li{height:35px;line-height:35px;list-style:none;padding:0 10px}
        .foot_temp{text-align:right;margin-top:20px}
        #my_div {
            width: 750px;
            border: aliceblue;
            margin: 20px;
        }
    </style>
</head>
<body>
    <div id="my_div">
        <div class="top">选择角色</div>
        <div class="middle_temp">   
            <div id="temp_left" style="float:left;width:290px;height:350px;margin:10px  0 0 10px;padding:10px;overflow-y:scroll;" class="show_user_div">
            
            <div class="layui-row" style="margin-bottom:10px;">
                    <div class="layui-col-sm9">
                        <input type="text" class="layui-input" id="search_input" style="height:30px;"/>
                    </div>
                    <div class="layui-col-sm3" style="text-align:right;">
                        <button  class="layui-btn layui-btn-sm"  id="search_btn">查询</button>
                    </div> 
            </div>
                <ul id="left_ul"  style="width:240px;display:inline;"  >
                </ul>
            </div>
            
            <div id="temp_button"  style="float:left;width:120px;height:350px;text-align:center;">
                <br></br>
                <br></br>
                <button type="button" id="addRole_btn" class="layui-btn layui-btn-sm" style="font-weight:800;text-align:left;" >&nbsp;&nbsp;&gt;&nbsp;&nbsp;</button>
                <br></br>
                <button type="button" id="removeRole_btn" class="layui-btn layui-btn-sm" style="font-weight:800;text-align:left;" >&nbsp;&nbsp;&lt;&nbsp;&nbsp;</button>
            </div>
            <div id="temp_right" class="select_ul" style="float:left;width:280px;height:350px;margin-top:10px;padding:10px;overflow-y:scroll;">
                <ul id="right_ul"  style="width:240px;display:inline;">
                </ul>
            </div>
            <h1 style="clear:both;"></h1>
        </div>
        <div class="foot_temp">
            <button class="layui-btn layui-btn sure_btn" id="sure_btn">确定</button>
            <button class="layui-btn layui-btn layui-btn-primary cancel_btn" id="cancel_btn" style="margin-right: 10px;">取消</button>
        </div>
    </div>
<script type="text/javascript" src="<%=basePath%>/resources/desmartbpm/js/layui.all.js"></script>
<script>
var elementId = '${id}';
var isSingle = '${isSingle}';
$(function () {
    $.ajax({
        url: common.getSystemPath() + "/sysRole/roleList",
        dataType: "json",
        type: "post",
        data: {},
        success: function (list) {
            init(list)
        }
    });
 
    // 过滤
    $("#search_btn").click(function () {
        var inputValue = $('#search_input').val().trim().toUpperCase();
        var liList = $('.left_li');
        if (!inputValue) {
            liList.show();
            return;
        }
        for (var i = 0; i < liList.length; i++) {
            var $li = liList.eq(i);
            var roleUid = $li.data("roleuid");
            var roleName = $li.data("rolename") + "";
            if ((roleName.toUpperCase()).indexOf(inputValue) == -1) {
                $li.hide();
            } else {
                $li.show();
            }
        }
    });
 
    // 移到右侧
    $("#addRole_btn").click(function () {
        var roleIds = [];
        $(".right_li").each(function () {
            roleIds.push($(this).data("roleuid"));
        });
 
        $(".left_li").each(function () {
            var $li = $(this);
            if ($li.hasClass("colorli")) {
                var roleUid = $li.data("roleuid");
                var roleName = $li.data("rolename");
                if ($.inArray(roleUid, roleIds) == -1) {
                    var str = '<li class="right_li" data-roleuid="' + roleUid + '" data-rolename="' + roleName +
                        '">' + roleName + '</li>';
                    $("#right_ul").append(str);
                }
                $li.remove();
            }
        });
    });
    
    $("#my_div").on("click", "li", function (e) {
        var $li = $(e.target); // 相当于元素绑定事件的 $(this)
        if ($li.hasClass("colorli")) {
            $li.removeClass("colorli");
        } else {
            $li.addClass("colorli");
        }
    });
    // 移到左侧
    $("#removeRole_btn").click(function () {
        var roleIds = [];
        $(".left_li").each(function () {
            roleIds.push($(this).data("roleuid"));
        });
        $(".right_li").each(function () {
            var $li = $(this);
            if ($li.hasClass("colorli")) {
                var roleUid = $li.data("roleuid");
                var roleName = $li.data("rolename");
                if ($.inArray(roleUid, roleIds) == -1) {
                    var str = '<li class="left_li" data-roleuid="' + roleUid + '" data-rolename="' + roleName +
                        '">' + roleName + '</li>';
                    $("#left_ul").append(str);
                }
                $li.remove();
            }
        });
    });
    $("#cancel_btn").click(function () {
        //window.parent.closeChildWindow();
 
    });
    $("#sure_btn").click(function () {
        var elementValue = "";
        var elementValue_view = "";
        if (isSingle == "true" && $(".right_li").length > 1) {
            layer.alert("只能选择一个");
            return;
        }
        $(".right_li").each(function () {
            var $li = $(this);
            elementValue += $li.data("roleuid") + ";";
            elementValue_view += $li.data("rolename") + ";";
        });
        window.parent.document.getElementById(elementId).value = elementValue;
        window.parent.document.getElementById(elementId + '_view').value = elementValue_view;
        //window.parent.closeChildWindow();
    });
});
 
function init(list) {
    var str = "";
    for (var i = 0; i < list.length; i++) {
        var role = list[i];
        if (role.roleType != 1) {
            continue;
        }
        var roleUid = role.roleUid;
        var roleName = role.roleName;
        str += '<li class="left_li" data-roleuid="' + roleUid + '" data-rolename="' + roleName + '">' + roleName +
            '</li>';
    }
    $("#left_ul").append(str);
 
    var choosedValue = window.parent.document.getElementById(elementId).value;
    if (!choosedValue) {
        return;
    }
    var chooseIds = choosedValue.split(";");
    $(".left_li").each(function () {
        var $li = $(this);
        var roleUid = $li.data("roleuid");
        var roleName = $li.data("rolename");
        if ($.inArray(roleUid, chooseIds) != -1) {
            var str = '<li class="right_li" data-roleuid="' + roleUid + '" data-rolename="' + roleName + '">' +
                roleName + '</li>';
            $("#right_ul").append(str);
            $li.remove();
        }
    });
}    
</script>
</body>
</html>