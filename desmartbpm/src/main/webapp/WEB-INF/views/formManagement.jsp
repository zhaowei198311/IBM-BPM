<!DOCTYPE html>
<%@ page language="java"  pageEncoding="UTF-8"%>
<html>
	<head>
  		<title>表单管理</title>
  		<%@include file="common/head.jsp" %>
        <%@include file="common/tag.jsp" %>
        <link href="<%=basePath%>/resources/tree/css/demo.css" rel="stylesheet">
        <link href="<%=basePath%>/resources/tree/css/zTreeStyle/zTreeStyle.css" rel="stylesheet">
  		<style>
			ul.ztree{
				border:0;
			}
  		</style>
	</head>
	<body>
		<div class="layui-container" style="margin-top:20px;width:100%;">  
		  	<div class="layui-row">
			    <div class="layui-col-md2" style="text-align: left;">
					<ul id="category_tree" class="ztree" style="width:auto;height:500px;"></ul>
			    </div>
			    <div class="layui-col-md10">
					<div class="search_area">
						<div class="layui-row layui-form">
							<div class="layui-col-md2">
						    	<input id="formTitle" type="text" placeholder="表单名称"  class="layui-input">
							</div>
							<div class="layui-col-md3" style="text-align:right;">
						        <button class="layui-btn layui-btn-sm" >查询</button>
						        <button class="layui-btn layui-btn-sm create_btn">新增</button>
						        <button class="layui-btn layui-btn-sm delete_btn">删除</button>
						        <button class="layui-btn layui-btn-sm copy_btn">复制快照</button>
							</div>
						</div>
					</div>
					<div style="width:100%;overflow-x:auto;">				
						<table class="layui-table backlog_table" lay-even lay-skin="nob">
							<colgroup>
								<col>
							    <col>
							    <col>
							    <col>
							    <col>
							</colgroup>
							<thead>
							    <tr>
							      <th><input type="checkbox" name="" title='全选' lay-skin="primary"> 序号</th>
							      <th>表单名称</th>
							      <th>表单描述</th>
							      <th>创建时间</th>
							      <th>创建人</th>
							    </tr> 
							</thead>
							<tbody>
							    <tr>
							      <td><input type="checkbox" name="" lay-skin="primary"> 1</td>
							      <td>表单名称</td>
							      <td>描述内容...</td>
							      <td>2018-04-10 10：00：00</td>
							      <td>zhangsan</td>
							    </tr>
							    <tr>
							      <td><input type="checkbox" name="" title='全选' lay-skin="primary"> 2</td>
							      <td>表单名称</td>
							      <td>描述内容...</td>
							      <td>2018-04-10 10：00：00</td>
							      <td>zhangsan</td>
							    </tr>
							    <tr>
							      <td><input type="checkbox" name="" title='全选' lay-skin="primary"> 3</td>
							      <td>表单名称</td>
							      <td>描述内容...</td>
							      <td>2018-04-10 10：00：00</td>
							      <td>zhangsan</td>
							    </tr>
							    <tr>
							      <td><input type="checkbox" name="" title='全选' lay-skin="primary"> 4</td>
							      <td>表单名称</td>
							      <td>描述内容...</td>
							      <td>2018-04-10 10：00：00</td>
							      <td>zhangsan</td>
							    </tr>
							    <tr>
							      <td><input type="checkbox" name="" title='全选' lay-skin="primary"> 5</td>
							      <td>表单名称</td>
							      <td>描述内容...</td>
							      <td>2018-04-10 10：00：00</td>
							      <td>zhangsan</td>
							    </tr>
							</tbody>
						</table>				
					</div>
					<div id="lay_page"></div>
			    </div>
		  	</div>
		</div>
		<div class="display_container">
			<div class="display_content">
				<div class="top">
					新增表单
				</div>
				<div class="middle">
					<form class="layui-form" action="" style="margin-top:30px;">
					  <div class="layui-form-item">
					    <label class="layui-form-label">表单名称</label>
					    <div class="layui-input-block">
					      <input type="text" name="title" required  lay-verify="required" value="表单名称" autocomplete="off" class="layui-input">
					    </div>
					  </div>
					  <div class="layui-form-item">
					    <label class="layui-form-label">表单描述</label>
					    <div class="layui-input-block">
					      <input type="text" name="title" required  lay-verify="required" value="描述内容..." autocomplete="off" class="layui-input">
					    </div>
					  </div>				  
					</form>
				</div>
				<div class="foot">
					<button class="layui-btn layui-btn sure_btn">确定</button>
					<button class="layui-btn layui-btn layui-btn-primary cancel_btn">取消</button>
				</div>
			</div>
		</div>
		<div class="display_container">
			<div class="display_content">
				<div class="top">
					新增表单
				</div>
				<div class="middle">
					<form class="layui-form" action="" style="margin-top:30px;">
					  <div class="layui-form-item">
					    <label class="layui-form-label">表单名称</label>
					    <div class="layui-input-block">
					      <input type="text" name="title" required  lay-verify="required" value="" autocomplete="off" class="layui-input">
					    </div>
					  </div>
					  <div class="layui-form-item">
					    <label class="layui-form-label">表单描述</label>
					    <div class="layui-input-block">
					      <input type="text" name="title" required  lay-verify="required" value="" autocomplete="off" class="layui-input">
					    </div>
					  </div>				  
					</form>
				</div>
				<div class="foot">
					<button class="layui-btn layui-btn sure_btn">确定</button>
					<button class="layui-btn layui-btn layui-btn-primary cancel_btn">取消</button>
				</div>
			</div>
		</div>
		<div class="display_container3">
			<div class="display_content3">
				<div class="top">
					复制快照
				</div>
				<div class="middle1">
					<table class="layui-table backlog_table" lay-even lay-skin="nob">
						<colgroup>
							<col>
						    <col>
						    <col>
						    <col>
						    <col>
						</colgroup>
						<thead>
						    <tr>
						      <th><input type="checkbox" name="" title='全选' lay-skin="primary"> 序号</th>
						      <th>表单名称</th>
						      <th>表单描述</th>
						      <th>创建时间</th>
						      <th>创建人</th>
						    </tr> 
						</thead>
						<tbody>
						    <tr>
						      <td><input type="checkbox" name="" title='全选' lay-skin="primary"> 1</td>
						      <td>表单名称</td>
						      <td>描述内容...</td>
						      <td>2018-04-10 10：00：00</td>
						      <td>zhangsan</td>
						    </tr>
						    <tr>
						      <td><input type="checkbox" name="" title='全选' lay-skin="primary"> 2</td>
						      <td>表单名称</td>
						      <td>描述内容...</td>
						      <td>2018-04-10 10：00：00</td>
						      <td>zhangsan</td>
						    </tr>
						    <tr>
						      <td><input type="checkbox" name="" title='全选' lay-skin="primary"> 3</td>
						      <td>表单名称</td>
						      <td>描述内容...</td>
						      <td>2018-04-10 10：00：00</td>
						      <td>zhangsan</td>
						    </tr>
						    <tr>
						      <td><input type="checkbox" name="" title='全选' lay-skin="primary"> 4</td>
						      <td>表单名称</td>
						      <td>描述内容...</td>
						      <td>2018-04-10 10：00：00</td>
						      <td>zhangsan</td>
						    </tr>
						    <tr>
						      <td><input type="checkbox" name="" title='全选' lay-skin="primary"> 5</td>
						      <td>表单名称</td>
						      <td>描述内容...</td>
						      <td>2018-04-10 10：00：00</td>
						      <td>zhangsan</td>
						    </tr>
						</tbody>
					</table>			
				</div>
				<div id="lay_page"></div>
				<div class="foot">
					<button class="layui-btn layui-btn sure_btn">确定</button>
					<button class="layui-btn layui-btn layui-btn-primary cancel_btn">取消</button>
				</div>
			</div>
		</div>
	</body>
	
