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
<meta charset="utf-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1">
<title>wPaintTest</title>
<link href="resources/desmartportal/css/layui.css" rel="stylesheet" />
<link rel="stylesheet"
	href="resources/desmartportal/css/modules/laydate/default/laydate.css" />
<link href="resources/desmartportal/css/my.css" rel="stylesheet" />

<!-- <script type="text/javascript" src="resources/desmartportal/js/jquery-3.3.1.js"></script> -->
<script type="text/javascript" src="resources/desmartportal/js/layui.all.js"></script>
<script type="text/javascript" src="resources/desmartportal/js/common.js"></script>

<!-- jQuery -->
<script type="text/javascript" src="resources/desmartportal/wPaint-master/inc/jquery.1.8.2.min.js"></script>
<script type="text/javascript" src="resources/desmartportal/wPaint-master/inc/jquery.ui.core.min.js"></script>
<script type="text/javascript" src="resources/desmartportal/wPaint-master/inc/jquery.ui.widget.min.js"></script>
<script type="text/javascript" src="resources/desmartportal/wPaint-master/inc/jquery.ui.mouse.min.js"></script>
<script type="text/javascript" src="resources/desmartportal/wPaint-master/inc/jquery.ui.draggable.min.js"></script>

<!-- wColorPicker -->  
<link rel="Stylesheet" type="text/css" href="resources/desmartportal/wPaint-master/inc/wColorPicker.css"/>  
<script type="text/javascript" src="resources/desmartportal/wPaint-master/inc/wColorPicker.js"></script>  
  
<!-- wPaint -->  
<link rel="Stylesheet" type="text/css" href="resources/desmartportal/wPaint-master/wPaint.css"/>  
<script type="text/javascript" src="resources/desmartportal/wPaint-master/wPaint.js"></script>  

<script type="text/javascript" src="resources/desmartportal/js/my/myWPaint.js"></script>


</head>
<body>
	
	<div id="imgEditMain" style="display: none;">
	<h2>附件图片编辑</h2>
	<input style="display: none;" id="imgEditAccessoryFileData" >
	<table>
	<tr>
		<td>
			<div id="wPaint" style="position:relative; width:500px; height:1000px;
				 background:#CACACA; border:solid black 1px;"></div>
		</td>
	</tr>
	</table>
	</div>
</body>
</html>