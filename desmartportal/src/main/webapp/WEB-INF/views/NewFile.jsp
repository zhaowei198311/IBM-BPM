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
  <title>来伊份BPM系统</title>
  <meta name="renderer" content="webkit">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
<link rel="stylesheet" href="resources/css/layui.css" media="all">
<link rel="stylesheet" href="resources/css/admin.css" media="all">
<link rel="stylesheet" href="resources/css/my.css" />
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
								<li class="logo"><img src="resources/images/logo.png" class="big_logo"/>
								<img src="resources/images/logo.png" class="mini_logo"/></li>
								<li style="text-align: center; margin-bottom: 20px;"><span
									class="system_title">BPM系统</span></li>
								<li class="layui-nav-item layui-this"><a
									href="test/backlog" target="iframe0"><i class="layui-icon">&#xe60e;</i><span>
											待办任务</span> </a></li>
								<li class="layui-nav-item"><a href=""
									target="iframe0"><i class="layui-icon">&#xe645;</i><span>
											未阅通知</span></a></li>
								<li class="layui-nav-item"><a href=""
									target="iframe0"><i class="layui-icon">&#x1005;</i><span>
											已办任务</span></a></li>
								<!--<li class="layui-nav-item"><a href="read.html" target="iframe0"><i class="layui-icon">&#xe615;</i><span> 通知查询</span></a></li>
							 	<li class="layui-nav-item"><a href="draft.html" target="iframe0"><i class="layui-icon">&#xe624;</i><span>草稿箱</span></a></li>
							 	<li class="layui-nav-item"><a href="new_tail.html" target="iframe0"><i class="layui-icon">&#xe609;</i> <span> 发起跟踪</span></a></li>-->
								<li class="layui-nav-item"><a href=""
									target="iframe0"><i class="layui-icon">&#xe6b2;</i><span>
											委托设置</span></a></li>
								<li class="layui-nav-item"><a href=""
									target="iframe0" class="detail_menu1"><i class="layui-icon">&#xe68e;</i><span>
											门店生命周期</span></a></li>
								<li class="layui-nav-item"><a href=""
									target="iframe0"><i class="layui-icon">&#xe629;</i> <span>
											加盟管理</span></a></li>
								<li class="layui-nav-item"><a href=""
									target="iframe0"><i class="layui-icon">&#xe62c;</i> <span>
											主数据管理</span></a></li>
								<!--<i class="layui-icon kit-side-fold" style="color:#ea6000;margin-left:20px;" title="收缩菜单">&#xe647;</i>-->
							</ul>
						</div>
					</div>
				</div>
			</div>
			<div class="layui-body" id="LAY_app_body"
				style="margin-bottom: 40px;">
				<div class="layadmin-tabsbody-item layui-show">
					<iframe src="test/backlog" name="iframe0" frameborder="0"
						class="layadmin-iframe"></iframe>
					<div class="menu_detail1">
						<div class="menu_container">
							<div class="menu_title">
								新开店流程 <i class="layui-icon"
									style="font-size: 14px; cursor: default;">&#xe602;</i>
							</div>
							<div class="menu_detail">
								<ul>
									<li><a href="" target="iframe0">目标门店调查表</a></li>
									<li><a href="" target="iframe0">合同用印</a></li>
									<h1 style="clear: both;"></h1>
								</ul>
							</div>
						</div>
						<div class="menu_container">
							<div class="menu_title">
								续租流程 <i class="layui-icon"
									style="font-size: 14px; cursor: default;">&#xe602;</i>
							</div>
							<div class="menu_detail">
								<ul>
									<li><a href="" target="iframe0">门店续租申请表</a></li>
									<li><a href="" target="iframe0">门店续租相关事项变动确认流程</a></li>
									<h1 style="clear: both;"></h1>
								</ul>
							</div>
						</div>
						<div class="menu_container">
							<div class="menu_title">
								老翻新流程 <i class="layui-icon"
									style="font-size: 14px; cursor: default;">&#xe602;</i>
							</div>
							<div class="menu_detail">
								<ul>
									<li><a href="" target="iframe0">门店整体翻新流程</a></li>
									<li><a href="" target="iframe0">门店部分翻新流程</a></li>
									<h1 style="clear: both;"></h1>
								</ul>
							</div>
						</div>
						<div class="menu_container">
							<div class="menu_title">
								门店暂闭流程 <i class="layui-icon"
									style="font-size: 14px; cursor: default;">&#xe602;</i>
							</div>
							<div class="menu_detail">
								<ul>
									<li><a href="" target="iframe0">门店暂闭申请表</a></li>
									<li><a href="" target="iframe0">门店被动暂闭和快速暂闭申请流程</a></li>
									<li><a href="" target="iframe0">门店暂闭重开申请表</a></li>
									<h1 style="clear: both;"></h1>
								</ul>
							</div>
						</div>
						<div class="menu_container">
							<div class="menu_title">
								拆店流程 <i class="layui-icon"
									style="font-size: 14px; cursor: default;">&#xe602;</i>
							</div>
							<div class="menu_detail">
								<ul>
									<li><a href="" target="iframe0">门店拆店申请表</a></li>
									<li><a href="" target="iframe0">拆店信息移交及相关事项确认流程</a></li>
									<h1 style="clear: both;"></h1>
								</ul>
							</div>
						</div>
						<div class="menu_container">
							<div class="menu_title">
								门店续租转让流程 <i class="layui-icon"
									style="font-size: 14px; cursor: default;">&#xe602;</i>
							</div>
							<div class="menu_detail">
								<ul>
									<li><a href="" target="iframe0">门店转租转让申请表</a></li>
									<h1 style="clear: both;"></h1>
								</ul>
							</div>
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
</body>
</html>
<script type="text/javascript" src="resources/js/jquery-3.3.1.js"></script>
<script type="text/javascript" src="resources/js/layui.all.js"></script>
<script>
		$(function(){
			$(".layui-nav-item a").mouseover(function(){
		        $(".layui-nav-item a").not(".detail_menu1").not(".detail_menu2").each(function() { 
		            $(".menu_detail1").css("display","none");
					$(".menu_detail2").css("display","none");
		        });
		   });			
			$(".detail_menu1").mouseover(function(){
				$(".menu_detail1").css("display","block");
				$(".menu_detail2").css("display","none");
			})
			$(".menu_detail1").click(function(){
				$(this).css("display","none");
			})
			$(".detail_menu2").mouseover(function(){
				$(".menu_detail2").css("display","block");
				$(".menu_detail1").css("display","none");
			})
			$(".menu_detail2").click(function(){
				$(this).css("display","none");
			})
		})

		 layui.use('element', function(){
        var element = layui.element;
   	});
    var isShow = true;  //定义一个标志位
    $('.kit-side-fold').click(function(){
        //选择出所有的span，并判断是不是hidden
        $('.layui-nav-item span').each(function(){
            if($(this).is(':hidden')){
                $(this).show();
            }else{
                $(this).hide();
            }
        });
        //判断isshow的状态
        if(isShow){
            $('.layui-side').animate({ 
				    width: "60px"						   
			  }); //设置宽度
            $('.kit-side-fold i').css('margin-right', '70%');  //修改图标的位置
						$('.layui-body').animate({ 
				    left: "60px"
					  }, function(){
					  		$(".mini_logo").show();
					  });
					  $('.layui-footer').animate({ 
				    left: "60px"
					  }, function(){
					  		$(".mini_logo").show();
					  });
            //将二级导航栏隐藏
            $('dd span').each(function(){
                $(this).hide();
            });
            //修改标志位
            isShow =false;
       }else{    
       			$('.layui-side').animate({ 
						    width: "220px"						   
					  },function(){
					  });
          	$('.kit-side-fold i').css('margin-right', '10%');
	            $(".mini_logo").hide();
	            
					 	 	$('.layui-body').animate({ 
						    left: "220px"	
					 	 	});					 	 	
					 	 	$('.layui-footer').animate({ 
						    left: "220px"	
					 	 	});
	            $('dd span').each(function(){
	                $(this).show();
	            });             
            isShow =true;
        }
    });
</script>