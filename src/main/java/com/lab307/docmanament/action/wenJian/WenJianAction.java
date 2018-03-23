package com.lab307.docmanament.action.wenJian;

import com.lab307.docmanament.action.DocManamentActionInterface;
import com.dengfeng.std.DengFengAction;
import net.sf.json.JSONObject;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;

@Controller
public class WenJianAction extends DengFengAction implements DocManamentActionInterface {

    @Resource(name = "WenJian")
    WenJian wj;

    public String wenJian() {
        return feiFa ? LOGIN : SUCCESS;
    }

    public String cxWenJian() {
        if (feiFa) {
            return reLogin();
        }
        if (jsonObj == null) {
            return chuCuoFanHui(DATA_NONE);
        }

        JSONObject result;
        String wjmc = jsonObj.optString("mc", "");
        String wjflbh = jsonObj.optString("wjfl", "");
        String khbh = jsonObj.optString("kh", "");
        String scr = jsonObj.optString("scr", "");
        String qssj = jsonObj.optString("qssj", "");
        String jssj = jsonObj.optString("jssj", "") + " 23:59:59";
        String gjz = jsonObj.optString("gjz", "");
        int zt = jsonObj.optInt("zt", 1);
        int yx = jsonObj.optInt("yx", 0);
        result = wj.cxWenJian(this.qybh, wjmc,wjflbh, khbh, scr, gjz, qssj, jssj, zt, yx, DocManamentActionInterface.MAXRESULTS,request.getSession());
        renderJSON(result);
        return null;
    }

    public String quWenJianZiLiao() {
        if (feiFa) {
            return reLogin();
        }
        if (jsonObj == null) {
            return chuCuoFanHui(DATA_NONE);
        }

        String wjbh = jsonObj.optString("bh", "");

        JSONObject result;
        if (wjbh.length() == 32) {
            result = wj.quWenJianZiLiao(this.qybh, wjbh);
        } else {
            return chuCuoFanHui("档案编码错误！");
        }
        renderJSON(result);
        return null;
    }

    public String zjWenJian() {
        if (feiFa) {
            return reLogin();
        }
        if (jsonObj == null) {
            return chuCuoFanHui(DATA_NONE);
        }

        String wjmc = jsonObj.optString("mc", "");
        if (wjmc.length() < 2) {
            return chuCuoFanHui("文件名称不合规范！");
        }
        int wjdx = jsonObj.optInt("wjdx", 0);
        if (wjdx <= 0) {
            return chuCuoFanHui("文件大小不合规范！");
        }
        String kh = jsonObj.optString("kh", "");
        if (kh.length() != 32) {
            return chuCuoFanHui("缺少客户编号！");
        }
        String bm = jsonObj.optString("bm", "");
        String wjfl = jsonObj.optString("wjfl", "");
        if (wjfl.length() != 32) {
            return chuCuoFanHui("缺少档案分类编号！");
        }
        String zz = jsonObj.optString("zz", "");
        String gjz = jsonObj.optString("gjz", "");
        String bz = jsonObj.optString("bz", "");
        JSONObject result = wj.zjWenJian(this.qybh, this.ygbh, wjmc, wjdx, kh, bm, wjfl, zz, gjz, bz);
        renderJSON(result);
        return null;
    }

    public String xgWenJian() {
        if (feiFa) {
            return reLogin();
        }
        if (jsonObj == null) {
            return chuCuoFanHui(DATA_NONE);
        }

        String wjbh = jsonObj.optString("bh", "");
        if (wjbh.length() != 32) {
            return chuCuoFanHui("缺少文件编号！");
        }
        String wjmc = jsonObj.optString("mc", "");
        if (wjmc.length() < 2) {
            return chuCuoFanHui("文件名称不合规范！");
        }
        String khbh = jsonObj.optString("kh", "");
        if (khbh.length() != 32) {
            return chuCuoFanHui("缺少客户编号！");
        }
        String bm = jsonObj.optString("bm", "");
        String wjfl = jsonObj.optString("wjfl", "");
        if (wjfl.length() != 32) {
            return chuCuoFanHui("缺少档案分类编号！");
        }
        String zz = jsonObj.optString("zz", "");
        String gjz = jsonObj.optString("gjz", "");
        String bz = jsonObj.optString("bz", "");
        JSONObject result = wj.xgWenJian(this.qybh, this.ygbh, wjbh, wjmc, khbh, bm, wjfl, zz, gjz, bz);
        renderJSON(result);
        return null;
    }

