<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>触发器</title>
<%@include file="common/head.jsp"%>
<%@include file="common/tag.jsp"%>
<link href="<%=basePath%>/resources/tree/css/demo.css" rel="stylesheet"
	media="all">
<link href="<%=basePath%>/resources/css/my.css" rel="stylesheet"
	media="all" />
</head>
<body>
	<div class="layui-container" style="margin-top: 20px; width: 100%;">
		<div class="search_area">
			<div class="layui-row layui-form">
				<div class="layui-col-md2">
					<input id="triggerName_input" type="text" placeholder="触发器名称"
						class="layui-input">
				</div>
				<div class="layui-col-md2">
					<select id="triggerType_select" name="triggerType">
						<option value="">请选触发器类型</option>
						<option value="script">script</option>
						<option value="sql">sql</option>
						<option value="interface">interface</option>
						<option value="javaclass">javaclass</option>
					</select>
				</div>
				<div class="layui-col-md2" style="text-align: right; width: 280px">
					<button class="layui-btn search_btn" id="searchMeat_btn">查询</button>
					<button class="layui-btn create_btn" id="show_expose_btn">添加</button>
					<button class="layui-btn delete_btn" id="meta_del_btn">删除</button>
				</div>
			</div>
		</div>
		<div>
			<table id="proMet_table" class="layui-table backlog_table " lay-even
				lay-skin="nob">
				<colgroup>
					<col>
					<col>
					<col>
					<col>
					<col>
					<col>
					<col>
					<col>
					<col>
					<col>
				</colgroup>
				<thead>
					<tr>
						<th><input type="checkbox" id="select_all_check" name=""
							title='全选' lay-skin="primary"> 序号</th>
						<th>触发器标题</th>
						<th>触发器描述</th>
						<th>触发器类型</th>
						<th>触发器执行命令</th>
						<th>触发器参数</th>
						<th>创建人</th>
						<th>创建时间</th>
						<th>更新人</th>
						<th>更新时间</th>
					</tr>
				</thead>
				<tbody id="trigger_table_tbody"></tbody>
			</table>
		</div>
		<div id="lay_page"></div>

		<div class="display_container">
			<div class="display_content3">
				<div class="top" style="color: red;">新增触发器</div>
				<label class="layui-input-label" style="color: red;">带*为必填参数</label>
				<form id="form1" class="layui-form" action=""
					style="margin-top: 30px;">
					<div class="layui-form-item">
						<div class="layui-inline">
							<label class="layui-form-label" style="width: 100px">触发器名称*:</label>
							<div class="layui-input-inline">
								<input type="text" id="triTitle" name="triTitle"
									lay-verify="triTitle" autocomplete="off" class="layui-input">
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-label" style="width: 125px">触发器描述:</label>
							<div class="layui-input-inline">
								<input type="text" id="triDescription" name="triDescription"
									lay-verify="triDescription" autocomplete="off"
									class="layui-input">
							</div>
						</div>
					</div>
					<div class="layui-form-item">
						<div class="layui-inline">
							<label class="layui-form-label" style="width: 100px">触发器类型*:</label>
							<div class="layui-input-inline">
								<select id="triType" name="triType">
									<option value="">请选择触发器类型</option>
									<option value="script">script</option>
									<option value="sql">sql</option>
									<option value="interface">interface</option>
									<option value="javaclass">javaclass</option>
								</select>
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-label" style="width: 125px">触发器执行命令*:</label>
							<div class="layui-input-inline">
								<input type="text" id="triWebbot" name="triWebbot"
									lay-verify="triWebbot" autocomplete="off" class="layui-input">
							</div>
						</div>
					</div>
					<div class="layui-form-item">
						<div class="layui-inline">
							<label class="layui-form-label" style="width: 100px">触发器参数:</label>
							<div class="layui-input-inline">
								<input type="text" id="triParam" name="triParam"
									lay-verify="triParam" autocomplete="off" class="layui-input">
							</div>
						</div>
					</div>
				</form>
				<div class="foot">
					<button id="sure_btn" class="layui-btn layui-btn sure_btn">确定</button>
					<button id="cancel_btn" class="layui-btn layui-btn cancel_btn">取消</button>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
