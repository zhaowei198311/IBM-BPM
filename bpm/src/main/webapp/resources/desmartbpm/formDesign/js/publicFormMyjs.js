var view = null;
var oldName = "";
var title = "";
//显示填写事件脚本的js
function showEventModal(obj) {
    $("#editEventModal").modal("show");
    title = $(obj).attr("title");
    $("#editEventModal textarea").val($(obj).val());
    $("#editEventModal .modal-header h3").text("给组件添加" + title + "事件");
}

function saveEditEvent() {
    var eventStr = $("#editEventModal textarea").val();
    $("textarea[title='" + title + "']").val(eventStr);
}

function saveEvent() {
    var inputObj = view.find("input");
    if (view.find("input").length == 0) {
        inputObj = view.find("textarea");
    }
    var onchangeStr = $("#textAddEventModal textarea[title='onchange']").val().replace(/\"/g, "\"");
    var onfocusStr = $("#textAddEventModal textarea[title='onfocus']").val().replace(/\"/g, "\"");
    var onkeypreStr = $("#textAddEventModal textarea[title='onkeypress']").val().replace(/\"/g, "\"");
    inputObj.attr({
        "onchange": onchangeStr,
        "onfocus": onfocusStr,
        "onkeypress": onkeypreStr
    });
}

//显示给文本框添加事件的模态框
function textAddEventModal(obj) {
    view = $(obj).parent().next().next();
    var inputObj = view.find("input");
    if (view.find("input").length == 0) {
        inputObj = view.find("textarea");
    }
    var onchangeStr = inputObj.attr("onchange");
    var onfocusStr = inputObj.attr("onfocus");
    var onkeypreStr = inputObj.attr("onkeypress");
    $("#textAddEventModal textarea[title='onchange']").val(onchangeStr);
    $("#textAddEventModal textarea[title='onfocus']").val(onfocusStr);
    $("#textAddEventModal textarea[title='onkeypress']").val(onkeypreStr);
    $("#textAddEventModal").modal("show");
}

//显示填写select组件事件脚本的js
function saveSelectEvent() {
    var inputObj = view.find("select");
    var onchangeStr = $("#selectAddEventModal textarea[title='onchange']").val().replace(/\"/g, "\"");
    inputObj.attr({
        "onchange": onchangeStr
    });
}

function selectAddEventModal(obj) {
    view = $(obj).parent().next().next();
    var onchangeStr = view.find("select").attr("onchange");
    $("#selectAddEventModal textarea[title='onchange']").val(onchangeStr);
    $("#selectAddEventModal").modal("show");
}

//给单选框、复选框加点击事件
function saveClickEvent() {
    var subObj = view.find("input[type='radio']");
    if (subObj.length == 0) {
        subObj = view.find("input[type='checkbox']");
    }
    var onclickStr = $("#clickAddEventModal textarea[title='onclick']").val().replace(/\"/g, "\"");
    subObj.attr({
        "onclick": onclickStr
    });
}

function clickAddEventModal(obj) {
    view = $(obj).parent().next().next();
    var subObj = view.find("input[type='radio']");
    if (subObj.length == 0) {
        subObj = view.find("input[type='checkbox']");
    }
    var onclickStr = subObj.attr("onclick");
    $("#clickAddEventModal textarea[title='onclick']").val(onclickStr);
    $("#clickAddEventModal").modal("show");
}

//显示设置文本属性的模态框
function showTextModal(obj) {
    $("#textModal").modal("show");

    view = $(obj).parent().next().next();
    var label = view.find("label").text();
    var inputObj = view.find("input");
    var defaultVal = inputObj.val();
    var id = inputObj.attr("id");
    var name = inputObj.prop("name");
    var place = inputObj.attr("placeholder");
    var textWidth = inputObj.width();
    var textLabelWidth = view.find(".labelDiv").width();
    var regx = inputObj.attr("regx");
    var regxCue = inputObj.attr("regx_cue");
    oldName = name;
    var rowWidth = $(".demo").width() - 5;
    var colWidth = rowWidth / 12;

    var textCol = inputObj.attr("col");
    var textLabelCol = view.find(".labelDiv").attr("col");

    $("#text-label").val(label);
    $("#text-name").val(name);
    $("#text-name").onlyNumAlpha(); //只能输入英文
    $("#text-id").val(id);
    $("#text-default-value").val(defaultVal);
    $("#text-place").val(place);
    $("#text-regx").val(regx);
    $("#text-regx-cue").val(regxCue);
    $("#text-width").val(textCol);
    $("#text-label-width").val(textLabelCol);

    var num = view.find(".labelDiv").find("span").length;
    if (num == 0) {
        $("#text-must").removeAttr("checked");
    } else {
        $("#text-must").attr("checked", "checked");
    }
};

//设置数字文本框属性的模态框
function showNumberModal(obj) {
    $("#numberModal").modal("show");

    view = $(obj).parent().next().next();
    var label = view.find("label").text();
    var inputObj = view.find("input");
    var defaultVal = inputObj.val();
    var id = inputObj.attr("id");
    var place = inputObj.attr("placeholder");
    var name = inputObj.attr("name");
    var textWidth = inputObj.width();
    var textLabelWidth = view.find(".labelDiv").width();
    oldName = name;
    var rowWidth = $(".demo").width() - 5;
    var colWidth = rowWidth / 12;

    var textCol = inputObj.attr("col");
    var textLabelCol = view.find(".labelDiv").attr("col");
    inputObj.desNumber();
    $("#number-label").val(label);
    $("#number-name").val(name);
    $("#number-name").onlyNumAlpha(); //只能输入英文
    $("#number-id").val(id);
    $("#number-default-value").desNumber();
    $("#number-default-value").val(defaultVal);
    $("#number-place").val(place);
    $("#number-width").val(textCol);
    $("#number-label-width").val(textLabelCol);

    var num = view.find(".labelDiv").find("span").length;
    if (num == 0) {
        $("#number-must").removeAttr("checked");
    } else {
        $("#number-must").attr("checked", "checked");
    }
}

//设置日期文本框属性的模态框
function showDateModal(obj) {
    $("#dateModal").modal("show");

    view = $(obj).parent().next().next();
    var label = view.find("label").text();
    var inputObj = view.find("input");

    var id = inputObj.attr("id");
    var place = inputObj.attr("placeholder");
    var name = inputObj.attr("name");
    var textWidth = inputObj.width();
    var textLabelWidth = view.find(".labelDiv").width();
    oldName = name;
    var rowWidth = $(".demo").width() - 5;
    var colWidth = rowWidth / 12;

    var textCol = inputObj.attr("col");
    var textLabelCol = view.find(".labelDiv").attr("col");
    $("#date-label").val(label);
    $("#date-name").val(name);
    $("#date-id").val(id);

    $("#date-place").val(place);
    $("#date-width").val(textCol);
    $("#date-label-width").val(textLabelCol);

    var num = view.find(".labelDiv").find("span").length;
    if (num == 0) {
        $("#date-must").removeAttr("checked");
    } else {
        $("#date-must").attr("checked", "checked");
    }
}

