

$(function(){
	$("#chilNodeParameterForm").validate();
});



function byParameterTypeHideAndShowElement(paraType,selector){
	var isMust=$('.isMust'+selector);
	var paraSize=$('.paraSize'+selector);
	var dateFormat=$('.dateFormat'+selector);
	var arryParameterDiv=$('#arryParameterDiv'+selector);
	var $table=$('#childNodeParameterTbody'+selector);
	switch (paraType) {
	case 'Date':
		dateFormat.show();
		paraSize.hide();
		isMust.show();
		arryParameterDiv.hide();
		$table.empty();
		paraSize.empty();
		break;
	case 'Array':
		paraSize.hide();
		dateFormat.hide();
		isMust.hide();
		arryParameterDiv.show();
		dateFormat.empty();
		break;
	default:
		paraSize.show();
		isMust.show();
		dateFormat.hide();
		arryParameterDiv.hide();
		$table.empty();
		dateFormat.show();
		dateFormat.empty();
		break;
	}
}


function isEmpty(val){
	if(typeof(val)=='undefined'||val==null){
		val=''
	}
	return val;
};


layui.use(['form', 'layedit', 'laydate'], function(){
	var form = layui.form
	
	form.on('switch(isMustCheck)', function(data) {
		var ckd2 = this.checked ? 'true' : 'false';
		$(this).val(ckd2);
	})
	
	form.on('select(paraType)', function(data){
		var value=data.value;
		byParameterTypeHideAndShowElement(value,"");
	});
	
	form.on('select(paraType1)', function(data){
		var value=data.value;
		byParameterTypeHideAndShowElement(value,"1");
	});
	
	
	
	//新增子参数
	form.on('submit(confimAddChildNodeParameter)', function(data){
		var field=data.field;
		if (typeof(field.isMust) == "undefined")
		{
			field.isMust='false';
		}
		var str='<tr value="'+encodeURI(JSON.stringify(field))+'">';
		str+='<td>'+field.paraName+'</td>';
		str+='<td>'+field.paraType+'</td>';
		str+='<td>'+field.paraDescription+'</td>';
		str+='<td>'+isEmpty(field.paraSize)+'</td>';
		str+='<td>'+field.isMust+'</td>';
		str+='<td>'+isEmpty(field.dateFormat)+'</td>';
		str+='<td>'+isEmpty(field.paraXml)+'</td>';
		str+='<td><i class="layui-icon" title="删除参数" onclick="deleteTr(this);" >&#xe640;</i></td>';
		$('#childNodeParameterTbody').append(str);
		layer.alert('绑定成功');
		$('#chilNodeParameterForm')[0].reset();
	    return false;
	});
	
	//新增参数
	form.on('submit(addParameterFilter)', function(data){
		
		var intUid=$('#intUid').val();
		
		var arrayParameter=new Array();
		var field=data.field;
		if (typeof(field.isMust) == "undefined")
		{
			field.isMust='false';
		}
		field.intUid=intUid;
		arrayParameter.push(field);
		
		
		$('#arryParameterDiv table tbody tr').each(function(index,value){
			var value=$(value).attr('value');
			var valueJson=JSON.parse(decodeURI(value));
			valueJson.intUid=intUid;
			arrayParameter.push(valueJson);
		});
		
		$.ajax({
			url : 'interfaceParamers/add',
			type : 'POST',
			dataType : 'json',
			contentType:"application/json",
			data : JSON.stringify(arrayParameter),
			success : function(result) {
				layer.alert('绑定成功');
				closePopup('exposed_table2_container','id');
				getParamersInfo(intUid);
			}
		})
	    return false;
	});
	
});


function deleteParameter(paraUid){
	$.ajax({
		url : 'interfaceParamers/delete',
		type : 'POST',
		dataType : 'json',
		data : {paraUid,paraUid},
		success : function(result) {
			layer.alert(result);
		}
	})
}

function getParameter(paraUid){
	
}

//删除当前tr
function deleteTr(ts){
	$(ts).parent().parent().remove();
}

//添加array参数
function addArrayParameter(){
	popupDivAndReset('chilNodeParameterContainer','id');
	byParameterTypeHideAndShowElement('String',1);
}




//弹出Div并重置
function popupDivAndReset(chooser,chooserType){
	var $dailogs;
	if(chooserType=='id'){
		$dailogs=$('#'+chooser);
	}else if(chooserType=='class'){
		$dailogs=$('.'+chooser);
	}
	$dailogs.css("display","block");
	$('form',$dailogs)[0].reset();
	$('form',$dailogs).validate().resetForm();
	$('form',$dailogs).find("input, select, textarea").removeClass('error');
}

//关闭
function closePopup(chooser,chooserType){
	var $dailogs;
	if(chooserType=='id'){
		$dailogs=$('#'+chooser);
	}else if(chooserType=='class'){
		$dailogs=$('.'+chooser);
	}
	$dailogs.css("display","none");
}