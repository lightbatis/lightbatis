/**
 * 
 */
package titan.lightbatis.web.generate.mapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.mysema.codegen.model.ClassType;
import com.mysema.codegen.model.Parameter;
import com.mysema.codegen.model.SimpleType;
import com.mysema.codegen.model.Type;
import com.mysema.codegen.model.TypeCategory;
import com.mysema.codegen.model.Types;
import com.querydsl.core.util.BeanUtils;

import titan.lightbatis.result.Page;
import titan.lightbatis.result.PageList;
import titan.lightbatis.table.ColumnSchema;
import titan.lightbatis.web.entity.TableEntitySchema;
import titan.lightbatis.web.generate.mapper.IMethodRecommender.MethodType;

/**
 * @author lifei
 *
 */
@Service
public class MapperMethodRecommenderFactory {
	private Set<Class<? extends IMethodRecommender>> recommenderSet = new HashSet<>();
	/**
	 * 
	 */
	public MapperMethodRecommenderFactory() {
		recommenderSet.add(GetMethodRecommender.class);
		recommenderSet.add(ListMethodRecommender.class);
		//recommenderSet.add(UpdateByMethodRecommender.class);
		
		
	}

	public Set<Class<? extends IMethodRecommender>> getRecommenderSet() {
		return recommenderSet;
	}

