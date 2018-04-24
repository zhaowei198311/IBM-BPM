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
<link href="styles/css/layui.css" rel="stylesheet" media="all" />
<link href="styles/css/my.css" rel="stylesheet" media="all" />
<title>接口管理</title>
</head>

<body>
	<div class="layui-container" style="margin-top: 20px; width: 100%;">
		<div class="search_area">
			<div class="layui-row layui-form">
				<div class="layui-col-md2">
					<input id="interfaceName" type="text" placeholder="接口名称"
						class="layui-input">
				</div>
				<div class="layui-col-md2">
					<select id="interfaceType" name="interfaceType">
						<option value="">请选择接口类型</option>
						<option value="webservice">webservice</option>
						<option value="restapi">restapi</option>
					</select>
				</div>
				<div class="layui-col-md2">
					<select id="interfaceState" name="interfaceState">
						<option value="">请选择接口状态</option>
						<option value="enabled">启用</option>
						<option value="disabled">停用</option>
					</select>
				</div>
				<div class="layui-col-md2" style="text-align: right; width: 280px">
					<button class="layui-btn layui-btn-sm select_btn">查询接口</button>
					<button id="addInterfaces"
						class="layui-btn layui-btn-sm create_btn">新增接口</button>
				</div>
			</div>
		</div>
		<div>
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
		<div class="display_content2" style="height: 300px">
			<div class="top" style="color: red;">新增接口</div>
			<label class="layui-input-label" style="color: red;">带*为必填参数</label>
			<form class="layui-form" action="" style="margin-top: 30px;">
				<div class="layui-form-item">
					<div class="layui-inline">
						<label class="layui-form-label">接口名称*:</label>
						<div class="layui-input-inline">
							<input type="text" id="intTitle" name="intTitle"
								lay-verify="text" autocomplete="off" class="layui-input">
						</div>
					</div>
					<div class="layui-inline">
						<label class="layui-form-label">接口描述:</label>
						<div class="layui-input-inline">
							<input type="text" id="intDescription" name="intDescription"
								lay-verify="text" autocomplete="off" class="layui-input">
						</div>
					</div>
				</div>
				<div class="layui-form-item">
					<div class="layui-inline">
						<label class="layui-form-label">接口类型*:</label>
						<div class="layui-input-inline">
							<select id="intType" name="intType">
								<option value="">请选择</option>
								<option value="webservice">webservice</option>
								<option value="restapi">restapi</option>
							</select>
						</div>
					</div>
					<div class="layui-inline">
						<label class="layui-form-label">接口地址*:</label>
						<div class="layui-input-inline">
							<input type="text" id="intUrl" name="intUrl" lay-verify="text"
								autocomplete="off" class="layui-input">
						</div>
					</div>
				</div>
				<div class="layui-form-item">
					<div class="layui-inline">
						<label class="layui-form-label">接口方法名:</label>
						<div class="layui-input-inline">
							<input type="text" id="intCallMethod" name="intCallMethod"
								lay-verify="text" autocomplete="off" class="layui-input">
						</div>
					</div>
					<div class="layui-inline">
						<label class="layui-form-label">登录用户名:</label>
						<div class="layui-input-inline">
							<input type="text" id="intLoginUser" name="intLoginUser"
								lay-verify="text" autocomplete="off" class="layui-input">
						</div>
					</div>
				</div>
				<div class="layui-form-item">
					<div class="layui-inline">
						<label class="layui-form-label">登陆密码:</label>
						<div class="layui-input-inline">
							<input type="text" id="intLoginPwd" name="intLoginPwd"
								lay-verify="text" autocomplete="off" class="layui-input">
						</div>
					</div>
					<div class="layui-inline">
						<label class="layui-form-label">接口状态*:</label>
						<div class="layui-input-inline">
							<input id="intStatus" type="checkbox" name="intStatus"
								lay-skin="switch" lay-filter="switch1" lay-text="启用|停用"
								value="disabled">
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
	<div class="display_container4" id="exposed_table2_container">
		<div class="display_content2" style="height: 300px">
			<div class="top" style="color: red;">绑定接口参数</div>
			<label style="color: red;">带*的参数为必填</label>
			<form class="layui-form" action="" style="margin-top: 30px;">
				<input id="intUid" style="display: none;" />
				<div class="layui-form-item">
					<div class="layui-inline">
						<label class="layui-form-label">参数索引*:</label>
						<div class="layui-input-inline">
							<input type="text" id="paraIndex" name="paraIndex"
								lay-verify="text" autocomplete="off" class="layui-input">
						</div>
					</div>
					<div class="layui-inline">
						<label class="layui-form-label">参数名称*:</label>
						<div class="layui-input-inline">
							<input type="text" id="paraName" name="paraName"
								lay-verify="text" autocomplete="off" class="layui-input">
						</div>
					</div>
				</div>
				<div class="layui-form-item">
					<div class="layui-inline">
						<label class="layui-form-label">参数描述:</label>
						<div class="layui-input-inline">
							<input type="text" id="paraDescription" name="paraDescription"
								lay-verify="text" autocomplete="off" class="layui-input">
						</div>
					</div>
					<div class="layui-inline">
						<label class="layui-form-label">参数类型*:</label>
						<div class="layui-input-inline">
							<input type="text" id="paraType" name="paraType"
								lay-verify="text" autocomplete="off" class="layui-input">
						</div>
					</div>
				</div>
				<div class="layui-form-item">
					<div class="layui-inline">
						<label class="layui-form-label">参数长度:</label>
						<div class="layui-input-inline">
							<input type="text" id="paraSize" name="paraSize"
								lay-verify="text" autocomplete="off" class="layui-input">
						</div>
					</div>
					<div class="layui-inline">
						<label class="layui-form-label">多值分隔符:</label>
						<div class="layui-input-inline">
							<input type="text" id="multiSeparator" name="multiSeparator"
								lay-verify="text" autocomplete="off" class="layui-input">
						</div>
					</div>
				</div>
				<div class="layui-form-item">
					<div class="layui-inline">
						<label class="layui-form-label">是否多值*:</label>
						<div class="layui-input-inline">
							<input id="multiValue" type="checkbox" name="intStatus2"
								lay-skin="switch" lay-filter="switch3" lay-text="true|false"
								value="false">

						</div>
					</div>
					<div class="layui-inline">
						<label class="layui-form-label">是否必须*:</label>
						<div class="layui-input-inline">
							<input id="isMust" type="checkbox" name="intStatus2"
								lay-skin="switch" lay-filter="switch4" lay-text="true|false"
								value="false">

						</div>
					</div>
				</div>
			</form>
			<div class="foot">
				<button id="sure3_btn" class="layui-btn layui-btn sure3_btn">确定</button>
				<button id="cancel3_btn" class="layui-btn layui-btn cancel3_btn">取消</button>
			</div>
		</div>
	</div>
	<div class="display_container5" id="exposed_table3_container">
		<div class="display_content2" style="height: 300px">
			<div class="top" style="color: red;">修改接口</div>
			<form class="layui-form" action="" style="margin-top: 30px;">
				<input id="intUid2" style="display: none;" />
				<div class="layui-form-item">
					<div class="layui-inline">
						<label class="layui-form-label">接口名称:</label>
						<div class="layui-input-inline">
							<input type="text" id="intTitle2" name="intTitle2"
								lay-verify="text" autocomplete="off" class="layui-input"
								value="">
						</div>
					</div>
					<div class="layui-inline">
						<label class="layui-form-label">接口描述:</label>
						<div class="layui-input-inline">
							<input type="text" id="intDescription2" name="intDescription2"
								lay-verify="text" autocomplete="off" class="layui-input"
								value="">
						</div>
					</div>
				</div>
				<div class="layui-form-item">
					<div class="layui-inline">
						<label class="layui-form-label">接口类型:</label>
						<div class="layui-input-inline">
							<select id="intType2" name="intType2">
								<option value="webservice">webservice</option>
								<option value="restapi">restapi</option>
							</select>
						</div>
					</div>
					<div class="layui-inline">
						<label class="layui-form-label">接口地址:</label>
						<div class="layui-input-inline">
							<input type="text" id="intUrl2" name="intUrl2" lay-verify="text"
								autocomplete="off" class="layui-input">
						</div>
					</div>
				</div>
				<div class="layui-form-item">
					<div class="layui-inline">
						<label class="layui-form-label">接口方法名:</label>
						<div class="layui-input-inline">
							<input type="text" id="intCallMethod2" name="intCallMethod2"
								lay-verify="text" autocomplete="off" class="layui-input">
						</div>
					</div>
					<div class="layui-inline">
						<label class="layui-form-label">登录用户名:</label>
						<div class="layui-input-inline">
							<input type="text" id="intLoginUser2" name="intLoginUser2"
								lay-verify="text" autocomplete="off" class="layui-input">
						</div>
					</div>
				</div>
				<div class="layui-form-item">
					<div class="layui-inline">
						<label class="layui-form-label">登陆密码:</label>
						<div class="layui-input-inline">
							<input type="text" id="intLoginPwd2" name="intLoginPwd2"
								lay-verify="text" autocomplete="off" class="layui-input">
						</div>
					</div>
					<div class="layui-inline">
						<label class="layui-form-label">接口状态:</label>
						<div class="layui-input-inline">
							<input id="intStatus2" type="checkbox" name="intStatus2"
								lay-skin="switch" lay-filter="switch2" lay-text="启用|停用"
								value="disabled">
						</div>
					</div>
				</div>
			</form>
			<div class="foot">
				<button id="sure4_btn" class="layui-btn layui-btn sure4_btn">确定</button>
				<button id="cancel4_btn" class="layui-btn layui-btn cancel4_btn">取消</button>
			</div>
		</div>
	</div>
	</div>
