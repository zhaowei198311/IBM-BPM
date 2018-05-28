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
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <link href="resources/desmartportal/formDesign/css/bootstrap-combined.min.css" rel="stylesheet">
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
    <div class="search_area top_btn">
        <input type="hidden" id="departNo" />
        <input type="hidden" id="companyNum" />
        <input type="hidden" id="insUid" value="${processInstance.insUid}" />
        <input id="insTitle" value="${processInstance.insTitle}" style="display: none;">
        <input id="formId" value="${dhStep.stepObjectUid}" style="display: none;">
        <input id="proUid" value="${processDefinition.proUid}" style="display: none;">
        <input id="proAppId" value="${processDefinition.proAppId}" style="display: none;">
        <input id="verUid" value="${processDefinition.proVerUid}" style="display: none;">
        <input id="proName" value="${processDefinition.proName}" style="display: none;">
        <input id="userId" value="${currentUser.userId}" style="display: none;">
        <input id="activityId" value="${bpmActivityMeta.activityId}" style="display: none;" />
        <span id="formData" style="display: none;">${ formData }</span>
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
                    <tbody>
                        <tr>
                            <th colspan="12" class="list_title">目标店调查表
                                <span style="float: right;font-size:14px;font-weight:normal;">表单编号：${formId}</span>
                            </th>
                        </tr>
                        <tr>
                            <th colspan="12">
                                <div class="layui-progress layui-progress-big" lay-showPercent="yes" style="position:relative;">
                                    <div class="layui-progress-bar" lay-percent="50%"></div>
                                    <span class="progress_time">审批剩余时间6小时</span>
                                </div>
                            </th>
                        </tr>
                        <tr>
                            <td class="td_title" colspan="1" style="width:120px">工号</td>
                            <td class="sub_title" colspan="5">
                                <input type="text" name="userId" value="${currentUser.userId}" class="layui-input">
                            </td>
                            <td class="td_title" colspan="1" style="width:120px">姓名</td>
                            <td class="sub_title" colspan="5">
                                <input type="text" name="userName" value="${currentUser.userName}" class="layui-input">
                            </td>
                        </tr>
                        <tr>
                            <td class="td_title" style="width:120px" colspan="1">创建日期</td>
                            <td class="sub_title" colspan="5">
                                <input type="text" name="createDate" value="2018-05-23" class="layui-input">
                            </td>
                            <td class="td_title" style="width:120px" colspan="1">所属部门</td>
                            <td class="sub_title" colspan="5">
                                <select id="creatorInfo" style="border-width:1px;">
                                    <option value="">请选择部门</option>
                                    <c:forEach items="${userDepartmentList}" var="item">
                                        <option value="${item.departNo},${item.companyCode}">${item.departName}</option>
                                    </c:forEach>
                                </select>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </form>

            <div id="formSet">${formHtml }</div>
            <div class="display_container">
                <div class="choose_display_content">
                    <div class="top">
                        选择下个环节处理人
                    </div>
                    <div class="choose_middle">
                        <form class="layui-form" action="" style="margin-top:30px;">

                        </form>
                    </div>
                    <div class="foot">
                        <button class="layui-btn layui-btn sure_btn" onclick="submitProcess()">确定</button>
                        <button class="layui-btn layui-btn layui-btn-primary cancel_btn" onclick="$('.display_container').css('display','none')">取消</button>
                    </div>
                </div>
            </div>
            <div class="layui-tab">
                <ul class="layui-tab-title">
                    <li class="layui-this">附件</li>
                </ul>
                <div class="layui-tab-content" style="padding:0;">
                    <div class="layui-tab-item layui-show" style="height: auto;">
                        <table class="layui-table upload-file-table" style="margin:0;">
                            <colgroup>
                                <col width="5%">
                                <col width="20%">
                                <%-- 
									    <col width="15%">
									    <col width="20%">  --%>
                                    <col width="10%">
                                    <col width="10%">
                                    <col width="20%">
                            </colgroup>
                            <thead>
                                <tr>
                                    <th>
                                        <!-- <input id="all-file-check" type="checkbox"> -->序号</th>
                                    <th>附件名称</th>
                                    <!-- 
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
<script type="text/javascript">
    //数据信息
    var view = $(".container-fluid");
    var form = null;
    $(function () {
        layui.use(['form'], function () {
            form = layui.form;
        });
        var formData = $("#formData").text();
        if (formData != null && formData != "") {
            getdata(formData);
        }
    });

    function getdata(jsonStr) {
        var json = JSON.parse(jsonStr);
        for (var name in json) {
            var paramObj = json[name];
            //给各个组件赋值
            setValue(paramObj, name);
            //判断组件是否可见
            isDisplay(paramObj, name);
            //判断组件对象是否可编辑
            isEdit(paramObj, name);
        }
    }

    /**
     * 根据组件对象的类型给各个组件赋值
     * @param paramObj 组件对象
     * @param id 各个组件的id(单选框为class)
     */
    var setValue = function (paramObj, name) {
        var tagName = $("[name='" + name + "']").prop("tagName");
        switch (tagName) {
            case "INPUT":
                {
                    var tagType = $("[name='" + name + "']").attr("type");
                    switch (tagType) {
                        case "text":
                            ;
                        case "tel":
                            ;
                        case "date":
                            {
                                $("[name='" + name + "']").val(paramObj["value"]);
                                form.render();
                                break;
                            };
                        case "radio":
                            {
                                $("[name='" + name + "'][id='" + paramObj["value"] + "']").prop("checked", "true");
                                form.render();
                                break;
                            }
                        case "checkbox":
                            {
                                var valueArr = paramObj["value"];
                                for (var value in valueArr) {
                                    $("[name='" + name + "'][id='" + valueArr[value] + "']").prop("checked", "true");
                                }
                                form.render();
                                break;
                            }
                    }
                    break;
                };
            case "SELECT":
                ;
            case "TEXTAREA":
                {
                    $("[name='" + name + "']").val(paramObj["value"]);
                    form.render();
                    break;
                }
        }
    }


    /**
     * 判读组件对象是否可见
     * @param paramObj 组件对象
     * @param id 各个组件的id(单选框为class)
     */
    var isDisplay = function (paramObj, name) {
        var display = paramObj["display"];
        if (display == "none") {
            var tagType = $("[name='" + name + "']").attr("type");
            $("[name='" + name + "']").parent().css("display", "none");
            $("[name='" + name + "']").parent().prev().css("display", "none");
            if (tagType == "radio" || tagType == "checkbox") {
                $("[name='" + name + "']").parent().css("display", "none");
                $("[name='" + name + "']").parent().prev().css("display", "none");
            }
        }
    }

    /**
     * 判读组件对象是否可编辑
     * @param paramObj 组件对象
     * @param id 各个组件的id(单选框为class)
     */
    var isEdit = function (paramObj, name) {
        var edit = paramObj["edit"];
        if (edit == "no") {
            $("[name='" + name + "']").attr("readonly", "true");
            var tagName = $("[name='" + name + "']").prop("tagName");
            var tagType = $("[name='" + name + "']").attr("type");
            var className = $("[name='" + name + "']").attr("class");
            if (tagType == "radio" || tagType == "checkbox") {
                $("[name='" + name + "']").attr("disabled", "true");
            }
            if (tagName == "SELECT") {
                $("[name='" + name + "']").attr("disabled", "true");
            }
            if (className == "date") {
                $("[name='" + name + "']").attr("disabled", "true");
            }
        }
    }
</script>