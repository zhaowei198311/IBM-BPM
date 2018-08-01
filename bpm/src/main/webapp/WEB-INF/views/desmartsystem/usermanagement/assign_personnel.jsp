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
<title>选择人员</title>
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
			<div class="query_user" style="width:85%;margin:10px 5px 0 25px;">
				<div>
					<form class="form-inline" method="post" action="sysUser/userList"  onsubmit="return searchReturn(this,setUserList);">
					<table>
						<tbody>
							<tr>
								<td>员工编码:</td>
								<td>员工姓名:</td>
								<td></td>
							</tr>
							<tr>
								<td><input type="text" id="user_code" name="userNo" autocomplete="off" class="layui-input" /></td>
								<td><input type="text" id="user_name" name="userName" autocomplete="off" class="layui-input" /></td>
								<td><!-- <input type="submit" id="query_btn" autocomplete="off" class=".layui-btn sure_btn" value="查询"/> -->
									<button type="submit" id="query_btn" class="layui-btn layui-btn sure_btn" style="margin-top: -20px;">查询</button>
									
								</td>
							</tr>
						</tbody>
					</table>
					</form>
				</div>
			</div>
			<div class="middle_temp">	
				<div style="float:left;width:220px;height: 240px;margin-left: 15px;">
					<ul id="treeDemo" class="ztree" style="width: 220px; height: 250px; -moz-user-select: none;"></ul>
				</div>
				
				<div id="temp_middle" style="float:left;width:134px;height:260px;" >
					<ul id="usersul" style="width:240px;display:inline;" >
					</ul>
				</div>
				<div id="temp_button">
					<br></br>
					<br></br>
					<button type="button" class="btn btn-default btn-xs" style="font-weight:800;color:blue;text-align:left;" onclick="add_user();">&nbsp;&nbsp;&gt;&nbsp;&nbsp;</button>
					<br></br>
					<button type="button" class="btn btn-default btn-xs" style="font-weight:800;color:blue;text-align:left;" onclick="delete_user();">&nbsp;&nbsp;&lt;&nbsp;&nbsp;</button>
				</div>
				<div id="temp_right" style="float:left;width:134px;height:260px;">
					<ul id="user_add" style="width:240px;display:inline;"></ul>
				</div>
			</div>
			<div class="foot_temp" style="margin:40px 20px 0 0;">
				<button class="layui-btn layui-btn sure_btn" style="float:left;" type="button" id="addpersonnel">确定</button>
				<button class="layui-btn layui-btn layui-btn-primary cancel_btn" id="close" style="float:left;">取消</button>
			</div>
	<script type="text/javascript">
		var assignPersonnelJson='${assignPersonnel}';
		var assignPersonnel=JSON.parse(assignPersonnelJson);
		var elementId=assignPersonnel.id;
		var isSingle=assignPersonnel.isSingle;
		var actcCanChooseUser=assignPersonnel.actcCanChooseUser;
		var actcAssignType=assignPersonnel.actcAssignType;
		var actcChooseableHandlerType=assignPersonnel.actcChooseableHandlerType;
		var taskUid = assignPersonnel.taskUid || '';
		
		$(function(){
			console.log(actcChooseableHandlerType);
			console.log(actcChooseableHandlerType=='allUsers');
			if(actcAssignType=='allUser'||actcChooseableHandlerType=='allUser'){
				console.log(actcChooseableHandlerType);
				$('#treeDemo').show();
				$('.query_user').show();
			}else{
				$('#treeDemo').hide();
				$('.query_user').hide();
			}
			var url='sysDepartment/treeDisplay';
			//tree展示
			setting.callback={onClick: onClick}
			treeDisplay(url,'treeDemo');
			
			var  useruid='';
			var  useruname='';
			//新增保存
			$("#addpersonnel").click(function(){
				var $user_add= $("#user_add li");
				if(isSingle=='true'){
					if($user_add.length>1){
						layer.alert('只能保存一个人!');
						return false;
					};
				}
				$user_add.each(function(){
					useruid+=$(this).attr('value')+";";
					useruname+=$(this).text()+";";
				});	
				window.parent.document.getElementById(elementId).value=useruid;
				window.parent.document.getElementById(elementId+"_view").value=useruname;
				var title = useruname.replace(/;/g,"\n");
				if(isSingle=='false'){
					window.parent.document.getElementById(elementId+"_view").title=title;
				}
			 	$('#close').click();
			});
			
			var $user_li=$("#user_add");
			$user_li.empty();
			//之前已配置的人员列表显示			
			var id = window.parent.document.getElementById(elementId).value.split(';');
			var name = window.parent.document.getElementById(elementId+"_view").value.split(';');
			for (var i = 0; i < name.length; i++) {
				if(name[i]!=''){
					var newName = name[i].replace(/\(.*?\)/g,'');
					var str='';
					str+="<li value='"+id[i]+"' onclick='selectClick(this);'>"+newName+"("+id[i]+")</li>";
					$user_li.append(str);
				}
			}
			
			if(actcChooseableHandlerType!='allUser'){
				var insUid=$("#insUid",parent.document).val();
				var formData=$("#formData",parent.document).text();
				var companyNum=$("#companyNum",parent.document).val();
				var departNo=$("#departNo",parent.document).val();
				
				$.ajax({
					type:'post',
					url:'dhRoute/choosableHandler',
					data:{
					    'insUid': insUid,
                        'companyNum': companyNum,
                        'departNo': departNo,
                        'activityId': elementId,
                        'formData': formData,
                        'taskUid': taskUid
					},
					dataType:'json',
					success: function (result){
						var $ul=$("#usersul");
						user_add_li(result.data,$ul);
					}
				});	
			}
			
		})
	
		
		function delete_user(){
			$("#user_add .colorli").remove();
		}
	
	
		function addUserRoleSuccess(data){
			returnSuccess(data,dialogs.add_team_dialog);
		}
			
		
		function selectClick(_this){
			if($(_this).hasClass("colorli")){
				$(_this).removeClass("colorli");
			}else{
				$(_this).addClass("colorli");
			}
		}
	
	
		function onClick(e, treeId, treeNode) {
			var departUid='';
			var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
			var sNodes = treeObj.getSelectedNodes();
			for (var i = 0; i < sNodes.length; i++) {
				departUid=sNodes[i].code;
			}
			departUid=departUid.replace(/\s/g, "")
			ajaxTodo('sysUser/userList?departUid='+departUid,'setUserList');
		}
	
	
		function setUserList(data){
			var $ul=$("#usersul");
			user_add_li(data,$ul);
		}
		
		function user_add_li(data,element,type){
			var $ul=element;
			$ul.empty();
			$("#usersul").empty();
			$(data).each(function(index){
				var str='';
				str+='<li type="hidden" value="'+this.userUid+'" actcCanChooseUser="true"  onclick="selectClick(this)" name="userUid">'+this.userName+'('+this.userUid+')</li>';
				$ul.append(str);
			});
		};
		
		
		function add_user(){
			var userids = [];
			$("#user_add li").each(function(){//遍历 右边栏目的ID 
				userids.push($(this).attr('value'));//获取 所有 已添加人员
			});
			$("#usersul li").each(function(){
				var $userLi=$(this);
				if($userLi.hasClass('colorli')){
					//判断 已添加人员
					var userUid=$userLi.attr('value');//用ID
					var departUid=$userLi.attr('departUid');//部门
					var roleUid=$userLi.attr('roleUid');
					var name=$userLi.text();
					if($.inArray(userUid, userids)==-1){
						var str='';
						str+="<li value='"+userUid+"' actcCanChooseUser='"+actcCanChooseUser+"' onclick='selectClick(this);'>"+name;
						str+="</li>";
						$("#user_add").append(str);
					}
				}
			});
		}
	
		function addUserRoleSuccess(data){
			returnSuccess(data,dialogs.add_team_dialog);
		}

	</script>
</body>
</html>