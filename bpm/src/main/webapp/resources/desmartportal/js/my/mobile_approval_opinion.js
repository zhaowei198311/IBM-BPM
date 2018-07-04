var editIndex;
layui.use('form', function(){
    var form = layui.form;
	form.on('select(useselfChange)', function(data) {
		if (data.value != '-1') {
			if ($("#myApprovalOpinion").val() == null
					|| $("#myApprovalOpinion").val() == '') {
				$("#myApprovalOpinion").val(data.value);
			} else {
				var info = $("#myApprovalOpinion").val() + data.value;
				$("#myApprovalOpinion").val(info);
			}
		}
	});
    
});

var reg=new RegExp("<br>","g"); // 创建正则RegExp对象
$(function(){
	var approvalJsonStr = $("#approvalData").text();
	if(approvalJsonStr!=null && approvalJsonStr!=''){
		var approvalData = JSON.parse(approvalJsonStr);
		if(approvalData.aprOpiComment!=null && approvalData.aprOpiComment!= ''){
			var info = approvalData.aprOpiComment.replace(reg,"\n");
			$("#myApprovalOpinion").val(info);
		}
	}
    loadDhApprovalOpinionList();//加载审批记录
    loadDhroutingRecords();//加载流转信息
});

function loadDhApprovalOpinionList(){
	var insUid = $("#insUid").val();//流程实例id--ins_uid
	//var activityId = $("#activityId").val();//环节id，activity_id
	$.ajax({
	     url:"dhApprovalOpinion/loadDhApprovalOpinion.do",
	     type : 'POST',
 		 dataType : 'json',
 		 data : {
 			insUid:insUid
			},
	     success : function(result){
	    	 $("#approve_record").empty();
	    	 var info = '<h1 style="clear: both;"></h1>';
	    	 for (var i = 0; i < result.data.length; i++) {
	    		 //var aprOpiComment = result.data[i].aprOpiComment.replace(reg,"\n");
	    		 info += '<li>'
						+'<table>'
						+'<tr>'
							+'<th>环节名称：</th>'
							+'<td>'+result.data[i].activityName+'</td>'
						+'</tr>'
						+'<tr>'
							+'<th>审批状态：</th>'
							+'<td>'+result.data[i].aprStatus+'</td>'
						+'</tr>'
						+'<tr>'
							+'<th>审批人：</th>'
							+'<td>'+result.data[i].aprUserName+'</td>'
						+'</tr>'
						+'<tr>'
							+'<th>岗位：</th>'
							+'<td>'+result.data[i].aprStation+'</td>'
						+'</tr>'
						+'<tr>'
							+'<th>审批时间：</th>'
							+'<td>'+datetimeFormat_1(result.data[i].aprDate)+'</td>'
						+'</tr>'
						+'<tr>'
							+'<th valign=top>审批意见：</th>'
							+'<td>'
							+result.data[i].aprOpiComment
							+'</td>'
						+'</tr>'
						+'</table>'
						+'</li>';
	    	 }
	    	 info += '<h1 style="clear: both;"></h1>';
	    	 $("#approve_record").append(info);
	     },error : function (){
	    	 layer.alert("网络异常！");
	     }
	});
	
}

function save(){
	//var aprOpiComment = layui.layedit.getContent(editIndex);
	var aprOpiComment = $("#myApprovalOpinion").text();
	//alert(aprOpiComment);
	var aprStatus = "";
	var insUid = $("#insUid").val();//流程实例id--ins_uid
	var activityId = $("#activityId").val();//环节id，activity_id
	var taskUid = $("#taskUid").val();
	$.ajax({
	     url:"dhApprovalOpinion/insertDhApprovalOpinion.do",
	     type : 'POST',
		 dataType : 'json',
		 data : {
			"insUid":insUid,
			"activityId":activityId,
			"aprOpiComment":aprOpiComment,
			"aprStatus":aprStatus,
			"taskUid":taskUid
			},
	     success : function(data){
	    	 loadDhApprovalOpinionList();
	    	 layer.alert(data.msg);
	     },error : function (){
	    	 layer.alert(data.msg);
	     }
	});
}


