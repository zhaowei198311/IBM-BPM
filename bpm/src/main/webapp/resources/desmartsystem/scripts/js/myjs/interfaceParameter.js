
function getPath(){
	var curWwwPath = window.document.location.href;
	var pathName = window.document.location.pathname;
	var pos = curWwwPath.indexOf(pathName);
	var localhostPaht = curWwwPath.substring(0, pos);
	var projectName = pathName.substring(0, pathName.substr(1).indexOf("/")+1);
	return (localhostPaht + projectName);
}

//弹出接口测试页面
function textInterface(intUid){
	var url = getPath() + '/interfaces/interfaceTest?intUid='+intUid;
    layer.open({
        type: 2,
        title: false,
        closeBtn: false,
        shadeClose: false,
        shade: 0.3,
        skin: 'layui-layer-molv',
        btn: ['关闭'],
        area: ['689px', '492px'],
        content: [url],
        success: function (layero, lockIndex) {
            var body = layer.getChildFrame('body', lockIndex);
            //绑定解锁按钮的点击事件
            body.find('button#close').on('click', function () {
                layer.close(lockIndex);
                //location.reload();//刷新
            });
        }
    });
}


function search(form){
	var $form = $(form);
	var _submitFn = function(){
		$.ajax({
			type: form.method || 'POST',
			url:$form.attr("action"),
			data:$form.serializeArray(),
			dataType:"json",
			success: function(result){	
				drawTable(result.data);
			}
		});
	}
	_submitFn();
	return false;
}

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
		break;
	case 'Array':
		paraSize.hide();
		dateFormat.hide();
		isMust.hide();
		arryParameterDiv.show();
		break;
	default:
		paraSize.show();
		isMust.show();
		dateFormat.hide();
		arryParameterDiv.hide();
		$table.empty();
		break;
	}
}

function interfaceInputShowAndHide(intType,selector){
	var intCallMethodDiv=$('.intCallMethodDiv'+selector);
	var intXml=$('.intXml'+selector);
	if(intType=='webservice'){
		intXml.show();
		intCallMethodDiv.hide();
	}else{
		intCallMethodDiv.show();
		intXml.hide();
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
	
	form.on('select(intType)', function(data){
		var value=data.value;
		interfaceInputShowAndHide(value,'');
	});
	
	form.on('select(intType1)', function(data){
		var value=data.value;
		interfaceInputShowAndHide(value,'1');
	});
	
	form.on('select(paraType1)', function(data){
		var value=data.value;
		byParameterTypeHideAndShowElement(value,"1");
	});
	
	form.on('select(paraType3)', function(data){
		var value=data.value;
		byParameterTypeHideAndShowElement(value,"3");
	});
	
	
	//新增子参数
	form.on('submit(confimAddChildNodeParameter)', function(data){
		var $form = $("#chilNodeParameterForm");
		if (!$form.valid()) {
			return false;
		}
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
//		str+='<td>'+isEmpty(field.paraXml)+'</td>';
		str+='<td><i class="layui-icon" title="修改参数" onclick="updateTr(this);" >&#xe642;</i> <i class="layui-icon" title="删除参数" onclick="deleteTr(this);" >&#xe640;</i></td>';
		$('#childNodeParameterTbody').append(str);
		layer.alert('绑定成功');
		$('#chilNodeParameterForm')[0].reset();
	    return false;
	});
	
	
	
	
	//新增参数
	form.on('submit(addParameterFilter)', function(data){
		var $form = $("#form2");
		if (!$form.valid()) {
			return false;
		}
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
				if(result.success==true){
					layer.alert(result.msg);
					closePopup('exposed_table2_container','id');
					getParamersInfo(intUid);
				}else{
					layer.alert(result.msg);
				}
				
				
			}
		})
	    return false;
	});
	
});


function deleteParameter(paraUid,intUid) {
	layer.confirm('是否删除该接口？', {
		btn : [ '确定', '取消' ], //按钮
		shade : false
	//不显示遮罩
	}, function(index) {
		// 提交表单的代码，然后用 layer.close 关闭就可以了，取消可以省略 ajax请求
		$.ajax({
			url : 'interfaceParamers/delete',
			type : 'POST',
			dataType : 'json',
			data : {
				paraUid : paraUid,
				intUid  : intUid
			},
			success : function(result) {
				if(result.success==true){
					layer.alert(result.msg);
					getParamersInfo(intUid);
				}else{
					layer.alert(result.msg);
				}
			}
		})
		layer.close(index);
	});
}


function getParameter(paraUid){
	layui.use([ 'layer', 'form' ], function() {
		var form = layui.form, layer = layui.layer, $ = layui.jquery;
		form.on('switch(switch4)', function(data) {
			var ckd = this.checked ? 'true' : 'false';
			document.getElementById("isMust3").value = ckd;
		})
		
		$.ajax({
			url : 'interfaceParamers/queryByparaId',
			type : 'POST',
			dataType : 'json',
			data : {
				paraUid : paraUid
			},
			success : function(result) {
				
				byParameterTypeHideAndShowElement(result.paraType,'3');
				
				$(".display_container6").css("display", "block");
				$("#paraUid3").val(result.paraUid);
				$("#paraIndex3").val(result.paraIndex);
				$("#paraName3").val(result.paraName);
				$("#paraDescription3").val(result.paraDescription);
				$("#paraType3").val(result.paraType);
				$("#paraSize3").val(result.paraSize);
				$("#multiSeparator3").val(result.multiSeparator);
				
				$("#dateFormat3").val(result.dateFormat);
				
				if(result.isMust=="true"){
					document.getElementById('isMust3').checked  = true;
				}else{
					document.getElementById('isMust3').checked  = false;
				}
				$("#intUid3").val(result.intUid);
				form.render();
			}
		});
	})

}

//删除当前tr
function deleteTr(ts){
	$(ts).parent().parent().remove();
}

function updateTr(ts){
	var value=$(ts).parent().parent().attr('value');
	var valueJson=JSON.parse(decodeURI(value));
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

$(function(){
	test('fasdfsa');
});

function test(name){
	var bb='<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" fasdfsa xmlns:web="http://webservice.lyfwebservice.ibm.com">';
	console.log(bb);
}