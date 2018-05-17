var form = null;
/* 动态表单渲染js */
$(function(){
	console.log($("[name='test_name']").prop("id"));
	var tableHead = '<table class="layui-table">'
				+'<colgroup>'
				+'<col width="">'
				+'<col width="">'
				+'<col width="">'
				+'<col width="">'
				+'<col width="">'
				+'<col width="">'
				+'<col width="">'
				+'<col width="">'
				+'<col width="">'
				+'<col width="">'
				+'<col width="">'
				+'<col width="">'
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
				}// end if editor
			}// end if column
		}// end for
		formHtml += "</tr>";
	});
	formHtml += "</tbody></table>";
	view.html(formHtml);
	if(view.find(".layui-table").width()>568){
		var colWidth = view.find(".layui-table").width()/12;
		view.find("col").css("width",colWidth);
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
				  }// end if
			  });
		  });
		  
		  view.find(".editor_textarea").each(function(){
			  var editorId = $(this).prop("id");
			  var editor = layedit.build(editorId, {
					  tool: [
						  'strong' // 加粗
						  ,'italic' // 斜体
						  ,'underline' // 下划线
						  ,'del' // 删除线
						  
						  ,'|' // 分割线
						  
						  ,'left' // 左对齐
						  ,'center' // 居中对齐
						  ,'right' // 右对齐
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
	
	/*var loadBtn = view.find(".file");
	loadBtn.each(function(){
		var modelHtml = $("#file_load_hide").html();
		view.append(modelHtml);
		view.find(".model").prop("id",$(this).prop("id")+"Model").removeClass("model");
		$("#"+$(this).prop("id")+"Model").find(".layui-upload-drag").prop("id",$(this).prop("id")+"Drag");
		$("#"+$(this).prop("id")+"Model").find(".listAction").prop("id",$(this).prop("id")+"Btn");
		
		$(this).click(function(){
			$("#"+$(this).prop("id")+"Model").css("display","block");
		});
	});*/
	
	$("#upload-file").click(function(){
		$("#upload_file_modal").css("display","block");
	});
	
	//var uploadModels = new Array();
	layui.use('upload', function(){
		  var $ = layui.jquery
		  ,upload = layui.upload;
		  
		  var fileCount = 0;
		  
			  var btnButtom = $(".foot_accessory_file").find(".listAction");
			  var maxFileSize = $(".hidden-value").find(".maxFileSize").val();
			  var maxFileCount = $(".hidden-value").find(".maxFileCount").val();
			  var fileFormat = $(".hidden-value").find(".fileFormat").val();
			  re = new RegExp(",","g");
			  var formatStr = fileFormat.replace(re,"|");
			  var dragDiv = $("#upload_file_modal").find(".layui-upload-drag");
			  // 拖拽上传
			  var demoListView = $(".layui-upload-list").find('.fileList')
			  ,uploadListIns = upload.render({
			    elem: dragDiv
			    ,url: 'accessoryFileUpload/saveFile.do'
			    ,auto: false// 不自动上传
			    ,exts: formatStr
			    ,multiple: true
			    ,field: "files"
			    ,bindAction: btnButtom
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
			              ,'<td><input type="text" style="width:70px;" /></td>'
			              ,'<td><input type="text" style="width:70px;" /></td>'
			              ,'<td><input type="text" /></td>'
			              ,'<td>等待上传</td>'
			              ,'<td>'
			              	,'<button class="layui-btn layui-btn-mini demo-upload ">上传</button>'
			                ,'<button class="layui-btn layui-btn-mini demo-reload layui-hide">重传</button>'
			                ,'<button class="layui-btn layui-btn-mini layui-btn-danger demo-delete">删除</button>'
			              ,'</td>'
			            ,'</tr>'].join(''));
			            
			            // 单个重传
			            tr.find('.demo-reload').on('click', function(){
			              obj.upload(index, file);
			            });
			            // 单个上传
			            tr.find('.demo-upload').on('click', function(){
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
			  		,before: function(obj){ //上传之前的回调函数
			  			var uploadModels = new Array();
			  			var trs = demoListView.children();
			  			trs.each(function(i){
			        		var tr = $(this),tds = tr.children();
			        		var appDocFileName = tds.eq(0).text();
			        		var appDocTitle = tds.eq(2).find("input").val();
						    var appDocComment = tds.eq(3).find("input").val();
						    var appDocTags = tds.eq(4).find("input").val();
						    var uploadModel = '{"appDocFileName":"'+appDocFileName+'","appDocTitle":"'+appDocTitle+'","appDocComment":"'+appDocComment
						    		+'","appDocTags":"'+appDocTags+'"}';
						    uploadModels.push(uploadModel);   
			        	});
		    		/*,data:{
				    	appDocTitle:"测试"
				    	,appDocComment:"测试"
				    	,appUid:"测试"
				    	,taskId:"1"
						,userUid:"测试"
						,appDocTags:appDocTags
				    }*/
	        	      this.data = {appUid:"测试"
					    	,taskId:"1",uploadModels:'{"uploadModels":['+uploadModels+']}'};
		    		//this.data = {uploadModels:uploadModels.toString()};
			  		}

			        ,done: function(res, index, upload){
			        	var tr = demoListView.find('tr#upload-'+ index)
			            ,tds = tr.children();
			        	if(res.data==1){ // 上传成功
			        		tds.eq(5).html('<span style="color: #5FB878;">上传成功</span>');
			        		tds.eq(6).html(''); // 清空操作
			        		return delete this.files[index]; // 删除文件队列已经上传成功的文件
			        	}else{
			        		this.error(index, upload);
			        		if(res.msg!=null && res.msg.length>0){
			        			layer.alert(res.msg);
			        		}
			          // model.css("display","none");
			        	}
			         },allDone: function(obj){ // 当文件全部被提交后，才触发
			            console.log(obj.total); // 得到总文件数
			            console.log(obj.successful); // 请求成功的文件数
			            console.log(obj.aborted); // 请求失败的文件数
			            /*
						 * if(obj.total==obj.successful){
						 * layer.alert("上传文件总数："+obj.total+"
						 * 成功："+obj.successful); }else{
						 * layer.alert("上传文件总数："+obj.total+" 失败："+obj.aborted); }
						 */
			          }
			        ,error: function(index, upload){
			          var tr = demoListView.find('tr#upload-'+ index)
			          ,tds = tr.children();
			          tds.eq(5).html('<span style="color: #FF5722;">上传失败</span>');
			          tds.eq(6).find('.demo-upload').addClass('layui-hide');// 隐藏上传
			          tds.eq(6).find('.demo-reload').removeClass('layui-hide'); // 显示重传
			        }
			  });
			  
			/*  document.getElementById(dragId).addEventListener("dragenter", function(e){ 
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
*/
	});
	
	loadFileList();
	//全选
	/*$("#all-file-check").click(function(){
		var checkeNodes= $(".layui-table.upload-file-table").find(".file-check");
		checkeNodes.prop("checked",$(this).prop("checked"));
	});
	*/
});
//反选
/*function invertSelection(a){
	var checkeNodes= $(".layui-table.upload-file-table").find(".file-check");
	var checkedNodes= $(".layui-table.upload-file-table").find(".file-check:checked");
	if(checkedNodes.length==checkeNodes.length){
		$("#all-file-check").prop("checked",$(a).prop("checked"));
	}else if(checkedNodes.length==0){
		$("#all-file-check").prop("checked",false);
	}
};*/
//加载已上传的文件列表
function loadFileList(){
	$.post('accessoryFileUpload/loadFileList.do'
		,{"appUid":"测试"}
		,function(result){
		var tagTbody = $(".layui-table.upload-file-table").find("tbody");
		tagTbody.empty();
		/*<input onclick='invertSelection(this)' class='file-check' type='checkbox'>*/
		for (var i = 0; i < result.data.length; i++) {
			var info = "<tr>"
		      +"<td>"
		      +"<input style='display: none;' name='appDocFileUrl' value='"+result.data[i].appDocFileUrl+"' />"
		      +(i+1)+"</td>"
		      +"<td>"+result.data[i].appDocFileName+"</td>"		
		      +"<td>"+result.data[i].appDocTags+"</td>"	
		      +"<td>"+result.data[i].appDocTitle+"</td>"	
		      +"<td>"+result.data[i].appDocComment+"</td>"	
		      +"<td>"+result.data[i].appDocType+"</td>"			      
		      +"<td>"+result.data[i].appUserName+"</td>"	
		      +"<td>"+result.data[i].appDocCreateDate+"</td>"	
		      +"<td><button onclick='singleDown(this)' class='layui-btn layui-btn-primary layui-btn-sm down' style='margin-left:20px;'>下载附件</button>"
		      +"<button class='layui-btn layui-btn-primary layui-btn-sm' style='margin-left:20px;'" +
		      		" value = '"+result.data[i].appDocUid+"'>删除</button>"
		      +"</td></tr>"; 
			tagTbody.append(info);
		}
		},'json');
}
// 隐藏上传文件的模态框
function cancelClick(obj){
	loadFileList();
	$("#upload_file_modal").css("display","none");
}

//批量下载触发事件
/*function batchDown(){
  var url = "accessoryFileUpload/batchFileDown.do";
  var checkedNodes= $(".layui-table.upload-file-table").find(".file-check:checked");
  checkedNodes.each(function (){
	  var appDocFileName = $(this).parent().parent().find("td").eq(1).text();
	  var appDocFileUrl = $(this).parent().parent().find("td").eq(0).find("input[name='appDocFileUrl']").val();
	  var info = {appDocFileName :appDocFileName,appDocFileUrl:appDocFileUrl};
  });
 // post(url,);
};
*/

//单个下载触发事件
function singleDown(a){
  var url = "accessoryFileUpload/singleFileDown.do";
  var appDocFileName = $(a).parent().parent().find("td").eq(1).text();
  var appDocFileUrl = $(a).parent().parent().find("td").eq(0).find("input[name='appDocFileUrl']").val();
  post(url,{appDocFileName :appDocFileName,appDocFileUrl:appDocFileUrl});
};

//文件下载
function post(URL, PARAMS) { 
	var temp_form = document.createElement("form");      
	temp_form .action = URL;      
	//temp_form .target = "_blank"; 如需新打开窗口 form 的target属性要设置为'_blank'
	temp_form .method = "post";      
	temp_form .style.display = "none"; 
	for (var x in PARAMS) { 
	var opt = document.createElement("textarea");      
    opt.name = x;      
    opt.value = PARAMS[x];      
    temp_form .appendChild(opt);      
	}      
	document.body.appendChild(temp_form);      
	temp_form .submit();     
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