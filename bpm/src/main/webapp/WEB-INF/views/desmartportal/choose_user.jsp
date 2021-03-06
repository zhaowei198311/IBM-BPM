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
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
  		<title>待办任务</title>
  		<link href="css/layui.css" rel="stylesheet"/>
  		<link rel="stylesheet" href="resources/desmartportal/css/modules/laydate/default/laydate.css" />
  		<link rel="stylesheet" href="resources/desmartportal/tree/css/demo.css" type="text/css">
		<link rel="stylesheet" href="resources/desmartportal/tree/css/zTreeStyle/zTreeStyle.css" type="text/css">
  		<link rel="stylesheet" href="resources/desmartportal/css/my.css" />
  		<style>
  			.ztree li span.button.add {
			    margin-right: 2px;
			    background-position: -143px 0px;
			    vertical-align: top;
			}
			ul.ztree{
				border:0;
			}
			.display_container3 .layui-laypage select{
				display:none;
			}
			.layui-form-label{width:100px;}
			.layui-input-block{margin-left:130px;}
			.choose_user{cursor:pointer;}
			.user_container{height:500px;}
			.user_left{height:100%;overflow-y:auto;border:1px solid #ccc;padding:20px 10px 0;}
			.user_middle{height:100%;text-align:center;}
			.user_middle button{margin:10px auto;}
			.user_right{height:100%;border:1px solid #ccc;}
			.choose_user_list{padding-top:20px;}
			.choose_user_list li{height:30px;line-height:30px;list-style:none;padding-left:20px;color:#666;cursor:pointer;}
  		</style>
	</head>
	<body>
		<div class="layui-container" style="margin-top:20px;width:100%;"> 
			<div class="search_area">
				<div class="search_area top_btn">
					<a href="set.html"><button class="layui-btn layui-btn-primary layui-btn-sm">返回</button></a>
					<span style="float:right;">
						<button class="layui-btn layui-btn-sm create_btn">确认</button>
				        <button class="layui-btn layui-btn-sm delete_btn">取消</button>	
					</span>
				</div>
			</div>
		  	<div class="layui-row user_container" style="margin-top:20px;">
			    <div class="layui-col-xs2" style="text-align: left;">
					<ul id="treeDemo" class="ztree" style="width:auto;height:500px;"></ul>
			    </div>
			    <div class="layui-col-xs5 user_left">
					<div class="search_area">
						<div class="layui-row layui-form">
							<div class="layui-col-md7">
						    	<input type="text" placeholder="流程名称"  class="layui-input">
							</div>
							<div class="layui-col-md3" style="text-align:right;">
						        <button class="layui-btn" >查询</button>
							</div>
						</div>
					</div>
					<table class="layui-table backlog_table">
						<colgroup>
							<col>
						    <col>
						    <col>
						    <col>
						</colgroup>
						<thead>
						    <tr>
						      <th><input type="checkbox" name="" title='全选' lay-skin="primary"> 序号</th>
						      <th>姓名</th>
						      <th>LDAP UID</th>
						      <th>账号别名</th>
						    </tr> 
						</thead>
						<tbody>
						    <tr>
						      <td><input type="checkbox" name="" title='全选' lay-skin="primary"> 1</td>
						      <td>00011178</td>
						      <td>00011178</td>
						      <td>zhangsan</td>
						    </tr>
						    <tr>
						      <td><input type="checkbox" name="" title='全选' lay-skin="primary"> 2</td>
						      <td>00011178</td>
						      <td>00011178</td>
						      <td>zhangsan</td>
						    </tr>
						    <tr>
						      <td><input type="checkbox" name="" title='全选' lay-skin="primary"> 3</td>
						      <td>00011178</td>
						      <td>00011178</td>
						      <td>zhangsan</td>
						    </tr>
						    <tr>
						      <td><input type="checkbox" name="" title='全选' lay-skin="primary"> 4</td>
						      <td>00011178</td>
						      <td>00011178</td>
						      <td>zhangsan</td>
						    </tr>
						    <tr>
						      <td><input type="checkbox" name="" title='全选' lay-skin="primary"> 5</td>
						      <td>00011178</td>
						      <td>00011178</td>
						      <td>zhangsan</td>
						    </tr>
						</tbody>
					</table>
					<div id="demo7"></div>
			    </div>
			    <div class="layui-col-md1 user_middle">
		    		<button class="layui-btn " style="margin-top:100px;"> <</button>
		    			<br>
		    		<button class="layui-btn "><<</button>				    
			    </div>
			    <div class="layui-col-md2 user_right">
			    	<ul class="choose_user_list">
			    		<li>用户1</li>
			    		<li>用户2</li>
			    		<li>用户3</li>
			    		<li>用户4</li>
			    		<li>用户5</li>
			    		<li>用户6</li>
			    	</ul>
			    </div>
		  	</div>
		</div>
	</body>
	
</html>
	<script type="text/javascript" src="resources/desmartportal/js/jquery-3.3.1.js" ></script>
	<script type="text/javascript" src="resources/desmartportal/js/layui.all.js"></script>
	<script type="text/javascript" src="resources/desmartportal/tree/js/jquery.ztree.core.js"></script>
	<script type="text/javascript" src="resources/desmartportal/tree/js/jquery.ztree.excheck.js"></script>
	<script type="text/javascript" src="resources/desmartportal/tree/js/jquery.ztree.exedit.js"></script>
	<script type="text/javascript" src="resources/desmartportal/js/common.js"></script>
	<script>
		layui.use(['laypage', 'layer'], function(){
			var laypage = layui.laypage,layer = layui.layer;  
			  //完整功能
			laypage.render({
			    elem: 'demo7'
			    ,count: 50
			    ,limit: 10
			    ,layout: ['count', 'prev', 'page', 'next', 'limit', 'skip']
			    ,jump: function(obj){
			      console.log(obj)
			    }
			}); 
		});
		//tree
		var setting = {	};

		var zNodes =[
			{ name:"父节点1 - 展开", open:true,
				children: [
					{ name:"父节点11 - 折叠",
						children: [
							{ name:"叶子节点111"},
							{ name:"叶子节点112"},
							{ name:"叶子节点113"},
							{ name:"叶子节点114"}
						]},
					{ name:"父节点12 - 折叠",
						children: [
							{ name:"叶子节点121"},
							{ name:"叶子节点122"},
							{ name:"叶子节点123"},
							{ name:"叶子节点124"}
						]},
					{ name:"父节点13 - 没有子节点", isParent:true}
				]},
			{ name:"父节点2 - 折叠",
				children: [
					{ name:"父节点21 - 展开", open:true,
						children: [
							{ name:"叶子节点211"},
							{ name:"叶子节点212"},
							{ name:"叶子节点213"},
							{ name:"叶子节点214"}
						]},
					{ name:"父节点22 - 折叠",
						children: [
							{ name:"叶子节点221"},
							{ name:"叶子节点222"},
							{ name:"叶子节点223"},
							{ name:"叶子节点224"}
						]},
					{ name:"父节点23 - 折叠",
						children: [
							{ name:"叶子节点231"},
							{ name:"叶子节点232"},
							{ name:"叶子节点233"},
							{ name:"叶子节点234"}
						]}
				]},
			{ name:"父节点3 - 没有子节点", isParent:true}

		];
		
		$(document).ready(function(){
			$.fn.zTree.init($("#treeDemo"), setting, zNodes);
			
			$(".create_btn").click(function(){
				$(".display_container3").css("display","block");
			})
			$(".link_table tr td").dblclick(function(){
				$(".display_container4").css("display","block");
			})
			$(".sure_btn").click(function(){
				$(".display_container3").css("display","none");
				$(".display_container4").css("display","none");
			})
			$(".cancel_btn").click(function(){
				$(".display_container3").css("display","none");
				$(".display_container4").css("display","none");
			})
		});
		
  </script>