var khZd = [];
var kh4Edit = {};
var ygZd = [];
var khbh = "";
var scrbh = "";
var isEditWj;
var WenJian = {"sz": [], "seq": -1, "yx": 1, "zys": 0};
var xzWjfl = {};
var cxWjfl;
var wjflQx = {};
var xzKh;
var cxKh;
var xzBm4Edit = {};
var wjqx = {"qx": {"cz": [], "xz": [], "zj": [], "xg": [], "zl": [], "sh": [], "ql": []}};

$(document).ready(function () {
    $("#tblWenJian").setListBottom(WenJian, cxWenJian);
    $("#tblWenJian").built();
    $("#dvCx4Wj").popTopBar("查询档案信息");
    $("#dvFrmWj").popTopBar("档案信息");
    $("#dvFmSh").popTopBar("审核档案");
    $("#dvFmZl").popTopBar("整理档案");

    $("#dvFrmWj,#dvCx4Wj").find("input,select").r2t();
    $("#dvCx4Wj").find("input,select").r2t();
    $("#tblCx4Wj_qssj,#tblCx4Wj_jssj").setCalendar();
    $("#logo").click(function () {
        $("#tblCx4Wj input[type=text]").val("");
        $("#dvCx4Wj").show();
        $("#board").css("z-index", $("#dvCx4Wj").css("z-index") - 1).show();
        WenJian.yx = 0;
        khbh = "";
        scrbh = "";
    });
    cd4WenJian();
    xsJinDuBiao();
    dm_cxKeHu(szKhXx);
    dm_cxYuanGong(szYgXx);
    cxWjQx();
});

function cxWjQx() {
    var j = {};
    $.dfAjax({
        url: "/wjfl/cxSqWjfl.do",
        data: j,
        fun: function (data) {
            if (data.result === 0) {
                wjqx = data;
                szWjflXx();
            }
        }
    });

}

function szYgXx(sz) {
    $("#tblCx4Wj_scr").inputKit({"src": sz, "fun": xzYg4Scr});
}

function szKhXx(sz) {
    $("#tblFrmWj_kh").inputKit({"src": sz, "fun": xzKh4Fp});
    $("#tblCx4Wj_kh").inputKit({"src": sz, "fun": xzKh4Cx});
}

function szWjflXx() {
    $("#dvCx4Fwj_zt option").hide();
    $("#dvCx4Fwj_zt_shtg").show();
    var keys = Object.keys(wjqx.qx);
    for (var i = 0; i < keys.length; i++) {
        var key = keys[i];
        if (wjqx.qx[key].length === 0) {
            $("#" + key + "Wj").remove();
        } else {
            switch (key) {
                case "zj":
                    $("#dvCx4Fwj_zt_dsh").show();
                    break;
                case "xg":
                    $("#dvCx4Fwj_zt_dsh,#dvCx4Fwj_zt_yzl").show();
                    break;
                case "zl":
                    $("#dvCx4Fwj_zt option").show();
                    break;
                case "sh":
                    $("#dvCx4Fwj_zt_dsh,#dvCx4Fwj_zt_shbtg").show();
                    break;
                case "ql":
                    $("#dvCx4Fwj_zt_dsh,#dvCx4Fwj_zt_shbtg,#dvCx4Fwj_zt_yzl").show();
                    break;
                default: 
                    break;
            }
        }
    }
    var src = {};
    src.sz = wjqx.qx.cz;
    $("#tblCx4Wjfl").inputKit({"src": src, "obj": "cxWjfl"});
}

function xzYg4Scr(yg) {
    scrbh = yg.bh;
    $("#tblCx4Wj_scr").val(yg.mc);
}

function xzKh4Fp(kh) {
    kh4Edit = kh;
    $("#tblFrmWj_kh").val(kh.mc);
}

function xzKh4Cx(kh) {
    khbh = kh.bh;
    $("#tblCx4Wj_kh").val(kh.mc);
}

function cd4WenJian() {
    $("#tblWenJian").contextMenu("cd4Da", {
        bindings: {
            "zjWj": function (t) {
                zjWenJian();
            },
            "xgWj": function (t) {
                xgWenJian(true);
            },
            "qlWj": function (t) {
                scWenJian();
            },
            zlWj: function (t) {
                zlWenJian();
            },
            shWj: function (t) {
                shWenJian();
            },
            xzWj: function (t) {
                xzWenJian();
            }
        }
    });
    $("#tblFileDown").contextMenu("cd4Download", {
        bindings: {
            "qxXz": function (t) {
                qxDownload(true);
            }
        }
    });
    $("#tblFileUp").contextMenu("cd4Upload", {
        bindings: {
            "qxSc": function (t) {
                qxUpload();
            }
        }
    });
}

