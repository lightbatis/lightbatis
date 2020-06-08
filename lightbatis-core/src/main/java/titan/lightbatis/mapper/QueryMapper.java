package titan.lightbatis.mapper;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;

import java.util.List;


public interface QueryMapper<T>  extends ILightMarker{

	//@SelectProvider(type = QuerySelectProvider.class, method = "query")
	//@LightbatisQuery
	public List<T> query(Predicate...  predicates);
	
}
