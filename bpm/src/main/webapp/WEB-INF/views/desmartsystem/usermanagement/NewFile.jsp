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
<title>Insert title here</title>
<%@ include file="common/common.jsp" %>
</head>
<body>
		
	<input type="text" id="test" value="张三;李四" />
	<input type="text" id="test_view" value="张三;李四" />
	<button onclick="openSelectPresonne();">打开人员选择配置</button>
		
	<script type="text/javascript">
	//打开新增按钮
	
		
	   var url='http://localhost:8080/desmartsystem/sysUser/assign_personnel?id=test&isSingle=true&actcCanChooseUser=hide&actcAssignType=allUsers&userIds=00025559;00025614;00025620;';
		
	   function openSelectPresonne() {
		    layer.open({
		     type: 2,
		     title: '选择人员',
		     shadeClose: true,
		     shade: 0.3,
		     area: ['790px', '580px'],
		     content : [ url, 'yes'],
		     success : function(layero, lockIndex) {
		      var body = layer.getChildFrame('body', lockIndex);
		      //绑定解锁按钮的点击事件
		      body.find('button#close').on('click', function() {
		       layer.close(lockIndex);
		       location.reload();//刷新
		      });
		     }
		    });
		  }
	</script>
</body>
</html>