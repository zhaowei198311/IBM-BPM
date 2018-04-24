$(function(){
	initCollapse();
	
});

// 初始化折叠菜单
function initCollapse() {
	$.ajax({
		url : common.getPath() + "/activityMeta/getActivitiyMetasForConfig",
		type : "post",
		dataType : "json",
		data : {
			"proAppId": proAppId,
			"proUid": proUid,
			"proVerUid": proVerUid
		},
		success : function(result){
			if(result.status == 0){
			    printCollapse(result.data);
			}else{
				layer.alert(result.msg);
			}
		},
		error : function(){
			layer.alert('操作失败');
		}
	});
}
function printCollapse(list) {
	var str = '';
	for (var i=0; i<list.length; i++) {
		var process = list[i];
		var name = process.name;
		var children = process.children;
		str += '<div class="layui-colla-item">'
		    +     '<h2 class="layui-colla-title">'+name+'</h2>';
		if (process.id == 'main') {
			str += '<div class="layui-colla-content layui-show" id="content'+i+'">';
		} else {
			str += '<div class="layui-colla-content " id="content'+i+'">';
		}
		str += '<ul class="link_list">';    
		for (var j=0; j<children.length; j++) {
			var meta = children[j];
			if (meta.activityId == firstHumanMeta) {
				str += '<li class="link_active">'+meta.activityName+'</li>';
			} else {
				str += '<li>'+meta.activityName+'</li>';
			}
		}
		str +=   '</ul>'
			  + '</div>'
			+ '</div>';
	}
	$("#my_collapse").append(str);
	layui.use(['element', 'layer'], function(){
		  var element = layui.element;
		  var layer = layui.layer;
		  
		  //监听折叠
		  element.on('collapse(demo)', function(data){
		    layer.msg('展开状态：'+ data.show);
		  });
	});
	$("#my_collapse li").click(function() {
		clickLi($(this));
	});
}
function clickLi($li) {
	alert($li.html());
	$("#my_collapse li").each(function(){
		if ($(this).hasClass('')) {
			
		}
	});
}
