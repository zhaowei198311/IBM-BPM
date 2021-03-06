<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<head>
    <base href="<%=basePath%>">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>待办任务</title>
    <link href="resources/desmartportal/formDesign/css/layoutit.css" rel="stylesheet">
    <link href="resources/desmartportal/css/modules/laydate/default/laydate.css" rel="stylesheet">
    <link href="resources/desmartportal/css/layui.css" rel="stylesheet" />
    <link href="resources/desmartportal/css/my.css?v=1.02" rel="stylesheet" />
    <link href="resources/desmartportal/js/css/myFileUpload.css" rel="stylesheet" />
    <link rel="Stylesheet" type="text/css" href="resources/desmartportal/wPaint-master/wPaint.css" />
    <link rel="Stylesheet" type="text/css" href="resources/desmartportal/wPaint-master/inc/wColorPicker.css" />
    <link rel="stylesheet" href="resources/desmartportal/selects/formSelects-v4.css">
    <!-- <script type="text/javascript" src="resources/desmartportal/js/jquery-3.3.1.js"></script> -->

    <script type="text/javascript" src="resources/desmartportal/wPaint-master/inc/jquery.1.8.2.min.js"></script>
    <script type="text/javascript" src="resources/desmartportal/js/layui.all.js"></script>
    <script type="text/javascript" src="resources/desmartportal/selects/formSelects-v4.js"></script>
    <script type="text/javascript" src="resources/desmartportal/formDesign/js/my.js?v=1.03"></script>
    <script type="text/javascript" src="resources/desmartportal/js/common.js?v=1.03"></script>

    <!-- jQuery -->
    <script type="text/javascript" src="resources/desmartportal/wPaint-master/inc/jquery.ui.core.min.js"></script>
    <script type="text/javascript" src="resources/desmartportal/wPaint-master/inc/jquery.ui.widget.min.js"></script>
    <script type="text/javascript" src="resources/desmartportal/wPaint-master/inc/jquery.ui.mouse.min.js"></script>
    <script type="text/javascript" src="resources/desmartportal/wPaint-master/inc/jquery.ui.draggable.min.js"></script>

    <!-- wColorPicker -->
    <script type="text/javascript" src="resources/desmartportal/wPaint-master/inc/wColorPicker.js"></script>

    <!-- wPaint -->
    <script type="text/javascript" src="resources/desmartportal/wPaint-master/wPaint.js"></script>
    <script type="text/javascript" src="resources/desmartportal/js/my/approval.js"></script>
    <script type="text/javascript" src="resources/desmartportal/js/city.js"></script>

    <style type="text/css">
        .container-fluid {
            padding-right: 0px;
            padding-left: 0px;
            zoom: 1;
        }
        .display_container_image_edit {
            z-index: 1001;
            color: #717171;
            padding: 20px;
            width: 95%;
            height: 88%;
            background: #fff;
            position: fixed;
            left: 1%;
            top: 50px;
            right: 1%;
            box-shadow: 0 0 10px #ccc;
            display: none;
        }
        .display_content_image_edit {
            z-index: 1001;
            width: 100%;
            height: 100%;
            background: #fff;
            box-shadow: 0 0 10px #ccc;
        }
        .display_container_file {
        	display:none;
		    position: fixed;
		    top: 0;
		    left: 0;
		    z-index: 1001;
		    background: rgba(255, 255, 255, 0.8);
		    width: 100%;
		    height: 100%;
		}
		#showHistoryModal{
			display:none;
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
            z-index: 1001;
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
        .choose_user {
            position: absolute;
            right: 0px;
            top: 8px;
            z-index: 1;
            width: 35px;
	    	background: #fff;
	    	text-align: center;
        }
        #formSet {
            display: none;
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
        .display_content6 {
            width: 40%;
            height: 100px;
            left: 70%;
            margin-top: 10%;
        }
    </style>
