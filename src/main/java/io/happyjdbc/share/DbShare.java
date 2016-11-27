package io.happyjdbc.share;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.ScriptException;
import javax.script.SimpleBindings;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.util.JdbcConstants;
import com.alibaba.druid.util.StringUtils;

import io.happyjdbc.util.Script;

public class DbShare {

	private static final DbShare instance = new DbShare();
	
	public static DbShare getInstance(){
		return instance;
	}
	
	private Script script = new Script();
	/** <tableName, script> */
	private Map<String, String> ruleMap = new HashMap<>();
	
	public void addRule(String tableName, String rule){
		ruleMap.put(tableName, rule);
	}
	
	public String getTargetSql(String rawSql, Object shareParam){
		String tableName = parseTableName(rawSql);
		if(StringUtils.isEmpty(tableName) || !ruleMap.containsKey(tableName)){
			return rawSql;
		}
		
		String targetTableName = null;
		SimpleBindings bindings = new SimpleBindings();
		bindings.put("shareParam", shareParam);
		try {
			targetTableName = (String)script.exec(ruleMap.get(tableName), bindings);
		} catch (ScriptException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		
		return rawSql.replaceAll(tableName, targetTableName);
	}
	
	private static final String dbType = JdbcConstants.MYSQL;
	
	private String parseTableName(String sql){
		List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);
		if(!stmtList.isEmpty()){
			MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
			stmtList.get(0).accept(visitor);
			
			return visitor.getCurrentTable();
		}
		
		return null;
	}
}
