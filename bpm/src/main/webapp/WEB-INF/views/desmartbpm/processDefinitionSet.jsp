<!DOCTYPE html>
<%@ page language="java"  pageEncoding="UTF-8"%>
<html>
<head>
    <title>流程编辑</title>
    <%@include file="common/head.jsp" %>
    <%@include file="common/tag.jsp" %>
    <link rel="stylesheet" href="<%=basePath%>/resources/desmartbpm/css/my.css" media="all">
    <link href="<%=basePath%>/resources/desmartbpm/tree/css/demo.css" rel="stylesheet">
    <link href="<%=basePath%>/resources/desmartbpm/tree/css/zTreeStyle/zTreeStyle.css" rel="stylesheet">
    <style>
        .layui-form-label{width:140px;}
        .layui-input-block{margin-left:170px;}
        .choose_tri{cursor:pointer;}
        .display_container3 {
            height: 600px;
        }
        .layui-form-item .layui-input-inline{width:70%;}
	    .colorli {
	        background-color: #9DA5EC;
	        color: white;
	    }
		.show_user_div,.select_ul{border:1px solid #ccc;}
		.show_user_div ul li,.select_ul li{height:35px;line-height:35px;list-style:none;padding:0 10px}
		.foot_temp{text-align:right;margin-top:20px}
        .p1 { height: 30px; text-align: center; width: 280px; background-color: #f5f5f5; line-height: 30px; }
        .startBusinessKeyUlDiv { color: black; height: 368px; width: 280px; margin: 0; padding: 0; overflow-y: auto; overflow-x: hidden; }
        .startBusinessKeyUl { width: 280px; margin: 0; padding: 0; }
        .colorli { background-color: #9DA5EC; color: white; }
        .tempColorli { background-color: #9DA5EC; color: white; border-top: 1px solid green; border-bottom: 1px solid green; }
        .startBusinessKeyUl li { height: 35px; line-height: 35px; list-style: none; padding: 0 10px; text-align: center; cursor: pointer; font-size: 14px; border-top: 1px solid white; border-bottom: 1px solid white; }
        .chooseStartBusinessKeyDiv { float: left; width: 280px; height: 398px; margin: 0; padding: 0; border: 1px solid #ccc; }
    </style>
</head>
<body>
<div class="container">
    <div class="search_area">
        <div class="search_area top_btn">
            <input type="hidden" name="proAppId" value="${definition.proAppId}"/>
            <input type="hidden" name="proUid" value="${definition.proUid}"/>
            <input type="hidden" name="proVerUid" value="${definition.proVerUid}"/>
            <input type="hidden" id = "isAllUserStart" value="${definition.isAllUserStart}">
            <button class="layui-btn layui-btn-primary layui-btn-sm" id="back_btn">返回</button>
            <span style="float:right;">
						<button class="layui-btn layui-btn-primary layui-btn-sm" id="save_btn">保存</button>
						<!--  <button class="layui-btn layui-btn-primary layui-btn-sm" id="finish_btn">配置完成</button>-->
					</span>
        </div>
    </div>
    <div>
        <p class="title_p" style="display: none;">配置</p>
        <form class="layui-form" action="" id="form1" style="display: none;">
            <div class="layui-row">
                <div class="layui-col-md6">
                    <div class="layui-form-item">
                        <label class="layui-form-label">流程所需时间（数量）</label>
                        <div class="layui-input-block">
                            <input type="text" name="proTime"   lay-verify="required" value="1" autocomplete="off" class="layui-input" />
                        </div>
                    </div>
                    <div class="layui-form-item" style="display: none;">
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
                            <select name="proTimeUnit" lay-verify="required" value="">
                                <option value=""></option>
                                <option value="hour" <c:if test="${definition.proTimeUnit=='hour'}" >selected</c:if>>小时</option>
                                <option value="day" <c:if test="${definition.proTimeUnit=='day'}" >selected</c:if>>天</option>
                                <option value="month" <c:if test="${definition.proTimeUnit=='month'}" >selected</c:if>>月</option>
                            </select>
                        </div>
                    </div>
                    <div class="layui-form-item" style="display: none;">
                        <label class="layui-form-label">发送邮件通知的模板</label>
                        <div class="layui-input-block">
                            <input type="text" name="proDerivationScreenTpl"  lay-verify="required" value="${definition.proDerivationScreenTpl}" autocomplete="off" class="layui-input" />
                        </div>
                    </div>
                </div>
            </div>
        </form>
        <p class="title_p" style="display: none;">触发器配置</p>
        <form class="layui-form" action="" id="form2" style="display: none;">
            <div class="layui-row">
                <div class="layui-col-md6">
                    <div class="layui-form-item">
                        <label class="layui-form-label">发起流程触发器</label>
                        <div class="layui-input-inline">
                            <input type="hidden" id="proTriStart" name="proTriStart" value="${definition.proTriStart}"/>
                            <input type="text" id="proTriStartTitle" name="proTriStartTitle"   lay-verify="required" value="${definition.proTriStartTitle}" autocomplete="off" class="layui-input" disabled="disabled">
                        </div>
                        <div class="layui-form-mid layui-word-aux"><i class="layui-icon choose_tri" data-identify="proTriStart">&#xe621;</i> </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">流程暂停触发器</label>
                        <div class="layui-input-inline">
                            <input type="hidden" id="proTriPaused" name="proTriPaused" value="${definition.proTriPaused}"/>
                            <input type="text" id="proTriPausedTitle" name="proTriPausedTitle"   lay-verify="required" value="${definition.proTriPausedTitle}" autocomplete="off" class="layui-input" disabled="disabled">
                        </div>
                        <div class="layui-form-mid layui-word-aux"><i class="layui-icon choose_tri" data-identify="proTriPaused">&#xe621;</i> </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">重新分配触发器</label>
                        <div class="layui-input-inline">
                            <input type="hidden" id="proTriReassigned" name="proTriReassigned" value="${definition.proTriReassigned}"/>
                            <input type="text" id="proTriReassignedTitle" name="proTriReassignedTitle"   lay-verify="required" value="${definition.proTriReassignedTitle}" autocomplete="off" class="layui-input" disabled="disabled">
                        </div>
                        <div class="layui-form-mid layui-word-aux"><i class="layui-icon choose_tri" data-identify="proTriReassigned">&#xe621;</i> </div>
                    </div>
                </div>
                <div class="layui-col-md6">
                    <div class="layui-form-item">
                        <label class="layui-form-label">删除流程触发器</label>
                        <div class="layui-input-inline">
                            <input type="hidden" id="proTriDeleted" name="proTriDeleted" value="${definition.proTriDeleted}"/>
                            <input type="text" id="proTriDeletedTitle" name="proTriDeletedTitle"   lay-verify="required" value="${definition.proTriDeletedTitle}" autocomplete="off" class="layui-input" disabled="disabled">
                        </div>
                        <div class="layui-form-mid layui-word-aux"><i class="layui-icon choose_tri" data-identify="proTriDeleted">&#xe621;</i> </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">取消暂停触发器</label>
                        <div class="layui-input-inline">
                            <input type="hidden" id="proTriUnpaused" name="proTriUnpaused" value="${definition.proTriUnpaused}"/>
                            <input type="text" id="proTriUnpausedTitle" name="proTriUnpausedTitle"   lay-verify="required" value="${definition.proTriUnpausedTitle}" autocomplete="off" class="layui-input" disabled="disabled">
                        </div>
                        <div class="layui-form-mid layui-word-aux"><i class="layui-icon choose_tri" data-identify="proTriUnpaused">&#xe621;</i> </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">取消触发器</label>
                        <div class="layui-input-inline">
                            <input type="hidden" id="proTriCanceled" name="proTriCanceled" value="${definition.proTriCanceled}"/>
                            <input type="text" id="proTriCanceledTitle" name="proTriCanceledTitle"   lay-verify="required" value="${definition.proTriCanceledTitle}" autocomplete="off" class="layui-input" disabled="disabled">
                        </div>
                        <div class="layui-form-mid layui-word-aux"><i class="layui-icon choose_tri" data-identify="proTriCanceled">&#xe621;</i> </div>
                    </div>
                </div>
            </div>
        </form>
        <p class="title_p">流程发起权限</p>
        <form class="layui-form" action="" id="form3">
            <div class="layui-row">
                <div class="layui-col-md12">
               	 	<div class="layui-form-item">
                        <label class="layui-form-label">是否全员可发起</label>
                        <div class="layui-input-inline">
                            <input type="radio" lay-filter="isAllUserStart"  name="isAllUserStart" value="TRUE" title="是" />
                            <input type="radio" lay-filter="isAllUserStart"  name="isAllUserStart" value="FALSE" title="否" checked />
                        </div>
                   	</div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">个人</label>
                        <div class="layui-input-inline">
                            <input type="text" name="permissionStartUser_view" id="permissionStartUser_view"  lay-verify="required" value="" autocomplete="off" class="layui-input" disabled="disabled"/>
                            <input type="hidden" name="permissionStartUser" id="permissionStartUser"/>
                        </div>
                        <div class="layui-form-mid layui-word-aux"><i class="layui-icon choose_user"  style="margin-right:10px;" id="chooseUser_btn">&#xe612;</i> </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">角色</label>
                        <div class="layui-input-inline" >
                            <input type="text"  name="permissionStartRole_view" id="permissionStartRole_view"  lay-verify="required" value="" autocomplete="off" class="layui-input" disabled="disabled"/>
                            <input type="hidden" name="permissionStartRole" id="permissionStartRole"/>
                        </div>
                        <div class="layui-form-mid layui-word-aux"><i class="layui-icon choose_user" style="margin-right:10px;" id="chooseRole_btn">&#xe612;</i> </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">角色组</label>
                        <div class="layui-input-inline">
                            <input type="text" name="permissionStartTeam_view" id="permissionStartTeam_view"   lay-verify="required" value="" autocomplete="off" class="layui-input" disabled="disabled"/>
                            <input type="hidden" name="permissionStartTeam" id="permissionStartTeam"/>
                        </div>
                        <div class="layui-form-mid layui-word-aux"><i class="layui-icon choose_user" style="margin-right:10px;" id="chooseTeam_btn">&#xe612;</i> </div>
                    </div>
                </div>
            </div>
        </form>
        <p class="title_p">可供发起的关键字</p>
        <form class="layui-form" action="" id="businessKeyForm">
            <div class="layui-row">
                <div class="layui-col-md12">
                    <div class="layui-form-item">
                        <label class="layui-form-label">关键字</label>
                        <div class="layui-input-inline">
                            <input type="text" id="proStartBusinessKey" name="proStartBusinessKey" value="${definition.proStartBusinessKey}" autocomplete="off" class="layui-input" disabled="disabled"/>
                        </div>
                        <div class="layui-form-mid layui-word-aux"><i class="layui-icon choose_user"  style="margin-right:10px;" id="showContinerI">&#xe615;</i> </div>
                    </div>
                </div>
            </div>
        </form>
        <p class="title_p" style="display:none">流程图配置</p>
        <form class="layui-form" action="" id="form4" style="display:none">
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
<!-- 选择触发器弹框 -->
<div class="display_container3" id="chooseTrigger_container" >
    <div class="display_content3"  style="height:450px;">
        <div class="top">
            选择触发器
        </div>
        <div class="middle1" style="height:300px;">
            <div class="search_area">
                <div class="layui-row layui-form" style="margin-top:10px">
                    <div class="layui-col-md5"><input id="triTitle_input" type="text" placeholder="触发器名称"  class="layui-input"></div>
                    <div class="layui-col-md5">
                        <select lay-verify="required" id="triType_sel">
                            <option value="">请选择类型</option>
                            <option value="javaclass">javaclass</option>
                            <option value="script">script</option>
                            <option value="sql">sql</option>
                            <option value="interface">interface</option>
                        </select>
                    </div>
                    <div class="layui-col-md2" style="text-align:right"><button class="layui-btn" id="searchTrigger_btn">查询</button></div>

                </div>
            </div>
            <table class="layui-table backlog_table" lay-even lay-skin="nob" text-overflow: ellipsis>
                <colgroup>
                    <col>
                    <col>
                    <col>
                    <col>
                    <col>
                </colgroup>
                <thead>
                <tr>
                    <th>序号</th>
                    <th>触发器名称</th>
                    <th>触发器类型</th>
                    <th>触发器内容</th>
                    <th>触发器参数</th>
                </tr>
                </thead>
                <tbody id="chooseTrigger_tbody"></tbody>
            </table>
        </div>
        <div id="lay_page"></div>
        <div class="foot">
            <button class="layui-btn layui-btn sure_btn" id="chooseTrigger_sureBtn">确定</button>
            <button class="layui-btn layui-btn layui-btn-primary cancel_btn" id="chooseTrigger_cancelBtn">取消</button>
        </div>
    </div>
</div>
<!-- 选择可发起步骤关键字弹框 -->
<div class="display_container6" id="chooseStartBusinessKeyContainer">
    <div class="display_content6" style="height: 500px; width: 730px;color:black;">
        <div class="top" style="font-size: 18px;">请选择可供发起的步骤关键字</div>
        <div class="middle6" style="height: 400px; width: 715px;overflow-y: hidden;">
            <div style="margin-left: 42.5px;" class="chooseStartBusinessKeyDiv">
                <p class="p1">可供选择的关键字</p>
                <div class="startBusinessKeyUlDiv" >
                    <ul   class="startBusinessKeyUl" id="unstartUl"></ul>
                </div>
            </div>
            <div class="middle_div">
                <button id="toRightBtn"
                        class="layui-btn layui-btn-sm" style="margin-top: 150px;">&nbsp;&nbsp;&gt;&nbsp;&nbsp;</button>
                <br> <br>
                <button id="toLeftBtn"
                        class="layui-btn layui-btn-sm">&nbsp;&nbsp;&lt;&nbsp;&nbsp;</button>
            </div>
            <div   class="chooseStartBusinessKeyDiv" style="">
                <p class="p1">可发起流程的关键字</p>
                <div class="startBusinessKeyUlDiv" >
                    <ul  class="startBusinessKeyUl" id="startUl"></ul>
                </div>
            </div>
            <h1 style="clear: both;"></h1>
        </div>
        <div class="foot">
            <button class="layui-btn layui-btn sure_btn"
                    id="startBusinessKeySureBtn">确定</button>
            <button class="layui-btn layui-btn layui-btn-primary cancel_btn"
                    onclick="$('#chooseStartBusinessKeyContainer').hide();">取消</button>
        </div>
    </div>
</div>

</body>


<script>
    var stepBusinessKeyStr = '<c:forEach items="${businessKeys}" var="businssKey">${businssKey};</c:forEach>';
</script>
<script type="text/javascript" src="<%=basePath%>/resources/desmartbpm/js/layui.all.js"></script>
<script type="text/javascript" src="<%=basePath%>/resources/desmartbpm/js/my/processDefinitionSet.js"></script>
<script type="text/javascript" src="<%=basePath%>/resources/desmartbpm/tree/js/jquery.ztree.core.js"></script>
<script type="text/javascript" src="<%=basePath%>/resources/desmartbpm/tree/js/jquery.ztree.excheck.js"></script>
<script type="text/javascript" src="<%=basePath%>/resources/desmartbpm/tree/js/jquery.ztree.exedit.js"></script>
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

</script>
<style>

</style>
</html>