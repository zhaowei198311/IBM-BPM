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
  		<style>
 			#usersul li, #user_add li{list-style-type:none;padding-left:12px;padding-top:2px;padding-bottom:2px;border-bottom:1px solid #CCC;}
 			#usersul , #user_add{list-style-type:none;padding-left:0px;width:100%;}
 			.colorli{background-color:#9DA5EC;color: white;}
 			ul{ width:200px;}
		</style>
	</head>
	<body>
		<div class="container">
			<form class="form-inline" method="post" action="sysRole/allSysRole"  onsubmit="return search(this);">
				<input type="hidden" name="pageNo" id="pageNo" value="1" >
				<input type="hidden" name="roleType" value="1" >
			</form>
			<div class="search_area">
				<div class="layui-row layui-form">					
					<div class="layui-col-md1" style="text-align:right;">
					        <button class="layui-btn create_btn" onclick="adddialog();" >新建</button>
					</div>
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
					</colgroup>
					<thead>
					    <tr>
					      <th>序号</th>
					      <th>角色名称</th>
					      <th>描述</th>
					      <th>状态</th>
					      <th>操作</th>
					    </tr> 
					</thead>
					<tbody id="tabletr">
					  <!--   <tr>
					      <td>1</td>
					      <td>普通员工</td>
					      <td>显示</td>
					      <td>待办任务，未读通知...</td>
					      <td><i class="layui-icon edit_user">&#xe642;</i> <i class="layui-icon add_user">&#xe654;</i>  <i class="layui-icon jurisdiction_btn">&#xe6b2;</i> <i class="layui-icon delete_btn">&#xe640;</i></td>
					    </tr> -->
					</tbody>
				</table>
			</div>
			<div id="pagination"></div>
		</div>
		<div class="display_container">
		<div class="display_content">
			<div class="top">
				新建角色
			</div>
			<div class="middle">
				<form class="layui-form form-horizontal" action="sysRole/addSysRole" method="post" style="margin-top:30px;"  onsubmit="return validateCallback(this,addsuccess);">
				  <div class="layui-form-item">
				    <label class="layui-form-label">角色名称</label>
				    <div class="layui-input-block">
				      <input type="text" name="roleName" required  lay-verify="required" placeholder="请输入角色名称" autocomplete="off" class="layui-input">
				    </div>
				  </div>
				  
				  <div class="layui-form-item">
				    <label class="layui-form-label">描述</label>
				    <div class="layui-input-block">
				      <input type="text" name="describe"  value="" autocomplete="off" placeholder="请输入描述" class="layui-input">
				    </div>
				  </div>
				  
				  <div class="layui-form-item">
				    <label class="layui-form-label">状态</label>
				    <div class="layui-input-block">
				      <input type="radio" name="isClosed" value="1" title="显示" checked />
				      <input type="radio" name="isClosed" value="0" title="隐藏"  />
				    </div>
				  </div>	
				  
				  <input type="hidden" name="orderIndex" value="1" />
				  <input type="hidden" name="categoryName" value="A"/>
				  <input type="hidden" name="roleType" value="1" />
				  <input type="hidden" id="submit_add" />			  
				</form>
			</div>
			<div class="foot">
				<button class="layui-btn layui-btn sure_btn" type="button" onclick="$('#submit_add').submit();" >确定</button>
				<button class="layui-btn layui-btn layui-btn-primary cancel_btn">取消</button>
			</div>
		</div>
	</div>
	<div class="display_container1">
		<div class="display_content1">
			<div class="top">
				编辑角色
			</div>
			<div class="middle">
				<form class="layui-form form-horizontal" action="sysRole/updateSysRole"  method="post" style="margin-top:30px;"  onsubmit="return validateCallback(this,updatesuccess);">
				  <div class="layui-form-item">
				    <label class="layui-form-label">角色名称</label>
				    <div class="layui-input-block">
				      <input type="text" name="roleName" required  lay-verify="required" value="普通用户" autocomplete="off" class="layui-input">
				    </div>
				  </div>
				  
				  <div class="layui-form-item">
				    <label class="layui-form-label">描述</label>
				    <div class="layui-input-block">
				      <input type="text" name="describe"  value=""  maxlength="200" autocomplete="off" placeholder="请输入描述" class="layui-input">
				    </div>
				  </div>
				  
				  <div class="layui-form-item">
				    <label class="layui-form-label">状态</label>
				    <div class="layui-input-block">
				        <input type="radio" name="isClosed" value="1" title="显示" />
				      <input type="radio" name="isClosed" value="0" title="隐藏"  />
				    </div>
				  </div>				  
				   <input type="hidden" name="roleUid" / >
				   <input type="hidden" name="orderIndex" value="1" />
				  <input type="hidden" name="categoryName" value="A" />
				  <input type="hidden" name="roleType" value="1" />
				  <input type="hidden" id="submit_upd" />		
				</form>
			</div>
			<div class="foot">
				<button class="layui-btn layui-btn sure_btn" type="button"  onclick="$('#submit_upd').submit();">确定</button>
				<button class="layui-btn layui-btn layui-btn-primary cancel_btn">取消</button>
			</div>
		</div>
	</div>
	<div class="display_container6">
		<div class="display_content6">
			<div class="top">
				授权菜单
			</div>
			<form method="post"  action="sysRoleResource/adSysRoleResource"   onsubmit="return validateCallback(this,closeResourceDialog);">
				<div class="middle" style="padding: 0px;">
					<ul id="resourceTree" class="ztree" style="width:auto;height:189px;"></ul>
				</div>
				<div class="foot">
					<button class="layui-btn layui-btn sure_btn" type="button" id="addresource">确定</button>
					<button class="layui-btn layui-btn layui-btn-primary cancel_btn" type="button">取消</button>
				</div>
				<div id="resource"></div>
				<input type="hidden" name="roleUid" id="roleUid1" />
			</form>
		</div>
	</div>
	<jsp:include page="common/role_user.jsp" >
	    <jsp:param name="mapType" value="1"/>  
	</jsp:include>
	
	<script src="resources/desmartsystem/scripts/js/myjs/role_user.js" type="text/javascript"></script>
	<script type="text/javascript" >
		$(function(){
			var url='sysDepartment/treeDisplay';
			//tree展示
			setting.callback={onClick: onClick}
			treeDisplay(url,'treeDemo');
			pageBreak($('#pageNo').val());
			$(".cancel_btn").click(function(){
				$(".display_container").css("display","none");
				$(".display_container1").css("display","none");
				$(".display_container2").css("display","none");
				$(".display_container6").css("display","none");
			});
			
			
			//新增保存
			$("#addresource").click(function(){
				var treeObj = $.fn.zTree.getZTreeObj("resourceTree");
				var nodes = treeObj.getCheckedNodes();
				var str="";
				for(var i=0;i<nodes.length;i++){
					str+="<input type='hidden' name='resourceUid' value='"+nodes[i].id+"'/>";
				}
				$("#resource").html(str);
				$(this).submit();
			});
		})
		
		
		function tabledata(dataList,data){
			 $(dataList).each(function(i){//重新生成
				var str='<tr>';
				str+='<td>' + (data.beginNum+i) + '</td>';
				str+='<td>' + this.roleName + '</td>';
				str+='<td>' + isEmpty(this.describe) + '</td>';
				if(this.isClosed==1){
	         		str+='<td>显示</td>';
	         	}else{
	         		str+='<td>隐藏</td>';
	         	}
		        str+='<td>';
		        str+='<i class="layui-icon edit_user" onclick=ajaxTodo("sysRole/getSysRole?roleUid='+this.roleUid+'","edit") >&#xe642;</i>';
		        str+='<i class="layui-icon add_user" onclick=openRoleUsers("'+this.roleUid+'")  >&#xe654;</i>';
		        /* str+='<i class="layui-icon jurisdiction_btn" onclick=openResourceDialog("'+this.roleUid+'"); >&#xe6b2;</i>'; */
		        str+='<i class="layui-icon delete_btn" onclick=ajaxTodo("sysRole/deleteSysRole?roleUid='+this.roleUid+'","del") >&#xe640;</i>';
		        str+='</td>';
	         	$("#tabletr").append(str);
	         });
		}
	</script>
	</body>
</html>
