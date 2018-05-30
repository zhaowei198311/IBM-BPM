var webpage = "";
var view = null;
var dynContent = "";
var formatJs = "<script type='text/javascript'>\n\n</script>";

function supportstorage() {
    if (typeof window.localStorage == 'object')
        return true;
    else
        return false;
}

function handleSaveLayout() {
    var e = $(".demo").html();
    if (!stopsave && e != window.demoHtml) {
        stopsave++;
        window.demoHtml = e;
        saveLayout();
        stopsave--;
    }
}

var layouthistory;

function saveLayout() {
    var data = layouthistory;
    if (!data) {
        data = {};
        data.count = 0;
        data.list = [];
    }
    if (data.list.length > data.count) {
        for (i = data.count; i < data.list.length; i++)
            data.list[i] = null;
    }
    data.list[data.count] = window.demoHtml;
    data.count++;
    if (supportstorage()) {
        localStorage.setItem("layoutdata", JSON.stringify(data));
    }
    layouthistory = data;
    //console.log(data);
    /*$.ajax({  
    	type: "POST",  
    	url: "/build/saveLayout",  
    	data: { layout: $('.demo').html() },  
    	success: function(data) {
    		//updateButtonsVisibility();
    	}
    });*/
}

function downloadLayout() {

    $.ajax({
        type: "POST",
        url: "/build/downloadLayout",
        data: {
            layout: $('#download-layout').html()
        },
        success: function (data) {
            window.location.href = '/build/download';
        }
    });
}

function downloadHtmlLayout() {
    $.ajax({
        type: "POST",
        url: "/build/downloadLayout",
        data: {
            layout: $('#download-layout').html()
        },
        success: function (data) {
            window.location.href = '/build/downloadHtml';
        }
    });
}

function undoLayout() {
    var data = layouthistory;
    //console.log(data);
    if (data) {
        if (data.count < 2) return false;
        window.demoHtml = data.list[data.count - 2];
        data.count--;
        $('.demo').html(window.demoHtml);
        if (supportstorage()) {
            localStorage.setItem("layoutdata", JSON.stringify(data));
        }
        return true;
    }
    return false;
    /*$.ajax({  
    	type: "POST",  
    	url: "/build/getPreviousLayout",  
    	data: { },  
    	success: function(data) {
    		undoOperation(data);
    	}
    });*/
}

function redoLayout() {
    var data = layouthistory;
    if (data) {
        if (data.list[data.count]) {
            window.demoHtml = data.list[data.count];
            data.count++;
            $('.demo').html(window.demoHtml);
            if (supportstorage()) {
                localStorage.setItem("layoutdata", JSON.stringify(data));
            }
            return true;
        }
    }
    return false;
    /*
    $.ajax({  
    	type: "POST",  
    	url: "/build/getPreviousLayout",  
    	data: { },  
    	success: function(data) {
    		redoOperation(data);
    	}
    });*/
}

function handleJsIds() {
    handleModalIds();
    handleAccordionIds();
    handleCarouselIds();
    handleTabsIds()
}

function handleAccordionIds() {
    var e = $(".demo #myAccordion");
    var t = randomNumber();
    var n = "accordion-" + t;
    var r;
    e.attr("id", n);
    e.find(".accordion-group").each(function (e, t) {
        r = "accordion-element-" + randomNumber();
        $(t).find(".accordion-toggle").each(function (e, t) {
            $(t).attr("data-parent", "#" + n);
            $(t).attr("href", "#" + r)
        });
        $(t).find(".accordion-body").each(function (e, t) {
            $(t).attr("id", r)
        })
    })
}

function handleCarouselIds() {
    var e = $(".demo #myCarousel");
    var t = randomNumber();
    var n = "carousel-" + t;
    e.attr("id", n);
    e.find(".carousel-indicators li").each(function (e, t) {
        $(t).attr("data-target", "#" + n)
    });
    e.find(".left").attr("href", "#" + n);
    e.find(".right").attr("href", "#" + n)
}

