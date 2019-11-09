package titan.lightbatis.web.generate.mapper;

public abstract class AbstractMapperMethodRecommender implements IMethodRecommender {

	public MethodType getType() { return MethodType.mapper; }
	
}
