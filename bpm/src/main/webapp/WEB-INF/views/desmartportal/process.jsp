<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
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
<link href="resources/desmartportal/formDesign/css/layoutit.css"
	rel="stylesheet">
<link
	href="resources/desmartportal/css/modules/laydate/default/laydate.css"
	rel="stylesheet">
<link rel="stylesheet" href="resources/desmartportal/css/layui.css"
	media="all">
<link rel="stylesheet" href="resources/desmartportal/css/my.css?v=1.02" />
<link href="resources/desmartportal/js/css/myFileUpload.css" rel="stylesheet" />
<link rel="stylesheet" href="resources/desmartportal/selects/formSelects-v4.css">
<script type="text/javascript" src="resources/desmartportal/js/jquery-3.3.1.js"></script>
<script type="text/javascript" src="resources/desmartportal/js/layui.all.js"></script>
<script type="text/javascript" src="resources/desmartportal/selects/formSelects-v4.js"></script>
<script type="text/javascript" src="resources/desmartportal/formDesign/js/my.js?v=1.02"></script>
<script type="text/javascript" src="resources/desmartportal/js/common.js?v=1.02"></script>
<script type="text/javascript" src="resources/desmartportal/js/my/process.js"></script>
<script type="text/javascript" src="resources/desmartportal/js/city.js"></script>
<style type="text/css">
.container-fluid {
	padding-right: 0px;
	padding-left: 0px;
	zoom: 1;
}

.display_container_file {
	display: none;
	position: fixed;
	top: 0;
	left: 0;
	z-index: 1001;
	background: rgba(255, 255, 255, 0.8);
	width: 100%;
	height: 100%;
}

#showHistoryModal {
	display: none;
	position: fixed;
	top: 0;
	left: 0;
	z-index: 1001;
	background: rgba(255, 255, 255, 0.8);
	width: 100%;
	height: 100%;
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

.foot_history_file {
	padding-top: 5px;
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
	overflow-y: auto;
	overflow-x: hidden;
}

#formSet {
	display: none;
}

.choose_middle {
	min-height: 100px;
	width: 96%;
	border: 1px solid #ccc;
	position: relative;
	padding: 0 10px;
}

.choose_middle .layui-input {
	width: 80%;
}

.choose_display_content {
	color: #717171;
	padding: 20px;
	width: 600px;
	min-height: 200px;
	background: #fff;
	position: absolute;
	margin: 100px 0 0 -300px;
	left: 50%;
	box-shadow: 0 0 10px #ccc;
}

.choose_user_div .form_label {
	float: left;
	display: block;
	padding: 9px 15px;
	font-weight: 400;
	line-height: 20px;
	text-align: right;
}
</style>
</head>