function handleModalIds() {
    var e = $(".demo #myModalLink");
    var t = randomNumber();
    var n = "modal-container-" + t;
    var r = "modal-" + t;
    e.attr("id", r);
    e.attr("href", "#" + n);
    e.next().attr("id", n)
}

function handleTabsIds() {
    var e = $(".demo #myTabs");
    var t = randomNumber();
    var n = "tabs-" + t;
    e.attr("id", n);
    e.find(".tab-pane").each(function (e, t) {
        var n = $(t).attr("id");
        var r = "panel-" + randomNumber();
        $(t).attr("id", r);
        $(t).parent().parent().find("a[href=#" + n + "]").attr("href", "#" + r)
    })
}

function randomNumber() {
    return randomFromInterval(1, 1e6)
}

function randomFromInterval(e, t) {
    return Math.floor(Math.random() * (t - e + 1) + e)
}

function gridSystemGenerator() {
    $(".lyrow .preview input").bind("keyup", function () {
        var e = 0;
        var t = "";
        var n = $(this).val().split(" ", 12);
        $.each(n, function (n, r) {
            e = e + parseInt(r);
            t += '<div class="span' + r + ' column"></div>'
        });
        if (e == 12) {
            $(this).parent().next().children().html(t);
            $(this).parent().prev().show();
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
        } else {
            $(this).parent().prev().hide()
        }
    })
}

function configurationElm(e, t) {
    $(".demo").delegate(".configuration > a", "click", function (e) {
        e.preventDefault();
        var t = $(this).parent().next().next().children();
        $(this).toggleClass("active");
        t.toggleClass($(this).attr("rel"))
    });
    $(".demo").delegate(".configuration .dropdown-menu a", "click", function (e) {
        e.preventDefault();
        var t = $(this).parent().parent();
        var n = t.parent().parent().next().next().children();
        t.find("li").removeClass("active");
        $(this).parent().addClass("active");
        var r = "";
        t.find("a").each(function () {
            r += $(this).attr("rel") + " "
        });
        t.parent().removeClass("open");
        n.removeClass(r);
        n.addClass($(this).attr("rel"))
    })
}

function removeElm() {
    $(".demo").delegate(".remove", "click", function (e) {
        e.preventDefault();
        $(this).parent().remove();
        if (!$(".demo .lyrow").length > 0) {
            clearDemo()
        }
    })
}

function clearDemo() {
    $(".demo").empty();
    layouthistory = null;
    if (supportstorage())
        localStorage.removeItem("layoutdata");
}

function removeMenuClasses() {
    $("#menu-layoutit li button").removeClass("active")
}

function changeStructure(e, t) {
    $("#download-layout ." + e).removeClass(e).addClass(t)
}

function cleanHtml(e) {
    $(e).parent().append($(e).children().html())
}

