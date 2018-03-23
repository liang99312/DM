var wjfl = {"sz": [], "seq": -1, "yx": 1, "zys": 0};
var quanXian = {"sz": [], "seq": -1};
var scQuanXian = [];
var isEditwjfl;
var bmXx = {};
var jsXx = {};
var czSz = ["cz","xz","zj","xg","zl","sh","ql"];
var curWjfl = {};

$(document).ready(function () {
    $("#tblwjfl").setListBottom(wjfl, cxWenJianFenLei);
    $("#tblwjfl").built();
    $("#dvCx4wjfl").popTopBar("查询分类信息");
    $("#dvFrmQx").popTopBar("权限设置");
    $("#dvFrmwjfl").popTopBar("分类信息");
    $("#dvWenJianFenLei").popTopBar("权限操作");
    $("#dvFrmwjfl").find("input,select").r2t();
    $("#dvCx4wjfl").find("input,select").r2t();
    $("#logo").click(function () {
        $("#tblCx4wjfl input[type=text]").val("");
        $("#dvCx4wjfl").show();
        $("#board").css("z-index", $("#dvCx4wjfl").css("z-index") - 1).show();
        $("#tblCx4wjfl_mc").focus();
    });
    cd4WenJianFenLei();
    qxfp2WenJianFenLei();
    dm_cxBuMen(szBuMenXx);
    dm_cxJueSe(szWenJianFenLeiJs);
});

function szBuMenXx(sz) {
    $("#tblWenJianFenLei_bm").inputKit({"src": sz, "obj": 'bmXx'});
}

function szWenJianFenLeiJs(sz) {
    $("#tblWenJianFenLei_js").inputKit({"src": sz, "obj": 'jsXx'});
}

function cd4WenJianFenLei() {
    $("#tblwjfl").contextMenu("cd4wjfl", {
        bindings: {
            "zjwjfl": function (t) {
                zjWenJianFenLei();
            },
            "xgwjfl": function (t) {
                xgWenJianFenLei();
            },
            "scwjfl": function (t) {
                scWenJianFenLei();
            },
            "sqwjfl": function (t) {
                sqWenJianFenLei();
            }
        }
    });
}

function qxfp2WenJianFenLei() {
    $("#tblqxfp").contextMenu("cd4fpQx", {
        bindings: {
            "zjQx": function (t) {
                zjWjflQx();
            },
            "xgQx": function (t) {
                xgWjflQx();
            },
            "scQx": function (t) {
                scWjflQx();
            }

        }
    });
}
function cxWenJianFenLei() {
    var j = {};
    j.mc = $("#tblCx4wjfl_mc").val();
    j.zt = $("#tblCx4wjfl_zt").val();
    j.yx = wjfl.yx;
    $.dfAjax({
        url: "/wjfl/cxWjfl.do",
        data: j,
        fun: function (data) {
            jxWenJianFenLei(data);
            $("#dvCx4wjfl,#board").hide();
        }
    });
}

function jxWenJianFenLei(json) {
    wjfl.sz = json.sz;
    wjfl.yx = json.yx;
    wjfl.zys = json.zys;
    wjfl.jls = json.jls;
    if (wjfl.yx === 0)
        wjfl.yx = 1;
    var data = [];
    for (var i = 0; i < json.sz.length; i++) {
        var e = json.sz[i];
        if (e.jb === null) {
            e.jb = "";
        }
        if (e.zt === 1) {
            e.zt = "有效";
        } else if (e.zt === -1) {
            e.zt = "无效";
        }
        var tr = {"td": [e.mc, e.dm, e.jb, e.bz, e.zt]};
        data.push(tr);
    }
    $("#tblwjfl").built({"data": data, "obj": wjfl, "dbclick": xgWenJianFenLei}).setPage(json);
    $("#tblCx4wjfl input[type=text]").val("");
}

function zjWenJianFenLei() {
    isEditwjfl = false;
    $("#tblFrmwjfl_bc").show();
    $("#tblFrmwjfl input[type=text]").val("").removeAttr("readonly");
    $("#dvFrmwjfl").css("z-index", "10").show();
    $("#board").css("z-index", $("#dvFrmwjfl").css("z-index") - 1).show();
    $("#tblFrmwjfl_mc").focus();
}

