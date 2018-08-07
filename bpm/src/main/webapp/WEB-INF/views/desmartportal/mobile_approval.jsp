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
    <script type="text/javascript" src="resources/desmartportal/js/my/mobile_approval.js"></script>
</head>
<body style="background-color: #f2f2f2;margin-bottom:80px;">
    <div class="mobile_container">
        <div class="mobile_top">
            <div class="top_left">
            	<i class="layui-icon" onclick="back()">&#xe65c;</i>
            </div>
            <div class="top_content">
            	<span>${processInstance.proName}</span>
            </div>
            <div class="top_right">
            	<span id="fieldPermissionInfo" style="display: none;">${fieldPermissionInfo}</span>
		    	<input id="departNo" type="hidden" value="${processInstance.departNo}"/>
		    	<input id="companyNum" type="hidden" value="${processInstance.companyNumber}"/>
		       	<input id="activityId" value="${activityMeta.activityId}" style="display: none;">
		        <input id="proUid" value="${processInstance.proUid}" style="display: none;">
		        <input id="proAppId" value="${processInstance.proAppId}" style="display: none;">
		        <input id="proVerUid" value="${processInstance.proVerUid}" style="display: none;">
		        <input id="insUid" value="${processInstance.insUid}" style="display: none;">
		        <input id="insId" value="${processInstance.insId}" style="display: none;">
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
            	<i class="layui-icon" id="operate_menu" title="菜单">&#xe671;</i>
            	<dl id="child_menu">
            		<dd><a href="javascript:void(0);" onclick="submitTaskByHandleType();">提交</a></dd>
	            	<dd><a href="javascript:void(0);" onclick="saveDraftsInfo();">保存草稿</a></dd>
            	</dl>
            </div>
        </div>
        <div class="mobile_middle">
			<div class="middle_content" id="form_content_div">
				<table class="layui-table">
	                <tbody>
	                    <tr>
	                        <th colspan="4" class="list_title" id="formTitle">
	                        	<span>${bpmForm.dynTitle}</span>
	                        </th>
	                    </tr>
	                    <tr>
	                    	<th colspan="4" class="list_title" id="formNum">
	                        	<span style="float: right; font-size: 10px; font-weight: normal;">流程编号：1000-10185-BG-60</span>
	                        </th>
	                    </tr>
	                    <tr>
	                        <th colspan="4">
	                            <div class="layui-progress layui-progress-big" lay-filter="progressBar" lay-showPercent="yes" style="position: relative;">
	                                <div class="layui-progress-bar" lay-percent="0%"></div>
	                                <!--<span class="progress_time">审批剩余时间6小时</span> -->
	                            </div>
	                        </th>
	                    </tr>
	                    <tr>
	                        <td class="td_title" style="width: 70px">工号</td>
	                        <td>
	                            <input type="text"  required lay-verify="required" value="${processInstance.insInitUser}" autocomplete="off" class="layui-input" disabled/>
	                        </td>
	                    </tr>
	                    <tr>
	                    	<td class="td_title" style="width: 70px">姓名</td>
	                        <td>
	                            <input type="text"  required lay-verify="required" value="${processInstance.initUserFullname}" autocomplete="off" class="layui-input" disabled/>
	                        </td>
	                    </tr>
	                    <tr>
	                        <td class="td_title" style="width: 70px">创建日期</td>
	                        <td>
	                            <input type="text"  required lay-verify="required" value="<fmt:formatDate value="${processInstance.insInitDate}" type="date" pattern="yyyy-MM-dd"/>" autocomplete="off" class="layui-input" disabled/>
	                        </td>
	                    </tr>
	                    <tr>
	                    	<td class="td_title" style="width: 70px">所属部门</td>
	                        <td>
	                            <input type="text"  required lay-verify="required" value="${processInstance.departName} - ${processInstance.companyName}" autocomplete="off" class="layui-input" disabled/>
	                       	</td>
	                    </tr>
	                </tbody>
	            </table>
	            
	            <p class="title_p">流程标题</p>
	            <table class="layui-table">
	                <tbody>
	                    <tr>
	                        <td class="td_title" colspan="1" style="width: 70px">流程标题</td>
	                        <td class="sub_title" colspan="3">
	                            <c:choose>
	                                <%-- 可编辑标题 --%>
	                                <c:when test="${canEditInsTitle == true}">
	                                    <input type="text" id="insTitle_input" class="layui-input" />
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
				<div id="formSet" style="padding-bottom:10px;">
					${bpmForm.dynWebpage}
				</div>
			</div>
			<div class="middle_content" id="approve_div">
				<div class="layui-form">
					<h1 style="clear: both;"></h1>
					<div class="handle_div layui-tab layui-tab-brief">
						<ul class="layui-tab-title">
						    <li onclick="handleBtnClick(this)" id="submit_btn" class="layui-this">同意</li>
						    <li onclick="handleBtnClick(this)" id="countersign_btn" <c:if test="${activityConf.actcCanAdd =='FALSE'}" >style="display:none;"</c:if>>会签</li>
						    <li onclick="handleBtnClick(this)" id="reject_btn" <c:if test="${activityConf.actcCanReject =='FALSE'}" >style="display:none;"</c:if>>驳回</li>
						    <li onclick="handleBtnClick(this)" id="transfer_btn" <c:if test="${activityConf.actcCanTransfer =='FALSE'}" >style="display:none;"</c:if>>传阅</li>
						</ul>
						<div class="layui-tab-content">
						    <div class="layui-tab-item handle_table layui-show" id="submit_table">
						    	<table>
							
								</table>
						    </div>
						    <div class="layui-tab-item handle_table" id="countersign_table">
						    	<table>
									<tr>
										<th>处理人：</th>
										<td>
											<div class="choose_user_name_ul">
												<ul></ul>
											</div>
											<i class="layui-icon choose_countersign_person" onclick="getUser(this,true,'countersign_table')">&#xe770;</i> 
											<input id="countersign_person" type="hidden"/>
										</td>
									</tr>
									<tr>
										<th>会签方式：</th>
										<td>
											<div id="countersign_type">
												<select class="layui-form_1 layui-select" lay-filter="useselfChange">
						                            <option value="normalAdd">随机会签</option>
						                            <option value="simpleLoopAdd">顺序会签</option>
						                            <option value="multiInstanceLoopAdd">并行会签</option>
						                        </select>
					                       	</div>
										</td>
									</tr>
								</table>
						    </div>
						    <div class="layui-tab-item handle_table" id="reject_table">
						    	<table class="layui-form">
								
								</table>
						    </div>
						    <div class="layui-tab-item handle_table" id="transfer_table">
						    	<table>
									<tr>
										<th>抄送至：</th>
										<td>
											<div class="choose_user_name_ul">
												<ul></ul>
											</div>
											<i class="layui-icon choose_transfer_person" onclick="getUser(this,true,'transfer_table')">&#xe770;</i> 
											<input id="transfer_person" type="hidden"/>
										</td>
									</tr>
								</table>
						    </div>
						</div>
					</div>
					<div id="suggestion">
						<p class="title_p" style="margin-top: 10px;<c:if test="${showResponsibility=='FALSE'}" >display:none;</c:if>">本环节审批要求</p>
			            <div class="layui-form approve_demand" <c:if test="${showResponsibility=='FALSE'}" >style="display:none;"</c:if>>
			                ${activityConf.actcResponsibility }
			            </div>
			            <p class="title_p" id="approve_p" <c:if test="${needApprovalOpinion == false}">style="display:none;"</c:if>>审批意见</p>
                		<div class="layui-form" id="approve_div" <c:if test="${needApprovalOpinion == false}">style="display:none;"</c:if>>
							<textarea placeholder="意见留言" class="layui-textarea" id="myApprovalOpinion"></textarea>
							<div style="padding: 10px 0px 5px 0px;">
								<label class="layui-form-label" id="fu_label">常用语</label>
								<div class="layui-input-block" id="frequently_used">
									<select class="layui-form" lay-filter="useselfChange">
										<option value="-1">--请选择--</option>
										<option value="通过">通过</option>
										<option value="驳回">驳回</option>
									</select>
								</div>
							</div>
						</div>
					</div>
				</div>
				<p class="title_p" style="margin-top: 0px;">历史审批意见</p>
				<ul id="approve_record" class="tab_ul">
					
				</ul>
			</div>
			<div class="middle_content" id="file_div">
				<ul id="loadFile_div" class="tab_ul">
					<h1 style="clear: both;"></h1>
					<h1 style="clear: both;"></h1>
				</ul>
			</div>
			<div class="middle_content" id="record_div">
				<p class="title_p" style="margin-top: 0px;">当前环节：</p>
				<div class="p">
					<p>
						<font>现在的环节号：</font> <span></span>
					</p>
					<p>
						<font>当前处理人：</font> <span></span>
					</p>
					<p>
						<font>当前处理环节：</font> <span></span>
					</p>
					<p>
						<font>当前处理到达时间：</font> <span></span>
					</p>
				</div>
				<p class="title_p">流转过程：</p>
				<ul id="transferProcess" class="tab_ul">
					<h1 style="clear: both;"></h1>
					<h1 style="clear: both;"></h1>
				</ul>
			</div>
        </div>
        <div class="mobile_bottom">
			<ul class="mobile_menu">
				<li style="color:#009688;"><i class="layui-icon menu_btn" id="form_content" title="form_content" onclick="menuBtnClick(this)">&#xe63c;</i><p>内容</p></li>
				<li><i class="layui-icon menu_btn" title="approve" id="approve" onclick="menuBtnClick(this)">&#xe642;</i><p>审批</p></li>
				<li><i class="layui-icon menu_btn" title="file" id="file" onclick="menuBtnClick(this)">&#xe67c;</i><p>附件</p></li>
				<li><i class="layui-icon menu_btn" title="record" id="record" onclick="menuBtnClick(this)">&#xe60e;</i><p>记录</p></li>
			</ul>
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
	<div id="table_tr_container">
		<div id="tr_con_top">
			<div id="tr_top_left">
				<i class="layui-icon" onclick="backApproval()">&#xe65c;</i>
			</div>
			<div id="tr_top_right">
				<span>数据详情</span>
			</div>
		</div>
		<div id="tr_con_content">
			<p class="title_p">数据表格</p>
			<table id="tr_table" class="layui-table">
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
</body>
<script type="text/javascript" src="resources/desmartportal/js/common.js"></script>
<script type="text/javascript">
	$(function(){
		$(".data-table").find("input[type='tel']").desNumber();
	});
</script>
</html>
