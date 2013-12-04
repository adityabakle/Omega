package com.cellmania.cmreports.db.handlers;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Calendar;
import java.util.Date;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import com.cellmania.cmreports.common.ObjectConvertor;

public class DateTypeHandler implements TypeHandler<Object> {

	
	public Object getResult(ResultSet arg0, String arg1) throws SQLException {
		Calendar calendar = Calendar.getInstance();
		return arg0.getTimestamp(arg1, calendar);
	}

	
	public Object getResult(CallableStatement arg0, int arg1)
			throws SQLException {
		
		Calendar calendar = Calendar.getInstance();
		return arg0.getTimestamp(arg1, calendar);
	}

	
	public void setParameter(PreparedStatement arg0, int arg1, Object arg2,
			JdbcType arg3) throws SQLException {
		Date date = (Date) arg2;
		if (date == null) {
			arg0.setNull(arg1, Types.VARCHAR);
		} else {
			String szDate = ObjectConvertor.simpleDate(date, "dd-MMM-yyyy");
			arg0.setString(arg1, szDate);
		}
	}


	public Object getResult(ResultSet arg0, int arg1) throws SQLException {
		Calendar calendar = Calendar.getInstance();
		return arg0.getTimestamp(arg1, calendar);
	}

}
