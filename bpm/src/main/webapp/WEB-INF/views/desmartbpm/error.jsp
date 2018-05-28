<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>触发器</title>
<%@include file="common/head.jsp"%>
<%@include file="common/tag.jsp"%>

</head>
    <body style="background:#f3f3f4;color:#676a6c;">
        <div class="container" style="text-align:center;">
            <h1 style="font-size:170px;margin-top:80px;">500</h1>
            <h3 style="font-weight:600;margin:10px 0 10px">${errorMessage}</h3>
    
            <div class="">出错了~ </div>
        </div>
        
    <!--IE8只能支持jQuery1.9-->
        <!--[if lte IE 8]>
        <script src="http://cdn.bootcss.com/jquery/1.9.0/jquery.min.js"></script>
        <![endif]-->
        <script type="text/javascript" src="js/jquery-3.3.1.js" ></script>
        <script type="text/javascript" src="js/layui.all.js"></script>
        
        <!-- 让IE8/9支持媒体查询，从而兼容栅格 -->
        <!--[if lt IE 9]>
          <script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
          <script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
        <![endif]-->
    </body>
</html>
<script src="<%=basePath%>/resources/desmartbpm/js/layui.all.js"></script>
<script src="<%=basePath%>/resources/desmartbpm/lay/modules/laypage.js"></script>
<script type="text/javascript">

</script>