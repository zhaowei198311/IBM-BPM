/*$(function(){

	//loadActivity_Meta();
	//appendGateWaySetList(activityMetas);
	
});*/
/*
    var type;
	var gatewayArr = [];
	var gatewayAndArr = [];
	var gatewayOrArr = [];
	var flag;
	var activityMetas= new Array();
	var currActivityMeta;
function checkAndLoadFrom(a){
type = currActivityMeta.type;
$(a).parent().attr("id",type);
flag=currActivityMeta.activityType;
//var gatewayIndex = $("#gatewayIndex").val(); sortNum暂时隐藏
if( gatewayIndex ==''|| gatewayIndex == undefined){
	return false;
}else{
		$("#sortNum").val(gatewayIndex);
		$("#gateway_result").empty();
	  $(".gatewayTbody input[type='checkbox']").each(function(){
		if($(this).prop("checked")==true){
			var info = "<option value='"+$(this).parent().find("label").text()+"'>"
			+$(this).parent().find("label").text()+"</option>";
			$("#gateway_result").append(info);
		}
	});
		layui.form.render('select');
	
}
} 
//提交保存已新建规则
function submitAddDatRule(){
  var addToRule = new Array();
  if(flag=="gateway"||flag==="gateway"){
	  addToRule=gatewayArr;
  }if(flag=="gatewayAnd"||flag==="gatewayAnd"){
	  addToRule=gatewayAndArr;
  }
  if(flag=="gatewayOr"||flag==="gatewayOr"){
	  addToRule=gatewayOrArr;
  }
  $.ajax({  
        type: "POST",    
        url:" ../datRule/addDatRule?type="+type+"&activityId="+currActivityMeta.activityId+"&activityType="+flag, 
        data:JSON.stringify(addToRule),
        contentType: "application/json",
        dataType:'json',
        async: false,  
        error: function(request) {   
             layer.alert("Connection error");  
        },  
        success: function(result) {  
              layer.alert(result.msg);
              var resultRule = result.data.predictRule;
              var s = "#"+type;
              $("#gatewayPredictRule").empty();
              $(resultRule).each(function(i){
            	  if(resultRule[i].ruleProcess!=null){
            		  $("#gatewayPredictRule").append("<span>"+resultRule[i].ruleProcess+"</span><br>");
            	   }
              })
              if(flag=="gateway"){
				  gatewayArr=[];
              	  gatewayArr = result.data.dataList;
              }else if(flag=="gatewayAnd"){
            	  gatewayAndArr=[];
            	  gatewayAndArr = result.data.dataList;
              }else if(flag=="gatewayOr"){
            	  gatewayOrArr=[];
            	  gatewayOrArr = result.data.dataList;
              }
              loadEachArr($("#gatewayTab"),result.data.dataList);
              layui.form.render();
        }  
     });
  
}
//添加规则到数组
function addToArr(){
  
  var sortNum = $("#addDatRule input[name='sortNum']").val();
  var result = $("#gateway_result").val();
  if(result==null||result==undefined||result==''){
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
	  rightValue=parseInt(rightValue);
  }
  var conditionOperator = $("#conditionOperator").val();
  var ruleVersion = $("#addDatRule input[name='ruleVersion']").val();
  var conditionGroupName = $("#addDatRule input[name='conditionGroupName']").val();
  var info ={result:result,leftValue:leftValue,valueOperator:valueOperator,rightValue:rightValue,rightValueType:rightValueType,conditionOperator:conditionOperator,ruleVersion:ruleVersion,conditionGroupName:conditionGroupName};
  var s = "#"+type;
  $(s).find("tbody").empty();
   if(flag=="gateway"||flag==="gateway"){
	  gatewayArr.push(info);
	  appendEachArr(s,gatewayArr);
   }else if(flag=="gatewayAnd"||flag==="gatewayAnd"){
	  gatewayAndArr.push(info);
	  appendEachArr(s,gatewayAndArr);
   }else if(flag=="gatewayOr"||flag==="gatewayAndOr"){
	  gatewayOrArr.push(info);
	  appendEachArr(s,gatewayOrArr);
   }
   $(".display_container5").css("display","none");
	layui.form.render(); 
}
//循环显示已新建规则
function appendEachArr(s,itemList){
  if(itemList!=undefined){
  $(itemList).each(function(i){
	  if(itemList[i]!=null){//防止删除后添加的null元素的影响
    	var tr = "<tr name = '"+i
			+"' ><td><input type='checkbox' name=''"
			+"	lay-skin='primary'>"+(i+1)+"</td>"
			+"<td>"+itemList[i].result+"</td>"
			+"<td>"+itemList[i].leftValue+"</td>"
			+"<td>"+itemList[i].valueOperator+"</td>"
			+"<td>"+itemList[i].rightValue+"</td>"
			+"<td>"+itemList[i].rightValueType+"</td>"
			+"<td>"+itemList[i].conditionOperator.split(":")[0]+"</td>"
			+"<td>"+itemList[i].ruleVersion+"</td>"
			+"<td>"+itemList[i].conditionGroupName+"</td>"
			+"</tr>";
			$(s).find("tbody").append(tr);
	  }
    	})
  }
}
function loadgatewaylist(a){
	var str = $(a).attr("id");
	var index = str.substring(str.indexOf("_")+1);
	//alert(index);
	currActivityMeta = activityMetas[index];
	$("#gatewaySet_tab_row").find("input").val(currActivityMeta.activityName);
    //加载条件组合规则
	var requestArr = new Array({"activityId":currActivityMeta.activityId});
	$.ajax({  
            type: "POST",    
            url:" ../datRule/loadConditionArr", 
            data:JSON.stringify(requestArr),
            contentType: "application/json",
            dataType:'json',
            async: false,  
            error: function(request) {   
                 layer.alert("Connection error");  
            },  
            success: function(result) {  
            	if(result.data.gatewayKey!=undefined&&result.data.gatewayKey.PredictRule!=undefined){
            		$("#gatewayPredictRule").empty();
            		$(result.data.gatewayKey.PredictRule).each(function(i){
            		if(result.data.gatewayKey.PredictRule[i].ruleProcess!=null){
            	$("#gatewayPredictRule").append("<span>"+result.data.gatewayKey.PredictRule[i].ruleProcess+"</span><br>");  
            		}
            		});
                if(flag=="gateway"){
                	if(gatewayArr.length==0){
                	gatewayArr = result.data.gatewayKey.DataList;
                	}
                }else if(flag=="gatewayAnd"){
                	if(gatewayAndArr.length==0){
                	gatewayAndArr = result.data.gatewayKey.DataList;
                	}
                }else if(flag=="gatewayOr"){
                	if(gatewayOrArr.length==0){
                	gatewayOrArr = result.data.gatewayKey.DataList;
                	}
                }
  				loadEachArr($("#gatewayTab"),result.data.gatewayKey.DataList);
            	}
            	
            	layui.form.render(); 
            	}
	});
}

//循环显示已有规则
function loadEachArr(s,itemList){
  s.find("tbody").empty();
  if(itemList!=undefined){
  $(itemList).each(function(i){
	  var str = itemList[i].result;
	  var index = str.substring(0,str.lastIndexOf("_")).lastIndexOf("_");
	  var result = str.substring(index+1);
	  itemList[i].result=result;
    	var tr = "<tr name = '"+i
			+"' ><td><input type='checkbox' name=''"
			+"	lay-skin='primary'>"+(i+1)+"</td>"
			+"<td>"+result+"</td>" 
			+"<td>"+itemList[i].leftValue+"</td>"
			+"<td>"+itemList[i].valueOperator+"</td>"
			+"<td>"+itemList[i].rightValue+"</td>"
			+"<td>"+itemList[i].rightValueType+"</td>"
			+"<td>"+itemList[i].conditionOperator.split(":")[0]+"</td>"
			+"<td>"+itemList[i].ruleVersion+"</td>"
			+"<td>"+itemList[i].conditionGroupName+"</td>"
			+"</tr>";
			s.find("tbody").append(tr);
    	})
  }
}*/
//加载activity_meta及确定网关环节列表
/*function loadActivity_Meta(){
	 $.ajax({  
        type: "POST",    
        url:" ../activityMeta/getGatewaysOfDhProcessDefinition", 
        data: {"proAppId":proAppId,"proUid":proUid,"proVerUid":proVerUid},
        dataType:'json',
        async: false,  
        error: function(request) {   
             layer.alert("Connection error");  
        },  
        success: function(result) {  
        	if(result.status == 0){
        		activityMetas=result.data;
            }else{
                layer.alert(result.msg);
            }
        	}
	 }); 
}
function appendGateWaySetList(itemList){
	 for (var i = 0; i < itemList.length; i++) {
		 if(i==0){
			 $("#gatewaySet_tab_title").append("<li id='activityMetas_"+i+"' class='layui-this' onclick='loadgatewaylist(this)'>"+itemList[0].activityName+"</li>");
			 currActivityMeta=itemList[0];
			 $("#gatewaySet_tab_row").find("input").val(currActivityMeta.activityName);
			 type = currActivityMeta.type;
			 flag=currActivityMeta.activityType;
			 loadgatewaylist("#activityMetas_0");
		 }else{
			 $("#gatewaySet_tab_title").append("<li id='activityMetas_"+i+"' onclick='loadgatewaylist(this)'>"+itemList[0].activityName+"</li>");
		 }
	}
	 
}*/
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
		/*if (checkAndLoadFrom($(this)) == false) {
			return;
		} else {*/
		$(".display_container5").css("display", "block");
		/*}*/
	})
	$(".delete_net").click(function(){
		/*var checkA = $(this).parent().find("input[name='allChecked']");
		if($(checkA[0]).prop("checked")==true){
			if(flag=="gateway"||flag==="gateway"){
				  gatewayArr=[];
			  }if(flag=="gatewayAnd"||flag==="gatewayAnd"){
				  gatewayAndArr=[];
			  }
			  if(flag=="gatewayOr"||flag==="gatewayOr"){
				  gatewayOrArr=[];
			  }
			$(this).parent().find("tbody").empty();
		}else{*/
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
				$("#gatewayTab").find("tbody").empty();
				printDatRuleCondition(result.data.DatConditionList);
				//画出做过更改的规则
				printUpdateDatRuleList(result.data.PredictRules);
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

/**/
//画出右侧
function printRightGatewayDetails(list){
	
	$("#gateway_result").append('<option value="">--请选择--</option>');
	for (var i = 0; i < list.length; i++) {
		var str = '<div class="layui-form-item">'
			+'<label class="layui-form-label">规则名称</label>'
			+'<div class="layui-input-block">'
			+'<table class="layui-table" lay-even lay-skin="nob">'
			+'<colgroup><col width="10%"><col width="13%"></colgroup><tbody class="gatewayTbody">'
			+'<tr><td><label>'
			var map = list[i];
			for (key in map) {
				if(map[key]=='default'||map[key]==='default'){
					str += '默认线路</label></td><td>线路：<label>'
						+key+'</label></td></tr></tbody></table></div></div>';
				}else{
					str += map[key].PredictRule.ruleName+'</label></td><td>线路：<label>'
						+key+'</label></td></tr></tbody></table></div></div>';
					str += '<div class="layui-form-item">'
					+'<label style="position: relative;bottom: 11px;" class="layui-form-label">组合预览</label>'
					+'<div class="layui-input-block">'
					+'<span id = "predictRule_'+map[key].PredictRule.ruleId+'">'
					+map[key].PredictRule.ruleProcess+'</span></div></div>';
					//将规则名、规则id添加到新增模态框中
					var info = '<option value="'+map[key].PredictRule.ruleId+'">'
					+map[key].PredictRule.ruleName+'</option>';
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
//循环画出规则条件
function printDatRuleCondition(itemList){
	$("#gateway_result").find("option").eq(0).prop("selected",true);//操作后将路由变量默认置为请选择
	
	var tbody = $("#gatewayTab").find("tbody");
	//tbody.empty();
	for (var i = 0; i < itemList.length; i++) {
	var tr = "<tr><th><input type='checkbox' data-ruleid='"+itemList[i].ruleId +
			"' data-conditionid='"+itemList[i].conditionId+"'"
	+"	lay-skin='primary'></th>"
	+"<td>"+itemList[i].ruleName+"</td>"
	+"<td>"+itemList[i].leftValue+"</td>"
	+"<td>"+itemList[i].valueOperator+"</td>"
	+"<td>"+itemList[i].rightValue+"</td>"
	+"<td>"+itemList[i].rightValueType+"</td>"
	+"<td>"+itemList[i].conditionOperator+"</td>"
	+"<td>"+itemList[i].ruleVersion+"</td>"
	+"<td>"+itemList[i].conditionGroupName+"</td>"
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

function saveDatRule(){
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
	  var info ={activityId:activityId,ruleId:ruleId,leftValue:leftValue,valueOperator:valueOperator,rightValue:rightValue,rightValueType:rightValueType,conditionOperator:conditionOperator,ruleVersion:ruleVersion,conditionGroupName:conditionGroupName};
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
