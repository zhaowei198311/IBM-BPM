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
				<input type="hidden" name="roleType" value="0" >
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
					</tbody>
				</table>
			</div>
			<div id="pagination"></div>
		</div>
	</body>
	<div class="display_container">
		<div class="display_content" style="height:auto;">
			<div class="top">
				新建角色
			</div>
			<div class="middle" style="height:auto;">
				<form class="layui-form form-horizontal" method="post"  action="sysRole/addSysRole" style="margin-top:20px;"  onsubmit="return validateCallback(this,addsuccess);">
				  <div class="layui-form-item">
				    <label class="layui-form-label">角色名称</label>
				    <div class="layui-input-block">
				      <input type="text" name="roleName" required  lay-verify="required" placeholder="请输入角色名称" autocomplete="off" class="layui-input"/>
				    </div>
				  </div>
				  <div class="layui-form-item">
				    <label class="layui-form-label">描述</label>
				    <div class="layui-input-block">
				      <input type="text" name="descrribe" value="" placeholder="请输入描述" autocomplete="off" class="layui-input" />
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
				  <input type="hidden" name="roleType" value="0" />
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
		<div class="display_content6" style="height:auto;">
			<div class="top">
				编辑角色
			</div>
			<div class="middle" style="height:auto;">
				<form class="layui-form form-horizontal"  method="post"  action="sysRole/updateSysRole" style="margin-top:20px;"  onsubmit="return validateCallback(this,updatesuccess);">
				  <div class="layui-form-item">
				    <label class="layui-form-label">角色名称</label>
				    <div class="layui-input-block">
				      <input type="text" name="roleName" required  lay-verify="required"  value="" placeholder="请输入角色名称" autocomplete="off" class="layui-input" />
				    </div>
				  </div>
				  <div class="layui-form-item">
				    <label class="layui-form-label">描述</label>
				    <div class="layui-input-block">
				      <input type="text" name="descrribe"  value="" placeholder="请输入描述"  autocomplete="off" class="layui-input" />
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
				  <input type="hidden" name="roleType" value="0" />
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
		<div class="display_content6" style="height:auto;">
			<div class="top">
				授权菜单
			</div>
			<form method="post"  action="sysRoleResource/adSysRoleResource"   onsubmit="return validateCallback(this,closeResourceDialog);">
				<div class="middle" style="height: auto;">
					<ul id="resourceTree" class="ztree" style="width:auto;height:240px;"></ul>
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
	    <jsp:param name="mapType" value="0"/>  
	</jsp:include>
</html>
	<script>
		
		function addUserRoleSuccess(data){
			returnSuccess(data,dialogs.add_team_dialog);
		}
		
		function selectClick(_this){
			if($(_this).hasClass("colorli")){
				$(_this).removeClass("colorli");
			}else{
				$(_this).addClass("colorli");
			}
		}
		
		
		
		
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
						str+="<input type='hidden' name='departUid' value='"+departUid+"'/>";
						str+="</li>";
						$("#user_add").append(str);
						index++;
					}
				}
			});
		}
		
		function delete_user(){
			$("#user_add .colorli").remove();
		}
		
		
		function setUserList(data){
			var $ul=$("#usersul");
			user_add_li(data,$ul);
		}
		
		function user_add_li(data,element,type){
			var $ul=element;
			$ul.empty();
			$("#usersul").empty();
			$(data).each(function(index){
				//$('#roleUid').val(this.roleUid);
				var str='';
				if(type=='addUserRole'){
					str+="<li value='"+this.userUid+"' onclick='selectClick(this);'>"+this.userName;
					str+="<input type='hidden' name='userUid' value='"+this.userUid+"'/>";
					str+="<input type='hidden' name='departUid' value='"+this.departUid+"'/>";
					str+="</li>";
				}else{
					str+='<li type="hidden" value="'+this.userUid+'" departUid="'+this.departUid+'" onclick="selectClick(this)" name="userUid">'+this.userName+'</li>';
				}
				$ul.append(str);
			});
		};
		
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
			})
			
			
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
		
		
		var ruleUids='';
		//群组人员分配
		function addRoleTema(data){
			opendialog(dialogs.add_team_dialog);
			var $ul=$("#user_add");
			user_add_li(data,$ul,'addUserRole');
			$('#roleUid').val(ruleUids);
		}
		
		function openRoleUsers(roleUid){
			ruleUids=roleUid;
			ajaxTodo("sysRoleUser/allSysRoleUser?roleUid="+roleUid,"addRoleTema");
		}
		
		var settingResource = {
			check: {
				enable: true
			},data: {
				simpleData: {
					enable: true
				}
			}
		};

		function openResourceDialog(roleUid){
			$('#resource').empty();
			$.ajax({  
		        url: 'sysResource/resourceTree',    //后台webservice里的方法名称  
		        type: "post",  
		        dataType: "json",  
		        success: function (data) {
					$.fn.zTree.init($("#resourceTree"), settingResource, data);
					$.ajax({  
				        url: 'sysRoleResource/allSysRoleResource?roleUid='+roleUid,    //后台webservice里的方法名称  
				        type: "post",  
				        dataType: "json",  
				        success: function (data1) {
				        	opendialog('display_container6');
							$('#roleUid1').val(roleUid);
							var treeObjs = $.fn.zTree.getZTreeObj("resourceTree");
							$.each(data1,function(i,value){
								var node = treeObjs.getNodeByParam("id",value.resourceUid);
								if(node!=null){
									if(node.isParent==false){
										treeObjs.checkNode(node, true, true);
									}
								}
							});
				        }
					});
		        }
		    });
		}
		
		function closeResourceDialog(data){
			returnSuccess(data,'display_container6');
		}
		
		
		
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
		        str+='<i class="layui-icon jurisdiction_btn" onclick=openResourceDialog("'+this.roleUid+'"); >&#xe6b2;</i>';
		        str+='<i class="layui-icon delete_btn" onclick=ajaxTodo("sysRole/deleteSysRole?roleUid='+this.roleUid+'","del") >&#xe640;</i>';
		        str+='</td>';
	         	$("#tabletr").append(str);
	         });
		}
		
	</script>