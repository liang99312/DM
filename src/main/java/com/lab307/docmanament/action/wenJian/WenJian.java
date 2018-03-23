package com.lab307.docmanament.action.wenJian;

import com.lab307.docmanament.model.WenJianLogModel;
import com.dengfeng.std.BusinessException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

import com.lab307.docmanament.model.WenJianModel;
import com.lab307.docmanament.model.WenJianTempModel;
import com.dengfeng.std.DengFeng;
import com.dengfeng.std.tools.DfTools;
import java.util.Calendar;
import javax.servlet.http.HttpSession;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.hibernate.type.StringType;

@Service("WenJian")
@SuppressWarnings("unchecked")
public class WenJian extends DengFeng {

    public JSONObject cxWenJian(String qybh, String mc, String wjflbh, String kh, String scr, String gjz, String qssj, String jssj, int zt, int yeXu, int maxResults, HttpSession session) {
        String tj = "where wj.qybh='" + qybh + "' and wj.zt=" + zt + " ";
        if (mc.length() > 0) {
            tj += "and wj.mc like '" + mc + "%' ";
        }
        if (wjflbh.length() == 32) {
            tj += "and wj.wjflbh='" + wjflbh + "' ";
        } else {
            String temp_tj = "and wj.wjflbh in('-1'";
            Object obj = session.getAttribute("czwjfl");
            if (obj != null) {
                List<String> bhList = (List<String>) obj;
                for (String temp_bh : bhList) {
                    temp_tj += ",'" + temp_bh + "'";
                }
            }
            temp_tj += ") ";
            tj += temp_tj;
        }
        if (kh.length() == 32) {
            tj += "and wj.khbh='" + kh + "' ";
        }
        if (scr.length() == 32) {
            tj += "and wj.scr='" + scr + "' ";
        }
        if (gjz.length() > 0) {
            tj += " and wj.gjz like '%" + gjz + "%'";
        }
        if ((qssj.length() == 10) && (jssj.length() == 19)) {
            tj += "and ((scsj>'" + qssj + "') and (scsj<'" + jssj + "')) ";
        } else if (qssj.length() == 10) {
            tj += "and scsj>'" + qssj + "' ";
        } else if (jssj.length() == 19) {
            tj += "and scsj<'" + jssj + "' ";
        }
        JSONObject result = dao.getPage("from t_wj as wj "
                + "left join t_yg as scr on scr.bh=wj.scr "
                + "left join t_kh as kh  on kh.bh=wj.khbh "
                + tj, yeXu, maxResults);
        String hql = "select {wj.*}, kh.mc as khmc, scr.mc as scrmc from t_wj as wj "
                + "left join t_yg as scr on scr.bh=wj.scr "
                + "left join t_kh as kh  on kh.bh=wj.khbh "
                + tj
                + "order by wj.mc";

        List<Object[]> objList = dao.getCurrentSession().createSQLQuery(hql)
                .addEntity("wj", WenJianModel.class)
                .addScalar("khmc", StringType.INSTANCE)
                .addScalar("scrmc", StringType.INSTANCE)
                .setFirstResult((result.optInt("yx", 1) - 1) * maxResults)
                .setMaxResults(maxResults)
                .list();
        JSONArray ja = new JSONArray();
        Map<String, Object> map = new HashMap();
        for (Object[] obj : objList) {
            map.clear();
            WenJianModel wj = (WenJianModel) obj[0];
            map.put("bh", wj.getBh());
            map.put("mc", wj.getMc());
            map.put("khbh", wj.getKhbh());
            map.put("bmbh", wj.getBmbh());
            map.put("wjflbh", wj.getWjflbh());
            map.put("wjdx", wj.getWjdx());
            map.put("khmc", (String) obj[1]);
            map.put("scrmc", (String) obj[2]);
            map.put("scsj", DfTools.defDateTime(wj.getScsj()));
            map.put("zt", wj.getZt());
            ja.add(map);
        }
        result.put("sz", ja);
        result.put("result", 0);
        return result;
    }

