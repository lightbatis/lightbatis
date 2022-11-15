package titan.lightbatis.jsv.engine;

import com.eclipsesource.v8.*;
import com.eclipsesource.v8.utils.V8ObjectUtils;
import com.sun.org.apache.xpath.internal.operations.Bool;
import titan.lightbatis.jsv.transform.JavaScriptTransform;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * V8 引擎基类
 *
 */

public class V8EngineServer {
	private static V8EngineServer server = new V8EngineServer();

	private V8 v8;

	public static V8EngineServer getInstance() {
		return server;
	}

	/*
	 * 避免多次初始化
	 */
	private V8EngineServer() {
		v8 = V8.createV8Runtime();

		//v8.registerJavaMethod(v8Functions, "matrix", "_matrix", new Class[]{String[].class, Object[].class});
		MatrixFunction matrixFun = new MatrixFunction();
		v8.registerJavaMethod(matrixFun, "_matrix");

		//Console
		Console console = new Console();
		V8Object v8Console = new V8Object(v8);
		v8.add("console", v8Console);
		v8Console.registerJavaMethod(console, "log", "log", new Class[] { String.class });
		v8Console.registerJavaMethod(console, "err", "err", new Class[] { String.class });
		v8Console.release();

		//数据区间相关的函数。
		v8.registerJavaMethod(DataRangeFunctions._closeOpen,"_rangeCloseOpen");
		v8.registerJavaMethod(DataRangeFunctions._close,"_rangeClose");
		v8.registerJavaMethod(DataRangeFunctions._open,"_rangeOpen");
		v8.registerJavaMethod(DataRangeFunctions._openClose,"_rangeOpenClose");

		v8.registerJavaMethod(DataRangeFunctions._greaterThan, "_greaterThan");
		v8.registerJavaMethod(DataRangeFunctions._lessThan, "_lessThan");
		v8.registerJavaMethod(DataRangeFunctions._atLeast, "_atLeast");
		v8.registerJavaMethod(DataRangeFunctions._atMost, "_atMost");

	}

	/**
	 * 注册js函数
	 * 
	 * @param script
	 */
	public void register(String script) {
		v8.getLocker().acquire();
		v8.executeScript(script);
	}
	public Object executeScript(String script, Map<String, Object> params) {
		V8Object context = null;
		V8Array parameters = null;
		try{
			v8.getLocker().acquire();
			context = asV8Object(params);
			parameters = new V8Array(v8.getRuntime()).push(context);
			for (Map.Entry<String, Object> entry: params.entrySet()) {
				Object value = entry.getValue();
				String key = entry.getKey();
				addValueAll(v8, key, value);
			}
			Object objectResult = v8.executeScript(script);
			try{
				if (objectResult instanceof V8Object) {
					V8Object object = (V8Object) objectResult;
					if (object.isUndefined()) {
						return null;
					}
					Map<String, Object> returnMap = asMap(object);
					object.release();
					return returnMap;
				} else {
					return objectResult;
				}
			}catch (V8ResultUndefined undefined) {
				undefined.printStackTrace(System.err);
				return null;
			}

		} finally {
			// 释放句柄
			if(context != null)
				context.release();
			if (parameters != null)
				parameters.release();
			v8.getLocker().release();
		}
	}
	/**
	 * 执行 JS 脚本
	 * @param script
	 * @param functionName
	 * @param params
	 * @return
	 */
	public Object executeScript(String script, String functionName,  Map<String, Object> params) {
		V8Object context = null;
		V8Array parameters = null;
		try{
			v8.getLocker().acquire();
			context = asV8Object(params);
			parameters = new V8Array(v8.getRuntime()).push(context);
			for (Map.Entry<String, Object> entry: params.entrySet()) {
				Object value = entry.getValue();
				String key = entry.getKey();
				addValue(v8, key, value);
			}
			 v8.executeScript(script);
			try{
				Object objectResult = v8.executeJSFunction(functionName);
				if (objectResult instanceof V8Object) {
					V8Object object = (V8Object) objectResult;
					if (object.isUndefined()) {
						return null;
					}
					Map<String, Object> returnMap = asMap(object);
					object.release();
					return returnMap;
				} else {
					return objectResult;
				}
			}catch (V8ResultUndefined undefined) {
				undefined.printStackTrace(System.err);
				return null;
			}

		} finally {
			// 释放句柄
			context.release();
			parameters.release();
			v8.getLocker().release();
		}
	}

