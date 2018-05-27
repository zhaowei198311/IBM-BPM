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
<meta name="viewport"	content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
<link href="resources/desmartportal/formDesign/css/bootstrap-combined.min.css"	rel="stylesheet"> 
<link href="resources/desmartportal/formDesign/css/layoutit.css" rel="stylesheet">
<link href="resources/desmartportal/css/modules/laydate/default/laydate.css" rel="stylesheet">
<link rel="stylesheet" href="resources/desmartportal/css/layui.css" media="all">
<link rel="stylesheet" href="resources/desmartportal/css/my.css" />
<style type="text/css">
	.container-fluid {
	    padding-right: 0px; 
	    padding-left: 0px;
	    zoom: 1;
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
				overflow-y:auto;
				overflow-x:hidden;
			}
</style>
</head>
<body>
	<div class="search_area top_btn">
	    <input id="formId" value="${formId}" style="display: none;">
	    <input id="proUid" value="${processDefinition.proUid}" style="display: none;">
	    <input id="proAppId" value="${processDefinition.proAppId}" style="display: none;">
	    <input id="verUid" value="${processDefinition.proVerUid}" style="display: none;">
	    <input id="proName" value="${processDefinition.proName}" style="display: none;">
	    <input id="userId" value="${currentUser.userId}" style="display: none;">
	    <input id="dataInfo" style="display: none;">
	    <span style="padding-left: 10px; color: #777; font-size: 18px;">门店生命周期流程</span>
	    <span style="float: right; padding-right: 20px;">
	        <button id="saveInfoBtn" class="layui-btn  layui-btn-sm">保存草稿</button>
	        <button id="startProcess" class="layui-btn layui-btn-sm">提交</button>
	        <button class="layui-btn layui-btn-sm back_btn" onclick="back()">退出</button>
	    </span>
	</div>
	<div class="container" style="width:96%">
		<div class="content">
		<form action="" class="layui-form">
		<table class="layui-table">
					<colgroup>
					    <col width="150">
					    <col>
					    <col width="150">
					    <col> 
					</colgroup>
					<tbody>
						<tr>
							<th colspan="4" class="list_title">目标店调查表
							<span style="float: right;font-size:14px;font-weight:normal;">表单编号：${formId}</span>
							</th>
						</tr>
						<tr>
							<th colspan="4" >
								<div class="layui-progress layui-progress-big" lay-showPercent="yes" style="position:relative;">
								  <div class="layui-progress-bar" lay-percent="50%"></div><span class="progress_time" >审批剩余时间6小时</span>
								</div>
							</th>
						</tr>
					    <tr>
					      <td class="td_title">工号</td>
					      <td><input type="text" name="title" required  lay-verify="required" value="${currentUser.userId}" autocomplete="off" class="layui-input"></td>
					      <td class="td_title">姓名</td>
					      <td><input type="text" name="title" required  lay-verify="required" value="${currentUser.userName}" autocomplete="off" class="layui-input"></td>
					    </tr>
					    <tr>
					      <td class="td_title">创建日期</td>
					      <td><input type="text" name="title" required  lay-verify="required" value="2018-05-23" autocomplete="off" class="layui-input"></td>
					      <td class="td_title">所属部门</td>
					      <td>
							<select id="processType" lay-verify="required" >
								<option value="">请选择部门</option>
								<c:forEach items="${userDepartmentList}" var="department">
									<option value="${department.companyCode}">${department.departName}</option>
								</c:forEach>
							</select>
					      </td>
					    </tr>
					</tbody>
				</table>
				</form>
		
			<div id="formSet">${formHtml }</div>
			<table class="layui-table">
		<colgroup>
		    <col width="150">
		    <col>
		    <col width="150">
		    <col> 
		</colgroup>
		<tbody>
			<c:forEach items="${activityMetaList}" var="activityMeta">
			    <tr>
			      <td>下一环节:<span class="tip_span"></span></td>
			      <td>
			      	<input type="text" name="title" required  lay-verify="required" value="${activityMeta.activityName}"  readonly="readonly" autocomplete="off" class="layui-input">
			      </td>
			      <td class="td_title">处理人:<span class="tip_span"></span></td>
			      <td>
			      	<input type="hidden" class="getUser" id="${activityMeta.activityId}"  value="${activityMeta.userUid}" 
			      	  data-assignvarname="${activityMeta.dhActivityConf.actcAssignVariable }"  data-signcountvarname="${activityMeta.dhActivityConf.signCountVarname }"
			      	  data-looptype="${activityMeta.loopType }" />
			      	<input type="text"    id="${activityMeta.activityId}_view"  required  lay-verify="required" value="${activityMeta.userName}"  readonly="readonly" autocomplete="off" class="layui-input">
			      	<input type="hidden"  id="choosable_${activityMeta.activityId}"  value="${activityMeta.userUid}"  />
			      </td>
			      <td colspan="3">
			      	<i class="layui-icon"  onclick="getConductor('${activityMeta.activityId}','false','${activityMeta.dhActivityConf.actcCanChooseUser}','${activityMeta.dhActivityConf.actcAssignType}');" >&#xe612;</i>
			      </td>
			    </tr>
		    </c:forEach>
		</tbody>
	</table>
	<div class="layui-tab">
					  	<ul class="layui-tab-title">
					  		<li class="layui-this">附件</li>
					  	</ul>
			<div class="layui-tab-content" style="padding:0;">	
				<div class="layui-tab-item layui-show" style="height: auto;">
						    	<table class="layui-table upload-file-table" style="margin:0;">
									<colgroup>
									    <col width="5%">
									    <col width ="20%"><%-- 
									    <col width="15%">
									    <col width="20%">  --%>
									    <col width="10%">
									    <col width="10%">
									    <col width="20%">
									</colgroup>
									<thead>
									    <tr>
									      <th><!-- <input id="all-file-check" type="checkbox"> -->序号</th>
									      <th>附件名称</th><!-- 
									      <th>附件说明</th>
									      <th>附件类型</th> -->
									      <th>上传人</th>
									      <th>上传时间</th>
									      <th>
									      	<button class="layui-btn layui-btn-primary layui-btn-sm upload" id="upload-file" style="margin-left:20px;">上传附件</button>
									      	<div class="hidden-value">
												<input class="maxFileSize" value="20" type="hidden" />
												<input class="maxFileCount" value="10" type="hidden" />
												<input class="fileFormat" value="jpg,png,xls,xlsx,exe" type="hidden" />
											</div>
									      	 </th>
									    </tr> 
									</thead>
									<tbody>
								
									</tbody>
								</table>
					</div>
				</div>
			</div>
		
			
		</div>
	</div>
	
	<!-- 附件上传模态框 -->
	<div class="display_content_accessory_file" id="upload_file_modal" >
				<div class="top">文件上传</div>
				<div class="upload_overflow_middle">
					<div class="layui-upload-drag" style="width: 94.5%;">
						<i class="layui-icon"></i>
						<p>点击上传，或将文件拖拽到此处</p>
					</div>
					<div class="layui-upload">
						<div class="layui-upload-list">
							<table class="layui-table">
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
					<button class="layui-btn layui-btn-primary " onclick="cancelClick(this)">取消</button>
				</div>
			</div>
		
</body>

</html>
<script type="text/javascript" src="resources/desmartportal/js/jquery-3.3.1.js"></script>
<script type="text/javascript" src="resources/desmartportal/js/layui.all.js"></script>
<script type="text/javascript" src="resources/desmartportal/js/common.js"></script>
<script type="text/javascript" src="resources/desmartportal/js/my/process.js"></script>
<!-- 附件上传js -->
	<script src="resources/desmartportal/js/my/myFileUpload.js"></script>
	<script>

</script>
<script type="text/javascript" src="resources/desmartportal/formDesign/js/my.js"></script>