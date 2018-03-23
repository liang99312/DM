var buMen = {"sz": [], "seq": -1, "yx": 1, "zys": 0};
var bmYuanGong = {"sz": [], "seq": -1};
var zjYg = {"sz": []};
var scYg = {"sz": []};
var isEditBm;
var isEditBmyg;

$(document).ready(function () {
    $("#tblBumen").setListBottom(buMen, cxBuMen);
    $("#tblBumen").built();
    $("#dvCx4Bm").popTopBar("查询部门信息");
    $("#dvFrmFpyg").popTopBar("分配员工");
    $("#dvFrmBm").popTopBar("部门信息");
    $("#dvFrmBmxq").popTopBar("部门详细信息");
    $("#dvFrmFpyg").find("input,select").r2t();
    $("#dvFrmBm").find("input,select").r2t();
    $("#dvCx4Bm").find("input,select").r2t();
    $("#logo").click(function () {
        $("#tblCx4Bm input[type=text]").val("");
        $("#dvCx4Bm").show();
        $("#board").css("z-index", $("#dvCx4Bm").css("z-index") - 1).show();
        $("#tblCx4Bm_mc").focus();
    });
    cd4BuMen();
    cd4FpYuanGong();
    dm_cxYuanGong();
});

function cd4BuMen() {
    $("#tblBumen").contextMenu("cd4Bm", {
        bindings: {
            "zjBm": function (t) {
                zjBuMen();
            },
            "xgBm": function (t) {
                xgBuMen();
            },
            "scBm": function (t) {
                scBuMen();
            },
            "xgBmyg": function (t) {
                xgBmYuanGong(true);
            }
        }
    });
}

function cd4FpYuanGong() {
    $("#tblBm_bmyg").contextMenu("cd4fpYg", {
        bindings: {
            "scYg": function (t) {
                scBmYuanGong();
            }
        }
    });
}

function cxBuMen() {
    var j = {};
    j.mc = $("#tblCx4Bm_mc").val();
    j.zt = $("#tblCx4Bm_zt").val();
    j.dm = $("#tblFrmBm_dm").val();
    j.yx = buMen.yx;
    $.dfAjax({
        url: "/bm/cxBm.do",
        data: j,
        fun: function (data) {
            jxBuMen(data);
            $("#dvCx4Bm,#board").hide();
        }
    });
}

function jxBuMen(json) {
    buMen.sz = json.sz;
    buMen.yx = json.yx;
    buMen.zys = json.zys;
    buMen.jls = json.jls;
    if (buMen.yx === 0)
        buMen.yx = 1;
    var data = [];
    for (var i = 0; i < json.sz.length; i++) {
        var e = json.sz[i];
        if (e.zt === 1) {
            e.zt = "有效";
        } else {
            e.zt = "无效";
        }
        var tr = {"td": [e.mc, e.bmms, e.dm, e.zt]};
        data.push(tr);
    }
    $("#tblBumen").built({"data": data, "obj": buMen, "dbclick":xgBmYuanGong}).setPage(json);
    $("#tblCx4Bm input[type=text]").val("");

}

function zjBuMen() {
    isEditBmyg = false;
    isEditBm = false;
    $("#tblFrmBm input[type=text]").val("").removeAttr("readonly");
    cd4FpYuanGong();
    $("#tblBm_bmyg input").val("");
    bmYuanGong.sz = [];
    gxBuMenYuanGong();
    $("#tblFrmBm .diBian").show();
    $("#dvFrmBm").css("z-index", "10").show();
    $("#board").css("z-index", $("#dvFrmBm").css("z-index") - 1).show();
    $("#tblFrmBm_mc").focus();
    bmYuanGong.sz = [];
}

function xgBuMen() {
    isEditBmyg = false;
    isEditBm = true;
    if (buMen.sz[buMen.seq] === undefined || buMen.sz[buMen.seq] === null) {
        return alert("请选择部门");
    }
    var bm = buMen.sz[buMen.seq];
    if(bm.zt === -1 || bm.zt === "无效"){
        return alert("无效部门不允许修改！");
    }
    $("#tblBm_bmyg").empty();
    $("#tblFrmBm input[type=text]").val("").removeAttr("readonly");
    $("#tblBm_bmyg").off("contextmenu");
    $("#tblFrmBm_mc").val(bm.mc);
    $("#tblFrmBm_ms").val(bm.bmms);
    $("#tblFrmBm_dm").val(bm.dm);
    $("#tblFrmBm_yg_lb").parent("tr").hide();
    $("#tblFrmBm .diBian").show();
    $("#dvFrmBm").css("z-index", "10").show();
    $("#board").css("z-index", $("#dvFrmBm").css("z-index") - 1).show();
    $("#tblFrmBm_mc").focus();
}

function check4Bmmc() {
    if ($("#tblFrmBm_mc").val() === "") {
        alert("请输部门名称");
        return false;
    }
    return true;
}

function scBuMen() {
    if (buMen.sz[buMen.seq] === undefined || buMen.sz[buMen.seq] === null) {
        return alert("请选择部门");
    }
    if (!confirm("是否删除部门：" + buMen.sz[buMen.seq].mc + "?"))
        return false;
    var j = {};
    j.bmbh = buMen.sz[buMen.seq].bh;
    $.dfAjax({
        url: "/bm/scBm.do",
        data: j,
        fun: function (data) {
            cxBuMen();
        }
    });
}