function cxWenJian() {
    var j = {};
    j.mc = $("#tblCx4Wj_mc").val();
    j.kh = khbh;
    j.scr = scrbh;
    if (cxWjfl && cxWjfl.mc && cxWjfl.mc === $("#tblCx4Wjfl").val()) {
        j.wjfl = cxWjfl.bh;
    }
    j.qssj = $("#tblCx4Wj_qssj").val();
    j.jssj = $("#tblCx4Wj_jssj").val();
    j.zt = $("#dvCx4Fwj_zt").val();
    j.yx = WenJian.yx;
    $.dfAjax({
        url: "/wj/cxWj.do",
        data: j,
        fun: function (data) {
            jxWenJian(data);
            $("#board,#dvCx4Wj").hide();
        }
    });
}

function jxWenJian(json) {
    WenJian.sz = json.sz;
    WenJian.yx = json.yx;
    WenJian.zys = json.zys;
    WenJian.jls = json.jls;
    if (WenJian.yx === 0)
        WenJian.yx = 1;
    var data = [];
    for (var i = 0; i < json.sz.length; i++) {
        var e = json.sz[i];
        var zt = "";
        switch (e.zt) {
            case 1:
                zt = "待审核";
                break;
            case 9:
                zt = "审核通过";
                break;
            case -9:
                zt = "审核不通过";
                break;
            case -1:
                zt = "已整理";
                break;
            default:
                break;
        }
        var tr = {"td": [e.mc, e.wjdx, e.khmc, e.scrmc, e.scsj, zt]};
        data.push(tr);
    }
    $("#tblWenJian").built({"data": data, "obj": WenJian, "dbclick": xgWenJian}).setPage(json);

}

function zjWenJian() {
    isEditWj = false;
    DaBianHao = "";
    var src = {};
    src.sz = wjqx.qx.zj;
    $("#tblFrmKh_bc").show();
    $("#tblFrmKh_hf").hide();
    $("#tblFrmWj_fl").inputKit({"src": src, "fun": xzWjfl_zj});
    $("#tblFrmWj input[type=text]").val("").removeAttr("readonly");
    $("#dvFrmWj").css("z-index", "10").show();
    $("#board").css("z-index", $("#dvFrmWj").css("z-index") - 1).show();
    $(".fileInputContainer").show();
    $("#tblFrmWj_mc").hide();
}

function xgWenJian(flag) {
    isEditWj = true;
    if (WenJian.sz[WenJian.seq] === undefined || WenJian.sz[WenJian.seq] === null) {
        return alert("请选择档案");
    }
    var wj = WenJian.sz[WenJian.seq];
    $("#tblFrmKh_bc,#tblFrmKh_hf").hide();
    if (wj.zt === 1) {
        if (chkQuanXian(wj, "xg", flag)) {
            $("#tblFrmKh_bc").show();
        }else{
            if(flag){
                return;
            }
        }
    } else if (wj.zt === -1) {
        if (chkQuanXian(wj, "sh", false)) {
            $("#tblFrmKh_hf").show();
        }
    }
    var src = {};
    src.sz = wjqx.qx.xg;
    $("#tblFrmWj_fl").inputKit({"src": src, "fun": xzWjfl_xg});
    var j = {};
    j.bh = wj.bh;
    $.dfAjax({
        url: "/wj/quWjZl.do",
        data: j,
        fun: function (data) {
            xzWjfl = {};
            xzWjfl.mc = data.flmc;
            xzWjfl.bh = data.wjflbh;
            xzBm4Edit.mc = data.bmmc;
            xzBm4Edit.bh = data.bmbh;
            if(data.bmbh === "0"){
                xzBm4Edit.mc = "公司";
                xzBm4Edit.bh = "0";
            }
            kh4Edit.mc = data.khmc;
            kh4Edit.bh = data.khbh;
            xzWjfl_xg(xzWjfl);
            $("#tblFrmWj input[type=text]").val("").removeAttr("readonly");
            $("#tblFrmWj_mc").val(data.mc);
            $("#tblFrmWj_kh").val(data.khmc);
            $("#tblFrmWj_fl").val(data.flmc);
            $("#tblFrmWj_bm").val(xzBm4Edit.mc);
            $("#tblFrmWj_zz").val(data.zz);
            $("#tblFrmWj_gjz").val(data.gjz);
            $("#tblFrmWj_bz").val(data.bz);
            $(".fileInputContainer").hide();
            $("#tblFrmWj_mc").show();
            $("#dvFrmWj").css("z-index", "10").show();
            $("#board").css("z-index", $("#dvFrmWj").css("z-index") - 1).show();
            $("#tblFrmWj_mc").focus();

        }
    });
}

