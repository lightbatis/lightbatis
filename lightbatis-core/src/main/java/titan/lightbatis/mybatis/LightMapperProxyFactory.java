package titan.lightbatis.mybatis;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.binding.MapperProxy;
import org.apache.ibatis.session.SqlSession;

/**
 * Mapper 的代理类工厂
 * @author lifei
 *
 */
public class LightMapperProxyFactory<T> {

	  private final Class<T> mapperInterface;
	  private final Map<Method, LightMapperMethod> methodCache = new ConcurrentHashMap<Method, LightMapperMethod>();

	  public LightMapperProxyFactory(Class<T> mapperInterface) {
	    this.mapperInterface = mapperInterface;
	  }

	  public Class<T> getMapperInterface() {
	    return mapperInterface;
	  }

	  public Map<Method, LightMapperMethod> getMethodCache() {
	    return methodCache;
	  }

	  @SuppressWarnings("unchecked")
	  protected T newInstance(LightMapperProxy<T> mapperProxy) {
	    return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[] { mapperInterface }, mapperProxy);
	  }

	  public T newInstance(SqlSession sqlSession) {
	    final LightMapperProxy<T> mapperProxy = new LightMapperProxy<T>(sqlSession, mapperInterface, methodCache);
	    return newInstance(mapperProxy);
	  }
}
