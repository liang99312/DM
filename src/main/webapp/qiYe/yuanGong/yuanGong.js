var yuanGong = {"sz": [], "seq": -1, "yx": 1, "zys": 0};
var ygJueSe = {"sz": [], "seq": -1};
var JueSe = {"sz": [], "seq": -1};
var syJueSe;
var ygBianHao;
var isEditYg;
var lxLuru = 0;
var onlyCheck = true;

$(document).ready(function () {
    $("#tblYuanGong").setListBottom(yuanGong, cxYuanGong);
    $("#tblYuanGong").built();
    $("#dvCx4Yg").popTopBar("查询员工信息");
    $("#dvFrmYg").popTopBar("员工信息");
    $("#dvFrmYgZh").popTopBar("重置员工账号");
    $("#dvFmJs").popTopBar("设置员工角色");
    $("#tblFrmYg_rzrq").setCalendar();
    $("#dvFrmYg").find("input,select").r2t();
    $("#dvCx4Yg").find("input,select").r2t();
    $("#logo").click(function () {
        $(":checkbox").prop('checked', false);
        $("#tblCx4Yg input[type=text]").val("");
        $("#dvCx4Yg,tr.diBian").show();
        $("#board").css("z-index", $("#dvCx4Yg").css("z-index") - 1).show();
    });
    $("#tblCx4Yg_rzsj").setCalendar();
    $("#tblFrmYg_rzsj").setCalendar();
    dm_cxJueSe(jxygJueSe);
    cd4YuanGong();
});

function cd4YuanGong() {
    $("#tblYuanGong").contextMenu("cd4Yg", {
        bindings: {
            "zjYg": function (t) {
                zjYuanGong();
            },
            "xgYg": function (t) {
                xgYuanGong();
            },
            "scYg": function (t) {
                scYuanGong();
            },
            "zdJs": function (t) {
                zdJueSe();
            },
            "czmm": function (t) {
                czZhangHao();
            }
//                        "qrlz":function(t){
//                            qrYglz();
//                        }
        }
    });
}

function cxYuanGong() {
    var j = {};
    j.mc = $("#tblCx4Yg_xm").val();
    j.xb = $("#tblCx4Yg_xb input:radio[name='xingbie']:checked").val();
    j.sfzh = $("#tblCx4Yg_sfzh").val();
    j.sjhm = $("#tblCx4Yg_sjhm").val();
    j.zt = $("#tblCx4Yg_zt").val();
    j.rzsj = $("#tblFrmYg_rzsj").val();
    j.yx = yuanGong.yx;
    $.dfAjax({
        url: "/yg/cxYg.do",
        data: j,
        fun: function (data) {
            jxYuanGong(data);
            if (lxLuru === 0) {
                $("#board").hide();
            }
            $("#dvCx4Yg").hide();
            lxLuru = 0;
        }
    });
}

function jxYuanGong(json) {
    yuanGong.sz = json.sz;
    yuanGong.yx = json.yx;
    yuanGong.zys = json.zys;
    yuanGong.jls = json.jls;
    if (yuanGong.yx === 0)
        yuanGong.yx = 1;
    var data = [];
    for (var i = 0; i < json.sz.length; i++) {
        var e = json.sz[i];
        if (e.zt === 1) {
            e.zt = "在职";
        } else {
            e.zt = "离职";
        }
        var tr = {"td": [e.mc, e.xb, e.sfzh, e.sjhm, e.rzsj, e.zt]};
        data.push(tr);
    }
    $("#tblYuanGong").built({"data": data, "obj": yuanGong, "dbclick": cxYgXinXi}).setPage(json);
}

function zjYuanGong() {
    isEditYg = false;
    ygBianHao = "";
    $("#tblFrmYg input[type=text]").val("").removeAttr("readonly");
    $("#tblFrmYg input[type=password]").val("");
    $("#tblFrmYg_lxsr").parents("label").show();
    $("#tblFrmYg_dlm").parents("tr").show();
    $("#tblFrmYg_miMa").parents("tr").show();
    $("#tblFrmYg_bc").parents("tr").show();
    $("tr.ckXianShi").hide();
    $("#tblFrmYg_hf").hide();
    $("#tblFrmYg_miMa").val("123456");
    $("tr.diBian").show();
    $("#dvFrmYg").css("z-index", "10").show();
    $("#board").css("z-index", $("#dvFrmYg").css("z-index") - 1).show();
    $("#tblFrmYg_xm").focus();
}

