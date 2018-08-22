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
<link href="resources/desmartportal/css/layui.css" rel="stylesheet"
	media="all" />
<link href="resources/desmartportal/css/my.css" rel="stylesheet"
	media="all" />
<link
	href="resources/desmartportal/css/modules/laydate/default/laydate.css"
	rel="stylesheet" media="all" />
<link href="resources/desmartportal/css/modules/layer/default/layer.css"
	rel="stylesheet" media="all" />
<link href="resources/desmartportal/css/modules/code.css"
	rel="stylesheet" media="all" />
<title>回收站页面</title>
</head>
<body>
	<div class="layui-fluid">
		<div class="body_content">
            <div class="search_area">
				<div class="layui-row layui-form">
					<div class="layui-col-md3">
						<label class="layui-form-label">流程名称</label>
						<div class="layui-input-block">
							<input id="proName" type="text" placeholder="流程名称" class="layui-input">
						</div>
					</div>
					<div class="layui-col-md3">
						<label class="layui-form-label">流程主题</label>
						<div class="layui-input-block">
							<input id="dfsTitle" type="text" placeholder="流程主题" class="layui-input">
						</div>
					</div>
					<div class="layui-col-md3" style="text-align: center;"> 
						<button class="layui-btn layui-btn-normal layui-btn-sm" onclick="search()">查询</button>
					</div>
				</div>
			</div>
			
			<div style="margin-top:5px;">
				<table class="layui-table backlog_table" lay-even lay-skin="nob"
					lay-filter="demo">
					<colgroup>
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
							<th>流程名称</th>
							<th>流程主题</th>
							<th>创建人</th>
							<th>创建日期</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody id="drafts_table_tbody" />
				</table>
			</div>
			<div id="lay_page"></div>
		</div>
	</div>
</body>
</html>
<script type="text/javascript"
	src="resources/desmartportal/js/jquery-3.3.1.js" charset="utf-8"></script>
<script type="text/javascript"
	src="resources/desmartportal/js/layui.all.js" charset="utf-8"></script>
<script type="text/javascript" src="resources/desmartportal/js/common.js"></script>
<script>
	// 为翻页提供支持
	var pageConfig = {
		pageNum : 1,
		pageSize : 10,
		total : 0,
		insTitle: "",
		proName:""
	}

	layui.use([ 'laypage', 'layer' ], function() {
		var laypage = layui.laypage, layer = layui.layer;
		//完整功能
		laypage.render({
			elem : 'lay_page',
			count : 50,
			limit : 10,
			layout : [ 'count', 'prev', 'page', 'next', 'limit', 'skip' ],
			jump : function(obj) {
				console.log(obj)
			}
		});
	});

	$(document).ready(function() {
		// 加载数据
		getDraftsInfo();
	});

	function getDraftsInfo() {
		$.ajax({
			url : common.getPath() +'/drafts/queryDraftsByList',
			type : 'POST',
			dataType : 'json',
			data : {
				pageNum : pageConfig.pageNum,
				pageSize : pageConfig.pageSize,
				insTitle : pageConfig.insTitle,
				proName : pageConfig.proName
			},
			success : function(result) {
				drawTable(result.data)
			}
		})
	}

    function drawTable(pageInfo, data) {
        pageConfig.pageNum = pageInfo.pageNum;
        pageConfig.pageSize = pageInfo.pageSize;
        pageConfig.total = pageInfo.total;
        doPage();
        // 渲染数据
        $("#drafts_table_tbody").html('');
        if (pageInfo.total == 0) {
            return;
        }
        var list = pageInfo.list;
        var startSort = pageInfo.startRow; // 开始序号
        var trs = '';
        for (var i = 0; i < list.length; i++) {
            var draft = list[i];
            var sortNum = startSort + i;
            var agentOdate = new Date(draft.dfsCreatedate);
            var showDate = datetimeFormat_1(agentOdate);
            trs += '<tr data-dfsId="' + draft.dfsId + '"><td>'
                + sortNum
                + '</td>'
                + '<td>'
                + draft.proName
                + '</td>'
                + '<td>'
                + draft.dfsTitle
                + '</td>'
                + '<td>'
                + draft.userName
                + '</td>'
                + '<td>'
                + showDate
                + '</td>'
                + '<td>'
                + '<i class="layui-icon"  title="继续发起"  onclick="checkStatus(\'' + draft.dfsId + '\');">&#xe60a;</i>'
                + '<i class="layui-icon"  title="删除草稿"  onclick=del("' + draft.dfsId + '") >&#xe640;</i>' + '</td>'
                + '</tr>';
        }
        $("#drafts_table_tbody").append(trs);
    }

	// 分页
	function doPage() {
		layui.use([ 'laypage', 'layer' ], function() {
			var laypage = layui.laypage, layer = layui.layer;
			//完整功能
			laypage.render({
				elem : 'lay_page',
				curr : pageConfig.pageNum,
				count : pageConfig.total,
				limit : pageConfig.pageSize,
				layout : [ 'count', 'prev', 'page', 'next', 'limit', 'skip' ],
				jump : function(obj, first) {
					// obj包含了当前分页的所有参数  
					pageConfig.pageNum = obj.curr;
					pageConfig.pageSize = obj.limit;
					if (!first) {
						getDraftsInfo();
					}
				}
			});
		});
	}

	function search() {
		pageConfig.pageNum = 1;
		pageConfig.pageSize = 10;
		pageConfig.insTitle = $("#dfsTitle").val();
		pageConfig.proName = $("#proName").val();
		getDraftsInfo();
	}

	function del(id) {
		layer.confirm('是否删除该草稿？', {
			btn : [ '确定', '取消' ], //按钮
			shade : false
		//不显示遮罩
		}, function(index) {
			// 提交表单的代码，然后用 layer.close 关闭就可以了，取消可以省略 ajax请求
			$.ajax({
				url : common.getPath() +'/drafts/deleteDraftsById',
				type : 'POST',
				dataType : 'text',
				data : {
					dfsId : id
				},
				success : function(result) {
					// 删除成功后 ajxa跳转 查询controller
					window.location.href = "drafts/index";
				}
			})
			layer.close(index);
		});
	}

	function cancel() {
		$(".display_container").css("display", "none");
	}


	function checkStatus(dfsId) {
		$.ajax({
		    url : common.getPath() + '/drafts/checkProcessDraftStatus',
		    type : 'post',
		    dataType : 'json',
		    data : {
                "dfsId": dfsId
		    },
			beforSend: function(){
		        layer.load(1);
			},
		    success : function (result) {
		        layer.closeAll('loading');
		        if(result.status == 0 ){
                    window.location.href = common.getPath() +'/menus/startProcess?insUid=' + result.data.insUid;
		        }else{
		            layer.alert(result.msg);
		        }
		    },
		    error : function () {
                layer.closeAll('loading');
                layer.alert('操作失败，请稍后再试');
		    }
		});
	}
</script>

