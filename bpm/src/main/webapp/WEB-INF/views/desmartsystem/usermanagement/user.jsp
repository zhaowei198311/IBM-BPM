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
  		<script src="resources/desmartsystem/scripts/js/myjs/department.js" type="text/javascript"></script>
	</head>
	<body>
		<div class="layui-container" style="margin-top:20px;width:100%;">  
		  	<div class="layui-row">
			    <div class="layui-col-md2" style="text-align: left;">
					<div class="tree" id="demo">
						<ul id="treeDemo2" class="ztree" style="width:auto;height:500px;"></ul>
					</div>
			    </div>
			    <div class="layui-col-md10">
			    	<form class="form-inline" method="post" action="sysUser/allSysUser"  onsubmit="return search(this);">
					<div class="search_area">
								<input type="hidden" name="pageNo" id="pageNo" value="1" >
								<div class="layui-col-md2">
							    	<input type="text"  placeholder="姓名"  name="userName" class="layui-input">
								</div>
								<div class="layui-col-md2">
									<input type="text" placeholder="用户名"  name="userNo"  class="layui-input">
								</div>
								<div class="layui-col-md2">
								<input type="hidden" name="departUid" id="departParent" >
									<input type="text"  placeholder="部门"  name="departName" class="layui-input" />
								</div>
								<!-- <div class="layui-col-md2">
								    <input type="text"  placeholder="类型"  name="employeeType" class="layui-input">				    
								</div> -->
								<div class="layui-col-md1" style="text-align:right;">
								        <button class="layui-btn" >查询</button>
								</div>
								<div class="layui-col-md1" style="text-align:right;">
								    <button class="layui-btn create_btn" >新建</button>
								</div>
								<div class="layui-col-md1" style="text-align:right;margin-left: 20px;">
								   <button class="layui-btn" type="button" onclick="synchronizationAppointUser();" >同步指定用户</button>
								</div>
					</form>
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
							      <!-- <th>类型</th> -->
							      <th>联系电话</th>
							      <th>操作</th>
							    </tr>  
							</thead>
							<tbody id="tabletr"></tbody>
						</table>
						<div id="pagination"></div>
					</div>
			    </div>
		  	</div>
		
	
	<div class="display_container3">
		<div class="display_content3" style="min-height: auto;padding-bottom:10px;">
			<div class="top">
				新建用户
			</div>
				<form  action="sysUser/addSysUser" method="post"   onsubmit="return validateCallback(this,addsuccess3);">
			<div class="middle" style="height: auto;">
				   <div class="layui-form-item" style="padding-top:20px;">
				    <label class="layui-form-label">工号</label>
				    <div class="layui-input-block">
				      <input type="text" name="userUid" required  lay-verify="required" remote="sysUser/userexists" placeholder="请输入工号" data-msg-remote="工号已存在请重新输入!" autocomplete="off" class="layui-input number" minlength="8" />
				    </div>
				  </div>
				  <div class="layui-form-item">
				    <label class="layui-form-label">姓名</label>
				    <div class="layui-input-block">
				      <input type="text" name="userName" required  lay-verify="required" placeholder="请输入姓名" autocomplete="off" class="layui-input" />
				    </div>
				  </div>
				  <div class="layui-form-item">
				    <label class="layui-form-label">邮箱</label>
				    <div class="layui-input-block">
				      <input type="text" name="email" required  lay-verify="required" placeholder="请输入邮箱" autocomplete="off" class="layui-input" />
				    </div>
				  </div>
				  <div class="layui-form-item">
				    <label class="layui-form-label">手机号码</label>
				    <div class="layui-input-block">
				      <input type="text" name="mobile" required  lay-verify="required" placeholder="请输入手机号码" autocomplete="off" class="layui-input" />
				    </div>
				  </div>
				   <div class="layui-form-item">
				    <label class="layui-form-label">成本中心</label>
				    <div class="layui-input-block">
				      <input type="text" name="costCenter" required  lay-verify="required" placeholder="请输入成本中心" autocomplete="off" class="layui-input" />
				    </div>
				  </div>
				   <!-- <div class="layui-form-item">
				    <label class="layui-form-label">部门</label>
				    <div class="layui-input-block">
				    	<div class="tree" id="demo"  >
				    		<input type="hidden"  name="departmetNumber" id="departmetNumber"/>
							<ul id="treeDemo"  class="ztree" departmetNumber="departmetNumber" style="width:auto;height:130px;"></ul>
						</div>
				    </div>
				  </div> -->
			</div>
			<div class="foot">
				<input  type="hidden" name="accountType" value="1"/>
				<button class="layui-btn layui-btn sure_btn" type="submit"  >确定</button>
				<button class="layui-btn layui-btn layui-btn-primary cancel_btn" type="button">取消</button>
			</div>
			</form>
		</div>
	</div>
	
	
	<div class="display_container4">
		<div class="display_content4" style="min-height: auto;">
			<div class="top">
				修改用户
			</div>
				<form  action="sysUser/updateSysUser" method="post"   onsubmit="return validateCallback(this,updatesuccess1);">
			<div class="middle" style="height: auto;">
				   <div class="layui-form-item" style="margin-top:20px;">
				    <label class="layui-form-label">工号</label>
				    <div class="layui-input-block">
				      <input type="text" name="userUid" required  readonly="readonly" lay-verify="required" placeholder="请输入工号" autocomplete="off" class="layui-input" />
				    </div>
				  </div>
				  <div class="layui-form-item">
				    <label class="layui-form-label">姓名</label>
				    <div class="layui-input-block">
				      <input type="text" name="userName" required  lay-verify="required" placeholder="请输入姓名" autocomplete="off" class="layui-input" />
				    </div>
				  </div>
				  <div class="layui-form-item">
				    <label class="layui-form-label">邮箱</label>
				    <div class="layui-input-block">
				      <input type="text" name="email" required  lay-verify="required" placeholder="请输入邮箱" autocomplete="off" class="layui-input" />
				    </div>
				  </div>
				  <div class="layui-form-item">
				    <label class="layui-form-label">手机号码</label>
				    <div class="layui-input-block">
				      <input type="text" name="mobile" required  lay-verify="required" placeholder="请输入手机号码" autocomplete="off" class="layui-input" />
				    </div>
				  </div>
				   <div class="layui-form-item">
				    <label class="layui-form-label">成本中心</label>
				    <div class="layui-input-block">
				      <input type="text" name="costCenter" required  lay-verify="required" placeholder="请输入成本中心" autocomplete="off" class="layui-input" />
				    </div>
				  </div>
				  <!-- <div class="layui-form-item">
				    <label class="layui-form-label">部门名称</label>
				    <div class="layui-input-block">
				      <input type="text" name="departName"  id="departName"  required  lay-verify="required" placeholder="请输入部门" autocomplete="off" class="layui-input" />
				    </div>
				  </div> -->
				  
				   <!-- <div class="layui-form-item">
				    <label class="layui-form-label">部门</label>
				    <div class="layui-input-block">
				    	<div class="tree" id="demo"  >
				    		<input type="hidden"  name="companyNumber" id="companyNumber_b"/>
				    		<input type="hidden"  name="departUid" id="departUid_b"/>
				    		<input type="hidden"  name="departmetNumber" id="departmetNumber_b"/>
							<ul id="treeDemo1"  class="ztree" depart="departUid_b" company="companyNumber_b" departmetNumber="departmetNumber_b" style="width:auto;height:130px;"></ul>
						</div>
				    </div>
				  </div> -->
			</div>
			<div class="foot">
				<!-- <input  type="hidden" name="userUid" /> -->
				<button class="layui-btn layui-btn sure_btn" type="submit"  >确定</button>
				<button class="layui-btn layui-btn layui-btn-primary cancel_btn" type="button">取消</button>
			</div>
			</form>
		</div>
	</div>
	
	<div class="display_container1">
			<div class="display_content1">
				<div class="top">
					绑定系统角色
				</div>
				<form class="form-horizontal" action="sysRoleUser/addRoleUser" method="post"   onsubmit="return validateCallback(this,addsuccess1);">
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
					<button class="layui-btn layui-btn layui-btn-primary cancelUserDepartmentBtn" type="button">取消</button>
				</div>
				<input  type="hidden" name="userUid" id="userUid"/>
				<input  type="hidden" name="departUid" id="departUid"/>
				<input  type="hidden" name="mapType" value="0"/>
				</form>
			</div>
		</div>
	
	
		<div class="display_container">
				<div class="display_content" style="height: auto;">
					<div class="top">
						角色绑定
					</div>
					
					<div style="height:50px;">
						<div class="layui-inline">
				      <label class="layui-form-label" style="width: auto;">角色名称：</label>
				      <div class="layui-input-inline">
				        <input name="roleName" id="jsbd_roleName"  autocomplete="off"  style="display:inline;" class="layui-input" type="tel" />
				      </div>
				      <div class="layui-input-inline" style="margin-left: 20px;">
				        	<button class="layui-btn" onclick="selectByNameRole();">查询</button>
				      </div>
				    </div>
					</div>
					<form class="form-horizontal" action="sysRoleUser/addRoleUser" method="post"   onsubmit="return validateCallback(this,addsuccess2);">
						<div class="middle">
							<table class="layui-table backlog_table" lay-even lay-skin="nob">
								<colgroup>
									<col>
								    <col>
								</colgroup>
								<thead>
								    <tr>
								      <th><input type="checkbox" name=""  id="checkAll_a"  title='全选' lay-skin="primary"> 序号</th>
								      <th>角色名称</th>
								    </tr> 
								</thead>
								<tbody id="businessRoleTable"></tbody>
							</table>		
						</div>
						<input  type="hidden" name="userUid" class="userUid"/>
						<input  type="hidden" name="departUid" class="departUid"/>
						<input  type="hidden" name="mapType" value="1"/>
						
						<div class="foot">
							<button class="layui-btn layui-btn sure_btn" type="submit">确定</button>
							<button class="layui-btn layui-btn layui-btn-primary cancel_btn" type="button">取消</button>
						</div>
					</form>
				</div>
		</div>
		
		
		
		<!-- 绑定部门对应的公司 -->
		<div class="display_container10">
			<div class="display_content10">
				<div class="top">
					<div class="layui-col-md12">绑定公司和部门</div>
				</div>
				<button class="layui-btn layui-btn-sm" id="add_department_company_btn" style="float: right;margin: 0 15px 15px 0;">添加</button>
				<div class="middle1" style="height:350px;">
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
						      <th>公司名称</th>
						      <th>公司代码</th>
						      <th>部门名称</th>
						      <th>部门代码</th>
						      <th>部门负责人</th>
						      <th>操作</th>
						    </tr>  
						</thead>
						<tbody id="tabletr2"></tbody>
					</table>	
				</div>
				<div class="foot" style="margin: 15px 10px 0;">
					<button class="layui-btn layui-btn layui-btn-primary cancel_btn" onclick="$('.display_container3').css('display','none');">取消</button>
				</div>
				</div>
			</div>
			
			
			
			<div class="display_container_company_department">
				<div class="display_content_company_department">
					<div class="top">
						添加公司和部门
					</div>
					<form class="layui-form" action="sysUserDepartment/addUserDepartment" method="post"    onsubmit="return validateCallback(this,addUserDepartment);">
					<div class="middle1" style="height: auto;">
					   <div class="layui-form-item" style="margin-top:30px;">
						    <label class="layui-form-label">公司</label>
						    <div class="layui-input-block">
						    	<select  name="companyCode" class="companyCode" id="companyCode"  style="width: 230px;"  lay-search="" ></select>
						    </div>
						  </div>
						  
						  <div class="layui-form-item">
						    <label class="layui-form-label">部门</label>
						    <div class="layui-input-block">
						    	<input type="hidden" name="departUid"  id="departUid_b" >
						    	<input type="text" name="departName" id="departName_b"  readonly="readonly"  required  lay-verify="required" autocomplete="off" class="layui-input">
						    	<ul id="departmentTree"  class="ztree"  depart="departName_b"  departmetNumber="departUid_b"  style="height: 200px;width: 96%;"></ul>
						    </div>
						  </div>
						   <div class="layui-form-item">
						    <label class="layui-form-label">是否是部门负责人</label>
						    <div class="layui-input-block">
						      <input type="radio" name="isManager" value="true" title="是">
						      <input type="radio" name="isManager" value="false" title="否" checked>
						    </div>
						  </div>
					</div>
					<div class="foot">
						<input  type="hidden" name="userUid" id="bdbm_userUid" />
						<button class="layui-btn layui-btn sure_btn" type="button"   onclick="submitUserDeaprtment(this);" >确定</button>
						<button class="layui-btn layui-btn layui-btn-primary" type="button" onclick="closeDialog('display_container_company_department');">取消</button>
					</div>
					</form>
			</div>
		</div>
	
		
		
		<!-- 同步指定用户  -->
		<div class="display_container_synAppointUser">
			<div class="display_content_synAppointUser">
				<div class="top">
					同步指定用户
				</div>
				<form class="form-horizontal" action="sysRoleUser/addRoleUser" method="post"   onsubmit="return validateCallback(this,addsuccess2);">
					<div class="middle" style="height: auto;">
					   <div class="layui-form-item" style="margin-top:20px;">
					    <label class="layui-form-label">工号</label>
					    <div class="layui-input-block">
					      <input type="text" name="costCenter" required  lay-verify="required" data-msg-required="请输入工号" placeholder="请输入工号" autocomplete="off" class="layui-input" />
					    </div>
					  </div>
					</div>
					<div class="foot">
						<button class="layui-btn layui-btn sure_btn" type="submit">确定</button>
						<button class="layui-btn layui-btn layui-btn-primary cancel_synAppointUser" onclick="closeDialog('display_container_synAppointUser');" type="button">取消</button>
					</div>
				</form>
			</div>
		</div>
	
		<script>
		$(function(){
			
			$(".create_btn").click(function(){
				//window.location.href="new_user.html";
				opendialog(dialogs.user_dialog);
			})
			$(".edit_user").click(function(){
				//alert('asdf');
				opendialog(dialogs.userEditDialog);
			})
			
			$("#checkAll").click(function(){   
			    if(this.checked){   
			        $("#systemRoleTable :checkbox").prop("checked", true);  
			    }else{   
					$("#systemRoleTable :checkbox").prop("checked", false);
			    }   
			});
			
			$("#checkAll_a").click(function(){   
			    if(this.checked){   
			        $("#businessRoleTable :checkbox").prop("checked", true);  
			    }else{   
					$("#businessRoleTable :checkbox").prop("checked", false);
			    }   
			});
			
			var url='sysDepartment/treeDisplay';
			//tree展示
			setting.callback={onClick: onClick1}
			treeDisplay(url,'treeDemo2');
			
			pageBreak($('#pageNo').val());
			
			var url='sysDepartment/treeDisplay';
			settings.callback={onClick: onClick}
			/* treeDisplays(url,['treeDemo','departmentTree']); */
			treeDisplays(url,['treeDemo1']);
			
			$(".cancelUserDepartmentBtn").click(function(){
				$(".display_container1").css("display","none");
				pageBreak($('#pageNo').val());
			})
			
			
			$(".cancel_btn").click(function(){
				$(".display_container").css("display","none");
				$(".display_container1").css("display","none");
				
				$("."+dialogs.user_dialog).css("display","none");
				$("."+dialogs.userEditDialog).css("display","none");
				
				$(".display_container10").css("display","none");
			})
		})
		
		var settings = {
			async: {
				enable: true,//是否开启异步加载模式
				url: "sysDepartment/treeDisplay",
				autoParam: ["id"]
			},
			data: {simpleData: {enable: true}}
		};
		
		function treeDisplays(url,ids){
				$.ajax({ 
			        url: url,    //后台webservice里的方法名称  
			        type: "post",  
			        dataType: "json",  
			        success: function (data) {
			        	if(typeof(ids)=='string'){
			        		$.fn.zTree.init($("#"+ids), settings, data);
			        	}else{
			        		for (var i = 0; i < ids.length; i++) {
			    				$.fn.zTree.init($("#"+ids[i]), settings, data);
			    			}
			        	}
			        }
			    });
			}
		
		
		function onClick1(e, treeId, treeNode) {
			var departUid='';
			var treeObj = $.fn.zTree.getZTreeObj("treeDemo2");
			var sNodes = treeObj.getSelectedNodes();
			if (sNodes.length > 0) {
				var node = sNodes[0].getPath();
				//console.log(node);
				for (var i = 0; i < node.length; i++) {
					departUid=node[i].code;
				}
			}
			//console.log(departUid);
			departUid=departUid.replace(/\s/g, "")
			$('#departParent').val(departUid);
			pageBreak(1);
		}
		
		function addsuccess3(data){
			returnSuccess(data,dialogs.user_dialog);
		}
		function updatesuccess1(data){
			if(data.msg=='success'){
				layer.alert(language.modify_successfully);
				dgclose(dialogs.userEditDialog);
				pageBreak();
			}else{
				layer.alert(language.modify_failed);
			}
		}
		
		
		//修改时属性菜单
		function onClick(e, treeId, treeNode) {
			var  depart=$("#"+treeId).attr("depart");
			var  departmentNo=$("#"+treeId).attr("departmetNumber");
			var treeObj = $.fn.zTree.getZTreeObj(treeId);
			var sNodes = treeObj.getSelectedNodes();
			if (sNodes.length > 0) {
				$("#"+depart).val(sNodes[0].name.replace(/\s/g, ""));
				$("#"+departmentNo).val(sNodes[0].code.replace(/\s/g, ""));
			}
		}
		
		
		//table数据显示
		function tabledata(dataList,data){
				$(dataList).each(function(i){//重新生成
				var str='<tr>';
				str+='<td>' + (data.beginNum+i) + '</td>';
	         	str+='<td>' + this.userName + '</td>';
	         	str+='<td>' + this.userUid + '</td>';
	         	str+='<td>' + depart(this.sysUserDepartmentList,this.departName) + '</td>';
	         	/* str+='<td>' + isEmpty(this.employeeType) + '</td>'; */
	         	str+='<td>' + this.mobile + '</td>';
	         	str+='<td>';
	         	str+='<i class="layui-icon" onclick=ajaxTodo("sysUser/getSysUser?userUid='+this.userUid+'","editUser") >&#xe642;</i>';
	         	str+='<i class="layui-icon link_role" title="绑定业务角色" onclick=openBusinessRoleBindings("'+this.userUid+'","'+this.departUid+'"); >&#xe612;</i>';
	         	str+='<i class="layui-icon link_system" title="绑定系统角色"  onclick=openSystemRoleBinding("'+this.userUid+'","'+this.departUid+'"); >&#xe614;</i>';
	         	str+='<i class="layui-icon link_system" title="绑定部门"  onclick=departmentOfBinding("'+this.userUid+'"); >&#xe631;</i>';
	         	str+='<i class="layui-icon" title="查看详情" onclick=userDetail("'+this.userUid+'")>&#xe60a;</i>';
	         	if(this.accountType=='1'){
	         		str+='<i class="layui-icon delete_btn" onclick=ajaxTodo("sysUser/deleteSysUser?userUid='+this.userUid+'","del") >&#xe640;</i>';
	         	}
		        str+='</td>';
	         	str+='</tr>';
	         	$("#tabletr").append(str);
	         });
		}
		
		
		
		//打开用户编辑对话框
		function editUser(data){
			var $dailogs=$('.'+dialogs.userEditDialog);
			$dailogs.css("display","block");
			$('form',$dailogs)[0].reset();
			$('form',$dailogs).formEdit(data);
			$('form',$dailogs).validate().resetForms();
			$('form',$dailogs).find("input, select, textarea").removeClass('error');
		}
		
		function userDetail(userUid){
			window.location.href="sysUser/userDetail?userUid="+userUid;
		}
		
		
		function selectByNameRole(){
			var userUid=$('.userUid').val();
			var deparUid=$('.departUid').val();
			
			var roleName=$('#jsbd_roleName').val();
			
			openBusinessRoleBindings(userUid,departUid,roleName);
		};
		
		//打开业务角色绑定
		function openBusinessRoleBindings(userUid,departUid,roleName){
			var xz = document.getElementById('checkAll_a');
			 xz.checked=false;
			$('.userUid').val(userUid);
			$('.departUid').val(departUid);
			$(".display_container").css("display","block");
			$("#businessRoleTable").empty();
			$.ajax({
				type:'POST',
				url:'sysRoleUser/allSysRoleUser?mapType=1&userUid='+userUid,
				dataType:"json",
				cache: false,
				success: function(data1){
					
					$.ajax({
						type:'POST',
						url:'sysRole/roleList?roleType=1',
						dataType:"json",
						data:{roleName:roleName},
						cache: false,
						success: function(data){
							$(data).each(function(i){
								var str='<tr>';
								var roleUid=this.roleUid
								var checkbox='<td><input type="checkbox" name="roleUid" value="'+this.roleUid+'" lay-skin="primary">'+   (i+1)+'</td>';
								for(var i=0,l=data1.length;i<l;i++){
									for(var key in data1[i]){
										if(data1[i][key]==roleUid){
											checkbox='<td><input type="checkbox" checked="checked" name="roleUid" value="'+this.roleUid+'" lay-skin="primary">'+   (i+1)+'</td>';
										}
									}
								}
								str+=checkbox;
								str+='<td>'+this.roleName+'</td>';
								str+='</tr>';
								$("#businessRoleTable").append(str);
							});
						}
					});
					
				}
			});
		}
		
		function addsuccess1(data){
			returnSuccess(data,'display_container1');
		}
		function addsuccess2(data){
			returnSuccess(data,'display_container');
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
				$("#systemRoleTable").prop("checked",true);
			}else{//不全选
				$("#systemRoleTable").prop("checked",false);
			}
		}
		
		
		//打开系统角色绑定
		function openSystemRoleBinding(userUid,departUid){
			
			var xz = document.getElementById('checkAll');
			 xz.checked=false;
			
			$('#userUid').val(userUid);
			$('#departUid').val(departUid);
			
			opendialog('display_container1');
			
			$("#systemRoleTable").empty();
			
			$.ajax({
				type:'POST',
				url:'sysRoleUser/allSysRoleUser?mapType=0&userUid='+userUid,
				dataType:"json",
				cache: false,
				success: function(data1){
					
					$.ajax({
						type:'POST',
						url:'sysRole/roleList?roleType=0',
						dataType:"json",
						cache: false,
						success: function(data){
							$(data).each(function(i){
								var index=i+1;
								var str='<tr>';
								var roleUid=this.roleUid
								var checkbox='<td><input type="checkbox" name="roleUid" value="'+this.roleUid+'" lay-skin="primary">'+   index+'</td>';
								for(var i=0,l=data1.length;i<l;i++){
									for(var key in data1[i]){
										if(data1[i][key]==roleUid){
											checkbox='<td><input type="checkbox" checked="checked" name="roleUid" value="'+this.roleUid+'" lay-skin="primary">'+ index+'</td>';
										}
									}
								}
								str+=checkbox;
								str+='<td>'+this.roleName+'</td>';
								str+='</tr>';
								$("#systemRoleTable").append(str);
							});
						}
					});
					
				}
			});
		}
		
		
		
	</script>
	</body>
	
</html>