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
    	    area: ['600px', '540px'],
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
		var inputArr = $("#formSet table input");
		var textareaArr = $("#formSet table textarea");
		var control = true; //用于控制复选框出现重复值
		var checkName = ""; //用于获得复选框的class值，分辨多个复选框
		var json = "{";
		for (var i = 0; i < inputArr.length; i++) {
			var type = $(inputArr[i]).attr("type");
			var textJson = "";
			var checkJson = "";
			switch (type) {
				case "text": {
					if ($(inputArr[i]).prop("class") == "layui-input layui-unselect") {
						var name = $(inputArr[i]).parent()
							.parent().prev().prop("name");
						var value = $("[name='" + name + "']")
							.val();
						textJson = "\"" + name
							+ "\":{\"value\":\"" + value
							+ "\"}";
						break;
					}
				}
					;
				case "tel":
					;
				case "date":
					;
				case "textarea": {
					var name = $(inputArr[i]).attr("name");
					var value = $("[name='" + name + "']")
						.val();
					textJson = "\"" + name + "\":{\"value\":\""
						+ value + "\"}";
					break;
				}
				case "radio": {
					var name = $(inputArr[i]).attr("name");
					var radio = $("[name='" + name + "']")
						.parent().parent().find(
							"input:radio:checked");
					textJson = "\"" + name + "\":{\"value\":\""
						+ radio.attr("id") + "\"}";
					break;
				}
				case "checkbox": {
					var name = $(inputArr[i]).attr("name");
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
						control = false;
						checkJson += "\"" + checkName
							+ "\":{\"value\":[";
						for (var j = 0; j < checkbox.length; j++) {
							if (j == checkbox.length - 1) {
								checkJson += "\""
									+ $(checkbox[j]).attr(
										"id") + "\"";
							} else {
								checkJson += "\""
									+ $(checkbox[j]).attr(
										"id") + "\",";
							}
						}
						checkJson += "]},";
					}

					json += checkJson;
					break;
				}
			}//end switch
			textJson += ",";
			if (json.indexOf(textJson) == -1) {
				json += textJson;
			}
		}

		for(var i=0;i<textareaArr.length;i++){
			var name = $(textareaArr[i]).attr("name");
			var value = $("[name='" + name + "']")
				.val();
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
		return json;
	},
};

