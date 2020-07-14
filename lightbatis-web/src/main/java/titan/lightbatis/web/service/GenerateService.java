package titan.lightbatis.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import titan.lightbatis.web.entity.OutputSetting;
import titan.lightbatis.web.entity.TableEntitySchema;
import titan.lightbatis.web.generate.*;
import titan.lightbatis.web.generate.mapper.MapperMethodRecommenderFactory;
import titan.lightbatis.web.generate.mapper.MethodMeta;

import java.io.File;
import java.io.StringWriter;
import java.util.List;

@Service
public class GenerateService {

	@Autowired
	private MapperMethodRecommenderFactory methodRecommenderFactory = null;
	
    public Object generateEntity(OutputSetting setting, TableEntitySchema tableSchema) {
 
        StringBuffer stringBuffer=new StringBuffer();
        try {

            TableSchemaExporter exporter = new TableSchemaExporter();
            exporter.setPackageName(tableSchema.getEntityPackageName() + ".query");
            exporter.setBeanPackageName(tableSchema.getEntityPackageName());
            exporter.setTargetFolder(new File(setting.getOutDir()));
            //BeanSerializer beanSerializer = new BeanSerializer();
            exporter.setTableNamePattern(tableSchema.getTableName());
            exporter.setValidationAnnotations(false);
            exporter.setColumnPropertyAnnotations(true);
            exporter.setSourceEncoding("UTF-8");
            exporter.setBeanSerializer(new LombokBeanSerializer(true, tableSchema.getCommon()));
            exporter.setColumnAnnotations(true);
            exporter.setSchemaToPackage(false);
            exporter.setBeanPrefix("");
            exporter.setBeanSuffix("");
            
            exporter.export(tableSchema);
        }catch (Exception e){
            e.printStackTrace();
            stringBuffer.append(e.getMessage());
        }
        return stringBuffer.toString();
    }

	public Object generateMapper(OutputSetting outSetting, TableEntitySchema tableSchema) throws Throwable{
		List<MethodMeta> methods = methodRecommenderFactory.recommandMapperMethods(tableSchema);
		MapperExporter exporter = new MapperExporter();
		exporter.setMethods(methods);
		exporter.setBeanPackageName(tableSchema.getMapperPackageName());
		exporter.setBeanClassName(tableSchema.getMapperClzName());
		exporter.setBeanSerializer(new InterfaceSerializer(false, " " + tableSchema.getCommon() + " Mapper"));
		exporter.setTargetFolder(new File(outSetting.getOutDir()));
		exporter.export(tableSchema);
		
		return null;
	}

	/**
	 * Mapper 的推荐方法
	 * @param tableSchema
	 * @return
	 */
	public List<MethodMeta> recommandMapperMethods (TableEntitySchema tableSchema) {
		List<MethodMeta> methods = methodRecommenderFactory.recommandMapperMethods(tableSchema);
		return methods;
	}
	
	public List<MethodMeta> recommandServiceMethods (TableEntitySchema tableSchema) {
		List<MethodMeta> methods = methodRecommenderFactory.recommandServiceMethods(tableSchema);
		return methods;
	}
	
	/**
	 * 生成 Mapper 脚本的方法
	 * @param tableSchema
	 * @param methods
	 * @return
	 * @throws Throwable
	 */
	public String generateScript(TableEntitySchema tableSchema, List<MethodMeta> methods) throws Throwable {
		MapperExporter exporter = new MapperExporter();
		exporter.setMethods(methods);
		exporter.setBeanPackageName(tableSchema.getMapperPackageName());
		//exporter.setBeanClassName(tableSchema.getMapperClzName());
		exporter.setBeanSerializer(new InterfaceSerializer(false, " " + tableSchema.getCommon() + " Mapper"));
		StringWriter writer = exporter.exportCode(tableSchema);
		String script = writer.toString();
		return script;
	}
	
	
	public Object generateService(OutputSetting outSetting, TableEntitySchema tableSchema) throws Throwable{
		List<MethodMeta> methods = methodRecommenderFactory.recommandMapperMethods(tableSchema);
		ServiceExporter exporter = new ServiceExporter();
		exporter.setMethods(methods);
		exporter.setBeanPackageName(tableSchema.getServicePackageName());
		exporter.setBeanClassName(tableSchema.getServiceClzName());
		exporter.setBeanSerializer(new ServiceBeanSerializer(false, " " + tableSchema.getCommon() + " Service"));
		exporter.setTargetFolder(new File(outSetting.getOutDir()));
		exporter.export(tableSchema);
		
		return null;
	}
	/**
	 * 生成服务脚本的方法
	 * @param tableSchema
	 * @param methods
	 * @return
	 * @throws Throwable
	 */
	public String generateService(TableEntitySchema tableSchema, List<MethodMeta> methods) throws Throwable  {
		ServiceExporter exporter = new ServiceExporter();
		exporter.setMethods(methods);
		exporter.setBeanPackageName(tableSchema.getServicePackageName());
		exporter.setBeanClassName(tableSchema.getServiceClzName());
		exporter.setBeanSerializer(new ServiceBeanSerializer(false, " " + tableSchema.getCommon() + " Service"));
		StringWriter writer = exporter.exportCode(tableSchema);
		String script = writer.toString();
		return script;
	}

	public void genertedTables(OutputSetting setting, List<TableEntitySchema> tableSchemas) throws Throwable {
		for (TableEntitySchema table: tableSchemas) {
			//生成实体类
			generateEntity(setting, table);
			//生成 Mapper 类
			generateMapper(setting, table);
			//生成 Service 类
			generateService(setting, table);
		}
	}
}
