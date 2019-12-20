package titan.lightbatis.mybatis.meta;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.sql.ColumnMetadata;
import com.querydsl.sql.RelationalPathBase;
import org.springframework.core.type.ClassMetadata;

import java.sql.Date;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashSet;
import java.util.Set;

/**
 * 联系作者扫描以下二维码：
 * <p>
 * █████████████████████████████████████
 * █████████████████████████████████████
 * ████ ▄▄▄▄▄ █▀█ █▄▄▀▀ ▀▄█ █ ▄▄▄▄▄ ████
 * ████ █   █ █▀▀▀█ ▀▀ ████▄█ █   █ ████
 * ████ █▄▄▄█ █▀ █▀▀▄▀▀▄ ▀█ █ █▄▄▄█ ████
 * ████▄▄▄▄▄▄▄█▄▀ ▀▄█▄▀▄█ ▀ █▄▄▄▄▄▄▄████
 * ████ ▄ ▄ ▀▄  ▄▀▄▀▄ █ █▀ █ ▀ ▀▄█▄▀████
 * ████▄ ▄   ▄▄██▄█▀▄  ▄▄▀█ ▄▀  ▀█▀█████
 * ████ ▀▄▄█ ▄▄▄ ▄█▄▄▀▄▄█▀ ▀▀▀▀▀▄▄█▀████
 * █████ ▀ ▄ ▄▄█▀  ▄██ █▄▄▀  ▄ ▀▄▄▀█████
 * ████▀▄  ▄▀▄▄█▄▀▄▀█▄▀▀ ▄ ▀▀▀ ▀▄ █▀████
 * ████ ██▄▄▄▄█▀▄▀█▀█▀▄▀█ ▀▄▄█▀██▄▀█████
 * ████▄███▄█▄█▀▄ █▄▀▄▄▀▄██ ▄▄▄ ▀   ████
 * ████ ▄▄▄▄▄ █▄█▄ ▄▄  ██▄  █▄█ ▄▄▀█████
 * ████ █   █ █ ▀█▄ ▀ ▄▄▀▀█ ▄▄▄▄▀ ▀ ████
 * ████ █▄▄▄█ █ ▄▀███▀▄▄▄▄▄ █▄▀  ▄ █████
 * ████▄▄▄▄▄▄▄█▄███▄█▄▄▄▄▄██▄█▄▄▄▄██████
 * █████████████████████████████████████
 * █████████████████████████████████████
 * <p>
 * Lightbatis 基于规范约定的快速数据操作层
 *
 * @Author lifei114@126.com
 */
public class QEntity extends RelationalPathBase {
    private Set<Path<?>> paths = new HashSet<>();

    public QEntity(EntityMeta entityMeta) {
        super(entityMeta.getEntityClass(), entityMeta.getName(), entityMeta.getSchema(), entityMeta.getName());
        initColumns(entityMeta);
    }

    protected void initColumns(EntityMeta entityMeta) {
        Set<ColumnMeta> columns = entityMeta.getClassColumns();
        for (ColumnMeta column : columns) {
            addColumn(column);
        }
    }

    protected void addColumn(ColumnMeta colMeta) {
        //Class<?> type = colMeta.getJavaType();
        String typeName = colMeta.getJavaType().getSimpleName();

        switch (typeName) {
            case "Integer":
                addPathMeta(createNumber(colMeta.getProperty(), Integer.class), colMeta);
                break;
            case "Long":
                addPathMeta(createNumber(colMeta.getProperty(), Long.class), colMeta);
                break;
            case "Timestamp":
                addPathMeta(createDateTime(colMeta.getProperty(), Timestamp.class), colMeta);
                break;
            case "Date":
                addPathMeta(createDate(colMeta.getProperty(), Date.class), colMeta);
                break;
            default:
                addPathMeta(createString(colMeta.getProperty()), colMeta);
                break;
        }

    }

    @Override
    protected Path<?> add(Path path) {
        Path<?> newPath = super.add(path);
        paths.add(newPath);
        return newPath;
    }

    public NumberPath numberPath(String property) {
        for (Path<?> path : paths) {
            String name = path.getMetadata().getName();
            if (name.equals(property)) {
                return (NumberPath) path;
            }
        }
        return null;
    }

    public Path getPath(String property) {
        for (Path<?> path : paths) {
            String name = path.getMetadata().getName();
            if (name.equals(property)) {
                return path;
            }
        }
        return null;
    }

    protected void addPathMeta(Path<?> path, ColumnMeta column) {
        addMetadata(path, ColumnMetadata.named(column.getColumn()).ofType(column.getJdbcType().TYPE_CODE));
    }
}
