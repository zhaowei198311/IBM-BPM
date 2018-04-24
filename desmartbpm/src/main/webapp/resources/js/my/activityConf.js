$(function(){
	initCollapse();
	getConfData(firstHumanMeteConf);
	
	$("#back_btn").click(function() {
		window.history.back();
	});
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
				str += '<li data-uid="'+meta.actcUid+'" class="link_active" onclick="clickLi(this);">'+meta.activityName+'</li>';
			} else {
				str += '<li data-uid="'+meta.actcUid+'" onclick="clickLi(this);">'+meta.activityName+'</li>';
			}
		}
		str +=   '</ul>'
			  + '</div>'
			+ '</div>';
	}
	$("#my_collapse").append(str);
	layui.use('element', function(){
		var element = layui.element;
		element.init();
	});
	
}
// 点击 li
function clickLi(li) {
	var $li = $(li);
	if ($li.hasClass('link_active')) {
		return;
	} else {
		$("#my_collapse li").each(function() {
			$(this).removeClass('link_active');
		});
		$li.addClass('link_active');
		console.log($li.data('uid'));
	}
}
function getConfData(actcUid) {
	$.ajax({
		url: common.getPath() + "/activityConf/getData",
		type: "post",
		dataType: "json",
		data: {
			"actcUid": actcUid
		},
		success : function(result){
			if(result.status == 0){
			    console.log(result.data);
			    initConf(result.data);
			}else{
				layer.alert(result.msg);
			}
		},
		error : function(){
			layer.alert('操作失败,请稍后再试');
		}
	});
}
function initConf(map) {
	var conf = map.conf;
	
}
