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
import com.lab307.docmanament.model.QuanXianModel;
import com.lab307.docmanament.model.WenJianFenLeiModel;
import com.dengfeng.std.BusinessException;
import com.dengfeng.std.DengFeng;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Service;

@Service("WenJianFenLei")
public class WenJianFenLei extends DengFeng {

    public JSONObject cxWenJianFenLei(String qybh, String mc, int zt, int yeXu, int maxResults) {
        String hql = "from WenJianFenLeiModel where qybh='" + qybh + "' and zt=" + zt + " ";
        if (mc.length() > 1) {
            hql = hql + "and mc like '" + mc + "%' ";
        }
        JSONObject result = this.dao.getPage(hql, yeXu, maxResults);

        List<WenJianFenLeiModel> wjflList = this.dao.getCurrentSession().createQuery(hql + "order by mc").setFirstResult((result.optInt("yx", 1) - 1) * maxResults).setMaxResults(maxResults).list();
        JSONArray ja = new JSONArray();
        Map<String, Object> map = new HashMap();
        for (WenJianFenLeiModel wjfl : wjflList) {
            map.clear();
            map.put("bh", wjfl.getBh());
            map.put("mc", wjfl.getMc());
            map.put("dm", wjfl.getDm());
            map.put("jb", wjfl.getJb());
            map.put("bz", wjfl.getBz());
            map.put("zt", wjfl.getZt());
            ja.add(map);
        }
        result.put("sz", ja);
        result.put("result", 0);

        return result;
    }

    public JSONObject quWenJianFenLeiZiLiao(String qybh, String bh) {
        String hql = "from WenJianFenLeiModel where bh='" + bh + "' and qybh='" + qybh + "'";
        List<WenJianFenLeiModel> wjflList = this.dao.find(hql);

        JSONObject result = new JSONObject();
        if (wjflList.size() > 0) {
            WenJianFenLeiModel wjfl = (WenJianFenLeiModel) wjflList.get(0);
            result.put("bh", wjfl.getBh());
            result.put("mc", wjfl.getMc());
            result.put("dm", wjfl.getDm());
            result.put("jb", wjfl.getJb());
            result.put("bz", wjfl.getBz());
            result.put("zt", wjfl.getZt());

            String sql = "select {qx.*},bm.mc as bmmc from t_qx as qx left join t_bm as bm on bm.bh=qx.bmbh where qx.wjflbh='" + bh + "'";

            List<Object[]> list = this.dao.getCurrentSession().createSQLQuery(sql).addEntity("qx", QuanXianModel.class).addScalar("bmmc", StringType.INSTANCE).list();

            JSONArray qxArray = new JSONArray();
            Map<String, Object> map = new HashMap();
            for (Object[] obj : list) {
                QuanXianModel qx = (QuanXianModel) obj[0];
                map.clear();
                map.put("bh", qx.getBh());
                map.put("wjflbh", qx.getWjflbh());
                map.put("js", qx.getJs());
                map.put("jsdm", qx.getJsdm());
                map.put("bmbh", qx.getBmbh());
                map.put("bz", qx.getBz());
                map.put("cz", qx.getCz());
                map.put("bmmc", (String) obj[1]);
                qxArray.add(map);
            }
            result.put("qx", qxArray);
            result.put("result", 0);
        } else {
            result.put("result", -1);
            result.put("msg", "没有找到指定分类！");
        }
        return result;
    }

    public JSONObject zjWenJianFenLei(String qybh, String mc, String dm, String jb, String bz)
            throws BusinessException {
        JSONObject result = new JSONObject();
        String hql = "select count(1) from WenJianFenLeiModel where qybh='" + qybh + "' and mc='" + mc + "'";
        if (((Long) this.dao.find(hql).get(0)) > 0L) {
            result.put("result", -1);
            result.put("msg", "该分类名称已经存在！");
            return result;
        }

        WenJianFenLeiModel wjfl = new WenJianFenLeiModel();
        wjfl.setQybh(qybh);
        wjfl.setBh(this.dao.getUuid());
        wjfl.setMc(mc);
        wjfl.setDm(dm);
        wjfl.setJb(jb);
        wjfl.setBz(bz);
        wjfl.setZt(1);

        try {
            this.dao.insert(wjfl);
            result.put("result", 0);
        } catch (Exception ex) {
            this.logger.error("zjWenJianFenLei," + ex.getMessage());
            throw new BusinessException("新增文件分类出错，请检查您录入的数据是否有误！");
        }
        return result;
    }

