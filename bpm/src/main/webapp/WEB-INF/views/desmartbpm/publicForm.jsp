<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <%@include file="common/head.jsp"%>
        <%@include file="common/tag.jsp"%>
        <link rel="stylesheet" href="<%=basePath%>/resources/desmartbpm/css/my.css" media="all">
        <link href="<%=basePath%>/resources/desmartbpm/tree/css/demo.css" rel="stylesheet">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>公共表单管理</title>
    </head>
    <body>
        <div class="layui-container" style="margin-top:20px;width:100%;">
            <div class="layui-row">
                <div class="layui-col-xs12">
                    <div class="search_area">
                        <div class="layui-row layui-form">
                            <div class="layui-col-xs2">
                                <input type="text" id="search_form_name" placeholder="表单名称" class="layui-input">
                            </div>
                            <div class="layui-col-xs4" style="text-align:right;">
                                <button class="layui-btn" id="search_btn">查询</button>
                                <button class="layui-btn create_btn" onclick="showCreateFormModal()">新增</button>
                                <button class="layui-btn delete_btn" style="background: #FF5151" onclick="deleteForm()">删除</button>
                                <button class="layui-btn copy_btn" onclick="showCopyFormModal()">复制</button>
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
                                <col>
                            </colgroup>
                            <thead>
                                <tr>
                                    <th>
                                        <input type="checkbox" name="allSel" title='全选' onclick="onClickHander(this)"> 序号</th>
                                    <th>表单名称</th>
                                    <th>表单编码</th>
                                    <th>表单描述</th>
                                    <th>创建时间</th>
                                    <th>创建人</th>
                                    <th>操作</th>
                                </tr>
                            </thead>
                            <tbody id="form_table_tbody">
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
                                <input type="text" id="add_form_name" name="add_form_name" autocomplete="off" class="layui-input">
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">表单编码</label>
                            <div class="layui-input-block">
                                <input type="text" id="add_form_code" name="add_form_code" autocomplete="off" class="layui-input">
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">表单描述</label>
                            <div class="layui-input-block">
                                <input type="text" id="add_form_description" name="add_form_description" autocomplete="off" class="layui-input">
                            </div>
                        </div>
                    </form>
                </div>
                <div class="foot">
                    <button class="layui-btn layui-btn sure_btn" onclick="saveForm()">确定</button>
                    <button class="layui-btn layui-btn layui-btn-primary cancel_btn">取消</button>
                </div>
            </div>
        </div>
        <div class="display_container1">
            <div class="display_content">
                <div class="top">
                   	修改表单属性
                </div>
                <div class="middle">
                    <form class="layui-form" action="" style="margin-top:30px;">
                        <div class="layui-form-item">
                            <label class="layui-form-label">表单名称</label>
                            <div class="layui-input-block">
                                <input type="text" id="update_form_name" name="update_form_name" autocomplete="off" class="layui-input">
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">表单编码</label>
                            <div class="layui-input-block">
                                <input type="text" id="update_form_code" name="update_form_code" autocomplete="off" class="layui-input" readonly="readonly">
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">表单描述</label>
                            <div class="layui-input-block">
                                <input type="text" id="update_form_description" name="update_form_description" autocomplete="off" class="layui-input">
                            </div>
                        </div>
                    </form>
                </div>
                <div class="foot">
                    <button class="layui-btn layui-btn sure_btn" onclick="updateForm();">确定</button>
                    <button class="layui-btn layui-btn layui-btn-primary cancel_btn">取消</button>
                </div>
            </div>
        </div>
        <!-- 复制表单 -->
        <div class="display_container2">
            <div class="display_content">
                <div class="top">
                   	请填写复制之后表单信息
                </div>
                <div class="middle">
                    <form class="layui-form" action="" style="margin-top:30px;">
                        <div class="layui-form-item">
                            <label class="layui-form-label">表单名称</label>
                            <div class="layui-input-block">
                                <input type="text" id="copy_form_name" name="copy_form_name" autocomplete="off" class="layui-input">
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">表单名称</label>
                            <div class="layui-input-block">
                                <input type="text" id="copy_form_code" name="copy_form_code" autocomplete="off" class="layui-input">
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">表单描述</label>
                            <div class="layui-input-block">
                                <input type="text" id="copy_form_description" name="copy_form_description" autocomplete="off" class="layui-input">
                            </div>
                        </div>
                    </form>
                </div>
                <div class="foot">
                    <button class="layui-btn layui-btn sure_btn" onclick="copyForm();">确定</button>
                    <button class="layui-btn layui-btn layui-btn-primary cancel_btn">取消</button>
                </div>
            </div>
        </div>
    </body>
    <script type="text/javascript" src="<%=basePath%>/resources/desmartbpm/js/layui.all.js"></script>
   	<script type="text/javascript" src="<%=basePath%>/resources/desmartbpm/js/my/publicForm.js"></script>
</html>