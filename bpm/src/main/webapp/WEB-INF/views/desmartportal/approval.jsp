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
<meta charset="utf-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1">
<title>待办任务</title>
<link
	href="resources/desmartportal/formDesign/css/bootstrap-combined.min.css"
	rel="stylesheet">
<link href="resources/desmartportal/formDesign/css/layoutit.css"
	rel="stylesheet">
<link
	href="resources/desmartportal/css/modules/laydate/default/laydate.css"
	rel="stylesheet">
<link href="resources/desmartportal/css/layui.css" rel="stylesheet" />
<link href="resources/desmartportal/css/my.css" rel="stylesheet" />
<style>
.layui-form-label {
	text-align: left;
	padding: 6px 0;
	width: 60px;
}

.layui-input-block {
	margin-left: 70px;
}

#upload_file_modal {
	display: none;
}

.display_content_accessory_file {
	color: #717171;
	padding: 20px;
	width: 70%;
	height: 60%;
	background: #fff;
	position: fixed;
	left: 12.5%;
	top: 16%;
	box-shadow: 0 0 10px #ccc;
}

.foot_accessory_file {
	text-align: right;
	height: 50px;
	line-height: 50px;
	padding-right: 25px;
}

.upload_overflow_middle {
	height: 80%;
	width: 96%;
	border: 1px solid #ccc;
	position: relative;
	padding: 0 10px;
	overflow: scroll;
}

