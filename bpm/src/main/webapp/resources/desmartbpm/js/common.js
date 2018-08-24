var common = {
	getPath : function(){
		var curWwwPath = window.document.location.href;
		var pathName = window.document.location.pathname;
		var pos = curWwwPath.indexOf(pathName);
		var localhostPaht = curWwwPath.substring(0, pos);
		var projectName = pathName.substring(0, pathName.substr(1).indexOf("/")+1);
		return (localhostPaht + projectName);
	},
	// 个人工作台项目路径
	getPortalPath: function() {
        var curWwwPath = window.document.location.href;
        var pathName = window.document.location.pathname;
        var pos = curWwwPath.indexOf(pathName);
        var localhostPaht = curWwwPath.substring(0, pos);
        return localhostPaht + "/portal";
	},
    // 管理项目路径
    getSystemPath: function() {
    	var curWwwPath = window.document.location.href;
        var pathName = window.document.location.pathname;
        var pos = curWwwPath.indexOf(pathName);
        var localhostPaht = curWwwPath.substring(0, pos);
        return localhostPaht + "/desmartsystem";
    },
    // 选人弹框的路径
    chooseUserPath : function(id, isSingle) {
    	return common.getPath() + "/sysUser/select_personnel?id=" + id +"&isSingle=" + isSingle; 
    },
    // 选角色弹框的路径
    chooseRolePath : function(id, isSingle) {
    	return common.getPath() + "/test/chooseRole?id=" + id +"&isSingle=" + isSingle; 
    },
    chooseTeamPath : function(id, isSingle) {
    	return common.getPath() + "/test/chooseTeam?id=" + id +"&isSingle=" + isSingle; 
    },
    chooseNotifyTemplatePath : function (id, templateType){
    	return common.getPath() + "/dhNotifyTemplate/select_template?id=" + id +"&templateType=" + templateType; 
    },
    chooseNotifyTemplate : function(elementId, templateType) {
    	var index=layer.open({
    	    type: 2,
    	    title: '选择模板',
    	    shadeClose: true,
    	    shade: 0.3,
    	    area: ['60%', '88%'],
    	    content: common.chooseNotifyTemplatePath(elementId, templateType),
    	    success: function (layero, lockIndex) {
    	        var body = layer.getChildFrame('body', lockIndex);
    	        body.find('button#close').on('click', function () {
    	            layer.close(lockIndex);
    	        });
    	    }
    	}); 
    },
    chooseUser: function(elementId, isSingle) {
    	var index=layer.open({
    	    type: 2,
    	    title: '选择人员',
    	    shadeClose: true,
    	    shade: 0.3,
    	    area: ['750px', '480px'],
    	    content: common.chooseUserPath(elementId, isSingle),
    	    success: function (layero, lockIndex) {
    	        var body = layer.getChildFrame('body', lockIndex);
    	        body.find('button#close').on('click', function () {
    	            layer.close(lockIndex);
    	        });
    	    }
    	}); 
    },
    chooseRole: function(elementId, isSingle) {
    	var index = layer.open({
            type: 2,
            title: '角色选择',
            shadeClose: true,
            shade: 0.3,
            area: ['790px', '580px'],
            content: common.chooseRolePath(elementId, isSingle),
            success: function(layero, lockIndex) {
            	var body = layer.getChildFrame('body', lockIndex);
            	body.find('button#cancel_btn').on('click', function () {
                    layer.close(lockIndex);
                });
            	body.find('button#sure_btn').on('click', function () {
                    layer.close(lockIndex);
                });
            }
        }); 
    },
    chooseTeam: function(elementId, isSingle) {
        var index = layer.open({
            type: 2,
            title: '角色组选择',
            shadeClose: true,
            shade: 0.3,
            area: ['790px', '580px'],
            content: common.chooseTeamPath(elementId, isSingle),
            success: function(layero, lockIndex) {
            	var body = layer.getChildFrame('body', lockIndex);
            	body.find('button#cancel_btn').on('click', function () {
                    layer.close(lockIndex);
                });
            	body.find('button#sure_btn').on('click', function () {
                    layer.close(lockIndex);
                });
            }
        }); 
    },
    //选择数据字典分类的路径
    chooseDictionaryPath:function(id) {
    	return common.getPath() + "/sysDictionary/selectDictionary?elementId=" + id; 
    },
    chooseDictionary:function(elementId){
    	var index=layer.open({
            type: 2,
            title: '数据字典分类选择',
            shadeClose: true,
            shade: 0.3,
            area: ['790px', '500px'],
            content: common.chooseDictionaryPath(elementId),
            success: function(layero, lockIndex) {
            	var body = layer.getChildFrame('body', lockIndex);
            	body.find('button#cancel_btn').on('click', function () {
                    layer.close(lockIndex);
                });
            	body.find('button#sure_btn').on('click', function () {
                    layer.close(lockIndex);
                });
            }
        }); 
    },
    //选择数据字典内容的路径
    chooseDicDataPath:function(id,dicUid) {
    	return common.getPath() + "/sysDictionary/selectDicData?elementId=" + id +"&dicUid="+dicUid; 
    },
    chooseDicData:function(elementId,dicUid){
    	var index = layer.open({
            type: 2,
            title: '数据字典详细数据选择',
            shadeClose: true,
            shade: 0.3,
            area: ['400px', '500px'],
            content: common.chooseDicDataPath(elementId,dicUid),
            success: function(layero, lockIndex) {
            	var body = layer.getChildFrame('body', lockIndex);
            	body.find('button#cancel_btn').on('click', function () {
                    layer.close(lockIndex);
                });
            	body.find('button#sure_btn').on('click', function () {
                    layer.close(lockIndex);
                });
            }
        }); 
    },
    //选择公共的子表单关联
    choosePublicFormPath:function(id,formUid) {
    	return common.getPath() + "/publicForm/selectPublicForm?elementId=" + id +"&formUid="+formUid; 
    },
    choosePublicForm:function(elementId,formUid){
    	var index=layer.open({
            type: 2,
            title: '选择关联公共子表单',
            shadeClose: true,
            shade: 0.3,
            area: ['500px', '500px'],
            content: common.choosePublicFormPath(elementId,formUid),
            success: function(layero, lockIndex) {
            	var body = layer.getChildFrame('body', lockIndex);
            	body.find('button#cancel_btn').on('click', function () {
                    layer.close(lockIndex);
                });
            }
        });
    },
	dateToString : function(date){   // 将date类型转为 "yyyy-MM-dd HH:mm:ss"
		var year = date.getFullYear();
		var month = date.getMonth()+1;
		var day = date.getDate();
		var hours = date.getHours();
		var minutes = date.getMinutes();
		var seconds = date.getSeconds();
		return year + "-" + common.getzf(month) + "-" + common.getzf(day) + " " + common.getzf(hours) + ":" + common.getzf(minutes) + ":" + common.getzf(seconds);
	},
	dateToDayString : function(date){   // 将date类型转为 "yyyy-MM-dd HH:mm:ss"
		var year = date.getFullYear();
		var month = date.getMonth()+1;
		var day = date.getDate();
		var hours = date.getHours();
		var minutes = date.getMinutes();
		var seconds = date.getSeconds();
		return year + "-" + common.getzf(month) + "-" + common.getzf(day);
	},
	getzf : function (num){  
		if(parseInt(num) < 10){  
			num = '0'+num;  
		}  
		return num;  
	},
	repNumber : function(obj){
		var reg = /^[\d]+$/g;
	    if (!reg.test(obj.value)) {
			 var txt = obj.value;
			 txt.replace(/[^0-9.]+/, function (char, index, val) {//匹配第一次非数字字符
				 obj.value = val.replace(/[^\d.]/g, "");//将非数字字符替换成""
				 var rtextRange = null;
				 if (obj.setSelectionRange) {
					 obj.setSelectionRange(index, index);
				 } else {//支持ie
					 rtextRange = obj.createTextRange();
					 rtextRange.moveStart('character', index);
					 rtextRange.collapse(true);
					 rtextRange.select();
				 }
			 })
		  }	
	 },
    // url: 访问路径， data： 传递的数据是一个对象， 成功时的回调函数，调用时自动传入 restult.data errMsg: error时的输出
    doPostAjax: function doPostAjax(params) {
        $.ajax({
            'url': params.url,
            'type': 'post',
            'dataType': 'json',
            'data': params.data,
            'beforeSend': function(){
                layer.load(1);
            },
            'success': function(result) {
                layer.closeAll('loading');
                if (result.status == 0) {
                    if (params.fn) {
                        params.fn(result.data);
                    } else {
                        layer.alert("操作成功");
                    }
                } else {
                    layer.alert(result.msg);
                }
            },
            error: function() {
                layer.closeAll('loading');
                layer.alert('操作失败，请稍后再试');
            }
        });
    },
    downLoadFile: function(url, params) {
        // 首先创建一个用来发送数据的iframe.
        var iframe = document.createElement('iframe')
        iframe.name = 'iframePost';
        iframe.style.display = 'none';
        document.body.appendChild(iframe);
        var form = document.createElement('form');
        var node = document.createElement('input');
        // 注册iframe的load事件处理程序,如果你需要在响应返回时执行一些操作的话.
        iframe.addEventListener('load', function () {
            if (iframe.contentDocument.body.innerText) {
                var val = iframe.contentDocument.body.innerText;
                var pattern = /^(msg:).+$/;
                if (pattern.exec(val)) {
                    layer.alert(val.replace('msg:', ''));
                }
            }
        });
        form.action = url;
        // 在指定的iframe中执行form
        form.target = iframe.name;
        form.method = 'post';
        for (var name in params) {
            node.name = name;
            node.value = params[name].toString()
            form.appendChild(node.cloneNode())
        }
        // 表单元素需要添加到主文档中.
        form.style.display = 'none';
        document.body.appendChild(form);
        form.submit();
        // 表单提交后,就可以删除这个表单,不影响下次的数据发送.
        document.body.removeChild(form);
    },
    openProView : function(insId){
		$.ajax({
	        url: common.getPath() +'/processInstance/viewProcess',
	        type: 'post',
	        dataType: 'text',
	        data: {
	            insId: insId
	        },
	        success: function (result) {
	            var index = layer.open({
	                type: 2,
	                title: '流程图',
	                shadeClose: true,
	                offset: ['50px', '20%'],
	                shade: 0.3,
	                maxmin:true,
	                area: ['890px', '570px'],
	                content: result
	            });
	        }
	    });
	}
	
	
};
// 如果要设置过期时间以秒为单位
function setCookie(c_name, value, expireseconds){
    var exdate=new Date();
    exdate.setTime(exdate.getTime()+expireseconds * 1000);
    document.cookie=c_name+ "=" +escape(value)+
    ((expireseconds==null) ? "" : ";expires="+exdate.toGMTString())
}