<body>
	<div>
      <div id="Mask"></div>
      <div id="Progress" data-dimension="180" data-text="0%" data-info="下载进度" data-width="30" data-fontsize="38" data-percent="0" data-fgcolor="#009688" data-bgcolor="#eee"></div>
 	</div>
	<div class="search_area top_btn" id="layerDemo">
		<div id="bpmInfoDiv" style="display: none;">
			<c:choose>
				<c:when test="${fn:length(userDepartmentList)==1}">
					<input id="departNo" value="${userDepartmentList[0].departNo}" />
					<input id="companyNum" value="${userDepartmentList[0].sysCompany.companyCode}" />
				</c:when>
				<c:otherwise>
					<input id="departNo" value="${departNo}" />
					<input id="companyNum" value="${companyNumber}" />
				</c:otherwise>
			</c:choose>
			<input id="insUid" value="${processInstance.insUid}" />
			<input id="proAppId" value="${processDefinition.proAppId}"/>
			<input id="proUid" value="${processDefinition.proUid}"/>
			<input id="verUid" value="${processDefinition.proVerUid}"/>
			<input id="insTitle" value="${processInstance.insTitle}"/>
			<input id="formId" value="${dhStep.stepObjectUid}"/>
			<input id="proName" value="${processDefinition.proName}"/>
			<input id="userId" value="${currentUser.userId}"/>
			<input id="userName" value="${currentUser.userName}"/>
			<input id="activityId" value="${bpmActivityMeta.activityId}"/>
			<input id="activityName" value="${bpmActivityMeta.activityName}"/>
			<span id="formData">${formData}</span>
			<span id="fieldPermissionInfo">${ fieldPermissionInfo}</span>
		</div>
        <div class="layui-row">
            <div class="layui-col-md8">
                <img src="resources/desmartportal/images/icon.png" class="icon"/>
                <span class="table_title">${bpmForm.dynTitle}</span>
            </div>
            <div class="layui-col-md4">
                <span style="float: right; padding-right: 40px;">
                    <button id="saveInfoBtn" class="layui-btn layui-btn-normal layui-btn-sm">保存草稿</button>
                    <button id="startProcess_btn" class="layui-btn layui-btn-sm layui-btn-normal submit_btn">提交</button>
                    <button class="layui-btn layui-btn-normal layui-btn-sm back_btn" onclick="back()">退出</button>
                </span>
            </div>
        </div>
        <div class="layui-row" style="margin: 0px 0 0 40px; padding-right: 40px;">
            <div class="layui-col-md4">发起人：${currentUser.userName}(${currentUser.userId})</div>
            <div class="layui-col-md4" id="createDate">
            	
            </div>
        </div>
        <div class="layui-row" style="margin-left: 40px; padding-right: 40px;">
            <div class="layui-col-md4 layui-form">
					<c:choose>
						<c:when test="${fn:length(userDepartmentList)==1}">
							部门：${userDepartmentList[0].departName} - ${userDepartmentList[0].sysCompany.companyName }
						</c:when>
						<c:otherwise>
						<label class="layui-form-label" style="padding: 9px 0px;text-align:left;width:11%">部门：</label>
						<div class="layui-input-block" style="margin-left:11%;">
							<select id="creatorInfo" lay-filter="creatorInfo">
								<option value="">请选择部门</option>
								<c:forEach items="${userDepartmentList}" var="item">
									<c:choose>
										<c:when test="${item.departNo eq departNo && item.companyCode eq companyNumber}">
											<option selected="selected" value="${item.departNo},${item.companyCode}">${item.departName} - ${item.sysCompany.companyName }</option>
										</c:when>
										<c:otherwise>
											<option value="${item.departNo},${item.companyCode}">${item.departName} - ${item.sysCompany.companyName }</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</select>
							</div>
						</c:otherwise>
					</c:choose>
            </div>
			<div class="layui-col-md4">
				流程编号：<span style="color: #1890ff;">${processInstance.proNo}</span>
			</div>
			<div class="layui-col-md4">
				表单编号： <span style="color: #1890ff;">${bpmForm.formNoStatic}</span>
				<%--<c:if test="${!empty bpmForm.formNo }">--%>
					<%--表单流水号： <span style="color: #1890ff;">${bpmForm.formNo}</span>--%>
				<%--</c:if>--%>
			</div>
        </div>
    </div>

	<div class="layui-fluid" style="padding-top: 135px;">
		<div class="table_content">
			<div class="table_container">
				<p class="title_p">流程主题</p>
				<table class="layui-table basic_information" lay-skin="nob">
					<tbody>
						<tr>
							<td class="td_title" colspan="1" style="width: 120px">流程主题<span class="tip_span"> *</span>：</td>
							<td class="sub_title" colspan="5"><input type="text"
								id="insTitle_input" class="layui-input"
								value="${draftTitle}"></td>
						</tr>
					</tbody>
				</table>
			</div>
			<!-- 动态表单区域 -->
			<div id="formSet">${bpmForm.dynWebpage }</div>

			<div class="table_container">
				<p class="title_p">附件上传</p>
				<div class="layui-tab-item layui-show"
					style="height: auto; padding: 10px 20px 20px">
					<table class="layui-table upload-file-table" style="margin: 0;">
						<colgroup>
							<col width="5%">
							<col width="20%">
							<col width="10%">
							<col width="20%">
						</colgroup>
						<thead>
							<tr>
								<th><input style='cursor: default;' id="all-file-check"
									type="checkbox"></th>
								<th>附件名称</th>
								<th>上传人</th>
								<th>上传时间</th>
								<th>
									<button class="layui-btn layui-btn-primary layui-btn-sm upload"
										id="upload-file"
										style="margin-left: 20px;<c:if test="${activityConf.actcCanUploadAttach =='FALSE'}" >display:none;</c:if>">上传附件</button>
									<button onclick="batchDown()"
										class="layui-btn layui-btn-primary layui-btn-sm "
										id="batch-down-file" style="margin-left: 20px;">批量下载</button>
									<div class="hidden-value">
										<input class="maxFileSize" value="20" type="hidden" /> <input
											class="maxFileCount" value="10" type="hidden" /> <input
											class="fileFormat"
											value="jpg,png,xls,xlsx,doc,docx,txt,pdf,ppt,pptx"
											type="hidden" />
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

	<div class="display_container2">
		<div class="display_content2">
			<div class="top" style="line-height: 50px;">选择下一环节</div>
			<div class="middle2">
				<table class="layui-table">
					<col width="19%">
					<col>
					<col width="15%">
					<col>
					<col width="10%">
					<tbody id="choose_user_tbody">

					</tbody>
				</table>
			</div>
			<div class="foot">
				<button class="layui-btn sure_btn" onclick="submitProcess();">确定</button>
				<button class="layui-btn layui-btn-primary cancel_btn"
					onclick="$('.display_container2').css('display','none')">取消</button>
			</div>
		</div>
	</div>

	<!-- 附件上传模态框 -->
	<div class="display_container_file">
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
							<thead>
								<tr>
									<th>文件名</th>
									<th>大小</th>
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
				<button type="button" onclick="saveDraftIfNotExists();" class="layui-btn listAction" >开始上传</button>
				<button class="layui-btn layui-btn-primary "
					onclick="cancelClick(this)">关闭</button>
			</div>
		</div>
	</div>

	<div id="showHistoryModal">
		<div class="display_content_accessory_file">
			<div class="top">历史版本</div>
			<div class="upload_overflow_middle">
				<div class="layui-upload">
					<div class="layui-upload-list">
						<table class="layui-table">
							<colgroup>
								<col width="10%">
								<col width="20%">
								<col width="10%">
								<col width="10%">
								<col width="20%">
								<col width="10%">
							</colgroup>
							<thead>
								<tr>
									<th>文件版本</th>
									<th>文件名</th>
									<th>上传人</th>
									<th>修改人</th>
									<th>修改时间</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody class="showHistoryList"></tbody>
						</table>
					</div>
				</div>
			</div>
			<div class="foot_history_file">
				<button class="layui-btn layui-btn layui-btn-primary cancel_btn"
					onclick="hideHistoryFile()">关闭</button>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
	$(function(){
		$(".data-table").find("input[type='tel']").desNumber();
	});
</script>
</html>
<!-- 附件上传js -->
<script src="resources/desmartportal/js/my/myFileUpload.js"></script>
<script type="text/javascript">
	//数据信息
	var view = $(".container-fluid");
	var form = null;
	$(function() {
		console.log("formData: ===============");
		console.log($("#formData").text());
		console.log("fieldPermissionInfo: ===============");
        console.log($("#fieldPermissionInfo").text());
		
		layui.use([ 'form' ], function() {
			form = layui.form;
		});
		var formData = $("#formData").text();
		if (formData != null && formData != "") {
			common.giveFormSetValue(formData);
		}
		var formFieldPer = $("#fieldPermissionInfo").text().trim();
		common.giveFormFieldPermission(formFieldPer);
        form.render();
        
        common.initTime();
        
        $("#formSet").find("select").each(function(){
			var id = $(this).prop("id");
			if($(this).attr("is-multi")=="true"){
				$(this).attr("xm-select",id);
				var formSelects = layui.formSelects;
				formSelects.render(id);
				formSelects.on(id, function(id, vals, val, isAdd, isDisabled){
					$("#"+id).trigger("change");
				    return true;   
				});
			}
		});
	});

</script>