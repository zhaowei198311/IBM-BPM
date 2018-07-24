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
    <title>加签任务</title>
    <link href="resources/desmartportal/formDesign/css/layoutit.css" rel="stylesheet">
    <link href="resources/desmartportal/css/modules/laydate/default/laydate.css" rel="stylesheet">
    <link href="resources/desmartportal/css/layui.css" rel="stylesheet" />
    <link href="resources/desmartportal/css/my.css" rel="stylesheet" />
    <script type="text/javascript" src="resources/desmartportal/js/jquery-3.3.1.js"></script>
	<script type="text/javascript" src="resources/desmartportal/js/layui.all.js"></script>
	<script type="text/javascript" src="resources/desmartportal/js/common.js"></script>
	<!-- 附件上传js -->
	<script src="resources/desmartportal/js/my/myFileUpload.js"></script>
	<!-- 审批信息js -->
	<script src="resources/desmartportal/js/my/myApprovalOpinion.js"></script>
	
	<script type="text/javascript" src="resources/desmartportal/formDesign/js/my.js"></script>
	<script type="text/javascript" src="resources/desmartportal/js/my/finished_detail.js"></script>
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
        <input id="actcCanEditAttach" value="${activityConf.actcCanEditAttach}" style="display: none;">
        <input id="actcCanUploadAttach" value="${activityConf.actcCanUploadAttach}" style="display: none;">
        <input id="actcCanDeleteAttach" value="${activityConf.actcCanDeleteAttach}" style="display: none;">
        <span id="insData" style="display: none;">${processInstance.insData}</span>
        <span id="listStr" style="display: none;">${listStr}</span>
        <span style="padding-left: 10px; color: #777; font-size: 18px;display: none;">门店生命周期流程</span>
        <div class="layui-row">
            <div class="layui-col-md6">
                <img src="resources/desmartportal/images/icon.png" class="icon"/>
                <span class="table_title">${bpmForm.dynTitle}</span>
            </div>
            <div class="layui-col-md6">
            	<span id="test" style="float: right; padding-right: 40px;">
		        	<button class="layui-btn layui-btn-sm layui-btn-normal" onclick="approvalCompletion()">提交</button>
		            <button class="layui-btn layui-btn-sm layui-btn-normal" onclick="processView(${processInstance.insId})">流程图</button>
		            <button class="layui-btn layui-btn-sm layui-btn-normal" onclick="back()">退出</button>
		        </span>
            </div>
        </div>
        <div class="layui-row" style="margin: 0px 0 0 40px; padding-right: 40px;">
            <div class="layui-col-md4">姓名：${processInstance.initUserFullname}(${processInstance.insInitUser})</div>
            <div class="layui-col-md4" style="position: relative;">
                <div class="layui-progress layui-progress-big" lay-filter="progressBar" style="position: relative;top: 5px;">
                    <div class="layui-progress-bar" lay-percent="0%"></div>
                    <!--<span class="progress_time">审批剩余时间6小时</span> -->
                </div>
            </div>
        </div>
        <div class="layui-row" style="margin-left: 40px; padding-right: 40px;">
            <div class="layui-col-md4">部门：${processInstance.departName} - ${processInstance.companyName}</div>
            <div class="layui-col-md4">填写时间：
                <fmt:formatDate value="${processInstance.insInitDate}" type="date" pattern="yyyy-MM-dd" />
            </div>
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
								<input type="text" id="insTitle_input" class="layui-input"
											value="${processInstance.insTitle }" disabled />
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			
			<div id="formSet">${bpmForm.dynWebpage }</div>
			
			<div class="table_container">
				<p class="title_p" style="margin-top: 10px;<c:if test="${showResponsibility=='FALSE'}" >display:none;</c:if>">本环节审批要求</p>
	            <div class="layui-form" <c:if test="${showResponsibility=='FALSE'}" >style="display:none;"</c:if>>
	                ${activityConf.actcResponsibility }
	            </div>
			</div>
			
			<div class="table_container">
				<p class="title_p" <c:if test="${activityConf.actcCanApprove =='FALSE'}">style="dislay:none;"</c:if>>审批意见</p>
	            <div style="padding:10px 20px 20px;">
		            <div class="layui-form" <c:if test="${activityConf.actcCanApprove =='FALSE'}">style="dislay:none;"</c:if>>
		                <textarea placeholder="意见留言" class="layui-textarea" id="myApprovalOpinion" style="margin-bottom: 10px;"></textarea>
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
	            </div>
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
	                                        <th>
	                                            <!-- <input id="all-file-check" type="checkbox"> -->序号
	                                        </th>
	                                        <th>附件名称</th>
	                                        <th>上传人</th>
	                                        <th>上传时间</th>
	                                        <th>
	                                            <button onclick="batchDown()" class="layui-btn layui-btn-primary layui-btn-sm " id="batch-down-file" style="margin-left:20px;">下载全部</button>
	                                            <div class="hidden-value">
	                                                <input class="maxFileSize" value="20" type="hidden" />
	                                                <input class="maxFileCount" value="10" type="hidden" />
	                                                <input class="fileFormat" value="jpg,png,xls,xlsx,doc,docx,txt,pdf,ppt,pptx" type="hidden" />
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
		</div>
	</div>
