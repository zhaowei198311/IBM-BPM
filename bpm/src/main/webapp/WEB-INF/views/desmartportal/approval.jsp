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
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>待办任务</title>
    <link href="resources/desmartportal/formDesign/css/bootstrap-combined.min.css" rel="stylesheet">
    <link href="resources/desmartportal/formDesign/css/layoutit.css" rel="stylesheet">
    <link href="resources/desmartportal/css/modules/laydate/default/laydate.css" rel="stylesheet">
    <link href="resources/desmartportal/css/layui.css" rel="stylesheet" />
    <link href="resources/desmartportal/css/my.css" rel="stylesheet" />
    <link href="resources/desmartportal/js/css/myFileUpload.css" rel="stylesheet" />
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
		
		.choose_user{
			position: absolute;
		    right: 10px;
		    top: 8px;
		    z-index: 1;
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
		
		#Mask {
   		  	 position: absolute; top: 0px; filter: alpha(opacity=60); background-color: #333;
    		 z-index: 1002; left: 0px;
    		 opacity:0.5; -moz-opacity:0.5;
		}
		#Progress{
    		position: absolute; top: 35%;left:35%;z-index: 2000;
		}
		#Progress .circle-info{
   			 color:black;
		}
 		.layui-progress-bar {
 		  display: block;
 		  min-width: 8px;
 		  height: 12px;
 		  background: #2067c5;
 		  background-image: -webkit-linear-gradient(top, rgba(255, 255, 255, 0.3), rgba(255, 255, 255, 0) 30%, rgba(0, 0, 0, 0) 65%, rgba(0, 0, 0, 0.2)), -webkit-linear-gradient(left, #2067c5, #24c1fc);
 		  background-image: -moz-linear-gradient(top, rgba(255, 255, 255, 0.3), rgba(255, 255, 255, 0) 30%, rgba(0, 0, 0, 0) 65%, rgba(0, 0, 0, 0.2)), -moz-linear-gradient(left, #2067c5, #24c1fc);
 		  background-image: -o-linear-gradient(top, rgba(255, 255, 255, 0.3), rgba(255, 255, 255, 0) 30%, rgba(0, 0, 0, 0) 65%, rgba(0, 0, 0, 0.2)), -o-linear-gradient(left, #2067c5, #24c1fc);
 		  background-image: linear-gradient(to bottom, rgba(255, 255, 255, 0.3), rgba(255, 255, 255, 0) 30%, rgba(0, 0, 0, 0) 65%, rgba(0, 0, 0, 0.2)), linear-gradient(to right, #2067c5, #24c1fc);
 		  border-radius: 4px;
 		  -webkit-box-shadow: inset 0 1px rgba(0, 0, 0, 0.2), inset 0 0 0 1px rgba(0, 0, 0, 0.2);
 		  box-shadow: inset 0 1px rgba(0, 0, 0, 0.2), inset 0 0 0 1px rgba(0, 0, 0, 0.2);
 		}
    </style>
</head>

<body>

	<!---Mask是遮罩，Progress是进度条-->
	<div>
      <div id="Mask"></div>
      <div id="Progress" data-dimension="250" data-text="0%" data-info="导出进度" data-width="30" data-fontsize="38" data-percent="0" data-fgcolor="#61a9dc" data-bgcolor="#eee"></div>
 	</div>
    <div class="search_area top_btn">
    	<span id="fieldPermissionInfo" style="display: none;">${fieldPermissionInfo}</span>
    	<input type="hidden" id="departNo" value="${processInstance.departNo}"/> 
    	<input type="hidden" id="companyNum" value="${processInstance.companyNumber}"/> 
       	<input id="activityId" value="${activityMeta.activityId}" style="display: none;">
        <input id="formId" value="${formId}" style="display: none;">
        <input id="proUid" value="${processInstance.proUid}" style="display: none;">
        <input id="proAppId" value="${processInstance.proAppId}" style="display: none;">
        <input id="proVerUid" value="${processInstance.proVerUid}" style="display: none;">
        <input id="insUid" value="${processInstance.insUid}" style="display: none;">
        <input id="insId" value="${processInstance.insId}" style="display: none;">
        <input id="taskId" value="${taskInstance.taskId}" style="display: none;">
        <input id="taskUid" value="${taskInstance.taskUid}" style="display: none;">
        <input id="actcCanReject" value="${activityConf.actcCanReject}" style="display: none;">
        <input id="actcCanTransfer" value="${activityConf.actcCanTransfer}" style="display: none;">
        <input id="actcCanAdd" value="${activityConf.actcCanAdd}" style="display: none;">
        <span id="formData" style="display: none;">${formData}</span>
        <span id="listStr" style="display: none;">${listStr}</span>
        <span style="padding-left: 10px; color: #777; font-size: 18px;">门店生命周期流程</span>
        <span id="test" style="float: right; padding-right: 20px;">
            <button class="layui-btn layui-btn-sm" onclick="processView(${processInstance.insId})">流程图</button>
            <%-- <button class="layui-btn layui-btn-sm" >保存</button> --%>
            <button class="layui-btn layui-btn-sm" onclick="checkUserData()">提交</button>
            <button class="layui-btn layui-btn-sm" id="reject" onclick="queryRejectByActivitiy()" <c:if test="${activityConf.actcCanReject =='FALSE'}" >style="display:none;"</c:if>>驳回</button>
            <button class="layui-btn layui-btn-sm" id="transfer" <c:if test="${activityConf.actcCanTransfer =='FALSE'}" >style="display:none;"</c:if>>传阅</button>
            <button class="layui-btn layui-btn-sm" id="add" <c:if test="${activityConf.actcCanAdd =='FALSE'}" >style="display:none;"</c:if>>会签</button>
            <button class="layui-btn layui-btn-sm" onclick="back()">退出</button>
        </span>
    </div>
    <div class="container" style="width: 96%">
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
                        <th colspan="4" class="list_title">${bpmForm.dynTitle}
                            <span style="float: right; font-size: 14px; font-weight: normal;">流程编号：1000-10185-BG-60</span>
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
                        <td class="td_title">工号</td>
                        <td>
                            <input type="text"  required lay-verify="required" value="${processInstance.insInitUser}" autocomplete="off" class="layui-input" readonly/>
                        </td>
                        <td class="td_title">姓名</td>
                        <td>
                            <input type="text"  required lay-verify="required" value="${processInstance.initUserFullname}" autocomplete="off" class="layui-input" readonly/>
                        </td>
                    </tr>
                    <tr>
                        <td class="td_title">创建日期</td>
                        <td>
                            <input type="text"  required lay-verify="required" value="<fmt:formatDate value="${processInstance.insInitDate}" type="date" pattern="yyyy-MM-dd"/>" autocomplete="off" class="layui-input" readonly/>
                        </td>
                        <td class="td_title">所属部门</td>
                        <td>
                            <input type="text"  required lay-verify="required" value="${processInstance.departName}" autocomplete="off" class="layui-input" readonly/>
                       	</td>
                    </tr>
                </tbody>
            </table>
            
            <p class="title_p">流程标题</p>
            <table class="layui-table">
                <tbody>
                    <tr>
                        <td class="td_title" colspan="1" style="width: 120px">流程标题</td>
                        <td class="sub_title" colspan="5">
                             <input type="text" id="insTitle_input" class="layui-input" value="${processInstance.insTitle }" readonly>
                        </td>
                    </tr>
                </tbody>
            </table>
            
            <div id="formSet">${formHtml }</div>
            <div class="display_container2">
				<div class="display_content2">
					<div class="top">
						选择下一环节
					</div>
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
						<button class="layui-btn sure_btn" onclick="agree();">确定</button>
						<button class="layui-btn layui-btn-primary cancel_btn" onclick="$('.display_container2').css('display','none')">取消</button>
					</div>				
				</div>
			</div>
            <div class="option_container">
                <!-- 环节职责区块  -->
                <p class="title_p" style="margin-top: 10px;<c:if test="${showResponsibility=='FALSE'}" >display:none;</c:if>">本环节审批要求</p>
                <div class="layui-form" <c:if test="${showResponsibility=='FALSE'}" >style="display:none;"</c:if>>
                    ${activityConf.actcResponsibility }
                </div>
                <!-- 审批意见区块  -->
                <p class="title_p" <c:if test="${activityConf.actcCanApprove =='FALSE'}">style="dislay:none;"</c:if>>审批意见</p>
                <div class="layui-form" <c:if test="${activityConf.actcCanApprove =='FALSE'}">style="dislay:none;"</c:if>>
                    <!--<label class="layui-form-label">审批意见</label>
				      	<div class="layui-input-block">-->
                    <textarea placeholder="意见留言" class="layui-textarea" id="myApprovalOpinion" style="margin-bottom: 10px;"></textarea>
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
                        <!-- 审批记录区域 -->
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
                        <!-- 附件区域 -->
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
                                            <button class="layui-btn layui-btn-primary layui-btn-sm upload" id="upload-file" style="margin-left: 20px;<c:if test="${activityConf.actcCanUploadAttach =='FALSE'}" >display:none;</c:if>">上传附件</button>
                                            <button onclick="batchDown()" class="layui-btn layui-btn-primary layui-btn-sm " id="batch-down-file" style="margin-left:20px;">下载全部</button>
                                            <div class="hidden-value">
                                                <input class="maxFileSize" value="20" type="hidden" />
                                                <input class="maxFileCount" value="10" type="hidden" />
                                                <input class="fileFormat" value="jpg,png,xls,xlsx,doc,docx,txt,pdf,ppt,pptx" type="hidden" />
                                            </div>
                                            <!-- <input type="file" class="upload_file"/> -->
                                            <!-- <input style="margin-left:20px;" class="layui-btn layui-btn-primary btn btn-primary file" value="上传附件" id="button-EafH" type="button" /> -->
                                        </th>
                                    </tr>
                                </thead>
                                <tbody>
                                </tbody>
                            </table>
                        </div>
                        <!-- 流转记录区域 -->
                        <div class="layui-tab-item">
                            <div class="p">
                                <p>
                                    <font>现在的环节号：</font>
                                    <span></span>
                                </p>
                                <p>
                                    <font>当前处理人：</font>
                                    <span></span>
                                </p>
                                <p>
                                    <font>当前处理状态：</font>
                                    <span></span>
                                </p>
                                <p>
                                    <font>当前处理到达时间：</font>
                                    <span></span>
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
            <button class="layui-btn layui-btn layui-btn-primary cancel_btn" onclick="cancelClick(this)">取消</button>
        </div>
    </div>
    <div id="showHistoryModal" style="display: none;" class="display_content_accessory_file">
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
	<div class="display_container7" style="display: none;">
		<div class="display_content7" style="height: 160px">
			    <label class="layui-form-label" style="width: 30px;">人员</label>
                <div class="layui-input-block" style="position:relative;margin-left: 60px;">
                    <input type="hidden" id="handleUser" name="handleUser" />
                    <input type="text" name="handleUser_view"  id="handleUser_view"  autocomplete="off" class="layui-input" disabled="disabled">
                    <i class="layui-icon choose_user" id="choose_handle_user" title="选择人员">&#xe612;</i>  
                </div>
                <div style="margin-top: 15px;">
                    <label class="layui-form-label" style="width: 60px;">会签方式</label>
                    <div class="layui-input-block" style="margin-left: 80px;">
                        <select class="layui-form_1" lay-filter="useselfChange" style="width: 20%;height: 30px">
                            <option value="normalAdd">随机会签</option>
                            <option value="simpleLoopAdd">顺序会签</option>
                            <option value="multiInstanceLoopAdd">并行会签</option>
                        </select>
                    </div>
                </div>
			    <div class="foot" style="margin-top: 20px;">
		            <button class="layui-btn layui-btn sure_btn" onclick="addSure()">确定</button>
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
</body>

</html>
<script type="text/javascript" src="resources/desmartportal/js/jquery-3.3.1.js"></script>
<script type="text/javascript" src="resources/desmartportal/js/layui.all.js"></script>
<script type="text/javascript" src="resources/desmartportal/js/common.js"></script>
<!-- 附件上传js -->
<script src="resources/desmartportal/js/my/myFileUpload.js"></script>
<!-- 审批信息js -->
<script src="resources/desmartportal/js/my/myApprovalOpinion.js"></script>

<script type="text/javascript" src="resources/desmartportal/formDesign/js/my.js"></script>
<script type="text/javascript" src="resources/desmartportal/js/my/approval.js"></script>