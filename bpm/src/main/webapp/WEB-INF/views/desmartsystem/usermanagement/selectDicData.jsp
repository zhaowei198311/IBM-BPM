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
			
		</style>
	</head>
	<body>
		<!-- 初始化页面 -->
		<div class="container">
			<div class="search_area">
				<input type="hidden" id="elementId" value="${ elementId }"/>
				<input type="hidden" id="dicUid" value="${ dicUid }"/>
				<div class="layui-row layui-form">
					<div class="layui-col-md1" style="float:left;width: 250px;">
						<input type="text" id="dicDataName" placeholder="请输入内容" autocomplete="off" class="layui-input"/>
					</div>
					<div class="layui-col-md1" style="float:left;margin-left:10px;">
						<button class="layui-btn create_btn" onclick="searchOnDicDataList()">查询</button>
					</div>
				</div>						
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
					      <th>字段名</th>
					      <th>说明</th>
					    </tr> 
					</thead>
					<tbody id="tabletrDetail">

					</tbody>
				</table>
			</div>
			<div id="pagination2"></div>
			<div class="foot" style="position: absolute;bottom: 35px;right: 5px;">
				<button class="layui-btn layui-btn sure_btn" id="sure_btn">确定</button>
				<button class="layui-btn layui-btn layui-btn-primary cancel_btn" id="cancel_btn">取消</button>
			</div>
		</div>
	<script type="text/javascript">
		var dicDataName = "";
		var oldDicDataUid = "";
		$(function(){
			$("#sure_btn").click(function(){
				var inputObj = $("[name='dic_check']:checked");
				var dicDataUid = inputObj.val();
				var elementId = $("#elementId").val();
				var dicDataName = inputObj.parent().next().text().trim();
				var dicDataCode = inputObj.attr("title").trim();
				window.parent.document.getElementById(elementId+"_code").value=dicDataCode
				window.parent.document.getElementById(elementId).value=dicDataUid;
				window.parent.document.getElementById(elementId+"_view").value=dicDataName;
			});
			var elementId = $("#elementId").val();
			oldDicDataUid = window.parent.document.getElementById(elementId).value;
			getOnDicDataList();
		});
		//复选框只能选择一个
		function onSelOne(obj){
			$('input[name="dic_check"]').not($(obj)).prop("checked", false);
		}
		// 跳转页面加载
		function tableDetail(data){
			$("#tabletrDetail").empty();
        	
        	var list = data;
        	for(var i=0; i<list.length; i++) {
        		var data = list[i];
				var str='<tr>';
				
				if(oldDicDataUid==data.dicDataUid){
					str+='<td><input type="checkbox" title="'+data.dicDataCode+'" name="dic_check" value="'+data.dicDataUid+'" onclick="onSelOne(this)" checked/>' + (i+1) + '</td>';
				}else{
					str+='<td><input type="checkbox" title="'+data.dicDataCode+'" name="dic_check" value="'+data.dicDataUid+'" onclick="onSelOne(this)"/>' + (i+1) + '</td>';
				}
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
		function getOnDicDataList(){
			// 选中行id
			$.ajax({
				url: "sysDictionary/listOnDicDataBydicUid",
				method: "post",
				async:false,
				data: {
					dicDataName:dicDataName,
					dicUid: $("#dicUid").val().trim()
				},
				success: function(result){
					if(result.status==0){
						tableDetail(result.data);
					}
				}
			});
		}
		//模糊查询
		function searchOnDicDataList(){
			dicDataName = $("#dicDataName").val().trim();
			getOnDicDataList();
		}
	</script>
	</body>
</html>
