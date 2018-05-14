<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<div class="top">
		<div class="layui-col-md12">绑定部门</div>
		<div class="">
			<div class="layui-col-md3" style="margin:5px 0 ;">
				<form>
					
				</form>
				<input id="bdbm_departName" type="text" class="layui-input" placeholder="部门名称" style="font-size:15px;">
			</div>
			
			<div class="layui-col-md2 layui-col-md-offset1" style="text-align:center;margin:5px 0;">
				<button class="layui-btn layui-btn-sm" id="copy_searchForm_btn">查询</button>
			</div>
		</div>
	</div>
	<div class="middle1" style="height: 400px;">
			<form class="form-horizontal" id="sysUserDepartmentForm" action="sysUserDepartment/addSysUserDepartments" method="post"   onsubmit="return validateCallback(this,addSysUserDepartmentsuccess);">
		<table class="layui-table backlog_table" lay-even lay-skin="nob">
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
			      <th>部门名称</th>
			      <th>部门代码</th>
			      <th>部门负责人</th>
			    </tr>  
			</thead>
			<tbody id="tabletr1"></tbody>
		</table>	
			<input type="hidden" name="userUid" id="bdbm_userUid"/>
			<input type="hidden" name="isManager" id="bdbm_isManager"/>
			</form>
		<div id="pagination1"></div>
	</div>
	
	<div class="middle1">
				<button  id="bdbm">绑定</button>
			</div>
			<div class="middle1" style="height: 400px;">
				<table class="layui-table backlog_table" lay-even lay-skin="nob">
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
					      <th>部门名称</th>
					      <th>客户</th>
					      <th>部门负责人</th>
					      <th>操作</th>
					    </tr>  
					</thead>
					<tbody id="tabletr2"></tbody>
				</table>	
			</div>
			<div id="lay_page_copy"></div>
			<div class="foot">
				<button class="layui-btn layui-btn layui-btn-primary cancel_btn" onclick="$('.display_container3').css('display','none');">取消</button>
			</div>
</body>

<script type="text/javascript">
	function serachDeparmet(){
		$.ajax({
			type:'POST',
			url:"sysDepartment/allSysDepartment",
			dataType:"json",
			success: function(data){
				laypage({
			        cont: 'pagination1',
			        pages: Math.ceil(data.total / data.pageSize),
			        curr: data.pageNo || 1,
			        group: 5,
			        skip: true,
			        jump: function (obj, first) {
			            if (!first) {
			            	pageBreak(obj.curr);
			            }
			        }
			    });
				var dataList = data.dataList;
			 	$("#tabletr1").empty();//清空表格内容
			    if (dataList.length > 0 ) {
			    	/* tabledata(dataList,data); */
			    	$(dataList).each(function(i){//重新生成
						var str='<tr>';
						str+='<td><input type="checkbox" name="departUid"  value="' + this.departUid + '"/>' + (data.beginNum+i) + '</td>';
			         	str+='<td>' + this.departName + '</td>';
			         	str+='<td>' + this.departNo + '</td>';
			         	/* str+='<td>' + this.departAdmins + '</td>'; */
			         	str+='<td><select ><option value="false">否</option><option value="true">是</option></select></td>'; 
			         	str+='</tr>';
			         	$("#tabletr1").append(str);
			         });
			    	
			     } else {       	
			    	var colspan= $('#tabletr1').prev().find("th").length;
			    	$("#tabletr1").append('<tr><td colspan ="'+colspan+'"><center>'+language.query_no_data+'</center></td></tr>');
			     }
			}
		});
		
		$.ajax({
			type:'POST',
			url:"sysUserDepartment/selectAll?userUid="+userUid,
			dataType:"json",
			success: function(data){
				var dataList = data;
			 	$("#tabletr2").empty();//清空表格内容
			    if (typeof(dataList) != "undefined" && dataList.length > 0 ) {
			    	/* tabledata(dataList,data); */
			    	$(dataList).each(function(i){//重新生成
						var str='<tr>';
						str+='<td>' + (i+1) + '</td>';
			         	str+='<td>' + this.departName + '</td>';
			         	str+='<td>' + this.userName + '</td>';
			         	if(this.isManager=='false'){
				         	str+='<td><select ><option value="false">否</option><option value="true">是</option></select></td>'; 
			         	}else{
			         		str+='<td><select ><option value="true">是</option><option value="false">否</option></select></td>'; 
			         	}
			         	str+='<td><i class="layui-icon delete_btn" onclick=ajaxTodo("sysUserDepartment/deleteSysUserDepartment?uduid='+this.uduid+'","del") >&#xe640;</i></td>';
			         	str+='</tr>';
			         	$("#tabletr2").append(str);
			         });
			     } else {       	
			    	var colspan= $('#tabletr2').prev().find("th").length;
			    	$("#tabletr2").append('<tr><td colspan ="'+colspan+'"><center>'+language.query_no_data+'</center></td></tr>');
			     }
			}
		});
	}
</script>
</html>