<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<head>
	<base href="<%=basePath%>">
	<meta charset="utf-8">
	<title>来伊份BPM系统</title>
	<meta name="renderer" content="webkit">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
	<link rel="stylesheet" href="resources/desmartportal/css/layui.css" media="all">
	<link rel="stylesheet" href="resources/desmartportal/css/admin.css" media="all">
	<link rel="stylesheet" href="resources/desmartportal/css/my.css" />
	<link rel="stylesheet" href="resources/desmartportal/css/menus/jquery-menus.css" />
	<script type="text/javascript" src="resources/desmartportal/js/jquery-3.3.1.js"></script>
	<script type="text/javascript" src="resources/desmartportal/js/layui.all.js"></script>
	<script type="text/javascript" src="resources/desmartportal/js/common.js"></script>
	<style>
		.username {
			color: #8095a8;
			font-weight: 600;
			height: 40px;
			text-align: center;
		}

		.logout_img {
			width: 20px;
			margin-left: 10px;
			cursor: pointer;
		}
	</style>
</head>

<body>
	<div id="LAY_app">
		<div class="layui-layout layui-layout-admin">
			<div class="layui-header">
				<div class="layui-nav index_top">
					<span class="layui-breadcrumb" style="margin-left: 220px;float: left;">
						<a href="javascript:;">我的任务</a>
						<a href="javascript:;">
							<cite>待办任务</cite>
						</a>
					</span>
					<span class="logout">
						<span style="padding-right:20px;cursor:default;">${userName}</span>
						<span onclick="logout();"><i class="layui-icon">&#xe60e;</i> 注销</span>
					</span>
				</div>
			</div>
			<!-- 侧边菜单 -->
			<div class="layui-side layui-side-menu">
				<div class="layui-side-scroll">
					<div class="layui-side layui-bg-black">
						<div class="layui-side-scroll">
							<!-- 左侧导航区域（可配合layui已有的垂直导航） -->
							<ul class="layui-nav layui-nav-tree" lay-filter="test">
								<li class="logo">
									<img src="resources/desmartportal/images/logo.png" class="big_logo" />
									<img src="resources/desmartportal/images/logo.png" class="mini_logo" />
									<span class="system_title">来伊份BPM系统</span>
								</li>
								<li class="layui-nav-item layui-nav-itemed">
									<a href="javascript:;" id="my_task">
										<i class="layui-icon">&#xe60e;</i>
										<span> 我的任务</span>
									</a>
									<dl class="layui-nav-child">
										<dd id="approvalTask" class="layui-this">
											<a href="menus/backlog" target="iframe0" class="typeColor" onclick="getUserTask();">
												待办任务
												<span class="layui-badge" id="daiban_icon">0</span>
											</a>
										</dd>
										<dd id="finishedTask">
											<a href="menus/finishProcess" target="iframe0" class="typeColor">已办任务</a>
										</dd>
									</dl>
								</li>
								<li class="layui-nav-item">
									<a href="menus/notRedProcess" target="iframe0" class="typeColor">
										<i class="layui-icon">&#xe645;</i>
										<span> 传阅</span>
									</a>
								</li>
								<li class="layui-nav-item">
									<a href="drafts/index" target="iframe0" class="typeColor">
										<i class="layui-icon">&#xe640;</i>
										<span> 草稿箱</span>
									</a>
								</li>
								<li class="layui-nav-item">
									<a href="agent/index" target="iframe0" class="typeColor">
										<i class="layui-icon">&#xe6b2;</i>
										<span> 代理设置</span>
									</a>
								</li>
								<li class="layui-nav-item">
									<a id="shopLife" href="javascript:void(0)" target="iframe0" class="detail_menu1 typeColor">
										<i class="layui-icon">&#xe68e;</i>
										<span> 门店生命周期</span>
									</a>
								</li>
								<li class="layui-nav-item">
									<a href="javascript:void(0)" target="iframe0" class="typeColor">
										<i class="layui-icon">&#xe629;</i>
										<span> 报表管理</span>
									</a>
								</li>
								<!-- <i class="layui-icon kit-side-fold" style="color:#ea6000;margin-left:20px;" title="收缩菜单">&#xe647;</i> -->
							</ul>
						</div>
					</div>
				</div>
			</div>
			<div class="layui-body" id="LAY_app_body" style="margin-bottom: 40px;min-width:1250px;">
				<div class="layadmin-tabsbody-item layui-show">
					<iframe src="menus/backlog" id="frameContent" name="iframe0" frameborder="0" class="layadmin-iframe"></iframe>
					<div class="menu_detail1">
						<input id="userId" value="${userId}" style="display: none;">
						<!--  -->
						<div class="nav">
							<ul class="nav1" style="width:200px;">
								<%--  								<c:forEach items="${listmap}" var="info">
									<li class="li1 haizei" style="position:relative;"><a>${fn:substringAfter(info.key, ",")}
											></a>
										<ul class="nav2">
											<c:forEach items="${info.value}" var="process">
												<li class="li2"><a
													href="menus/processInstanceByUser?proUid=${process.proUid}&proAppId=${process.proAppId}&proName=${process.proName}"
													target="iframe0">${process.proName}</a></li>
												<h1 style="clear: both;"></h1>
											</c:forEach>
										</ul>
									</li>
								</c:forEach> --%>
							</ul>
						</div>
						<div class="tab2">

						</div>
					</div>
				</div>
			</div>
			<div class="layui-footer">
				<div class="footer">Copyright &copy; 来伊份 2018</div>
			</div>
			<!-- 辅助元素，一般用于移动设备下遮罩 -->
			<div class="layadmin-body-shade" layadmin-event="shade"></div>
		</div>
	</div>
	<script>
		$(function () {
			$(".menu_detail1").click(function(){
				$(".layui-nav-item a").not(".detail_menu1").not(
				".detail_menu2").each(function () {
				$(".menu_detail1").css("display", "none");
				$(".menu_detail2").css("display", "none");
			});
			if($(".nav").is(":hidden")){
				$(".layui-nav.layui-nav-tree").find(".layui-this").find("a").css("background-color", "#1890ff").css("color","#fff");
				var id = $(".layui-nav.layui-nav-tree").find(".layui-this").find("a").attr("id");
				if(id!="shopLife"){
					$("#shopLife").css("background-color", "#001529").css("color","#B3B9BF");
				}
			}
			});			
			layui.use('element', function () {
				var element = layui.element;
				
				element.render();
			});
			
			getUserTask();
			// 定时 去查询我的 代办任务
			window.setInterval(getUserTask, 30000);

			$(".layui-nav-item a").not("#shopLife").mouseover(
				function () {
					$(".layui-nav-item a").not(".detail_menu1").not(
						".detail_menu2").each(function () {
						$(".menu_detail1").css("display", "none");
						$(".menu_detail2").css("display", "none");
					});
					if($(".nav").is(":hidden")){
						$(".layui-nav.layui-nav-tree").find(".layui-this").find("a").css("background-color", "#1890ff").css("color","#fff");
						var id = $(".layui-nav.layui-nav-tree").find(".layui-this").find("a").attr("id");
						if(id!="shopLife"){
							$("#shopLife").css("background-color", "#001529").css("color","#B3B9BF");
						}
					}
				});
			$(".detail_menu1").mouseover(function () {
				$(".menu_detail1").css("display", "block");
				$(".menu_detail2").css("display", "none");
			})
			$(".menu_detail1").click(function () {
				$(this).css("display", "none");
			})
			$(".detail_menu2").mouseover(function () {
				$(".menu_detail2").css("display", "block");
				$(".menu_detail1").css("display", "none");
			})
			$(".menu_detail2").click(function () {
				$(this).css("display", "none");
			})
		})

		var isShow = true; //定义一个标志位
		$('.kit-side-fold').click(function () {
			//选择出所有的span，并判断是不是hidden
			$('.layui-nav-item span').each(function () {
				if ($(this).is(':hidden')) {
					$(this).show();
				} else {
					$(this).hide();
				}
			});
			//判断isshow的状态
			if (isShow) {
				$('.layui-side').animate({
					width: "60px"
				}); //设置宽度
				$('.kit-side-fold i').css('margin-right', '70%'); //修改图标的位置
				$('.layui-body').animate({
					left: "60px"
				}, function () {
					$(".mini_logo").show();
				});
				$('.layui-footer').animate({
					left: "60px"
				}, function () {
					$(".mini_logo").show();
				});
				//将二级导航栏隐藏
				$('dd span').each(function () {
					$(this).hide();
				});
				//修改标志位
				isShow = false;
			} else {
				$('.layui-side').animate({
					width: "220px"
				}, function () {});
				$('.kit-side-fold i').css('margin-right', '10%');
				$(".mini_logo").hide();

				$('.layui-body').animate({
					left: "220px"
				});
				$('.layui-footer').animate({
					left: "220px"
				});
				$('dd span').each(function () {
					$(this).show();
				});
				isShow = true;
			}
		});

		// 获取用户有多少代办
		function getUserTask() {
			var uId = document.getElementById('userId').value;
			$.ajax({
				url:common.getPath() + '/user/todoTask',
				type: 'POST',
				dataType: 'text',
				data: {
					userId: uId
				},
				success: function (result) {
					// 渲染到待办
					if (result == 0) {
						$("#daiban_icon").css("display", "none");
						var daibanObj = document.getElementById("frameContent").contentDocument.getElementById("daiban_icon");
						$(daibanObj).text(result);
					} else if (result > 0) {
						$("#daiban_icon").text(result);
						var daibanObj = document.getElementById("frameContent").contentDocument.getElementById("daiban_icon");
						$(daibanObj).text(result);
					} else {
						layer.msg("登陆超时！请重新登陆！", function () {
							window.location.reload();
						});
						/* layer.alert("登陆超时！请重新登陆！",function(){
							window.location.reload();
						}); */
					}
				}
			})
		}
		
		//注销
		function logout(){
			window.location.href = common.getPath()+"/user/logout";
		}
	</script>
