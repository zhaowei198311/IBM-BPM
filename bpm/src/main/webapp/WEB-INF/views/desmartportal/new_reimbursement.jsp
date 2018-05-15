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
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
  		<title>待办任务</title>
  		<link href="resources/desmartportal/css/layui.css" rel="stylesheet"/>
  		<link href="resources/desmartportal/css/my.css" rel="stylesheet" />
  		
	</head>
	<body>
		<div class="search_area top_btn">
			
			<span style="padding-left:10px;color:#777;font-size:18px;">（人工任务2）新开店流程</span>
			<span style="float:right;padding-right:20px;">
				<button class="layui-btn layui-btn-sm">作废</button><button class="layui-btn  layui-btn-sm">保存</button><button class="layui-btn layui-btn-sm">提交</button><button class="layui-btn layui-btn-sm">流程图</button><a href="draft.html" style="margin-left:10px;"><button class="layui-btn layui-btn-sm" >退出</button></a>
			</span>
		</div>
		<div class="container">			
			<div class="content">
				<table class="layui-table">
					<colgroup>
					    <col width="150">
					    <col>
					    <col width="150">
					    <col> 
					</colgroup>
					<tbody>
						<tr>
							<th colspan="4" class="list_title">目标店调查表
							<span style="float: right;font-size:14px;font-weight:normal;">流程编号：1000-10185-BG-60</span>
							</th>
						</tr>
					    <tr>
					      <td>工号</td>
					      <td><input type="text" name="title" required  lay-verify="required" value="00003" autocomplete="off" class="layui-input"></td>
					      <td>姓名</td>
					      <td><input type="text" name="title" required  lay-verify="required" value="00003" autocomplete="off" class="layui-input"></td>
					    </tr>
					    <tr>
					      <td>创建日期</td>
					      <td><input type="text" name="title" required  lay-verify="required" value="2018-03-12" autocomplete="off" class="layui-input"></td>
					      <td>金额</td>
					      <td><input type="text" name="title" required  lay-verify="required" value="11453" autocomplete="off" class="layui-input"></td>
					    </tr>
					</tbody>
				</table>
				<p class="title_p">门店基础信息</p>
				<table class="layui-table">
					<colgroup>
					    <col width="150">
					    <col>
					    <col width="150">
					    <col> 
					    <col width="150">
					    <col> 
					</colgroup>
					<tbody>
					    <tr>
					      <td class="td_title">省<span class="tip_span">*</span></td>
					      <td><input type="text" name="title" required  lay-verify="required" value="" autocomplete="off" class="layui-input"></td>
					      <td class="td_title">区/县</td>
					      <td><input type="text" name="title" required  lay-verify="required" value="" autocomplete="off" class="layui-input"></td>
					      <td class="td_title">街道</td>
					      <td><input type="text" name="title" required  lay-verify="required" value="" autocomplete="off" class="layui-input"></td>
					    </tr>
					    <tr>
					      <td class="td_title">路<span class="tip_span">*</span></td>
					      <td><input type="text" name="title" required  lay-verify="required" value="" autocomplete="off" class="layui-input"></td>
					      <td class="td_title">号<span class="tip_span">*</span></td>
					      <td><input type="text" name="title" required  lay-verify="required" value="" autocomplete="off" class="layui-input"></td>
					      <td class="td_title">近<span class="tip_span">*</span></td>
					      <td><input type="text" name="title" required  lay-verify="required" value="" autocomplete="off" class="layui-input"></td>
					    </tr>
					    <tr>
					      <td class="td_title">公司代码</td>
					      <td><input type="text" name="title" required  lay-verify="required" value="" autocomplete="off" class="layui-input"></td>
					      <td class="td_title">公司名称</td>
					      <td colspan="3"><input type="text" name="title" required  lay-verify="required" value="" autocomplete="off" class="layui-input"></td>
					    </tr>
					    <tr>
					      <td class="td_title">销售组织<span class="tip_span">*</span></td>
					      <td><input type="text" name="title" required  lay-verify="required" value="" autocomplete="off" class="layui-input"></td>
					      <td class="td_title">销售组织名称</td>
					      <td colspan="3"><input type="text" name="title" required  lay-verify="required" value="" autocomplete="off" class="layui-input"></td>
					    </tr>
					    <tr>
					      <td class="td_title">产权地址<span class="tip_span">*</span></td>
					      <td><input type="text" name="title" required  lay-verify="required" value="" autocomplete="off" class="layui-input"></td>
					      <td class="td_title">办证资料<span class="tip_span">*</span></td>
					      <td><input type="text" name="title" required  lay-verify="required" value="" autocomplete="off" class="layui-input"></td>
					      <td class="td_title">产权方名称/出租房名称<span class="tip_span">*</span></td>
					      <td><input type="text" name="title" required  lay-verify="required" value="" autocomplete="off" class="layui-input"></td>
					    </tr>
					    <tr>
					      <td class="td_title">建筑面积(平方米)<span class="tip_span">*</span></td>
					      <td><input type="text" name="title" required  lay-verify="required" value="" autocomplete="off" class="layui-input"></td>
					      <td class="td_title">使用面积(平方米)<span class="tip_span">*</span></td>
					      <td colspan="3"><input type="text" name="title" required  lay-verify="required" value="" autocomplete="off" class="layui-input"></td>
					    </tr>
					    <tr>
					      <td class="td_title">门宽(米)<span class="tip_span">*</span></td>
					      <td><input type="text" name="title" required  lay-verify="required" value="" autocomplete="off" class="layui-input"></td>
					      <td class="td_title">内径门高(米)<span class="tip_span">*</span></td>
					      <td><input type="text" name="title" required  lay-verify="required" value="" autocomplete="off" class="layui-input"></td>
					      <td class="td_title">高(米)<span class="tip_span">*</span></td>
					      <td><input type="text" name="title" required  lay-verify="required" value="" autocomplete="off" class="layui-input"></td>
					    </tr>					   
					</tbody>
				</table>
				<p class="title_p">房产性质</p>
				<table class="layui-table">
					<colgroup>
					    <col width="150">
					    <col>
					    <col width="150">
					    <col> 
					    <col width="150">
					    <col> 
					</colgroup>
					<tbody>
					    <tr>
					      <td class="td_title">房产性质<span class="tip_span">*</span></td>
					      <td colspan="3"><input type="text" name="title" required  lay-verify="required" value="" autocomplete="off" class="layui-input"></td>
					      <td class="td_title">房东<span class="tip_span">*</span></td>
					      <td><input type="text" name="title" required  lay-verify="required" value="" autocomplete="off" class="layui-input"></td>
					    </tr>
					    <tr>
					      <td class="td_title">合作模式<span class="tip_span">*</span></td>
					      <td><input type="text" name="title" required  lay-verify="required" value="" autocomplete="off" class="layui-input"></td>
					      <td class="td_title">扣率(%)</td>
					      <td><input type="text" name="title" required  lay-verify="required" value="" autocomplete="off" class="layui-input"></td>
					      <td class="td_title">保底</td>
					      <td><input type="text" name="title" required  lay-verify="required" value="" autocomplete="off" class="layui-input"></td>
					    </tr>
					    <tr>
					      <td class="td_title">租金(元/月(含税))<span class="tip_span">*</span></td>
					      <td><input type="text" name="title" required  lay-verify="required" value="" autocomplete="off" class="layui-input"></td>
					      <td class="td_title">房租期间(月)<span class="tip_span">*</span></td>
					      <td colspan="3"><input type="text" name="title" required  lay-verify="required" value="" autocomplete="off" class="layui-input"></td>
					    </tr>
					    <tr>
					      <td class="td_title">付款方式<span class="tip_span">*</span></td>
					      <td><input type="text" name="title" required  lay-verify="required" value="" autocomplete="off" class="layui-input"></td>
					      <td class="td_title">押金支付金额<span class="tip_span">*</span></td>
					      <td><input type="text" name="title" required  lay-verify="required" value="" autocomplete="off" class="layui-input"></td>
					      <td class="td_title">免租期(天)<span class="tip_span">*</span></td>
					      <td><input type="text" name="title" required  lay-verify="required" value="" autocomplete="off" class="layui-input"></td>
					    </tr>					    			   
					</tbody>
				</table>
				<p class="title_p">本环节审批要求</p>
				<div class="layui-form">
					<textarea  class="layui-textarea">
						
					</textarea>
				</div>
				<p class="title_p">审批意见</p>
				<div class="layui-form">
					<label class="layui-form-label">审批意见</label>
			      	<div class="layui-input-block">
			        	<textarea placeholder="意见留言" class="layui-textarea" id="demo" style="display: none;margin-bottom:10px;"></textarea>
	      			</div>						
			      	<label class="layui-form-label">常用语</label>
			      	<div class="layui-input-block">
			        	<select>
							<option>--请选择--</option>
							<option>通过</option>
							<option>驳回</option>
						</select>
	      			</div>				
				</div>
				<div class="layui-tab">
				  	<ul class="layui-tab-title">
					    <li class="layui-this">附件</li>
					    <li>处理流程</li>
					    <li>审批意见</li>
				  	</ul>
				  	<div class="layui-tab-content" style="padding:0;">
					    <div class="layui-tab-item layui-show">
					    	<table class="layui-table" style="margin:0;">
								<colgroup>
								    <col width="60">
								    <col>
								    <col>
								    <col>
								    <col width="200"> 
								    <col>
								</colgroup>
								<thead>
								    <tr>
								      <th>序号</th>
								      <th>附件名称</th>
								      <th>上传人</th>
								      <th>上传时间</th>
								      <th>操作<button class="layui-btn layui-btn-primary layui-btn-sm" style="margin-left:20px;">上传附件</button></th>
								    </tr> 
								</thead>
								<tbody>
								    <tr>
								      <td>1</td>
								      <td>附件1</td>					      
								      <td>张三</td>
								      <td>2018-04-13</td>
								      <td><button class="layui-btn layui-btn-primary layui-btn-sm">删除</button></td>
								    </tr>
								</tbody>
							</table>
					    </div>
					    <div class="layui-tab-item">
					    	<table class="layui-table" style="margin:0;">
								<colgroup>
								    <col width="60">
								    <col>
								    <col>
								    <col>
								    <col width="200"> 
								    <col>
								</colgroup>
								<thead>
								    <tr>
								      <th>序号</th>
								      <th>附件名称</th>
								      <th>上传人</th>
								      <th>上传时间</th>
								      <th>操作<button class="layui-btn layui-btn-primary layui-btn-sm" style="margin-left:20px;">上传附件</button></th>
								    </tr> 
								</thead>
								<tbody>
								    <tr>
								      <td>1</td>
								      <td>附件1</td>					      
								      <td>张三</td>
								      <td>2018-04-13</td>
								      <td><button class="layui-btn layui-btn-primary layui-btn-sm">删除</button></td>
								    </tr>
								</tbody>
							</table>
					    </div>
					    <div class="layui-tab-item">
					    	<table class="layui-table" style="margin:0;">
								<colgroup>
								    <col width="120">
								    <col width="180">
								    <col width="120">
								    <col width="180">
								    <col width="120">
								    <col width="200">
								    <col width="120">
								    <col>
								</colgroup>
								<tbody>
								    <tr>
								      <th class="approval_th">环节名称</th>
								      <td>人工任务1</td>
								      <th class="approval_th">审批人</th>
								      <td>张三</td>
								      <th class="approval_th">岗位名称</th>
								      <td>网点开发员</td>
								      <th class="approval_th">审批时间</th>
								      <td>2018-03-12 10:00:00</td>								     
								    </tr> 
								    <tr>
								    	<th class="approval_th">审批意见</th>
								    	<td colspan="7">审批意见</td>
								    </tr>
								    <tr>
								      <th class="approval_th">环节名称</th>
								      <td>人工任务1</td>
								      <th class="approval_th">审批人</th>
								      <td>张三</td>
								      <th class="approval_th">岗位名称</th>
								      <td>网点开发员</td>
								      <th class="approval_th">审批时间</th>
								      <td>2018-03-12 10:00:00</td>								     
								    </tr> 
								    <tr>
								    	<th class="approval_th">审批意见</th>
								    	<td colspan="7">审批意见</td>
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
	<script type="text/javascript" src="resources/desmartportal/js/jquery-3.3.1.js" ></script>
	<script type="text/javascript" src="resources/desmartportal/js/layui.all.js"></script>	
	<script>
		$(function(){
			$(".add_row").click(function(){
				var le=$(".create_table tbody tr").length + 1;
				$(".create_table").append($('<tr>'+
						'<td>'+ le +'</td>'+
						'<td><input type="text" class="txt"/></td>'+
						'<td><input type="text" class="txt"/></td>'+
						'<td><input type="text" class="txt"/></td>'+
						'<td><input type="text" class="txt"/></td>'+
						'<td><i class="layui-icon delete_row">&#xe640;</i></td>'+
					'</tr>'));
				$(".delete_row").click(function(){
					$(this).parent().parent().remove();
				});
			});
			$(".delete_row").click(function(){
				$(this).parent().parent().remove();
			});
		})
	</script>