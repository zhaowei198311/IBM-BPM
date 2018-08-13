<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<meta http-equiv="X-UA-Compatible" content="IE=Edge，chrome=1">
<!--[if lt IE 9]>
  <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
  <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
<![endif]-->
<%@include file="head.jsp"%>
<%-- <link href="<%=basePath%>/resources/desmartportal/formDesign/css/bootstrap-combined.min.css" rel="stylesheet"> --%>
<link href="<%=basePath%>/resources/desmartportal/formDesign/css/layoutit.css" rel="stylesheet">
<link href="<%=basePath%>/resources/desmartbpm/css/my.css" rel="stylesheet">
<link rel="stylesheet" href="<%=basePath%>/resources/desmartportal/selects/formSelects-v4.css">
<script type="text/javascript" src="<%=basePath%>/resources/desmartportal/js/common.js"></script>
<script type="text/javascript" src="<%=basePath%>/resources/desmartbpm/js/city.js"></script>
<script type="text/javascript" src="<%=basePath%>/resources/desmartportal/selects/formSelects-v4.js"></script>
<title>Insert title here</title>
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
@media screen and (max-width: 35.5em) {
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
}
</style>
</head>
<body style="margin-left: 0px; padding: 10px;">
	<div class="search_area top_btn">
		<input type="hidden" id="formUid" value="${formUid}"/>
		<input type="hidden" id="formName" value="${formName}"/>
		<input type="hidden" id="formDescription" value="${formDescription}"/>
		<input type="hidden" id="proUid" value="${proUid}"/>
		<input type="hidden" id="proVersion" value="${proVersion}"/>
		<input type="hidden" id="formNoExpression" value="${formNoExpression}"/>
		<input type="hidden" id="formNoStatic" value="${formNoStatic}"/>
		<span id="dynHtml" style="display:none;">${dynHtml}</span>
        <span id="test" style="float: right; padding-right: 20px;">
            <button class="layui-btn layui-btn-sm" onclick="returnEdit()">返回编辑</button>
        	<button class="layui-btn layui-btn-sm" onclick="testData()">测试表单数据</button>
        </span>
    </div>
	<div id="formSet" style="margin-top:10px;display:none;">${ webpage }</div>
</body>
<script type="text/javascript" src="<%=basePath%>/resources/desmartbpm/js/layui.all.js"></script>
<script type="text/javascript">
	$(function(){
		var formHtml = $("#formSet").html();
		formHtml = formHtml.replace(/&lt;/g, "<").replace(/&gt;/g, ">")
				.replace(/&amp;lc;/g, "(").replace(/&amp;gc;/g, ")")
				.replace(/&amp;/g, "&");
		$("#formSet").html(formHtml);
	});
