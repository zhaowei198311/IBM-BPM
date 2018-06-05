
layui.use(['form', 'layedit', 'laydate'], function(){
	var form = layui.form
	
	form.on('switch(isMustCheck)', function(data) {
		var ckd2 = this.checked ? 'true' : 'false';
		$(this).val(ckd2);
	})
	
	form.on('submit(confimAddChildNodeParameter)', function(data){
		var field=data.field;
		if (typeof(field.isMust) == "undefined")
		{
			field.isMust='false';
		}
		var str='<tr>';
		str+='<td>'+field.paraName+'</td>';
		str+='<td>'+field.paraType+'</td>';
		str+='<td>'+field.paraDescription+'</td>';
		str+='<td>'+field.paraSize+'</td>';
		str+='<td>'+field.isMust+'</td>';
		
		str+='<td>'+field.isMust+'</td>';
		str+='</tr>';
		$('#childNodeParameterTbody').append(str);
	    layer.alert(JSON.stringify(field));
	    return false;
	});
});

//添加array参数
function addArrayParameter(){
	popupDivAndReset('chilNodeParameterContainer','id');
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