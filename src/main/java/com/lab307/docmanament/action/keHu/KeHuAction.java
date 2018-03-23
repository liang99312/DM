package com.lab307.docmanament.action.keHu;

import com.lab307.docmanament.action.DocManamentActionInterface;
import com.dengfeng.std.DengFengAction;
import javax.annotation.Resource;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;

@Controller
public class KeHuAction extends DengFengAction implements DocManamentActionInterface {

    @Resource(name = "KeHu")
    KeHu kh;

    public String keHu() {
        return feiFa ? LOGIN : SUCCESS;
    }

    public String cxKeHu() {
        if (feiFa) {
            return reLogin();
        }
        if (jsonObj == null) {
            return chuCuoFanHui(DATA_NONE);
        }

        JSONObject result;
        String khmc = jsonObj.optString("mc", "");
        int zt = jsonObj.optInt("zt", 1);
        int yx = jsonObj.optInt("yx", 0);
        result = kh.cxKeHu(this.qybh, khmc, zt, yx, DocManamentActionInterface.MAXRESULTS);

        renderJSON(result);
        return null;
    }

    public String quKeHuZiLiao() {
        if (feiFa) {
            return reLogin();
        }
        if (jsonObj == null) {
            return chuCuoFanHui(DATA_NONE);
        }

        String khbh = jsonObj.optString("bh", "");
        JSONObject result;
        if (khbh.length() == 32) {
            result = kh.quKeHuZiLiao(this.qybh, khbh);
        } else {
            return chuCuoFanHui("客户编号错误！");
        }

        renderJSON(result);
        return null;
    }

    public String cxKeHuZiDian() {
        if (feiFa) {
            return reLogin();
        }
        JSONObject result = kh.cxKeHuZiDian(this.qybh);
        renderJSON(result);
        return null;
    }

    public String zjKeHu() {
        if (feiFa) {
            return reLogin();
        }
        if (jsonObj == null) {
            return chuCuoFanHui(DATA_NONE);
        }

        String khmc = jsonObj.optString("mc", "");
        if (khmc.length() < 2) {
            return chuCuoFanHui("客户名称不符合规范！");
        }
        String dm = jsonObj.optString("dm", "");
        String lxr = jsonObj.optString("lxr", "");
        String lxfs = jsonObj.optString("lxfs", "");
        JSONObject result = kh.zjKeHu(this.qybh, khmc, dm, lxr, lxfs);

        renderJSON(result);
        return null;
    }

    public String xgKeHu() {
        if (feiFa) {
            return reLogin();
        }
        if (jsonObj == null) {
            return chuCuoFanHui(DATA_NONE);
        }

        String khbh = jsonObj.optString("bh", "");
        if (khbh.length() != 32) {
            return chuCuoFanHui("客户编号不正确！");
        }
        String khmc = jsonObj.optString("mc", "");
        if (khmc.length() < 2) {
            return chuCuoFanHui("客户名称不符合规范！");
        }
        String dm = jsonObj.optString("dm", "");
        String lxr = jsonObj.optString("lxr", "");
        String lxfs = jsonObj.optString("lxfs", "");
        JSONObject result = kh.xgKeHu(this.qybh, khbh, khmc, dm, lxr, lxfs);

        renderJSON(result);
        return null;
    }

    public String scKeHu() {
        if (feiFa) {
            return reLogin();
        }
        if (jsonObj == null) {
            return chuCuoFanHui(DATA_NONE);
        }

        String khbh = jsonObj.optString("bh", "");
        if (khbh.length() != 32) {
            return chuCuoFanHui("客户编号不正确！");
        }
        JSONObject result = kh.scKeHu(this.qybh, khbh);

        renderJSON(result);
        return null;
    }
}
