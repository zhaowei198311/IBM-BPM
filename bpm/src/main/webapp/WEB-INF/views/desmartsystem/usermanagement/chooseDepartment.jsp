<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
<title>选择部门</title>
<%-- <%@ include file="common/common.jsp" %> --%>
<link href="resources/desmartsystem/styles/css/layui.css" rel="stylesheet"/>
<!-- <link rel="stylesheet" href="styles/css/modules/laydate/default/laydate.css" /> -->
<link href="resources/desmartsystem/styles/css/my.css" rel="stylesheet" />
<link rel="stylesheet" href="resources/desmartsystem/tree/css/demo.css" type="text/css">
<link rel="stylesheet" href="resources/desmartsystem/tree/css/zTreeStyle/zTreeStyle.css" type="text/css">

<link type="text/css" href="resources/desmartsystem/scripts/laypage/1.2/skin/laypage.css">
<script type="text/javascript" src="resources/desmartsystem/scripts/laypage/1.2/laypage.js"></script>

<script type="text/javascript" src="resources/desmartsystem/scripts/js/jquery-3.3.1.js" /></script>
<script type="text/javascript" src="resources/desmartsystem/scripts/js/layui.all.js"></script>	
<script type="text/javascript" src="resources/desmartsystem/tree/js/jquery.ztree.core.js"></script>
<script type="text/javascript" src="resources/desmartsystem/tree/js/jquery.ztree.excheck.js"></script>
<script type="scripts/text/javascript" src="resources/desmartsystem/scripts/js/validate_util/jquery.form.min.js" ></script>
<script src="resources/desmartsystem/scripts/js/validate_util/jquery.validate.js" type="text/javascript"></script>
<script src="resources/desmartsystem/scripts/js/validate_util/dwz.util.date.js" type="text/javascript"></script>
<script src="resources/desmartsystem/scripts/js/validate_util/dwz.validate.method.js" type="text/javascript"></script>
<script src="resources/desmartsystem/scripts/js/validate_util/dwz.regional.zh_CN.js" type="text/javascript"></script>
<script src="resources/desmartsystem/scripts/js/myjs/myajax.js" type="text/javascript"></script>
<script src="resources/desmartsystem/scripts/js/myjs/role_user.js" type="text/javascript"></script>
<style>
	#usersul li, #user_add li{list-style-type:none;padding-left:12px;padding-top:2px;padding-bottom:2px;border-bottom:1px solid #CCC;}
	#usersul , #user_add{list-style-type:none;padding-left:0px;width:100%;}
	.colorli{background-color:#9DA5EC;color: white;}
	ul{ width:200px;}
</style>
</head>
<body>
			<div class="query_user" style="width:85%;margin:10px 5px 0 30px;">
				<div>
					<table>
						<tbody>
							<tr>
								<td>部门编码:</td>
								<td>部门名称:</td>
								<td></td>
							</tr>
							<tr>
								<td><input type="text" id="depart_code" name="departNo" autocomplete="off" class="layui-input" /></td>
								<td><input type="text" id="depart_name" name="departName" autocomplete="off" class="layui-input" /></td>
								<td>
									<button id="query_btn" class="layui-btn layui-btn sure_btn" style="margin-top: -20px;">查询</button>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
			<div class="middle_temp" style="top:10px;height: 350px;">	
				<div style="width:85%;height: 350px;margin:auto;">
					<ul id="treeDemo" class="ztree" style="width: 98%; height: 350px; -moz-user-select: none;"></ul>
				</div>
			</div>
			<div class="foot_temp">
				<button class="layui-btn layui-btn sure_btn" type="button" id="addDepart">确定</button>
				<button class="layui-btn layui-btn layui-btn-primary cancel_btn" id="close">取消</button>
			</div>
	<script type="text/javascript">
		var setting1 = {
			async: {
				enable: true,//是否开启异步加载模式
				url: "/sysDepartment/treeDisplay",
				autoParam: ["id"]
			},
			data: {simpleData: {enable: true}}
		};
	
		var elementId='';
		var treeObj = null;
		var selectedNode = "";
		var expandNodes = "";
		$(function(){
			var url='/sysDepartment/treeDisplay';
			//tree展示
			setting1.callback={
				onClick: onClick,
				onAsyncSuccess: zTreeOnAsyncSuccess
			}
			$.ajax({ 
		        url: '<%=request.getContextPath()%>'+url,    //后台webservice里的方法名称  
		        type: "post",  
		        async:false,
		        dataType: "json",  
		        success: function (data) {
		        	$.fn.zTree.init($("#treeDemo"), setting1, data);
		        }
		    });
			
			treeObj = $.fn.zTree.getZTreeObj("treeDemo");
			elementId='${elementId}';
			var departNo = window.parent.document.getElementById(elementId).value;
			if(departNo!=null && departNo!=""){
				$.ajax({
					url:'<%=request.getContextPath()%>/sysDepartment/queryDepartByNoAndName',
					method:"post",
					async:false,
					data:{
						departNo:departNo
					},
					success:function(result){
						if(result.status==0){
							var departObj = result.data;
							selectedNode = departObj;
							treeObj.expandAll(false);
							asyncExpandNodes(departObj);
						}
					}
				});
			}
			
			//获得组织树种被选中的节点对象
			$("#addDepart").click(function(){
				var checkNode = treeObj.getSelectedNodes(true);
				if(null==checkNode){
					layer.alert("请选择一个部门");
				}else{
					window.parent.document.getElementById(elementId).value = checkNode[0].id;
					window.parent.document.getElementById(elementId+"_view").value = checkNode[0].name;
					$('#close').click();
				}
			});
			
			//模糊查询树种的某个组织
			$("#query_btn").click(function(){
				var departCode = $("#depart_code").val().trim();
				var departName = $("#depart_name").val().trim();
				$.ajax({
					url:"sysDepartment/queryDepartByNoAndName",
					method:"post",
					data:{
						departNo:departCode,
						departName:departName
					},
					success:function(result){
						if(result.status==0){
							var departObj = result.data;
							selectedNode = departObj;
							treeObj.expandAll(false);
							asyncExpandNodes(departObj);
						}
					}
				});
			});
		});
		
		//根据部门id找到上级部门，一直到顶层为止
		function asyncExpandNodes(departObj){
			$.ajax({
				url:"sysDepartment/queryDepartParentsByDepartId",
				method:"post",
				data:{
					departUid:departObj.departUid
				},
				success:function(result){
					if(result.status==0){
						expandNodes = result.data;
						zTreeOnAsyncSuccess();
					}
				}
			});
		}
		
		function zTreeOnAsyncSuccess(){
			var length = expandNodes.length-1;
			for(var i=length;i>=0;i--){
				var treeNodeId = expandNodes[i].departUid;
				if(!treeNodeIsExpand(treeNodeId)){
					continue;
				}
			}
			var selNodeObj = treeObj.getNodeByParam("id", selectedNode.departUid);
			if(selNodeObj!=null){
				treeObj.cancelSelectedNode();
				treeObj.selectNode(selNodeObj, true);
			}
		}
		
		function treeNodeIsExpand(nodeId){
			var nodeObj = treeObj.getNodeByParam("id", nodeId);
			if(null==nodeObj || nodeObj.open){
				return false;
			}else{
				treeObj.expandNode(nodeObj, true, false); 
				return true;
			}
		}
		
		function onClick(e, treeId, treeNode) {
			treeObj.cancelSelectedNode();
			treeObj.selectNode(treeNode,true);
		}
	</script>
</body>
</html>