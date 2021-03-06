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
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
<link href="resources/desmartportal/formDesign/css/bootstrap-combined.min.css"
	rel="stylesheet">
<link href="resources/desmartportal/formDesign/css/layoutit.css" rel="stylesheet">
<link href="resources/desmartportal/css/modules/laydate/default/laydate.css"
	rel="stylesheet">
<link rel="stylesheet" href="resources/desmartportal/css/layui.css" media="all">
<link rel="stylesheet" href="resources/desmartportal/css/my.css" />
<style type="text/css">
	.container-fluid {
	    padding-right: 0px; 
	    padding-left: 0px;
	    zoom: 1;
	}
</style>
</head>
<body>
	<div class="search_area top_btn">
		<input id="formId" value="${formId}" style="display: none;"> <input
			id="proUid" value="${proUid}" style="display: none;"> <input
			id="proAppId" value="${proAppId}" style="display: none;"> <input
			id="verUid" value="${verUid}" style="display: none;"> <input
			id="userId" value="${userId}" style="display: none;"> <input
			id="proName" value="${proName}" style="display: none;"> <input
			id="dataInfo" style="display: none;"> <span
			style="padding-left: 10px; color: #777; font-size: 18px;">门店生命周期流程</span>
		<span style="float: right; padding-right: 20px;">
			<button id="saveInfoBtn" class="layui-btn  layui-btn-sm">保存</button>
			<button class="layui-btn layui-btn-sm" onclick="startProcess()">提交</button>
			<button class="layui-btn layui-btn-sm back_btn" onclick="back()">退出</button>
		</span>
	</div>
	<div class="container" style="margin-left:0px;">
		<div class="content" style="width: 1100px">
		<table class="layui-table" style="width: 1100px">
					<colgroup>
					    <col width="150">
					    <col>
					    <col width="150">
					    <col> 
					</colgroup>
					<tbody>
						<tr>
							<th colspan="4" class="list_title">目标店调查表
							<span style="float: right;font-size:14px;font-weight:normal;">流程编号：1000-10185-BG-60</span>
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
					      <td><input type="text" name="title" required  lay-verify="required" value="00003" autocomplete="off" class="layui-input"></td>
					      <td class="td_title">姓名</td>
					      <td><input type="text" name="title" required  lay-verify="required" value="00003" autocomplete="off" class="layui-input"></td>
					    </tr>
					    <tr>
					      <td class="td_title">创建日期</td>
					      <td><input type="text" name="title" required  lay-verify="required" value="2018-03-12" autocomplete="off" class="layui-input"></td>
					      <td class="td_title">金额</td>
					      <td><input type="text" name="title" required  lay-verify="required" value="11453" autocomplete="off" class="layui-input"></td>
					    </tr>
					</tbody>
				</table>
		
			<div id="formSet"></div>
		</div>
	</div>
</body>