    public String shWenJian() {
        if (feiFa) {
            return reLogin();
        }
        if (jsonObj == null) {
            return chuCuoFanHui(DATA_NONE);
        }

        String wjbh = jsonObj.optString("bh", "");
        if (wjbh.length() != 32) {
            return chuCuoFanHui("缺少文件编号！");
        }
        String bz = jsonObj.optString("bz", "");

        int shyj = jsonObj.optInt("shyj", -9);
        if ((shyj != 9) && (shyj != -9)) {
            return chuCuoFanHui("审核意见不合规范！");
        }
        JSONObject result = wj.shWenJian(this.qybh, this.ygbh, wjbh, shyj, bz);
        renderJSON(result);
        return null;
    }

    public String zlWenJian() {
        if (feiFa) {
            return reLogin();
        }
        if (jsonObj == null) {
            return chuCuoFanHui(DATA_NONE);
        }

        String wjbh = jsonObj.optString("bh", "");
        if (wjbh.length() != 32) {
            return chuCuoFanHui("缺少文件编号！");
        }
        String zlbz = jsonObj.optString("bz", "");

        JSONObject result = wj.zlWenJian(this.qybh, this.ygbh, wjbh, zlbz);
        renderJSON(result);
        return null;
    }

    public String qlWenJian() {
        if (feiFa) {
            return reLogin();
        }
        if (jsonObj == null) {
            return chuCuoFanHui(DATA_NONE);
        }

        String wjbh = jsonObj.optString("bh", "");
        if (wjbh.length() != 32) {
            return chuCuoFanHui("缺少文件编号！");
        }
        String zlbz = jsonObj.optString("bz", "");

        JSONObject result = wj.qlWenJian(this.qybh,this.ygbh, wjbh);
        renderJSON(result);
        return null;
    }

    public String wcscWenJian() {
        if (feiFa) {
            return reLogin();
        }
        if (jsonObj == null) {
            return chuCuoFanHui(DATA_NONE);
        }

        String wjbh = jsonObj.optString("bh", "");
        if (wjbh.length() != 32) {
            return chuCuoFanHui("缺少文件编号！");
        }
        JSONObject result = wj.wanChengShangChuanWenJian(this.qybh, this.ygbh, wjbh);
        renderJSON(result);
        return null;
    }

    public String qxscWenJian() {
        if (feiFa) {
            return reLogin();
        }
        if (jsonObj == null) {
            return chuCuoFanHui(DATA_NONE);
        }

        String wjbh = jsonObj.optString("bh", "");
        if (wjbh.length() != 32) {
            return chuCuoFanHui("缺少文件编号！");
        }
        JSONObject result = wj.quXiaoShangChuanWenJian(this.qybh, wjbh);
        renderJSON(result);
        return null;
    }
    
    public String wcxzWenJian() {
        if (feiFa) {
            return reLogin();
        }
        if (jsonObj == null) {
            return chuCuoFanHui(DATA_NONE);
        }

        String wjbh = jsonObj.optString("bh", "");
        if (wjbh.length() != 32) {
            return chuCuoFanHui("缺少文件编号！");
        }
        JSONObject result = wj.wanChengXiaZaiWenJian(this.qybh, this.ygbh, wjbh);
        renderJSON(result);
        return null;
    }
    
    public String hfWenJian(){
        if (feiFa) {
            return reLogin();
        }
        if (jsonObj == null) {
            return chuCuoFanHui(DATA_NONE);
        }

        String wjbh = jsonObj.optString("bh", "");
        if (wjbh.length() != 32) {
            return chuCuoFanHui("缺少文件编号！");
        }
        JSONObject result = wj.hfWenJian(this.qybh, this.ygbh, wjbh);
        renderJSON(result);
        return null;
    }
}
