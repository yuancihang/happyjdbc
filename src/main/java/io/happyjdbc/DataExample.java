package io.happyjdbc;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

public class DataExample {

	public static void main(String[] args) throws SQLException {
		DataAccess.getInstance().addDataSource("local", buildDataSource());
		
		sampleTransaction();
	}
	
	private static DataSource buildDataSource() throws SQLException{
		return new DataSourceBuilder()
						.setDbName("my")
						.setHost("localhost")
						.setPort(3306)
						.setUsername("root")
						.setPassword("123456")
						.build();
	}
	
	public static void sampleUniq() throws SQLException{
		int result = DataAccess.getInstance()
							.execute(new OpUniq<Integer>("local", "select c2 from t1 where c1=?", 0, 1){})
							.uniqResult();
		System.err.println(result);
		
		T1 t1 = DataAccess.getInstance()
							.execute(new OpUniq<T1>("local", "select * from t1 where c1=?", 0, 1){})
							.uniqResult();
		System.err.println(t1);
		
		Map<String, Object> map = DataAccess.getInstance()
									.execute(new OpUniq<Map<String, Object>>("local", "select * from t1 where c1=?", 0, 1){})
									.uniqResult();
		for(String name : map.keySet()){
			System.err.println(name + "=" + map.get(name));
		}
	}
	
	public static void sampleList() throws SQLException{
		List<Integer> result = DataAccess.getInstance()
				.execute(new OpList<Integer>("local", "select c2 from t1", 0){})
				.listResult();
		System.err.println(result);
		
		List<T1> t1 = DataAccess.getInstance()
				.execute(new OpList<T1>("local", "select * from t1", 0){})
				.listResult();
		System.err.println(t1);
	}
	
	public static void sampleUpdate() throws SQLException{
		int result = DataAccess.getInstance()
						.execute(new OpUpdate("local", "update t1 set c2=? where c1=?", 0, 3, 1))
						.uniqResult();
		System.err.println(result);
		
		long id = DataAccess.getInstance()
						.execute(new OpInsertReturnId("local", "insert into t1(c2,c3) value(?,?)", 0, 10, 10))
						.uniqResult();
		System.err.println(id);
	}
	
	public static void sampleTransaction() throws SQLException{
		// dbInstance必须一致
		List<OpResult<?>> resultList = DataAccess.getInstance().executeTransaction(Arrays.asList(
											new OpUpdate("local", "update t1 set c2=? where c1=?", 0, 4, 1),
											new OpUpdate("local", "update t1 set c2=? where c1=?", 0, 6, 3)
											));
		for(OpResult<?> opResult : resultList){
			System.err.println(opResult.uniqResult());
		}
	}
	
	public static class T1{
		private long c1;
		private int c2;
		private int c3;
		
		public long getC1() {
			return c1;
		}
		public void setC1(long c1) {
			this.c1 = c1;
		}
		public int getC2() {
			return c2;
		}
		public void setC2(int c2) {
			this.c2 = c2;
		}
		public int getC3() {
			return c3;
		}
		public void setC3(int c3) {
			this.c3 = c3;
		}
		
		@Override
		public String toString(){
			return c1+","+c2+","+c3;
		}
		
	}

}
