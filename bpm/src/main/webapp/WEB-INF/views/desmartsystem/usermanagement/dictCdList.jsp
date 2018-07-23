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
		
		<div class="container">
			<div class="search_area">
				<div class="layui-row">
					<form class="form-inline layui-form" method="post" action="sysDictionary/getSysDictionaryDataList"  onsubmit="return search(this);">
						<input type="hidden" name="pageNo" id="pageNo" value="1" />
						
						<div class="layui-col-md2">
							<input type="text" name="dicCode" placeholder="请输入字典代码" class="layui-input"/>
						</div>
						<div class="layui-col-md2">
							<input type="text" name="dicDataName" placeholder="请输入字典名称" class="layui-input"/>
						</div>
						<div class="layui-col-md2">
							<select  name="dicUid" class="dictTypeCd"   lay-verify="required"  lay-search="" ></select>
						</div>
						<div class="layui-col-md1" style="text-align:right;">
							<button class="layui-btn" type="button" onclick="pageBreak(1);">检索</button>
						</div>
						<div class="layui-col-md1" style="text-align:right;">
							<button class="layui-btn create_btn" type="button"  onclick="adddialog()">新建</button>
						</div>
						<div class="layui-col-md1" style="text-align:right;">
							<button class="layui-btn delete_btn" style="background: #FF5151" onclick="deleteDicCd();">删除</button>
						</div>
					</form>
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
					      <th><input type="checkbox" name="dic_data_list" onclick="onClickHander(this)"/>序号</th>
					      <th>字典代码</th>
					      <th>字典名称</th>
					      <th>字典类型代码</th>
					      <th>字典类型名称</th>
					      <th>字典说明</th>
					      <th>排序号</th>
					      <th>状态</th>
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
			<form class="layui-form form-horizontal" action="sysDictionary/saveSysDictionaryData" method="post"  onsubmit="return validateCallback(this,addsuccess);">
				<div class="middle" style="height:auto;" >
					  <div class="layui-form-item"  style="margin-top:20px;">
					    <label class="layui-form-label">字典代码</label>
					    <div class="layui-input-block">
					      <input type="text" name="dicDataCode" required lay-verify="required" autocomplete="off" class="layui-input required" />
					    </div>
					  </div>
				  	  <div class="layui-form-item">
					    <label class="layui-form-label">字典名称</label>
					    <div class="layui-input-block">
					      <input type="text" name="dicDataName" required lay-verify="required" autocomplete="off" class="layui-input required" />
					    </div>
					  </div>
					  <div class="layui-form-item">
					    <label class="layui-form-label">字典类型</label>
					    <div class="layui-input-block">
					    	<select  name="dicUid" class="dictTypeCd" required   lay-search="" style="width: 230px;" lay-verify="required"   ></select>
					    </div>
					  </div>
					  <div class="layui-form-item">
					    <label class="layui-form-label">字典说明</label>
					    <div class="layui-input-block">
					      <input type="text" name="dicDataDescription" autocomplete="off" class="layui-input" />
					    </div>
					  </div>
					  <div class="layui-form-item">
					    <label class="layui-form-label">排序号</label>
					    <div class="layui-input-block">
					      <input type="text" name="dicDataSort" autocomplete="off" class="layui-input required" onkeyup="value=value.replace(/[^\d]/g,'') "/>
					    </div>
					  </div>
					  <div class="layui-form-item">
					    <label class="layui-form-label">状态</label>
					    <div class="layui-input-block">
					      <input type="radio" name="dicDataStatus" value="on" title="启用" checked />
					      <input type="radio" name="dicDataStatus" value="off" title="禁用"  />
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
				<form class="layui-form" action="sysDictionary/updateSysDictionaryData" method="post"  onsubmit="return validateCallback(this,updatesuccess);">
				<div class="middle" style="height:auto;" >
					  <div class="layui-form-item"  style="margin-top:20px;">
					    <label class="layui-form-label">字典代码</label>
					    <div class="layui-input-block">
					      <input type="text" name="dicDataCode" required lay-verify="required" autocomplete="off" class="layui-input required" />
					    </div>
					  </div>
				  	  <div class="layui-form-item">
					    <label class="layui-form-label">字典名称</label>
					    <div class="layui-input-block">
					      <input type="text" name="dicDataName" required lay-verify="required" autocomplete="off" class="layui-input required" />
					    </div>
					  </div>
					  <div class="layui-form-item">
					    <label class="layui-form-label">字典类型</label>
					    <div class="layui-input-block">
					    	<select  name="dicUid" class="dictTypeCd" required  lay-search="" style="width: 230px;" lay-verify="required"   ></select>
					    </div>
					  </div>
					  <div class="layui-form-item">
					    <label class="layui-form-label">字典说明</label>
					    <div class="layui-input-block">
					      <input type="text" name="dicDataDescription" autocomplete="off" class="layui-input" />
					    </div>
					  </div>
					  <div class="layui-form-item">
					    <label class="layui-form-label">排序号</label>
					    <div class="layui-input-block">
					      <input type="text" name="dicDataSort" autocomplete="off" class="layui-input required" onkeyup="value=value.replace(/[^\d]/g,'') "/>
					    </div>
					  </div>
					  <div class="layui-form-item">
					    <label class="layui-form-label">状态</label>
					    <div class="layui-input-block">
					      <input type="radio" name="dicDataStatus" value="on" title="启用" checked />
					      <input type="radio" name="dicDataStatus" value="off" title="禁用"  />
					    </div>
					  </div>
					  </div>
					  <div class="foot">
					  	<input type="hidden" name="dicDataUid"/>
						<button class="layui-btn layui-btn sure_btn" type="submit"  >确定</button>
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
			
			dictTypeSelect('sysDictionary/listAllOnSysDictionary','.dictTypeCd');
			
		})
		//批量删除
		function deleteDicCd(){
			var dicDataUidArr = new Array();
			var checkArr = $("#tabletr").find("input[name='dic_one']");
			if(checkArr.length<1){
				layer.alert("请至少选择一条数据");
			}else{
				checkArr.each(function(){
					dicDataUidArr.push($(this).val());
				});
				layer.confirm("确认删除数据字典？", function(){
					$.ajax({
						url:"sysDictionary/deleteSysDictionaryDataList",
						data:{dicDataUidArr:dicDataUidArr},
						method:"post",
						beforeSend:function(){
							layer.load(1);
						},
						traditional:true,
						success:function(result){
							if(result.status==0){
								pageBreak(1);
								$("input[name='dic_data_list']").prop("checked",false);
								layer.alert("删除成功");
							}else{
								layer.alert("删除失败");
							}
							layer.closeAll("loading");
						},
						error:function(){
							layer.closeAll("loading");
						}
					});
				});
			}
		}
		//复选框全选，取消全选
		function onClickHander(obj){
			if(obj.checked){
				$("input[name='dic_one']").prop("checked",true);
			}else{
				$("input[name='dic_one']").prop("checked",false);
			}
		}
		
		//复选框分选
		function onClickSel(obj){
			if(obj.checked){
				var allSel = false;
				$("input[name='dic_one']").each(function(){
					if(!$(this).is(":checked")){
						allSel = true;
					}
				});
				
				//如果有checkbox没有被选中
				if(allSel){
					$("input[name='dic_data_list']").prop("checked",false);
				}else{
					$("input[name='dic_data_list']").prop("checked",true);
				}
			}else{
				$("input[name='dic_data_list']").prop("checked",false);
			}
		}
		
		//table数据显示
		function tabledata(dataList,data){
			var dataList = data.dataList;
			$(dataList).each(function(i){//重新生成
				var str='<tr>';
				str+='<td><input value="'+this.dicDataUid+'" type="checkbox" name="dic_one" onclick="onClickSel(this)"> ' + (data.beginNum+i) + '</td>';
				str+='<td>' + this.dicDataCode + '</td>';
				str+='<td>' + this.dicDataName + '</td>';
				
				if(this.sysDictionary!=null){
					str+='<td>' + this.sysDictionary.dicCode + '</td>';
					str+='<td>' + this.sysDictionary.dicName + '</td>';
				}else{
					str+='<td></td>';
					str+='<td></td>';
				}
				str+='<td>' + isEmpty(this.dicDataDescription) + '</td>';
				str+='<td>' + isEmpty(this.dicDataSort) + '</td>';
				if(this.dicDataStatus == 'on'){str+='<td>启用</td>';}else{str+='<td>禁用</td>';}
		        str+='<td>';
		        str+='<i class="layui-icon edit_user" onclick=ajaxTodo("sysDictionary/getSysDictionaryDataById?dicDataUid='+this.dicDataUid+'","edit") >&#xe642;</i>';
		        str+='<i class="layui-icon delete_btn" onclick=ajaxTodo("sysDictionary/deleteSysDictionaryData?dicDataUid='+this.dicDataUid+'","del") >&#xe640;</i>';
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
		        	optionstring+="<option value='' >请选择字典类型</option>";
		        	$(data.data).each(function(){
		        		optionstring+="<option value=\"" + this.dicUid + "\" >" + this.dicName + "</option>";
		        	}); 
		        	$(select).prepend(optionstring);
		        	layui.use('form', function(){
		    	        var form = layui.form;
		    	        form.render();
		    	    });
		        }
		    });
		}

	</script>
	</body>
	
</html>