//设置多行文本框属性的模态框
function showTextareaModal(obj) {
    $("#textareaModal").modal("show");

    view = $(obj).parent().next().next();
    var label = view.find("label").text();
    var textareaObj = view.find("textarea");
    var defaultVal = textareaObj.val();
    var id = textareaObj.attr("id");
    var name = textareaObj.attr("name");
    var rows = textareaObj.attr("rows");
    var textWidth = textareaObj.width();
    var textLabelWidth = view.find(".labelDiv").width();
    oldName = name;
    var rowWidth = $(".demo").width() - 5;
    var colWidth = rowWidth / 12;

    var textCol = textareaObj.attr("col");
    var textLabelCol = view.find(".labelDiv").attr("col");

    $("#textarea-label").val(label);
    $("#textarea-name").val(name);
    $("#textarea-name").onlyNumAlpha(); //只能输入英文
    $("#textarea-id").val(id);
    $("#textarea-default-value").val(defaultVal);
    $("#textarea-width").val(textCol);
    $("#textarea-label-width").val(textLabelCol);
    $("#textarea-row").number();
    $("#textarea-row").val(rows);

    var num = view.find(".labelDiv").find("span").length;
    if (num == 0) {
        $("#textarea-must").removeAttr("checked");
    } else {
        $("#textarea-must").attr("checked", "checked");
    }
}

//设置下拉列表框属性的模态框
function showSelectModal(obj) {
    $("#selectModal").modal("show");

    view = $(obj).parent().next().next();
    var label = view.find("label").text();
    var selectObj = view.find("select");
    var id = selectObj.attr("id");
    var name = selectObj.attr("name");
    var dataSource = selectObj.attr("data_source");
    var databaseType = selectObj.attr("database_type");

    var textWidth = selectObj.width();
    var textLabelWidth = view.find(".labelDiv").width();
    oldName = name;
    var rowWidth = $(".demo").width() - 5;
    var colWidth = rowWidth / 12;

    var textCol = selectObj.attr("col");
    var textLabelCol = view.find(".labelDiv").attr("col");

    $("#select-label").val(label);
    $("#select-name").val(name);
    $("#select-name").onlyNumAlpha(); //只能输入英文
    $("#select-id").val(id);
    $("#select-width").val(textCol);
    $("#select-label-width").val(textLabelCol);

    if (dataSource == "数据字典拉取") {
        $(".hand_act").css("display", "none");
        $(".database").css("display", "block");
        $("input[value='数据字典拉取']").prop("checked", true);
        $(".database select").val(databaseType);
    } else {
        $(".hand_act").css("display", "block");
        $(".database").css("display", "none");
        $("input[value='手动填写']").prop("checked", true);

        $("#selectModal .add-form-obj").remove();
        var optionValueArr = selectObj.children();
        $(".option-value").val($(optionValueArr[0]).val());
        for (var i = 1; i < optionValueArr.length; i++) {
            var html = "<div class='form-group add-form-obj'>" +
                "<div class='col-xs-7 col-sm-offset-4'>";
            html += "<input type='text' " + "class='form-control option-value' " +
                "value='" + $(optionValueArr[i]).val() + "'" +
                "placeholder='请输入列表选项值'/>";
            html += " <span class='glyphicon glyphicon-minus' onclick='removeOptionInput(this)'" +
                "style='font-size:20px;color:#888;cursor:pointer;'></span>" +
                " <span class='glyphicon glyphicon-plus' onclick='addOptionInput(this)'" +
                "style='font-size:20px;color:#888;cursor:pointer;'></span>";
            html += "</div></div>";;
            $("#selectModal form").append(html);
        }
    }

    var num = view.find(".labelDiv").find("span").length;
    if (num == 0) {
        $("#select-must").removeAttr("checked");
    } else {
        $("#select-must").attr("checked", "checked");
    }
}

//设置单选框属性的模态框
function showRadioModal(obj) {
    $("#radioModal").modal("show");

    view = $(obj).parent().next().next();
    var label = view.find(".labelDiv label").text();
    var inputRadioObj = view.find("input[type='radio']");
    var id = inputRadioObj.attr("class");
    var name = inputRadioObj.attr("name");
    var textLabelCol = view.find(".labelDiv").attr("col");
    var textCol = view.find(".subDiv").attr("col");
    oldName = name;
    $("#radio-id").val(id);
    $("#radio-label").val(label);
    $("#radio-name").val(name);
    $("#radio-name").onlyNumAlpha(); //只能输入英文
    $("#radio-label-width").val(textLabelCol);
    $("#radio-width").val(textCol);

    $(".radio-value").val($(view.find(".subDiv label")[0]).text().trim());
    $("#radioModal .add-form-obj").remove();
    for (var i = 1; i < view.find(".subDiv label").length; i++) {
        var radioLabelObj = $(view.find(".subDiv label")[i]);
        var radioAddVal = radioLabelObj.text().trim();
        var html = "<div class='form-group add-form-obj'>" +
            "<div class='col-xs-7 col-sm-offset-4'>";
        html += "<input type='text' " +
            "class='form-control radio-value' " +
            "value='" + radioAddVal + "' " +
            "placeholder='请输入单选框选项值'/>";
        html += " <span class='glyphicon glyphicon-minus' onclick='removeRadioInput(this)'" +
            "style='font-size:20px;color:#888;cursor:pointer;'></span>" +
            " <span class='glyphicon glyphicon-plus' onclick='addRadioInput(this)'" +
            "style='font-size:20px;color:#888;cursor:pointer;'></span>";
        html += "</div></div>";
        $("#radioModal form").append(html);
    }

    var num = view.find(".labelDiv").find("span").length;
    if (num == 0) {
        $("#radio-must").removeAttr("checked");
    } else {
        $("#radio-must").attr("checked", "checked");
    }
}

//设置多选框属性的模态框
function showCheckboxModal(obj) {
    $("#checkboxModal").modal("show");

    view = $(obj).parent().next().next();
    var label = view.find(".labelDiv label").text();
    var inputCheckboxObj = view.find(".subDiv input[type='checkbox']");
    var id = inputCheckboxObj.attr("class");
    var name = inputCheckboxObj.attr("name");
    var textLabelCol = view.find(".labelDiv").attr("col");
    var textCol = view.find(".subDiv").attr("col");
    oldName = name;
    $("#checkbox-id").val(id);
    $("#checkbox-label").val(label);
    $("#checkbox-name").val(name);
    $("#checkbox-name").onlyNumAlpha(); //只能输入英文
    $("#checkbox-label-width").val(textLabelCol);
    $("#checkbox-width").val(textCol);

    var checkboxLabelArr = view.find(".subDiv label");
    $(".checkbox-value").val($(checkboxLabelArr[0]).text().trim());

    if ($(checkboxLabelArr[0]).find("input").is(":checked")) {
        $(".checkbox-value").next().find("input").attr("checked", "checked");
    } else {
        $(".checkbox-value").next().find("input").removeAttr("checked");
    }

    $("#checkboxModal .add-form-obj").remove();
    for (var i = 1; i < checkboxLabelArr.length; i++) {
        var checkboxLabelObj = $(view.find(".subDiv label")[i]);
        var checkboxAddVal = checkboxLabelObj.text().trim();

        var html = "<div class='form-group add-form-obj'>" +
            "<div class='col-xs-7 col-sm-offset-4'>";

        html += "<input type='text' " +
            "class='col-xs-4 checkbox-value' " +
            "placeholder='请输入复选框选项值' value='" + checkboxAddVal + "'/> " +
            "<div class='col-sm-offset-1 col-xs-1' style='margin-top:4px;'>"

        if (checkboxLabelObj.find("input").is(":checked")) {
            html += "<input type='checkbox' title='勾选让该选项选中' checked/> ";
        } else {
            html += "<input type='checkbox' title='勾选让该选项选中'/> ";
        }

        html += "</div>";
        html += " <span class='glyphicon glyphicon-minus' onclick='removeCheckboxInput(this)'" +
            "style='font-size:20px;color:#888;margin:4px 0 0 7px;cursor:pointer;'></span>" +
            " <span class='glyphicon glyphicon-plus' onclick='addCheckboxInput(this)'" +
            "style='font-size:20px;color:#888;margin:4px 0 0 5px;cursor:pointer;'></span>";
        html += "</div></div>";
        $("#checkboxModal form").append(html);
    }

    var num = view.find(".labelDiv").find("span").length;
    if (num == 0) {
        $("#checkbox-must").removeAttr("checked");
    } else {
        $("#checkbox-must").attr("checked", "checked");
    }
}

