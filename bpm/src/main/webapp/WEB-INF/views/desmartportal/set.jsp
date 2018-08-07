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
  		<link href="resources/desmartportal/css/my.css" rel="stylesheet" />
  		<style>  			
  			.ztree li span.button.add {
			    margin-right: 2px;
			    background-position: -143px 0px;
			    vertical-align: top;
			}
  		</style>
  		
	</head>
	<body>
		<div class="container">
			<div class="search_area">
				<div class="layui-row layui-form">
					<div class="layui-col-xs3">
					    <div class="layui-form-pane">
					    	<div class="layui-form-item">
					          	<label class="layui-form-label" style="cursor:pointer;">刷新</label>
						        <div class="layui-input-block">
						        	<input type="text" placeholder="授权规则名称"  class="layui-input">
						            <!--<select class="layui-input-block group_select" name="group" lay-verify="required">
									  	<option value="">分组</option>
									  	<option value="01">不分组</option>
									  	<option value="02">按类型分组</option>
									  	<option value="03">按任务创建分组</option>
									  	<option value="04">按任务创建人所在部门分组</option>
									  	<option value="05">按优先级分组</option>
									</select>-->
						        </div>
					       </div>					    	     
					    </div>
					</div>
					<!--<div class="layui-col-md2">
				    	<input type="text" placeholder="来自"  class="layui-input">
					</div>
					<div class="layui-col-md2">
						<input type="text" placeholder="标题"  class="layui-input">
					</div>
					<div class="layui-col-md2">
						<input type="text"  placeholder="开始时间"  class="layui-input" id="test1">
					</div>
					<div class="layui-col-md2">
					    <input type="text"  placeholder="结束时间"  class="layui-input" id="test2">				    
					</div>-->
					<div class="layui-col-xs4" style="text-align:right;">
					        <button class="layui-btn" >查询</button>
					        <button class="layui-btn create_btn" >新建</button> 
					        <button class="layui-btn delete_btn" >删除</button>
					        <button class="layui-btn " >启用</button>
					        <button class="layui-btn " >停止</button>
					</div>
				</div>
			</div>
			<div>
				<!--<p class="table_list"><i class="layui-icon">&#xe61d;</i>共3条任务</p>-->
				<table class="layui-table backlog_table">
					<colgroup>
					    <col width="90">
					    <col width="120">					    
					    <col width="100">
					    <col width="100">
					    <col width="60">
					    <col>
					    <col>
					    <col>
					</colgroup>
					<thead>
					    <tr>
					      <th><input type="checkbox" name="" title='全选' lay-skin="primary"> 序号</th>
					      <th>授权规则名</th>
					      <th>授权者</th>
					      <th>授权给</th>
					      <th>状态</th>
					      <th>生效时间</th>
					      <th>失效时间</th>
					      <th>创建者</th>
					      <th>创建时间</th>
					      <th>更新者</th>
					      <th>更新时间</th>
					    </tr> 
					</thead>
					<tbody>
					    <tr>
					      <td><input type="checkbox" name="" lay-skin="primary"> 1</td>
					      <td>高级授权</td>
					      <td>zhangsan</td>					      
					      <td>lisi</td>
					      <td>停用</td>
					      <td>2018-03-12</td>
					      <td>2019-03-12</td>
					      <td>zhangsan</td>
					      <td>2018-03-12 10:00:00</td>
					      <td>zhangsan</td>
					      <td>2018-03-12 10:00:00</td>
					    </tr>
					    <tr>
					     <td><input type="checkbox" name="" lay-skin="primary"> 2</td>
					      <td>高级授权</td>
					      <td>zhangsan</td>					      
					      <td>lisi</td>
					      <td>停用</td>
					      <td>2018-03-12</td>
					      <td>2019-03-12</td>
					      <td>zhangsan</td>
					      <td>2018-03-12 10:00:00</td>
					      <td>zhangsan</td>
					      <td>2018-03-12 10:00:00</td>
					    </tr>
					    <tr>
					      <td><input type="checkbox" name="" lay-skin="primary"> 3</td>
					      <td>高级授权</td>
					      <td>zhangsan</td>					      
					      <td>lisi</td>
					      <td>停用</td>
					      <td>2018-03-12</td>
					      <td>2019-03-12</td>
					      <td>zhangsan</td>
					      <td>2018-03-12 10:00:00</td>
					      <td>zhangsan</td>
					      <td>2018-03-12 10:00:00</td>
					    </tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="display_container3">
			<div class="display_content3">
				<div class="top">
					新建授权
				</div>
				<div class="middle3" style="padding-top:20px;">
					<div class="layui-form-item">
					    <label class="layui-form-label">授权模式</label>
					    <div class="layui-input-block">
					      	<input type="radio" name="type" value="简单授权" title="简单授权" checked id="hight_class1" onclick="show1()"> <label for="hight_class1">简单授权</label>
					      	<input type="radio" name="type" value="高级授权" title="高级授权" id="hight_class2" onclick="show2()"> <label for="hight_class2">高级授权</label>
					    </div>
				 	</div>					  	
				  	<!--<div class="layui-form-item">
					    <label class="layui-form-label">规则名称</label>
					    <div class="layui-input-block">
					      <input type="text" name="title" required  lay-verify="required" placeholder="请输入角色名称" autocomplete="off" class="layui-input">
					    </div>
				  	</div>-->
				  	<div class="layui-form-item to_user">
					    <label class="layui-form-label">授权给</label>
				      	<div class="layui-input-inline">
							<input type="text" name="text" required lay-verify="required"  autocomplete="off" class="layui-input">
						</div>
						<div class="layui-form-mid layui-word-aux"><i class="layui-icon choose_user">&#xe612;</i> </div>
					</div>
					<div class="layui-form-item">
					    <label class="layui-form-label">生效方式</label>
					    <div class="layui-input-block">
					      <input type="radio" name="time" value="立即生效" title="立即生效" checked id="time1" onclick="show3()"><label for="time1">立即生效</label>
					      <input type="radio" name="time" value="按时生效" title="按时生效" id="time2" onclick="show4()"><label for="time2">按时生效</label>
					    </div>
					  </div>
					<div class="layui-form-item lose_efficacy">
					    <label class="layui-form-label">启用时间</label>
					    <div class="layui-input-block">
					      	<input type="text"  placeholder=""  class="layui-input" id="test1">
						</div>
					</div>
					<div class="layui-form-item lose_efficacy">
					    <label class="layui-form-label">失效时间</label>
					    <div class="layui-input-block">
					      	<input type="text"  placeholder=""  class="layui-input" id="test2">
						</div>
					</div>
					<div class="layui-form-item">
					    <label class="layui-form-label">选择授权流程</label>
				      	<div class="layui-input-inline">
							<input type="text" name="text" required lay-verify="required"  autocomplete="off" class="layui-input">
						</div>
						<div class="layui-form-mid layui-word-aux">
							<button class="layui-btn layui-btn-sm layui-btn-primary choose_condition">选择流程</button>
						</div>
					</div>
					<div class="layui-form-item high_class">
					    <label class="layui-form-label">按条件授权</label>
					    <div class="layui-input-block">
					      	<button class="layui-btn layui-btn-sm layui-btn-primary choose_condition1">选择条件</button>
						</div>
					</div>
					<textarea name="desc"  class="layui-textarea high_class" disabled="disabled"></textarea>
				</div>
				<div class="foot">
					<button class="layui-btn layui-btn sure_btn">确定</button>
					<button class="layui-btn layui-btn layui-btn-primary cancel_btn">取消</button>
				</div>
			</div>
		</div>
		<div class="display_container6">
			<div class="display_content6">
				<div class="top">
					授权给
				</div>
				<div class="middle6" style="padding-top:20px;">
					<div class="layui-row user_container" style="margin-top:20px;">
					    <div class="layui-col-md2" style="text-align: left;">
							<ul id="treeDemo" class="ztree" style="width:auto;height:320px;"></ul>
					    </div>
					    <div class="layui-col-md5 user_left">
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
							<table class="layui-table backlog_table" lay-even lay-skin="nob">
								<colgroup>
									<col>
								    <col>
								    <col>
								    <col>
								</colgroup>
								<thead>
								    <tr>
								      <th> 序号</th>
								      <th>姓名</th>
								      <th>LDAP UID</th>
								      <th>账号别名</th>
								    </tr> 
								</thead>
								<tbody>
								    <tr>
								      <td><input type="radio" name="to_user" lay-skin="primary"> 1</td>
								      <td>00011178</td>
								      <td>00011178</td>
								      <td>zhangsan</td>
								    </tr>
								    <tr>
								      <td><input type="radio" name="to_user" lay-skin="primary"> 2</td>
								      <td>00011178</td>
								      <td>00011178</td>
								      <td>zhangsan</td>
								    </tr>
								    <tr>
								      <td><input type="radio" name="to_user" lay-skin="primary"> 3</td>
								      <td>00011178</td>
								      <td>00011178</td>
								      <td>zhangsan</td>
								    </tr>
								    <tr>
								      <td><input type="radio" name="to_user" lay-skin="primary"> 4</td>
								      <td>00011178</td>
								      <td>00011178</td>
								      <td>zhangsan</td>
								    </tr>
								    <tr>
								      <td><input type="radio" name="to_user" lay-skin="primary"> 5</td>
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
				    			<!--<br>
				    		<button class="layui-btn "><<</button>				    -->
					    </div>
					    <div class="layui-col-md2 user_right">
					    	<ul class="choose_user_list">
					    		<li>用户1</li>
					    	</ul>
					    </div>
				  	</div>
				</div>
				<div class="foot">
					<button class="layui-btn layui-btn sure_btn1">确定</button>
					<button class="layui-btn layui-btn layui-btn-primary cancel_btn1">取消</button>
				</div>
			</div>
		</div>
		
		<div class="display_container5">
			<div class="display_content5">
				<div class="top">
					选择代理流程
				</div>
				<div class="middle5" style="padding-top:20px;">
					<div class="layui-row user_container" style="margin-top:20px;height:320px;">
					    <div class="layui-col-md5" style="text-align: left;">
							<ul id="treeDemo1" class="ztree" style="width:auto;height:300px;"></ul>
					    </div>
					    <div class="layui-col-md6 user_left" style="height:310px;">							
							<table class="layui-table backlog_table" lay-even lay-skin="nob" style="height:300px;">
								<colgroup>
									<col>
								    <col>
								</colgroup>
								<thead>
								    <tr>
								      <th><input type="checkbox" name="" title='全选' lay-skin="primary"> 序号</th>
								      <th>流程名称</th>
								    </tr> 
								</thead>
								<tbody>
								    <tr>
								      <td><input type="checkbox" name=""  lay-skin="primary"> 1</td>
								      <td>流程1</td>
								    </tr>
								    <tr>
								      <td><input type="checkbox" name="" lay-skin="primary"> 2</td>
								      <td>流程2</td>
								    </tr>
								    <tr>
								      <td><input type="checkbox" name=""  lay-skin="primary"> 3</td>
								      <td>流程3</td>
								    </tr>
								    <tr>
								      <td><input type="checkbox" name=""  lay-skin="primary"> 4</td>
								     <td>流程4</td>
								    </tr>
								    <tr>
								      <td><input type="checkbox" name=""  lay-skin="primary"> 5</td>
								      <td>流程5</td>
								    </tr>
								</tbody>
							</table>
					   </div>					   
				  	</div>
				</div>
				<div class="foot">
					<button class="layui-btn layui-btn sure_btn1">确定</button>
					<button class="layui-btn layui-btn layui-btn-primary cancel_btn1">取消</button>
				</div>
			</div>
		</div>
		<div class="display_container8">
			<div class="display_content8">
				<div class="top">
					选择条件
				</div>
				<div class="middle7" style="padding-top:10px;">
					<button class="layui-btn lauyui-btn-primary layui-btn-sm create_condition">新建</button>
					<button class="layui-btn lauyui-btn-primary layui-btn-sm delete_condition">删除</button>
					<table class="layui-table backlog_table trigger_table" lay-even lay-skin="nob">
						<colgroup>
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
						      <th>序号</th>
						      <th>字段名称</th>
						      <th>运算符</th>
						      <th>字段值</th>
						      <th>操作类型</th>
						      <th>授权给</th>
						      <th>授权</th>
						    </tr> 
						</thead>
						<tbody>
							<tr>
								<td><input type="checkbox" name=""  lay-skin="primary"> 1</td>
							    <td>销售额</td>
							    <td>==</td>
							    <td>1</td>
							    <td>与</td>
							    <td>张三</td>
							    <td><i class="layui-icon choose_user1">&#xe612;</i></td>
							</tr>
						</tbody>
					</table>				
				</div>
				<div class="foot">
					<button class="layui-btn layui-btn sure_btn1">确定</button>
					<button class="layui-btn layui-btn layui-btn-primary cancel_btn1">取消</button>
				</div>
			</div>
		</div>
		<div class="display_container7">
			<div class="display_content7">
				<div class="top">
					新增条件
				</div>
				<div class="middle7" style="padding-top:10px;">
					<form class="layui-form" action="">
					  	<div class="layui-form-item">
						    <label class="layui-form-label">字段名称</label>
						    <div class="layui-input-block">
						      	<select name="" lay-verify="required">
							        <option value="amount">amount</option>
							    </select>
						    </div>
					  	</div>
						<div class="layui-form-item">
						    <label class="layui-form-label">运算符</label>
						    <div class="layui-input-block">
						     <select name="" lay-verify="required">
							        <option value="=">=</option>
							        <option value="<"><</option>
							        <option value=">">></option>
							        <option value=">=">>=</option>
							        <option value="<="><=</option>
							    </select>
						    </div>
					  	</div>
					  	<div class="layui-form-item">
						    <label class="layui-form-label">字段值</label>
						    <div class="layui-input-block">
						      <input type="text" name="title" required  lay-verify="required" placeholder="" autocomplete="off" class="layui-input">
						    </div>
					  	</div>
					  	<div class="layui-form-item">
						    <label class="layui-form-label">字段值类型</label>
						    <div class="layui-input-block">
						      <select name="" lay-verify="required">
							        <option value="字符串">字符串</option>
							         <option value="整数">整数</option>
							    </select>
						    </div>
					  	</div>
					  	<div class="layui-form-item">
						    <label class="layui-form-label">操作类型</label>
						    <div class="layui-input-block">
						      <select name="" lay-verify="required">
							        <option value="与">与</option> 
							        <option value="或">或</option>
							    </select>
						    </div>
					  	</div>
					</form>					
				</div>
				<div class="foot">
					<button class="layui-btn layui-btn sure_btn2">确定</button>
					<button class="layui-btn layui-btn layui-btn-primary cancel_btn2">取消</button>
				</div>
			</div>
		</div>
		<div class="display_container9">
			<div class="display_content6">
				<div class="top">
					授权给
				</div>
				<div class="middle6" style="padding-top:20px;">
					<div class="layui-row user_container" style="margin-top:20px;">
					    <div class="layui-col-xs2" style="text-align: left;">
							<ul id="treeDemo2" class="ztree" style="width:auto;height:320px;"></ul>
					    </div>
					    <div class="layui-col-xs5 user_left">
							<div class="search_area">
								<div class="layui-row layui-form">
									<div class="layui-col-xs7">
								    	<input type="text" placeholder="流程名称"  class="layui-input">
									</div>
									<div class="layui-col-xs3" style="text-align:right;">
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
								      <th> 序号</th>
								      <th>姓名</th>
								      <th>LDAP UID</th>
								      <th>账号别名</th>
								    </tr> 
								</thead>
								<tbody>
								    <tr>
								      <td><input type="radio" name="to_user" lay-skin="primary"> 1</td>
								      <td>00011178</td>
								      <td>00011178</td>
								      <td>zhangsan</td>
								    </tr>
								    <tr>
								      <td><input type="radio" name="to_user" lay-skin="primary"> 2</td>
								      <td>00011178</td>
								      <td>00011178</td>
								      <td>zhangsan</td>
								    </tr>
								    <tr>
								      <td><input type="radio" name="to_user" lay-skin="primary"> 3</td>
								      <td>00011178</td>
								      <td>00011178</td>
								      <td>zhangsan</td>
								    </tr>
								    <tr>
								      <td><input type="radio" name="to_user" lay-skin="primary"> 4</td>
								      <td>00011178</td>
								      <td>00011178</td>
								      <td>zhangsan</td>
								    </tr>
								    <tr>
								      <td><input type="radio" name="to_user" lay-skin="primary"> 5</td>
								      <td>00011178</td>
								      <td>00011178</td>
								      <td>zhangsan</td>
								    </tr>
								</tbody>
							</table>
							<div id="demo7"></div>
					    </div>
					    <div class="layui-col-xs1 user_middle">
				    		<button class="layui-btn " style="margin-top:100px;"> <</button>
				    			<!--<br>
				    		<button class="layui-btn "><<</button>				    -->
					    </div>
					    <div class="layui-col-xs2 user_right">
					    	<ul class="choose_user_list">
					    		<li>用户1</li>
					    	</ul>
					    </div>
				  	</div>
				</div>
				<div class="foot">
					<button class="layui-btn layui-btn sure_btn1">确定</button>
					<button class="layui-btn layui-btn layui-btn-primary cancel_btn1">取消</button>
				</div>
			</div>
		</div>
		<script type="text/javascript" src="resources/desmartportal/js/jquery-3.3.1.js" ></script>
		<script type="text/javascript" src="resources/desmartportal/js/layui.all.js"></script>	
		<script type="text/javascript" src="resources/desmartportal/tree/js/jquery.ztree.core.js"></script>
		<script type="text/javascript" src="resources/desmartportal/tree/js/jquery.ztree.excheck.js"></script>
		<script type="text/javascript" src="resources/desmartportal/tree/js/jquery.ztree.exedit.js"></script>
		<script type="text/javascript" src="resources/desmartportal/js/common.js"></script>
		<!--IE8只能支持jQuery1.9-->
	    <!--[if lte IE 8]>
	    <script src="http://cdn.bootcss.com/jquery/1.9.0/jquery.min.js"></script>
	    <![endif]-->
		<!-- 让IE8/9支持媒体查询，从而兼容栅格 -->
		<!--[if lt IE 9]>
		  <script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
		  <script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
		<![endif]-->
	</body>
	
