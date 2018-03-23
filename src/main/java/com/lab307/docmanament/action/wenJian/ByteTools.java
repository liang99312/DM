/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lab307.docmanament.action.wenJian;

/**
 *
 * @author Administrator
 */
public class ByteTools {

    public static long unsigned4Bytes2Int(byte[] buf, int pos) {
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

    //将byte转换为int16
    public static int byte2int(byte[] res, int offset) {
        int targets = (res[offset] & 0xff) | ((res[offset + 1] << 8) & 0xff00);
        return targets;
    }

    public static byte[] int2Bytes(int i) {
//		int i = 65535;
        byte[] a = new byte[4];
        a[0] = (byte) (0xff & i);
        a[1] = (byte) ((0xff00 & i) >> 8);
        a[2] = (byte) ((0xff0000 & i) >> 16);
        a[3] = (byte) ((0xff000000 & i) >> 24);
        return a;
    }

    public static String getMyFileName(byte[] b, int offset, int end) {//byte to String 
        int len = end - offset;
        char[] a = new char[len];
        String fileName = "";
        for (int i = 0, j = 0; i < len * 2; i++, j++) {
            a[j] = (char) byte2int(b, offset + i);
            i++;
        }
        fileName = String.valueOf(a);
        return fileName;
    }

    //获取文件大小
    public static long getFileSize(byte[] b, int offset, int end) {
        int len = end - offset;
        String s = "";
        long l = 0L;
        for (int i = 0; i < len; i++) {
            s = s + b[i + offset];
        }
        l = Long.parseLong(s);
        return l;
    }
}