    public JSONObject xgWenJianFenLei(String qybh, String bh, String mc, String dm, String jb, String bz) {
        JSONObject result = new JSONObject();
        String hql = "select count(1) from WenJianFenLeiModel where qybh='" + qybh + "' and mc='" + mc + "' and bh !='" + bh + "'";
        if (((Long) this.dao.find(hql).get(0)) > 0L) {
            result.put("result", -1);
            result.put("msg", "该分类名称已经存在！");
            return result;
        }

        hql = "from WenJianFenLeiModel where bh='" + bh + "' and qybh='" + qybh + "'";
        List<WenJianFenLeiModel> wjflList = this.dao.find(hql);
        if (wjflList.size() > 0) {
            WenJianFenLeiModel wjfl = (WenJianFenLeiModel) wjflList.get(0);
            wjfl.setMc(mc);
            wjfl.setDm(dm);
            wjfl.setJb(jb);
            wjfl.setBz(bz);
            try {
                this.dao.update(wjfl);
                result.put("result", 0);
            } catch (Exception ex) {
                result.put("result", -1);
                result.put("msg", "");
                this.logger.error("xgYuanGong," + ex.getMessage());
            }
        } else {
            result.put("result", -1);
            result.put("msg", "修改文件分类出错，请检查您录入的数据是否有误！");
        }
        return result;
    }

    public JSONObject xgWenJianFenLeiQuanXian(String bh, String qybh, JSONArray qxArray) {
        String hql = "from QuanXianModel where qybh = '" + qybh + "' and wjflbh = '" + bh + "'";
        List<QuanXianModel> qxList = this.dao.find(hql);
        HashMap<String, QuanXianModel> qxMap = new HashMap<String, QuanXianModel>();
        for (QuanXianModel qxm : qxList) {
            qxMap.put(qxm.getBh(), qxm);
        }
        for (int i = 0; i < qxArray.size(); i++) {
            JSONObject qxJson = qxArray.optJSONObject(i);
            if (qxJson == null) {
                throw new BusinessException("数据错误！");
            }
            int act = qxJson.optInt("act", -1);
            String bmbh = qxJson.optString("bmbh", "");
            String js = qxJson.optString("js", "");
            String jsdm = qxJson.optString("jsdm", "");
            String cz = qxJson.optString("cz", "");
            String bz = qxJson.optString("bz", "");

            QuanXianModel qx;
            switch (act) {
                case 0:
                    break;
                case 1:
                    qx = new QuanXianModel();
                    qx.setBh(dao.getUuid());
                    qx.setBmbh(bmbh);
                    qx.setBz(bz);
                    qx.setCz(cz);
                    qx.setJs(js);
                    qx.setJsdm(jsdm);
                    qx.setQybh(qybh);
                    qx.setWjflbh(bh);
                    dao.insert(qx);
                    break;
                case 2:
                    qx = qxMap.get(qxJson.optString("bh", ""));
                    if (qx == null) {
                        throw new BusinessException("数据错误！");
                    }
                    qx.setBmbh(bmbh);
                    qx.setBz(bz);
                    qx.setCz(cz);
                    qx.setJs(js);
                    qx.setJsdm(jsdm);
                    dao.update(qx);
                    break;
                case 3:
                    qx = qxMap.get(qxJson.optString("bh", ""));
                    if (qx == null) {
                        throw new BusinessException("数据错误！");
                    }
                    dao.delete(qx);
                    break;
                default:
                    throw new BusinessException("数据错误！");
            }
        }
        JSONObject result = new JSONObject();
        result.put("result", 0);
        return result;
    }

