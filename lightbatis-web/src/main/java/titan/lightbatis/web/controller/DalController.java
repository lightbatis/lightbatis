package titan.lightbatis.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import titan.lightbatis.table.ITableSchemaManager;
import titan.lightbatis.table.TableSchema;
import titan.lightbatis.web.DalConfig;
import titan.lightbatis.web.EntityMeta;
import titan.lightbatis.web.EntityRespository;
import titan.lightbatis.web.entity.*;
import titan.lightbatis.web.generate.mapper.MethodMeta;
import titan.lightbatis.web.service.GenerateService;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * 实体类自动生成工具
 */

@RestController
@RequestMapping("/dal")
@Api("数据层管理")
@Slf4j
public class DalController{

	@Autowired
	private ITableSchemaManager tableSchemaManager = null;
	@Autowired
	private GenerateService generateService;
	@Autowired
	private EntityRespository entityRepository = null;
	@Autowired
	private DalConfig dalConfig = null;
	
	@ApiOperation(value = "列出当前数据库中所有的表", response = TableSchema.class, responseContainer = "List")
	@GetMapping("tables")
	public List<TableSchema> tables() {
		List<TableSchema> tables = tableSchemaManager.listTables();
		return tables;
	}

	@ApiOperation(value = "列出当前数据库中所有的表及对应的实体类", response = TableEntitySchema.class, responseContainer = "List")
	@GetMapping("table/entities")
	public List<TableEntitySchema> tableEntities() {
		List<TableSchema> tables = tableSchemaManager.listTables();
		List<TableEntitySchema> entites = new ArrayList<TableEntitySchema>();
		for (TableSchema table : tables) {
			TableEntitySchema entitySchema = toEntitySchema(table);
			entites.add(entitySchema);
		}
		return entites;
	}

	@ApiOperation(value = "当前的默认的输出配置", response = OutputSetting.class)
	@GetMapping("generate/setting")
	public Object generateSetting() {
		OutputSetting setting = getDefaultSetting();
		return setting;
	}

	@ApiOperation(value = "列出指定表的结构", response = TableSchema.class)
	@GetMapping("table/{table}")
	public TableSchema find(@PathVariable("table") String table) {
		return tableSchemaManager.getTable(table);
	}

	@ApiOperation(value = "根据传入的类型生成相应的Java类")
	@PostMapping("generated")
	public Object generated(@RequestBody GenerateSettingVo generateSetting, @RequestParam String type) {
		OutputSetting outSetting = generateSetting.getSetting();
		TableEntitySchema tableSchema = generateSetting.getTableSchema();
		Object result = null;
		if (type.equals("entity")) {
			result = generateService.generateEntity(outSetting, tableSchema);
		} else if (type.equals("mapper")) {
			try {
				result = generateService.generateMapper(outSetting, tableSchema);
			} catch (Throwable e) {
				e.printStackTrace(System.err);
			}
		} else if (type.equals("service")) {
			try {
				generateService.generateService(outSetting, tableSchema);
			} catch (Throwable e) {
				e.printStackTrace(System.err);
			}
		}
		else if (type.equals("controller")) {

		}
		return result;
	}

	@ApiOperation(value = "批量生成 Entity, Mapper, Service")
	@PostMapping("generated/batch")
	public Object batchGenerated(@RequestBody BatchGenerateVO generateSetting) {
		log.debug(generateSetting.toString());
		OutputSetting setting = generateSetting.getSetting();
		try {
			List<TableEntitySchema> tableSchemas = generateSetting.getTableSchemas();
			generateService.genertedTables(setting, tableSchemas);
		} catch (Throwable throwable) {
			throwable.printStackTrace();
			return "fail";
		}
		return "ok";
	}

	@ApiOperation(value = "获取当前生成的实体类情况", response = EntityMeta.class, responseContainer = "list")
	@GetMapping("list/entities")
	public List<EntityMeta> listEntities() {
		return entityRepository.getTables();
	}

	@ApiOperation(value = "根据表结构获取 Mapper 类的结构信息", response = MapperMetaVO.class)
	@PostMapping("get/mapper")
	public MapperMetaVO getMapper(@RequestBody TableEntitySchema tableSchema) {
		MapperMetaVO meta = new MapperMetaVO();
		try {
			List<MethodMeta> metaList = generateService.recommandMapperMethods(tableSchema);
			//meta.setMethods(metaList);
			String script = generateService.generateScript(tableSchema, metaList);
			meta.setMapperScript(script);
			meta.setTableName(tableSchema.getTableName());
		} catch (Throwable ex) {
			ex.printStackTrace(System.err);
		}
		return meta;
	}
	
	@ApiOperation(value = "根据表结构获取 Service 类的结构信息", response = MapperMetaVO.class)
	@PostMapping("get/service")
	public MapperMetaVO getService(@RequestBody TableEntitySchema tableSchema) {
		MapperMetaVO meta = new MapperMetaVO();
		try {
			List<MethodMeta> metaList = generateService.recommandMapperMethods(tableSchema);
			//meta.setMethods(metaList);
			String script = generateService.generateService(tableSchema, metaList);
			meta.setServiceScript(script);
			meta.setTableName(tableSchema.getTableName());
		} catch (Throwable ex) {
			ex.printStackTrace(System.err);
		}
		return meta;
	}

	private TableEntitySchema toEntitySchema(TableSchema table) {
		TableEntitySchema entitySchema = new TableEntitySchema();
		BeanUtils.copyProperties(table, entitySchema);
		entitySchema.setMapperClzName(table.getEntityName() + "Mapper");
		entitySchema.setControllerClzName(table.getEntityName() + "Controller");
		entitySchema.setServiceClzName(table.getEntityName() + "Service");
		entitySchema.setEntityPackageName(dalConfig.getBasePackage() + ".model.entity");
		entitySchema.setMapperPackageName(dalConfig.getBasePackage() + ".mapper");
		entitySchema.setControllerPackageName(dalConfig.getBasePackage() + ".web");
		entitySchema.setServicePackageName(dalConfig.getBasePackage() + ".service");
		return entitySchema;
	}

	private OutputSetting getDefaultSetting() {
		OutputSetting setting = new OutputSetting();
		// 获取当前输出的文件夹。
		try {
			ApplicationHome appHome = new ApplicationHome();
			String outDir = null;
			File dir = appHome.getDir();
			if (dir == null) {
				dir = ResourceUtils.getFile("src/main/java");
				outDir = dir.getAbsolutePath();
			} else {
				outDir = dir.getAbsolutePath() + "/src/main/java";
			}
			setting.setOutDir(outDir);
			setting.setOutPackage(dalConfig.getBasePackage());
			setting.setOverwrite(true);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return setting;
	}
}
