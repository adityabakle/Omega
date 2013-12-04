package com.cellmania.cmreports.db.handlers;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

public class BooleanTypeHandler implements TypeHandler<Object> {

	
	public Object getResult(ResultSet arg0, String arg1) throws SQLException {
		BigDecimal result = (BigDecimal) arg0.getObject(arg1);
		
		if(result==null) 
			return null;
		else if(result.longValue()==1) 
			return true;
		else
			return false;
	}

	
	public Object getResult(CallableStatement arg0, int arg1)
			throws SQLException {
		BigDecimal result = (BigDecimal) arg0.getObject(arg1);
		
		if(result==null) 
			return null;
		else if(result.longValue()==1) 
			return true;
		else
			return false;
	}

	
	public void setParameter(PreparedStatement arg0, int arg1, Object arg2,
			JdbcType arg3) throws SQLException {
		Boolean parameter = (Boolean)arg2;
		if(parameter != null && parameter == true) {
			arg0.setLong(arg1, 1);
		} else if(parameter==null){
			arg0.setNull(arg1, Types.NUMERIC);
		}
		else {
			arg0.setLong(arg1, 0);
		}
	}


	public Object getResult(ResultSet arg0, int arg1) throws SQLException {
		BigDecimal result = (BigDecimal) arg0.getObject(arg1);
		if(result==null) 
			return null;
		else return (result.longValue()==1);
	}

}