//设置文件上传按钮属性的模态框
function showLoadFileModal(obj) {
    $("#loadFileModal").modal("show");

    view = $(obj).parent().next().next();
    var label = view.find(".labelDiv label").text();
    var loadFileObj = view.find(".subDiv input[type='button']");
    var id = loadFileObj.attr("id");
    var defaultVal = loadFileObj.val();
    var textLabelCol = view.find(".labelDiv").attr("col");
    var textCol = view.find(".subDiv").attr("col");

    var maxFileSize = view.find(".maxFileSize").val();
    var maxFileCount = view.find(".maxFileCount").val();

    var fileFormatStr = view.find(".fileFormat").val();
    var fileFormatArr = fileFormatStr.split(",");
    var fileFormatCheckArr = $(".format-check");

    var addFileFormatStr = "";
    for (var i = 0; i < fileFormatArr.length; i++) {
        var formatStr = fileFormatArr[i];
        var addFlag = true;
        for (var j = 0; j < fileFormatCheckArr.length; j++) {
            var formatCheckLabelStr = $(fileFormatCheckArr[j]).parent().text().trim();
            if (formatCheckLabelStr == formatStr) {
                $("#format-" + formatStr).attr("checked", "checked");
                addFlag = false;
            }
        }
        if (addFlag) {
            addFileFormatStr += formatStr + ",";
        }
    }
    if (addFileFormatStr.substr(addFileFormatStr.length - 1, 1) == ",") {
        addFileFormatStr = addFileFormatStr.substr(0, addFileFormatStr.length - 1);
    }
    $("#loadFile-format").val(addFileFormatStr);

    $("#loadFile-id").val(id);
    $("#loadFile-name").val(label);
    $("#loadFile-label-width").val(textLabelCol);
    $("#loadFile-width").val(textCol);
    $("#loadFile-defaultVal").val(defaultVal);
    $("#loadFile-maxSize").val(maxFileSize);
    $("#loadFile-maxCount").val(maxFileCount);
    $("#loadFile-maxSize").desNumber();
    $("#loadFile-maxCount").number();

    var num = view.find(".labelDiv").find("span").length;
    if (num == 0) {
        $("#checkbox-must").removeAttr("checked");
    } else {
        $("#checkbox-must").attr("checked", "checked");
    }
}

//设置富文本框属性的模态框
function showEditorModal(obj) {
    $("#editorAreaModal").modal("show");

    view = $(obj).parent().next().next();
    var textareaObj = view.find("textarea");
    var id = textareaObj.attr("id");

    var rowWidth = $(".demo").width() - 5;
    var colWidth = rowWidth / 12;

    var textCol = textareaObj.attr("col");
    var textLabelCol = view.find(".labelDiv").attr("col");

    $("#editor-id").val(id);
    $("#editor-width").val(textCol);
    $("#editor-label-width").val(textLabelCol);
}

//设置文本块属性的模态框
function showTextBlockModal(obj) {
    $("#textBlockModal").modal("show");

    view = $(obj).parent().next().next();
    var pObj = view.find("p");

    var textRow = pObj.parent().attr("row");
    var textCol = pObj.parent().attr("col");

    $("#text-block-row").val(textRow);
    $("#text-block-col").val(textCol);
}

function showDataTableModal(obj) {
    $("#dataTableModal").modal("show");

    view = $(obj).parent().next().next();
    var tableObj = view.find(".subDiv table");
    var id = tableObj.attr("id");
    var name = tableObj.attr("name");
    var thObjArr = tableObj.find("thead th");
    var thNum = thObjArr.length;

    $("#dataTableModal .data-table-set").remove();
    for (var i = 0; i < thObjArr.length; i++) {
        var thObj = $(thObjArr[i]);
        var thText = thObj.text();
        var thSetHtml = '<div class="form-group col-xs-12 data-table-set">' +
            '<label class="col-xs-2 col-sm-offset-2 control-label">' +
            '表格列头<span style="color:red;float:left;">*</span>' +
            '</label>' +
            '<div class="col-xs-1">' +
            '<input type="text" class="col-xs-12 col data-table-head"' +
            'placeholder="列头" style="width:70px;" value="' + thText + '">' +
            '</div>' +
            '<label class="col-xs-1 col-sm-offset-1 control-label">' +
            '列组件类型' +
            '</label>' +
            '<div class="col-xs-2">' +
            '<select class="data-table-type col-xs-12">';
        var thType = thObj.attr("col-type");
        switch (thType) {
            case "text":
                {
                    thSetHtml += '<option value="text" selected>文本框</option>' +
                    '<option value="number">数字框</option>' +
                    '<option value="date">日期文本框</option>' +
                    '<option value="select">下拉列表</option>';
                    break;
                }
            case "number":
                {
                    thSetHtml += '<option value="text">文本框</option>' +
                    '<option value="number" selected>数字框</option>' +
                    '<option value="date">日期文本框</option>' +
                    '<option value="select">下拉列表</option>';
                    break;
                }
            case "date":
                {
                    thSetHtml += '<option value="text">文本框</option>' +
                    '<option value="number">数字框</option>' +
                    '<option value="date" selected>日期文本框</option>' +
                    '<option value="select">下拉列表</option>';
                    break;
                }
            case "select":
                {
                    thSetHtml += '<option value="text">文本框</option>' +
                    '<option value="number">数字框</option>' +
                    '<option value="date">日期文本框</option>' +
                    '<option value="select" selected>下拉列表</option>';
                    break;
                }
        }
        thSetHtml += '</select></div></div>';
        $("#dataTableModal form").append(thSetHtml);
    }

    $("#data-table-number").blur(function () {
        var forNum = $("#data-table-number").val();
        $("#dataTableModal .data-table-set").remove();
        for (var i = 0; i < forNum; i++) {
            var thSetHtml = '<div class="form-group col-xs-12 data-table-set">' +
                '<label class="col-xs-2 col-sm-offset-2 control-label">' +
                '表格列头<span style="color:red;float:left;">*</span>' +
                '</label>' +
                '<div class="col-xs-1">' +
                '<input type="text" class="col-xs-12 col data-table-head"' +
                'placeholder="列头" style="width:70px;">' +
                '</div>' +
                '<label class="col-xs-1 col-sm-offset-1 control-label">' +
                '列组件类型' +
                '</label>' +
                '<div class="col-xs-2">' +
                '<select class="data-table-type col-xs-12">' +
                '<option value="text">文本框</option>' +
                '<option value="number">数字框</option>' +
                '<option value="date">日期文本框</option>' +
                '<option value="select">下拉列表</option>' +
                '</select></div></div>';
            $("#dataTableModal form").append(thSetHtml);
        } //end for

        for (var i = 0; i < thObjArr.length; i++) {
            var thObj = $(thObjArr[i]);
            $($(".data-table-head")[i]).val(thObj.text());
            $($(".data-table-type")[i]).val(thObj.attr("col-type"));
        }
    }); //end blur

    $("#data-table-id").val(id);
    $("#data-table-name").val(name);
    $("#data-table-number").val(thNum);
}

