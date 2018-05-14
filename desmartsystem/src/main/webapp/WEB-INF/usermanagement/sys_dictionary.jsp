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
			<form class="form-inline" method="post" action="sysDictionary/getSysDictionaryList"  onsubmit="return search(this);">
				<input type="hidden" name="pageNo" id="pageNo" value="1" />
				<input type="hidden" name="roleType" value="1" />
			</form>
			<div class="search_area">
				<div class="layui-row layui-form">
					<div class="layui-col-md1" style="text-align:right;width: 250px;">
						<input type="text" id="dicName" placeholder="请输入内容" autocomplete="off" class="layui-input"/>
					</div>
					<div class="layui-col-md1" style="text-align:right;">
						<button class="layui-btn create_btn" onclick="search_1()">检索</button>
					</div>
					<div class="layui-col-md1" style="text-align:right;">
						<button class="layui-btn create_btn" onclick="adddialog()">新建</button>
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
		<div class="display_content" style="height: 350px;">
			<div class="top">
				新建
			</div>
			<div class="middle" style="height: 250px;">
				<form class="layui-form form-horizontal" action="sysDictionary/saveSysDictionary" method="post" style="margin-top:30px;"  onsubmit="return validateCallback(this,addsuccess);">
				  <div class="layui-form-item">
				    <label class="layui-form-label">编码</label>
				    <div class="layui-input-block">
				      <input type="text" name="dicCode" required lay-verify="required" autocomplete="off" class="layui-input" onkeyup="value=value.replace(/[^\d]/g,'') "/>
				    </div>
				  </div>
			  	  <div class="layui-form-item">
				    <label class="layui-form-label">字段名</label>
				    <div class="layui-input-block">
				      <input type="text" name="dicName" required lay-verify="required" autocomplete="off" class="layui-input" />
				    </div>
				  </div>
				  <div class="layui-form-item">
				    <label class="layui-form-label">描述</label>
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
		<div class="display_content1" style="height: 350px;">
			<div class="top">
				编辑
			</div>
			<div class="middle" style="height: 250px;">
				<form class="layui-form form-horizontal" action="sysDictionary/updateSysDictionary" method="post" style="margin-top:30px;"  onsubmit="return validateCallback(this,updatesuccess);">
				  <div class="layui-form-item">
				    <label class="layui-form-label">编码</label>
				    <div class="layui-input-block">
				      <input type="text" name="dicCode" required lay-verify="required" autocomplete="off" class="layui-input" onkeyup="value=value.replace(/[^\d]/g,'') "/>
				    </div>
				  </div>
			  	  <div class="layui-form-item">
				    <label class="layui-form-label">字段名</label>
				    <div class="layui-input-block">
				      <input type="text" name="dicName" required lay-verify="required" autocomplete="off" class="layui-input" />
				    </div>
				  </div>
				  <div class="layui-form-item">
				    <label class="layui-form-label">描述</label>
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
	
<!-- 	<div class="display_container6"> -->
<!-- 		<div class="display_content6"> -->
<!-- 			<div class="top"> -->
<!-- 				授权菜单 -->
<!-- 			</div> -->
<!-- 			<form method="post"  action="sysRoleResource/adSysRoleResource"   onsubmit="return validateCallback(this,closeResourceDialog);"> -->
<!-- 				<div class="middle" style="padding: 0px;"> -->
<!-- 					<ul id="resourceTree" class="ztree" style="width:auto;height:189px;"></ul> -->
<!-- 				</div> -->
<!-- 				<div class="foot"> -->
<!-- 					<button class="layui-btn layui-btn sure_btn" type="button" id="addresource">确定</button> -->
<!-- 					<button class="layui-btn layui-btn layui-btn-primary cancel_btn" type="button">取消</button> -->
<!-- 				</div> -->
<!-- 				<div id="resource"></div> -->
<!-- 				<input type="hidden" name="roleUid" id="roleUid1" /> -->
<!-- 			</form> -->
<!-- 		</div> -->
<!-- 	</div> -->
	
	<!-- 双击行跳转至详情页面 -->
	<div class="display_container5">
		<div class="display_content5">
			<div class="top">
				数据字典详情
			</div>
			<div class="search_area">
				<div class="layui-row layui-form">		
					<div class="layui-col-md1" style="text-align:right;width: 250px;">
						<input type="text" id="dicDataName" placeholder="请输入内容" autocomplete="off" class="layui-input" />
					</div>
					<div class="layui-col-md1" style="text-align:right;margin-left: 20px;">
						<button class="layui-btn create_btn" onclick="search_2()">检索</button>
					</div>			
					<div class="layui-col-md1" style="text-align:right;margin-left: 30px;">
						<button class="layui-btn create_btn" onclick="adddialog_1()">新建</button>
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
					      <th>排序号</th>
					      <th>状态</th>
					      <th>操作</th>
					    </tr> 
					</thead>
					<tbody id="tabletrDetail">

					</tbody>
				</table>
			</div>
