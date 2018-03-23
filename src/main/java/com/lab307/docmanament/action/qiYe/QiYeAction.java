package com.lab307.docmanament.action.qiYe;

import com.lab307.docmanament.action.DocManamentActionInterface;
import com.dengfeng.std.DengFengAction;
import javax.annotation.Resource;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;

@Controller
public class QiYeAction extends DengFengAction implements DocManamentActionInterface {

    @Resource(name = "QiYe")
    QiYe kh;

    public String qiYe() {
        return SUCCESS;
    }

    public String cxQiYe() {
        if (jsonObj == null) {
            return chuCuoFanHui(DATA_NONE);
        }

        JSONObject result;
        String khmc = jsonObj.optString("mc", "");
        int zt = jsonObj.optInt("zt", 1);
        int yx = jsonObj.optInt("yx", 0);
        String yzm = jsonObj.optString("yzm", "");
        result = kh.cxQiYe(khmc, zt, yx, yzm, DocManamentActionInterface.MAXRESULTS);

        renderJSON(result);
        return null;
    }

    public String zjQiYe() {
        if (jsonObj == null) {
            return chuCuoFanHui(DATA_NONE);
        }

        String khmc = jsonObj.optString("mc", "");
        if (khmc.length() < 2) {
            return chuCuoFanHui("企业名称不符合规范！");
        }
        String sjhm = jsonObj.optString("sjhm", "");
        String yzm = jsonObj.optString("yzm", "");
        JSONObject result = kh.zjQiYe(khmc, sjhm, yzm);
        renderJSON(result);
        return null;
    }

}
