package com.sjzy.data.monitor.rs;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("rawtypes")
public class ListResultSetExtractor implements  ResultSetExtractor<List>{

	private List<String> fieldList;

	
	public ListResultSetExtractor() {
		
	}
	
    public ListResultSetExtractor(List<String> fieldList) {
    	this.fieldList = fieldList;
 		
	}


	/**
	 * 将查询出来的结果转成List的形式进行存储
	 *
	 * @param rs	查询出来的结果集
	 * @return		格式规范的结果集
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	@Override
	public List<Object> extractData(ResultSet rs) throws SQLException, DataAccessException {

		//存储格式规范的结果集
		List<Object> result = new ArrayList<>();

		//查询出来的结果集进行遍历
		while(rs.next()){

			//取出的字段变成list
			List<String> collectorList = fieldList.parallelStream()
					.filter(itrm->itrm != null).map(f->f.toString().trim()).collect(Collectors.toList());

			//结果存储
			for(String col:collectorList) {
				result.add(rs.getString(col));
			}
		}

		return result;

	}


}