</html>
<script type="text/javascript" src="resources/desmartportal/js/jquery-3.3.1.js"></script>
<script type="text/javascript" src="resources/desmartportal/js/layui.all.js"></script>
<script type="text/javascript" src="resources/desmartportal/js/common.js"></script>
<script>
	function startProcess() {
		var proUids = $("#proUid").val();
		var proAppIds = $("#proAppId").val();
		var verUids = $("#verUid").val();
		$.ajax({
			url : 'process/startProcess',
			type : 'POST',
			dataType : 'json',
			data : {
				proUid : proUids,
				proAppId : proAppIds,
				verUid : verUids
			},
			success : function(result) {
				if (result.status == 0) {
					layer.alert('提交成功', {
						icon : 1
					});
				}
				if (result.status == 1) {
					layer.alert('提交失败', {
						icon : 2
					});
				}
			},
			error : function(result) {
				layer.alert('提交失败', {
					icon : 2
				});
			}
		});
	}

	function clientSideInclude(dynUid) {
		$.ajax({
			url : "formSet/getFormFileByFormUid",
			method : "post",
			async : false,
			data : {
				dynUid : dynUid
			//表单Id，唯一主键
			},
			success : function(result) {
				if (result.status == 0) {
					console.log(result.data);
					$("#formSet").html(result.data);
				}
			}
		});
	}
	$(function() {
		clientSideInclude(document.getElementById('formId').value);
		saveData();
	})

	/**
	 * 保存表单数据的方法
	 */
	var saveData = function() {
		$("#saveInfoBtn")
				.click(
						function(e) {
							e.preventDefault();
							var inputArr = $("table input");
							var selectArr = $("table select");
							var control = true; //用于控制复选框出现重复值
							var checkName = ""; //用于获得复选框的class值，分辨多个复选框
							var json = "{";
							for (var i = 0; i < inputArr.length; i++) {
								var type = $(inputArr[i]).attr("type");
								var textJson = "";

								var checkJson = "";
								switch (type) {
								case "text": {
									if ($(inputArr[i]).prop("class") == "layui-input layui-unselect") {
										var name = $(inputArr[i]).parent()
												.parent().prev().prop("name");
										var value = $("[name='" + name + "']")
												.val();
										textJson = "\"" + name
												+ "\":{\"value\":\"" + value
												+ "\"}";
										break;
									}
								}
									;
								case "tel":
									;
								case "date":
									;
								case "textarae": {
									var name = $(inputArr[i]).attr("name");
									var value = $("[name='" + name + "']")
											.val();
									textJson = "\"" + name + "\":{\"value\":\""
											+ value + "\"}";
									break;
								}
								case "radio": {
									var name = $(inputArr[i]).attr("name");
									var radio = $("[name='" + name + "']")
											.parent().parent().find(
													"input:radio:checked");
									textJson = "\"" + name + "\":{\"value\":\""
											+ radio.attr("id") + "\"}";
									break;
								}
								case "checkbox": {
									var name = $(inputArr[i]).attr("name");
									var checkbox = $("[name='" + name + "']")
											.parent().parent().find(
													"input:checkbox:checked");
									//判断每次的复选框是否为同一个class
									if (control) {
										checkName = checkbox.attr("name");
									} else {
										if (checkName != checkbox.attr("name")) {
											checkName = checkbox.attr("name");
											control = true;
										}
									}

									if (control) {
										control = false;
										checkJson += "\"" + checkName
												+ "\":{\"value\":[";
										for (var j = 0; j < checkbox.length; j++) {
											if (j == checkbox.length - 1) {
												checkJson += "\""
														+ $(checkbox[j]).attr(
																"id") + "\"";
											} else {
												checkJson += "\""
														+ $(checkbox[j]).attr(
																"id") + "\",";
											}
										}
										checkJson += "]},";
									}

									json += checkJson;
									break;
								}
								}//end switch
								textJson += ",";
								if (json.indexOf(textJson) == -1) {
									json += textJson;
								}
							}
							//获得最后一位字符是否为","
							var charStr = json.substring(json.length - 1,
									json.length);

							if (charStr == ",") {
								json = json.substring(0, json.length - 1);
							}
							json += "}";
							document.getElementById('dataInfo').value = json;
							// 保存草稿数据					
							var proUids = $("#proUid").val();
							var proAppIds = $("#proAppId").val();
							var verUids = $("#verUid").val();
							var proName = $("#proName").val();
							var userId = $("#userId").val();
							var dataInfo = $("#dataInfo").val();
							$.ajax({
								url : "drafts/saveDrafts",
								method : "post",
								async : false,
								data : {
									dfsTitle : proName,
									dfsData : dataInfo,
									dfsCreator : userId,
									proUid : proUids,
									proVerUid : verUids,
									proAppId : proAppIds
								},
								success : function(result) {
									layer.alert('保存成功')
								}
							});
						}); //end
	}
	
	// 回退到上一页面
	function back(){
		var proUid = $("#proUid").val();
		var proAppId = $("#proAppId").val();
		var verUid = $("#verUid").val();
		window.location.href = 'menus/processType?proUid=' + proUid
		+ '&proAppId=' + proAppId + '&verUid=' + verUid ;
	}
	
	// 查看流程图
	function viewProcess(){
		
	}
</script>
<script type="text/javascript" src="resources/desmartportal/formDesign/js/my.js"></script>