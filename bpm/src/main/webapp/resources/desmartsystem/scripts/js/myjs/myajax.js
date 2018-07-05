
//var index;
//$.ajaxSetup({beforeSend : function(){index=layer.load(1);}
//,complete: function () {layer.close(index)}
//});

var language= {
	query_no_data:'查询无数据',
	paging:function(beginNum,endNum,total){
		return "显示第 "+beginNum+" 至第 "+endNum+" 条记录, 共 "+total+" 条记录 ";
	},
	successful_operation:'操作成功',
	operation_failed:'操作失败',
	delete_success:'删除成功',
	delete_failed:'删除失败',
	modify_successfully:'修改成功',
	modify_failed:'修改失败',
	add_success:'添加成功',
	add_failed:'添加失败',
	please_select:'请选择',
	determine_to_perform:'确定要执行删除操作?',
	please_upload_pictures:'请上传小于200k的图片',
	please_upload_pictures1:'请上传小于2M的图片',
	error:'错误',
	image_types:'图片类型必须是.gif,jpeg,jpg,png,bmp中的一种',
	save_successfully:'保存成功',  
	save_failed:'保存失败',
	please_select_the_project_point:'请选择项目点',
	please_upload_pictures_less_than_500k:'请上传小于500k的图片'
};


var setting = {
	async: {
		enable: true,//是否开启异步加载模式
		url: "sysDepartment/treeDisplay",
		autoParam: ["id"]
	},
	data: {simpleData: {enable: true}}
};

//属性菜单展示
function treeDisplay(url,ids){
	$.ajax({ 
        url: url,    //后台webservice里的方法名称  
        type: "post",  
        dataType: "json",  
        success: function (data) {
        	if(typeof(ids)=='string'){
        		$.fn.zTree.init($("#"+ids), setting, data);
        	}else{
        		for (var i = 0; i < ids.length; i++) {
    				$.fn.zTree.init($("#"+ids[i]), setting, data);
    			}
        	}
        }
    });
}


(function($){
	$.fn.formEdit = function(data){
	    return this.each(function(){
	        var input, name;
	        if(data == null){this.reset(); return; }
	        for(var i = 0; i < this.length; i++){  
	            input = this.elements[i];
	            //checkbox的name可能是name[]数组形式
	            name = (input.type == "checkbox")? input.name.replace(/(.+)\[\]$/, "$1") : input.name;
	            if(data[name] == undefined) continue;
	            switch(input.type){
	                case "checkbox":
	                    if(data[name] == ""){
	                        input.checked = false;
	                    }else{
	                        //数组查找元素
	                        if(data[name].indexOf(input.value) > -1){
	                            input.checked = true;
	                        }else{
	                            input.checked = false;
	                        }
	                    }
	                break;
	                case "radio":
	                    if(input.value == data[name]){
	                        input.checked = true;
	                    }else{
	                    	input.checked = false;
	                    }
	                break;
	                case "select-one":
	                	theSelected(input,data[name]);
	                break;
	                case "button": break;
	                default:
	                	input.value = data[name];
	            }
	        }
	    });
	};
})(jQuery);

function theSelected(obj,data){
	for (var i = 0; i < obj.length; i++) {
		if(obj[i].value==data){
			obj[i].selected = true;
		}
	}
}

function selectoption(url,select){
	$(select).empty();
	$.ajax({  
        url: url,    //后台webservice里的方法名称  
        type: "post",  
        dataType: "json",  
        success: function (data) {
        	var optionstring="";
        	$(data).each(function(){
        		optionstring+="<option value=\"" + this.companyCode + "\" >" + this.companyName + "</option>";
        	});
        	$(select).prepend(optionstring);
        	if(select!='.companyCode'){
        		$(select).first().prepend("<option value='' selected='selected'>"+language.please_select+"</option>");
        	}
        }
    });
}



var dialogs = {
	add_dialog:'display_container',
	edit_dialog:'display_container1',
	add_team_dialog:'display_container2',
	user_dialog:'display_container3',
	userEditDialog:'display_container4',
	//userEditDialog:'display_container5',
};

//点新增打开dialog
function adddialog(){
	var $dailogs=$('.'+dialogs.add_dialog);
	$dailogs.css("display","block");
	$('form',$dailogs)[0].reset();
	$('form',$dailogs).validate().resetForms();
	$('form',$dailogs).find("input, select, textarea").removeClass('error');
}

