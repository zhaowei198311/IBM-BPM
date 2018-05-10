var form = null;

/*动态表单渲染js*/
$(function(){
	alert("123");
	var tableHead = '<table class="layui-table">'
					+'<colgroup>'
					+'<col>'
					+'<col>'
					+'<col>'
					+'<col>'
					+'<col>'
					+'<col>'
					+'<col>'
					+'<col>'
					+'<col>'
					+'<col>'
					+'<col>'
					+'<col>'
					+'</colgroup>'
				+'<tbody>';
	var formHtml = tableHead;
	var view = $(".container-fluid");
	view.addClass("layui-form");
	var rowObj = view.find(".row-fluid");
	rowObj.each(function(){
		var colObj = $(this).find(".column");
		formHtml += '<tr>';
		for(var i=0;i<colObj.length;i++){
			var column = $(colObj[i]);
			if(column.find(".subDiv").length==0 && column.find(".labelDiv").length==0){
				column.find("p").addClass("title_p");
				pHtml = column.html();
				if(column.find("p").length!=0){
					formHtml = formHtml.substring(0,formHtml.length-4);
					formHtml += "</tbody></table>";
					formHtml += pHtml;
					formHtml += tableHead;
				}else{
					continue;
				}
			}else{
				var labelDivObj = column.find(".labelDiv");
				var labelDivCol = $(labelDivObj).attr("col");
				var subDivObj = column.find(".subDiv");
				var subDivCol = $(subDivObj).attr("col");
				
				labelDivObj.find("span").addClass("tip_span");
				
				var labelHtml = "";
				var subHtml = "";
				if($(labelDivObj).next().find(".editor_textarea").length==1){
					formHtml = formHtml.substring(0,formHtml.length-4);
					formHtml += "</tbody></table>";
					labelHtml = "<p class='title_p'>"+$(labelDivObj).text()+"</p>";
					subHtml = "<div class='layui-form'>"+$(subDivObj).html()+"</div>";
					formHtml += labelHtml;
					formHtml += subHtml;
					continue;
				}else{
					labelHtml = $(labelDivObj).html();
					var labelText = $(labelDivObj).text();
					if(subDivObj.find("label").length==0){
						if($(subDivObj).next().prop("class")=="hidden-value"){
							subHtml = $(subDivObj).html()+$(subDivObj).next().html();
						}else{
							subHtml = $(subDivObj).html();
						}
					}else if(subDivObj.find("label").length==1){
						subHtml = $(subDivObj).find("label").html();
					}else{
						subDivObj.find("label").each(function(){
							var title = $(this).text();
							$(this).find("input").prop("title",title);
							$(this).html($(this).find("input"));
							subHtml += $(this).html();
						});
					}
					
					if(!isNaN(labelDivCol)){
						formHtml += '<td class="td_title" colspan='+labelDivCol+'>'+labelHtml+'</td>';
					}
					
					if(!isNaN(subDivCol)){
						formHtml += '<td colspan='+subDivCol+' data-label="'+labelText+'">'+subHtml+'</td>';
					}
				}//end if editor
			}//end if column
		}//end for
		formHtml += "</tr>";
	});
	formHtml += "</tbody></table>";
	view.html(formHtml);
	var userAgent = navigator.userAgent;
	if(userAgent.indexOf("compatible")==-1 && userAgent.indexOf("Edge")==-1){ 
		if(view.find(".layui-table").width()>568){
			var colWidth = view.find(".layui-table").width()/12;
			view.find("col").width(colWidth);
		}
	}	
	view.css("display","block");

	view.find("input[type='tel']").desNumber();
	
	layui.use(['form', 'layedit', 'laydate'], function(){
		  form = layui.form
		  ,layer = layui.layer
		  ,layedit = layui.layedit
		  ,laydate = layui.laydate;
		  
		  form.render();
		  
		  view.find("input[type='text']").each(function(){
			  $(this).blur(function(){
				  if($(this).attr("regx")!=null && $(this).attr("regx")!=""
					&& $(this).val()!=null && $(this).val()!=""){
					  reg = new RegExp($(this).attr("regx"),"g");
					  if(!reg.test($(this).val())){
						  var regxCue = $(this).attr("regxCue");
						  if(regxCue!=null && regxCue!=""){
							  layer.msg(regxCue,{icon:2});
						  }else{
							  layer.msg("输入框的值与正则表达式不匹配",{icon:2});
						  }
					  }
				  }//end if
			  });
		  });
		  
		  view.find(".editor_textarea").each(function(){
			  var editorId = $(this).prop("id");
			  var editor = layedit.build(editorId, {
					  tool: [
						  'strong' //加粗
						  ,'italic' //斜体
						  ,'underline' //下划线
						  ,'del' //删除线
						  
						  ,'|' //分割线
						  
						  ,'left' //左对齐
						  ,'center' //居中对齐
						  ,'right' //右对齐
						],
						height: 100
			  });
		  });
		  
		  var dateInput = view.find(".date");
		  dateInput.each(function(){
			  dateInput.prop("readonly",true);
			  var dateInputId = $(this).prop("id");
			  // 日期
			  laydate.render({
			    elem: '#'+dateInputId
			    ,trigger: 'click'
			  });
		  });
	});
	
	var loadBtn = view.find(".file");
	loadBtn.each(function(){
		var modelHtml = $("#file_load_hide").html();
		view.append(modelHtml);
		view.find(".model").prop("id",$(this).prop("id")+"Model").removeClass("model");
		$("#"+$(this).prop("id")+"Model").find(".layui-upload-drag").prop("id",$(this).prop("id")+"Drag");
		$("#"+$(this).prop("id")+"Model").find(".listAction").prop("id",$(this).prop("id")+"Btn");
		
		$(this).click(function(){
			$("#"+$(this).prop("id")+"Model").css("display","block");
		});
	});
	
	layui.use('upload', function(){
		  var $ = layui.jquery
		  ,upload = layui.upload;
		  
		  var fileCount = 0;
		  var fileModels = view.find(".display_container");
		  fileModels.each(function(){
			  var model = $(this);
			  var btnId = model.prop("id").replace("Model","");
			  var maxFileSize = $("#"+btnId).parent().find(".maxFileSize").val();
			  var maxFileCount = $("#"+btnId).parent().find(".maxFileCount").val();
			  var fileFormat = $("#"+btnId).parent().find(".fileFormat").val();
			  
			  re = new RegExp(",","g");
			  var formatStr = fileFormat.replace(re,"|");
			  var dragId = model.find(".layui-upload-drag").prop("id");
			  // 拖拽上传
			  var demoListView = model.find('.fileList')
			  ,uploadListIns = upload.render({
			    elem: '#'+dragId
			    ,url: 'saveFile.do'//'dhInstanceDoc/loadFile.do'
			    ,auto: false// 不自动上传
			    ,exts: formatStr
			    ,multiple: true
			    ,bindAction: '#'+btnId+"Btn"
			    ,choose: function(obj){   
			          var files = this.files = obj.pushFile(); // 将每次选择的文件追加到文件队列
			          layer.load();
			          // 读取本地文件
			          obj.preview(function(index, file, result){
			        	fileCount++;
			        	if(fileCount>maxFileCount){
			        	    fileCount = maxFileCount;
			          	    layer.msg('文件数量不得超过'+maxFileCount+'个',{icon:2});
			          	    layer.closeAll('loading');
			          	    delete files[index];
			          		return;
			            }
			        	var size = file.size;
			          	if(size > maxFileSize*1024*1024){
			          		layer.msg('文件大小不得超过'+maxFileSize+'M',{icon:2});
			          		layer.closeAll('loading');
			          		delete files[index];
			          		return;
			          	}
			          	if(size == 0){
			          		layer.msg('文件大小不能为空',{icon:2});
			          		layer.closeAll('loading');
			          		delete files[index];
			          		return;
			          	}
			          	
			            var tr = $(['<tr id="upload-'+ index +'">'
			              ,'<td>'+ file.name +'</td>'
			              ,'<td>'+ (file.size/1024/1024).toFixed(2) +'Mb</td>'
			              ,'<td>等待上传</td>'
			              ,'<td>'
			                ,'<button class="layui-btn layui-btn-mini demo-reload layui-hide">重传</button>'
			                ,'<button class="layui-btn layui-btn-mini layui-btn-danger demo-delete">删除</button>'
			              ,'</td>'
			            ,'</tr>'].join(''));
			            
			            // 单个重传
			            tr.find('.demo-reload').on('click', function(){
			              obj.upload(index, file);
			            });
			            
			            // 删除
			            tr.find('.demo-delete').on('click', function(){
			              delete files[index]; // 删除对应的文件
			              tr.remove();
			              fileCount--;
			              uploadListIns.config.elem.next()[0].value = ''; 
			            });
			            
			            demoListView.append(tr);
			            layer.closeAll('loading');
			          });
			        }
			        ,done: function(res, index, upload){
			        	var tr = demoListView.find('tr#upload-'+ index)
			            ,tds = tr.children();
			          if(res.filename == tds.eq(0).text().trim()){ // 上传成功
			            tds.eq(2).html('<span style="color: #5FB878;">上传成功</span>');
			            tds.eq(3).html(''); // 清空操作
			            return delete this.files[index]; // 删除文件队列已经上传成功的文件
			          }
			          this.error(index, upload);
			          model.css("display","none");
			        }
			        ,error: function(index, upload){
			          var tr = demoListView.find('tr#upload-'+ index)
			          ,tds = tr.children();
			          tds.eq(2).html('<span style="color: #FF5722;">上传失败</span>');
			          tds.eq(3).find('.demo-reload').removeClass('layui-hide'); // 显示重传
			        }
			  });
			  
			  document.getElementById(dragId).addEventListener("dragenter", function(e){ 
				    e.stopPropagation(); 
				    e.preventDefault(); 
				}, false);  
			  document.getElementById(dragId).addEventListener("dragover", function(e){ 
				    e.stopPropagation(); 
				    e.preventDefault(); 
				}, false); 
			  document.getElementById(dragId).addEventListener("drop", function(e){ 
				    e.stopPropagation(); 
				    e.preventDefault(); 
				}, false);
		  });
	});

	//getData("PI00-0000-0000-0000-0000-0000-02","3");
	
	//保存表单数据
	//saveData();
});

