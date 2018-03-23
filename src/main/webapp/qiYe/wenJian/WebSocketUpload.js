var fileObj = null;
var seqUpload = -1;
var ztUpFlag = false;
var upUuidBuf = null;
var reCount = 0;
var DocWsk = {
    fileList: [],
    headBuf: new ArrayBuffer(8),
    headBufView: null,
    myFile: null,
    fileBuf: null,
    fileUuid: null,
    fileOffset: null,
    Init: function () {
        webSocket = new WebSocket("ws://" + window.location.host + "/docMgmtWsk");
        if (!webSocket) {
            return false;
        }
        this.fileOffset = 0;
        webSocket.onerror = function (event) {
            onError(event);
        };

        webSocket.onopen = function (event) {
            webSocket.binaryType = "arraybuffer";
            onOpen(event);
        };

        webSocket.onmessage = function (event) {
            onMessage(event);
        };
        return true;
    },
    setPackHead: function (packLen, verify, sendID, msgType) {//包头
        var len = new Uint32Array(this.headBuf, 0, 1);
        len[0] = packLen;
        var verify = new Int16Array(this.headBuf, 4, 1);
        verify[0] = verify;
        var SendID = new Int8Array(this.headBuf, 6, 1);//包编号
        SendID[0] = sendID;
        var MsgType = new Int8Array(this.headBuf, 7, 1);
        MsgType[0] = msgType;
        this.headBufView = new Int8Array(this.headBuf, 0, 8);
    },
    setFileInfo: function (sendArrBuf, file, md5) {//文件信息
        var viewSendBuf = new Int8Array(sendArrBuf, 0, 8);
        viewSendBuf.set(this.headBufView);
        this.myFile = file;
        var SfileName = file.name;
        var fileNameLen = new Int16Array(sendArrBuf, 8, 1);
        fileNameLen[0] = SfileName.length;
        var len = fileNameLen[0];
        var fileName = new Int16Array(sendArrBuf, 10, len);
        for (var i = 0; i < len; i++) {
            fileName[i] = SfileName.charCodeAt(i);
        }
        var fileSize = file.size;
        var sFileSize = fileSize.toString();
        var fileLen = sFileSize.length;
        var fileSizeBuf = new Int8Array(sendArrBuf, 10 + len * 2, 20);//不用IntArray32是因为Int32的最大长度无法满足大文件
        for (var i = 20 - fileLen, j = 0; i < 20; i++, j++) {//这个用于将文件的长度从后面开始写入
            fileSizeBuf[i] = sFileSize[j];
        }
        this.send(sendArrBuf);
    },
    setFileBody: function (fileIdBuf, sendFileBuf) {//文件体
        var sendData;
        var sendFileView = new Int8Array(sendFileBuf);
        sendData = new ArrayBuffer(sendFileBuf.byteLength + DocWsk.headBuf.byteLength + 40);
        var sendFileId = new Int8Array(fileIdBuf);
        var sendOffset = new Int32Array(sendData, 40, 1);
        var sendLen = new Int32Array(sendData, 44, 1);
        sendOffset[0] = DocWsk.fileOffset;
        sendLen[0] = 1000000;
        var sendView = new Int8Array(sendData);
        DocWsk.setPackHead(4294967213, 456, DocWsk.fileOffset, 3);
        sendView.set(DocWsk.headBufView, 0);
        sendView.set(sendFileId, 8);
        sendView.set(sendFileView, 48);
        webSocket.send(sendView);
    },
    send: function (dataBuffer) {
        webSocket.send(dataBuffer);
    },
    close: function () {
        webSocket.close();
    }
};

$(document).ready(function () {
    DocWsk.Init();
});

function wsFileObjFun() {
    var wsFileObj = null;
    var wsFileUuid = null;
}

function onerror(event) {
    console("Connect Error");
}

function onOpen(event) {
    console.log("open Websocket");
}

function onMessage(event) {
    var receBuffer;
    receBuffer = event.data;
    var msg = new Int8Array(receBuffer);
    var str1 = msg[8];
    if (msg[7] === 2) {//文件信息应答
        ydWenJianXinXi();
    } else if (msg[7] === 4) {//文件体应答
        if (str1 === 0) {//文件包体写入成功
            reCount = 0;
            ydWenJianTi4ok(receBuffer);
        } else if (str1 === -1) {//失败，重发三次
            if(reCount >= 3){
                reCount = 0;
                uploadNextFile(false);
            }else{
                reCount++;            
                ydWenJianTi4ShiBai(receBuffer);
            }
        } else if (str1 === 1) {//整个文件成功
            uploadNextFile(true);
        } else if (str1 === 2) {//文件校验失败
            console.log("文件校验失败");
            uploadNextFile(false);
        }
    }
}

