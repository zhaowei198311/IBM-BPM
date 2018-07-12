
//分组名称事件
function dothis(v){
    if(v.value == ''){
        return false;
    }
    //小写转大写
    v.value = v.value.toUpperCase();
    //判断是否为英文
    if(!/^([A-Za-z]+\s?)*[A-Za-z]$/.test(v.value)){
        return v.value = v.value.substr(0, v.value.length - 1);
    }
    //判断是否超过长度
    if(v.value.length > 10){
        return v.value = v.value.substr(0, 9);
    }
}
//优先级序号事件
function doVersion(v){
	if(!/^[1-9]\d*$/.test(v.value)){
        return v.value = v.value.substr(0, v.value.length - 1);
    }
	//判断是否超过长度
    if(v.value.length > 10){
        return v.value = v.value.substr(0, 9);
    }
}

layui.use('form', function() {
	var form = layui.form;
	//全选  
	form.on('checkbox(allChoose)', function(data){  
		var child = $(data.elem).parents('table').find('tbody input[type="checkbox"]');  
		child.each(function(index, item){  
			item.checked = data.elem.checked;  
		});  
		form.render('checkbox');  
	});  
});
/**
 * 绑定create_net,delete_net事件
 * @returns
 */
$(function() {
	
	$(".create_net").click(function() {
		$(".display_container5").find(".top").text("新增网关");
		$("#addToArr").text("确定");
		$("#addToArr").unbind("click");
		$("#addToArr").click(function(){saveDatRule("");});
		$(".display_container5").css("display", "block");
	})
	$(".delete_net").click(function(){
		var checkB = $(this).parent().find("input[name!='allChecked']:checked");
		var dataArr = new Array();
		var activityId =  $("#gatewayActivityName").data("activityId");
		$(checkB).each(function(){
			var ruleId = $(this).data('ruleid');
			var conditionId = $(this).data('conditionid');
			var info = {"ruleId":ruleId,"conditionId":conditionId};
			dataArr.push(info);
		})
		$.ajax({
			url:common.getPath()+"/datRule/deleteDatRule?activityId="+activityId,
			type:"post",
			data: JSON.stringify(dataArr),
			contentType: "application/json",
			dataType:'json',
			async: false,  
			success: function(result){
				layer.alert(result.msg);
				if(result.success){
					$("#gatewayTab").find("tbody").empty();
					printDatRuleCondition(result.data.DatConditionList);
					//画出做过更改的规则
					printUpdateDatRuleList(result.data.PredictRules);
				}
				layui.form.render();
			},error: function(result){
				layer.alert(result.msg);
			}
		});
		 	checkB.parent().parent().remove();
		/*}*/
	})

});
/** 改动网关配置开始 **/
var activityType = "gateway";
//页面加载完成
$(function(){
	initFoldHurdle();
	
	$("#snapshotFlowChart_btn").click(function() {
		/*var cks = $("[name='definition_ck']:checked")
		if (!cks.length) {
			layer.alert("请选择一个流程定义");
			return;
		}
		if (cks.length > 1) {
			layer.alert("请选择一个流程定义，不能选择多个");
			return;
		}*/
		/*var ck = cks.eq(0);
		var proUid = ck.data('prouid');
		var proVerUid = ck.data('proveruid');
		var proAppId = ck.data('proappid');
*/
		$.ajax({
			url : common.getPath() + "/processDefinition/snapshotFlowChart",
			dataType : "text",
			type : "POST",
			data : {
				"proUid" : proUid,
				"proVerUid" : proVerUid,
				"proAppId" : proAppId
			},
			success : function(result) {
				layer.open({
					type : 2,
					title : '流程快照',
					shadeClose : true,
					shade : 0.3,
					area : [ '790px', '580px' ],
					content : result
				});
			}
		})
	})
})

