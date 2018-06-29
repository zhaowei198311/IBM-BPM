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
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1">
<title>门店生命周期-任务实例</title>
<link href="resources/desmartportal/css/layui.css" rel="stylesheet" />
<link rel="stylesheet"
	href="resources/desmartportal/css/modules/laydate/default/laydate.css" />
<link href="resources/desmartportal/css/my.css" rel="stylesheet" />
<style type="text/css">

.display_content_ins_business_key{
            color: #717171;
            padding: 20px;
            width: 50%;
            height: 60%;
            background: #fff;
            position: fixed;
            left: 18%;
            top: 16%;
            box-shadow: 0 0 10px #ccc;
        }

        .foot_ins_business_key{
            text-align: right;
            height: 50px;
            line-height: 50px;
            padding-right: 15px;
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
		
</style>
</head>
<body>
	<div class="container">
		<div class="search_area">
			<div class="layui-row layui-form">
				<div style="display: none;">
					<input id="proUid" value="${proUid}" style="display: none;">
					<input id="proAppId" value="${proAppId}" style="display: none;">
					<input id="proName" value="${proName}" style="display: none;">
				</div>
				<div class="layui-col-xs3">
					<div class="layui-form-pane">
						<div class="layui-form-this">
<!-- 							<label class="layui-form-label" style="cursor: pointer;">刷新</label> -->
							<div class="layui-input-block">
								<select id="searchType" class="layui-input-block group_select" name="group"
									lay-verify="required">
									<option value="formData">表单字段</option>
									<option value="processData">流程字段</option>
								</select>
							</div>
						</div>
					</div>
				</div>
				<div class="layui-col-xs2">
					<input id="fieldName" type="text" placeholder="字段名" class="layui-input">
				</div>
				<div class="layui-col-xs2">
					<input id="fieldValue" type="text" placeholder="字段值" class="layui-input">			
				</div>
				<div class="layui-col-xs3" style="margin-left: 25px;">
					<button class="layui-btn" onclick="queryProcessInstance(false)">查询</button>
					<button class="layui-btn" onclick="checkedBusinesskey()">发起新流程</button>
				</div>
			</div>
		</div>
		<div>
			<p class="table_list">
				<i class="layui-icon">&#xe61d;</i>共<span id="number"></span>条任务
			</p>
			<table class="layui-table" lay-even lay-skin="nob">
				<colgroup>
					 	<col width="5%">
					    <col>
					    <col>
					    <col>
					    <col> 
				</colgroup>
				<thead>
					<tr>
						<th>序号</th>
						<th>流程名称</th>
						<th>实例ID</th>
						<th>实例状态</th>
					    <th>流程实例标题</th>
					    <th>流程发起人</th>
					    <th>流程实例创建时间</th>
					    <th>流程实例完成时间</th>
					    <th>操作</th>
					</tr>
				</thead>
				<tbody id="processInstance_tbody"/>
			</table>
		</div>
		<div id="lay_page"></div>
	</div>
		<!-- 选择业务关键字 -->
	<div id="checkedBusinessKey" style="display: none;"
		class="display_content_ins_business_key">
		<div class="top">选择业务关键字</div>
		<div class="upload_overflow_middle">
			<div class="layui-upload">
				<div class="layui-upload-list">
					<table class="layui-table">
						<colgroup>
							<col width="10%">
							<col>
						</colgroup>
						<thead>
							<tr>
								<th>序号</th>
								<th style="text-align: center;">业务关键字</th>
							</tr>
						</thead>
						<tbody class="showBusinessList">

						</tbody>
					</table>
				</div>
			</div>
		</div>
		<div class="foot_ins_business_key">
			<button class="layui-btn layui-btn sure_btn" onclick="startProcess()">确定</button>
			<button class="layui-btn layui-btn cancel_btn"
				onclick="$('#checkedBusinessKey').hide()">取消</button>
		</div>
	</div>
</body>
<script type="text/javascript" src="resources/desmartportal/js/jquery-3.3.1.js" ></script>
<script type="text/javascript" src="resources/desmartportal/js/layui.all.js"></script>
<script type="text/javascript" src="resources/desmartportal/js/common.js" ></script>
<script>
	// 为翻页提供支持
	var pageConfig = {
		pageNum : 1,
		pageSize : 10,
		total : 0
	}

// 	layui.use([ 'laypage', 'layer' ], function() {
// 		var laypage = layui.laypage, layer = layui.layer;
// 		//完整功能
// 		laypage.render({
// 			elem : 'lay_page',
// 			count : 50,
// 			limit : 10,
// 			layout : [ 'count', 'prev', 'page', 'next', 'limit', 'skip' ],
// 			jump : function(obj) {
// // 				console.log(obj)
// 			}
// 		});
// 	});

	$(document).ready(function() {
		var sign = true;
		// 加载数据
		queryProcessInstance(sign);
	});

	var index;
	function queryProcessInstance(sign) {
		var proUid = $('#proUid').val();
		var proAppId = $('#proAppId').val();
		var key = $('#fieldName').val();
		var value = $('#fieldValue').val();
		// 按条件查询 流程实例
		$.ajax({
			url : 'processInstance/queryProcessInstanceByIds',
			type : 'post',
			dataType : 'json',
			data : {
				pageNum : pageConfig.pageNum,
				pageSize : pageConfig.pageSize,
				proUid : proUid,
				proAppId : proAppId,
				key : key,
				value : value,
				sign : sign
			},
			beforeSend : function() {
				index = layer.load(1);
			},
			success : function(result) {
				if (result.status == 0) {
					drawTable(result.data);
				} else {
					layer.alert(result.msg);
				}
				layer.close(index);
			},
			error : function() {
				layer.close(index);
			}
		})
	}

	function drawTable(data) {
		// 查询结果总数
		pageConfig.total = data[data.length - 1].count;
		// 数据总数
		$('#number').text(pageConfig.total);
		// 分页
		doPage();
		// 渲染数据
		$("#processInstance_tbody").html('');
		if (data.length == 0) {
			return;
		}
		// 移除数组最后一个元素
		data.pop();
		var trs = "";
		// 流程名称
		var proName = $('#proName').val();
		$(data).each(function(i) {
				var sortNum = i + 1;
				var createDate = datetimeFormat_1(new Date(this.insCreateDate));
				var finishDate = "";
				if (this.insFinishDate != null && this.insFinishDate != "") {
					finishDate = datetimeFormat_1(new Date(this.insFinishDate));
				}
				if(this.insStatusId=='1'){
					this.insStatus = '运转中';
				}else if(this.insStatusId=='2'){
					this.insStatus = '完成';
				}else if(this.insStatusId=='3'){
					this.insStatus = '失败';
				}else if(this.insStatusId=='4'){
					this.insStatus = '终止';
				}else if(this.insStatusId=='5'){
					this.insStatus = '未启动';
				}else if(this.insStatusId=='6'){
					this.insStatus = '暂停';
				}
				trs += '<tr style = "cursor:pointer;" ondblclick=openTaskInstance("'+ this._id +'")>'
						+ '<td>'+ sortNum +'</td>'
						+ '<td>'+ proName +'</td>'
						+ '<td>'+ this.insId +'</td>'
						+ '<td>'+ this.insStatus +'</td>'
						+ '<td>'+ this.insTitle +'</td>'
						+ '<td>'+ this.initUserFullname +'</td>'
						+ '<td>'+ createDate +'</td>'
						+ '<td>'+ finishDate +'</td>'
						+ '<td>'
						+ '<i class="layui-icon layui-icon-search" onclick=openTaskInstance("'+ this._id +'")></i>'
						+ '</td>'
						+ '</tr>';
		});
		$("#processInstance_tbody").append(trs);
	}

	// 分页
	function doPage() {
		layui.use([ 'laypage', 'layer' ], function() {
			var laypage = layui.laypage, layer = layui.layer;
			//完整功能
			laypage.render({
				elem : 'lay_page',
				curr : pageConfig.pageNum,
				count : pageConfig.total,
				limit : pageConfig.pageSize,
				layout : [ 'count', 'prev', 'page', 'next', 'limit', 'skip' ],
				jump : function(obj, first) {
					// obj包含了当前分页的所有参数  
					pageConfig.pageNum = obj.curr;
					pageConfig.pageSize = obj.limit;
					if (!first) {
						queryProcessInstance(false);
					}
				}
			});
		});
	}
	// 单击行跳转至任务实例页面
	function openTaskInstance(insUid) {
		window.location.href = 'menus/processType?insUid=' + insUid;
	}
	function startProcess(insBusinessKey) {
		if (insBusinessKey == null || insBusinessKey == ''
				|| insBusinessKey == undefined) {
			insBusinessKey = $(".showBusinessList").find(
					"input[name='insBusinessKey']:checked").val();
			if (insBusinessKey == null || insBusinessKey == ''
					|| insBusinessKey == undefined) {
				layer.alert("请选择业务关键字");
				return;
			}
		}
		var proUid = $('#proUid').val();
		var proAppId = $('#proAppId').val();
		var verUid = $('#verUid').val();
		var proName = $('#proName').val();
		var categoryName = $('#categoryName').val();
		window.location.href = 'menus/startProcess?proUid=' + proUid
				+ '&proAppId=' + proAppId + '&verUid=' + verUid + '&proName='
				+ proName + '&categoryName=' + categoryName
				+ '&insBusinessKey=' + insBusinessKey;
	}

	function checkedBusinesskey() {
		var proUid = $('#proUid').val();
		var proAppId = $('#proAppId').val();
		var verUid = $('#verUid').val();
		$.ajax({
			url : "processInstance/checkedBusinesskey",
			type : 'POST',
			dataType : 'json',
			beforeSend : function() {
				layer.load(1);
			},
			data : {
				proUid : proUid,
				proAppId : proAppId,
				proVerUid : verUid
			},
			success : function(result) {
				if (result.status == 0) {
					if (result.data.flag == 1) {
						startProcess(result.data.stepBusinessKey);
					} else {
						layer.closeAll('loading');
						$("#checkedBusinessKey").find(
								".showBusinessList").empty();
						for (var i = 0; i < result.data.length; i++) {
							var info = '<tr><td><input type="radio" name="insBusinessKey" '
									+'value="'
									+ result.data[i]
									+'" >'
									+ (i + 1)
									+ '</td>'
									+ '<td style="text-align: center;">'
									+ result.data[i] + '</td></tr>';
							$("#checkedBusinessKey").find(
									".showBusinessList").append(info);
						}
						$("#checkedBusinessKey").show();
					}
				} else {
					layer.closeAll('loading');
					layer.alert(result.msg);
				}
			},
			error : function() {
				layer.closeAll('loading');
				layer.alert("发起流程异常");
			}
		})
	}
</script>
</html>