var mkList = [];
$(document).ready(function () {
    $("dd").hide();
    $("dt").hide();
    $("li").hide();
    wdQuanXian();
    $.each($("dt"), function () {
        $(this).click(function () {
            $("#dt1 dd").not($(this).next()).slideUp();
            $(this).next().slideToggle();
            $("dt.selected").not($(this)).removeClass("selected");
            $(this).toggleClass("selected");
        });
    });

    $.each($("li"), function () {
        $(this).click(function () {
            $("li").removeClass("selected");
            $(this).addClass("selected");
        });
    });
});

function wdQuanXian() {
    var json = {};
    json = {"jsonObj": JSON.stringify(json)};
    var s = $.ajax({
        url: "/yg/getQx.do",
        data: json,
        dataType: "json",
        type: "post",
        cache: false,
        success: function (json) {
            saoMiao(json.qx);
        }
    });
}

function saoMiao(ja) {
    $("dt").each(function () {
        var id = parseInt($(this).attr("id") / 100000);
        for (var i = 0; i < ja.length; i++) {
            var j = parseInt(ja[i] / 100000);
            if (j === id) {
                $(this).show();
                break;
            }
        }
        $(".xs").show();
    });
    $("li").each(function () {
        var id = parseInt($(this).attr("id") / 1000);
        for (var i = 0; i < ja.length; i++) {
            var j = parseInt(ja[i] / 1000);
            if (j === id) {
                $(this).show();
                break;
            }
        }
        $(".xs").show();
    });
}

function gzq(title, url, id) {
    if (id) {
        parent.wz.addDiv(title, url, id);
    }
}

function exit() {
    if (!confirm("您确定要退出系统吗??"))
        return;
    window.top.location.href = '/logout.do';
    window.SysExit();
}