function xzWjfl_zj(json) {
    xzWjfl4Edit(json, wjqx.qx.zj);
}

function xzWjfl_xg(json) {
    xzWjfl4Edit(json, wjqx.qx.xg);
}

function xzWjfl4Edit(json, sz) {
    xzWjfl = json;
    $("#tblFrmWj_fl").val(json.mc);
    var bmObj = {"sz": []};
    for (var i = 0; i < sz.length; i++) {
        var e = sz[i];
        if (e.bh === json.bh) {
            if (e.jb === "公司") {
                $("#tblFrmWj_bm").val("公司").attr("readonly", "readonly");
                xzBm4Edit.mc = "公司";
                xzBm4Edit.bh = "0";
            } else {
                bmObj.sz = e.bm;
                $("#tblFrmWj_bm").val("").removeAttr("readonly");
            }
            xzWjfl = e;
            break;
        }
    }
    $("#tblFrmWj_bm").inputKit({"src": bmObj, "obj": "xzBm4Edit"});
}

function check4Sjhm() {
    if ($("#tblFrmWj_mc").val() === "") {
        alert("请输入姓名");
        return false;
    }
    return true;
}

function scWenJian() {
    if (WenJian.sz[WenJian.seq] === undefined || WenJian.sz[WenJian.seq] === null) {
        return alert("请选择档案");
    }
    var wj  = WenJian.sz[WenJian.seq];
    if(wj.zt === 9 || wj.zt === -9){
        return alert("审核过的档案不能直接被清理，应先整理！");
    }
    if (!chkQuanXian(wj, "ql", true)) {
        return;
    }
    if (!confirm("确定清理档案：" + wj.mc + "?"))
        return false;
    var j = {};
    j.bh = wj.bh;
    $.dfAjax({
        url: "/wj/qlWj.do",
        data: j,
        fun: function (data) {
            alert("删除成功");
            cxWenJian();
        }
    });
}

function bcWenJian() {
    var j = {};
    j.kh = kh4Edit.bh;
    if (!xzWjfl || !xzWjfl.mc || xzWjfl.mc !== $("#tblFrmWj_fl").val()) {
        return alert("请选择文件分类");
    }
    j.wjfl = xzWjfl.bh;
    j.bm = xzBm4Edit.bh;
    j.zz = $("#tblFrmWj_zz").val();
    j.gjz = $("#tblFrmWj_gjz").val();
    j.bz = $("#tblFrmWj_bz").val();
    if (isEditWj) {
        var url = "/wj/xgWj.do";
        j.mc = $("#tblFrmWj_mc").val();
        j.bh = WenJian.sz[WenJian.seq].bh;
    } else {
        var file = $("#fileInput").get(0).files[0];
        j.mc = file.name;
        j.wjdx = file.size;
        url = "/wj/zjWj.do";
    }
    $.dfAjax({
        url: url,
        data: j,
        fun: function (data) {
            cxWenJian();
            if (data.wjbh !== "undefined" && data.wjbh !== null) {
                addFile(data.wjbh);
            }
            $("#dvFrmWj,#board").hide();
        }
    });
}

function hfWenJian() {
    if (!confirm("确定恢复档案：" + WenJian.sz[WenJian.seq].mc + "?"))
        return false;
    var j = {};
    j.bh = WenJian.sz[WenJian.seq].bh;
    $.dfAjax({
        url: "/wj/hfWj.do",
        data: j,
        fun: function (data) {
            cxWenJian();
            $("#dvFrmWj,#board").hide();
        }
    });
}

function shWenJian() {
    if (WenJian.sz[WenJian.seq] === undefined || WenJian.sz[WenJian.seq] === null) {
        return alert("请选择档案");
    }
    var wj = WenJian.sz[WenJian.seq];
    if(wj.zt !== 1){
        return alert("该档案不是待审核状态");
    }
    if (!chkQuanXian(wj, "sh", true)) {
        return;
    }
    $("#tblFrmSh_bz").val("");
    $("#tblFrmSh_mc").val(wj.mc);
    $("#dvFmSh").css("z-index", "10").show();
    $("#board").css("z-index", $("#dvFmSh").css("z-index") - 1).show();
}

