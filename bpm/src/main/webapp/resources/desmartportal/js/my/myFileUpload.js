var form = null;
var fileCount = 0;
/* 动态表单渲染js */
$(function(){
	//图片文件上传
	$("#formSet").find(".layui-upload-list").each(function(){
		var id = $(this).prop("id");
		//多图片上传
		var uploadImgListIns = upload.render({
		    elem: '#'+id+'_choose'
		    ,url: common.getPath() +'/accessoryFileUpload/uploadImgFile'
		    ,bindAction: '#'+id+'_load'
		    ,multiple: true
		    ,acceptMime: 'image/*'
		    ,auto: false
		    ,choose: function(obj){
		      var imgFiles = this.files = obj.pushFile();
		      //预读本地文件示例，不支持ie8
		      obj.preview(function(index, file, result){
		    	  var imgHtml = '<div style="display:inline;margin-right:20px;">'
		    	  		+'<img id="img_'+index+'" style="width:400px;" src="'+result+'" alt="'
		    	  		+ file.name +'" class="layui-upload-img">'
		    	  		+'<span style="cursor: pointer;"><i id="upload_'+index+'" class="layui-icon delete_img_file" title="删除图片">&#x1007;</i></span>'
		    	  		+'</div>';
		          $('#'+id).append(imgHtml);
		          $("#"+id).find(".delete_img_file").on('click', function(){
		              delete imgFiles[index]; // 删除对应的文件
		              $(this).parent().parent().remove();
		              uploadImgListIns.config.elem.next()[0].value = ''; 
		          });
		      });
		    }
			,before:function(){
				if($("#"+id).find(".delete_img_file").length>0){
					layer.load(1);
				}
		    }
		    ,done: function(res, index, upload){
		    	if(res.status==0){
		    		var fileNameHtml = '<div><a style="color:#1E9FFF;" href="http://'
		        		+ res.data.href +'" onclick="imgUrlClick(event);">'
		        		+ res.data.fileName +' <i class="layui-icon">&#xe64c;</i></a> '
		        		+'<span data-id="img_'+index+'" style="color:red;cursor: pointer;" onclick="deleteFile(\''+res.data.fileName+'\',this)">删除</span></div>';  
		        	$("#"+id+"_loc").find(".fileList").append(fileNameHtml);
		        	$("#upload_"+index).parent().remove();
		        	layer.closeAll('loading');
		        	return delete this.files[index];
		    	}else{
	        		this.error(index, upload);
	        		if(res.msg!=null && res.msg.length>0){
	        			layer.alert("上传图片失败");
	        		}
	        		layer.closeAll('loading');
	        	}
		    },
		    allDone:function(){
		    	layer.closeAll('loading');
		    }
		});
	});
	
	//表格上传文件
	$("#formSet").find(".load_data_file").each(function(){
		var appUid = $("#insUid").val();
		var activityId = $("#activityId").val();	
  		var taskUid = $("#taskUid").val();	
  	    var dataFileCount = 0;
		upload.render({ //允许上传的文件后缀
		    elem: $(this)
		    ,url: common.getPath() +'/accessoryFileUpload/uploadXlsOrXlsxFile'
		    ,accept: 'file' //普通文件
			,data: {"appUid":appUid,"activityId":activityId,"taskUid":taskUid}
		    ,exts: 'xls|xlsx' //只允许上传
		    ,before: function(obj){
			     	layer.load(1);
			    }
		    ,done: function(res){
		    	if(res.status==0){//上传成功，加载数据
		    		loadDataFormFileList();
		    	}
		    	layer.alert(res.msg);
		    	layer.closeAll("loading");
		    }
		    ,error: function(index, upload){
		    	layer.closeAll("loading");
		    }
		});
	});
	
	// 拉取全局配置
	loadGlobalConfig();
	
	$("#upload-file").click(function(){
		$(".display_container_file").css("display","block");
	});
	
	layui.use('upload', function(){
		  var $ = layui.jquery
		  ,upload = layui.upload;
		  
		  var appUid = $("#insUid").val();
		  var activityId = $("#activityId").val();	
  		  var taskUid = $("#taskUid").val();
  		  if(taskUid == undefined){
  			  taskUid = null;
  			}
		  var re = new RegExp(",","g");
		  var formatStr = fileFormat.replace(re,"|");
			 /*
				 * var maxFileSize =
				 * $(".hidden-value").find(".maxFileSize").val(); var
				 * maxFileCount =
				 * $(".hidden-value").find(".maxFileCount").val(); var
				 * fileFormat = $(".hidden-value").find(".fileFormat").val();
				 */
		  var btnButtom = $(".foot_accessory_file").find(".listAction");
			  var dragDiv = $("#upload_file_modal").find(".layui-upload-drag");
			  // 拖拽上传
			  var demoListView = $(".layui-upload-list").find('.fileList')
			  ,uploadListIns = upload.render({
			    elem: dragDiv
			    ,url: common.getPath() +'/accessoryFileUpload/saveFile.do'
			    ,accept: 'file' //普通文件
			    ,auto: false// 不自动上传
			    ,exts: formatStr
			    ,multiple: true
			    ,field: "files"
			    ,bindAction: btnButtom
			    ,choose: function(obj){   
			          var files = this.files = obj.pushFile(); // 将每次选择的文件追加到文件队列
			          layer.load(1);
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
			          	var fileSize = "";
			          	 if((file.size/1024/1024)<1){
			          		fileSize = (file.size/1024).toFixed(2) +'Kb'
			              }else{
			            	fileSize = (file.size/1024/1024).toFixed(2) +'Mb'
			              }
			          	
			            var tr = $(['<tr id="upload-'+ index +'">'
			              ,'<td>'+ file.name +'</td>'
			              ,'<td>'+ fileSize +'</td>'
			              ,/*
							 * '<td><input type="text" style="width:70px;" /></td>' ,'<td><input
							 * type="text" style="width:70px;" /></td>' ,'<td><input
							 * type="text" /></td>' ,
							 */'<td>等待上传</td>'
			              ,'<td>'
			              	/*
							 * ,'<button class="layui-btn layui-btn-mini
							 * demo-upload ">上传</button>'
							 */
			                ,'<button class="layui-btn layui-btn-mini demo-reload layui-hide">重传</button>'
			                ,'<button class="layui-btn layui-btn-mini layui-btn-danger demo-delete">删除</button>'
			              ,'</td>'
			            ,'</tr>'].join(''));
			            
			            // 单个重传
			            tr.find('.demo-reload').on('click', function(){
			              obj.upload(index, file);
			            });
			            // 单个上传
			            /*
						 * tr.find('.demo-upload').on('click', function(){
						 * obj.upload(index, file); });
						 */
			            
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
			  		,before: function(obj){ // 上传之前的回调函数
			  			var uploadModels = new Array();
			  			var trs = demoListView.children();
			  			trs.each(function(i){
			        		var tr = $(this),tds = tr.children();
			        		var operatorFlag =tds.eq(3).html();
			        		var appDocFileName =tds.eq(0).text();
			        		var appDocTitle = "";
						    var appDocComment = "";
						    var appDocTags = "process";
						    var appDocComment = "";
						    var uploadModel = '{"appDocFileName":"'+appDocFileName+'","appDocTitle":"'+appDocTitle+'","appDocComment":"'+appDocComment
						    		+'","appDocTags":"'+appDocTags+'"}';
						    if(operatorFlag!=""&&operatorFlag!=null){
						    uploadModels.push(uploadModel); 
						    }
			        	});
			  			if(uploadModels.length==0){
			  				layer.closeAll('loading');
			  			}else if(uploadModels.length>0){
			  				layer.load(1);
			  			}

	        	      this.data = {"appUid":appUid
					    	,"activityId":activityId,"taskUid":taskUid
					    	,uploadModels:'{"uploadModels":['+uploadModels+']}'};
		    		// this.data = {uploadModels:uploadModels.toString()};
			  		}

			        ,done: function(res, index, upload){
			        	var tr = demoListView.find('tr#upload-'+ index)
			            ,tds = tr.children();
			        	if(res.status==0){ // 上传成功
			        		tds.eq(2).html('<span style="color: #5FB878;">上传成功</span>');
			        		tds.eq(3).html(''); // 清空操作
			        		layer.closeAll('loading');
			        		return delete this.files[index]; // 删除文件队列已经上传成功的文件
			        	}else{
			        		this.error(index, upload);
			        		if(res.msg!=null && res.msg.length>0){
			        			layer.alert(res.msg);
			        		}
			        		layer.closeAll('loading');
			          // model.css("display","none");
			        	}
			         },allDone: function(obj){ // 当文件全部被提交后，才触发
			        	 layer.closeAll('loading');
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
			          tds.eq(2).html('<span style="color: #FF5722;">上传失败</span>');
			          // tds.eq(3).find('.demo-upload').addClass('layui-hide');//
						// 隐藏上传
			          tds.eq(3).find('.demo-reload').removeClass('layui-hide'); // 显示重传
			          layer.closeAll('loading');
			        }
			  });
			  if(window.innerWidth>700){
				  dragDiv.get(0).addEventListener("dragenter", function(e){ 
					    e.stopPropagation(); 
					    e.preventDefault(); 
					}, false);  
				  /* document.getElementById(dragId) */
				  dragDiv.get(0).addEventListener("dragover", function(e){ 
					    e.stopPropagation(); 
					    e.preventDefault(); 
					}, false); 
				  dragDiv.get(0).addEventListener("drop", function(e){ 
					    e.stopPropagation(); 
					    e.preventDefault(); 
					}, false);
			  }
	});
	
	loadFileList(true);//加载附件列表
	loadDataFormFileList();//加载数据表格文件列表

	// 全选
	$("#all-file-check").click(function(){ 
		var checkeNodes=$(".layui-table.upload-file-table").find(".file-check");
		checkeNodes.prop("checked",$(this).prop("checked")); 
	});
});
var maxFileSize = "";
var maxFileCount = "";
var fileFormat = "";
function loadGlobalConfig(){
	$.ajax({
		url:common.getPath()+"/accessoryFileUpload/loadGlobalConfig.do",
		type:'post',
        async: false,  
		dataType:'json',
		success: function(result){
			maxFileSize= result.data.maxFileSize;
			maxFileCount = result.data.maxFileCount;
			fileFormat = result.data.fileFormat;
		},
		error: function(){
			layer.alert("全局配置拉取异常！");
		}
	});
	
}
 

// 反选
  function invertSelection(a){ 
	  var checkeNodes= $(".layui-table.upload-file-table").find(".file-check");
	  var checkedNodes= $(".layui-table.upload-file-table").find(".file-check:checked");
	  if(checkedNodes.length==checkeNodes.length){
		  $("#all-file-check").prop("checked",$(a).prop("checked")); 
		}else{ 
		  $("#all-file-check").prop("checked",false); 
		} 
	  };

// 加载已上传的文件列表
function loadFileList(isFirst){
	var appUid = $("#insUid").val();
	var taskStatus = $("#taskStatus").val();
	$.post('accessoryFileUpload/loadFileList.do'
		,{"appUid":appUid}
		,function(result){
		var tagTbody = $(".layui-table.upload-file-table").find("tbody");
		tagTbody.empty();
		/*
		 * <input onclick='invertSelection(this)' class='file-check'
		 * type='checkbox'>
		 */
		for (var i = 0; i < result.data.length; i++) {
			var info = "<tr>"
		      +"<td>"
		      +"<input style = 'cursor: default;' onclick='invertSelection(this)' class='file-check' type='checkbox'>"
		      +"<input style='display: none;' name='appDocFileUrl' value='"+result.data[i].appDocFileUrl+"' />"
		      +(i+1)+"</td>"
		      +"<td>"+result.data[i].appDocFileName+"</td>"		
		     /*
				 * +"<td>"+result.data[i].appDocTags+"</td>" +"<td>"+result.data[i].appDocTitle+"</td>"
				 *//*
					 * +"<td>"+result.data[i].appDocComment+"</td>" +"<td>"+result.data[i].appDocType+"</td>"
					 */		      
		      +"<td>"+result.data[i].appUserName+"</td>"	
		      +"<td>"+datetimeFormat_1(result.data[i].appDocCreateDate)+"</td>"	
		      +"<td>";
		      
			  info += "<button value ='"+result.data[i].appDocUid+"' onclick='singleDown(this)' class='layui-btn layui-btn-primary layui-btn-sm down' style='margin-left:20px;'>下载附件</button>"
		     
		      if(taskStatus!="32"&&taskStatus!="-2"){
		    	  info += "<button class='layui-btn layui-btn-primary layui-btn-sm layui-update-file' style='margin-left:20px;' value = '"+result.data[i].appDocIdCard+"' data-appdocuid = '"+result.data[i].appDocUid+"'>更新附件</button>"
		      }
			
		      info +="<button onclick = 'showHistoryFile(this)' class='layui-btn layui-btn-primary layui-btn-sm layui-history-file' style='margin-left:20px;' value = '"+result.data[i].appDocIdCard+"'>查看历史版本</button>"
			
		      if(taskStatus!="32"&&taskStatus!="-2"){
		    	  info += "<button onclick='deleteAccessoryFile(this)' class='layui-btn layui-btn-primary layui-btn-sm' style='margin-left:20px;' value = '"+result.data[i].appDocIdCard+"' data-appdocuid = '"+result.data[i].appDocUid+"'>删除</button>"
		      }
		      
		      if($("#taskStatus").length!=0){
		    	  if( taskStatus!="32"&&taskStatus!="-2" && (result.data[i].appDocType=="image/png"||result.data[i].appDocType=="image/jpeg")){
			    	  info += "<button onclick='editAccessoryFile(this)' class='layui-btn layui-btn-primary layui-btn-sm' style='margin-left:20px;' value = '"+result.data[i].appDocIdCard+"' data-appdocuid = '"+result.data[i].appDocUid+"'>编辑</button>"			        
			      }
		      }
		      
		      info += "</td></tr>"; 
			tagTbody.append(info);
		}
		if(isFirst){
			fileCount = result.data.length
		};
		$(".layui-update-file").each(function(){
			var updateElem = $(this);
			var fileCount = 0;
			var appUid = $("#insUid").val();
		    var appDocIdCard = updateElem.val();
		    var appDocUid = updateElem.data("appdocuid");
			var activityId = $("#activityId").val();	
	  		  var taskUid = $("#taskUid").val();	
	  		if(taskUid == undefined){
	  		  taskUid = null;
	  		}
		    var re = new RegExp(",","g");
		    var formatStr = fileFormat.replace(re,"|");
			layui.use('upload', function(){
				  var $ = layui.jquery
				  ,upload = layui.upload;
			  var updateAccessoryFile = upload.render({
				  elem: updateElem
				    ,url: common.getPath()+'/accessoryFileUpload/updateAccessoryFile.do'
				    ,accept: 'file' //普通文件
				    ,data: {"appUid":appUid
				    	,"appDocIdCard":appDocIdCard,"appDocUid":appDocUid
				    	,"activityId":activityId,"taskUid":taskUid,"appDocTags":"process"}
				    ,exts: formatStr
				    ,field: "file"
			    ,before: function(obj){
			    	var files = this.files = obj.pushFile(); // 将每次选择的文件追加到文件队列
			          layer.load(1);
			          // 读取本地文件
			          obj.preview(function(index, file, result){
			        	if(fileCount>1){
				        	    fileCount = maxFileCount;
				          	    layer.msg('文件数量不得超过1个',{icon:2});
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
			          })
			    }
			    ,done: function(res){
			    	layer.alert(res.msg);
			    	loadFileList(false);
			    	layer.closeAll('loading');
			      // 上传完毕回调
			    }
			    ,error: function(res){
			      // 请求异常回调
			    	layer.alert(res.msg);
			    	layer.closeAll('loading');
			    }
			  });
		});
	 });
		},'json');
}
// 隐藏上传文件的模态框
function cancelClick(obj){
	loadFileList(false);
	$(".display_container_file").css("display","none");
}

// 批量下载触发事件

function batchDown(){ 
		var url = common.getPath()+"/accessoryFileUpload/bachFileDown.do"; 
		var trNodes= $(".layui-table.upload-file-table").find("tbody").find("tr");
		var params = new Array();
		for (var i = 0; i < trNodes.length; i++) {
			var flag = $(trNodes[i]).find("td").eq(0).find("input[type='checkbox']").prop("checked");
			if(flag==true){
			var appDocFileName = $(trNodes[i]).find("td").eq(1).text(); 
			var appDocFileUrl =$(trNodes[i]).find("td").eq(0).find("input[name='appDocFileUrl']").val();
			var info = {appDocFileName :appDocFileName,appDocFileUrl:appDocFileUrl};
			params.push(info);
			}
		}
		if(params != undefined && params.length>0){
			// layer.load(1);
		batchPost(url,params);
		}else{
			return;
		}
 };


// 单个下载触发事件
function singleDown(a){
  var url = common.getPath()+"/accessoryFileUpload/singleFileDown.do";
  var appDocUid = $(a).val();
  // layer.load(1);
  post(url,{"appDocUid" : appDocUid});
};

// 文件下载(单个下载)
function post(URL, PARAMS) { 
	var temp_form = document.createElement("form");      
	temp_form .action = URL;      
	// temp_form .target = "_blank"; 如需新打开窗口 form 的target属性要设置为'_blank'
	temp_form .method = "post";      
	temp_form .style.display = "none"; 
	for (var x in PARAMS) { 
	var opt = document.createElement("textarea");      
    opt.name = x;      
    opt.value = PARAMS[x];      
    temp_form .appendChild(opt);      
	}      
	document.body.appendChild(temp_form);      
	temp_form.submit();   
	temp_form.remove();
} 
function batchPost(URL, PARAMS) { 
	var temp_form = document.createElement("form");      
	temp_form .action = URL;      
	// temp_form .target = "_blank"; 如需新打开窗口 form 的target属性要设置为'_blank'
	temp_form .method = "post";      
	temp_form .style.display = "none";
	for (var i = 0; i < PARAMS.length; i++) {
	for (var x in PARAMS[i]) { 
	var opt = document.createElement("textarea");      
    opt.name = x;      
    opt.value = PARAMS[i][x];      
    temp_form .appendChild(opt);      
	}   
	}
	document.body.appendChild(temp_form);      
	temp_form.submit();
	temp_form.remove();
	// Ajax刷新进度条
	 /*
		 * $("#Progress").empty(); showProgress();
		 */
    window.setTimeout(function(){
       var timer=window.setInterval(function(){
         $.ajax({
            type:'post',
             dataType:'json', 
             url: common.getPath()+"/accessoryFileUpload/flushProgress.do",
             success: function(result) {
                
                 if(result.data.percent=="100"){
                     window.clearInterval(timer);
                     hideProgress();
                 }else{
                	 // $.data($.fn.circliful,"percent",result.data.percent)
                	 // settings.precent=result.data.percent;
                	 $("#Progress").data("percent",result.data.percent);
                	 $("#Progress").empty();
                	 showProgress();
                	 $("#Progress .circle-text").text(result.data.percentText);
                     if(result.data.curCount===undefined||result.data.totalCount===undefined){
                         $("#Progress .circle-info").text("下载进度");
                     }
                     else{
                          $("#Progress .circle-info").text("下载进度:"+result.data.curCount+"/"+result.data.totalCount);
                     }
                 }
             },
             error:function(result){}
          });
       },1000);
    },100);
    isFirstExport=false;
} 

// 显示历史版本附件
function showHistoryFile(a){
	var appDocIdCard = $(a).val();
	$.ajax({
		url : common.getPath() +"/accessoryFileUpload/loadHistoryFile.do",
		type : 'POST',
		dataType : 'json',
		data : {
			"appDocIdCard":appDocIdCard
		},
		success : function(result) {
			var tbody = $("#showHistoryModal").find(".showHistoryList");
			tbody.empty();
			if(result.data.length>0){
				for (var i = 0; i < result.data.length; i++) {
					var info = "<tr><td>"
						+"<input style='display: none;' name='appDocFileUrl' value='"+result.data[i].appDocFileUrl+"' />"
						+result.data[i].docVersion+"</td>"
						+"<td>"+result.data[i].appDocFileName+"</td>"
						+"<td>"+result.data[i].appUserName+"</td>"
						+"<td>"+result.data[i].updateUserName+"</td>"
						+"<td>"+datetimeFormat_1(result.data[i].appDocUpdateDate)+"</td>"
						+"<td><button value ='"+result.data[i].appDocUid+"' onclick='singleDown(this)'" 
						+" class='layui-btn layui-btn-primary layui-btn-sm down' style='margin-left:20px;'>下载附件</button>"
						+"</td></tr>";
						tbody.append(info);
				}
			}else{
				var info ='<tr><td colspan="6" style="text-align: center;"><li>该附件暂无历史版本'
					+'</td></tr>';
				tbody.append(info);
			}
		},
		error : function(data) {
			layer.alert(data.msg);
		}
	});
	$("#showHistoryModal").css("display","block");
}
// 隐藏历史版本附件模态框
function hideHistoryFile(){
	$("#showHistoryModal").css("display","none");
}

// 删除附件触发事件
function deleteAccessoryFile(a){
	layer.confirm('确认删除？', {
		  btn: ['确定', '取消'] 
		}, function(index){
	var appDocFileUrl = $(a).parent().parent().find("td").eq(0).find("input[name='appDocFileUrl']").val();
    var appDocIdCard = $(a).val();
    var appDocUid = $(a).data("appdocuid");		  
	var activityId = $("#activityId").val();
	  var taskUid = $("#taskUid").val();
	  if(taskUid == undefined){
		  taskUid = null;
	  }
    	$.ajax({
    		url : common.getPath()+"/accessoryFileUpload/deleteAccessoryFile.do",
    		type : 'POST',
    		dataType : 'json',
    		data : {
			"appDocFileUrl":appDocFileUrl,
			"appDocUid":appDocUid,
			"appDocIdCard":appDocIdCard,
			"activityId":activityId,
			"taskUid":taskUid
			},
		success : function(data) {
			fileCount--;
			loadFileList(false);
			layer.alert(data.msg);
		  },
		error : function(data) {
			layer.alert(data.msg);
		  }
     });
		}, function(index){
			 layer.close(index)
		});
}

// 附件进度条js开始
// 显示进度条
var isFirstExport=true;
function showProgress(){
    $("#Mask").css("height",$(document).height()+100);
    $("#Mask").css("width",$(document).width());
    $("#Mask").show();
    // if(isFirstExport){
        $("#Progress").circliful();
    // }else{
        // $("#Progress .circle-text").text("0%");
        // $("#Progress .circle-info").text("导出进度");
        $("#Progress").show();
    // }
}  
// 隐藏进度条
function hideProgress(){
    $("#Mask").hide();
    $("#Progress").empty();
    $("#Progress").hide();
}

(function( $ ) {
	
	 // 滚动条事件
    $(window).scroll(function() {    
        // 获取窗口一半的高度+滚动条高度
        var offsetTop = $(window).scrollTop() + $(window).height() / 2+100;    
        // 改变样式 显示
        // $("#test").show();
        // 改变样式 top 高度
        $("#Progress").animate({ top: offsetTop + "px" }, { duration: 600, queue: false });    
        // 改变left 位置
        // $("#test").animate({ left: $(window).width() - 40 + "px" }, {
		// duration: 600, queue: false });
    });    
	
    
    $.fn.circliful = function(options) {

        var settings = $.extend({
            // These are the defaults.
            foregroundColor: "#556b2f",
            backgroundColor: "#86DB49",
            fillColor: false,
            width: 15,
            dimension: 200,
            size: 15, 
            percent: 50,
            animationStep: 1.0
        }, options );
         return this.each(function() {
                var dimension = '';
                var text = '';
                var info = '';
                var width = '';
                var size = 0;
                var percent = 0;
                var endPercent = 100;
                var fgcolor = '';
                var bgcolor = '';
                var icon = '';
                var animationstep = 0.0;

                $(this).addClass('circliful');

                if($(this).data('dimension') != undefined) {
                    dimension = $(this).data('dimension');
                } else {
                    dimension = settings.dimension;
                }

                if($(this).data('width') != undefined) {
                    width = $(this).data('width');
                } else {
                    width = settings.width;
                }

                if($(this).data('fontsize') != undefined) {
                    size = $(this).data('fontsize');
                } else {
                    size = settings.size;
                }
                if($(this).data('percent') != undefined) {
                    percent = $(this).data('percent') / 100;
                    endPercent = $(this).data('percent');
                } else {
                    percent = settings.percent / 100;
                }

                if($(this).data('fgcolor') != undefined) {
                    fgcolor = $(this).data('fgcolor');
                } else {
                    fgcolor = settings.foregroundColor;
                }

                if($(this).data('bgcolor') != undefined) {
                    bgcolor = $(this).data('bgcolor');
                } else {
                    bgcolor = settings.backgroundColor;
                }

                if($(this).data('animation-step') != undefined) {
                    animationstep = parseFloat($(this).data('animation-step'));
                } else {
                    animationstep = settings.animationStep;
                }
                if($(this).data('text') != undefined) {
                    text = $(this).data('text');

                    if($(this).data('icon') != undefined) {
                        icon = '<i class="fa ' + $(this).data('icon') + '"></i>';
                    }

                     if($(this).data('type') != undefined) {
                        type = $(this).data('type');

                        if(type == 'half') {
                            $(this).append('<span class="circle-text-half">' +  icon  + text + '</span>');
                            $(this).find('.circle-text-half').css({'line-height': (dimension / 1.45) + 'px', 'font-size' : size + 'px' });
                        } else {
                            $(this).append('<span class="circle-text">' + icon + text + '</span>');
                            $(this).find('.circle-text').css({'line-height': dimension + 'px', 'font-size' : size + 'px' });
                        }
                    } else {
                        $(this).append('<span class="circle-text">' + icon + text + '</span>');
                        $(this).find('.circle-text').css({'line-height': dimension + 'px', 'font-size' : size + 'px' });
                    }
                } else if($(this).data('icon') != undefined) {

                }

                if($(this).data('info') != undefined) {
                    info = $(this).data('info');

                    if($(this).data('type') != undefined) {
                        type = $(this).data('type');

                        if(type == 'half') { 
                            $(this).append('<span class="circle-info-half">' + info + '</span>');
                            $(this).find('.circle-info-half').css({'line-height': (dimension * 0.9) + 'px', });
                        } else {
                            $(this).append('<span class="circle-info">' + info + '</span>');
                            $(this).find('.circle-info').css({'line-height': (dimension * 1.25) + 'px', });
                        }
                    } else {
                        $(this).append('<span class="circle-info">' + info + '</span>');
                        $(this).find('.circle-info').css({'line-height': (dimension * 1.25) + 'px', });
                    }
                }

                $(this).width(dimension + 'px');

              var canvas = $('<canvas></canvas>').attr({ width: dimension, height: dimension }).appendTo($(this)).get(0);
              var context = canvas.getContext('2d');
              var x = canvas.width / 2;
              var y = canvas.height / 2;
              var degrees = percent * 360.0;
              var radians = degrees * (Math.PI / 180);
              var radius = canvas.width / 2.5;
              var startAngle = 2.3 * Math.PI;
              var endAngle = 0;
              var counterClockwise = false;
              var curPerc = animationstep === 0.0 ? endPercent : 0.0;
              var curStep = Math.max(animationstep, 0.0);
              var circ = Math.PI * 2;
              var quart = Math.PI / 2;
              var type = '';
              var fill = false;

              if($(this).data('type') != undefined) {
                    type = $(this).data('type');

                    if(type == 'half') {
                        var startAngle = 2.0 * Math.PI;
                        var endAngle = 3.13;
                        var circ = Math.PI * 1.0;
                        var quart = Math.PI / 0.996;
                    }
                }

                if($(this).data('fill') != undefined) {
                    fill = $(this).data('fill');
                } else {
                    fill = settings.fillColor;
                }
              // animate foreground circle
              function animate(current) {
                context.clearRect(0, 0, canvas.width, canvas.height);

                context.beginPath();
                context.arc(x, y, radius, endAngle, startAngle, false);
                context.lineWidth = width - 1;

                // line color
                context.strokeStyle = bgcolor;
                context.stroke();

                if(fill) {
                    context.fillStyle = fill;
                    context.fill();
                }

                context.beginPath();
                context.arc(x, y, radius, -(quart), ((circ) * current) - quart, false);
                context.lineWidth = width;
                // line color
                context.strokeStyle = fgcolor;
                context.stroke();

                if (curPerc < endPercent) {
                     curPerc += curStep;
                     // requestAnimationFrame(function () {
                         animate(Math.min(curPerc, endPercent) / 100);
                     // });
                }

             }

             animate(curPerc / 100);

        });

    };

}( jQuery ));
//下载数据表格模板文件
function downTemplateFile(){
	 var url = common.getPath()+"/accessoryFileUpload/singleFileDown.do";
	 var appDocUid = $("#dataFormTemplateDocUid").val();
	 post(url,{"appDocUid" : "123456"});
}
//加载数据表格文件
function loadDataFormFileList(){
	//var activityId = $("#activityId").val();
	var appUid = $("#insUid").val();
	$.post('accessoryFileUpload/loadDataFormFileList'
		,{"appUid":appUid}
		,function(result){
		$("td[data-label='操作数据']").empty();
		$("td[data-label='文件名称']").html("");
		$("td[data-label='上传人']").html("");
		$("td[data-label='上传时间']").html("");
		for (var i = 0; i < result.data.length; i++) {
			$("td[data-label='文件名称']").html(result.data[i].appDocFileName);
			$("td[data-label='上传人']").html(result.data[i].appUserName);
			$("td[data-label='上传时间']").html(datetimeFormat_1(result.data[i].appDocCreateDate));
			 var info = "<button value ='"+result.data[i].appDocUid+"' onclick='singleDown(this)' class='layui-btn layui-btn-primary layui-btn-sm down' style='margin-left:20px;'>下载数据文件</button>"
		     		  +"<button onclick='deleteDataFormFileList(this)' class='layui-btn layui-btn-primary layui-btn-sm' style='margin-left:20px;' data-appdocuid = '"+result.data[i].appDocUid+"'>删除数据文件</button>"
			 $("td[data-label='操作数据']").append(info);
		}
		});
}
function deleteDataFormFileList(a){
	layer.confirm('确认删除？', {
		  btn: ['确定', '取消'] 
		}, function(index){
	  var appDocUid = $(a).data("appdocuid");		  
	  var activityId = $("#activityId").val();
	  var taskUid = $("#taskUid").val();
	  if(taskUid == undefined){
		  taskUid = null;
	  }
  	$.ajax({
  		url : common.getPath()+"/accessoryFileUpload/deleteDataFormFileList",
  		type : 'POST',
  		dataType : 'json',
  		data : {
			"appDocUid":appDocUid,
			"taskId":activityId,
			"taskUid":taskUid
			},
		success : function(data) {
			loadDataFormFileList();
			layer.alert(data.msg);
		  },
		error : function(data) {
			layer.alert(data.msg);
		  }
   });
		}, function(index){
			 layer.close(index)
		});
}

/**
 * 附件编辑方法--调用myWPaint.js中的方法
 * 
 * @param a
 * @returns
 */
function editAccessoryFile(a){
	loadImageData(a);
}

//图片上传后禁止图片链接默认事件
function imgUrlClick(event){
	event.preventDefault();
}

//删除图片附件的方法
function deleteFile(fileName,obj){
	layer.confirm('确认删除？', {
		  btn: ['确定', '取消'] 
	}, function(index){
		$.ajax({
			url:common.getPath()+"/accessoryFileUpload/deleteImgFile",
			data:{
				fileName:fileName
			},
			beforeSend:function(){
				layer.load(1);
			},
			success:function(res){
				if(res.status == 0){
					var id = $(obj).attr("data-id");
					$("#"+id).parent().remove();
					$(obj).parent().remove();
					$(obj).remove();
					layer.alert("删除成功");
				}else{
					layer.alert("删除失败");
				}
				layer.closeAll("loading");
			}
		});
	});
}