package io.happyjdbc;

import java.util.List;

public interface OpResult<T> {

	public T uniqResult();
	public List<T> listResult();
	
}