</html>
	
	<script>
		layui.use('laydate', function(){
			var laydate = layui.laydate;
			  	laydate.render({
			    elem: '#test1'
			});
		});
		layui.use('laydate', function(){
			var laydate = layui.laydate;
			  	laydate.render({
			    elem: '#test2'
			});
		});
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
		function show1(){
			$(".high_class").css("display","none");
			$(".to_user").css("display","block");
		}
		function show2(){
			$(".high_class").css("display","block");
			$(".to_user").css("display","none");
		}
		function show3(){
			$(".lose_efficacy").css("display","none");
		}
		function show4(){
			$(".lose_efficacy").css("display","block");
		}
		$(function(){
			$.fn.zTree.init($("#treeDemo"), setting, zNodes);
			$.fn.zTree.init($("#treeDemo1"), setting, zNodes);
			$.fn.zTree.init($("#treeDemo2"), setting, zNodes);
			
			$(".choose_user").click(function(){
				$(".display_container6").css("display","block");
			})
			$(".choose_user1").click(function(){
				$(".display_container9").css("display","block");
			})
			$(".create_btn").click(function(){
				$(".display_container3").css("display","block");
			})
			$(".link_table tr td").dblclick(function(){
				$(".display_container4").css("display","block");
			})
			$(".choose_condition").click(function(){
				$(".display_container5").css("display","block");
			})
			$(".choose_condition1").click(function(){
				$(".display_container8").css("display","block");
			})
			$(".create_condition").click(function(){
				$(".display_container7").css("display","block");
			})
			$(".sure_btn").click(function(){
				$(".display_container3").css("display","none");
				$(".display_container4").css("display","none");
			})
			$(".cancel_btn").click(function(){
				$(".display_container3").css("display","none");
				$(".display_container4").css("display","none");
			})
			$(".sure_btn1").click(function(){
				$(".display_container5").css("display","none");
				$(".display_container6").css("display","none");
				$(".display_container8").css("display","none");
				$(".display_container9").css("display","none");
			})
			$(".cancel_btn1").click(function(){
				$(".display_container5").css("display","none");
				$(".display_container6").css("display","none");
				$(".display_container8").css("display","none");
				$(".display_container9").css("display","none");
			})
			$(".sure_btn2").click(function(){
				$(".display_container7").css("display","none");
			})
			$(".cancel_btn2").click(function(){
				$(".display_container7").css("display","none");
			})
		})
	</script>