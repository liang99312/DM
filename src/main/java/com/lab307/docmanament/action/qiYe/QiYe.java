package com.lab307.docmanament.action.qiYe;

import com.lab307.docmanament.model.DengLuModel;
import com.dengfeng.std.BusinessException;
import com.dengfeng.std.DengFeng;
import com.lab307.docmanament.model.QiYeModel;
import com.lab307.docmanament.model.YuanGongJueSeModel;
import com.lab307.docmanament.model.YuanGongModel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

@Service("QiYe")
@SuppressWarnings("unchecked")
public class QiYe extends DengFeng {

    public JSONObject cxQiYe(String mc, int zt, int yeXu, String yzm, int maxResults) {
        JSONObject ffresult = new JSONObject();
        if(!"DfWd_6726".equals(yzm)){
            ffresult.put("result", -1);
            ffresult.put("msg", "验证码不正确！");
            return ffresult;
        }
        String hql = "from QiYeModel where 1=1 ";
        if (mc.length() > 1) {
            hql += "and mc like '" + mc + "%' ";
        }
        JSONObject result = dao.getPage(hql, yeXu, maxResults);
        List<QiYeModel> khList = dao.getCurrentSession().createQuery(hql + "order by mc")
                .setFirstResult((result.optInt("yx", 1) - 1) * maxResults)
                .setMaxResults(maxResults)
                .list();
        JSONArray ja = new JSONArray();
        Map<String, Object> map = new HashMap();
        for (QiYeModel kh : khList) {
            map.clear();
            map.put("bh", kh.getBh());
            map.put("mc", kh.getMc());
            map.put("sjhm", kh.getSjhm());
            ja.add(map);
        }
        result.put("sz", ja);
        result.put("result", 0);
        return result;
    }

    public JSONObject zjQiYe(String mc, String sjhm, String yzm) throws BusinessException {        
        JSONObject result = new JSONObject();
        if(!"DfWd_6726".equals(yzm)){
            result.put("result", -1);
            result.put("msg", "验证码不正确！");
            return result;
        }
        
        String hql = "select count(1) from QiYeModel where sjhm='" + sjhm + "' or mc = '" + mc + "'";
        if (((Long) this.dao.find(hql).get(0)) > 0L) {
            result.put("result", -1);
            result.put("msg", "该企业名称或手机号码已经存在！");
            return result;
        }
        QiYeModel qy = new QiYeModel();
        qy.setBh(dao.getUuid());
        qy.setMc(mc);
        qy.setSjhm(sjhm);
        dao.insert(qy);

        YuanGongModel yg = new YuanGongModel();
        yg.setBh(dao.getUuid());
        yg.setMc(mc);
        yg.setQybh(qy.getBh());
        yg.setSjhm(sjhm);
        yg.setSfzh(sjhm);
        yg.setDm("gly");
        yg.setXb("男");
        yg.setZt(99);
        dao.insert(yg);

        DengLuModel dl = new DengLuModel();
        dl.setDlm(sjhm);
        dl.setDlmm("123456");
        dl.setYgbh(yg.getBh());

        YuanGongJueSeModel js = new YuanGongJueSeModel();
        js.setBh(dao.getUuid());
        js.setJs("系统管理员");
        js.setJsdm("101");
        js.setYgbh(yg.getBh());

        dao.insert(dl);
        dao.insert(js);
        result.put("result", 0);
        return result;
    }
}