    public JSONObject scWenJianFenLei(String qybh, String bh) {
        JSONObject result = new JSONObject();
        try {
            String sql = "update t_wjfl set zt=-1 where bh='" + bh + "' and qybh='" + qybh + "'";
            this.dao.executeUpdate(sql);
            result.put("result", 0);
        } catch (Exception ex) {
            result.put("result", -1);
            result.put("msg", "删除文件分类出错！");
            this.logger.error("scWenJianFenLei" + ex.getMessage());
        }
        return result;
    }

    public JSONObject qlWenJianFenLei(String qybh, String bh) {
        JSONObject result = new JSONObject();
        try {
            String hql = "delete from t_qx where wjflbh='" + bh + "'";
            this.dao.executeUpdate(hql);
            hql = "delete from t_wjfl where bh='" + bh + "' and qybh='" + qybh + "'";
            this.dao.executeUpdate(hql);
            result.put("result", 0);
        } catch (Exception ex) {
            result.put("result", -1);
            result.put("msg", "清理文件分类错误！");
            this.logger.error("qlWenJianFenLei" + ex.getMessage());
        }
        return result;
    }

    public JSONObject cxWenJianFenLeiZiDian(String qybh) {
        String hql = "from WenJianFenLeiModel where qybh='" + qybh + "' and zt=1 ";
        JSONObject result = new JSONObject();
        List<WenJianFenLeiModel> wjflList = this.dao.find(hql);
        JSONArray ja = new JSONArray();
        Map<String, Object> map = new HashMap();
        for (WenJianFenLeiModel wjfl : wjflList) {
            map.clear();
            map.put("bh", wjfl.getBh());
            map.put("mc", wjfl.getMc());
            map.put("dm", wjfl.getDm());
            ja.add(map);
        }
        result.put("sz", ja);
        result.put("result", 0);
        return result;
    }

