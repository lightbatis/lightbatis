package titan.lightbatis.mapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import titan.lightbatis.mybatis.meta.IColumnMappingMeta;
import titan.lightbatis.mybatis.meta.StatementFragment;
import titan.lightbatis.mybatis.scanner.FileDynamicMapperScanner;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MapperManager implements InitializingBean {
    private static MapperManager manager = null;
    @Autowired
    private FileDynamicMapperScanner mapperScanner = null;
    public static final String defaultNamespace = "lightbatis.mapper";
    private File mapperDir = null;
    private VelocityEngine engine = null;
    private HashMap<String, List<StatementFragment>> statementMappingColumns = new HashMap<>();
    public static MapperManager getManager() {
        return manager;
    }
    public MapperManager() {
        MapperManager.manager = this;
    }

    public String addSelectMapper(String id, String query, List<? extends IColumnMappingMeta> mappingColumns) throws IOException {
        return this.addSelectMapper(defaultNamespace, id, query, mappingColumns);
    }

    public String addSelectMapper(String namespace, String id, String query, List<? extends IColumnMappingMeta> mappingColumns) throws IOException{
        HashMap<String,Object> params = new HashMap<>();
        params.put("namespace", namespace);
        params.put("id", id);
        params.put("query", query);
        String filename = namespace + "_" + id + ".xml";

        String statementId = namespace + "." + id;
        params.put("statementId",statementId);
        List<StatementFragment>  statementList = addColumnMapping(statementId, mappingColumns);
        statementMappingColumns.put(statementId, statementList);
        return generateMapper(params, filename, statementList);
    }

    /**
     * 获取映射关系
     * @param statementId
     * @return
     */
    public List<StatementFragment> getStatementMappingColumns(String statementId) {
        if (statementMappingColumns.containsKey(statementId)) {
            return statementMappingColumns.get(statementId);
        }
        return Collections.emptyList();
    }

    private String generateMapper(HashMap<String, Object> params, String filename, List<StatementFragment> statementList) throws IOException {
        Template template = engine.getTemplate("titan/lightbatis/mybatis/template/DefaultMapper.xml.vm", "UTF-8");
        VelocityContext context = new VelocityContext(params);
        context.put("statements", statementList);
        File file = new File(mapperDir,filename);
        FileWriter writer = new FileWriter(file);
        template.merge(context, writer);
        writer.close();
        return file.getAbsolutePath();
    }

    public List<StatementFragment>  addColumnMapping(String parentStatementId, List<? extends IColumnMappingMeta> columnList) {
        Map<MappingKey, List<IColumnMappingMeta>> map = columnList.stream().collect(
                Collectors.groupingBy(col ->
                        new MappingKey(col.getSourceColumn(), col.getTargetTable(), col.getTargetPK())));
        List<StatementFragment> statementList = map.entrySet().stream().map(entry -> {
            MappingKey mkey = entry.getKey();
            String statementId = entry.getKey().toKey();
            List<IColumnMappingMeta> cols = entry.getValue();
            StatementFragment statement = new StatementFragment();
            statement.setPkColumn(mkey.getTargetPK());
            statement.setTable(mkey.getTargetTable());
            statement.setSourceColumn(mkey.getSourceColumn());
            statement.setMappingColumns(cols);
            statement.setStatementId(parentStatementId + "_" + statementId);
            statement.addColumn(mkey.getTargetPK());
            cols.forEach( col ->{
                statement.addColumn(col.getTargetColumn());
            });
            return statement;
        }).collect(Collectors.toList());
        return statementList;
    }

    public void removeResource(String resource) {
        mapperScanner.removeResource(resource);
    }
    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(mapperScanner, "获取 FileDynamicMapperScanner 不能为空，用来获取 Mapper  的目录。 ");
        this.mapperDir = mapperScanner.getScanDir();

        engine = new VelocityEngine();
        engine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        engine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        engine.init();
    }


    @Data
    @AllArgsConstructor
    @ToString
    private class MappingKey {
        private String sourceColumn;
        private String targetTable;
        private String targetPK;

//        public MappingKey(String sourceColumn, String targetTable, String targetPK) {
//            this.sourceColumn = sourceColumn;
//            this.targetTable = targetTable;
//            this.targetPK = targetPK;
//        }

        public String toKey() {
            String key = new StringBuilder().append(sourceColumn).append("_").append(targetTable).append("_").append(targetPK).toString();
            key = DigestUtils.md5Hex(key);
            return key;
        }

    }
}
