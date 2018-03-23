package com.lab307.docmanament.action.yuanGong;

import com.lab307.docmanament.model.YuanGongModel;
import com.lab307.docmanament.model.YuanGongJueSeModel;
import com.lab307.docmanament.model.DengLuModel;
import java.util.List;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import com.dengfeng.std.DengFeng;
import com.lab307.docmanament.util.Tools;
import com.dengfeng.std.tools.DfTools;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpSession;
import net.sf.json.JSONArray;

@Service("YuanGongProcess")
@SuppressWarnings("unchecked")
public class YuanGong extends DengFeng {

    private final List<String> list1 = Arrays.asList("402000","901000","902000");
    private final List<String> list101 = Arrays.asList("101000", "201000", "301000", "401000", "402000","901000","902000");
    private final List<String> list102 = Arrays.asList("40100", "402000","901000","902000");
    private final List<String> list103 = Arrays.asList("201000", "301000", "402000","901000","902000");
    private final List<String> list10301 = Arrays.asList("101000", "201000", "301000", "402000","901000","902000");

    public JSONObject dengLu(String dlm, String mm, HttpSession session) {
        String hql = "from DengLuModel where dlm='" + dlm + "' and dlmm='" + mm + "'";
        List<DengLuModel> dlList = dao.find(hql);
        JSONObject json = new JSONObject();

        if (dlList.size() > 0) {
            DengLuModel dl = dlList.get(0);
            hql = "from YuanGongModel where bh='" + dl.getYgbh() + "'";
            List<YuanGongModel> ygList = dao.find(hql);
            if (ygList.size() > 0) {
                YuanGongModel yg = (YuanGongModel) ygList.get(0);
                session.setAttribute("ygbh", yg.getBh());
                session.setAttribute("qybh", yg.getQybh());
                session.setAttribute("mc", yg.getMc());
                session.setAttribute("dlm", dlm);

                json.put("result", 0);
            } else {
                json.put("result", -2);
                json.put("msg", "没有相关的员工记录！");
            }
        } else {
            json.put("result", -1);
            json.put("msg", "用户名和密码不正确！");
        }
        return json;
    }

    public JSONObject getQuanXian(String qybh, String ygbh) {
        JSONObject result = new JSONObject();
        try {
            String hql = "from YuanGongJueSeModel where ygbh='" + ygbh + "'";
            List<YuanGongJueSeModel> jsList = dao.find(hql);
            JSONArray qx = new JSONArray();
            List<String> qxList = new ArrayList<>();
            for (YuanGongJueSeModel ygjs : jsList) {
                switch (ygjs.getJsdm()) {
                    case "1":
                        qxList.removeAll(list1);
                        qxList.addAll(list1);
                        break;
                    case "101":
                        qxList.removeAll(list101);
                        qxList.addAll(list101);
                        break;
                    case "102":
                        qxList.removeAll(list102);
                        qxList.addAll(list102);
                        break;
                    case "103":
                        qxList.removeAll(list103);
                        qxList.addAll(list103);
                        break;
                    case "10301":
                        qxList.removeAll(list10301);
                        qxList.addAll(list10301);
                        break;
                    default:
                        break;
                }
            }
            for (String s : qxList) {
                qx.add(s);
            }
            result.put("result", 0);
            result.put("qx", qx);
        } catch (Exception e) {
            result.put("result", -1);
            result.put("msg", "获取菜单权限失败");
        }
        return result;
    }

    public JSONObject xgMiMa(String qybh, String ygbh, String ymm, String xmm) {
        JSONObject result = new JSONObject();
        try {
            String hql = "from DengLuModel where ygbh='" + ygbh + "'";
            List<DengLuModel> dlList = dao.find(hql);
            if(dlList.size() > 0){
                DengLuModel dl = dlList.get(0);
                if(!dl.getDlmm().equals(ymm)){
                    result.put("result", -1);
                    result.put("msg", "原密码不正确");
                    return result;
                }
                dl.setDlmm(xmm);
                this.dao.update(dl);
                result.put("result", 0);
            }else{
                result.put("result", -1);
                result.put("msg", "密码修改出错！");
            }
        } catch (Exception e) {
            result.put("result", -1);
            result.put("msg", "密码修改出错！");
        }
        return result;
    }

