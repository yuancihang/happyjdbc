package io.happyjdbc.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.Map;

public class BeanUtil {

	private static final Lookup lookup = MethodHandles.lookup();
	
	public static Map<String, MethodHandle> getAllWriteMethods(Class<?> clazz){
		Map<String, MethodHandle> writeMethodMap = new HashMap<String, MethodHandle>();
		
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
			for(PropertyDescriptor pd  : beanInfo.getPropertyDescriptors()){
				Method m = pd.getWriteMethod();
				if(m != null){
					writeMethodMap.put(pd.getName(), lookup.unreflect(m));
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		
		return writeMethodMap;
	}
	
	public static <T> T fillModel(ResultSet rs, Class<T> modelClass) throws Throwable{
		ResultSetMetaData rsmd = rs.getMetaData();
		
		T model = modelClass.newInstance();
		Map<String, MethodHandle> mhCache = getAllWriteMethods(modelClass);
		for(int i=1; i<=rsmd.getColumnCount(); i++){
			Object val = rs.getObject(i);
			if(val != null){
				String columnLabel = rsmd.getColumnLabel(i);
				if(mhCache.containsKey(columnLabel)){
					// 调用set方法填充result
					mhCache.get(columnLabel).invoke(model, val);
				}
			}
		}
		
		return model;
	}
	
	/**
	 * 获取实际的泛型类型
	 * @param obj 对象
	 * @return 实际参数类型
	 */
	public static Type[] getActualTypeArguments(Object obj){
		Type type = obj.getClass().getGenericSuperclass();
		if(type instanceof ParameterizedType){
			return ((ParameterizedType)type).getActualTypeArguments();
		}
		
		return null;
	}
}
