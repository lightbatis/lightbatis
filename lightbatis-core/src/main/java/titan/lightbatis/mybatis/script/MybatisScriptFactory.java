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
package titan.lightbatis.mybatis.script;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.helper.ConditionalHelpers;
import com.github.jknack.handlebars.helper.EachHelper;
import com.github.jknack.handlebars.helper.IfHelper;
import titan.lightbatis.mybatis.meta.ColumnMeta;

import java.io.IOException;
import java.util.*;

/**
 * @author lifei114@126.com
 * SQL 的脚本编辑处理
 */
public class MybatisScriptFactory {
	private static Handlebars handlebars = new Handlebars();

	static {
		handlebars.getLoader().setPrefix("/" + MybatisScriptFactory.class.getPackage().getName().replace(".", "/"));
		handlebars.getLoader().setSuffix(".mustache");
		handlebars.registerHelpers(new MustacheHelper());
		handlebars.registerHelper(IfHelper.NAME ,IfHelper.INSTANCE);
		handlebars.registerHelper(ConditionalHelpers.eq.name(), ConditionalHelpers.eq);
		handlebars.registerHelper(ConditionalHelpers.gt.name(), ConditionalHelpers.gt);
		handlebars.registerHelper(EachHelper.NAME, EachHelper.INSTANCE);
	}
	/**
	 * 
	 */
	public MybatisScriptFactory() {

	}

	public static String buildDynamicSelectSQL(String table, String mappedStatementId) throws IOException {
		Map<String, Object> param = new HashMap<>();
		param.put("table", table);
		param.put("mappedStatementId", mappedStatementId);
		Template template = handlebars.compile("dynamicSelectSQL");
		String script = template.apply(param);
		return script;
	}

	/**
	 * 生成查询的SQL
	 * @param mappedStatementId
	 * @param table
	 * @param columns
	 * @param orderColumns
	 * @return
	 * @throws IOException
	 */
	public static String buildSelectSQL(String mappedStatementId, String table,Set<ColumnMeta> columns,Set<ColumnMeta> orderColumns) throws IOException {
		Map<String, Object> param = new HashMap<>();
		param.put("table", table);
		param.put("columns", columns);
		param.put("orderColumns", orderColumns);
		param.put("mappedStatementId", mappedStatementId);
		Template template = handlebars.compile("selectSQL");
		String script = template.apply(param);
		return script;
	}

	public static String updateByPrimaryKey(String table, Set<ColumnMeta> columns, Set<ColumnMeta> pkColumns) throws IOException {
		Map<String, Object> param = new HashMap<>();
		param.put("table", table);
		param.put("columns", columns);
		param.put("pkColumns", pkColumns);
		Template template = handlebars.compile("updateByPrimaryKey");
		String script = template.apply(param);
		return script;
	}

	/**
	 * 生成按主键删除数据的SQL
	 *
	 * @param table
	 * @param pkColumns
	 * @return
	 * @throws IOException
	 */
	public static String deleteByPrimaryKey(String table, Set<ColumnMeta> pkColumns) throws IOException {
		Map<String, Object> param = new HashMap<>();
		param.put("table", table);
		param.put("pkColumns", pkColumns);
		Template template = handlebars.compile("deleteByPrimaryKey");
		String script = template.apply(param);
		return script;
	}

	public static String buildInsert (String table, Set<ColumnMeta> columns, Set values) throws IOException {
		Map<String, Object> param = new HashMap<>();
		param.put("table", table);
		param.put("columns", columns);
		param.put("values", values);
		Template template = handlebars.compile("insertSQL");
		String script = template.apply(param);
		return script;
	}


	public static void testFunction() throws IOException {
		Template template = handlebars.compile("testFun");

		Map<String, Object> param = new HashMap<>();
		param.put("javaType",Long.class);
		List<ColumnMeta> orderBy = new ArrayList<>();
		ColumnMeta col = new ColumnMeta();
		col.setColumn("col1");
		col.setOrderBy("DESC");
		orderBy.add(col);

		col = new ColumnMeta();
		col.setColumn("col2");
		col.setOrderBy("ASC");
		orderBy.add(col);
		param.put("orderColumns", orderBy);

		String result = template.apply(param);
		System.out.println(result);

	}

}
