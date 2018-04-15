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
  		
	</head>
	<body>
		<div class="container">
			<div class="search_area">
				<form class="form-inline" method="post" action="sysTeam/allSysTeam"  onsubmit="return search(this);">
					<input type="hidden" name="pageNo" id="pageNo" value="1" >
				</form>
				<div class="layui-row layui-form">					
					<div class="layui-col-md1" style="text-align:right;">
					        <button class="layui-btn create_btn"  onclick="adddialog();"  >新建</button>
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
				<form class="layui-form form-horizontal" action="sysTeam/addSysTeam" style="margin-top:30px;"  onsubmit="return validateCallback(this,addsuccess);">
				  <div class="layui-form-item">
				    <label class="layui-form-label">角色组名称</label>
				    <div class="layui-input-block">
				      <input type="text" name="teamName" required  lay-verify="required" placeholder="请输入角色组名称" autocomplete="off" class="layui-input">
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
				<form class="layui-form form-horizontal" action="sysTeam/updateSysTeam" style="margin-top:30px;"  onsubmit="return validateCallback(this,updatesuccess);">
				  <div class="layui-form-item">
				    <label class="layui-form-label">角色组名称</label>
				    <div class="layui-input-block">
				      <input type="text" name="teamName" required  lay-verify="required" value="组1" autocomplete="off" class="layui-input">
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
</html>
	<script>
		$(function(){
			pageBreak($('#pageNo').val());
			$(".cancel_btn").click(function(){
				$(".display_container").css("display","none");
				$(".display_container1").css("display","none");
			})
			
		})
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
		        str+='<i class="layui-icon edit_user" onclick=ajaxTodo("sysTeam/getSysTeam?teamUid='+this.teamUid+'","edit") >&#xe642;</i>';
		        str+='<i class="layui-icon jurisdiction_btn">&#xe654;</i>';
		        str+='<i class="layui-icon delete_btn" onclick=ajaxTodo("sysTeam/deleteSysTeam?teamUid='+this.teamUid+'","del") >&#xe640;</i>';
		        str+='</td>';
	         	$("#tabletr").append(str);
	         });
		}
	</script>