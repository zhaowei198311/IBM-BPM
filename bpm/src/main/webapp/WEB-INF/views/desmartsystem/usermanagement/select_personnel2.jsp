<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
        <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <base href="<%=basePath%>">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>选择人员</title>
    <link href="resources/desmartsystem/styles/css/layui.css" rel="stylesheet" />
    <link href="resources/desmartsystem/orgtree/css/bootstrap.min.css" rel="stylesheet" type="text/css">
    <link href="resources/desmartsystem/orgtree/css/font-awesome.css" rel="stylesheet" type="text/css">
    <link href="resources/desmartsystem/orgtree/css/plugins/jsTree/style.min.css" rel="stylesheet" type="text/css">
    <link href="resources/desmartsystem/orgtree/css/animate.css" rel="stylesheet" type="text/css">
    <link href="resources/desmartsystem/orgtree/css/style.css" rel="stylesheet" type="text/css">
    <link href="resources/desmartsystem/orgtree/css/my.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="resources/desmartsystem/orgtree/js/jquery.min.js"></script>
    <script type="text/javascript" src="resources/desmartsystem/orgtree/js/jquery-ui.js"></script>
    <script type="text/javascript" src="resources/desmartsystem/orgtree/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="resources/desmartsystem/orgtree/js/plugins/iCheck/icheck.min.js"></script>
    <script type="text/javascript" src="resources/desmartsystem/orgtree/js/content.js"></script>
    <script type="text/javascript" src="resources/desmartsystem/orgtree/js/plugins/jsTree/jstree.min.js"></script>
    <script type="text/javascript" src="resources/desmartsystem/scripts/js/layui.all.js"></script>
    <!-- <script type="text/javascript" src="resources/desmartsystem/scripts/js/jquery-3.3.1.js" /></script> -->
    <script type="text/javascript" src="resources/desmartsystem/scripts/js/myjs/myajax.js"></script>
    <script type="text/javascript" src="resources/desmartsystem/scripts/js/myjs/role_user.js"></script>
    <style>
        .jstree-open>.jstree-anchor>.fa-folder:before {
            content: "\f07c";
        }

        .jstree-default .jstree-icon.none {
            width: 0;
        }

        .new_edit {
            font-size: 10px;
        }
    </style>
    <script>
        $(function () {
            resizeLayout();
            $(window).resize(function () {
                resizeLayout();
            });
        })

        function resizeLayout() {
            var hei = $(window).height() - 124 + 'px';
            var hei2 = $(window).height() + "px";
            $(".list").css("height", hei)
            $("#jstree1").css("height", hei2)
        }
    </script>
</head>
<body style="height:95%;">
    <div class="wrapper wrapper-content animated fadeInRight" id="choose_person_div">
        <div class="row">
            <div class="col-xs-4">
                <div id="jstree1"></div>
            </div>
            <div class="col-xs-8">
           		<div class="row">
					<div class="col-xs-12">
						<div class="search_user_div" style="margin-left: 10px;">
							<div class="search_user" style="position:relative;">
								<div class="col-xs-11" style="padding:0px;">
									<input type="text" class="search_txt" placeholder="请输入用户ID/用户名"/> 
								</div>
								 <div class="col-xs-1" style="padding: 2px 0px 0px 3px;">
								 	<button class="layui-btn layui-btn-normal layui-btn-sm" onclick="search()">查询</button>		
								 </div>
							</div>
						</div>	
					</div>
				</div>
                <table class="table table-bordered" style="margin-left:10px">
                    <tr>
                        <th style="width:40%;">备选列表</th>
                        <th style="width:20%;"></th>
                        <th style="width:40%;">已选列表</th>
                    </tr>
                    <tr>
                        <td valign="top">
                            <div class="list" id="users">
                                <ul class="list_ul" id="usersul"></ul>
                            </div>
                        </td>
                        <td valign="top">
                            <div class="action-list">
                                <div class="action-group">
                                    <button id="btn_add_user" style="margin:10px" class="layui-btn layui-btn-normal layui-btn-sm">添加</button>
                                    <button id="single_delete" style="margin:10px" class="layui-btn layui-btn-primary layui-btn-sm">删除</button>
                                    <button id="all_delete" style="margin:10px" class="layui-btn layui-btn-primary layui-btn-sm">全部删除</button>
                                </div>
                            </div>
                        </td>
                        <td valign="top">
                            <div class="list">
                                <table class="table" id="receiver_table">
                                    <tbody></tbody>
                                </table>
                            </div>
                        </td>
                    </tr>
                </table>
                <div class="row">
                	<div class="col-xs-12" style="text-align: right;margin-top: 12px;padding-right: 6px;">
                		<button id="save" class="layui-btn layui-btn-normal layui-btn-sm">确认</button>
                       	<button id="close" class="layui-btn layui-btn-primary layui-btn-sm">取消</button>
                	</div>
                </div>
            </div>
        </div>
    </div>
</body>
<script type="text/javascript">
var elementId = '${id}';
var isSingle = '${isSingle}';
</script>
<script type="text/javascript" src="resources/desmartsystem/scripts/js/myjs/select_personnel.js"></script>
</html>