//初始化网关左侧折叠菜单
function initFoldHurdle() {
    $.ajax({
        url : common.getPath() + "/datRule/loadGatewaySet",
        type : "post",
        dataType : "json",
        data : {
            "proAppId": proAppId,
            "bpdId": proUid,
            "snapshotId": proVerUid,
            "activityType":activityType
        },
        success : function(result){
            if(result.status == 0){
            	printFoldHurdle(result.data.leftMenus);
            }else{
                layer.alert(result.msg);
            }
            $("#gatewayTab").find("tbody").empty();//只清空一次
            printRightGatewayDetails(result.data.rightDetailsList);
            	layui.form.render("checkbox");
        },
        error : function(){
            layer.alert('操作失败');
        }
    });
}
//画出折叠栏
function printFoldHurdle(list) {
	if(list!= undefined && list!=null &&list.length>0){
    var str = '';
    str += '<div class="layui-colla-item">'
    	+     '<h2 class="layui-colla-title">网关环节线路配置</h2>';
    str += '<div class="layui-colla-content layui-show">';
    str += '<ul class="link_list">';
    for (var i=0; i<list.length; i++) {
        var bpmActivityMeta = list[i];
        
            if (i == 0) {
                str += '<li data-uid="'+bpmActivityMeta.activityId+'" class="link_active" onclick="clickGatewayLi(this);">'+bpmActivityMeta.activityName+'</li>';
                //$("#gatewaySet_tab_title").append("<li class='layui-this' >"+bpmActivityMeta.activityName+"</li>");
                $("#gatewayActivityName").val(bpmActivityMeta.activityName);
                $("#gatewayActivityName").data("activityId",bpmActivityMeta.activityId);
            } else {
                str += '<li data-uid="'+bpmActivityMeta.activityId+'" onclick="clickGatewayLi(this);">'+bpmActivityMeta.activityName+'</li>';
            }
    }
    str += '</ul>'
        + '</div></div>';
    $("#my_fold_hurdle").append(str);
    layui.use('element', function(){
        var element = layui.element;
        element.init();
    });
	}
}

