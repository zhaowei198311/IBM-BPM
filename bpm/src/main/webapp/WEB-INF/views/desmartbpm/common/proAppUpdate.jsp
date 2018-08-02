<!DOCTYPE html>
<%@ page language="java"  pageEncoding="UTF-8"%>
<html>
<head>
    <title>请选择角色</title>
    <%@include file="head.jsp" %>
    <%@include file="tag.jsp" %>
    <style>
        .layui-form-label{width:140px;}
        .layui-input-block{margin-left:170px;}
        .choose_tri{cursor:pointer;}
        .display_container3 {
            height: 600px;
        }
        .layui-form-item .layui-input-inline{width:70%;}
        .colorli {
            background-color: #9DA5EC;
            color: white;
        }
        .templi {
            background-color: #9DA5EC;
            color: white;
        }
        ul{
            width:198px;
        }
        li{
            height:35px;
            line-height:35px;
            list-style:none;
            padding:0 10px;
            text-align: center;
            cursor:pointer;
            border: 1px solid white;
        }
        .foot_temp{text-align:right;margin-top:20px}
        #my_div {
            width: 715px;
            border: aliceblue;
            margin: 20px;
            padding-bottom: 10px;
        }
        .p1 {
            height: 30px;
            text-align: center;
            width: 215px;
            background-color: #f5f5f5;
            line-height: 30px;
        }
        .divContainer {
            padding-top: 0px;
            float:left;
            width:215px;
            height:350px;
            margin:10px 10px 0 10px;
            border: 1px solid #ccc;
        }
    </style>
</head>
<body>
<div id="my_div">
    <div class="top">

    </div>
    <div class="middle_temp">
        <div id="temp_left"  class="divContainer">
            <P class="p1">请选择应用库</P>
            <div style="height:320px;overflow-y: auto">
                <ul id="left_ul"></ul>
            </div>
        </div>

        <div id="temp_middle"  class=" divContainer" >
            <P class="p1">请选择作为样本的版本</P>
            <div style="height:320px;overflow-y: auto">
                <ul id="middle_ul"  class="contentUl"></ul>
            </div>
        </div>
        <div id="temp_right" class=" divContainer" >
            <P class="p1">请选择升级到哪个版本</P>
            <div style="height:320px;overflow-y: auto">
                <ul id="right_ul"></ul>
            </div>
        </div>
        <h1 style="clear:both;"></h1>
    </div>
    <div class="foot_temp">
        <button class="layui-btn layui-btn sure_btn" id="sure_btn">确定</button>
        <button class="layui-btn layui-btn layui-btn-primary cancel_btn" id="cancel_btn" style="margin-right: 10px;">取消</button>
    </div>