//隐藏上传文件的模态框
function cancelClick(obj){
	$(obj).parent().parent().parent().css("display","none");
}

// 只能输入数字
jQuery.fn.desNumber = function() {
	this.bind("keypress", function(e) {
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
	this.bind("paste", function() {
		return false;
	});
	this.bind("keyup", function() {
		if (this.value.slice(0, 1) == ".") {
			this.value = "";
		}
	});
	this.bind("blur", function() {
		if (this.value.slice(-1) == ".") {
			this.value = this.value.slice(0, this.value.length - 1);
		}
	});
}; 

/**
 * 根据组件对象的类型给各个组件赋值
 * @param paramObj 组件对象
 * @param id 各个组件的id(单选框为class)
 */
var setValue = function(paramObj,name){
	var tagName = $("[name='"+name+"']").prop("tagName");
	switch(tagName){
		case "input":{
			var tagType = $("[name='"+name+"']").attr("type");
			switch(tagType){
				case "text":;
				case "tel":;
				case "date":{
					$("[name='"+name+"']").val(paramObj["value"]);
					form.render();
					break;
				};
				case "radio":{
					$("[name='"+name+"'][id='"+paramObj["value"]+"']").prop("checked","true");
					form.render();
					break;
				}
				case "checkbox":{
					var valueArr = paramObj["value"];
					for(var value in valueArr){
						$("[name='"+name+"'][id='"+value+"']").prop("checked","true");
					}
					form.render();
					break;
				}
			}
			break;
		};
		case "select":;
		case "textarae":{
			$("[name='"+name+"']").val(paramObj["value"]);
			form.render();
			break;
		}
	}
}

/**
 * 判读组件对象是否可见
 * @param paramObj 组件对象
 * @param id 各个组件的id(单选框为class)
 */
var isDisplay = function(paramObj,name){
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
}

/**
 * 判读组件对象是否可编辑
 * @param paramObj 组件对象
 * @param id 各个组件的id(单选框为class)
 */
var isEdit = function(paramObj,name){
	var edit = paramObj["edit"];
	if(edit=="no"){
		$("[name='"+name+"']").attr("readonly","true");
		var tagName = $("[name='"+name+"']").prop("tagName");
		var tagType = $("[name='"+name+"']").attr("type");
		if(tagType=="radio" || tagType=="checkbox"){
			$("[name='"+name+"']").attr("disabled","true");
		}
		if(tagName=="select"){
			$("[name='"+name+"']").attr("disabled","true");
		}
		if(tagType=="date"){
			$("[name='"+name+"']").attr("disabled","true");
		}
	}
}

/**
 * 根据流程实例Id和环节配置Id获得某环节下的表单数据及配置数据
 * @param processId 流程实例Id
 * @param tacheCofigureId 环节配置Id
 */
var getData = function(processId,tacheConfigureId){
	if(processId!=null && processId!="" 
		&& tacheConfigureId!=null && tacheConfigureId!=""){
		$.ajax({
			url:"form.json",
			method:"post",
			success:function(jsonStr){
				var json = JSON.parse(jsonStr);
				for(var name in json){
					var paramObj = json[name];
					//给各个组件赋值
					setValue(paramObj,name);
					//判断组件是否可见
					isDisplay(paramObj,name);
					//判断组件对象是否可编辑
					isEdit(paramObj,name);
					$("body").css("display","block");
				}
			}
		});
		/*$.ajax({
			url:"getFormDataByProcessId.do",
			method:"post",
			data:{"processInstanceId":processId,
				"tacheConfigureId":tacheConfigureId},
			success:function(jsonStr){
				var json = JSON.parse(jsonStr);
				for(var name in json){
					var paramObj = json[name];
					//给各个组件赋值
					setValue(paramObj,name);
					//判断组件是否可见
					isDisplay(paramObj,name);
					//判断组件对象是否可编辑
					isEdit(paramObj,name);
					$("body").css("display","block");
				}
			}//end success
		});*/
	}else{
		$("body").css("display","block");
	}
} 

/**
 * 保存表单数据的方法
 */
var saveData = function(){
	$("#saveInfoBtn").click(function(e){
		e.preventDefault();
		var inputArr = $("table input");
		var selectArr = $("table select");
		var control = true;	//用于控制复选框出现重复值
		var checkName = ""; //用于获得复选框的class值，分辨多个复选框
		var json = "{";
		for(var i=0;i<inputArr.length;i++){
			var type = $(inputArr[i]).attr("type");
			var textJson = "";
			
			var checkJson = "";
			switch(type){
				case "text":{
					if($(inputArr[i]).prop("class")=="layui-input layui-unselect"){
						var name = $(inputArr[i]).parent().parent().prev().prop("name");
						var value = $("[name='"+name+"']").val();
						textJson = "\""+name+"\":{\"value\":\""+value+"\"}";
						break;
					}
				};
				case "tel":;
				case "date":;
				case "textarae":{
					var name = $(inputArr[i]).attr("name");
					var value = $("[name='"+name+"']").val();
					textJson = "\""+name+"\":{\"value\":\""+value+"\"}";
					break;
				}
				case "radio":{
					var name = $(inputArr[i]).attr("name");
					var radio = $("[name='"+name+"']").parent().parent().find("input:radio:checked");
					textJson = "\""+name+"\":{\"value\":\""+radio.attr("id")+"\"}";
					break;
				}
				case "checkbox":{
					var name = $(inputArr[i]).attr("name");
					var checkbox = $("[name='"+name+"']").parent().parent().find("input:checkbox:checked");
					//判断每次的复选框是否为同一个class
					if(control){
						checkName = checkbox.attr("name");
					}else{
						if(checkName!=checkbox.attr("name")){
							checkName = checkbox.attr("name");
							control = true;
						}
					}
					
					if(control){
						control = false;
						checkJson += "\""+checkName+"\":{\"value\":[";
						for(var j=0;j<checkbox.length;j++){
							if(j==checkbox.length-1){
								checkJson += "\""+$(checkbox[j]).attr("id")+"\"";
							}else{
								checkJson += "\""+$(checkbox[j]).attr("id")+"\",";
							}
						}
						checkJson += "]},";
					}
					
					json += checkJson;
					break;
				}
			}//end switch
			textJson += ",";
			if(json.indexOf(textJson)==-1){
				json += textJson;
			}
		}
		//获得最后一位字符是否为","
		var charStr = json.substring(json.length-1,json.length);
		
		if(charStr == ","){
			json = json.substring(0,json.length-1);
		}
		json += "}";
		alert(json);
		/*$.ajax({
			url:"saveData.do",
			method:"post",
			data:{
				"processInstanceDate":json,
				"processInstanceId":"PI00-0000-0000-0000-0000-0000-01",
				"tacheConfigureId":"3"
			},
			success:function(response){
				alert(true);
			}
		});*///end ajax
	}); //end
}

jQuery.fn.number = function() {
	this.bind("keypress", function(e) {
		var code = (e.keyCode ? e.keyCode : e.which); // 兼容火狐 IE
		// 火狐下不能使用退格键
		if (e.keyCode == 0x8) {
			return;
		}
		return code >= 48 && code <= 57
	});
	this.bind("paste", function() {
		return false;
	});
}; 

/*$.ajax({
	url:"fileController/fileUpLoad.do",
	method:"post",
	data:{
		filename:"test002.html",
		webpage:"<table><tr><td>1</td><td>2</td></tr></table>"
	},
	success:function(result){
		console.log(result);
	}
});*/