    public JSONObject cxYuanGong(String qybh, String mc, String xb, String sfzh, String sjhm, int zt, int yeXu, int maxResults) {
        String hql = "from YuanGongModel where qybh='" + qybh + "' and zt=" + zt + " ";
        if (mc.length() > 1) {
            hql += "and mc like '" + mc + "%' ";
        }
        if (xb.length() > 0) {
            hql += "and xb='" + xb + "' ";
        }
        if (sfzh.length() == 18) {
            hql += "and sfzh='" + sfzh + "' ";
        }
        if (sjhm.length() == 11) {
            hql += "and sjhm='" + sjhm + "' ";
        }

        JSONObject result = dao.getPage(hql, yeXu, maxResults);

        List<YuanGongModel> ygList = dao.getCurrentSession().createQuery(hql + "order by mc")
                .setFirstResult((result.optInt("yx", 1) - 1) * maxResults)
                .setMaxResults(maxResults)
                .list();
        JSONArray ja = new JSONArray();
        Map<String, Object> map = new HashMap();
        for (YuanGongModel yg : ygList) {
            map.clear();
            map.put("bh", yg.getBh());
            map.put("mc", yg.getMc());
            map.put("xb", yg.getXb());
            map.put("sfzh", yg.getSfzh());
            map.put("sjhm", yg.getSjhm());
            map.put("rzsj", DfTools.defDate(yg.getRzsj()));
            map.put("zt", yg.getZt());
            ja.add(map);
        }
        result.put("sz", ja);
        result.put("result", 0);

        return result;
    }

    public JSONObject quYuanGongZiLiao(String qybh, String ygbh) {
        String hql = "from YuanGongModel where bh='" + ygbh + "' and qybh='" + qybh + "'";
        List<YuanGongModel> ygList = dao.find(hql);

        hql = "from DengLuModel where ygbh='" + ygbh + "'";
        List<DengLuModel> dlList = dao.find(hql);

        hql = "from YuanGongJueSeModel where ygbh='" + ygbh + "'";
        List<YuanGongJueSeModel> jsList = dao.find(hql);

        JSONObject result = new JSONObject();
        if (ygList.size() > 0) {
            YuanGongModel yg = ygList.get(0);
            result.put("bh", yg.getBh());
            result.put("mc", yg.getMc());
            result.put("dm", yg.getDm());
            result.put("xb", yg.getXb());
            result.put("sfzh", yg.getSfzh());
            result.put("sjhm", yg.getSjhm());
            result.put("rzsj", DfTools.defDate(yg.getRzsj()));
            result.put("zt", yg.getZt());
            if (dlList.size() > 0) {
                DengLuModel dl = dlList.get(0);
                result.put("dlm", dl.getDlm());
            } else {
                result.put("dlm", "");
            }
            JSONArray ja = new JSONArray();
            Map<String, Object> map = new HashMap();
            for (YuanGongJueSeModel ygjs : jsList) {
                map.clear();
                map.put("bh", ygjs.getBh());
                map.put("js", ygjs.getJs());
                map.put("jsdm", ygjs.getJsdm());
                map.put("ygbh", ygjs.getYgbh());
                ja.add(map);
            }
            result.put("js", ja);
            result.put("result", 0);
        } else {
            result.put("result", -1);
            result.put("msg", "没有找到指定的员工信息！");
        }

        return result;
    }

