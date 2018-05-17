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
<title>草稿详情</title>
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
   				 overflow: scroll;
			}
	
</style>
</head>
<body>
	<div class="search_area top_btn">
			<input id="proUid" value="${proUid}" style="display: none;"> <input
			id="proAppId" value="${proAppId}" style="display: none;"> <input
			id="verUid" value="${verUid}" style="display: none;"> <input
			id="dfsId" value="${dfsId}" style="display: none;"> <span
			id="dfsData" value='${dfsData}'></span> <input
			id="formId" value="${formId}" style="display: none;"><span
			style="padding-left: 10px; color: #777; font-size: 18px;">门店生命周期流程</span>
		<span style="float: right; padding-right: 20px;">
			<button class="layui-btn layui-btn-sm" onclick="startProcess()">提交</button>
			<button class="layui-btn layui-btn-sm back_btn" onclick="back()">返回草稿箱列表</button>
		</span>
	</div>
	<div class="container" style="margin-left:0px; width: 100%">
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
			<div class="layui-tab">
					  	<ul class="layui-tab-title">
					  		<li class="layui-this">附件</li>
					  	</ul>
			<div class="layui-tab-content" style="padding:0;">	
				<div class="layui-tab-item layui-show" style="height: auto;">
						    	<table class="layui-table upload-file-table" style="margin:0;">
									<colgroup>
									    <col width="5%">
									    <col width ="20%"><%-- 
									    <col width="15%">
									    <col width="20%"> 
									    <col width="10%"> --%>
									    <col width="10%">
									    <col width="20%">
									</colgroup>
									<thead>
									    <tr>
									      <th><!-- <input id="all-file-check" type="checkbox"> -->序号</th>
									      <th>附件名称</th><!-- 
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
	<div class="display_content_accessory_file" id="upload_file_modal" >
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
									    <col width ="20%">
									    <col width="15%">
									    <col width="20%"> 
									    <col width="10%">
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
		
</body>

</html>
<script type="text/javascript" src="resources/desmartportal/js/jquery-3.3.1.js"></script>
<script type="text/javascript" src="resources/desmartportal/js/layui.all.js"></script>
<script type="text/javascript" src="resources/desmartportal/js/common.js"></script>
<!-- 附件上传js -->
	<script src="resources/desmartportal/js/my/myFileUpload.js"></script>
<script>
	function startProcess() {
		var proUids = $("#proUid").val();
		var proAppIds = $("#proAppId").val();
		var verUids = $("#verUid").val();
		var dfsIds = $("#dfsId").val();
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
					// 提交成功之后 删除该草稿数据
					deleteDraftsInfo(dfsIds);
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
	
	function deleteDraftsInfo(id){
		$.ajax({
			url : 'drafts/deleteDraftsById',
			type : 'POST',
			dataType : 'text',
			data : {
				dfsId : id
			},
			success : function(result) {
				// 删除成功后 ajxa跳转 查询controller
				window.location.href = "drafts/index";
			}
		})
	}

	$(function() {
		clientSideInclude(document.getElementById('formId').value);	
	})
	/**
	 * 表单数据
	 */
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
						$("#formSet").html(result.data);
					}
				}
			});
		}
	
	
	// 回退到上一页面
	function back(){
		window.location.href = 'drafts/index';
	}
	
</script>
<script type="text/javascript" src="resources/desmartportal/formDesign/js/my.js"></script>
<script>
	//数据信息
	var view = $(".container-fluid");
	var form = null;
	$(function(){
		layui.use(['form'], function () {
	        form = layui.form;
		});
		getdata($("#dfsData").attr("value"));
	});
	
	function getdata(jsonStr){
		var json = JSON.parse(jsonStr);
		for(var name in json){
			var paramObj = json[name];
			//给各个组件赋值
			setValue(paramObj,name);
			//判断组件是否可见
			isDisplay(paramObj,name);
			//判断组件对象是否可编辑
			isEdit(paramObj,name);
		}
	}

	/**
	 * 根据组件对象的类型给各个组件赋值
	 * @param paramObj 组件对象
	 * @param id 各个组件的id(单选框为class)
	 */
	var setValue = function(paramObj,name){
		var tagName = $("[name='"+name+"']").prop("tagName");
		switch(tagName){
			case "INPUT":{
				var tagType = $("[name='"+name+"']").attr("type");
				switch(tagType){
					case "text":;
					case "tel":;
					case "date":{
						$("[name='"+name+"']").val(paramObj["value"]);
						form.render();
						break;
					};
					case "radio":{
						$("[name='"+name+"'][id='"+paramObj["value"]+"']").prop("checked","true");
						form.render();
						break;
					}
					case "checkbox":{
						var valueArr = paramObj["value"];
						for(var value in valueArr){
							$("[name='"+name+"'][id='"+valueArr[value]+"']").prop("checked","true");
						}
						form.render();
						break;
					}
				}
				break;
			};
			case "SELECT":;
			case "TEXTAREA":{
				$("[name='"+name+"']").val(paramObj["value"]);
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
	var isDisplay = function(paramObj,name){
		var display = paramObj["display"];
		if(display=="none"){
			var tagType = $("[name='"+name+"']").attr("type");
			$("[name='"+name+"']").parent().css("display","none");
			$("[name='"+name+"']").parent().prev().css("display","none");
			if(tagType=="radio" || tagType=="checkbox"){
				$("[name='"+name+"']").parent().css("display","none");
				$("[name='"+name+"']").parent().prev().css("display","none");
			}
		}
	}
	
	/**
	 * 判读组件对象是否可编辑
	 * @param paramObj 组件对象
	 * @param id 各个组件的id(单选框为class)
	 */
	var isEdit = function(paramObj,name){
		var edit = paramObj["edit"];
		if(edit=="no"){
			$("[name='"+name+"']").attr("readonly","true");
			var tagName = $("[name='"+name+"']").prop("tagName");
			var tagType = $("[name='"+name+"']").attr("type");
			var className = $("[name='"+name+"']").attr("class");
			if(tagType=="radio" || tagType=="checkbox"){
				$("[name='"+name+"']").attr("disabled","true");
			}
			if(tagName=="SELECT"){
				$("[name='"+name+"']").attr("disabled","true");
			}
			if(className=="date"){
				$("[name='"+name+"']").attr("disabled","true");
			}
		}
	}
</script>