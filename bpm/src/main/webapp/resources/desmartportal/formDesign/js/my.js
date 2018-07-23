var form = null;
var view = null;
/*动态表单渲染js*/
$(function () {
	drawPage();
	$("#formSet").css("display", "block");
});

//添加关联子表单的内容
function addPublicFormContent(view) {
	var divArr = view.find(".subDiv div[title='choose_form']");
	divArr.each(function () {
		var divObj = $(this);
		var publicFormUid = divObj.find("input[type='hidden']").val();
		$.ajax({
			url: common.getPath() + "/publicForm/queryFormByFormUid",
			method: "post",
			async: false,
			data: {
				formUid: publicFormUid
			},
			success: function (result) {
				if (result.status == 0) {
					divObj.parent().parent().parent().removeClass("row-fluid");
					divObj.parent().parent().parent().html(result.data.publicFormWebpage);
					var publicFormHtml = divObj.parent().parent().parent().find(".container-fluid").html();
					divObj.parent().parent().parent().html(publicFormHtml);
				} else {
					layer.alert("子表单内容读取失败");
					divObj.parent().parent().parent().html("");
				}
			}
		});
	});
}

var chooseInputWidth = new Array();
//渲染页面的方法
function drawPage() {
	var tableHead = '<div class="table_container"><table class="layui-table form-sub basic_information" lay-skin="nob">' + '<tbody>';
	var formHtml = tableHead;
	view = $(".container-fluid");
	//将关联子表单内容添加进来
	addPublicFormContent(view);
	view.addClass("layui-form");
	var rowObj = view.find(".row-fluid");
	rowObj.each(function () {
		var colObj = $(this).find(".column");
		var flag = true;
		var isContinue = false;
		formHtml += '<tr>';
		for (var i = 0; i < colObj.length; i++) {
			var column = $(colObj[i]);
			if (column.find(".subDiv").length == 0
				&& column.find(".labelDiv").length == 0) {
				flag = false;
				if (column.find(".title_p").length != 0) {
					//表单块标题
					column.find("p").addClass("title_p");
					column.find("p").removeAttr("title");
					column.find("p").append('<i class="layui-icon arrow" style="margin-left:10px;" onclick="showTable(this)">&#xe625;</i>');
					pHtml = column.html();
					var pText = column.find("p")[0].firstChild.data.trim();
					formHtml = formHtml.substring(0, formHtml.length - 4);
					formHtml += "</tbody></table><div>";
					formHtml += "<div class='table_container'>"+pHtml;
					formHtml += '<table class="layui-table form-sub basic_information" lay-skin="nob" title=' + pText + '>' + '<tbody>';
				} else {
					continue;
				}
			} else if (column.find(".subDiv").length != 0 && column.find(".labelDiv").length == 0
				|| column.find(".subDiv").find("div[title='choose_user']").length != 0
				|| column.find(".subDiv").find("div[title='choose_value']").length != 0
				|| column.find(".subDiv").find("div[title='choose_depart']").length != 0) {
				//表单中的填写说明与数据表格
				var subDivObj = column.find(".subDiv");
				var tableObj = subDivObj.find("table");
				var pObj = subDivObj.find("p");
				var hiddenObj = subDivObj.find("input[title='hidden_text']");
				if (tableObj.length != 0) {
					flag = false;
					formHtml = formHtml.substring(0, formHtml.length - 4);
					formHtml += "</tbody></table></div>";
					tableObj.find("thead tr").append("<th col-type='tool'>操作</th>");
					var isleading = tableObj.attr("isleading");
					if(isleading=="true"){
						var thObjArr = tableObj.find("thead th");
						var id = tableObj.attr("id")+_getRandomString(2);
						var trHtml = '<tr>';
						for (var i = 0; i < thObjArr.length; i++) {
							var thObj = $(thObjArr[i]);
							if(thObj.attr("col-type")=="tool"){
								trHtml += '<td data-label="' + thObj.text().trim() + '" style="max-width:180px;min-width:90px;">'
									+'<button class="layui-btn layui-btn-primary" onclick="downTemplateFile();" style="width:45%;padding:0 10px;" type="button">导出模板文件</button>'
									+'<button class="layui-btn layui-btn-primary load_data_file" id="'+id+'" style="width:45%;padding:0 10px;" type="button">导入数据文件</button></td>';
							}else{
								trHtml += '<td data-label="' + thObj.text().trim() + '"></td>';
							}
						}
					}else{
						var thObjArr = tableObj.find("thead th");
						var trHtml = '<tr>';
						for (var i = 0; i < thObjArr.length; i++) {
							var thObj = $(thObjArr[i]);
							trHtml += '<td data-label="' + thObj.text().trim() + '">';
							switch (thObj.attr("col-type")) {
								case "text": {
									trHtml += '<input type="text" class="layui-input"/>';
									break;
								}
								case "number": {
									trHtml += '<input type="tel" class="layui-input"/>';
									break;
								}
								case "date": {
									trHtml += '<input type="date" class="layui-input date" id="date_1"/>';
									break;
								}
								case "tool": {
									trHtml += '<i class="layui-icon" title="添加新的一行" onclick="addDataRow(this)">&#xe654;</i>' +
										'<i class="layui-icon" title="删除本行" onclick="removeDataRow(this)">&#xe640;</i></td>'
									break;
								};
							}
							trHtml += '</td>';
						}
					}
					
					trHtml += '</tr>';
					var tableLabel = tableObj.attr("table-label");
					tableObj.append("<tbody>" + trHtml + "</tbody>");
					formHtml += "<div class='table_container'><p class='title_p'>" + tableLabel
						+ "<i class='layui-icon arrow' style='margin-left:10px;' onclick='showTable(this)'>&#xe625;</i></p>"
						+"<div style='padding:0px 20px 5px;margin-bottom:10px;'><table class='layui-table data-table basic_information' name='"
						+ tableObj.attr("name") + "' title='" + tableLabel + "'>" + tableObj.html()
						+ "</table></div></div>";
					formHtml += tableHead;
				} else if (pObj.length != 0) {
					if (pObj.attr("title") == "table_title") {
						//表单块标题
						column.find("p").addClass("title_p");
						column.find("p").removeAttr("title");
						column.find("p").append('<i class="layui-icon arrow" style="margin-left:10px;" onclick="showTable(this)">&#xe625;</i>');
						pHtml = column.find(".subDiv").html();
						var pText = column.find("p")[0].firstChild.data.trim();
						flag = false;
						if (column.find(".title_p").length != 0) {
							formHtml = formHtml.substring(0, formHtml.length - 4);
							formHtml += "</tbody></table></div>";
							formHtml += "<div class='table_container'>"+pHtml;
							formHtml += '<table class="layui-table form-sub basic_information" lay-skin="nob" title=' + pText + '>' + '<tbody>';
						} else {
							continue;
						}
					} else {
						var subDivCol = subDivObj.attr("col");
						var subDivRow = subDivObj.attr("row");
						if (subDivRow == 1) {//说明
							var subHtml = subDivObj.html();
							formHtml += '<td class="td_sub_explain" colspan=' + subDivCol + '>' + subHtml + '</td>';
						} else {//表格行的头部
							var subHtml = subDivObj.text();
							formHtml += '<td><p>'+ subHtml + '</p></td>'
							isContinue = true;
						}
					}
				} else if (column.find(".subDiv").find("div[title='choose_user']").length != 0) {
					var labelDivObj = column.find(".labelDiv");
					var labelDivCol = labelDivObj.attr("col");
					var subDivObj = column.find(".subDiv div[title='choose_user']");
					var subDivCol = subDivObj.attr("col");
					var labelHtml = $(labelDivObj).html();
					var subDivId = $(subDivObj).attr("id");
					$(subDivObj).find("span").remove();
					if (subDivCol < 4) {
						chooseInputWidth.push("90%");
					} else {
						chooseInputWidth.push("93%");
					}
					$(subDivObj).append('<i class="layui-icon" title="choose_user" id="' + subDivId + '" onclick="desChooseUser(this);">&#xe612;</i>');
					var subHtml = $(subDivObj).html();
					if (!isNaN(labelDivCol)) {
						formHtml += '<td class="td_title" colspan=' + labelDivCol + ' style="width:120px">' + labelHtml + '</td>';
					}

					if (!isNaN(subDivCol)) {
						formHtml += '<td class="td_sub" colspan=' + subDivCol + '>' + subHtml + '</td>';
					}
				} else if (column.find(".subDiv").find("div[title='choose_value']").length != 0) {
					var labelDivObj = column.find(".labelDiv");
					var labelDivCol = labelDivObj.attr("col");
					var subDivObj = column.find(".subDiv div[title='choose_value']");
					var subDivCol = subDivObj.attr("col");
					var labelHtml = $(labelDivObj).html();
					var subDivId = $(subDivObj).attr("id");
					$(subDivObj).find("span").remove();
					if (subDivCol < 4) {
						chooseInputWidth.push("90%");
					} else {
						chooseInputWidth.push("93%");
					}
					$(subDivObj).append('<i class="layui-icon" title="choose_value" id="' + subDivId + '" onclick="chooseDicData(this);">&#xe615;</i>');
					var subHtml = $(subDivObj).html();
					if (!isNaN(labelDivCol)) {
						formHtml += '<td class="td_title" colspan=' + labelDivCol + ' style="width:120px">' + labelHtml + '</td>';
					}

					if (!isNaN(subDivCol)) {
						formHtml += '<td class="td_sub" colspan=' + subDivCol + '>' + subHtml + '</td>';
					}
				} else if (column.find(".subDiv").find("div[title='choose_depart']").length != 0) {
					var labelDivObj = column.find(".labelDiv");
					var labelDivCol = labelDivObj.attr("col");
					var subDivObj = column.find(".subDiv div[title='choose_depart']");
					var subDivCol = subDivObj.attr("col");
					var labelHtml = $(labelDivObj).html();
					var subDivId = $(subDivObj).attr("id");
					$(subDivObj).find("span").remove();
					if (subDivCol < 4) {
						chooseInputWidth.push("90%");
					} else {
						chooseInputWidth.push("93%");
					}
					$(subDivObj).append('<i class="layui-icon" title="choose_depart" id="' + subDivId + '" onclick="desChooseDepart(this);">&#xe62e;</i>');
					var subHtml = $(subDivObj).html();
					if (!isNaN(labelDivCol)) {
						formHtml += '<td class="td_title" colspan=' + labelDivCol + ' style="width:120px">' + labelHtml + '</td>';
					}

					if (!isNaN(subDivCol)) {
						formHtml += '<td class="td_sub" colspan=' + subDivCol + '>' + subHtml + '</td>';
					}
				}else if(hiddenObj.length!=0){
					var subDivObj = column.find(".subDiv input[title='hidden_text']");
					var subDivId = $(subDivObj).attr("id");
					var subHtml = $(subDivObj).parent().html();
					formHtml += '<td class="td_sub" style="display:none">' + subHtml + '</td>';
				}
			} else {
				//普通组件
				flag = true;
				if (isContinue) {
					column.find(".subDiv").each(function (index) {
						formHtml += '<tr>';
						var labelDivObj = $(column.find(".labelDiv")[index]);
						var labelDivCol = labelDivObj.attr("col");
						var subDivObj = $(column.find(".subDiv")[index]);
						var subDivCol = subDivObj.attr("col");

						labelDivObj.find("span").addClass("tip_span");
						var labelHtml = $(labelDivObj).html();
						var subHtml = "";
						if (subDivObj.find("label").length == 0) {
							if ($(subDivObj).next().prop("class") == "hidden-value") {
								subHtml = $(subDivObj).html() + $(subDivObj).next().html();
							} else {
								subHtml = $(subDivObj).html();
							}
						} else if (subDivObj.find("label").length == 1) {
							subHtml = $(subDivObj).find("label").html();
						} else {
							subDivObj.find("label").each(function () {
								var title = $(this).text();
								$(this).find("input").prop("title", title);
								$(this).html($(this).find("input"));
								subHtml += $(this).html();
							});
						}

						if (!isNaN(labelDivCol)) {
							formHtml += '<td class="td_title" colspan=' + labelDivCol + ' style="width:200px">' + labelHtml + '</td>';
						}

						if (!isNaN(subDivCol)) {
							formHtml += '<td class="td_sub" colspan=' + subDivCol + '>' + subHtml + '</td>';
						}
						if (index != column.find(".subDiv").length - 1) {
							formHtml += '</tr>';
						}
					});
					isContinue = false;
				} else {
					var labelDivObj = column.find(".labelDiv");
					var labelDivCol = labelDivObj.attr("col");
					var subDivObj = column.find(".subDiv");
					var subDivCol = subDivObj.attr("col");

					labelDivObj.find("span").addClass("tip_span");

					var labelHtml = "";
					var subHtml = "";
					if ($(labelDivObj).next().find(".editor_textarea").length == 1) {
						formHtml = formHtml.substring(0, formHtml.length - 4);
						formHtml += "</tbody></table></div>";
						labelHtml = "<p class='title_p'>" + $(labelDivObj).text() + "</p>";
						subHtml = "<div class='layui-form'>" + $(subDivObj).html() + "</div>";
						formHtml += labelHtml;
						formHtml += subHtml;
						continue;
					} else {
						labelHtml = $(labelDivObj).html();
						if (subDivObj.find("label").length == 0) {
							if ($(subDivObj).next().prop("class") == "hidden-value") {
								subHtml = $(subDivObj).html() + $(subDivObj).next().html();
							} else {
								subHtml = $(subDivObj).html();
							}
						} else if (subDivObj.find("label").length == 1) {
							subHtml = $(subDivObj).find("label").html();
						} else {
							subDivObj.find("label").each(function () {
								var title = $(this).text();
								$(this).find("input").prop("title", title);
								$(this).html($(this).find("input"));
								subHtml += $(this).html();
							});
						}

						if (!isNaN(labelDivCol)) {
							formHtml += '<td class="td_title" colspan=' + labelDivCol + ' style="width:120px">' + labelHtml + '</td>';
						}

						if (!isNaN(subDivCol)) {
							formHtml += '<td class="td_sub" colspan=' + subDivCol + '>' + subHtml + '</td>';
						}
					} //end if editor
				}//
			} //end if column
		} //end for
		if (flag) {
			formHtml += "</tr>";
		}
	}); //end rowObj for	
	formHtml += "</tbody></table></div>";
	view.html(formHtml);
	
	var explainArr = $(".td_sub_explain");
	explainArr.each(function () {
		$(this).find("p").prepend('<img src="' + common.getPath() + '/resources/desmartportal/images/top_star.png" class="star_img">');
	});

	//给动态表单中的下拉列表赋值
	var selectArr = $("select[data_source='数据字典拉取']");
	selectArr.each(function () {
		getDataToSelect(this, $(this).attr("database_type"));
	});

	view.find("input[type='tel']").desNumber();
	
	//给选人组件调整样式
	var chooseUserInputArr = view.find("i[title='choose_user']").parent().find("input[type='text']");
	chooseUserInputArr.each(function (index) {
		$(this).css({ "display": "inline", "width": chooseInputWidth[index] }).attr("readonly", true);
	});

	//给选择部门调整样式
	var chooseUserInputArr = view.find("i[title='choose_depart']").parent().find("input[type='text']");
	chooseUserInputArr.each(function (index) {
		$(this).css({ "display": "inline", "width": chooseInputWidth[index] }).attr("readonly", true);
	});

	//给弹框选值调整样式
	var chooseValueInputArr = view.find("i[title='choose_value']").parent().find("input[type='text']");
	chooseValueInputArr.each(function (index) {
		$(this).css({ "display": "inline", "width": chooseInputWidth[index] }).attr("readonly", true);
	});

	layui.use(['form', 'layedit', 'laydate', 'upload'], function () {
		form = layui.form, layer = layui.layer, layedit = layui.layedit
				, laydate = layui.laydate,upload = layui.upload;

		view.find("input[type='radio']").each(function(){
			$(this).attr("lay-filter",$(this).attr("name"));
			var filter = $(this).attr("lay-filter");
			form.on('radio('+filter+')',function(){
				$(this).trigger("click");
			});
		});
		
		view.find("input[type='checkbox']").each(function(){
			$(this).attr("lay-filter",$(this).attr("name"));
			var filter = $(this).attr("lay-filter");
			form.on('checkbox('+filter+')',function(){
				$(this).trigger("click");
			});
		});
		
		view.find("select").each(function(){
			$(this).attr("lay-filter",$(this).attr("name"));
			var id = $(this).prop("id");
			var filter = $(this).attr("lay-filter");
			form.on('select('+filter+')',function(){
				$("#"+id).trigger("change");
			});
		});
		
		form.render();

		view.find(".editor_textarea").each(function () {
			var editorId = $(this).prop("id");
			var editor = layedit.build(editorId, {
				tool: [
					'strong' //加粗
					, 'italic' //斜体
					, 'underline' //下划线
					, 'del' //删除线

					, '|' //分割线

					, 'left' //左对齐
					, 'center' //居中对齐
					, 'right' //右对齐
				],
				height: 100
			});
		});

		var dateInput = view.find(".date");
		if ($(window).width() < 568) {
			dateInput.attr("type", "text");
		}
		dateInput.prop("readonly", true);
		dateInput.each(function () {
			$(this).next().remove();
			var dateInputId = $(this).prop("id");
			// 日期
			laydate.render({
				elem: '#' + dateInputId,
				trigger: 'click'
			});
		});
	});
}