</body>
</html>
<script type="text/javascript">
	$(function () {
		// 门店生命周期鼠标悬停事件  加颜色
		$("#shopLife").hover(
			function () {
				$(".typeColor").css("background-color", "#001529").css("color","#B3B9BF");
				$("#shopLife").css("background-color", "#1890ff").css("color","#fff");
			},function(){
				
			}
		);
		$(".typeColor").click(function () {
			if($(this).parent().prop("id")=="approvalTask" || $(this).parent().prop("id")=="finishedTask"){
				var text = $(this)[0].firstChild.data.trim();
				$(".layui-breadcrumb").find("a:eq(0)").text("我的任务");
				$(".layui-breadcrumb").find("cite").text(text);
				$(".layui-breadcrumb").css("display","block");
			}else{
				$(".layui-breadcrumb").css("display","none");
			}
			$(".typeColor").not(this).css("background-color", "#001529").css("color","#B3B9BF");
			$("#shopLife").css("background-color", "#001529");
			$(this).css("background-color", "#1890ff").css("color","#fff");
		})

		function color() {
			alert("a")
			$("#shopLife").css("background-color", "#001529");
		}

		queryByParent('rootCategory');

		$(".nav1 .oneCategory").hover( //为li绑定了鼠标进入和鼠标移开的两个参数
			function () {
				$(".nav1 .oneCategory").not(this).children("ul").hide();
				// 鼠标悬停 查询数据
				var categoryuid = $(this).data("categoryuid");
				var child_ul = $(this).children("ul");
				if (child_ul.length <= 0) {
					var categoryuid = $(this).data("categoryuid");
					queryPorcess(categoryuid);
				}
				$(this).children("ul").show();
				// 修改背景色
				$(this).css("background-color", "#1890ff");
				$("#shopLife").css("background-color", "#1890ff");
			},
			function () {
				$(this).css("background-color", "");
			}
		);

		$(".nav1 .nav2").hover(
			function () {
				$("a").css("background-color", "#1890ff");
			}
		);

	});

	function overViewProcess(a) {
		//$(a).parent().parent().children("li").not($(a).parent()).hide();
		// 鼠标悬停 查询数据
		var categoryuid = $(a).data("categoryuid");
		var child_ul = $(a).children("ul");
		if (child_ul.length <= 0) {
			queryPorcess(categoryuid);
		}
		$(a).children("ul").show();
	}

	function outViewProcess(a) {
		$(a).children("ul").hide();
	}

	function queryByParent(categoryuid) {
		$.ajax({
			url: common.getPath() +'/processCategory/queryByParent',
			type: 'post',
			dataType: 'json',
			async: false,
			data: {
				categoryParent: categoryuid
			},
			success: function (result) {
				var list = result
				console.info(list)
				for (var i = 0; i < list.length; i++) {
					var id = list[i].categoryUid.split(":")[1];
					var trs = '<li class="oneCategory" id="' + id + '" data-categoryuid="' + list[i].categoryUid +
						'"><a style="width: 200px;">' +
						list[i].categoryName + '>' + '</a></li>';
					$(".nav1").append(trs)
				}
			},
			error: function (result) {
				layer.alert("查询门店生命周期失败")
			}
		})
	};

	function queryNextGategory(categoryuid) {
		$.ajax({
			url: common.getPath() +'/processCategory/queryByParent',
			type: 'post',
			dataType: 'json',
			async: false,
			data: {
				categoryParent: categoryuid
			},
			success: function (result) {
				var list = result
				for (var i = 0; i < list.length; i++) {
					var id = list[i].categoryUid.split(":")[1];
					trs += '<li onmouseover="overViewProcess(this);" onmouseout="outViewProcess(this);" id="' + id +
						'" data-categoryuid="' + list[i].categoryUid +
						'"><a style="width: 200px;">' + list[i].categoryName + '>' +
						'</a></li>';
				}
			},
			error: function (result) {
				layer.alert("查询门店生命周期失败")
			}
		})
	};
	var trs = "";

	function queryPorcess(categoryuid) {
		$.ajax({
			url: common.getPath() +'/processMeta/searchByCategoryUid',
			type: 'post',
			dataType: 'json',
			async: false,
			data: {
				categoryUid: categoryuid
			},
			success: function (result) {
				var list = result.data;
				console.info(list)
				var id = categoryuid.split(":")[1];
				var selective = "#" + id;
				//$(selective).parent().find("li").not(selective).find("ul").remove();
				trs = '<ul class="nav2" style="width: 200px;">';
				queryNextGategory(categoryuid);
				for (var i = 0; i < list.length; i++) {
					trs += '<li class="li2"><a style="width: 200px;" href="" target="iframe0">' +
						list[i].proName + '</a></li>' +
						'<h1 style="clear: both;"></h1>';
				}
				trs += '</ul>';
				$(selective).append(trs)
			},
			error: function (result) {
				layer.alert("查询门店生命周期失败")
			}
		})
	};
	var trs = "";

	function queryPorcess(categoryuid) {

		$.ajax({
			url: common.getPath() +'/processMeta/searchByCategoryUid',
			type: 'post',
			dataType: 'json',
			async: false,
			data: {
				categoryUid: categoryuid
			},
			success: function (result) {
				var list = result.data;
				console.info(list)
				var id = categoryuid.split(":")[1];
				var selective = "#" + id;
				trs = '<ul class="nav2" style="width: 200px;">';
				queryNextGategory(categoryuid);
				for (var i = 0; i < list.length; i++) {
					trs += '<li class="li2"><a style="width: 200px;" onclick="hideHead();" href="menus/processInstanceByUser?proUid=' +
						list[i].proUid +
						'&proAppId=' +
						list[i].proAppId  +
						'" target="iframe0">' +
						list[i].proName +
						'</a></li>' +
						'<h1 style="clear: both;"></h1>';
				}
				trs += '</ul>';
				$(selective).append(trs);
			},
			error: function (result) {}
		})
	}
	
	function hideHead(){
		$(".layui-nav.layui-nav-tree").find(".layui-this").removeClass("layui-this");
		$("#shopLife").parent().addClass("layui-this");
		$(".layui-breadcrumb").css("display","none");
	}
	
	function openProView(insId){
		$.ajax({
	        url: common.getPath() +'/processInstance/viewProcess',
	        type: 'post',
	        dataType: 'text',
	        data: {
	            insId: insId
	        },
	        success: function (result) {
	            var index = layer.open({
	                type: 2,
	                title: '流程图',
	                shadeClose: true,
	                offset: ['50px', '20%'],
	                shade: 0.3,
	                maxmin:true,
	                area: ['890px', '570px'],
	                content: result
	            });
	            layer.style(index, {
	            	zoom:1.1
	            });
	        }
	    });
	}
	
	// 利用 CSS3 旋转 对根容器逆时针旋转 90 度
	/* var detectOrient = function() {
	  var width = window.screen.width/0.9,
	      height =  window.screen.height/0.9,
	      $wrapper =  document.getElementById("LAY_app"),
	      style = "";

	  if( width >= height ){ // 横屏
	      style += "width:" + width + "px;";  // 注意旋转后的宽高切换
	      style += "height:" + height + "px;";
	      style += "-webkit-transform: rotate(0); transform: rotate(0);";
	      style += "-webkit-transform-origin: 0 0;";
	      style += "transform-origin: 0 0;";
	  }
	  else{ // 竖屏
	      style += "width:" + height + "px;";
	      style += "height:" + width + "px;";
	      style += "-webkit-transform: rotate(90deg); transform: rotate(90deg);";
	      // 注意旋转中点的处理
	      style += "-webkit-transform-origin: " + width / 2 + "px " + width / 2 + "px;";
	      style += "transform-origin: " + width / 2 + "px " + width / 2 + "px;";
	  }
	  $wrapper.style.cssText = style;
	}
	window.onresize = detectOrient;
	detectOrient(); 
	
	/* var evt = "onorientationchange" in window ? "orientationchange" : "resize";
    window.addEventListener(evt,resize,false);
    function resize(fals) {
	    if(window.orientation == 0 || window.orientation == 180) {
	    	console.log("竖屏");
	    	
	    }else {
	    	console.log("横屏");
	    }
    }
    resize(true);*/
</script>