/**
 * 流转信息js开始
 */
function loadDhroutingRecords(){
	var insUid = $("#insUid").val();
	var insId = $("#insId").val();
	
	$.ajax({
	     url:"dhRoutingRecord/loadDhRoutingRecords.do",
	     type : 'POST',
	     data : {
	    	 insUid:insUid,
	    	 insId:insId
	     },
	     dataType:'json',
	     async: false, 
	     success : function(result){
	    	 $(".p").find("p").find("span").empty();
	    	 //$(".p").find("p").eq(0).find("span").html(result.data.bpmActivityMeta.sortNum);环节序号
	    	 var h="";
	    	 var activityNameHtml = "";
	    	 for (var i = 0; i < result.data.bpmActivityMetaList.length; i++) {
				 if(i==(result.data.bpmActivityMetaList.length-1)){
					 h += result.data.bpmActivityMetaList[i].sortNum;
					 activityNameHtml +=result.data.bpmActivityMetaList[i].activityName;
				 }else{
					 h += result.data.bpmActivityMetaList[i].sortNum+"、";
					 activityNameHtml +=result.data.bpmActivityMetaList[i].activityName+"、";
				 }
			 }
	    	 $(".p").find("p").eq(0).find("span").html(h);
	    	 $(".p").find("p").eq(2).find("span").html(activityNameHtml);
	    	 var dhTaskHandlerHtml = "";
	    	 for (var i = 0; i < result.data.dhTaskHandlers.length; i++) {//当前处理人
	    		 if(i==(result.data.dhTaskHandlers.length-1)){
					 dhTaskHandlerHtml +=result.data.dhTaskHandlers[i].sysUser.userName;
				 }else{
					 dhTaskHandlerHtml +=result.data.dhTaskHandlers[i].sysUser.userName+"、";
				 }
	    	 }
	    	 $(".p").find("p").eq(1).find("span").html(dhTaskHandlerHtml);
	    	 //$(".p").find("p").eq(2).find("span").html(result.data.bpmActivityMeta.activityName);
	    	 if(result.data.dhRoutingRecords!=null){
	    	 var index = result.data.dhRoutingRecords.length-1;
	    	 	if(index>=0){
	    	 		var date = new Date(result.data.dhRoutingRecords[index].createTime);
	    	 		$(".p").find("p").eq(3).find("span").html(datetimeFormat_1(date));
	    	 	}
	    	 }
	    	 $("#transferProcess").find("li").remove();
	    	 for (var i = 0; i < result.data.dhRoutingRecords.length; i++) {
	    		var date = new Date(result.data.dhRoutingRecords[i].createTime);
	    		var info = "<li>"
				+"<div>("+(i+1)+")</div>"
				+"<div>"+result.data.dhRoutingRecords[i].userName+"</div>"
				+"<div>岗位："+result.data.dhRoutingRecords[i].station+"</div>"
				+"<div>"+result.data.dhRoutingRecords[i].activityName+"</div>";
				switch(result.data.dhRoutingRecords[i].routeType){
					case "submitTask":
						info += "<div>通过</div>";
						break;
					case "revokeTask":
						info += "<div>取回任务</div>";
						break;
					case "transferTask":
						info += "<div>发起传阅</div>";
						break;
					case "rejectTask":
						info += "<div>驳回</div>";
						break;
					case "addTask":
						info += "<div>发起会签任务</div>";
						break;
					case "finishAddTask":
						info += "<div>完成会签任务</div>";
						break;
					case "trunOffTask":
						info += "<div>管理员撤转任务</div>";
						break;
				}
	    		info += "<div>"+datetimeFormat_1(date)+"</div>"
				+"</li>";
	    		$("#transferProcess").append(info);
			}
	    	 $("#transferProcess").append("<h1 style='clear: both;'></h1>");
	     },error : function (){
	    	 layer.alert("审批意见出现异常！");
	     }
	});
}

