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
  		<link href="css/my.css" rel="stylesheet" />
  		<style>
  			.layui-anim.layui-icon{vertical-align:-8px;}
  		</style>
  		
	</head>
	<body>
		<div class="container">
			<div class="search_area top_btn">
				<a href="set.html"><button class="layui-btn layui-btn-primary layui-btn-sm">返回</button></a>
				<span style="padding-left:10px;color:#777;font-size:18px;">添加委托设置</span>
				<span style="float:right;">
					<button class="layui-btn layui-btn-primary layui-btn-sm">保存</button><button class="layui-btn layui-btn-sm">提交</button>
				</span>
			</div>
			<div class="content">
				<p class="title_p">详细信息</p>
				<table class="layui-table">
					<colgroup>
					    <col width="150">
					    <col>
					    <col width="150">
					    <col> 
					</colgroup>
					<tbody>
					    <tr>
					      <td>申请人</td>
					      <td><input type="text" name="title" required  lay-verify="required" value="00003" autocomplete="off" class="layui-input"></td>
					      <td>申请人姓名</td>
					      <td><input type="text" name="title" required  lay-verify="required" value="00003" autocomplete="off" class="layui-input"></td>
					    </tr>
					    <tr>
					      <td>申请人单位</td>
					      <td><input type="text" name="title" required  lay-verify="required" value="2018-03-12" autocomplete="off" class="layui-input"></td>
					      <td>申请人部门</td>
					      <td><input type="text" name="title" required  lay-verify="required" value="11453" autocomplete="off" class="layui-input"></td>
					    </tr>
					</tbody>
				</table>
				<p class="title_p">委托信息</p>
				<table class="layui-table layui-form">
					<colgroup>
					    <col width="150">
					    <col>
					    <col width="150">
					    <col> 
					</colgroup>
					<tbody>
					    <tr class="layui-input-item">
					      <td>代理人<span class="red">*</span></td>
					      <td style="position:relative;"><input type="text" name="title" required  lay-verify="required" value="00003" autocomplete="off" class="layui-input"><i class="layui-icon choice_user" style="position:absolute;right:20px;top:15px;cursor:pointer;">&#xe61d;</i> </td>
					      <td>范围<span class="red">*</span></td>
					      <td class="layui-input-block">      
					      		<input class="choice_type1" type="radio" name="choice" value="1" title="全部" checked="checked">
      							<input class="choice_type2" type="radio" name="choice" value="2" title="部分">
					      </td>
					    </tr>
					    <tr class="choice_tr">
					      <td height="50">流程<span class="red">*</span></td>
					      <td colspan="3"><textarea placeholder="意见留言" class="layui-textarea"></textarea></td>
					    </tr>
					    <tr>
					      <td>生效时间<span class="red">*</span></td>
					      <td><input type="text" name="title" required  lay-verify="required" value="2018-03-12" autocomplete="off" class="layui-input"></td>
					      <td>失效时间<span class="red">*</span></td>
					      <td><input type="text" name="title" required  lay-verify="required" value="11453" autocomplete="off" class="layui-input"></td>
					    </tr>
					    <tr>
					      <td height="50">委托说明<span class="red">*</span></td>
					      <td colspan="3"><textarea placeholder="意见留言" class="layui-textarea"></textarea></td>
					    </tr>
					</tbody>
				</table>				
			</div>
		</div>
		<div class="display_container">
			<div class="display_content">
				<div class="top">
					地址簿
				</div>
				<div class="middle">
					<input type="text" class="search_user" placeholder="快速搜索"/>
					<i class="layui-icon choice_user" style="position:absolute;right:20px;top:15px;cursor:pointer;">&#xe615;</i>
					<div class="user_list" id="user_list">
						<ul class="tab_list" id="tab_list">
							<li class="active">组织</li>
							<li>角色</li>
							<li>群组</li>
						</ul>
						<div class="tree" id="demo">
							
						</div>
						<div class="tree1" id="demo1">
							
						</div>
						<div class="tree2" id="demo2">
							
						</div>
					</div>
				</div>
				<div class="foot">
					<button class="layui-btn layui-btn sure_btn">确定</button>
					<button class="layui-btn layui-btn layui-btn-primary cancel_btn">取消</button>
				</div>
			</div>
		</div>
	</body>
	
</html>
	<script type="text/javascript" src="js/jquery-3.3.1.js" ></script>
	<script type="text/javascript" src="js/layui.all.js"></script>	
	<script>
		$(function(){
			$(".choice_user").click(function(){
				$(".display_container").css("display","block");
			})
			$(".sure_btn").click(function(){
				$(".display_container").css("display","none");
			})
			$(".cancel_btn").click(function(){
				$(".display_container").css("display","none");
			})
		})
		window.onload=function(){
			var tab=document.getElementById("tab_list");
			//console.log(divs.length);
			var lis=tab.getElementsByTagName("li");
			//console.log(lis.length);
			for(var i=0;i<lis.length;i++){
				lis[i].onclick=function(){						
					for(var j=0;j<lis.length;j++){
						lis[j].className="";
					}
					this.className="active";
				}
			}
		}
		layui.tree({
		  elem: '#demo' //传入元素选择器
		  ,nodes: [{ //节点
		    name: '部门1'
		    ,children: [{
		      name: '管理员'
		    },{
		      name: 'test1'
		    }]
		  },{
		    name: '部门2'
		    ,children: [{
		      name: 'test2'
		    }]
		  }]
		});
		layui.tree({
		  elem: '#demo1' //传入元素选择器
		  ,nodes: [{ //节点
		    name: '用户'
		    ,children: [{
		      name: '管理员'
		    },{
		      name: 'test1'
		    }]
		  },{
		    name: '系统'
		    ,children: [{
		      name: 'test2'
		    }]
		  }]
		});
		layui.tree({
		  elem: '#demo2' //传入元素选择器
		  ,nodes: [{ //节点
		    name: '其他'
		    ,children: [{
		      name: '管理员'
		    },{
		      name: 'test1'
		    }]
		  },{
		    name: '系统'
		    ,children: [{
		      name: 'test2'
		    }]
		  }]
		});
	</script>