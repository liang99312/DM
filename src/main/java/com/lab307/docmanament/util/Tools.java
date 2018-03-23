package com.lab307.docmanament.util;

import net.sourceforge.pinyin4j.PinyinHelper;

public class Tools {
	public static String getPinYinDaiMa(String mingCheng){
		if ((mingCheng==null)||(mingCheng.length()==0)) return "";
		String daiMa = "";
		for (int i=0, len=mingCheng.length(); i<len; i++){
			char code = mingCheng.charAt(i);
			String[] pinYin = PinyinHelper.toHanyuPinyinStringArray(code);
			if (pinYin==null){
				if ((code>='a'&&code<='z')||(code>='A'&&code<='Z')||(code>='0'&&code<='9')) daiMa += code;
			}else{
				daiMa += pinYin[0].charAt(0);
			}
		}
		int len = (daiMa.length()>6) ? 6 : daiMa.length();
		return daiMa.substring(0, len).toLowerCase();
	}
	
	public static void main(String args[]) {
	}
}