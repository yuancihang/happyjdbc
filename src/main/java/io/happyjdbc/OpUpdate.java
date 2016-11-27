package io.happyjdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OpUpdate extends Op<Integer> {

	private int result;
	
	public OpUpdate(String dbInstance, String rawSql, Object shareParam, Object...params) {
		super(dbInstance, rawSql, shareParam, params);
	}

	@Override
	protected Object execute(PreparedStatement ps) throws SQLException {
		return ps.executeUpdate();
	}

	@Override
	protected void processResult(Object r) throws SQLException {
		if(r instanceof Integer){
			this.result = (Integer)r;
		}
	}

	public Integer uniqResult(){
		return result;
	}
}