var nameArr = new Array();

//新建、修改表单组件时判断该组件的name是否重复
function nameIsRepeat(name) {
    if (oldName == name) {
        return false;
    } else if ($.inArray(name, nameArr) == -1) {
        nameArr.splice($.inArray(oldName, nameArr), 1);
        nameArr.push(name);
        return false;
    } else {
        return true;
    }
}

//下拉列表的数据源选择
function dataSourceClick(obj) {
    var dataSource = $(obj).val();
    console.log(dataSource);
    if (dataSource == "数据字典拉取") {
        $(".database").css("display", "block");
        $(".hand_act").css("display", "none");
    } else {
        $(".database").css("display", "none");
        $(".hand_act").css("display", "block");
    }
}

//下拉列表的数据类型选择
function selectData() {
    var selectObj = $(".database").find("select");
    selectObj.children().remove();
    $.ajax({
        url: common.getPath() + "/sysDictionary/listAllOnSysDictionary",
        method: "post",
        success: function (result) {
            if (result.status == 0) {
                var dicList = result.data;
                for (var i = 0; i < dicList.length; i++) {
                    var dicObj = dicList[i];
                    var optionObj = '<option value="' + dicObj.dicUid + '">' + dicObj.dicName + '</option>';
                    selectObj.append(optionObj);
                }
            }
        }
    });
}

//日期控件图标的宽度
var dateIconWidth = 28;

