function getSysUser(userUid){
	$.ajax({
		url:common.getPath()+"/sysUser/getSysUser",
		dataType:"json",
		type:"post",
		data: {userUid : userUid},
		beforeSend:function(){
			layer.load(1);
		},
		success:function(result){
			if(result!=null){
				$("input[name='ourPhoneNumber']").val(result.mobile)
			}
			layer.closeAll("loading");
		},
		error:function(){
			layer.closeAll("loading");
		}
	});
	
}


function chooseUsersChange(obj){
	var startIndex = $(obj).val().indexOf("(");
	var endIndex = $(obj).val().indexOf(")", startIndex);
	var userUid = $(obj).val().substring(startIndex + 1, endIndex);
	getSysUser(userUid);
}

function alnalyseClosedStartDate(obj){
	
}
//closedNotifyDate    closedStatusDate
//门店暂闭
function setNotifyAndClosedDate(obj) {
	var startClosedDateStr = $(obj).val();
	if(startClosedDateStr!=null){
	        var day = 24 * 60 * 60 *1000; 

	        var closedDate = new Date(Date.parse(startClosedDateStr.replace(/-/g, "/")));
	        var currDate = new Date();
	        var closedY  = closedDate.getFullYear();
	        var closedM  = closedDate.getMonth()+1;
	        var closedD  = closedDate.getDate();
	        
	        //得到前一天(算头不算尾)
	        //currDate = new Date(currDate.getTime() - day);
	        
	        var days = closedDate.getTime() - currDate.getTime();
	        var time = parseInt(days / day);
	        if(time<=7){
	        	var currY = currDate.getFullYear();
	        	var currM = currDate.getMonth()+1;
	        	var currD = currDate.getDate();
	        	$("input[name='closedNotifyDate']").val(currY+"-"+currM+"-"+currD);
	        }else{
	        	currDate.setDate(date.getDate() + time);
	        	var currY = currDate.getFullYear();
	        	var currM = currDate.getMonth()+1;
	        	var currD = currDate.getDate();
	        	$("input[name='closedNotifyDate']").val(currY+"-"+currM+"-"+currD);
	        }
	        $("input[name='closedStatusDate']").val(closedY+"-"+closedM+"-"+closedD);
	    }
}