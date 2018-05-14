<%@ page language="java" contentType="text/html; charset=UTF-8"
    isErrorPage="true"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

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
		<div class="container">
			<div class="search_area">
				<div class="search_area top_btn">	
					<a onclick="javascript: window.history.go(-1);return false;"><button class="layui-btn layui-btn-primary layui-btn-sm">返回</button></a>
				</div>
			</div>
			<div>	
				<p class="title_p">常规信息</p>
				<div class="layui-container">  
					<div class="layui-row">
						<form class="layui-form">
					    	<div class="layui-col-md6">					    	
						  		<div class="layui-form-item">
								    <label class="layui-form-label">部门</label>
								    <div class="layui-input-block">
								    	<label style="line-height: 40px;">${sysUser.sysDepartment.departName}</label>
<!-- 								      <input type="text" value="sysUser" required  lay-verify="required" placeholder="" class="layui-input"> -->
								    </div>
								</div>
								<div class="layui-form-item">
								    <label class="layui-form-label">用户名</label>
								    <div class="layui-input-block">
								    	<label style="line-height: 40px;">${sysUser.userNo}</label>
<!-- 								      <input type="text" name="title" required  lay-verify="required" placeholder=""  class="layui-input"> -->
								    </div>
								</div>
								<div class="layui-form-item">
								    <label class="layui-form-label">姓名</label>
								    <div class="layui-input-block">
								    	<label style="line-height: 40px;">${sysUser.userName}</label>
<!-- 								      <input type="text" name="title" required  lay-verify="required" placeholder=""  class="layui-input"> -->
								    </div>
								</div>
								<div class="layui-form-item">
								    <label class="layui-form-label">部门管理者</label>
								    <div class="layui-input-block">
								    	<label style="line-height: 40px;">${sysUser.sysDepartment.departName}</label>
								    </div>
								</div>
						    </div>
						    <div class="layui-col-md6">
						    	<div class="layui-form-item">
								    <label class="layui-form-label">类型</label>
								    <div class="layui-input-block">
								    	<label style="line-height: 40px;">${sysUser.employeeType}</label>
<!-- 								      <input type="text" name="title" required  lay-verify="required" placeholder="" class="layui-input"> -->
								    </div>
								</div>
								<div class="layui-form-item">
								    <label class="layui-form-label">创建日期</label>
								    <div class="layui-input-block">
								    	<label style="line-height: 40px;"><fmt:formatDate value="${sysUser.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></label>
										<!--<input type="text" name="title" required  lay-verify="required" value="2019-04-02" disabled="disabled" class="layui-input">-->
								    </div>
								</div> 
								<div class="layui-form-item">
								    <label class="layui-form-label">更新日期</label>
								    <div class="layui-input-block">
								    	<label style="line-height: 40px;"><fmt:formatDate value="${sysUser.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/></label>
										<!--<input type="text" name="title" required  lay-verify="required" value="2019-04-02" disabled="disabled" class="layui-input"> -->
								    </div>
								</div>
						    </div>
						</form>
					</div>
				</div>
				<p class="title_p">个人信息</p>
				<div class="layui-container">  
					<div class="layui-row">
						<form class="layui-form">
					    	<div class="layui-col-md6">									
								<div class="layui-form-item">
								    <label class="layui-form-label">电话</label>
								    <div class="layui-input-block">
<!-- 								      <input type="text" name="title" required  lay-verify="required"  class="layui-input"> -->
											<label class="layui-form-label">${sysUser.mobile}</label>
								    </div>
								</div>
						    </div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</body>
	
</html>
	<!-- <script type="text/javascript" src="js/jquery-3.3.1.js" ></script>
	<script type="text/javascript" src="js/layui.all.js"></script> -->
	<script>
		layui.use('laydate', function(){
			var laydate = layui.laydate;
			  	laydate.render({
			    elem: '#test1'
			});
		});
		layui.use('laydate', function(){
			var laydate = layui.laydate;
			  	laydate.render({
			    elem: '#test2'
			});
		});
		$(function(){
			$("create_btn").click(function(){
				window.location.href="new_user.html";
			})
			
		})
	</script>