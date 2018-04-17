<%@ page language="java" contentType="text/html; charset=UTF-8"
    isErrorPage="true"
    pageEncoding="UTF-8"%>
<div class="display_container2">
		<div class="display_content2">
			<div class="top">
				群组人员分配
				<div class="query_user">
					<div>
						<form class="form-inline" method="post" action="sysUser/userList"  onsubmit="return searchReturn(this,setUserList);">
						<table>
							<tbody>
								<tr>
									<td>员工编码:</td>
									<td>员工姓名:</td>
									<td></td>
								</tr>
								<tr>
									<td><input type="text" id="user_code" name="userNo" autocomplete="off" class="layui-input" /></td>
									<td><input type="text" id="user_name" name="userName" autocomplete="off" class="layui-input" /></td>
									<td><input type="submit" id="query_btn" autocomplete="off" class="layui-input" value="查询"/></td>
								</tr>
							</tbody>
						</table>
						</form>
					</div>
				</div>
			</div>
			<div class="middle_temp">	
				<div style="float:left;width:220px;height: 240px;margin:10px 0 10px 10px;">
					<!-- <ul id="treeDemo" class="ztree" style="height: 100%;width: 96%;" ></ul> -->
					<ul id="treeDemo" class="ztree" style="width: 220px; height: 250px; -moz-user-select: none;"></ul>
				</div>
				
				<div id="temp_middle" style="float:left;width:120px;height:230px;margin:10px 0 0 30px;padding:0;overflow-y:scroll;" class="show_user_div">
					<ul id="usersul" style="width:240px;display:inline;" >
					</ul>
				</div>
				<div id="temp_button">
					<br></br>
					<br></br>
					<button type="button" class="btn btn-default btn-xs" style="font-weight:800;color:blue;text-align:left;" onclick="add_user();">&nbsp;&nbsp;&gt;&nbsp;&nbsp;</button>
					<br></br>
					<button type="button" class="btn btn-default btn-xs" style="font-weight:800;color:blue;text-align:left;" onclick="delete_user();">&nbsp;&nbsp;&lt;&nbsp;&nbsp;</button>
				</div>
				<form class="layui-form form-horizontal" id="addRoleUserForm" action="sysRoleUser/insertRoleUser" style="margin-top:30px;"  onsubmit="return validateCallback(this,addUserRoleSuccess);">
					<div id="temp_right" style="float:left;width:120px;height:230px;margin-top:10px;padding:0;overflow-y:scroll;">
						<ul id="user_add" style="width:240px;display:inline;"></ul>
					</div>
					<input type="hidden" name="roleUid" id="roleUid"  />
					<input type="hidden" name="mapType" value="${param.mapType}"  />
				</form>
			</div>
			<div class="foot_temp">
				<button class="layui-btn layui-btn sure_btn" style="float:left;" type="button" onclick="$('#addRoleUserForm').submit();">确定</button>
				<button class="layui-btn layui-btn layui-btn-primary cancel_btn" style="float:left;">取消</button>
			</div>
		</div>
	</div>