function bcBuMen() {
    if (isEditBmyg) {
        return bcBmYuanGong();
    }
    if (check4Bmmc()) {
        var j = {};
        var url = "/bm/zjBm.do";
        j.mc = $("#tblFrmBm_mc").val();
        j.bmms = $("#tblFrmBm_ms").val();
        j.dm = $("#tblFrmBm_dm").val();
        if (isEditBm) {
            j.bmbh = buMen.sz[buMen.seq].bh;
            url = "/bm/xgBm.do";
        } else {
            j.bmyg = bmYuanGong.sz;
        }
        $.dfAjax({
            url: url,
            data: j,
            fun: function (data) {
                cxBuMen();
                $("#dvFrmBm").hide();
                $("#board").hide();
            }
        });
    }
}

function xgBmYuanGong(editFlag) {
    if (buMen.sz[buMen.seq] === undefined || buMen.sz[buMen.seq] === null) {
        return alert("请选择部门");
    }
    if(editFlag  && (buMen.sz[buMen.seq].zt === -1 || buMen.sz[buMen.seq].zt === "无效")){
        return alert("无效部门不允许设置员工！");
    }
    isEditBmyg = true;
    var j = {"bmbh": buMen.sz[buMen.seq].bh};
    $.dfAjax({
        url: "/bm/quBmZl.do",
        data: j,
        fun: function (d) {
            if (d.result === 0) {
                xsBmYuanGong(d,editFlag);
            }
        }
    });
}

function xsBmYuanGong(json, editFlag) {
    zjYg.sz = [];
    scYg.sz = [];
    bmYuanGong.sz = json.bmyg;
    $("#tblFrmBm_mc").val(json.mc).attr("readonly", "readonly");
    $("#tblFrmBm_ms").val(json.bmms).attr("readonly", "readonly");
    $("#tblFrmBm_dm").val(json.dm).attr("readonly", "readonly");
    $("#tblFrmBm_yg_lb").parent("tr").show();
    if (editFlag) {
        $("#tblFrmBm .diBian").show();
        gxBuMenYuanGong();
    } else {
        $("#tblFrmBm .diBian").hide();
        jxBmYuanGong();
    }
    $("#tblFrmBm_mc").focus();
    $("#dvFrmBm").show();
    $("#board").css("z-index", $("#dvFrmBm").css("z-index") - 1).show();
}

function jxBmYuanGong() {
    $("#tblBm_bmyg").empty();
    var rowMax = parseInt((bmYuanGong.sz.length + 4) / 5);
    if(rowMax === 0){
        $("#tblBm_bmyg").append("<tr><td></td><td></td><td></td><td></td><td></td></tr>");
    }
    for (var row = 0; row < rowMax; row++) {
        var rs = "";
        for (var col = 0; col < 5; col++) {
            if ((row * 5 + col) < bmYuanGong.sz.length)
                rs += "<td>" + bmYuanGong.sz[row * 5 + col].mc + "</td>";
            else
                rs += "<td></td>";
        }
        $("#tblBm_bmyg").append("<tr>" + rs + "</tr>");
    }
}

function gxBuMenYuanGong() {
    $("#tblBm_bmyg input").val("");
    $("#tblBm_bmyg").empty();
    var rowMax = parseInt((bmYuanGong.sz.length + 4) / 5);
    var colLast = bmYuanGong.sz.length % 5;
    for (var row = 0; row < rowMax; row++) {
        var rs = "";
        for (var col = 0; col < 5; col++) {
            if ((row * 5 + col) < bmYuanGong.sz.length)
                rs += "<td>" + bmYuanGong.sz[row * 5 + col].mc + "</td>";
            else
                rs += "<td></td>";
        }
        $("#tblBm_bmyg").append("<tr>" + rs + "</tr>");
    }
    if (colLast === 0) {
        $("#tblBm_bmyg").append("<tr><td></td><td></td><td></td><td></td><td></td></tr>");
        $("#tblBm_bmyg tr:eq(" + rowMax + ") td:eq(0)").html($("#divYgXm").html());
    } else {
        $("#tblBm_bmyg tr:eq(" + (rowMax - 1) + ") td:eq(" + colLast + ")").html($("#divYgXm").html());
    }
    $("#tblBm_bmyg input").inputKit({"src": dm_YuanGongList, "obj": "", "fun": pushBmyg});

    $("#tblBm_bmyg td:lt(" + bmYuanGong.sz.length + ")").mousedown(function () {
        bmYuanGong.seq = $("#tblBm_bmyg td").index($(this));
    });
    $("#tblBm_bmyg input").focus();
}

function pushBmyg(sz) {
    var t = {};
    t.bh = sz.bh;
    t.mc = sz.mc;
    for (var i = 0; i < bmYuanGong.sz.length; i++) {
        if (bmYuanGong.sz[i].bh === sz.bh)
            return alert("该员工已存在!");
    }
    bmYuanGong.sz.push(t);
    zjYg.sz.push(t.bh);
    gxBuMenYuanGong();
}

function scBmYuanGong() {
    if (!confirm("是否删除员工：" + bmYuanGong.sz[bmYuanGong.seq].mc + "?"))
        return false;
    scYg.sz.push(bmYuanGong.sz[bmYuanGong.seq].bh);
    var xh = bmYuanGong.seq;
    bmYuanGong.sz.splice(xh, 1);
    zjYg.sz.splice(xh, 1);
    gxBuMenYuanGong();
}

function bcBmYuanGong() {
    if (buMen.sz[buMen.seq] === undefined || buMen.sz[buMen.seq] === null) {
        return;
    }
    var json = {};
    json.bmbh = buMen.sz[buMen.seq].bh;
    json.zjYg = zjYg.sz;
    json.scYg = scYg.sz;
    $.dfAjax({
        url: "/bm/xgBmyg.do",
        data: json,
        fun: function (j) {
            if (j.result === 0) {
                zjYg.sz = [];
                scYg.sz = [];
                isEditBmyg = false;
                $("#dvFrmBm, #board").hide();
            }
        }
    });
}
