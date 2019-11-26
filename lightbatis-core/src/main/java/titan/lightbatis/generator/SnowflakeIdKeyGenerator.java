/**
 * 
 */
package titan.lightbatis.generator;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import titan.lightbatis.table.ITableSchemaManager;

import java.sql.Statement;

/**
 *
 * 使用 SnowflakeId 的算法生成自动增长的值。
 * @author lifei114@126.com
 *
 */
public class SnowflakeIdKeyGenerator implements KeyGenerator {
	public static final SnowflakeIdKeyGenerator INSTANCE = new SnowflakeIdKeyGenerator();
	@Override
	public void processBefore(Executor executor, MappedStatement ms, Statement stmt, Object parameter) {
		// do nothing
		processGeneratedKeys(executor, ms, parameter);
	}

	@Override
	public void processAfter(Executor executor, MappedStatement ms, Statement stmt, Object parameter) {
	
	}

    private void processGeneratedKeys(Executor executor, MappedStatement ms, Object parameter) {
        try {
            if (parameter != null && ms.getKeyProperties() != null ) {
                String[] keyProperties = ms.getKeyProperties();// keyStatement.getKeyProperties();
                final Configuration configuration = ms.getConfiguration();
                final MetaObject metaParam = configuration.newMetaObject(parameter);
                if (keyProperties != null) {
                    if (keyProperties.length == 1) {
                    	Long nextId = ITableSchemaManager.getInstance().nextId();
                    	setValue(metaParam, keyProperties[0], nextId);
                    } else {
                    	int size = keyProperties.length;
                    	Long[] nextIds = ITableSchemaManager.getInstance().nextIds(size);
                    	for (int i=0;i<size;i++) {
                    		setValue(metaParam, keyProperties[i], nextIds[i]);
                    	}
                       // handleMultipleProperties(keyProperties, metaParam, metaResult);
                    	
                    }
                }
            }
        } catch (ExecutorException e) {
            throw e;
        } catch (Exception e) {
            throw new ExecutorException("Error selecting key or setting result to parameter object. Cause: " + e, e);
        }
    }

    private void setValue(MetaObject metaParam, String property, Object value) {
        if (metaParam.hasSetter(property)) {
            if(metaParam.hasGetter(property)){
                Object defaultValue = metaParam.getValue(property);
                if(defaultValue != null){
                    return;
                }
            }
            metaParam.setValue(property, value);
        } else {
            throw new ExecutorException("No setter found for the keyProperty '" + property + "' in " + metaParam.getOriginalObject().getClass().getName() + ".");
        }
    }
}
