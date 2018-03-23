package com.lab307.docmanament.action.buMen;

import com.lab307.docmanament.action.DocManamentActionInterface;
import com.dengfeng.std.BusinessException;
import com.dengfeng.std.DengFengAction;
import javax.annotation.Resource;
import net.sf.json.JSONObject;
import net.sf.json.JSONArray;
import org.springframework.stereotype.Controller;

@Controller
public class BuMenAction extends DengFengAction implements DocManamentActionInterface {

    @Resource(name = "BuMen")
    BuMen bm;

    public String buMen() {
        return feiFa ? LOGIN : SUCCESS;
    }

    public String cxBuMen() {
        if (feiFa) {
            return reLogin();
        }
        if (jsonObj == null) {
            return chuCuoFanHui(DATA_NONE);
        }

        JSONObject result;
        String mc = jsonObj.optString("mc", "");
        int zt = jsonObj.optInt("zt", 1);
        int yx = jsonObj.optInt("yx", 0);
        result = bm.cxBuMen(this.qybh, mc, zt, yx, DocManamentActionInterface.MAXRESULTS);

        renderJSON(result);
        return null;
    }

    public String quBuMenZiLiao() {
        if (feiFa) {
            return reLogin();
        }
        if (jsonObj == null) {
            return chuCuoFanHui(DATA_NONE);
        }

        String bh = jsonObj.optString("bmbh", "");
        JSONObject result;
        if (bh.length() != 32) {
            return chuCuoFanHui("部门编号不正确！");
        }
        result = bm.quBuMenZiLiao(qybh, bh);
        renderJSON(result);
        return null;
    }

    public String cxBuMenZiDian() {
        if (feiFa) {
            return reLogin();
        }
        JSONObject result;
        result = bm.cxBuMenZiDian(this.qybh);
        renderJSON(result);
        return null;
    }

    public String zjBuMen() {
        if (feiFa) {
            return reLogin();
        }
        if (jsonObj == null) {
            return chuCuoFanHui(DATA_NONE);
        }

        String mc = jsonObj.optString("mc", "");
        if (mc.length() < 2) {
            return chuCuoFanHui("部门名称不合规范！");
        }
        String dm = jsonObj.optString("dm", "");
        String bmms = jsonObj.optString("bmms", "");
        JSONArray bmyg = jsonObj.optJSONArray("bmyg");
        if (bmyg == null) {
            return chuCuoFanHui("缺少部门员工数据！");
        }

        JSONObject result;
        try {
            result = bm.zjBuMen(this.qybh, mc, dm, bmms, bmyg);
        } catch (BusinessException ex) {
            result = this.getErrResult(ex.getMessage());
        }
        renderJSON(result);
        return null;
    }

    public String xgBuMen() {
        if (feiFa) {
            return reLogin();
        }
        if (jsonObj == null) {
            return chuCuoFanHui(DATA_NONE);
        }

        String bmbh = jsonObj.optString("bmbh", "");
        if (bmbh.length() != 32) {
            return chuCuoFanHui("没有指定要修改的部门编号！");
        }
        String mc = jsonObj.optString("mc", "");
        if (mc.length() < 2) {
            return chuCuoFanHui("部门名称不合规范！");
        }
        String dm = jsonObj.optString("dm", "");
        String bmms = jsonObj.optString("bmms", "");
        JSONObject result = bm.xgBuMen(qybh, bmbh, mc, dm, bmms);
        renderJSON(result);
        return null;
    }

    public String xgBuMenYuanGong() {
        if (feiFa) {
            return reLogin();
        }
        if (jsonObj == null) {
            return chuCuoFanHui(DATA_NONE);
        }

        String bmbh = jsonObj.optString("bmbh", "");
        if (bmbh.length() != 32) {
            return chuCuoFanHui("缺少部门编号！");
        }
        JSONArray zjYg = jsonObj.getJSONArray("zjYg");
        JSONArray scYg = jsonObj.getJSONArray("scYg");
        if (((zjYg == null) || (zjYg.isEmpty())) && ((scYg == null) || (scYg.isEmpty()))) {
            return chuCuoFanHui("缺少部门员工的数据！");
        }

        JSONObject result;
        try {
            result = bm.xgBuMenYuanGong(bmbh, zjYg, scYg);
        } catch (BusinessException ex) {
            result = this.getErrResult(ex.getMessage());
        }
        renderJSON(result);
        return null;
    }

    public String scBuMen() {
        if (feiFa) {
            return reLogin();
        }
        if (jsonObj == null) {
            return chuCuoFanHui(DATA_NONE);
        }

        String bmbh = jsonObj.optString("bmbh", "");
        if (bmbh.length() != 32) {
            return chuCuoFanHui("没有指定要删除的部门编号！");
        }
        JSONObject result = bm.scBuMen(this.qybh, bmbh);
        renderJSON(result);
        return null;
    }
}
