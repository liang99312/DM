package com.lab307.docmanament.action.yuanGong;

import com.dengfeng.std.DengFengAction;
import com.lab307.docmanament.action.DocManamentActionInterface;
import static com.opensymphony.xwork2.Action.SUCCESS;
import javax.annotation.Resource;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;

@Controller
public class YuanGongAction extends DengFengAction implements DocManamentActionInterface {

    @Resource(name = "YuanGongProcess")
    private YuanGong yg;
    private String dlm;
    private String mm;
    private String hardwareId;

    public void setDlm(String dlm) {
        this.dlm = dlm;
    }

    public void setMm(String mm) {
        this.mm = mm;
    }

    public void setHardwareId(String hardwareId) {
        this.hardwareId = hardwareId;
    }

    public void dengLu() {
        if (jsonObj != null) {
            this.dlm = jsonObj.optString("dlm", "");
            this.mm = jsonObj.optString("mm", "");
        }

        JSONObject result = yg.dengLu(this.dlm, this.mm, request.getSession());

        if (result.getInt("result") == 0) {
            result.put("url", "/yg/zm.do");
        }

        renderJSON(result);
    }

    public String getQuanXian() {
        if (feiFa) {
            return reLogin();
        }
        if (jsonObj == null) {
            return chuCuoFanHui(DATA_NONE);
        }
        JSONObject result = yg.getQuanXian(this.qybh, this.ygbh);

        renderJSON(result);
        return null;
    }

    public String getYongHu() {
        if (feiFa) {
            return reLogin();
        }
        if (jsonObj == null) {
            return chuCuoFanHui(DATA_NONE);
        }
        JSONObject result = new JSONObject();
        result.put("result", 0);
        result.put("ygbh", request.getSession().getAttribute("ygbh"));
        result.put("mc", request.getSession().getAttribute("mc"));
        renderJSON(result);
        return null;
    }

    public String xgMiMa() {
        if (feiFa) {
            return reLogin();
        }
        if (jsonObj == null) {
            return chuCuoFanHui(DATA_NONE);
        }
        String ymm = jsonObj.optString("ymm", "");
        String xmm = jsonObj.optString("xmm", "");
        JSONObject result = yg.xgMiMa(this.qybh, this.ygbh, ymm, xmm);

        renderJSON(result);
        return null;
    }

    public String zhuoMian() {
        return feiFa ? RELOGIN : SUCCESS;
    }

    public String hengTiao() {
        return feiFa ? RELOGIN : SUCCESS;
    }

    public String caiDan() {
        return feiFa ? RELOGIN : SUCCESS;
    }

    public String miMa() {
        return feiFa ? RELOGIN : SUCCESS;
    }

    public String yuanGong() {
        return feiFa ? LOGIN : SUCCESS;
    }

    public String cxYuanGong() {
        if (feiFa) {
            return reLogin();
        }
        if (jsonObj == null) {
            return chuCuoFanHui(DATA_NONE);
        }

        String mc = jsonObj.optString("mc", "");
        String xb = jsonObj.optString("xb", "");
        String sfzh = jsonObj.optString("sfzh", "");
        String sjhm = jsonObj.optString("sjhm", "");
        int zt = jsonObj.optInt("zt", 1);
        int yx = jsonObj.optInt("yx", 0);
        JSONObject result = yg.cxYuanGong(this.qybh, mc, xb, sfzh, sjhm, zt, yx, DocManamentActionInterface.MAXRESULTS);

        renderJSON(result);
        return null;
    }

    public String quYuanGongZiLiao() {
        if (feiFa) {
            return reLogin();
        }
        if (jsonObj == null) {
            return chuCuoFanHui(DATA_NONE);
        }

        String bh = jsonObj.optString("ygbh", "");
        if (bh.length() != 32) {
            return chuCuoFanHui("员工编号不正确！");
        }
        JSONObject result = yg.quYuanGongZiLiao(this.qybh, bh);
        renderJSON(result);
        return null;
    }

    public String cxYuanGongZiDian() {
        if (feiFa) {
            return reLogin();
        }
        JSONObject result = yg.cxYuanGongZiDian(this.qybh);
        renderJSON(result);
        return null;
    }

