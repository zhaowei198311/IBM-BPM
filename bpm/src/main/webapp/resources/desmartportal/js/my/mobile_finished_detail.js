var form = null;
$(function () {
	layui.use(['form'], function () {
        form = layui.form;
    });
	var insData = $("#insData").text();
	var insDataFromDb = JSON.parse(insData);
    var formData = insDataFromDb.formData;
    var str = JSON.stringify(formData);
    common.giveFormSetValue(str);
    var fieldPermissionInfo = $("#fieldPermissionInfo").text();
    common.giveFormFieldPermission(fieldPermissionInfo);
    form.render();
    
    //加载已上传的附件
    loadFileList();
});

function back() {
	window.location.href = 'javascript:history.go(-1)';
}

//取回
function revokeTask(taskUid) {
    $.ajax({
        url : common.getPath() +"/taskInstance/revokeTask",
        type : "post",
        dataType : "json",
        data : {
            "taskUid": taskUid
		},
        beforeSend : function(){
            layer.load(1);
        },
        success : function(result){
            layer.closeAll('loading');
            if(result.status == 0){
				layer.alert('取回成功，请去待办页面处理', function () {
                    window.history.back();
                });
            }else{
                layer.alert(result.msg);
            }
        },
        error : function(){
            layer.closeAll('loading');
            layer.alert('操作失败');
        }
    });
}

//加载已上传的文件列表
function loadFileList(){
	var appUid = $("#insUid").val();
	var taskStatus = $("#taskStatus").val();
	$.post('accessoryFileUpload/loadFileList.do'
		,{"appUid":appUid}
		,function(result){
		$("#loadFile_div").empty();
		var info = '<h1 style="clear: both;"></h1>';
		for (var i = 0; i < result.data.length; i++) {
			info += '<li>'
				+'<table>'
					+'<tr>'
						+'<th>附件名称：</th>'
						+'<td>'+result.data[i].appDocFileName+'</td>'
					+'</tr>'
					+'<tr>'
						+'<th>上传人：</th>'
						+'<td>'+result.data[i].appUserName+'</td>'
					+'</tr>'
					+'<tr>'
						+'<th>上传时间：</th>'
						+'<td>'+datetimeFormat_1(result.data[i].appDocCreateDate)+'</td>'
					+'</tr>'
					+'<tr>'
						+'<th>操作：</th>'
						+'<td value="'+result.data[i].appDocUid
						+'" onclick="singleDown(this)" style="color:#009688;">下载附件<i class="layui-icon">&#xe601;</i></td>'
					+'</tr>'
				+'</table>'
				+'</li>';
		}
		info+='<h1 style="clear: both;"></h1>';
		$("#loadFile_div").append(info);
	});
}

//单个下载触发事件
function singleDown(a){
  var url = common.getPath()+"/accessoryFileUpload/singleFileDown.do";
  var appDocUid = $(a).attr("value");
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

function backApproval(){
	jQuery('html,body').animate({
	    scrollTop: scrollTop-80
	}, 300);
	$(".mobile_container").css("display","block");
	$("#table_tr_container").css("display","none");
}