	public List<MethodMeta> recommandMapperMethods(TableEntitySchema tableSchema) {
		List<MethodMeta> methods = new ArrayList<>();
		Set<Class<? extends IMethodRecommender>> set = getRecommenderSet();
		for (Class<? extends IMethodRecommender> methodClz: set ) {
			try {
				IMethodRecommender recommender = methodClz.newInstance();
				if (recommender.valid(tableSchema) && recommender.getType() == MethodType.mapper) {
					MethodMeta  methodMapper = recommender.recommend(tableSchema);
					
					methods.add(methodMapper);
				}
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return methods;
	}
	
	public List<MethodMeta> recommandServiceMethods(TableEntitySchema tableSchema) {
		List<MethodMeta> methods = new ArrayList<>();
		Set<Class<? extends IMethodRecommender>> set = getRecommenderSet();
		for (Class<? extends IMethodRecommender> methodClz: set ) {
			try {
				IMethodRecommender recommender = methodClz.newInstance();
				if (recommender.valid(tableSchema) && recommender.getType() == MethodType.service) {
					MethodMeta  methodMapper = recommender.recommend(tableSchema);
					
					methods.add(methodMapper);
				}
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return methods;
	}
	
	static Type getPropertyType(ColumnSchema columnSchema) {
		Class<?> clazz = columnSchema.getColumnClz();// configuration.getJavaType(columnType, typeName, columnSize !=
														// null ? columnSize.intValue() : 0,
		// columnDigits != null ? columnDigits.intValue() : 0, tableName, columnName);
		if (clazz == null) {
			clazz = Object.class;
		}
		TypeCategory fieldType = TypeCategory.get(clazz.getName());
		if (Number.class.isAssignableFrom(clazz)) {
			fieldType = TypeCategory.NUMERIC;
		} else if (Enum.class.isAssignableFrom(clazz)) {
			fieldType = TypeCategory.ENUM;
		}
		Type typeModel = new ClassType(fieldType, clazz);
		return typeModel;
	}
	
	static Type getEntityType (TableEntitySchema tableSchema) {
		String packageName = tableSchema.getEntityPackageName();
		String simpleName = tableSchema.getEntityName();
		Type classTypeModel = new SimpleType(TypeCategory.ENTITY, packageName + "." + simpleName, packageName,
				simpleName, false, false);
		return classTypeModel;
	}

	static class ServiceMethodProxy implements IMethodRecommender {
		private IMethodRecommender mapperMethod = null;
		
		public ServiceMethodProxy(IMethodRecommender mapperMethod) {
			this.mapperMethod= mapperMethod;
		}
		
		@Override
		public boolean valid(TableEntitySchema tableSchema) {
			return mapperMethod.valid(tableSchema);
		}

		@Override
		public MethodMeta recommend(TableEntitySchema tableSchema) {
			MethodMeta methodMeta = mapperMethod.recommend(tableSchema);
			
			return methodMeta;
		}
		public MethodType getType() { return MethodType.service; }
	}
	
 	static class GetMethodRecommender extends AbstractMapperMethodRecommender implements IMethodRecommender {
		public GetMethodRecommender() {
			
		}
		@Override
		public boolean valid(TableEntitySchema tableSchema) {
			return tableSchema.getPrimaryKeys().size() > 0;
		}

		@Override
		public MethodMeta recommend(TableEntitySchema tableSchema) {
			String methodName = "get";
			Type returnType = getEntityType(tableSchema);
			List<String> keys = tableSchema.getPrimaryKeys();
			String[] keyArray = new String[keys.size()];
			keyArray = keys.toArray(keyArray);
			String commonKey = Arrays.toString(keyArray);
			String common = String.format("根据主键 %s 获取一条记录(" + tableSchema.getCommon() + ")", commonKey);

			List<Parameter> parameters = new ArrayList<Parameter>();
			List<String> commonLines = new ArrayList<>();
			for (String key : keys) {
				ColumnSchema column = tableSchema.getColumn(key);
				Type paramType = getPropertyType(column);
				Parameter parameter = new Parameter(column.getPropertyName(), paramType);
				parameters.add(parameter);
				commonLines.add("@param " + column.getPropertyName() + " 主键 " + column.getCommon());
			}
			commonLines.add("@return 返回主键对应的对象");
			Parameter[] paraArray = parameters.toArray(new Parameter[parameters.size()]);
			MethodMeta methodMapper = new MethodMeta(common, methodName, returnType, paraArray);
			methodMapper.setParamCommons(commonLines);
			return methodMapper;
		}

	}
	
	static class UpdateByMethodRecommender extends AbstractMapperMethodRecommender implements IMethodRecommender {
		public UpdateByMethodRecommender() {
			
		}
		@Override
		public boolean valid(TableEntitySchema tableSchema) {
			return tableSchema.getPrimaryKeys().size() > 0;
		}

		@Override
		public MethodMeta recommend(TableEntitySchema tableSchema) {
			String methodName = "updateBy";
			Type entityType = getEntityType(tableSchema);
			List<String> keys = tableSchema.getPrimaryKeys();
			String[] keyArray = new String[keys.size()];
			keyArray = keys.toArray(keyArray);
			String commonKey = Arrays.toString(keyArray);
			String common = String.format("根据主键 %s 更新一条记录(" + tableSchema.getCommon() + ")", commonKey);

			List<Parameter> parameters = new ArrayList<Parameter>();
			List<String> commonLines = new ArrayList<>();
			for (String key : keys) {
				ColumnSchema column = tableSchema.getColumn(key);
				Type paramType = getPropertyType(column);
				Parameter parameter = new Parameter(column.getPropertyName(), paramType);
				parameters.add(parameter);
				commonLines.add("@param " + column.getPropertyName() + " 主键 " + column.getCommon());
			}
			parameters.add(new Parameter(BeanUtils.uncapitalize(tableSchema.getEntityName()), entityType));
			commonLines.add("@param " + tableSchema.getEntityName() + " 实体表 " + tableSchema.getTableName() + ", " + tableSchema.getCommon());
			commonLines.add("@return 本次更新影响的条数");
			Parameter[] paraArray = parameters.toArray(new Parameter[parameters.size()]);
			MethodMeta methodMapper = new MethodMeta(common, methodName, new ClassType(Long.class) , paraArray);
			methodMapper.setParamCommons(commonLines);
			return methodMapper;
		}

	}
	
	static class ListMethodRecommender extends AbstractMapperMethodRecommender implements IMethodRecommender {
			public ListMethodRecommender() {
				
			}
			@Override
			public boolean valid(TableEntitySchema tableSchema) {
				return true;
			}

			@Override
			public MethodMeta recommend(TableEntitySchema tableSchema) {
				String methodName = "list"+tableSchema.getEntityName();
				Type entityType = getEntityType(tableSchema);
				Type returnType = new ClassType(PageList.class, entityType);
				String common = String.format("分页列出所有的记录(" + tableSchema.getCommon() + ")");

				List<Parameter> parameters = new ArrayList<Parameter>();
				List<String> commonLines = new ArrayList<>();
				parameters.add(new Parameter("page",new ClassType(Page.class)));
				commonLines.add("@param page 分页条件 ");
				commonLines.add("@return 本次分页查询的结果");
				Parameter[] paraArray = parameters.toArray(new Parameter[parameters.size()]);
				MethodMeta methodMapper = new MethodMeta(common, methodName, returnType, paraArray);
				methodMapper.setParamCommons(commonLines);
				return methodMapper;
			}

		}
}
