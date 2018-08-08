

function openResourceDialog(roleUid){
	$('#resource').empty();
	$.ajax({  
        url: 'sysResource/resourceTree',    //后台webservice里的方法名称  
        type: "post",  
        dataType: "json",  
        success: function (data) {
			$.fn.zTree.init($("#resourceTree"), settingResource, data);
			$.ajax({  
		        url: 'sysRoleResource/allSysRoleResource?roleUid='+roleUid,    //后台webservice里的方法名称  
		        type: "post",  
		        dataType: "json",  
		        success: function (data1) {
		        	opendialog('display_container6');
					$('#roleUid1').val(roleUid);
					var treeObjs = $.fn.zTree.getZTreeObj("resourceTree");
					$.each(data1,function(i,value){
						var node = treeObjs.getNodeByParam("id",value.resourceUid);
						if(node!=null){
							if(node.isParent==false){
								treeObjs.checkNode(node, true, true);
							}
						}
					});
		        }
			});
        }
    });
}

function delete_user(){
	$("#user_add .colorli").remove();
}

var settingResource = {
	check: {
		enable: true
	},data: {
		simpleData: {
			enable: true
		}
	}
};

function closeResourceDialog(data){
	returnSuccess(data,'display_container6');
}


function addUserRoleSuccess(data){
	returnSuccess(data,dialogs.add_team_dialog);
}
		
function selectClick(_this){
	if($(_this).hasClass("colorli")){
		$(_this).removeClass("colorli");
	}else{
		$(_this).addClass("colorli");
	}
}



function onClick(e, treeId, treeNode) {
	var departUid='';
	var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
	var sNodes = treeObj.getSelectedNodes();
	for (var i = 0; i < sNodes.length; i++) {
		departUid=sNodes[i].code;
	}
	departUid=departUid.replace(/\s/g, "")
	ajaxTodo('sysUser/userList?departUid='+departUid,'setUserList');
}


var ruleUids='';
//群组人员分配
function addRoleTema(data){
	opendialog(dialogs.add_team_dialog);
	var $ul=$("#user_add");
	user_add_li(data,$ul,'addUserRole');
	$('#roleUid').val(ruleUids);
}

function openRoleUsers(roleUid){
	ruleUids=roleUid;
	ajaxTodo("sysRoleUser/allSysRoleUser?roleUid="+roleUid,"addRoleTema");
}

function setUserList(data){
	var $ul=$("#usersul");
	user_add_li(data,$ul);
}

function user_add_li(data,element,type){
	var $ul=element;
	$ul.empty();
	$("#usersul").empty();
	$(data).each(function(index){
		var str='';
		if(type=='addUserRole'){
			str+="<li userUid='"+this.userUid+"' onclick='selectClick(this);'>"+this.userName+'('+this.userUid+')';
			str+="<input type='hidden' name='userUid' value='"+this.userUid+"'/>";
			//str+="<input type='hidden' name='departUid' value='"+this.departUid+"'/>";
			str+="</li>";
		}else{
//			str+='<li type="hidden" value="'+this.userUid+'" departUid="'+this.departUid+'" onclick="selectClick(this)" name="userUid">'+this.userName+'</li>';
			str+='<li type="hidden" userUid="'+this.userUid+'" onclick="selectClick(this)" name="userUid">'+this.userName+'('+this.userUid+')</li>';
		}
		$ul.append(str);
	});
};

function add_user(){
	var userids = [];
	$("#user_add li").each(function(){//遍历 右边栏目的ID 
		userids.push($(this).attr('userUid'));//获取 所有 已添加人员
	});
	
	var index=0;
	$("#usersul li").each(function(){
		var $userLi=$(this);
		if($userLi.hasClass('colorli')){
			//判断 已添加人员
			var userUid=$userLi.attr('userUid');//用ID
			var departUid=$userLi.attr('departUid');//部门
			var roleUid=$userLi.attr('roleUid');
			var name=$userLi.text();
			if($.inArray(userUid, userids)==-1){
				var str='';
				str+="<li userUid='"+userUid+"' onclick='selectClick(this);'>"+name;
				str+="<input type='hidden' name='userUid' value='"+userUid+"'/>";
				//str+="<input type='hidden' name='departUid' value='"+departUid+"'/>";
				str+="</li>";
				$("#user_add").append(str);
				index++;
			}
		}
	});
}

function addUserRoleSuccess(data){
	returnSuccess(data,dialogs.add_team_dialog);
}