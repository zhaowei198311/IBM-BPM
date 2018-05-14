//绑定用户部门成功
function addSysUserDepartmentsuccess(data){
	serachDeparmet($("#bdbm_userUid").val());
}

function dels(data){
	serachDeparmet($("#bdbm_userUid").val());
}


$(function(){
	$("#bdbm").click(function(){
		var isManagerStr='';
		$('#sysUserDepartmentForm input[name="departUid"]:checked').each(function(){
			isManagerStr+=$(this).parent().siblings().last().children("select").val()+",";
		});
		$('#bdbm_isManager').val(isManagerStr);
		$('#sysUserDepartmentForm').submit();
	});
	 
	
	$("#copy_searchForm_btn").click(function(){
		var pageNo=$('#pageNoDepartmet').val();
		pageBreakDepartmet(pageNo);
	});
	
});


function departmentOfBinding(userUid){
	$("#bdbm_departName").val('');
	$("#bdbm_isManager").val('');
	$("#bdbm_userUid").val(userUid);
	$(".display_container10").css("display","block");
	serachDeparmet(userUid)
}

//pageBreakDepartmet(){
//	var pageNo=$('#pageNoDepartmet').val();
//}

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
	pageBreakDepartmet('');
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
		         	str+='<td>' + this.departName + '</td>';
		         	str+='<td>' + this.userName + '</td>';
		         	if(this.isManager=='false'){
			         	str+='<td><select ><option value="false">否</option><option value="true">是</option></select></td>'; 
		         	}else{
		         		str+='<td><select ><option value="true">是</option><option value="false">否</option></select></td>'; 
		         	}
		         	str+='<td><i class="layui-icon delete_btn" onclick=ajaxTodo("sysUserDepartment/deleteSysUserDepartment?uduid='+this.uduid+'","dels") >&#xe640;</i></td>';
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