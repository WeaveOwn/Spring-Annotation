package cn.weaveown.selector;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import cn.weaveown.bean.Person;

/**
 * 
 * ImportBeanDefinitionRegistrar接口实现
 * @author willvi
 *
 */
public class AnnoImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar{

	/**
	 * AnnotationMetadata获取当前类的注解信息
	 * BeanDefinitionRegistry 注册类 可以手工注册组件
	 */
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(Person.class);
		//参数为：bean id名 bean定义信息
		registry.registerBeanDefinition("person", rootBeanDefinition);
		
	}

}
