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
			<div class="search_area">
				<form class="form-inline" method="post" action="sysTeam/allSysTeam"  onsubmit="return search(this);">
					<input type="hidden" name="pageNo" id="pageNo" value="1" >
				</form>
				<div class="layui-row layui-form">					
					<div class="layui-col-md1" style="text-align:right;">
					        <button class="layui-btn create_btn"  onclick="adddialog1();"  >新建</button>
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
					      <th>角色组名称</th>
					      <th>状态</th>
					      <th>操作</th>
					    </tr> 
					</thead>
					<tbody id="tabletr">
					</tbody>
				</table>
			</div>
			<div id="pagination"></div>
		</div>
	</body>
	<div class="display_container">
		<div class="display_content">
			<div class="top">
				新建角色组
			</div>
			<div class="middle">
				<form class="layui-form form-horizontal" method="post" action="sysTeam/addSysTeam" style="margin-top:30px;"  onsubmit="return validateCallback(this,addsuccess);">
				  <div class="layui-form-item">
				    <label class="layui-form-label">角色组名称</label>
				    <div class="layui-input-block">
				      <input type="text" name="teamName" required  lay-verify="required" placeholder="请输入角色组名称" autocomplete="off" class="layui-input">
				    </div>
				  </div>
				  
				  <div class="layui-form-item">
				    <label class="layui-form-label">上级团队</label>
				    <div class="layui-input-block">
				    	<select class="parentId" name="parentId"  id="parentId" ></select>
				    </div>
				  </div>
				  
				  <div class="layui-form-item">
				    <label class="layui-form-label">状态</label>
				    <div class="layui-input-block">
				     <input type="radio" name="isClosed" value="1" title="显示">
				      <input type="radio" name="isClosed" value="0" title="隐藏" checked>
				    </div>
				  </div>		
				   <input type="hidden" id="submit_add" />	
				   <input type="hidden" name="orderIndex" value="1"/>		  
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
				编辑角色组
			</div>
			<div class="middle">
				<form class="layui-form form-horizontal" method="post" action="sysTeam/updateSysTeam" style="margin-top:30px;"  onsubmit="return validateCallback(this,updatesuccess);">
				  <div class="layui-form-item">
				    <label class="layui-form-label">角色组名称</label>
				    <div class="layui-input-block">
				      <input type="text" name="teamName" required  lay-verify="required" value="组1" autocomplete="off" class="layui-input">
				    </div>
				  </div>
				  
				   <div class="layui-form-item">
				    <label class="layui-form-label">上级团队</label>
				    <div class="layui-input-block">
				    	<select class="parentId" name="parentId" id="parentId1" ></select>
				    </div>
				  </div>
				  
				  <div class="layui-form-item">
				    <label class="layui-form-label">状态</label>
				    <div class="layui-input-block">
				      <input type="radio" name="isClosed" value="1" title="显示">
				      <input type="radio" name="isClosed" value="0" title="隐藏" checked>
				    </div>
				  </div>	
				  <input type="hidden" name="teamUid" />
				  <input type="hidden" name="orderIndex" />
				  <input type="hidden" id="submit_upd" />			  
				</form>
			</div>
			<div class="foot">
				<button class="layui-btn layui-btn sure_btn" type="button"  onclick="$('#submit_upd').submit();">确定</button>
				<button class="layui-btn layui-btn layui-btn-primary cancel_btn">取消</button>
			</div>
		</div>
	</div>
	
	
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
					<form class="layui-form form-horizontal" id="addRoleUserForm" action="sysTeamMember/addSysTeamMember" style="margin-top:30px;"  onsubmit="return validateCallback(this,addUserRoleSuccess);">
						<div id="temp_right" style="float:left;width:120px;height:230px;margin-top:10px;padding:0;overflow-y:scroll;">
							<ul id="user_add" style="width:240px;display:inline;"></ul>
						</div>
						<input type="hidden" name="teamUid" id="teamUid"  />
					</form>
				</div>
				<div class="foot_temp">
					<button class="layui-btn layui-btn sure_btn" style="float:left;" type="button" onclick="$('#addRoleUserForm').submit();">确定</button>
					<button class="layui-btn layui-btn layui-btn-primary cancel_btn" style="float:left;">取消</button>
				</div>
			</div>
	</div>
	
