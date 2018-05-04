<!DOCTYPE html>
<%@ page language="java"  pageEncoding="UTF-8"%>
<html>
    <head>
        <title>环节配置</title>
        <%@include file="common/head.jsp" %>
        <%@include file="common/tag.jsp" %>
        <style>
            .layui-form-label{width:140px;}
            .layui-input-block{margin-left:170px;}
            .layui-colla-item .layui-icon{font-size:10px;}
            #set_detail>.layui-tab.divActive{display:block;}
            #set_detail>.layui-tab{display:none;}
            .colorli {
	            background-color: #9DA5EC;
	            color: white;
	        }
        </style>
    </head>
    <body>
        <div class="container">
            <div class="search_area">
                <div class="search_area top_btn">
                    <button class="layui-btn layui-btn-primary layui-btn-sm" id="back_btn">返回</button>
                    <span style="float:right;">
                        <button class="layui-btn layui-btn-primary layui-btn-sm" onclick="save('');">保存</button>
                    </span>
                </div>
            </div>
            <div style="margin-top:20px;">
                <form class="layui-form" action="">
                    <div class="layui-row">
                        <div class="layui-col-md6">                         
                            <div class="layui-form-item">
                                <label class="layui-form-label">流程名称</label>
                                <div class="layui-input-block">
                                    <input type="text" name="title" required  value="${processDefinition.proName}" autocomplete="off" class="layui-input" disabled="disabled">
                                </div>
                            </div>
                            <div class="layui-form-item">
                                <label class="layui-form-label">快照号</label>
                                <div class="layui-input-block">
                                    <input type="text" name="title" required  value="${processDefinition.proVerUid}" autocomplete="off" class="layui-input" disabled="disabled">
                                </div>
                            </div>                          
                        </div>
                        <div class="layui-col-md6">
                            <div class="layui-form-item">
                                <label class="layui-form-label">快照名称</label>
                                <div class="layui-input-block">
                                    <input type="text" name="title" required  value="${lswSnapshot.name}" autocomplete="off" class="layui-input" disabled="disabled">
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
                
                <div class="layui-tab">
                    <ul class="layui-tab-title">
                        <li class="layui-this" >人工环节</li>
                        <li>网关环节列表</li>
                    </ul>
                    <div class="layui-tab-content">
                        <div class="layui-tab-item layui-show">
                            <div class="layui-row">
                                <div class="layui-col-md2">
                                    <!-- 折叠层 -->
                                    <div class="layui-collapse" lay-accordion id="my_collapse" lay-filter="demo">
                                    </div>                                  
                                </div>
                                <div class="layui-col-md10 set_detail" id="set_detail">
                                    <div class="layui-tab divActive">
                                        <ul class="layui-tab-title">
                                            <li class="layui-this">环节属性</li>
                                            <li>环节SLA配置</li>
                                            <li>步骤配置</li>
                                        </ul>
                                        <div class="layui-tab-content">
                                            <!-- 环节属性配置开始 -->
                                            <div class="layui-tab-item layui-show">
                                                <form class="layui-form" action="" id="config_form">
                                                    <input type="hidden" name=actcUid value=""/>
                                                    <div class="layui-row">
                                                        <div class="layui-col-md6">
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">默认处理人</label>
                                                                <div class="layui-input-block">
                                                                    <select name="actcAssignType" lay-filter="assignType" lay-verify="required">
                                                                        <option value="none">&nbsp;</option>
                                                                        <option value="role">角色</option>
                                                                        <option value="team">角色组</option>
                                                                        <option value="roleAndDepartment">角色 + 部门</option>
                                                                        <option value="roleAndCompany">角色 + 公司编码</option>
                                                                        <option value="teamAndDepartment">角色组 + 部门</option>
                                                                        <option value="teamAndCompany">角色组 + 公司编码</option>
                                                                        <option value="leaderOfPreActivityUser">上个环节提交人的上级</option>
                                                                        <option value="users">指定人员</option>
                                                                        <option value="processCreator">流程发起人</option>
                                                                        <option value="byField">根据表字段选择</option>
                                                                    </select>
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item " id="handleUser_div" style="display:none;">
                                                                <label class="layui-form-label">人员</label>
                                                                <div class="layui-input-block" style="position:relative;">
                                                                    <input type="hidden" id="handleUser" name="handleUser" />
                                                                    <input type="text" name="handleUser_view"  id="handleUser_view"  autocomplete="off" class="layui-input" disabled="disabled">
                                                                    <i class="layui-icon choose_user" id="choose_handle_user" title="选择人员">&#xe612;</i>  
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item" id="handleRole_div" style="display:none;">
                                                                <label class="layui-form-label">角色</label>
                                                                <div class="layui-input-block" style="position:relative;">
                                                                    <input type="hidden" id="handleRole" name="handleRole"   autocomplete="off" class="layui-input">
                                                                    <input type="text" id="handleRole_view" name="handleRole_view"  value="" autocomplete="off" class="layui-input" disabled="disabled">
                                                                    <i id="choose_handle_role" class="layui-icon choose_role" title="选择角色">&#xe612;</i>  
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item " id="handleTeam_div" style="display:none;">
                                                                <label class="layui-form-label">角色组</label>
                                                                <div class="layui-input-block" style="position:relative;">
                                                                    <input type="hidden" id="handleTeam" name="handleTeam"  value="" autocomplete="off" class="layui-input">
                                                                    <input type="text" id="handleTeam_view" name="handleTeam_view"  value="" autocomplete="off" class="layui-input" disabled="disabled">
                                                                    <i id="choose_handle_team" class="layui-icon choose_role" title="选择角色组">&#xe612;</i>  
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item " id="handleField_div" style="display:none;">
                                                                <label class="layui-form-label">字段名称</label>
                                                                <div class="layui-input-block" style="position:relative;">
                                                                    <input type="text" name="handleField"  value="" autocomplete="off" class="layui-input">
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">是否允许手动选择</label>
                                                                <div class="layui-input-block">
                                                                    <input type="radio" name="actcCanChooseUser" value="TRUE" title="可选" >
                                                                    <input type="radio" name="actcCanChooseUser" value="FALSE" title="不可选" >
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">是否可以驳回</label>
                                                                <div class="layui-input-block">
                                                                    <input type="radio" name="actcCanReject" lay-filter="canReject" value="TRUE" title="可以驳回">
                                                                    <input type="radio" name="actcCanReject" lay-filter="canReject" value="FALSE" title="不可以驳回" >
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item" id="rejectType_div" style="display:none;">
                                                                <label class="layui-form-label">驳回方式</label>
                                                                <div class="layui-input-block">
                                                                    <select name="actcRejectType" lay-filter="rejectType" lay-verify="required">
                                                                        <option value="toPreActivity">驳回到上个环节</option>
                                                                        <option value="toProcessStart">驳回到发起人</option>
                                                                        <option value="toActivities">驳回到指定环节</option>
                                                                    </select>
                                                                </div>
                                                            </div>                                                          
                                                            <div class="layui-form-item" id="rejectActivities_div" style="display:none;">
                                                                <label class="layui-form-label">驳回环节号</label>
                                                                <div class="layui-input-block" style="position:relative;">
                                                                    <input type="hidden" name="rejectActivities" id="rejectActivities"/>
                                                                    <input type="text" name="rejectActivities_view" id="rejectActivities_view"  value="" autocomplete="off" class="layui-input" disabled="disabled">
                                                                    <i class="layui-icon choose_num" id="chooseActivity_i" title="选择环节" >&#xe615;</i>
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">是否允许自动提交</label>
                                                                <div class="layui-input-block">
                                                                    <input type="radio" name="actcCanAutocommit" value="TRUE" title="允许" >
                                                                    <input type="radio" name="actcCanAutocommit" value="FALSE" title="禁止">
                                                                </div>
                                                            </div>
                                                         
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">是否可以审批</label>
                                                                <div class="layui-input-block">
                                                                    <input type="radio" name="actcCanApprove" value="TRUE" title="可以审批" >
                                                                    <input type="radio" name="actcCanApprove" value="FALSE" title="不能审批">
                                                                </div>
                                                            </div>                                                          
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">环节排序号</label>
                                                                <div class="layui-input-block">
                                                                    <input type="text" name="actcSort" required  lay-verify="required" value="" autocomplete="off" class="layui-input">
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">分配变量名称</label>
                                                                <div class="layui-input-block">
                                                                    <select name="actcAssignVariable" lay-verify="required">
                                                                        <option value="nextOwners_0">nextOwners_0</option>
                                                                        <option value="nextOwners_1">nextOwners_1</option>
                                                                        <option value="nextOwners_2">nextOwners_2</option>
                                                                        <option value="nextOwners_3">nextOwners_3</option>
                                                                        <option value="nextOwners_4">nextOwners_4</option>
                                                                        <option value="nextOwners_5">nextOwners_5</option>
                                                                        <option value="nextOwners_6">nextOwners_6</option>
                                                                        <option value="nextOwners_7">nextOwners_7</option>
                                                                        <option value="nextOwners_8">nextOwners_8</option>
                                                                        <option value="nextOwners_9">nextOwners_9</option>
                                                                        <option value="nextOwners_10">nextOwners_10</option>
                                                                        <option value="nextOwners_11">nextOwners_11</option>
                                                                        <option value="nextOwners_12">nextOwners_12</option>
                                                                        <option value="nextOwners_13">nextOwners_13</option>
                                                                        <option value="nextOwners_14">nextOwners_14</option>
                                                                        <option value="nextOwners_15">nextOwners_15</option>
                                                                        <option value="nextOwners_16">nextOwners_16</option>
                                                                        <option value="nextOwners_17">nextOwners_17</option>
                                                                        <option value="nextOwners_18">nextOwners_18</option>
                                                                        <option value="nextOwners_19">nextOwners_19</option>
                                                                        <option value="nextOwners_20">nextOwners_20</option>
                                                                    </select>
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">会签变量名称</label>
                                                                <div class="layui-input-block">
                                                                    <select name="signCountVarname" lay-verify="required">
                                                                        <option value="signCount_0">signCount_0</option>
                                                                        <option value="signCount_1">signCount_1</option>
                                                                        <option value="signCount_2">signCount_2</option>
                                                                        <option value="signCount_3">signCount_3</option>
                                                                        <option value="signCount_4">signCount_4</option>
                                                                        <option value="signCount_5">signCount_5</option>
                                                                        <option value="signCount_6">signCount_6</option>
                                                                        <option value="signCount_7">signCount_7</option>
                                                                        <option value="signCount_8">signCount_8</option>
                                                                        <option value="signCount_9">signCount_9</option>
                                                                        <option value="signCount_10">signCount_10</option>
                                                                        <option value="signCount_11">signCount_11</option>
                                                                        <option value="signCount_12">signCount_12</option>
                                                                        <option value="signCount_13">signCount_13</option>
                                                                        <option value="signCount_14">signCount_14</option>
                                                                        <option value="signCount_15">signCount_15</option>
                                                                        <option value="signCount_16">signCount_16</option>
                                                                        <option value="signCount_17">signCount_17</option>
                                                                        <option value="signCount_18">signCount_18</option>
                                                                        <option value="signCount_19">signCount_19</option>
                                                                        <option value="signCount_20">signCount_20</option>
                                                                    </select>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class="layui-col-md6">
                                                            
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">是否允许转签</label>
                                                                <div class="layui-input-block">
                                                                    <input type="radio" name="actcCanTransfer" value="TRUE" title="允许" >
                                                                    <input type="radio" name="actcCanTransfer" value="FALSE" title="不允许">
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">允许上传附件</label>
                                                                <div class="layui-input-block">
                                                                    <input type="radio" name="actcCanUploadAttach" value="TRUE" title="允许" >
                                                                    <input type="radio" name="actcCanUploadAttach" value="FALSE" title="不允许">
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">能否编辑附件</label>
                                                                <div class="layui-input-block">
                                                                    <input type="radio" name="actcCanEditAttach" value="TRUE" title="可以编辑" >
                                                                    <input type="radio" name="actcCanEditAttach" value="FALSE" title="不能编辑">
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">允许删除附件</label>
                                                                <div class="layui-input-block">
                                                                    <input type="radio" name="actcCanDeleteAttach" value="TRUE" title="允许" >
                                                                    <input type="radio" name="actcCanDeleteAttach" value="FALSE" title="不允许">
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">允许代理</label>
                                                                <div class="layui-input-block">
                                                                    <input type="radio" name="actcCanDelegate" value="TRUE" title="允许" >
                                                                    <input type="radio" name="actcCanDelegate" value="FALSE" title="不允许">
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">是否允许取回</label>
                                                                <div class="layui-input-block">
                                                                    <input type="radio" name="actcCanRevoke" value="TRUE" title="允许" >
                                                                    <input type="radio" name="actcCanRevoke" value="FALSE" title="不允许">
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">是否允许加签</label>
                                                                <div class="layui-input-block">
                                                                    <input type="radio" name="actcCanAdd" value="TRUE" title="允许" >
                                                                    <input type="radio" name="actcCanAdd" value="FALSE" title="不允许">
                                                                </div>
                                                            </div> 
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">是否邮件通知本环节处理人</label>
                                                                <div class="layui-input-block">
                                                                    <input type="radio" name="actcCanMailNotify" value="TRUE" title="是" >
                                                                    <input type="radio" name="actcCanMailNotify" value="FALSE" title="否">
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">邮件通知模板</label>
                                                                <div class="layui-input-block">
                                                                    <input type="text" name="actcMailNotifyTemplate"  value="" autocomplete="off" class="layui-input" >
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">是否短信通知本环节处理人</label>
                                                                <div class="layui-input-block">
                                                                    <input type="radio" name="actcCanMessageNotify" value="TRUE" title="是" >
                                                                    <input type="radio" name="actcCanMessageNotify" value="FALSE" title="否">
                                                                </div>
                                                            </div>
                                                        </div>                                                      
                                                    </div>
                                                </form>
                                            </div>
                                            
                                            <!-- 环节SLA配置开始  -->
                                            <div class="layui-tab-item">
                                                <form class="layui-form" action="" id="sla_form">
                                                    <div class="layui-row">
                                                        <div class="layui-col-md6">
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">运行时长</label>
                                                                <div class="layui-input-block">
                                                                    <input type="text" name="actcTime" value="" autocomplete="off" class="layui-input" >
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">超时通知内容模板</label>
                                                                <div class="layui-input-block">
                                                                    <input type="text" name="actcOuttimeTemplate" value="" autocomplete="off" class="layui-input" >
                                                                </div>
                                                            </div>
										                    <div class="layui-form-item ">
                                                                <label class="layui-form-label">超时触发器</label>
                                                                <div class="layui-input-block" style="position:relative;">
                                                                    <input type="hidden" id="actcOuttimeTrigger" name="actcOuttimeTrigger" value=""/>
                                                                    <input type="text"  id="actcOuttimeTriggerTitle"  name="actcOuttimeTriggerTitle" value="" autocomplete="off" class="layui-input" disabled="disabled">
                                                                    <i id="choose_outtimeTri_btn" class="layui-icon choose_role" title="选择触发器">&#xe621;</i>  
                                                                </div>
                                                            </div>                                                        
                                                        </div>
                                                        <div class="layui-col-md6">
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">运行时间单位</label>
                                                                <div class="layui-input-block">
                                                                    <select name="actcTimeunit" lay-verify="required">
                                                                        <option value="hour">小时</option>
                                                                        <option value="day">天</option>
                                                                        <option value="month">月</option>
                                                                    </select>                                           
                                                                </div>
                                                            </div>
                                                            <!-- 超时通知 -->
															<div class="layui-form-item " id="outtimeUser_div" >
															    <label class="layui-form-label">超时通知人员</label>
															    <div class="layui-input-block" style="position:relative;">
															        <input type="hidden" id="outtimeUser" name="outtimeUser" />
															        <input type="text" name="outtimeUser_view"  id="outtimeUser_view"  value="" autocomplete="off" class="layui-input">
															        <i class="layui-icon choose_user" id="choose_outtime_user" title="选择人员">&#xe612;</i>
															    </div>
															</div>
															<div class="layui-form-item" id="outtimeRole_div" >
															    <label class="layui-form-label">超时通知角色</label>
															    <div class="layui-input-block" style="position:relative;">
															        <input type="hidden" id="outtimeRole" name="outtimeRole"  value="" autocomplete="off" class="layui-input">
															        <input type="text" id="outtimeRole_view" name="outtimeRole_view"  value="" autocomplete="off" class="layui-input">
															        <i id="choose_outtime_role" class="layui-icon choose_role" title="选择角色">&#xe612;</i>
															    </div>
															</div>
															<div class="layui-form-item " id="outtimeTeam_div" >
															    <label class="layui-form-label">超时通知角色组</label>
															    <div class="layui-input-block" style="position:relative;">
															        <input type="hidden" id="outtimeTeam" name="outtimeTeam"  value="" autocomplete="off" class="layui-input">
															        <input type="text" id="outtimeTeam_view" name="outtimeTeam_view"  value="" autocomplete="off" class="layui-input">
															        <i id="choose_outtime_team" class="layui-icon choose_role" title="选择角色组">&#xe612;</i>
															    </div>
															</div>
                                                         
                                                        </div>
                                                    </div>
                                                    <div class="layui-row">
                                                        <div class="layui-col-md12">
                                                            <div class="layui-form-item ">
                                                                <label class="layui-form-label">环节职责</label>
                                                                <div class="layui-input-block" style="position:relative;">
                                                                    <textarea id="editDemo" name="actcResponsibility"  placeholder="" class="layui-textarea" style="height:400px;"></textarea>
                                                                </div>
                                                            </div>
                                                            
                                                        </div>
                                                    </div>
                                                </form>
                                            </div>
                                            
                                            <!--  步骤配置开始 -->
                                            <div class="layui-tab-item">
                                                新增步骤：<button class="layui-btn layui-btn-sm layui-btn-primary add_step" id="add_step_btn">新增步骤</button>
                                                <!-- <p class="title_p">第一步</p> -->
                                                <table class="layui-table backlog_table" lay-even lay-skin="nob">
                                                    <colgroup>
                                                        <col>
                                                        <col>
                                                        <col>
                                                        <col>
                                                    </colgroup>
                                                    <thead>
                                                        <tr>
															<th>步骤序号</th>
															<th>步骤类型</th>
															<th>步骤关键字</th>
															<th>表单名</th>
															<th>触发器名</th>
															<th>操作</th>
                                                        </tr> 
                                                    </thead>
                                                    <tbody id="step_table"></tbody>
                                                </table>                                                
                                            </div>
                                        </div>
                                    </div>
                                    
                                </div>
                            </div>
                        </div>
                        <div class="layui-tab-item">
                            <div class="layui-row">
                                <div class="layui-col-md12">
                                    <div class="layui-tab">
                                        <ul class="layui-tab-title">
                                            <li class="layui-this">并行处理</li>
                                            <li>根据金额判断</li>
                                            <li>汇合</li>
                                        </ul>
                                        <div class="layui-tab-content">
                                            <div class="layui-tab-item layui-show">
                                                <form class="layui-form" action="">
                                                    <div class="layui-row">
                                                        <div class="layui-col-md6">                                                         
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">网关类型</label>
                                                                <div class="layui-input-block">
                                                                    <input type="text" name="title" required  lay-verify="required" value="并行网关" autocomplete="off" class="layui-input" disabled="disabled">
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class="layui-col-md6">
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">网关环节序号</label>
                                                                <div class="layui-input-block">
                                                                    <input type="text" name="title" required  lay-verify="required" value="" autocomplete="off" class="layui-input" >
                                                                </div>
                                                            </div>                                                          
                                                        </div>
                                                        <div class="layui-col-md12">
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">路由变量名称</label>
                                                                <div class="layui-input-block">
                                                                    <table class="layui-table" lay-even lay-skin="nob" >
                                                                        <colgroup>
                                                                            <col>
                                                                            <col>
                                                                            <col>
                                                                            <col>
                                                                            <col>
                                                                        </colgroup>
                                                                        <tbody>
                                                                            <tr>
                                                                                <td><input type="checkbox" id="0"/><label for="0">result_0</label></td>
                                                                                <td><input type="checkbox" id="1"/><label for="1">result_1</label></td>
                                                                                <td><input type="checkbox" id="2"/><label for="2">result_2</label></td>
                                                                                <td><input type="checkbox" id="3"/><label for="3">result_3</label></td>
                                                                                <td><input type="checkbox" id="4"/><label for="4">result_4</label></td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td><input type="checkbox" id="5"/><label for="5">result_5</label></td>
                                                                                <td><input type="checkbox" id="6"/><label for="6">result_6</label></td>
                                                                                <td><input type="checkbox" id="7"/><label for="7">result_7</label></td>
                                                                                <td><input type="checkbox" id="8"/><label for="8">result_8</label></td>
                                                                                <td><input type="checkbox" id="9"/><label for="9">result_9</label></td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td><input type="checkbox" id="10"/><label for="10">result_10</label></td>
                                                                                <td><input type="checkbox" id="11"/><label for="11">result_11</label></td>
                                                                                <td><input type="checkbox" id="12"/><label for="12">result_12</label></td>
                                                                                <td><input type="checkbox" id="13"/><label for="13">result_13</label></td>
                                                                                <td><input type="checkbox" id="14"/><label for="14">result_14</label></td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td><input type="checkbox" id="15"/><label for="15">result_15</label></td>
                                                                                <td><input type="checkbox" id="16"/><label for="16">result_16</label></td>
                                                                                <td><input type="checkbox" id="17"/><label for="17">result_17</label></td>
                                                                                <td><input type="checkbox" id="18"/><label for="18">result_18</label></td>
                                                                                <td><input type="checkbox" id="19"/><label for="19">result_19</label></td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td><input type="checkbox" id="20"/><label for="20">result_20</label></td>
                                                                            </tr>
                                                                        </tbody>
                                                                    </table>
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">路由变量名称</label>
                                                                <div class="layui-input-block">
                                                                    <button class="layui-btn layui-btn-sm layui-btn-primary create_net">新建</button>
                                                                    <button class="layui-btn layui-btn-sm layui-btn-primary delete_net">删除</button>
                                                                    <table class="layui-table" lay-even lay-skin="nob" >
                                                                        <colgroup>
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
                                                                                <th><input type="checkbox" name="" title='全选' lay-skin="primary"> 序号</th>
                                                                                <th>路由变量</th>
                                                                                <th>字段名称</th>
                                                                                <th>比较运算符</th>
                                                                                <th>字段值</th>
                                                                                <th>字段值类型</th>
                                                                                <th>条件组合运算</th>
                                                                                <th>优先级</th>
                                                                                <th>条件分组</th>
                                                                            </tr> 
                                                                        </thead>
                                                                        <tbody>
                                                                            <tr>
                                                                                <td><input type="checkbox" name="" lay-skin="primary"> 1</td>
                                                                                <td>result_0</td>
                                                                                <td>amount</td>
                                                                                <td>==</td>
                                                                                <td>12</td>
                                                                                <td>text</td>
                                                                                <td>||</td>
                                                                                <td>0</td>
                                                                                <td>1</td>
                                                                            </tr>                                                                           
                                                                        </tbody>
                                                                    </table>
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">条件组合预览</label>
                                                                <div class="layui-input-block">
                                                                    
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </form>
                                            </div>
                                            <div class="layui-tab-item">
                                                <form class="layui-form" action="">
                                                    <div class="layui-row">
                                                        <div class="layui-col-md6">                                                         
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">网关类型</label>
                                                                <div class="layui-input-block">
                                                                    <input type="text" name="title" required  lay-verify="required" value="排它网关" autocomplete="off" class="layui-input" disabled="disabled">
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class="layui-col-md6">
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">网关环节序号</label>
                                                                <div class="layui-input-block">
                                                                    <input type="text" name="title" required  lay-verify="required" value="" autocomplete="off" class="layui-input" >
                                                                </div>
                                                            </div>                                                          
                                                        </div>
                                                        <div class="layui-col-md12">
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">路由变量名称</label>
                                                                <div class="layui-input-block">
                                                                    <table class="layui-table" lay-even lay-skin="nob" >
                                                                        <colgroup>
                                                                            <col>
                                                                            <col>
                                                                            <col>
                                                                            <col>
                                                                            <col>
                                                                        </colgroup>
                                                                        <tbody>
                                                                            <tr>
                                                                                <td><input type="checkbox" id="0"/><label for="0">result_0</label></td>
                                                                                <td><input type="checkbox" id="1"/><label for="1">result_1</label></td>
                                                                                <td><input type="checkbox" id="2"/><label for="2">result_2</label></td>
                                                                                <td><input type="checkbox" id="3"/><label for="3">result_3</label></td>
                                                                                <td><input type="checkbox" id="4"/><label for="4">result_4</label></td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td><input type="checkbox" id="5"/><label for="5">result_5</label></td>
                                                                                <td><input type="checkbox" id="6"/><label for="6">result_6</label></td>
                                                                                <td><input type="checkbox" id="7"/><label for="7">result_7</label></td>
                                                                                <td><input type="checkbox" id="8"/><label for="8">result_8</label></td>
                                                                                <td><input type="checkbox" id="9"/><label for="9">result_9</label></td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td><input type="checkbox" id="10"/><label for="10">result_10</label></td>
                                                                                <td><input type="checkbox" id="11"/><label for="11">result_11</label></td>
                                                                                <td><input type="checkbox" id="12"/><label for="12">result_12</label></td>
                                                                                <td><input type="checkbox" id="13"/><label for="13">result_13</label></td>
                                                                                <td><input type="checkbox" id="14"/><label for="14">result_14</label></td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td><input type="checkbox" id="15"/><label for="15">result_15</label></td>
                                                                                <td><input type="checkbox" id="16"/><label for="16">result_16</label></td>
                                                                                <td><input type="checkbox" id="17"/><label for="17">result_17</label></td>
                                                                                <td><input type="checkbox" id="18"/><label for="18">result_18</label></td>
                                                                                <td><input type="checkbox" id="19"/><label for="19">result_19</label></td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td><input type="checkbox" id="20"/><label for="20">result_20</label></td>
                                                                            </tr>
                                                                        </tbody>
                                                                    </table>
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">路由变量名称</label>
                                                                <div class="layui-input-block">
                                                                    <button class="layui-btn layui-btn-sm layui-btn-primary create_net">新建</button>
                                                                    <button class="layui-btn layui-btn-sm layui-btn-primary delete_net">删除</button>
                                                                    <table class="layui-table" lay-even lay-skin="nob" >
                                                                        <colgroup>
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
                                                                                <th><input type="checkbox" name="" title='全选' lay-skin="primary"> 序号</th>
                                                                                <th>路由变量</th>
                                                                                <th>字段名称</th>
                                                                                <th>比较运算符</th>
                                                                                <th>字段值</th>
                                                                                <th>字段值类型</th>
                                                                                <th>条件组合运算</th>
                                                                                <th>优先级</th>
                                                                                <th>条件分组</th>
                                                                            </tr> 
                                                                        </thead>
                                                                        <tbody>
                                                                            <tr>
                                                                                <td><input type="checkbox" name="" lay-skin="primary"> 1</td>
                                                                                <td>result_0</td>
                                                                                <td>amount</td>
                                                                                <td>==</td>
                                                                                <td>12</td>
                                                                                <td>text</td>
                                                                                <td>||</td>
                                                                                <td>0</td>
                                                                                <td>1</td>
                                                                            </tr>                                                                           
                                                                        </tbody>
                                                                    </table>
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">条件组合预览</label>
                                                                <div class="layui-input-block">
                                                                    
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </form>
                                            </div>
                                            <div class="layui-tab-item">
                                                <form class="layui-form" action="">
                                                    <div class="layui-row">
                                                        <div class="layui-col-md6">                                                         
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">网关类型</label>
                                                                <div class="layui-input-block">
                                                                    <input type="text" name="title" required  lay-verify="required" value="包容网关" autocomplete="off" class="layui-input" disabled="disabled">
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class="layui-col-md6">
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">网关环节序号</label>
                                                                <div class="layui-input-block">
                                                                    <input type="text" name="title" required  lay-verify="required" value="" autocomplete="off" class="layui-input" >
                                                                </div>
                                                            </div>                                                          
                                                        </div>
                                                        <div class="layui-col-md12">
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">路由变量名称</label>
                                                                <div class="layui-input-block">
                                                                    <table class="layui-table" lay-even lay-skin="nob" >
                                                                        <colgroup>
                                                                            <col>
                                                                            <col>
                                                                            <col>
                                                                            <col>
                                                                            <col>
                                                                        </colgroup>
                                                                        <tbody>
                                                                            <tr>
                                                                                <td><input type="checkbox" id="0"/><label for="0">result_0</label></td>
                                                                                <td><input type="checkbox" id="1"/><label for="1">result_1</label></td>
                                                                                <td><input type="checkbox" id="2"/><label for="2">result_2</label></td>
                                                                                <td><input type="checkbox" id="3"/><label for="3">result_3</label></td>
                                                                                <td><input type="checkbox" id="4"/><label for="4">result_4</label></td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td><input type="checkbox" id="5"/><label for="5">result_5</label></td>
                                                                                <td><input type="checkbox" id="6"/><label for="6">result_6</label></td>
                                                                                <td><input type="checkbox" id="7"/><label for="7">result_7</label></td>
                                                                                <td><input type="checkbox" id="8"/><label for="8">result_8</label></td>
                                                                                <td><input type="checkbox" id="9"/><label for="9">result_9</label></td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td><input type="checkbox" id="10"/><label for="10">result_10</label></td>
                                                                                <td><input type="checkbox" id="11"/><label for="11">result_11</label></td>
                                                                                <td><input type="checkbox" id="12"/><label for="12">result_12</label></td>
                                                                                <td><input type="checkbox" id="13"/><label for="13">result_13</label></td>
                                                                                <td><input type="checkbox" id="14"/><label for="14">result_14</label></td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td><input type="checkbox" id="15"/><label for="15">result_15</label></td>
                                                                                <td><input type="checkbox" id="16"/><label for="16">result_16</label></td>
                                                                                <td><input type="checkbox" id="17"/><label for="17">result_17</label></td>
                                                                                <td><input type="checkbox" id="18"/><label for="18">result_18</label></td>
                                                                                <td><input type="checkbox" id="19"/><label for="19">result_19</label></td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td><input type="checkbox" id="20"/><label for="20">result_20</label></td>
                                                                            </tr>
                                                                        </tbody>
                                                                    </table>
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">路由变量名称</label>
                                                                <div class="layui-input-block">
                                                                    <button class="layui-btn layui-btn-sm layui-btn-primary create_net">新建</button>
                                                                    <button class="layui-btn layui-btn-sm layui-btn-primary delete_net">删除</button>
                                                                    <table class="layui-table" lay-even lay-skin="nob" >
                                                                        <colgroup>
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
                                                                                <th><input type="checkbox" name="" title='全选' lay-skin="primary"> 序号</th>
                                                                                <th>路由变量</th>
                                                                                <th>字段名称</th>
                                                                                <th>比较运算符</th>
                                                                                <th>字段值</th>
                                                                                <th>字段值类型</th>
                                                                                <th>条件组合运算</th>
                                                                                <th>优先级</th>
                                                                                <th>条件分组</th>
                                                                            </tr> 
                                                                        </thead>
                                                                        <tbody>
                                                                            <tr>
                                                                                <td><input type="checkbox" name="" lay-skin="primary"> 1</td>
                                                                                <td>result_0</td>
                                                                                <td>amount</td>
                                                                                <td>==</td>
                                                                                <td>12</td>
                                                                                <td>text</td>
                                                                                <td>||</td>
                                                                                <td>0</td>
                                                                                <td>1</td>
                                                                            </tr>                                                                           
                                                                        </tbody>
                                                                    </table>
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">条件组合预览</label>
                                                                <div class="layui-input-block">
                                                                    
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </form>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>              
            </div>
        </div>
        <!-- todo -->
        <!-- 新增步骤弹出框 -->
        <div class="display_container3" id="addStep_container">
            <form id="addStep_form">
            <div class="display_content3" style="height:500px;width:900px;">
                <div class="top">新增步骤</div>
                <div class="middle1" style="height:420px;">
                    <div class="search_area" style="height:38px;">
                        <div class="layui-row layui-form" style="margin-top:10px">
                            <div class="layui-col-md3">
                                <input type="radio" name="stepType" value="form" title="表单"  lay-filter="stepTypeFilter" style="margin-left:10px;"> 
                                <input type="radio" name="stepType" value="trigger" title="触发器" lay-filter="stepTypeFilter"> 
                            </div>
                            <div class="layui-col-md2">
	                           <input id="stepSort" name="stepSort" type="text" placeholder="步骤序号"  class="layui-input">
                            </div>
                            <div class="layui-col-md4" style="padding-left:5px;">
                                <input type="radio" name="stepBusinessKeyType" lay-filter="stepBusinessKey" value="default" title="默认关键字"  checked > 
                                <input type="radio" name="stepBusinessKeyType" lay-filter="stepBusinessKey" value="custom" title="自定义关键字" >    
                            </div>
                            <div class="layui-col-md3">
                                <input id="stepBusinessKey_input" name="stepBusinessKey" type="text" placeholder="输入步骤关键字"  class="layui-input" >
                            </div>
                        </div>
                    </div>
                    <div id="form_innerArea" style="height:362px;">
                        <div style="height:38px;" class="layui-row layui-form">
                            <div class="layui-col-md2" style="margin-right: 5px;">
                               <input id="dynTitle" type="text" placeholder="标题"  class="layui-input">
                            </div>
                            
                            <div class="layui-col-md2" style="margin-right: 5px;">
                               <input id="dynDescription" type="text" placeholder="描述"  class="layui-input">
                            </div>
                            <div class="layui-col-md2">
                               <button class="layui-btn" id="search_form_btn" type="button">查询</button>
                            </div>
                        </div>
                        <div style="height:334px;overflow-y: auto;">
                            <table class="layui-table backlog_table form_table" lay-even lay-skin="nob">
                                <colgroup>
                                    <col>
                                    <col>
                                    <col>
                                </colgroup>
                                <thead>
                                    <tr>
                                      <th>序号</th>
                                      <th>表单名称</th>
                                      <th>表单描述</th>
                                    </tr>
                                </thead>
                                <tbody id="form_tbody">
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div id="trigger_innerArea" style="display:none;padding-top: 30px;padding-right:50px;">
                          <div class="layui-form-item ">
                              <label class="layui-form-label">请选择触发器</label>
                              <div class="layui-input-block" style="position:relative;">
                                  <input type="hidden" id="trigger_of_step" name="" value=""/>
                                  <input type="text"  id="trigger_of_stepTitle"  name="" value="" autocomplete="off" class="layui-input" disabled="disabled">
                                  <i id="choose_stepTri_btn" class="layui-icon choose_role" title="选择触发器">&#xe621;</i>  
                              </div>
                          </div>  
                    </div>
                </div>
                <div id="demo8"></div>
                <div class="foot">
                    <button type="button" class="layui-btn layui-btn sure_btn" onclick="updateStep();">确定</button>
                    <button type="button" class="layui-btn layui-btn layui-btn-primary cancel_btn" onclick="$('#addStep_container').hide();">取消</button>
                </div>
            </div>
        </form>
        </div>
        <div class="display_container4" id="editFieldPermissions">
            <div class="display_content3" style="width: 950px;height: 500px;">
                <div class="top">
                    编辑字段权限
                </div>
                <div class="middle1" style="height: 400px;">
                    <form class="form-horizontal"  >
                    <table class="layui-table" lay-even lay-skin="nob" >
                        <colgroup>
                            <col width="100">
                            <col>
                            <col>
                        </colgroup>
                        <thead>
                            <th><!-- <input type="checkbox"  name="fldUid"  id="field_check" lay-skin="primary"> -->序号</th>
                            <th>字段名称</th>
                            <th><input type="radio" name="radioAll"  lay-skin="primary" onclick="editAllclick(this)">编辑</th>
                            <th><input type="radio" name="radioAll"  lay-skin="primary" id="viewAllclick">只读</th>
                            <th><input type="radio" name="radioAll"  lay-skin="primary" id="hiddenAllclick">隐藏</th>
                        </thead>
                        
                        <tbody id="field_permissions_table" >
                            <tr>
                                <td><input type="checkbox" id="0"/><label for="0"> 1</label></td>
                                <td>字段1</td>
                                <td></td>
                            </tr>
                            <tr>
                                <td><input type="checkbox" id="1"/><label for="1"> 2</label></td>
                                <td>字段2</td>
                                <td></td>
                            </tr>
                            <tr>
                                <td><input type="checkbox" id="2"/><label for="2"> 3</label></td>
                                <td>字段3</td>
                                <td></td>
                            </tr>
                            <tr>
                                <td><input type="checkbox" id="3"/><label for="3"> 4</label></td>
                                <td>字段4</td>
                                <td>只读</td>
                            </tr>
                            <tr>
                                <td><input type="checkbox" id="4"/><label for="4"> 5</label></td>
                                <td>字段5</td>
                                <td>隐藏</td>
                            </tr>
                            <tr>
                                <td><input type="checkbox" id="5"/><label for="5"> 6</label></td>
                                <td>字段6</td>
                                <td>隐藏</td>
                            </tr>
                        </tbody>
                    </table>                
	                    </form>
                </div>
                <div class="foot">
                    <button class="layui-btn layui-btn " id="filedSave">保存</button>
                    <!-- <button class="layui-btn layui-btn " id="filedReadOnly">只读</button> -->
                    <!-- <button class="layui-btn layui-btn " id="filedHide">隐藏</button> -->
                    <button class="layui-btn layui-btn layui-btn-primary cancel_btn">取消</button>
                </div>
            </div>
        </div>
        <!-- 修改步骤关联表单弹出框 -->
        <div class="display_container10" id="update_step_form_container">
            <form id="edtiStep_form">
            <div class="display_content3" style="height:500px;width:900px;">
                <div class="top">修改步骤关联表单信息</div>
                <div class="middle1" style="height:420px;">
                	<input type="hidden" id="eidtstepUid"/>
                    <div class="search_area" style="height:38px;">
                        <div class="layui-row layui-form" style="margin-top:10px">
                            <div class="layui-col-md2">
	                           <input id="updateStepSort" name="stepSort" type="text" placeholder="步骤序号"  class="layui-input">
                            </div>
                            <div class="layui-col-md4" style="padding-left:5px;">
                                <input type="radio" name="edtiStepBusinessKeyType" lay-filter="StepBusinessKey" value="default" title="默认关键字"  checked > 
                                <input type="radio" name="edtiStepBusinessKeyType" lay-filter="edtiStepBusinessKey" value="custom" title="自定义关键字" >    
                            </div>
                            <div class="layui-col-md3">
                                <input id="update_stepBusinessKey_input" name="edtistepBusinessKey" type="text" placeholder="输入步骤关键字"  class="layui-input" >
                            </div>
                        </div>
                    </div>
                    <div id="form_innerArea" style="height:362px;">
                        <div style="height:38px;" class="layui-row layui-form">
                            <div class="layui-col-md2" style="margin-right: 5px;">
                               <input id="updateDynTitle" type="text" placeholder="标题"  class="layui-input">
                            </div>
                            
                            <div class="layui-col-md2" style="margin-right: 5px;">
                               <input id="updateDynTitle" type="text" placeholder="描述"  class="layui-input">
                            </div>
                            <div class="layui-col-md2">
                               <button class="layui-btn" id="update_search_form_btn" type="button">查询</button>
                            </div>
                        </div>
                        <div style="height:334px;overflow-y: auto;">
                            <table class="layui-table backlog_table form_table" lay-even lay-skin="nob">
                                <colgroup>
                                    <col>
                                    <col>
                                    <col>
                                </colgroup>
                                <thead>
                                    <tr>
                                      <th>序号</th>
                                      <th>表单名称</th>
                                      <th>表单描述</th>
                                    </tr>
                                </thead>
                                <tbody id="update_step_form_tbody">
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div id="trigger_innerArea" style="display:none;padding-top: 30px;padding-right:50px;">
                          <div class="layui-form-item ">
                              <label class="layui-form-label">请选择触发器</label>
                              <div class="layui-input-block" style="position:relative;">
                                  <input type="hidden" id="trigger_of_step" name="" value=""/>
                                  <input type="text"  id="trigger_of_stepTitle"  name="" value="" autocomplete="off" class="layui-input" disabled="disabled">
                                  <i id="choose_stepTri_btn" class="layui-icon choose_role" title="选择触发器">&#xe621;</i>  
                              </div>
                          </div>  
                    </div>
                </div>
                <div id="demo8"></div>
                <div class="foot">
                    <button type="button" class="layui-btn layui-btn sure_btn" onclick="updateStep();">确定</button>
                    <button type="button" class="layui-btn layui-btn layui-btn-primary cancel_btn" onclick="$('#addStep_container').hide();">取消</button>
                </div>
            </div>
        </form>
        </div>
        <div class="display_container5">
            <div class="display_content5">
                <div class="top">新增网关</div>
                <div class="middle1">
                    <form class="layui-form" action="">
                        <div class="layui-form-item">
                            <label class="layui-form-label">路由器变量</label>
                            <div class="layui-input-block">
                                <select name="" lay-verify="required">
                                    <option value=""></option>
                                </select>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">字段名称</label>
                            <div class="layui-input-block">
                                <select name="" lay-verify="required">
                                    <option value="amount">amount</option>
                                </select>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">值比较运算符</label>
                            <div class="layui-input-block">
                             <select name="" lay-verify="required">
                                    <option value="=">=</option>
                                    <option value="<"><</option>
                                    <option value=">">></option>
                                    <option value=">=">>=</option>
                                    <option value="<="><=</option>
                                </select>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">字段值</label>
                            <div class="layui-input-block">
                              <input type="text" name="title" required  lay-verify="required" placeholder="" autocomplete="off" class="layui-input">
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">字段值类型</label>
                            <div class="layui-input-block">
                              <select name="" lay-verify="required">
                                    <option value="字符串">字符串</option>
                                     <option value="整数">整数</option>
                                </select>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">条件组合运算符</label>
                            <div class="layui-input-block">
                              <select name="" lay-verify="required">
                                    <option value="与">与</option> 
                                    <option value="或">或</option>
                                </select>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">优先级序号</label>
                            <div class="layui-input-block">
                              <input type="text" name="title" required  lay-verify="required" placeholder="" autocomplete="off" class="layui-input">
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">条件分组名称</label>
                            <div class="layui-input-block">
                              <input type="text" name="title" required  lay-verify="required" placeholder="" autocomplete="off" class="layui-input">
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
        <div class="display_container6" id="choose_activity_container" >
            <div class="display_content6" style="height:500px;width:700px;">
                <div class="top"> 选择退回环节</div>
                <div class="middle6" style="height:400px;width:700px;">
                    <div class="left_div" style="float:left;width:290px;height:350px;margin:10px  0 0 10px;padding:10px;overflow-y:scroll;" class="show_user_div">
                        <ul id="left_activity_ul">
                        </ul>
                    </div>
                    <div class="middle_div">
                        <button onclick="moveActivityToRight();" class="layui-btn layui-btn-sm" style="margin-top:150px;">&nbsp;&nbsp;&gt;&nbsp;&nbsp;</button>
                            <br><br>
                        <button onclick="moveActivityToLeft();" class="layui-btn layui-btn-sm">&nbsp;&nbsp;&lt;&nbsp;&nbsp;</button>
                    </div>
                    <div class="right_div" style="float:left;width:280px;height:350px;margin-top:10px;padding:10px;overflow-y:scroll;">
                        <ul id="right_activity_ul">
                        </ul>
                    </div>
                    <h1 style="clear:both;"></h1>
                </div>
                <div class="foot">
                    <button class="layui-btn layui-btn sure_btn" id="chooseActivities_sureBtn">确定</button>
                    <button class="layui-btn layui-btn layui-btn-primary cancel_btn" onclick="$('#choose_activity_container').hide();">取消</button>
                </div>
            </div>
        </div>
        <!-- 编辑触发器步骤的表单 -->
        <div class="display_container3" id="ETS_container">
            <form id="editStep_form">
            <div class="display_content3" style="height:500px;width:900px;">
                <div class="top">编辑触发器步骤</div>
                <div class="middle1" style="height:420px;">
                    <div class="search_area" style="height:38px;">
                        <div class="layui-row layui-form" style="margin-top:10px">
                            <div class="layui-col-md3">
                                <input id="ETS_stepSort" name="ETS_stepSort" type="text" placeholder="步骤序号"  class="layui-input">
                            </div>
                            <div class="layui-col-md4" style="padding-left:5px;">
                                <input type="radio" id="ETS_radio_default" name="ETS_BusinessKeyType" lay-filter="ETS_stepBusinessKey" value="default" title="默认关键字"  checked > 
                                <input type="radio" id="ETS_radio_custom" name="ETS_BusinessKeyType" lay-filter="ETS_stepBusinessKey" value="custom" title="自定义关键字" >    
                            </div>
                            <div class="layui-col-md5">
                                <input id="ETS_stepBusinessKey"  type="text" placeholder="输入步骤关键字"  class="layui-input" >
                            </div>
                        </div>
                    </div>
                    <div id="trigger_innerArea" style="padding-top: 30px;padding-right:50px;">
                          <div class="layui-form-item ">
                              <label class="layui-form-label">请选择触发器</label>
                              <div class="layui-input-block" style="position:relative;">
                                  <input type="hidden" id="ETS_trigger_of_step" name="" value=""/>
                                  <input type="text"  id="ETS_trigger_of_stepTitle"  name="" value="" autocomplete="off" class="layui-input" disabled="disabled">
                                  <i id="ETS_choose_stepTri_btn" class="layui-icon choose_role" title="选择触发器">&#xe621;</i>  
                              </div>
                          </div>  
                    </div>
                </div>
                <div id="demo8"></div>
                <div class="foot">
                    <button type="button" class="layui-btn layui-btn sure_btn" onclick="updateTriggerStep();">确定</button>
                    <button type="button" class="layui-btn layui-btn layui-btn-primary cancel_btn" onclick="$('#ETS_container').hide();">取消</button>
                </div>
            </div>
            </form>
        </div>
        
		<!-- 选择触发器弹框 -->
		<div class="display_container3" id="chooseTrigger_container" >
		    <div class="display_content3"  style="height:550px;">
		        <div class="top"> 选择触发器</div>
		        <div class="middle1" style="height:400px;">
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
		<!-- 选择触发器弹框结束 -->

    </body>
    
    <script type="text/javascript" src="<%=basePath%>/resources/js/layui.all.js"></script>
    <script type="text/javascript" src="<%=basePath%>/resources/js/my/activityConf.js"></script>

    <script>
        var proAppId = '${processDefinition.proAppId}';
        var proUid = '${processDefinition.proUid}';
        var proVerUid = '${processDefinition.proVerUid}';
        var firstHumanMeta = '${firstHumanMeta}';
        var firstHumanMeteConf = '${firstHumanMeteConf}';
        var activityStr = '<c:forEach items="${humanActivities}" var="humanActivity" varStatus="varStatus"><li data-activityid="${humanActivity.activityId}">${humanActivity.activityName}</li></c:forEach>';
        
        
        window.onload=function(){
            
        }
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
        	$(".cancel_btn").click(function(){
                $(".display_container4").css("display","none");
                $(".display_container10").css("display","none");
            })
        	
            $(".create_net").click(function(){
                $(".display_container5").css("display","block");
            })
           
            $(".edit_role").click(function(){
                $(".display_container4").css("display","block");
                $.ajax({
                  url: common.getPath() + "/formField/queryFieldByFormUidAndStepId",
                  type: "post",
                  dataType: "json",
                  data:{
                   		stepUid:stepUid,
                        formUid:formUid
                  },
                  success: function(result) {
                	  $('#field_permissions_table').empty();
	           		   var trs='';
	           		   $(result.data).each(function(index){
	           			   trs+='<tr>';
	           			   trs+='<td><input type="checkbox" name="tri_check" value="' + this.dynUid + '" lay-skin="primary">'+ (index+1) +'</td>';
	           			   trs+='<td>'+this.dynTitle+'</td>'
	           			   trs+='<td>'+this.dynDescription+'</td>'
	           			   trs+='</tr>';
	           		   });
	           		   $("#field_permissions_table").append(trs);
                  }
                 });
            })
            
            $("#sure_btn").click(function(){
                $(".display_container3").css("display","none");
                $(".display_container5").css("display","none");
                $(".display_container6").css("display","none");
                $(".display_container10").css("display","none");
            })
            $("#cancel_btn").click(function(){
                $(".display_container3").css("display","none");
                $(".display_container4").css("display","none");
                $(".display_container5").css("display","none");
                $(".display_container6").css("display","none");
                $(".display_container10").css("display","none");
            })
            
            
            
             //表单字段权限  保存   只读 隐藏
             $("#filedSave").click(function(){
           		var $activeLi = $("#my_collapse li.link_active");
           		var actcUid = $activeLi.data('uid');
            	 
            	var jsonArr = new Array();
            	 var radioSelArr = $("#field_permissions_table").find("input[type='radio']:checked");
            	 //console.log(radioSelArr.length);
            	 radioSelArr.each(function(){
            		 var opObjUid = $(this).parent().parent().find("input[name='fldUid']").val();
            		 var stepUid = $(this).parent().parent().find("input[name='stepUid']").val();
            		 var jsonParam = {
                          stepUid:stepUid,//步骤ID
                          opObjUid:opObjUid,//表单字段ID
                          opObjType:"FIELD",
                          opAction:""//EDIT，HIDDEN，VIEW
                     };
            		var opAction = $(this).val();
                    jsonParam.opAction = opAction;
                    jsonArr.push(jsonParam);
            	 });
                 //给表单字段添加权限
                $.ajax({
		            url:common.getPath() +"/formField/saveFormFieldPermission",
		            method:"post",
		            dataType:"json",
		            contentType:"application/json",
		            data:JSON.stringify(jsonArr),
		            success:function(result){
		            	if(result.status == 0){
							$('#editFieldPermissions').hide();
							loadActivityConf(actcUid);
						}else{
							layer.alert(result.msg);
						}
		            }
                 });
             })
             
             //全选
            /*  $("#field_check").click(function(){   
 			    if(this.checked){   
 			        $("#field_permissions_table :checkbox").prop("checked", true);  
 			    }else{   
 					$("#field_permissions_table :checkbox").prop("checked", false);
 			    }   
 			}); */
        	
        	
        	//只读
            $("#viewAllclick").click(function(){   
            	if(this.checked){
            		$("#field_permissions_table").find("input[value='VIEW']").prop("checked",true);
            	}
			});
        	
           //隐藏
            $("#hiddenAllclick").click(function(){   
            	if(this.checked){
            		$("#field_permissions_table").find("input[value='HIDDEN']").prop("checked",true);
            	}
			});
           
        })
        
        
        
        function editAllclick(obj){
        	if(obj.checked){
        		$("#field_permissions_table").find("input[value='EDIT']").prop("checked",true);
        	}
        }
            
        function show1(){
            $(".form_table").css("display","inline-table");
            $(".trigger_table").css("display","none");
        }
        function show2(){
            $(".form_table").css("display","none");
            $(".trigger_table").css("display","inline-table");
        }
        
        
    </script>
</html>