function showTable(obj) {
	var pText = $(obj).parent()[0].firstChild.data.trim();
	var tableText = $(obj).parent().next().attr("title");
	if (pText == tableText) {
		$(obj).parent().next().slideToggle(100);
	}
}

/**
 * 根据下拉列表的组件对象，和数据字典的id，动态生成下拉组件
 */
function getDataToSelect(obj, dicUid) {
	$(obj).children().remove();
	$.ajax({
		url: common.getPath() + "/sysDictionary/listOnDicDataBydicUid",
		method: "post",
		data: {
			dicUid: dicUid
		},
		success: function (result) {
			if (result.status == 0) {
				var dicDataList = result.data;
				for (var i = 0; i < dicDataList.length; i++) {
					var dicDataObj = dicDataList[i];
					var optionObj = '<option value="' + dicDataObj.dicDataCode + '">' + dicDataObj.dicDataName + '</option>';
					$(obj).append(optionObj);
				}
				form.render();
			}
		}
	});
}

//动态选人的方法
function desChooseUser(obj) {
	var hideId = $(obj).parent().find("input[type='hidden']").prop("id");
	common.chooseUser(hideId, 'false');
}

//动态选部门的方法
function desChooseDepart(obj) {
	var hideId = $(obj).parent().find("input[type='hidden']").prop("id");
	common.chooseDepart(hideId);
}

