/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lab307.docmanament.action.wenJian;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.ServerEndpointConfig;
import org.apache.log4j.Logger;

/**
 *
 * @author DengFeng
 */
@ServerEndpoint(value = "/docMgmtWskDownLoad", configurator = HttpSessionConf.class)
public class FileDownLoad extends ServerEndpointConfig.Configurator {

    @Resource(name = "WenJian")
    WenJian wj;

    private String qybh;
    private Session session;
    protected Logger logger = Logger.getLogger(this.getClass());
    private int readLen = 1024 * 100;

    @OnOpen
    public void open(Session session, EndpointConfig config) {
        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        logger.error("下载的 httpSession为:" + httpSession);
        qybh = (String) httpSession.getAttribute("qybh");
        this.session = session;
    }

    @OnMessage
    public byte[] receiveMessage(byte[] b) {
        byte[] fileByte = null;
        byte retByte[] = null;
        String fileID = "";
        int readOfs = 0;
        byte[] fileIdBytes = new byte[32];
        if (b[0] == 1) {
            try {
                //文件信息请求
                System.arraycopy(b, 1, fileIdBytes, 0, fileIdBytes.length);
                fileID = new String(fileIdBytes, "UTF-8");
                readOfs = (int) unsigned4Bytes2Int(b, 36);
                fileByte = readFile(wj.getFileDir(qybh) + fileID, readLen, readOfs);
                if (fileByte != null) {//读取文件成功
                    if (fileByte[0] < 0) {
                        retByte = fitByte(fileByte[0], fileIdBytes, fileByte, 1);
                    } else {
                        retByte = fitByte((byte) 0, fileIdBytes, fileByte, 0);
                    }
                } else {//读取文件失败
                    logger.error("读取文件出现异常,但不是找不到文件,总长度为：" + fileByte.length);
                    retByte = fitByte((byte) -1, fileIdBytes, "can not read the file".getBytes(), 0);
                }
            } catch (UnsupportedEncodingException ex) {
                java.util.logging.Logger.getLogger(FileDownLoad.class.getName()).log(Level.SEVERE, null, ex);
                retByte = fitByte((byte) -1, fileIdBytes, "deal with data error".getBytes(), 0);
            }
        } else if (b[0] == -1) {
            retByte = fitByte((byte) -1, fileIdBytes, "download cancel by user".getBytes(), 0);
        } else {
            logger.error("没有该请求");
        }
        return retByte;
    }

    public byte[] fitByte(byte result, byte[] fileIdBytes, byte[] data, int index) {
        byte retByte[] = null;
        retByte = new byte[data.length - index + 33];
        retByte[0] = result;
        System.arraycopy(fileIdBytes, 0, retByte, 1, fileIdBytes.length);
        System.arraycopy(data, index, retByte, 33, data.length - index);
        return retByte;
    }

    public byte[] readFile(String filePath, int readLen, int offset) {
        long totalOfs = 0;//总共多少片
        String fileSize = null;
        byte[] fileByte = null;
        byte[] retByte = null;
        FileInputStream in = null;
        try {
            File f = new File(filePath);
            if (!f.exists()) {
                logger.error("找不到文件");
                String errMsg = "can not find the file";
                retByte = new byte[errMsg.getBytes().length + 1];
                retByte[0] = -2;
                System.arraycopy(errMsg.getBytes(), 0, retByte, 1, errMsg.getBytes().length);
            } else {
                totalOfs = (f.length() % readLen) == 0 ? f.length() / readLen : f.length() / readLen + 1;
                if (offset == (totalOfs - 1)) {//判断是否是最后一片
                    fileByte = new byte[(int) (f.length() - offset * readLen)];
                    retByte = new byte[fileByte.length + 25];
                    retByte[0] = 2;
                } else {
                    fileByte = new byte[readLen];
                    retByte = new byte[fileByte.length + 25];
                    retByte[0] = 1;
                }

                in = new FileInputStream(f);
                in.skip(offset * readLen);
                if (in.read(fileByte) != -1) {//看文件是否读完
                    System.arraycopy(fileByte, 0, retByte, 25, fileByte.length);
                    fileSize = Long.toString(offset * readLen);//将文件字节偏移量转换为字符串
                    System.arraycopy(fileSize.getBytes(), 0, retByte, 1, fileSize.getBytes().length);
                    System.arraycopy(int2Bytes(fileByte.length), 0, retByte, 21, 4);
                }
            }
        } catch (Exception e) {
            logger.error("文件读取抛出异常:" + e.getMessage());
            String errMsg = "read the file error";
            retByte = new byte[errMsg.getBytes().length + 1];
            retByte[0] = -1;
            System.arraycopy(errMsg.getBytes(), 0, retByte, 1, errMsg.getBytes().length);
            fileByte = null;
        }finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(FileDownLoad.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return retByte;
    }

    public byte[] int2Bytes(long i) {
        byte[] a = new byte[4];
        a[0] = (byte) (0xff & i);
        a[1] = (byte) ((0xff00 & i) >> 8);
        a[2] = (byte) ((0xff0000 & i) >> 16);
        a[3] = (byte) ((0xff000000 & i) >> 24);
        return a;
    }

    public long unsigned4Bytes2Int(byte[] buf, int pos) {
        int firstByte = 0;
        int secondByte = 0;
        int thirdByte = 0;
        int fourthByte = 0;
        int index = pos;
        firstByte = (0x000000FF & ((int) buf[index + 3]));  //等价于：256+(int) buf[index]
        secondByte = (0x000000FF & ((int) buf[index + 2]));
        thirdByte = (0x000000FF & ((int) buf[index + 1]));
        fourthByte = (0x000000FF & ((int) buf[index]));
        return ((long) (firstByte << 24 | secondByte << 16 | thirdByte << 8 | fourthByte)) & 0xFFFFFFFFL;
        //强制转换为long类型，防止高32位丢失
    }

    @OnClose
    public void closeDown() {
        logger.error("关闭websocket！");
    }

    @OnError
    public void errorDown(Throwable t) {
        logger.error("websocket error:" + t.getMessage());
    }
}