$(function () {
    $(".col").number();
    $(".col").bind('input propertychange', function () {
        var colVal = $(this).val();
        if (colVal < 0) {
            $(this).val(0);
        } else if (colVal >= 12) {
            $(this).val(12);
        }
    });
    $(".col").blur(function () {
        var colVal = $(this).val();
        if (colVal == 0 || colVal == "" || colVal == null) {
            $(this).val(1);
        }
    });
    var rowWidth = $(".demo").width() - 5;
    var colWidth = rowWidth / 12;
    $(".demo").css("display", "none");
    $(".row-fluid .span12").css({
        "float": "left",
        "margin-left": "0px",
        "width": colWidth * 12
    });
    $(".row-fluid .span11").css({
        "float": "left",
        "margin-left": "0px",
        "width": colWidth * 11
    });
    $(".row-fluid .span10").css({
        "float": "left",
        "margin-left": "0px",
        "width": colWidth * 10
    });
    $(".row-fluid .span9").css({
        "float": "left",
        "margin-left": "0px",
        "width": colWidth * 9
    });
    $(".row-fluid .span8").css({
        "float": "left",
        "margin-left": "0px",
        "width": colWidth * 8
    });
    $(".row-fluid .span7").css({
        "float": "left",
        "margin-left": "0px",
        "width": colWidth * 7
    });
    $(".row-fluid .span6").css({
        "float": "left",
        "margin-left": "0px",
        "width": colWidth * 6
    });
    $(".row-fluid .span5").css({
        "float": "left",
        "margin-left": "0px",
        "width": colWidth * 5
    });
    $(".row-fluid .span4").css({
        "float": "left",
        "margin-left": "0px",
        "width": colWidth * 4
    });
    $(".row-fluid .span3").css({
        "float": "left",
        "margin-left": "0px",
        "width": colWidth * 3
    });
    $(".row-fluid .span2").css({
        "float": "left",
        "margin-left": "0px",
        "width": colWidth * 2
    });
    $(".row-fluid .span1").css({
        "float": "left",
        "margin-left": "0px",
        "width": colWidth * 1
    });

    var labelColNum = $(".demo").find(".labelDiv").attr("col");
    $(".demo").find(".labelDiv").css("width", colWidth * labelColNum);
    for (var i = 0; i < $(".demo").find(".subDiv").length; i++) {
        var inputDiv = $($(".demo").find(".subDiv")[i]);
        var colNum = inputDiv.attr("col");
        inputDiv.css("width", colNum * colWidth - 18);
        if (inputDiv.children().length > 1) {
            if ($(inputDiv.children()[0]).attr("class") == "editor_textarea") {
                inputDiv.find("div").css("width", colNum * colWidth - 18);
            } else if ($(inputDiv.children()[0]).prop("type") == "button") {
                continue;
            } else {
                $(inputDiv.children()[0]).css("width", colNum * colWidth - 18 - dateIconWidth);
            }
        } else {
            if ($(inputDiv.children()[0]).prop("tagName") == "SELECT") {
                $(inputDiv.children()[0]).css("width", colNum * colWidth - 3);
            } else if ($(inputDiv.children()[0]).prop("type") == "button") {
                continue;
            } else {
                $(inputDiv.children()[0]).css("width", colNum * colWidth - 18);
            }
        }
    }

    $(".demo").css("display", "block");
    //给name数组赋值
    nameArr = $("#downloadModal #nameArr").val().split(",");

    $(".demo").delegate(".remove", "click", function (e) {
        e.preventDefault();
        var removeName = "";
        if ($(this).parent().find(".subDiv").find("label").length >= 1) {
            removeName = $($(this).parent().find(".subDiv label").children()[0]).attr("name");
        } else {
            removeName = $($(this).parent().find(".subDiv").children()[0]).attr("name");
        }
        nameArr.splice($.inArray(removeName, nameArr), 1);
    })

    //给下拉框数据来源赋值
    selectData();

    //保存单行文本框的属性编辑
    $("#save-text-content").click(function (e) {
        e.preventDefault();
        var id = $("#text-id").val();
        var name = $("#text-name").val().trim();
        if (id == "" || id == null || name == null || name == "") {
            $("#text-warn").modal('show');
        } else if (nameIsRepeat(name)) { //判断组件name是否重复
            $("#text-warn").html("<strong>警告！</strong>您输入的name重复，请重新输入");
            $("#text-warn").modal('show');
        } else {
            var isRegx = false;
            var regx = $("#text-regx").val();
            if (regx != null && regx != "") {
                try {  
                    isRegx = true;
                    new  RegExp(regx);
                } catch (e) {  
                    isRegx = false;
                    $("#text-warn").html("<strong>警告！</strong>请输入有效正则表达式");
                    $("#text-warn").modal('show');
                } 
            }
            if (isRegx) {
                rowWidth = $(".demo").width() - 5;
                colWidth = rowWidth / 12;
                var label = $("#text-label").val();

                var regxCue = $("#text-regx-cue").val();
                var defaultVal = $("#text-default-value").val().trim();
                if (defaultVal != null && defaultVal != "") {
                    try {
                        regx.test(defaultVal);
                        var place = $("#text-place").val();
                        var isMust = $("#text-must").is(':checked');

                        var textWidth = $("#text-width").val() * colWidth;
                        var textLabelWidth = $("#text-label-width").val() * colWidth;

                        view.find("label").text(label);
                        var inputObj = view.find("input");
                        inputObj.attr("value", defaultVal);
                        inputObj.attr({
                            "id": id,
                            "placeholder": place,
                            "name": name
                        });

                        view.find(".labelDiv").css("width", textLabelWidth).attr("col", $("#text-label-width").val());
                        inputObj.parent().css("width", textWidth - 18).attr("col", $("#text-width").val());
                        inputObj.css("width", textWidth - 18).attr("col", $("#text-width").val()).attr({
                            "regx": regx,
                            "regx_cue": regxCue
                        });

                        if (isMust) {
                            var num = view.find(".labelDiv").find("span").length;
                            if (num == 0) {
                                view.find(".labelDiv").prepend("<span style='color:red;float:right;'>*</span>");
                            }
                        } else {
                            view.find(".labelDiv").find("span").remove();
                        }
                        $("#text-warn").modal('hide');
                        $("#textModal").modal("hide");
                    } catch (e) {
                        $("#text-warn").html("<strong>警告！</strong>默认值和正则表达式不匹配");
                        $("#text-warn").modal('show');
                    }
                } else {
                    var place = $("#text-place").val();
                    var isMust = $("#text-must").is(':checked');

                    var textWidth = $("#text-width").val() * colWidth;
                    var textLabelWidth = $("#text-label-width").val() * colWidth;

                    view.find("label").text(label);
                    var inputObj = view.find("input");
                    inputObj.attr("value", defaultVal);
                    inputObj.attr({
                        "id": id,
                        "placeholder": place,
                        "name": name
                    });

                    view.find(".labelDiv").css("width", textLabelWidth).attr("col", $("#text-label-width").val());
                    inputObj.parent().css("width", textWidth - 18).attr("col", $("#text-width").val());
                    inputObj.css("width", textWidth - 18).attr("col", $("#text-width").val()).attr({
                        "regx": regx,
                        "regx_cue": regxCue
                    });

                    if (isMust) {
                        var num = view.find(".labelDiv").find("span").length;
                        if (num == 0) {
                            view.find(".labelDiv").prepend("<span style='color:red;float:right;'>*</span>");
                        }
                    } else {
                        view.find(".labelDiv").find("span").remove();
                    }
                    $("#text-warn").modal('hide');
                    $("#textModal").modal("hide");
                }
            } else { //else 正则为空
                rowWidth = $(".demo").width() - 5;
                colWidth = rowWidth / 12;
                var label = $("#text-label").val();

                var defaultVal = $("#text-default-value").val().trim();
                var place = $("#text-place").val();
                var isMust = $("#text-must").is(':checked');

                var textWidth = $("#text-width").val() * colWidth;
                var textLabelWidth = $("#text-label-width").val() * colWidth;

                view.find("label").text(label);
                var inputObj = view.find("input");
                inputObj.attr("value", defaultVal);
                inputObj.attr({
                    "id": id,
                    "placeholder": place,
                    "name": name
                });

                view.find(".labelDiv").css("width", textLabelWidth).attr("col", $("#text-label-width").val());
                inputObj.parent().css("width", textWidth - 18).attr("col", $("#text-width").val());
                inputObj.css("width", textWidth - 18).attr("col", $("#text-width").val()).attr({
                    "regx_cue": regxCue
                });

                if (isMust) {
                    var num = view.find(".labelDiv").find("span").length;
                    if (num == 0) {
                        view.find(".labelDiv").prepend("<span style='color:red;float:right;'>*</span>");
                    }
                } else {
                    view.find(".labelDiv").find("span").remove();
                }
                $("#text-warn").modal('hide');
                $("#textModal").modal("hide");
            }
        }
    });

    //保存数字文本框的属性编辑
    $("#save-number-content").click(function (e) {
        e.preventDefault();
        var id = $("#number-id").val();
        var name = $("#number-name").val().trim();
        if (id == "" || id == null || name == null || name == "") {
            $("#number-warn").modal('show');
        } else if (nameIsRepeat(name)) { //判断组件name是否重复
            $("#number-warn").html("<strong>警告！</strong>您输入的name重复，请重新输入");
            $("#number-warn").modal('show');
        } else {
            var label = $("#number-label").val();
            var defaultVal = $("#number-default-value").val();
            var place = $("#number-place").val();
            var isMust = $("#number-must").is(':checked');

            var textWidth = $("#number-width").val() * colWidth;
            var textLabelWidth = $("#number-label-width").val() * colWidth;

            view.find("label").text(label);
            var inputObj = view.find("input");
            inputObj.attr("value", defaultVal);
            inputObj.attr({
                "id": id,
                "placeholder": place,
                "name": name
            });

            view.find(".labelDiv").css("width", textLabelWidth).attr("col", $("#number-label-width").val());
            inputObj.parent().css("width", textWidth - 18).attr("col", $("#number-width").val());
            inputObj.css("width", textWidth - 18).attr("col", $("#number-width").val());

            if (isMust) {
                var num = view.find(".labelDiv").find("span").length;
                if (num == 0) {
                    view.find(".labelDiv").prepend("<span style='color:red;float:right;'>*</span>");
                }
            } else {
                view.find(".labelDiv").find("span").remove();
            }
            $("#number-warn").modal('hide');
            $("#numberModal").modal("hide");
        }
    });

    //保存日期文本框的属性编辑
    $("#save-date-content").click(function (e) {
        e.preventDefault();
        var id = $("#date-id").val();
        var name = $("#date-name").val();
        if (id == "" || id == null || name == null || name == "") {
            $("#date-warn").modal('show');
        } else if (nameIsRepeat(name)) { //判断组件name是否重复
            $("#date-warn").html("<strong>警告！</strong>您输入的name重复，请重新输入");
            $("#date-warn").modal('show');
        } else {
            var label = $("#date-label").val();
            var defaultVal = $("#date-default-value").val();
            var place = $("#date-place").val();
            var isMust = $("#date-must").is(':checked');

            var textWidth = $("#date-width").val() * colWidth;
            var textLabelWidth = $("#date-label-width").val() * colWidth;

            view.find("label").text(label);
            var inputObj = view.find("input");

            inputObj.attr("value", defaultVal);
            inputObj.attr({
                "id": id,
                "placeholder": place,
                "name": name
            });

            view.find(".labelDiv").css("width", textLabelWidth).attr("col", $("#date-label-width").val());
            inputObj.parent().css("width", textWidth - 18).attr("col", $("#date-width").val());
            inputObj.css("width", textWidth - 18 - dateIconWidth).attr("col", $("#date-width").val());

            if (isMust) {
                var num = view.find(".labelDiv").find("span").length;
                if (num == 0) {
                    view.find(".labelDiv").prepend("<span style='color:red;float:right;'>*</span>");
                }
            } else {
                view.find(".labelDiv").find("span").remove();
            }
            $("#date-warn").modal('hide');
            $("#dateModal").modal("hide");
        }
    });

    //保存多行文本框的属性编辑
    $("#save-textarea-content").click(function (e) {
        e.preventDefault();
        var id = $("#textarea-id").val();
        var name = $("#textarea-name").val();
        if (id == "" || id == null || name == null || name == "") {
            $("#textarea-warn").modal('show');
        } else if (nameIsRepeat(name)) { //判断组件name是否重复
            $("#textarea-warn").html("<strong>警告！</strong>您输入的name重复，请重新输入");
            $("#textarea-warn").modal('show');
        } else {
            var label = $("#textarea-label").val();
            var defaultVal = $("#textarea-default-value").val();
            var isMust = $("#textarea-must").is(':checked');
            var rows = $("#textarea-row").val();

            var textWidth = $("#textarea-width").val() * colWidth;
            var textLabelWidth = $("#textarea-label-width").val() * colWidth;

            view.find("label").text(label);
            var textareaObj = view.find("textarea");
            textareaObj.attr("value", defaultVal);
            textareaObj.attr({
                "id": id,
                "rows": rows,
                "name": name
            });

            view.find(".labelDiv").css("width", textLabelWidth).attr("col", $("#textarea-label-width").val());
            textareaObj.parent().css("width", textWidth - 18).attr("col", $("#textarea-width").val());
            textareaObj.css("width", textWidth - 18).attr("col", $("#textarea-width").val());

            if (isMust) {
                var num = view.find(".labelDiv").find("span").length;
                if (num == 0) {
                    view.find(".labelDiv").prepend("<span style='color:red;float:right;'>*</span>");
                }
            } else {
                view.find(".labelDiv").find("span").remove();
            }
            $("#textarea-warn").modal('hide');
            $("#textareaModal").modal("hide");
        }
    });

    //保存下拉选择框的属性编辑
    $("#save-select-content").click(function (e) {
        e.preventDefault();
        var id = $("#select-id").val();
        var name = $("#select-name").val();
        if (id == "" || id == null || name == null || name == "") {
            $("#select-warn").modal('show');
        } else if (nameIsRepeat(name)) { //判断组件name是否重复
            $("#select-warn").html("<strong>警告！</strong>您输入的name重复，请重新输入");
            $("#select-warn").modal('show');
        } else {
            var label = $("#select-label").val();
            var name = $("#select-name").val();
            var isMust = $("#select-must").is(':checked');
            var optionObjArr = $(".option-value"); //下拉列表添加选项的输入框对象

            var dataSource = $("input[name='data_source']:checked").val();

            var textWidth = $("#select-width").val() * colWidth;
            var textLabelWidth = $("#select-label-width").val() * colWidth;

            view.find("label").text(label);
            var selectObj = view.find("select");
            selectObj.attr({
                "id": id,
                "name": name,
                "data_source": dataSource
            });

            if (dataSource == "数据字典拉取") {
                var databaseType = $(".database select").val();
                selectObj.attr("database_type", databaseType);
            }

            view.find(".labelDiv").css("width", textLabelWidth).attr("col", $("#select-label-width").val());
            selectObj.parent().css("width", textWidth - 18).attr("col", $("#select-width").val());
            selectObj.css("width", textWidth - 3.5).attr("col", $("#select-width").val());
            selectObj.children().remove();
            for (var i = 0; i < optionObjArr.length; i++) {
                var optionObj = $(optionObjArr[i]);
                var optionVal = optionObj.val();
                if (optionVal.trim() != null && optionVal.trim() != "") {
                    selectObj.append("<option value=" + optionVal + ">" + optionVal + "</option>");
                }
            }
            if (isMust) {
                var num = view.find(".labelDiv").find("span").length;
                if (num == 0) {
                    view.find(".labelDiv").prepend("<span style='color:red;float:right;'>*</span>");
                }
            } else {
                view.find(".labelDiv").find("span").remove();
            }
            $("#select-warn").modal('hide');
            $("#selectModal").modal("hide");
        }
    });

    //保存单选框的属性编辑
    $("#save-radio-content").click(function (e) {
        e.preventDefault();
        var id = $("#radio-id").val();
        var name = $("#radio-name").val();
        if (id == "" || id == null || name == null || name == "") {
            $("#radio-warn").modal('show');
        } else if (nameIsRepeat(name)) { //判断组件name是否重复
            $("#radio-warn").html("<strong>警告！</strong>您输入的name重复，请重新输入");
            $("#radio-warn").modal('show');
        } else {
            var label = $("#radio-label").val();
            var isMust = $("#radio-must").is(':checked');
            var radioObjArr = $(".radio-value"); //单选框添加选项的输入框对象

            var textWidth = $("#radio-width").val() * colWidth;
            var textLabelWidth = $("#radio-label-width").val() * colWidth;

            view.find(".labelDiv label").text(label);

            var parentDivObj = view.find(".subDiv");
            view.find(".radio").remove();
            for (var i = 0; i < radioObjArr.length; i++) {
                if ($(radioObjArr[i]).val() != "" && $(radioObjArr[i]).val() != null) {
                    var radioObj = $(radioObjArr[i]);
                    var html = "<label class='radio'>" +
                        "<input type='radio' class='" + id + "' id='" + id + i + "' name='" + name + "'/>" +
                        radioObj.val() +
                        "</label>";
                    parentDivObj.append(html);
                }
            }

            view.find(".labelDiv").width(textLabelWidth).attr("col", $("#radio-label-width").val());
            view.find(".subDiv").width(textWidth - 18).attr("col", $("#radio-width").val());

            if (isMust) {
                var num = view.find(".labelDiv").find("span").length;
                if (num == 0) {
                    view.find(".labelDiv").prepend("<span style='color:red;'>*</span>");
                }
            } else {
                view.find(".labelDiv").find("span").remove();
            }
            $("#radio-warn").modal('hide');
            $("#radioModal").modal("hide");
        }
    });

    //保存多选框的属性编辑
    $("#save-checkbox-content").click(function (e) {
        e.preventDefault();
        var id = $("#checkbox-id").val().trim();
        var name = $("#checkbox-name").val();
        if (id == "" || id == null || name == null || name == "") {
            $("#checkbox-warn").modal('show');
        } else if (nameIsRepeat(name)) { //判断组件name是否重复
            $("#checkbox-warn").html("<strong>警告！</strong>您输入的name重复，请重新输入");
            $("#checkbox-warn").modal('show');
        } else {
            var label = $("#checkbox-label").val();
            var isMust = $("#checkbox-must").is(':checked');
            var checkboxObjArr = $(".checkbox-value"); //单选框添加选项的输入框对象
            var id = $("#checkbox-id").val();

            var textWidth = $("#checkbox-width").val() * colWidth;
            var textLabelWidth = $("#checkbox-label-width").val() * colWidth;

            view.find(".labelDiv label").text(label);
            var parentDivObj = view.find(".subDiv");
            view.find(".checkbox").remove();
            for (var i = 0; i < checkboxObjArr.length; i++) {
                var checkboxObj = $(checkboxObjArr[i])
                var html = "";
                if (checkboxObj.val() == "" || checkboxObj.val() == null) {
                    if (i == 0) {
                        if (checkboxObj.next().find("input").is(":checked")) {
                            html += "<label class='checkbox'>" +
                                "<input type='checkbox' class='" + id + "' id='" + id + i + "' name='" + name + "' checked/>checkboxValue" +
                                "</label>";
                        } else {
                            html += "<label class='checkbox'>" +
                                "<input type='checkbox' class='" + id + "' id='" + id + i + "' name='" + name + "'/>checkboxValue" +
                                "</label>";
                        }
                    }
                } else {
                    if (checkboxObj.next().find("input").is(":checked")) {
                        html += "<label class='checkbox'>" +
                            "<input type='checkbox' class='" + id + "' id='" + id + i + "' name='" + name + "' checked/>" +
                            checkboxObj.val() +
                            "</label>";
                    } else {
                        html += "<label class='checkbox'>" +
                            "<input type='checkbox' class='" + id + "' id='" + id + i + "' name='" + name + "'/>" +
                            checkboxObj.val() +
                            "</label>";
                    }
                }
                parentDivObj.append(html);
            }
            view.find(".labelDiv").width(textLabelWidth).attr("col", $("#checkbox-label-width").val());
            view.find(".subDiv").width(textWidth - 18).attr("col", $("#checkbox-width").val());
            if (isMust) {
                var num = view.find(".labelDiv").find("span").length;
                if (num == 0) {
                    view.find(".labelDiv").prepend("<span style='color:red;'>*</span>");
                }
            } else {
                view.find(".labelDiv").find("span").remove();
            }
            $("#checkbox-warn").modal('hide');
            $("#checkboxModal").modal("hide");
        }
    });

    //保存文件上传按钮的属性编辑
    $("#save-loadFile-content").click(function (e) {
        e.preventDefault();
        var id = $("#loadFile-id").val();
        if (id == "" || id == null) {
            $("#loadFile-warn").modal('show');
        } else {
            rowWidth = $(".demo").width() - 5;
            colWidth = rowWidth / 12;
            var label = $("#loadFile-name").val();
            var defaultVal = $("#loadFile-defaultVal").val();
            var isMust = $("#loadFile-must").is(':checked');
            var maxFileSize = $("#loadFile-maxSize").val();
            var maxFileCount = $("#loadFile-maxCount").val();
            view.find(".maxFileSize").val(maxFileSize);
            view.find(".maxFileCount").val(maxFileCount);

            var textWidth = $("#loadFile-width").val() * colWidth;
            var textLabelWidth = $("#loadFile-label-width").val() * colWidth;

            var formatObjArr = $(".format-check:checked");
            var formatStr = "";
            for (var i = 0; i < formatObjArr.length; i++) {
                var formatObjStr = $(formatObjArr[i]).parent().text().trim();
                formatStr += formatObjStr + ",";
            }
            formatStr += $("#loadFile-format").val();
            if (formatStr.substr(formatStr.length - 1, 1) == ",") {
                formatStr = formatStr.substr(0, formatStr.length - 1);
            }
            view.find(".fileFormat").val(formatStr);
            view.find("label").text(label);
            var inputObj = view.find(".subDiv input");
            inputObj.attr("value", defaultVal);
            inputObj.attr({
                "id": id
            });

            view.find(".labelDiv").css("width", textLabelWidth).attr("col", $("#loadFile-label-width").val());
            inputObj.parent().css("width", textWidth - 18).attr("col", $("#loadFile-width").val());

            if (isMust) {
                var num = view.find(".labelDiv").find("span").length;
                if (num == 0) {
                    view.find(".labelDiv").prepend("<span style='color:red;float:right;'>*</span>");
                }
            } else {
                view.find(".labelDiv").find("span").remove();
            }
            $("#loadFile-warn").modal('hide');
            $("#loadFileModal").modal("hide");
        }
    });
    //保存富文本框的属性编辑
    $("#save-editor-content").click(function (e) {
        e.preventDefault();
        var id = $("#editor-id").val();
        if (id == "" || id == null) {
            $("#editor-warn").modal('show');
        } else {
            var textWidth = $("#editor-width").val() * colWidth;
            var textLabelWidth = $("#editor-label-width").val() * colWidth;

            var textareaObj = view.find("textarea");
            textareaObj.attr({
                "id": id
            });

            view.find(".labelDiv").css("width", textLabelWidth).attr("col", $("#editor-label-width").val());
            textareaObj.parent().css("width", textWidth - 18).attr("col", $("#editor-width").val());
            textareaObj.css("width", textWidth - 18).attr("col", $("#editor-width").val());

            $("#editor-warn").modal('hide');
            $("#editorAreaModal").modal("hide");
        }
    });
    //保存文本块的属性编辑
    $("#save-text-block-content").click(function (e) {
        e.preventDefault();
        var textRow = $("#text-block-row").val();
        var textCol = $("#text-block-col").val();

        var pObj = view.find("p");
        pObj.attr({
            "id": id
        });

        pObj.attr({
            "row": textRow,
            "col": textCol
        });
        pObj.parent().attr({
            "row": textRow,
            "col": textCol
        });

        $("#text-block-warn").modal('hide');
        $("#textBlockModal").modal("hide");
    });
    //保存数据表格的属性编辑
    $("#save-dataTable-content").click(function (e) {
        e.preventDefault();
        var id = $("#data-table-id").val().trim();
        var name = $("#data-table-name").val();
        oldName = name;
        var thContentFlag = false;
        $(".data-table-head").each(function () {
            if ($(this).val() == "" || $(this).val() == null) {
                thContentFlag = true;
            }
        });
        if (id == "" || id == null || name == null || name == "" || thContentFlag) {
            $("#data-table-warn").modal('show');
        } else if (nameIsRepeat(name)) { //判断组件name是否重复
            $("#data-table-warn").html("<strong>警告！</strong>您输入的name重复，请重新输入");
            $("#data-table-warn").modal('show');
        } else {
            var tableObj = view.find("table");
            tableObj.attr({
                "id": id,
                "name": name
            });
            var tableThArr = $(".data-table-head");
            tableObj.find("th").remove();
            for (var i = 0; i < tableThArr.length; i++) {
                var tableThObj = $(tableThArr[i]);
                var tableThType = $($(".data-table-type")[i]);
                var thHtml = "<th col-type='" + tableThType.val() + "'>" + tableThObj.val() + "</th>";
                tableObj.find("thead tr").append(thHtml);
            }

            $("#data-table-warn").modal('hide');
            $("#dataTableModal").modal("hide");
        }
    });

    //当demo div改变大小时触发的事件
    $(".demo").resize(function () {
        var souObj = $(".demo");
        var rowWidth = souObj.width() - 5;
        var colWidth = rowWidth / 12;
        $(".demo").css("display", "none");
        $(".row-fluid .span12").css({
            "float": "left",
            "margin-left": "0px",
            "width": colWidth * 12
        });
        $(".row-fluid .span11").css({
            "float": "left",
            "margin-left": "0px",
            "width": colWidth * 11
        });
        $(".row-fluid .span10").css({
            "float": "left",
            "margin-left": "0px",
            "width": colWidth * 10
        });
        $(".row-fluid .span9").css({
            "float": "left",
            "margin-left": "0px",
            "width": colWidth * 9
        });
        $(".row-fluid .span8").css({
            "float": "left",
            "margin-left": "0px",
            "width": colWidth * 8
        });
        $(".row-fluid .span7").css({
            "float": "left",
            "margin-left": "0px",
            "width": colWidth * 7
        });
        $(".row-fluid .span6").css({
            "float": "left",
            "margin-left": "0px",
            "width": colWidth * 6
        });
        $(".row-fluid .span5").css({
            "float": "left",
            "margin-left": "0px",
            "width": colWidth * 5
        });
        $(".row-fluid .span4").css({
            "float": "left",
            "margin-left": "0px",
            "width": colWidth * 4
        });
        $(".row-fluid .span3").css({
            "float": "left",
            "margin-left": "0px",
            "width": colWidth * 3
        });
        $(".row-fluid .span2").css({
            "float": "left",
            "margin-left": "0px",
            "width": colWidth * 2
        });
        $(".row-fluid .span1").css({
            "float": "left",
            "margin-left": "0px",
            "width": colWidth * 1
        });

        var labelColNum = souObj.find(".labelDiv").attr("col");
        souObj.find(".labelDiv").css("width", colWidth * labelColNum);
        for (var i = 0; i < souObj.find(".subDiv").length; i++) {
            var inputDiv = $(souObj.find(".subDiv")[i]);
            var colNum = inputDiv.attr("col");
            inputDiv.css("width", colNum * colWidth - 18);
            if (inputDiv.children().length > 1) {
                if ($(inputDiv.children()[0]).attr("class") == "editor_textarea") {
                    inputDiv.find("div").css("width", colNum * colWidth - 18);
                } else if ($(inputDiv.children()[0]).prop("type") == "button") {
                    continue;
                } else {
                    $(inputDiv.children()[0]).css("width", colNum * colWidth - 18 - dateIconWidth);
                }
            } else {
                if ($(inputDiv.children()[0]).prop("tagName") == "SELECT") {
                    $(inputDiv.children()[0]).css("width", colNum * colWidth - 3);
                } else if ($(inputDiv.children()[0]).prop("type") == "button") {
                    continue;
                } else {
                    $(inputDiv.children()[0]).css("width", colNum * colWidth - 18);
                }
            }
        }
        $(".demo").css("display", "block");
        return false
    });
});