//选择具体数据字典分类的数据内容
function chooseDicData(obj) {
	var elementId = $(obj).parent().find("input[type='hidden']").prop("id");
	var dicUid = $(obj).parent().find("input[type='text']").attr("database_type");
	common.chooseDicData(elementId, dicUid);
}

function addDataRow(obj) {
	var trHtml = $(obj).parent().parent().html();
	var trNum = $(obj).parent().parent().parent().find("tr").length + 1;
	var layKey = parseInt($(obj).parent().parent().find(".date").attr("lay-key")) + 1;
	$(obj).parent().parent().parent().append("<tr>" + trHtml + "</tr>");
	$(obj).parent().parent().parent().find("tr:last").find(".layui-input").val("");
	$(obj).parent().parent().parent().find("tr:last").find(".date").prop("id", "date_" + trNum).attr("lay-key", layKey);
	$(obj).parent().parent().parent().find("input[type='tel']").desNumber();

	var dateInput = $(obj).parent().parent().parent().find(".date");
	if ($(window).width() < 568) {
		dateInput.attr("type", "text");
	}
	dateInput.each(function () {
		var dateInputId = $(this).prop("id");
		layui.use(['laydate'], function () {
			laydate = layui.laydate;
			laydate.render({
				elem: '#' + dateInputId,
				trigger: 'click'
			});
		});
	});
}

