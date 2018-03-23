/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lab307.docmanament.action.wenJianFenLei;

/**
 *
 * @author Administrator
 */
import com.dengfeng.std.BusinessException;
import com.dengfeng.std.DengFengAction;
import javax.annotation.Resource;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;

@Controller
public class WenJianFenLeiAction extends DengFengAction {

    @Resource(name = "WenJianFenLei")
    WenJianFenLei wjfl;

    public String wenJianFenLei() {
        return this.feiFa ? "login" : "success";
    }

    public String cxWenJianFenLei() {
        if (this.feiFa) {
            return reLogin();
        }
        if (this.jsonObj == null) {
            return chuCuoFanHui(DATA_NONE);
        }
        JSONObject result;
        String wjflmc = this.jsonObj.optString("mc", "");
        int zt = this.jsonObj.optInt("zt", 1);
        int yx = this.jsonObj.optInt("yx", 0);
        result = this.wjfl.cxWenJianFenLei(this.qybh, wjflmc, zt, yx, MAXRESULTS);
        renderJSON(result);
        return null;
    }

    public String quWenJianFenLeiZiLiao() {
        if (this.feiFa) {
            return reLogin();
        }
        if (this.jsonObj == null) {
            return chuCuoFanHui(DATA_NONE);
        }
        String bh = this.jsonObj.optString("bh", "");
        JSONObject result;
        if (bh.length() == 32) {
            result = this.wjfl.quWenJianFenLeiZiLiao(this.qybh, bh);
        } else {
            return chuCuoFanHui("分类编号错误");
        }
        renderJSON(result);
        return null;
    }

    public String zjWenJianFenLei() {
        if (this.feiFa) {
            return reLogin();
        }
        if (this.jsonObj == null) {
            return chuCuoFanHui(DATA_NONE);
        }
        String mc = this.jsonObj.optString("mc", "");
        if (mc.length() < 2) {
            return chuCuoFanHui("分类名称长度不够");
        }
        String dm = this.jsonObj.optString("dm", "");
        String bz = this.jsonObj.optString("bz", "");
        String jb = this.jsonObj.optString("jb", "");
        JSONObject result;
        try {
            result = this.wjfl.zjWenJianFenLei(this.qybh, mc, dm, jb, bz);
        } catch (BusinessException ex) {
            result = getErrResult(ex.getMessage());
        }
        renderJSON(result);
        return null;
    }

    public String xgWenJianFenLei() {
        if (this.feiFa) {
            return reLogin();
        }
        if (this.jsonObj == null) {
            return chuCuoFanHui(DATA_NONE);
        }
        String bh = this.jsonObj.optString("bh", "");
        if (bh.length() != 32) {
            return chuCuoFanHui("该分类编号出错");
        }
        String mc = this.jsonObj.optString("mc", "");
        if (mc.length() < 2) {
            return chuCuoFanHui("分类名称长度不够");
        }
        String dm = this.jsonObj.optString("dm", "");
        String bz = this.jsonObj.optString("bz", "");
        String jb = this.jsonObj.optString("jb", "");
        JSONObject result = this.wjfl.xgWenJianFenLei(this.qybh, bh, mc, dm, jb, bz);
        renderJSON(result);
        return null;
    }

    public String xgWenJianFenLeiQuanXian() {
        if (this.feiFa) {
            return reLogin();
        }
        if (this.jsonObj == null) {
            return chuCuoFanHui(DATA_NONE);
        }
        String bh = this.jsonObj.optString("bh", "");
        if (bh.length() != 32) {
            return chuCuoFanHui("该分类编号出错");
        }
        JSONArray qx = this.jsonObj.getJSONArray("qx");
        if ((qx == null) || (qx.isEmpty())) {
            return chuCuoFanHui("请设置文件分类权限");
        }
        JSONObject result;
        try {
            result = this.wjfl.xgWenJianFenLeiQuanXian(bh, this.qybh, qx);
        } catch (BusinessException ex) {
            result = getErrResult(ex.getMessage());
        }
        renderJSON(result);
        return null;
    }

    public String scWenJianFenLei() {
        if (this.feiFa) {
            return reLogin();
        }
        if (this.jsonObj == null) {
            return chuCuoFanHui(DATA_NONE);
        }
        String bh = this.jsonObj.optString("bh", "");
        if (bh.length() != 32) {
            return chuCuoFanHui("该分类编号出错");
        }
        JSONObject result = this.wjfl.scWenJianFenLei(this.qybh, bh);
        renderJSON(result);
        return null;
    }

    public String qlWenJianFenLei() {
        if (this.feiFa) {
            return reLogin();
        }
        if (this.jsonObj == null) {
            return chuCuoFanHui(DATA_NONE);
        }
        String bh = this.jsonObj.optString("bh", "");
        if (bh.length() != 32) {
            return chuCuoFanHui("该分类编号出错");
        }
        JSONObject result = this.wjfl.qlWenJianFenLei(this.qybh, bh);
        renderJSON(result);
        return null;
    }

    public String cxWenJianFenLeiZiDian() {
        if (this.feiFa) {
            return reLogin();
        }
        JSONObject result = this.wjfl.cxWenJianFenLeiZiDian(this.qybh);
        renderJSON(result);
        return null;
    }

    public String cxShouQuanWenJianFen() {
        if (this.feiFa) {
            return reLogin();
        }
        JSONObject result = this.wjfl.cxShouQuanWenJianFen(this.qybh, this.ygbh, request.getSession());
        renderJSON(result);
        return null;
    }
}
