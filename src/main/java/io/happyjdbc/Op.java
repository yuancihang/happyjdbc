package io.happyjdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import io.happyjdbc.share.DbShare;

public abstract class Op<T> implements OpResult<T>{

	protected String dbInstance;
	protected String rawSql;
	protected Object[] params;
	protected Object shareParam;
	
	public Op(String dbInstance, String rawSql, Object shareParam, Object...params) {
		super();
		this.dbInstance = dbInstance;
		this.rawSql = rawSql;
		this.params = params;
		this.shareParam = shareParam;
	}

	protected void setPreparedStatement(PreparedStatement ps) throws SQLException{
		if((params != null) && (params.length > 0)){
			for(int i=0; i<params.length; i++){
				ps.setObject(i+1, params[i]);
			}
		}
	}
	
	protected void parseResultSet(ResultSet rs)throws SQLException{
		
	}
	
	protected abstract Object execute(PreparedStatement ps)throws SQLException;
	protected abstract void processResult(Object r)throws SQLException;
	
	protected int getAutoGeneratedKeys(){
		return 0;
	}
	
	protected String getTargetSql(){
		if(shareParam == null){
			return rawSql;
		}
		
		return DbShare.getInstance().getTargetSql(rawSql, shareParam);
	}
	
	@Override
	public T uniqResult(){
		return null;
	}
	
	@Override
	public List<T> listResult(){
		return null;
	}

	public String getDbInstance() {
		return dbInstance;
	}
	
} 