/**/
//画出右侧
function printRightGatewayDetails(list){
	if(list!= undefined && list!=null &&list.length>0){
	$("#gateway_result").append('<option value="">--请选择--</option>');
	for (var i = 0; i < list.length; i++) {
		var str = '<div class="layui-form-item">'
			+'<label class="layui-form-label">下一节点</label>'
			+'<div class="layui-input-block">'
			+'<table class="layui-table" lay-even lay-skin="nob">'
			+'<colgroup><col width="40%"><col width="60%"></colgroup><tbody class="gatewayTbody">'
			+'<tr><td><label>'
			var map = list[i];
			for (key in map) {
				if(map[key]=='default'||map[key]==='default'){
					str += key+'</label></td><td>规则名称：<label>'
						+'默认节点</label></td></tr></tbody></table></div></div>';
				}else{
					str += key+'</label></td><td>规则名称：<label>'
						+map[key].PredictRule.ruleName.replace(/"([^"]*)"/g, "'$1'")+'</label></td></tr></tbody></table></div></div>';
					str += '<div class="layui-form-item">'
					+'<label style="position: relative;bottom: 11px;" class="layui-form-label">组合预览</label>'
					+'<div class="layui-input-block">'
					+'<span id = "predictRule_'+map[key].PredictRule.ruleId+'">'
					+map[key].PredictRule.ruleProcess+'</span></div></div>';
					//将规则名、规则id添加到新增模态框中
					var info = '<option value="'+map[key].PredictRule.ruleId+'">'
					+map[key].PredictRule.ruleName.replace(/"([^"]*)"/g, "'$1'")+'</option>';
					$("#gateway_result").append(info);
					//画出当前环节的所有的规则条件
					printDatRuleCondition(map[key].DatConditionList);
				}
			}
			$("#rightGatewayDetails").append(str);//将规则名以及规则预览画出
	}
		layui.use('form',function(){
			var form = layui.form;
			form.render('select');
		});
	}
}
//循环画出规则条件
function printDatRuleCondition(itemList){
	$("#gateway_result").find("option").eq(0).prop("selected",true);//操作后将路由变量默认置为请选择
	
	var tbody = $("#gatewayTab").find("tbody");
	//tbody.empty();
	for (var i = 0; i < itemList.length; i++) {
	var tr = "<tr style = 'cursor:pointer;'>" +
			"<th><input type='checkbox' data-ruleid='"+itemList[i].ruleId +
			"' data-conditionid='"+itemList[i].conditionId+"'"
	+"	lay-skin='primary'></th>"
	+"<td onclick='editDateRuleCondition(this)'>"+itemList[i].ruleName.replace(/"([^"]*)"/g, "'$1'")+"</td>"
	+"<td onclick='editDateRuleCondition(this)'>"+itemList[i].leftValue+"</td>"
	+"<td onclick='editDateRuleCondition(this)'>"+itemList[i].valueOperator+"</td>"
	+"<td onclick='editDateRuleCondition(this)'>"+itemList[i].rightValue+"</td>"
	+"<td onclick='editDateRuleCondition(this)'>"+itemList[i].rightValueType+"</td>"
	+"<td onclick='editDateRuleCondition(this)'>"+itemList[i].conditionOperator+"</td>"
	+"<td onclick='editDateRuleCondition(this)'>"+itemList[i].ruleVersion+"</td>"
	+"<td onclick='editDateRuleCondition(this)'>"+itemList[i].conditionGroupName+"</td>"
	+"</tr>";
	tbody.append(tr);
	}
}
//点击li
function clickGatewayLi(a){
	var $li = $(a);
    if ($li.hasClass('link_active')) {
        return;
    } else {
    	var activityId = $li.data('uid');
            $("#my_fold_hurdle li").each(function() {
                $(this).removeClass('link_active');
            });
            $li.addClass('link_active');//选中左侧
            $("#gatewayActivityName").data("activityId",activityId);
            $("#gatewayActivityName").val($li.text());
            $.ajax({
                url : common.getPath() + "/datRule/loadRightDetailsList",
                type : "post",
                dataType : "json",
                data : {
                    "activityId":activityId
                },
                success : function(result){
                	$("#rightGatewayDetails").empty();//将规则名以及规则预览清空
                	$("#gateway_result").empty();//清空新建模态框
                    $("#gatewayTab").find("tbody").empty();//只清空一次
                    printRightGatewayDetails(result.data);
                    	layui.form.render("checkbox");
                },
                error : function(){
                    layer.alert('操作失败');
                }
            });
    }
}

