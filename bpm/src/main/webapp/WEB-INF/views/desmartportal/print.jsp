<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<html>
<head>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
%>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<meta http-equiv="X-UA-Compatible" content="IE=Edge，chrome=1">
<!--[if lt IE 9]>
  <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
  <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
<![endif]-->
	<link href="/bpm/resources/desmartportal/formDesign/css/bootstrap-combined.min.css" rel="stylesheet">
    <link href="/bpm/resources/desmartportal/formDesign/css/layoutit.css" rel="stylesheet">
    <link href="/bpm/resources/desmartportal/css/modules/laydate/default/laydate.css" rel="stylesheet">
    <link href="/bpm/resources/desmartportal/css/layui.css" rel="stylesheet" />
    <link href="/bpm/resources/desmartportal/css/my.css" rel="stylesheet" />
    <link href="/bpm/resources/desmartportal/js/css/myFileUpload.css" rel="stylesheet" />
<title>打印预览</title>
<style>
	.layui-form-label {
		text-align: left;
		padding: 6px 0;
		width: 60px;
	}
	.layui-table td{
		padding:5px 10px;
	}
	.layui-table .td_title{
		padding:5px 15px;
	}
	input[type='date'] {
		cursor: pointer;
	}
	#file_load_hide {
		display: none;
	}
	.td_sub{
		max-width:20px;
		overflow:hidden;
	}
/* @media screen and (max-width: 35.5em) {
    .layui-table td{
    	padding:3px 15px;
    	height: 35px;
    }
    input[lay-key][readonly]{
   	 	cursor: pointer;
    	border-width: 1px;
    	background:#F2F2F2;
    }
    .data-table .layui-input{
    	border-radius:2px;
    	height: 35px;
    	width: 70%;
    	float: right;
    }
    .data-table .layui-form-select dl{
		min-width: 70%;
		left: 30%;
		top: 38px;
    }
    .data-table .layui-form-select .layui-edge {
	    top: 17px;
    }
    .data-table {
        border: 0;
    }
    .data-table thead,.data-table .td_title {
        display: none;
    }
    .data-table tr {
        margin-bottom: 10px;
        display: block;
        border-bottom: 2px solid #ddd;
    }
    .data-table td {
        display: block;
        text-align: right;
        border-bottom: 1px dotted #ccc;
    }
    .data-table td:before {
        content: attr(data-label);
        float: left;
        text-transform: uppercase;
    }
} */
</style>
</head>
<body style="margin-left: 0px; padding: 10px;">
	${ webpage }
</body>
</html>