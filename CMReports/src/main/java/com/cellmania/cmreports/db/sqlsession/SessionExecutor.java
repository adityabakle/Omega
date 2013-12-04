package com.cellmania.cmreports.db.sqlsession;

import java.util.Collection;
import java.util.LinkedList;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class SessionExecutor {
	
	public static Object selectOne(String sqlStatementId) throws Exception {
		SqlSessionFactory factory = SqlSessionConfig.getSqlMapClient();
		SqlSession session = factory.openSession();
		return session.selectOne(sqlStatementId);
	}
	
	public static Object selectOne(String sqlStatementId, Object params) throws Exception {
		SqlSessionFactory factory = SqlSessionConfig.getSqlMapClient();
		SqlSession session = factory.openSession();
		return session.selectOne(sqlStatementId, params);
	}
	
	public static Object selectList(String sqlStatementId, Object params, int startRow, int numOfRec) throws Exception {
		SqlSessionFactory factory = SqlSessionConfig.getSqlMapClient();
		SqlSession session = factory.openSession();
		RowBounds rb = new RowBounds(startRow, numOfRec);
		try{
			return session.selectList(sqlStatementId,params,rb);
		} finally{
			session.close();
		}
	}
	
	public static Object selectList(String sqlStatementId,  int startRow, int numOfRec) throws Exception {
		SqlSessionFactory factory = SqlSessionConfig.getSqlMapClient();
		SqlSession session = factory.openSession();
		RowBounds rb = new RowBounds(startRow, numOfRec);
		try {
			return session.selectList(sqlStatementId,null,rb);
		} finally{
			session.close();
		}
		
	}
	
	public static Object selectList(String sqlStatementId, Object params) throws Exception {
		SqlSessionFactory factory = SqlSessionConfig.getSqlMapClient();
		SqlSession session = factory.openSession();
		try {
			return session.selectList(sqlStatementId,params);
		} finally{
			session.close();
		}
		
	}
	
	public static Object selectList(String sqlStatementId) throws Exception {
		SqlSessionFactory factory = SqlSessionConfig.getSqlMapClient();
		SqlSession session = factory.openSession();
		try {
			return session.selectList(sqlStatementId);
		} finally{
			session.close();
		}
		
	}
	
	public static void insert(String sqlStatemtnId, Object params) throws Exception{
		SqlSessionFactory factory = SqlSessionConfig.getSqlMapClient();
		SqlSession session = factory.openSession();
		try{
			session.insert(sqlStatemtnId, params);
			session.commit(true);
		} catch (Exception e){
			session.rollback(true);
			throw e;
		}
		finally{
			session.close();
		}
	}
	
	public static void update(String sqlStatemtnId, Object params) throws Exception{
		SqlSessionFactory factory = SqlSessionConfig.getSqlMapClient();
		SqlSession session = factory.openSession();
		try{
			session.update(sqlStatemtnId, params);
			session.commit(true);
		} catch (Exception e){
			session.rollback(true);
			throw e;
		}
		finally{
			session.close();
		}
	}

	public static void update(String sqlStatemtnId) throws Exception {
		SqlSessionFactory factory = SqlSessionConfig.getSqlMapClient();
		SqlSession session = factory.openSession();
		try{
			session.update(sqlStatemtnId);
			session.commit(true);
		} catch (Exception e){
			session.rollback(true);
			throw e;
		}
		finally{
			session.close();
		}
		
	}
	
	public static void insert(LinkedList<Object[]> queryAndParams) throws Exception {
		if(queryAndParams == null || queryAndParams.size()==0)
			throw new Exception("Query and Params cannot be null");
		
		SqlSessionFactory factory = SqlSessionConfig.getSqlMapClient();
		SqlSession session = factory.openSession();
		try{
			for(Object[] obj : queryAndParams){
				String query = (String) obj[0];
				Object param = obj[1];
				
				if(param instanceof Collection){
					@SuppressWarnings("unchecked")
					Collection<Object> cParams = (Collection<Object>) param;
					for(Object paramObj : cParams){
						session.insert(query, paramObj);
					}
				} else {
					session.insert(query,param);
				}
			}
			session.commit(true);
		} catch (Exception e){
			session.rollback(true);
			throw e;
		}
		finally{
			session.close();
		}
	}
	
	public static void update(LinkedList<Object[]> queryAndParams) throws Exception {
		if(queryAndParams == null || queryAndParams.size()==0)
			throw new Exception("Query and Params cannot be null");
		
		SqlSessionFactory factory = SqlSessionConfig.getSqlMapClient();
		SqlSession session = factory.openSession();
		try{
			for(Object[] obj : queryAndParams){
				String query = (String) obj[0];
				Object param = obj[1];
				
				if(param instanceof Collection){
					@SuppressWarnings("unchecked")
					Collection<Object> cParams = (Collection<Object>) param;
					for(Object paramObj : cParams){
						session.update(query, paramObj);
					}
				} else {
					session.update(query,param);
				}
			}
			session.commit(true);
		} catch (Exception e){
			session.rollback(true);
			throw e;
		}
		finally{
			session.close();
		}
	}

	public static void delete(String sqlStatemtnId, Object param) throws Exception{
		SqlSessionFactory factory = SqlSessionConfig.getSqlMapClient();
		SqlSession session = factory.openSession();
		try{
			session.delete(sqlStatemtnId, param);
			session.commit(true);
		} catch (Exception e){
			session.rollback(true);
			throw e;
		}
		finally{
			session.close();
		}
		
	}
}
