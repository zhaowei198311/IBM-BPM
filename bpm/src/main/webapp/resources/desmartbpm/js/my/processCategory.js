var oldCategoryName = "";// 记录重命名分类的原名字
var parentNode = "";  // 记录往哪个节点下新建分类
var parentNodeTId = ""; // 父节点的zTree唯一id
var tempRemoveTreeNode; // 记录要被删除的节点
var tempRemoveParentTreeNode; // 记录要被删除的节点的父节点
var metaToEdit = ""; // 编辑的元数据
// 准备新建的对象
var newMeta = {
    categoryUid: "rootCategory",
    categoryName: "流程分类",
    proAppId: "",
    proUid: "",
    proName: "",
    proDisplay: ""
}


// 为翻页提供支持
var pageConfig = {
    pageNum: 1,
    pageSize: 10,
    total: 0,
    categoryUid: "rootCategory",
    proName: ""
}
//为权限配置分页提供支持
var pageConfigByPower = {
	pageNum: 1,
	pageSize: 10,
	total: 0,
	proAppId: "",
	proUid: ""
}
// 为弹出框的分页控件服务
var pageConfig2 = {
    pageNum: 1,
    pageSize: 5,
    total: 0,
    processAppName: "",
    processAppAcronym: "",
    display: ""
}

var setting = {
    view: {
        addHoverDom: addHoverDom,
        removeHoverDom: removeHoverDom,
        selectedMulti: false
    },
    edit: {
        enable: true,
        editNameSelectAll: true,
        showRemoveBtn: showRemoveBtn,
        showRenameBtn: showRenameBtn,
        renameTitle: '重命名分类',
        removeTitile: '删除分类',
    },
    data: {
        key: {
            name: "name",
        },
        simpleData: {
            enable: true,
            idKey: "id",
            pIdKey: "pid",
            rootPId: "rootCategory"
        }
    },
    callback: {
        onClick: zTreeOnClick,// 点击回调
        beforeDrag: beforeDrag,
        beforeEditName: beforeEditName, // 改名前函数
        beforeRemove: beforeRemove,
        beforeRename: beforeRename,
        onRemove: onRemove,
        onRename: onRename
    }
};

var setting_1 = {
    data: {
        key: {
            name: "name",
        },
        simpleData: {
            enable: true,
            idKey: "id",
            pIdKey: "pid",
            rootPId: "rootCategory"
        }
    }
};

function zTreeOnClick(event, treeId, treeNode) {
    newMeta.categoryUid = treeNode.id;
    newMeta.categoryName = treeNode.name;
    pageConfig.categoryUid = treeNode.id;
    pageConfig.pageNum = 1;
    $("#proName_input").val('');
    getMetaInfo();
}

var log, className = "dark";
function beforeDrag(treeId, treeNodes) {
    return false;
}
// 编辑节点名前
function beforeEditName(treeId, treeNode) {
    // 全局变量中记录修改前的名字，用于还原
    oldCategoryName = treeNode.name;
    className = (className === "dark" ? "":"dark");
    showLog("[ "+getTime()+" beforeEditName ]&nbsp;&nbsp;&nbsp;&nbsp; " + treeNode.name);
    var zTree = $.fn.zTree.getZTreeObj("category_tree");
    zTree.selectNode(treeNode);
    setTimeout(function() {
        if (confirm("进入分类" + treeNode.name + " 的编辑状态吗？")) {
            setTimeout(function() {
                zTree.editName(treeNode);
            }, 0);
        }
    }, 0);
    return false;
}
function beforeRemove(treeId, treeNode) {
    console.log(treeId);
    console.log(treeNode);
    className = (className === "dark" ? "":"dark");
    var zTree = $.fn.zTree.getZTreeObj("category_tree");
    zTree.selectNode(treeNode);
    tempRemoveTreeNode = treeNode;
    tempRemoveParentTreeNode = treeNode.getParentNode();
    return confirm("确认删除分类" + treeNode.name + " 吗？");
}
function onRemove(e, treeId, treeNode) {
    $.ajax({
        url: common.getPath() + "/processCategory/removeCategory",
        dataType: "json",
        type: "post",
        data: {
            "categoryUid": tempRemoveTreeNode.id
        },
        success: function(result) {
            if (result.status == 0) {

            } else {
                var zTree = $.fn.zTree.getZTreeObj("category_tree");
                zTree.addNodes(tempRemoveParentTreeNode,  tempRemoveTreeNode);
                layer.alert(result.msg);
            }
        },
        error: function() {
            var zTree = $.fn.zTree.getZTreeObj("category_tree");
            zTree.addNodes(tempRemoveParentTreeNode,  tempRemoveTreeNode);
        }
    });
}

