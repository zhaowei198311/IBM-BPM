<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page isELIgnored="false"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">

<!-- 引入css -->
<link href="<%=basePath%>/resources/css/layui.css" rel="stylesheet">
<link href="<%=basePath%>/resources/css/modules/laydate/default/laydate.css" rel="stylesheet">


<!-- 全局js -->
<script src="<%=basePath%>/resources/js/jquery-3.3.1.js"></script>
<script src="<%=basePath%>/resources/js/jquery.validate.min.js"></script>
<script src="<%=basePath%>/resources/js/dwz.regional.zh_CN.js"></script>
<script src="<%=basePath%>/resources/js/common.js"></script>
<script src="<%=basePath%>/resources/js/jquery.fastLiveFilter.js"></script>











