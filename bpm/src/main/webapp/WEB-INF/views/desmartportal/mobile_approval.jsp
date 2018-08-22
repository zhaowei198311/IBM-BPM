<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
    String path = request.getContextPath();
     String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
             + path + "/";
%>
<html>
        
<head>
    <base href="<%=basePath%>">
    <meta charset="utf-8" />
    <!-- <meta name="apple-mobile-web-app-capable" content="yes"> -->
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
	<meta http-equiv="X-UA-Compatible" content="IE=Edge，chrome=1">
	<!--[if lt IE 9]>
	  <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
	  <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
	<![endif]-->
    <title>待办任务</title>
    <%@include file="common/mobile_head.jsp" %>
    <script type="text/javascript" src="resources/desmartportal/js/my/mobile_approval.js?v=1.01"></script>
</head>
<body style="background-color: #f2f2f2;margin-bottom:40px;">
    <div class="mobile_container" id="approval">
        <div class="mobile_top">
        	<div>
				<div class="top_left">
					<i class="layui-icon" onclick="back()">&#xe65c;</i>
				</div>
				<div class="top_content">
					<span>${processInstance.proName}</span>
				</div>
			</div>
            <div class="top_right">
            	
            </div>
        </div>
        <div class="task_div layui-tab layui-tab-brief">
			<ul class="layui-tab-title mobile_btn">
				<li class="layui-this">
					<p>内容</p>
				</li>
				<li>
					<p>审批</p>
				</li>
				<li>
					<p>附件</p>
				</li>
				<li>
					<p>记录</p>
				</li>
			</ul>
			<div class="layui-tab-content mobile_middle" style="padding-top:0px">
				<!-- 表单详情 -->
				<div class="layui-tab-item layui-show" id="form_content_div">
					<div id="search_top">
						<div>
							<span id="fieldPermissionInfo" style="display: none;">${fieldPermissionInfo}</span>
					    	<input id="departNo" type="hidden" value="${processInstance.departNo}"/>
					    	<input id="companyNum" type="hidden" value="${processInstance.companyNumber}"/>
					       	<input id="activityId" value="${activityMeta.activityId}" style="display: none;">
					        <input id="proUid" value="${processInstance.proUid}" style="display: none;">
					        <input id="proAppId" value="${processInstance.proAppId}" style="display: none;">
					        <input id="proVerUid" value="${processInstance.proVerUid}" style="display: none;">
					        <input id="insUid" value="${processInstance.insUid}" style="display: none;">
					        <input id="insId" value="${processInstance.insId}" style="display: none;">
					        <input id="userName" value="${currentUser.userName}" style="display: none;">
					        <input id="taskId" value="${taskInstance.taskId}" style="display: none;">
					        <input id="taskUid" value="${taskInstance.taskUid}" style="display: none;">
					        <input id="taskStatus" value="${taskInstance.taskStatus}" style="display: none;">
					        <input id="actcCanReject" value="${activityConf.actcCanReject}" style="display: none;">
					        <input id="actcCanTransfer" value="${activityConf.actcCanTransfer}" style="display: none;">
					        <input id="actcCanAdd" value="${activityConf.actcCanAdd}" style="display: none;">
					        <input id="canEditInsTitle" value="${canEditInsTitle}" style="display: none;"/>
					        <input id="isReject" value="${activityConf.actcCanReject}" style="display: none;"/>
					        <input id="skipFromReject_newTaskOwnerName" type="hidden" value="${dataForSkipFromReject.newTaskOwnerName}"/>
					        <input id="skipFromReject_targetNodeName" type="hidden" value="${dataForSkipFromReject.targetNode.activityName}"/>
					        <input id="needApprovalOpinion" type="hidden" value="${needApprovalOpinion}"/>
					        <span id="formData" style="display: none;">${formData}</span>
					        <span id="listStr" style="display: none;">${listStr}</span>
					        <span id="approvalData" style="display: none;">${approvalData}</span>
						</div>
						<div class="backlog_top">
							<div class="backlog_process">
								<span class="process_title_div">${bpmForm.dynTitle}</span>
								<span class="backlog_task_status">待处理</span>
							</div>
						</div>
						<div class="backlog_content">
							<table>
								<tbody>
									<tr>
										<td>创建日期：<fmt:formatDate value="${processInstance.insInitDate}" type="date" pattern="yyyy-MM-dd" /></td>
									</tr>
									<tr>
										<td>创建人：${processInstance.initUserFullname}(${processInstance.insInitUser})</td>
									</tr>
									<tr>
										<td>所属部门：${processInstance.departName} - ${processInstance.companyName}</td>
									</tr>
								</tbody>
							</table>
							<div class="progress_bar_div">
								<div class="progress_time_div">
									<span class="progress_time">剩余：6小时</span>
								</div>
								<div class="layui-progress" lay-filter="progressBar" lay-showpercent="yes" style="position: relative;">
									<div class="layui-progress-bar" lay-percent="0%" style="background-color: #FF9052; width: 75%;"></div>
								</div>
							</div>
						</div>
					</div>
					<div class="table_container layui-form">
						<p class="title_p">流程主题
							<i class="layui-icon arrow" style="float:right;" onclick="showTable(this)">&#xe61a;</i>
						</p>
						<table title="流程主题" class="layui-table">
							<tbody>
								<tr>
									<td class="td_title" colspan="1" style="width: 70px;padding-right: 0px;">
										<label>流程主题</label>
										<span class="tip_span">*</span>
									</td>
									<td class="td_sub" colspan="1">
										<c:choose>
			                                <%-- 可编辑标题 --%>
			                                <c:when test="${canEditInsTitle == true}">
			                                    <input type="text" id="insTitle_input" value="${processInstance.insTitle }" placeholder="请输入流程主题" class="layui-input" />
			                                </c:when>
			                                <%-- 不可编辑标题 --%>
			                                <c:otherwise>
			                                    <input type="text" id="insTitle_input" class="layui-input" value="${processInstance.insTitle }" disabled/>
			                                </c:otherwise>
			                            </c:choose>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
					<div id="formSet">
						${bpmForm.dynWebpage}
					</div>
					<div class="table_container backlog_content" style="padding-bottom:10px;">
						<p class="title_p">
							流程基本信息 <i class="layui-icon arrow" style="float: right;"
								onclick="showTable(this)">&#xe61a;</i>
						</p>
						<table title="流程基本信息" style="margin-left:10px;margin-top:10px;">
							<tr>
								<td>流程编号：${processInstance.proNo}</td>
							</tr>
							<tr>
								<td>表单编号：${bpmForm.formNoStatic}</td>
							</tr>
							<tr>
								<td>流水号： <c:if test="${!empty bpmForm.formNo }">${bpmForm.formNo}</c:if>
								</td>
							</tr>
						</table>
					</div>
				</div>
				<!-- 审批意见 -->
				<div class="layui-tab-item" id="approve_div">
					<div class="layui-form">
						<div class="handle_div">
							<table id="handle_type_table">
								<tr>
									<td style="text-align: center;">
										<button class="layui-btn layui-btn-sm handle_btn" onclick="handleBtnClick(this)" id="submit_btn">同意</button>
										<button class="layui-btn layui-btn-sm handle_btn" onclick="handleBtnClick(this)" id="countersign_btn" <c:if test="${activityConf.actcCanAdd =='FALSE'}" >style="display:none;"</c:if>>会签</button>
										<button class="layui-btn layui-btn-sm handle_btn" onclick="handleBtnClick(this)" id="reject_btn" <c:if test="${activityConf.actcCanReject =='FALSE'}" >style="display:none;"</c:if>>驳回</button>
										<button class="layui-btn layui-btn-sm handle_btn" onclick="handleBtnClick(this)" id="transfer_btn" <c:if test="${activityConf.actcCanTransfer =='FALSE'}" >style="display:none;"</c:if>>抄送</button>
									</td>
								</tr>
							</table>
							<!-- 同意 -->
							<div class="handle_table table_container layui-form" id="submit_table">
							</div>
							<!-- 会签 -->
							<div class="handle_table" id="countersign_table">
								<table>
									<tr>
										<th>会签方式</th>
										<td>
											<div id="countersign_type">
												<select class="layui-form_1 layui-select" lay-ignore lay-filter="useselfChange" id="countersign_type_select">
													<option value="normalAdd" selected>随机会签</option>
													<option value="simpleLoopAdd">顺序会签</option>
													<option value="multiInstanceLoopAdd">并行会签</option>
												</select>
											</div>
										</td>
									</tr>
									<tr>
										<th>处理人</th>
										<td>
											<div class="handle_person_name">
												<ul>
													<li class="choose_user_li" style="margin: 17px 8px;">
														<i class="layui-icon choose_countersign_person" onclick="getUser(this,true,'countersign_table')">&#xe654;</i>
													</li>
												</ul>
											</div>
										</td>
									</tr>
								</table>
							</div>
							<!-- 驳回 -->
							<div class="handle_table" id="reject_table">
								<table>
									
								</table>
							</div>
							<!-- 抄送 -->
							<div class="handle_table" id="transfer_table">
								<table>
									<tr>
										<th>抄送至</th>
										<td>
											<div class="handle_person_name">
												<ul>
													<li class="choose_user_li" style="margin: 17px 8px;">
														<i class="layui-icon choose_transfer_person" onclick="getUser(this,true,'transfer_table')">&#xe654;</i>
													</li>
												</ul>
											</div>
										</td>
									</tr>
								</table>
							</div>
							<!-- end 抄送 -->
						</div>
						<div class="table_container layui-form" <c:if test="${showResponsibility=='FALSE'}" >style="display:none;"</c:if>>
							<p class="title_p">审批要求
								<i class="layui-icon arrow" style="float:right;" onclick="showDiv(this)">&#xe61a;</i>
							</p>
							<div class="layui-form approve_demand">
				                ${activityConf.actcResponsibility }
				            </div>
						</div>
						<div class="table_container layui-form" id="suggestion" <c:if test="${needApprovalOpinion == false}">style="display:none;"</c:if>>
							<p class="title_p">审批意见
								<i class="layui-icon arrow" style="float:right;" onclick="showDiv(this)">&#xe61a;</i>
							</p>
							<div>
								<textarea placeholder="请输入审批意见" class="layui-textarea" id="myApprovalOpinion"></textarea>
								<label class="layui-form-label" id="fu_label">常用语</label>
								<div class="layui-input-block" id="frequently_used">
									<select class="layui-form" lay-ignore lay-filter="useselfChange" id="frequently_used_select">
										<option value="-1">请选择</option>
										<option value="同意">同意</option>
										<option value="不同意">不同意</option>
									</select>
								</div>
							</div>
						</div>
						<!-- 审批记录 -->
						<div class="table_container">
							<p class="title_p">审批记录
								<i class="layui-icon arrow" style="float:right;" onclick="showDiv(this)">&#xe61a;</i>
							</p>
							<ul class="layui-timeline" id="approve_record">
							</ul>
						</div>
					</div>
					<div class="approval_btn_div">
						<input type="button" class="layui-btn filter_btn" id="save_drafts_btn" value="保存草稿" onclick="saveDraftsInfo();"/>
						<input type="button" class="layui-btn filter_btn" id="save_submit_btn" value="提交审批" onclick="submitTaskByHandleType()"/>
					</div>
				</div>
				<!-- end 审批意见 -->
				<!-- 附件 -->
				<div class="layui-tab-item" id="loadFile_div">
					<p>附件</p>
				</div>
				<!-- 流转信息 -->
				<div class="layui-tab-item" id="record_div">
					<div class="table_container" style="margin-top:0px;">
						<p class="title_p">当前环节
							<i class="layui-icon arrow" style="float:right;" onclick="showDiv(this)">&#xe61a;</i>
						</p>
						<div class="curr_activity">
						</div>
					</div>
					<div class="table_container">
						<p class="title_p">流转过程
							<i class="layui-icon arrow" style="float:right;" onclick="showDiv(this)">&#xe61a;</i>
						</p>
						<ul class="layui-timeline" id="transferProcess">
						</ul>
					</div>
				</div>
			</div>
		</div>
        
        <div id="choose_div">
        	<div class="choose_head" id="choose_user_search_div">
        		<div class="search_div" >
        			<input type="text" id="search_user_input" class="layui-input" placeholder="工号/姓名--搜索" style="height:38px;"/>
        		</div>
        		<div class="sure_div">
        			<input type="button" class="layui-btn" onclick="searchChooseUser();" value="搜索"/>
        		</div>
        	</div>
        	<div class="choose_head" id="choose_value_search_div">
        		<div class="search_div" >
        			<input type="text" id="search_value_input" class="layui-input" placeholder="数据字典名/说明--搜索" style="height:38px;"/>
        		</div>
        		<div class="sure_div">
        			<input type="button" class="layui-btn" onclick="searchDicData();" value="搜索"/>
        		</div>
        	</div>
        	<div class="choose_head" id="choose_depart_search_div">
        		<div class="search_div" >
        			<input type="text" id="search_depart_input" class="layui-input" placeholder="部门编号/部门名称--搜索" style="height:38px;"/>
        		</div>
        		<div class="sure_div">
        			<input type="button" class="layui-btn" onclick="searchChooseDepart();" value="搜索"/>
        		</div>
        	</div>
        	<div class="choose_body layui-form" id="choose_user_table">
        		<table>
        			<thead>
        				<tr>
        					<th class="choose_second_th">员工号</th>
        					<th class="choose_three_th">员工姓名</th>
        				</tr>
        			</thead>
        			<tbody id="choose_user_tbody">
        				
        			</tbody>
        		</table>
        	</div>
        	<div class="choose_body layui-form" id="choose_value_table">
        		<table>
        			<thead>
        				<tr>
        					<th class="choose_second_th">字典数据名</th>
        					<th class="choose_three_th">字典数据说明</th>
        				</tr>
        			</thead>
        			<tbody id="choose_value_tbody">
        				
        			</tbody>
        		</table>
        	</div>
        	<div class="choose_body layui-form" id="choose_depart_table">
        		<table>
        			<thead>
        				<tr>
        					<th class="choose_second_th">部门编码</th>
        					<th class="choose_three_th">部门名称</th>
        				</tr>
        			</thead>
        			<tbody id="choose_depart_tbody">
        				
        			</tbody>
        		</table>
        	</div>
        </div>
		<ul class="layui-fixbar">
			<li class="layui-icon layui-fixbar-top" lay-type="top" style="display: list-item;">
				&#xe604;
			</li>
		</ul>
	</div>
	<div class="mobile_container layui-form" id="table_tr_container">
        <div class="mobile_top">
        	<div>
				<div class="top_left">
					<i class="layui-icon" onclick="backApproval()">&#xe65c;</i>
				</div>
				<div class="top_content">
					<span>${processInstance.proName}</span>
				</div>
			</div>
            <div class="top_right">
            </div>
        </div>
        <div id="tr_con_content" class="mobile_middle" style="margin-top:40px">
        	<div class="table_container" style="margin-top:0px">
				<p class="title_p"><label>数据表格</label><i class="layui-icon arrow" style="float:right;" onclick="showDiv(this)">&#xe61a;</i></p>
				<table id="tr_table" class="layui-table data-table">
					<tr>
					    <td data-label="1">
					        <input type="tel" class="layui-input">
					    </td>
					    <td data-label="2">
					        <input type="text" class="layui-input">
					    </td>
					    <td data-label="3">
					        <input type="text" class="layui-input">
					    </td>
					    <td data-label="4">
					        <input type="tel" class="layui-input">
					    </td>
					</tr>
				</table>
			</div>
		</div>
    </div>
