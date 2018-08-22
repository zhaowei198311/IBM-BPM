$(function () {
	$(".ul_li").mCustomScrollbar();
	// 菜单栏 生命周期 鼠标移入事件 active
	$("#shopLife").hover(function () {
		$(".typeColor").css("background-color", "#001529").css("color", "#B3B9BF");
	    $("#shopLife").css("background-color", "#1890ff").css("color", "#fff");
	    $(".menu_container").show();
	});
	
	//除生命周期外的菜单 点击事件
	$(".typeColor").click(function () {
        if ($(this).parent().prop("id") == "approvalTask" || $(this).parent().prop("id") == "finishedTask") {
            var text = $(this)[0].firstChild.data.trim();
            $(".layui-breadcrumb").find("a:eq(0)").text("我的任务");
            $(".layui-breadcrumb").find("cite").text(text);
            $(".layui-breadcrumb").css("display", "block");
        }else if($(this).parent().prop("id") == "projectStatement" || $(this).parent().prop("id") == "formBusinessReport" 
        	|| $(this).parent().prop("id") == "storeBusinessReport"){
        	var text = $(this)[0].firstChild.data.trim();
            $(".layui-breadcrumb").find("a:eq(0)").text("报表管理");
            $(".layui-breadcrumb").find("cite").text(text);
            $(".layui-breadcrumb").css("display", "block");
        } else {
        	var text = $(this).find("span").text();
        	if(text!=null && text!=""){
        		$(".layui-breadcrumb").find("a:eq(0)").text("");
        		$(".layui-breadcrumb").find("cite").text(text);
        		$(".layui-breadcrumb").css("display", "block");
        	}else{
        		$(".layui-breadcrumb").css("display", "none");
        	}
        }
        $(".typeColor").not(this).css("background-color", "#001529").css("color", "#B3B9BF");
        $("#shopLife").css("background-color", "#001529");
        $(this).css("background-color", "#1890ff").css("color", "#fff");
    })
    //除生命周期外的菜单 鼠标移入事件
    $(".layui-nav-item a").not("#shopLife").mouseover(
        function () {
            $(".layui-nav-item a").not(".menu_container").each(function () {
                $(".menu_container").css("display", "none");
            });
            if ($(".menu_container").is(":hidden")) {
                $(".layui-nav.layui-nav-tree").find(".layui-this").find("a").css("background-color", "#1890ff").css("color", "#fff");
                var id = $(".layui-nav.layui-nav-tree").find(".layui-this").find("a").attr("id");
                if (id != "shopLife") {
                    $("#shopLife").css("background-color", "#001529").css("color", "#B3B9BF");
            }
        }
    });
	
	//获得父分类下
	queryByParent("menu0",'rootCategory');
	
	//流程地图菜单的移入移出事件
	$(window).load(function(){
        $(".ul_li").mCustomScrollbar();
    });
})
//渲染菜单
function drawMenu(){
	for(var i=0;i<$(".menu").length;i++){
		$(".menu"+i).find($(".ul_li ul li")).each(function(_index){
			var num = i;
			$(this).hover(function(){
				if($(this).attr("class")!="category active"){
					var categoryUid = $(this).attr("data-categoryuid");
					if($(this).text()!=null || $(this).text()!=""){
						var text = $(this)[0].firstChild.data;
						queryByParent("menu"+(num+1),categoryUid);
						queryPorcess("menu"+(num+1),categoryUid);
						$(".menu"+(num+1)).find(".menu_title_span").text(text);
		        	}
				}
				$(".menu"+num).find(".ul_li ul li").removeClass("active").eq(_index).addClass("active");		
				$(".menu"+(num+1)).css("display","block");
				$(".menu"+(num+2)).css("display","none");
				$(".menu"+(num+3)).css("display","none");
			});
		});
	}
}

//根据父分类id 查出子分类并渲染
function queryByParent(className,categoryuid) {
	if(categoryuid=="" || categoryuid==null){
		return;
	}
    $.ajax({
        url: common.getPath() + '/processCategory/queryByParent',
        type: 'post',
        dataType: 'json',
        async: false,
        data: {
            categoryParent: categoryuid
        },
        success: function (result) {
        	if(result.status==0){
        		var list = result.data;
        		$("."+className).find(".ul_li .category").remove();
                for (var i = 0; i < list.length; i++) {
                    var liHtml = '<li class="category" data-categoryuid="' + list[i].categoryUid + '">'
                    		+ list[i].categoryName + '<i class="layui-icon category_icon">&#xe602;</i></li>';
                    $("."+className).find(".ul_li ul").append(liHtml);
                }
                drawMenu();
        	}else{
        		layer.alert("查询门店生命周期失败")
        	}
        },
        error: function (result) {
            layer.alert("查询门店生命周期失败")
        }
    })
};
//根据父分类id 查出子流程并渲染
function queryPorcess(className,categoryuid) {
	if(categoryuid=="" || categoryuid==null){
		return;
	}
    $.ajax({
        url: common.getPath() + '/processMeta/searchByCategoryUid',
        type: 'post',
        dataType: 'json',
        async: false,
        data: {
            categoryUid: categoryuid
        },
        success: function (result) {
        	if(result.status==0){
        		var list = result.data;
        		$("."+className).find(".ul_li .processMeta").remove();
                for (var i = 0; i < list.length; i++) {
                    var liHtml = '<li class="processMeta" data-prouid="'+list[i].proUid
                    		+'"><a onclick="hideHead(this);" href="menus/processInstanceByUser?proUid='
		                    +list[i].proUid 
		                    +'&proAppId=' 
		                    +list[i].proAppId 
		                    +'" target="iframe0">'+list[i].proName+'</a></li>';
                    $("."+className).find(".ul_li ul").append(liHtml);
                }
        	}
        }
    });
}
//更换导航栏
function hideHead(obj) {
    $(".layui-nav.layui-nav-tree").find(".layui-this").removeClass("layui-this");
    $("#shopLife").parent().addClass("layui-this");
    $(".menu_container").css("display", "none");
    $(".layui-breadcrumb").css("display", "none");
}