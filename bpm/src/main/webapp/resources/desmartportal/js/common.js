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
    chooseUser: function(elementId, isSingle) {
    	layer.open({
    	    type: 2,
    	    title: '选择人员',
    	    shadeClose: true,
    	    shade: 0.8,
    	    area: ['615px', '492px'],
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
    	layer.open({
            type: 2,
            title: '角色选择',
            shadeClose: true,
            shade: 0.8,
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
        layer.open({
            type: 2,
            title: '角色组选择',
            shadeClose: true,
            shade: 0.8,
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
    	layer.open({
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
    	layer.open({
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
	dateToString : function(date){   // 将date类型转为 "yyyy-MM-dd HH:mm:ss"
		var year = date.getFullYear();
		var month = date.getMonth()+1;
		var day = date.getDate();
		var hours = date.getHours();
		var minutes = date.getMinutes();
		var seconds = date.getSeconds();
		return year + "-" + common.getzf(month) + "-" + common.getzf(day) + " " + common.getzf(hours) + ":" + common.getzf(minutes) + ":" + common.getzf(seconds);
	},
	dateToSimpleString : function(date){   // 将date类型转为 "yyyy-MM-dd HH:mm:ss"
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
	//抽取页面中动态表单的数据
	getDesignFormData:function(){
		var inputArr = $("#formSet .form-sub input");
		var textareaArr = $("#formSet .form-sub textarea");
		var control = true; //用于控制复选框出现重复值
		var checkName = ""; //用于获得复选框的class值，分辨多个复选框
		var json = "{";
		for (var i = 0; i < inputArr.length; i++) {
			var type = $(inputArr[i]).attr("type");
			var textJson = "";
			var checkJson = "";
			switch (type) {
				case "text": {
					if ($(inputArr[i]).prop("class") == "layui-input layui-unselect" 
						|| $(inputArr[i]).prop("class") == "layui-input layui-unselect layui-disabled") {
						var name = $(inputArr[i]).parent()
							.parent().prev().prop("name");
						if(name==null || name==""){
							break;
						}
						var value = $("[name='" + name + "']")
							.val().trim();
						textJson = "\"" + name
							+ "\":{\"value\":\"" + value
							+ "\"}";
						break;
					}else if($(inputArr[i]).attr("title")=="choose_user"){
						var name = $(inputArr[i]).attr("name");
						if(name==null || name==""){
							break;
						}
						var userNameArr = $(inputArr[i]).val().trim();
						var userIdArr = $(inputArr[i]).parent().find("input[type='hidden']")
									.val().trim();
						textJson = "\"" + name + "\":{\"value\":\""
							+ userIdArr + "\",\"description\":\"" + userNameArr + "\"}";
						break;
					}else if($(inputArr[i]).attr("title")=="choose_value"){
						var name = $(inputArr[i]).attr("name");
						if(name==null || name==""){
							break;
						}
						var dicDataName = $(inputArr[i]).val().trim();
						var dicDataCode = $(inputArr[i]).parent().find("input[class='value_code']")
									.val().trim();
						var dicDataUid = $(inputArr[i]).parent().find("input[class='value_id']")
									.val().trim();
						textJson = "\"" + name + "\":{\"value\":\""
							+ dicDataCode + "\",\"description\":\"" + dicDataName + "\",\"dicDataUid\":\""+dicDataUid+"\"}";
						break;
					}
				}
					;
				case "tel":
					;
				case "date":{
					var name = $(inputArr[i]).attr("name");
					if(name==null || name==""){
						break;
					}
					var value = $("[name='" + name + "']")
						.val().trim();
					textJson = "\"" + name + "\":{\"value\":\""
						+ value + "\"}";
					break;
				};
				case "radio": {
					var name = $(inputArr[i]).attr("name");
					if(name==null || name==""){
						break;
					}
					var radio = $("[name='" + name + "']")
						.parent().parent().find(
							"input:radio:checked");
					if(radio.length!=0){
						textJson = "\"" + name + "\":{\"value\":\""
						+ radio.val().trim() + "\"}";
					}else{
						textJson = "\"" + name + "\":{\"value\":\"\"}";
					}
					break;
				}
				case "checkbox": {
					var name = $(inputArr[i]).attr("name");
					if(name==null || name==""){
						break;
					}
					var checkbox = $("[name='" + name + "']")
						.parent().parent().find(
							"input:checkbox:checked");
					
					//判断每次的复选框是否为同一个class
					if (control) {
						checkName = checkbox.attr("name");
					} else {
						if (checkName != checkbox.attr("name")) {
							checkName = checkbox.attr("name");
							control = true;
						}
					}
					
					if (control) {
						if(checkbox.length!=0){
							control = false;
							checkJson += "\"" + checkName
								+ "\":{\"value\":[";
							for (var j = 0; j < checkbox.length; j++) {
								if (j == checkbox.length - 1) {
									checkJson += "\""
										+ $(checkbox[j]).val().trim() + "\"";
								} else {
									checkJson += "\""
										+ $(checkbox[j]).val().trim() + "\",";
								}
							}
							checkJson += "]},";
						}else{
							control = false;
							checkJson += "\"" + name + "\":{\"value\":[]},";
						}
					}
					json += checkJson;
					break;
				}
			}//end switch
			if(textJson.substring(textJson.length-1) != "," && textJson!=null && textJson!=""){
				textJson += ",";
			}
			if (json.indexOf(textJson) == -1) {
				json += textJson;
			}
		}

		for(var i=0;i<textareaArr.length;i++){
			var name = $(textareaArr[i]).attr("name");
			var value = $("[name='" + name + "']")
				.val().trim();
			var textJson = "\"" + name + "\":{\"value\":\""
				+ value + "\"},";
			if (json.indexOf(textJson) == -1) {
				json += textJson;
			}
		}
		//获得最后一位字符是否为","
		var charStr = json.substring(json.length - 1,
			json.length);

		if (charStr == ",") {
			json = json.substring(0, json.length - 1);
		}
		json += "}";
		json = json.replace(/\t/g,"");
		console.log(json);
		return json;
	},
	//传入表单json数据给表单组件赋值
	giveFormSetValue:function(jsonStr){
		var json = JSON.parse(jsonStr)
		for(var name in json){
			var paramObj = json[name];
			var tagName = $("[name='"+name+"']").prop("tagName");
			switch(tagName){
				case "INPUT":{
					var tagType = $("[name='"+name+"']").attr("type");
					switch(tagType){
						case "text":{
							if($("[name='"+name+"']").attr("title")=="choose_user"){
								var valueStr = paramObj["value"];
								var descriptionStr = paramObj["description"];
								$("[name='"+name+"']").val(descriptionStr);
								$("[name='"+name+"']").parent().find("input[type='hidden']").val(valueStr);
								break;
							}else if($("[name='"+name+"']").attr("title")=="choose_value"){
								var value = paramObj["value"];
								var description = paramObj["description"];
								var id = paramObj["id"];
								$("[name='"+name+"']").val(description);
								$("[name='"+name+"']").parent().find("input[type='value_id']").val(value);
								$("[name='"+name+"']").parent().find("input[type='value_code']").val(id);
								break;
							}
						};
						case "tel":;
						case "date":{
							$("[name='"+name+"']").val(paramObj["value"]);
							break;
						};
						case "radio":{
							$("[name='"+name+"'][value='"+paramObj["value"]+"']").prop("checked","true");
							break;
						}
						case "checkbox":{
							var valueArr = paramObj["value"];
							for(var value in valueArr){
								$("[name='"+name+"'][value='"+valueArr[value]+"']").prop("checked","true");
							}
							break;
						}
					}
					break;
				};
				case "SELECT":;
				case "TEXTAREA":{
					$("[name='"+name+"']").val(paramObj["value"]);
					break;
				}
			}
		}//end for
	},
	//当表单中某块信息全都不可见时，title也要不可见
	showTableP:function(){
		var tableArr = $("#formSet").find(".layui-table");
		tableArr.each(function(){
			var tdArr = $(this).find("td");
			var flag = true;
			tdArr.each(function(){
				if($(this).find("p").length==0 && $(this).css("display")!="none"){
					flag = false;
				}
			});
			if(flag){
				$(this).css("display","none");
				if($(this).prev().prop("tagName")=="P"){
					var pText = $(this).prev()[0].firstChild.data.trim();
					if($(this).attr("title")==pText){
						$(this).prev().css("display","none");
					}
				}
			}
		});
	},
	//根据字段权限json给动态表单组件设置权限
	giveFormFieldPermission:function(jsonStr){
		console.log(jsonStr);
		var json = JSON.parse(jsonStr);
		for(var name in json){
			var perJsonStr = json[name];
			if(perJsonStr!=null && perJsonStr!=""){
				switch(name){
					case "fieldJsonStr":{
						common.fieldPermissionNoPrint(perJsonStr);
						break;
					}
					case "titleJsonStr":{
						common.titlePermissionNoPrint(perJsonStr);
						break;
					}
					case "fieldPrintJsonStr":{
						common.fieldPrintPermission(perJsonStr);
						break;
					}
					case "titlePrintJsonStr":{
						common.titlePrintPermission(perJsonStr);
						break;
					}
				}
			}
		}
	},
	//普通字段的可见性、可编辑行控制
	fieldPermissionNoPrint:function(json){
		for(var name in json){
			var paramObj = json[name];
			var display = paramObj["display"];
			if(display=="none"){
				var tagType = $("[name='"+name+"']").attr("type");
				$("[name='"+name+"']").parent().css("display","none");
				$("[name='"+name+"']").parent().prev().css("display","none");
				if(tagType=="radio" || tagType=="checkbox"){
					$("[name='"+name+"']").parent().css("display","none");
					$("[name='"+name+"']").parent().prev().css("display","none");
				}
			}
			var edit = paramObj["edit"];
			if(edit=="no"){
				common.fieldNoEditPermission(name);
			}
		}
		//是否显示标题
		common.showTableP();
	},
	//普通字段不可编辑
	fieldNoEditPermission:function(name){
		$("[name='"+name+"']").attr("disabled","true");
		var tagName = $("[name='"+name+"']").prop("tagName");
		var tagType = $("[name='"+name+"']").attr("type");
		var className = $("[name='"+name+"']").attr("class");
		if(tagType=="checkbox"){
			$("[name='"+name+"']").attr("disabled","true");
		}
		if(tagType=="radio"){
			$("[name='"+name+"']").attr("disabled","true");
			var title = $("[name='"+name+"']:checked").attr("title");
			$("[name='"+name+"']:checked").parent().html("<span style='margin-left:10px;'>"+title+"</span>");
		}
		if(tagName=="SELECT"){
			$("[name='"+name+"']").attr("disabled","true");
			$("[name='"+name+"']").next().find("input");
		}
		if($("[name='"+name+"']").attr("title")=="choose_user" 
			|| $("[name='"+name+"']").attr("title")=="choose_value"){
			$("[name='"+name+"']").parent().find("i").css("display","none");
			$("[name='"+name+"']").css("width","100%");
		}
		if(className=="layui-input date"){
			$("[name='"+name+"']").attr("disabled","true");
			if($("[name='"+name+"']").val()=="" || $("[name='"+name+"']").val()==null){
				$("[name='"+name+"']").prop("type","text");
			}
		}
	},
	//标题字段的可见性、可编辑性控制
	titlePermissionNoPrint:function(json){
		for(var name in json){
			var paramObj = json[name];
			var display = paramObj["display"];
			var edit = paramObj["edit"];
			if(display=="none"){
				$("[name='"+name+"']").parent().css("display","none");
				var pTitle = $("[name='"+name+"']")[0].firstChild.data.trim();
				var tableArr = $("#formSet").find(".layui-table");
				for(var i=0;i<tableArr.length;i++){
					var talbeTitle = $(tableArr[i]).attr("title");
					if(talbeTitle==pTitle){
						$(tableArr[i]).css("display","none");
						break;
					}
				}
			}
			if(edit=="no"){
				var pTitle = $("[name='"+name+"']")[0].firstChild.data.trim();
				var tableArr = $("#formSet").find(".layui-table");
				for(var i=0;i<tableArr.length;i++){
					var talbeTitle = $(tableArr[i]).attr("title");
					if(talbeTitle==pTitle){
						var tdArr = $(tableArr[i]).find("td");
						tdArr.each(function(){
							var tdObj = $(this);
							var fieldCodeName = "";
							if(tdObj.find("input[type='text']").length!=0){
								fieldCodeName = tdObj.find("input[type='text']").attr("name");
							}
							if(tdObj.find("input[type='tel']").length!=0){
								fieldCodeName = tdObj.find("input[type='tel']").attr("name");
							} 
							if(tdObj.find("input[type='date']").length!=0){
								fieldCodeName = tdObj.find("input[type='date']").attr("name");
							}
							if(tdObj.find("input[type='radio']").length!=0){
								fieldCodeName = tdObj.find("input[type='radio']").attr("name");
							}
							if(tdObj.find("input[type='checkbox']").length!=0){
								fieldCodeName = tdObj.find("input[type='checkbox']").attr("name");
							}
							if(tdObj.find("select").length!=0){
								fieldCodeName = tdObj.find("select").attr("name");
							}
							if(tdObj.find("textarea").length!=0){
								fieldCodeName = tdObj.find("textarea").attr("name");
							}
							common.fieldNoEditPermission(fieldCodeName);
						});
						break;
					}
				}
			}
		}
	},
	//普通字段的打印权限控制
	fieldPrintPermission:function(json){
		
	},
	//标题字段的打印权限控制
	titlePrintPermission:function(json){
		
	},
	//验证动态表单必填项
	validateFormMust:function(id){
		var mustObjArr = $("#formSet table .tip_span");
		var value = "";
		var name = "";
		var flag = true;
		for(var i=0;i<mustObjArr.length;i++){
			var mustObj = $(mustObjArr[i]);
			var inputObj = $(mustObj.parent().next().find("input")[0]);
			var selectObj = $(mustObj.parent().next().find("select")[0]);
			var textareaObj = $(mustObj.parent().next().find("textarea")[0]);
			var text = mustObj.parent().find("label").text();
			if(inputObj.length == 1){
				var type = inputObj.attr("type");
				switch(type){
					case "text":
					case "date":
					case "tel":{
						value = inputObj.val();
						name = inputObj.attr("name");
						break;
					}
					case "radio":
					case "checkbox":{
						name = inputObj.attr("name");
						if($("[name='"+name+"']:checked").length<1){
							value = "";
						}
						break;
					}
				}
			}else if(textareaObj.length == 1){
				value = textareaObj.val();
				name = textareaObj.attr("name");
			}else if(selectObj.length == 1){
				value = selectObj.val();
				name = selectObj.attr("name");
			}
			$("[name='"+name+"']").attr({"required":"required","lay-verify":"required"});
			layui.use('form', function(){
				var form = layui.form;
				//监听提交
				form.on('submit('+id+')', function(data){
				    layer.msg(JSON.stringify(data.field));
				    return false;
				});
			});
			
			if(value=="" || value==null){
				var eleId = $("[name='"+name+"']").prop("id");
				var Y = $('#'+eleId).offset().top-100;
				$("body,html").animate({scrollTop: Y}, 500);
				if(text!=null && text!=""){
					layer.msg("请填写必填项 "+text, {icon: 2});
				}else{
					layer.msg("请填写必填项", {icon: 2});
				}
				$("[name='"+name+"']").focus();
				flag = false; 
				break;
			}
		}
		return flag;
	},
};