function bcShenHe() {
    if (!confirm("确定审核档案：" + WenJian.sz[WenJian.seq].mc + "?"))
        return false;
    var j = {};
    j.bh = WenJian.sz[WenJian.seq].bh;
    j.shyj = $("#tblFrmSh_shyj").val();
    j.bz = $("#tblFrmSh_bz").val();
    $.dfAjax({
        url: "/wj/shWj.do",
        data: j,
        fun: function (data) {
            cxWenJian();
            $("#board,#dvFmSh").hide();
        }
    });
}

function chkQuanXian(wj, cz, showFlag) {
    var qx = wjqx.qx[cz];
    for (var i = 0; i < qx.length; i++) {
        var f = qx[i];
        if (f.bh === wj.wjflbh) {
            for (var j = 0; j < f.bm.length; j++) {
                var b = f.bm[j];
                if (b.bh === wj.bmbh) {
                    return true;
                }
            }
        }
    }
    var s = "";
    switch (cz) {
        case "cz":
            s = "查找";
            break;
        case "zj":
            s = "增加";
            break;
        case "xg":
            s = "修改";
            break;
        case "zl":
            s = "整理";
            break;
        case "xz":
            s = "下载";
            break;
        case "sh":
            s = "审核";
            break;
        case "ql":
            s = "清理";
            break;
        default :
            s = "";
    }
    if(showFlag)
        alert("权限不足，不能" + s + "档案：" + wj.mc);
    return false;
}

function zlWenJian() {
    if (WenJian.sz[WenJian.seq] === undefined || WenJian.sz[WenJian.seq] === null) {
        return alert("请选择档案");
    }
    var wj = WenJian.sz[WenJian.seq];
    if(wj.zt === -1){
        return alert("该档案已整理！");
    }
    if(wj.zt === 1){
        return alert("未审核档案不需要整理！");
    }
    if (!chkQuanXian(wj, "zl", true)) {
        return;
    }
    $("#tblFrmZl_bz").val("");
    $("#tblFrmZl_mc").val(wj.mc);
    $("#dvFmZl").css("z-index", "10").show();
    $("#board").css("z-index", $("#dvFmZl").css("z-index") - 1).show();
}

function bcZhengLi() {
    if (!confirm("确定整理档案：" + WenJian.sz[WenJian.seq].mc + "?"))
        return false;
    var j = {};
    j.bh = WenJian.sz[WenJian.seq].bh;
    j.bz = $("#tblFrmZl_bz").val();
    $.dfAjax({
        url: "/wj/zlWj.do",
        data: j,
        fun: function (data) {
            cxWenJian();
            $("#board,#dvFmZl").hide();
        }
    });
}

function xzWenJian() {
    $("#fmDownload").show();
    if (WenJian.sz[WenJian.seq] === undefined || WenJian.sz[WenJian.seq] === null) {
        return alert("请选择档案");
    }
    var wj = WenJian.sz[WenJian.seq];
    if (!chkQuanXian(wj, "xz", true)) {
        return;
    }
    downLoad(wj.mc, wj.bh, wj.wjdx);
}

function xsJinDuBiao() {//这个是上传进度函数的加载
    $("#fmUpload_down").click(function () {
        $("#tblFileUp").hide();
        $("#tblFileUpWc").hide();
        $("#fmUpload").css({
            "height": "40px"
        });
    });

    $("#fmUpload_up").click(function () {
        $("#fmUpload").css({
            "height": "400px"
        });
        $("#tblFileUp").show();
        $("#tblFileUpWc").show();
    });

    $("#fmUpload_close").click(function () {
        $("#fmUpload").hide();
    });

    $("#fmDownload_down").click(function () {
        $("#tblFileDown").hide();
        $("#tblFileDownWc").hide();
        $("#fmDownload").css({
            "height": "40px"
        });
    });

    $("#fmDownload_up").click(function () {
        $("#fmDownload").css({
            "height": "400px"
        });
        $("#tblFileDown").show();
        $("#tblFileDownWc").show();
    });

    $("#fmDownload_close").click(function () {
        $("#fmDownload").hide();
    });
}
