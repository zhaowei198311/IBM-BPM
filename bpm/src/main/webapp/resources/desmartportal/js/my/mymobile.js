var form = null;
$(function(){
	layui.use(['form', 'layedit', 'laydate','element'], function () {
		form = layui.form, layer = layui.layer, element = layui.element,
			layedit = layui.layedit, laydate = layui.laydate;
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
	});
});

window.onclick=function(){
	$("#child_menu").css("display","none");
}

//选择处理方式
function handleBtnClick(obj){
	$(".handle_btn").css({"background":"#fff","color":"#111","border-color":"#ccc"});
	$(obj).css({"background":"#FEE9DD","color":"#FC9E69","border-color":"#FDBD98"});
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

/*setInterval(function isTop(){
	var top = $('html')[0].scrollTop;
	console.log(top);
	if(top==0){
		$(".layui-fixbar-top").css("display","none");
	}else{
		$(".layui-fixbar-top").css("display","list-item");
	}
},500);*/