function opendialog(cls){
	var $dailogs=$('.'+cls);
	$dailogs.css("display","block");
	$('form',$dailogs)[0].reset();
	$('form',$dailogs).validate().resetForms();
	$('form',$dailogs).find("input, select, textarea").removeClass('error');
}


function returnSuccess(data,cls){
	if(data.msg=='success'){
		layer.alert(language.add_success);
		dgclose(cls);
		pageBreak();
	}else{
		layer.alert(language.add_failed);
	}
}

//点修改打开dialog
function edit(data){
	var $dailogs=$('.'+dialogs.edit_dialog);
	$dailogs.css("display","block");
	$('form',$dailogs)[0].reset();
	$('form',$dailogs).formEdit(data);
	$('form',$dailogs).validate().resetForms();
	$('form',$dailogs).find("input, select, textarea").removeClass('error');
}

function choosableEdit(data,selector){
	var $dailogs=$('.'+selector);
	$dailogs.css("display","block");
	$('form',$dailogs)[0].reset();
	$('form',$dailogs).formEdit(data);
	$('form',$dailogs).validate().resetForms();
	$('form',$dailogs).find("input, select, textarea").removeClass('error');
}


//添加和修改
function validateCallback(form, callback) {
	var $form = $(form);
	if (!$form.valid()) {
		return false;
	}
	var _submitFn = function(){
		$form.find(':focus').blur();
		$.ajax({
			type: form.method || 'POST',
			url:$form.attr("action"),
			data:$form.serializeArray(),
			dataType:"json",
			cache: false,
			beforeSend: function () {
			   $('.layui-btn',$form).attr({disabled: "disabled"});
		    },
			success: callback,
			complete: function () {//完成响应
			   $('.layui-btn',$form).removeAttr("disabled");
		    },
			error: function(e){
	 	       //alert("添加或修改失败");
				layer.alert("失败");
		    }
		});
	}
	_submitFn();
	return false;
}

//添加和修改
function validateCallback_file(form, callback) {
	var $form = $(form);
	if (!$form.valid()) {
		return false;
	}
	var $ele =$form.find(':file');
	var file = $ele.val();
	if(file!=''){
		if(!/.(gif|jpg|jpeg|png|GIF|JPG|bmp)$/.test(file.toLowerCase())){
			layer.alert(language.image_types);
			return false;
		}else{
		    if((($ele.get(0).files[0].size).toFixed(2))>=(1024*200)){
		    	layer.alert(language.please_upload_pictures);
		         return false;
		     }
		}
	}
	
	var _submitFn = function(){
		$form.find(':focus').blur();
		$(form).ajaxSubmit({
			beforeSend: function () {
			   $('.btn-primary',$form).attr({disabled: "disabled"});
		    },
			success:callback,
			complete: function () {//完成响应
			   $('.btn-primary',$form).removeAttr("disabled");
		    },
			error: function (error) { layer.alert(language.error); }
		}); 
	}
	_submitFn();
	return false;
}

function ajaxTodo(url, callback){
	if(callback=='del'){
		layer.confirm(language.determine_to_perform, {
            btn : [ '确定', '取消' ]//按钮
        }, function(index) {
            layer.close(index);
            var $callback = callback;
        	if (! $.isFunction($callback)) $callback = eval('(' + callback + ')');
        	$.ajax({
        		type:'POST',
        		url:url,
        		dataType:"json",
        		cache: false,
        		success: $callback
        	});
        }); 
		return;
	}
	
	var $callback = callback;
 	if (! $.isFunction($callback)) $callback = eval('(' + callback + ')');
 	$.ajax({
 		type:'POST',
 		url:url,
 		dataType:"json",
 		cache: false,
 		success: $callback
 	});
}


function dishajaxTodos(url,form,prompting){
	var $form = $(form);
	$.ajax({
		type:'POST',
		url:url,
		data:$form.serializeArray(),
		dataType:"json",
		cache: false,
		success: function(data){
			if(data==false){
				$form.submit();
        	}else{
        		layer.alert(prompting);
        		return false;
        	}
		},error: function(e){
//			alert(language.operation_failed);
	    }
	});
}