	public Object executeJSV(String script,  Map<String, Object> params) throws Exception {
		JavaScriptTransform transform = new JavaScriptTransform();

		V8Object context = null;
		V8Array parameters = null;
		try{
			String jsScript = null;
			String functionName = "main";
			jsScript = transform.transform(script, functionName);
			System.out.println("转换后的脚本 =========================== ");
			System.out.println(jsScript);

			v8.getLocker().acquire();
			context = asV8Object(params);
			parameters = new V8Array(v8.getRuntime()).push(context);
			for (Map.Entry<String, Object> entry: params.entrySet()) {
				Object value = entry.getValue();
				String key = entry.getKey();
				addValue(v8, key, value);
			}
			v8.executeScript(jsScript);

			try{
				Object objectResult = v8.executeJSFunction(functionName);
				if (objectResult instanceof V8Object) {
					V8Object object = (V8Object) objectResult;
					if (object.isUndefined()) {
						return null;
					}
					Map<String,Object> runtimeMap = asMap(v8);
					Map<String, Object> returnMap = asMap(object);
					//String[] keys = v8.getKeys();
//					System.out.println("================== key start ============ ");
//					 for (String key: keys) {
//					 	System.out.println(String.format("%s = %s", key, v8.get(key).toString()));
//					 }
//					System.out.println("================== key end ============ ");
					returnMap.putAll(runtimeMap);
					object.release();
					return returnMap;
				} else {
					return objectResult;
				}
			}catch (V8ResultUndefined undefined) {
				undefined.printStackTrace(System.err);
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace(System.err);
			throw new Exception(e);
		} finally {
			// 释放句柄
			if (context != null) {
				context.release();
			}
			if (parameters != null) {
				parameters.release();
			}
			v8.getLocker().release();
		}
	}

	/**
	 * 执行计算结果是 Boolean 类型的脚本
	 * @param script
	 * @param params
	 * @return
	 */
	public Boolean executeBoolean(String script, Map<String,Object> params) {
		V8Object context = null;
		V8Array parameters = null;
		try{
			v8.getLocker().acquire();
			context = asV8Object(params);
			parameters = new V8Array(v8.getRuntime()).push(context);
			for (Map.Entry<String, Object> entry: params.entrySet()) {
				Object value = entry.getValue();
				String key = entry.getKey();
				addValue(v8, key, value);
			}
			return v8.executeBooleanScript(script);
		} finally {
			// 释放句柄
			if (context != null)
				context.release();
			if (parameters != null)
				parameters.release();
			v8.getLocker().release();
		}
	}

	public HashMap<String,Object> executeMap(String script, Map<String,Object> params, String... keys) {
		V8Object context = null;
		V8Array parameters = null;
		HashMap<String,Object> result = new HashMap<>();
		try{
			v8.getLocker().acquire();
			context = asV8Object(params);
			parameters = new V8Array(v8.getRuntime()).push(context);
			for (Map.Entry<String, Object> entry: params.entrySet()) {
				Object value = entry.getValue();
				String key = entry.getKey();
				addValue(v8, key, value);
			}
			V8Object object = v8.executeObjectScript(script);
			for (String key:keys) {
				if (v8.contains(key)) {
					Object obj = v8.get(key);
					if (obj instanceof V8Object) {
						V8Object vObject = (V8Object)obj;
						Map<String, Object> value = asMap(vObject);
						result.put(key, value);
						vObject.release();
					}else {
						result.put(key, obj);
					}
				}
			}
			object.release();
		} finally {
			// 释放句柄
			context.release();
			parameters.release();
			v8.getLocker().release();
		}
		return result;
	}

	/**
	 * 获取v8执行对象
	 * 
	 * @return
	 */
	public V8 getRuntime() {
		return v8;
	}

	/**
	 * 执行函数
	 * 
	 * @param name
	 * @param contextData
	 * @throws RuntimeException
	 */
	public Map<String, Object> execute(String name, Map<String, Object> contextData) throws RuntimeException {
		V8Object context = asV8Object(contextData);
		V8Array parameters = new V8Array(v8.getRuntime()).push(context);
		try {
			for (Map.Entry<String, Object> entry: contextData.entrySet()) {
				Object value = entry.getValue();
				String key = entry.getKey();
				Class clz = value.getClass();
				if (clz.isAssignableFrom(Integer.class)){
					Integer v = (Integer)value;
					v8.add(key, v);
				}
				//v8.add(entry.getKey(), entry.getValue());
			}
			//System.out.println("a = " + a);
			try{

				V8Object object = v8.executeObjectFunction(name, parameters);
				Map<String, Object> returnMap = asMap(object);
				//System.out.println("a = " + a);
				object.release();
				return returnMap;
			} catch (V8ResultUndefined undefined) {
				undefined.printStackTrace(System.err);
				return null;
			}
		} finally {
			// 释放句柄
			context.release();
			parameters.release();
			v8.getLocker().release();
		}
	}

	/**
	 * 将v8对象转换成map
	 * 
	 * @param object
	 * @return
	 */
	public Map<String, Object> asMap(V8Object object) {
		if (object == null) {
			return null;
		}
		Map<String, Object> returnMap = new HashMap<>();
		for (String key : object.getKeys()) {
			Object value = object.get(key);
			// 递归调用, 可能返回的是对象的嵌套
			if (value instanceof V8Object) {
				returnMap.put(key, asMap((V8Object) value));
			} else {
				returnMap.put(key, object.get(key));
			}
		}

		return returnMap;
	}

	/**
	 * 将map对象转换为V8相关的对象
	 * 
	 * @param data
	 * @return
	 */
	private V8Object asV8Object(Map<String, Object> data) {
		V8Object context = new V8Object(v8.getRuntime());
		if (data == null || data.isEmpty()) {
			return context;
		}

		for (Map.Entry<String, Object> entry : data.entrySet()) {
			Object value = entry.getValue();
			addValueAll(context, entry.getKey(), value);
		}

		return context;
	}
	public void addValueAll(V8Object context, String key, Object value) {

		if (value instanceof Double) {
			context.add(key, (Double) value);
		} else if (value instanceof String) {
			context.add(key, (String) value);
		} else if (value instanceof Integer) {
			context.add(key, (Integer) value);
		} else if (value instanceof Boolean) {
			context.add(key, (Boolean) value);
		} else if (value instanceof List) {
			List list = (List) value;
			V8Array array = new V8Array(context.getRuntime());
			for (int i=0;i<list.size();i++) {
				Object v = list.get(i);
				if (v instanceof Map) {
					V8Object vo = asV8Object((Map<String, Object>) v);
					array.push(vo);
				}
			}
			context.add(key, array);
		}
		else {
			//throw new RuntimeException("数据类型: " + value.getClass().getName() + " 不支持");
		}

	}
	public static void addValue(V8Object context, String key, Object value) {

		if (value instanceof Double) {
			context.add(key, (Double) value);
		} else if (value instanceof String) {
			context.add(key, (String) value);
		} else if (value instanceof Integer) {
			context.add(key, (Integer) value);
		} else if (value instanceof Boolean) {
			context.add(key, (Boolean) value);
		} else if (value instanceof List) {
			List list = (List) value;
			V8Array array = new V8Array(context.getRuntime());
			for (int i=0;i<list.size();i++) {
				Object v = list.get(i);
				if (v instanceof Map) {
				}
			}
			context.add(key, array);
		}
		else {
			throw new RuntimeException("数据类型: " + value.getClass().getName() + " 不支持");
		}

	}

	class Console {
		public void log(final String message) {
			System.out.println( message);
		}
		public void err(final String message) {
			System.out.println( message);
		}
	}

}
