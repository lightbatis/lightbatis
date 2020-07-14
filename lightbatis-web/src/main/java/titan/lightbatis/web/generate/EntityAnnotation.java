/**
 * 
 */
package titan.lightbatis.web.generate;

import javax.persistence.Entity;
import java.lang.annotation.Annotation;

/**
 * @author lifei
 *
 */
public class EntityAnnotation implements Entity{
	private String name = null;
	
	/**
	 * @param name
	 */
	public EntityAnnotation(String name) {
		super();
		this.name = name;
	}

	@Override
	public Class<? extends Annotation> annotationType() {
		return Entity.class;
	}

	@Override
	public String name() {
		return name;
	}

}
