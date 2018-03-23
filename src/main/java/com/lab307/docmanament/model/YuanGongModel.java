package com.lab307.docmanament.model;

import java.util.Calendar;
import java.util.Date;

public class YuanGongModel {
	private String bh;
	private String qybh;
	private String mc;
	private String dm;
	private String xb;
	private String sfzh;
	private String sjhm;
        private Calendar rzsj;
	private int zt;
	
	public YuanGongModel(){
		
	}

	/**
	 * @return the bh
	 */
	public String getBh() {
		return bh;
	}

	/**
	 * @param bh the bh to set
	 */
	public void setBh(String bh) {
		this.bh = bh;
	}

	/**
	 * @return the qybh
	 */
	public String getQybh() {
		return qybh;
	}

	/**
	 * @param qybh the qybh to set
	 */
	public void setQybh(String qybh) {
		this.qybh = qybh;
	}

	/**
	 * @return the mc
	 */
	public String getMc() {
		return mc;
	}

	/**
	 * @param mc the mc to set
	 */
	public void setMc(String mc) {
		this.mc = mc;
	}

	/**
	 * @return the dm
	 */
	public String getDm() {
		return dm;
	}

	/**
	 * @param dm the dm to set
	 */
	public void setDm(String dm) {
		this.dm = dm;
	}

	/**
	 * @return the xb
	 */
	public String getXb() {
		return xb;
	}

	/**
	 * @param xb the xb to set
	 */
	public void setXb(String xb) {
		this.xb = xb;
	}

	/**
	 * @return the sfzh
	 */
	public String getSfzh() {
		return sfzh;
	}

	/**
	 * @param sfzh the sfzh to set
	 */
	public void setSfzh(String sfzh) {
		this.sfzh = sfzh;
	}

	/**
	 * @return the sjhm
	 */
	public String getSjhm() {
		return sjhm;
	}

	/**
	 * @param sjhm the sjhm to set
	 */
	public void setSjhm(String sjhm) {
		this.sjhm = sjhm;
	}

        public Calendar getRzsj() {
            return rzsj;
        }

        public void setRzsj(Calendar rzsj) {
            this.rzsj = rzsj;
        }

	/**
	 * @return the zt
	 */
	public int getZt() {
		return zt;
	}

	/**
	 * @param zt the zt to set
	 */
	public void setZt(int zt) {
		this.zt = zt;
	}

}