// 用户确认修改分类名
function beforeRename(treeId, treeNode, newName, isCancel) {
    className = (className === "dark" ? "":"dark");
    showLog((isCancel ? "<span style='color:red'>":"") + "[ "+getTime()+" beforeRename ]&nbsp;&nbsp;&nbsp;&nbsp; " + treeNode.name + (isCancel ? "</span>":""));
    if (newName.length == 0) {
        setTimeout(function() {
            var zTree = $.fn.zTree.getZTreeObj("category_tree");
            zTree.cancelEditName();
            layer.alert("节点名称不能为空.");
        }, 0);
        return false;
    }
    return true;
}

function onRename(e, treeId, treeNode, isCancel) {
    showLog((isCancel ? "<span style='color:red'>":"") + "[ "+getTime()+" onRename ]&nbsp;&nbsp;&nbsp;&nbsp; " + treeNode.name + (isCancel ? "</span>":""));
    $.ajax({
        url: common.getPath() + "/processCategory/renameCategory",
        type: "post",
        data: {
            "categoryUid": treeNode.id,
            "newName": treeNode.name
        },
        dataType: "json",
        success : function(result) {
            if (result.status == 0 ){// 修改成功
            } else {// 修改失败，还原名字
                var zTree = $.fn.zTree.getZTreeObj(treeId);
                treeNode.name = oldCategoryName;
                zTree.updateNode(treeNode);
                layer.alert(result.msg);
            }
        }
    });
}
// 是否显示删除按钮
function showRemoveBtn(treeId, treeNode) {
    if (treeNode.id == 'rootCategory') {
        return false;
    }
    return true;
}
// 是否显示改名按钮
function showRenameBtn(treeId, treeNode) {
    if (treeNode.id == 'rootCategory') {
        return false;
    }
    return true;
}
function showLog(str) {
    if (!log) log = $("#log");
    log.append("<li class='"+className+"'>"+str+"</li>");
    if(log.children("li").length > 8) {
        log.get(0).removeChild(log.children("li")[0]);
    }
}
function getTime() {
    var now= new Date(),
        h=now.getHours(),
        m=now.getMinutes(),
        s=now.getSeconds(),
        ms=now.getMilliseconds();
    return (h+":"+m+":"+s+ " " +ms);
}

var newCount = 1;
function addHoverDom(treeId, treeNode) {
    var sObj = $("#" + treeNode.tId + "_span");
    if (treeNode.editNameFlag || $("#addBtn_"+treeNode.tId).length>0) return;
    var addStr = "<span class='button add' id='addBtn_" + treeNode.tId
        + "' title='新增分类' onfocus='this.blur();'></span>";
    sObj.after(addStr);
    var btn = $("#addBtn_"+treeNode.tId);
    if (btn) btn.bind("click", function(){
        parentNode = treeNode.id;
        parentNodeTId = treeNode.tId;
        // ajax新建一个分类，再加一个节点
        $("#categoryName_input").val('');
        $("#addCategory_container").css("display","block");
        $("#categoryName_input").focus();
        return false;
    });
};
function removeHoverDom(treeId, treeNode) {
    $("#addBtn_"+treeNode.tId).unbind().remove();
};
function selectAll() {
    var zTree = $.fn.zTree.getZTreeObj("category_tree");
    zTree.setting.edit.editNameSelectAll =  $("#selectAll").attr("checked");
}

