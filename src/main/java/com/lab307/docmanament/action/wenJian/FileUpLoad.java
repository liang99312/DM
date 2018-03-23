package com.lab307.docmanament.action.wenJian;

import java.io.File;
import javax.annotation.Resource;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import javax.servlet.http.HttpSession;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.OnError;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.ServerEndpointConfig;
import org.apache.log4j.Logger;
import java.util.UUID;

import javax.websocket.EndpointConfig;

/**
 *
 * @author HuangXY
 */
@ServerEndpoint(value = "/docMgmtWsk", configurator = HttpSessionConf.class)
public class FileUpLoad extends ServerEndpointConfig.Configurator {

    @Resource(name = "WenJian")
    WenJian wj;

    private String qybh;
    protected Logger logger = Logger.getLogger(this.getClass());
    String fileId = "";
    String fileName = "";
    String filePath = "";

    ShuJuJieXi shuJu = new ShuJuJieXi();
    ResponseMsg res = new ResponseMsg();

    @OnOpen
    public void open(Session session, EndpointConfig config) {
        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        qybh = (String) httpSession.getAttribute("qybh");
        filePath = wj.getFileDir((String) this.qybh);
        File file = new File(filePath);
        if (!file.exists() && !file.isDirectory()) {
            file.mkdir();
        }
        session.setMaxBinaryMessageBufferSize(1024 * 1024);
    }

    @OnMessage
    public byte[] receiveMessage(byte[] b) {
        byte[] msg = null;
        try {
            shuJu.Init(b);
            switch (shuJu.getHeadMsgType()) {
                //文件信息包
                case 1: {
                    fileName = shuJu.getFileInfoName();
                    logger.error("文件大小为：" + shuJu.getFileInfoSize());
                    fileId = this.getUuid();//uuid前台传输过来了，这个生成的uuid会被前台覆盖
                    res.setHead(shuJu.getHeadLen(), (short) shuJu.getHeadVerify(), (byte) shuJu.getHeadSendID(), (byte) 2);
                    res.setFileInfoRes((byte) 0, fileId, "");
                    byte[] resHeadBytes = res.getSendHead();
                    byte[] resInfoBytes = res.getFileInfoRes();
                    byte[] resBytes = new byte[resInfoBytes.length + 8];
                    System.arraycopy(resHeadBytes, 0, resBytes, 0, 8);
                    System.arraycopy(resInfoBytes, 0, resBytes, 8, resInfoBytes.length);
                    msg = resInfoBytes;
                }
                break;
                case 3: {//文件体
                    byte[] fileBytes = new byte[b.length - 48];
                    System.arraycopy(b, 48, fileBytes, 0, fileBytes.length);

                    if (shuJu.getHeadSendID() == 0) {//第一个包发送过来的时候确定uuid
                        fileId = shuJu.getFileID();
                    }
                    File Writefile = new File(filePath + shuJu.getFileID());
                    if (WriteFile(fileBytes, filePath + shuJu.getFileID())) {//成功，则返回成功
                        res.setHead(shuJu.getHeadLen(), (short) shuJu.getHeadVerify(), (byte) shuJu.getHeadSendID(), (byte) 4);
                        if (Writefile.length() == shuJu.getFileInfoSize()) {//传完s
                            res.setFileBodyRes(1, fileId, shuJu.getFileOffset(), (byte) 0, "");
                        } else if (filePath.length() < shuJu.getFileInfoSize()) {//未传完
                            res.setFileBodyRes(0, fileId, shuJu.getFileOffset(), (byte) 0, "");
                        }

                        byte[] resHeadBytes = res.getSendHead();
                        byte[] resBodyBytes = res.getFileBodyRes();
                        byte[] resBytes = new byte[resBodyBytes.length];
                        System.arraycopy(resBodyBytes, 0, resBytes, 0, resBodyBytes.length);
                        msg = resBytes;
                    } else {//失败，则返回失败让其重传
                        res.setHead(shuJu.getHeadLen(), (short) shuJu.getHeadVerify(), (byte) shuJu.getHeadSendID(), (byte) 4);
                        res.setFileBodyRes(-1, fileId, shuJu.getFileOffset(), (byte) "write file error".length(), "write file error");
                        byte[] resHeadBytes = res.getSendHead();
                        byte[] resBodyBytes = res.getFileBodyRes();
                        byte[] resBytes = new byte[resBodyBytes.length];
                        System.arraycopy(resBodyBytes, 0, resBytes, 0, resBodyBytes.length);
                        msg = resBytes;
                    }
                }
                break;
                default:
                    logger.error("文件信息类别出错:" + shuJu.getHeadMsgType());
                    break;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("接收到客户端发送来的信息出错啦！");

        }
        return msg;
    }

    @OnClose
    public void close() {
        logger.error("关闭websocket！");
    }

    @OnError
    public void error(Throwable t) {
        logger.error("websocket error:"+t.getMessage());
    }

    public boolean WriteFile(byte[] fileByte, String filePath) {
        FileOutputStream out = null;
        boolean result = false;
        try {
            out = new FileOutputStream(filePath, true);
            out.write(fileByte);
            out.close();
            result = true;
        } catch (IOException ex) {
            logger.error("写入文件操作失败");
            java.util.logging.Logger.getLogger(FileUpLoad.class.getName()).log(Level.SEVERE, null, ex);
            result = false;
        }finally{
            try{
                if(out != null){
                    out.close();
                }
            }catch(IOException ex){
                java.util.logging.Logger.getLogger(FileUpLoad.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    public String getUuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
