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
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
<link rel="stylesheet" href="resources/desmartportal/css/layui.css"
	media="all">
<link rel="stylesheet" href="resources/desmartportal/css/admin.css"
	media="all">
<link rel="stylesheet"
	href="resources/desmartportal/css/menus/jquery-menus.css" />
<link rel="stylesheet" href="resources/desmartportal/css/my.css" />
<link rel="stylesheet" href="https://unpkg.com/element-ui/lib/theme-chalk/index.css">
</head>
<body class="layui-layout-body">

	<div id="LAY_app">
		<div class="layui-layout layui-layout-admin">
			<!--<div class="layui-header">
        <div class="layui-nav layui-layout-left index_top">
					
					
					<span class="logout"><i class="layui-icon">&#xe60e;</i> 退出</span>
				</div>
      </div>-->

			<!-- 侧边菜单 -->
			<div class="layui-side layui-side-menu">
				<div class="layui-side-scroll">
					<div class="layui-side layui-bg-black">
						<div class="layui-side-scroll">
							<!-- 左侧导航区域（可配合layui已有的垂直导航） -->
							<ul class="layui-nav layui-nav-tree" lay-filter="test">
								<li class="logo"><img
									src="resources/desmartportal/images/logo.png" class="big_logo" /><img
									src="resources/desmartportal/images/logo.png" class="mini_logo" /></li>
								<li style="text-align: center; margin-bottom: 20px;"><span
									class="system_title">BPM系统</span></li>
								<li class="layui-nav-item layui-this"><a
									href="menus/backlog" target="iframe0"><i class="layui-icon"
										style="left: 40px">&#xe60e;</i><span> 待办任务<span
											class="layui-badge" id="daiban_icon"></span></span> </a></li>
								<li class="layui-nav-item"><a href="menus/notRedProcess"
									target="iframe0"><i class="layui-icon" style="left: 40px">&#xe645;</i><span>
											抄送</span></a></li>
								<li class="layui-nav-item"><a href="menus/finishProcess"
									target="iframe0"><i class="layui-icon" style="left: 40px">&#x1005;</i><span>
											已办任务</span></a></li>
								<!--<li class="layui-nav-item"><a href="read.html" target="iframe0"><i class="layui-icon">&#xe615;</i><span> 通知查询</span></a></li>
							 	<li class="layui-nav-item"><a href="draft.html" target="iframe0"><i class="layui-icon">&#xe624;</i><span>草稿箱</span></a></li>
							 	<li class="layui-nav-item"><a href="new_tail.html" target="iframe0"><i class="layui-icon">&#xe609;</i> <span> 发起跟踪</span></a></li>-->
								<li class="layui-nav-item"><a href="agent/index"
									target="iframe0"><i class="layui-icon" style="left: 40px">&#xe6b2;</i><span>
											代理设置</span></a></li>
								<li class="layui-nav-item"><a href="javascript:void(0)"
									target="iframe0" class="detail_menu1"><i class="layui-icon"
										style="left: 40px">&#xe68e;</i><span> 门店生命周期</span></a></li>
								<li class="layui-nav-item"><a href="drafts/index"
									target="iframe0"><i class="layui-icon" style="left: 40px">&#xe640;</i>
										<span> 草稿箱</span></a></li>
								<li class="layui-nav-item"><a href="javascript:void(0)"
									target="iframe0"><i class="layui-icon" style="left: 40px">&#xe629;</i>
										<span> 报表</span></a></li>
								<!--<i class="layui-icon kit-side-fold" style="color:#ea6000;margin-left:20px;" title="收缩菜单">&#xe647;</i>-->
							</ul>
						</div>
					</div>
				</div>
			</div>
			<div class="layui-body" id="LAY_app_body"
				style="margin-bottom: 40px;">
				<div class="layadmin-tabsbody-item layui-show">
					<iframe src="menus/backlog" name="iframe0" frameborder="0"
						class="layadmin-iframe"></iframe>
					<div class="menu_detail1">
						<input id="userId" value="${userId}" style="display: none;">
						<div class="nav" style="margin-top: 50%">
							<ul class="nav1">