// 函数中的参数为 要获取的cookie键的名称。
function getCookie(c_name){
    if (document.cookie.length>0){
        c_start=document.cookie.indexOf(c_name + "=");
        if (c_start!=-1){
            c_start=c_start + c_name.length+1;
            c_end=document.cookie.indexOf(";",c_start);
            if (c_end==-1){ 
                c_end=document.cookie.length;
            }

            return unescape(document.cookie.substring(c_start,c_end));
        }
     }

    return "";
}
// 删除指定的Cookie
function delCookie(name) {    
    var exp = new Date();    
    exp.setTime(exp.getTime() - 1);    
    var cval = getCookie(name);    
    if (cval != null) document.cookie = name + "=" + cval + ";expires=" + exp.toGMTString();   

}

// validate添加验证 1-5位正整数
if (jQuery.validator) {
    jQuery.validator.addMethod('positiveInteger5', function (value, element) {
        if (/^[0-9]*[1-9][0-9]*$/.exec(value) && +value <= 99999) {
            return true;
        }
        return false;
    }, '请输入1-5位正整数');

    jQuery.validator.addMethod('positiveInteger', function (value, element) {
        if (/^[0-9]*[1-9][0-9]*$/.exec(value)) {
            return true;
        }
        return false;
    }, '请输入1-5位正整数');
}
