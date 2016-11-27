package io.happyjdbc;

import java.sql.SQLException;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSource;

public class DataSourceBuilder {

	private String url;
	private String username;
	private String password;
	private int initPoolSize;
	private int maxPoolSize;
	private String host = "127.0.0.1";
	private int port = 3306;
	private String dbName;
	
	public DataSourceBuilder setDbName(String dbName) {
		this.dbName = dbName;
		return this;
	}
	public DataSourceBuilder setUrl(String url) {
		this.url = url;
		return this;
	}
	public DataSourceBuilder setUsername(String username) {
		this.username = username;
		return this;
	}
	public DataSourceBuilder setPassword(String password) {
		this.password = password;
		return this;
	}
	public DataSourceBuilder setInitPoolSize(int initPoolSize) {
		this.initPoolSize = initPoolSize;
		return this;
	}
	public DataSourceBuilder setMaxPoolSize(int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
		return this;
	}
	
	public DataSourceBuilder setHost(String host) {
		this.host = host;
		return this;
	}
	public DataSourceBuilder setPort(int port) {
		this.port = port;
		return this;
	}
	public DataSource build() throws SQLException{
		DruidDataSource dataSource = new DruidDataSource();
		
		if (url == null) {
			url = String.format(
					"jdbc:mysql://%s:%s/%s?useUnicode=true&characterEncoding=utf-8&autoReconnect=true&failOverReadOnly=false&useSSL=false&serverTimezone=GMT ",
					host, port, dbName);
		}
		
		dataSource.setUrl(url);
		if(username != null){
			dataSource.setUsername(username);
			dataSource.setPassword(password);
		}
		if(initPoolSize > 0){
			dataSource.setInitialSize(initPoolSize);
		}
		if(maxPoolSize > 0){
			dataSource.setMaxActive(maxPoolSize);
		}
		dataSource.setValidationQuery("select 1");
		dataSource.setTestWhileIdle(true);
		
		dataSource.init();
		
		return dataSource;
	}
	
}