</script>
<script type="text/javascript" src="<%=basePath%>/resources/desmartportal/formDesign/js/my.js?v=1.01"></script>
<script type="text/javascript">
	$(function(){
		common.initTime();
		
		$("#formSet").find("select").each(function(){
			var id = $(this).prop("id");
			if($(this).attr("is-multi")=="true"){
				$(this).attr("xm-select",id);
				//formSelects.render(id);
				formSelects.render(id);
				formSelects.on(id, function(id, vals, val, isAdd, isDisabled){
					$("#"+id).trigger("change");
				    return true;   
				});
			}
		});
		
		$("#formSet").find(".layui-upload-list").each(function(){
			var id = $(this).prop("id");
			//多图片上传
			var uploadImgListIns = upload.render({
			    elem: '#'+id+'_choose'
			    ,url: common.getPath() +'/accessoryFileUpload/uploadImgFile'
			    ,bindAction: '#'+id+'_load'
			    ,multiple: true
			    ,acceptMime: 'image/*'
			    ,auto: false
			    ,choose: function(obj){
			      var imgFiles = this.files = obj.pushFile();
			      //预读本地文件示例，不支持ie8
			      obj.preview(function(index, file, result){
			    	  var imgHtml = '<div style="display:inline;margin-right:20px;">'
			    	  		+'<img id="img_'+index+'" style="width:400px;" src="'+result+'" alt="'
			    	  		+ file.name +'" class="layui-upload-img">'
			    	  		+'<span style="cursor: pointer;"><i id="upload_'+index+'" class="layui-icon delete_img_file" title="删除图片">&#x1007;</i></span>'
			    	  		+'</div>';
			          $('#'+id).append(imgHtml);
			          $("#"+id).find(".delete_img_file").on('click', function(){
			              delete imgFiles[index]; // 删除对应的文件
			              $(this).parent().parent().remove();
			              uploadImgListIns.config.elem.next()[0].value = ''; 
			          });
			      });
			    }
				,before:function(){
					if($("#"+id).find(".delete_img_file").length>0){
						layer.load(1);
					}
			    }
			    ,done: function(res, index, upload){
			    	if(res.status==0){
			    		var fileNameHtml = '<div><a style="color:#1E9FFF;" href="http://'
			        		+ res.data.href +'" onclick="imgUrlClick(event);">'
			        		+ res.data.fileName +' <i class="layui-icon">&#xe64c;</i></a> '
			        		+'<span data-id="img_'+index+'" style="color:red;cursor: pointer;" onclick="deleteFile(\''+res.data.fileName+'\',this)">删除</span></div>';  
			        	$("#"+id+"_loc").find(".fileList").append(fileNameHtml);
			        	$("#upload_"+index).parent().remove();
			        	layer.closeAll('loading');
			        	return delete this.files[index];
			    	}else{
		        		this.error(index, upload);
		        		if(res.msg!=null && res.msg.length>0){
		        			layer.alert("上传图片失败");
		        		}
		        		layer.closeAll('loading');
		        	}
			    },
			    allDone:function(){
			    	layer.closeAll('loading');
			    }
			});
		});
		
		/* var json = '{"choose_user_xD7n":{"value":"00055685;00054290;00056430;","description":"苏思佳;胡笛;王安永;"}}';
		common.giveFormSetValue(json);
		
		var perJson = '{"choose_user_xD7n":{"edit":"no"}}';
		common.giveFormFieldPermission(perJson); */
		var json = '{"province":{"value":"360000"},"city":{"value":"360300"},"county":{"value":"360313"}}';
		common.againSetValue(json);
	});
	
	function deleteFile(fileName,obj){
		layer.confirm('确认删除？', {
			  btn: ['确定', '取消'] 
		}, function(index){
			$.ajax({
				url:common.getPath()+"/accessoryFileUpload/deleteImgFile",
				data:{
					fileName:fileName
				},
				beforeSend:function(){
					layer.load(1);
				},
				success:function(res){
					if(res.status == 0){
						var id = $(obj).attr("data-id");
						$("#"+id).parent().remove();
						$(obj).parent().remove();
						$(obj).remove();
						layer.alert("删除成功");
					}else{
						layer.alert("删除失败");
					}
					layer.closeAll("loading");
				}
			});
		});
	}
	
	function imgUrlClick(event){
		event.preventDefault();
	}
	
	function testData(){
		common.validateRegx();
		var json = common.getDesignFormData();
		console.log(json);
	}
	function returnEdit(){
		layer.load(1);
		var dynHtml = $("#dynHtml").text();
		var dynContent = dynHtml.replace(/</g,"&lt;").replace(/>/g,"&gt;");
		var proUid = $("#proUid").val().trim();
		var url = "";
		if(proUid!="" && proUid!=null){
			url = common.getPath()+"/formManage/designForm";
		}else{
			url = common.getPath()+"/publicForm/designForm";
		}
		
		var preParam = {
			proUid:$("#proUid").val(),
			proVersion:$("#proVersion").val(),
			formName:$("#formName").val(),
			formDescription:$("#formDescription").val(),
			formUid:$("#formUid").val(),
			formNoExpression:$("#formNoExpression").val(),
			formNoStatic:$("#formNoStatic").val(),
			dynHtml:dynContent 
		};
		post(url,preParam);
	}
	
	function post(URL, PARAMS) { 
		var temp_form = document.createElement("form");      
		temp_form .action = URL;      
		// temp_form .target = "_blank"; 如需新打开窗口 form 的target属性要设置为'_blank'
		temp_form .method = "post";      
		temp_form .style.display = "none"; 
		for (var x in PARAMS) { 
		var opt = document.createElement("textarea");      
	    opt.name = x;      
	    opt.value = PARAMS[x];      
	    temp_form .appendChild(opt);      
		}      
		document.body.appendChild(temp_form);      
		temp_form .submit();   
		temp_form.remove();
	} 
	
	function _getRandomString(len) {
	    len = len || 32;
	    var $chars = 'ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678'; // 默认去掉了容易混淆的字符oOLl,9gq,Vv,Uu,I1  
	    var maxPos = $chars.length;
	    var pwd = '';
	    for (i = 0; i < len; i++) {
	        pwd += $chars.charAt(Math.floor(Math.random() * maxPos));
	    }
	    return pwd;
	}
</script>
</html>