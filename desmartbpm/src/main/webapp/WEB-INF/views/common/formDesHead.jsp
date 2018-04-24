<%@ page isELIgnored="false"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path;
%>
<link href="<%=basePath%>/resources/formDesign/css/bootstrap-combined.min.css"
	rel="stylesheet">
<link href="<%=basePath%>/resources/formDesign/css/layoutit.css" rel="stylesheet">
<link href="<%=basePath%>/resources/formDesign/css/docs.min.css" rel="stylesheet">
<link rel="stylesheet"
	href="<%=basePath%>/resources/formDesign/css/font-awesome.min.css">
<link rel="stylesheet"
	href="<%=basePath%>/resources/formDesign/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css">
<link rel="stylesheet"
	href="<%=basePath%>/resources/formDesign/fileinput/css/fileinput.min.css">

<script type="text/javascript"
	src="<%=basePath%>/resources/formDesign/js/jquery-2.0.0.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/resources/formDesign/js/common.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/resources/formDesign/fileinput/js/fileinput.min.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/resources/formDesign/js/bootstrap.min.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/resources/formDesign/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/resources/formDesign/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/resources/formDesign/js/jquery-ui.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/resources/formDesign/js/jquery.ui.touch-punch.min.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/resources/formDesign/js/jquery.htmlClean.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/resources/formDesign/ckeditor/ckeditor.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/resources/formDesign/ckeditor/config.js"></script>
<script src="<%=basePath%>/resources/js/common.js"></script>
<script type="text/javascript" src="<%=basePath%>/resources/formDesign/js/scripts.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/resources/formDesign/js/FileSaver.js"></script>
<script type="text/javascript" src="<%=basePath%>/resources/formDesign/js/blob.js"></script>
<script type="text/javascript" src="<%=basePath%>/resources/formDesign/js/myjs.js"></script>
<script type="text/javascript" src="<%=basePath%>/resources/formDesign/js/docs.min.js"></script>