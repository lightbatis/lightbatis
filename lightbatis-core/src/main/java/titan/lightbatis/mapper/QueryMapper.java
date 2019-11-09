package titan.lightbatis.mapper;

import java.util.List;

import com.querydsl.core.types.Predicate;

import titan.lightbatis.annotations.LightQuery;


public interface QueryMapper<T>  extends ILightMarker{

	//@SelectProvider(type = QuerySelectProvider.class, method = "query")
	@LightQuery
	public List<T> query(Predicate...  predicates);
	
	
}
