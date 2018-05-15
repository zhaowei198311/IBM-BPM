<!DOCTYPE html>
<%@ page language="java"  pageEncoding="UTF-8"%>
<html>
    <head>
        <%@include file="common/head.jsp" %>
        <%@include file="common/tag.jsp" %>
        <link rel="stylesheet" href="<%=basePath%>/resources/desmartbpm/css/my.css" media="all">
    </head>
    <body>
        <div class="layui-container" style="margin-top:20px;width:100%;">  
            <div class="layui-row">
                <div class="layui-col-md2" style="text-align: left;">
                    <div class="tree" id="demo">
                            
                    </div>
                </div>
                <div class="layui-col-md10">
                    <div class="search_area">
                        <div class="layui-row layui-form">
                            <div class="layui-col-md2">
                                <input type="text" placeholder="组织名称"  class="layui-input">
                            </div>
                            <div class="layui-col-md1" style="text-align:right;">
                                    <button class="layui-btn" >查询</button>
                            </div>
                        </div>
                    </div>
                    <div>               
                        <table class="layui-table backlog_table" lay-even lay-skin="nob">
                            <colgroup>
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
                                  <th>组织名称</th>
                                  <th>操作</th>
                                </tr> 
                            </thead>
                            <tbody>
                                <tr>
                                  <td>1</td>
                                  <td>营运部</td>
                                  <td> <i class="layui-icon delete_btn">&#xe640;</i></td>
                                </tr>
                                <tr>
                                  <td>2</td>
                                   <td>营运部</td>
                                  <td> <i class="layui-icon delete_btn">&#xe640;</i></td>
                                </tr>
                                <tr>
                                  <td>3</td>
                                  <td>营运部</td>
                                  <td> <i class="layui-icon delete_btn">&#xe640;</i></td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>  
    </body>
    
</html>
    <script type="text/javascript" src="js/jquery-3.3.1.js" ></script>
    <script type="text/javascript" src="js/layui.all.js"></script>
    <script>
        layui.tree({
          elem: '#demo' //传入元素选择器
          ,nodes: [{ //节点
            name: '来伊份'
            ,children: [{
              name: '部门1'
              ,children:[{
                name: '管理员'
              },{
                name:'test1'
              },{
                name:'test2'
              }]
            },{
              name: '部门2'
              ,children:[{
                name: '管理员'
              },{
                name:'test1'
              },{
                name:'test2'
              }]
            }]
          }]
        });
        
  </script>