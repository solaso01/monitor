package com.sjzy.data.monitor.rs;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("rawtypes")
public class TestExtractor  implements  ResultSetExtractor<List>{
	/**
	 * 字符串字段
	 */
	private String field;
	
	public  TestExtractor() {
		
	}

	public  TestExtractor(String field) {
		this.field = field;
	}

	 /**
     * 
     */
	@Override
	public List<Object> extractData(ResultSet rs) throws SQLException, DataAccessException {
	
		List<Object> result = new ArrayList<>();
		while(rs.next()){
			result.add(rs.getString(field));
		}
		
	return result;

	}
}
