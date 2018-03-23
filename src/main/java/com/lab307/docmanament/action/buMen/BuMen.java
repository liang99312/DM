package com.lab307.docmanament.action.buMen;

import java.util.List;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import com.dengfeng.std.DengFeng;
import com.dengfeng.std.BusinessException;
import com.lab307.docmanament.model.BuMenModel;
import com.lab307.docmanament.model.YuanGongModel;
import com.lab307.docmanament.model.BuMenYuanGongModel;
import java.util.HashMap;
import java.util.Map;
import net.sf.json.JSONArray;

@Service("BuMen")
@SuppressWarnings("unchecked")
public class BuMen extends DengFeng {

    public JSONObject cxBuMen(String qybh, String mc, int zt, int yeXu, int maxResults) {
        String hql = "from BuMenModel where qybh='" + qybh + "' and zt=" + zt + " ";
        if (mc.length() > 1) {
            hql += "and mc like '" + mc + "%' ";
        }

        JSONObject result = dao.getPage(hql, yeXu, maxResults);
        List<BuMenModel> bmList = dao.getCurrentSession().createQuery(hql + "order by mc")
                .setFirstResult((result.optInt("yx", 1) - 1) * maxResults)
                .setMaxResults(maxResults)
                .list();
        JSONArray ja = new JSONArray();
        Map<String, Object> map = new HashMap();
        for (BuMenModel bm : bmList) {
            map.clear();
            map.put("bh", bm.getBh());
            map.put("mc", bm.getMc());
            map.put("dm", bm.getDm());
            map.put("bmms", bm.getBmms());
            map.put("zt", bm.getZt());
            ja.add(map);
        }
        result.put("sz", ja);
        result.put("result", 0);

        return result;
    }

    public JSONObject cxBuMenZiDian(String qybh) {
        String hql = "from BuMenModel where qybh='" + qybh + "' and zt=1 ";
        List<BuMenModel> bmList = dao.find(hql);
        JSONArray ja = new JSONArray();
        Map<String, Object> map = new HashMap();
        for (BuMenModel bm : bmList) {
            map.clear();
            map.put("bh", bm.getBh());
            map.put("mc", bm.getMc());
            map.put("dm", bm.getDm());
            ja.add(map);
        }
        JSONObject result = new JSONObject();
        result.put("sz", ja);
        result.put("result", 0);

        return result;
    }

    public JSONObject quBuMenZiLiao(String qybh, String bmbh) {
        String hql = "from BuMenModel where bh='" + bmbh + "' and qybh='" + qybh + "'";
        List<BuMenModel> bmList = dao.find(hql);

        JSONObject result = new JSONObject();
        if (bmList.size() > 0) {
            BuMenModel bm = bmList.get(0);
            result.put("bh", bm.getBh());
            result.put("mc", bm.getMc());
            result.put("dm", bm.getDm());
            result.put("bmms", bm.getBmms());
            result.put("zt", bm.getZt());

            String sql = "select {yg.*} from t_bmyg as bmyg "
                    + "inner join t_yg as yg on yg.bh=bmyg.ygbh "
                    + "where bmyg.bmbh='" + bmbh + "' and yg.zt > 0";

            List<Object> list = dao.getCurrentSession().createSQLQuery(sql)
                    .addEntity("yg", YuanGongModel.class)
                    .list();

            JSONArray ja = new JSONArray();
            Map<String, Object> map = new HashMap();
            for (Object obj : list) {
                YuanGongModel yg = (YuanGongModel) obj;
                map.clear();
                map.put("bh", yg.getBh());
                map.put("mc", yg.getMc());
                map.put("zt", yg.getZt());
                ja.add(map);
            }
            result.put("bmyg", ja);
            result.put("result", 0);
        } else {
            result.put("result", -1);
            result.put("msg", "找不到指定的部门信息！");
        }

        return result;
    }

