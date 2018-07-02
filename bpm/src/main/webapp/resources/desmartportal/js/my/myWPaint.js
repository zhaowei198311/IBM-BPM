
/*$(function(){
		loadImageData();
});*/
var img = null;
function loadImageData(a){
	window.onbeforeunload = function()
	{
		deleteTemporaryFile();
	}
	
	//为左侧菜单栏添加click事件
	/*$(".layui-nav.layui-nav-tree").on('click',function () {
		layer.alert('sss');
	})
	$("#imgEditMain").bind('click',function(){
		layer.alert('sss');
	})*/
    //var appDocIdCard = $(a).val();
    var appDocUid = $(a).data("appdocuid");
/*
	var editIdElem = $(a).parent().parent().find("td").eq(0);
	var appDocFileUrl = editIdElem.find("input[name='appDocFileUrl']").val();*/
	$.ajax({
		url: common.getPath()+'/accessoryFileUpload/loadImageData',
 		async: false,
		data: {"appDocUid":appDocUid},
		type: 'post',
		dataType: 'json',
		beforeSend: function () {
            layer.load(1);
        },
		success: function(resp)
		{
			//var base64ImageData = resp.data.base64ImageData;
			if(resp.status==0){
			var demoFileName = resp.data.demoFileName;
			var absoulteImgPath = resp.data.absoulteImgPath;
			$("#imgEditAccessoryFileData").data("appdocuid",resp.data.dhInstanceDocument.appDocUid);
			$("#imgEditAccessoryFileData").data("absoulteimgpath",absoulteImgPath);
			
			//$("#imgEditAccessoryFileData").data("appdocidcard",resp.data.dhInstanceDocument.appDocIdCard);
			img= new Image();
			// 改变图片的src
			var imgSrc = common.getPath()+"/resources/desmartportal/upload/demo/"+demoFileName;
			img.src = imgSrc;
			
			// 定时执行获取宽高
			var check = function(){
			    // 只要任何一方大于0
			    // 表示已经服务器已经返回宽高
			    if(img.width>0 || img.height>0){
			        clearInterval(set);
			    }
			};
			 
			var set = setInterval(check,40);
			 
			// 加载完成获取宽高
			img.onload = function(){
				$("#wPaint").css({"width":img.width+"px","height":img.height+"px"});
				$("#wPaint").children('canvas:first').css({"width":img.width+"px","height":img.height+"px"});
				//$("#imgEditMain").css({"width":img.width+"px","height":img.height+"px"});
				$(".display_container_image_edit").css("display", "block");
				
				var wp = $("#wPaint").wPaint({ 
					drawDown: function(e, mode){ $("#canvasDown").val(this.settings.mode + ": " + e.pageX + ',' + e.pageY); },
					drawMove: function(e, mode){ $("#canvasMove").val(this.settings.mode + ": " + e.pageX + ',' + e.pageY); },
					drawUp: function(e, mode){ $("#canvasUp").val(this.settings.mode + ": " + e.pageX + ',' + e.pageY); }
				}).data('_wPaint');
				$("#wPaint").wPaint("image",img.src);
				//$("#wPaint").wPaint("image", common.getPath()+"/resources/desmartportal/wPaint-master/images/demo/显卡天梯图 - 副本.png");
				layer.closeAll('loading');
			};
			
			}else{
				layer.alert(resp.msg);
				layer.closeAll('loading');
			}
			
		},
		error: function(){
			layer.alert("error uploaded image!");
			//请求异常回调
	    	layer.closeAll('loading');
		}
	});
}

function loadImage_png()
{
	//$("#wPaint").wPaint("image", common.getPath()+"/resources/desmartportal/wPaint-master/images/demo/显卡天梯图 - 副本.png");
	if(img!=null){
		$("#wPaint").wPaint("image", img.src);
	}
}

/*function saveImage()
{
	var imageData = $("#wPaint").wPaint("image");
	
	$("#canvasImage").attr('src', imageData);
	$("#canvasImageData").val(imageData);
}*/

function clearCanvas()
{
	$("#wPaint").wPaint("clear");
}

function upload_image()
{
	var appUid = $("#insUid").val();
    var taskId = $("#activityId").val();
    //var appDocIdCard = $("#imgEditAccessoryFileData").data("appdocidcard");
    var appDocUid = $("#imgEditAccessoryFileData").data("appdocuid");
    var absoulteImgPath = $("#imgEditAccessoryFileData").data("absoulteimgpath");
	var activityId = $("#activityId").val();	
    var taskUid = $("#taskUid").val();	
	if(taskUid == undefined){
		 taskUid = null;
	}
	var imageData = $("#wPaint").wPaint("image");

	//var preFix = "data:image/png;base64,";
	//imageData = imageData.substring(preFix.length);
	var info = {"image": imageData,"appUid":appUid
			,"taskId":taskId,"appDocUid":appDocUid
	    	,"activityId":activityId,"taskUid":taskUid
	    	,"absoulteImgPath":absoulteImgPath
			 };
	$.ajax({
		url: common.getPath()+'/accessoryFileUpload/uploadEditData',
		data: JSON.stringify(info),
		contentType: "application/json",
		type: 'post',
		dataType: 'json',
		beforeSend: function () {
            layer.load(1);
        },
		success: function(resp)
		{
			layer.alert(resp.msg);
	    	loadFileList();
	    	$(".display_container_image_edit").css("display", "none");
	    	layer.closeAll('loading');
		},
		error: function(){
			layer.alert("error uploaded image!");
			//请求异常回调
	    	layer.closeAll('loading');
		}
	});
}

function closeImageEdit(){
	layer.confirm('是否保存修改?', function(index){
		upload_image();
		layer.close(index);
		
		$(".display_container_image_edit").css("display", "none");
	},function(index){
		deleteTemporaryFile();
		layer.close(index);
		
		$(".display_container_image_edit").css("display", "none");
	});
	
}

function deleteTemporaryFile(){
	var absoulteImgPath = $("#imgEditAccessoryFileData").data("absoulteimgpath");
	$.ajax({
		url: common.getPath()+'/accessoryFileUpload/deleteTemporaryFile',
		data: {"absoulteImgPath":absoulteImgPath},
		type: 'post',
		dataType: 'json',
		beforeSend: function () {
            layer.load(1);
        },
		success: function(resp)
		{
			layer.closeAll('loading');
		},
		error: function(){
			layer.alert("error uploaded image!");
			//请求异常回调
	    	layer.closeAll('loading');
		}
	});
}
/*(function( $ ) {
	
$(window).scroll(function() {    
    //获取窗口一半的高度+滚动条高度    
    var offsetTop = $(window).scrollTop() + $(window).height() / 2;  
    //改变样式 top 高度    
    $("._wPaint_menu _wPaint_menu_horizontal ui-draggable").animate({ top: offsetTop + "px" }, { duration: 600, queue: false });    
    //改变left 位置    
    //$("._wPaint_menu _wPaint_menu_horizontal ui-draggable").animate({ left: $(window).width() - 40 + "px" }, { duration: 600, queue: false });    
}); }( jQuery ));  */