.approval_th{
 	background-color: #A9B1B3;
}
</style>
</head>
<body>
	<div class="search_area top_btn">
	<input id="formId" value="${formId}" style="display: none;"> <input
			id="proUid" value="${proUid}" style="display: none;"> <input
			id="proAppId" value="${proAppId}" style="display: none;"> <input
			id="proVerUid" value="${proVerUid}" style="display: none;"> <input
			id="insUid" value="${insUid}" style="display: none;"> <input
			id="insData" value="${insData}" style="display: none;"> <input
			id="activityId" value="${activityId}" style="display: none;">
			<input id="activityName" value="${activityName}" style="display: none;">
		<span style="padding-left: 10px; color: #777; font-size: 18px;">门店生命周期流程</span>
		<span style="float: right; padding-right: 20px;">
			<button class="layui-btn  layui-btn-sm">流程图</button>
			<button class="layui-btn layui-btn-sm">通过</button>
			<button class="layui-btn layui-btn-sm">驳回</button>
			<button class="layui-btn layui-btn-sm">转办</button>
			<button class="layui-btn layui-btn-sm">会签</button> <a
			href="backlog.html" style="margin-left: 10px;"><button
					class="layui-btn layui-btn-sm back_btn">退出</button></a>
		</span>
	</div>
	<div class="container" style="width:96%">
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
						<th colspan="4" class="list_title">目标店调查表 <span
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
							lay-verify="required" value="00003" autocomplete="off"
							class="layui-input"></td>
						<td class="td_title">姓名</td>
						<td><input type="text" name="title" required
							lay-verify="required" value="00003" autocomplete="off"
							class="layui-input"></td>
					</tr>
					<tr>
						<td class="td_title">创建日期</td>
						<td><input type="text" name="title" required
							lay-verify="required" value="2018-03-12" autocomplete="off"
							class="layui-input"></td>
						<td class="td_title">金额</td>
						<td><input type="text" name="title" required
							lay-verify="required" value="11453" autocomplete="off"
							class="layui-input"></td>
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
							lay-verify="required" value="江西南昌" autocomplete="off"
							class="layui-input"></td>
						<td class="td_title">区/县</td>
						<td><input type="text" name="title" required
							lay-verify="required" value="西湖区" autocomplete="off"
							class="layui-input"></td>
						<td class="td_title">街道</td>
						<td><input type="text" name="title" required
							lay-verify="required" value="系马桩街道" autocomplete="off"
							class="layui-input"></td>
					</tr>
					<tr>
						<td class="td_title">路<span class="tip_span">*</span></td>
						<td><input type="text" name="title" required
							lay-verify="required" value="系马桩路" autocomplete="off"
							class="layui-input"></td>
						<td class="td_title">号<span class="tip_span">*</span></td>
						<td><input type="text" name="title" required
							lay-verify="required" value="89号" autocomplete="off"
							class="layui-input"></td>
						<td class="td_title">近<span class="tip_span">*</span></td>
						<td><input type="text" name="title" required
							lay-verify="required" value="三硬并图" autocomplete="off"
							class="layui-input"></td>
					</tr>
					<tr>
						<td class="td_title">公司代码</td>
						<td><input type="text" name="title" required
							lay-verify="required" value="3500" autocomplete="off"
							class="layui-input"></td>
						<td class="td_title">公司名称</td>
						<td colspan="3"><input type="text" name="title" required
							lay-verify="required" value="江西来伊份食品有限公司" autocomplete="off"
							class="layui-input"></td>
					</tr>
					<tr>
						<td class="td_title">销售组织<span class="tip_span">*</span></td>
						<td><input type="text" name="title" required
							lay-verify="required" value="3500" autocomplete="off"
							class="layui-input"></td>
						<td class="td_title">销售组织名称</td>
						<td colspan="3"><input type="text" name="title" required
							lay-verify="required" value="江西来伊份食品有限公司" autocomplete="off"
							class="layui-input"></td>
					</tr>
					<tr>
						<td class="td_title">产权地址<span class="tip_span">*</span></td>
						<td><input type="text" name="title" required
							lay-verify="required" value="西湖区系马桩街89号附10号" autocomplete="off"
							class="layui-input"></td>
						<td class="td_title">办证资料<span class="tip_span">*</span></td>
						<td><input type="text" name="title" required
							lay-verify="required" value="产权证，上家合同，出租方身份证，其他"
							autocomplete="off" class="layui-input"></td>
						<td class="td_title">产权方名称/<br>出租房名称<span
							class="tip_span">*</span></td>
						<td><input type="text" name="title" required
							lay-verify="required" value="黄春英" autocomplete="off"
							class="layui-input"></td>
					</tr>
					<tr>
						<td class="td_title">建筑面积(平方米)<span class="tip_span">*</span></td>
						<td><input type="text" name="title" required
							lay-verify="required" value="55.43" autocomplete="off"
							class="layui-input"></td>
						<td class="td_title">使用面积(平方米)<span class="tip_span">*</span></td>
						<td colspan="3"><input type="text" name="title" required
							lay-verify="required" value="52.12" autocomplete="off"
							class="layui-input"></td>
					</tr>
					<tr>
						<td class="td_title">门宽(米)<span class="tip_span">*</span></td>
						<td><input type="text" name="title" required
							lay-verify="required" value="4.20" autocomplete="off"
							class="layui-input"></td>
						<td class="td_title">内径门高(米)<span class="tip_span">*</span></td>
						<td><input type="text" name="title" required
							lay-verify="required" value="3.00" autocomplete="off"
							class="layui-input"></td>
						<td class="td_title">高(米)<span class="tip_span">*</span></td>
						<td><input type="text" name="title" required
							lay-verify="required" value="13.00" autocomplete="off"
							class="layui-input"></td>
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
							lay-verify="required" value="个人产权" autocomplete="off"
							class="layui-input"></td>
						<td class="td_title">房东<span class="tip_span">*</span></td>
						<td><input type="text" name="title" required
							lay-verify="required" value="张三" autocomplete="off"
							class="layui-input"></td>
					</tr>
					<tr>
						<td class="td_title">合作模式<span class="tip_span">*</span></td>
						<td><input type="text" name="title" required
							lay-verify="required" value="固定租赁金" autocomplete="off"
							class="layui-input"></td>
						<td class="td_title">扣率(%)</td>
						<td><input type="text" name="title" required
							lay-verify="required" value="" autocomplete="off"
							class="layui-input"></td>
						<td class="td_title">保底</td>
						<td><input type="text" name="title" required
							lay-verify="required" value="" autocomplete="off"
							class="layui-input"></td>
					</tr>
					<tr>
						<td class="td_title">租金(元/月(含税))<span class="tip_span">*</span></td>
						<td><input type="text" name="title" required
							lay-verify="required" value="12620.51" autocomplete="off"
							class="layui-input"></td>
						<td class="td_title">房租期间(月)<span class="tip_span">*</span></td>
						<td colspan="3"><input type="text" name="title" required
							lay-verify="required" value="12" autocomplete="off"
							class="layui-input"></td>
					</tr>
					<tr>
						<td class="td_title">付款方式<span class="tip_span">*</span></td>
						<td><input type="text" name="title" required
							lay-verify="required" value="3500" autocomplete="off"
							class="layui-input"></td>
						<td class="td_title">押金支付金额<span class="tip_span">*</span></td>
						<td><input type="text" name="title" required
							lay-verify="required" value="江西来伊份食品有限公司" autocomplete="off"
							class="layui-input"></td>
						<td class="td_title">免租期(天)<span class="tip_span">*</span></td>
						<td><input type="text" name="title" required
							lay-verify="required" value="0" autocomplete="off"
							class="layui-input"></td>
					</tr>
				</tbody>
			</table>
			<div class="option_container">
				<p class="title_p" style="margin-top: 10px;">本环节审批要求</p>
				<div class="layui-form">
					<p style="margin-bottom: 10px;">
						<img src="resources/desmartportal/images/top_star.png"
							class="star_img" />单店单月租金≤1.5万元，华东区租金占比≤12%，其他地区≤15%。
					</p>
					<p>
						<img src="resources/desmartportal/images/top_star.png"
							class="star_img" />审核门店地理位置，评估门店未来盈利趋势，对门店信息合理性负责。
					</p>
				</div>
				<p class="title_p">审批意见</p>
				<div class="layui-form">
					<!--<label class="layui-form-label">审批意见</label>
				      	<div class="layui-input-block">-->
					<textarea placeholder="意见留言" class="layui-textarea" id="demo"
						style="display: none; margin-bottom: 10px;"></textarea>
					<!--</div>-->
					<div style="margin-top: 10px;">
						<label class="layui-form-label">常用语</label>
						<div class="layui-input-block">
							<select class="layui-form" lay-filter="useselfChange">
								<option value="-1">--请选择--</option>
								<option value="通过">通过</option>
								<option value="驳回">驳回</option>
							</select>
						</div>
					</div>
				</div>
				<div class="layui-tab">
					<ul class="layui-tab-title">
						<li class="layui-this">审批记录</li>
						<li>附件</li>
						<li>流转信息</li>

					</ul>
					<div class="layui-tab-content" style="padding: 0;">
						<div class="layui-tab-item layui-show">
							<table class="layui-table" style="margin: 0;">
								<colgroup>
									<col width="10%">
									<col width="10%">
									<col width="8%">
									<col width="10%">
									<col width="10%">
									<col width="10%">
									<col width="10%">
									<col width="22%">
								</colgroup>
								<tbody id="approval_tbody">

								</tbody>
							</table>
						</div>
						<div class="layui-tab-item" style="height: auto;">
							<table class="layui-table upload-file-table" style="margin: 0;">
								<colgroup>
									<col width="5%">
									<col width="20%">
									<%-- 
									    <col width="15%">
									    <col width="20%"> 
									    <col width="10%"> --%>
									<col width="10%">
									<col width="20%">
								</colgroup>
								<thead>
									<tr>
										<th>
											<!-- <input id="all-file-check" type="checkbox"> -->序号
										</th>
										<th>附件名称</th>
										<!-- 
									      <th>附件说明</th>
									      <th>附件类型</th> -->
										<th>上传人</th>
										<th>上传时间</th>
										<th>
											<button
												class="layui-btn layui-btn-primary layui-btn-sm upload"
												id="upload-file" style="margin-left: 20px;">上传附件</button> <!-- <button class="layui-btn layui-btn-primary layui-btn-sm " id="batch-down-file" style="margin-left:20px;">批量下载</button> -->
											<div class="hidden-value">
												<input class="maxFileSize" value="20" type="hidden" /> <input
													class="maxFileCount" value="10" type="hidden" /> <input
													class="fileFormat" value="jpg,png,xls,xlsx,exe"
													type="hidden" />
											</div> <!-- <input type="file" class="upload_file"/> --> <!-- <input style="margin-left:20px;" class="layui-btn layui-btn-primary btn btn-primary file" value="上传附件" id="button-EafH" type="button" /> -->
										</th>
									</tr>
								</thead>
								<tbody>

								</tbody>
							</table>
						</div>
						<div class="layui-tab-item">
							<div class="p">
								<p>
									<font>现在的环节号：</font><span></span>
								</p>
								<p>
									<font>当前处理人：</font><span></span>
								</p>
								<p>
									<font>当前处理状态：</font><span></span>
								</p>
								<p>
									<font>当前处理到达时间：</font><span></span>
								</p>
								<p>
									<font>流转过程：</font>
								</p>
							</div>
							<ul id="transferProcess" class="tab_ul">
								
								<h1 style="clear: both;"></h1>
							</ul>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript"
		src="resources/desmartportal/js/jquery-3.3.1.js"></script>
	<script type="text/javascript"
		src="resources/desmartportal/js/layui.all.js"></script>
	<!-- 附件上传js -->
	<script src="resources/desmartportal/js/my/myFileUpload.js"></script>
	<!-- 审批信息js -->
	<script src="resources/desmartportal/js/my/myApprovalOpinion.js"></script>
	<!--IE8只能支持jQuery1.9-->
	<!--[if lte IE 8]>
	    <script src="http://cdn.bootcss.com/jquery/1.9.0/jquery.min.js"></script>
	    <![endif]-->
	<!-- 让IE8/9支持媒体查询，从而兼容栅格 -->
	<!--[if lt IE 9]>
		  <script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
		  <script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
		<![endif]-->
	<div class="display_content_accessory_file" id="upload_file_modal">
		<div class="top">文件上传</div>
		<div class="upload_overflow_middle">
			<div class="layui-upload-drag" style="width: 94.5%;">
				<i class="layui-icon"></i>
				<p>点击上传，或将文件拖拽到此处</p>
			</div>
			<div class="layui-upload">
				<div class="layui-upload-list">
					<table class="layui-table">
						<colgroup>
							<col width="5%">
							<col width="20%">
							<%-- 
									    <col width="15%">
									    <col width="20%"> 
									    <col width="10%"> --%>
							<col width="10%">
							<col width="20%">
						</colgroup>
						<thead>
							<tr>
								<th>文件名</th>
								<th>大小</th>
								<!-- <th>文件标题</th>
										<th>文件标签</th>
										<th>文件说明</th> -->
								<th>状态</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody class="fileList"></tbody>
					</table>
				</div>
			</div>
		</div>
		<div class="foot_accessory_file">
			<button type="button" class="layui-btn listAction">开始上传</button>
			<button class="layui-btn layui-btn layui-btn-primary cancel_btn"
				onclick="cancelClick(this)">取消</button>
		</div>
	</div>
