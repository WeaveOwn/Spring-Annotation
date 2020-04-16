package cn.weaveown.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import cn.weaveown.bean.Dog;
import cn.weaveown.selector.AnnoImportBeanDefinitionRegistrar;
import cn.weaveown.selector.AnnoImportSelector;

/**
 * @Import注解的运用
 * @author willvi
 *
 */
@Configuration
//快速导入到容器内 输入的组件名称为全类名
//通过实现ImportSelector接口
//通过实现ImportBeanDefinitionRegistrar手工注册
@Import({Dog.class,AnnoImportSelector.class,AnnoImportBeanDefinitionRegistrar.class})

public class AnnoImportConfig {

	public static void main(String[] args) {
		ApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(AnnoImportConfig.class);
		String[] beanNames = annotationConfigApplicationContext.getBeanDefinitionNames();
		for (String bean : beanNames) {
			System.out.println(bean);
		}
	}
}
