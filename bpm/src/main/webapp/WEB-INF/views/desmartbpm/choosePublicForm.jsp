<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<%@include file="common/head.jsp"%>
	<%@include file="common/tag.jsp"%>
	<link rel="stylesheet" href="<%=basePath%>/resources/desmartbpm/css/my.css" media="all">
	<link href="<%=basePath%>/resources/desmartbpm/tree/css/demo.css" rel="stylesheet">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>选择关联公共表单</title>
</head>
<body>
	<div class="container" style="margin-top: 20px;">
		<div class="search_area">
			<div class="layui-row layui-form">
				<div class="layui-col-xs4">
					<input type="text" id="search_form_name" placeholder="表单名称" class="layui-input">
					<input type="hidden" id="formUid" value="${ formUid }" />
					<input type="hidden" id="elementId" value="${ elementId }" />
				</div>
				<div class="layui-col-xs4" style="text-align: right;">
					<button class="layui-btn" id="search_btn">查询</button>
				</div>
			</div>
		</div>
		<div style="width: 100%; overflow-x: auto;">
			<table class="layui-table backlog_table" lay-even lay-skin="nob">
				<colgroup>
					<col>
					<col>
					<col>
				</colgroup>
				<thead>
					<tr>
						<th>序号</th>
						<th>表单名称</th>
						<th>表单描述</th>
					</tr>
				</thead>
				<tbody id="form_table_tbody">
				</tbody>
			</table>
		</div>
		<div id="lay_page"></div>
		<div class="foot" style="position: absolute; bottom: 35px; right: 5px;">
			<button class="layui-btn layui-btn sure_btn" id="sure_btn">确定</button>
			<button class="layui-btn layui-btn layui-btn-primary cancel_btn" id="cancel_btn">取消</button>
		</div>
	</div>
</body>
<script type="text/javascript" src="<%=basePath%>/resources/desmartbpm/js/layui.all.js"></script>
<script type="text/javascript">
	//分页控件的参数
	var pageConfig = {
		pageNum: 1,
		pageSize: 10,
		total: 0
	}

	$(function () {
		queryFormByName();
		searchForm();

		$("#sure_btn").click(
			function () {
				var checkObj = $("input[name='formInfo_check']:checked");
				var publicFormUid = checkObj.val();
				var elementId = $("#elementId").val();
				var publicFormUidStr = window.parent.document.getElementById("publicFormUidArr").value;
				var publicFormUidArr = publicFormUidStr.split(";");
				if(publicFormUid==window.parent.document.getElementById(elementId).value){
					var publicFormName = checkObj.parent().next().text();
					window.parent.document
							.getElementById(elementId).value = publicFormUid;
					window.parent.document
							.getElementById(elementId + "_view").innerText = "关联的子表单名："+publicFormName;
				}else{
					if($.inArray(publicFormUid,publicFormUidArr) == -1){
						if(window.parent.document.getElementById(elementId).value!=null 
								&& window.parent.document.getElementById(elementId).value!=""){
							publicFormUidArr.splice($.inArray(window.parent.document.getElementById(elementId).value, publicFormUidArr), 1);
						}
						publicFormUidArr.push(publicFormUid);
						publicFormUidStr = publicFormUidArr.join(";");
						var publicFormName = checkObj.parent().next().text();
						window.parent.document.getElementById(elementId).value = publicFormUid;
						window.parent.document.getElementById(elementId + "_view").innerText = "关联的子表单名："+publicFormName;
						window.parent.document.getElementById("publicFormUidArr").value = publicFormUidStr;
					}else{
						layer.alert("该主表单已经绑定过相同的子表单");
						return false;
					}
				}
				//$('#cancel_btn').click();
		});

		$(".cancel_btn").click(function () {
			$(".display_container").css("display", "none");
			$(".display_container1").css("display", "none");
			$(".display_container2").css("display", "none");
		});
	});

	//查询所有的公共表单数据
	function queryFormByName() {
		$.ajax({
			url: common.getPath()
				+ "/publicForm/listFormByFormName",
			method: "post",
			data: {
				formName: $("#search_form_name").val().trim(),
				pageNum: pageConfig.pageNum,
				pageSize: pageConfig.pageSize
			},
			success: function (result) {
				drawTable(result.data);
			}
		});
	}
	//表单信息模糊查询
	function searchForm() {
		$("#search_btn").click(function () {
			queryFormByName();
		});
	}

	//渲染数据表格
	function drawTable(pageInfo) {
		pageConfig.pageNum = pageInfo.pageNum;
		pageConfig.pageSize = pageInfo.pageSize;
		pageConfig.total = pageInfo.total;
		doPage();
		// 渲染数据
		$("#form_table_tbody").html('');
		if (pageInfo.total == 0) {
			return;
		}
		var elementId = $("#elementId").val();
		var publicFormUid = window.parent.document.getElementById(elementId).value;
		
		var list = pageInfo.list;
		var startSort = pageInfo.startRow;//开始序号
		var trs = "";
		for (var i = 0; i < list.length; i++) {
			var formInfo = list[i];
			var sortNum = startSort + i;
			var createTime = common.dateToString(new Date(
				formInfo.createTime));
			trs += '<tr data-formuid="'+ formInfo.publicFormUid+ '">'
				+ '<td>'
			if(publicFormUid == formInfo.publicFormUid){
				trs += '<input type="checkbox" name="formInfo_check" onclick="onSelOne(this);" checked value="'
			}else{
				trs += '<input type="checkbox" name="formInfo_check" onclick="onSelOne(this);" value="'
			}
			trs += formInfo.publicFormUid
				+ '" lay-skin="primary"> '
				+ sortNum
				+ '</td>'
				+ '<td>'
				+ formInfo.publicFormName + '</td>';
			if (formInfo.publicFormDescription != null
				&& formInfo.publicFormDescription != "") {
				trs += '<td>' + formInfo.publicFormDescription
					+ '</td>';
			} else {
				trs += '<td></td>';
			}
			trs += '</tr>';
		}
		$("#form_table_tbody").append(trs);
	}

	//渲染分页插件
	function doPage() {
		layui.use(['laypage', 'layer'], function () {
			var laypage = layui.laypage, layer = layui.layer;
			//完整功能
			laypage.render({
				elem: 'lay_page',
				curr: pageConfig.pageNum,
				count: pageConfig.total,
				limit: pageConfig.pageSize,
				layout: ['count', 'prev', 'page', 'next',
					'skip'],
				jump: function (obj, first) {
					// obj包含了当前分页的所有参数  
					pageConfig.pageNum = obj.curr;
					pageConfig.pageSize = obj.limit;
					if (!first) {
						queryFormByName();
					}
				}
			});
		});
	}

	//复制快照复选框只能选择一个
	function onSelOne(obj) {
		$('input[name="formInfo_check"]').not($(obj)).prop(
			"checked", false);
	}

	//生成随机码的方法
	function _getRandomString(len) {
		len = len || 32;
		var $chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'; // 默认去掉了容易混淆的字符oOLl,9gq,Vv,Uu,I1  
		var maxPos = $chars.length;
		var pwd = '';
		for (i = 0; i < len; i++) {
			pwd += $chars.charAt(Math.floor(Math.random()
				* maxPos));
		}
		return pwd;
	}
</script>
</html>