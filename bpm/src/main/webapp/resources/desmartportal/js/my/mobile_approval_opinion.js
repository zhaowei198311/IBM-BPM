var editIndex;
layui.use('form', function(){
    var form = layui.form;
	form.on('select(useselfChange)', function(data) {
		$("#myApprovalOpinion").val("");
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
	     url:common.getPath()+"/dhApprovalOpinion/loadDhApprovalOpinion.do",
	     type : 'POST',
 		 dataType : 'json',
 		 data : {
 			insUid:insUid
			},
	     success : function(result){
	    	 $("#approve_record").empty();
	    	 if(result.data.length==0){
	    		 $("#approve_record").parent().css("display","none");
	    	 }
	    	 var info = '';
	    	 for (var i = 0; i < result.data.length; i++) {
	    		 var lastName = "";//姓
	    		 var taskHandleUserName = result.data[i].taskHandleUserName;
	    		 if(taskHandleUserName==null || taskHandleUserName==""){
	    			 lastName = common.splitName(result.data[i].aprUserName);
	    		 }else{
	    			 lastName = common.splitName(taskHandleUserName);
	    		 }
	    		 info += '<li class="layui-timeline-item">';
	    		 if(lastName.length>1){
	    			 info += '<i class="layui-icon layui-timeline-axis" style="font-size:12px;">'+lastName+'</i>';
	    		 }else{
	    			 info += '<i class="layui-icon layui-timeline-axis">'+lastName+'</i>';
	    		 }
	    		 info += '<div class="layui-timeline-content layui-text">'
	    			 +'<h5 class="layui-timeline-title">'
	    			 +'<span class="activity_name">'+result.data[i].activityName+'</span>'
	    			 +'<span class="approval_time">'+datetimeFormat_1(result.data[i].aprDate)+'</span>'
	    			 +'</h5>'
	    			 +'<p class="timeline_p">'
	    			 +'<span class="approval_person">';
	    		 if(taskHandleUserName==null || taskHandleUserName==""){
	    			 info += result.data[i].aprUserName;
	    		 }else{
	    			 info += taskHandleUserName+"("+result.data[i].aprUserName+"代理)";
	    		 }
	    		 info += '</span>'
	    			 +'<span class="person_despart">'+result.data[i].aprStation+'</span>'
	    			 +'<span class="approval_status">'+result.data[i].aprStatus+'</span>'
	    			 +'</p>'
	    			 +'<p>'
	    			 +'<span class="approval_sugg">';
	    		 if(result.data[i].aprOpiComment.length>25){
	    			 info += '<span class="partComment">'
						 +result.data[i].aprOpiComment.substring(0,25)
						 +'......</span>'
		    			 +'<span class="hiddenAprOpiComment" style="display:none;">'
						 +result.data[i].aprOpiComment
						 +'</span>'
		    			 +'<p class="retractAllComment allComment" style="display:none;" onclick="retractAllFun(this);">'
		    			 +'点击收起 <i class="layui-icon" style="font-size:14px">&#xe619;</i>'
		    			 +'</p>'
		    			 +'<p class="extendAllComment allComment" onclick="extendAllFun(this);">'
		    			 +'展开全部 <i class="layui-icon" style="font-size:14px">&#xe61a;</i>'
		    			 +'</p>';
	    		 }else{
	    			 info += result.data[i].aprOpiComment
	    		 }
	    		 info += '</span>'
	    			 +'</p>'
	    			 +'</div>'
					+'</li>';
	    	 }
	    	 $("#approve_record").append(info);
	     },error : function (){
	    	 layer.alert("网络异常！");
	     }
	});
}

//展开一条审批意见的方法
function extendAllFun(obj){
	$(obj).hide();
	$(obj).parent().find(".partComment").hide();
	$(obj).parent().find(".hiddenAprOpiComment").show();
	$(obj).parent().find(".retractAllComment").show();
}

//收起一条审批意见的方法
function retractAllFun(obj){
	$(obj).hide();
	$(obj).parent().find(".partComment").show();
	$(obj).parent().find(".hiddenAprOpiComment").hide();
	$(obj).parent().find(".extendAllComment").show();
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
	     url:common.getPath()+"/dhApprovalOpinion/insertDhApprovalOpinion.do",
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
	     url:common.getPath()+"/dhRoutingRecord/loadDhRoutingRecords.do",
	     type : 'POST',
	     data : {
	    	 insUid:insUid,
	    	 insId:insId
	     },
	     dataType:'json',
	     async: false, 
	     success : function(result){
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
	    	 var dhTaskHandlerHtml = "";
	    	 for (var i = 0; i < result.data.dhTaskHandlers.length; i++) {
	    		 if(i==(result.data.dhTaskHandlers.length-1)){
					 dhTaskHandlerHtml +=result.data.dhTaskHandlers[i].sysUser.userName;
				 }else{
					 dhTaskHandlerHtml +=result.data.dhTaskHandlers[i].sysUser.userName+"、";
				 }
	    	 }
	    	 var dateStr = "";
	    	 if(result.data.dhRoutingRecords!=null){
	    		 var index = result.data.dhRoutingRecords.length-1;
	    	 	 if(index>=0){
	    	 		var date = new Date(result.data.dhRoutingRecords[index].createTime);
	    	 		dateStr = datetimeFormat_1(date);//当前处理到达时间
	    	 	 }
	    	 }
	    	 var currActiHtml = "<p>当前的环节号："+h+"</p>"
				+"<p>当前处理人："+dhTaskHandlerHtml+"</p>"
				+"<p>当前处理环节："+activityNameHtml+"</p>"
				+"<p>当前处理到达时间："+dateStr+"</p>";
	    	 $(".curr_activity").html(currActiHtml);
	    	 if(result.data.dhRoutingRecords.length==0){
	    		 $("#transferProcess").parent().css("display","none");
	    	 }
	    	 for (var i = 0; i < result.data.dhRoutingRecords.length; i++) {
	    		var date = new Date(result.data.dhRoutingRecords[i].createTime);
	    		var info = '<li class="layui-timeline-item">'
					+'<i class="layui-icon layui-timeline-axis">&#xe63f;</i>'
					+'<div class="layui-timeline-content layui-text">'
					+'<h5 class="layui-timeline-title">'
					+'<span class="activity_name">'+result.data.dhRoutingRecords[i].activityName+'</span>'
					+'<span class="approval_time">'+datetimeFormat_1(date)+'</span>'
					+'</h5>'
					+'<p class="timeline_p">'
					+'<span class="approval_person">';
	    		var taskHandleUserName = result.data.dhRoutingRecords[i].taskHandleUserName;
	    		if(taskHandleUserName==null || taskHandleUserName==""){
	    			info += result.data.dhRoutingRecords[i].userName;
	    		}else{
	    			info += taskHandleUserName+"("+result.data.dhRoutingRecords[i].userName+"代理)";
	    		}
	    		info += '</span>'
					+'<span class="person_despart">'+result.data.dhRoutingRecords[i].station+'</span>';
	    		switch(result.data.dhRoutingRecords[i].routeType){
					case "submitTask":
						info += "<span class='approval_status'>通过</span>";
						break;
					case "revokeTask":
						info += "<span class='approval_status' style='color:#FC9153'>取回</span>";
						break;
					case "transferTask":
						info += "<span class='approval_status' style='color:#FC9153'>传阅</span>";
						break;
					case "rejectTask":
						info += "<span class='approval_status' style='color:#f33640'>驳回</span>";
						break;
					case "addTask":
						info += "<span class='approval_status' style='color:#FC9153'>发起会签</span>";
						break;
					case "finishAddTask":
						info += "<span class='approval_status' style='color:#FC9153'>完成会签</span>";
						break;
					case "trunOffTask":
						info += "<span class='approval_status' style='color:#f33640'>撤转</span>";
						break;
	    		}
	    		info += '</span>'
					+'</p>'
					+'</div>'
					+'</li>';
	    		$("#transferProcess").append(info);
			}
	     },error : function (){
	    	 layer.alert("审批意见出现异常！");
	     }
	});
}

