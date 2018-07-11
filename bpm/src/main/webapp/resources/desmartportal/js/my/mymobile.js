var form = null;
$(function(){
	layui.use(['form', 'layedit', 'laydate'], function () {
		form = layui.form, layer = layui.layer, layedit = layui.layedit, laydate = layui.laydate;
		form.render();
	});
	
	$("#operate_menu").click(function(e){
		e.stopPropagation();
		$("#child_menu").toggle(150);
	});
	
	$(".layui-fixbar-top").click(function(){
		jQuery('html,body').animate({
		    scrollTop: 0
		}, 300);
		$(".layui-fixbar-top").css("display","none");
	});
});

window.onclick=function(){
	$("#child_menu").css("display","none");
}

//切换tab的方法
function menuBtnClick(obj){
	$(".mobile_menu li").css({"color":"#A0A0A0"});
	$(obj).parent().css({"color":"#009688"});
	var id = $(obj).attr("title");
	$(".middle_content").css("display","none");
	$("#"+id+"_div").css("display","block");
}

//选择处理方式
function handleBtnClick(obj){
	$(".handle_btn").css("background","#AAA");
	$(obj).css("background","#009688");
	$(".handle_table").css("display","none");
	var handleType = $(obj).text();
	switch(handleType){
		case "同意":{
			$("#submit_table").css("display","block");
			$("#suggestion").css("display","block");
			break;
		};
		case "会签":{
			$("#countersign_table").css("display","block");
			$("#suggestion").css("display","none");
			break;
		};
		case "驳回":{
			$("#reject_table").css("display","block");
			$("#suggestion").css("display","block");
			break;
		};
		case "传阅":{
			$("#transfer_table").css("display","block");
			$("#suggestion").css("display","none");
			break;
		};
	}
}

function isTop(){
	var top = $('html')[0].scrollTop;
	if(top==0){
		$(".layui-fixbar-top").css("display","none");
	}else{
		$(".layui-fixbar-top").css("display","list-item");
	}
	setTimeout("isTop()",500);
}

setTimeout("isTop()",500);