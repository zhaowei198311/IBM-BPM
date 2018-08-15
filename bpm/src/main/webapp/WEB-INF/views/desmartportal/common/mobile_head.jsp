<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<link href="resources/desmartportal/css/mymobile_layui.css" rel="stylesheet"/>
<link rel="stylesheet" href="resources/desmartportal/css/modules/laydate/default/laydate.css" />
<link href="resources/desmartportal/css/iconfont.css" rel="stylesheet" />
<link href="resources/desmartportal/css/mymobile.css?v=0.2" rel="stylesheet" />
<link href="resources/desmartportal/lCalendar/css/lCalendar.css" rel="stylesheet" />
<link href="resources/desmartportal/css/mobile_approval.css?v=1.01" rel="stylesheet" />

<!-- jQuery -->
<script type="text/javascript" src="resources/desmartportal/js/jquery-3.3.1.js"></script>

<script type="text/javascript" src="resources/desmartportal/js/layui.all.js"></script>
<script type="text/javascript" src="resources/desmartportal/js/my/mymobile.js"></script>
<script type="text/javascript" src="resources/desmartportal/lCalendar/js/lCalendar.js"></script>
<script type="text/javascript" src="resources/desmartportal/formDesign/js/mobile_form.js"></script>
<script type="text/javascript" src="resources/desmartportal/js/common.js"></script>
<!-- 附件上传js -->
<script src="resources/desmartportal/js/my/myFileUpload.js"></script>
<!-- 审批信息js -->
<script src="resources/desmartportal/js/my/mobile_approval_opinion.js"></script>
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
.display_content6 {
	width: 40%;
	height: 100px;
	left: 70%;
	margin-top: 10%;
}
    input[lay-key][readonly]{
   	 	cursor: pointer;
    }
    .data-table .layui-input,#tr_table .layui-input{
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
    .data-table{
        border: 0;
    }
    #tr_table{
     	border: 0;
        background: #fbfbfb;
    }
    .data-table thead,.data-table .td_title {
        display: none;
    }
    .data-table tr,#tr_table tr {
        padding-top: 10px;
        display: block;
        border-bottom: 1px solid #ddd;
    }
    #tr_table tr {
    	padding-top: 10px;
        display: block;
        border-bottom: 0px solid #ddd;
        padding-bottom: 10px;
    }
    .data-table td,#tr_table td {
        display: block;
        text-align: right;
        border-bottom: 1px dotted #ccc;
    }
    .data-table td:before,#tr_table td:before {
        content: attr(data-label);
        float: left;
        text-transform: uppercase;
        text-align: left;
   		max-width: 75px;
    }
    .layui-table td, .layui-table th,
    #tr_table td,#tr_table th{
    	border-width: 0px;
    }
    .td_title {
    	text-align: left;
    }
</style>