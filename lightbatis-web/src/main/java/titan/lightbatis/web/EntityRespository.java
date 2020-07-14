/**
 * 
 */
package titan.lightbatis.web;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author lifei
 *
 */
@Repository
public class EntityRespository {
	private List<EntityMeta> tables = new ArrayList<>();
	
	public void addEntity(String table,String beanClz) {
		EntityMeta m = findEntityMeata(table);
		if (m == null) {
			m = new EntityMeta();
			tables.add(m);
		}
		m.setTable(table);
		m.setBeanClzName(beanClz);
	}

	public List<EntityMeta> getTables() {
		return tables;
	}
	
	public EntityMeta findEntityMeata(String table) {
		Optional<EntityMeta> optional = tables.stream().filter( meta -> meta.getTable().equals(table)).findAny();
		if (optional.isPresent()) {
			return optional.get();
		}
		
		return null;
	}
}