<!-- 			<div id="pagination"></div> -->
			<div class="foot" style="position: absolute;bottom: 0px;right: 5px;">
				<button class="layui-btn layui-btn layui-btn-primary cancel_btn">取消</button>
			</div>
		</div>
	</div>
	<!-- 新建详情页面 -->
	<div class="display_container2">
		<div class="display_content2" style="min-height: 410px;">
			<div class="top">
				新建详情
			</div>
			<div class="middle" style="height: 300px;">
				<form class="layui-form form-horizontal" action="sysDictionary/saveSysDictionaryData" method="post" style="margin-top:30px;"  onsubmit="return validateCallback(this,addsuccess_1);">
				  <div class="layui-form-item">
				    <label class="layui-form-label">编码</label>
				    <div class="layui-input-block">
				      <input type="text" name="dicDataCode" required lay-verify="required" autocomplete="off" class="layui-input" onkeyup="value=value.replace(/[^\d]/g,'') "/>
				    </div>
				  </div>
			  	  <div class="layui-form-item">
				    <label class="layui-form-label">字段名</label>
				    <div class="layui-input-block">
				      <input type="text" name="dicDataName" required lay-verify="required" autocomplete="off" class="layui-input" />
				    </div>
				  </div>
				  <div class="layui-form-item">
				    <label class="layui-form-label">描述</label>
				    <div class="layui-input-block">
				      <input type="text" name="dicDataDescription" autocomplete="off" class="layui-input" />
				    </div>
				  </div>
				  <div class="layui-form-item">
				    <label class="layui-form-label">排序号</label>
				    <div class="layui-input-block">
				      <input type="text" name="dicDataSort" autocomplete="off" class="layui-input" onkeyup="value=value.replace(/[^\d]/g,'') "/>
				    </div>
				  </div>
				  <div class="layui-form-item">
				    <label class="layui-form-label">状态</label>
				    <div class="layui-input-block">
				      <input type="radio" name="dicDataStatus" value="on" title="启用" checked />
				      <input type="radio" name="dicDataStatus" value="off" title="禁用"  />
				    </div>
				  </div>
				  
				  <input type="hidden" id="dicUid" name="dicUid"/>
				  <input type="hidden" id="submit_add_a" />
				</form>
			</div>
			<div class="foot">
				<button class="layui-btn layui-btn sure_btn" type="button" onclick="$('#submit_add_a').submit();" >确定</button>
				<button class="layui-btn layui-btn layui-btn-primary cancel_btn">取消</button>
			</div>
		</div>
	</div>
	<!-- 编辑详情页面 -->
	<div class="display_container3">
		<div class="display_content3" style="min-height: 410px;">
			<div class="top">
				编辑详情
			</div>
			<div class="middle" style="height: 300px;">
				<form class="layui-form form-horizontal" action="sysDictionary/updateSysDictionaryData" method="post" style="margin-top:30px;"  onsubmit="return validateCallback(this,updatesuccess_1);">
				  <div class="layui-form-item">
				    <label class="layui-form-label">编码</label>
				    <div class="layui-input-block">
				      <input type="text" name="dicDataCode" required lay-verify="required" autocomplete="off" class="layui-input" onkeyup="value=value.replace(/[^\d]/g,'') "/>
				    </div>
				  </div>
			  	  <div class="layui-form-item">
				    <label class="layui-form-label">字段名</label>
				    <div class="layui-input-block">
				      <input type="text" name="dicDataName" required lay-verify="required" autocomplete="off" class="layui-input" />
				    </div>
				  </div>
				  <div class="layui-form-item">
				    <label class="layui-form-label">描述</label>
				    <div class="layui-input-block">
				      <input type="text" name="dicDataDescription" autocomplete="off" class="layui-input" />
				    </div>
				  </div>
				  <div class="layui-form-item">
				    <label class="layui-form-label">排序号</label>
				    <div class="layui-input-block">
				      <input type="text" name="dicDataSort" autocomplete="off" class="layui-input" onkeyup="value=value.replace(/[^\d]/g,'') "/>
				    </div>
				  </div>
				  <div class="layui-form-item">
				    <label class="layui-form-label">状态</label>
				    <div class="layui-input-block">
				      <input type="radio" name="dicDataStatus" value="on" title="启用" checked />
				      <input type="radio" name="dicDataStatus" value="off" title="禁用"  />
				    </div>
				  </div>
				  
				  <input type="hidden" name="dicDataUid" />
				  <input type="hidden" id="submit_upd_a" />
				</form>
			</div>
			<div class="foot">
				<button class="layui-btn layui-btn sure_btn" type="button" onclick="$('#submit_upd_a').submit();" >确定</button>
				<button class="layui-btn layui-btn layui-btn-primary cancel_btn">取消</button>
			</div>
		</div>
	</div>
	