function downloadLayoutSrc() {
    var e = "";
    dynContent = $(".demo").html();
    $("#download-layout").children().html($(".demo").html());
    var t = $("#download-layout").children();
    t.find(".preview, .configuration, .drag, .remove").remove();
    t.find(".lyrow").addClass("removeClean");
    t.find(".box-element").addClass("removeClean");
    t.find(".lyrow .lyrow .lyrow .lyrow .lyrow .removeClean").each(function () {
        cleanHtml(this)
    });
    t.find(".lyrow .lyrow .lyrow .lyrow .removeClean").each(function () {
        cleanHtml(this)
    });
    t.find(".lyrow .lyrow .lyrow .removeClean").each(function () {
        cleanHtml(this)
    });
    t.find(".lyrow .lyrow .removeClean").each(function () {
        cleanHtml(this)
    });
    t.find(".lyrow .removeClean").each(function () {
        cleanHtml(this)
    });
    t.find(".removeClean").each(function () {
        cleanHtml(this)
    });
    t.find(".removeClean").remove();
    t.find(".add-on").remove();
    $("#download-layout .column").removeClass("ui-sortable");
    $("#download-layout .column").removeClass("clearfix").children().removeClass("column");
    if ($("#download-layout .container").length > 0) {
        changeStructure("column", "row")
    }
    formatSrc = $.htmlClean($("#download-layout").html(), {
        format: true,
        allowedAttributes: [
            ["id"],
            ["class"],
            ["data-toggle"],
            ["data-target"],
            ["data-parent"],
            ["role"],
            ["data-dismiss"],
            ["aria-labelledby"],
            ["aria-hidden"],
            ["data-slide-to"],
            ["data-slide"],
            ["col"],
            ["row"],
            ["rows"],
            ["col-type"],
            ["regx"],
            ["regx_cue"],
            ["data_source"],
            ["database_type"],
            ["onclick"],
            ["onchange"],
            ["onfocus"],
            ["onkeypress"],
            ["value"]
        ]
    });
    $("#download-layout").html(formatSrc);
    $("#downloadModal textarea").empty();
    $("#downloadModal textarea").val(formatSrc + "\n" + formatJs);
    webpage = formatSrc + "\n" + formatJs;
}

function saveJS() {
    formatJs = $("#addJSModal textarea").val();
}

var currentDocument = null;
var timerSave = 1000;
var stopsave = 0;
var startdrag = 0;
var demoHtml = $(".demo").html();
var currenteditor = null;
$(window).resize(function () {
    $("body").css("min-height", $(window).height() - 90);
    $(".demo").css("min-height", $(window).height() - 160)
});

function restoreData() {
    if (supportstorage()) {
        layouthistory = JSON.parse(localStorage.getItem("layoutdata"));
        if (!layouthistory) return false;
        window.demoHtml = layouthistory.list[layouthistory.count - 1];
        if (window.demoHtml) $(".demo").html(window.demoHtml);
    }
}

function initContainer() {
    $(".demo").sortable({
        connectWith: ".demo",
        opacity: .35,
        handle: ".drag",
        start: function (e, t) {
            if (!startdrag) stopsave++;
            startdrag = 1;
        },
        stop: function (e, t) {
            if (stopsave > 0) stopsave--;
            startdrag = 0;
        }
    });

    $(".demo .column").sortable({
        connectWith: ".column",
        opacity: .35,
        handle: ".drag",
        start: function (e, t) {
            if (!startdrag) stopsave++;
            startdrag = 1;
        },
        stop: function (e, t) {
            if (stopsave > 0) stopsave--;
            startdrag = 0;
        }
    });
    configurationElm();
}

var id = _getRandomString(6);

function _getRandomString(len) {
    len = len || 32;
    var $chars = 'ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678'; // 默认去掉了容易混淆的字符oOLl,9gq,Vv,Uu,I1  
    var maxPos = $chars.length;
    var pwd = '';
    for (i = 0; i < len; i++) {
        pwd += $chars.charAt(Math.floor(Math.random() * maxPos));
    }
    return pwd;
}

