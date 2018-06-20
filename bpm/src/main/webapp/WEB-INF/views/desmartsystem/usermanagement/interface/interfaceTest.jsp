<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML>
<html>

<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>测试接口页面</title>
<%@ include file="../common/common.jsp" %>
<style type="text/css">
	.layui-form-label{width: 110px;}
</style>
</head>
<body>
	<fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
	  <legend>${intTitle} 请求参数</legend>
	</fieldset> 
	<!-- <form class="layui-form form-inline" method="post" action="interfaceExecute/interfaceSchedule"  onsubmit="return submitInterface(this);"> -->
	<form class="layui-form"  method="post" action="" >
		<input type="hidden"  value="${intUid}" id="intUid" />
			<c:forEach items="${dhInterfaceParameterList}" var="dhInterfaceParameter">
				<div class="layui-form-item">
			       <c:if test="${dhInterfaceParameter.paraType!='Array' &&empty dhInterfaceParameter.paraParent}">
				    <label class="layui-form-label">${dhInterfaceParameter.paraDescription}</label>
				    <div class="layui-input-inline">
				      <input name="${dhInterfaceParameter.paraName}"  maxlength="${dhInterfaceParameter.paraSize}"    autocomplete="off" class="layui-input  
				       <c:if test="${dhInterfaceParameter.isMust=='false'}">
				       		required
				       </c:if>
				       <c:if test="${dhInterfaceParameter.paraType=='Date'}">
				       		dateISO
				       </c:if>"
				       type="text">
				    </div>
			      </c:if>
			  </div>
			</c:forEach>
			
			<c:forEach items="${dhInterfaceParameterList}" var="dhInterfaceParameter">
				<c:if test="${dhInterfaceParameter.paraType=='Array'}">
					<input type="hidden" class="layui-input" id="array"  value="${dhInterfaceParameter.paraName}" />
				</c:if>
			</c:forEach>
			
			<div id="childParameter" style="width:95%;margin-left: 18px;" > 
			  <table class="layui-table" id="interfaceParameterTable" >
			    <colgroup>
			      <col>
			      <col>
			      <col>
			      <col>
			    </colgroup>
			    <thead>
			      <tr>
			      	<c:forEach items="${dhInterfaceParameterList}" var="dhInterfaceParameter">
						<c:if test="${dhInterfaceParameter.paraType!='Array' && not empty dhInterfaceParameter.paraParent}">
							 <th>${dhInterfaceParameter.paraDescription}</th>
						</c:if>
					</c:forEach>
					<th>操作</th>
			      </tr> 
			    </thead>
			    <tbody id="interfaceParameterList" >
			    	<tr>
			    		<c:forEach items="${dhInterfaceParameterList}" var="dhInterfaceParameter">
							<c:if test="${dhInterfaceParameter.paraType!='Array' && not empty dhInterfaceParameter.paraParent}">
								 <td name="${dhInterfaceParameter.paraName}" ><input  class="layui-input" style="height: 28px;" type="text" /></td>
							</c:if>
						</c:forEach>
						<td><i class="layui-icon" onclick="addTr(this);" >&#xe654;</i><i class="layui-icon" onclick="deleteTr(this);" >&#xe640;</i></td>
			    	</tr>
			    </tbody>
			  </table>
			</div>
	
		<div class="layui-form-item">
			<label class="layui-form-label"></label>
		    <div class="layui-input-inline">
		      <button class="layui-btn layui-btn-sm" lay-submit="" lay-filter="btnSubmit" >发送请求</button>
		    </div>
		  </div>	
	</form>
	<fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
	  <legend>响应信息</legend>
	</fieldset>
	<div class="layui-form-item layui-form-text">
	    <div class="layui-input-block">
	      <textarea placeholder="" class="layui-textarea" id="reseponse" style="width: 95%;"></textarea>
	    </div>
	  </div>
</body>
<script type="text/javascript" src="resources/desmartsystem/scripts/js/myjs/public.js"></script>
<script type="text/javascript">

	function addTr(ts){
		var parenthtml=$(ts).parents("tr").html();
		var str="<tr>";
		str+=parenthtml;
		str+="</tr>";
		$('#interfaceParameterList').append(str);
	}
	
	
	
	function deleteTr(ts){
		var length = $('#interfaceParameterList tr').length;
		if(length>1){
			$(ts).parent().parent().remove();
		}
	}
	
	$('#reseponse').val('');
	
	layui.use(['form', 'layedit', 'laydate','jquery'], function(){
		  var form = layui.form;
		  var array=$('#array').val();
		  
		  if(typeof array != 'undefined'){
			  $('#childParameter').show();
		  }else{
			  $('#childParameter').empty();
		  }
	  
	   //监听提交
	   form.on('submit(btnSubmit)', function(data){
		  var item=[];
		  var $tr=$('#interfaceParameterList tr');
		  var trLength = $tr.length;
		  $tr.each(function(index,element){
			   var strJson="{";
			   var length=$("td",element).length;
			   $("td",element).each(function(index1,element1){
				  var name=$(element1).attr('name');
				  var value=$(element1).children('input').val();
				  if(typeof(name)!='undefined'){
					  if(index1<length-2){
						  strJson+='"'+name+'":"'+value+'",';
					  }else{
						  strJson+='"'+name+'":"'+value+'"';
					  }
				  }
			   });
			   strJson+="}";
			   item.push(strJson);
		   });
		   
		   
		   var $form=$('.layui-form');
		   if (!$form.valid()) {
				return false;
		   }
		   
		   var length=$form.find('input').length;
		   if(length==1){
			   layer.alert('请配置请求参数!');
			   return false;
		   }
		  
		   if(typeof array != 'undefined'){
			   data.field[$('#array').val()]=JSON.stringify(item);
		   }
		   
		   var parameter={
			  intUid:$('#intUid').val(),
			  inputParameter:data.field,
		   }
		   
		   console.log(parameter);
		   
		 $.ajax({
				type:'POST',//默认以get提交，以get提交如果是中文后台会出现乱码
				dataType : 'json',
				url : '<%=request.getContextPath()%>/interfaceExecute/interfaceSchedule',
				data :JSON.stringify(parameter),
				contentType:'application/json;charset=UTF-8',
				success : function(obj) {
					if (obj.success) {
						/* pubUtil.msg(obj.msg,layer,1,function(){
						},500);*/	
						$('#reseponse').val(obj.msg);
					} else {
						$('#reseponse').val(obj.msg);
						/*pubUtil.msg(obj.msg,layer,2,function(){
						},5*1000); */
					}
				}
			});
	   	 	return false;
	  });
	});
	
</script>

</html>