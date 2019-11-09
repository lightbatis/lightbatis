/**
 * 
 */
package titan.common.mybatis.web;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.commons.lang3.ClassPathUtils;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolver;

import titan.lightbatis.web.entity.OutputSetting;

/**
 * @author lifei
 *
 */
public class ClzHomeTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String clzPath = ClassPathUtils.toFullyQualifiedPath(OutputSetting.class, "OutputSetting.class");
		System.out.println(clzPath);
		try {
			File clzFile = ResourceUtils.getFile("classpath:" + clzPath);
			System.out.println(clzFile.exists() + " ==== " +  clzFile.getAbsolutePath());
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String javaPath = ClassPathUtils.toFullyQualifiedPath(OutputSetting.class, "OutputSetting.java");
		System.out.println(javaPath);
		try {
			javaPath = "file:" + javaPath;
			File javaFile = ResourceUtils.getFile("src/main/java");
			System.out.println(javaFile.getAbsolutePath());
			System.out.println(javaFile.exists());
			
			
			ResourcePatternResolver patternResolver = null;
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}

}
