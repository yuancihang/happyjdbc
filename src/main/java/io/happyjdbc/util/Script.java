package io.happyjdbc.util;

import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * 嵌入脚本引擎
 * @author yuan<cihang.yuan@happyelements.com>
 *
 */
public class Script {
	
	private ScriptEngine scriptEngine;
	
	public Script(){
		ScriptEngineManager sem = new ScriptEngineManager();
		scriptEngine = sem.getEngineByName("js");
	}
	
	public Object exec(String script, Bindings n)throws ScriptException{
		return scriptEngine.eval(script, n);
	}
	
	/**
	 * 调用脚本中的函数, 和bindObject互斥
	 * @param functionName String
	 * @param args Object[]
	 * @return Object
	 */
	public Object invokeFunction(String functionName, Object... args)throws ScriptException, NoSuchMethodException{
		Invocable invocable = (Invocable)scriptEngine; 
		return invocable.invokeFunction(functionName, args);
	}
	
	public Object get(String key){
		return scriptEngine.get(key);
	}
	public void put(String key, Object value){
		scriptEngine.put(key, value);
	}
	
}
