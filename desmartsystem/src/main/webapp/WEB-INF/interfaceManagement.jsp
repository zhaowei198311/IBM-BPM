<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8"%>
<html>
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
<link href="styles/css/layui.css" rel="stylesheet" />
<link href="styles/css/my.css" rel="stylesheet" />
<title>接口管理</title>
</head>

<body>
	<div class="layui-container" style="margin-top: 20px; width: 100%;">
		<div class="layui-row">
			<div class="layui-col-md10">
				<div class="search_area">
					<div class="layui-row layui-form">
						<div class="layui-col-md2">
							<input id="queryInterfaces" type="text" placeholder="接口名称"
								class="layui-input">
						</div>
						<div class="layui-col-md2" style="text-align: right; width: 280px">
							<button class="layui-btn layui-btn-sm select_btn">查询接口</button>
							<button id="addInterfaces"
								class="layui-btn layui-btn-sm create_btn">新增接口</button>
							<button class="layui-btn layui-btn-sm delete_btn">删除接口</button>
						</div>
					</div>
				</div>
				<div style="width: 1350px; overflow-x: auto;">
					<table class="layui-table backlog_table" lay-even lay-skin="nob"
						lay-filter="demo">
						<colgroup>
							<col>
							<col>
							<col>
							<col>
							<col>
							<col>
						</colgroup>
						<thead>
							<tr>
								<th><input type="checkbox" name="" title='全选'
									lay-skin="primary"> 序号</th>
								<th>接口名称</th>
								<th>接口描述</th>
								<th>接口类型</th>
								<th>接口访问地址</th>
								<th>接口访问方法名</th>
								<th>接口状态</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody id="proMet_table_tbody" />
					</table>
				</div>
				<div id="lay_page"></div>
			</div>
		</div>
	</div>
	<div class="display_container3" id="exposed_table_container">
		<div class="display_content2">
			<div class="top">接口参数配置</div>
			<div class="middle1"
				style="width: 700px; height: 225px; overflow: scroll">
				<table style="width: 1500px" class="layui-table backlog_table"
					lay-even lay-skin="nob">
					<colgroup>
						<col>
						<col>
						<col>
						<col>
					</colgroup>
					<thead>
						<tr>
							<th>序号</th>
							<th>参数索引</th>
							<th>参数名称</th>
							<th>参数描述</th>
							<th>参数类型</th>
							<th>参数长度</th>
							<th>多值分隔符</th>
							<th>是否多值</th>
							<th>是否必须</th>
							<th>接口定义ID</th>
						</tr>
					</thead>
					<tbody id="exposed_table_tbody"></tbody>
				</table>
			</div>
			<div class="foot">
				<button class="layui-btn layui-btn sure2_btn">保存</button>
				<button class="layui-btn layui-btn layui-btn-primary cancel2_btn">取消</button>
			</div>
		</div>
	</div>
	<div class="display_container">
		<div class="display_content2" style="overflow: scroll; height: 580px;">
			<div class="top">新增接口</div>
			<label class="layui-form-label" style="color: red;">带*的为必填参数</label>
			<div class="max">
				<form class="layui-form" action="" style="margin-top: 30px;">
					<div class="layui-form-item">
						<label class="layui-form-label">接口名称*:</label>
						<div class="layui-input-block">
							<input id="intTitle" type="text" name="intTitle" required
								lay-verify="required" value="" autocomplete="off"
								class="layui-input">
						</div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">接口描述:</label>
						<div class="layui-input-block">
							<input id="intDescription" type="text" name="intDescription"
								required lay-verify="required" value="" autocomplete="off"
								class="layui-input">
						</div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">接口类型*:</label>
						<div class="layui-input-block">
							<input id="intType" type="text" name="intType" required
								lay-verify="required" value="" autocomplete="off"
								class="layui-input">
						</div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">接口访问地址*:</label>
						<div class="layui-input-block">
							<input id="intUrl" type="text" name="intUrl" required
								lay-verify="required" value="" autocomplete="off"
								class="layui-input">
						</div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">接口访问方法名:</label>
						<div class="layui-input-block">
							<input id="intCallMethod" type="text" name="intCallMethod"
								required lay-verify="required" value="" autocomplete="off"
								class="layui-input">
						</div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">登陆用户名:</label>
						<div class="layui-input-block">
							<input id="intLoginUser" type="text" name="intLoginUser" required
								lay-verify="required" value="" autocomplete="off"
								class="layui-input">
						</div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">登陆密码:</label>
						<div class="layui-input-block">
							<input id="intLoginPwd" type="text" name="intLoginPwd" required
								lay-verify="required" value="" autocomplete="off"
								class="layui-input">
						</div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">接口状态*:</label>
						<div class="layui-input-block">
							<input id="intStatus" type="text" name="intStatus" required
								lay-verify="required" value="" autocomplete="off"
								class="layui-input">
						</div>
					</div>
				</form>
			</div>
			<div class="foot">
				<button id="sure_btn" class="layui-btn layui-btn sure_btn">确定</button>
				<button id="cancel_btn"
					class="layui-btn layui-btn layui-btn-primary cancel_btn">取消</button>
			</div>
		</div>
	</div>
	<div class="display_container4" id="exposed_table2_container">
		<div class="display_content2" style="height: 300px">
			<div class="top">新增接口参数</div>
			<form class="layui-form" action="" style="margin-top: 30px;">
				<input id="intUid" style="display: none;"/>
				<div class="layui-form-item">
					<div class="layui-inline">
						<label class="layui-form-label">参数索引:</label>
						<div class="layui-input-inline">
							<input type="text" id="paraIndex" name="paraIndex"
								lay-verify="required|phone" autocomplete="off"
								class="layui-input">
						</div>
					</div>
					<div class="layui-inline">
						<label class="layui-form-label">参数名称:</label>
						<div class="layui-input-inline">
							<input type="text" id="paraName" name="paraName"
								lay-verify="email" autocomplete="off" class="layui-input">
						</div>
					</div>
				</div>
				<div class="layui-form-item">
					<div class="layui-inline">
						<label class="layui-form-label">参数描述:</label>
						<div class="layui-input-inline">
							<input type="text" id="paraDescription" name="paraDescription"
								lay-verify="required|phone" autocomplete="off"
								class="layui-input">
						</div>
					</div>
					<div class="layui-inline">
						<label class="layui-form-label">参数类型:</label>
						<div class="layui-input-inline">
							<input type="text" id="paraType" name="paraType"
								lay-verify="email" autocomplete="off" class="layui-input">
						</div>
					</div>
				</div>
				<div class="layui-form-item">
					<div class="layui-inline">
						<label class="layui-form-label">参数长度:</label>
						<div class="layui-input-inline">
							<input type="text" id="paraSize" name="paraSize"
								lay-verify="required|phone" autocomplete="off"
								class="layui-input">
						</div>
					</div>
					<div class="layui-inline">
						<label class="layui-form-label">多值分隔符:</label>
						<div class="layui-input-inline">
							<input type="text" id="multiSeparator" name="multiSeparator"
								lay-verify="required|phone" autocomplete="off"
								class="layui-input">
						</div>
					</div>
				</div>
				<div class="layui-form-item">
					<div class="layui-inline">
						<label class="layui-form-label">是否多值:</label>
						<div class="layui-input-inline">
							<input type="text" id="multiValue" name="multiValue"
								lay-verify="required|phone" autocomplete="off"
								class="layui-input">
						</div>
					</div>
					<div class="layui-inline">
						<label class="layui-form-label">是否必须:</label>
						<div class="layui-input-inline">
							<input type="text" id="isMust" name="isMust"
								lay-verify="required|phone" autocomplete="off"
								class="layui-input">
						</div>
					</div>
				</div>
			</form>
			<div class="foot">
				<button id="sure_btn" class="layui-btn layui-btn sure3_btn">确定</button>
				<button id="cancel_btn"
					class="layui-btn layui-btn layui-btn-primary cancel3_btn">取消</button>
			</div>
		</div>
	</div>
	</div>