    public JSONObject cxYuanGongZiDian(String qybh) {
        String hql = "from YuanGongModel where qybh='" + qybh + "' and zt>0";
        List<YuanGongModel> ygList = dao.find(hql);

        JSONArray ja = new JSONArray();
        Map<String, Object> map = new HashMap();
        for (YuanGongModel yg : ygList) {
            map.clear();
            map.put("bh", yg.getBh());
            map.put("mc", yg.getMc());
            map.put("dm", yg.getDm());
            ja.add(map);
        }
        JSONObject result = new JSONObject();
        result.put("sz", ja);
        result.put("result", 0);
        return result;
    }

    public JSONObject zjYuanGong(String qybh, String mc, String xb, String sfzh, String sjhm, String dlm, String dlmm, String rzsj) {
        JSONObject result = new JSONObject();
        String hql = "select count(1) from YuanGongModel where qybh='" + qybh + "' and sfzh='" + sfzh + "'";
        if (((Long) this.dao.find(hql).get(0)) > 0L) {
            result.put("result", -1);
            result.put("msg", "该身份证号已经存在！");
            return result;
        }
        hql = "select count(1) from YuanGongModel where qybh='" + qybh + "' and mc='" + mc + "'";
        if (((Long) this.dao.find(hql).get(0)) > 0L) {
            result.put("result", -1);
            result.put("msg", "该姓名已经存在！");
            return result;
        }
        hql = "select count(1) from DengLuModel where dlm='" + dlm + "'";
        if (((Long) this.dao.find(hql).get(0)) > 0L) {
            result.put("result", -1);
            result.put("msg", "该登录名已经存在！");
            return result;
        }

        YuanGongModel yg = new YuanGongModel();
        yg.setQybh(qybh);
        yg.setBh(dao.getUuid());
        yg.setMc(mc);
        yg.setDm(Tools.getPinYinDaiMa(mc));
        yg.setXb(xb);
        yg.setSfzh(sfzh);
        yg.setSjhm(sjhm);
        yg.setRzsj(DfTools.parseDefDateToCal(rzsj));
        yg.setZt(1);

        DengLuModel dl = new DengLuModel();
        dl.setYgbh(yg.getBh());
        dl.setDlm(dlm);
        dl.setDlmm(dlmm);

        try {
            dao.insert(yg);
            dao.insert(dl);
            result.put("result", 0);
        } catch (Exception ex) {
            result.put("result", -1);
            result.put("msg", "增加员工出错！");
            logger.error("zjYuanGong, 增加员工出错！" + ex.getMessage());
        }

        return result;
    }

    public JSONObject xgYuanGong(String qybh, String ygbh, String mc, String xb, String sfzh, String sjhm, String rzsj) {
        JSONObject result = new JSONObject();
        String hql = "select count(1) from YuanGongModel where qybh='" + qybh + "' and sfzh='" + sfzh + "' and bh !='" + ygbh + "'";
        if (((Long) this.dao.find(hql).get(0)) > 0L) {
            result.put("result", -1);
            result.put("msg", "该身份证号已经存在！");
            return result;
        }
        hql = "select count(1) from YuanGongModel where qybh='" + qybh + "' and mc='" + mc + "' and bh !='" + ygbh + "'";
        if (((Long) this.dao.find(hql).get(0)) > 0L) {
            result.put("result", -1);
            result.put("msg", "该姓名已经存在！");
            return result;
        }
        hql = "from YuanGongModel where bh='" + ygbh + "' and qybh='" + qybh + "'";
        List<YuanGongModel> ygList = dao.find(hql);
        if (ygList.size() > 0) {
            YuanGongModel yg = ygList.get(0);
            yg.setMc(mc);
            yg.setXb(xb);
            yg.setSfzh(sfzh);
            yg.setSjhm(sjhm);
            yg.setRzsj(DfTools.parseDefDateToCal(rzsj));
            try {
                dao.update(yg);
                result.put("result", 0);
            } catch (Exception ex) {
                result.put("result", -1);
                result.put("msg", "修改员工信息出错！");
                logger.error("xgYuanGong, 修改员工信息出错！" + ex.getMessage());
            }
        } else {
            result.put("result", -1);
            result.put("msg", "未找到需要修改的员工！");
        }

        return result;
    }

