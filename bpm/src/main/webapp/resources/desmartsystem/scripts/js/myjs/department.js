//绑定用户部门成功
function addSysUserDepartmentsuccess(data){
	serachDeparmet($("#bdbm_userUid").val());
}



//同步指定用户  syn--synchronization
function synchronizationAppointUser(){
	opendialog('display_container_synAppointUser');
}

function addUserDepartment(data){
	returnSuccess(data,'display_container_company_department');
	if(data.msg='sucess'){
		serachDeparmet(data.obj.userUid);
	}
}



function closeDepartmentOfBinding(){
	$('.display_container3').css('display','none');
	pageBreak();
}

//提交用户和部门
function submitUserDeaprtment(ts){
	var companyCode=$('#companyCode').val();
	if(companyCode==''){
		layer.alert('请选择公司');
		return false;
	}
	
	var userUid=$('#bdbm_userUid').val();
	
	
	var departUid=$('#departUid_b').val();
	if(departUid==''){
		layer.alert('请选择部门');
		return false;
	}
	
	$.ajax({
		url:'sysUserDepartment/userDepartmentExists',
		type:'post',
		dataType:'json',
		data:{userUid:userUid,departUid:departUid},
		success:function(data){
			if(data){
				$(ts).submit();
			}else{
				layer.alert('当前部门已配置请选择其他部门');
			}
		}
	});
	
};

//删除部门
function dels(data){
	serachDeparmet($("#bdbm_userUid").val());
}


$(function(){
	$('#add_department_company_btn').click(function(){
		opendialog('display_container_company_department');
		companySelect('sysCompany/allCompany','.companyCode');
	});
});


function companySelect(url,select){
	$(select).empty();
	$.ajax({  
        url: url,    //后台webservice里的方法名称  
        type: "post",  
        dataType: "json",  
        success: function (data) {
        	var optionstring="";
        	optionstring+="<option value='' ></option>";
        	$(data).each(function(){
        		optionstring+="<option value=\"" + this.companyCode + "\" >" + this.companyName + "</option>";
        	});
        	$(select).prepend(optionstring);
        	if(select!='.companyCode'){
        		$(select).first().prepend("<option value='' selected='selected'>"+language.please_select+"</option>");
        	}
        	
        	layui.use('form', function(){
    	        var form = layui.form;
    	        form.render();
    	    });
        }
    });
}


//关闭弹框
function closeDialog(cls){
	$("."+cls).css("display","none");
}

function departmentOfBinding(userUid){
	$("#bdbm_departName").val('');
	$("#bdbm_isManager").val('');
	$("#bdbm_userUid").val(userUid);
	$(".display_container10").css("display","block");
	serachDeparmet(userUid)
}

function pageBreakDepartmet(){
	var pageNo=$('#pageNoDepartmet').val();
}

function pageBreakDepartmet(curr){
	
	var  bdbm_userUid=$('#bdbm_userUid').val();
	
	$.ajax({
		type:'POST',
		url:"sysDepartment/allSysDepartment",
		data:{departName:$('#bdbm_departName').val(),ext1:bdbm_userUid,pageNo:curr},
		dataType:"json",
		success: function(data){
			laypage({
		        cont: 'pagination1',
		        pages: Math.ceil(data.total / data.pageSize),
		        curr: data.pageNo || 1,
		        group: 3,
		        //skip: true,
		        jump: function (obj, first) {
		            if (!first) {
		            	pageBreakDepartmet(obj.curr);
		            }
		        }
		    });
			var dataList = data.dataList;
		 	$("#tabletr1").empty();//清空表格内容
		    if (dataList.length > 0 ) {
		    	/* tabledata(dataList,data); */
		    	$(dataList).each(function(i){//重新生成
					var str='<tr>';
					str+='<td><input type="checkbox" name="departUid"  value="' + this.departUid + '"/>' + (data.beginNum+i) + '</td>';
					str+='<td>' + isEmpty(this.companyCode) + '</td>';
					str+='<td>' + isEmpty(this.companyName) + '</td>';
		         	str+='<td>' + this.departName + '</td>';
		         	str+='<td>' + this.departNo + '</td>';
		         	/* str+='<td>' + this.departAdmins + '</td>'; */
		         	str+='<td><select ><option value="false">否</option><option value="true">是</option></select></td>'; 
		         	str+='</tr>';
		         	$("#tabletr1").append(str);
		         });
		    	
		     } else {       	
		    	var colspan= $('#tabletr1').prev().find("th").length;
		    	$("#tabletr1").append('<tr><td colspan ="'+colspan+'"><center>'+language.query_no_data+'</center></td></tr>');
		     }
		}
	});
};

function serachDeparmet(userUid){
	//pageBreakDepartmet('');
	$.ajax({
		type:'POST',
		url:"sysUserDepartment/selectAll?userUid="+userUid,
		dataType:"json",
		success: function(data){
			var dataList = data;
		 	$("#tabletr2").empty();//清空表格内容
		    if (typeof(dataList) != "undefined" && dataList.length > 0 ) {
		    	/* tabledata(dataList,data); */
		    	$(dataList).each(function(i){//重新生成
					var str='<tr>';
					str+='<td>' + (i+1) + '</td>';
					
					var companyName='';
					if(this.sysCompany!=null){
						companyName=isEmpty(this.sysCompany.companyName);
					}
					str+='<td>' + companyName + '</td>';
					str+='<td>' + isEmpty(this.companyCode) + '</td>';
		         	str+='<td>' + this.departName + '</td>';
		         	str+='<td>' + this.departNo + '</td>';
		         	if(this.isManager=='false'){
			         	str+='<td>否</td>'; 
		         	}else{
		         		str+='<td>是</td>'; 
		         	}
		         	str+='<td><i class="layui-icon delete_btn" onclick=delUserDepartment("'+this.uduid+'","'+this.userUid+'"); >&#xe640;</i></td>';
		         	
		         	
		         	
		         	str+='</tr>';
		         	$("#tabletr2").append(str);
		         });
		     } else {       	
		    	var colspan= $('#tabletr2').prev().find("th").length;
		    	$("#tabletr2").append('<tr><td colspan ="'+colspan+'"><center>'+language.query_no_data+'</center></td></tr>');
		     }
		}
	});
}

function delUserDepartment(uduid,userUid){
	layer.confirm(language.determine_to_perform,function(index){
		layer.close(index);
		$.ajax({
			url:'sysUserDepartment/selectAll',
			type:'post',
			data:{userUid:userUid},
			dataType:'json',
			success:function(data){
				console.log(data.length);
				if(data.length==1){
					layer.alert('部门信息不能全部删除');
					return false;
				}else{
					ajaxTodo("sysUserDepartment/deleteSysUserDepartment?uduid="+uduid,"dels");
				}
			}
		});
	});
	
}