</html>
	<script type="text/javascript" src="<%=basePath%>/resources/tree/js/jquery.ztree.core.js"></script>
    <script type="text/javascript" src="<%=basePath%>/resources/tree/js/jquery.ztree.excheck.js"></script>
    <script type="text/javascript" src="<%=basePath%>/resources/tree/js/jquery.ztree.exedit.js"></script>
	<script>
		// 为翻页提供支持
	    var pageConfig = {
	    	pageNum: 1,
	    	pageSize: 10,
	    	total: 0,
	    	processUid: "",
	    	proVerUid: ""
	    }
	    // 为弹出框的分页控件服务
	    /* var pageConfig2 = {
	        pageNum: 1,
	        pageSize: 5,
	        total: 0,
	        processAppName: "",
	        processAppAcronym: "",
	        display: ""
	    } */
	
		//tree
		var setting = {
			data : {
				key : {
					name : "categoryName"
				},
				simpleData : {
					enable : true,
					idKey : "categoryUid",
					pIdKey : "categoryParent",
					rootPId : "rootCategory"
				}
			},
			callback : {
				onClick : zTreeOnClick
			} 
		};

		
		var zNodes = [
				{
					"categoryUid" : "rootCategory",
					"categoryParent" : "null",
					"categoryName" : "流程分类",
					"categoryIcon" : "icon"
				},{
					"categoryUid" : "uid1",
					"categoryParent" : "rootCategory",
					"categoryName" : "分类1",
					"categoryIcon" : "icon"
				},{
					"categoryUid" : "uid2",
					"categoryParent" : "rootCategory",
					"categoryName" : "分类2",
					"categoryIcon" : "icon"
				}/* ,{
					"categoryUid" : "pro1",
					"categoryParent" : "rootCategory",
					"categoryName" : "分类1",
					"categoryIcon" : null
				} */
				]

		// 分页
        function doPage() {
            layui.use(['laypage', 'layer'], function(){
                var laypage = layui.laypage,layer = layui.layer;  
                  //完整功能
                laypage.render({
                    elem: 'lay_page',
                    curr: pageConfig.pageNum,
                    count: pageConfig.total,
                    limit: pageConfig.pageSize,
                    layout: ['count', 'prev', 'page', 'next', 'limit', 'skip'],
                    jump: function(obj, first){
                    	// obj包含了当前分页的所有参数  
                    	pageConfig.pageNum = obj.curr;
                    	pageConfig.pageSize = obj.limit;
                    	if (!first) {
                    		getMetaInfo();
                    	}
                    }
                }); 
            });
        }

		$(document).ready(function() {
			$.fn.zTree.init($("#category_tree"), setting, zNodes);

			$(".create_btn").click(function() {
				$(".display_container").css("display", "block");
			})
			$(".copy_btn").click(function() {
				$(".display_container3").css("display", "block");
			})
			$(".sure_btn").click(function() {
				$(".display_container").css("display", "none");
				$(".display_container3").css("display", "none");
			})
			$(".cancel_btn").click(function() {
				$(".display_container").css("display", "none");
				$(".display_container3").css("display", "none");
			})
		});

		//获得表单数据
		function getFormInfo(){
			$.ajax({
				url:common.getPath()+"/",
				method:"",
				dataType:"json",
				data:{
					"pageNum":pageConfig.pageNum,
					"pageSize":pageConfig.pageSize,
					"processUid":pageConfig.processUid,
					"proVerUid":pageConfig.proVerUid,
					"formTitle":$("#formTitle").val().trim()
				},
				success:function(result){
					if(result.status==0){
						drawTable(result.data);
					}
				}
			});
		};
		
		function zTreeOnClick(event, treeId, treeNode) {
			//alert(treeNode.tId + ", " + treeNode.categoryName);
		};
	</script>