function xgWenJianFenLei() {
    isEditwjfl = true;
    if (wjfl.sz[wjfl.seq] === undefined || wjfl.sz[wjfl.seq] === null) {
        return alert("请选择分类");
    }
    var wj = wjfl.sz[wjfl.seq];
    if(wj.zt === -1 || wj.zt === "无效"){
        $("#tblFrmwjfl_bc").hide();
    }else{
        $("#tblFrmwjfl_bc").show();
    }
    $("#tblFrmwjfl input[type=text]").val("").removeAttr("readonly");
    $("#tblFrmwjfl_mc").val(wj.mc);
    $("#tblFrmwjfl_dm").val(wj.dm);
    $("#tblFrmwjfl_jb").val(wj.jb);
    $("#tblFrmwjfl_bz").val(wj.bz);
    $("#dvFrmwjfl").css("z-index", "10").show();
    $("#board").css("z-index", $("#dvFrmwjfl").css("z-index") - 1).show();
    $("#tblFrmwjfl_mc").focus();
}

function check4wjflmc() {
    if ($("#tblFrmwjfl_mc").val() === "") {
        alert("请输档案分类名称");
        return false;
    }
    return true;
}

function scWenJianFenLei() {
    if (wjfl.sz[wjfl.seq] === undefined || wjfl.sz[wjfl.seq] === null) {
        return alert("请选择档案分类");
    }
    if (!confirm("是否删除档案分类：" + wjfl.sz[wjfl.seq].mc + "?"))
        return false;
    var j = {};
    j.bh = wjfl.sz[wjfl.seq].bh;
    $.dfAjax({
        url: "/wjfl/scWjfl.do",
        data: j,
        fun: function (data) {
            cxWenJianFenLei();
            $("#board").hide();
        }
    });
}

function bcWenJianFenLei() {
    if (check4wjflmc()) {
        var j = {};
        var url = "/wjfl/zjWjfl.do";
        j.mc = $("#tblFrmwjfl_mc").val();
        j.dm = $("#tblFrmwjfl_dm").val();
        j.jb = $("#tblFrmwjfl_jb").val();
        j.bz = $("#tblFrmwjfl_bz").val();
        if (isEditwjfl) {
            j.bh = wjfl.sz[wjfl.seq].bh;
            url = "/wjfl/xgWjfl.do";
        }
        $.dfAjax({
            url: url,
            data: j,
            fun: function (data) {
                cxWenJianFenLei();
                $("#dvFrmwjfl").hide();
                $("#board").hide();
            }
        });
    }
}

function sqWenJianFenLei() {
    isEditwjfl = true;
    if (wjfl.sz[wjfl.seq] === undefined || wjfl.sz[wjfl.seq] === null) {
        return alert("请选择分类");
    }
    var wj = wjfl.sz[wjfl.seq];
    if(wj.zt === -1 || wj.zt === "无效"){
        return alert("无效分类不允许授权！");
    }
    var bh = wjfl.sz[wjfl.seq].bh;
    $("#sqFrmwjfl_mc").val(wj.mc);
    $("#sqFrmwjfl_jb").val(wj.jb);
    $("#sqFrmwjfl_dm").val(wj.dm);
    var j = {};
    j.bh = bh;
    $.dfAjax({
        url: "wjfl/quWjflZl.do",
        data: j,
        fun: function (data) {
            curWjfl = data;
            quanXian.sz = data.qx;
            quanXian.seq = -1;
            jxWjfl4Qx();
        }
    });

    $("#dvFrmQx").css("z-index", "10").show();
    $("#board").css("z-index", $("#dvFrmQx").css("z-index") - 1).show();
    $("#tblFrmwjfl_mc").focus();

}
function zjWjflQx() {
    isEditwjfl = false;
    var bh = wjfl.sz[wjfl.seq].bh;
    $("#bmbhwej").html(bh);
    $("#tblWenJianFenLei input[type=text]").val("").removeAttr("readonly");
    $("#tblWenJianFenLei input:checkbox[name='Qx']:checked").attr("checked", false);
    if(curWjfl.jb === "公司"){
        $("#tblWenJianFenLei_trbm").hide();
    }else{
        $("#tblWenJianFenLei_trbm").show();
    }
    $("#dvWenJianFenLei").css("z-index", "20").show();
    $("#board").css("z-index", $("#dvWenJianFenLei").css("z-index") - 1).show();

}

