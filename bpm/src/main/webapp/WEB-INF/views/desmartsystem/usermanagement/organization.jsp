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
						    	<input type="text" placeholder="组织名称"  name="departName" class="layui-input">
							</div>
							<div class="layui-col-md2">
						    	<input type="text" placeholder="组织代码"  name="departNo" class="layui-input">
							</div>
							<div class="layui-col-md1" style="text-align:right;">
							    <button class="layui-btn" >查询</button>
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
		  	
		  	<div class="display_container1">
				<div class="display_content1" style="height: 450px;">
					<div class="top">
						查看部门
					</div>
					<div class="middle" style="height: 350px;">
						<form class="layui-form form-horizontal"  method="post"  action="" style="margin-top:30px;"  >
						  <div class="layui-form-item">
						    <label class="layui-form-label">部门名称</label>
						    <div class="layui-input-block">
						      <input type="text" name="departName" disabled  lay-verify="required" autocomplete="off" class="layui-input">
						    </div>
						  </div>
						   <div class="layui-form-item">
						    <label class="layui-form-label">部门代码</label>
						    <div class="layui-input-block">
						      <input type="text" name="departNo" disabled  lay-verify="required" autocomplete="off" class="layui-input">
						    </div>
						  </div>
						   <div class="layui-form-item">
						    <label class="layui-form-label">上级部门名称</label>
						    <div class="layui-input-block">
						      <input type="text" name="ext1" disabled  lay-verify="required" autocomplete="off" class="layui-input">
						    </div>
						  </div>
						   <div class="layui-form-item">
						    <label class="layui-form-label">上级部门代码</label>
						    <div class="layui-input-block">
						      <input type="text" name="departParent" disabled  lay-verify="required" autocomplete="off" class="layui-input">
						    </div>
						  </div>
						   <div class="layui-form-item">
						    <label class="layui-form-label">部门负责人</label>
						    <div class="layui-input-block">
						      <input type="text" name="departAdmins" disabled  lay-verify="required" autocomplete="off" class="layui-input">
						    </div>
						  </div>
						</form>
					</div>
					<div class="foot">
						<button class="layui-btn layui-btn layui-btn-primary cancel_btn" style="margin-top: 20px;" type="button">取消</button>
					</div>
				</div>
			</div>	
		  	
		  		<script>
		  		
		  		
				$(function(){
					var url='sysDepartment/treeDisplay';
					//tree展示
					setting.callback={onClick: onClick}
					treeDisplay(url,'treeDemo');
					
					pageBreak($('#pageNo').val());
					
					$(".cancel_btn").click(function(){
						$(".display_container1").css("display","none");
					})
				})
				
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
			         	str+='<td>' + this.ext1 + '</td>';
			         	str+='<td>' + this.departParent + '</td>';
			         	str+='<td>' + this.departAdmins + '</td>';
			         	/* str+='<td>' + userName(this.sysUserDepartmentList,this.departAdmins) + '</td>'; */
				        str+='<td><i class="layui-icon" onclick=ajaxTodo("sysDepartment/getSysDepartment?departUid='+this.departUid+'","edit")>&#xe60a;</i></tr>';
			         	$("#tabletr").append(str);
			         });
				}
		
	</script>
		  	
	</body>
</html>