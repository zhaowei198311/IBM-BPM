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
  		<title>待办任务</title>
  		<%@ include file="common/common.jsp" %>
  		<style>

		</style>
	</head>
	<body>
		<!-- 初始化页面 -->
		<div class="container">
			<div class="search_area">
				<div class="layui-row">
					<form class="form-inline layui-form" method="post" action="sysDictionary/getSysDictionaryList"  onsubmit="return search(this);">
						<input type="hidden" name="pageNo" id="pageNo" value="1" />
						<div class="layui-col-md2">
							<input type="text" name="dicCode" placeholder="请输入字典类型编码" autocomplete="off" class="layui-input"/>
						</div>
						<div class="layui-col-md2">
							<input type="text" name="dicName" placeholder="请输入字典类型名称" lay-search="" autocomplete="off" class="layui-input"/>
						</div>
						<div class="layui-col-md1" style="text-align:right;">
							<button class="layui-btn create_btn" type="submit">检索</button>
						</div>
						<div class="layui-col-md1" style="text-align:right;">
							<button class="layui-btn create_btn" type="button"  onclick="adddialog()">新建</button>
						</div>
					</form>
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
					      <th>字典类型编码</th>
					      <th>字典类型名称</th>
					      <th>字典类型说明</th>
					      <th>状态</th>
					      <th>操作</th>
					    </tr> 
					</thead>
					<tbody id="tabletr">

					</tbody>
				</table>
			</div>
			<div id="pagination"></div>
		</div>
		<!-- 新建页面 -->
		<div class="display_container" >
		<div class="display_content" style="height:auto;" >
			<div class="top">
				新建
			</div>
			<div class="middle" style="height:auto;" >
				<form class="layui-form form-horizontal" action="sysDictionary/saveSysDictionary" method="post" style="margin-top:30px;"  onsubmit="return validateCallback(this,addsuccess);">
				  <div class="layui-form-item">
				    <label class="layui-form-label">类型编码</label>
				    <div class="layui-input-block">
				      <input type="text" name="dicCode " required lay-verify="required" autocomplete="off" class="layui-input" />
				    </div>
				  </div>
			  	  <div class="layui-form-item">
				    <label class="layui-form-label">类型名称</label>
				    <div class="layui-input-block">
				      <input type="text" name="dicName" required lay-verify="required" autocomplete="off" class="layui-input" />
				    </div>
				  </div>
				  <div class="layui-form-item">
				    <label class="layui-form-label">类型说明</label>
				    <div class="layui-input-block">
				      <input type="text" name="dicDescription" autocomplete="off" class="layui-input" />
				    </div>
				  </div>
				  <div class="layui-form-item">
				    <label class="layui-form-label">状态</label>
				    <div class="layui-input-block">
				      <input type="radio" name="dicStatus" value="on" title="启用" checked />
				      <input type="radio" name="dicStatus" value="off" title="禁用"  />
				    </div>
				  </div>
				  
				  <input type="hidden" id="submit_add" />
				</form>
			</div>
			<div class="foot">
				<button class="layui-btn layui-btn sure_btn" type="button" onclick="$('#submit_add').submit();" >确定</button>
				<button class="layui-btn layui-btn layui-btn-primary cancel_btn">取消</button>
			</div>
		</div>
	</div>
	<!-- 编辑页面 -->
	<div class="display_container1">
		<div class="display_content1" style="height:auto;">
			<div class="top">
				编辑
			</div>
			<div class="middle" style="height:auto;" >
				<form class="layui-form form-horizontal" action="sysDictionary/updateSysDictionary" method="post" style="margin-top:30px;"  onsubmit="return validateCallback(this,updatesuccess);">
				  <div class="layui-form-item">
				    <label class="layui-form-label">类型编码</label>
				    <div class="layui-input-block">
				      <input type="text" name="dicCode" required lay-verify="required" autocomplete="off" class="layui-input" />
				    </div>
				  </div>
			  	  <div class="layui-form-item">
				    <label class="layui-form-label">类型名称</label>
				    <div class="layui-input-block">
				      <input type="text" name="dicName" required lay-verify="required" autocomplete="off" class="layui-input" />
				    </div>
				  </div>
				  <div class="layui-form-item">
				    <label class="layui-form-label">类型说明</label>
				    <div class="layui-input-block">
				      <input type="text" name="dicDescription" autocomplete="off" class="layui-input" />
				    </div>
				  </div>
				  <div class="layui-form-item">
				    <label class="layui-form-label">状态</label>
				    <div class="layui-input-block">
				      <input type="radio" name="dicStatus" value="on" title="启用" checked />
				      <input type="radio" name="dicStatus" value="off" title="禁用"  />
				    </div>
				  </div>
				  
				  <input type="hidden" name="dicUid" />
				  <input type="hidden" id="submit_upd" />
				</form>
			</div>
			<div class="foot">
				<button class="layui-btn layui-btn sure_btn" type="button"  onclick="$('#submit_upd').submit();">确定</button>
				<button class="layui-btn layui-btn layui-btn-primary cancel_btn">取消</button>
			</div>
		</div>
	</div>
	
	<script type="text/javascript" >
		$(function(){
			var url='sysDepartment/treeDisplay';
			pageBreak($('#pageNo').val());
			$(".cancel_btn").click(function(){
				$(".display_container").css("display","none");
				$(".display_container1").css("display","none");
			});
		})
		
		function tabledata(dataList,data){
			 $(dataList).each(function(i){//重新生成
				var str='<tr value="'+this.dicUid+'" >';
				str+='<td>' + (data.beginNum+i) + '</td>';
				str+='<td>' + this.dicCode + '</td>';
				str+='<td>' + this.dicName + '</td>';
				str+='<td>' + isEmpty(this.dicDescription) + '</td>';
				if(this.dicStatus == 'on'){
	         		str+='<td>启用</td>';
	         	}else{
	         		str+='<td>禁用</td>';
	         	}
		        str+='<td>';
		        str+='<i class="layui-icon edit_user" onclick=ajaxTodo("sysDictionary/getSysDictionaryById?dicUid='+this.dicUid+'","edit") >&#xe642;</i>';
		        str+='<i class="layui-icon edit_user" onclick=ajaxTodo("sysDictionary/deleteSysDictionary?dicUid='+this.dicUid+'","del") >&#xe640;</i>';
		        str+='</td>';
	         	$("#tabletr").append(str);
	         });
		}
	</script>
	</body>
</html>
