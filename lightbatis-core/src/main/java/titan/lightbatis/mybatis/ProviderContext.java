package titan.lightbatis.mybatis;

import java.lang.reflect.Method;

public class ProviderContext {

	  private final Class<?> mapperType;
	  private final Method mapperMethod;

	  /**
	   * Constructor.
	   *
	   * @param mapperType A mapper interface type that specified provider
	   * @param mapperMethod A mapper method that specified provider
	   */
	  ProviderContext(Class<?> mapperType, Method mapperMethod) {
	    this.mapperType = mapperType;
	    this.mapperMethod = mapperMethod;
	  }

	  /**
	   * Get a mapper interface type that specified provider.
	   *
	   * @return A mapper interface type that specified provider
	   */
	  public Class<?> getMapperType() {
	    return mapperType;
	  }

	  /**
	   * Get a mapper method that specified provider.
	   *
	   * @return A mapper method that specified provider
	   */
	  public Method getMapperMethod() {
	    return mapperMethod;
	  }

}
