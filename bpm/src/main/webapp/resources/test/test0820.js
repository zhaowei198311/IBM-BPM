
//局翻
function setRenovateCase(){
	//设置门店局翻原因网关条件 renovate_case_store
	let renovateCase = $("[name='renovate_case'").val();
		if(renovateCase!=null){
			let renovateCaseArr= renovateCase.split(",");
			for ( let item of renovateCaseArr) {
			switch(item){
			case "商超移位":
				$("input[name='renovate_case_store'").val(item);
				break;
			case "网点优化":
				$("input[name='renovate_case_network'").val(item);
				break;
			case "影响门店形象":
				$("input[name='renovate_case_shop'").val(item);
			break;
			}	
			}
	}
}
function isRenewalClick(){
	var flag = $("input[name='isRenewal']:checked").val();
	if(flag=='true'){
		var currDate = new Date();
		  var currY  = currDate.getFullYear();     
	      var currM  = currDate.getMonth()+1;
	      var currD  = currDate.getDate();
		$("input[name='renewalDate']").val(currY+"-"+currM+"-"+currD);
	}else if(flag=='false'){
		$("input[name='renewalDate']").val("");
	}
chooseRadioToChange($("input[name='isRenewal']:checked"),'date_KNhw');
}
function check_before_submit(){
	setRenovateCase();
	return true;
}

//续租

//common
function CompareDate(id1,id2)
{
	var d1 = $("#"+id1).val();
	var d2 = $("#"+id2).val();
    if((new Date(d1.replace(/-/g,"\/"))) > (new Date(d2.replace(/-/g,"\/")))){
    	layer.msg("日期填写错误,起始日期不能大于结束日期", {
			icon: 2
		});
    	$("#"+id2).val("");
    }
}