<%--  								<c:forEach items="${listmap}" var="info">
									<li class="li1 haizei"><a>${fn:substringAfter(info.key, ",")}
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
	<script type="text/javascript"
		src="resources/desmartportal/js/jquery-3.3.1.js"></script>
	<script type="text/javascript"
		src="resources/desmartportal/js/layui.all.js"></script>
	<script>
		$(function() {

			getUserTask();
			// 定时 去查询我的 代办任务
			window.setInterval(getUserTask, 30000);

			$(".layui-nav-item a").mouseover(
					function() {
						$(".layui-nav-item a").not(".detail_menu1").not(
								".detail_menu2").each(function() {
							$(".menu_detail1").css("display", "none");
							$(".menu_detail2").css("display", "none");
						});
					});
			$(".detail_menu1").mouseover(function() {
				$(".menu_detail1").css("display", "block");
				$(".menu_detail2").css("display", "none");
			})
			$(".menu_detail1").click(function() {
				$(this).css("display", "none");
			})
			$(".detail_menu2").mouseover(function() {
				$(".menu_detail2").css("display", "block");
				$(".menu_detail1").css("display", "none");
			})
			$(".menu_detail2").click(function() {
				$(this).css("display", "none");
			})
		})

		layui.use('element', function() {
			var element = layui.element;
		});
		var isShow = true; //定义一个标志位
		$('.kit-side-fold').click(function() {
			//选择出所有的span，并判断是不是hidden
			$('.layui-nav-item span').each(function() {
				if ($(this).is(':hidden')) {
					$(this).show();
				} else {
					$(this).hide();
				}
			});
			//判断isshow的状态
			if (isShow) {
				$('.layui-side').animate({
					width : "60px"
				}); //设置宽度
				$('.kit-side-fold i').css('margin-right', '70%'); //修改图标的位置
				$('.layui-body').animate({
					left : "60px"
				}, function() {
					$(".mini_logo").show();
				});
				$('.layui-footer').animate({
					left : "60px"
				}, function() {
					$(".mini_logo").show();
				});
				//将二级导航栏隐藏
				$('dd span').each(function() {
					$(this).hide();
				});
				//修改标志位
				isShow = false;
			} else {
				$('.layui-side').animate({
					width : "220px"
				}, function() {
				});
				$('.kit-side-fold i').css('margin-right', '10%');
				$(".mini_logo").hide();

				$('.layui-body').animate({
					left : "220px"
				});
				$('.layui-footer').animate({
					left : "220px"
				});
				$('dd span').each(function() {
					$(this).show();
				});
				isShow = true;
			}
		});

		// 获取用户有多少代办
		function getUserTask() {
			var uId = document.getElementById('userId').value;
			$.ajax({
				url : 'user/todoTask',
				type : 'POST',
				dataType : 'text',
				data : {
					userId : uId
				},
				success : function(result) {
					// 渲染到待办
					if (result == 0) {
						$("#daiban_icon").css("display", "none");
					} else {
						$("#daiban_icon").text(result);
					}
				}
			})
		}
	</script>
</body>
</html>

<script type="text/javascript" src="resources/desmartportal/js/jquery-3.3.1.js" charset="utf-8"></script>
<script type="text/javascript" src="resources/desmartportal/js/layui.all.js" charset="utf-8"></script>
<script type="text/javascript" src="resources/desmartportal/js/common.js"></script>
<script src="https://unpkg.com/element-ui/lib/index.js"></script>
<script type="text/javascript" >	
$(function(){ 
	
	queryByParent('rootCategory');
	
	$(".nav1 .oneCategory").hover(//为li绑定了鼠标进入和鼠标移开的两个参数
			  function() {
				   $(".nav").find("ul").not(".nav1").remove();
				  // 鼠标悬停 查询数据
				  var categoryuid = $(this).data("categoryuid");
				  queryPorcess(categoryuid);
			  }, function() {
				  
			  }
			);

});

function queryByParent(categoryuid){
	$.ajax({
		url : 'processCategory/queryByParent',
		type : 'post',
		dataType : 'json',
		async:false, 
		data : {
			categoryParent : categoryuid
		},
		success : function(result){
			var list = result
			console.info(list)
			for (var i = 0; i < list.length; i++) {
				var id = list[i].categoryUid.split(":")[1];
				var trs = '<li class="oneCategory" id="'+id+'" data-categoryuid="'+list[i].categoryUid+'"><a class="viewProcess">'
						+ list[i].categoryName
						+ '>'
						+ '</a></li>';
				$(".nav1").append(trs)
			}
		},
		error : function(result){
			layer.alert("查询门店生命周期失败")
		}
	})
};

function queryNextGategory(categoryuid){
	$.ajax({
		url : 'processCategory/queryByParent',
		type : 'post',
		dataType : 'json',
		async:false, 
		data : {
			categoryParent : categoryuid
		},
		success : function(result){
			var list = result
			for (var i = 0; i < list.length; i++) {
				var id = list[i].categoryUid.split(":")[1];
				trs += '<li class="oneCategory" id="'+id+'" data-categoryuid="'+list[i].categoryUid+'"><a class="viewProcess">'
						+ list[i].categoryName
						+ '>'
						+ '</a></li>';
			}
		},
		error : function(result){
			layer.alert("查询门店生命周期失败")
		}
	})
};
var trs = "";
function queryPorcess(categoryuid){
	$.ajax({
		url : 'processMeta/searchByCategoryUid',
		type : 'post',
		dataType : 'json',
		async:false, 
		data : {
			categoryUid : categoryuid
		},
		success : function(result){
			var list = result.data;
			console.info(list)
			var id = categoryuid.split(":")[1];
			var selective = "#"+id;
			//$(selective).parent().find("li").not(selective).find("ul").remove();
			trs = '<ul class="nav2" style="overflow:yes;">';
			queryNextGategory(categoryuid);
			for (var i = 0; i < list.length; i++) {
				 trs += '<li class="li2"><a href="menus/processInstanceByUser?proUid='+list[i].proUid+'&proAppId='+list[i].proAppId+'&proName='+list[i].proAppId+'" target="iframe0">'
						+ list[i].proName
						+ '</a></li>'
						+ '<h1 style="clear: both;"></h1>';
			}
			trs+='</ul>';
				$(selective).append(trs)
			$(".nav2").show();
		},
		error : function(result){
		}
	})
}

</script>
