<%@ page language="java" contentType="text/html; charset=UTF-8"
    isErrorPage="true"
    pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<base href="<%=basePath%>">
		<meta charset="utf-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
  		<title>待办任务</title>
  		<%@ include file="common/common.jsp" %>
  		
	</head>
	<body>
		<div class="container">
			<div class="search_area">
				<div class="layui-row layui-form">
				<form class="form-inline" method="post" action="sysUser/allSysUser"  onsubmit="return search(this);">
					<input type="hidden" name="pageNo" id="pageNo" value="1" >
					<div class="layui-col-md2">
				    	<input type="text"  placeholder="姓名"  name="userName" class="layui-input">
					</div>
					<div class="layui-col-md2">
						<input type="text" placeholder="用户名"  name="userId"  class="layui-input">
					</div>
					<div class="layui-col-md2">
						<input type="text"  placeholder="部门"  name="departName" class="layui-input" >
					</div>
					<div class="layui-col-md2">
					    <input type="text"  placeholder="类型"  name="departName" class="layui-input">				    
					</div>
					<div class="layui-col-md1" style="text-align:right;">
					        <button class="layui-btn" >查询</button>
					</div>
					<div class="layui-col-md1" style="text-align:right;">
					        <button class="layui-btn create_btn" >新建</button>
					</div>
				</form>
				</div>
			</div>
			<div>				
				<table class="layui-table backlog_table" lay-even lay-skin="nob">
					<colgroup>
					    <col>
					    <col>
					    <col>
					    <col>
					    <col> 
					    <col>
					    <col>
					</colgroup>
					<thead>
					    <tr>
					      <th>序号</th>
					      <th>姓名</th>
					      <th>用户名</th>
					      <th>部门</th>
					      <th>类型</th>
					      <th>联系电话</th>
					      <th>操作</th>
					    </tr> 
					</thead>
					<tbody id="tabletr">
					</tbody>
				</table>
			</div>
			<div id="pagination"></div>
		</div>
		
		<div class="display_container">
		<div class="display_content">
			<div class="top">
				新建用户
			</div>
			<div class="middle">
				<form class="layui-form form-horizontal" action="sysResource/addSysResource" style="margin-top:30px;"  onsubmit="return validateCallback(this,addsuccess);">
				  <div class="layui-form-item">
				    <label class="layui-form-label">用户名称</label>
				    <div class="layui-input-block">
				      <input type="text" name="resouceName" required  lay-verify="required" placeholder="请输入模块权限名称" autocomplete="off" class="layui-input">
				    </div>
				  </div>
				   <input type="hidden" id="submit_add" />	
				</form>
			</div>
			<div class="foot">
				<button class="layui-btn layui-btn sure_btn" type="button" onclick="$('#submit_add').submit();" >确定</button>
				<button class="layui-btn layui-btn layui-btn-primary cancel_btn" type="button">取消</button>
			</div>
		</div>
	</div>
	
	<div class="display_container1">
			<div class="display_content1">
				<div class="top">
					绑定系统角色
				</div>
				<form class="form-horizontal" action="sysRoleUser/addSysRoleUser"  onsubmit="return validateCallback(this,addsuccess);">
				<div class="middle">
					<table class="layui-table backlog_table" lay-even  lay-skin="nob">
						<colgroup>
							<col>
						    <col>
						</colgroup>
						<thead>
						    <tr>
						      <th><input type="checkbox" id="checkAll"  lay-skin="primary"  />序号</th>
						      <th>角色名称</th>
						    </tr> 
						</thead>
						<tbody id="systemRoleTable">
						    <!-- <tr>
						      <td><input type="checkbox" name="" title='全选' lay-skin="primary"> 1</td>
						      <td>系统管理员</td>
						    </tr>
						    <tr>
						      <td><input type="checkbox" name="" title='全选' lay-skin="primary"> 2</td>
						      <td>流程管理员</td>
						    </tr>
						    <tr>
						      <td><input type="checkbox" name="" title='全选' lay-skin="primary"> 3</td>
						      <td>普通用户</td>
						    </tr> -->
						</tbody>
					</table>		
				</div>
				<div class="foot">
					<button class="layui-btn layui-btn sure_btn" type="submit">确定</button>
					<button class="layui-btn layui-btn layui-btn-primary cancel_btn" type="button">取消</button>
				</div>
				<input  type="hidden" name="userUid" id="userUid"/>
				<input  type="hidden" name="departUid" id="departUid"/>
				</form>
			</div>
		</div>
	
	
	<div class="display_container">
			<div class="display_content">
				<div class="top">
					绑定业务角色
				</div>
				<form class="form-horizontal" action="sysResource/addSysResource"  onsubmit="return validateCallback(this,addsuccess);">
				<div class="middle">
					<table class="layui-table backlog_table" lay-even lay-skin="nob">
						<colgroup>
							<col>
						    <col>
						</colgroup>
						<thead>
						    <tr>
						      <th><input type="checkbox" name="" title='全选' lay-skin="primary"> 序号</th>
						      <th>角色名称</th>
						    </tr> 
						</thead>
						<tbody>
						    <tr>
						      <td><input type="checkbox" name="" title='全选' lay-skin="primary"> 1</td>
						      <td>店长</td>
						    </tr>
						    <tr>
						      <td><input type="checkbox" name="" title='全选' lay-skin="primary"> 2</td>
						      <td>经理</td>
						    </tr>
						    <tr>
						      <td><input type="checkbox" name="" title='全选' lay-skin="primary"> 3</td>
						      <td>区域经理</td>
						    </tr>
						    <tr>
						      <td><input type="checkbox" name="" title='全选' lay-skin="primary"> 4</td>
						      <td>总监</td>
						    </tr>
						    <tr>
						</tbody>
					</table>		
				</div>
				<input  type="hidden" name="userUid" class="userUid"/>
				<input  type="hidden" name="departUid" class="departUid"/>
				
				<div class="foot">
					<button class="layui-btn layui-btn sure_btn" type="submit">确定</button>
					<button class="layui-btn layui-btn layui-btn-primary cancel_btn" type="button">取消</button>
				</div>
				</form>
			</div>
		</div>
	
		<script>
		$(function(){
			$(".create_btn").click(function(){
				window.location.href="new_user.html";
			})
			$(".edit_user").click(function(){
				window.location.href="edit_user.html";
			})
			
			$("#checkAll").click(function(){   
			    if(this.checked){   
			        $("#systemRoleTable :checkbox").prop("checked", true);  
			    }else{   
					$("#systemRoleTable :checkbox").prop("checked", false);
			    }   
			});
			
			pageBreak($('#pageNo').val());
			
			$(".cancel_btn").click(function(){
				$(".display_container").css("display","none");
				$(".display_container1").css("display","none");
			})
		})
		
		//table数据显示
		function tabledata(dataList,data){
				$(dataList).each(function(i){//重新生成
				var str='<tr>';
				str+='<td>' + (data.beginNum+i) + '</td>';
	         	str+='<td>' + this.userName + '</td>';
	         	str+='<td>' + this.userId + '</td>';
	         	str+='<td>' + this.userNo + '</td>';
	         	str+='<td>' + this.userNo + '</td>';
	         	str+='<td>' + this.mobile + '</td>';
	         	str+='<td>';
	         	str+='<i class="layui-icon link_role" title="绑定业务角色" onclick=openBusinessRoleBindings("'+this.userUid+'","'+this.departUid+'"); >&#xe612;</i>';
	         	str+='<i class="layui-icon link_system" title="绑定系统角色"  onclick=openSystemRoleBinding("'+this.userUid+'","'+this.departUid+'"); >&#xe614;</i>';
	         	str+='<i class="layui-icon edit_user" title="查看详情">&#xe60a;</i>';
		        str+='</td>';
	         	str+='</tr>';
	         	$("#tabletr").append(str);
	         });
		}
		
		
		//打开业务角色绑定
		function openBusinessRoleBindings(userUid,departUid){
			$('.userUid').val(userUid);
			$('.departUid').val(departUid);
			$(".display_container1").css("display","block");
			$("#systemRoleTable").empty();
			$.ajax({
				type:'POST',
				url:'sysRole/roleList?roleType=1',
				dataType:"json",
				cache: false,
				success: function(data){
					$(data).each(function(i){
						var str='<tr>';
						str+='<td><input type="checkbox" name="roleUid" value="'+this.roleUid+'" lay-skin="primary">'+   (i+1)+'</td>';
						str+='<td>'+this.roleName+'</td>';
						str+='</tr>';
						$("#systemRoleTable").append(str);
					});
				}
			});
		}
		
		
		function check_all(){
			var chknum = $("#systemRoleTable :checkbox").length;//选项总个数
			var chk = 0;
			$("#systemRoleTable :checkbox").each(function () {  
		        if($(this).prop("checked")==true){
					chk++;
				}
		    });
			if(chknum==chk){//全选
				console.log('fasd');
				$("#systemRoleTable").prop("checked",true);
			}else{//不全选
				console.log('fasd');
				$("#systemRoleTable").prop("checked",false);
			}
		}
		
		
		//打开系统角色绑定
		function openSystemRoleBinding(userUid,departUid){
			$('#userUid').val(userUid);
			$('#departUid').val(departUid);
			$(".display_container1").css("display","block");
			$("#systemRoleTable").empty();
			$.ajax({
				type:'POST',
				url:'sysRole/roleList?roleType=0',
				dataType:"json",
				cache: false,
				success: function(data){
					$(data).each(function(i){
						var str='<tr>';
						str+='<td><input type="checkbox" name="roleUid" value="'+this.roleUid+'" lay-skin="primary">'+   (i+1)+'</td>';
						str+='<td>'+this.roleName+'</td>';
						str+='</tr>';
						$("#systemRoleTable").append(str);
					});
				}
			});
		}
		
		
	</script>
	</body>
	
</html>