<script src="<%=basePath%>/resources/js/layui.all.js"></script>
<script src="<%=basePath%>/resources/lay/modules/laypage.js"></script>
<script type="text/javascript">
	
	 // 为翻页提供支持
        var pageConfig = {
        	pageNum: 1,
        	pageSize: 10,
        	total: 0
        }
	
    	// 加载事件
    	$(document).ready(function(){  		
    	// 加载数据
    	getTriggerInfo();
    	
		// 表单验证
		$('#form1').validate({
	        rules : {
	        	triTitle : {
	        		required : true
	            },
	            triType: {
	            	required: true
	            },
	            triWebbot: {
	            	required: true
	            }
	        }
	    })
    	})
	
    	function getTriggerInfo() {
		$.ajax({
			url : common.getPath() + "/trigger/search",
			type : "post",
			dataType : "json",
			data : {
				"pageNum" : pageConfig.pageNum,
				"pageSize" : pageConfig.pageSize
			},
			success : function(result) {
				if (result.status == 0) {
					drawTable(result.data);
				}
			}
		});
	}	
    	
	// 请求数据成功
	function drawTable(pageInfo) {
		pageConfig.pageNum = pageInfo.pageNum;
		pageConfig.pageSize = pageInfo.pageSize;
		pageConfig.total = pageInfo.total;
		doPage();
		// 渲染数据
		$("#trigger_table_tbody").html('');
		if (pageInfo.total == 0) {
			return;
		}

		var list = pageInfo.list;
		var startSort = pageInfo.startRow;//开始序号
		var trs = "";
		for (var i = 0; i < list.length; i++) {
			var meta = list[i];
			var sortNum = startSort + i;
			var createTime = "";
			var updateTime = "";
			if (meta.createTime) {
				createTime = common.dateToString(new Date(meta.createTime));
			}
			if (meta.updateTime) {
				updateTime = common.dateToString(new Date(meta.updateTime));
			}
			trs += '<tr><td><input type="checkbox" name="eCheck" value="' + meta.triUid + '" lay-skin="primary">'
					+ sortNum + '</td>' + '<td>' + meta.triTitle + '</td>'
					+ '<td>' + meta.triDescription + '</td>' + '<td>'
					+ meta.triType + '</td>' + '<td>' + meta.triWebbot
					+ '</td>' + '<td>' + meta.triParam + '</td>' + '<td>'
					+ meta.creator + '</td>' + '<td>' + createTime + '</td>'
					+ '<td>' + meta.updator + '</td>' + '<td>' + updateTime
					+ '</td>' + '</tr>';
		}
		$("#trigger_table_tbody").append(trs);

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
						getTriggerInfo();
					}
				}
			});
		});
	}
	
	$(".delete_btn").click(function(){
		$("input[name='eCheck']:checked").each(function() {
			$.ajax({
				url : '<%=basePath%>/trigger/delete',
				type : 'POST',
				dataType : 'text',
				data : {
					triUid : this.value
				},
				success : function(result) {
					window.location.href = "<%=basePath%>/trigger/index";
				}
			})
		})
	})
	
	$(".search_btn").click(function(){
		var name = document.getElementById("triggerName_input").value;
		var type = document.getElementById("triggerType_select").value;
		$.ajax({
			url : '<%=basePath%>/trigger/search',
			type : 'POST',
			dataType : 'json',
			data : {
				triTitle : name,
				triType : type
			},
			success : function(result) {
				if (result.status == 0) {
					drawTable(result.data);
				}
			}
		})
	})
	
	$(".create_btn").click(function(){
		$(".display_container").css("display", "block");
	})
	
	$(".cancel_btn").click(function(){
		$(".display_container").css("display", "none");
	})
	
	$(".sure_btn").click(function(){
		// 新增触发器		
		if ($("#form1").valid()) {
	 		$.ajax({
				url : '<%=basePath%>/trigger/save',
				type : 'POST',
				dataType : 'text',
				data : {
					triTitle : $("#triTitle").val(),
					triDescription : $("#triDescription").val(),
					triType : $("#triType").val(),
					triWebbot : $("#triWebbot").val(),
					triParam : $("#triParam").val()
				},
				success : function(result){
					window.location.href = "<%=basePath%>" + "/trigger/index"
				},
				error : function(result) {
					layer.msg('添加失败', {
						icon : 5
					});
				}
			})
		}
	})
</script>