    public JSONObject zjBuMen(String qybh, String mc, String dm, String bmms, JSONArray jaYg) throws BusinessException {
        JSONObject result = new JSONObject();
        String hql = "select count(1) from BuMenModel where qybh='" + qybh + "' and mc='" + mc + "'";
        if (((Long) this.dao.find(hql).get(0)) > 0L) {
            result.put("result", -1);
            result.put("msg", "该部门名称已经存在！");
            return result;
        }

        BuMenModel bm = new BuMenModel();
        bm.setQybh(qybh);
        bm.setBh(dao.getUuid());
        bm.setMc(mc);
        bm.setDm(dm);
        bm.setBmms(bmms);
        bm.setZt(1);
        try {
            dao.insert(bm);
            for (int i = 0; i < jaYg.size(); i++) {
                JSONObject jsonYg = jaYg.getJSONObject(i);
                BuMenYuanGongModel yg = new BuMenYuanGongModel();
                yg.setYgbh(jsonYg.optString("bh", ""));
                if (yg.getYgbh().length() != 32) {
                    logger.error("部门员工编号不正确！" + yg.getYgbh());
                    throw new BusinessException("部门员工编号不正确！");
                }
                yg.setBh(dao.getUuid());
                yg.setBmbh(bm.getBh());
                dao.insert(yg);
            }
            result.put("result", 0);
        } catch (Exception ex) {
            logger.error("增加部门出错：" + ex.getMessage());
            throw new BusinessException("增加部门出错！请检查您录入的数据是否有误！");
        }
        return result;
    }

    public JSONObject xgBuMen(String qybh, String bmbh, String mc, String dm, String bmms) {
        JSONObject result = new JSONObject();
        String hql = "select count(1) from BuMenModel where qybh='" + qybh + "' and mc='" + mc + "' and bh !='" + bmbh + "'";
        if (((Long) this.dao.find(hql).get(0)) > 0L) {
            result.put("result", -1);
            result.put("msg", "该部门名称已经存在！");
            return result;
        }
        hql = "from BuMenModel where bh='" + bmbh + "' and qybh='" + qybh + "'";
        List<BuMenModel> bmList = dao.find(hql);
        if (bmList.size() > 0) {
            BuMenModel bm = bmList.get(0);
            bm.setMc(mc);
            bm.setDm(dm);
            bm.setBmms(bmms);
            try {
                dao.update(bm);
                result.put("result", 0);
            } catch (Exception ex) {
                result.put("result", -1);
                result.put("msg", "修改部门信息出错！");
                logger.error("xgYuanGong, 修改部门信息出错！" + ex.getMessage());
            }
        } else {
            result.put("result", -1);
            result.put("msg", "未找到需要修改的部门信息！");
        }

        return result;
    }

    public JSONObject xgBuMenYuanGong(String bmbh, JSONArray zjYg, JSONArray scYg) {
        JSONObject result = new JSONObject();
        try {
            if (scYg.size() > 0) {
                String hql = "delete from BuMenYuanGongModel where bmbh='" + bmbh + "' and ygbh in('-1'";
                for (int i = 0; i < scYg.size(); i++) {
                    String ygbh = scYg.getString(i);
                    hql += ",'" + ygbh + "'";
                }
                hql += ")";
                this.dao.executeUpdateByHql(hql);
            }
            for (int i = 0; i < zjYg.size(); i++) {
                String ygbh = zjYg.getString(i);
                BuMenYuanGongModel bmyg = new BuMenYuanGongModel();
                bmyg.setBh(dao.getUuid());
                bmyg.setBmbh(bmbh);
                bmyg.setYgbh(ygbh);
                this.dao.insert(bmyg);
            }
            result.put("result", 0);
        } catch (Exception ex) {
            result.put("result", -1);
            result.put("msg", "设置部门员工出错！");
            logger.error("设置部门员工出错！" + ex.getMessage());
        }
        return result;
    }

    public JSONObject scBuMen(String qybh, String bmbh) {
        JSONObject result = new JSONObject();
        try {
            String hql = "update BuMenModel set zt=-1 where bh='" + bmbh + "' and qybh='" + qybh + "'";
            dao.executeUpdateByHql(hql);
            result.put("result", 0);
        } catch (Exception ex) {
            result.put("result", -1);
            result.put("msg", "删除部门出错！");
            logger.error("scBuMen, 删除部门出错！" + ex.getMessage());
        }
        return result;
    }
}
