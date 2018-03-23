var qiYe = {"sz": [], "seq": -1, "yx": 1, "zys": 0};
var qyBianHao;

$(document).ready(function () {
    $("#tblQiye").setListBottom(qiYe, cxQiYe);
    $("#tblQiye").built();
    $("#dvCx4Qy").popTopBar("查询企业信息");
    $("#dvFrmQy").popTopBar("企业信息");
    $("#dvFrmQy").find("input,select").r2t();
    $("#dvCx4Qy").find("input,select").r2t();
    $("#logo").click(function () {
        $("#dvCx4Qy").show();
        $("#board").css("z-index", $("#dvCx4Qy").css("z-index") - 1).show();
        $("#tblCx4Qy_mc").focus();
    });
});

function cxQiYe(yzm) {
    var j = {"yx": qiYe.yx};
    j.mc = $("#tblCx4Qy_mc").val();
    if(yzm){
        j.yzm = yzm; 
    }else{
        j.yzm = $("#tblCx4Qy_yzm").val();
    }
    $.dfAjax({
        url: "/qy/cxQy.do",
        data: j,
        fun: function (data) {
            if(data.result === -1){
                return alert(data.msg);
            }
            jxQiYe(data);
            $("#dvCx4Qy,#board").hide();
        }
    });
}

function jxQiYe(json) {
    qiYe.sz = json.sz;
    qiYe.yx = json.yx;
    qiYe.zys = json.zys;
    qiYe.jls = json.jls;
    if (qiYe.yx === 0)
        qiYe.yx = 1;
    var data = [];
    for (var i = 0; i < json.sz.length; i++) {
        var e = json.sz[i];
        var tr = {"td": [e.mc, e.sjhm]};
        data.push(tr);
    }
    $("#tblQiye").built({"data": data, "obj": qiYe}).setPage(json);
    $("#tblCx4Qy input[type=text]").val("");

}

function zjQiYe() {
    isEditQy = false;
    qyBianHao = "";
    $("#dvFrmQy").css("z-index", "10").show();
    $("#board").css("z-index", $("#dvFrmQy").css("z-index") - 1).show();
    $("#tblFrmQy_mc").focus();

}

function checkQyXinXi() {
    if ($("#tblFrmQy_mc").val() === "") {
        alert("请输入名称");
        $("#tblFrmQy_mc").focus();
        return false;
    }
    return true;
}

function bcQiYe() {
    if (checkQyXinXi()) {
        var j = {};
        j.mc = $("#tblFrmQy_mc").val();
        j.sjhm = $("#tblFrmQy_sjhm").val();
        j.yzm = $("#tblFrmQy_yzm").val();
        var url = "/qy/qyZc.do";
        $.dfAjax({
            url: url,
            data: j,
            fun: function (data) {
                if (data.result === 0) {
                    cxQiYe($("#tblFrmQy_yzm").val());
                    $("#dvFrmQy,#board").hide();
                }
            }
        });
    }

}