</body>
</html>
<script>
	layui.use('layedit', function() {
		var layedit = layui.layedit;
		editIndex = layedit.build('demo', {
			height : 100,
			tool : [ 'strong' //加粗
			, 'italic' //斜体
			, 'underline' //下划线
			, 'del' //删除线

			, '|' //分割线

			, 'left' //左对齐
			, 'center' //居中对齐
			, 'right' //右对齐
			]
		}); //建立编辑器

	});
	$(function() {
		$(".add_row")
				.click(
						function() {
							var le = $(".create_table tbody tr").length + 1;
							$(".create_table")
									.append(
											$('<tr>'
													+ '<td>'
													+ le
													+ '</td>'
													+ '<td><input type="text" class="txt"/></td>'
													+ '<td><input type="text" class="txt"/></td>'
													+ '<td><input type="text" class="txt"/></td>'
													+ '<td><input type="text" class="txt"/></td>'
													+ '<td><i class="layui-icon delete_row">&#xe640;</i></td>'
													+ '</tr>'));
							$(".delete_row").click(function() {
								$(this).parent().parent().remove();
							});
						});
		$(".delete_row").click(function() {
			$(this).parent().parent().remove();
		});
		$(".upload").click(function() {
			$(".upload_file").click();
		});
		$(".back_btn").click(function() {
			window.location.href = "backlog.html";
		});
	})
</script>