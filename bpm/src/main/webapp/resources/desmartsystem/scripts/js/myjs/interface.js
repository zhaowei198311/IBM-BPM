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
			if ($("#form2").valid()) {
				addParames();
			}
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

		$(".cancel5_btn").click(function() {
			$(".display_container6").css("display", "none");
		})

		// 修改接口参数配置
		$(".sure5_btn").click(function() {
			$.ajax({
				url : 'interfaceParamers/update',
				type : 'POST',
				dataType : 'text',
				data : {
					paraUid : $("#paraUid3").val(),
					paraIndex : $("#paraIndex3").val(),
					paraName : $("#paraName3").val(),
					paraDescription : $("#paraDescription3").val(),
					paraType : $("#paraType3").val(),
					paraSize : $("#paraSize3").val(),
					multiSeparator : $("#multiSeparator3").val(),
					multiValue : $("#multiValue3").val(),
					isMust : $("#isMust3").val()
				},
				success : function(result){
					layer.alert('修改参数成功')
					window.location.href = "interfaces/index";
				}
			})
		})

		// 根据id 修改接口参数页面
		$(".update2_btn").click(function() {
			layui.use([ 'layer', 'form' ], function() {
				var form = layui.form, layer = layui.layer, $ = layui.jquery;
				form.on('switch(switch3)', function(data) {
					var ckd = this.checked ? 'true' : 'false';
					document.getElementById("multiValue3").value = ckd;
				})
				form.on('switch(switch4)', function(data) {
					var ckd = this.checked ? 'true' : 'false';
					document.getElementById("isMust3").value = ckd;
				})

				$("input[name='eCheck']:checked").each(function() {
					var cks = $("[name='eCheck']:checked")
					if (cks.length < 1) {
						layer.alert("请选择一个接口参数");
						return;
					}
					if (cks.length > 1) {
						layer.alert("请选择一个接口参数，不能选择多个");
						return;
					}

					$.ajax({
						url : 'interfaceParamers/queryByparaId',
						type : 'POST',
						dataType : 'json',
						data : {
							paraUid : this.value
						},
						success : function(result) {
							$(".display_container6").css("display", "block");
							$("#paraUid3").val(result.paraUid);
							$("#paraIndex3").val(result.paraIndex);
							$("#paraName3").val(result.paraName);
							$("#paraDescription3").val(result.paraDescription);
							$("#paraType3").val(result.paraType);
							$("#paraSize3").val(result.paraSize);
							$("#multiSeparator3").val(result.multiSeparator);
							if(result.multiValue=="true"){
								document.getElementById('multiValue3').checked  = true;
							}else{
								document.getElementById('multiValue3').checked  = false;
							}
							if(result.isMust=="true"){
								document.getElementById('isMust3').checked  = true;
							}else{
								document.getElementById('isMust3').checked  = false;
							}
							$("#intUid3").val(result.intUid);
							form.render();
						}
					})
				})
			})
		})

		// 多表单验证
		$("#form1").validate({
			rules : {
				intTitle : {
					required : true
				},
				intType : {
					required : true
				},
				intUrl : {
					required : true
				},
				intStatus : {
					required : true
				}
			}
		});

		$("#form2").validate({
			rules : {
				paraIndex : {
					required : true
				},
				paraName : {
					required : true
				},
				paraType : {
					required : true
				}
			}
		});

	})

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
		if ($("#form1").valid()) {
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
		}
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
	function flasher() {
		document.flash.inputes1.style.color = "blue"
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
			var sortNum = startSort + i;
			var status = "";
			if(meta.intStatus == "enabled"){
				status = "启用";
			}else{
				status = "停用"
			}
			trs += '<tr>' + '<td>' + sortNum + '</td>' + '<td>' + meta.intTitle
					+ '</td>' + '<td>' + meta.intDescription + '</td>' + '<td>'
					+ meta.intType + '</td>'
					+ '<td id="requestUrl" style="color:blue" onclick=urls("'
					+ meta.intUrl + '")>' + meta.intUrl + '</td>' + '<td>'
					+ meta.intCallMethod + '</td>' + '<td>'+status+'</td>' + '<td>'
					+ '<i class="layui-icon"  title="修改接口"  onclick=updatate("'
					+ meta.intUid + '") >&#xe642;</i>'
					+ '<i class="layui-icon"  title="查看详情"  onclick=info("'
					+ meta.intUid + '")>&#xe60a;</i>'
					+ '<i class="layui-icon"  title="删除接口"  onclick=del("'
					+ meta.intUid + '") >&#xe640;</i>'
					+ '<i class="layui-icon"  title="绑定参数"  onclick=add("'
					+ meta.intUid + '")>&#xe614;</i>' + '</td>' + '</tr>'
		}
		$("#proMet_table_tbody").append(trs);

	}

	// url 监听事件
	function urls(url) {
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
					if(result.intStatus=="enabled"){
						document.getElementById('intStatus2').checked  = true;
					}else{
						document.getElementById('intStatus2').checked  = false;
					}
					$("#intType2").val(result.intType);
					$("#intTitle2").val(result.intTitle);
					$("#intUrl2").val(result.intUrl);
					$("#intCallMethod2").val(result.intCallMethod);
					$("#intLoginUser2").val(result.intLoginUser);
					$("#intLoginPwd2").val(result.intLoginPwd);
					form.render();
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
					+ sortNum
					+ '</td>'
					+ '<td>'
					+ meta.paraName
					+ '</td>'
					+ '<td>'
					+ meta.paraDescription
					+ '</td>'
					+ '<td>'
					+ meta.paraType
					+ '</td>'
					+ '<td>'
					+ meta.paraSize
					+ '</td>'
					+ '<td>'
					+ meta.multiSeparator
					+ '</td>'
					+ '<td>'
					+ meta.multiValue
					+ '</td>'
					+ '<td>'
					+ meta.isMust
					+ '</td>' + '</tr>';
		}
		$("#exposed_table_tbody").append(trs);
	}