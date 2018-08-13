<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>任务移交</title>
<%@include file="common/head.jsp" %>
<%@include file="common/tag.jsp" %>
<link rel="stylesheet" href="<%=basePath%>/resources/desmartbpm/css/my.css" media="all">
<link href="<%=basePath%>/resources/desmartbpm/css/layui.css" rel="stylesheet"/>
<link rel="stylesheet" href="<%=basePath%>/resources/desmartbpm/css/modules/laydate/default/laydate.css" />
<script type="text/javascript" src="<%=basePath%>/resources/desmartbpm/js/jquery-3.3.1.js" ></script>
<script type="text/javascript" src="<%=basePath%>/resources/desmartbpm/js/layui.all.js"></script>
<script type="text/javascript" src="<%=basePath%>/resources/desmartbpm/js/common.js"></script>
<script type="text/javascript" src="<%=basePath%>/resources/desmartbpm/js/my/turnTaskInstance.js"></script>

<style type="text/css">
.display_container5_custom{
    display: none;
    position: fixed;
    top: 0px;
    left: 0px;
    z-index: 8;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.3);
}
.display_content5_custom{
    overflow-y: auto;
    color: #717171;
    padding: 20px;
    width: 570px;
    height: 400px;
    background: #fff;
    position: absolute;
    margin: 80px 0 0 -306px;
    left: 50%;
    box-shadow: 0 0 10px #ccc;
}

.colorli {
	background-color: #9DA5EC;
	color: white;
}
.layui-label-turn-user{
	font-weight: bold;
	color: #009688;
}
.layui-icon-custom{
    font-family: layui-icon!important;
    font-style: normal;
    font-size: 22px;
    -webkit-font-smoothing: antialiased;
}
</style>

</head>
<body>
<div class="layui-container" style="margin-top:20px;width:100%;">  
		  	<div class="layui-row">
			    <div class="layui-col-xs12">
					<div class="search_area">
						<div class="layui-row layui-form">
							<div class="layui-col-xs12" style="text-align:right;">
								<div class="layui-col-md2">
								<label class="layui-form-label layui-label-turn-user" style="width: 80%;">选择用户：</label>
								<input type="hidden" id="userId_view" name="userId_view" onchange="setLabelUserView(this)"/>
								<i class="layui-icon-custom choose_user" id="choose_handle_user" title="选择用户">&#xe612;</i>
								</div>
								<div class="layui-col-xs2" style="width: 15%;">
								<label id="labelUserView" class="layui-label-turn-user"><c:if test="${not empty userName and not empty userId}">
								${userName }(${userId})的待办任务
								</c:if></label>
								<input type="hidden" id="userId" name="userId" value="<c:if test="${not empty userId}">${userId }</c:if>"/>
								</div>
								
								<!-- <div class="layui-col-xs3">
								<input type="text" placeholder="任务接收时间"  class="layui-input" id="process-taskInitDate-search">
								</div> -->
								<div class="layui-col-xs2" >
								<!-- <button class="layui-btn layui-btn-sm" onclick="search();">查询</button> -->
								<button class="layui-btn layui-btn-sm" value="byBatch" onclick="checkTurnBatchTaskIns(this);">移交</button>
								<button class="layui-btn layui-btn-sm" value="byAll" onclick="showTurnTaskIns(this);">移交全部</button>
								</div>
								
							</div>
						</div>
					</div>
					<div style="margin-top: 5px;">
		                <table class="layui-table" lay-even lay-skin="line">
		                    <colgroup>
		                        <col>
		                        <col>
		                        <col>
		                        <col>
		                        <col>
		                        <col>
		                        <col>
		                        <col>
		                        <col>
		                        <col>
		                    </colgroup>
		                    <thead>
		                        <tr>
		                            <th><!-- 序号 --><input type="checkbox" id="checked-All-ins"/></th>
		                            <th>流程名称</th>
		                            <th>流程标题</th>
		                            <th>环节名称</th>
		                            <th>任务所属人</th>
		                            <th>代理人</th>
		                            <th>任务状态</th>
		                            <th>上一环节处理人</th>
		                            <th>流程创建人</th>
		                            <th>任务接收时间</th>
		                        </tr>
		                    	</thead>
		                    <tbody id="proMet_table_tbody" />
		                </table>
		            </div>
					<div id="lay_page"></div>
			    </div>
		  	</div>
		</div>

	<!-- 任务移交 -->
	<div id="task-trun-off-div" class="display_container5_custom">
		<div class="display_content5_custom" style="height: 400px;">
			<div class="top"><label id="task-trun-title"></label></div>
			<div class="middle1" style="height: 290px;">
			<input id="task-trun-operation" type="hidden">
				<div class="layui-form" style="padding-top: 4%;padding-right: 4%;">
					<div class="layui-form-item">
						<label class="layui-form-label">移交给</label>
						<div class="layui-input-block" style="position: relative;">
							 <input type="text" name="targetUser_view" 
								 id="targetUser_view" autocomplete="off"
								 placeholder="请选择任务接收人"
								 class="layui-input" disabled="disabled"> <i
								 class="layui-icon choose_user" id="choose_target_user"
								 title="选择任务接收人">&#xe612;</i>
						 </div>
						<input type="hidden" id="targetUser" name="targetUser" />
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">移交原因</label>
						<div class="layui-input-block">
							<textarea id="trunOffCause" name="trunOffCause" style="width: 100%;height: 100px;">
							
							</textarea>
						</div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">任务移交</label>
						<div class="layui-input-block">
							<pre style="width: 100%;height: 50px;">
								将业务移交他人处理,员工离职,职位调用可利用任务移交功能进行任务转交
							</pre>
						</div>
					</div>
				</div>
			</div>
			<div class="foot">

				<button type="button" onclick="trunOffTaskIns();"
					class="layui-btn layui-btn sure_btn">确定</button>
				<button class="layui-btn layui-btn layui-btn-primary cancel_btn"
						onclick="$('#task-trun-off-div').hide();">取消</button>
				
		</div>
	</div>
	</div>
</body>
<script type="text/javascript">

</script>
</html>