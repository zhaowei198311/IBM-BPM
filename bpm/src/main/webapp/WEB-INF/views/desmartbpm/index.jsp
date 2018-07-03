<%@ page language="java"  pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>来伊份BPM系统</title>
  <%@include file="common/head.jsp" %>
  <%@include file="common/tag.jsp" %>
  <%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>  
  <link rel="stylesheet" href="<%=basePath%>/resources/desmartbpm/css/admin.css" media="all">
  <link rel="stylesheet" href="<%=basePath%>/resources/desmartbpm/css/my.css" media="all">
  
</head>
<body class="layui-layout-body">
  
  <div id="LAY_app">
    <div class="layui-layout layui-layout-admin">
      <div class="layui-header">
        <div class="layui-nav layui-layout-left index_top">                 
            <span class="system_title">来伊份BPM系统</span>
            <!--<span class="logout"><i class="layui-icon">&#xe60e;</i> 退出</span>-->
        </div>
      </div>
      
      <!-- 侧边菜单 -->
      <div class="layui-side layui-side-menu">
        <div class="layui-side-scroll">
          <div class="layui-side layui-bg-black">
                <div class="layui-side-scroll">        
                <!-- 左侧导航区域（可配合layui已有的垂直导航） -->
                    <ul class="layui-nav layui-nav-tree layui-nav-side">
                        <li class="logo"><img src="<%=basePath%>/resources/desmartbpm/images/logo.gif"/></li>
                        <li class="layui-nav-item">
                            <a href="javascript:;"><i class="layui-icon">&#xe609;</i> 流程管理</a>
                            <dl class="layui-nav-child">
                              <shiro:hasPermission name="processCategory:index">
                                 <dd><a href="<%=basePath%>/processCategory/index" target="iframe0">流程分类</a></dd>
                              </shiro:hasPermission> 
                              <shiro:hasPermission name="processDefinition:index">
                                 <dd><a href="<%=basePath%>/processDefinition/index" target="iframe0">流程定义</a></dd>
                              </shiro:hasPermission> 
                            </dl>
                        </li>
                        <li class="layui-nav-item">
                        	<a href="javascript:;"><i class="layui-icon">&#xe62d;</i> 表单管理</a>
                            <dl class="layui-nav-child">
                            <shiro:hasPermission name="formManage:index">
                              <dd><a href="<%=basePath%>/formManage/index" target="iframe0">流程表单管理</a></dd>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="publicForm:index">
                              <dd><a href="<%=basePath%>/publicForm/index" target="iframe0">子表单管理</a></dd>
                            </shiro:hasPermission> 
                            </dl>
                        </li>
                        <li class="layui-nav-item">
                        	<a href="javascript:;"><i class="layui-icon">&#xe629;</i> 报表管理</a>
                            <dl class="layui-nav-child">
                            <shiro:hasPermission name="dhProcessRetrieve:processRetrieve">
                              <dd><a href="<%=basePath%>/dhProcessRetrieve/processRetrieve" target="iframe0">检索条件配置</a></dd>
                            </shiro:hasPermission>
                            </dl>
                        </li>
                        <shiro:hasPermission name="interfaces:index">
                          <li class="layui-nav-item"><a href="interfaces/index" target="iframe0"><i class="layui-icon">&#xe614;</i> 接口管理</a></li>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="trigger:index">
                          <li class="layui-nav-item"><a href="<%=basePath%>/trigger/index" target="iframe0"><i class="layui-icon">&#xe6b2;</i> 触发器管理</a></li>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="quarz:quarzList">
                          <li class="layui-nav-item"><a href="quarz/quarzList" target="iframe0"><i class="layui-icon">&#xe857;</i>定时任务管理</a></li>
                        </shiro:hasPermission>
                        <li class="layui-nav-item"><a href="<%=basePath%>/dhProcessInsManage/toProcessInsManage" target="iframe0"><i class="layui-icon">&#xe629;</i> 流程实例管理</a></li>
                        <li class="layui-nav-item">
						     <a href="javascript:;"><i class="layui-icon">&#xe614;</i> 系统管理</a>
						    <dl class="layui-nav-child">
						    <shiro:hasPermission name="sysUser:organization">
						      <dd><a href="sysUser/organization" target="iframe0">组织管理</a></dd>
						    </shiro:hasPermission> 
						    <shiro:hasPermission name="sysUser:user">
						      <dd><a href="sysUser/user"  target="iframe0">用户管理</a></dd>
						    </shiro:hasPermission>
						    <shiro:hasPermission name="sysRole:role">
						      <dd><a href="sysRole/role" target="iframe0">业务角色管理</a></dd>
						    </shiro:hasPermission>
						    <shiro:hasPermission name="sysResource:resource_list">  
						      <dd><a href="sysResource/resource_list" target="iframe0">模块资源管理</a></dd>
						    </shiro:hasPermission>
						    <shiro:hasPermission name="sysRole:system_role">  
						      <dd><a href="sysRole/system_role" target="iframe0">系统角色管理</a></dd>
						    </shiro:hasPermission>
						    <shiro:hasPermission name="sysTeam:group"> 
						      <dd><a href="sysTeam/group" target="iframe0">角色组管理</a></dd>
						    </shiro:hasPermission>
						    <shiro:hasPermission name="sysDictionary:dictionary">
						      <dd><a href="sysDictionary/dictionary" target="iframe0">字典类型</a></dd>
						    </shiro:hasPermission>
						    <shiro:hasPermission name="sysDictionary:dictCdList">
						      <dd><a href="sysDictionary/dictCdList" target="iframe0">数据字典</a></dd>
						    </shiro:hasPermission>   
						    <shiro:hasPermission name="sysUser:globalConfig">
						      <dd><a href="sysUser/globalConfig" target="iframe0">全局配置管理</a></dd>
						    </shiro:hasPermission>
						    </dl>
						</li>
                    </ul>
                </div>
            </div>
        </div>
      </div>
      <div class="layui-body" id="LAY_app_body" style="margin-bottom:40px;">
        <div class="layadmin-tabsbody-item layui-show">
          <iframe src="<%=basePath%>/processCategory/index" name="iframe0" frameborder="0" class="layadmin-iframe"></iframe>
        </div>
      </div>
        <div class="layui-footer">
            <div class="footer">
                Copyright &copy; 来伊份 2018
            </div>
        </div>
      <!-- 辅助元素，一般用于移动设备下遮罩 -->
      <div class="layadmin-body-shade" layadmin-event="shade"></div>
    </div>
  </div>
  <script src="<%=basePath%>/resources/desmartbpm/js/layui.all.js"></script>
</body>
</html>


