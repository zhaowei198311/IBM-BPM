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
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1">
<link href="resources/desmartsystem/styles/css/layui.css"
	rel="stylesheet" media="all" />
<link href="resources/desmartsystem/styles/css/my.css" rel="stylesheet"
	media="all" />
<link
	href="resources/desmartsystem/styles/css/modules/laydate/default/laydate.css"
	rel="stylesheet" media="all" />
<title>接口管理</title>
<style type="text/css">
.display_content2 {
	min-height: auto;
}
.cancel2_btn{margin-top:15px}
</style>
</head>

<body>
	<div class="layui-container" style="margin-top: 20px; width: 100%;">
		<div class="search_area">
			<div class="layui-row layui-form">
				<form action="interfaces/queryDhInterfaceByTitle"  method="post"  onsubmit="return search(this);"  id="interfaceSearch">
				<div class="layui-col-md2">
					<input id="interfaceName" type="text" placeholder="接口名称" class="layui-input" name="intTitle" >
				</div>
				<div class="layui-col-md2">
					<select id="interfaceType" name="intType">
						<option value="">请选择接口类型</option>
						<option value="webservice">webservice</option>
						<option value="restapi">restapi</option>
						<option value="rpc">rpc</option>
					</select>
				</div>
				<div class="layui-col-md2">
					<select id="interfaceState" name="intStatus">
						<option value="">请选择接口状态</option>
						<option value="enabled">启用</option>
						<option value="disabled">停用</option>
					</select>
				</div>
				<div class="layui-col-md2" style="text-align: right; width: 280px">
					<button class="layui-btn layui-btn-sm serch_interface">查询接口</button>
					<button id="addInterfaces"
						class="layui-btn layui-btn-sm create_btn" type="button">新增接口</button>
				</div>
				</form>
			</div>
		</div>
		<div>
			<table class="layui-table backlog_table" lay-even lay-skin="nob"
				lay-filter="demo">
				<colgroup>
					<col width="8%">
					<col width="10%">
					<col width="10%">
					<col width="9%">
					<col width="15%">
					<col width="10%">
					<col width="10%">
					<col width="3%">
				</colgroup>
				<thead>
					<tr>
						<th>序号</th>
						<th>接口名称</th>
						<th>接口描述</th>
						<th>接口类型</th>
						<th>接口访问地址</th>
						<th>接口访问方法名</th>
						<th>接口状态</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody id="proMet_table_tbody" />
			</table>
		</div>
		<div id="lay_page"></div>
	</div>
	<div class="display_container3" id="exposed_table_container">
		<div class="display_content2">
			<div class="top">接口参数配置</div>
			<button class="layui-btn layui-btn-sm"
				style="float: right; margin: 0 15px 15px 0;" onclick="add();">添加</button>
			<div class="middle1">
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
							<th>参数名称</th>
							<th>参数描述</th>
							<th>参数类型</th>
							<th>参数长度</th>
							<th>是否必须</th>
							<th>日期格式</th>
							<th>父参数</th>
							<th>输入/输出</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody id="exposed_table_tbody"></tbody>
				</table>
			</div>
			<div class="foot">
				<!-- <button class="layui-btn update2_btn"></button> -->
				<button class="layui-btn cancel2_btn">关闭</button>
			</div>
		</div>
	</div>
	<div class="display_container">
		<div class="display_content2">

			<div class="top" style="color: red;">新增接口</div>
			<label class="layui-input-label" style="color: red;">带*为必填参数</label>
			<form id="form1" class="layui-form" action=""
				style="margin-top: 30px;">
				<div class="layui-form-item">
					<div class="layui-row">
						<div class="layui-col-md6">
							<div class="layui-inline">
								<label class="layui-form-label">接口名称*:</label>
								<div class="layui-input-inline">
									<input type="text" id="intTitle" name="intTitle"
										lay-verify="intTitle" autocomplete="off" class="layui-input required">
								</div>
							</div>
						</div>
						<div class="layui-col-md6">
							<div class="layui-inline">
								<label class="layui-form-label">接口描述:</label>
								<div class="layui-input-inline">
									<input type="text" id="intDescription" name="intDescription"
										lay-verify="intDescription" autocomplete="off"
										class="layui-input required">
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="layui-form-item">
					<div class="layui-row">
						<div class="layui-col-md6">
							<label class="layui-form-label">接口类型*:</label>
							<div class="layui-input-inline">
								<select id="intType" name="intType" lay-filter="intType" class="required">
									<option value="">请选择接口类型</option>
									<option value="webservice">webservice</option>
									<option value="restapi">restapi</option>
									<option value="rpc">rpc</option>
								</select>
							</div>
						</div>
						<div class="layui-col-md6">
							<div class="layui-inline">
								<label class="layui-form-label">接口地址*:</label>
								<div class="layui-input-inline">
									<input type="text" id="intUrl" name="intUrl"
										lay-verify="intUrl" autocomplete="off" class="layui-input required">
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="layui-form-item">
					<div class="layui-row">
						<div class="layui-col-md6 intCallMethodDiv" >
							<label class="layui-form-label">接口方法名:</label>
							<div class="layui-input-inline">
								<input type="text" id="intCallMethod" name="intCallMethod"
									lay-verify="intCallMethod" autocomplete="off"
									class="layui-input required">
							</div>
						</div>
						<div class="layui-col-md6">
							<div class="layui-inline">
								<label class="layui-form-label">登录用户名:</label>
								<div class="layui-input-inline">
									<input type="text" id="intLoginUser" name="intLoginUser"
										lay-verify="intLoginUser" autocomplete="off"
										class="layui-input required">
								</div>
							</div>
						</div>
					</div>
				</div>
				
				<div class="layui-col-md11 intRequestXml" >
				  <div class="layui-form-item layui-form-text">
				 	<label class="layui-form-label">requset:</label>
				    <div class="layui-input-block">
				      <textarea placeholder="" class="layui-textarea" name="intRequestXml" ></textarea>
				    </div>
				  </div>
				</div>		
				
				<div class="layui-col-md11 intResponseXml" >
				  <div class="layui-form-item layui-form-text">
				 	<label class="layui-form-label">response:</label>
				    <div class="layui-input-block">
				      <textarea placeholder="" class="layui-textarea" name="intResponseXml" ></textarea>
				    </div>
				  </div>
				</div>	
						
				<div class="layui-form-item">
					<div class="layui-row">
						<div class="layui-col-md6">
							<label class="layui-form-label">登陆密码:</label>
							<div class="layui-input-inline">
								<input type="text" id="intLoginPwd" name="intLoginPwd"
									lay-verify="intLoginPwd" autocomplete="off" class="layui-input required">
							</div>
						</div>
						<div class="layui-col-md6">
							<div class="layui-inline">
								<label class="layui-form-label">接口状态*:</label>
								<div class="layui-input-inline">
									<input id="intStatus" type="checkbox" name="intStatus"
										lay-skin="switch" lay-filter="intStatus" lay-text="启用|停用"
										value="disabled">
								</div>
							</div>
						</div>
					</div>
				</div>
			</form>
			<div class="foot">
				<button id="sure_btn" class="layui-btn layui-btn sure_btn">确定</button>
				<button id="cancel_btn" class="layui-btn layui-btn cancel_btn">取消</button>
			</div>
		</div>
	</div>

	<!-- 新增接口参数表单 -->
	<div class="display_container4" id="exposed_table2_container">
		<div class="display_content2 boundInterfaceParameter" >
			<div class="top" style="color: red;">绑定接口参数</div>
			<label style="color: red;">带*的参数为必填</label> <input id="intUid"
				type="hidden" />
			<form id="form2" class="layui-form" action="javascript:void(0);" style="margin-top: 30px;">
				<div class="layui-form-item">
					<div class="layui-row">
						<div class="layui-col-md6">
							<label class="layui-form-label">参数名称*:</label>
							<div class="layui-input-inline">
								<input type="text" id="paraName" name="paraName"
									lay-verify="paraName" autocomplete="off" class="layui-input required">
							</div>
						</div>
						<div class="layui-col-md6">
							<label class="layui-form-label">参数类型*:</label>
							<div class="layui-input-inline">
								<select name="paraType" class="require" lay-filter="paraType">
									<option value="String">String</option>
									<option value="Integer">Integer</option>
									<option value="Double">Double</option>
									<option value="Boolean">Boolean</option>
									<option value="Date">Date</option>
									<option value="Array">Array</option>
								</select>
							</div>
						  </div>
					</div>
				</div>
				
				<div class="layui-form-item">
					<div class="layui-row">
						<div class="layui-col-md12">
							<label class="layui-form-label">参数描述:</label>
							<div class="layui-input-block">
								<input type="text" id="paraDescription" name="paraDescription"
									lay-verify="paraDescription" autocomplete="off"
									class="layui-input paraDescription required" />
							</div>
						</div>
					</div>
				</div>
				<div class="layui-form-item">
					<div class="layui-row">
						<div class="layui-col-md6 paraSize">
							<label class="layui-form-label">参数长度:</label>
							<div class="layui-input-inline">
								<input type="text" id="paraSize" name="paraSize"
									lay-verify="paraSize" autocomplete="off" class="layui-input">
							</div>
						</div>
						
						<div class="layui-col-md6 dateFormat">
							<label class="layui-form-label">日期格式:</label>
							<div class="layui-input-inline">
								<input type="text"  name="dateFormat"  placeholder="例如:yyyy-MM-dd" class="layui-input">
							</div>
						</div>
						
						
						<div class="layui-col-md6  isMust">
							<label class="layui-form-label">是否必须*:</label>
							<div class="layui-input-inline">
								<input id="isMust" type="checkbox" name="isMust"
									lay-skin="switch" lay-filter="switch4" lay-text="true|false"
									value="false">
							</div>
						</div>
					</div>
				</div>
				
				<div class="layui-form-item">
					<div class="layui-col-md6 ">
						<label class="layui-form-label">输入/输出:</label>
						<div class="layui-input-inline">
							<input  type="checkbox" name="paraInOut" lay-skin="switch" lay-filter="paraInOut"  lay-text="输出|输入"  />
						</div>
					</div>
				</div>

				<div id="arryParameterDiv" >
					<div class="layui-form-item">
					  <button class="layui-btn layui-btn-sm" type="button" onclick="addArrayParameter('add');" style="float: right;margin: 0 15px 0;" >
					    <i class="layui-icon">&#xe654;</i>
					  </button>
					</div>
					<div class="middle1">
						<table class="layui-table backlog_table"
							lay-even lay-skin="nob">
							<colgroup>
								<col>
								<col>
								<col>
								<col>
								<col>
							</colgroup>
							<thead>
								<tr>
									<th>参数名称</th>
									<th>参数描述</th>
									<th>参数类型</th>
									<th>参数长度</th>
									<th>是否必须</th>
									<th>日期格式</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody id="childNodeParameterTbody" ></tbody>
						</table>
					</div>
				</div>
			<div class="foot">
				<!-- <button id="sure3_btn" class="layui-btn layui-btn sure3_btn" lay-submit="" lay-filter="addParameterFilter" >确定</button> -->
				<button  class="layui-btn layui-btn" lay-submit="" lay-filter="addParameterFilter" >确定</button>
				<button id="cancel3_btn" class="layui-btn layui-btn cancel3_btn">取消</button>
			</div>
			</form>
		</div>
	</div>
	<div class="display_container5" id="exposed_table3_container">
		<div class="display_content2">
			<div class="top" style="color: red;">修改接口</div>
			<form id="updaArrayForm" class="layui-form" action="javascript:void(0);" style="margin-top: 30px;">
				<input id="intUid2" style="display: none;" name="intUid" />
				<div class="layui-form-item">
					<div class="layui-inline">
						<label class="layui-form-label">接口名称:</label>
						<div class="layui-input-inline">
							<input type="text" id="intTitle2" name="intTitle"
								lay-verify="text" autocomplete="off" class="layui-input required"
								value="">
						</div>
					</div>
					<div class="layui-inline">
						<label class="layui-form-label">接口描述:</label>
						<div class="layui-input-inline">
							<input type="text" id="intDescription2" name="intDescription"
								lay-verify="text" autocomplete="off" class="layui-input required"
								value="">
						</div>
					</div>
				</div>
				<div class="layui-form-item">
					<div class="layui-inline">
						<label class="layui-form-label">接口类型:</label>
						<div class="layui-input-inline">
							<select id="intType2" name="intType" lay-filter="intType1">
								<option value="webservice">webservice</option>
								<option value="restapi">restapi</option>
								<option value="rpc">rpc</option>
							</select>
						</div>
					</div>
					<div class="layui-inline">
						<label class="layui-form-label">接口地址:</label>
						<div class="layui-input-inline">
							<input type="text" id="intUrl2" name="intUrl" lay-verify="text"
								autocomplete="off" class="layui-input required">
						</div>
					</div>
				</div>
				<div class="layui-form-item">
					<div class="layui-inline intCallMethodDiv1">
						<label class="layui-form-label intCallMethod1">接口方法名:</label>
						<div class="layui-input-inline">
							<input type="text" id="intCallMethod2" name="intCallMethod"
								lay-verify="text" autocomplete="off" class="layui-input required">
						</div>
					</div>
					<div class="layui-inline">
						<label class="layui-form-label">登录用户名:</label>
						<div class="layui-input-inline">
							<input type="text" id="intLoginUser2" name="intLoginUser"
								lay-verify="text" autocomplete="off" class="layui-input required">
						</div>
					</div>
				</div>
				
				<div class="layui-col-md11 intRequestXml1" >
				  <div class="layui-form-item layui-form-text">
				 	<label class="layui-form-label">requset:</label>
				    <div class="layui-input-block">
				      <textarea placeholder="" class="layui-textarea" id="intRequestXml2" name="intRequestXml" ></textarea>
				    </div>
				  </div>
				</div>		
				
				<div class="layui-col-md11 intResponseXml1" >
				  <div class="layui-form-item layui-form-text">
				 	<label class="layui-form-label">response:</label>
				    <div class="layui-input-block">
				      <textarea placeholder="" class="layui-textarea" id="intResponseXml2" name="intResponseXml" ></textarea>
				    </div>
				  </div>
				</div>	
				
				<div class="layui-form-item">
					<div class="layui-inline">
						<label class="layui-form-label">登陆密码:</label>
						<div class="layui-input-inline">
							<input type="text" id="intLoginPwd2" name="intLoginPwd"
								lay-verify="text" autocomplete="off" class="layui-input required">
						</div>
					</div>
					<div class="layui-inline">
						<label class="layui-form-label">接口状态:</label>
						<div class="layui-input-inline">
							<input id="intStatus2" type="checkbox" name="intStatus" lay-skin="switch" lay-filter="intStatusUpd" lay-text="启用|停用" >
						</div>
					</div>
				</div>
			</form>
			<div class="foot">
				<button id="sure4_btn" class="layui-btn layui-btn sure4_btn">确定</button>
				<button id="cancel4_btn" class="layui-btn layui-btn cancel4_btn">取消</button>
			</div>
		</div>
	</div>
	
	
	<div class="display_container6" id="exposed_table3_container">
		<div class="display_content2  boundInterfaceParameter">
			<div class="top" style="color: red;">修改绑定接口参数</div>
			<form class="layui-form" action="" style="margin-top: 30px;">
				<input id="intUid3" type="hidden" /> 
				<input id="paraUid3" type="hidden"  name="paraUid" />
			
				<div class="layui-form-item">
					<div class="layui-row">
						<div class="layui-col-md6">
							<label class="layui-form-label">参数名称*:</label>
							<div class="layui-input-inline">
								<input type="text" id="paraName3" name="paraName"
									lay-verify="paraName" autocomplete="off" class="layui-input required">
							</div>
						</div>
						<div class="layui-col-md6">
							<label class="layui-form-label">参数类型*:</label>
							<div class="layui-input-inline">
								<select name="paraType" class="require" id="paraType3"  lay-filter="paraType3">
									<option value="String">String</option>
									<option value="Integer">Integer</option>
									<option value="Double">Double</option>
									<option value="Boolean">Boolean</option>
									<option value="Date">Date</option>
									<option value="Array">Array</option>
								</select>
							</div>
						  </div>
					</div>
				</div>
				
				<div class="layui-form-item">
					<div class="layui-row">
						<div class="layui-col-md12">
							<label class="layui-form-label">参数描述:</label>
							<div class="layui-input-block">
								<input type="text" id="paraDescription3" name="paraDescription"
									lay-verify="paraDescription" autocomplete="off"
									class="layui-input paraDescription required" />
							</div>
						</div>
					</div>
				</div>
				<div class="layui-form-item">
					<div class="layui-row">
						<div class="layui-col-md6 paraSize3">
							<label class="layui-form-label">参数长度:</label>
							<div class="layui-input-inline">
								<input type="text" id="paraSize3" name="paraSize"
									lay-verify="paraSize" autocomplete="off" class="layui-input">
							</div>
						</div>
						
						
						<div class="layui-col-md6 dateFormat3" style="display:none;">
							<label class="layui-form-label">日期格式:</label>
							<div class="layui-input-inline">
								<input type="text"  name="dateFormat" id="dateFormat3" placeholder="例如:yyyy-MM-dd" lay-verify="paraSize"   autocomplete="off" class="layui-input">
							</div>
						</div>
						
						
						<div class="layui-col-md6">
							<label class="layui-form-label">是否必须*:</label>
							<div class="layui-input-inline">
								<input id="isMust3" type="checkbox" name="isMust" lay-skin="switch" lay-filter="switch4" lay-text="true|false" />
							</div>
						</div>
					</div>
				</div>
				
				<div class="layui-form-item" id="paraInOut" >
					<div class="layui-col-md6 ">
						<label class="layui-form-label">输入/输出:</label>
						<div class="layui-input-inline">
							<input type="checkbox" name="paraInOut"   lay-skin="switch"  lay-filter="paraInOut" lay-text="输出|输入"  />
						</div>
					</div>
				</div>
				
			<div id="arryParameterDiv3" >
				<div class="layui-form-item">
				  <button class="layui-btn layui-btn-sm" type="button" onclick="addArrayParameter('update');" style="float: right;margin: 0 15px 0;" >
				    <i class="layui-icon">&#xe654;</i>
				  </button>
				</div>
				<div class="middle1">
					<table class="layui-table backlog_table"
						lay-even lay-skin="nob">
						<colgroup>
							<col>
							<col>
							<col>
							<col>
							<col>
						</colgroup>
						<thead>
							<tr>
								<th>参数名称</th>
								<th>参数描述</th>
								<th>参数类型</th>
								<th>参数长度</th>
								<th>是否必须</th>
								<th>日期格式</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody id="childNodeParameterTbody3" ></tbody>
					</table>
				</div>
			</div>
			
			<div class="foot">
				<button  class="layui-btn layui-btn" lay-submit="" lay-filter="updateParameterFilter" >确定</button>
				<!-- <button id="sure5_btn" class="layui-btn layui-btn sure5_btn">确定</button> -->
				<button  class="layui-btn layui-btn" type="button" onclick="closePopup('display_container6','class');" >取消</button>
			</div>
		</form>
		</div>
	</div>