</html>
	<script>
		$(function(){
			pageBreak($('#pageNo').val());
			$(".cancel_btn").click(function(){
				$(".display_container").css("display","none");
				$(".display_container1").css("display","none");
				$(".display_container2").css("display","none");
			})
			var url='sysDepartment/treeDisplay';
			setting.callback={onClick: onClick}
			treeDisplay(url,'treeDemo');
		})
		
		
		function adddialog1(){
			var select=['parentId'];
			selectoptions('sysTeam/sysTeamList',select);
			adddialog();
		};
		
		function edit1(data){
			var select=['parentId1'];
			$.ajax({  
		        url: 'sysTeam/sysTeamList',    //后台webservice里的方法名称  
		        type: "post",  
		        dataType: "json",  
		        success: function (data1) {
		        	for (var i = 0; i < select.length; i++) {
		        		$("#"+select[i]).empty();
		            	var optionstring="";
		            	$(data1).each(function(){
		            		if(data.teamUid!=this.teamUid){
		            			optionstring+="<option value=\"" + this.teamUid + "\" >" + this.teamName + "</option>";
		            		}
		            	});
		            	$("#"+select[i]).prepend(optionstring);
			        	$("#"+select[i]).first().prepend("<option value='' selected='selected'>"+language.please_select+"</option>");
					}
		        	edit(data);
		        }
		    });
		};
		
		function selectoptions(url,select){
			$.ajax({  
		        url: url,    //后台webservice里的方法名称  
		        type: "post",  
		        dataType: "json",  
		        success: function (data) {
		        	for (var i = 0; i < select.length; i++) {
		        		$("#"+select[i]).empty();
		            	var optionstring="";
		            	$(data).each(function(){
	            			optionstring+="<option value=\"" + this.teamUid + "\" >" + this.teamName + "</option>";
		            	});
		            	$("#"+select[i]).prepend(optionstring);
			        	$("#"+select[i]).first().prepend("<option value='' selected='selected'>"+language.please_select+"</option>");
					}
		        }
		    });
		}
		
		
		
		function delete_user(){
			$("#user_add .colorli").remove();
		}
		
		//群组人员分配
		function addRoleTema(data){
			opendialog(dialogs.add_team_dialog);
			$('#teamUid').val(data.teamUid);
			var $ul=$("#user_add");
			user_add_li(data.users,$ul);
		}
		
		function selectClick(_this){
			if($(_this).hasClass("colorli")){
				$(_this).removeClass("colorli");
			}else{
				$(_this).addClass("colorli");
			}
		}
		
		
		function tabledata(dataList,data){
			 $(dataList).each(function(i){//重新生成
				var str='<tr>';
				str+='<td>' + (data.beginNum+i) + '</td>';
				str+='<td>' + this.teamName + '</td>';
				if(this.isClosed==1){
	         		str+='<td>显示</td>';
	         	}else{
	         		str+='<td>隐藏</td>';
	         	}
		        str+='<td>';
		        str+='<i class="layui-icon edit_user" onclick=ajaxTodo("sysTeam/getSysTeam?teamUid='+this.teamUid+'","edit1") >&#xe642;</i>';
		        str+='<i class="layui-icon add_user" onclick=ajaxTodo("sysTeam/getSysTeam?teamUid='+this.teamUid+'","addRoleTema")  >&#xe654;</i>';
		        str+='<i class="layui-icon delete_btn" onclick=ajaxTodo("sysTeam/deleteSysTeam?teamUid='+this.teamUid+'","del") >&#xe640;</i>';
		        str+='</td>';
	         	$("#tabletr").append(str);
	         });
		}

		function addUserRoleSuccess(data){
			returnSuccess(data,dialogs.add_team_dialog);
		}
				



		function onClick(e, treeId, treeNode) {
			var departUid='';
			var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
			var sNodes = treeObj.getSelectedNodes();
			for (var i = 0; i < sNodes.length; i++) {
				departUid=sNodes[i].code;
			}
			departUid=departUid.replace(/\s/g, "")
			ajaxTodo('sysUser/userList?departUid='+departUid,'setUserList');
		}
		
		function setUserList(data){
			var $ul=$("#usersul");
			user_add_li(data,$ul);
		}

		//群组人员分配
		function addRoleTema(data){
			opendialog(dialogs.add_team_dialog);
			$('#teamUid').val(data.teamUid);
			var $ul=$("#user_add");
			user_add_li(data.members,$ul);
		}

		function user_add_li(data,element){
			var $ul=element;
			$ul.empty();
			$("#usersul").empty();
			$(data).each(function(index){
				var str='';
				str+='<li type="hidden" value="'+this.userUid+'" departUid="'+this.departUid+'" onclick="selectClick(this)" name="userUid">'+this.userName+'</li>';
				$ul.append(str);
			});
		};

		function add_user(){
			var userids = [];
			$("#user_add li").each(function(){//遍历 右边栏目的ID 
				userids.push($(this).attr('value'));//获取 所有 已添加人员
			});
			
			var index=0;
			$("#usersul li").each(function(){
				var $userLi=$(this);
				if($userLi.hasClass('colorli')){
					//判断 已添加人员
					var userUid=$userLi.attr('value');//用ID
					var departUid=$userLi.attr('departUid');//部门
					var roleUid=$userLi.attr('roleUid');
					
					
					var name=$userLi.text();
					if($.inArray(userUid, userids)==-1){
						var str='';
						str+="<li value='"+userUid+"' onclick='selectClick(this);'>"+name;
						str+="<input type='hidden' name='userUid' value='"+userUid+"'/>";
						str+="</li>";
						$("#user_add").append(str);
						index++;
					}
				}
			});
		}

	</script>