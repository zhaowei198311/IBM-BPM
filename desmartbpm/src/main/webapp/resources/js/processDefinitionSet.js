$(function() {
    // “返回”按钮
    $("#back_btn").click(function () {
        window.history.back();
    });

    // “保存”按钮
    $("#save_btn").click(function () {
        var data = getData();
        console.log(data);
        if (!data.isOk) {
            layer.alert(data.msg);
            return;
        }
        $.ajax({

        });

    });

    // “配置完成”按钮
    $("#finish_btn").click(function () {

    });

});

// 获取页面上的数据
function getData() {
    var data = {};
    data.isOk = true; // 数据是否都正常
    data.msg = "";    // 错误信息
    data.proUid = $('[name="proUid"]').val();
    data.proAppId = $('[name="proAppId"]').val();
    data.proVerUid = $('[name="proVerUid"]').val();
    data.proTime = $('[name="proTime"]').val();
    data.proDynaforms = $('[name="proDynaforms"]').val();
    data.proTimeUnit = $('[name="proTimeUnit"]').val();
    data.proDerivationScreenTpl = $('[name="proDerivationScreenTpl"]').val();
    data.proTriStart = $('[name="proTriStart"]').val();
    data.proTriPaused = $('[name="proTriPaused"]').val();
    data.proTriReassigned = $('[name="proTriReassigned"]').val();
    data.proTriDeleted = $('[name="proTriDeleted"]').val();
    data.proTriUnpaused = $('[name="proTriUnpaused"]').val();
    data.proTriCanceled = $('[name="proTriCanceled"]').val();
    data.proHeight = $('[name="proHeight"]').val();
    data.proWidth = $('[name="proWidth"]').val();
    data.proTitleX = $('[name="proTitleX"]').val();
    data.proTitleY = $('[name="proTitleY"]').val();


    if(!/^[0-9]*[1-9][0-9]*$/.test(data.proHeight) || !/^[0-9]*[1-9][0-9]*$/.test(data.proWidth)
        || !/^[0-9]*[1-9][0-9]*$/.test(data.proTitleX) || !/^[0-9]*[1-9][0-9]*$/.test(data.proTitleY)){
        data.isOk = false;
        data.msg = "流程图信息，需要输入正整数";
    }
    return data;
}