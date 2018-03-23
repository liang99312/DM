package com.lab307.docmanament.action.wenJian;

import java.io.UnsupportedEncodingException;

public class ResponseMsg {

    private byte[] sendFileInfoMsg;
    private byte[] sendFileBodyMsg;
    private byte[] sendHead = new byte[8];

    private myTools tool = new myTools();

    public void setHead(long Len, short Verify, byte SendID, byte MsgType) {//设置要返回的头部
        System.arraycopy(tool.int2Bytes(Len), 0, this.sendHead, 0, 4);
        System.arraycopy(tool.short2Byte(Verify), 0, this.sendHead, 4, 2);
        this.sendHead[6] = SendID;
        this.sendHead[7] = MsgType;
    }

    public void setFileInfoRes(byte result, String ID,/*byte Len,*/ String errMsg) {//设置文件信息报传送时返回的包
        if (result == 0) {
            byte[] byteID = null;
            try {
                byteID = ID.getBytes("utf-8");
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
            this.sendFileInfoMsg = new byte[byteID.length + 1 + 8];
            System.arraycopy(this.sendHead, 0, this.sendFileInfoMsg, 0, 8);
            this.sendFileInfoMsg[8] = result;//表示成功
            System.arraycopy(byteID, 0, this.sendFileInfoMsg, 9, byteID.length);//将b数组的数据拷贝到sendMsg数组中去
        } else if (result == -1) {
            byte[] errByteMsg = null;
            char[] errMsgArr = errMsg.toCharArray();
            errByteMsg = tool.chars2Bytes(errMsgArr);
            this.sendFileInfoMsg = new byte[errByteMsg.length + 2 + 8];
            System.arraycopy(this.sendHead, 0, this.sendFileInfoMsg, 0, 8);
            this.sendFileInfoMsg[8] = result;
            this.sendFileBodyMsg[9] = (byte) errByteMsg.length;//这句新增的
            System.arraycopy(errByteMsg, 0, this.sendFileInfoMsg, 10, errByteMsg.length);
        }
    }

    public void setFileBodyRes(int result, String fileID, int offset, byte Len, String errMsg) {
        switch (result) {
            //执行成功
            case 0: {
                byte[] byteID = null;
                byte[] byteOffset = tool.int2Bytes(offset);
                this.sendFileBodyMsg = new byte[45];
                System.arraycopy(this.sendHead, 0, this.sendFileBodyMsg, 0, 8);
                this.sendFileBodyMsg[8] = (byte) result;
                try {
                    byteID = fileID.getBytes("utf-8");
                    System.arraycopy(byteID, 0, this.sendFileBodyMsg, 9, byteID.length);
                    System.arraycopy(byteOffset, 0, this.sendFileBodyMsg, byteID.length + 9, byteOffset.length);
                } catch (UnsupportedEncodingException ex) {
                    ex.printStackTrace();
                }
            }
            break;
            //执行失败
            case -1: {
                byte[] byteID = null;
                byte[] errByteMsg = null;
                char[] errMsgArr = errMsg.toCharArray();
                byte[] byteOffset = tool.int2Bytes(offset);
                errByteMsg = tool.chars2Bytes(errMsgArr);
                this.sendFileBodyMsg = new byte[46 + errByteMsg.length];
                this.sendFileBodyMsg[8] = -1;
                try {
                    byteID = fileID.getBytes("utf-8");
                    System.arraycopy(this.sendHead, 0, this.sendFileBodyMsg, 0, 8);
                    System.arraycopy(byteID, 0, this.sendFileBodyMsg, 9, byteID.length);
                    System.arraycopy(byteOffset, 0, this.sendFileBodyMsg, byteID.length + 9, byteOffset.length);
                    this.sendFileBodyMsg[byteID.length + 13] = (byte) errByteMsg.length;
                    System.arraycopy(errByteMsg, 0, this.sendFileBodyMsg, 46, errByteMsg.length);
                } catch (UnsupportedEncodingException ex) {
                    ex.printStackTrace();
                }
            }
            break;
            //整个文件成功
            case 1: {
                byte[] byteID = null;
                byte[] byteOffset = tool.int2Bytes(offset);
                this.sendFileBodyMsg = new byte[45];
                this.sendFileBodyMsg[8] = (byte) result;
                try {
                    byteID = fileID.getBytes("utf-8");
                    System.arraycopy(this.sendHead, 0, this.sendFileBodyMsg, 0, 8);
                    System.arraycopy(byteID, 0, this.sendFileBodyMsg, 9, byteID.length);
                    System.arraycopy(byteOffset, 0, this.sendFileBodyMsg, byteID.length + 9, byteOffset.length);
                } catch (UnsupportedEncodingException ex) {
                    ex.printStackTrace();
                }
            }
            break;
            //文件校验失败
            case 2: {
                byte[] byteID = null;
                byte[] byteOffset = tool.int2Bytes(offset);
                this.sendFileBodyMsg = new byte[37];
                this.sendFileBodyMsg[0] = (byte) result;
                try {
                    byteID = fileID.getBytes("utf-8");
                    System.arraycopy(byteID, 0, this.sendFileBodyMsg, 1, byteID.length);
                    System.arraycopy(byteOffset, 0, this.sendFileBodyMsg, byteID.length + 1, byteOffset.length);
                } catch (UnsupportedEncodingException ex) {
                    ex.printStackTrace();
                }
            }
            break;
            default:
                break;
        }
    }

    public byte[] getSendHead() {
        return this.sendHead;
    }

    public byte[] getFileInfoRes() {
        return this.sendFileInfoMsg;
    }

    public byte[] getFileBodyRes() {
        return this.sendFileBodyMsg;
    }

    class myTools {

        public byte[] chars2Bytes(char[] c) {
            byte[] bs = new byte[(c.length) * 2];
            for (int i = 0; i < c.length; i++) {
                bs[2 * i + 1] = (byte) ((c[i] & 0xFF00) >> 8);
                bs[2 * i] = (byte) (c[i] & 0xFF);
            }
            return bs;
        }

        public byte[] int2Bytes(long i) {
            byte[] a = new byte[4];
            a[0] = (byte) (0xff & i);
            a[1] = (byte) ((0xff00 & i) >> 8);
            a[2] = (byte) ((0xff0000 & i) >> 16);
            a[3] = (byte) ((0xff000000 & i) >> 24);
            return a;
        }

        public byte[] short2Byte(short a) {
            byte[] b = new byte[2];
            b[0] = (byte) (a >> 8);
            b[1] = (byte) (a);
            return b;
        }
    }
}
