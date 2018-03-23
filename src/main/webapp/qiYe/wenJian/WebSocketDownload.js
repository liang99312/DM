var fileUuid = "";
var viewUuid = null;
var sendBuff = new ArrayBuffer(40);
var viewSend = new Int8Array(sendBuff);
var viewSendOfs = new Int32Array(sendBuff, 36);//起始位置为33，一个长度(4个字节)
var seqDownload = -1;
var ztDownFlag = false;
var WskDownLoad = {
    fileList: [],
    sliceOfs: 0, //请求的片
    receBuffer: null, //接收到的数据片
    dataView: null, //接收到的数据片的dataView
    recvfileId: "", //接收的文件编号
    fileOfs: "", //接收到的片序号
    uuidBuf: null, //文件uuid的Buf
    ofsLen: null, //接收到的文件长度
    ofsLenBuf: null, //接收文件的buf
    ofsLenView: null, //文件长度的dataview；
    Init: function () {
        downWebSocket = new WebSocket("ws://" + window.location.host + "/docMgmtWskDownLoad");
        if (!downWebSocket) {
            return false;
        }
        this.fileOffset = 0;
        downWebSocket.onerror = function (event) {
            alert("downWebSocket 出错");
        };

        downWebSocket.onopen = function (event) {
            downWebSocket.binaryType = "arraybuffer";
        };

        downWebSocket.onmessage = function (event) {
            downOnMessage(event);
        };
        return true;
    },
    send: function (dataBuffer) {
        if (downWebSocket.readyState !== 1) {
            downWebSocket.close();
            if (Init()) {
                setTimeout(function () {
                    downWebSocket.send(dataBuffer);
                }, 500);
            } else {
                alert("连接不到服务器");
            }
        } else {
            downWebSocket.send(dataBuffer);
        }
    },
    close: function () {
        downWebSocket.close();
    }
};

$(document).ready(function () {
    WskDownLoad.Init();
});

function downOnMessage(event) {
    WskDownLoad.receBuffer = event.data;
    WskDownLoad.dataView = new Int8Array(WskDownLoad.receBuffer);
    if (WskDownLoad.dataView[0] === 0) {//接收文件包
        WskDownLoad.uuidBuf = WskDownLoad.dataView.slice(1, 33);//w文件uuid
        WskDownLoad.ofsLenBuf = WskDownLoad.receBuffer.slice(54, 58);
        for (var i = 0; i < 32; i++) {
            WskDownLoad.recvfileId = WskDownLoad.recvfileId + String.fromCharCode(WskDownLoad.uuidBuf[i]);
        }
        WskDownLoad.ofsLenView = new Int32Array(WskDownLoad.ofsLenBuf);
        WskDownLoad.ofsLen = WskDownLoad.ofsLenView[0];
        if (WskDownLoad.dataView[33] === 1) {//后台文件读取成功
            for (var i = 34; i < 54; i++) {//解析文件uuid
                WskDownLoad.fileOfs = WskDownLoad.fileOfs + String.fromCharCode(WskDownLoad.dataView[i]);
            }
            if (save(WskDownLoad.fileOfs, WskDownLoad.dataView.slice(58, WskDownLoad.dataView.length), WskDownLoad.ofsLen)) {
                WskDownLoad.sliceOfs = WskDownLoad.sliceOfs + 1;
                viewSendOfs[0] = WskDownLoad.sliceOfs;
                WskDownLoad.recvfileId = "";
                WskDownLoad.fileOfs = "";
                WskDownLoad.dataView = null;
                WskDownLoad.receBuffer = null;
                if (!ztDownFlag) {
                    WskDownLoad.send(sendBuff);
                }
            } else {
                //alert("写入文件失败");
            }
        } else if (WskDownLoad.dataView[33] === 2) {//整个文件读取成功
            for (var i = 34; i < 54; i++) {//解析文件uuid
                WskDownLoad.fileOfs = WskDownLoad.fileOfs + String.fromCharCode(WskDownLoad.dataView[i]);
            }
            if (save(WskDownLoad.fileOfs, WskDownLoad.dataView.slice(58, WskDownLoad.dataView.length), WskDownLoad.ofsLen)) {
                WskDownLoad.sliceOfs = 0;
                viewSendOfs[0] = WskDownLoad.sliceOfs;
                WskDownLoad.recvfileId = "";
                WskDownLoad.fileOfs = "";
                WskDownLoad.dataView = null;
                WskDownLoad.receBuffer = null;
                window.WriteFile(fileUuid, "", 0, -1);
                wcxzWenJian(fileUuid);
                $("#tblFileDown tr:eq(1) td:eq(2)").html("100%");
                var dl = WskDownLoad.fileList[0];
                $("#tblFileDownWc").append("<tr><td style='width:62%;'>" + dl.fileName + "</td><td style='width:20%;text-align:right;'>" + (dl.size / 1048576).toFixed(2) + "M</td><td style='color:#0000ff'>完成</td></tr>");
                $("#tblFileDown tr:eq(1)").remove();
                WskDownLoad.fileList.splice(0, 1);
                startDownload(true);
            }
        } else {
            alert("后台读取文件出错");//然后解析错误信息出来
        }
    } else {
        alert("后台读取文件出错");
        console.log("后台读取文件出错");
        seqDownload = 0;
        qxDownload(false);
    }
}

