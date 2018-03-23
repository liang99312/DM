package com.lab307.docmanament.action.wenJian;

import java.io.UnsupportedEncodingException;
import org.apache.log4j.Logger;

public class ShuJuJieXi {

    protected Logger logger = Logger.getLogger(this.getClass());
    private long headLen = -1;//信息长度
    private int headVerify = -1;//包头识别码
    private int headSendID = -1;//包编号
    private int headMsgType = -1;//包信息类别
    private int fileInfoNameLen = -1;//文件名长度
    private String fileInfoName = "";//文件名
    private int fileInfoSize = -1;//文件大小
    private String fileID = "";//文件ID
    private int fileOffset = -1;//偏移量
    private int fileSliceLen = -1;//长度

    public boolean Init(byte[] revByteArr) {
        if (revByteArr == null) {
            return false;
        }
        headLen = ByteTools.unsigned4Bytes2Int(revByteArr, 0);
        headVerify = ByteTools.byte2int(revByteArr, 4);
        this.headSendID = revByteArr[6];
        this.headMsgType = revByteArr[7];
        switch (this.headMsgType) {
            case 1:
                this.fileInfoNameLen = ByteTools.byte2int(revByteArr, 8);
                this.fileInfoName = ByteTools.getMyFileName(revByteArr, 10, 10 + this.fileInfoNameLen);
                int fileNameOffset = 2 * this.fileInfoNameLen + 10;
                int fileNameEnd = fileNameOffset + 20;
                this.fileInfoSize = (int) ByteTools.getFileSize(revByteArr, fileNameOffset, fileNameEnd);
                break;
            case 3:
                try {
                    byte[] fileIdBytes = new byte[32];
                    System.arraycopy(revByteArr, 8, fileIdBytes, 0, fileIdBytes.length);
                    this.fileID = new String(fileIdBytes, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    logger.error("文件ID解析失败");
                }
                this.fileOffset = (int) ByteTools.unsigned4Bytes2Int(revByteArr, 40);
                this.fileSliceLen = (int) ByteTools.unsigned4Bytes2Int(revByteArr, 44);
                break;
            default:
                logger.error("传输的既不是文件信息也不是文件体，请修改源代码");
                return false;
        }
        return true;
    }

    public long getHeadLen() {
        return headLen;
    }

    public int getHeadVerify() {
        return headVerify;
    }

    public int getHeadSendID() {
        return headSendID;
    }

    public int getHeadMsgType() {
        return headMsgType;
    }

    public int getFileInfoNameLen() {
        return fileInfoNameLen;
    }

    public String getFileInfoName() {
        return fileInfoName;
    }

    public int getFileInfoSize() {
        return fileInfoSize;
    }

    public String getFileID() {
        return fileID;
    }

    public int getFileOffset() {
        return fileOffset;
    }

    public int getFileSliceLen() {
        return fileSliceLen;
    }
}
