var form = null;
var view = null;
var layedit = null;
var laydate = null;
var upload = null;
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
				|| column.find(".subDiv").find("div[title='choose_depart']").length != 0
				|| column.find(".subDiv").find("div[title='img_upload']").length != 0) {
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
									var layKey = _getRandomString(2);
									trHtml += '<input type="date" class="layui-input date" id="date_'+layKey+'" lay-key="'+layKey+'"/>';
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
					labelDivObj.find("span").remove();
					labelDivObj.find("label").append('<span class="tip_span"> *</span>：');
					var labelDivCol = labelDivObj.attr("col");
					var subDivObj = column.find(".subDiv div[title='choose_user']");
					subDivObj.find("input[title='choose_user']").attr("choose-type","choose_user");
					var subDivCol = subDivObj.attr("col");
					var labelHtml = $(labelDivObj).html();
					var subDivId = $(subDivObj).attr("id");
					$(subDivObj).find("span").remove();
					if(subDivCol<=3){
						chooseInputWidth.push("80%");
					}else if(subDivCol < 4) {
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
					labelDivObj.find("span").remove();
					labelDivObj.find("label").append('<span class="tip_span"> *</span>：');
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
					labelDivObj.find("span").remove();
					labelDivObj.find("label").append('<span class="tip_span"> *</span>：');
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
				}else if(column.find(".subDiv").find("div[title='img_upload']").length != 0){
					var preViewImgId = column.find(".subDiv").find("div[title='img_upload']").attr("name");
					formHtml += '<td col="1" class="td_title" stylt="width:120px;"><label>缩略图展示</label></td>'
							+'<td col="11" class="td_sub">'
							+'<blockquote class="layui-elem-quote layui-quote-nm" style="margin-top: 10px;">'
							+'<div class="layui-upload-list" data-title="img_upload" id="'+preViewImgId+'"></div>'
							+'</blockquote></td></tr>'
							+'<tr>'
							+'<td col="1" class="td_title" stylt="width:120px;">附件</td>'
							+'<td col="11" class="td_sub">'
								+'<div class="loc_div" id="'+preViewImgId+'_loc" style="float: left;width: 50%;min-height: 85px;border: 1px solid #e6e6e6;">'
								+'<h5 style="margin: 5px 5px 10px 5px;">文件名称：<span style="color:red">(PS:鼠标右击文件名复制链接并用浏览器打开，即可浏览或下载图片)</span></h5>'
								+'<div style="margin:10px" class="fileList"></div>'
								+'</div>'
								+'<div class="btn_div" id="'+preViewImgId+'_btn" style="float: left;margin-left: 30px;">'
								+'<button class="layui-btn layui-btn-primary" id="'+preViewImgId+'_choose" style="margin-bottom: 10px;">选择文件</button><br/>'
								+'<button class="layui-btn layui-btn-primary" id="'+preViewImgId+'_load">开始上传</button>'
								+'</div></td>'
							+'</tr>';
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

						labelDivObj.find("span").remove();
						labelDivObj.find("label").append('<span class="tip_span"> *</span>：');
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
					
					labelDivObj.find("span").remove();
					labelDivObj.find("label").append('<span class="tip_span"> *</span>：');

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

		/*var dateInput = view.find(".date");
		dateInput.prop("readonly", true);
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
		});*/

		form.render();
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
	var isSingle = $(obj).parent().find("input[type='hidden']").attr("single");
	if(isSingle!="" && isSingle!=null){
		common.chooseUser(hideId, isSingle);
	}else{
		common.chooseUser(hideId, 'false');
	}
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
	var layKey = _getRandomString(2);
	$(obj).parent().parent().parent().append("<tr>" + trHtml + "</tr>");
	$(obj).parent().parent().parent().find("tr:last").find(".layui-input").val("");
	$(obj).parent().parent().parent().find("tr:last").find(".date").prop("id", "date_" + layKey).attr("lay-key", layKey);
	$(obj).parent().parent().parent().find("input[type='tel']").desNumber();

	var dateInput = $(obj).parent().parent().parent().find(".date");
	if ($(window).width() < 568) {
		dateInput.attr("type", "text");
	}
	layui.use(['laydate'], function () {
		laydate = layui.laydate;
		dateInput.each(function () {
			var dateInputId = $(this).prop("id");
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
		if(/[^1234567890.]/.test(this.value)){
			this.value = this.value.replace(/[^1234567890.]/g,"");
		}
		this.value = this.value.replace(/[^1234567890.]/g,"");
	});
	this.bind("blur", function () {
		if (this.value.slice(-1) == ".") {
			this.value = this.value.slice(0, this.value.length - 1);
		}else if(/[^1234567890.]/.test(this.value)){
			this.value = this.value.replace(/[^1234567890.]/g,"");
		}else if(/^0+\d+(\.\d*)*$/.test(this.value)){
			this.value = this.value.replace(/^0+/,"");
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