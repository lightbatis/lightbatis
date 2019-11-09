package titan.common.mybatis.web;

import java.io.File;
import java.sql.SQLException;

import titan.lightbatis.web.entity.TableEntitySchema;
import titan.lightbatis.web.generate.InterfaceSerializer;
import titan.lightbatis.web.generate.MapperExporter;

public class MapperExporterMain {

	public static void main(String[] args) throws SQLException {
		MapperExporter exporter = new MapperExporter();
		InterfaceSerializer serializer = new InterfaceSerializer(false, "生成 Mapper");
		//serializer.addInterface(LightbatisMapper.class, AssignOrder.class);
		exporter.setBeanSerializer(serializer);
		TableEntitySchema tableSchema = new TableEntitySchema();
		tableSchema.setMapperClzName("AssignOrderMapper");
		tableSchema.setMapperPackageName("titan.lightbatis.web.mapper");
		exporter.setBeanPackageName(tableSchema.getMapperPackageName());
		exporter.setBeansTargetFolder(new File("src/test/java"));
		
		//exporter.export(tableSchema);
		
		
	}

}
