<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<head>
  <base href="<%=basePath%>">
  <meta charset="utf-8">
  <title>登陆</title>
  <meta name="renderer" content="webkit">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
  <link rel="stylesheet" href="resources/desmartportal/css/layui.css" media="all">
  <link rel="stylesheet" href="resources/desmartportal/css/admin.css" media="all">
  <link rel="stylesheet" href="resources/desmartportal/css/my.css" />
  
</head>
<body>
<form action="user/login">
用户名:<input type="text" id="username" name="username"/>
密 码:<input type="text" id="password" name="password"/>
<button type="submit">登 陆</button>
<button type="reset">清 除</button>
</form>
</body>
</html>