    public String zjYuanGong() {
        if (feiFa) {
            return reLogin();
        }
        if (jsonObj == null) {
            return chuCuoFanHui(DATA_NONE);
        }

        String mc = jsonObj.optString("mc", "");
        if (mc.length() < 2) {
            return chuCuoFanHui("员工姓名不合规范！");
        }
        String xb = jsonObj.optString("xb", "");
        String sfzh = jsonObj.optString("sfzh", "");
        String sjhm = jsonObj.optString("sjhm", "");
        String dlm = jsonObj.optString("dlm", "");
        String dlmm = jsonObj.optString("dlmm", "");
        String rzsj = jsonObj.optString("rzsj", "");
        JSONObject result = yg.zjYuanGong(this.qybh, mc, xb, sfzh, sjhm, dlm, dlmm, rzsj);
        renderJSON(result);
        return null;
    }

    public String xgYuanGong() {
        if (feiFa) {
            return reLogin();
        }
        if (jsonObj == null) {
            return chuCuoFanHui(DATA_NONE);
        }

        String bh = jsonObj.optString("ygbh", "");
        if (bh.length() != 32) {
            return chuCuoFanHui("没有指定要修改的员工编号！");
        }
        String mc = jsonObj.optString("mc", "");
        if (mc.length() < 2) {
            return chuCuoFanHui("员工姓名不合规范！");
        }
        String xb = jsonObj.optString("xb", "");
        String sfzh = jsonObj.optString("sfzh", "");
        String sjhm = jsonObj.optString("sjhm", "");
        String rzsj = jsonObj.optString("rzsj", "");
        JSONObject result = yg.xgYuanGong(this.qybh, bh, mc, xb, sfzh, sjhm, rzsj);
        renderJSON(result);
        return null;
    }

    public String scYuanGong() {
        if (feiFa) {
            return reLogin();
        }
        if (jsonObj == null) {
            return chuCuoFanHui(DATA_NONE);
        }

        String bh = jsonObj.optString("ygbh", "");
        if (bh.length() != 32) {
            return chuCuoFanHui("没有指定要删除的员工编号！");
        }

        JSONObject result = yg.scYuanGong(this.qybh, bh);
        renderJSON(result);
        return null;
    }
    
    public String hfYuanGong(){
        if (feiFa) {
            return reLogin();
        }
        if (jsonObj == null) {
            return chuCuoFanHui(DATA_NONE);
        }

        String bh = jsonObj.optString("ygbh", "");
        if (bh.length() != 32) {
            return chuCuoFanHui("没有指定要恢复的员工编号！");
        }

        JSONObject result = yg.hfYuanGong(this.qybh, bh);
        renderJSON(result);
        return null;
    }

    public String czZhangHao() {
        if (feiFa) {
            return reLogin();
        }
        if (jsonObj == null) {
            return chuCuoFanHui(DATA_NONE);
        }

        String bh = jsonObj.optString("ygbh", "");
        if (bh.length() != 32) {
            return chuCuoFanHui("没有指定员工编号！");
        }
        String dlm = jsonObj.optString("dlm", "");
        if (dlm.length() < 2) {
            return chuCuoFanHui("登录名不符合规范！");
        }
        String dlmm = jsonObj.optString("dlmm", "");

        JSONObject result = yg.czZhangHao(this.qybh, bh, dlm, dlmm);
        renderJSON(result);
        return null;
    }

    public String zdJueSe() {
        if (feiFa) {
            return reLogin();
        }
        if (jsonObj == null) {
            return chuCuoFanHui(DATA_NONE);
        }

        String bh = jsonObj.optString("ygbh", "");
        if (bh.length() != 32) {
            return chuCuoFanHui("没有指定员工编号！");
        }
        JSONArray js = jsonObj.getJSONArray("js");
        if ((js == null) || (js.isEmpty())) {
            return chuCuoFanHui("没有需要保存的数据！");
        }

        JSONObject result = yg.zdJueSe(this.qybh, bh, js);
        renderJSON(result);
        return null;
    }
}