function uploadNextFile(flag){
    var fileObjList = DocWsk.fileList[0];
    var j = {};
    j.bh = fileObjList.wsFileUuid;
    $.dfAjax({
        url: "/wj/wcscWj.do",
        data: j,
        fun: function (data) {
        }
    });
    $("#tblFileUpWc").append("<tr><td style='width:62%;'>" + fileObjList.wsFileObj.name + "</td><td style='width:20%;text-align:right;'>" + (fileObjList.wsFileObj.size / 1048576).toFixed(2) + (flag? "M</td><td style='color:#0000ff;text-align:center;'>完成</td></tr>" : "M</td><td style='color:#FF0000;text-align:center;'>失败</td></tr>"));
    $("#tblFileUp tr:eq(1)").remove();
    DocWsk.fileOffset = 0;
    DocWsk.fileList.splice(0, 1);
    start();
}

function ydWenJianTi4ok(receBuffer) {//文件体应答
    var uuidBuf = receBuffer.slice(9, 41);//w文件uuid
    var pylBuf = receBuffer.slice(41, 45);
    var pyl = new Int32Array(pylBuf);
    DocWsk.fileOffset = parseInt(pyl[0]) + 1;
    readSendFile(uuidBuf);
}

function ydWenJianTi4ShiBai(receBuffer) {//文件体应答
    var uuidBuf = receBuffer.slice(9, 41);//w文件uuid
    var pylBuf = receBuffer.slice(41, 45);
    var pyl = new Int32Array(pylBuf);
    DocWsk.fileOffset = pyl;
    readSendFile(uuidBuf);
}

function ydWenJianXinXi() {//文件信息应答
    var uuidBuf = new Int8Array(new ArrayBuffer(32));
    for (var i = 0; i < DocWsk.fileList[0].wsFileUuid.length; i++) {
        uuidBuf[i] = DocWsk.fileList[0].wsFileUuid.charCodeAt(i);  //以10进制的整数返回 某个字符 的unicode编码
    }
//  DocWsk.fileUuid = String.fromCharCode.apply(null, new Uint8Array(uuidBuf));//如果后台生成uuid则用这句
    DocWsk.fileUuid = DocWsk.fileList[0].wsFileUuid;
    readSendFile(uuidBuf);
}

function start() {
    var sendArrBuf = new ArrayBuffer(1024 * 2);
    if (DocWsk.fileOffset === 0 && DocWsk.fileList.length > 0) {
        DocWsk.setPackHead(DocWsk.fileList[0].wsFileObj.size, 456, 0, 1);
        DocWsk.setFileInfo(sendArrBuf, DocWsk.fileList[0].wsFileObj, "");
    }
}

function readSendFile(uuidBuf) {
    upUuidBuf = uuidBuf;
    if (ztUpFlag || uuidBuf === null) {
        return;
    }
    upUuidBuf = null;
    var reader = new FileReader();
    blobSlice = File.prototype.mozSlice || File.prototype.webkitSlice || File.prototype.slice;
    var sendSize = 1000000, end = DocWsk.fileOffset * sendSize + sendSize >= DocWsk.myFile.size ? DocWsk.myFile.size : DocWsk.fileOffset * sendSize + sendSize;
    var sendFileBuf;

    reader.readAsArrayBuffer(blobSlice.call(DocWsk.myFile, DocWsk.fileOffset * sendSize, end));
    reader.onload = function (e) {
        sendFileBuf = this.result;
        $("#tblFileUp tr:eq(1) td:eq(2)").html(((end / DocWsk.myFile.size) * 100).toFixed(2) + "%");
        DocWsk.setFileBody(uuidBuf, sendFileBuf);
    };
}

function selectFile(files) {
    fileObj = files[0];
}

function addFile(fileUuid) {
    $("#fmUpload").show();
    if (fileObj !== undefined && fileObj !== null) {
        var fileObjList = new wsFileObjFun();
        fileObjList.wsFileObj = fileObj;
        fileObjList.wsFileUuid = fileUuid;
        DocWsk.fileList.push(fileObjList);
        $("#tblFileUp").append("<tr><td>" + fileObj.name + "</td><td style='text-align:right;'>" + (fileObj.size / 1048576).toFixed(2) + "M</td><td style='text-align:center;'>等待上传</td></tr>");
        $("#tblFileUp").find("tr").mousedown(function () {
            seqUpload = $(this).prevAll().length - 1;
        });
        fileObj = null;
        start();
    }
}

function qxUpload() {
    if (DocWsk.fileList[seqUpload] === undefined || DocWsk.fileList[seqUpload] === null) {
        return;
    }
    var dl = DocWsk.fileList[seqUpload];
    if (!confirm("确定取消下载文件：" + dl.wsFileObj.name + "?")) {
        return;
    }
    var j = {};
    j.bh = dl.wsFileUuid;
    $.dfAjax({
        url: "/wj/qxscWj.do",
        data: j,
        fun: function (data) {
        }
    });
    ztUpFlag = seqUpload === 0;
    $("#tblFileUp tr:eq(" + (seqUpload + 1) + ")").remove();
    DocWsk.fileList.splice(seqUpload, 1);
    setTimeout(function () {
        if (ztUpFlag) {
            ztUpFlag = false;
            DocWsk.fileOffset = 0;
            start();
        } else {
            readSendFile(upUuidBuf);
        }
    }, 500);
}