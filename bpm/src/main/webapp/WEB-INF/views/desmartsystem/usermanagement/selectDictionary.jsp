<%@ page language="java" contentType="text/html; charset=UTF-8"
    isErrorPage="true"
    pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<base href="<%=basePath%>" />
		<meta charset="utf-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1" />
  		<title>选择数据字典</title>
  		<%@ include file="common/common.jsp" %>
  		<style>
			.display_content5{
				margin: 10px 0 0 -300px;
			}
		</style>
	</head>
	<body>
		<!-- 初始化页面 -->
		<div class="container">
			<div class="search_area">
				<input type="hidden" id="elementId" value="${ elementId }"/>
				<div class="layui-row layui-form">
					<div class="layui-col-md1" style="float:left;width: 250px;">
						<input type="text" id="dicName" placeholder="请输入内容" autocomplete="off" class="layui-input"/>
					</div>
					<div class="layui-col-md1" style="float:left;margin-left:10px;">
						<button class="layui-btn create_btn" onclick="searchOnDictionaryList()">查询</button>
					</div>
				</div>						
			</div>
			<div>				
				<table class="layui-table backlog_table" lay-even lay-skin="nob">
					<colgroup>
					    <col/>
					    <col/>
					    <col/>
					    <col/>
					    <col/>
					</colgroup>
					<thead>
					    <tr>
					      <th>序号</th>
					      <th>编码</th>
					      <th>字段名</th>
					      <th>说明</th>
					    </tr> 
					</thead>
					<tbody id="tabletr">

					</tbody>
				</table>
			</div>
			<div id="pagination1"></div>
			<div class="foot" style="position: absolute;bottom: 35px;right: 5px;">
				<button class="layui-btn layui-btn sure_btn" id="sure_btn">确定</button>
				<button class="layui-btn layui-btn layui-btn-primary cancel_btn" id="cancel_btn">取消</button>
			</div>
		</div>
		<!-- 双击行跳转至详情页面 -->
	<div class="display_container5">
		<div class="display_content5">
			<div class="top">
				数据字典详情
			</div>
			<div>				
				<table class="layui-table" lay-even lay-skin="nob">
					<colgroup>
					    <col/>
					    <col/>
					    <col/>
					    <col/>
					    <col/>
					</colgroup>
					<thead>
					    <tr>
					      <th>序号</th>
					      <th>编码</th>
					      <th>字段名</th>
					      <th>说明</th>
					    </tr> 
					</thead>
					<tbody id="tabletrDetail">

					</tbody>
				</table>
			</div>
			<div id="pagination2"></div>
			<div class="foot" style="position: absolute;bottom: 0px;right: 5px;">
				<button class="layui-btn layui-btn layui-btn-primary cancel_btn">取消</button>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		// 为翻页提供支持
	    var pageConfig = {
	    	pageNum: 1,
	    	pageSize: 5,
	    	total: 0,
	    	dicName:""
	    }
		
	    var pageConfig1 = {
	    	pageNum: 1,
	    	pageSize: 5,
	    	total: 0
	    }
		// 分页
	    function doPage() {
	        layui.use(['laypage', 'layer'], function(){
	            var laypage = layui.laypage,layer = layui.layer;  
	              //完整功能
	            laypage.render({
	                elem: 'pagination1',
	                curr: pageConfig.pageNum,
	                count: pageConfig.total,
	                limit: pageConfig.pageSize,
	                layout: ['count', 'prev', 'page', 'next', 'skip'],
	                jump: function(obj, first){
	                	// obj包含了当前分页的所有参数  
	                	pageConfig.pageNum = obj.curr;
	                	pageConfig.pageSize = obj.limit;
	                	if (!first) {
	                		$("input[name='allSel']").prop("checked",false);
	                		getOnDictionaryList();
	                	}
	                }
	            }); 
	        });
	    }
		
	    function doPage1() {
	        layui.use(['laypage', 'layer'], function(){
	            var laypage = layui.laypage,layer = layui.layer;  
	              //完整功能
	            laypage.render({
	                elem: 'pagination2',
	                curr: pageConfig1.pageNum,
	                count: pageConfig1.total,
	                limit: pageConfig1.pageSize,
	                layout: ['count', 'prev', 'page', 'next', 'skip'],
	                jump: function(obj, first){
	                	// obj包含了当前分页的所有参数  
	                	pageConfig1.pageNum = obj.curr;
	                	pageConfig1.pageSize = obj.limit;
	                	if (!first) {
	                		getOnDicDataList(dicUid);
	                	}
	                }
	            }); 
	        });
	    }
	
		$(function(){
			getOnDictionaryList();
			
			$(".display_container5 .cancel_btn").click(function(){
				$(".display_container5").css("display","none");
			});
			
			$("#sure_btn").click(function(){
				var inputObj = $("[name='dic_check']:checked");
				var dictionaryUid = inputObj.parent().parent().attr("value");
				var dicName = inputObj.parent().next().next().text().trim();
				var elementId = $("#elementId").val();
				window.parent.document.getElementById(elementId).value=dictionaryUid;
				window.parent.document.getElementById(elementId+"_view").value=dicName;
			});
		});
		// 主页面id
		var dicUid = "";
		// 初始化加载
		function drawTable(pageInfo){
			$("#tabletr").html('');
			pageConfig.pageNum = pageInfo.pageNum;
        	pageConfig.pageSize = pageInfo.pageSize;
        	pageConfig.total = pageInfo.total;
        	doPage();
        	if (pageInfo.total == 0) {
        		return;
        	}
        	
        	var list = pageInfo.list;
        	var startSort = pageInfo.startRow;//开始序号
        	var elementId = $("#elementId").val();
        	var dictionaryUid = window.parent.document.getElementById(elementId).value;
        	for(var i=0; i<list.length; i++) {
        		var data = list[i];
        		var sortNum = startSort + i;
        		var str='<tr value="'+data.dicUid+'" title="双击查看详细数据内容">';
        		if(dictionaryUid==data.dicUid){
        			str+='<td><input type="checkbox" value="'+data.dicUid+'" name="dic_check" onclick="onSelOne(this);" checked>' + sortNum + '</td>';
        		}else{
        			str+='<td><input type="checkbox" value="'+data.dicUid+'" name="dic_check" onclick="onSelOne(this);">' + sortNum + '</td>';
        		}
					str+='<td>' + data.dicCode + '</td>';
					str+='<td>' + data.dicName + '</td>';
				if(data.dicDescription!=null && data.dicDescription!=""){
					str+='<td>' + data.dicDescription + '</td>';
				}else{
					str+='<td></td>';
				}
		        $("#tabletr").append(str);
        	}
			 // 为每一行设置双击跳转事件
			 $("#tabletr").on("dblclick","tr",function(){
				 // 显示详情页面
				$(".display_container5").css("display","block");
				dicUid = $(this).attr('value');
				getOnDicDataList(dicUid);
			});
		}
		//复选框只能选择一个
		function onSelOne(obj){
			$('input[name="dic_check"]').not($(obj)).prop("checked", false);
		}
		// 跳转页面加载
		function tableDetail(pageInfo){
			$("#tabletrDetail").empty();
			pageConfig1.pageNum = pageInfo.pageNum;
        	pageConfig1.pageSize = pageInfo.pageSize;
        	pageConfig1.total = pageInfo.total;
        	doPage1();
			if (pageInfo.total == 0) {
        		return;
        	}
        	
        	var list = pageInfo.list;
        	var startSort = pageInfo.startRow;//开始序号
        	for(var i=0; i<list.length; i++) {
        		var data = list[i];
        		var sortNum = startSort + i;
				var str='<tr>';
					str+='<td>' + sortNum + '</td>';
					str+='<td>' + data.dicDataCode + '</td>';
					str+='<td>' + data.dicDataName + '</td>';
				if(data.dicDataDescription!=null && data.dicDataDescription!=""){
					str+='<td>' + data.dicDataDescription + '</td>';	
				}else{
					str+='<td></td>';
				}
	         	$("#tabletrDetail").append(str);
	         }
		}
		//查询所有的数据字典详细信息
		function getOnDicDataList(dicUid){
			// 选中行id
			$.ajax({
				url: "sysDictionary/getOnSysDictionaryDataList",
				method: "post",
				data: {
					pageNum:pageConfig1.pageNum,
					pageSize:pageConfig1.pageSize,
					dicUid: dicUid
				},
				success: function(result){
					if(result.status==0){
						tableDetail(result.data);
					}
				}
			});
		}
		// 查询所有的数据字典分类
		function getOnDictionaryList(){
			$.ajax({
				url: "sysDictionary/getOnSysDictionaryList",
				method: "post",
				data: {
					pageNum:pageConfig.pageNum,
					pageSize:pageConfig.pageSize,
					dicName: pageConfig.dicName
				},
				success: function(result){
					if(result.status==0){
						drawTable(result.data);
					}
				}
			})	
		}
		//模糊查询
		function searchOnDictionaryList(){
			pageConfig.dicName = $("#dicName").val().trim();
			getOnDictionaryList();
		}
	</script>
	</body>
</html>
