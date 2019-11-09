/**
 * 
 */
package titan.lightbatis.web.generate.mapper;

import titan.lightbatis.web.entity.TableEntitySchema;

/**
 * Mapper 的推荐方法生成器
 * @author lifei
 *
 */
public interface IMethodRecommender {

	static enum MethodType {
		mapper,
		service
	}
	
	/**
	 * 验证 TableSchema 是否可以生成指定的方法
	 * @param tableSchema
	 * @return
	 */
	boolean valid(TableEntitySchema tableSchema);
	
	
	/**
	 * 获取推荐方法
	 * @param tableSchema
	 * @return
	 */
	MethodMeta recommend(TableEntitySchema tableSchema);

	MethodType getType();
}
