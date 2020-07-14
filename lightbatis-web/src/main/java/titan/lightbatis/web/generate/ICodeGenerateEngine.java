package titan.lightbatis.web.generate;

/**
 * 代码生成接口
 */
public interface ICodeGenerateEngine<R> {

    /**
     * 根据配置文件 生成指定的代码输出
     * @param config
     * @return
     * @throws Exception
     */
    public R generate(GenerateConfig config) throws Exception;

    /**
     * 根据配置文件 预览代码输出
     * @param config
     * @return
     * @throws Exception
     */
    public StringBuffer preview(GenerateConfig config) throws Exception;




}