function xgYuanGong() {
    isEditYg = true;
    if (yuanGong.sz[yuanGong.seq] === undefined || yuanGong.sz[yuanGong.seq] === null) {
        return alert("请选择员工");
    }
    $("#tblFrmYg input[type='radio']").prop("checked", false);
    $("#tblFrmYg_lxsr").parents("label").hide();
    $(":checkbox").prop('checked', false);
    var yg = yuanGong.sz[yuanGong.seq];
    if(yg.zt === -1 || yg.zt === "离职"){
        $("#tblFrmYg_hf").show();
        $("#tblFrmYg_bc").hide();
    }else{
        $("#tblFrmYg_hf").hide();
        $("#tblFrmYg_bc").show();
    }
    ygBianHao = yg.bh;
    $("#tblFrmYg input[type=text]").val("").removeAttr("readonly");
    $("#tblFrmYg_xm").val(yg.mc);
    $("#tblFrmYg_xb").val(yg.xb);
    yg.xb === "男" ? $("#nan").prop("checked", true) : $("#nv").prop("checked", true);
    $("#tblFrmYg_sfzh").val(yg.sfzh);
    $("#tblFrmYg_sjhm").val(yg.sjhm);
    $("#tblFrmYg_rzsj").val(yg.rzsj);
    $("#tblFrmYg_dlm").parents("tr").hide();
    $("#tblFrmYg_miMa").parents("tr").hide();
    $("tr.ckXianShi").hide();
    $("tr.diBian").show();
    $("#tblFrmYg_bc").parents("tr").show();
    $("#dvFrmYg").css("z-index", "10").show();
    $("#board").css("z-index", $("#dvFrmYg").css("z-index") - 1).show();
    $("#tblFrmYg_xm").focus();
}

function hfYuanGong() {
    if (!confirm("确定恢复员工状态：" + yuanGong.sz[yuanGong.seq].mc + "?"))
        return false;
    var j = {"ygbh": yuanGong.sz[yuanGong.seq].bh};
    $.dfAjax({
        url: "/yg/hfYg.do",
        data: j,
        fun: function (data) {
            if (data.result === 0) {
                alert("恢复成功");
                $("#dvFrmYg").hide();
                $("#board").hide();
                cxYuanGong();
            }
        }
    });
}

function checkYgXinXi() {
    //身份证号的正则表达式：15位时全为数字，18位的时候允许最后一位为：X
    sfre = /^(\d{15}$|^\d{18}$|^\d{17}(\d|X|x))$/;
    if (!sfre.test($("#tblFrmYg_sfzh").val())) {
        alert("身份证号不正确");
        return false;
    }
    if ($("#tblFrmYg_sjhm").val().length !== 11) {
        alert("手机号码不对");
        return false;
        re = /^1\d{10}$/;
        if (!re.test($("#tblFrmYg_sjhm").val())) {
            alert("手机号码不正确");
            return false;
        }
    }
    return true;
}

function scYuanGong() {
    if (yuanGong.sz[yuanGong.seq] === undefined || yuanGong.sz[yuanGong.seq] === null) {
        return alert("请选择员工");
    }
    if (!confirm("确定删除员工：" + yuanGong.sz[yuanGong.seq].mc + "?"))
        return false;
    var j = {"ygbh": yuanGong.sz[yuanGong.seq].bh};
    $.dfAjax({
        url: "/yg/scYg.do",
        data: j,
        fun: function (data) {
            $("#tblCx4Yg input[type=text]").val("");
            $("#tblCx4Yg input[type=radio]").prop("checked", false);
            cxYuanGong();
        }
    });
}

//function qrYglz(){
//    if(yuanGong.sz[yuanGong.seq] === undefined || yuanGong.sz[yuanGong.seq] === null){
//        return alert("请选择员工");
//    }
//    if(!confirm("是否彻底删除员工" + yuanGong.sz[yuanGong.seq].mc + "?")){
//        return false;
//    }
//    var j = {"ygbh":yuanGong.sz[yuanGong.seq].bh};
//    $.dfAjax({
//        url:"/yg/qrYglz.do",
//        data: j,
//        fun: function (data){
//            $("#tblCx4Yg input[type=text]").val("");
//            $("#tblCx4Yg input[type=radio]").prop("checked", false);
//            cxYuanGong();
//        }
//    })
//}

