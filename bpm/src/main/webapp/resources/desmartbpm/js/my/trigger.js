// 为翻页提供支持
var pageConfig = {
	pageNum: 1,
	pageSize: 10,
	total: 0
}

// 加载事件
$(document).ready(function(){  		
// 加载数据
getTriggerInfo();

// 表单验证
$('#form1').validate({
    rules : {
    	triTitle : {
    		required : true
        },
        triType: {
        	required: true
        },
    }
})
})

function getTriggerInfo() {
$.ajax({
	url : common.getPath() + "/trigger/search",
	type : "post",
	dataType : "json",
	data : {
		"pageNum" : pageConfig.pageNum,
		"pageSize" : pageConfig.pageSize
	},
	success : function(result) {
		if (result.status == 0) {
			drawTable(result.data);
		}
	}
});
}	

// 请求数据成功
function drawTable(pageInfo) {
pageConfig.pageNum = pageInfo.pageNum;
pageConfig.pageSize = pageInfo.pageSize;
pageConfig.total = pageInfo.total;
doPage();
// 渲染数据
$("#trigger_table_tbody").html('');
if (pageInfo.total == 0) {
	return;
}

var list = pageInfo.list;
var startSort = pageInfo.startRow;// 开始序号
var trs = "";
for (var i = 0; i < list.length; i++) {
	var meta = list[i];
	var sortNum = startSort + i;
	var createTime = "";
	var updateTime = "";
	if (meta.createTime) {
		createTime = common.dateToString(new Date(meta.createTime));
	}
	if (meta.updateTime) {
		updateTime = common.dateToString(new Date(meta.updateTime));
	}
	trs += '<tr><td>'
			+ sortNum 
			+ '</td>' 
			+ '<td>' 
			+ meta.triTitle 
			+ '</td>'
			+ '<td>' 
			+ meta.triDescription 
			+ '</td>' 
			+ '<td>'
			+ meta.triType 
			+ '</td>' 
			+ '<td>' 
			+ meta.triWebbot
			+ '</td>' 
			+ '<td>' 
			+ meta.triParam 
			+ '</td>' 
			+ '<td>'
			+ meta.creator 
			+ '</td>' 
			+ '<td>' 
			+ createTime 
			+ '</td>'
			+ '<td>'
			+ '<i class="layui-icon"  title="修改触发器"  onclick=updatate("'
			+ meta.triUid + '") >&#xe642;</i>'
			+ '<i class="layui-icon"  title="删除触发器"  onclick=del("'
			+ meta.triUid + '") >&#xe640;</i>'
			+ '</td>'
			+ '</tr>';
}
$("#trigger_table_tbody").append(trs);

}

// 分页
function doPage() {
layui.use([ 'laypage', 'layer', 'form', 'jquery' ], function() {
	var laypage = layui.laypage, layer = layui.layer, form = layui.form;
	var $= layui.jquery;
	// 完整功能
	laypage.render({
		elem : 'lay_page',
		curr : pageConfig.pageNum,
		count : pageConfig.total,
		limit : pageConfig.pageSize,
		layout : [ 'count', 'prev', 'page', 'next', 'limit', 'skip' ],
		jump : function(obj, first) {
			// obj包含了当前分页的所有参数
			pageConfig.pageNum = obj.curr;
			pageConfig.pageSize = obj.limit;
			if (!first) {
				getTriggerInfo();
			}
		}
	});
	
	form.on('select(triType)', function(data){
		var value=data.value;
		byParameterTypeHideAndShowElement(value,"");
		if(data.value == 'interface'){
			// 请求ajax 获取接口数据
			$.ajax({
				url : common.getPath()+'/interfaces/queryDhInterfaceList',
				type : 'post',
				dataType : 'json',
				data : {
				},
				success : function(result){
					var data = result.data.list;
					 for(var i=0; i<data.length; i++){
						 var trs = '<option value="'+data[i].intUid+'">'
							 + data[i].intTitle
							 + '</option>';
						$("#triWebbotType").append(trs)
					 } 
					
					 form.render();
				},
				error : function (){
					layer.msg('查询接口异常', {
						icon : 5
					});
				}
			})
			form.render();
		}
	});
	
	function byParameterTypeHideAndShowElement(paraType,selector){
		var triWebbot=$('.triWebbot'); // 触发器执行命令
		var trijiekou=$('.triInterface'); // 选择接口
		var triParam = $('.triParam'); // 参数
		var triType = $('.triType');
		switch (paraType) {
		case 'sql':
			triWebbot.show();
			triParam.show();
			trijiekou.hide();
			clearTableData();
			break;
		case 'javaclass':
			triWebbot.show();
			triParam.show();
			trijiekou.hide();
			clearTableData();
			break;
		case 'interface':
			trijiekou.css("display", "block");
			triParam.show();
			triWebbot.hide();
			$("#triWebbotType").empty();
			clearTableData();
			break;	
		case 'script':
			triWebbot.show();
			triParam.show();
			trijiekou.hide();
			clearTableData();
			break;	
		}
		form.render();
	}
	
	function clearTableData(){
		 $("#triWebbot").val("");
		 $("#triParam").val("");
		 $("#triTitle").val("");
		 $("#triDescription").val("");
	}
});
}


function del(triUid) {
layer.confirm('是否删除该触发器？', {icon: 3, title:'提示'}, function(index){
	$.ajax({
		url : common.getPath()+'/trigger/delete',
		type : 'POST',
		dataType : 'text',
		data : {
			triUid : triUid
		},
		success : function(result) {
			window.location.href = common.getPath()+"/trigger/index";
		}
	})
	  layer.close(index);
	}); 
}

$(".search_btn").click(function(){
var name = document.getElementById("triggerName_input").value;
var type = document.getElementById("triggerType_select").value;
$.ajax({
	url : common.getPath()+'/trigger/search',
	type : 'POST',
	dataType : 'json',
	data : {
		triTitle : name,
		triType : type
	},
	success : function(result) {
		if (result.status == 0) {
			drawTable(result.data);
		}
	}
})
})

$(".create_btn").click(function(){
$(".display_container").css("display", "block");
})

$(".cancel_btn").click(function(){
$(".display_container").css("display", "none");
$("#form1").validate().resetForm();
})

$(".sure_btn").click(function(){
// 新增触发器
	var triTitle = $("#triTitle").val();
	var triWebbot = $("#triWebbot").val();
	var triType = $("#triType").val();
	var triDescription = $("#triDescription").val();
	var triParam = $("#triParam").val();
	if(triTitle.replace(/(^s*)|(s*$)/g, "").length !=0 && triType !=null){
		var webbot = "";
		var options=$("#triWebbotType option:selected");
		if($("#triType").val()=="interface"){
			webbot = options.val();
		}else{
			webbot = $("#triWebbot").val();
		}
			$.ajax({
			url : common.getPath()+'/trigger/save',
			type : 'POST',
			dataType : 'text',
			data : {
				triTitle : triTitle,
				triDescription : triDescription,
				triType : triType,
				triWebbot : webbot,
				triParam : triParam
			},
			success : function(result){
				window.location.href = common.getPath()+'/trigger/index'
					},
					error : function(result) {
						layer.msg('添加失败', {
							icon : 5
						});
					}
				})
		}else{
			layer.alert('参数不能为空')
		}
})