function wcxzWenJian(wjbh){
    var j = {"bh":wjbh};
    $.dfAjax({
        url: "/wj/wcxzWj.do",
        data: j,
        fun: function (data) {
        }
    });
}

function save(fileOffset, fileBuf, size) {
    var result = window.WriteFile(fileUuid, fileBuf.toString(), parseInt(fileOffset, 10), size);
    if (result === 0) {
        var dl = WskDownLoad.fileList[0];
        $("#tblFileDown tr:eq(1) td:eq(2)").html(((parseInt(fileOffset, 10) / dl.size) * 100).toFixed(2) + "%");
        return true;
    } else {
        if (result === -1) {
            console.log("无法打开文件");
        } else if (result === -3) {
            console.log("文件写入失败");
        }
        seqDownload = 0;
        qxDownload(false);
    }
    return false;
}

function downLoad(fileName, fileId, size) {//这个函数用于触发文件下载
    var sizeM = parseInt(size / 1048576);
    ztDownFlag = true;
    //做一个延时，完成最后一次数据接收，暂停下载，防止websocket服务器因超时而断连
    setTimeout(function () {
        var r = window.CreateFile(fileName, fileId, sizeM);
        ztDownFlag = false;
        if (WskDownLoad.fileList.length > 0) {
            WskDownLoad.send(sendBuff);
        }
        if (isNaN(r)) {
            var i = r.lastIndexOf("\\");
            var dl = {};
            dl.fileName = r.slice(i + 1);
            dl.fileId = fileId;
            dl.size = size;
            WskDownLoad.fileList.push(dl);
            $("#tblFileDown").append("<tr><td style='width:62%;'>" + dl.fileName + "</td><td style='width:20%;text-align:right;'>" + (dl.size / 1048576).toFixed(2) + "M</td><td style='text-align:center;'>等待下载</td></tr>");
            $("#tblFileDown").find("tr").mousedown(function () {
                seqDownload = $(this).prevAll().length - 1;
            });
            startDownload(false);
        } else if (r !== -1) {
            alert("创建文件失败");
        }
    }, 500);

}

function startDownload(flag) {
    if (WskDownLoad.fileList.length === 1 || (flag && WskDownLoad.fileList.length > 0)) {
        qxFlag = false;
        var dl = WskDownLoad.fileList[0];
        fileUuid = dl.fileId;
        viewSendOfs[0] = 0;
        viewSend[0] = 1;
        for (var i = 1; i < fileUuid.length + 1; i++) {
            viewSend[i] = fileUuid.charCodeAt(i - 1);  ///< 以10进制的整数返回 某个字符 的unicode编码
        }
        viewUuid = viewSend;
        WskDownLoad.sliceOfs = 0;
        viewSendOfs[0] = WskDownLoad.sliceOfs;
        WskDownLoad.recvfileId = "";
        WskDownLoad.fileOfs = "";
        WskDownLoad.dataView = null;
        WskDownLoad.receBuffer = null;
        $("#tblFileDown tr:eq(1) td:eq(2)").html("0%");
        WskDownLoad.send(viewSend);
    }
}

function qxDownload(flag) {
    if (WskDownLoad.fileList[seqDownload] === undefined || WskDownLoad.fileList[seqDownload] === null) {
        return;
    }
    var dl = WskDownLoad.fileList[seqDownload];
    if (flag) {
        if (!confirm("确定取消下载文件：" + dl.fileName + "?")) {
            return;
        }
    }else{
        $("#tblFileDownWc").append("<tr><td style='width:62%;'>" + dl.fileName + "</td><td style='width:20%;text-align:right;'>" + (dl.size / 1048576).toFixed(2) + "M</td><td style='color:#FF0000;text-align:center;'>失败</td></tr>");
    }
    ztDownFlag = seqDownload === 0;
    $("#tblFileDown tr:eq(" + (seqDownload + 1) + ")").remove();
    WskDownLoad.fileList.splice(seqDownload, 1);
    //做一个延时，完成最后一次数据接收，防止websocket服务器因超时而断连
    setTimeout(function () {
        if (ztDownFlag) {
            ztDownFlag = false;
            window.WriteFile(fileUuid, "", 0, -1);
            startDownload(true);
        } else {
            WskDownLoad.send(sendBuff);
        }
    }, 500);
}
