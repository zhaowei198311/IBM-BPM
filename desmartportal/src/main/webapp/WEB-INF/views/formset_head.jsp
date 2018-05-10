<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">

<link href="resources/formDesign/css/bootstrap-combined.min.css"
	rel="stylesheet">
<link href="resources/formDesign/css/layoutit.css" rel="stylesheet">
<!-- 引入css -->
<link href="resources/css/layui.css" rel="stylesheet">
<link href="resources/css/modules/laydate/default/laydate.css" rel="stylesheet">
<link href="resources/css/my.css" rel="stylesheet">

<!-- 全局js -->
<script src="resources/js/jquery-3.3.1.js"></script>
<script src="resources/js/common.js"></script>
<script src="resources/js/layui.all.js"></script>
<script src="resources/formDesign/js/my.js"></script>