</body>






<!-- 新增array接口参数表单 -->
<div class="display_container4" id="chilNodeParameterContainer">
	<div class="display_content2 boundInterfaceParameter" >
		<div class="top" style="color: red;">绑定接口参数</div>
		<label style="color: red;">带*的参数为必填</label>
		<form class="layui-form" id="chilNodeParameterForm"
			action="javascript:void(0);" style="margin-top: 30px;">
			<div class="layui-form-item">
				<div class="layui-inline">
					<label class="layui-form-label">参数名称*:</label>
					<div class="layui-input-inline">
						<input type="text" name="paraName" lay-verify="paraName"
							autocomplete="off" class="layui-input required">
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">参数类型*:</label>
					<div class="layui-input-inline">
						<select name="paraType" class="require" lay-filter="paraType1" >
							<option value="String">String</option>
							<option value="Integer">Integer</option>
							<option value="Double">Double</option>
							<option value="Boolean">Boolean</option>
							<option value="Date">Date</option>
						</select>
					</div>
				</div>
			</div>

			<div class="layui-form-item">
				<label class="layui-form-label">参数描述:</label>
				<div class="layui-input-block">
					<input type="text" name="paraDescription"
						lay-verify="paraDescription" autocomplete="off"
						class="layui-input paraDescription" />
				</div>
			</div>
			<div class="layui-form-item">
				<div class="layui-inline paraSize1">
					<label class="layui-form-label">参数长度:</label>
					<div class="layui-input-inline">
						<input type="text" name="paraSize" lay-verify="paraSize"
							autocomplete="off" class="layui-input">
					</div>
				</div>
				
				<div class="layui-inline dateFormat1" style="display:none;">
					<label class="layui-form-label">日期格式:</label>
					<div class="layui-input-inline">
						<input type="text"  name="dateFormat"  placeholder="例如:yyyy-MM-dd" lay-verify="paraSize"   autocomplete="off" class="layui-input">
					</div>
				</div>
				
				<div class="layui-inline isMust1">
					<label class="layui-form-label">是否必须*:</label>
					<div class="layui-input-inline">
						<input type="checkbox" name="isMust" lay-skin="switch"
							lay-filter="isMustCheck" lay-text="true|false">
					</div>
				</div>
			</div>
			<div class="foot">
				<button class="layui-btn layui-btn" lay-submit=""
					lay-filter="confimAddChildNodeParameter">确定</button>
				<button class="layui-btn layui-btn"
					onclick="closePopup('chilNodeParameterContainer','id');">取消</button>
			</div>
		</form>
		<input type="hidden" id="addArrayInAddOrUpdate" />
	</div>
