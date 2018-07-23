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
  			.display_content1{height: auto;}
  			.choose_user{position:absolute;right:10px;top:8px;cursor:pointer;z-index:1;}
  		</style>
	</head>
	<body>
		<div class="layui-container" style="margin-top:20px;width:100%;">  
		  	<div class="layui-row">
			    <div class="layui-col-md2" style="text-align: left;">
					<div class="tree" id="demo">
						<ul id="treeDemo" class="ztree" style="width:auto;height:500px;"></ul>
					</div>
			    </div>
			    <div class="layui-col-md10">
			    	<form class="form-inline" method="post" action="sysDepartment/allSysDepartment"  onsubmit="return search(this);">
					<div class="search_area">
						<div class="layui-row layui-form">
							<div class="layui-col-md2">
						    	<input type="text" placeholder="部门名称"  name="departName" class="layui-input">
							</div>
							<div class="layui-col-md2">
						    	<input type="text" placeholder="部门代码"  name="departNo" class="layui-input">
							</div>
							<div class="layui-col-md1" style="text-align:right;">
							    <button class="layui-btn" type="button" onclick="pageBreak(1);" >查询</button>
							</div>
							<div class="layui-col-md1" style="text-align:right;">
							    <button class="layui-btn create_btn" type="button"  id="addDepartment"   >新建</button>
							</div>
						</div>
						<input type="hidden" name="departParent" id="departParent" >
						
						<input type="hidden" name="pageNo" id="pageNo" value="1" >
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
							      <th>部门名称</th>
							      <th>部门代码</th>
							      <th>上级部门名称</th>
							      <th>上级部门代码</th>
							      <th>部门负责人</th>
							      <th>操作</th>
							    </tr>  
							</thead>
							<tbody id="tabletr"></tbody>
						</table>
						<div id="pagination"></div>
					</div>
			    </div>
		  	</div>
		  	
			
			<div class="display_container1 departmentDetail">
				<div class="display_content1">
					<div class="top">
						查看部门
					</div>
					<div class="middle" style="height: auto;">
						<form class="layui-form form-horizontal"  method="post"  action=""  >
						  <div class="layui-form-item" style="margin-top:20px;" >
						    <label class="layui-form-label">部门名称</label>
						    <div class="layui-input-block">
						      <input type="text" name="departName" disabled  placeholder="部门名称" lay-verify="required" autocomplete="off" class="layui-input">
						    </div>
						  </div>
						   <div class="layui-form-item">
						    <label class="layui-form-label">部门代码</label>
						    <div class="layui-input-block">
						      <input type="text" name="departNo" disabled  lay-verify="required"  remote="sysUser/userexists" placeholder="部门代码" autocomplete="off" class="layui-input">
						    </div>
						  </div>
						   <div class="layui-form-item">
						    <label class="layui-form-label">上级部门名称</label>
						    <div class="layui-input-block">
						      <input type="text" name="departParentName" disabled  lay-verify="required" placeholder="上级部门名称" autocomplete="off" class="layui-input">
						    </div>
						  </div>
						   <div class="layui-form-item">
						    <label class="layui-form-label">上级部门代码</label>
						    <div class="layui-input-block">
						      <input type="text" name="departParent" disabled  lay-verify="required" placeholder="上级部门代码"  autocomplete="off" class="layui-input">
						    </div>
						  </div>
						   <div class="layui-form-item">
						    <label class="layui-form-label">部门负责人</label>
						    <div class="layui-input-block">
						      <input type="text" name="departAdmins" disabled  lay-verify="required" placeholder="部门负责人" autocomplete="off" class="layui-input">
						    </div>
						  </div>
						</form>
						</div>
					<div class="foot">
						<button class="layui-btn layui-btn layui-btn-primary cancel_btn" onclick="dgclose('departmentDetail');" style="margin-top: 10px;margin-left:7px;" type="button" >取消</button>
					</div>
				</div>
			</div>	
			
			<div class="display_container1 addDeaprtement">
				<div class="display_content1">
					<div class="top">
						新增部门
					</div>
						<form class="layui-form form-horizontal"  method="post"  action="sysDepartment/insertSysDepartment" onsubmit="return validateCallback(this,addsuccessNew);"  >
					<div class="middle" style="height: auto;">
						<div class="layui-form-item" style="margin-top:20px;">
						    <label class="layui-form-label">部门代码</label>
						    <div class="layui-input-block">
						      <input type="text" name="departUid" required  lay-verify="required" remote="sysDepartment/departmentExists" placeholder="部门代码" data-msg-remote="部门代码已存在请重新输入!"  autocomplete="off" class="layui-input number" minlength="8" />
						    </div>
						  </div>
						 <div class="layui-form-item">
						    <label class="layui-form-label">部门名称</label>
						    <div class="layui-input-block">
						      <input type="text" name="departName" required  lay-verify="required"  placeholder="部门名称" autocomplete="off" class="layui-input" />
						    </div>
						  </div>
						  <div class="layui-form-item">
						    <label class="layui-form-label">部门简称</label>
						    <div class="layui-input-block">
						      <input type="text" name="displayName" required  lay-verify="required" placeholder="部门简称" autocomplete="off" class="layui-input" />
						    </div>
						  </div>
						   <div class="layui-form-item">
						    <label class="layui-form-label">上级部门</label>
						    <div class="layui-input-block">
							   <input type="hidden" name="departParent" id="departParentOne"  class="layui-input" /> 
							  <input type="text" id="departParentOne_view" placeholder="上级部门"   autocomplete="off" class="layui-input" required  readonly="readonly" /> 
							  <i onclick="chooseDepartment('departParentOne');" class="layui-icon choose_user" title="选择部门">&#xe612;</i>
						    </div>
						  </div>
						   <div class="layui-form-item">
						    <label class="layui-form-label">部门负责人</label>
						    <div class="layui-input-block">
						      <input type="hidden" name="departAdmins" id="departAdminsOne"  class="layui-input"/>
						      <input type="text"  required  lay-verify="required" placeholder="部门负责人"  id="departAdminsOne_view"   autocomplete="off" class="layui-input" readonly="readonly" />
						      <i onclick="chooseUser('departAdminsOne');" class="layui-icon choose_user"  title="选择人员">&#xe613;</i>
						    </div>
						  </div>
					</div>
					<div class="foot">
						<button class="layui-btn layui-btn sure_btn" type="submit">确定</button>
						<button class="layui-btn layui-btn layui-btn-primary cancel_synAppointUser" onclick="dgclose('addDeaprtement');" type="button">取消</button>
					</div>
					</form>
				</div>
			</div>	
			
			
			<div class="display_container1 updateDeaprtement">
				<div class="display_content1">
					<div class="top">
						修改部门
					</div>
					<form class="layui-form form-horizontal"  method="post"  action="sysDepartment/updateSysDepartment" onsubmit="return validateCallback(this,updatesuccessNew);"  >
					<div class="middle"  style="height: auto;">
							<div class="layui-form-item" style="margin-top:20px;">
						    <label class="layui-form-label">部门代码</label>
						    <div class="layui-input-block">
						      <input type="text" name="departUid"   lay-verify="required"  placeholder="部门代码"  disabled="disabled"  class="layui-input" />
						    </div>
						  </div>
						  <div class="layui-form-item">
						    <label class="layui-form-label">部门名称</label>
						    <div class="layui-input-block">
						      <input type="text" name="departName" required  lay-verify="required" placeholder="部门名称" autocomplete="off" class="layui-input" />
						    </div>
						  </div>
						   <div class="layui-form-item">
						    <label class="layui-form-label">部门简称</label>
						    <div class="layui-input-block">
						      <input type="text" name="displayName" required  lay-verify="required" placeholder="部门简称" autocomplete="off" class="layui-input" />
						    </div>
						  </div>
						   <div class="layui-form-item">
						    <label class="layui-form-label">上级部门</label>
						    <div class="layui-input-block">
						    <input type="hidden" id="departParentTwo" name="departParent" class="layui-input"   autocomplete="off" />
						      <input type="text"    lay-verify="required" id="departParentTwo_view"  name="departParentName" placeholder="上级部门" readonly="readonly"   autocomplete="off" class="layui-input required" />
						    	<i onclick="chooseDepartment('departParentTwo');" class="layui-icon choose_user" title="选择角色">&#xe612;</i>
						    </div>
						  </div>
						   <div class="layui-form-item">
						    <label class="layui-form-label">部门负责人</label>
						    <div class="layui-input-block">
						    	<input type="hidden" name="departAdmins" id="departAdminsTow"  class="layui-input"/>
								<input type="text"  lay-verify="required" name="userName"  placeholder="部门负责人"  id="departAdminsTow_view"  readonly="readonly"  autocomplete="off" class="layui-input" />
						    	 <i onclick="chooseUser('departAdminsTow');" class="layui-icon choose_user"  title="选择人员">&#xe613;</i>
						    </div>
						  </div>
					</div>
					<div class="foot">
						<input type="hidden" name="departUid" />
						<button class="layui-btn layui-btn sure_btn" type="submit">确定</button>
						<button class="layui-btn layui-btn layui-btn-primary cancel_synAppointUser" onclick="dgclose('updateDeaprtement');" type="button">取消</button>
					</div>
					</form>
				</div>
			</div>	
			
		  		<script type="text/javascript" src="resources/desmartportal/js/common.js"></script>
		  		<script>
		  		
		  		
		  		//选择人员
		  		function chooseUser(id){
		  			common.chooseUser(id,'true');
		  		}
		  		
		  		//选择部门
		  		function chooseDepartment(id){
		  			common.chooseDepart(id);
		  		}
		  		
				$(function(){
					$('#addDepartment').click(function(){
						$('#departParentOne').removeAttr('value');
						$('#departAdminsOne').removeAttr('value');
						opendialog('addDeaprtement');
					});
					var url='sysDepartment/treeDisplay';
					//tree展示
					setting.callback={onClick: onClick}
					treeDisplay(url,'treeDemo');
					pageBreak($('#pageNo').val());
				})
				
				function addsuccessNew(data){
					returnSuccess(data,'addDeaprtement');
				}
				
				function updatesuccessNew(data){
					if(data.msg=='success'){
						layer.alert(language.modify_successfully);
						dgclose('updateDeaprtement');
						pageBreak();
					}else{
						layer.alert(language.modify_failed);
					}
				}
				
				function onClick(e, treeId, treeNode) {
					var departUid='';
					var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
					var sNodes = treeObj.getSelectedNodes();
					if (sNodes.length > 0) {
						var node = sNodes[0].getPath();
						for (var i = 0; i < node.length; i++) {
							departUid=node[i].id;
						}
					}
					departUid=departUid.replace(/\s/g, "");
					$('#departParent').val(departUid);
					pageBreak(1);
				}
		
				//table数据显示
				function tabledata(dataList,data){
					 $(dataList).each(function(i){//重新生成
						var str='<tr>';
						str+='<td>' + (data.beginNum+i) + '</td>';
			         	str+='<td>' + this.departName + '</td>';
			         	str+='<td>' + this.departNo + '</td>';
			         	str+='<td>' + isEmpty(this.departParentName) + '</td>';
			         	str+='<td>' + this.departParent + '</td>';
			         	str+='<td>' + this.departAdmins + '</td>';
			         	str+='<td>';
				        str+='<i class="layui-icon" onclick=ajaxTodo("sysDepartment/getSysDepartment?departUid='+this.departUid+'","departmentDetail")>&#xe60a;</i>';
			         	str+='<i class="layui-icon" onclick=ajaxTodo("sysDepartment/getSysDepartment?departUid='+this.departUid+'","updateDeaprtement") >&#xe642;</i>';
				        str+='<i class="layui-icon" onclick=ajaxTodo("sysDepartment/deleteSysDepartment?departUid='+this.departUid+'","del")>&#xe640;</i>';
				        str+='</td>';
				        str+='</tr>';
			         	$("#tabletr").append(str);
			         });
				}
				
				function updateDeaprtement(data){
					choosableEdit(data,'updateDeaprtement');
				}
				
				function departmentDetail(data){
					choosableEdit(data,'departmentDetail');
				}
		
	</script>
		  	
	</body>
</html>