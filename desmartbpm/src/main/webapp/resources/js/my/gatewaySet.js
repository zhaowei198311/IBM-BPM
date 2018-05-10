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
		if (checkAndLoadFrom($(this)) == false) {
			return;
		} else {
			$(".display_container5").css("display", "block");
		}
	})
	$(".delete_net").click(function(){
		var checkA = $(this).parent().find("input[name='allChecked']");
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
		}else{
			var checkB = $(this).parent().find("input[name!='allChecked']");
			$(checkB).each(function(){
				if($(this).prop("checked")==true){
	
				var index = $(this).parent().parent().attr("name");
				//alert(index);
				if(flag=="gateway"||flag==="gateway"){
					  //alert(gatewayArr.length);
					  gatewayArr.splice(index,1,null);//为了防止数组长度发生变化
					  //alert(gatewayArr.length);
				  }if(flag=="gatewayAnd"||flag==="gatewayAnd"){
					  gatewayAndArr.splice(index,1,null);
				  }
				  if(flag=="gatewayOr"||flag==="gatewayOr"){
					  gatewayOrArr.splice(index,1,null);
				  }
				$(this).parent().parent().remove();
				}
			})
		}
	})
	
	loadActivity_Meta();
	appendGateWaySetList(activityMetas);
	
});

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
/*if( gatewayIndex ==''|| gatewayIndex == undefined){
	return false;
}else{*/
		/*$("#sortNum").val(gatewayIndex);*/
		$("#gateway_result").empty();
	  $(".gatewayTbody input[type='checkbox']").each(function(){
		if($(this).prop("checked")==true){
			var info = "<option value='"+$(this).parent().find("label").text()+"'>"
			+$(this).parent().find("label").text()+"</option>";
			$("#gateway_result").append(info);
		}
	});
		layui.form.render('select');
	
/*}*/
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
  
  /*var sortNum = $("#addDatRule input[name='sortNum']").val();*/
  var result = $("#gateway_result").val();
  if(result==null||result==undefined){
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
}
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
//加载activity_meta及确定网关环节列表
function loadActivity_Meta(){
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
	 
}