    public JSONObject quWenJianZiLiao(String qybh, String wjbh) {
        String hql = "select {wj.*}, kh.mc as khmc, scr.mc as scrmc, zlr.mc as zlrmc, shr.mc as shrmc,wjfl.mc as flmc,bm.mc as bmmc from t_wj as wj "
                + "left join t_yg as scr on scr.bh=wj.scr "
                + "left join t_yg as zlr on zlr.bh=wj.zlr "
                + "left join t_yg as shr on shr.bh=wj.shr "
                + "left join t_kh as kh on kh.bh=wj.khbh "
                + "left join t_wjfl as wjfl on wjfl.bh=wj.wjflbh "
                + "left join t_bm as bm on bm.bh=wj.bmbh "
                + "where wj.bh='" + wjbh + "'";
        List<Object[]> objList = dao.getCurrentSession().createSQLQuery(hql)
                .addEntity("wj", WenJianModel.class)
                .addScalar("khmc", StringType.INSTANCE)
                .addScalar("scrmc", StringType.INSTANCE)
                .addScalar("zlrmc", StringType.INSTANCE)
                .addScalar("shrmc", StringType.INSTANCE)
                .addScalar("flmc", StringType.INSTANCE)
                .addScalar("bmmc", StringType.INSTANCE)
                .list();
        JSONObject result = new JSONObject();
        if (objList.size() > 0) {
            Object[] obj = objList.get(0);
            WenJianModel wj = (WenJianModel) obj[0];
            result.put("bh", wj.getBh());
            result.put("mc", wj.getMc());
            result.put("wjdx", wj.getWjdx());
            result.put("khbh", wj.getKhbh());
            result.put("bmbh", wj.getBmbh());
            result.put("bz", wj.getBz());
            result.put("gjz", wj.getGjz());
            result.put("jym", wj.getJym());
            result.put("scr", wj.getScr());
            result.put("shr", wj.getShr());
            result.put("shbz", wj.getShbz());
            result.put("shsj", DfTools.defDateTime(wj.getShsj()));
            result.put("wjflbh", wj.getWjflbh());
            result.put("wjlx", wj.getWjlx());
            result.put("zlbz", wj.getZlbz());
            result.put("zlr", wj.getZlr());
            result.put("zlsj", DfTools.defDateTime(wj.getZlsj()));
            result.put("zz", wj.getZz());
            result.put("khmc", (String) obj[1]);
            result.put("scrmc", (String) obj[2]);
            result.put("zlrmc", (String) obj[3]);
            result.put("shrmc", (String) obj[4]);
            result.put("flmc", (String) obj[5]);
            result.put("bmmc", (String) obj[6]);
            result.put("scsj", DfTools.defDateTime(wj.getScsj()));
            result.put("zt", wj.getZt());

            result.put("result", 0);
        } else {
            result.put("result", -1);
            result.put("msg", "没有找到指定文件！");
        }

        return result;
    }

    public JSONObject zjWenJian(String qybh, String scr, String wjmc, int wjdx, String khbh, String bm, String wjfl, String zz, String gjz, String bz) throws BusinessException {
        WenJianTempModel wj = new WenJianTempModel();
        String wjlx = "";
        if (wjmc.contains(".")) {
            wjlx = wjmc.substring(wjmc.lastIndexOf(".") + 1);
        }
        wj.setBh(dao.getUuid());
        wj.setQybh(qybh);
        wj.setMc(wjmc);
        wj.setWjdx(wjdx);
        wj.setWjlx(wjlx);
        wj.setKhbh(khbh);
        wj.setScr(scr);
        wj.setScsj(Calendar.getInstance());
        wj.setBmbh(bm);
        wj.setWjflbh(wjfl);
        wj.setZz(zz);
        wj.setGjz(gjz);
        wj.setBz(bz);
        try {
            dao.insert(wj);
        } catch (Exception ex) {
            logger.error("新增文件错误：" + ex.getMessage());
            throw new BusinessException("新增文件出错！请检查您录入的数据是否有误！");
        }
        JSONObject result = new JSONObject();
        result.put("wjbh", wj.getBh());
        result.put("result", 0);
        return result;
    }