</body>
</html>
<script type="text/javascript" src="scripts/js/jquery-3.3.1.js"
	charset="utf-8"></script>
<script type="text/javascript" src="scripts/js/layui.all.js"
	charset="utf-8"></script>
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
			layui.use([ 'layer', 'form' ], function() {
				var form = layui.form, layer = layui.layer, $ = layui.jquery;
				$(".display_container").css("display", "block");

				form.on('switch(switch1)', function(data) {
					var ckd = this.checked ? 'enabled' : 'disabled';
					document.getElementById("intStatus").value = ckd;
				})
			})
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
			// 确定给 当前接口 添加新的参数
			addParames();
		})
		$(".cancel4_btn").click(function() {
			$(".display_container5").css("display", "none");
		})
		$(".sure4_btn").click(function() {
			// 修改 当前接口  interfaces/update
			$.ajax({
				url : 'interfaces/update',
				type : 'POST',
				dataType : 'text',
				data : {
					intTitle : $("#intTitle2").val(),
					intDescription : $("#intDescription2").val(),
					intType : $("#intType2").val(),
					intUrl : $("#intUrl2").val(),
					intCallMethod : $("#intCallMethod2").val(),
					intLoginUser : $("#intLoginUser2").val(),
					intLoginPwd : $("#intLoginPwd2").val(),
					intStatus : $("#intStatus2").val(),
					intUid : $("#intUid2").val()
				},
				success : function(result) {
					window.location.href = "interfaces/index";
				}
			})
		})
	});

	function updateParames() {
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
				success : function(result) {
					window.location.href = "trigger/index";
					layer.alert('修改成功')
				}
			})
		})
	}

	function addParames() {
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
			success : function(result) {
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

	// 查看
	$(".select_btn").click(function() {
		var interfaceName = $("#interfaceName").val();
		var interfaceType = $("#interfaceType").val();
		var interfaceState = $("#interfaceState").val();
		$.ajax({
			url : 'interfaces/queryDhInterfaceByTitle',
			type : 'POST',
			dataType : 'json',
			data : {
				intTitle : interfaceName,
				intType : interfaceType,
				intStatus : interfaceState
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
	
	//
	function flasher(){
		document.flash.inputes1.style.color="blue"
	}
	
	// 请求数据成功
	function drawTable(pageInfo, data) {
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
			trs += '<tr>' 
					+ '<td>' 
					+ meta.intTitle 
					+ '</td>' 
					+ '<td>'
					+ meta.intDescription 
					+ '</td>' 
					+ '<td>' 
					+ meta.intType
					+ '</td>' 
					+ '<td id="requestUrl" style="color:blue" onclick=urls("'
					+ meta.intUrl + '")>' 
					+ meta.intUrl 
					+ '</td>'
					+ '<td>'
					+ meta.intCallMethod
					+ '</td>' 
					+ '<td>' 
					+ meta.intStatus
					+ '</td>' 
					+ '<td>'
					+ '<i class="layui-icon"  title="修改接口"  onclick=updatate("'
					+ meta.intUid + '") >&#xe642;</i>'
					+ '<i class="layui-icon"  title="查看详情"  onclick=info("'
					+ meta.intUid + '")>&#xe60a;</i>'
					+ '<i class="layui-icon"  title="删除接口"  onclick=del("'
					+ meta.intUid + '") >&#xe640;</i>'
					+ '<i class="layui-icon"  title="绑定参数"  onclick=add("'
					+ meta.intUid + '")>&#xe614;</i>' 
					+ '</td>' 
					+ '</tr>'
		}
		$("#proMet_table_tbody").append(trs);

	}
	
	// url 监听事件
	function urls(url){		
		window.open(url);
	}
	
	// 按钮事件
	function info(intUid) {
		// “接口参数详情”按钮
		$("#exposed_table_container").css("display", "block");
		getParamersInfo(intUid);
	}

	function add(intUid) {
		// 绑定参数页面
		layui.use([ 'layer', 'form' ], function() {
			var form = layui.form, layer = layui.layer, $ = layui.jquery;
			form.on('switch(switch3)', function(data) {
				var ckd = this.checked ? 'true' : 'false';
				document.getElementById("multiValue").value = ckd;
			})
			form.on('switch(switch4)', function(data) {
				var ckd2 = this.checked ? 'true' : 'false';
				document.getElementById("isMust").value = ckd2;
			})
			$("#exposed_table2_container").css("display", "block");
			$("#intUid").val(intUid);
		})
	}

	function del(intUid) {
		layer.confirm('是否删除该接口？', {
			btn : [ '确定', '取消' ], //按钮
			shade : false
		//不显示遮罩
		}, function(index) {
			// 提交表单的代码，然后用 layer.close 关闭就可以了，取消可以省略 ajax请求
			$.ajax({
				url : 'interfaces/del',
				type : 'POST',
				dataType : 'text',
				data : {
					intUid : intUid
				},
				success : function(result) {
					// 删除成功后 ajxa跳转 查询controller
					window.location.href = "interfaces/index";
				}
			})
			layer.close(index);
		});
	}

	function updatate(intUid) {
		// 修改接口页面
		layui.use([ 'layer', 'form' ], function() {
			var form = layui.form, layer = layui.layer, $ = layui.jquery;
			form.on('switch(switch2)', function(data) {
				var ckd = this.checked ? 'enabled' : 'disabled';
				document.getElementById("intStatus2").value = ckd;
			})
			$.ajax({
				url : 'interfaces/queryDhInterfaceById',
				type : 'POST',
				dataType : 'json',
				data : {
					intUid : intUid
				},
				success : function(result) {
					$("#exposed_table3_container").css("display", "block");
					$("#intUid2").val(result.intUid);
					$("#intDescription2").val(result.intDescription);
					$("#intType2").val(result.intType);
					$("#intTitle2").val(result.intTitle);
					$("#intUrl2").val(result.intUrl);
					$("#intCallMethod2").val(result.intCallMethod);
					$("#intLoginUser2").val(result.intLoginUser);
					$("#intLoginPwd2").val(result.intLoginPwd);
				}
			})
		})
	}

	function getInterfaceById(intUid) {
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
			trs += '<tr><td><input id="paraUid" type="checkbox" name="eCheck" value="' + meta.paraUid + '" lay-skin="primary">'
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
					+ '</td>' + '<td>' + meta.intUid + '</td>' + '</tr>';
		}
		$("#exposed_table_tbody").append(trs);
	}
</script>