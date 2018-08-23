
$(function(){
     getOldContractYear();
     $("input[name='contractType']").val(0);
});

function getOldContractYear(){
     var startDateStr = $("#date_r4Hf").val();
     var endDateStr = $("#date_f7pN").val();
     $("input[name='oldContractYear']").val(getDateYearSub(startDateStr
           ,endDateStr));
}

function getDateYearSub(startDateStr, endDateStr) {
if(startDateStr!=null && endDateStr!=null){
        var day = 24 * 60 * 60 *1000; 

        var sDate = new Date(Date.parse(startDateStr.replace(/-/g, "/")));
        var eDate = new Date(Date.parse(endDateStr.replace(/-/g, "/")));

        // 得到前一天(算头不算尾)
        sDate = new Date(sDate.getTime() - day);

        // 获得各自的年、月、日
        var sY  = sDate.getFullYear();     
        var sM  = sDate.getMonth()+1;
        var sD  = sDate.getDate();
        var eY  = eDate.getFullYear();
        var eM  = eDate.getMonth()+1;
        var eD  = eDate.getDate();

        if(eY > sY ) {
          if(eM<sM || (sM==eM && eD<sD)){
             return eY-1-sY;
          }else{
             return eY - sY;
          }
        } else {
            return 0;
        }
    }
}
function getSysUser(userUid,name){
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
				$("input[name='"+name+"']").val(result.mobile)
			}
			layer.closeAll("loading");
		},
		error:function(){
			layer.closeAll("loading");
		}
	});
	
}


function chooseUsersChange(obj,name){
	var startIndex = $(obj).val().indexOf("(");
	var endIndex = $(obj).val().indexOf(")", startIndex);
	var userUid = $(obj).val().substring(startIndex + 1, endIndex);
	getSysUser(userUid,name);
}