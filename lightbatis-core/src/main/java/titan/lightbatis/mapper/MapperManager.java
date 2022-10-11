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
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import titan.lightbatis.mybatis.meta.IColumnMappingMeta;
import titan.lightbatis.mybatis.meta.StatementFragment;
import titan.lightbatis.mybatis.scanner.FileDynamicMapperScanner;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Scope("prototype")
public class MapperManager implements InitializingBean, ApplicationContextAware {

    public static final String DefaultNamespace = "lightbatis.mapper";
    public static final String DATA_SCOPE_NAME_SPACE = DefaultNamespace + ".datascope";

    public static final String DefaultTempNamespace = DefaultNamespace + ".temp";
    protected File mapperDir = null;
    private VelocityEngine engine = null;
    private HashMap<String, List<StatementFragment>> statementMappingColumns = new HashMap<>();
//    private HashMap<String,String> datasourceMap = new HashMap<>();
//    private HashMap<String, File> datasourceDirMap = new HashMap<>();
//    private HashSet<FileDynamicMapperScanner> mapperScannerSet = new HashSet<>();
    public MapperManager() {

    }



    public String addSelectMapper(String namespace, String id, String query, List<? extends IColumnMappingMeta> mappingColumns, String datasource) throws IOException{
        HashMap<String,Object> params = new HashMap<>();
        params.put("namespace", namespace);
        params.put("id", id);
        params.put("query", query);
        String filename = namespace + "_" + id + ".xml";

        String statementId = namespace + "." + id;
        params.put("statementId",statementId);
        if (mappingColumns != null) {
            List<StatementFragment>  statementList = addColumnMapping(statementId, mappingColumns);
            statementMappingColumns.put(statementId, statementList);
            params.put("statements", statementList);
        }

        return generateMapper("titan/lightbatis/mybatis/template/DefaultMapper.xml.vm", params, filename, datasource);
    }

    public String addSQLMapper(String namespace, String id, String query, String datasource) throws IOException {
        String tmplFile = "titan/lightbatis/mybatis/template/DefaultDatascopeMapper.xml.vm";
        HashMap<String,Object> params = new HashMap<>();
        params.put("namespace", namespace);
        params.put("id", id);
        params.put("query", query);
        String filename = namespace + "_" + id + ".xml";

        String statementId = namespace + "." + id;
        params.put("statementId",statementId);
        return generateMapper(tmplFile, params, filename, datasource);
    }


    public String  addMergeSelectMapper(String id, String datasetStmtId, String datascopeStmtId, List<? extends IColumnMappingMeta> mappingColumns, String datasource) throws IOException{
        HashMap<String,Object> params = new HashMap<>();
        params.put("namespace", DefaultNamespace);
        params.put("id", id);
        String filename = DefaultNamespace + "_" + id + ".xml";

        String statementId = DefaultNamespace + "." + id;
        params.put("statementId",statementId);
        params.put("dataset_id", datasetStmtId);
        params.put("datascope_id", datascopeStmtId);
        List<StatementFragment>  statementList = addColumnMapping(statementId, mappingColumns);
        statementMappingColumns.put(statementId, statementList);
        params.put("statements", statementList);
        return generateMapper("titan/lightbatis/mybatis/template/DefaultDatasetScopeMapper.xml.vm", params, filename, datasource);
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

    private String generateMapper(String templateFile, HashMap<String, Object> params, String filename, String datasource) throws IOException {
        Template template = engine.getTemplate(templateFile, "UTF-8");
        VelocityContext context = new VelocityContext(params);
//        File mapperDir = null;
//        if (datasourceDirMap.containsKey(datasource)) {
//            mapperDir = datasourceDirMap.get(datasource);
//        }
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

    public void removeResource(String resource, boolean delete) {

//        for (FileDynamicMapperScanner scanner: mapperScannerSet) {
//            scanner.removeResource(resource);
//        }
        if (delete) {
            File file = new File (resource);
            if (file.exists()) {
                file.delete();
            }
        }
    }
    @Override
    public void afterPropertiesSet() throws Exception {
//        Assert.notNull(mapperScanner, "获取 FileDynamicMapperScanner 不能为空，用来获取 Mapper  的目录。 ");

        engine = new VelocityEngine();
        engine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        engine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        engine.init();
    }

    public void setMapperDir(File dir) {
        this.mapperDir = dir;
        if (!mapperDir.exists()){
            mapperDir.mkdirs();
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//        String[] dsNames = applicationContext.getBeanNamesForType(DataSource.class);
//        for(String dsName: dsNames) {
//            System.out.println("=================== dsName =" + dsName);
//        }
//        String[] names = applicationContext.getBeanNamesForType(FileDynamicMapperScanner.class);
//        for (String name: names) {
//           FileDynamicMapperScanner mapperScanner = applicationContext.getBean(name, FileDynamicMapperScanner.class);
//           datasourceMap.put(name, mapperScanner.getDataSourceName());
//           datasourceDirMap.put(mapperScanner.getDataSourceName(), mapperScanner.getScanDir());
//           mapperScannerSet.add(mapperScanner);
//        }
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
