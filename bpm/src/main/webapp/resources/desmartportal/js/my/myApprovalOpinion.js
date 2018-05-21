var editIndex;
layui.use('form', function(){
    var form = layui.form;
    form.on('select(useselfChange)', function(data){
    		$("#demo").val("");
    	    if(data.value!='-1'){
    	    	$("#demo").val(data.value);
    	    	//alert($("#demo").val());
    	    }
    	    layui.use('layedit', function(){
    	    	editIndex = layui.layedit.build("demo");
    	    });
    	    /*layui.use('layedit', function(){
    	    layui.layedit.sync(editIndex);
    	    });*/
    });
    
});
$(function(){

    loadDhApprovalOpinionList();
});

function loadDhApprovalOpinionList(){
	var aprUid = "sa";//流程实例id--
	var taskUid = "sa";//环节id，activity_id
	$.ajax({
	     url:"dhApprovalOpinion/loadDhApprovalOpinion.do",
	     type : 'POST',
 		 dataType : 'json',
 		 data : {
 			aprUid:aprUid,
 			taskUid:taskUid
			},
	     success : function(result){
	    	 $("#approval_tbody").empty();
	    	 for (var i = 0; i < result.data.length; i++) {
	    	 var info = "<tr>"
			      +"<th class='approval_th'>环节名称</th>"
			      +"<td>"+result.data[i].activityName+"</td>"
			      +"<th class='approval_th'>审批人</th>"
			      +"<td>"+result.data[i].aprUserName+"</td>"
			      +"<th class='approval_th'>岗位名称</th>"
			      +"<td>"+result.data[i].aprStation+"</td>"
			      +"<th class='approval_th'>审批时间</th>"
			      +"<td>"+datetimeFormat_1(result.data[i].aprDate)+"</td>"								     
			      +"</tr> <tr>"
			      +"<th class='approval_th'>审批意见</th>"
			      +"<td colspan='7'>"+result.data[i].aprOpiComment+"</td>"
			      +"</tr>";
	    	 $("#approval_tbody").append(info);
	    	 }
	     },error : function (){
	    	 layer.alert("网络异常！");
	     }
	});
	
}

function save(){
	var aprOpiComment = layui.layedit.getContent(editIndex);
	alert(aprOpiComment);
	var aprStatus = "ok";
	var aprUid = "sa";
	var taskUid = "sa";
	$.ajax({
	     url:"dhApprovalOpinion/insertDhApprovalOpinion.do",
	     type : 'POST',
		 dataType : 'json',
		 data : {
			aprUid:aprUid,
			taskUid:taskUid,
			aprOpiComment:aprOpiComment,
			aprStatus:aprStatus
			},
	     success : function(data){
	    	 loadDhApprovalOpinionList();
	    	 layer.alert(data.msg);
	     },error : function (){
	    	 layer.alert(data.msg);
	     }
	});
	
	/**
	 * 流转信息js开始
	 */
	
}