    public JSONObject xgWenJian(String qybh, String ygbh, String wjbh, String wjmc, String khbh, String bm, String wjfl, String zz, String gjz, String bz) throws BusinessException {
        String hql = "from WenJianModel where bh='" + wjbh + "' and qybh='" + qybh + "'";
        List<WenJianModel> wjList = dao.find(hql);
        JSONObject result = new JSONObject();
        if (wjList.size() > 0) {
            WenJianModel wj = wjList.get(0);
            wj.setMc(wjmc);
            wj.setKhbh(khbh);
            wj.setBmbh(bm);
            wj.setWjflbh(wjfl);
            wj.setZz(zz);
            wj.setGjz(gjz);
            wj.setBz(bz);
            try {
                dao.update(wj);
                addWjLog(qybh, wjbh, "修改", ygbh, "");
            } catch (Exception ex) {
                logger.error("修改文件错误：" + ex.getMessage());
                throw new BusinessException("修改文件出错！请检查您录入的数据是否有误！");
            }
            result.put("result", 0);
        } else {
            result.put("result", -1);
            result.put("msg", "没有找到要修改的文件！");
        }
        return result;
    }

    public JSONObject shWenJian(String qybh, String ygbh, String wjbh, int shyj, String bz) {
        String hql = "from WenJianModel where bh='" + wjbh + "' and qybh='" + qybh + "' and zt>-1";
        List<WenJianModel> wjList = dao.find(hql);
        JSONObject result = new JSONObject();
        if (wjList.size() > 0) {
            WenJianModel wj = wjList.get(0);
            wj.setShbz(bz);
            wj.setShr(ygbh);
            wj.setShsj(Calendar.getInstance());
            wj.setZt(shyj);
            try {
                dao.update(wj);
                addWjLog(qybh, wjbh, "审核", ygbh, "");
            } catch (Exception ex) {
                logger.error("审核文件错误：" + ex.getMessage());
                throw new BusinessException("审核文件出错！请检查您录入的数据是否有误！");
            }
            result.put("result", 0);
        } else {
            result.put("result", -1);
            result.put("msg", "没有找到要整理的文件！");
        }
        return result;
    }

    public JSONObject zlWenJian(String qybh, String ygbh, String wjbh, String zlbz) throws BusinessException {
        String hql = "from WenJianModel where bh='" + wjbh + "' and qybh='" + qybh + "' and zt=9";
        List<WenJianModel> wjList = dao.find(hql);
        JSONObject result = new JSONObject();
        if (wjList.size() > 0) {
            WenJianModel wj = wjList.get(0);
            wj.setZlbz(zlbz);
            wj.setZlr(ygbh);
            wj.setZlsj(Calendar.getInstance());
            wj.setZt(-1);
            try {
                dao.update(wj);
                addWjLog(qybh, wjbh, "整理", ygbh, "");
            } catch (Exception ex) {
                logger.error("整理文件错误：" + ex.getMessage());
                throw new BusinessException("整理文件出错！请检查您录入的数据是否有误！");
            }
            result.put("result", 0);
        } else {
            result.put("result", -1);
            result.put("msg", "没有找到要整理的文件！");
        }
        return result;
    }

    public JSONObject qlWenJian(String qybh, String ygbh, String wjbh) {
        String hql = "delete from t_wj where bh='" + wjbh + "' and qybh='" + qybh + "' and zt<0";
        JSONObject result = new JSONObject();
        if (dao.executeUpdate(hql) == 1) {
            addWjLog(qybh, wjbh, "清理", ygbh, "");
            result.put("result", 0);
        } else {
            result.put("result", -1);
            result.put("msg", "没有找到要整理的文件！");
        }
        return result;
    }
    
