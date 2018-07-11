var index = "";
$(function(){
	var calendar1 = new lCalendar();
	calendar1.init({
		'trigger': '#start_time',
		'type': 'date'
	});

	var calendar2 = new lCalendar();
	calendar2.init({
		'trigger': '#end_time',
		'type': 'date'
	});
});

//打开筛选条件div的方法
function fiterDivShow(){
	$("body").css({"position":"fixed"});
	index = layer.open({
		type: 1
		,content: $("#filter_div")
		,offset: '40px'
		,title: false
    	,shadeClose: true
    	,closeBtn:0
    	,shade: 0.3
    	,anim:2
    	,zIndex:100
    	,resize:false
    	,area: ['width:100%', '300px']
		,end:function(){
			$("#filter_div").css("display","none");
			$("body").css({"position":""});
		}
	});
}

//确认搜索条件的方法
function queryTask(){
	layer.close(index);
}