//动态添加下拉框的选项添加框
function addOptionInput(obj) {
    var addFormObj = $(obj).parent().parent();
    var html = "<div class='form-group add-form-obj'>" +
        "<div class='col-xs-7 col-sm-offset-4'>";
    html += "<input type='text' " +
        "class='form-control option-value' " +
        "placeholder='请输入列表选项值'/>";
    html += " <span class='glyphicon glyphicon-minus' onclick='removeOptionInput(this)'" +
        "style='font-size:20px;color:#888;cursor:pointer;'></span>" +
        " <span class='glyphicon glyphicon-plus' onclick='addOptionInput(this)'" +
        "style='font-size:20px;color:#888;cursor:pointer;'></span>";
    html += "</div></div>";
    addFormObj.after(html);
}

//动态删除下拉框的选项添加框
function removeOptionInput(obj) {
    var removeFormObj = $(obj).parent().parent();
    if (removeFormObj.find("label").length == 1) {
        $(obj).parent().find("input").val("");
    } else {
        removeFormObj.remove();
    }
}

//动态添加单选框的选项添加框
function addRadioInput(obj) {
    var addFormObj = $(obj).parent().parent();
    var html = "<div class='form-group add-form-obj'>" +
        "<div class='col-xs-7 col-sm-offset-4'>";
    html += "<input type='text' " +
        "class='form-control radio-value' " +
        "placeholder='请输入单选框选项值'/>";
    html += " <span class='glyphicon glyphicon-minus' onclick='removeRadioInput(this)'" +
        "style='font-size:20px;color:#888;cursor:pointer;'></span>" +
        " <span class='glyphicon glyphicon-plus' onclick='addRadioInput(this)'" +
        "style='font-size:20px;color:#888;cursor:pointer;'></span>";
    html += "</div></div>";
    addFormObj.after(html);
}