function removeDataRow(obj) {
	if ($(obj).parent().parent().parent().find("tr").length > 1) {
		$(obj).parent().parent().remove();
	}
}

// 只能输入数字
jQuery.fn.desNumber = function () {
	this.bind("keypress", function (e) {
		var code = (e.keyCode ? e.keyCode : e.which); // 兼容火狐 IE
		// 火狐下不能使用退格键
		if (e.keyCode == 0x8) {
			return;
		}
		if (this.value.indexOf(".") == -1) {
			return (code >= 48 && code <= 57) || (code == 46);
		} else {
			return code >= 48 && code <= 57
		}
	});
	this.bind("paste", function () {
		return false;
	});
	this.bind("keyup", function () {
		if (this.value.slice(0, 1) == ".") {
			this.value = "";
		}
	});
	this.bind("blur", function () {
		if (this.value.slice(-1) == ".") {
			this.value = this.value.slice(0, this.value.length - 1);
		}else if(/^[\u4e00-\u9fa5]/.test(this.value)){
			this.value = "";
		}else if(/^[a-zA-Z]/.test(this.value)){
			this.value = "";
		}
	});
};


jQuery.fn.number = function () {
	this.bind("keypress", function (e) {
		var code = (e.keyCode ? e.keyCode : e.which); // 兼容火狐 IE
		// 火狐下不能使用退格键
		if (e.keyCode == 0x8) {
			return;
		}
		return code >= 48 && code <= 57
	});
	this.bind("paste", function () {
		return false;
	});
}; 

function _getRandomString(len) {
    len = len || 32;
    var $chars = 'ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678'; // 默认去掉了容易混淆的字符oOLl,9gq,Vv,Uu,I1  
    var maxPos = $chars.length;
    var pwd = '';
    for (i = 0; i < len; i++) {
        pwd += $chars.charAt(Math.floor(Math.random() * maxPos));
    }
    return pwd;
}