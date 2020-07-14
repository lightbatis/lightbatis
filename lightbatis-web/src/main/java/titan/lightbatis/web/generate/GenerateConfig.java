package titan.lightbatis.web.generate;

import lombok.Data;
import titan.lightbatis.table.TableSchema;
import titan.lightbatis.web.entity.OutputSetting;

import java.io.Serializable;
@Data
public class GenerateConfig implements Serializable {
    /*模板名称*/
    private String templateName ="/template/EntityTemplate.ftl";
    /*freemarker生成参数*/
    private Object root;
    /*输出文件名称*/
    private String outFilePath;
    /*输出文件地址（不包括文件名） 格式：X:/xxx/xxx/*/
    private String path;
    /*是否覆盖源文件*/
    private boolean overwrite;
    
    private OutputSetting setting = null;

    private TableSchema tableSchema = null;
    
}