<%-- 	<jsp:include page="common/role_user.jsp" > --%>
<%-- 	    <jsp:param name="mapType" value="1"/>   --%>
<%-- 	</jsp:include> --%>
	
	<script src="scripts/js/myjs/role_user.js" type="text/javascript"></script>
	<script type="text/javascript" >
		$(function(){
			var url='sysDepartment/treeDisplay';
			// tree展示
			setting.callback={onClick: onClick}
			treeDisplay(url,'treeDemo');
			pageBreak($('#pageNo').val());
			$(".cancel_btn").click(function(){
				$(".display_container").css("display","none");
				$(".display_container1").css("display","none");
				$(".display_container6").css("display","none");
				$(".display_container5").css("display","none");
				$(".display_container2").css("display","none");
				$(".display_container3").css("display","none");
			});
					
			// 新增保存
			$("#addresource").click(function(){
				var treeObj = $.fn.zTree.getZTreeObj("resourceTree");
				var nodes = treeObj.getCheckedNodes();
				var str="";
				for(var i=0;i<nodes.length;i++){
					str+="<input type='hidden' name='resourceUid' value='"+nodes[i].id+"'/>";
				}
				$("#resource").html(str);
				$(this).submit();
			});
		})
		// 主页面id
		var dicUid_1 = "";
		// 初始化加载
		function tabledata(dataList,data){
			 $(dataList).each(function(i){//重新生成
				var str='<tr value="'+this.dicUid+'" >';
				str+='<td>' + (data.beginNum+i) + '</td>';
				str+='<td>' + this.dicCode + '</td>';
				str+='<td>' + this.dicName + '</td>';
				str+='<td>' + this.dicDescription + '</td>';
				if(this.dicStatus == 'on'){
	         		str+='<td>启用</td>';
	         	}else{
	         		str+='<td>禁用</td>';
	         	}
		        str+='<td>';
		        str+='<i class="layui-icon edit_user" onclick=ajaxTodo("sysDictionary/getSysDictionaryById?dicUid='+this.dicUid+'","edit") >&#xe642;</i>';
// 		        str+='<i class="layui-icon add_user" onclick=openRoleUsers("'+this.roleUid+'")  >&#xe654;</i>';
		        str+='</td>';
	         	$("#tabletr").append(str);
	         });
			 // 为每一行设置双击跳转事件
			 $("#tabletr").on("dblclick","tr",function(){
				 // 显示详情页面
				$(".display_container5").css("display","block");
				$("#tabletrDetail").empty();
				$("#dicDataName").val("");
				// 选中行id
				var dicUid = ($(this).attr('value'));
				dicUid_1 = dicUid;
				$.ajax({
					async: false,
					url: "sysDictionary/getSysDictionaryDataList",
					type: "post",
					dataType: "json",
					data: {
						dicUid: dicUid
					},
					success: function(data){
						tableDetail(data);
					}
				})
			});
		}
		// 跳转页面加载
		function tableDetail(data){
			var dataList = data.dataList;
			$(dataList).each(function(i){//重新生成
				var str='<tr>';
				str+='<td>' + (data.beginNum+i) + '</td>';
				str+='<td>' + this.dicDataCode + '</td>';
				str+='<td>' + this.dicDataName + '</td>';
				str+='<td>' + this.dicDataDescription + '</td>';
				str+='<td>' + this.dicDataSort + '</td>';
				if(this.dicDataStatus == 'on'){
	         		str+='<td>启用</td>';
	         	}else{
	         		str+='<td>禁用</td>';
	         	}
		        str+='<td>';
		        str+='<i class="layui-icon edit_user" onclick=edit_1("sysDictionary/getSysDictionaryDataById?dicDataUid='+this.dicDataUid+'") >&#xe642;</i>';
//			        str+='<i class="layui-icon jurisdiction_btn" onclick=openResourceDialog("'+this.dicUid+'"); >&#xe6b2;</i>';
		        str+='<i class="layui-icon delete_btn" onclick=ajaxTodo("sysDictionary/deleteSysDictionaryData?dicDataUid='+this.dicDataUid+'","del") >&#xe640;</i>';
		        str+='</td>';
	         	$("#tabletrDetail").append(str);
	         });
		}
		// 检索
		function search_1(){
			var dicName = $("#dicName").val();
			$.ajax({
				async: false,
				url: "sysDictionary/getSysDictionaryList",
				type: "post",
				dataType: "json",
				data: {
					dicName: dicName
				},
				success: function(data){
					// 清空tbody，重新加载
					table(data);
				}
			})	
		}
		// 详情检索
		function search_2(){
			var dicDataName = $("#dicDataName").val();
			$.ajax({
				async: false,
				url: "sysDictionary/getSysDictionaryDataList",
				type: "post",
				dataType: "json",
				data: {
					dicUid: dicUid_1,
					dicDataName: dicDataName
				},
				success: function(data){
					// 清空tbody
					$("#tabletrDetail").empty();
					tableDetail(data);
				}
			})	
		}
		// 新建详情页面
		function adddialog_1(){
			$("#dicUid").val(dicUid_1);
			var $dailogs=$('.display_container2');
			$dailogs.css("display","block");
			$('form',$dailogs)[0].reset();
			$('form',$dailogs).validate().resetForms();
			$('form',$dailogs).find("input, select, textarea").removeClass('error');
		}
		// 编辑详情页面
		function edit_1(url){
			$.ajax({
				async: false,
				url: url,
				type: "post",
				dataType: "json",
				success: function(data){
					var $dailogs=$('.display_container3');
					$dailogs.css("display","block");
					$('form',$dailogs)[0].reset();
					$('form',$dailogs).formEdit(data);
					$('form',$dailogs).validate().resetForms();
					$('form',$dailogs).find("input, select, textarea").removeClass('error');
				}
			})			
		}
		//添加成功关闭dialog
		function addsuccess_1(data){
			if(data.msg=='success'){
				layer.alert(language.add_success);
				dgclose("display_container2");
				search_2();
			}else{
				if(data.msg != 'error'){
					layer.alert(data.msg);
				}else {
					layer.alert(language.add_failed);
				}
			}
		}

		//修改成功关闭dialog
		function updatesuccess_1(data){
			if(data.msg=='success'){
				layer.alert(language.modify_successfully);
				dgclose("display_container3");
				search_2();
			}else{
				if(data.msg != 'error'){
					layer.alert(data.msg);
				}else {
					layer.alert(language.add_failed);
				}
			}
		}
	</script>
	</body>
</html>