function bcYuanGong() {
    if (checkYgXinXi()) {
        var j = {};
        j.mc = $("#tblFrmYg_xm").val();
        j.xb = $("#tblFrmYg_xb input:radio[name='xingbie']:checked").val();
        j.sfzh = $("#tblFrmYg_sfzh").val();
        j.sjhm = $("#tblFrmYg_sjhm").val();
        j.dlm = $("#tblFrmYg_dlm").val();
        j.dlmm = $("#tblFrmYg_miMa").val();
        if (j.dlmm.length < 6) {
            return alert("请设置初始密码，至少6位");
        }
        j.rzsj = $("#tblFrmYg_rzsj").val();
        var url = "/yg/zjYg.do";
        if (isEditYg) {
            j.ygbh = yuanGong.sz[yuanGong.seq].bh;
            url = "/yg/xgYg.do";
        } else {
            if (j.dlm === "") {
                return alert("请设置登录名！");
            }
        }
        $.dfAjax({
            url: url,
            data: j,
            fun: function (data) {
                if (data.result === 0) {
                    $("#tblCx4Yg input[type=text]").val("");
                    $("#tblCx4Yg input[type=radio]").prop("checked", false);
                    if ($("#tblFrmYg_lxsr").is(":checked")) {
                        lxLuru = 1;
                        alert("保存成功");
                        $("#tblFrmYg input[type=text]").val("");
                        $("#tblFrmYg input[type=password]").val("");
                    } else {
                        $("#dvFrmYg").hide();
                        $("#board").hide();
                        lxLuru = 0;
                    }
                    cxYuanGong();
                }
            }
        });
    }
}

function cxYgXinXi() {
    var j = {"ygbh": yuanGong.sz[yuanGong.seq].bh};
    $.dfAjax({
        url: "/yg/quYgzl.do",
        data: j,
        fun: function (data) {
            jxYgXinXi(data);
        }
    });
}

function jxYgXinXi(data) {
    if (data.js.length > 0) {
        jxYgJueSe4Table(data.js, "tblFrmYg_js", "js", "jsdm", false);
    }
    $("#tblFrmYg_xm").val(data.mc).attr("readonly", "true");
    data.xb === "男" ? $("#nan").prop("checked", true) : $("#nv").prop("checked", true);
    $("#tblFrmYg_sfzh").val(data.sfzh).attr("readonly", "true");
    $("#tblFrmYg_sjhm").val(data.sjhm).attr("readonly", "true");
    $("#tblFrmYg_rzsj").val(data.rzsj).attr("readonly", "true");
    $("#tblFrmYg_dlm").val(data.dlm).attr("readonly", "true");
    $("#tblFrmYg_miMa").parents("tr").hide();
    $("#tblFrmYg_bc").parents("tr").hide();
    $("tr.diBian").hide();
    $("tr.ckXianShi").show();
    $("#dvFrmYg").css("z-index", "10").show();
    $("#board").css("z-index", $("#dvFrmYg").css("z-index") - 1).show();

}
function bcChongZhi() {
    $("#tblFrmYg_xm").val("");
    $("#tblFrmYg_xb").val("");
    $("#tblFrmYg input[type=text]").val("");
    $("#tblFrmYg input[type=password]").val("");
}

function jxYgJueSe4Table(jsSz, tableId, name, dmm, checkFlag) {
    $("#" + tableId).empty();
    var rowMax = parseInt((jsSz.length + 2) / 3);
    if (rowMax <= 0) {
        $("#" + tableId).append("<tr><td></td><td></td><td></td></tr>");
    }
    for (var row = 0; row < rowMax; row++) {
        var rs = "";
        for (var col = 0; col < 3; col++) {
            if ((row * 3 + col) < jsSz.length) {
                if (checkFlag) {
                    rs += "<td><label><input id='js_" + jsSz[row * 3 + col][dmm] + "' type='checkbox'>" + jsSz[row * 3 + col][name] + "</label></td>";
                } else {
                    rs += "<td>" + jsSz[row * 3 + col][name] + "</td>";
                }
            } else {
                rs += "<td></td>";
            }
        }
        $("#" + tableId).append("<tr>" + rs + "</tr>");
    }
    $("#" + tableId + " td").width("33%");
}

