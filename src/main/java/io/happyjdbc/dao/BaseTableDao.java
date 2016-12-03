package io.happyjdbc.dao;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import io.happyjdbc.DataAccess;
import io.happyjdbc.OpInsertReturnId;
import io.happyjdbc.OpList;
import io.happyjdbc.OpUniq;
import io.happyjdbc.OpUpdate;
import io.happyjdbc.util.BeanUtil;

/**
 * @author <a href="mailto:cihang.yuan@happyelements.com">cihang.yuan@happyelements.com</a>
 *
 */
public abstract class BaseTableDao<T> {

	protected String dbInstance;
	protected boolean supportCustomFill;
	protected Class<T> modelClass;
	
	@SuppressWarnings("unchecked")
	public BaseTableDao(String dbInstance, boolean supportCustomFill){
		this.dbInstance = dbInstance;
		this.supportCustomFill = supportCustomFill;
		
		Type[] actualTypeArguments = BeanUtil.getActualTypeArguments(this);
		if(actualTypeArguments != null){
			if(actualTypeArguments[0] instanceof ParameterizedType){
				modelClass = (Class<T>)((ParameterizedType)actualTypeArguments[0]).getRawType();
			}else{
				modelClass = (Class<T>)actualTypeArguments[0];
			}
		}
	}
	
	protected T fillModel(ResultSet rs){
		return null;
	}
	
	public <E> E queryField(Class<E> clazz, String sql, Object shareParam, Object... params) throws SQLException {
		return DataAccess.getInstance()
				.execute(new OpUniq<E>(clazz, dbInstance, sql, shareParam, params) {})
				.uniqResult();
	}
	
	public <E> List<E> queryColumn(Class<E> clazz, String sql, Object shareParam, Object...params)throws SQLException{
		return DataAccess.getInstance()
				.execute(new OpList<E>(clazz, dbInstance, sql, shareParam, params){})
				.listResult();
	}
	
	public Map<String, Object> queryMultiField(String sql, Object shareParam, Object...params)throws SQLException{
		return DataAccess.getInstance()
				.execute(new OpUniq<Map<String, Object>>(dbInstance, sql, shareParam, params){})
				.uniqResult();
	}
	
	public List<Map<String, Object>> queryMultiColumn(String sql, Object shareParam, Object...params)throws SQLException{
		return DataAccess.getInstance()
				.execute(new OpList<Map<String, Object>>(dbInstance, sql, shareParam, params){})
				.listResult();
	}
	
	public T queryModel(String sql, Object shareParam, Object...params)throws SQLException{
		return DataAccess.getInstance()
				.execute(new OpUniq<T>(modelClass, dbInstance, sql, shareParam, params){
					@Override
					protected T parseModel(ResultSet rs) throws Throwable{
						if(supportCustomFill){
							return fillModel(rs);
						}else{
							return super.parseModel(rs);
						}
					}
				})
				.uniqResult();
	}
	
	public List<T> queryModelList(String sql, Object shareParam, Object...params)throws SQLException{
		return DataAccess.getInstance()
				.execute(new OpList<T>(modelClass, dbInstance, sql, shareParam, params){
					@Override
					protected T parseModel(ResultSet rs) throws Throwable{
						if(supportCustomFill){
							return fillModel(rs);
						}else{
							return super.parseModel(rs);
						}
					}
				})
				.listResult();
	}
	
	public int update(String sql, Object shareParam, Object...params)throws SQLException{
		return DataAccess.getInstance()
				.execute(new OpUpdate(dbInstance, sql, shareParam, params))
				.uniqResult();
	}
	
	public long insertReturnId(String sql, Object shareParam, Object...params)throws SQLException{
		return DataAccess.getInstance()
				.execute(new OpInsertReturnId(dbInstance, sql, null, params))
				.uniqResult();
	}
	
}