$(document).ready(function () {
    CKEDITOR.disableAutoInline = true;
    restoreData();
    var contenthandle = CKEDITOR.replace('contenteditor', {
        language: 'en',
        contentsCss: ['../resources/desmartbpm/formDesign/css/bootstrap-combined.min.css'],
        allowedContent: true
    });
    $("body").css("min-height", $(window).height() - 50);
    $(".demo").css("min-height", $(window).height() - 130);

    var formUid = $("#formUid").val();
    var dynHtml = $("#dynHtml").html();
    $("#dynHtml").html("");
    if (dynHtml != null && dynHtml != "") {
        dynHtml = dynHtml.replace(/&lt;/g, "<").replace(/&gt;/g, ">")
            .replace(/&amp;lc;/g, "(").replace(/&amp;gc;/g, ")");
        var jsIndex = dynHtml.indexOf("<script type='text/javascript'>");
        var jsIndex2 = dynHtml.indexOf("<script type=\"text/javascript\">");
        var demoHtml = "";
        if (jsIndex != -1) {
            formatJs = dynHtml.substr(jsIndex);
            demoHtml = dynHtml.substring(0, jsIndex);
        } else if (jsIndex2 != -1) {
            formatJs = dynHtml.substr(jsIndex2);
            demoHtml = dynHtml.substring(0, jsIndex2);
        } else {
            demoHtml = dynHtml;
        }
        $(".demo").html(demoHtml);
        var nameVal = "";
        $(".demo .subDiv").each(function () {
            var name = $($(this).children()[0]).attr("name");
            if (name != undefined) {
                nameVal += name + ",";
            }
        });
        var charStr = nameVal.substring(nameVal.length - 1, nameVal.length);
        if (charStr == ",") {
            nameVal = nameVal.substring(0, nameVal.length - 1);
        }
        $("#nameArr").val(nameVal);
    } else {
        if (formUid != null && formUid != "") {
            $.ajax({
                url: common.getPath() + "/formManage/queryFormByFormUid",
                method: "post",
                async: false,
                data: {
                    formUid: formUid
                },
                success: function (result) {
                    if (result.status == 0) {
                        $("#proUid").val(result.data.proUid);
                        $("#proVersion").val(result.data.proVersion);
                        var jsIndex = result.data.dynContent.indexOf("<script type='text/javascript'>");
                        var jsIndex2 = result.data.dynContent.indexOf("<script type=\"text/javascript\">");
                        var demoHtml = "";
                        if (jsIndex != -1) {
                            formatJs = result.data.dynContent.substr(jsIndex);
                            demoHtml = result.data.dynContent.substring(0, jsIndex);
                        } else if (jsIndex2 != -1) {
                            formatJs = result.data.dynContent.substr(jsIndex2);
                            demoHtml = result.data.dynContent.substring(0, jsIndex2);
                        } else {
                            demoHtml = result.data.dynContent;
                        }
                        $(".demo").html(demoHtml);
                        var nameVal = "";
                        $(".demo .subDiv").each(function () {
                            var name = $($(this).children()[0]).attr("name");
                            if (name != undefined) {
                                nameVal += name + ",";
                            }
                        });
                        var charStr = nameVal.substring(nameVal.length - 1, nameVal.length);
                        if (charStr == ",") {
                            nameVal = nameVal.substring(0, nameVal.length - 1);
                        }
                        $("#nameArr").val(nameVal);
                    }
                }
            });
        } else {
            clearDemo();
        }
    }

    $(".sidebar-nav .lyrow").draggable({
        connectToSortable: ".demo",
        helper: "clone",
        handle: ".drag",
        start: function (e, t) {
            if (!startdrag) stopsave++;
            startdrag = 1;
        },
        drag: function (e, t) {
            t.helper.width(400)
        },
        stop: function (e, t) {
            $(".demo .column").sortable({
                opacity: .35,
                connectWith: ".column",
                start: function (e, t) {
                    if (!startdrag) stopsave++;
                    startdrag = 1;
                },
                stop: function (e, t) {
                    if (stopsave > 0) stopsave--;
                    startdrag = 0;
                }
            });
            if (stopsave > 0) stopsave--;
            startdrag = 0;
        }
    });

    var temp = "";
    $(".sidebar-nav .box").draggable({
        connectToSortable: ".column",
        helper: "clone",
        handle: ".drag",
        start: function (e, t) {
            temp = id;
            $(".sidebar-nav .box").find(".edit-attr").attr("id", id);
            if (!startdrag) stopsave++;
            startdrag = 1;
        },
        drag: function (e, t) {
            t.helper.width(400)
        },
        stop: function () {
            id = _getRandomString(6);
            $(".sidebar-nav .box").find("#" + temp).attr("id", id);
            handleJsIds();
            if (stopsave > 0) stopsave--;
            startdrag = 0;
            var inputId = _getRandomString(4);

            if ($("#" + temp).parent().parent().find(".subDiv").find("input").length > 0) {
                switch ($("#" + temp).parent().parent().find(".subDiv").find("input").attr("type")) {
                    case "text":
                        {
                            inputId = "text_" + inputId;
                            break;
                        };
                    case "tel":
                        {
                            inputId = "number_" + inputId;
                            break;
                        };
                    case "date":
                        {
                            inputId = "date_" + inputId;
                            break;
                        };
                    case "button":
                        {
                            inputId = "button_" + inputId;
                            break;
                        };
                }
            } else if ($("#" + temp).parent().parent().find(".subDiv").find("select").length > 0) {
                inputId = "select_" + inputId;
            } else if ($("#" + temp).parent().parent().find(".subDiv").find("textarea").length > 0) {
                if ($("#" + inputId).attr("class") == "editor_textarea") {
                    inputId = "editor_" + inputId;
                } else {
                    inputId = "textarea_" + inputId;
                }
            }

            if ($("#" + temp).parent().parent().find(".subDiv").find("input[type='radio']").length > 0 ||
                $("#" + temp).parent().parent().find(".subDiv").find("input[type='checkbox']").length > 0) {
                $("#" + temp).parent().parent().find(".subDiv").find("input").attr("class", inputId).attr("name", inputId);
            } else {
                $($("#" + temp).parent().parent().find(".subDiv").children()[0]).attr("id", inputId).attr("name", inputId);
            }
            $("#" + temp).trigger("click");
        }
    });
    initContainer();
    $('body.edit .demo').on("click", "[data-target=#editorModal]", function (e) {
        e.preventDefault();
        currenteditor = $(this).parent().parent().find('.view');
        var eText = currenteditor.html();
        contenthandle.setData(eText);
    });
    $("#savecontent").click(function (e) {
        e.preventDefault();
        currenteditor.html(contenthandle.getData());
    });
    $("[data-target=#downloadModal]").click(function (e) {
        e.preventDefault();
        downloadLayoutSrc();
    });
    $("[data-target=#addJSModal]").click(function (e) {
        e.preventDefault();
        $("#addJSModal textarea").empty();
        $("#addJSModal textarea").val(formatJs);
    });
    $("[data-target=#shareModal]").click(function (e) {
        e.preventDefault();
        handleSaveLayout();
    });
    $("#download").click(function () {
        downloadLayout();
        return false
    });
    $("#downloadhtml").click(function () {
        downloadHtmlLayout();
        return false
    });
    $("#edit").click(function () {
        $("body").removeClass("devpreview sourcepreview");
        $("body").addClass("edit");
        removeMenuClasses();
        $(this).addClass("active");
        return false
    });
    $("#clear").click(function (e) {
        e.preventDefault();
        clearDemo()
    });
    $("#devpreview").click(function () {
        $("body").removeClass("edit sourcepreview");
        $("body").addClass("devpreview");
        removeMenuClasses();
        $(this).addClass("active");
        return false
    });
    $("#sourcepreview").click(function () {
        layer.load(1);
        downloadLayoutSrc();
        dynContent += formatJs;
        var dynHtml = dynContent.replace(/\"/g, "\"").replace(/</g, "&lt;").replace(/>/g, "&gt;");
        var preHtml = webpage.replace(/\"/g, "\"");
        var url = common.getPath() + "/formManage/preIndex";
        var preParam = {
            proUid: $("#proUid").val(),
            proVersion: $("#proVersion").val(),
            forName: $("#formName").val(),
            formDescription: $("#formDescription").val(),
            formUid: $("#formUid").val(),
            webpage: preHtml,
            dynHtml: dynHtml
        };
        post(url, preParam);
        return false;
    });
    $("#fluidPage").click(function (e) {
        e.preventDefault();
        changeStructure("container", "container-fluid");
        $("#fixedPage").removeClass("active");
        $(this).addClass("active");
        downloadLayoutSrc()
    });
    $("#fixedPage").click(function (e) {
        e.preventDefault();
        changeStructure("container-fluid", "container");
        $("#fluidPage").removeClass("active");
        $(this).addClass("active");
        downloadLayoutSrc()
    });
    $(".nav-header").click(function () {
        $(".sidebar-nav .boxes, .sidebar-nav .rows").hide();
        $(this).next().slideDown()
    });
    $('#undo').click(function () {
        stopsave++;
        if (undoLayout()) initContainer();
        stopsave--;
    });
    $('#redo').click(function () {
        stopsave++;
        if (redoLayout()) initContainer();
        stopsave--;
    });
    removeElm();
    gridSystemGenerator();
    setInterval(function () {
        handleSaveLayout()
    }, timerSave)
});

