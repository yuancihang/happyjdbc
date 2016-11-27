package io.happyjdbc;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.happyjdbc.util.BeanUtil;

public abstract class OpList<T> extends Op<T> {

	protected List<T> result = new ArrayList<>();
	protected Class<T> modelClass;
	
	@SuppressWarnings("unchecked")
	public OpList(String dbInstance, String rawSql, Object shareParam, Object...params) {
		super(dbInstance, rawSql, shareParam, params);
		
		Type[] actualTypeArguments = BeanUtil.getActualTypeArguments(this);
		if(actualTypeArguments != null){
			if(actualTypeArguments[0] instanceof ParameterizedType){
				modelClass = (Class<T>)((ParameterizedType)actualTypeArguments[0]).getRawType();
			}else{
				modelClass = (Class<T>)actualTypeArguments[0];
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void parseResultSet(ResultSet rs) throws SQLException{
		if(rs.getMetaData().getColumnCount() > 1){
			try {
				if(Map.class.isAssignableFrom(modelClass)){
					result.add((T)parseMap(rs));
				}else{
					result.add(BeanUtil.fillModel(rs, modelClass));
				}
			} catch (Throwable e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		}else{
			result.add(modelClass.cast(rs.getObject(1)));
		}
	}
	
	private Map<String, Object> parseMap(ResultSet rs) throws SQLException{
		Map<String, Object> data = new HashMap<>();
		
		ResultSetMetaData rsmd = rs.getMetaData();
		for(int i=1; i<=rsmd.getColumnCount(); i++){
			data.put(rsmd.getColumnLabel(i), rs.getObject(i));
		}
		
		return data;
	}
 
	@Override
	public List<T> listResult(){
		return result;
	}

	@Override
	protected Object execute(PreparedStatement ps) throws SQLException {
		return ps.executeQuery();
	}

	@Override
	protected void processResult(Object r) throws SQLException {
		if(r instanceof ResultSet){
			ResultSet rs = (ResultSet)r;
			while(rs.next()){
				parseResultSet(rs);
			}
		}
	}
	
}
