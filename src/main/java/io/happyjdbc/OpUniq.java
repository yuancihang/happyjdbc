package io.happyjdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class OpUniq<T> extends OpList<T> {

	public OpUniq(String dbInstance, String rawSql, Object shareParam, Object...params) {
		super(dbInstance, rawSql, shareParam, params);
	}
	
	public OpUniq(Class<T> modelClass, String dbInstance, String rawSql, Object shareParam, Object...params) {
		super(modelClass, dbInstance, rawSql, shareParam, params);
	}
	
	@Override
	public T uniqResult(){
		if(result.isEmpty()){
			return null;
		}
		
		return result.get(0);
	}
	
	@Override
	protected void processResult(Object r) throws SQLException {
		if(r instanceof ResultSet){
			ResultSet rs = (ResultSet)r;
			if(rs.next()){
				parseResultSet(rs);
			}
		}
	}
	
}
