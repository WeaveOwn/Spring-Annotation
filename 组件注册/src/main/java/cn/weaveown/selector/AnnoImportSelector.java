package cn.weaveown.selector;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * ImportSelector @Import注解其中之一 选择器例子
 * @author willvi
 *
 */
public class AnnoImportSelector implements ImportSelector{

	/**
	 * AnnotationMetadata获取当前类的注解信息
	 */
	public String[] selectImports(AnnotationMetadata importingClassMetadata) {
		//需要返回全类名
		String [] beans = {"cn.willvi.bean.Pig"};
		return beans;
	}

}
