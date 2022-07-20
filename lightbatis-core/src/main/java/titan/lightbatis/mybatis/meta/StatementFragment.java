package titan.lightbatis.mybatis.meta;

import lombok.Data;
import lombok.ToString;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@ToString
public class StatementFragment {
    private Set<String> columns = new HashSet<>();
    private String table;
    private String pkColumn;
    private String statementId;
    private String sourceColumn;
    private List<IColumnMappingMeta> mappingColumns = null;
    public void addColumn(String column) {
        columns.add(column);
    }
}
