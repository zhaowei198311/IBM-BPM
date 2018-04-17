<!DOCTYPE html>
<%@ page language="java"  pageEncoding="UTF-8"%>
<html>
<head>
    <title>流程编辑</title>
    <%@include file="common/head.jsp" %>
    <%@include file="common/tag.jsp" %>
    <style>
        .layui-form-label{width:140px;}
        .layui-input-block{margin-left:170px;}
    </style>
    <link href="<%=basePath%>/resources/tree/css/demo.css" rel="stylesheet">
    <link href="<%=basePath%>/resources/tree/css/zTreeStyle/zTreeStyle.css" rel="stylesheet">
    <script type="text/javascript" src="<%=basePath%>/resources/js/processDefinitionSet.js"></script>
</head>
<body>
<div class="container">
    <div class="search_area">
        <div class="search_area top_btn">
            <input type="hidden" name="proAppId" value="${definition.proAppId}"/>
            <input type="hidden" name="proUid" value="${definition.proUid}"/>
            <input type="hidden" name="proVerUid" value="${definition.proVerUid}"/>
            <button class="layui-btn layui-btn-primary layui-btn-sm" id="back_btn">返回</button>
            <span style="float:right;">
						<button class="layui-btn layui-btn-primary layui-btn-sm" id="save_btn">保存</button>
						<button class="layui-btn layui-btn-primary layui-btn-sm" id="finish_btn">配置完成</button>
					</span>
        </div>
    </div>
    <div>
        <p class="title_p">配置</p>
        <form class="layui-form" action="">
            <div class="layui-row">
                <div class="layui-col-md6">
                    <div class="layui-form-item">
                        <label class="layui-form-label">流程所需时间（数量）</label>
                        <div class="layui-input-block">
                            <input type="text" name="proTime" onkeyup="common.repNumber(this)"  lay-verify="required" value="${definition.proTime}" autocomplete="off" class="layui-input" />
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">最终信息汇总表单</label>
                        <div class="layui-input-block">
                            <input type="text" name="proDynaforms"  lay-verify="required" value="${definition.proDynaforms}" autocomplete="off" class="layui-input" />
                        </div>
                    </div>
                </div>
                <div class="layui-col-md6">
                    <div class="layui-form-item">
                        <label class="layui-form-label">流程所需时间（单位）</label>
                        <div class="layui-input-block">
                            <select name="proTimeUnit" lay-verify="required" value="${definition.proTimeUnit}">
                                <option value=""></option>
                                <option value="hour">小时</option>
                                <option value="day">天</option>
                                <option value="month">月</option>
                            </select>
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">发送邮件通知的模板</label>
                        <div class="layui-input-block">
                            <input type="text" name="proDerivationScreenTpl"  lay-verify="required" value="${definition.proDerivationScreenTpl}" autocomplete="off" class="layui-input" />
                        </div>
                    </div>
                </div>
            </div>
        </form>
        <p class="title_p">触发器配置</p>
        <form class="layui-form" action="">
            <div class="layui-row">
                <div class="layui-col-md6">
                    <div class="layui-form-item">
                        <label class="layui-form-label">发起流程触发器</label>
                        <div class="layui-input-inline">
                            <input type="hidden" name="proTriStart" value="${definition.proTriStart}"/>
                            <input type="text" name="proTriStartTitle"   lay-verify="required" value="${definition.proTriStartTitle}" autocomplete="off" class="layui-input" disabled="disabled">
                        </div>
                        <div class="layui-form-mid layui-word-aux"><i class="layui-icon choose_user">&#xe621;</i> </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">流程暂停触发器</label>
                        <div class="layui-input-inline">
                            <input type="hidden" name="proTriPaused" value="${definition.proTriPaused}"/>
                            <input type="text" name="proTriPausedTitle"   lay-verify="required" value="${definition.proTriPausedTitle}" autocomplete="off" class="layui-input" disabled="disabled">
                        </div>
                        <div class="layui-form-mid layui-word-aux"><i class="layui-icon choose_user">&#xe621;</i> </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">重新分配触发器</label>
                        <div class="layui-input-inline">
                            <input type="hidden" name="proTriReassigned" value="${definition.proTriReassigned}"/>
                            <input type="text" name="proTriReassignedTitle"   lay-verify="required" value="${definition.proTriReassignedTitle}" autocomplete="off" class="layui-input" disabled="disabled">
                        </div>
                        <div class="layui-form-mid layui-word-aux"><i class="layui-icon choose_user">&#xe621;</i> </div>
                    </div>
                </div>
                <div class="layui-col-md6">
                    <div class="layui-form-item">
                        <label class="layui-form-label">删除流程触发器</label>
                        <div class="layui-input-inline">
                            <input type="hidden" name="proTriDeleted" value="${definition.proTriDeleted}"/>
                            <input type="text" name="proTriDeletedTitle"   lay-verify="required" value="${definition.proTriDeletedTitle}" autocomplete="off" class="layui-input" disabled="disabled">
                        </div>
                        <div class="layui-form-mid layui-word-aux"><i class="layui-icon choose_user">&#xe621;</i> </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">取消暂停触发器</label>
                        <div class="layui-input-inline">
                            <input type="hidden" name="proTriUnpaused" value="${definition.proTriUnpaused}"/>
                            <input type="text" name="proTriUnpausedTitle"   lay-verify="required" value="${definition.proTriUnpausedTitle}" autocomplete="off" class="layui-input" disabled="disabled">
                        </div>
                        <div class="layui-form-mid layui-word-aux"><i class="layui-icon choose_user">&#xe621;</i> </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">取消触发器</label>
                        <div class="layui-input-inline">
                            <input type="hidden" name="proTriCanceled" value="${definition.proTriCanceled}"/>
                            <input type="text" name="proTriCanceledTitle"   lay-verify="required" value="${definition.proTriCanceledTitle}" autocomplete="off" class="layui-input" disabled="disabled">
                        </div>
                        <div class="layui-form-mid layui-word-aux"><i class="layui-icon choose_user">&#xe621;</i> </div>
                    </div>
                </div>
            </div>
        </form>
        <p class="title_p">流程发起权限</p>
        <form class="layui-form" action="">
            <div class="layui-row">
                <div class="layui-col-md12">
                    <div class="layui-form-item">
                        <label class="layui-form-label">个人</label>
                        <div class="layui-input-inline">
                            <input type="text" name="proHeight"   lay-verify="required" value="" autocomplete="off" class="layui-input" />
                        </div>
                        <div class="layui-form-mid layui-word-aux"><i class="layui-icon choose_user">&#xe612;</i> </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">部门</label>
                        <div class="layui-input-inline">
                            <input type="text" name="proHeight"   lay-verify="required" value="" autocomplete="off" class="layui-input" />
                        </div>
                        <div class="layui-form-mid layui-word-aux"><i class="layui-icon choose_user">&#xe612;</i> </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">角色</label>
                        <div class="layui-input-inline">
                            <input type="text" name="proHeight"   lay-verify="required" value="" autocomplete="off" class="layui-input" />
                        </div>
                        <div class="layui-form-mid layui-word-aux"><i class="layui-icon choose_user">&#xe612;</i> </div>
                    </div>
                </div>
            </div>
        </form>
        <p class="title_p">流程图配置</p>
        <form class="layui-form" action="">
            <div class="layui-row">
                <div class="layui-col-md6">
                    <div class="layui-form-item">
                        <label class="layui-form-label">流程图高度</label>
                        <div class="layui-input-block">
                            <input type="text" name="proHeight"   lay-verify="required" value="${definition.proHeight}" autocomplete="off" class="layui-input" />
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">流程图标题x坐标</label>
                        <div class="layui-input-block">
                            <input type="text" name="proTitleX"  lay-verify="required" value="${definition.proTitleX}" autocomplete="off" class="layui-input" />
                        </div>
                    </div>
                </div>
                <div class="layui-col-md6">
                    <div class="layui-form-item">
                        <label class="layui-form-label">流程图宽度</label>
                        <div class="layui-input-block">
                            <input type="text" name="proWidth"   lay-verify="required" value="${definition.proWidth}" autocomplete="off" class="layui-input" />
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">流程图标题y坐标</label>
                        <div class="layui-input-block">
                            <input type="text" name="proTitleY"  lay-verify="required" value="${definition.proTitleY}" autocomplete="off" class="layui-input" />
                        </div>
                    </div>
                </div>
            </div>
        </form>
    </div>
</div>

</body>

</html>
<script type="text/javascript" src="<%=basePath%>/resources/js/layui.all.js"></script>
<script type="text/javascript" src="<%=basePath%>/resources/tree/js/jquery.ztree.core.js"></script>
<script type="text/javascript" src="<%=basePath%>/resources/tree/js/jquery.ztree.excheck.js"></script>
<script type="text/javascript" src="<%=basePath%>/resources/tree/js/jquery.ztree.exedit.js"></script>
<script>
    layui.use('laydate', function(){
        var laydate = layui.laydate;
        laydate.render({
            elem: '#test1'
        });
    });
    layui.use('laydate', function(){
        var laydate = layui.laydate;
        laydate.render({
            elem: '#test2'
        });
    });
    $(function(){
        var url = common.getPath() + 'sysDepartment/treeDisplay';
        //tree展示
        setting.callback={onClick: onClick}
        treeDisplay(url,'treeDemo');
        pageBreak($('#pageNo').val());
        $(".cancel_btn").click(function(){
            $(".display_container").css("display","none");
            $(".display_container1").css("display","none");
            $(".display_container2").css("display","none");
            $(".display_container6").css("display","none");
        });

    })
</script>