</body>
</html>
<script type="text/javascript" src="scripts/js/jquery-3.3.1.js"></script>
<script type="text/javascript" src="scripts/js/layui.all.js"></script>
<script>
	// 为翻页提供支持
	var pageConfig = {
		pageNum : 1,
		pageSize : 10,
		total : 0
	}

	layui.use([ 'laypage', 'layer' ], function() {
		var laypage = layui.laypage, layer = layui.layer;
		//完整功能
		laypage.render({
			elem : 'lay_page',
			count : 50,
			limit : 10,
			layout : [ 'count', 'prev', 'page', 'next', 'limit', 'skip' ],
			jump : function(obj) {
				console.log(obj)
			}
		});
	});

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
						getInterfaceInfo();
					}
				}
			});
		});
	}

	$(document).ready(function() {
		// 加载数据
		getInterfaceInfo();

		$(".create_btn").click(function() {
			$(".display_container").css("display", "block");
		})
		$(".sure_btn").click(function() {
			$(".display_container").css("display", "none");
		})
		$(".cancel_btn").click(function() {
			$(".display_container").css("display", "none");
		})
		$(".cancel2_btn").click(function() {
			$(".display_container3").css("display", "none");
		})
		$(".sure2_btn").click(function() {
			// 修改保存当前接口参数配置
			updateParames();
		})
		$(".cancel3_btn").click(function() {
			$(".display_container4").css("display", "none");
		})
		$(".sure3_btn").click(function() {
			// 确定给 当当前接口 添加新的参数
			addParames();
		})
	});
	
	function updateParames(){
		$("input[name='eCheck']:checked").each(function() {
			$.ajax({
					url : 'interfaceParamers/update',
					type : 'POST',
					dataType : 'text',
					data : {
						paraIndex : $("#paraIndex").val(),
						paraName : $("#paraName").val(),
						paraDescription : $("#paraDescription").val(),
						paraType : $("#paraType").val(),
						paraSize : $("#paraSize").val(),
						multiSeparator : $("#multiSeparator").val(),
						multiValue : $("#multiValue").val(),
						isMust : $("#isMust").val(),
						paraUid : this.value
					},
					success : function(result){
						window.location.href = "interfaces/index";
						layer.alert('修改成功')
					}
				})
		})
	}
	
	function addParames(){
		$.ajax({
			url : 'interfaceParamers/add',
			type : 'POST',
			dataType : 'text',
			data : {
				paraIndex : $("#paraIndex").val(),
				paraName : $("#paraName").val(),
				paraDescription : $("#paraDescription").val(),
				paraType : $("#paraType").val(),
				paraSize : $("#paraSize").val(),
				multiSeparator : $("#multiSeparator").val(),
				multiValue : $("#multiValue").val(),
				isMust : $("#isMust").val(),
				intUid : $("#intUid").val()
			},
			success : function(result){
				window.location.href = "interfaces/index";
				
			}			
		})
	}

	$("#addInterfaces").click(function() {
		$(".display_container").css("display", "block")

		layui.use('laydate', function() {
			var laydate = layui.laydate
			laydate.render({
				elem : '#interfaceCreateDate'
			});
		})
	})

	$("#cancel_btn").click(function() {
		$(".display_container").css("display", "none")
	})

	$("#sure_btn").click(function() {
		layui.use([ 'layer', 'form' ], function() {
			var form = layui.form, layer = layui.layer, $ = layui.jquery;

			$.ajax({
				url : 'interfaces/add',
				type : 'POST',
				dataType : 'text',
				data : {
					intTitle : $("#intTitle").val(),
					intDescription : $("#intDescription").val(),
					intType : $("#intType").val(),
					intUrl : $("#intUrl").val(),
					intCallMethod : $("#intCallMethod").val(),
					intLoginUser : $("#intLoginUser").val(),
					intLoginPwd : $("#intLoginPwd").val(),
					intStatus : $("#intStatus").val()
				},
				success : function(result) {
					// 添加成功后 ajxa跳转 查询controller
					layer.msg('添加成功');
					window.location.href = "interfaces/index";
				}
			})
		})
	})

	// 详情 按钮
	$(".details_btn").click(function() {
		$("input[name='eCheck']:checked").each(function() {
			// 请求ajax
			$.ajax({
				url : '',
				type : 'POST',
				dataType : 'text',
				data : {
					intUid : this.value
				},
				success : function(result) {

				}
			})
		})
	})

	// 删除 行 监听表格	
	$(".delete_btn").click(function() {
		$("input[name='eCheck']:checked").each(function() {
			// 请求ajax
			$.ajax({
				url : 'interfaces/del',
				type : 'POST',
				dataType : 'text',
				data : {
					intUid : this.value
				},
				success : function(result) {
					// 删除成功后 ajxa跳转 查询controller
					window.location.href = "interfaces/index";
				}
			})
		})
	})

	// 查看
	$(".select_btn").click(function() {
		var queryInterfaces = $("#queryInterfaces").val();
		$.ajax({
			url : 'interfaces/queryDhInterfaceByTitle',
			type : 'POST',
			dataType : 'json',
			data : {
				intTitle : queryInterfaces
			},
			success : function(result) {
				// 成功的时候返回json数据 然后 进行展示
				if (result.status == 0) {
					drawTable(result.data);
				}
			}
		})
	})

	function getInterfaceInfo() {
		$.ajax({
			url : 'interfaces/queryDhInterfaceList',
			type : 'post',
			dataType : 'json',
			data : {
				pageNum : pageConfig.pageNum,
				pageSize : pageConfig.pageSize,
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
		$("#proMet_table_tbody").html('');
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
			if (meta.lastUpdateTime) {
				updateTime = common.dateToString(new Date(meta.lastUpdateTime));
			}
			trs += '<tr><td><input id="listInterface" type="checkbox" name="eCheck" value="' + meta.intUid + '" lay-skin="primary">'
					+ meta.intUid
					+ '</td>'
					+ '<td>'
					+ meta.intTitle
					+ '</td>'
					+ '<td>'
					+ meta.intDescription
					+ '</td>'
					+ '<td>'
					+ meta.intType
					+ '</td>'
					+ '<td>'
					+ meta.intUrl
					+ '</td>'
					+ '<td>'
					+ meta.intCallMethod
					+ '</td>'
					+ '<td>'
					+ meta.intStatus
					+ '</td>'
					+ '<td>'
					+ '<button class="layui-btn layui-btn-sm layui-btn-danger" onclick=danger("'
					+ meta.intUid
					+ '")>参数操作</button>'
					+ '<button class="layui-btn layui-btn-sm layui-btn-warm" onclick=warm("'
					+ meta.intUid + '")>添加参数</button>' + '</td>' + '</tr>';
		}
		$("#proMet_table_tbody").append(trs);

	}
	// 按钮事件
	function danger(intUid) {
		// “接口参数详情”按钮
		//	getParamersInfo();
		$("#exposed_table_container").css("display", "block");
		getParamersInfo(intUid);
	}

	function warm(intUid) {
		// 添加页面
		$("#exposed_table2_container").css("display", "block");
		$("#intUid").val(intUid);
	}

	function getParamersInfo(intUid) {
		$.ajax({
			url : 'interfaceParamers/index',
			type : 'post',
			dataType : 'json',
			data : {
				intUid : intUid,
				pageNum : pageConfig.pageNum,
				pageSize : pageConfig.pageSize
			},
			success : function(result) {
				if (result.status == 0) {
					drawTable2(result.data)
				}
			}
		})
	}

	// 请求数据成功
	function drawTable2(pageInfo) {
		pageConfig.pageNum = pageInfo.pageNum;
		pageConfig.pageSize = pageInfo.pageSize;
		pageConfig.total = pageInfo.total;
		// 渲染数据
		$("#exposed_table_tbody").html('');
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
			if (meta.lastUpdateTime) {
				updateTime = common.dateToString(new Date(meta.lastUpdateTime));
			}
			trs += '<tr><td><input id="paraUid" type="checkbox" name="eCheck" value="' + meta.paraUid + '" lay-skin="primary">'
					+ meta.paraUid
					+ '</td>'
					+ '<td>'
					+ '<input id="paraIndex" style="" type="text" value = "'+meta.paraIndex+'" />'
					+ '</td>'
					+ '<td>'
					+ '<input id="paraName" style="" type="text" value = "'+meta.paraName+'" />'
					+ '</td>'
					+ '<td>'
					+ '<input id="paraDescription" style="" type="text" value = "'+meta.paraDescription+'" />'
					+ '</td>'
					+ '<td>'
					+ '<input id="paraType" style="" type="text" value = "'+meta.paraType+'" />'
					+ '</td>'
					+ '<td>'
					+ '<input id="paraSize" style="" type="text" value = "'+meta.paraSize+'" />'
					+ '</td>'
					+ '<td>'
					+ '<input id="multiSeparator" style="" type="text" value = "'+meta.multiSeparator+'" />'
					+ '</td>'
					+ '<td>'
					+ '<input id="multiValue" style="" type="text" value = "'+meta.multiValue+'" />'
					+ '</td>'
					+ '<td>'
					+ '<input id="isMust" style="" type="text" value = "'+meta.isMust+'" />'
					+ '</td>'
					+ '<td>'
					+ meta.intUid + '</td>' + '</tr>';
		}
		$("#exposed_table_tbody").append(trs);
	}
</script>