function xgWjflQx() {
    isEditwjfl = true;
    if (quanXian.sz[quanXian.seq] === undefined || quanXian.sz[quanXian.seq] === null) {
        return alert("请选档案分类");
    }
    var qx = quanXian.sz[quanXian.seq];
    bmXx.mc = qx.bmmc;
    bmXx.bh = qx.bmbh;
    jsXx.mc = qx.js;
    jsXx.dm = qx.jsdm;
    var cz = qx.cz.toString();
    for(var i = 0;i <czSz.length;i++){
        var c = czSz[i];
        if(cz.indexOf(c) > -1){
             $("#tblWenJianFenLei input:checkbox[name='Qx'][value='"+c+"']").prop("checked", true);
        }else{
            $("#tblWenJianFenLei input:checkbox[name='Qx'][value='"+c+"']").prop("checked", false);
        }
    }
    $("#tblWenJianFenLei_bm").val(qx.bmmc);
    $("#tblWenJianFenLei_js").val(qx.js);
    $("#tblWenJianFenLei_bz").val(qx.bz);
    if(curWjfl.jb === "公司"){
        $("#tblWenJianFenLei_trbm").hide();
    }else{
        $("#tblWenJianFenLei_trbm").show();
    }
    $("#dvWenJianFenLei").css("z-index", "20").show();
    $("#board").css("z-index", $("#dvWenJianFenLei").css("z-index") - 1).show();
}

function scWjflQx() {
    if (quanXian.sz[quanXian.seq] === undefined || quanXian.sz[quanXian.seq] === null) {
        return alert("请选档案分类");
    }
    var qx = quanXian.sz[quanXian.seq];
    if (!confirm("是否删除该权限?"))
        return false;
    if (qx.bh && qx.bh.length === 32) {
        scQuanXian.push(qx);
    }
    quanXian.sz.splice(quanXian.seq, 1);
    jxWjfl4Qx();
}

function bcWjflQxMx() {
    var qx = {};
    if (isEditwjfl) {
        qx = quanXian.sz[quanXian.seq];
        qx.act = 2;
    } else {
        qx.act = 1;
        quanXian.sz.push(qx);
    }
    if(curWjfl.jb === "公司"){
        qx.bmmc = "公司";
        qx.bmbh = "0";
    }else{
        if (bmXx.mc !== $("#tblWenJianFenLei_bm").val()) {
            return alert("请选择部门");
        }
        qx.bmmc = bmXx.mc;
        qx.bmbh = bmXx.bh;
    }
    
    if (jsXx.mc !== $("#tblWenJianFenLei_js").val()) {
        return alert("请选择角色");
    }
    qx.js = jsXx.mc;
    qx.jsdm = jsXx.dm;

    var czSz = [];
    var i = 0;
    $("#tblWenJianFenLei input:checkbox[name='Qx']:checked").each(function () {
        czSz[i] = $(this).val();
        i++;
    });
    qx.cz = czSz;
    qx.bz = $("#tblWenJianFenLei_bz").val();
    jxWjfl4Qx();
    $("#dvWenJianFenLei").hide();
    $("#board").css("z-index", $("#dvFrmQx").css("z-index") - 1).show();
}

function bcWjflQx() {
    var j = {};
    j.bh = wjfl.sz[wjfl.seq].bh;
    var qx = [];
    for (var i = 0; i < quanXian.sz.length; i++) {
        var e = quanXian.sz[i];
        if (e.act) {
            qx.push(e);
        } else {
            e.act = 0;
            qx.push(e);
        }
    }
    for (var i = 0; i < scQuanXian.length; i++) {
        var e = scQuanXian[i];
        e.act = 3;
        qx.push(e);
    }
    j.qx = qx;
    $.dfAjax({
        url: "/wjfl/xgWjflQx.do",
        data: j,
        fun: function (data) {
            quanXian.sz = [];
            scQuanXian = [];
            cxWenJianFenLei();
            $("#dvFrmQx,#board").hide();
        }
    });
}

function jxWjfl4Qx() {
    var data = [];
    for (var i = 0; i < quanXian.sz.length; i++) {
        var e = quanXian.sz[i];
        if (e.bz === null)
            e.bz = "";
        var cz = e.cz.toString().replace("cz","查找").replace("xz","下载").replace("zj","增加").replace("xg","修改").replace("zl","整理").replace("sh","审核").replace("ql","清理");
        var tr = {"td": [e.bmmc, e.js, cz]};
        data.push(tr);
    }
    $("#tblqxfp").built({"data": data, "obj": quanXian, "fit": true});
    if(curWjfl.jb === "公司"){
        $("#tblqxfp tr").find("td:eq(0)").hide();
    }else{
        $("#tblqxfp tr").find("td:eq(0)").show();
    }
}