</body>

</html>
<script type="text/javascript">
	// 初始化加载进度条
	$(function(){
		var taskUid = $('#taskUid').val();
		$.ajax({
			async: false,
			url: common.getPath() + '/taskInstance/queryProgressBar',
			type: 'post',
			dataType: 'json',
			data: {
				activityId: '',
				taskUid: taskUid
			},
			success: function(data){
	            var result = data.data;
	            // 剩余时间
	            var hour = result.hour;
	            // 剩余时间百分比
	            var percent = result.percent;
	            if (data.status == 0) {
	                if (hour == -1) {
	                    $(".layui-progress").append('<span class="progress_time">审批已超时</span>');
	                    $(".progress_time").css('right', '2%');
	                } else {
	                    $(".layui-progress").append('<span class="progress_time">审批剩余时间' + hour + '小时</span>');
	                    $(".progress_time").css('right', '2%');          
	                }
	                // 加载进度条
	                layui.use('element', function () {
	                    var $ = layui.jquery,
	                        element = layui.element; //Tab的切换功能，切换事件监听等，需要依赖element模块
	                    // 延迟加载
	                    setTimeout(function () {
	                    	if (percent > 50) {
	                            $('.layui-progress-bar').css('background-color', '#FFFF33');
	                        }
	                        if (percent > 80) {
	                            $('.layui-progress-bar').css('background-color', 'red');
	                        }
	                        element.progress('progressBar', percent + '%');
	                    }, 500);
	                });
	            } else {
	                $(".layui-progress").append('<span class="progress_time">加载失败!</span>');
	            }
			}
		})
	});
	// 提交
	function approvalCompletion(){
		var taskUid = $('#taskUid').val();
		var activityId = $('#activityId').val();
		// 审批意见
		var approvalContent = $("#myApprovalOpinion").val();//审批意见
	    if(approvalContent == null || approvalContent == "" || approvalContent == undefined){
	    	layer.alert("请填写审批意见！");
	    	return;
	    }
		$.ajax({
			async: false,
			url: common.getPath() + '/taskInstance/finishAdd',
			type: 'post',
			dataType: 'json',
			data: {
				taskUid: taskUid,
				activityId: activityId,
				approvalContent: approvalContent
			},
			success: function(data){
				console.log("data: "+data);
				layer.closeAll('loading');
				if (data.status == 0) {
					layer.alert("提交成功！", function(){
	                	window.history.back();
	                });
				}else {
					layer.alert(data.msg);
				}
			}
		})
	}
</script>