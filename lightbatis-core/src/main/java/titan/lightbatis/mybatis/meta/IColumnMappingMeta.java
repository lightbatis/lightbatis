package titan.lightbatis.mybatis.meta;

import io.swagger.annotations.ApiModelProperty;

public interface IColumnMappingMeta {
    public String getColumn();

    public String getSourceColumn() ;

    public String getTargetTable();

    public String getTargetColumn();

    public String getTargetPK();
}
