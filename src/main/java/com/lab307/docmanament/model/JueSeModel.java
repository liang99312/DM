/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lab307.docmanament.model;

/**
 *
 * @author Administrator
 */
public class JueSeModel {
    private String bh;
    private String dm;
    private String mc;
    
    public JueSeModel(){
        
    }
    
    public JueSeModel(String bh,String dm,String mc){
        this.bh = bh;
        this.dm = dm;
        this.mc = mc;
    }

    public String getBh() {
        return bh;
    }

    public void setBh(String bh) {
        this.bh = bh;
    }

    public String getDm() {
        return dm;
    }

    public void setDm(String dm) {
        this.dm = dm;
    }

    public String getMc() {
        return mc;
    }

    public void setMc(String mc) {
        this.mc = mc;
    }
    
}
