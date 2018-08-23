
$(function(){
	renovatePartFun();
getRectifyToNowDate();
  getOpenToNowDate();
          getToRentDate();
});
function renovatePartFun() {
	var checkVal = $("select[name='renovatePart']").val();
	switch (checkVal) {
	case "1":
		showOrHidePart("nE6t","block");
		showOrHidePart("SFfD","none");
		showOrHidePart("Ntcm","none");
		showOrHidePart("wmmB","none");
		showOrHidePart("aESA","none");
		showOrHidePart("ptyx","none");
		break;
	case "2":
		showOrHidePart("nE6t","none");
		showOrHidePart("SFfD","block");
		showOrHidePart("Ntcm","none");
		showOrHidePart("wmmB","none");
		showOrHidePart("aESA","none");
		showOrHidePart("ptyx","none");
		break;
	case "3":
		showOrHidePart("nE6t","none");
		showOrHidePart("SFfD","none");
		showOrHidePart("Ntcm","block");
		showOrHidePart("wmmB","none");
		showOrHidePart("aESA","none");
		showOrHidePart("ptyx","none");
		break;
	case "4":
		showOrHidePart("nE6t","none");
		showOrHidePart("SFfD","none");
		showOrHidePart("Ntcm","none");
		showOrHidePart("wmmB","block");
		showOrHidePart("aESA","none");
		showOrHidePart("ptyx","none");
		break;
	case "5":
		showOrHidePart("nE6t","none");
		showOrHidePart("SFfD","none");
		showOrHidePart("Ntcm","none");
		showOrHidePart("wmmB","none");
		showOrHidePart("aESA","block");
		showOrHidePart("ptyx","none");
		break;
	case "6":
		showOrHidePart("nE6t","none");
		showOrHidePart("SFfD","none");
		showOrHidePart("Ntcm","none");
		showOrHidePart("wmmB","none");
		showOrHidePart("aESA","none");
		showOrHidePart("ptyx","block");
		break;
	}
}
function getRectifyToNowDate(){
	var startDateStr = $("#date_s2ZK").val();
	var endDateStr = common.dateToString(new Date());
	$("#number_mwRy").val(getDateMonthSub(startDateStr
	           ,endDateStr));
}

function getOpenToNowDate(){
	var startDateStr = $("#date_pWJT").val();
	var endDateStr = common.dateToString(new Date());
	$("#text_EDJN").val(getDateMonthSub(startDateStr
	           ,endDateStr));
}
function getToRentDate(){
	var endDateStr= $("#date_AehK").val();
	var startDateStr = common.dateToString(new Date());
	layer.alert(endDateStr);
	$("#number_WP47").val(getDateMonthSub(startDateStr
	           ,endDateStr));
}

function showOrHidePart(name, status) {
	if(status=="none"){
		$("[name='" + name + "']").hide();
		var pTitle = $("[name='" + name + "']")[0].firstChild.data.trim();
		$("table[title='"+pTitle+"']").hide();
	}else{
		$("[name='" + name + "']").show();
		var pTitle = $("[name='" + name + "']")[0].firstChild.data.trim();
		$("table[title='"+pTitle+"']").show();
	}
}
function getDateMonthSub(startDateStr, endDateStr) {
        var day = 24 * 60 * 60 *1000; 
         var sDate = null;
         var eDate = null;
        if(null!=startDateStr){
           sDate = new Date(Date.parse(startDateStr.replace(/-/g, "/")));
        }
        if(null!=endDateStr){
           eDate = new Date(Date.parse(endDateStr.replace(/-/g, "/")));
        }
       if(sDate != null && eDate != null){
        //得到前一天(算头不算尾)
        sDate = new Date(sDate.getTime() - day);

        //获得各自的年、月、日
        var sY  = sDate.getFullYear();     
        var sM  = sDate.getMonth()+1;
        var sD  = sDate.getDate();
        var eY  = eDate.getFullYear();
        var eM  = eDate.getMonth()+1;
        var eD  = eDate.getDate();

        if(eY > sY ) {
        	return (eY*12+eM)-(sY*12+sM);
        } else {
        	if(eM>sM){
        	  return eM - sM;
        	}else{
              return 0;
        	}
        }
}else{
return 0;
}
}