<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<html>
<head>
<jsp:include page="fileUploadHead.jsp"></jsp:include>
<title>Insert title here</title>
<style>
.layui-form-label {
	text-align: left;
	padding: 6px 0;
	width: 60px;
}

input[type='date'] {
	cursor: pointer;
}

#file_load_hide {
	display: none;
}

.display_content_accessory_file {
    color: #717171;
    padding: 20px;
    width: 1200px;
    height: 500px;
    background: #fff;
    position: absolute;
    margin: 100px 0 0 -300px;
    left: 30%;
    box-shadow: 0 0 10px #ccc;
}

.foot_accessory_file {
    text-align: right;
    height: 50px;
    line-height: 50px;
    padding-right: 25px;
}
</style>
</head>
<body style="margin-left: 0px; padding: 10px;">
	<%-- <jsp:include page="fileUploadHtml.html"></jsp:include> --%>
	 <%-- <%@include file="fileUploadHtml.html" %>   --%>
	 <div class="container-fluid">
	<div class="row-fluid clearfix">
		<div class="span6 column">
			<div class="form-group">
				<div class="labelDiv" col="1">
					 <label>loadbutton</label>
				</div>
				<div class="subDiv" col="5">
					<input class="btn btn-primary file" value="上传附件" id="button-EafH" type="button" />
				</div>
				<div class="hidden-value">
					<input class="maxFileSize" value="20" type="hidden" /><input class="maxFileCount" value="10" type="hidden" /><input class="fileFormat" value="jpg,png,xls,xlsx,exe" type="hidden" />
				</div>
			</div>
		</div>
		<div class="span6 column">
		</div>
	</div>
</div>
	<button type="submit" class="btn btn-default" id="saveInfoBtn">提交</button>
	<div id="file_load_hide">
		<div class="display_container model" >
			<div class="display_content_accessory_file" >
				<div class="top">文件上传</div>
				<div class="middle">
					<div class="layui-upload-drag" style="width: 94.5%;">
						<i class="layui-icon"></i>
						<p>点击上传，或将文件拖拽到此处</p>
					</div>
					<div class="layui-upload">
						<div class="layui-upload-list">
							<table class="layui-table">
								<thead>
									<tr>
										<th>文件名</th>
										<th>大小</th>
										<th>文件标题</th>
										<th>文件标签</th>
										<th>文件说明</th>
										<th>状态</th>
										<th>操作</th>
									</tr>
								</thead>
								<tbody class="fileList"></tbody>
							</table>
						</div>
					</div>
				</div>
				<div class="foot_accessory_file">
					<button type="button" class="layui-btn listAction">开始上传</button>
					<button class="layui-btn layui-btn layui-btn-primary cancel_btn" onclick="cancelClick(this)">取消</button>
				</div>
			</div>
		</div>
	</div>
</body>
</html>