</div>
<script type="text/javascript" src="<%=basePath%>/resources/desmartbpm/js/layui.all.js"></script>
<script>
    var URL = {
        getAllProApp: common.getPath() + '/processAppUpdate/getAllProcessApp',
        getSynchronizedSnapshot: common.getPath() + '/processAppUpdate/findSynchronizedSnapshotByProAppId',
        getUnsynchronizedSnapshot: common.getPath() + '/processAppUpdate/findUnsynchronizedSnapshotByProAppId',
        updateToNewVersion: common.getPath() + '/processAppUpdate/updateToNewVersion',
        synchronizeApp: common.getPath() + '/processAppUpdate/pullDefintionByAppIdAndSnapshotId'
    }

    var ele = {
        appUl: $('#left_ul'),
        synUl: $('#middle_ul'),
        unsynUl: $('#right_ul'),
        sureBtn: $('#sure_btn'),
        cancelBtn: $('#cancel_btn')
    }

    $(function(){
        $('.divContainer').each(function(){
            $(this).on('click', 'li', function(e){
                var $li = $(e.target);
                if ($li.hasClass('colorli')) {
                    // 如果此元素原先被选中，去除选中状态
                    $li.removeClass('colorli');
                } else {
                    // 如果此元素原先未被选中，选中此节点
                    var $otherLis = $li.parent().find('li');
                    $otherLis.each(function(){
                        $(this).removeClass('colorli');
                    });
                    $li.addClass('colorli');
                    if ($li.attr('type') == 'app') {
                        refreshSnapshots($li.data('id'));
                    }
                }
            });
            $(this).on('mouseenter', 'li', function(e){
                var $li = $(e.target);
                $li.addClass('templi');
            });
            $(this).on('mouseleave', 'li', function(e){
                var $li = $(e.target);
                $li.removeClass('templi');
            });
        });

        $('.divContainer').each(function(){
            $(this).on('hov', 'li', function(e){
                var $li = $(e.target);
                if ($li.hasClass('colorli')) {
                    // 如果此元素原先被选中，去除选中状态
                    $li.removeClass('colorli');
                } else {
                    // 如果此元素原先未被选中，选中此节点
                    var $otherLis = $li.parent().find('li');
                    $otherLis.each(function(){
                        $(this).removeClass('colorli');
                    });
                    $li.addClass('colorli');
                    if ($li.attr('type') == 'app') {
                        refreshSnapshots($li.data('id'));
                    }
                }
            });
        });


        // 获得应用库信息
        $.post(URL.getAllProApp, {}, function(result){
            if (result.status == 0) {
                drawAppLi(result.data);
            }
        });

        ele.sureBtn.click(updateApp);
    });

    function drawAppLi(apps) {
        if (!apps.length) {
            return;
        }
        var content = '';
        for (var i = 0; i < apps.length; i++) {
            var app = apps[i];
            content += '<li type="app" data-id="' + app.proAppId + '" data-name="' + app.proAppName + '">' + app.proAppName + '</li>';
        }
        ele.appUl.append(content);
    }


    function refreshSnapshots(proAppId) {
        if (!proAppId) {
            return;
        }
        ele.synUl.html('');
        ele.unsynUl.html('');
        $.post(URL.getSynchronizedSnapshot, {'proAppId': proAppId}, function(result){
            if (result.status == 0) {
                drawSynLi(result.data);
            }
        });
        $.post(URL.getUnsynchronizedSnapshot, {'proAppId': proAppId}, function(result){
            if (result.status == 0) {
                drawUnsynLi(result.data);
            }
        });
    }

    function drawSynLi(synSnapshots) {
        ele.synUl.empty();
        if (!synSnapshots || !synSnapshots.length) {
            return;
        }
        var content = '';
        for (var i = 0; i < synSnapshots.length; i++) {
            var snapshot = synSnapshots[i];
            if (!snapshot.snapshotName) {
                continue;
            }
            var snapshotName = snapshot.snapshotName.length > 10 ? snapshot.snapshotName.substring(0,10) + '...' : snapshot.snapshotName;
            content += '<li data-id="' + snapshot.snapshotId + '" title="'+ snapshot.snapshotName +'&#10;创建时间：'+ snapshot.createTime +'" data-name="' + snapshot.snapshotName + '">'
                + snapshotName + '</li>';
        }
        ele.synUl.append(content);
    }

    function drawUnsynLi(synSnapshots) {
        ele.unsynUl.empty();
        if (!synSnapshots || !synSnapshots.length) {
            return;
        }
        var content = '';
        for (var i = 0; i < synSnapshots.length; i++) {
            var snapshot = synSnapshots[i];
            if (!snapshot.snapshotName) {
                continue;
            }
            var snapshotName = snapshot.snapshotName.length > 10 ? snapshot.snapshotName.substring(0,10) + '...' : snapshot.snapshotName;
            content += '<li data-id="' + snapshot.snapshotId + '" title="'+ snapshot.snapshotName +'&#10;创建时间：'+ snapshot.createTime +'" data-name="' + snapshot.snapshotName + '">'
                + snapshotName + '</li>';
        }
        ele.unsynUl.append(content);
    }

    function updateApp() {
        var appLi = ele.appUl.find(".colorli");
        var synLi = ele.synUl.find(".colorli");
        var unsynLi = ele.unsynUl.find(".colorli");
        if (appLi.length != 1) {
            layer.alert('请选择应用库');
            return;
        }
        if (unsynLi.length != 1) {
            layer.alert('请选择想要升级到哪个版本');
            return;
        }
        var appId = appLi.data('id');
        var newSnapshotId = unsynLi.data('id');
        if (synLi.length == 1) {
            // 选择了老版本应用库
            var oldSnapshotId = synLi.data('id');
            var confirmIndex = layer.confirm('是否升级应用库？</br><b>应用库：</b>' + appLi.data('name') + '<br>'
                + '<b>老版本：</b>' + synLi.data('name') + '</br><b>新版本：</b>' + unsynLi.data('name'), {
                btn: ['确定', '取消']
            }, function () {
                layer.close(confirmIndex); // 关闭confirm层
                doUpdate(appId, oldSnapshotId, newSnapshotId);
            }, function () {});

        } else if (synLi.length == 0) {
            // 为选择老版本应用库
            var confirmIndex = layer.confirm('未选择老版本，是否直接同步应用库不复制配置？</br><b>应用库：</b>' + appLi.data('name') + '<br>'
                + '<b>新版本：</b>' + unsynLi.data('name'), {
                btn: ['确定', '取消']
            }, function () {
                layer.close(confirmIndex); // 关闭confirm层
                doSynchronizeApp(appId, newSnapshotId);
            }, function () {});
        }
    }

    function doUpdate(appId, oldSnapshotId, newSnapshotId) {
        $.ajax({
            url: URL.updateToNewVersion,
            type: 'post',
            data: {
                proAppId: appId,
                oldProVerUid: oldSnapshotId,
                newProVerUid: newSnapshotId
            },
            dataType: 'json',
            beforeSend : function(){
                layer.load(1);
            },
            success : function(result) {
                layer.closeAll('loading');
                if (result.status == 0) {
                    refreshSnapshots(appId);
                    layer.alert('升级成功');
                } else {
                    layer.alert(result.msg);
                }
            },
            error: function() {
                layer.closeAll('loading');
                layer.alert('操作失败请稍后再试');
            }
        });
    }

    function doSynchronizeApp(appId,  newSnapshotId) {
        $.ajax({
            url: URL.synchronizeApp,
            type: 'post',
            data: {
                proAppId: appId,
                proVerUid: newSnapshotId
            },
            dataType: 'json',
            beforeSend : function(){
                layer.load(1);
            },
            success : function(result) {
                layer.closeAll('loading');
                if (result.status == 0) {
                    refreshSnapshots(appId);
                    layer.alert('同步成功');
                } else {
                    layer.alert(result.msg);
                }
            },
            error: function() {
                layer.closeAll('loading');
                layer.alert('操作失败请稍后再试');
            }
        });
    }

</script>
</body>
</html>