</body>
<script type="text/javascript">
	var didTriggerBeforeFormError = '${didTriggerBeforeFormError}';
	$(function(){
		//$(".mobile_top").css("display","none");
		var t1 = window.setInterval(function(){
			if($("#approval .mobile_top").is(":hidden")){
				$("#approval .mobile_btn").css("top","0px");
				$("#approval .mobile_middle").css("margin-top","40px");
				window.clearInterval(t1);
			}
		},100); 
		if (didTriggerBeforeFormError == 'true') {
			layer.alert("获得数据失败，不能进行操作");
		}
		$(".data-table").find("input[type='tel']").desNumber();
		
		/*var dateInput = $("#formSet").find(".date");
		dateInput.attr("type", "text");
		dateInput.prop("readonly", true);
		dateInput.each(function () {
			$(this).next().remove();
			var id = $(this).prop("id");
			var isDatetime = $(this).attr("date_type");
			var dateType = "date";
			
			var calendar = new lCalendar();
			if(isDatetime=="true"){
				dateType = "datetime";
			}
			var isChange = $(this).attr("onchange")==null || $(this).attr("onchange")=="" ? "false" : "true";
			calendar.init({
				'trigger': '#'+id,
				'type': dateType,
				'isChange':isChange
			});
		}); */
		var formSelects = layui.formSelects;
		$("#formSet").find("select").each(function(){
			var id = $(this).prop("id");
			if($(this).attr("is-multi")=="true"){
				$(this).attr({"xm-select":id,"xm-select-skin":"danger"});
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