    public JSONObject scYuanGong(String qybh, String ygbh) {
        String hql = "from YuanGongModel where bh='" + ygbh + "' and qybh='" + qybh + "'";
        List<YuanGongModel> ygList = dao.find(hql);

        JSONObject result = new JSONObject();
        if (ygList.size() > 0) {
            YuanGongModel yg = ygList.get(0);
            if (yg.getZt() == 99) {
                result.put("result", -1);
                result.put("msg", "系统固有员工，不可离职！");
                return result;
            }

            yg.setZt(-1);
            try {
                dao.update(yg);
                result.put("result", 0);
            } catch (Exception ex) {
                result.put("result", -1);
                result.put("msg", "删除员工出错！");
                logger.error("scYuanGong, 删除员工出错！" + ex.getMessage());
            }
        } else {
            result.put("result", -1);
            result.put("msg", "未找到需要删除的员工！");
        }
        return result;
    }
    
    public JSONObject hfYuanGong(String qybh, String ygbh) {
        String hql = "from YuanGongModel where bh='" + ygbh + "' and qybh='" + qybh + "'";
        List<YuanGongModel> ygList = dao.find(hql);

        JSONObject result = new JSONObject();
        if (ygList.size() > 0) {
            YuanGongModel yg = ygList.get(0);
            if (yg.getZt() == 99) {
                result.put("result", -1);
                result.put("msg", "系统固有员工，不用恢复状态！");
                return result;
            }

            yg.setZt(1);
            try {
                dao.update(yg);
                result.put("result", 0);
            } catch (Exception ex) {
                result.put("result", -1);
                result.put("msg", "恢复员工状态出错！");
                logger.error("scYuanGong, 恢复员工状态出错！" + ex.getMessage());
            }
        } else {
            result.put("result", -1);
            result.put("msg", "未找到需要恢复的员工！");
        }
        return result;
    }

    public JSONObject czZhangHao(String qybh, String ygbh, String dlm, String mm) {
        JSONObject result = new JSONObject();
        String hql = "select count(1) from DengLuModel where dlm='" + dlm + "' and ygbh != '" + ygbh + "'";
        if (((Long) this.dao.find(hql).get(0)) > 0L) {
            result.put("result", -1);
            result.put("msg", "该登录名已经存在！");
            return result;
        }
        hql = "from DengLuModel where ygbh='" + ygbh + "'";
        List<DengLuModel> dlList = dao.find(hql);
        if (dlList.size() > 0) {
            DengLuModel dl = dlList.get(0);
            dl.setDlmm(mm);
            dl.setDlm(dlm);
            try {
                dao.update(dl);
                result.put("result", 0);
            } catch (Exception ex) {
                result.put("result", -1);
                result.put("msg", "重置员工账号密码出错！");
                logger.error("czMiMa, 重置员工账号密码出错！" + ex.getMessage());
            }
        } else {
            result.put("result", -1);
            result.put("msg", "未找到需要修改的员工登录信息！");
        }
        return result;
    }

    public JSONObject zdJueSe(String qybh, String ygbh, JSONArray js) {
        JSONObject result = new JSONObject();
        try {
            String sql = "delete from t_ygjs where ygbh='" + ygbh + "'";
            this.dao.executeUpdate(sql);

            for (int i = 0; i < js.size(); i++) {
                JSONObject jsonJs = js.getJSONObject(i);
                YuanGongJueSeModel ygjs = new YuanGongJueSeModel();
                ygjs.setYgbh(ygbh);
                ygjs.setJs(jsonJs.optString("js", ""));
                ygjs.setJsdm(jsonJs.optString("jsdm", ""));
                ygjs.setBh(dao.getUuid());
                dao.insert(ygjs);
            }
            result.put("result", 0);
        } catch (Exception ex) {
            result.put("result", -1);
            result.put("msg", "指定员工角色出错！");
            logger.error("zdJueSe, 指定员工角色出错！" + ex.getMessage());
        }
        return result;
    }
}
