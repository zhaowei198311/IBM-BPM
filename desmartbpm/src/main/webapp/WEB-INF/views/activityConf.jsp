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
        </style>
    </head>
    <body>
        <div class="container">
            <div class="search_area">
                <div class="search_area top_btn">
                    <a href="set.html"><button class="layui-btn layui-btn-primary layui-btn-sm">返回</button></a>
                    <span style="float:right;">
                        <button class="layui-btn layui-btn-primary layui-btn-sm">保存</button>
                    </span>
                </div>
            </div>
            <div style="margin-top:20px;">
                <form class="layui-form" action="">
                    <div class="layui-row">
                        <div class="layui-col-md6">                         
                            <div class="layui-form-item">
                                <label class="layui-form-label"></label>
                                <div class="layui-input-block">
                                    <input type="text" name="title" required  lay-verify="required" value="流程名称" autocomplete="off" class="layui-input" disabled="disabled">
                                </div>
                            </div>
                            <div class="layui-form-item">
                                <label class="layui-form-label">快照号</label>
                                <div class="layui-input-block">
                                    <input type="text" name="title" required  lay-verify="required" value="快照号" autocomplete="off" class="layui-input" disabled="disabled">
                                </div>
                            </div>                          
                        </div>
                        <div class="layui-col-md6">
                            <div class="layui-form-item">
                                <label class="layui-form-label">快照名称</label>
                                <div class="layui-input-block">
                                    <input type="text" name="title" required  lay-verify="required" value="快照名称1" autocomplete="off" class="layui-input" disabled="disabled">
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
                
                <div class="layui-tab">
                    <ul class="layui-tab-title">
                        <li class="layui-this">人工环节</li>
                        <li>网关环节列表</li>
                    </ul>
                    <div class="layui-tab-content">
                        <div class="layui-tab-item layui-show">
                            <div class="layui-row">
                                <div class="layui-col-md2">
                                    <div class="layui-collapse" lay-accordion id="my_collapse" lay-filter="demo">
                                        <!--  
                                        <div class="layui-colla-item">
                                            <h2 class="layui-colla-title">主流程环节</h2>
                                            <div class="layui-colla-content layui-show" id="content1">
                                                <ul class="link_list">
                                                    <li class="link_active">提交环节</li>
                                                    <li>审批1</li>
                                                    <li>审批2</li>
                                                </ul>
                                            </div>
                                        </div>
                                        <div class="layui-colla-item">
                                            <h2 class="layui-colla-title">子流程环节</h2>
                                            <div class="layui-colla-content" id="content2">
                                                <ul class="link_list">
                                                    <li class="link_active">提交环节</li>
                                                    <li>审批1</li>
                                                    <li>审批2</li>
                                                    <li>审批3</li>
                                                </ul>
                                            </div>
                                        </div>
                                        -->                                      
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
                                            <div class="layui-tab-item layui-show">
                                                <form class="layui-form" action="">
                                                    <div class="layui-row">
                                                        <div class="layui-col-md6">
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">禁止委托办理</label>
                                                                <div class="layui-input-block">
                                                                    <input type="radio" name="entrust" value="是" title="是" checked>
                                                                    <input type="radio" name="entrust" value="否" title="否" >
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">默认处理人</label>
                                                                <div class="layui-input-block">
                                                                    <select name="" lay-filter="filter" lay-verify="required">
                                                                        <option value="select1">角色+部门</option>
                                                                        <option value="select2">角色+公司编码</option>
                                                                        <option value="select3">上个环节提交人的上级</option>
                                                                        <option value="select4">指定人员</option>
                                                                        <option value="select5">流程发起人</option>
                                                                        <option value="select6">根据表字段选择</option>
                                                                    </select>
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item role_div">
                                                                <label class="layui-form-label">角色</label>
                                                                <div class="layui-input-block" style="position:relative;">
                                                                    <input type="text" name="title" required  lay-verify="required" value="" autocomplete="off" class="layui-input">
                                                                    <i class="layui-icon choose_role" title="选择角色">&#xe612;</i>  
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item user_div">
                                                                <label class="layui-form-label">人员</label>
                                                                <div class="layui-input-block" style="position:relative;">
                                                                    <input type="text" name="title" required  lay-verify="required" value="" autocomplete="off" class="layui-input">
                                                                    <i class="layui-icon choose_user" title="选择人员">&#xe612;</i>  
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item name_div">
                                                                <label class="layui-form-label">字段名称</label>
                                                                <div class="layui-input-block" style="position:relative;">
                                                                    <input type="text" name="title" required  lay-verify="required" value="" autocomplete="off" class="layui-input">
                                                                    <i class="layui-icon choose_name" title="选择字段">&#xe654;</i>  
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">是否可选</label>
                                                                <div class="layui-input-block">
                                                                    <input type="radio" name="range" value="不可选" title="不可选" checked>
                                                                    <input type="radio" name="range" value="可选" title="可选" >
                                                                    <!--<input type="radio" name="range" value="指定范围" title="指定范围" >
                                                                    <input type="radio" name="range" value="指定角色" title="指定角色" >-->
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">驳回方式</label>
                                                                <div class="layui-input-block">
                                                                    <select name="" lay-verify="required">
                                                                        <option value="">到指定环节</option>
                                                                        <option></option>
                                                                    </select>
                                                                </div>
                                                            </div>                                                          
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">驳回环节号</label>
                                                                <div class="layui-input-block" style="position:relative;">
                                                                    <input type="text" name="title" required  lay-verify="required" value="" autocomplete="off" class="layui-input" disabled="disabled">
                                                                    <i class="layui-icon choose_num" title="选择环节" >&#xe615;</i>
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">是否编辑表单</label>
                                                                <div class="layui-input-block">
                                                                    <input type="radio" name="document" value="可以编辑" title="可以编辑" checked>
                                                                    <input type="radio" name="document" value="不能编辑" title="不能编辑">
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">环节自动提交</label>
                                                                <div class="layui-input-block">
                                                                    <input type="radio" name="self-motion" value="允许" title="允许" checked>
                                                                    <input type="radio" name="self-motion" value="禁止" title="禁止">
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">是否允许取回</label>
                                                                <div class="layui-input-block">
                                                                    <input type="radio" name="get-back" value="允许" title="允许" checked>
                                                                    <input type="radio" name="get-back" value="不允许" title="不允许">
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">是否允许加签</label>
                                                                <div class="layui-input-block">
                                                                    <input type="radio" name="yes" value="允许" title="允许" checked>
                                                                    <input type="radio" name="yes" value="不允许" title="不允许">
                                                                </div>
                                                            </div>                                                          
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">是否可以审批</label>
                                                                <div class="layui-input-block">
                                                                    <input type="radio" name="approval" value="可以审批" title="可以审批" checked>
                                                                    <input type="radio" name="approval" value="不能审批" title="不能审批">
                                                                </div>
                                                            </div>                                                          
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">流程环节号</label>
                                                                <div class="layui-input-block">
                                                                    <input type="text" name="title" required  lay-verify="required" value="" autocomplete="off" class="layui-input">
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">分配变量名称</label>
                                                                <div class="layui-input-block">
                                                                    <select name="" lay-verify="required">
                                                                        <option value="">NextOwners_0</option>
                                                                        <option value="">NextOwners_1</option>
                                                                        <option value="">NextOwners_2</option>
                                                                        <option value="">NextOwners_3</option>
                                                                        <option value="">NextOwners_4</option>
                                                                    </select>
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">会签变量名称</label>
                                                                <div class="layui-input-block">
                                                                    <select name="" lay-verify="required">
                                                                        <option value="">signCount_0</option>
                                                                        <option value="">signCount_1</option>
                                                                        <option value="">signCount_2</option>
                                                                        <option value="">signCount_3</option>
                                                                        <option value="">signCount_4</option>
                                                                    </select>
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">处理时间</label>
                                                                <div class="layui-input-inline">
                                                                    <input name="date" id="test1" lay-verify="date" placeholder="yyyy-MM-dd" autocomplete="off" class="layui-input" type="text">
                                                                    <input name="date" id="test2" lay-verify="date" placeholder="yyyy-MM-dd" autocomplete="off" class="layui-input" type="text">
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class="layui-col-md6">
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">多对象处理规则</label>
                                                                <div class="layui-input-block">
                                                                    <input type="radio" name="more" value="顺序流转" title="顺序流转" checked>
                                                                    <input type="radio" name="more" value="并发审批（所有审批人必须都通过）" title="并发审批（所有审批人必须都通过）">
                                                                    <input type="radio" name="more" value="并发审批（只有一个审批人通过即可）" title="并发审批（只有一个审批人通过即可）">
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">是否允许转签</label>
                                                                <div class="layui-input-block">
                                                                    <input type="radio" name="endorse" value="允许" title="允许" checked>
                                                                    <input type="radio" name="endorse" value="不允许" title="不允许">
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">允许上传附件</label>
                                                                <div class="layui-input-block">
                                                                    <input type="radio" name="upload" value="允许" title="允许" checked>
                                                                    <input type="radio" name="upload" value="不允许" title="不允许">
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">能否编辑附件</label>
                                                                <div class="layui-input-block">
                                                                    <input type="radio" name="edit" value="可以编辑" title="可以编辑" checked>
                                                                    <input type="radio" name="edit" value="不能编辑" title="不能编辑">
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">允许删除附件</label>
                                                                <div class="layui-input-block">
                                                                    <input type="radio" name="delete" value="允许" title="允许" checked>
                                                                    <input type="radio" name="delete" value="不允许" title="不允许">
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">是否将当前处理人填写到域中</label>
                                                                <div class="layui-input-block">
                                                                    <input type="radio" name="approval_user" value="是" title="是" checked>
                                                                    <input type="radio" name="approval_user" value="否" title="否">
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">能否将意见填写到域中</label>
                                                                <div class="layui-input-block">
                                                                    <input type="radio" name="opinion" value="不添加" title="不添加" checked>
                                                                    <input type="radio" name="opinion" value="添加到域" title="添加到域">
                                                                    <input type="radio" name="opinion" value="处理人选择" title="处理人选择">
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">是否邮件通知本环节处理人</label>
                                                                <div class="layui-input-block">
                                                                    <input type="radio" name="mail" value="是" title="是" checked>
                                                                    <input type="radio" name="mail" value="否" title="否">
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">是否短信通知本环节处理人</label>
                                                                <div class="layui-input-block">
                                                                    <input type="radio" name="message" value="是" title="是" checked>
                                                                    <input type="radio" name="message" value="否" title="否">
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">是否部门会签（部门处理）</label>
                                                                <div class="layui-input-block">
                                                                    <input type="radio" name="countersign" value="是" title="是" checked>
                                                                    <input type="radio" name="countersign" value="否" title="否">
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
                                                                <label class="layui-form-label">运行时长</label>
                                                                <div class="layui-input-block">
                                                                    <input type="text" name="title" required  lay-verify="required" value="" autocomplete="off" class="layui-input" >
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">超时通知内容模板</label>
                                                                <div class="layui-input-block">
                                                                    <select name="" lay-verify="required">
                                                                        <option value="">（空）</option>
                                                                    </select>
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">超时通知触发事件类</label>
                                                                <div class="layui-input-block">
                                                                    <input type="text" name="title" required  lay-verify="required" value="" autocomplete="off" class="layui-input" >
                                                                </div>
                                                            </div>                                                          
                                                        </div>
                                                        <div class="layui-col-md6">
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">运行时间单位</label>
                                                                <div class="layui-input-block">
                                                                    <select name="" lay-verify="required">
                                                                        <option value=""></option>
                                                                        <option value="">分钟</option>
                                                                        <option value="">小时</option>
                                                                        <option value="">天</option>
                                                                    </select>                                           
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">超时通知人员</label>
                                                                <div class="layui-input-block">
                                                                    <div class="layui-input-inline">
                                                                        <input type="text" name="text" required lay-verify="required" value="" autocomplete="off" class="layui-input" disabled="disabled">
                                                                    </div>
                                                                    <div class="layui-form-mid layui-word-aux"><i class="layui-icon choose_user">&#xe612;</i> </div>
                                                                </div>
                                                            </div>                                                          
                                                        </div>
                                                    </div>
                                                </form>
                                            </div>
                                            <div class="layui-tab-item">
                                                新增步骤：<button class="layui-btn layui-btn-sm layui-btn-primary add_step">新增</button>
                                                <p class="title_p">第一步</p>
                                                <table class="layui-table backlog_table" lay-even lay-skin="nob">
                                                    <colgroup>
                                                        <col>
                                                        <col>
                                                        <col>
                                                        <col>
                                                    </colgroup>
                                                    <thead>
                                                        <tr>
                                                          <th>序号</th>
                                                          <th>表单名称</th>
                                                          <th>表单描述</th>
                                                          <th>操作</th>
                                                        </tr> 
                                                    </thead>
                                                    <tbody>
                                                        <tr>
                                                            <td>1</td>
                                                            <td>表单名称</td>
                                                            <td>描述内容...</td>
                                                            <td><i class="layui-icon delete_btn edit_role" title="权限设置">&#xe654;</i> <i class="layui-icon delete_btn">&#xe640;</i></td>
                                                        </tr>
                                                    </tbody>
                                                </table>                                                
                                                <p class="title_p">第二步</p>
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
                                                          <th>序号</th>
                                                          <th>触发器名称</th>
                                                          <th>类型</th>
                                                          <th>脚本名称</th>
                                                          <th>操作</th>
                                                        </tr> 
                                                    </thead>
                                                    <tbody>
                                                        <tr>
                                                            <td>1</td>
                                                            <td>名称</td>
                                                            <td>类型1</td>
                                                            <td>脚本名1...</td>
                                                            <td><i class="layui-icon delete_btn">&#xe640;</i></td>
                                                        </tr>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="layui-tab">
                                        <ul class="layui-tab-title">
                                            <li class="layui-this">环节属性</li>
                                            <li>环节SLA配置</li>
                                            <li>步骤配置</li>
                                        </ul>
                                        <div class="layui-tab-content">
                                            <div class="layui-tab-item layui-show">
                                                <form class="layui-form" action="">
                                                    <div class="layui-row">
                                                        <div class="layui-col-md6">
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">禁止委托办理</label>
                                                                <div class="layui-input-block">
                                                                    <input type="radio" name="entrust" value="是" title="是" checked>
                                                                    <input type="radio" name="entrust" value="否" title="否" >
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">默认处理人</label>
                                                                <div class="layui-input-block">
                                                                    <select name="" lay-verify="required">
                                                                        <option value="select1">由系统角色计算</option>
                                                                        <option value="select2">有文档中的域指定</option>
                                                                        <option value="select3">现在指定</option>
                                                                        <option value="select4">流程发起人</option>
                                                                    </select>
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">是否可选</label>
                                                                <div class="layui-input-block">
                                                                    <input type="radio" name="range" value="不可选" title="不可选" checked>
                                                                    <input type="radio" name="range" value="可选" title="可选" >
                                                                    <!--<input type="radio" name="range" value="指定范围" title="指定范围" >
                                                                    <input type="radio" name="range" value="指定角色" title="指定角色" >-->
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">驳回方式</label>
                                                                <div class="layui-input-block">
                                                                    <select name="" lay-verify="required">
                                                                        <option value="">到指定环节</option>
                                                                        <option></option>
                                                                    </select>
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">是否编辑表单</label>
                                                                <div class="layui-input-block">
                                                                    <input type="radio" name="document" value="可以编辑" title="可以编辑" checked>
                                                                    <input type="radio" name="document" value="不能编辑" title="不能编辑">
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">环节自动提交</label>
                                                                <div class="layui-input-block">
                                                                    <input type="radio" name="self-motion" value="允许" title="允许" checked>
                                                                    <input type="radio" name="self-motion" value="禁止" title="禁止">
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">是否允许取回</label>
                                                                <div class="layui-input-block">
                                                                    <input type="radio" name="get-back" value="允许" title="允许" checked>
                                                                    <input type="radio" name="get-back" value="不允许" title="不允许">
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">是否允许加签</label>
                                                                <div class="layui-input-block">
                                                                    <input type="radio" name="yes" value="允许" title="允许" checked>
                                                                    <input type="radio" name="yes" value="不允许" title="不允许">
                                                                </div>
                                                            </div>                                                          
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">是否可以审批</label>
                                                                <div class="layui-input-block">
                                                                    <input type="radio" name="approval" value="可以审批" title="可以审批" checked>
                                                                    <input type="radio" name="approval" value="不能审批" title="不能审批">
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">是否将当前处理人填写到域中</label>
                                                                <div class="layui-input-block">
                                                                    <input type="radio" name="approval_user" value="是" title="是" checked>
                                                                    <input type="radio" name="approval_user" value="否" title="否">
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">是否短信通知本环节处理人</label>
                                                                <div class="layui-input-block">
                                                                    <input type="radio" name="message" value="是" title="是" checked>
                                                                    <input type="radio" name="message" value="否" title="否">
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">处理时间</label>
                                                                <div class="layui-input-inline">
                                                                    <input name="date" id="test1" lay-verify="date" placeholder="yyyy-MM-dd" autocomplete="off" class="layui-input" type="text">
                                                                    <input name="date" id="test2" lay-verify="date" placeholder="yyyy-MM-dd" autocomplete="off" class="layui-input" type="text">
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class="layui-col-md6">                                                         
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">多对象处理规则</label>
                                                                <div class="layui-input-block">
                                                                    <input type="radio" name="more" value="顺序流转" title="顺序流转" checked>
                                                                    <input type="radio" name="more" value="并发审批（所有审批人必须都通过）" title="并发审批（所有审批人必须都通过）">
                                                                    <input type="radio" name="more" value="并发审批（只有一个审批人通过即可）" title="并发审批（只有一个审批人通过即可）">
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">驳回环节号</label>
                                                                <div class="layui-input-block">
                                                                    <input type="text" name="title" required  lay-verify="required" value="" autocomplete="off" class="layui-input" disabled="disabled">
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">是否允许转签</label>
                                                                <div class="layui-input-block">
                                                                    <input type="radio" name="endorse" value="允许" title="允许" checked>
                                                                    <input type="radio" name="endorse" value="不允许" title="不允许">
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">允许上传附件</label>
                                                                <div class="layui-input-block">
                                                                    <input type="radio" name="upload" value="允许" title="允许" checked>
                                                                    <input type="radio" name="upload" value="不允许" title="不允许">
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">能否编辑附件</label>
                                                                <div class="layui-input-block">
                                                                    <input type="radio" name="edit" value="可以编辑" title="可以编辑" checked>
                                                                    <input type="radio" name="edit" value="不能编辑" title="不能编辑">
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">允许删除附件</label>
                                                                <div class="layui-input-block">
                                                                    <input type="radio" name="delete" value="允许" title="允许" checked>
                                                                    <input type="radio" name="delete" value="不允许" title="不允许">
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">能否将意见填写到域中</label>
                                                                <div class="layui-input-block">
                                                                    <input type="radio" name="opinion" value="不添加" title="不添加" checked>
                                                                    <input type="radio" name="opinion" value="添加到域" title="添加到域">
                                                                    <input type="radio" name="opinion" value="处理人选择" title="处理人选择">
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">是否邮件通知本环节处理人</label>
                                                                <div class="layui-input-block">
                                                                    <input type="radio" name="mail" value="是" title="是" checked>
                                                                    <input type="radio" name="mail" value="否" title="否">
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">是否部门会签（部门处理）</label>
                                                                <div class="layui-input-block">
                                                                    <input type="radio" name="countersign" value="是" title="是" checked>
                                                                    <input type="radio" name="countersign" value="否" title="否">
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
                                                                <label class="layui-form-label">运行时长</label>
                                                                <div class="layui-input-block">
                                                                    <input type="text" name="title" required  lay-verify="required" value="" autocomplete="off" class="layui-input" >
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">超时通知内容模板</label>
                                                                <div class="layui-input-block">
                                                                    <select name="" lay-verify="required">
                                                                        <option value="">（空）</option>
                                                                    </select>
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">超时通知触发事件类</label>
                                                                <div class="layui-input-block">
                                                                    <input type="text" name="title" required  lay-verify="required" value="" autocomplete="off" class="layui-input" >
                                                                </div>
                                                            </div>                                                          
                                                        </div>
                                                        <div class="layui-col-md6">
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">运行时间单位</label>
                                                                <div class="layui-input-block">
                                                                    <select name="" lay-verify="required">
                                                                        <option value=""></option>
                                                                        <option value="">分钟</option>
                                                                        <option value="">小时</option>
                                                                        <option value="">天</option>
                                                                    </select>                                           
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">超时通知人员</label>
                                                                <div class="layui-input-block">
                                                                    <div class="layui-input-inline">
                                                                        <input type="text" name="text" required lay-verify="required" value="" autocomplete="off" class="layui-input" disabled="disabled">
                                                                    </div>
                                                                    <div class="layui-form-mid layui-word-aux"><i class="layui-icon choose_user">&#xe612;</i> </div>
                                                                </div>
                                                            </div>                                                          
                                                        </div>
                                                    </div>
                                                </form>
                                            </div>
                                            <div class="layui-tab-item">
                                                新增步骤：<button class="layui-btn layui-btn-sm layui-btn-primary add_step">新增</button>
                                                <p class="title_p">第一步</p>
                                                <table class="layui-table backlog_table" lay-even lay-skin="nob">
                                                    <colgroup>
                                                        <col>
                                                        <col>
                                                        <col>
                                                        <col>
                                                    </colgroup>
                                                    <thead>
                                                        <tr>
                                                          <th>序号</th>
                                                          <th>表单名称</th>
                                                          <th>表单描述</th>
                                                          <th>操作</th>
                                                        </tr> 
                                                    </thead>
                                                    <tbody>
                                                        <tr>
                                                            <td>1</td>
                                                            <td>表单名称</td>
                                                            <td>描述内容...</td>
                                                            <td><i class="layui-icon delete_btn edit_role" title="权限设置">&#xe654;</i> <i class="layui-icon delete_btn">&#xe640;</i></td>
                                                        </tr>
                                                    </tbody>
                                                </table>                                                
                                                <p class="title_p">第二步</p>
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
                                                          <th>序号</th>
                                                          <th>触发器名称</th>
                                                          <th>类型</th>
                                                          <th>脚本名称</th>
                                                          <th>操作</th>
                                                        </tr> 
                                                    </thead>
                                                    <tbody>
                                                        <tr>
                                                            <td>1</td>
                                                            <td>名称</td>
                                                            <td>类型1</td>
                                                            <td>脚本名1...</td>
                                                            <td><i class="layui-icon delete_btn">&#xe640;</i></td>
                                                        </tr>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="layui-tab">
                                        <ul class="layui-tab-title">
                                            <li class="layui-this">环节属性</li>
                                            <li>环节SLA配置</li>
                                            <li>步骤配置</li>
                                        </ul>
                                        <div class="layui-tab-content">
                                            <div class="layui-tab-item layui-show">
                                                <form class="layui-form" action="">
                                                    <div class="layui-row">
                                                        <div class="layui-col-md6">
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">禁止委托办理</label>
                                                                <div class="layui-input-block">
                                                                    <input type="radio" name="entrust" value="是" title="是" checked>
                                                                    <input type="radio" name="entrust" value="否" title="否" >
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">选择范围</label>
                                                                <div class="layui-input-block">
                                                                    <input type="radio" name="range" value="不可选" title="不可选" checked>
                                                                    <input type="radio" name="range" value="全体员工" title="全体员工" >
                                                                    <input type="radio" name="range" value="指定范围" title="指定范围" >
                                                                    <input type="radio" name="range" value="指定角色" title="指定角色" >
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">驳回方式</label>
                                                                <div class="layui-input-block">
                                                                    <select name="" lay-verify="required">
                                                                        <option value="">到指定环节</option>
                                                                        <option></option>
                                                                    </select>
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">是否编辑表单</label>
                                                                <div class="layui-input-block">
                                                                    <input type="radio" name="document" value="可以编辑" title="可以编辑" checked>
                                                                    <input type="radio" name="document" value="不能编辑" title="不能编辑">
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">环节自动提交</label>
                                                                <div class="layui-input-block">
                                                                    <input type="radio" name="self-motion" value="允许" title="允许" checked>
                                                                    <input type="radio" name="self-motion" value="禁止" title="禁止">
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">是否允许取回</label>
                                                                <div class="layui-input-block">
                                                                    <input type="radio" name="get-back" value="允许" title="允许" checked>
                                                                    <input type="radio" name="get-back" value="不允许" title="不允许">
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">是否允许加签</label>
                                                                <div class="layui-input-block">
                                                                    <input type="radio" name="yes" value="允许" title="允许" checked>
                                                                    <input type="radio" name="yes" value="不允许" title="不允许">
                                                                </div>
                                                            </div>                                                          
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">是否可以审批</label>
                                                                <div class="layui-input-block">
                                                                    <input type="radio" name="approval" value="可以审批" title="可以审批" checked>
                                                                    <input type="radio" name="approval" value="不能审批" title="不能审批">
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">是否将当前处理人填写到域中</label>
                                                                <div class="layui-input-block">
                                                                    <input type="radio" name="approval_user" value="是" title="是" checked>
                                                                    <input type="radio" name="approval_user" value="否" title="否">
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">是否短信通知本环节处理人</label>
                                                                <div class="layui-input-block">
                                                                    <input type="radio" name="message" value="是" title="是" checked>
                                                                    <input type="radio" name="message" value="否" title="否">
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">处理时间</label>
                                                                <div class="layui-input-inline">
                                                                    <input name="date" id="test1" lay-verify="date" placeholder="yyyy-MM-dd" autocomplete="off" class="layui-input" type="text">
                                                                    <input name="date" id="test2" lay-verify="date" placeholder="yyyy-MM-dd" autocomplete="off" class="layui-input" type="text">
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class="layui-col-md6">
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">默认处理人</label>
                                                                <div class="layui-input-block">
                                                                    <select name="" lay-verify="required">
                                                                        <option value="">由系统角色计算</option>
                                                                        <option value="">有文档中的域指定</option>
                                                                        <option value="">现在指定</option>
                                                                        <option value="">流程发起人</option>
                                                                        <option></option>
                                                                    </select>
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">多对象处理规则</label>
                                                                <div class="layui-input-block">
                                                                    <input type="radio" name="more" value="顺序流转" title="顺序流转" checked>
                                                                    <input type="radio" name="more" value="并发审批（所有审批人必须都通过）" title="并发审批（所有审批人必须都通过）">
                                                                    <input type="radio" name="more" value="并发审批（只有一个审批人通过即可）" title="并发审批（只有一个审批人通过即可）">
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">驳回环节号</label>
                                                                <div class="layui-input-block">
                                                                    <input type="text" name="title" required  lay-verify="required" value="" autocomplete="off" class="layui-input" disabled="disabled">
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">是否允许转签</label>
                                                                <div class="layui-input-block">
                                                                    <input type="radio" name="endorse" value="允许" title="允许" checked>
                                                                    <input type="radio" name="endorse" value="不允许" title="不允许">
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">允许上传附件</label>
                                                                <div class="layui-input-block">
                                                                    <input type="radio" name="upload" value="允许" title="允许" checked>
                                                                    <input type="radio" name="upload" value="不允许" title="不允许">
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">能否编辑附件</label>
                                                                <div class="layui-input-block">
                                                                    <input type="radio" name="edit" value="可以编辑" title="可以编辑" checked>
                                                                    <input type="radio" name="edit" value="不能编辑" title="不能编辑">
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">允许删除附件</label>
                                                                <div class="layui-input-block">
                                                                    <input type="radio" name="delete" value="允许" title="允许" checked>
                                                                    <input type="radio" name="delete" value="不允许" title="不允许">
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">能否将意见填写到域中</label>
                                                                <div class="layui-input-block">
                                                                    <input type="radio" name="opinion" value="不添加" title="不添加" checked>
                                                                    <input type="radio" name="opinion" value="添加到域" title="添加到域">
                                                                    <input type="radio" name="opinion" value="处理人选择" title="处理人选择">
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">是否邮件通知本环节处理人</label>
                                                                <div class="layui-input-block">
                                                                    <input type="radio" name="mail" value="是" title="是" checked>
                                                                    <input type="radio" name="mail" value="否" title="否">
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">是否部门会签（部门处理）</label>
                                                                <div class="layui-input-block">
                                                                    <input type="radio" name="countersign" value="是" title="是" checked>
                                                                    <input type="radio" name="countersign" value="否" title="否">
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
                                                                <label class="layui-form-label">运行时长</label>
                                                                <div class="layui-input-block">
                                                                    <input type="text" name="title" required  lay-verify="required" value="" autocomplete="off" class="layui-input" >
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">超时通知内容模板</label>
                                                                <div class="layui-input-block">
                                                                    <select name="" lay-verify="required">
                                                                        <option value="">（空）</option>
                                                                    </select>
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">超时通知触发事件类</label>
                                                                <div class="layui-input-block">
                                                                    <input type="text" name="title" required  lay-verify="required" value="" autocomplete="off" class="layui-input" >
                                                                </div>
                                                            </div>                                                          
                                                        </div>
                                                        <div class="layui-col-md6">
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">运行时间单位</label>
                                                                <div class="layui-input-block">
                                                                    <select name="" lay-verify="required">
                                                                        <option value=""></option>
                                                                        <option value="">分钟</option>
                                                                        <option value="">小时</option>
                                                                        <option value="">天</option>
                                                                    </select>                                           
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">超时通知人员</label>
                                                                <div class="layui-input-block">
                                                                    <div class="layui-input-inline">
                                                                        <input type="text" name="text" required lay-verify="required" value="" autocomplete="off" class="layui-input" disabled="disabled">
                                                                    </div>
                                                                    <div class="layui-form-mid layui-word-aux"><i class="layui-icon choose_user">&#xe612;</i> </div>
                                                                </div>
                                                            </div>                                                          
                                                        </div>
                                                    </div>
                                                </form>
                                            </div>
                                            <div class="layui-tab-item">
                                                新增步骤：<button class="layui-btn layui-btn-sm layui-btn-primary add_step">新增</button>
                                                <p class="title_p">第一步</p>
                                                <table class="layui-table backlog_table" lay-even lay-skin="nob">
                                                    <colgroup>
                                                        <col>
                                                        <col>
                                                        <col>
                                                        <col>
                                                    </colgroup>
                                                    <thead>
                                                        <tr>
                                                          <th>序号</th>
                                                          <th>表单名称</th>
                                                          <th>表单描述</th>
                                                          <th>操作</th>
                                                        </tr> 
                                                    </thead>
                                                    <tbody>
                                                        <tr>
                                                            <td>1</td>
                                                            <td>表单名称</td>
                                                            <td>描述内容...</td>
                                                            <td><i class="layui-icon delete_btn edit_role" title="权限设置">&#xe654;</i> <i class="layui-icon delete_btn">&#xe640;</i></td>
                                                        </tr>
                                                    </tbody>
                                                </table>                                                
                                                <p class="title_p">第二步</p>
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
                                                          <th>序号</th>
                                                          <th>触发器名称</th>
                                                          <th>类型</th>
                                                          <th>脚本名称</th>
                                                          <th>操作</th>
                                                        </tr> 
                                                    </thead>
                                                    <tbody>
                                                        <tr>
                                                            <td>1</td>
                                                            <td>名称</td>
                                                            <td>类型1</td>
                                                            <td>脚本名1...</td>
                                                            <td><i class="layui-icon delete_btn">&#xe640;</i></td>
                                                        </tr>
                                                    </tbody>
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
        <div class="display_container3">
            <div class="display_content3">
                <div class="top">
                    新增步骤
                </div>
                <div class="middle1" >
                    <div class="radio_div">选择类型：
                        <input type="radio" name="type" value="type1" title="表单"  checked onclick="show1()" id="radio1"> <label for="radio1">表单</label>
                        <input type="radio" name="type" value="type2" title="触发器" onclick="show2()" id="radio2"> <label for="radio2">触发器</label>
                    </div>
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
                        <tbody>
                            <tr>
                                <td><input type="checkbox" name=""  lay-skin="primary"> 1</td>
                                <td>表单名称</td>
                                <td>描述内容...</td>
                            </tr>
                            <tr>
                                <td><input type="checkbox" name=""  lay-skin="primary"> 2</td>
                                <td>表单名称</td>
                                <td>描述内容...</td>
                            </tr>
                            <tr>
                                <td><input type="checkbox" name=""  lay-skin="primary"> 3</td>
                                <td>表单名称</td>
                                <td>描述内容...</td>
                            </tr>
                            <tr>
                                <td><input type="checkbox" name=""  lay-skin="primary"> 4</td>
                                <td>表单名称</td>
                                <td>描述内容...</td>
                            </tr>
                            <tr>
                                <td><input type="checkbox" name=""  lay-skin="primary"> 5</td>
                                <td>表单名称</td>
                                <td>描述内容...</td>
                            </tr>
                        </tbody>
                    </table>
                    <table class="layui-table backlog_table trigger_table" lay-even lay-skin="nob">
                        <colgroup>
                            <col>
                            <col>
                            <col>
                            <col>
                        </colgroup>
                        <thead>
                            <tr>
                              <th>序号</th>
                              <th>触发器名称</th>
                              <th>类型</th>
                              <th>脚本名称</th>
                            </tr> 
                        </thead>
                        <tbody>
                            <tr>
                                <td><input type="checkbox" name=""  lay-skin="primary"> 1</td>
                                <td>名称</td>
                                <td>类型1</td>
                                <td>脚本名1...</td>
                            </tr>
                            <tr>
                                <td><input type="checkbox" name=""  lay-skin="primary"> 2</td>
                                <td>名称</td>
                                <td>类型1</td>
                                <td>脚本名1...</td>
                            </tr>
                            <tr>
                                <td><input type="checkbox" name=""  lay-skin="primary"> 3</td>
                                <td>名称</td>
                                <td>类型1</td>
                                <td>脚本名1...</td>
                            </tr>
                            <tr>
                                <td><input type="checkbox" name=""  lay-skin="primary"> 4</td>
                                <td>名称</td>
                                <td>类型1</td>
                                <td>脚本名1...</td>
                            </tr>
                            <tr>
                                <td><input type="checkbox" name=""  lay-skin="primary"> 5</td>
                                <td>名称</td>
                                <td>类型1</td>
                                <td>脚本名1...</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                <div id="demo8"></div>
                <div class="foot">
                    <button class="layui-btn layui-btn sure_btn">确定</button>
                    <button class="layui-btn layui-btn layui-btn-primary cancel_btn">取消</button>
                </div>
            </div>
        </div>
        <div class="display_container4">
            <div class="display_content3">
                <div class="top">
                    编辑字段权限
                </div>
                <div class="middle1">
                    <table class="layui-table" lay-even lay-skin="nob" >
                        <colgroup>
                            <col width="100">
                            <col>
                            <col>
                        </colgroup>
                        <thead>
                            <th>序号</th>
                            <th>字段名称</th>
                            <th>状态</th>
                        </thead>
                        <tbody>
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
                </div>
                <div class="foot">
                    <button class="layui-btn layui-btn ">编辑</button>
                    <button class="layui-btn layui-btn ">只读</button>
                    <button class="layui-btn layui-btn ">隐藏</button>
                    <button class="layui-btn layui-btn layui-btn-primary cancel_btn">取消</button>
                </div>
            </div>
        </div>
        <div class="display_container10">
            <div class="display_content3">
                <div class="top">
                    编辑字段权限
                </div>
                <div class="middle1">
                    <table class="layui-table" lay-even lay-skin="nob" >
                        <colgroup>
                            <col>
                            <col>
                            <col>
                            <col>
                        </colgroup>
                        <tbody>
                            <tr>
                                <td><input type="checkbox" id="0"/><label for="0"> 字段1</label></td>
                                <td><input type="checkbox" id="1"/><label for="1"> 字段2</label></td>
                                <td><input type="checkbox" id="2"/><label for="2"> 字段3</label></td>
                                <td><input type="checkbox" id="3"/><label for="3"> 字段4</label></td>
                            </tr>
                            <tr>
                                <td><input type="checkbox" id="0"/><label for="4"> 字段5</label></td>
                                <td><input type="checkbox" id="1"/><label for="5"> 字段6</label></td>
                                <td><input type="checkbox" id="2"/><label for="6"> 字段7</label></td>
                                <td><input type="checkbox" id="3"/><label for="7"> 字段8</label></td>
                            </tr>
                            <tr>
                                <td><input type="checkbox" id="0"/><label for="8"> 字段9</label></td>
                                <td><input type="checkbox" id="1"/><label for="9"> 字段10</label></td>
                                <td><input type="checkbox" id="2"/><label for="10"> 字段11</label></td>
                                <td><input type="checkbox" id="3"/><label for="11"> 字段12</label></td>
                            </tr>
                        </tbody>
                    </table>                
                </div>
                <div class="foot">
                    <button class="layui-btn layui-btn sure_btn">确定</button>
                    <button class="layui-btn layui-btn layui-btn-primary cancel_btn">取消</button>
                </div>
            </div>
        </div>
        <div class="display_container5">
            <div class="display_content5">
                <div class="top">
                    新增网关
                </div>
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
        <div class="display_container6">
            <div class="display_content6">
                <div class="top">
                    选择退回环节
                </div>
                <div class="middle6">
                    <div class="left_div">
                        <ul>
                            <li>会签</li>
                            <li>审批01</li>
                            <li>审批02</li>
                            <li>审批03</li>
                            <li>审批04</li>
                            <li>审批05</li>
                        </ul>
                    </div>
                    <div class="middle_div">
                        <button class="layui-btn layui-btn-sm" style="margin-top:100px;"><</button>
                            <br><br>
                        <button class="layui-btn layui-btn-sm">></button>
                    </div>
                    <div class="right_div">
                        <ul>
                            <li>会签</li>
                            <li>审批01</li>
                            <li>审批02</li>
                            <li>审批03</li>
                            <li>审批04</li>
                            <li>审批05</li>
                        </ul>
                    </div>
                    <h1 style="clear:both;"></h1>
                </div>
                <div class="foot">
                    <button class="layui-btn layui-btn sure_btn">确定</button>
                    <button class="layui-btn layui-btn layui-btn-primary cancel_btn">取消</button>
                </div>
            </div>
        </div>
    </body>
    
    <script type="text/javascript" src="<%=basePath%>/resources/js/layui.all.js"></script>
    <script type="text/javascript" src="<%=basePath%>/resources/js/my/activityConf.js"></script>
    <script>
        var proAppId = '${processDefinition.proAppId}';
        var proUid = '${processDefinition.proUid}';
        var proVerUid = '${processDefinition.proVerUid}';
        var firstHumanMeta = '${firstHumanMeta}';
    
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
            $(".create_net").click(function(){
                $(".display_container5").css("display","block");
            })
            $(".add_step").click(function(){
                $(".display_container3").css("display","block");
            })
            $(".edit_role").click(function(){
                $(".display_container4").css("display","block");
            })
            $(".choose_num").click(function(){
                $(".display_container6").css("display","block");
            })
            $(".choose_name").click(function(){
                $(".display_container10").css("display","block");
            })
            $(".sure_btn").click(function(){
                $(".display_container3").css("display","none");
                $(".display_container5").css("display","none");
                $(".display_container6").css("display","none");
                $(".display_container10").css("display","none");
            })
            $(".cancel_btn").click(function(){
                $(".display_container3").css("display","none");
                $(".display_container4").css("display","none");
                $(".display_container5").css("display","none");
                $(".display_container6").css("display","none");
                $(".display_container10").css("display","none");
            })
        })
        layui.use('form', function(){
          var form = layui.form;
          form.on('select(filter)', function(data){         
                if(data.value=="select1"){ 
                    $(".role_div").css("display","block");
                    $(".user_div").css("display","none");
                    $(".name_div").css("display","none");
                }else  if(data.value=="select2"){
                    $(".role_div").css("display","block");
                    $(".user_div").css("display","none");
                    $(".name_div").css("display","none");
                }else if(data.value=="select3"){
                    $(".role_div").css("display","none");
                    $(".user_div").css("display","none");
                    $(".name_div").css("display","none");
                }else if(data.value=="select4"){
                    $(".role_div").css("display","none");
                    $(".user_div").css("display","block");
                    $(".name_div").css("display","none");
                }else if(data.value=="select5"){
                    $(".role_div").css("display","none");
                    $(".user_div").css("display","none");
                    $(".name_div").css("display","none");
                }else if(data.value=="select6"){
                    $(".role_div").css("display","none");
                    $(".user_div").css("display","none");
                    $(".name_div").css("display","block");
                }           
            });     
        });
            
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