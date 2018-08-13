<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8" %>
<html>

<head>
    <title>流程分类</title>
    <%@include file="common/head.jsp" %>
    <%@include file="common/tag.jsp" %>
    <link rel="stylesheet" href="<%=basePath%>/resources/desmartbpm/css/my.css" media="all">
    <link href="<%=basePath%>/resources/desmartbpm/tree/css/demo.css" rel="stylesheet">
    <link href="<%=basePath%>/resources/desmartbpm/tree/css/zTreeStyle/zTreeStyle.css" rel="stylesheet">

    <style>
        .ztree li span.button.add {
            margin-right: 2px;
            background-position: -143px 0px;
            vertical-align: top;
        }

        ul.ztree {
            border: 0;
        }

        .display_container3 .layui-laypage select {
            display: none;
        }

        .layui-form-label {
            width: 100px;
        }

        .layui-input-block {
            margin-left: 130px;
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
                    <div class="layui-col-md3">
                        <input id="proName_input" type="text" placeholder="流程名称" class="layui-input">
                    </div>
                    <div class="layui-col-md9" style="text-align:left;width: 65%;padding-left:20px;">
                        <button class="layui-btn" id="searchMeat_btn">查询</button>
                        <button class="layui-btn create_btn" id="show_expose_btn">添加</button>
                        <button class="layui-btn delete_btn" id="meta_del_btn" style="background: #FF5151">删除</button>
                        <button class="layui-btn move_btn" id="move_btn">移动</button>
                        <button class="layui-btn hide_btn" id="hide_btn">隐藏</button>
                        <button class="layui-btn close_btn" id="close_btn">关闭</button>
                        <button class="layui-btn enable_btn" id="enable_btn">启用</button>
                        <button class="layui-btn " id="reloadExposedItem_btn">同步公开的流程</button>
                    </div>
                </div>
            </div>
            <div style="width:100%;overflow-x:auto;">
                <table id="proMet_table" class="layui-table backlog_table link_table" lay-even lay-skin="nob"
                       style="width:2000px;">
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
                        <th>序号</th>
                        <th>流程元数据名称</th>
                        <th>流程应用库id</th>
                        <th>流程图id</th>
                        <th>引擎中流程名</th>
                        <th>流程状态</th>
                        <th>创建者</th>
                        <th>创建时间</th>
                        <th>更新者</th>
                        <th>更新时间</th>
                    </tr>
                    </thead>
                    <tbody id="proMet_table_tbody"></tbody>
                </table>
            </div>
            <div id="lay_page"></div>
        </div>
    </div>
</div>
<div class="display_container4" id="editMeta_container">
    <div class="display_content4" style="padding-bottom: 10px;height:421px;">
        <div class="top">
            编辑流程元数据
        </div>
        <div class="middle2" style="height: 318px;">
            <form class="layui-form" action="" style="margin-top:30px;">
                <div class="layui-form-item">
                    <label class="layui-form-label">流程元数据名称</label>
                    <div class="layui-input-block">
                        <input type="text" id="metarename_input" required lay-verify="required" value=""
                               autocomplete="off" class="layui-input">
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">流程编号</label>
                    <div class="layui-input-block">
                        <input type="text" id="metaProNo_input" required lay-verify="required" value=""
                               autocomplete="off" class="layui-input">
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">流程应用库</label>
                    <div class="layui-input-block">
                        <input type="text" id="eidtMeta_appIdShow" name="title" required lay-verify="required" value=""
                               autocomplete="off" class="layui-input"
                               disabled="disabled">
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">流程图id</label>
                    <div class="layui-input-block">
                        <input type="text" name="title" id="eidtMet_bpdIdShow" required lay-verify="required" value=""
                               autocomplete="off" class="layui-input"
                               disabled="disabled">
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">引擎中流程名</label>
                    <div class="layui-input-block">
                        <input type="text" name="title" id="eidtMet_displayShow" required lay-verify="required" value=""
                               autocomplete="off" class="layui-input"
                               disabled="disabled">
                    </div>
                </div>
            </form>
        </div>
        <div class="foot">
            <button class="layui-btn layui-btn sure_btn" id="editMeta_sureBtn">确定</button>
            <button class="layui-btn layui-btn layui-btn-primary cancel_btn" id="editMeta_cancelBtn">取消</button>
        </div>
    </div>
</div>
<div class="display_container3" id="exposed_table_container">
    <div class="display_content3">
        <div class="top">
            新增流程元数据
        </div>
        <div class="middle1">
            <table id="unsynProMetaTable" class="layui-table backlog_table" lay-even lay-skin="nob">
                <colgroup>
                    <col>
                    <col>
                    <col>
                    <col>
                </colgroup>
                <thead>
                <tr>
                    <th>序号</th>
                    <th>流程名称</th>
                    <th>流程应用库名称</th>
                    <th>流程图id</th>
                </tr>
                </thead>
                <tbody id="exposed_table_tbody"></tbody>
            </table>
        </div>
        <div id="lay_page2"></div>
        <div class="foot">
            <button class="layui-btn layui-btn sure_btn" id="bind_meta_btn">确定</button>
            <button class="layui-btn layui-btn layui-btn-primary cancel_btn" id="cancel_bind_btn">取消</button>
        </div>
    </div>
</div>
<div class="display_container" id="addCategory_container">
    <div class="display_content" style="height:350px;">
        <div class="top">填写分类名</div>
        <div class="middle1">
            <input type="text" class="layui-input" id="categoryName_input" style="margin-top:20px;"/>
        </div>
        <div class="foot">
            <button class="layui-btn layui-btn sure_btn" id="addCategorySure_btn">确定</button>
            <button class="layui-btn layui-btn layui-btn-primary cancel_btn" id="addCategoryCancel_btn">取消</button>
        </div>
    </div>
</div>
<div class="display_container" id="addMeta_container">
    <div class="display_content" style="height: 360px;">
        <div class="top">请命名流程元数据</div>
        <div class="middle1" style="padding-top:15px;">
            <input type="text" class="layui-input" id="addMetaName_input"/>
        </div>
        <div class="foot">
            <button class="layui-btn layui-btn sure_btn" id="addMetaSure_btn">确定</button>
            <button class="layui-btn layui-btn layui-btn-primary cancel_btn" id="addMetaCancel_btn">取消</button>
        </div>
    </div>
</div>
<div class="display_container8">
    <div class="display_content8">
        <div class="top">
            流程分类
        </div>
        <div class="middle1" style="height: 75%">
            <ul id="category_tree1" class="ztree" style="width:auto;height:75%;"></ul>
        </div>
        <div class="foot">
            <button class="layui-btn layui-btn sure_btn" id="moveSure_btn">确定</button>
            <button class="layui-btn layui-btn layui-btn-primary cancel_btn" id="moveCancel_btn">取消</button>
        </div>
    </div>
</div>
<script src="<%=basePath%>/resources/desmartbpm/js/layui.all.js"></script>
<script src="<%=basePath%>/resources/desmartbpm/js/my/processCategory.js"></script>
<script type="text/javascript" src="<%=basePath%>/resources/desmartbpm/tree/js/jquery.ztree.core.js"></script>
<script type="text/javascript" src="<%=basePath%>/resources/desmartbpm/tree/js/jquery.ztree.excheck.js"></script>
<script type="text/javascript" src="<%=basePath%>/resources/desmartbpm/tree/js/jquery.ztree.exedit.js"></script>
</body>

</html>