    public JSONObject cxShouQuanWenJianFen(String qybh, String ygbh, HttpSession session) {
        JSONObject result = new JSONObject();
        List<String> czwjfl = new ArrayList();
        try {
            String gsSql = "select {q.*},w.mc as flmc,w.dm as fldm,w.jb as fljb from t_ygjs j,t_qx q,t_wjfl w where q.wjflbh = w.bh and q.bmbh='0' and position(q.jsdm in j.jsdm) = 1 and j.ygbh ='" + ygbh + "'";
            String bmSql = "select {q.*},w.mc as flmc,w.dm as fldm,w.jb as fljb,b.mc as bmmc,b.dm as bmdm from t_ygjs j,t_qx q,t_bmyg g,t_wjfl w,t_bm b"
                    + " where q.bmbh=b.bh and q.wjflbh = w.bh and q.bmbh=g.bmbh and j.ygbh=g.ygbh and position(q.jsdm in j.jsdm) = 1 and j.ygbh ='" + ygbh + "'";
            List<Object[]> gsList = this.dao.getCurrentSession().createSQLQuery(gsSql)
                    .addEntity("q", QuanXianModel.class)
                    .addScalar("flmc", StringType.INSTANCE)
                    .addScalar("fldm", StringType.INSTANCE)
                    .addScalar("fljb", StringType.INSTANCE).list();
            List<Object[]> bmList = this.dao.getCurrentSession().createSQLQuery(bmSql)
                    .addEntity("q", QuanXianModel.class)
                    .addScalar("flmc", StringType.INSTANCE)
                    .addScalar("fldm", StringType.INSTANCE)
                    .addScalar("fljb", StringType.INSTANCE)
                    .addScalar("bmmc", StringType.INSTANCE)
                    .addScalar("bmdm", StringType.INSTANCE).list();

            JSONArray czArray = new JSONArray();
            JSONArray xzArray = new JSONArray();
            JSONArray zjArray = new JSONArray();
            JSONArray xgArray = new JSONArray();
            JSONArray zlArray = new JSONArray();
            JSONArray shArray = new JSONArray();
            JSONArray qlArray = new JSONArray();

            for (Object[] objs : bmList) {
                QuanXianModel qx = (QuanXianModel) objs[0];
                String flmc = objs[1].toString();
                String fldm = objs[2].toString();
                String fljb = objs[3].toString();
                String bmmc = objs[4].toString();
                String bmdm = objs[5].toString();
                JSONObject bm = new JSONObject();
                bm.put("bh", qx.getBmbh());
                bm.put("mc", bmmc);
                bm.put("dm", bmdm);
                JSONObject fl = new JSONObject();
                fl.put("bh", qx.getWjflbh());
                fl.put("mc", flmc);
                fl.put("dm", fldm);
                fl.put("jb", fljb);
                addObject("cz", qx, czArray, fl, bm);
                addObject("xz", qx, xzArray, fl, bm);
                addObject("zj", qx, zjArray, fl, bm);
                addObject("xg", qx, xgArray, fl, bm);
                addObject("zl", qx, zlArray, fl, bm);
                addObject("sh", qx, shArray, fl, bm);
                addObject("ql", qx, qlArray, fl, bm);
            }

            for (Object[] objs : gsList) {
                QuanXianModel qx = (QuanXianModel) objs[0];
                String flmc = objs[1].toString();
                String fldm = objs[2].toString();
                String fljb = objs[3].toString();
                String bmmc = "公司";
                String bmdm = "gs";
                JSONObject bm = new JSONObject();
                bm.put("bh", qx.getBmbh());
                bm.put("mc", bmmc);
                bm.put("dm", bmdm);
                JSONObject fl = new JSONObject();
                fl.put("bh", qx.getWjflbh());
                fl.put("mc", flmc);
                fl.put("dm", fldm);
                fl.put("jb", fljb);
                addObject("cz", qx, czArray, fl, bm);
                addObject("xz", qx, xzArray, fl, bm);
                addObject("zj", qx, zjArray, fl, bm);
                addObject("xg", qx, xgArray, fl, bm);
                addObject("zl", qx, zlArray, fl, bm);
                addObject("sh", qx, shArray, fl, bm);
                addObject("ql", qx, qlArray, fl, bm);
            }

            JSONObject qx = new JSONObject();
            qx.put("cz", czArray);
            qx.put("xz", xzArray);
            qx.put("zj", zjArray);
            qx.put("xg", xgArray);
            qx.put("zl", zlArray);
            qx.put("sh", shArray);
            qx.put("ql", qlArray);

            result.put("qx", qx);
            result.put("result", 0);

            for (int i = 0; i < czArray.size(); i++) {
                JSONObject obj = czArray.optJSONObject(i);
                String bh = obj.optString("bh", "");
                if (!"".equals(bh)) {
                    if (!czwjfl.contains(bh)) {
                        czwjfl.add(bh);
                    }
                }
            }
            session.setAttribute("czwjfl", czwjfl);
        } catch (Exception e) {
            result.put("result", -1);
            result.put("msg", "查询权限出错！");
        }
        return result;
    }

    public void addObject(String cz, QuanXianModel qx, JSONArray array, JSONObject fl, JSONObject bm) {
        if (qx.getCz().contains(cz)) {
            JSONObject ckfl = findObject(array, qx.getWjflbh());
            boolean flag = true;
            if (ckfl == null) {
                ckfl = fl;
                flag = false;
            }
            JSONArray bmArray;
            if (ckfl.containsKey("bm")) {
                bmArray = ckfl.getJSONArray("bm");
            } else {
                bmArray = new JSONArray();
            }
            if (findObject(bmArray, qx.getBmbh()) == null) {
                bmArray.add(bm);
            }
            ckfl.put("bm", bmArray);
            if (!flag) {
                array.add(ckfl);
            }

        }
    }

    public JSONObject findObject(JSONArray array, String bh) {
        if (array.isEmpty()) {
            return null;
        }
        for (int i = 0; i < array.size(); i++) {
            JSONObject obj = array.optJSONObject(i);
            if (obj.optString("bh", "").equalsIgnoreCase(bh)) {
                return obj;
            }
        }
        return null;
    }
}
