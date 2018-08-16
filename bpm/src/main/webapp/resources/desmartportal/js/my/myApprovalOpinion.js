var editIndex;
layui.use('form', function(){
    var form = layui.form;
    form.on('select(useselfChange)', function(data){
    		//$("#demo").val("");
    	$("#myApprovalOpinion").val("");
    	    if(data.value!='-1'){
    	    	//$("#demo").val(data.value);
    	    	//alert($("#demo").val());
    	    	if($("#myApprovalOpinion").val()==null||
    	    			$("#myApprovalOpinion").val()==''){
    	    		$("#myApprovalOpinion").val(data.value);
    	    	}else{
    	    		var info = $("#myApprovalOpinion").val()+data.value;
    	    		$("#myApprovalOpinion").val(info);
    	    	}
    	    }
    	    //form.render();
    	    /*layu8i.use('layedit', function(){
    	    	editIndex = layui.layedit.build("demo");
    	    });*/
    	    /*layui.use('layedit', function(){
    	    layui.layedit.sync(editIndex);
    	    });*/
    });
    
});

var reg=new RegExp("<br>","g"); //创建正则RegExp对象        
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
	     url:common.getPath() +"/dhApprovalOpinion/loadDhApprovalOpinion.do",
	     type : 'POST',
 		 dataType : 'json',
 		 data : {
 			insUid:insUid
			},
	     success : function(result){
	    	 $("#approval_tbody").empty();
	    	 for (var i = 0; i < result.data.length; i++) {
	    		 
	    		 var aprOpiComment = result.data[i].aprOpiComment.replace(reg,"\n");
	    	 var info = "<tr>"
			      +"<th style='background-color: #F2F2F2;color:#000000;' class='approval_th'>环节名称</th>"
			      +"<td>"+result.data[i].activityName+"</td>"
			      +"<th style='background-color: #F2F2F2;color:#000000;' class='approval_th'>审批状态</th>"
			      +"<td>"+result.data[i].aprStatus+"</td>"
			      +"<th style='background-color: #F2F2F2;color:#000000;' class='approval_th'>审批人</th>"
			      +"<td>";
	    	 var taskHandleUserName = result.data[i].taskHandleUserName;
    		 if(taskHandleUserName==null || taskHandleUserName==""){
    			 info += result.data[i].aprUserName;
    		 }else{
    			 info += taskHandleUserName+"("+result.data[i].aprUserName+"代理)";
    		 }
			 info += "</td>"
			      +"<th style='background-color: #F2F2F2;color:#000000;' class='approval_th'>岗位名称</th>"
			      +"<td>"+result.data[i].aprStation+"</td>"
			      +"<th style='background-color: #F2F2F2;color:#000000;' class='approval_th'>审批时间</th>"
			      +"<td>"+datetimeFormat_1(result.data[i].aprDate)+"</td>"								     
			      +"</tr> <tr>"
			      +"<th style='background-color: #F2F2F2;color:#000000;' class='approval_th'>审批意见</th>"
			     /* +"<td colspan='9'><textarea class='layui-textarea'>"+aprOpiComment+"</textarea></td>"*/
			      +"<td colspan='9'><pre style='background-color: #FFFFFF;border: 0px;'>"+aprOpiComment+"</pre></td>"
			      +"</tr>";
	    	 $("#approval_tbody").append(info);
	    	 }
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
	     url:common.getPath() +"/dhApprovalOpinion/insertDhApprovalOpinion.do",
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
	     url:common.getPath() +"/dhRoutingRecord/loadDhRoutingRecords.do",
	     type : 'POST',
	     data : {
	    	 insUid:insUid,
	    	 insId:insId
	     },
	     dataType:'json',
	     async: false, 
	     success : function(result){
	    	drawUpRoutingRecord(result);//画出当前实例的流转记录及当前正在处理的环节信息
	    	
	     },error : function (result){
	    	 layer.alert(result.msg);
	     }
	});
}
/**
 * 画出当前实例的流转记录及当前正在处理的环节信息
 * @param result
 * @returns
 */
function drawUpRoutingRecord(result){
	 //当前处理环节信息开始
	 $("#routingRecordTbody").empty();
	 var info ="";
	 var currentTaskMap = result.data.currentTaskMap;
	 for(let k of Object.keys(currentTaskMap)){
		 let datDhRoutingRecord = currentTaskMap[k];
		 let currActivityMeta = datDhRoutingRecord.bpmActivityMeta;//获得环节信息
		 let currDhTaskInstanceList = datDhRoutingRecord.dhTaskInstanceList;//获得环节对应的任务信息
		 for(let dhTaskInstance of currDhTaskInstanceList){
			 let tr = "<tr>"
			 +"<td>"+currActivityMeta.sortNum+"</td><td>";
			 if(dhTaskInstance.taskAgentUserName != null && dhTaskInstance.taskAgentUserName.trim()!=''){
				 tr+=dhTaskInstance.taskAgentUserName;
			 }else if(dhTaskInstance.taskHandler != null && dhTaskInstance.taskHandler.trim()!=''){
				 tr+=dhTaskInstance.taskHandler;
			 }
			 tr+="</td><td>"+currActivityMeta.activityName+"</td>"
			 +"<td>"
			 +common.dateToString(new Date(dhTaskInstance.taskInitDate))
			 +"</td>";
			 tr+="</tr>";
			 info+=tr;
		 }
	 }
	 $("#routingRecordTbody").append(info);
	 //当前处理信息结束
	 //历史流转记录js开始
	 $("#transferProcess").find("li").remove();
	 for (var i = 0; i < result.data.dhRoutingRecords.length; i++) {
		 var date = new Date(result.data.dhRoutingRecords[i].createTime);
		 var info = "<li>"
			+"<div>("+(i+1)+")</div>"
			+"<div>";
		 var taskHandleUserName = result.data.dhRoutingRecords[i].taskHandleUserName;
		 if(taskHandleUserName==null || taskHandleUserName==""){
			 info += result.data.dhRoutingRecords[i].userName;
		 }else{
			 info += taskHandleUserName+"("+result.data.dhRoutingRecords[i].userName+"代理)";
		 }
		 info += "</div>"
			 +"<div>岗位："+result.data.dhRoutingRecords[i].station+"</div>"
			 +"<div style='height: 36px;'>"+result.data.dhRoutingRecords[i].activityName+"</div>";
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
			case "autoCommit":
               info += "<div>管理员自动提交</div>";
               break;
		}
		info += "<div>"+datetimeFormat_1(date)+"</div>"
		+"</li>";
		$("#transferProcess").append(info);
	}
	 $("#transferProcess").append("<h1 style='clear: both;'></h1>");
	 //历史流转记录js结束
}

