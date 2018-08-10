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
    	var index = layer.open({
    	    type: 2,
    	    title: '选择人员',
    	    shadeClose: true,
    	    shade: 0.3,
    	    area: ['620px', '480px'],
    	    content: common.chooseUserPath(elementId, isSingle),
    	    success: function (layero, lockIndex) {
    	        var body = layer.getChildFrame('body', lockIndex);
    	        body.find('button#close').on('click', function () {
    	            layer.close(lockIndex);
    	        });
    	    }
    	}); 
    	layer.style(index, {
        	zoom:1.1
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
    	layer.style(index, {
        	zoom:1.1
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
        layer.style(index, {
        	zoom:1.1
        });
    },
    //选择数据字典分类的路径
    chooseDictionaryPath:function(id) {
    	return common.getPath() + "/sysDictionary/selectDictionary?elementId=" + id; 
    },
    chooseDictionary:function(elementId){
    	var index = layer.open({
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
    	layer.style(index, {
        	zoom:1.1
        });
    },
    //选择数据字典内容的路径
    chooseDicDataPath:function(id,dicUid) {
    	return common.getPath() + "/sysDictionary/selectDicData?elementId=" + id +"&dicUid="+dicUid; 
    },
    chooseDicData:function(elementId,dicUid){
    	var index=layer.open({
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
    	layer.style(index, {
        	zoom:1.1
        });
    },
    //选择部门的路径
    chooseDepartPath:function(id) {
    	return common.getPath() + "/sysDepartment/chooseDepartment?elementId=" + id ; 
    },
    chooseDepart:function(elementId){
    	var index = layer.open({
            type: 2,
            title: '选择部门',
            shadeClose: true,
            shade: 0.3,
            area: ['400px', '550px'],
            content: common.chooseDepartPath(elementId),
            success: function(layero, lockIndex) {
            	var body = layer.getChildFrame('body', lockIndex);
            	body.find('button#close').on('click', function () {
    	            layer.close(lockIndex);
    	        });
            }
        }); 
    	layer.style(index, {
        	zoom:1.1
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
	//初始化时间控件
	initTime:function(){
		var dateInput = $("#formSet").find(".date");
		dateInput.prop("readonly", true);
		layui.use(['laydate'], function () {
			var laydate = layui.laydate;
			dateInput.each(function () {
				$(this)[0].type = "text";
				var isDatetime = $(this).attr("date_type");
				var dateType = "date";
				if(isDatetime=="true"){
					dateType = "datetime";
				}
				var dateInputId = $(this).prop("id");
				// 日期
				laydate.render({
					elem: "#"+dateInputId,
					trigger: 'click',
					type: dateType,
					position: 'fixed',
					done:function(value){
						$("#"+dateInputId).val(value);
						$("#"+dateInputId).trigger("change");
					}
				});
			});
		});
	},
	//抽取页面中动态表单的数据
	getDesignFormData:function(){
		var inputArr = $("#formSet .form-sub input");
		var textareaArr = $("#formSet .form-sub textarea");
		var tableArr = $("#formSet .data-table");
		var uploadArr = $("#formSet .loc_div");
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
						if($("[name='" + name + "']").val()==null || $("[name='" + name + "']").val()==""){
							break;
						}
						var value = $("[name='" + name + "']")
							.val().trim();
						textJson = "\"" + name
							+ "\":{\"value\":\"" + value
							+ "\"}";
						break;
					}else if($(inputArr[i]).attr("choose-type")=="choose_user"){
						var name = $(inputArr[i]).attr("name");
						if(name==null || name==""){
							break;
						}
						var userNameArr = $(inputArr[i]).val().trim();
						var userIdArr = $(inputArr[i]).parent().find("input[type='hidden']")
									.val().trim().replace(";","");
						console.log(userNameArr);
						console.log(userIdArr);
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
					}else if($(inputArr[i]).attr("title")=="choose_depart"){
						var name = $(inputArr[i]).attr("name");
						if(name==null || name==""){
							break;
						}
						var departName = $(inputArr[i]).val().trim();
						var departNo = $(inputArr[i]).parent().find("input[type='hidden']")
									.val().trim();
						textJson = "\"" + name + "\":{\"value\":\""
							+ departNo + "\",\"description\":\"" + departName +"\"}";
						break;
					}
				}
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
				case "tel":{
					var name = $(inputArr[i]).attr("name");
					if(name==null || name==""){
						break;
					}
					var value = $("[name='" + name + "']")
						.val().trim();
					if(value=="" || value==null || isNaN(value)){
						textJson = "\"" + name + "\":{\"value\":\"\"}";
					}else{
						textJson = "\"" + name + "\":{\"value\":"
						+ value + "}";
					}
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
		
		for(var i=0;i<tableArr.length;i++){
			var tableObj = $(tableArr[i]);
			var tableName = tableObj.attr("name");
			var trArr = tableObj.find("tbody tr");
			var tableJson = "\""+tableName+"\":{\"value\":[";
			trArr.each(function(trIndex){
				var trObj = $(this);
				var tdArr = trObj.find("td");
				tableJson += "{";
				tdArr.each(function(tdIndex){
					if(tdIndex!=tdArr.length-1){
						var tdName = $(this).data("label");
						if(tdName!="" && tdName!=null && $(this).find("input").length>0){
							var tdValue = $(this).find("input").val();
							if(tdValue!="undefined"){
								var tdInputType = $(this).find("input").attr("type");
								if(tdInputType=="number" || tdInputType=="tel"){
									if(tdValue=="" || tdValue==null || isNaN(tdValue)){
										tableJson += "\""+tdName+"\":\"\",";
									}else{
										tableJson += "\""+tdName+"\":"+tdValue+",";
									}
								}else{
									if(tdValue!=null && tdValue!=""){
										tableJson += "\""+tdName+"\":\""+tdValue+"\",";
									}
								}
							}
						}//end tdName!=null
					}
				});
				console.log(tableJson);
				tableJson = common.removeJsonStrComma(tableJson);
				tableJson += "}";
				if(trIndex!=trArr.length-1){
					tableJson += ",";
				}
			});
			tableJson += "],\"type\":\"table\"}";
			if(i!=tableArr.length-1){
				tableJson += ",";
			}
			json += tableJson;
			console.log("表格数据:"+tableJson);
		}
		
		for(var i=0;i<uploadArr.length;i++){
			var uploadObj = $(uploadArr[i]);
			console.log(uploadObj.attr("id"));
			var name = uploadObj.attr("id").replace("_loc","");
			var value = "";
			var aObj = uploadObj.find("a");
			var uploadJson = "\"" + name + "\":{\"value\":[";
			aObj.each(function(){
				var aHref= $(this).attr("href");
				uploadJson += "\""+aHref+"\",";
			});
			uploadJson = common.removeJsonStrComma(uploadJson);
			uploadJson += "],\"type\":\"upload\"},";
			if (json.indexOf(uploadJson) == -1) {
				json += uploadJson;
			}
		}
		
		//获得最后一位字符是否为","
		var charStr = json.substring(json.length - 1,
			json.length);

		if (charStr == ",") {
			json = json.substring(0, json.length - 1);
		}
		json += "}";
		console.log("表单数据："+json);
		json = json.replace(/\t/g,"");
		return json;
	},
	//去除json字符串后多余的逗号
	removeJsonStrComma:function(json){
		if (json.substring(json.length - 1,json.length) == ",") {
			json = json.substring(0, json.length - 1);
			common.removeJsonStrComma(json);
		}
		return json;
	},
	//传入表单json数据给表单组件赋值
	giveFormSetValue:function(jsonStr){
		console.log(jsonStr);
		var json = JSON.parse(jsonStr)
		for(var name in json){
			var paramObj = json[name];
			var type = paramObj.type;
			if(type=="table"){
				var valueArr = paramObj.value;
				var trObj = $("[name='"+name+"']").find("tbody tr").html();
				$("[name='"+name+"']").find("tbody").html("");
				for(var i=0;i<valueArr.length;i++){
					$("[name='"+name+"']").find("tbody").append("<tr>"+trObj+"</tr>");
					var valueObj = valueArr[i];
					var tdArr = $("[name='"+name+"']").find("tbody tr:eq("+i+")").find("td");
					tdArr.each(function(index){
						if(index!=tdArr.length-1){
							var key = $(this).data("label");
							$(this).find("input").val(valueObj[key]);
						}
					});
				}
			}else if(type=="upload"){
				var valueArr = paramObj.value;
				for(var i=0;i<valueArr.length;i++){
					var valueObj = valueArr[i];
					if(valueObj!=null && valueObj!=""){
						var fileName = "图片附件";
						if(valueObj.lastIndexOf("/")!=-1){
							fileName = valueObj.substring(valueObj.lastIndexOf("/")+1);
						}
						var imgHtml = '<div style="display:inline;margin-right:20px;">'
			    	  		+'<img id="img_'+i+'" style="width:400px;" alt="'+fileName+'" src="'+valueObj+'" class="layui-upload-img">'
			    	  		+'</div>';
						$("#"+name).append(imgHtml);//预览图放置区
						var fileNameHtml = '<div><a style="color:#1E9FFF;" href="'
			        		+ valueObj +'" onclick="imgUrlClick(event);">'
			        		+ fileName +' <i class="layui-icon">&#xe64c;</i></a> '
			        		+'</div>';  
						$("#"+name+"_loc").find(".fileList").append(fileNameHtml);//文件名放置区
					}
				}
			}
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
								$("[name='"+name+"']").parent().find("input[class='value_id']").val(value);
								$("[name='"+name+"']").parent().find("input[class='value_code']").val(id);
								break;
							}else if($("[name='"+name+"']").attr("title")=="choose_depart"){
								var value = paramObj["value"];
								var description = paramObj["description"];
								$("[name='"+name+"']").val(description);
								$("[name='"+name+"']").parent().find("input[type='hidden']").val(value);
								break;
							}/*else if($("[name='"+name+"']").attr("class")=="xm-hide-input"){
								console.log("xm-hide-input");
								var value = paramObj["value"];
								var arr = value.split(",");
								formSelects.value(name, arr);    
							}*/
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
				case "SELECT":{
					if($("[name='"+name+"']").attr("is-multi")=="true"){
						console.log(name);
						var value = paramObj["value"];
						var arr = value.split(",");
						$("[name='"+name+"']").attr("xm-select",name);
						formSelects.value(name, arr); 
						break;
					}
				}
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
						$(this).prev().find(".tip_span").remove();
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
					case "fieldSkipJsonStr":{
						common.fieldSkipPermission(perJsonStr);
						break;
					}
					case "titleSkipJsonStr":{
						common.titleSkipPermission(perJsonStr);
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
				if(tagType=="radio" || tagType=="checkbox"){
					$("[name='"+name+"']").parent().css("display","none");
					$("[name='"+name+"']").parent().prev().css("display","none");
					$("[name='"+name+"']").parent().prev().find(".tip_span").remove();
					continue;
				}else if($("[name='"+name+"']").attr("data-title")=="img_upload"){
					$("[name='"+name+"']").parent().parent().parent().css("display","none");
					$("[name='"+name+"']").parent().parent().parent().next().css("display","none");
					continue;
				}else{
					$("[name='"+name+"']").parent().css("display","none");
					$("[name='"+name+"']").parent().prev().css("display","none");
					$("[name='"+name+"']").parent().prev().find(".tip_span").remove();
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
			return;
		}
		if(tagType=="radio"){
			$("[name='"+name+"']").attr("display","none");
			var title = $("[name='"+name+"']:checked").attr("title");
			if($("[name='"+name+"']:checked").parent().find(".radio_value").length==0){
				$("[name='"+name+"']:checked").parent().append("<span class='radio_value' style='margin-left:10px;'>"+title+"</span>");
			}
			return;
		}
		if(tagName=="SELECT"){
			$("[name='"+name+"']").attr("disabled","true");
			$("[name='"+name+"']").next().find("input").attr("disabled","true");
			$("[name='"+name+"']").next().find("input").removeAttr("placeholder");
			$("[name='"+name+"']").next().find(".layui-edge").css("display","none");
			return;
		}
		if($("[name='"+name+"']").attr("title")=="choose_user" 
			|| $("[name='"+name+"']").attr("title")=="choose_value"
			|| $("[name='"+name+"']").attr("title")=="choose_depart"){
			$("[name='"+name+"']").parent().find("i").css("display","none");
			$("[name='"+name+"']").css("width","100%");
			return;
		}
		if(className=="layui-input date"){
			$("[name='"+name+"']").attr("disabled","true");
			if($("[name='"+name+"']").val()=="" || $("[name='"+name+"']").val()==null){
				$("[name='"+name+"']").prop("type","text");
			}
			return;
		}
		if($("[name='"+name+"']").attr("data-title")=="img_upload"){
			$("#"+name+"_choose").attr("disabled","true").css("cursor","not-allowed");
			$("#"+name+"_load").attr("disabled","true").css("cursor","not-allowed");
			return;
		}
	},
	//标题字段的可见性、可编辑性控制
	titlePermissionNoPrint:function(json){
		console.log(json);
		for(var name in json){
			var paramObj = json[name];
			var display = paramObj["display"];
			var edit = paramObj["edit"];
			if(edit=="no"){
				console.log($("[name='"+name+"']").prop("tagName"));
				if($("[name='"+name+"']").prop("tagName")=="P"){
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
				}else if($("[name='"+name+"']").prop("tagName")=="TABLE"){
					var tableObj = $("[name='"+name+"']");
					var tdArr = tableObj.find("td");
					tableObj.find("th[col-type='tool']").remove();					
					tableObj.find("td[data-label='操作']").remove();
					var fieldCodeName = "";
					tdArr.each(function(){
						if($(this).find("input").length != 0){
							$(this).find("input").attr("disabled","true");
							var type = $(this).find("input").get(0).getAttribute("type");
							if(type="date"){
								$(this).find("input").get(0).setAttribute("type","text");
							}
						}else{
							$(this).remove();
						}
					});
				}
			}//end edit
			if(display=="none"){
				if($("[name='"+name+"']").prop("tagName")=="P"){
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
				}else if($("[name='"+name+"']").prop("tagName")=="TABLE"){
					$("[name='"+name+"']").css("display","none");
					var talbeTitle = $("[name='"+name+"']").attr("title");
					var pTitle = $("[name='"+name+"']").prev()[0].firstChild.data.trim();
					if(talbeTitle==pTitle){
						$("[name='"+name+"']").prev().css("display","none");
					}
				}
			}//end display
		}
	},
	//普通字段的打印权限控制
	fieldPrintPermission:function(json){
		for(var name in json){
			var paramObj = json[name];
			var print = paramObj["print"];
			var oldPrint = $("[name='"+name+"']").attr("print");
			if(print=="no" && oldPrint!="yes"){
				$("[name='"+name+"']").attr("print","no");
			}else if(print=="yes"){
				$("[name='"+name+"']").attr("print","yes");
			}
		}
		common.printTableP();
	},
	//当表单中某块信息全都不可打印时，title也要不打印
	printTableP:function(){
		var tableArr = $("#formSet").find(".layui-table");
		tableArr.each(function(){
			var tdArr = $(this).find("td");
			var flag = true;
			tdArr.each(function(){
				if($(this).find("p").length==0 && $(this).find("[print='yes']").length!=0){
					flag = false;
				}
			});
			if(flag){
				if($(this).prev().prop("tagName")=="P"){
					var pText = $(this).prev()[0].firstChild.data.trim();
					if($(this).attr("title")==pText){
						$(this).prev().attr("print","no");
					}
				}
				$(this).attr("print","no");
			}else{
				$(this).attr("print","yes");
				$(this).prev().attr("print","yes");
			}
		});
	},
	//标题字段的打印权限控制
	titlePrintPermission:function(json){
		for(var name in json){
			var paramObj = json[name];
			var print = paramObj["print"];
			if(print=="no"){
				$("[name='"+name+"']").attr("print","no");
				if($("[name='"+name+"']").prop("tagName")=="P"){
					var pText = $("[name='"+name+"']")[0].firstChild.data.trim();
					$(".form-sub").each(function(){
						var tableTitle = $(this).attr("title");
						if(tableTitle == pText){
							$(this).attr("print","yes");
							$(this).find("[print='yes']").attr("print","no");
						}
					});
				}
			}else if(print=="yes"){
				$("[name='"+name+"']").attr("print","yes");
				if($("[name='"+name+"']").prop("tagName")=="P"){
					var pText = $("[name='"+name+"']")[0].firstChild.data.trim();
					$(".form-sub").each(function(){
						var tableTitle = $(this).attr("title");
						if(tableTitle == pText){
							$(this).attr("print","yes");
							$(this).find("[print='no']").attr("print","yes");
						}
					});
				}
			}
		}
	},
	//跳过普通字段必填验证权限管理
	fieldSkipPermission:function(json){
		for(var name in json){
			var paramObj = json[name];
			var skip = paramObj["skip"];
			if(skip=="yes"){
				var labelObj = $("[name='"+name+"']").parent().prev();
				console.log(labelObj.find(".tip_span").length);
				if(labelObj.find(".tip_span").length>=1){
					labelObj.find(".tip_span").remove();
				}
			}
		}
	},
	//跳过标题块或者表格必填验证权限管理
	titleSkipPermission:function(json){
		for(var name in json){
			var paramObj = json[name];
			var skip = paramObj["skip"];
			if(skip=="yes"){
				if($("[name='"+name+"']").prop("tagName")=="P"){
					var labelObj = $("[name='"+name+"']").next();
					if(labelObj.find(".tip_span").length>=1){
						labelObj.find(".tip_span").remove();
					}
				}
			}
		}
	},
	//验证动态表单必填项
	validateFormMust:function(id){
		var mustObjArr = $("#formSet table:visible").find(" .tip_span");
		var value = "";
		var name = "";
		var flag = true;
		for(var i=0;i<mustObjArr.length;i++){
			var mustObj = $(mustObjArr[i]);
			if(mustObj.css("display")=="none"){
				continue;
			}
			var inputObj = $(mustObj.parent().parent().next().find("input")[0]);
			var selectObj = $(mustObj.parent().parent().next().find("select")[0]);
			var textareaObj = $(mustObj.parent().parent().next().find("textarea")[0]);
			var text = mustObj.parent().parent().find("label").text().replace("*","").replace("：","");
			if(inputObj.length == 1){
				if(inputObj.is(":hidden")){
					continue;
				}
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
				if(textareaObj.is(":hidden")){
					continue;
				}
				value = textareaObj.val();
				name = textareaObj.attr("name");
			}else if(selectObj.length == 1){
				if(selectObj.is(":hidden")){
					continue;
				}
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
				var Y = $('#'+eleId).offset().top-170;
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
	//验证动态表单正则
	validateRegx:function(){
		var flag = true;
		$("#formSet table:visible").find("input").each(function () {
			if ($(this).attr("regx") != null && $(this).attr("regx") != "" &&
				$(this).val() != null && $(this).val() != "") {
				reg = new RegExp($(this).attr("regx").trim(), "g");
				if (!reg.test($(this).val())) {
					var regxCue = $(this).attr("regx_cue");
					$(this).focus();
					if (regxCue != null && regxCue != "") {
						layer.msg(regxCue, {
							icon: 2
						});
					} else {
						layer.msg("输入框的值与正则表达式不匹配", {
							icon: 2
						});
					}
					var Y = $(this).offset().top-190;
					$("body,html").animate({scrollTop: Y}, 500);
					flag = false;
				}
			} //end if
		});
		return flag;
	},
	//省市区再次赋值的问题
	againSetValue:function(jsonStr){
		var json = JSON.parse(jsonStr);
		var province = "";
		var city = "";
		var county = "";
		for (var name in json) {
			var paramObj = json[name];
			var tagName = $("[name='" + name + "']").prop("tagName");
			if (name == "province") {
				province = paramObj["value"];
			} else if (name == "city") {
				city = paramObj["value"];
			} else if (name == "county") {
				county = paramObj["value"];
			} else {
				continue;
			}
		}
		var cityJson = {
			province: province,
			city: city,
			county: county
		};
		return cityJson;
	}
};

function datetimeFormat_1(longTypeDate){  
    var datetimeType = "";  
    var date = new Date();  
    date.setTime(longTypeDate);  
    datetimeType+= date.getFullYear();   //年  
    datetimeType+= "-" + getMonth(date); //月   
    datetimeType += "-" + getDay(date);   //日  
    datetimeType+= "&nbsp;&nbsp;" + getHours(date);   //时  
    datetimeType+= ":" + getMinutes(date);      //分
    datetimeType+= ":" + getSeconds(date);      //分
    return datetimeType;
} 
//返回 01-12 的月份值   
function getMonth(date){  
    var month = "";  
    month = date.getMonth() + 1; //getMonth()得到的月份是0-11  
    if(month<10){  
        month = "0" + month;  
    }  
    return month;  
}  
//返回01-30的日期  
function getDay(date){  
    var day = "";  
    day = date.getDate();  
    if(day<10){  
        day = "0" + day;  
    }  
    return day;  
}
//返回小时
function getHours(date){
    var hours = "";
    hours = date.getHours();
    if(hours<10){  
        hours = "0" + hours;  
    }  
    return hours;  
}
//返回分
function getMinutes(date){
    var minute = "";
    minute = date.getMinutes();
    if(minute<10){  
        minute = "0" + minute;  
    }  
    return minute;  
}
//返回秒
function getSeconds(date){
    var second = "";
    second = date.getSeconds();
    if(second<10){  
        second = "0" + second;  
    }  
    return second;  
}