$(document).ready(function(){
    // 加载树
    $.ajax({
        url: common.getPath() + "/processCategory/getTreeData",
        type: "post",
        data: {},
        dataType: "json",
        success: function(result) {
            $.fn.zTree.init($("#category_tree"), setting, result);
        }
    });

    doPage();

    // “添加”按钮
    $("#show_expose_btn").click(function(){
        getExposedInfo();
        $("#exposed_table_container").css("display","block");
    })


    $("#proMet_table").on("click", ":checkbox", function(){
        if ($(this).prop("checked")) {
            $("#proMet_table :checkbox").prop("checked", false);
            $(this).prop("checked", true);
        }
    });

    // 确认添加
    $('#bind_meta_btn').click(function() {
        // 被勾选的复选框
        var checkedItems = $("[name='unbindMeta_checkbox']:checked");
        if (!checkedItems.length) {
            layer.alert("请选择要添加的流程元数据");
            return;
        }
        if (checkedItems.length > 1) {
            layer.alert("请选择一个要添加流程元数据，不能选择多个");
            return;
        }
        newMeta.proAppId = checkedItems.eq(0).data('processappid');
        newMeta.proUid = checkedItems.eq(0).data('bpdid');
        var orginName = checkedItems.eq(0).parent().next().html();
        newMeta.proDisplay = orginName;
        $('#addMeta_container').css("display", "block");
        $('#addMetaName_input').val(orginName);
        $('#addMetaName_input').focus();

    });

    $('#meta_del_btn').click(function() {
        var cks = $("[name='proMeta_check']:checked");
        if (cks.length != 1) {
            layer.alert("请只选择一个流程元数据进行删除!");
            return;
        }
        var metaUid = "";
        cks.each(function(index, element){
            metaUid = $(element).parent().parent().data('metauid');
        });
        layer.confirm("确认删除元数据？", function () {
            $.ajax({
                url: common.getPath() + "/processMeta/remove",
                type: "post",
                dataType: "json",
                data: {
                    metaUid: metaUid
                },
                success: function(result) {
                    if (result.status == 0) {
                        getMetaInfo();
                        layer.alert('删除成功');
                    } else {
                        layer.alert(result.msg);
                    }
                },
                error: function() {
                    layer.alert("删除失败，请稍后再试");
                }
            });
        })

    });

    // 取消添加
    $('#cancel_bind_btn').click(function() {
        getMetaInfo();
        $('#exposed_table_container').css("display", "none");
    });

    // 取消命名
    $('#addMetaCancel_btn').click(function() {
        $('#addMeta_container').css("display", "none");
    });

    // 取消修改
    $('#editMeta_cancelBtn').click(function() {
        $('#editMeta_container').css("display", "none");
    });

    // 确认修改元数据名称
    $('#editMeta_sureBtn').click(function() {
        var newName = $('#metarename_input').val().trim();
        var proNo = $('#metaProNo_input').val().trim();
        if (!newName) {
            layer.alert('请输入新名称');
            $('#metarename_input').focus();
            return;
        }
        if (newName.length > 30) {
            layer.alert('名称过长');
            $('#metarename_input').focus();
            return;
        }
        if (!proNo) {
            layer.alert('请输入流程编号');
            $('#metaProNo_input').focus();
            return;
        }
        if (proNo.length > 30) {
            layer.alert('流程编号过长');
            $('#metaProNo_input').focus();
            return;
        }
        $.ajax({
            url: common.getPath() + "/processMeta/update",
            type: "post",
            dataType: "json",
            data: {
                "metaUid": metaToEdit,
                "newName": newName,
                "proNo": proNo
            },
            success: function(result) {
                if (result.status == 0) {
                    $('#editMeta_container').hide();
                    getMetaInfo();
                    layer.alert('修改成功');
                } else {
                    layer.alert(result.msg);
                }
            },
            error: function() {
                layer.alert('修改失败，请稍后再试');
            }

        });
    });

    // 命名后再次确认添加元数据
    $('#addMetaSure_btn').click(function(){
        var inputVal = $('#addMetaName_input').val().trim();
        if (!inputVal) {
            layer.alert("请输入元数据名称");
            return;
        }
        if (inputVal.length > 50) {
            layer.alert("元数据名称过长");
            return;
        }
        $.ajax({
            url: common.getPath() + "/processMeta/create",
            type: "post",
            dataType: "json",
            data: {
                "categoryUid": newMeta.categoryUid,
                "proAppId": newMeta.proAppId,
                "proUid": newMeta.proUid,
                "proName": inputVal,
                "proDisplay": newMeta.proDisplay
            },
            success: function(result) {
                if (result.status == 0) {
                    $('#addMeta_container').css('display', 'none');
                    getExposedInfo();
                    layer.alert("添加成功");
                } else {
                    layer.alert(result.msg);
                }
            },
            error: function() {

            }
        });
    });

    // 确认新建分类
    $("#addCategorySure_btn").click(function(){
        var newName = $("#categoryName_input").val().trim();

        console.log(newName);
        if (!newName) {
            layer.alert("分类名不能为空");
            return;
        }
        $.ajax({
            url: common.getPath() + "/processCategory/addCategory",
            type: "post",
            dataType: "json",
            data: {
                "categoryParent": parentNode,
                "categoryName": newName
            },
            success: function(result) {
                if (result.status == 0) {
                    $("#addCategory_container").css("display","none");
                    // 在tree上加入新节点
                    var zTree = $.fn.zTree.getZTreeObj("category_tree");
                    var treeNode = zTree.getNodeByTId(parentNodeTId);
                    zTree.addNodes(treeNode, {"id": result.data.categoryUid, "pid": result.data.categoryParent, "name": result.data.categoryName, "icon": "../resources/desmartbpm/images/1.png" });
                } else {
                    layer.alert(result.msg);
                }
            }
        });
    });

    // 取消新建分类
    $("#addCategoryCancel_btn").click(function(){
        $("#addCategory_container").css("display","none")
    });

    $("#searchMeat_btn").click(function() {
        pageConfig.pageNum = 1;
        pageConfig.total = 0;
        getMetaInfo();
    });

    // 移动
    $("#move_btn").click(function(){
        var cks = $("[name='proMeta_check']:checked");
        if (cks.length != 1) {
            layer.alert("请只选择一条流程定义");
            return;
        }
        $(".display_container8").css("display","block");
        // 加载树
        $.ajax({
            url: common.getPath() + "/processCategory/getTreeData",
            type: "post",
            data: {},
            dataType: "json",
            success: function(result) {
                $.fn.zTree.init($("#category_tree1"), setting_1, result);
            }
        });
    });
    // 取消移动
    $("#moveCancel_btn").click(function(){
        $(".display_container8").css("display","none");
    })
    // 确定修改流程元数据的分类
    $("#moveSure_btn").click(function(){
        var treeObj = $.fn.zTree.getZTreeObj("category_tree1"),
            nodes = treeObj.getSelectedNodes();
        if (nodes.length != 1) {
            layer.alert("请只选择一条节点");
            return;
        }
        var cks = $("input[name='proMeta_check']:checked");
        var metaUid = "";
        cks.each(function(){
            var $tr=$(this).parents('tr');
            metaUid = $tr.attr('data-metauid');
        });
        $.ajax({
            async: false,
            url: common.getPath() + "/processCategory/changeTheCategoryOfProcessMeta",
            type: "post",
            dataType: "json",
            data:{
                metaUid: metaUid,
                categoryUid: nodes[0].id
            },
            success: function(data){
                if (data.status == 0) {
                    layer.alert("移动成功！");
                    $(".display_container8").css("display","none");
                    cks.each(function(){
                        $(this).parents('tr').remove();
                    })
                }else {
                    layer.alert(data.msg);
                }
            }
        })
    })

    // 隐藏
    $("#hide_btn").click(function(){
        var sign = "hide";
        if (commonCheck(sign)) {
            var url = "/processCategory/changeStatus";
            commonMethod(url);
        }
    })

    // 关闭流程
    $("#close_btn").click(function(){
        var sign = "close";
        if (commonCheck(sign)) {
            var url = "/processCategory/closeCategory";
            commonMethod(url);
        }
    })

    //启用
    $("#enable_btn").click(function(){
        var sign = "enable";
        if (commonCheck(sign)) {
            var url = "/processCategory/enableCategory";
            commonMethod(url);
        }
    })

    // 重新加载
    $("#reloadExposedItem_btn").click(reloadExposedItems);

});
// 隐藏,关闭,启用功能公共验证方法
function commonCheck(sign){
    var cks = $("input[name='proMeta_check']:checked");
    if (cks.length != 1) {
        layer.alert("请只选择一条流程定义");
        return false;
    }
    var status = "";
    cks.each(function(){
        status = $(this).parents('tr').children().eq(5).text();
    });
    if (sign == "close" && status == 'closed') {
        layer.alert("该流程已经是关闭状态!");
        return false;
    }
    if (sign == "enable" && status == 'on') {
        layer.alert("该流程已经是启用状态!");
        return false;
    }
    if (sign == "hide" && status == 'hide') {
        layer.alert("该流程已经是隐藏状态!");
        return false;
    }
    return true;
}
// 隐藏,关闭,启用功能公共方法
function commonMethod(url){
    var cks = $("input[name='proMeta_check']:checked");
    var metaUid = "";
    cks.each(function(){
        metaUid = $(this).parents('tr').attr('data-metauid');
    });
    $.ajax({
        async: false,
        url: common.getPath() + url,
        type: 'post',
        dataType: 'json',
        data: {
            metaUid: metaUid
        },
        success: function(data){
            if (data.status == 0) {
                layer.alert("操作成功!");
                getMetaInfo();
            }else {
                layer.alert("操作失败!");
            }
        }
    })
}

