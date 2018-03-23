package com.lab307.docmanament.action.keHu;

import com.dengfeng.std.BusinessException;
import com.dengfeng.std.DengFeng;
import com.lab307.docmanament.model.KeHuModel;
import com.lab307.docmanament.util.Tools;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

@Service("KeHu")
@SuppressWarnings("unchecked")
public class KeHu extends DengFeng {

    public JSONObject cxKeHu(String qybh, String mc, int zt, int yeXu, int maxResults) {
        String hql = "from KeHuModel where qybh='" + qybh + "' and zt=" + zt + " ";
        if (mc.length() > 1) {
            hql += "and mc like '" + mc + "%' ";
        }
        JSONObject result = dao.getPage(hql, yeXu, maxResults);
        List<KeHuModel> khList = dao.getCurrentSession().createQuery(hql + "order by mc")
                .setFirstResult((result.optInt("yx", 1) - 1) * maxResults)
                .setMaxResults(maxResults)
                .list();
        JSONArray ja = new JSONArray();
        Map<String, Object> map = new HashMap();
        for (KeHuModel kh : khList) {
            map.clear();
            map.put("bh", kh.getBh());
            map.put("mc", kh.getMc());
            map.put("dm", kh.getDm());
            map.put("lxr", kh.getLxr());
            map.put("lxfs", kh.getLxfs());
            map.put("zt", kh.getZt());
            ja.add(map);
        }
        result.put("sz", ja);
        result.put("result", 0);
        return result;
    }

    public JSONObject quKeHuZiLiao(String qybh, String khbh) {
        String hql = "from KeHuModel where bh='" + khbh + "' and qybh='" + qybh + "' ";
        List<KeHuModel> khList = dao.find(hql);
        JSONObject result = new JSONObject();
        if (khList.size() > 0) {
            KeHuModel kh = khList.get(0);
            result.put("bh", kh.getBh());
            result.put("mc", kh.getMc());
            result.put("dm", kh.getDm());
            result.put("lxr", kh.getLxr());
            result.put("lxfs", kh.getLxfs());
            result.put("zt", kh.getZt());
            result.put("result", 0);
        } else {
            result.put("result", -1);
            result.put("msg", "没有找到指定的客户信息！");
        }

        return result;
    }

    public JSONObject cxKeHuZiDian(String qybh) {
        String hql = "from KeHuModel where qybh='" + qybh + "' and zt>0 ";
        List<KeHuModel> khList = dao.find(hql);
        JSONArray ja = new JSONArray();
        Map<String, Object> map = new HashMap();
        for (KeHuModel kh : khList) {
            map.clear();
            map.put("bh", kh.getBh());
            map.put("mc", kh.getMc());
            map.put("dm", kh.getDm());
            ja.add(map);
        }
        JSONObject result = new JSONObject();
        result.put("sz", ja);
        result.put("result", 0);
        return result;
    }

    public JSONObject zjKeHu(String qybh, String mc, String dm, String lxr, String lxfs) throws BusinessException {
        JSONObject result = new JSONObject();
        String hql = "select count(1) from BuMenModel where qybh='" + qybh + "' and mc='" + mc + "'";
        if (((Long) this.dao.find(hql).get(0)) > 0L) {
            result.put("result", -1);
            result.put("msg", "该客户名称已经存在！");
            return result;
        }
        hql = "from KeHuModel where qybh='" + qybh + "' and mc='" + mc + "'";
        List<KeHuModel> khList = dao.find(hql);
        if (khList.size() > 0) {
            KeHuModel kh = khList.get(0);
            kh.setZt(1);
            if (dm.length() > 0) {
                kh.setDm(dm);
            }
            kh.setLxr(lxr);
            kh.setLxfs(lxfs);
            try {
                dao.update(kh);
            } catch (Exception ex) {
                logger.error("找回客户错误：" + ex.getMessage());
                throw new BusinessException("找回客户出错！请检查您录入的数据是否有误！");
            }
        } else {
            KeHuModel kh = new KeHuModel();
            kh.setBh(dao.getUuid());
            kh.setQybh(qybh);
            kh.setMc(mc);
            kh.setDm(Tools.getPinYinDaiMa(mc));
            kh.setLxr(lxr);
            kh.setLxfs(lxfs);
            kh.setZt(1);
            try {
                dao.insert(kh);
            } catch (Exception ex) {
                logger.error("新增客户错误：" + ex.getMessage());
                throw new BusinessException("新增客户出错！请检查您录入的数据是否有误！");
            }
        }
        result.put("result", 0);
        return result;
    }

    public JSONObject xgKeHu(String qybh, String khbh, String mc, String dm, String lxr, String lxfs) {
        JSONObject result = new JSONObject();
        String hql = "select count(1) from BuMenModel where qybh='" + qybh + "' and mc='" + mc + "' and bh !='" + khbh + "'";
        if (((Long) this.dao.find(hql).get(0)) > 0L) {
            result.put("result", -1);
            result.put("msg", "该客户名称已经存在！");
            return result;
        }
        hql = "from KeHuModel where bh='" + khbh + "' and qybh='" + qybh + "'";
        List<KeHuModel> khList = dao.find(hql);
        if (khList.size() > 0) {
            KeHuModel kh = khList.get(0);
            kh.setMc(mc);
            kh.setDm((dm.length() > 0) ? dm : Tools.getPinYinDaiMa(mc));
            kh.setLxr(lxr);
            kh.setLxfs(lxfs);
            try {
                dao.update(kh);
            } catch (Exception ex) {
                logger.error("修改客户信息错误：" + ex.getMessage());
                throw new BusinessException("修改客户信息错误！请检查您录入的数据是否有误！");
            }
            result.put("result", 0);
        } else {
            result.put("result", -1);
            result.put("msg", "没有找到要修改的客户信息！");
        }
        return result;
    }

    public JSONObject scKeHu(String qybh, String khbh) {
        String hql = "from KeHuModel where bh='" + khbh + "' and qybh='" + qybh + "'";
        List<KeHuModel> khList = dao.find(hql);
        JSONObject result = new JSONObject();
        if (khList.size() > 0) {
            KeHuModel kh = khList.get(0);
            if (kh.getZt() < 1) {
                throw new BusinessException("客户信息已经被删除，不能重复删除！");
            }
            kh.setZt(-1);
            try {
                dao.update(kh);
            } catch (Exception ex) {
                logger.error("删除客户信息错误：" + ex.getMessage());
                throw new BusinessException("删除客户信息错误！请检查您录入的数据是否有误！");
            }
            result.put("result", 0);
        } else {
            result.put("result", -1);
            result.put("msg", "没有找到要删除的客户信息！");
        }
        return result;
    }
}