//动态删除单选框的选项添加框
function removeRadioInput(obj) {
    var removeFormObj = $(obj).parent().parent();
    if (removeFormObj.find("label").length != 1) {
        removeFormObj.remove();
    }
}

//动态添加复选框的选项添加框
function addCheckboxInput(obj) {
    var addFormObj = $(obj).parent().parent();
    var html = "<div class='form-group add-form-obj'>" +
        "<div class='col-xs-7 col-sm-offset-4'>";
    html += "<input type='text' " +
        "class='col-xs-4 checkbox-value' " +
        "placeholder='请输入复选框选项值'/> " +
        "<div class='col-sm-offset-1 col-xs-1' style='margin-top:4px;'>" +
        "<input type='checkbox' title='勾选让该选项选中'/> " +
        "</div>";
    html += " <span class='glyphicon glyphicon-minus' onclick='removeCheckboxInput(this)'" +
        "style='font-size:20px;color:#888;margin:4px 0 0 7px;cursor:pointer;'></span>" +
        " <span class='glyphicon glyphicon-plus' onclick='addCheckboxInput(this)'" +
        "style='font-size:20px;color:#888;margin:4px 0 0 5px;cursor:pointer;'></span>";
    html += "</div></div>";
    addFormObj.after(html);
}

//动态删除复选框的选项添加框
function removeCheckboxInput(obj) {
    var removeFormObj = $(obj).parent().parent();
    if (removeFormObj.find("label").length != 1) {
        removeFormObj.remove();
    }
}

