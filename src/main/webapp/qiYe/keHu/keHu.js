var keHu = {"sz": [], "seq": -1, "yx": 1, "zys": 0};
var khBianHao;
var isEditKh;

$(document).ready(function () {
    $("#tblKehu").setListBottom(keHu, cxKeHu);
    $("#tblKehu").built();
    $("#dvCx4Kh").popTopBar("查询客户信息");
    $("#dvFrmKh").popTopBar("客户信息");
    $("#dvFrmKh").find("input,select").r2t();
    $("#dvCx4Kh").find("input,select").r2t();
    $("#logo").click(function () {
        $("#dvCx4Kh").show();
        $("#board").css("z-index", $("#dvCx4Kh").css("z-index") - 1).show();
        $("#tblCx4Kh_mc").focus();
    });
    cd4KeHu();
});

function cd4KeHu() {
    $("#tblKehu").contextMenu("cd4Kh", {
        bindings: {
            "zjKh": function (t) {
                zjKeHu();
            },
            "xgKh": function (t) {
                xgKeHu();
            },
            "scKh": function (t) {
                scKeHu();
            }
        }
    });
}

function cxKeHu() {
    var j = {"yx": keHu.yx};
    j.mc = $("#tblCx4Kh_mc").val();
    j.zt = $("#tblCx4Kh_zt").val();
    $.dfAjax({
        url: "/kh/cxKh.do",
        data: j,
        fun: function (data) {
            jxKeHu(data);
            $("#dvCx4Kh,#board").hide();
        }
    });
}

function jxKeHu(json) {
    keHu.sz = json.sz;
    keHu.yx = json.yx;
    keHu.zys = json.zys;
    keHu.jls = json.jls;
    if (keHu.yx === 0)
        keHu.yx = 1;
    var data = [];
    for (var i = 0; i < json.sz.length; i++) {
        var e = json.sz[i];
        if (e.zt === 1) {
            e.zt = "有效";
        } else if (e.zt === -1) {
            e.zt = "无效";
        }
        var tr = {"td": [e.mc, e.dm, e.lxr, e.lxfs, e.zt]};
        data.push(tr);
    }
    $("#tblKehu").built({"data": data, "obj": keHu, "dbclick": xgKeHu}).setPage(json);
    $("#tblCx4Kh input[type=text]").val("");

}

function zjKeHu() {
    isEditKh = false;
    khBianHao = "";
    $("#tblFrmKh_bc").show();
    $("#tblFrmKh input[type=text]").val("").removeAttr("readonly");
    $("#dvFrmKh").css("z-index", "10").show();
    $("#board").css("z-index", $("#dvFrmKh").css("z-index") - 1).show();
    $("#tblFrmKh_mc").focus();

}

function xgKeHu() {
    isEditKh = true;
    if (keHu.sz[keHu.seq] === undefined || keHu.sz[keHu.seq] === null) {
        return alert("请选择客户");
    }
    var kh = keHu.sz[keHu.seq];
    if(kh.zt === -1 || kh.zt === "无效"){
        $("#tblFrmKh_bc").hide();
    }else{
        $("#tblFrmKh_bc").show();
    }
    $("#tblFrmKh input[type=text]").val("").removeAttr("readonly");
    $("#tblFrmKh_mc").val(kh.mc);
    $("#tblFrmKh_dm").val(kh.dm);
    $("#tblFrmKh_dz").val(kh.dz);
    $("#tblFrmKh_lxr").val(kh.lxr);
    $("#tblFrmKh_lxfs").val(kh.lxfs);
    $("#tblFrmKh_bz").val(kh.bz);
    $("#dvFrmKh").css("z-index", "10").show();
    $("#board").css("z-index", $("#dvFrmKh").css("z-index") - 1).show();
    $("#tblFrmKh_mc").focus();
}

function checkKhXinXi() {
    if ($("#tblFrmKh_mc").val() === "") {
        alert("请输入姓名");
        $("#tblFrmKh_mc").focus();
        return false;
    }
    return true;
}

function scKeHu() {
    if (keHu.sz[keHu.seq] === undefined || keHu.sz[keHu.seq] === null) {
        return alert("请选择客户");
    }
    if (!confirm("是否删除客户：" + keHu.sz[keHu.seq].mc + "?"))
        return false;
    var j = {"bh": keHu.sz[keHu.seq].bh};
    $.dfAjax({
        url: "/kh/scKh.do",
        data: j,
        fun: function (data) {
            if (data.result === 0) {
                cxKeHu();
            }
        }
    });
}

function bcKeHu() {
    if (checkKhXinXi()) {
        var j = {};
        j.mc = $("#tblFrmKh_mc").val();
        j.dm = $("#tblFrmKh_dm").val();
        j.lxr = $("#tblFrmKh_lxr").val();
        j.lxfs = $("#tblFrmKh_lxfs").val();
        if (isEditKh) {
            j.bh = keHu.sz[keHu.seq].bh;
        }
        var url = isEditKh ? "/kh/xgKh.do" : "/kh/zjKh.do";
        $.dfAjax({
            url: url,
            data: j,
            fun: function (data) {
                if (data.result === 0) {
                    cxKeHu();
                    $("#dvFrmKh,#board").hide();
                }
            }
        });
    }

}
