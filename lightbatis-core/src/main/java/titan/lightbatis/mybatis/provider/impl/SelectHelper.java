package titan.lightbatis.mybatis.provider.impl;

import com.querydsl.core.types.Path;
import com.querydsl.sql.ColumnMetadata;
import com.querydsl.sql.RelationalPath;
import titan.lightbatis.mybatis.meta.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class SelectHelper {


    public static boolean hasProjection (Object paramemter, String mappedStatementId) {
        MapperMeta meta = MapperMetaManger.getMeta(mappedStatementId);
        Set<ParamMeta> projectionParams = meta.getProjectionParams();
        return !projectionParams.isEmpty();
    }
    public static Set<ColumnMeta> projectionColumns (Object paramemter, String mappedStatementId) {
        HashMap<String, Object> paramMap =  (HashMap<String, Object>) paramemter;
        EntityMeta entityMeta = EntityMetaManager.getEntityMeta(mappedStatementId);
        Set<ColumnMeta> columns = new HashSet<>();
        MapperMeta meta = MapperMetaManger.getMeta(mappedStatementId);
        Set<ParamMeta> projectionParams = meta.getProjectionParams();
        for (ParamMeta param: projectionParams) {
            Path[] paths = null;
            if (param.getType().isArray()) {
                String paraName = meta.getParamCount() == 1 ?"array": param.getName();
                paths = (Path<?>[]) paramMap.get(paraName);
            } else {
                paths =new Path[1];
                String name = param.getName();
                Path<?> path = (Path<?>) paramMap.get(name);
                paths[0] = path;
            }
            for (Path path: paths) {
                Path<?> parent = path.getMetadata().getParent();
                if (parent instanceof RelationalPath) {
                    RelationalPath relationalPath = (RelationalPath)parent;
                    ColumnMetadata columnMeta = relationalPath.getMetadata(path);
                    ColumnMeta col = entityMeta.findColumnByColumn(columnMeta.getName());
                    columns.add(col);
                } else {
                    ColumnMeta col = entityMeta.findColumnByProperty(path.getMetadata().getName());
                    columns.add(col);
                }
            }
        }
        return columns;
    }
}
