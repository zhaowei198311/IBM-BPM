$(function(){
     getOldContractYear();
});
function checkAndfomartNumber(obj,integer,decimal){
	let num = $(obj).val();
	let l = num.split(".")[0];
	if(l.length>integer){
		layer.alert("格式错误,正确格式为:F"+integer+"."+decimal);
		$(obj).val("");
	}else{
		decimal = decimal > 0 && decimal <= 20 ? decimal : 2;
		num = parseFloat((num + "").replace(/[^\d\.-]/g, "")).toFixed(decimal) + "";
		$(obj).val(num);
	}
}
function fmoney(obj, n)
{
   var s = $(obj).val();
if(s!=null&&s.length>0){
   n = n > 0 && n <= 20 ? n : 2;
   s = parseFloat((s + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";
   var l = s.split(".")[0].split("").reverse(),
   r = s.split(".")[1];
   t = "";
   for(i = 0; i < l.length; i ++ )
   {
      t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");
   }
   $(obj).val(t.split("").reverse().join("") + "." + r);
}
}
function getOldContractYear(){
     var startDateStr = $("#date_r4Hf").val();
     var endDateStr = $("#date_f7pN").val();
     $("input[name='oldContractYear']").val(getDateYearSub(startDateStr
           ,endDateStr));
}

function getRectifyToNowDate(){
	var startDateStr = $("#date_s2ZK").val();
	var endDateStr = new Date();
	$("#number_mwRy").val(getDateYearSub(startDateStr
	           ,endDateStr));
}

function getOpenToNowDate(){
	var startDateStr = $("#date_pWJT").val();
	var endDateStr = new Date();
	$("#text_EDJN").val(getDateYearSub(startDateStr
	           ,endDateStr));
}

function getDateYearSub(startDateStr, endDateStr) {
	if(startDateStr != null && endDateStr != null){
        var day = 24 * 60 * 60 *1000; 
        
        var sDate = new Date(Date.parse(startDateStr.replace(/-/g, "/")));
        var eDate = new Date(Date.parse(endDateStr.replace(/-/g, "/")));

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
          if(eM<sM || (sM==eM && eD<sD)){
             return eY-1-sY;
          }else{
             return eY - sY;
          }
        } else {
            return 0;
        }
    }else{
    	return 0;
    }
}

function getDateMonthSub(startDateStr, endDateStr) {
    var day = 24 * 60 * 60 *1000; 

    var sDate = new Date(Date.parse(startDateStr.replace(/-/g, "/")));
    var eDate = new Date(Date.parse(endDateStr.replace(/-/g, "/")));

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
}

function getDateDaySub(startDateStr, endDateStr) {
	var day = 24 * 60 * 60 *1000; 

    var sDate = new Date(Date.parse(startDateStr.replace(/-/g, "/")));
    var eDate = new Date(Date.parse(endDateStr.replace(/-/g, "/")));

    var days = eDate.getTime() - sDate.getTime();
    if(days>=0){
    	var time = parseInt(days / day);
    	return time;
    }else{
    	return -1;
    }
    
}
function chooseRadioToChange(obj,id){
	if($(obj).val()=='true'){
		$("#"+id).parent().prev().css("visibility","visible");
		$("#"+id).parent().css("visibility","visible");
	}else{
		$("#"+id).parent().prev().css("visibility","hidden");
		$("#"+id).parent().css("visibility","hidden");
	}
}
function isRenewal(obj){
	if($(obj).val()=='true'){
		$("#date_QpkS").parent().prev().children().css("display","block");
		$("#date_QpkS").css("display","block");
	}else{
		$("#date_QpkS").parent().prev().children().css("display","none");
		$("#date_QpkS").css("display","none");
	}
}
function foodBusinessEndDateOnChangeFun(){
	var startDateStr = common.dateToString(new Date());
	var endDateStr = $("input[name='FoodBusinessEndDate']").val();
	var num = getDateDaySub(startDateStr,endDateStr);
	if(num>=0){
		$("input[name='foodBusinessPast'][value='false']").prop("checked",true);
		$("input[name='foodBusinessPast'][value='false']").parent().find(".radio_value").remove();
		$("input[name='foodBusinessPast'][value='false']").parent().prepend("<span class='radio_value' style='margin-left:10px;'>否</span>");
	}else{
		$("input[name='foodBusinessPast'][value='true']").prop("checked",true);
		$("input[name='foodBusinessPast'][value='true']").parent().find(".radio_value").remove();
		$("input[name='foodBusinessPast'][value='true']").parent().prepend("<span class='radio_value' style='margin-left:10px;'>是</span>");
	}
	layui.form.render('radio');
}
function sumCurrentFee(){
	var oldRent = $("input[name='oldRent']").val();
	var oldPropertyManagementFee = $("input[name='oldPropertyManagementFee']").val();
	var oldOtherMicrofinance = $("input[name='oldOtherMicrofinance']").val();
	var sum = 0;
	if(oldRent!=null && oldRent.length>0){
		sum += Number(rmoney(oldRent));
	}
	if(oldPropertyManagementFee!=null&&oldPropertyManagementFee.length>0){
		sum += Number(rmoney(oldPropertyManagementFee));
	}
	if(oldOtherMicrofinance!=null&&oldOtherMicrofinance.length>0){
		sum += Number(rmoney(oldOtherMicrofinance));
	}
	$("input[name='sumOldFee']").val(sum);
	fmoney($("input[name='sumOldFee']"),2);
}
function sumFee(){
	var rent = $("input[name='rent']").val();
	var oldRent = $("input[name='oldRent']").val();
	var propertyManagementFee = $("input[name='propertyManagementFee']").val();
	var otherMicrofinance = $("input[name='otherMicrofinance']").val();
	var sum = 0;
	var rentIncreases = 0;
	var renewRentProportion = 0;
	if(rent!=null&&rent.length>0){
		sum += Number(rmoney(rent));
	}
	if(propertyManagementFee!=null&&propertyManagementFee.length>0){
		sum += Number(rmoney(propertyManagementFee));
	}
	if(otherMicrofinance!=null&&otherMicrofinance.length>0){
		sum += Number(rmoney(otherMicrofinance));
	}
	
	if(rent!=null&&rent.length>0&&oldRent!=null&&oldRent.length>0&&oldRent!=0){
		rentIncreases = ((rmoney(rent)-rmoney(oldRent))/rmoney(oldRent))*100;
	}
	if(rent != null&& rent.length>0 && sum != 0){
		renewRentProportion = (rmoney(rent)/sum)*100;
	}
	$("input[name='sumFee']").val(sum);
	fmoney($("input[name='sumFee']"),2);
	$("input[name='rentIncreases']").val(rentIncreases);
	$("input[name='renewRentProportion']").val(renewRentProportion);
}

function checkCreditCode(){
	var creditCodeDeadline = $("input[name='creditCodeDeadline']").val();
	var creditCodeStartDate = new Date($("input[name='creditCodeStartDate']").val());
	creditCodeStartDate.setMonth(Number(creditCodeStartDate.getMonth())+Number(creditCodeDeadline));
	var startDateStr = common.dateToString(creditCodeStartDate);
	var endDateStr = common.dateToString(new Date());
	var num = getDateDaySub(startDateStr,endDateStr);
	if(num>=0){
		$("input[name='creditCodeIsValid'][value='false']").prop("checked",true);
		$("input[name='creditCodeIsValid'][value='false']").parent().find(".radio_value").remove();
		$("input[name='creditCodeIsValid'][value='false']").parent().prepend("<span class='radio_value' style='margin-left:10px;'>否</span>");
	}else{
		$("input[name='creditCodeIsValid'][value='true']").prop("checked",true);
		$("input[name='creditCodeIsValid'][value='true']").parent().find(".radio_value").remove();
		$("input[name='creditCodeIsValid'][value='true']").parent().prepend("<span class='radio_value' style='margin-left:10px;'>是</span>");
	}
	layui.form.render('radio');
}
function setRenovationPeriod(){
	var date_38zQ = $("input[name='date_38zQ']").val();
	var endDateStr = common.dateToString(new Date());
	$("input[name='number_MiAz'").val(getDateMonthSub(date_38zQ,endDateStr));
}
function setDeadline(){
	var date_hfjf = $("input[name='date_hfjf']").val();
	var endDateStr = common.dateToString(new Date());
	$("input[name='number_cSpY'").val(getDateMonthSub(date_hfjf,endDateStr));
}
$(function(){
	$("input[name='date_jjrX']").val("9999-12-31");
});

function rmoney(s)  
{  
   return parseFloat(s.replace(/[^\d\.-]/g, ""));  
}  
function setNowDate(id){
   $("#"+id).val(common.dateToSimpleString(new Date()));
}