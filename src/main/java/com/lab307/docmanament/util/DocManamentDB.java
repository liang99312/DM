package com.lab307.docmanament.util;

import com.dengfeng.std.db.DengFengDB;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import javax.annotation.Resource;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SQLQuery;

@Repository
public class DocManamentDB {
	@Resource(name = "sessionFactory")
	private SessionFactory sessionFactory;
	private final Logger logger = Logger.getLogger(this.getClass());

	public String getUuid(){
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	public SessionFactory getSessionFactory(){
		return this.sessionFactory;
	}
	
	public Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	public Session newSession() {
		return sessionFactory.openSession();
	}

	public Serializable insert(Object o) {
		return this.getCurrentSession().save(o);
	}

	public void delete(Object o) {
		this.getCurrentSession().delete(o);
	}

	public void update(Object o) {
		this.getCurrentSession().update(o);
	}

	public void saveOrUpdate(Object o) {
		this.getCurrentSession().saveOrUpdate(o);
	}

	public List find(String hql) {
		return this.getCurrentSession().createQuery(hql).list();
	}

	public int executeUpdate(String sql){
		SQLQuery query = this.getCurrentSession().createSQLQuery(sql);
		return query.executeUpdate();
	}

	
	public int executeUpdateByHql(String hql) {
		Query query = this.getCurrentSession().createQuery(hql);
		return query.executeUpdate();
	}

	/**
	 * 查询页序
	 */
	public JSONObject getPage(String sql, int yeXu, int maxResults){
		JSONObject result = new JSONObject();
		int jiLuShu;
		if (sql.indexOf("Model")>0)
			jiLuShu = ((Long)find("select count(*) "+sql).get(0)).intValue();
		else
			jiLuShu = ((BigInteger)(this.getCurrentSession().createSQLQuery("select count(*) "+sql).list().get(0))).intValue();
		
		int zongYeShu = (jiLuShu+maxResults-1)/maxResults;
		
		int dangQianYe = (yeXu>zongYeShu) ? zongYeShu : yeXu;
		if (dangQianYe<1) dangQianYe = 1;
		
		result.put("jls", jiLuShu);
		result.put("zys", zongYeShu);
		result.put("yx", dangQianYe);
		return result;
	}
}