<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!--<link href="resources/desmartportal/formDesign/css/bootstrap-combined.min.css" rel="stylesheet">
<link href="resources/desmartportal/formDesign/css/layoutit.css" rel="stylesheet">-->
<link href="resources/desmartportal/css/modules/laydate/default/laydate.css" rel="stylesheet">
<link href="resources/desmartportal/css/mymobile_layui.css" rel="stylesheet" />
<link href="resources/desmartportal/css/my.css" rel="stylesheet" />
<link href="resources/desmartportal/js/css/myFileUpload.css" rel="stylesheet" />

<!-- jQuery -->
<script type="text/javascript" src="resources/desmartportal/js/jquery-3.3.1.js"></script>

<script type="text/javascript" src="resources/desmartportal/js/layui.all.js"></script>
<script type="text/javascript" src="resources/desmartportal/js/my/mymobile.js"></script>

<script type="text/javascript" src="resources/desmartportal/formDesign/js/mobile_form.js"></script>
<script type="text/javascript" src="resources/desmartportal/js/common.js"></script>
<!-- 附件上传js -->
<script src="resources/desmartportal/js/my/myFileUpload.js"></script>
<!-- 审批信息js -->
<script src="resources/desmartportal/js/my/mobile_approval_opinion.js"></script>
<script type="text/javascript" src="resources/desmartportal/js/my/mobile_approval.js"></script>

<style type="text/css">
.container-fluid {
	padding-right: 0px;
	padding-left: 0px;
	zoom: 1;
}

#upload_file_modal {
	display: none;
}

.display_container_image_edit {
	z-index: 11;
	color: #717171;
	padding: 20px;
	width: 95%;
	height: 100%;
	background: #fff;
	position: fixed;
	left: 1%;
	top: 1%;
	right: 1%;
	box-shadow: 0 0 10px #ccc;
	display: none;
}

.display_content_image_edit {
	z-index: 11;
	width: 100%;
	height: 100%;
	background: #fff;
	box-shadow: 0 0 10px #ccc;
	overflow: scroll;
}

.display_content_accessory_file {
	color: #717171;
	padding: 20px;
	width: 70%;
	height: 60%;
	background: #fff;
	position: fixed;
	left: 12.5%;
	top: 16%;
	box-shadow: 0 0 10px #ccc;
}

.foot_accessory_file {
	text-align: right;
	height: 50px;
	line-height: 50px;
	padding-right: 25px;
}

.foot_history_file {
	padding-top: 5px;
	text-align: right;
	height: 50px;
	line-height: 50px;
	padding-right: 25px;
}

.upload_overflow_middle {
	height: 80%;
	width: 96%;
	border: 1px solid #ccc;
	position: relative;
	padding: 0 10px;
	overflow-y: auto;
	overflow-x: hidden;
}

.choose_user {
	position: absolute;
	right: 10px;
	top: 8px;
	z-index: 1;
}

#formSet {
	display: none;
}

.choose_display_content {
	color: #717171;
	padding: 20px;
	width: 600px;
	min-height: 200px;
	background: #fff;
	position: absolute;
	margin: 100px 0 0 -300px;
	left: 50%;
	box-shadow: 0 0 10px #ccc;
}

.layui-progress-bar {
	display: block;
	min-width: 8px;
	height: 12px;
	background: #2067c5;
	background-image: -webkit-linear-gradient(top, rgba(255, 255, 255, 0.3),
		rgba(255, 255, 255, 0) 30%, rgba(0, 0, 0, 0) 65%, rgba(0, 0, 0, 0.2)),
		-webkit-linear-gradient(left, #2067c5, #24c1fc);
	background-image: -moz-linear-gradient(top, rgba(255, 255, 255, 0.3),
		rgba(255, 255, 255, 0) 30%, rgba(0, 0, 0, 0) 65%, rgba(0, 0, 0, 0.2)),
		-moz-linear-gradient(left, #2067c5, #24c1fc);
	background-image: -o-linear-gradient(top, rgba(255, 255, 255, 0.3),
		rgba(255, 255, 255, 0) 30%, rgba(0, 0, 0, 0) 65%, rgba(0, 0, 0, 0.2)),
		-o-linear-gradient(left, #2067c5, #24c1fc);
	background-image: linear-gradient(to bottom, rgba(255, 255, 255, 0.3),
		rgba(255, 255, 255, 0) 30%, rgba(0, 0, 0, 0) 65%, rgba(0, 0, 0, 0.2)),
		linear-gradient(to right, #2067c5, #24c1fc);
	border-radius: 4px;
	-webkit-box-shadow: inset 0 1px rgba(0, 0, 0, 0.2), inset 0 0 0 1px
		rgba(0, 0, 0, 0.2);
	box-shadow: inset 0 1px rgba(0, 0, 0, 0.2), inset 0 0 0 1px
		rgba(0, 0, 0, 0.2);
}

.display_content6 {
	width: 40%;
	height: 100px;
	left: 70%;
	margin-top: 10%;
}
    .layui-table td{
    	padding:3px 15px;
    	height: 30px;
    }
    input[lay-key][readonly]{
   	 	cursor: pointer;
    	border-width: 1px;
    	background:#F2F2F2;
    }
    .data-table .layui-input{
    	border-radius:2px;
    	height: 30px;
    	width: 75%;
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
        padding-top: 10px;
        display: block;
        border-bottom: 1px solid #ddd;
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
    .layui-table td, .layui-table th{
    	border-width: 0px;
    }
    .td_title {
    	text-align: left;
    }
</style>
<link href="resources/desmartportal/css/mymobile.css" rel="stylesheet" />