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
		<form class="form-inline" method="post" action="sysResource/allSysResource"  onsubmit="return search(this);">
			<input type="hidden" name="parentId" value="${resouceUid}" >
			<input type="hidden" name="pageNo" id="pageNo" value="1" >
		</form>
		<div class="container">
			<div class="search_area">
				<div class="layui-row layui-form">	
					<button class="layui-btn layui-btn-primary layui-btn-sm" onclick="javascript: window.history.go(-1);return false;">返回</button>
					<span style="float:right;">
						<button class="layui-btn  layui-btn-sm create_btn" onclick="adddialog();" >新建</button>
					</span>
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
					      <th>模块权限名称</th>
					      <th>状态</th>
					      <th>操作</th>
					    </tr> 
					</thead>
					<tbody id="tabletr">
					</tbody>
				</table>
				<div id="pagination"></div>
			</div>
		</div>
	</body>
	<div class="display_container">
		<div class="display_content">
			<div class="top">
				新建模块资源
			</div>
			<div class="middle">
				<form class="layui-form form-horizontal" method="post"  action="sysResource/addSysResource" style="margin-top:30px;"  onsubmit="return validateCallback(this,addsuccess);">
				  <div class="layui-form-item">
				    <label class="layui-form-label">模块资源名称</label>
				    <div class="layui-input-block">
				      <input type="text" name="resouceName" required  lay-verify="required" placeholder="请输入模块权限名称" autocomplete="off" class="layui-input">
				    </div>
				  </div>
				   <div class="layui-form-item">
				    <label class="layui-form-label">url</label>
				    <div class="layui-input-block">
				      <input type="text" name="resouceNo" required  lay-verify="required" placeholder="请输入url" autocomplete="off" class="layui-input">
				    </div>
				  </div>
				  <div class="layui-form-item">
				    <label class="layui-form-label">状态</label>
				    <div class="layui-input-block">
				      <input type="radio" name="isClosed" value="1" title="显示" checked>
				      <input type="radio" name="isClosed" value="0" title="隐藏" >
				    </div>
				  </div>	
				   <input type="hidden" id="submit_add" />	
				   <input type="hidden" name="parentId" value="${resouceUid}"/>		  
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
				编辑模块资源
			</div>
			<div class="middle">
				<form class="layui-form form-horizontal" method="post"  action="sysResource/updateSysResource" style="margin-top:30px;"  onsubmit="return validateCallback(this,updatesuccess);">
				   <div class="layui-form-item">
				    <label class="layui-form-label">模块资源名称</label>
				    <div class="layui-input-block">
				      <input type="text" name="resouceName" required  lay-verify="required" placeholder="请输入模块权限名称" autocomplete="off" class="layui-input">
				    </div>
				  </div>
				   <div class="layui-form-item">
				    <label class="layui-form-label">url</label>
				    <div class="layui-input-block">
				      <input type="text" name="resouceNo" required  lay-verify="required" placeholder="请输入url" autocomplete="off" class="layui-input">
				    </div>
				  </div>
				  <div class="layui-form-item">
				    <label class="layui-form-label">状态</label>
				    <div class="layui-input-block">
				      <input type="radio" name="isClosed" value="1" title="显示" checked>
				      <input type="radio" name="isClosed" value="0" title="隐藏" >
				    </div>
				  <input type="hidden" id="submit_upd" />
				   <input type="hidden" name="resouceUid" />
				   <input type="hidden" name="orderIndex" />
				   <input type="hidden" name="resouceType" />		
				    <input type="hidden" name="parentId" />		  
				</form>
			</div>
			<div class="foot">
				<button class="layui-btn layui-btn sure_btn" type="button"  onclick="$('#submit_upd').submit();">确定</button>
				<button class="layui-btn layui-btn layui-btn-primary cancel_btn" type="button">取消</button>
			</div>
		</div>
	</div>	
	
	<script>
		$(function(){
			pageBreak($('#pageNo').val());
			
			$(".cancel_btn").click(function(){
				$(".display_container").css("display","none");
				$(".display_container1").css("display","none");
			})
		})
		
		
		//table数据显示
		function tabledata(dataList,data){
			 $(dataList).each(function(i){//重新生成
				var url='findUserMenu?userId='+this.userId;
				var str='<tr>';
				str+='<td>' + (data.beginNum+i) + '</td>';
	         	str+='<td>' + this.resouceName + '</td>';
	         	if(this.isClosed==1){
	         		str+='<td>显示</td>';
	         	}else{
	         		str+='<td>隐藏</td>';
	         	}
		        str+='<td>';
		        str+='<i class="layui-icon edit_user" onclick=ajaxTodo("sysResource/getSysResource?resouceUid='+this.resouceUid+'","edit") >&#xe642;</i>';
		        str+='<i class="layui-icon delete_btn" onclick=ajaxTodo("sysResource/deleteSysResource?resouceUid='+this.resouceUid+'","del") >&#xe640;</i>';
		        str+='</td>';
	         	$("#tabletr").append(str);
	         });
			 
		}
		
		function detail(){
			window.location.href='<%=request.getContextPath()%>/sysResource/resource';
		}

	</script>
</html>