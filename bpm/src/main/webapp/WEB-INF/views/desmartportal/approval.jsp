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
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>待办任务</title>
    <link href="resources/desmartportal/formDesign/css/bootstrap-combined.min.css" rel="stylesheet">
    <link href="resources/desmartportal/formDesign/css/layoutit.css" rel="stylesheet">
    <link href="resources/desmartportal/css/modules/laydate/default/laydate.css" rel="stylesheet">
    <link href="resources/desmartportal/css/layui.css" rel="stylesheet" />
    <link href="resources/desmartportal/css/my.css" rel="stylesheet" />
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
            overflow-y: auto;
            overflow-x: hidden;
        }

        #formSet {
            display: none;
        }
    </style>
</head>

<body>
    <div class="search_area top_btn">
        <input id="activityId" value="${activityId}" style="display: none;">
        <input id="formId" value="${formId}" style="display: none;">
        <input id="proUid" value="${proUid}" style="display: none;">
        <input id="proAppId" value="${proAppId}" style="display: none;">
        <input id="proVerUid" value="${proVerUid}" style="display: none;">
        <input id="insUid" value="${insUid}" style="display: none;">
        <input id="insId" value="${insId}" style="display: none;">
        <input id="taskId" value="${taskId}" style="display: none;">
        <input id="taskUid" value="${taskUid}" style="display: none;">
        <span id="insData" style="display: none;">${insData}</span>
        <span id="listStr" style="display: none;">${listStr}</span>
        <span style="padding-left: 10px; color: #777; font-size: 18px;">门店生命周期流程</span>
        <span id="test" style="float: right; padding-right: 20px;">
            <button class="layui-btn layui-btn-sm" onclick="processView(${insId})">流程图</button>
            <button class="layui-btn layui-btn-sm" onclick="agree()">通过</button>
            <button class="layui-btn layui-btn-sm" onclick="reject()">驳回</button>
            <button class="layui-btn layui-btn-sm">转办</button>
            <button class="layui-btn layui-btn-sm">会签</button>
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
                        <th colspan="4" class="list_title">目标店调查表
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
                            <input type="text" name="title" required lay-verify="required" value="00003" autocomplete="off" class="layui-input">
                        </td>
                        <td class="td_title">姓名</td>
                        <td>
                            <input type="text" name="title" required lay-verify="required" value="测试用户" autocomplete="off" class="layui-input">
                        </td>
                    </tr>
                    <tr>
                        <td class="td_title">创建日期</td>
                        <td>
                            <input type="text" name="title" required lay-verify="required" value="2018-05-23" autocomplete="off" class="layui-input">
                        </td>
                        <td class="td_title">所属部门</td>
                        <td>
                            <input type="text" name="title" required lay-verify="required" value="信息部" autocomplete="off" class="layui-input">
                        </td>
                    </tr>
                </tbody>
            </table>
            <div id="formSet">${formHtml }</div>
            <div class="option_container">
                <p class="title_p" style="margin-top: 10px;">本环节审批要求</p>
                <div class="layui-form">
                    <p style="margin-bottom: 10px;">
                        <img src="resources/desmartportal/images/top_star.png" class="star_img" />单店单月租金≤1.5万元，华东区租金占比≤12%，其他地区≤15%。
                    </p>
                    <p>
                        <img src="resources/desmartportal/images/top_star.png" class="star_img" />审核门店地理位置，评估门店未来盈利趋势，对门店信息合理性负责。
                    </p>
                </div>
                <p class="title_p">审批意见</p>
                <div class="layui-form">
                    <!--<label class="layui-form-label">审批意见</label>
				      	<div class="layui-input-block">-->
                    <textarea placeholder="意见留言" class="layui-textarea" id="demo" style="display: none; margin-bottom: 10px;"></textarea>
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
                                    <td>下一环节:
                                        <span class="tip_span"></span>
                                    </td>
                                    <td>
                                        <input type="text" name="title" required lay-verify="required" value="${activityMeta.activityName}" readonly="readonly" autocomplete="off"
                                            class="layui-input">
                                    </td>
                                    <td class="td_title">处理人:
                                        <span class="tip_span"></span>
                                    </td>
                                    <td>
                                        <input type="hidden" class="getUser" id="${activityMeta.activityId}" value="${activityMeta.userUid}" />
                                        <input type="text" id="${activityMeta.activityId}_view" required lay-verify="required" value="${activityMeta.userName}" readonly="readonly"
                                            autocomplete="off" class="layui-input">
                                        <input type="hidden" id="choosable_${activityMeta.activityId}" value="${activityMeta.userUid}" />
                                    </td>
                                    <td colspan="3">
                                        <i class="layui-icon" onclick="getConductor('${activityMeta.activityId}','false','${activityMeta.dhActivityConf.actcCanChooseUser}','${activityMeta.dhActivityConf.actcAssignType}');">&#xe612;</i>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
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
                                            <button class="layui-btn layui-btn-primary layui-btn-sm upload" id="upload-file" style="margin-left: 20px;">上传附件</button>
                                            <button class="layui-btn layui-btn-primary layui-btn-sm " id="batch-down-file" style="margin-left:20px;">下载全部</button>
                                            <div class="hidden-value">
                                                <input class="maxFileSize" value="20" type="hidden" />
                                                <input class="maxFileCount" value="10" type="hidden" />
                                                <input class="fileFormat" value="jpg,png,xls,xlsx,exe" type="hidden" />
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
                            <col width="5%">
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