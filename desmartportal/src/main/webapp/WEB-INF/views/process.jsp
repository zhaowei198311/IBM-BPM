<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<head>
<base href="<%=basePath%>">
<meta charset="utf-8">
<title>发起流程</title>
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
<link rel="stylesheet" href="resources/css/layui.css" media="all">
<link rel="stylesheet" href="resources/css/admin.css" media="all">
<link rel="stylesheet" href="resources/css/my.css" />
</head>
<body>
	<div class="search_area top_btn">
		<span style="padding-left: 10px; color: #777; font-size: 18px;">${categoryName}</span>
	</div>
	<div class="container">
		<div class="content">
				<table class="layui-table">
					<colgroup>
						<col width="150">
						<col>
						<col width="150">
						<col>
					</colgroup>
					<tbody>
						<tr>
							<th colspan="4" class="list_title">${proName}<span
								style="float: right; font-size: 14px; font-weight: normal;">流程编号：1000-10185-BG-60</span>
							</th>
						</tr>
						<tr>
							<th colspan="4">
								<div class="layui-progress layui-progress-big"
									lay-showPercent="yes" style="position: relative;">
									<div class="layui-progress-bar" lay-percent="50%"></div>
									<span class="progress_time">审批剩余时间6小时</span>
								</div>
							</th>
						</tr>
						<tr>
							<td class="td_title">工号</td>
							<td><input type="text" name="title" required
								lay-verify="required" autocomplete="off" class="layui-input"></td>
							<td class="td_title">姓名</td>
							<td><input type="text" name="title" required
								lay-verify="required" autocomplete="off" class="layui-input"></td>
						</tr>
						<tr>
							<td class="td_title">创建日期</td>
							<td><input type="text" name="title" required
								lay-verify="required" autocomplete="off" class="layui-input"></td>
							<td class="td_title">金额</td>
							<td><input type="text" name="title" required
								lay-verify="required" autocomplete="off" class="layui-input"></td>
						</tr>
					</tbody>
				</table>
				<p class="title_p">门店基础信息</p>
				<table class="layui-table">
					<colgroup>
						<col width="150">
						<col>
						<col width="150">
						<col>
						<col width="150">
						<col>
					</colgroup>
					<tbody>
						<tr>
							<td class="td_title">省<span class="tip_span">*</span></td>
							<td><input type="text" name="title" required
								lay-verify="required" autocomplete="off" class="layui-input"></td>
							<td class="td_title">区/县</td>
							<td><input type="text" name="title" required
								lay-verify="required" autocomplete="off" class="layui-input"></td>
							<td class="td_title">街道</td>
							<td><input type="text" name="title" required
								lay-verify="required" autocomplete="off" class="layui-input"></td>
						</tr>
						<tr>
							<td class="td_title">路<span class="tip_span">*</span></td>
							<td><input type="text" name="title" required
								lay-verify="required" autocomplete="off" class="layui-input"></td>
							<td class="td_title">号<span class="tip_span">*</span></td>
							<td><input type="text" name="title" required
								lay-verify="required" autocomplete="off" class="layui-input"></td>
							<td class="td_title">近<span class="tip_span">*</span></td>
							<td><input type="text" name="title" required
								lay-verify="required" autocomplete="off" class="layui-input"></td>
						</tr>
						<tr>
							<td class="td_title">公司代码</td>
							<td><input type="text" name="title" required
								lay-verify="required" autocomplete="off" class="layui-input"></td>
							<td class="td_title">公司名称</td>
							<td colspan="3"><input type="text" name="title" required
								lay-verify="required" autocomplete="off" class="layui-input"></td>
						</tr>
						<tr>
							<td class="td_title">销售组织<span class="tip_span">*</span></td>
							<td><input type="text" name="title" required
								lay-verify="required" autocomplete="off" class="layui-input"></td>
							<td class="td_title">销售组织名称</td>
							<td colspan="3"><input type="text" name="title" required
								lay-verify="required" autocomplete="off" class="layui-input"></td>
						</tr>
						<tr>
							<td class="td_title">产权地址<span class="tip_span">*</span></td>
							<td><input type="text" name="title" required
								lay-verify="required" autocomplete="off" class="layui-input"></td>
							<td class="td_title">办证资料<span class="tip_span">*</span></td>
							<td><input type="text" name="title" required
								lay-verify="required" autocomplete="off" class="layui-input"></td>
							<td class="td_title">产权方名称/<br>出租房名称<span
								class="tip_span">*</span></td>
							<td><input type="text" name="title" required
								lay-verify="required" autocomplete="off" class="layui-input"></td>
						</tr>
						<tr>
							<td class="td_title">建筑面积(平方米)<span class="tip_span">*</span></td>
							<td><input type="text" name="title" required
								lay-verify="required" autocomplete="off" class="layui-input"></td>
							<td class="td_title">使用面积(平方米)<span class="tip_span">*</span></td>
							<td colspan="3"><input type="text" name="title" required
								lay-verify="required" autocomplete="off" class="layui-input"></td>
						</tr>
						<tr>
							<td class="td_title">门宽(米)<span class="tip_span">*</span></td>
							<td><input type="text" name="title" required
								lay-verify="required" autocomplete="off" class="layui-input"></td>
							<td class="td_title">内径门高(米)<span class="tip_span">*</span></td>
							<td><input type="text" name="title" required
								lay-verify="required" autocomplete="off" class="layui-input"></td>
							<td class="td_title">高(米)<span class="tip_span">*</span></td>
							<td><input type="text" name="title" required
								lay-verify="required" autocomplete="off" class="layui-input"></td>
						</tr>
					</tbody>
				</table>
				<p class="title_p">房产性质</p>
				<table class="layui-table">
					<colgroup>
						<col width="150">
						<col>
						<col width="150">
						<col>
						<col width="150">
						<col>
					</colgroup>
					<tbody>
						<tr>
							<td class="td_title">房产性质<span class="tip_span">*</span></td>
							<td colspan="3"><input type="text" name="title" required
								lay-verify="required" autocomplete="off" class="layui-input"></td>
							<td class="td_title">房东<span class="tip_span">*</span></td>
							<td><input type="text" name="title" required
								lay-verify="required" autocomplete="off" class="layui-input"></td>
						</tr>
						<tr>
							<td class="td_title">合作模式<span class="tip_span">*</span></td>
							<td><input type="text" name="title" required
								lay-verify="required" autocomplete="off" class="layui-input"></td>
							<td class="td_title">扣率(%)</td>
							<td><input type="text" name="title" required
								lay-verify="required" autocomplete="off" class="layui-input"></td>
							<td class="td_title">保底</td>
							<td><input type="text" name="title" required
								lay-verify="required" autocomplete="off" class="layui-input"></td>
						</tr>
						<tr>
							<td class="td_title">租金(元/月(含税))<span class="tip_span">*</span></td>
							<td><input type="text" name="title" required
								lay-verify="required" autocomplete="off" class="layui-input"></td>
							<td class="td_title">房租期间(月)<span class="tip_span">*</span></td>
							<td colspan="3"><input type="text" name="title" required
								lay-verify="required" autocomplete="off" class="layui-input"></td>
						</tr>
						<tr>
							<td class="td_title">付款方式<span class="tip_span">*</span></td>
							<td><input type="text" name="title" required
								lay-verify="required" autocomplete="off" class="layui-input"></td>
							<td class="td_title">押金支付金额<span class="tip_span">*</span></td>
							<td><input type="text" name="title" required
								lay-verify="required" autocomplete="off" class="layui-input"></td>
							<td class="td_title">免租期(天)<span class="tip_span">*</span></td>
							<td><input type="text" name="title" required
								lay-verify="required" autocomplete="off" class="layui-input"></td>
							<!-- 隐藏域 -->
							<td style="display: none;">
								<input id="proUid" value="${proUid}">
								<input id="proAppId" value="${proAppId}">
								<input id="verUid" value="${verUid}">
							</td>	
						</tr>
					</tbody>
				</table>
				<div align="center">
					<button id="submits" class="layui-btn" onclick="startProcess()">立即提交</button>
					<button type="reset" class="layui-btn layui-btn-primary">重置</button>
				</div>
		</div>
	</div>
</body>
</html>
<script type="text/javascript" src="resources/js/jquery-3.3.1.js"></script>
<script type="text/javascript" src="resources/js/layui.all.js"></script>
<script>
			function startProcess(){
				var proUids = $("#proUid").val();
				var proAppIds =  $("#proAppId").val();
				var verUids = $("#verUid").val();
				 $.ajax({
					url : 'process/startProcess',
					type : 'POST',
					dataType : 'json',
					data : {
						proUid : proUids,
						proAppId : proAppIds,
						verUid : verUids
					},
					success : function(result){
						if (result.status == 0) {
							layer.alert('提交成功', {icon: 1});
						}
						if (result.status == 1) {
							layer.alert('提交失败', {icon: 2});
						}
					},
					error :  function(result){
						layer.alert('提交失败', {icon: 2});
					}
				}); 
			}
</script>