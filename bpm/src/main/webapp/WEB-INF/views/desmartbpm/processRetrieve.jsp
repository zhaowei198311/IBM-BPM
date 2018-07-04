<!DOCTYPE html>
<%@ page language="java"  pageEncoding="UTF-8"%>
<html>
    <head>
        <title>检索条件配置</title>
        <%@include file="common/head.jsp" %>
        <%@include file="common/tag.jsp" %>
        <link rel="stylesheet" href="<%=basePath%>/resources/desmartbpm/css/my.css" media="all">
        <link href="<%=basePath%>/resources/desmartbpm/tree/css/demo.css" rel="stylesheet">
        <link href="<%=basePath%>/resources/desmartbpm/tree/css/zTreeStyle/zTreeStyle.css" rel="stylesheet">
        <style>
            ul.ztree{
                border:0;
            }
            .display_container3 .layui-laypage select{
                display:none;
            }
            
            .layui-input-block-custom {
    			margin-left: 130px;
    			min-height: 36px;
			}
			.source-by-dictionaries{
				display: none;
			}
			#dataSet-error{
				margin-left: 23%;
			}
        </style>
    </head>
    <body>
        <div class="layui-container" style="margin-top:20px;width:100%;">  
            <div class="layui-row">
                <div class="layui-col-md2" style="text-align: left;">
                    <ul id="treeDemo" class="ztree" style="width:auto;height:500px;"></ul>
                </div>
                <div class="layui-col-md10">
                    <div class="search_area">
                        <div class="layui-row layui-form">
                            <div class="layui-col-md3">
                                <input type="text" placeholder="流程名称"  class="layui-input" id="proName_input">
                            </div>
                            <div class="layui-col-md9" style="text-align:left;padding-left:20px;line-height:38px;">
                                    <button class="layui-btn layui-btn-sm" id="searchByProName_btn">查询流程</button>
                                    <button class="layui-btn layui-btn-primary layui-btn-sm " onclick="addProcessRetrieve();">新增检索字段</button>
                                    <button class="layui-btn layui-btn-primary layui-btn-sm " onclick="updateProcessRetrieve();">修改检索字段</button>
                                    <button class="layui-btn layui-btn-primary layui-btn-sm " onclick="deleteProcessRetrieve();">删除检索字段</button>
                            </div>
                        </div>
                    </div>
                    <div style="width:100%;overflow-x:auto;">               
                        <table class="layui-table backlog_table" lay-even lay-skin="nob" style="width:1300px;">
                           <colgroup>
                                <col width="4%;">
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
                                  <th>序号</th>
                                  <th>字段标签</th>
                                  <th>字段名</th>
                                  <th>界面元素类型</th>
                                  <th>是否范围</th>
                                  <th>数据来源</th>
                                  <th>创建人</th>
                                  <th>创建时间</th>
                                  <th>修改人</th>
                                  <th>修改时间</th>
                                </tr> 
                            </thead>
                            <tbody id="definitionList_tbody"></tbody>
                        </table>                
                    </div>
                    <div id="lay_page"></div>
                </div>
            </div>
        </div>
       <div class="display_container5">
		<div class="display_content5" style="height: 410px;">
			<div class="top"><label id="retrieveTitle"></label></div>
			<div class="middle1" style="height: 300px;">
				<form id="addProcessRetrieve_form" class="layui-form" action="">
					<input name="retrieveUid" type="hidden">
					<div class="layui-form-item">
						<label class="layui-form-label">字段标签</label>
						<div class="layui-input-block-custom">
							<input id="leftValue" type="text" name="fieldLabel" required
								lay-verify="required" placeholder="请输入字段标签" autocomplete="off"
								class="layui-input">
						</div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">字段名</label>
						<div class="layui-input-block-custom">
							<input type="text" name="fieldName" required
								lay-verify="required" placeholder="请输入字段名" value=""
								autocomplete="off" class="layui-input">
						</div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label" style="width: 100px;">界面元素类型</label>
						<div class="layui-input-block-custom">
							<select id="elementType" name="elementType" 
								lay-filter="elementTypeFilter" lay-verify="required">
								<option value="input">文本框</option>
								<option value="date">日期控件</option>
								<option value="select">下拉列表</option>
							</select>
						</div>
					</div>
					<div class="layui-form-item is-scope">
						<label class="layui-form-label">是否范围</label>
						<div class="layui-input-block-custom">
								<input type="radio" name="isScope" value="TRUE" checked title="是"/> 
								<input type="radio" name="isScope" value="FALSE"  title="否"/>	
						</div>
					</div>
					<div class="layui-form-item source-by-dictionaries"
						style="display: none;">
						<label class="layui-form-label" style="width: 100px;">选择数据分类</label>
						<div class="layui-input-block-custom" style="position: relative;">
							<input type="text" id="dataSet_view" placeholder="请选择数据分类"
								name="dataSet_view" value="" autocomplete="off"
								class="layui-input" disabled="disabled">
							<input type="hidden" id="dataSet" name="dataSet">
							<i onclick="selectData(this);" class="layui-icon choose_num"
								title="选择数据分类">&#xe615;</i>
						</div>
					</div>

					<!-- <div class="layui-form-item ">
							<label class="col-xs-2 col-sm-offset-2 control-label">
								数据来源
							</label>
							<div class="col-xs-7" style="margin-bottom:10px;">
								<label class="col-xs-4">
									<input type="radio" name="data_source" value="手动填写" onclick="dataSourceClick(this)" /> 手动填写
								</label>
								<label class="col-xs-4">
									<input type="radio" name="data_source" value="数据字典拉取" onclick="dataSourceClick(this)" checked/> 数据字典拉取
								</label>
							</div>
					</div> -->
					<!-- <div class="layui-form-item source-by-hand">
							<label class="col-xs-2 col-sm-offset-2 control-label">
								显示的文本
							</label>
							<div class="col-xs-7">
								<input type="text" class="form-control option-text col-xs-3" placeholder="显示的文本" />
								<label class="col-xs-1" style="padding:0 10px 0 2px;">value</label>
								<input type="text" class="form-control option-value col-xs-3" placeholder="存储的value" />
								<span class="glyphicon glyphicon-minus" onclick="removeOptionInput(this)" style="font-size:20px;color:#888;cursor:pointer;"></span>
								<span class="glyphicon glyphicon-plus" onclick="addOptionInput(this)" style="font-size:20px;color:#888;cursor:pointer;"></span>
							</div>
					</div> -->
					
					
				</form>
			</div>
			<div class="foot">
				<button type="button" onclick="submitOperationProcessRetrieve();"
					class="layui-btn layui-btn sure_btn">确定</button>
				<button class="layui-btn layui-btn layui-btn-primary" onclick="$('.display_container5').hide();">取消</button>
			</div>
		</div>
	</div>
        
    </body>
    
<script type="text/javascript" src="<%=basePath%>/resources/desmartbpm/js/layui.all.js"></script>
<script type="text/javascript" src="<%=basePath%>/resources/desmartbpm/tree/js/jquery.ztree.core.js"></script>
<script type="text/javascript" src="<%=basePath%>/resources/desmartbpm/tree/js/jquery.ztree.excheck.js"></script>
<script type="text/javascript" src="<%=basePath%>/resources/desmartbpm/tree/js/jquery.ztree.exedit.js"></script>
<script type="text/javascript" src="<%=basePath%>/resources/desmartbpm/js/my/processRetrieve.js"></script>
<script>

	
     $(document).ready(function(){
        
     });

</script>
</html>
    