function saveDatRule(conditionId){
	  var ruleId = $("#gateway_result").val();
	  if(ruleId==null||ruleId==undefined||ruleId==''){
		  layer.alert("请选择路由变量名称！");
		  return false;
	  }
	  var leftValue = $("#leftValue").val();
	  if(leftValue.trim()==""){
		  layer.alert("请输入字段名称！");
		  return false;
	  }
	  var valueOperator = $("#valueOperator").val();
	  var rightValue = $("#addDatRule input[name='rightValue']").val();
	  var rightValueType = $("#rightValueType").val();
	  
	  if(rightValueType=="String"){
		  if(rightValue.trim()==""){
			  rightValue=null;
		  }
		  rightValue = "'"+rightValue+"'";
	  }else{
		  if(!/^[+-]?\d+(\.\d+)?$/.test(rightValue)){
			  layer.alert("请输入正确的数字格式!");
			  return false;
		  }
		  //rightValue=parseInt(rightValue);
	  }
	  var conditionOperator = $("#conditionOperator").val();
	  var ruleVersion = $("#addDatRule input[name='ruleVersion']").val();
	  var conditionGroupName = $("#addDatRule input[name='conditionGroupName']").val();
	  var activityId =  $("#gatewayActivityName").data("activityId");
	  var info ={};
	  if(conditionId != null && conditionId !='' && conditionId != undefined){
		  info = {"activityId":activityId,"ruleId":ruleId,"leftValue":leftValue,"valueOperator":valueOperator
			  ,"rightValue":rightValue,"rightValueType":rightValueType,"conditionOperator":conditionOperator
			  ,"ruleVersion":ruleVersion,"conditionGroupName":conditionGroupName,"conditionId":conditionId
			  ,"oldRuleId":oldRuleId};
	  }else{
		  info = {"activityId":activityId,"ruleId":ruleId,"leftValue":leftValue,"valueOperator":valueOperator
				  ,"rightValue":rightValue,"rightValueType":rightValueType,"conditionOperator":conditionOperator
				  ,"ruleVersion":ruleVersion,"conditionGroupName":conditionGroupName};
	  }
	  //保存规则
	  	$.ajax({
	  		url:common.getPath() +"/datRule/saveDatRule",
	  		type:'post',
	  		data:info,
	  		dataType:'json',
	  		success: function(result){
	  			layer.alert(result.msg);
	  			//画出当前新增的规则
	  			var doc = document.getElementById("predictRule_"+ruleId);
	  			doc.innerHTML="";
	  			doc.innerHTML=result.data.PredictRule.ruleProcess;
	  			if(result.data.oldRule!=null){
	  				var oldDoc = document.getElementById("predictRule_"+result.data.oldRule.ruleId);
	  				oldDoc.innerHTML="";
	  				oldDoc.innerHTML=result.data.oldRule.ruleProcess;
	  			}
	  		//画出当前环节的所有的规则条件
	  			$("#gatewayTab").find("tbody").empty();
	  			printDatRuleCondition(result.data.DatConditionList);
	  			
            	layui.form.render();
	  		},error: function(result){
	  			layer.alert(result.msg);
	  		}
	  	});
	   $(".display_container5").css("display","none");
		layui.form.render(); 
	}
/*
 * 画出做出修改的规则
 */
function printUpdateDatRuleList(list){
	for (var i = 0; i < list.length; i++) {
		var doc = document.getElementById("predictRule_"+list[i].ruleId);
			doc.innerHTML="";
			doc.innerHTML=list[i].ruleProcess;
	}
	
}
var oldRuleId ="";//用于保存修改之前的ruleId,编辑规则的时候用到
function editDateRuleCondition(a){
	var conditionId = $(a).parent().find("th").find("input").data("conditionid");
	oldRuleId = $(a).parent().find("th").find("input").data("ruleid"); 
	$("#gateway_result").find("option[value='"+oldRuleId+"']").prop("selected",true);
	var valueOperator = $(a).parent().find("td").eq(2).text(); 
	$("#valueOperator").find("option[value='"+valueOperator+"']").prop("selected",true);
	var rightValueType = $(a).parent().find("td").eq(4).text(); 
	$("#rightValueType").find("option[value='"+rightValueType+"']").prop("selected",true);
	var conditionOperator = "";
	conditionOperator = $(a).parent().find("td").eq(5).text(); 
	if(conditionOperator=="&&"){
		conditionOperator+=":与";
	}else{
		conditionOperator+=":或";
	}
	$("#conditionOperator").find("option[value='"+conditionOperator+"']").prop("selected",true);
	var leftValue = $(a).parent().find("td").eq(1).text(); 
	$("#addDatRule").find("input[name='leftValue']").val(leftValue);
	var rightValue = $(a).parent().find("td").eq(3).text().replace(/\'/g, "");; 
	$("#addDatRule").find("input[name='rightValue']").val(rightValue);
	var ruleVersion = $(a).parent().find("td").eq(6).text(); 
	$("#addDatRule").find("input[name='ruleVersion']").val(ruleVersion);
	var conditionGroupName = $(a).parent().find("td").eq(7).text(); 
	$("#addDatRule").find("input[name='conditionGroupName']").val(conditionGroupName);
	
	layui.use('form',function(){
		var form = layui.form;
		form.render('select');
	});
	$(".display_container5").find(".top").text("修改网关");
	$("#addToArr").text("保存");
	$("#addToArr").unbind("click");
	$("#addToArr").click(function(){
		saveDatRule(conditionId);
	});
	$(".display_container5").css("display","block");
}