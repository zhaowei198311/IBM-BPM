<%@ page language="java"  pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="renderer" content="webkit">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <title>角色绑定</title>
    <%@include file="common/common.jsp" %>
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
        .foot_temp{text-align:right;}
        #my_div {
            width: 750px;
            border: aliceblue;
            margin: 20px;
        }
        li{font-size: 14px;}
    </style>
</head>
<body>
    <div id="my_div">
    	<div class="top" style="height: 45px;">角色绑定</div>
        <div>选择角色</div>
        <div class="middle_temp" style="top: 0px;">   
            <div id="temp_left" style="float:left;width:270px;height:350px;margin:10px  0 0 10px;padding:10px;overflow-y:scroll;" class="show_user_div">
            
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
            <div id="temp_right" class="select_ul" style="float:left;width:270px;height:350px;margin-top:10px;padding:10px;overflow-y:scroll;">
                <ul id="right_ul"  style="width:240px;display:inline;">
                </ul>
            </div>
            <h1 style="clear:both;"></h1>
        </div>
        <div class="foot_temp">
<!--             <button class="layui-btn layui-btn sure_btn" id="sure_btn">确定</button> -->
            <button class="layui-btn layui-btn layui-btn-primary cancel_btn" id="cancel_btn" style="margin-right: 10px;">关闭</button>
        </div>
    </div>
<script type="text/javascript" src="resources/desmartbpm/js/layui.all.js"></script>
<script>

var userUid = '${userUid}';
$(function () {
	
	init();
	
    // 过滤
    $("#search_btn").click(function () {
    	searchRole();
    });
 
    // 移到右侧
    $("#addRole_btn").click(function () {
    	
        var roleIds = [];
        $(".left_li").each(function () {
        	 var $li = $(this);
        	if ($li.hasClass("colorli")) {
        		roleIds.push($(this).data("roleuid"));
            }
        });
        
        if(roleIds.length==0){
        	return false;	
        }
        
        $.ajax({
            url: "sysRoleUser/addRoleUser",
            dataType: "json",
            type: "post",
            data: {roleUid:roleIds.join(","),mapType:1,userUid:userUid},
            success: function (data) {
            	if(data.msg=='success'){
            		init();
            	}else{
            		layer.alert('绑定失败!');
            	}
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
        var mapUids = [];
        $(".right_li").each(function () {
        	var $li = $(this);
            if ($li.hasClass("colorli")) {
	        	mapUids.push($(this).data("mapuid"));
            }
        });
        
        if(mapUids.length==0){
        	return false;	
        }
        
        $.ajax({
            url: "sysRoleUser/deleteSysRoleUsers",
            dataType: "json",
            type: "post",
            data: {mapUids:mapUids},
            success: function (data) {
            	if(data.msg=='success'){
            		init();
            	}else{
            		layer.alert('解绑失败!');
            	}
            }
        });
    });
    $("#cancel_btn").click(function () {
        //window.parent.closeChildWindow();
    });
});


function searchRole(){
	$.ajax({
        url: "sysRole/roleList",
        dataType: "json",
        type: "post",
        data: {roleType:1,isClosed:1,roleName:$('#search_input').val()},
        success: function (list) {
        	$("#left_ul").empty();
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
        }
    });
}


function init() {
	$.ajax({
		type:'POST',
		url:'sysRoleUser/allSysRoleUser',
		dataType:"json",
		data:{mapType:1,userUid:userUid,isClosed:1},
		cache: false,
		success: function(data){
			
			$("#right_ul").empty();
			 $(data).each(function () {
				var str = '<li class="right_li" data-roleuid="' + this.roleUid + '" data-mapuid="' + this.mapUid + '" data-rolename="' + this.roleName + '">' +this.roleName + '</li>';
				$("#right_ul").append(str);
			 });
			 
			 
			 $.ajax({
		        url: "sysRole/roleList",
		        dataType: "json",
		        type: "post",
		        data: {roleType:1,isClosed:1,roleName:$('#search_input').val()},
		        success: function (list) {
		        	$("#left_ul").empty();
		        	var str = "";
		            for (var i = 0; i < list.length; i++) {
		                var role = list[i];
		                if (role.roleType != 1) {
		                    continue;
		                }
		                
		                var roleUid = role.roleUid;
		                var roleName = role.roleName;
		                
		                var bol=false;
		                for (var j = 0; j < data.length; j++) {
		                	if(roleUid==data[j].roleUid){
		                		bol=true;
		                		break;	
		                	}
						}
		               	if(bol){
		               		continue;
		               	}
		                str += '<li class="left_li" data-roleuid="' + roleUid + '" data-rolename="' + roleName + '">' + roleName +
		                    '</li>';
		            }
		            $("#left_ul").append(str);
		        }
		    });
		}
    });
	
}    
</script>
</body>
</html>