function returnFormManage() {
    layer.confirm('你编辑的内容可能还未保存，是否确定返回？', {
        btn: ['确定', '取消'] //按钮
    }, function () {
        window.location.href = common.getPath() +
            "/formManage/index?proUid=" + $("#proUid").val() +
            "&proVersion=" + $("#proVersion").val();
    });
}

function post(URL, PARAMS) {
    var temp_form = document.createElement("form");
    temp_form.action = URL;
    // temp_form .target = "_blank"; 如需新打开窗口 form 的target属性要设置为'_blank'
    temp_form.method = "post";
    temp_form.style.display = "none";
    for (var x in PARAMS) {
        var opt = document.createElement("textarea");
        opt.name = x;
        opt.value = PARAMS[x];
        temp_form.appendChild(opt);
    }
    document.body.appendChild(temp_form);
    temp_form.submit();
    temp_form.remove();
}

function saveHtml() {
    var formUidArr = new Array();
    var formId = $("#downloadModal #formUid").val();
    formUidArr.push(formId);
    layer.load(1);
    //先判断该表单是否可修改
    $.ajax({
        url: common.getPath() + "/formManage/isBindStep",
        method: "post",
        data: {
            "formUids": formUidArr
        },
        traditional: true,
        success: function (result) {
            if (result.status == 0) { //未绑定
                var filename = $("#formName").val() + ".html";
                webpage = $("#downloadModal textarea").val();
                dynContent += formatJs;
                var subDivArr = $("#download-layout").find(".subDiv");
                var jsonArr = new Array();
                //修改表单
                var formUid = $("#formUid").val();
                if (formUid != null && formUid != "") {
                    $.ajax({
                        url: common.getPath() + "/formManage/saveFormFile",
                        method: "post",
                        data: {
                            "webpage": webpage,
                            "filename": filename
                        },
                        success: function (result1) {
                            var formParam = {
                                dynUid: formUid,
                                proUid: $("#proUid").val(),
                                proVersion: $("#proVersion").val(),
                                dynTitle: $("#formName").val(),
                                dynDescription: $("#formDescription").val(),
                                dynFilename: result1,
                                dynContent: dynContent
                            };
                            $.ajax({
                                url: common.getPath() + "/formManage/upadteFormContent",
                                method: "post",
                                traditional: true, //传递数组给后台
                                contentType: "application/json",
                                data: JSON.stringify(formParam),
                                success: function (result) {
                                    if (result.status == 0) {
                                        for (var i = 0; i < subDivArr.length; i++) {
                                            var subDivObj = $(subDivArr[i]);
                                            var subObj = $($(subDivArr[i]).children()[0]);
                                            var filedAttr = {
                                                fldIndex: i, //索引
                                                formUid: formUid, //表单Id
                                                fldCodeName: "", //字段编码Id
                                                fldName: "", //字段名
                                                fldDescription: "", //字段描述
                                                fldType: "", //字段类型
                                                fldSize: "", //字段长度
                                                multiSeparator: "", //多值分隔符
                                                multiValue: "" //是否多值
                                            };
                                            switch (subObj.prop("tagName")) {
                                                case "INPUT":
                                                    {
                                                        filedAttr.fldCodeName = subObj.attr("name");
                                                        filedAttr.fldName = subDivObj.prev().find("label").text();
                                                        filedAttr.multiValue = "false";
                                                        switch (subObj.attr("type")) {
                                                            case "text":
                                                                {
                                                                    filedAttr.fldType = "string";
                                                                    break;
                                                                };
                                                            case "tel":
                                                                {
                                                                    filedAttr.fldType = "number";
                                                                    break;
                                                                };
                                                            case "date":
                                                                {
                                                                    filedAttr.fldType = "date";
                                                                    break;
                                                                };
                                                            case "button":
                                                                {
                                                                    filedAttr.fldType = "file";
                                                                    break;
                                                                };
                                                        }
                                                        jsonArr.push(filedAttr);
                                                        break;
                                                    };
                                                case "TEXTAREA":
                                                    { //文本域，富文本编辑器
                                                        filedAttr.fldCodeName = subObj.attr("name");
                                                        filedAttr.fldName = subDivObj.prev().find("label").text();
                                                        filedAttr.multiValue = "false";
                                                        filedAttr.fldType = "string";
                                                        jsonArr.push(filedAttr);
                                                        break;
                                                    };
                                                case "SELECT":
                                                    {
                                                        filedAttr.fldCodeName = subObj.attr("name");
                                                        filedAttr.fldName = subDivObj.prev().find("label").text();
                                                        filedAttr.multiValue = "false";
                                                        filedAttr.fldType = "string";
                                                        jsonArr.push(filedAttr);
                                                        break;
                                                    };
                                                case "LABEL":
                                                    { //多选框、单选框
                                                        filedAttr.fldCodeName = subObj.find("input").attr("name");
                                                        filedAttr.fldName = subDivObj.prev().find("label").text();
                                                        filedAttr.multiValue = "true";
                                                        filedAttr.multiSeparator = ",";
                                                        filedAttr.fldType = "string";
                                                        jsonArr.push(filedAttr);
                                                        break;
                                                    };
                                            }
                                        }
                                        if (jsonArr != null && jsonArr != "") {
                                            $.ajax({ //添加表单字段数据
                                                url: common.getPath() + "/formField/saveFormField",
                                                method: "post",
                                                dataType: "json",
                                                contentType: "application/json",
                                                data: JSON.stringify(jsonArr),
                                                success: function (result2) {
                                                    if (result2.status == 0) {
                                                        clearDemo();
                                                        window.location.href = common.getPath() +
                                                            "/formManage/index?proUid=" + $("#proUid").val() +
                                                            "&proVersion=" + $("#proVersion").val();
                                                    } else {
                                                        layer.alert("添加失败");
                                                    }
                                                }
                                            });
                                        } else {
                                            window.location.href = common.getPath() +
                                                "/formManage/index?proUid=" + $("#proUid").val() +
                                                "&proVersion=" + $("#proVersion").val();
                                        }
                                    }
                                }
                            });
                        }
                    });
                } else {
                    $.ajax({
                        url: common.getPath() + "/formManage/saveFormFile",
                        method: "post",
                        data: {
                            "webpage": webpage,
                            "filename": filename
                        },
                        success: function (result) {
                            var formParam = {
                                proUid: $("#proUid").val(),
                                proVersion: $("#proVersion").val(),
                                dynTitle: $("#formName").val(),
                                dynDescription: $("#formDescription").val(),
                                dynFilename: result,
                                dynContent: dynContent
                            };
                            $.ajax({ //添加表单数据
                                url: common.getPath() + "/formManage/saveForm",
                                method: "post",
                                dataType: "json",
                                contentType: "application/json",
                                data: JSON.stringify(formParam),
                                success: function (result2) {
                                    if (result2.status == 0) {
                                        for (var i = 0; i < subDivArr.length; i++) {
                                            var subDivObj = $(subDivArr[i]);
                                            var subObj = $($(subDivArr[i]).children()[0]);
                                            var filedAttr = {
                                                fldIndex: i, //索引
                                                formUid: result2.data, //表单Id
                                                fldCodeName: "", //字段编码Id
                                                fldName: "", //字段名
                                                fldDescription: "", //字段描述
                                                fldType: "", //字段类型
                                                fldSize: "", //字段长度
                                                multiSeparator: "", //多值分隔符
                                                multiValue: "" //是否多值
                                            };
                                            switch (subObj.prop("tagName")) {
                                                case "INPUT":
                                                    {
                                                        filedAttr.fldCodeName = subObj.attr("name");
                                                        filedAttr.fldName = subDivObj.prev().find("label").text();
                                                        filedAttr.multiValue = "false";
                                                        switch (subObj.attr("type")) {
                                                            case "text":
                                                                {
                                                                    filedAttr.fldType = "string";
                                                                    break;
                                                                };
                                                            case "tel":
                                                                {
                                                                    filedAttr.fldType = "number";
                                                                    break;
                                                                };
                                                            case "date":
                                                                {
                                                                    filedAttr.fldType = "date";
                                                                    break;
                                                                };
                                                            case "button":
                                                                {
                                                                    filedAttr.fldType = "file";
                                                                    break;
                                                                };
                                                        }
                                                        jsonArr.push(filedAttr);
                                                        break;
                                                    };
                                                case "TEXTAREA":
                                                    { //文本域，富文本编辑器
                                                        filedAttr.fldCodeName = subObj.attr("name");
                                                        filedAttr.fldName = subDivObj.prev().find("label").text();
                                                        filedAttr.multiValue = "false";
                                                        filedAttr.fldType = "string";
                                                        jsonArr.push(filedAttr);
                                                        break;
                                                    };
                                                case "SELECT":
                                                    {
                                                        filedAttr.fldCodeName = subObj.attr("name");
                                                        filedAttr.fldName = subDivObj.prev().find("label").text();
                                                        filedAttr.multiValue = "false";
                                                        filedAttr.fldType = "string";
                                                        jsonArr.push(filedAttr);
                                                        break;
                                                    };
                                                case "LABEL":
                                                    { //多选框、单选框
                                                        filedAttr.fldCodeName = subObj.find("input").attr("name");
                                                        filedAttr.fldName = subDivObj.prev().find("label").text();
                                                        filedAttr.multiValue = "true";
                                                        filedAttr.multiSeparator = ",";
                                                        filedAttr.fldType = "string";
                                                        jsonArr.push(filedAttr);
                                                        break;
                                                    };
                                            }
                                        }
                                        if (jsonArr != null && jsonArr != "") {
                                            $.ajax({ //添加表单字段数据
                                                url: common.getPath() + "/formField/saveFormField",
                                                method: "post",
                                                dataType: "json",
                                                contentType: "application/json",
                                                data: JSON.stringify(jsonArr),
                                                success: function (result) {
                                                    if (result2.status == 0) {
                                                        clearDemo();
                                                        window.location.href = common.getPath() +
                                                            "/formManage/index?proUid=" + $("#proUid").val() +
                                                            "&proVersion=" + $("#proVersion").val();
                                                    } else {
                                                        layer.alert("添加失败");
                                                    }
                                                }
                                            });
                                        } else {
                                            window.location.href = common.getPath() +
                                                "/formManage/index?proUid=" + $("#proUid").val() +
                                                "&proVersion=" + $("#proVersion").val();
                                        }
                                    } else {
                                        layer.alert("添加失败");
                                    }
                                }
                            });
                        }
                    });
                }
            } else {
                layer.alert("该表单已被步骤绑定");
            }
            layer.closeAll("loading");
        }
    });
}