package io.happyjdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OpInsertReturnId extends Op<Long> {

	private long result;
	
	public OpInsertReturnId(String dbInstance, String rawSql, Object shareParam, Object...params) {
		super(dbInstance, rawSql, shareParam, params);
	}

	@Override
	protected int getAutoGeneratedKeys(){
		return PreparedStatement.RETURN_GENERATED_KEYS;
	}
	
	@Override
	protected Object execute(PreparedStatement ps) throws SQLException {
		ps.executeUpdate();
		
		return ps.getGeneratedKeys();
	}

	@Override
	protected void processResult(Object r) throws SQLException {
		if(r instanceof ResultSet){
			ResultSet rs = (ResultSet)r;
			if(rs.next()){
				Object tmp = rs.getObject(1);
				if(tmp instanceof Number){
					result = ((Number)tmp).longValue();
				}else{
					result = (long)tmp;
				}
			}else{
				result = -1;
			}
		}
	}
	
	@Override
	public Long uniqResult(){
		return result;
	}

}