/* 向服务器请求流程元数据   */
function getMetaInfo() {
    $.ajax({
        url: common.getPath() + "/processMeta/listByCategoryUid",
        type: "post",
        dataType: "json",
        data: {
            "pageNum": pageConfig.pageNum,
            "pageSize": pageConfig.pageSize,
            "categoryUid": pageConfig.categoryUid,
            "proName": $('#proName_input').val().trim()
        },
        success: function(result) {
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
    $("#proMet_table_tbody").html('');
    if (pageInfo.total == 0) {
        return;
    }

    var list = pageInfo.list;
    var startSort = pageInfo.startRow;//开始序号
    var trs = "";
    for(var i=0; i<list.length; i++) {
        var meta = list[i];
        var sortNum = startSort + i;
        var createTime = "";
        var updateTime = "";
        if (meta.createTime) {
            createTime = common.dateToString(new Date(meta.createTime));
        }
        if (meta.lastUpdateTime) {
            updateTime = common.dateToString(new Date(meta.lastUpdateTime));
        }
        trs += '<tr data-metauid="'+meta.proMetaUid+'" data-proNo="' + meta.proNo + '" ondblclick="showEditDiv(this);">'
            + '<td><input type="checkbox" name="proMeta_check" value="' + meta.categoryUid + '" lay-skin="primary">'+ sortNum +'</td>'
            + '<td>'+meta.proName+'</td>'
            + '<td>'+meta.proAppId+'</td>'
            + '<td>'+meta.proUid+'</td>'
            + '<td>'+meta.proDisplay+'</td>'
            + '<td>'+meta.proMetaStatus+'</td>'
            + '<td>'+meta.creatorFullName+'</td>'
            + '<td>'+createTime+'</td>'
            + '<td>'+meta.updatorFullName+'</td>'
            + '<td>'+updateTime+'</td>'
            + '</tr>';
    }
    $("#proMet_table_tbody").append(trs);

}

// 分页
function doPage() {
    layui.use(['laypage', 'layer'], function(){
        var laypage = layui.laypage,layer = layui.layer;
        //完整功能
        laypage.render({
            elem: 'lay_page',
            curr: pageConfig.pageNum,
            count: pageConfig.total,
            limit: pageConfig.pageSize,
            layout: ['count', 'prev', 'page', 'next', 'limit', 'skip'],
            jump: function(obj, first){
                // obj包含了当前分页的所有参数
                pageConfig.pageNum = obj.curr;
                pageConfig.pageSize = obj.limit;
                if (!first) {
                    getMetaInfo();
                }
            }
        });
    });
}
// 获取公开的流程数据
function getExposedInfo() {
    $.ajax({
        url: common.getPath() + "/processMeta/getUnSynchronizedProcessMeta",
        type: "post",
        dataType: "json",
        data: {
            "pageNum": pageConfig2.pageNum,
            "pageSize": pageConfig2.pageSize,
            "processAppName": "",
            "processAppAcronym": "",
            "display": ""
        },
        success: function(result) {
            if (result.status == 0) {
                drawExposedTable(result.data);
            }
        }
    });
}
// 渲染公开流程表
function drawExposedTable(pageInfo) {
    pageConfig2.pageNum = pageInfo.pageNum;
    pageConfig2.pageSize = pageInfo.pageSize;
    pageConfig2.total = pageInfo.total;

    $("#exposed_table_tbody").html('');
    doPage2();
    if (pageInfo.total == 0) {
        return;
    }

    var list = pageInfo.list;
    var startSort = pageInfo.startRow;// 开始序号
    var trs = "";
    for(var i=0; i<list.length; i++) {
        var item = list[i];
        var sortNum = startSort + i;
        var bpdName = item.bpdName.length > 10 ? item.bpdName.substring(0, 10) + '...' : item.bpdName;
        var proAppName = item.proAppName.length > 10 ? item.proAppName.substring(0, 10) + '...' : item.proAppName;
        trs += '<tr>'
            + '<td><input type="checkbox" name="unbindMeta_checkbox"  lay-skin="primary" data-processAppId="'+item.proAppId+'" data-bpdId="'+item.bpdId+'">'+sortNum+'</td>'
            + '<td title="' + item.bpdName + '">' + bpdName + '</td>'
            + '<td title="' + item.proAppName + '">' + proAppName + '</td>'
            + '<td>'+item.bpdId+'</td>'
            + '</tr>';
    }
    $("#exposed_table_tbody").html(trs);
}

// 点击tr排他选中checkbox
$('#unsynProMetaTable').on('click', 'tr', function(event) {
    var $ck = $(this).find(':checkbox');
    var checked = $ck.prop('checked');
    if (event.target.type == 'checkbox') {
        checked = !checked;
    }
    if (checked) {
        $ck.prop('checked', false);
    } else {
        $('#unsynProMetaTable :checkbox').prop('checked', false);
        $ck.prop('checked', true);
    }
});

// 分页
function doPage2() {
    layui.use(['laypage', 'layer'], function(){
        var laypage = layui.laypage,layer = layui.layer;
        //完整功能
        laypage.render({
            elem: 'lay_page2',
            curr: pageConfig2.pageNum,
            count: pageConfig2.total,
            limit: pageConfig2.pageSize,
            layout: ['count', 'prev', 'page', 'next', 'limit', 'skip'],
            jump: function(obj, first){
                // obj包含了当前分页的所有参数
                pageConfig2.pageNum = obj.curr;
                pageConfig2.pageSize = obj.limit;
                if (!first) {
                    getExposedInfo();
                }
            }
        });
    });
}
// 展示重命名流程表
function showEditDiv(tr){
    metaToEdit = $(tr).data('metauid');
    $('#metarename_input').val($(tr).find('td').eq(1).html());
    $('#metaProNo_input').val($(tr).data('prono'));
    $('#eidtMeta_appIdShow').val($(tr).find('td').eq(2).html());
    $('#eidtMet_bpdIdShow').val($(tr).find('td').eq(3).html());
    $('#eidtMet_displayShow').val($(tr).find('td').eq(4).html());
    $('#editMeta_container').show();
    $('#metarename_input').focus();
}

function reloadExposedItems() {
    var param = {
        url: common.getPath() + "/processDefinition/reloadExposedItems",
        data: {}
    };
    common.doPostAjax(param);

}

//权限配置
//分页
function doPageByPower() {
    layui.use(['laypage', 'layer'], function(){
        var laypage = layui.laypage,layer = layui.layer;
        //完整功能
        laypage.render({
            elem: 'lay_page_power',
            curr: pageConfigByPower.pageNum,
            count: pageConfigByPower.total,
            limit: pageConfigByPower.pageSize,
            layout: ['count', 'prev', 'page', 'next', 'limit', 'skip'],
            jump: function(obj, first){
                // obj包含了当前分页的所有参数
                pageConfigByPower.pageNum = obj.curr;
                pageConfigByPower.pageSize = obj.limit;
                if (!first) {
                	getProcessReadOfMeta();
                }
            }
        });
    });
}
//字符转换,截取指定字符长度
function interceptStr(str, len) {
    var reg = /[\u4e00-\u9fa5]/g,    //匹配中文字符
        slice = str.substring(0, len),
        chineseCharNum = (~~(slice.match(reg) && slice.match(reg).length)),
        realen = slice.length * 2 - chineseCharNum;
    return str.substr(0, realen) + (realen < str.length ? "..." : "");
}
//画出权限列表
function drawProcessPowerTable(pageInfo){
	//渲染分页
	pageConfigByPower.pageNum = pageInfo.pageNum;
	pageConfigByPower.pageSize = pageInfo.pageSize;
	pageConfigByPower.total = pageInfo.total;
    doPageByPower();
    $("#power_table_tbody").empty();
    if (pageInfo.total == 0) {
        return;
    }
    //渲染数据
    let trs = "";
    let list = pageInfo.list;
    let startSort = pageInfo.startRow;// 开始序号
    for (var i = 0; i < list.length; i++) {
    	let item = list[i];
    	let sortNum = startSort+i;
    	 trs += '<tr>'
             + '<td><input onclick = "metaPowerCheckbox(this)" type="checkbox" name="metaPower_checkbox"  lay-skin="primary" value = "'
             +item.opUid+'">'+sortNum+'</td>'
             + '<td onclick = "metaPowerCheckbox(this)">' + item.proName + '</td>'
             + '<td onclick = "metaPowerCheckbox(this)">'; 
    	 	if(item.opObjUid!=null){
    	 		trs+=item.opObjUid;
    	 	}
          trs+='</td><td style="text-align: center;" >'
             + item.opParticipateView
             + '</td></tr>';
	}
    $("#power_table_tbody").append(trs);
}

//点击tr选中checkbox
function metaPowerCheckbox(obj){
	var $ck = null;
	if($(obj).attr("type")=="checkbox"){//因为可能点击的是checkbox
		$ck = $(obj);
	}else{
		var $tr = $(obj).parent();
		$ck = $tr.find("input[name='metaPower_checkbox']");
	}
		
    let checked = $ck.prop("checked");
    if($(obj).attr("type")!="checkbox"){//如果选中的不是checkbox,则取反
    	checked=!checked;
    }
    $ck.prop('checked', checked);
    
    let checkboxNodes = $("input[name='metaPower_checkbox']");
    let checkedNodes = $("input[name='metaPower_checkbox']:checked");
    if(checkboxNodes.length==checkedNodes.length){
    	$("#all_read_power_checkbox").prop("checked",true);
    }else{
    	$("#all_read_power_checkbox").prop("checked",false);
    }
};
//全选
$("#all_read_power_checkbox").on('click',function(){
	let flag = $(this).prop("checked");
	$("input[name='metaPower_checkbox']").prop("checked",flag);
})

$(function(){
	//流程元数据查看权限配置
	$("#readPower_btn").click(function(){
		var checkedNode = $("input[name='proMeta_check']:checked");
		if(checkedNode!=null&&checkedNode.length==1){
			let proAppId = checkedNode.parent().next().next().text();//获得流程库id
			let proUid = checkedNode.parent().next().next().next().text();//获得流程图id
			let proName = checkedNode.parent().next().text();//获得元数据名称
			$("#add_read_proAppId").val(proAppId);
			$("#add_read_proUid").val(proUid);
			//设置新增时显示的流程名
			$("#power_proName_view").val(proName);
			pageConfigByPower.proAppId = proAppId;
			pageConfigByPower.proUid = proUid;
			//查询该流程元数据下的权限配置
			getProcessReadOfMeta();
			$("#read_power_Container").show();
		}else{
			layer.alert("请选择一个流程定义进行权限配置");
		}
	});
	//加载配置界面公司数据
	$.ajax({
		url : common.getPath() + '/sysCompany/allCompany',
		type : 'post',
		dataType : 'json',
		data : {}, 
		beforeSend : function(){
			layer.load(1);
		},
		success : function(result){
			for (var i = 0; i < result.length - 1; i++) {
				if(result[i].companyName.indexOf("Country") < 0){
					$("#companyData").append("<option value="+result[i].companyCode+">"+result[i].companyName+"</option>");	
				}
			}
			layer.closeAll("loading");
			layui.form.render("select");
		},
		error : function(result){
			layer.closeAll("loading");
		}
	})
	// 选择查看人员
    $("#chooseUser_btn").click(function () {
    	common.chooseUser('permissionUser', 'false');
    });
    
    // 选择查看角色
    $("#chooseRole_btn").click(function(){
    	common.chooseRole('permissionRole', 'false');
    });
    
    // 选择查看角色组
    $("#chooseTeam_btn").click(function(){
    	common.chooseTeam('permissionTeam', 'false');
    });

});
//分页查询该流程元数据下的权限配置
function getProcessReadOfMeta(){
	 $.ajax({
	        url:common.getPath()+"/permission/processReadByPage",
	        type: "post",
	        dataType: "json",
	        data: {
	        	"pageNum": pageConfigByPower.pageNum,
	        	"pageSize": pageConfigByPower.pageSize,
	            "proAppId": pageConfigByPower.proAppId,
	            "proUid": pageConfigByPower.proUid
	        },
	        beforeSend : function(){
	        	layer.load(1);
	        },
	        success: function(result) {
	            if (result.status == 0) {
	                drawProcessPowerTable(result.data);
	            }
	            layer.closeAll("loading");
	        },
	        error:function(result){
	        	layer.alert(result.msg);
	        	layer.closeAll("loading");
	        }
	    });
}

//显示新增流程元数据查看权限model
function showAddReadPower(){
	$("#deployReadPower").find("input").removeAttr("readonly");
	$("#deployReadPower").find("select").removeAttr("readonly");
	$("#add_read_power_Container").show();
}
//查看流程元数据权限详细
function showReadPowerView(){
	$("#deployReadPower").find("input").attr("readonly","readonly");
	$("#deployReadPower").find("select").attr("readonly","readonly");
	$("#add_read_power_Container").show();
}
//提交新增/更新的流程元数据的查看权限
function submitDeployReadPower(){
	let data = $("#deployReadPower").serialize();
	$.ajax({
		url:common.getPath()+"/processMeta/updateDhProcessMetaPower",
		type:"post",
		data:data,
		dataType:"json",
		beforeSend : function(){
			layer.load(1);
		},
		success : function(result){
			if(result.status==0){
				//查询该流程元数据下的权限配置
				getProcessReadOfMeta();
				$("#add_read_power_Container").hide();
			}else{
				layer.alert(result.msg);
			}
			layer.closeAll("loading");
		},
		error : function(){
			layer.alert("更新流程元数据查询权限配置异常");
			layer.closeAll("loading");
		}
	});
}
//删除流程元数据查看权限
function deleteReadPower(){
	var checkedNodes = $("input[name='metaPower_checkbox']:checked");
	if(checkedNodes==null||checkedNodes.length==0){
		layer.alert("请选择要删除的权限");
		return;
	}else{
		let primaryKeyList = new Array();
		for (var i = 0; i < checkedNodes.length; i++) {
			primaryKeyList.push($(checkedNodes[i]).val());
		}
		$.ajax({
			url: common.getPath()+'/permission/deleteBatchByPrimaryKeys',
			type:"post",
			data:{"primaryKeys":primaryKeyList},
			dataType:"json",
			traditional: true,
			beforeSend : function(){
				layer.load(1);
			},
			success : function(result){
				if(result.status==0){
					//批量删除权限配置
					getProcessReadOfMeta();
				}
				layer.alert(result.msg);
				layer.closeAll("loading");
			},
			error : function(){
				layer.alert("删除权限配置异常");
				layer.closeAll("loading");
			}
		});
	}
}