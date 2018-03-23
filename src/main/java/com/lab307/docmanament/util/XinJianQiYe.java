package com.lab307.docmanament.util;

import com.lab307.docmanament.model.YuanGongModel;
import com.lab307.docmanament.model.YuanGongJueSeModel;
import com.lab307.docmanament.model.QiYeModel;
import com.lab307.docmanament.model.DengLuModel;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import javax.annotation.Resource;

public class XinJianQiYe {
	@Resource(name = "DocManamentDB")
	private DocManamentDB dao;
	private QiYeModel qy;
	
	public void zjQiYe(){
		qy = new QiYeModel();
		qy.setBh(dao.getUuid());
		qy.setMc("XXXX公司");
		dao.insert(qy);
                
                zjYuanGong(qy.getBh());
	}
	
	private String zjYuanGong(String qybh){
		YuanGongModel yg = new YuanGongModel();
		yg.setBh(dao.getUuid());
		yg.setMc("李四");
		yg.setQybh(qybh);
		yg.setSfzh("18178292679");
		yg.setSjhm("18178292679");
		yg.setXb("男");
		yg.setZt(99);
                dao.insert(yg);

		zjDengLu(yg.getSjhm(), yg.getBh());

		return yg.getBh();
	}
	
	public void zjDengLu(String sjhm, String ygbh){
		DengLuModel dl = new DengLuModel();
		dl.setDlm(sjhm);
		dl.setDlmm("123456");
		dl.setYgbh(ygbh);
                
                YuanGongJueSeModel js = new YuanGongJueSeModel();
                js.setBh(dao.getUuid());
                js.setJs("系统管理员");
                js.setJsdm("101");
                js.setYgbh(ygbh);
		
		dao.insert(dl);
                dao.insert(js);
	}
	
	public static void main(String[] args){
		ApplicationContext context = new FileSystemXmlApplicationContext("src/java/com/lab307/docmanament/util/ClientAppContext.xml");
		XinJianQiYe xqy = (XinJianQiYe)context.getBean("XinJianQiYe");
		//xqy.testBB();
		xqy.zjQiYe();
	}

}