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
		<base href="<%=basePath%>">
		<meta charset="utf-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
  		<title>待办任务</title>
  		<%@ include file="common/common.jsp" %>
	</head>
	<body>
		<div class="container">
			<form class="form-inline" method="post" action="sysRole/allSysRole"  onsubmit="return search(this);">
				<input type="hidden" name="pageNo" id="pageNo" value="1" >
				<input type="hidden" name="roleType" value="0" >
			</form>
			<div class="search_area">
				<div class="layui-row layui-form">					
					<div class="layui-col-md1" style="text-align:right;">
					        <button class="layui-btn create_btn" onclick="adddialog();" >新建</button>
					</div>
				</div>
			</div>
			<div>				
				<table class="layui-table backlog_table" lay-even lay-skin="nob">
					<colgroup>
					    <col>
					    <col>
					    <col>
					    <col>
					    <col>
					</colgroup>
					<thead>
					    <tr>
					      <th>序号</th>
					      <th>角色名称</th>
					      <th>状态</th>
					      <th>菜单权限</th>
					      <th>操作</th>
					    </tr> 
					</thead>
					<tbody id="tabletr">
					  <!--   <tr>
					      <td>1</td>
					      <td>普通员工</td>
					      <td>显示</td>
					      <td>待办任务，未读通知...</td>
					      <td><i class="layui-icon edit_user">&#xe642;</i> <i class="layui-icon add_user">&#xe654;</i>  <i class="layui-icon jurisdiction_btn">&#xe6b2;</i> <i class="layui-icon delete_btn">&#xe640;</i></td>
					    </tr> -->
					</tbody>
				</table>
			</div>
			<div id="pagination"></div>
		</div>
	</body>
	<div class="display_container">
		<div class="display_content">
			<div class="top">
				新建角色
			</div>
			<div class="middle">
				<form class="layui-form form-horizontal" action="sysRole/addSysRole" style="margin-top:30px;"  onsubmit="return validateCallback(this,addsuccess);">
				  <div class="layui-form-item">
				    <label class="layui-form-label">角色名称</label>
				    <div class="layui-input-block">
				      <input type="text" name="roleName" required  lay-verify="required" placeholder="请输入角色名称" autocomplete="off" class="layui-input">
				    </div>
				  </div>
				  <div class="layui-form-item">
				    <label class="layui-form-label">状态</label>
				    <div class="layui-input-block">
				      <input type="radio" name="isClosed" value="1" title="显示" />
				      <input type="radio" name="isClosed" value="0" title="隐藏" checked />
				    </div>
				  </div>	
				  
				  <input type="hidden" name="orderIndex" value="1" />
				  <input type="hidden" name="categoryName" value="A"/>
				  <input type="hidden" name="roleType" value="1" />
				  <input type="hidden" id="submit_add" />			  
				</form>
			</div>
			<div class="foot">
				<button class="layui-btn layui-btn sure_btn" type="button" onclick="$('#submit_add').submit();" >确定</button>
				<button class="layui-btn layui-btn layui-btn-primary cancel_btn">取消</button>
			</div>
		</div>
	</div>
	<div class="display_container1">
		<div class="display_content1">
			<div class="top">
				编辑角色
			</div>
			<div class="middle">
				<form class="layui-form form-horizontal" action="sysRole/updateSysRole" style="margin-top:30px;"  onsubmit="return validateCallback(this,updatesuccess);">
				  <div class="layui-form-item">
				    <label class="layui-form-label">角色名称</label>
				    <div class="layui-input-block">
				      <input type="text" name="roleName" required  lay-verify="required" value="普通用户" autocomplete="off" class="layui-input">
				    </div>
				  </div>
				  <div class="layui-form-item">
				    <label class="layui-form-label">状态</label>
				    <div class="layui-input-block">
				        <input type="radio" name="isClosed" value="1" title="显示" />
				      <input type="radio" name="isClosed" value="0" title="隐藏" checked />
				    </div>
				  </div>				  
				   <input type="hidden" name="roleUid" / >
				   <input type="hidden" name="orderIndex" value="1" />
				  <input type="hidden" name="categoryName" value="A" />
				  <input type="hidden" name="roleType" value="1" />
				  <input type="hidden" id="submit_upd" />		
				</form>
			</div>
			<div class="foot">
				<button class="layui-btn layui-btn sure_btn" type="button"  onclick="$('#submit_upd').submit();">确定</button>
				<button class="layui-btn layui-btn layui-btn-primary cancel_btn">取消</button>
			</div>
		</div>
	</div>
	<div class="display_container2">
		<div class="display_content2">
			<div class="top">
				授权菜单
			</div>
			<div class="middle">
				<ul id="treeDemo" class="ztree" style="width:auto;height:200px;"></ul>
			</div>
			<div class="foot">
				<button class="layui-btn layui-btn sure_btn">确定</button>
				<button class="layui-btn layui-btn layui-btn-primary cancel_btn">取消</button>
			</div>
		</div>
	</div>
</html>
	<script>
		var setting = {
				check: {
					enable: true
				},
				data: {
					simpleData: {
						enable: true
					}
				}
			};
	
			var zNodes =[						
				{ id:1, pId:0, name:"全部", open:true},				
				{ id:11, pId:1, name:"待办任务"},				
				{ id:12, pId:1, name:"未阅通知"},
				{ id:13, pId:1, name:"已办任务"},				
				{ id:14, pId:1, name:"通知查询"},
				{ id:15, pId:1, name:"草稿箱"},
				{ id:16, pId:1, name:"发起跟踪"},
				{ id:17, pId:1, name:"委托设置"},
				{ id:18, pId:1, name:"高级选项"},
			];
			
			var code;
			
			function setCheck() {
				var zTree = $.fn.zTree.getZTreeObj("treeDemo"),
				py = $("#py").attr("checked")? "p":"",
				sy = $("#sy").attr("checked")? "s":"",
				pn = $("#pn").attr("checked")? "p":"",
				sn = $("#sn").attr("checked")? "s":"",
				type = { "Y":py + sy, "N":pn + sn};
				zTree.setting.check.chkboxType = type;
				showCode('setting.check.chkboxType = { "Y" : "' + type.Y + '", "N" : "' + type.N + '" };');
			}
			function showCode(str) {
				if (!code) code = $("#code");
				code.empty();
				code.append("<li>"+str+"</li>");
			}
		$(function(){
			pageBreak($('#pageNo').val());
			$(".cancel_btn").click(function(){
				$(".display_container").css("display","none");
				$(".display_container1").css("display","none");
				$(".display_container2").css("display","none");
			})
		})
		
		function tabledata(dataList,data){
			 $(dataList).each(function(i){//重新生成
				var str='<tr>';
				str+='<td>' + (data.beginNum+i) + '</td>';
				if(this.isClosed==1){
	         		str+='<td>显示</td>';
	         	}else{
	         		str+='<td>隐藏</td>';
	         	}
				str+='<td>' + this.roleName + '</td>';
				str+='<td></td>';
		        str+='<td>';
		        str+='<i class="layui-icon edit_user" onclick=ajaxTodo("sysRole/getSysRole?roleUid='+this.roleUid+'","edit") >&#xe642;</i>';
		        str+='<i class="layui-icon add_user">&#xe654;</i>';
		        str+='<i class="layui-icon jurisdiction_btn">&#xe6b2;</i>';
		        str+='<i class="layui-icon delete_btn" onclick=ajaxTodo("sysRole/deleteSysRole?roleUid='+this.roleUid+'","del") >&#xe640;</i>';
		        str+='</td>';
	         	$("#tabletr").append(str);
	         });
		}
		
	</script>