</div>
<!-- 新增array接口参数表单 -->

<!-- 修改array接口参数表单 -->
<div class="display_container4" id="updateInterfaceParameter">
	<div class="display_content2 boundInterfaceParameter">
		<div class="top" style="color: red;">修改绑定接口参数</div>
		<label style="color: red;">带*的参数为必填</label>
		<form class="layui-form" id="updateInterfaceParameterForm"
			action="javascript:void(0);" style="margin-top: 30px;">
			<input  type="hidden" name="paraUid" />
			<div class="layui-form-item">
				<div class="layui-inline">
					<label class="layui-form-label">参数名称*:</label>
					<div class="layui-input-inline">
						<input type="text" name="paraName" lay-verify="paraName"
							autocomplete="off" class="layui-input required">
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">参数类型*:</label>
					<div class="layui-input-inline">
						<select name="paraType"  lay-filter="paraType2" id="paraType2" >
							<option value="String">String</option>
							<option value="Integer">Integer</option>
							<option value="Double">Double</option>
							<option value="Boolean">Boolean</option>
							<option value="Date">Date</option>
						</select>
					</div>
				</div>
			</div>

			<div class="layui-form-item">
				<label class="layui-form-label">参数描述:</label>
				<div class="layui-input-block">
					<input type="text" name="paraDescription"
						lay-verify="paraDescription" autocomplete="off"
						class="layui-input paraDescription" />
				</div>
			</div>
			<div class="layui-form-item">
				<div class="layui-inline paraSize2">
					<label class="layui-form-label">参数长度:</label>
					<div class="layui-input-inline">
						<input type="text" name="paraSize" lay-verify="paraSize"
							autocomplete="off" class="layui-input">
					</div>
				</div>
				
				<div class="layui-inline dateFormat2" style="display:none;">
					<label class="layui-form-label">日期格式:</label>
					<div class="layui-input-inline">
						<input type="text"  name="dateFormat"  placeholder="例如:yyyy-MM-dd" lay-verify="paraSize"   autocomplete="off" class="layui-input">
					</div>
				</div>
				
				<div class="layui-inline isMust2">
					<label class="layui-form-label">是否必须*:</label>
					<div class="layui-input-inline">
						<input type="checkbox" name="isMust" id="isMust2"  lay-skin="switch"  lay-filter="isMustCheckUpd" lay-text="true|false" />
					</div>
				</div>
			</div>
			<div class="foot">
				<button class="layui-btn layui-btn" lay-submit=""
					lay-filter="updateAddChildNodeParameter">确定</button>
				<button class="layui-btn layui-btn"
					onclick="closePopup('updateInterfaceParameter','id');">取消</button>
			</div>
			<input type="hidden" id="updateArrayInAddOrUpdate"  />
		</form>
	</div>
</div>
<!-- 修改array接口参数表单 -->



</html>
<script type="text/javascript"
	src="resources/desmartsystem/scripts/js/jquery-3.3.1.js"
	charset="utf-8"></script>
<script type="text/javascript"
	src="resources/desmartsystem/scripts/js/layui.all.js" charset="utf-8"></script>
<script type="text/javascript"
	src="resources/desmartsystem/scripts/js/validate_util/jquery.validate.min.js"
	charset="utf-8"></script>
<script type="text/javascript"
	src="resources/desmartsystem/scripts/js/validate_util/dwz.regional.zh_CN.js"
	charset="utf-8"></script>
<script type="text/javascript"
	src="resources/desmartsystem/scripts/js/myjs/interface.js"></script>

<script type="text/javascript"
	src="resources/desmartsystem/scripts/js/myjs/interfaceParameter.js"></script>