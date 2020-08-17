package com.sjzy.data.monitor.rs;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("rawtypes")
public class ListMapResultSetExtractor implements ResultSetExtractor<List> {

	private List<String> fieldList;
	
	public ListMapResultSetExtractor() {
		
	}

	public ListMapResultSetExtractor(List<String> fieldList) {
		this.fieldList = fieldList;
	}

	/**
	 * 将查询出来的结果转成List<map>的形式进行存储
	 *
	 * @param rs	查询出来的结果集
	 * @return		格式规范的结果集
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	@Override
	public List<Map<String,Object>> extractData(ResultSet rs) throws SQLException, DataAccessException {

		//存储格式规范的结果集
		List<Map<String,Object>> result = new ArrayList<>();

		//具体是存储数据库中的一条记录

		//查询出来的结果集进行遍历
		while(rs.next()){

			Map<String, Object> map = new HashMap<String,Object>();
			//取出的字段变成list
			List<String> collectorList = fieldList.parallelStream()
					.filter(itrm->itrm != null).map(f->f.toString().trim()).collect(Collectors.toList());

			//结果存储
			for(String col:collectorList) {
				Object ob = rs.getObject(col);
				if ( ob instanceof Timestamp){
					System.err.println();
					if ( rs.getString(col) != null ){
						map.put(col,new Timestamp(rs.getTimestamp(col).getTime()+28800000));
					}else{
						map.put(col,rs.getString(col));
					}
//					map.put(col,rs.getString(col).split(".")[0]);
				}else{
					map.put(col,rs.getObject(col));
				}
			}
			result.add(map);
		}

		return result;

	}

}
