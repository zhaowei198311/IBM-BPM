<%@ page language="java" contentType="text/html; charset=UTF-8"
    isErrorPage="true"
    pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<base href="<%=basePath%>">
		<meta charset="utf-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
  		<title>数据字典</title>
  		<%@ include file="common/common.jsp" %>
  		
	</head>
	<body>
		<form class="form-inline" method="post" action="sysDictCd/allSysDictCd"  onsubmit="return search(this);">
			<input type="hidden" name="pageNo" id="pageNo" value="1" >
			<input type="hidden" name="parentId" value="0" >
		</form>
		<div class="container">
			<div class="search_area">
				<div class="layui-row layui-form">					
					<div class="layui-col-md1" style="text-align:right;">
					    <button class="layui-btn create_btn" onclick="adddialog();" >新建</button>
					</div>
				</div>
			</div>
			<div>				
				<table class="layui-table backlog_table" lay-even lay-skin="nob">
					<colgroup>
					    <col>
					    <col>
					    <col>
					</colgroup>
					<thead>
					    <tr>
					      <th>序号</th>
					      <th>字典代码</th>
					      <th>字典名称</th>
					      <th>字典类型</th>
					      <th>字典类型名称</th>
					      <th>操作</th>
					    </tr> 
					</thead>
					<tbody id="tabletr">
					</tbody>
				</table>
				<div id="pagination"></div>
			</div>
		</div>
	<div class="display_container">
		<div class="display_content" style="height:auto;">
			<div class="top">
				新增字典
			</div>
			<form class="layui-form"  method="post"  action="sysDictCd/addSysDictCd"  onsubmit="return validateCallback(this,addsuccess);">
			<div class="middle" style="height:auto;" >
				  <div class="layui-form-item" style="margin-top:20px;" >
				    <label class="layui-form-label">字典代码</label>
				    <div class="layui-input-block">
				      <input type="text" name="dictCd" required  lay-verify="required"  autocomplete="off" class="layui-input" />
				    </div>
				  </div>
				  <div class="layui-form-item">
				    <label class="layui-form-label">字典名称</label>
				    <div class="layui-input-block">
				      <input type="text" name="dictNm" required  lay-verify="required" autocomplete="off" class="layui-input" />
				    </div>
				  </div>
				  <div class="layui-form-item">
				    <label class="layui-form-label">字典类型</label>
				    <div class="layui-input-block">
				    	<select  name="dictTypeCd" class="dictTypeCd" required  style="width: 230px;" lay-verify="required"   ></select>
				    </div>
				  </div>
			</div>
			<div class="foot">
				<button class="layui-btn layui-btn sure_btn" type="submit"  >确定</button>
				<button class="layui-btn layui-btn layui-btn-primary cancel_btn" type="button">取消</button>
			</div>
			</form>
		</div>
	</div>	
	
	
	<div class="display_container1">
		<div class="display_content1" style="height:auto;">
			<div class="top">
				编辑字典
			</div>
			<form class="layui-form" action="sysDictCd/updateSysDictCd" method="post"  onsubmit="return validateCallback(this,updatesuccess);">
			<div class="middle" style="height:auto;" >
				  <div class="layui-form-item" style="margin-top:20px;">
				    <label class="layui-form-label">字典代码</label>
				    <div class="layui-input-block">
				      <input type="text" name="dictCd" required  lay-verify="required" autocomplete="off" class="layui-input" />
				    </div>
				  </div>
				  <div class="layui-form-item">
				    <label class="layui-form-label">字典名称</label>
				    <div class="layui-input-block">
				      <input type="text" name="dictNm" required  lay-verify="required" autocomplete="off" class="layui-input" />
				    </div>
				  </div>
				  <div class="layui-form-item">
				    <label class="layui-form-label">字典类型</label>
				    <div class="layui-input-block">
				    	<select  name="dictTypeCd" class="dictTypeCd"  style="width: 230px;"  lay-search="" ></select>
				    </div>
				  </div>
				  <input type="hidden" name="dcUid"  />
			</div>
			<div class="foot">
				<button class="layui-btn layui-btn sure_btn" type="submit" >确定</button>
				<button class="layui-btn layui-btn layui-btn-primary cancel_btn" type="button">取消</button>
			</div>
			</form>
		</div>
	</div>	
	
		<script>
		$(function(){
			pageBreak($('#pageNo').val());
			
			$(".cancel_btn").click(function(){
				$(".display_container").css("display","none");
				$(".display_container1").css("display","none");
			});
			
			dictTypeSelect('sysDictTp/allSysDictTp','.dictTypeCd');
			
		})
		//table数据显示
		function tabledata(dataList,data){
			 $(dataList).each(function(i){//重新生成
				var str='<tr>';
				str+='<td>' + (data.beginNum+i) + '</td>';
	         	str+='<td>' + this.dictCd + '</td>';
	         	str+='<td>' + this.dictNm + '</td>';
	         	str+='<td>' + this.sysDictTp.dictTypeCd + '</td>';
	         	str+='<td>' + this.sysDictTp.dictTypeNm + '</td>';
		        str+='<td>';       
		        str+='<i class="layui-icon edit_user" onclick=ajaxTodo("sysDictCd/getSysDictCd?dcUid='+this.dcUid+'","edit") >&#xe642;</i>';
		        str+='<i class="layui-icon delete_btn" onclick=ajaxTodo("sysDictCd/deleteSysDictCd?dcUid='+this.dcUid+'","del") >&#xe640;</i>';
		        str+='</td>';
	         	$("#tabletr").append(str);
	         });
		}
		
		
		
		function dictTypeSelect(url,select){
			$(select).empty();
			$.ajax({  
		        url: url,    //后台webservice里的方法名称  
		        type: "post",  
		        dataType: "json",  
		        success: function (data) {
		        	var optionstring="";
		        	optionstring+="<option value='' ></option>";
		        	$(data.dataList).each(function(){
		        		optionstring+="<option value=\"" + this.dictTypeCd + "\" >" + this.dictTypeNm + "</option>";
		        	}); 
		        	$(select).prepend(optionstring);
		        	/* layui.use('form', function(){
		    	        var form = layui.form;
		    	        form.render();
		    	    }); */
		        }
		    });
		}

	</script>
	</body>
	
</html>