</head>
<body>
	<div>
      <div id="Mask"></div>
      <div id="Progress" data-dimension="180" data-text="0%" data-info="下载进度" data-width="30" data-fontsize="38" data-percent="0" data-fgcolor="#009688" data-bgcolor="#eee"></div>
 	</div>
    <div class="search_area top_btn" id="layerDemo" style="height:113px">
		<div id="bpmInfoDiv" style="display:none;">
			<input id="taskUid" value="${taskInstance.taskUid}"/>
			<input id="taskId" value="${taskInstance.taskId}"/>
			<input id="insUid" value="${processInstance.insUid}"/>
			<input id="insId" value="${processInstance.insId}"/>
			<input id="proAppId" value="${processInstance.proAppId}"/>
			<input id="proUid" value="${processInstance.proUid}"/>
			<input id="proVerUid" value="${processInstance.proVerUid}"/>
			<input id="insBusinessKey" value="${processInstance.insBusinessKey}"/>
			<input id="departNo" value="${processInstance.departNo}"/>
			<input id="companyNum" value="${processInstance.companyNumber}"/>
			<input id="activityId" value="${activityMeta.activityId}"/>
			<input id="activityName" value="${activityMeta.activityName}"/>
			<input id="userName" value="${currentUser.userName}"/>
			<input id="taskStatus" value="${taskInstance.taskStatus}"/>
			<input id="actcCanReject" value="${activityConf.actcCanReject}"/>
			<input id="actcCanTransfer" value="${activityConf.actcCanTransfer}"/>
			<input id="actcCanAdd" value="${activityConf.actcCanAdd}"/>
			<input id="canEditInsTitle" value="${canEditInsTitle}"/>
			<input id="skipFromReject_newTaskOwnerName" value="${dataForSkipFromReject.newTaskOwnerName}"/>
			<input id="skipFromReject_targetNodeName" value="${dataForSkipFromReject.targetNode.activityName}"/>
			<input id="needApprovalOpinion" value="${needApprovalOpinion}"/>
			<span id="formData">${formData}</span>
			<span id="fieldPermissionInfo">${fieldPermissionInfo}</span>
			<span id="listStr">${listStr}</span>
			<span id="approvalData">${approvalData}</span>
		</div>
        <div class="layui-row">
            <div class="layui-col-sm6">
                <img src="resources/desmartportal/images/icon.png" class="icon"/>
                <span class="table_title">${bpmForm.dynTitle}</span>
            </div>
            <div class="layui-col-sm6">
                <span style="float: right; padding-right: 40px;">
					<span <c:if test="${didTriggerBeforeFormError == true}" >style="display: none;"</c:if>>
						<button class="layui-btn layui-btn-sm layui-btn-normal chart" onclick="processView(${processInstance.insId})">流程图</button>
						<button class="layui-btn layui-btn-normal layui-btn-sm" onclick="saveDraftsInfo()">保存草稿</button>
						<button class="layui-btn layui-btn-sm layui-btn-normal submit_btn" onclick="checkUserData()">提交</button>
						<button class="layui-btn layui-btn-sm layui-btn-normal turn_down" id="reject" onclick="queryRejectByActivitiy()" <c:if test="${activityConf.actcCanReject =='FALSE'}">style="display:none;"</c:if>>驳回</button>
						<button class="layui-btn layui-btn-sm layui-btn-normal turn_btn" id="transfer" <c:if test="${activityConf.actcCanTransfer =='FALSE'}">style="display:none;"</c:if>>抄送</button>
						<button class="layui-btn layui-btn-sm layui-btn-normal countersign" id="add" <c:if test="${activityConf.actcCanAdd =='FALSE'}">style="display:none;"</c:if>>会签</button>
					</span>
					<span >
                    	<button class="layui-btn layui-btn-normal layui-btn-sm back_btn" onclick="back()">退出</button>
					</span>
                </span>
            </div>
        </div>
        <div class="layui-row" style="margin: 0px 0 0 40px; padding-right: 40px;">
            <div class="layui-col-sm4">发起人：${processInstance.initUserFullname}(${processInstance.insInitUser})</div>
            <div class="layui-col-sm4">部门：${processInstance.departName} - ${processInstance.companyName}</div>
            <div class="layui-col-sm4">填写时间：
                <fmt:formatDate value="${processInstance.insInitDate}" type="date" pattern="yyyy-MM-dd" />
            </div>
        </div>
        <div class="layui-row" style="margin-left: 40px; padding-right: 40px;">
            <div class="layui-col-sm4">
            	流程编号：<span style="color: #1890ff;">${processInstance.proNo}</span>
            </div>
            <div class="layui-col-sm4">
				<c:if test="${!empty bpmForm.formNo }">
					表单流水号： <span style="color: #1890ff;">${bpmForm.formNo}</span>
				</c:if>
            </div>
            <div class="layui-col-sm4">
            	表单编号： <span style="color: #1890ff;">${bpmForm.formNoStatic}</span>
            </div>
        </div>
        <div class="layui-row" style="margin-left: 40px; padding-right: 40px;">
        	<div class="layui-col-sm11" style="position: relative;">
                <div class="layui-progress layui-progress-big" lay-filter="progressBar" lay-showPercent="yes" style="position: relative;top: 5px;">
                    <div class="layui-progress-bar" lay-percent="0%"></div>
                    <!--<span class="progress_time">审批剩余时间6小时</span> -->
                </div>
            </div>
        </div>
    </div>

    <div class="layui-fluid" style="padding-top: 150px;">
		<div class="table_content">
			<div class="table_container" <c:if test="${showResponsibility=='FALSE'}">style="display:none;"</c:if>>
				<p class="title_p">本环节审批要求</p>
				<div class="layui-form" style="padding:10px 20px 20px;">
					${activityConf.actcResponsibility }
				</div>
			</div>
			<div class="table_container">
				<p class="title_p">流程主题</p>
				<table class="layui-table basic_information" lay-skin="nob">
					<tbody>
						<tr>
							<td class="td_title" colspan="1" style="width: 120px">
								流程主题
								<span class="tip_span"> *</span>：
							</td>
							<td class="sub_title" colspan="5"><c:choose>
									<%-- 可编辑标题 --%>
									<c:when test="${canEditInsTitle == true}">
										<input type="text" id="insTitle_input" value="${processInstance.insTitle }" class="layui-input" />
									</c:when>
									<%-- 不可编辑标题 --%>
									<c:otherwise>
										<input type="text" id="insTitle_input" class="layui-input"
											value="${processInstance.insTitle }" disabled />
									</c:otherwise>
								</c:choose></td>
						</tr>
					</tbody>
				</table>
			</div>
			<div id="formSet">${bpmForm.dynWebpage }</div>

			<div class="table_container">
				<p class="title_p" id="approve_p" <c:if test="${needApprovalOpinion == false}">style="display:none;"</c:if>>审批信息</p>
				<div style="padding:10px 20px 20px;">
					<div class="layui-form" id="approve_div" style="<c:if test="${needApprovalOpinion == false}">display:none;</c:if>">
						<textarea placeholder="意见留言" class="layui-textarea"
							id="myApprovalOpinion" style="margin-bottom: 10px;"></textarea>
						<div style="margin-top: 10px;">
							<label class="layui-form-label">常用语</label>
							<div class="layui-input-block">
								<select class="layui-form" lay-filter="useselfChange">
									<option value="-1">--请选择--</option>
									<option value="同意">同意</option>
									<option value="不同意">不同意</option>
								</select>
							</div>
						</div>
					</div>
				</div>
				<div style="padding:0 20px 20px;">
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
										<col width="8%">
										<col width="18%">
										<col width="8%">
										<col width="6%">
										<col width="8%">
										<col width="13%">
										<col width="8%">
										<col width="12%">
										<col width="8%">
										<col width="15%">
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
										<col width="10%">
										<col width="20%">
									</colgroup>
									<thead>
										<tr>
											<th><input style='cursor: default;' id="all-file-check"
												type="checkbox"> <!-- 序号 --></th>
											<th>附件名称</th>
											<!-- 
										      <th>附件说明</th>
										      <th>附件类型</th> -->
											<th>上传人</th>
											<th>上传时间</th>
											<th>
												<button	class="layui-btn layui-btn-primary layui-btn-sm upload"
													id="upload-file" style="margin-left: 20px;
													<c:if test=" ${activityConf.actcCanUploadAttach=='FALSE'} " >display:none;</c:if>">
													上传附件
												</button>
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
							<div class="layui-tab-item">
								<div class="p">
									<table class="layui-table" lay-even lay-skin="nob" lay-size="sm">
 									<colgroup>
										<col width="10%">
										<col width="22.5%">
										<col width="22.5%">
										<col width="22.5%">
										<col width="22.5%">
									</colgroup>
									<thead>
									<tr><th>现在的环节号</th>
									<th>当前处理人</th>
									<th>当前处理人岗位</th>
									<th>当前处理环节</th>
									<th>当前处理到达时间</th></tr>
									</thead>
									<tbody id="routingRecordTbody">
									
									</tbody>
									</table>
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
    </div>
    <!--IE8只能支持jQuery1.9-->
    <!--[if lte IE 8]>
	    <script src="http://cdn.bootcss.com/jquery/1.9.0/jquery.min.js"></script>
	    <![endif]-->
    <!-- 让IE8/9支持媒体查询，从而兼容栅格 -->
    <!--[if lt IE 9]>
		  <script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
		  <script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
		<![endif]-->
	<!-- 下个环节信息 -->
	<div class="display_container2">
		<div class="display_content2" style="width: 700px; left: 44%">
			<div class="top">下个环节信息</div>
			<div class="middle2">
				<table class="layui-table">
					<col width="14%">
					<col width="35%">
					<col width="13%">
					<col width="33%">
					<col width="7%">
					<tbody id="choose_user_tbody">

					</tbody>
				</table>
			</div>
			<div class="foot">
				<button class="layui-btn sure_btn" onclick="doSubmit();">确定</button>
				<button class="layui-btn layui-btn-primary cancel_btn"
					onclick="$('.display_container2').css('display','none')">取消</button>
			</div>
		</div>
	</div>
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
	                        <colgroup>
	                            <col width="5%">
	                            <col width="20%">
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
	            <button class="layui-btn layui-btn layui-btn-primary cancel_btn" onclick="cancelClick(this)">关闭</button>
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
	            <button class="layui-btn layui-btn layui-btn-primary cancel_btn" onclick="hideHistoryFile()">关闭</button>
	        </div>
	    </div>
    </div>
    <!-- 会签选择人员 -->
    <div class="display_container7" style="display: none;">
        <div class="display_content7 layui-form" style="height: 200px;width:500px;">
            <div style="margin-top: 15px;" class="layui-row">
                <label class="layui-form-label" style="width: 60px;">会签方式</label>
                <div class="layui-input-block">
                    <select class="layui-form_1" lay-filter="useselfChange">
                        <option value="normalAdd">随机会签</option>
                        <option value="simpleLoopAdd">顺序会签</option>
                        <option value="multiInstanceLoopAdd">并行会签</option>
                    </select>
                </div>
            </div>
            <div class="layui-row" style="margin-top:30px;">
	            <label class="layui-form-label" style="width: 60px;">人员</label>
	            <div class="layui-input-block" style="position:relative;">
	                <input type="hidden" id="handleUser" name="handleUser" />
	                <input type="text" name="handleUser_view" id="handleUser_view" autocomplete="off" class="layui-input" disabled="disabled">
	                <i class="layui-icon choose_user" id="choose_handle_user" title="选择人员">&#xe612;</i>
	            </div>
	        </div>
            <div class="foot" style="margin-top: 25px;">
                <button class="layui-btn layui-btn sure_btn" onclick="addSure()">确定</button>
                <button class="layui-btn layui-btn cancel_btn">取消</button>
            </div>
        </div>
    </div>
    <!-- 抄送选择人员 -->
    <div class="display_container6" style="display: none;">
        <div class="display_content6">
            <label class="layui-form-label" style="width: 30px;">人员</label>
            <div class="layui-input-block" style="position:relative;margin-left: 60px;">
                <input type="hidden" id="handleUser1" name="handleUser1" />
                <input type="text" id="handleUser1_view" name="handleUser1_view" autocomplete="off" class="layui-input" disabled="disabled">
                <i class="layui-icon choose_user" id="choose_handle_user1" title="选择人员">&#xe612;</i>
            </div>
            <div class="foot" style="margin-top: 25px;">
                <button class="layui-btn layui-btn sure_btn" onclick="transferSure()">确定</button>
                <button class="layui-btn layui-btn cancel_btn">取消</button>
            </div>
        </div>
    </div>
    <div class="display_container8">
        <div class="display_content10">
            <div class="top">
               	 驳回至：
            </div>
            <div class="middle10">
                <ul id="middle10">

                </ul>
            </div>
            <div class="foot">
                <button id="reject_btn" class="layui-btn reject_btn">确定</button>
                <button class="layui-btn layui-btn-primary cancel5_btn">取消</button>
            </div>
        </div>
    </div>
    <!-- 附件图片编辑 -->
    <div class="display_container_image_edit">
        <div class="top">附件图片编辑</div>
        <img onclick="closeImageEdit()" style="position: absolute;left: 94%;bottom: 92%;" alt="关闭" src="resources/desmartportal/images/close.jpg">
        <div class="display_content_image_edit">
            <div id="imgEditMain" style="width: 100%;height: 88%;overflow:  scroll;">

                <input style="display: none;" id="imgEditAccessoryFileData">
                <div id="wPaint" style="position:relative; width:500px; height:1000px;
					background:#CACACA; border:solid black 1px;"></div>

            </div>
        </div>
    </div>
</body>
<script type="text/javascript" src="resources/desmartportal/js/my/myWPaint.js"></script>
<!-- 附件上传js -->
<script src="resources/desmartportal/js/my/myFileUpload.js"></script>
<!-- 审批信息js -->
<script src="resources/desmartportal/js/my/myApprovalOpinion.js"></script>
<script type="text/javascript">
	var didTriggerBeforeFormError = '${didTriggerBeforeFormError}';
    $(function () {
        $(".data-table").find("input[type='tel']").desNumber();
        if (didTriggerBeforeFormError == 'true') {
			layer.msg("获得数据失败，请联系管理员");
		}
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
</html>

