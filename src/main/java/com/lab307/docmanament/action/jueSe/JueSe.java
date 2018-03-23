/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lab307.docmanament.action.jueSe;

import com.lab307.docmanament.model.JueSeModel;
import com.dengfeng.std.DengFeng;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

@Service("JueSe")
public class JueSe extends DengFeng {

    public static List<JueSeModel> jueSeList = new ArrayList();

    public static List<JueSeModel> getJueSeList() {
        if (jueSeList.isEmpty()) {
            JueSeModel js_pt = new JueSeModel(getBh(), "1", "普通用户");
            jueSeList.add(js_pt);
            JueSeModel js_xtgly = new JueSeModel(getBh(), "101", "系统管理员");
            jueSeList.add(js_xtgly);
            JueSeModel js_dmgly = new JueSeModel(getBh(), "102", "档案管理员");
            jueSeList.add(js_dmgly);
            JueSeModel js_bmzl = new JueSeModel(getBh(), "103", "部门助理");
            jueSeList.add(js_bmzl);
            JueSeModel js_bmzg = new JueSeModel(getBh(), "10301", "部门主管");
            jueSeList.add(js_bmzg);
        }
        return jueSeList;
    }

    public static String getBh() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public JSONObject cxJueSe(String qybh) {
        List<JueSeModel> jsList = getJueSeList();
        JSONObject result = new JSONObject();
        JSONArray ja = new JSONArray();
        Map<String, Object> map = new HashMap();
        for (JueSeModel js : jsList) {
            map.clear();
            map.put("bh", js.getBh());
            map.put("mc", js.getMc());
            map.put("dm", js.getDm());
            ja.add(map);
        }
        result.put("sz", ja);
        result.put("result", 0);
        return result;
    }

}
