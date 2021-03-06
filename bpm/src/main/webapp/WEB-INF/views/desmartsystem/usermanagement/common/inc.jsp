<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<meta charset="utf-8">
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="format-detection" content="telephone=no">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">

<link href="<%=request.getContextPath()%>/resources/desmartsystem/styles/css/layui.css" rel="stylesheet"/>
<link href="<%=request.getContextPath()%>/resources/desmartsystem/styles/css/my.css" rel="stylesheet" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/desmartsystem/tree/css/demo.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/desmartsystem/tree/css/zTreeStyle/zTreeStyle.css" type="text/css">

<link type="text/css" href="<%=request.getContextPath()%>/resources/desmartsystem/scripts/laypage/1.2/skin/laypage.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/desmartsystem/scripts/laypage/1.2/laypage.js"></script>

<script type="text/javascript" src="<%=request.getContextPath()%>/resources/desmartsystem/scripts/js/jquery-3.3.1.js"/></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/desmartsystem/scripts/js/layui.all.js"></script>	
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/desmartsystem/scripts/js/myjs/public.js"></script>
<!-- 公共组件 -->
<jsp:include page="pbPlugins.jsp"></jsp:include>

<style>
.content {
    width:960px;
    height:2024px;
    border:1px solid #000;
    margin:0 auto;
}
.nav {
    width:100%;
    height:100px;
    margin:0 auto;
    position:fixed;
    bottom:0;
}
</style>