    public JSONObject hfWenJian(String qybh, String ygbh, String wjbh) throws BusinessException {
        String hql = "from WenJianModel where bh='" + wjbh + "' and qybh='" + qybh + "' and zt=-1";
        List<WenJianModel> wjList = dao.find(hql);
        JSONObject result = new JSONObject();
        if (wjList.size() > 0) {
            WenJianModel wj = wjList.get(0);
            wj.setZt(1);
            try {
                dao.update(wj);
                addWjLog(qybh, wjbh, "恢复", ygbh, "");
            } catch (Exception ex) {
                logger.error("恢复文件错误：" + ex.getMessage());
                throw new BusinessException("恢复文件出错！请检查您录入的数据是否有误！");
            }
            result.put("result", 0);
        } else {
            result.put("result", -1);
            result.put("msg", "没有找到要恢复的文件！");
        }
        return result;
    }

    public String getFileDir(String qybh) {
        return "/home/upLoadFiles/"+qybh+"/";
        //return "D:\\upLoadFiles/" + qybh + "/";
    }

    public JSONObject wanChengShangChuanWenJian(String qybh, String ygbh, String wjbh) {
        JSONObject result = new JSONObject();
        String hql = "from WenJianTempModel where bh='" + wjbh + "' and qybh='" + qybh + "'";
        List<WenJianTempModel> wjList = dao.find(hql);
        if (wjList.size() > 0) {
            WenJianTempModel wj_temp = wjList.get(0);
            WenJianModel wj = new WenJianModel();
            wj.setBh(wj_temp.getBh());
            wj.setQybh(qybh);
            wj.setMc(wj_temp.getMc());
            wj.setWjdx(wj_temp.getWjdx());
            wj.setKhbh(wj_temp.getKhbh());
            wj.setScr(wj_temp.getScr());
            wj.setScsj(wj_temp.getScsj());
            wj.setGjz(wj_temp.getGjz());
            wj.setJym(wj_temp.getJym());
            wj.setBz(wj_temp.getBz());
            wj.setZz(wj_temp.getZz());
            wj.setWjflbh(wj_temp.getWjflbh());
            wj.setWjlx(wj_temp.getWjlx());
            wj.setBmbh(wj_temp.getBmbh());
            wj.setZt(1);
            try {
                dao.insert(wj);
                dao.delete(wj_temp);
                addWjLog(qybh, wj.getBh(), "新增", ygbh, "");
            } catch (Exception ex) {
                logger.error("新增文件错误：" + ex.getMessage());
                throw new BusinessException("新增文件出错！请检查您录入的数据是否有误！");
            }
            result.put("wjbh", wj.getBh());
            result.put("result", 0);
        } else {
            result.put("result", -1);
            result.put("msg", "上传文件失败！");
        }
        return result;
    }

    public JSONObject quXiaoShangChuanWenJian(String qybh, String wjbh) {
        String hql = "delete from t_wj_temp where bh='" + wjbh + "' and qybh='" + qybh + "'";
        JSONObject result = new JSONObject();
        if (dao.executeUpdate(hql) == 1) {
            result.put("result", 0);
        } else {
            result.put("result", -1);
            result.put("msg", "没有找到要取消的文件！");
        }
        return result;
    }

    public JSONObject wanChengXiaZaiWenJian(String qybh, String ygbh, String wjbh) {
        JSONObject result = new JSONObject();
        try {
            addWjLog(qybh, wjbh, "下载", ygbh, "");
            result.put("result", 0);
        } catch (Exception ex) {
            result.put("result", -1);
            result.put("msg", "新增日志错误");
            logger.error("新增日志错误：" + ex.getMessage());
            throw new BusinessException("新增日志错误！");
        }
        return result;
    }

    private void addWjLog(String qybh, String wjbh, String cz, String czr, String bz) {
        WenJianLogModel log = new WenJianLogModel();
        log.setBh(dao.getUuid());
        log.setBz(bz);
        log.setCz(cz);
        log.setCzr(czr);
        log.setCzsj(Calendar.getInstance());
        log.setQybh(qybh);
        log.setWjbh(wjbh);
        dao.insert(log);
    }
}