//添加成功关闭dialog
function addsuccess(data){
	if(data.msg=='success'){
		layer.alert(language.add_success);
		dgclose(dialogs.add_dialog);
		pageBreak();
	}else{
		if(data.msg != 'error'){
			layer.alert(data.msg);
		}else {
			layer.alert(language.add_failed);
		}
	}
}

//修改成功关闭dialog
function updatesuccess(data){
	if(data.msg=='success'){
		layer.alert(language.modify_successfully);
		dgclose(dialogs.edit_dialog);
		pageBreak();
	}else{
		if(data.meg != 'error'){
			layer.alert(data.msg);
		}else {
			layer.alert(language.modify_failed);
		}
	}
}

function dgclose(dlgid){
	$("."+dlgid).css("display","none");
}


//修改操作状态
function del(data){
	if(data.msg=='success'){
		layer.alert(language.delete_success);
		pageBreak();
	}else{
		
		layer.alert(language.delete_failed);
	}
}


//分页查询
function pageBreak(pageNo){
	var form = $(".form-inline").get(0);
	if (pageNo) form["pageNo"].value =pageNo;
	search(form);
};

var page={pageSize:10}

//查询
function search(form){
	var $form = $(form);
	var _submitFn = function(){
		$.ajax({
			type: form.method || 'POST',
			url:$form.attr("action")+"?pageSize="+page.pageSize,
			data:$form.serializeArray(),
			dataType:"json",
			success: function(data){	
				table(data);
			},error: function(e){
	 	        //alert("查询失败");
		    }
		});
	}
	_submitFn();
	return false;
}


//自定定义查询回掉
function searchReturn(form,methods){
	var $form = $(form);
	var _submitFn = function(){
		$.ajax({
			type: form.method || 'POST',
			url:$form.attr("action"),
			data:$form.serializeArray(),
			dataType:"json",
			success: methods,
			error: function(e){
	 	        //alert("查询失败");
		    }
		});
	}
	_submitFn();
	return false;
}



//生成表格
function table(data) {
	var dataList = data.dataList;
	layui.use('laypage', function(){
	  var laypage = layui.laypage;
	  //执行一个laypage实例
	  laypage.render({
          elem: 'pagination',
          curr: data.pageNo,
          count: data.total,
          limit: data.pageSize,
          groups:3,
          layout: ['count', 'prev', 'page', 'next', 'limit', 'skip'],
          jump: function(obj, first){
              //obj包含了当前分页的所有参数  
        	  data.pageNo = obj.curr;
        	  data.pageSize = obj.limit;
        	  page.pageSize=obj.limit;
              if (!first) {
              	pageBreak(obj.curr);
              }
          }
      });
	});
	
//	laypage({
//        cont: 'pagination',
//        pages: Math.ceil(data.total / data.pageSize),
//        curr: data.pageNo || 1,
//        group: 3,
//        skip: true,
//        jump: function (obj, first) {
//            if (!first) {
//            	pageBreak(obj.curr);
//            }
//        }
//    });
	
 	
 	$("#tabletr").empty();//清空表格内容
    if (dataList.length > 0 ) {
    	tabledata(dataList,data);
     } else {       	
    	var colspan= $('#tabletr').prev().find("th").length;
    	$("#tabletr").append('<tr><td colspan ="'+colspan+'"><center>'+language.query_no_data+'</center></td></tr>');
     }
     //$("#pagingData").text(language.paging(data.beginNum,data.endNum,data.total));
}

var locat = (window.location+'').split('/'); 
locat =  locat[0]+'//'+locat[2]+'/'+locat[3];

function isEmpty(val){
	if(val==null){
		return "";
	}
	return val;
}


//修改操作状态
function status(data){
	if(data.msg=='success'){
		layer.alert(language.successful_operation);
		pageBreak();
	}else{
		layer.alert(language.operation_failed);
	}
}

function depart(data,depart){
	var projectarray=[];
	//projectarray.push(depart);
 	$(data).each(function(index,val){
 		projectarray.push(val.departName);
 	});
 	return projectarray.join(",");
}

function userName(data,user){
	var projectarray=[];
	//projectarray.push(user);
 	$(data).each(function(index,val){
 		projectarray.push(val.userUid);
 	});
 	return projectarray.join(",");
}