function jxygJueSe(json) {
    syJueSe = json.sz;
    jxYgJueSe4Table(syJueSe, "tblJs", "mc", "dm", true);
}

function zdJueSe() {
    if (yuanGong.sz[yuanGong.seq] === undefined || yuanGong.sz[yuanGong.seq] === null) {
        return alert("请选择员工");
    }
    var yg = yuanGong.sz[yuanGong.seq];
    if(yg.zt === -1 || yg.zt === "离职"){
        return alert("已离职员工不允许设置角色！");
    }
    var j = {"ygbh": yg.bh};
    $.dfAjax({
        url: "/yg/quYgzl.do",
        data: j,
        fun: function (data) {
            onlyCheck = false;
            ygBianHao = yg.bh;
            $("#tblJs input[type='checkbox']").prop("checked", false);
            for (var i = 0; i < data.js.length; i++) {
                var js = data.js[i];
                $("#js_" + js.jsdm).prop("checked", true);
            }
            $("#dvFmJs").show();
            $("#tblFmJs_xm").html(yg.mc);
            $("#tblFmJs_sfzh").html(yg.sfzh);
            $("#dvFmJs").css("z-index", "10").show();
            $("#board").css("z-index", $("#dvFmJs").css("z-index") - 1).show();
        }
    });
}

function bcYgJueSe() {
    if (yuanGong.sz[yuanGong.seq] === undefined || yuanGong.sz[yuanGong.seq] === null) {
        return;
    }
    var yxJs = [];
    $("#tblJs input[type='checkbox']").each(function (i) {
        if ($(this).is(":checked")) {
            var e = {};
            e.js = syJueSe[i].mc;
            e.jsdm = syJueSe[i].dm;
            yxJs.push(e);
        }
    });
    var json = {};
    json.ygbh = yuanGong.sz[yuanGong.seq].bh;
    json.js = yxJs;
    $.dfAjax({
        url: "/yg/zdJs.do",
        data: json,
        fun: function (j) {
            if (j.result === 0) {
                $("#dvFmJs").hide();
                $("#board").hide();
            }
        }
    });
}

function czZhangHao() {
    if (yuanGong.sz[yuanGong.seq] === undefined || yuanGong.sz[yuanGong.seq] === null) {
        return alert("请选择员工");
    }
    var yg = yuanGong.sz[yuanGong.seq];
    if(yg.zt === -1 || yg.zt === "离职"){
        return alert("已离职员工不允许重置账号！");
    }
    var j = {"ygbh": yg.bh};
    $.dfAjax({
        url: "/yg/quYgzl.do",
        data: j,
        fun: function (d) {
            if (d.result === 0) {
                $("#tblFrmYgZh_xm").val(d.mc);
                $("#tblFrmYgZh_sfzh").val(d.sfzh);
                $("#tblFrmYgZh_dlm").val(d.dlm);
                $("#dvFrmYgZh").css("z-index", "10").show();
                $("#board").css("z-index", $("#dvFrmYgZh").css("z-index") - 1).show();
            }
        }
    });
}

function bcYuanGongZh() {
    if (yuanGong.sz[yuanGong.seq] === undefined || yuanGong.sz[yuanGong.seq] === null) {
        return;
    }
    var json = {};
    json.ygbh = yuanGong.sz[yuanGong.seq].bh;
    json.dlm = $("#tblFrmYgZh_dlm").val();
    if (json.dlm === "") {
        return alert("请设置登录名");
    }
    json.dlmm = $("#tblFrmYgZh_dlmm").val();
    if (json.dlmm.length < 6) {
        return alert("请设置密码，至少6位");
    }
    $.dfAjax({
        url: "/yg/czZh.do",
        data: json,
        fun: function (j) {
            if (j.result === 0) {
                $("#dvFrmYgZh").hide();
                $("#board").hide();
            }
        }
    });
}