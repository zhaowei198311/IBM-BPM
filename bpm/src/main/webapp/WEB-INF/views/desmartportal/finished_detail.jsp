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
    <title>已办任务</title>
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
			
		.layui-radio-disbaled{
			display:none;
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
      <div id="Progress" data-dimension="180" data-text="0%" data-info="下载进度" data-width="30" data-fontsize="38" data-percent="0" data-fgcolor="#009688" data-bgcolor="#eee"></div>
 	</div>
    <div class="search_area top_btn">
    	<span id="fieldPermissionInfo" style="display: none;">${fieldPermissionInfo}</span>
    	<input type="hidden" id="departNo" value="${processInstance.departNo}"/> 
    	<input type="hidden" id="companyNum" value="${processInstance.companyNumber}"/> 
       	<input id="activityId" value="${activityMeta.activityId}" style="display: none;">
        <input id="formId" value="${bpmForm.dynUid}" style="display: none;">
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
        <input id="actcCanEditAttach" value="${activityConf.actcCanEditAttach}" style="display: none;">
        <input id="actcCanUploadAttach" value="${activityConf.actcCanUploadAttach}" style="display: none;">
        <input id="actcCanDeleteAttach" value="${activityConf.actcCanDeleteAttach}" style="display: none;">
        <input id="canBeRevoke" type="hidden" value="${canBeRevoke}"/>
        <span id="insData" style="display: none;">${processInstance.insData}</span>
        <span id="listStr" style="display: none;">${listStr}</span>
        <span style="padding-left: 10px; color: #777; font-size: 18px;display: none;">门店生命周期流程</span>
        <div class="layui-row">
            <div class="layui-col-md6">
                <img src="resources/desmartportal/images/icon.png" class="icon"/>
                <span class="table_title">${bpmForm.dynTitle}</span>
            </div>
            <div class="layui-col-md6">
            	<span style="float: right; padding-right: 40px;">
		        	<button class="layui-btn layui-btn-sm layui-btn-normal chart" onclick="processView(${processInstance.insId})">流程图</button>
		            <c:if test="${canBeRevoke == 'TRUE'}" >
		            <button class="layui-btn layui-btn-normal layui-btn-sm" onclick="revokeTask('${taskInstance.taskUid}');">取回</button>
		            </c:if>
		            <c:if test="${taskInstance.taskStatus=='32'}">
		            	<button class="layui-btn layui-btn-sm layui-btn-normal" onclick="prePrintIndex()">打印预览</button>
		            </c:if>
		            <button class="layui-btn layui-btn-normal layui-btn-sm back_btn" onclick="back()">退出</button>
				</span>
			</div>
		</div>
		<div class="layui-row" style="margin: 0px 0 0 40px; padding-right: 40px;">
            <div class="layui-col-md4">姓名：${processInstance.initUserFullname}(${processInstance.insInitUser})</div>
        	<div class="layui-col-md4">填写时间：
                <fmt:formatDate value="${processInstance.insInitDate}" type="date" pattern="yyyy-MM-dd" />
            </div>
        </div>
        <div class="layui-row" style="margin-left: 40px; padding-right: 40px;">
            <div class="layui-col-md4">部门：${processInstance.departName} - ${processInstance.companyName}</div>
            <div class="layui-col-md4">&nbsp;</div>
            <div class="layui-col-md4" style="text-align: right;">
               	 表单编号：
                <span style="color: #1890ff;">1000-10185-BG-60</span>
            </div>
        </div>
    </div>
     <div class="layui-fluid" style="padding-top: 120px;">
		<div class="table_content">
			<div class="table_container">
				<p class="title_p">流程标题</p>
				<table class="layui-table basic_information" lay-skin="nob">
					<tbody>
						<tr>
							<td class="td_title" colspan="1" style="width: 120px">流程标题</td>
							<td class="sub_title" colspan="5">
								<input type="text" id="insTitle_input" class="layui-input" value="${processInstance.insTitle }" disabled />
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			
			<div id="formSet">${bpmForm.dynWebpage }</div>
			<div id="preViewForm"></div>
			
			<div class="table_container">
				<p class="title_p">审批信息</p>
				<div class="option_container">
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
	                                    <col width="8%">
	                                    <col width="8%">
	                                    <col width="12%">
	                                    <col width="8%">
	                                    <col width="20%">
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
	                                        <th>
	                                            <input style = 'cursor: default;' id="all-file-check" type="checkbox"> <!-- 序号 -->
	                                        </th>
	                                        <th>附件名称</th>
	                                        <!-- 
										      <th>附件说明</th>
										      <th>附件类型</th> -->
	                                        <th>上传人</th>
	                                        <th>上传时间</th>
	                                        <th>
	                                        <c:if test="${taskInstance.taskStatus!=32 && taskInstance.taskStatus!=-2}">
	                                            <button class="layui-btn layui-btn-primary layui-btn-sm upload" id="upload-file" style="margin-left: 20px;">上传附件</button>
	                                            </c:if>
	                                            <button onclick="batchDown()" class="layui-btn layui-btn-primary layui-btn-sm " id="batch-down-file" style="margin-left:20px;">批量下载</button>
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
	                                    <font>当前处理环节：</font>
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
            <button class="layui-btn layui-btn layui-btn-primary cancel_btn" onclick="cancelClick(this)">关闭</button>
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
		<div class="display_content7" style="height: 125px">
			    <label class="layui-form-label" style="width: 30px;">人员</label>
                <div class="layui-input-block" style="position:relative;">
                    <input type="hidden" id="handleUser" name="handleUser" />
                    <input type="text" name="handleUser_view"  id="handleUser_view"  autocomplete="off" class="layui-input" disabled="disabled">
                    <i class="layui-icon choose_user" id="choose_handle_user" title="选择人员">&#xe612;</i>  
                </div>
			    <div class="foot" style="margin-top: 20px;">
		            <button class="layui-btn layui-btn sure_btn" onclick="addSure()">确定</button>
		            <button class="layui-btn layui-btn cancel_btn">取消</button>
		        </div>
	    </div>
	</div>
	<div class="display_container8" style="display: none;">
		<div class="display_content7" style="height: 125px;width: 400px">
			<div class="layui-form-item">
			<label class="layui-form-label" style="width: 80px;">驳回的环节:</label>
				<div class="layui-input-inline">
					<form action="" class="layui-form">
					<select name="quiz1">
						<option value="">请选择驳回环节</option>
					</select>
					</form>
				</div>
			</div>
			    <div class="foot" style="margin-top: 20px;">
		            <button class="layui-btn layui-btn sure_btn" onclick="addSure()">确定</button>
		            <button class="layui-btn layui-btn cancel5_btn">取消</button>
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
<script type="text/javascript" src="resources/desmartportal/js/my/finished_detail.js"></script>