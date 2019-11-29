/**
 *
 * 联系作者扫描以下二维码：
 *
 █████████████████████████████████████
 █████████████████████████████████████
 ████ ▄▄▄▄▄ █▀█ █▄▄▀▀ ▀▄█ █ ▄▄▄▄▄ ████
 ████ █   █ █▀▀▀█ ▀▀ ████▄█ █   █ ████
 ████ █▄▄▄█ █▀ █▀▀▄▀▀▄ ▀█ █ █▄▄▄█ ████
 ████▄▄▄▄▄▄▄█▄▀ ▀▄█▄▀▄█ ▀ █▄▄▄▄▄▄▄████
 ████ ▄ ▄ ▀▄  ▄▀▄▀▄ █ █▀ █ ▀ ▀▄█▄▀████
 ████▄ ▄   ▄▄██▄█▀▄  ▄▄▀█ ▄▀  ▀█▀█████
 ████ ▀▄▄█ ▄▄▄ ▄█▄▄▀▄▄█▀ ▀▀▀▀▀▄▄█▀████
 █████ ▀ ▄ ▄▄█▀  ▄██ █▄▄▀  ▄ ▀▄▄▀█████
 ████▀▄  ▄▀▄▄█▄▀▄▀█▄▀▀ ▄ ▀▀▀ ▀▄ █▀████
 ████ ██▄▄▄▄█▀▄▀█▀█▀▄▀█ ▀▄▄█▀██▄▀█████
 ████▄███▄█▄█▀▄ █▄▀▄▄▀▄██ ▄▄▄ ▀   ████
 ████ ▄▄▄▄▄ █▄█▄ ▄▄  ██▄  █▄█ ▄▄▀█████
 ████ █   █ █ ▀█▄ ▀ ▄▄▀▀█ ▄▄▄▄▀ ▀ ████
 ████ █▄▄▄█ █ ▄▀███▀▄▄▄▄▄ █▄▀  ▄ █████
 ████▄▄▄▄▄▄▄█▄███▄█▄▄▄▄▄██▄█▄▄▄▄██████
 █████████████████████████████████████
 █████████████████████████████████████
 *
 * 基于 MyBatis 扩展的数据访问统一层
 */
package titan.lightbatis.generator;

import org.apache.commons.lang3.StringUtils;
import titan.lightbatis.utils.SpringContextUtil;

/**
 * @author lifei114@126.com
 *
 */
public class GeneratorHelper {

	
	public static Object generated(Object paramemter, String property, String generator, String mappedStatementId) {
		ILightbatisGenerator lightbatisGenerator = null;
		if (StringUtils.isEmpty(generator)) {
			lightbatisGenerator = new TimestampGenerator();
		} else {
			lightbatisGenerator = SpringContextUtil.getBean(generator);
		}
		Object result = lightbatisGenerator.generated(mappedStatementId, paramemter,property);
		return result;
	}

}