// 当点击上传文件的按钮时，触发的事件
var btnClick = function (obj) {
    $("#myModal").modal("show");
    fileInput(obj);
    myModal.addEventListener("dragenter", function (e) {
        e.stopPropagation();
        e.preventDefault();
    }, false);
    myModal.addEventListener("dragover", function (e) {
        e.stopPropagation();
        e.preventDefault();
    }, false);
    myModal.addEventListener("drop", function (e) {
        e.stopPropagation();
        e.preventDefault();
    }, false);

    //导入文件上传完成之后的事件
    $("#txt_file").on("fileuploaded",
        function (event, data, previewId, index) {
            var flag = true;
            var aObjArr = $(obj).parent().find("a");
            for (var i = 0; i < aObjArr.length; i++) {
                var aObjStr = $(aObjArr[i]).text();
                if (aObjStr == data.response.filename) {
                    flag = false;
                }
            }
            if (flag) {
                $(obj).parent().append(
                    "<div><a class='excelFile' href='" + common.getPath() + "/resources/file/" + data.response.filename + "'>" +
                    data.response.filename +
                    "</a> " +
                    "<a href='javascript:void(0);' class='deleteFile' onclick='aClick(this)'>删除</a></div>");

            }
            $(".kv-file-remove").trigger("click");
            $("#myModal").modal("hide");
        });
}

//当点击删除文件时触发的事件
var aClick = function (obj) {
    var filename = $(obj).prev().text();
    $.ajax({
        url: common.getPath() + "/formManage/deleteFile",
        method: "post",
        data: {
            "filename": filename
        },
        success: function (response) {
            $(obj).parent().remove();
        }
    });
}

//初始化文件上传框
var fileInput = function (obj) {
    var hiddenObj = $(obj).parent().next();
    var maxFileSize = new Number(hiddenObj.find(".maxFileSize").val());
    var maxFileCount = hiddenObj.find(".maxFileCount").val();
    var fileFormatStr = hiddenObj.find(".fileFormat").val();
    var fileFormatArr = fileFormatStr.split(",");

    $('#txt_file').fileinput({
        language: 'zh', //设置语言
        uploadUrl: common.getPath() + "/formManage/saveFile", //上传的地址
        allowedFileExtensions: fileFormatArr, //接收的文件后缀
        showUpload: true, //是否显示上传按钮
        showCaption: true, //是否显示标题
        browseClass: "btn btn-primary", //按钮样式
        showPreview: true,
        dropZoneEnabled: true,
        showRemove: true,
        showCancel: true,
        slugCallback: function (filename) {
            return filename.replace('(', '_').replace(']', '_');
        },
        //minImageWidth: 50, //图片的最小宽度
        //minImageHeight: 50,//图片的最小高度
        //maxImageWidth: 1000,//图片的最大宽度
        //maxImageHeight: 1000,//图片的最大高度
        maxFileSize: maxFileSize * 1024, //单位为kb，如果为0表示不限制文件大小
        minFileCount: 1,
        maxFileCount: maxFileCount, //表示允许同时上传的最大文件个数
        enctype: 'multipart/form-data',
        validateInitialCount: true,
        msgSizeTooLarge: "文件 {name} (<b>{size} MB</b>) 超过了允许大小 <b>{maxSize} MB</b>！",
        msgFilesTooMany: "选择上传的文件数量({n}) 超过允许的最大数值{m}！",
    });
};