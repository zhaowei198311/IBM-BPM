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
							<div class="layui-col-md1" style="text-align:right;">
							        <button class="layui-btn" >查询</button>
							</div>
						</div>
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
							      <th>组织名称</th>
							      <th>操作</th>
							    </tr> 
							</thead>
							<tbody id="tabletr"></tbody>
						</table>
						<div id="pagination"></div>
					</div>
			    </div>
		  	</div>
		  		<script>
		  		
		  		
				$(function(){
					var url='sysDepartment/treeDisplay';
					//tree展示
					treeDisplay(url,'treeDemo');
					
					pageBreak($('#pageNo').val());
				})
		
				//table数据显示
				function tabledata(dataList,data){
					 $(dataList).each(function(i){//重新生成
						var str='<tr>';
						str+='<td>' + (data.beginNum+i) + '</td>';
			         	str+='<td>' + this.departName + '</td>';
				        str+='<td><i class="layui-icon edit_user">&#xe640;</i></tr>';
			         	$("#tabletr").append(str);
			         });
				}
		
	</script>
		  	
	</body>
</html>