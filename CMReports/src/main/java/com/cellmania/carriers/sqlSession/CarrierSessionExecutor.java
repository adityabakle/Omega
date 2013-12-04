package com.cellmania.carriers.sqlSession;

import java.util.Collection;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class CarrierSessionExecutor {
	
	public static Collection<?> selectList(SqlSessionFactory factory, String sqlStatementId,Object params) throws Exception {
		SqlSession session = factory.openSession();
		try{
			return session.selectList(sqlStatementId,params);
		} finally{
			session.close();
		}
	}
	
	public static Collection<?> selectList(SqlSessionFactory factory, String sqlStatementId) throws Exception {
		SqlSession session = factory.openSession();
		try{
			return session.selectList(sqlStatementId);
		} finally{
			session.close();
		}
	}
	
	public static Object selectOne(SqlSessionFactory factory, String sqlStatementId,Object params) throws Exception {
		SqlSession session = factory.openSession();
		try{
			return session.selectOne(sqlStatementId,params);
		} finally{
			session.close();
		}
	}
	
	public static Object selectOne(SqlSessionFactory factory, String sqlStatementId) throws Exception {
		SqlSession session = factory.openSession();
		try{
			return session.selectOne(sqlStatementId);
		} finally{
			session.close();
		}
	}
}
